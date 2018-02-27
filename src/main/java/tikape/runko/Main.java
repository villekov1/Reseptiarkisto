package tikape.runko;

import tikape.runko.tmp.*;
import tikape.runko.database.*;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import spark.ModelAndView;
import spark.Spark;
import spark.template.thymeleaf.ThymeleafTemplateEngine;

public class Main {
    
    
    // reseptin luonnin väliaikais tallennus

    static final Resepti_TMP luontilomake = new Resepti_TMP();
    static List<Annos> haetutAnnokset = new ArrayList<>();
    static List<RaakaAine> haetutRaakaAineet = new ArrayList<>();
    
    public static void main(String[] args) throws Exception {
        // asetetaan portti jos heroku antaa PORT-ympäristömuuttujan
        if (System.getenv("PORT") != null) {
            Spark.port(Integer.valueOf(System.getenv("PORT")));
        }
        
        Database database = new Database();
        
        RaakaAineDao aineDao = new RaakaAineDao(database);
        AnnosDao annosDao = new AnnosDao(database);
        AnnosRaakaAineDao annosRaakaAineDao = new AnnosRaakaAineDao(database);
        
        
        //-------------------Raaka-aine lista alkaa----------------------------
        Spark.get("/raakaAineet", (req, res) -> {
            List<RaakaAine> aineet = new ArrayList<>();
            aineet = aineDao.findAll();
            
            
            List<Aine_TMP> annokset = annosRaakaAineDao.etsiAnnokset();
            
            HashMap map = new HashMap<>();
            map.put("aineet", aineet);
            map.put("annokset", annokset);
            
            return new ModelAndView(map, "raakaAineet");
        }, new ThymeleafTemplateEngine());
        
        //--------------------Raaka-aine lista päättyy-------------------------
        
        Spark.get("/reseptit", (req, res) -> {

            List<Annos> annokset = new ArrayList<>();
            List<AnnosRaakaAine> annosRaakaAineet = new ArrayList<>();
            
            annokset = annosDao.findAll();

            //Haetaan reseptien raaka-aineet Resepti_TMP-listana
            List<Resepti_TMP> reseptit = annosRaakaAineDao.etsiRaakaAineet();
            
            int koko = reseptit.size();
            HashMap map = new HashMap<>(); 
            map.put("annokset", annokset);
            map.put("reseptit", reseptit);
            map.put("koko", koko);
            
            return new ModelAndView(map, "reseptit");
        }, new ThymeleafTemplateEngine());
        
        Spark.post("etusivu/haku", (req, res) -> {
            
            String haettava = req.queryParams("haettava");
            haetutAnnokset = annosDao.findNameLike(haettava);
            haetutRaakaAineet = aineDao.findNameLike(haettava);
            
            res.redirect("/haku");
            return "";
        });
        
        Spark.get("/haku", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("haetutAnnokset", haetutAnnokset);
            map.put("haetutRaakaAIneet", haetutRaakaAineet);
            
            return new ModelAndView(map, "etusivu");
        }, new ThymeleafTemplateEngine());
        
        Spark.post("reseptit/delete/:id", (req, res) -> {
            //Poistetaan id:tä vastaava annos
            int id = Integer.parseInt(req.params(":id"));
            
            annosRaakaAineDao.deleteAnnoksenPerusteella(id);
            annosDao.delete(id);
            
            res.redirect("/reseptit");
            return "";
        });
        
        Spark.get("/etusivu", (req, res) -> {
            HashMap map = new HashMap<>();
            
            return new ModelAndView(map, "etusivu");
        }, new ThymeleafTemplateEngine());

        // ---------- Alla reseptin luontiin liittyvät jutut -----------------
        Spark.post("/luo_resepti/lisaa_nimi", (req, res) -> {
            luontilomake.setNimi(req.queryParams("reseptinNimi"));
            res.redirect("/luo_resepti");
            return "";
        });
        
        Spark.post("/luo_resepti/lisaa_raakaAine", (req, res) -> {
            luontilomake.lisaaRaakaAine(new RaakaAine_TMP(req.queryParams("rkaine"),
                    req.queryParams("maara")));
            res.redirect("/luo_resepti");
            return "";
        });
        
        Spark.post("luo_resepti/lisaa_ohje", (req, res) -> {
            luontilomake.setOhje(req.queryParams("ohjeTeksti"));
            res.redirect("/luo_resepti");
            return "";
        });
        
        Spark.post("luo_resepti/tallenna_ja_poistu", (req, res) -> {
            Annos annos = annosDao.saveOrUpdate(new Annos(luontilomake.getNimi(),
                    luontilomake.getOhje()));
            
            int jarjestysNro = 1;
            
            for (RaakaAine_TMP lomakkeenRkaine : luontilomake.getRaakaAineet()) {
                RaakaAine raakaAine = aineDao.saveOrUpdate(new RaakaAine(lomakkeenRkaine.getNimi()));
                
                annosRaakaAineDao.saveOrUpdate(new AnnosRaakaAine(raakaAine.id,
                        annos.getId(), jarjestysNro, lomakkeenRkaine.getMaara()));
                
                jarjestysNro++;                
            }
            
            luontilomake.tyhjenna();            
            res.redirect("/etusivu");
            return "";
        });
        
        Spark.post("luo_resepti/tyhjenna", (req, res) -> {
            luontilomake.tyhjenna();            
            res.redirect("/luo_resepti");            
            return "";
        });
        
        Spark.get("luo_resepti/poistu_tallentamatta", (req,res) -> {
            luontilomake.tyhjenna();
            res.redirect("/etusivu");
            return "";
        });
        
        Spark.get("/luo_resepti", (req, res) -> {
            HashMap map = new HashMap<>();

            // debuggausta varten
            System.out.println("\nReseptiolion tila tällä hetkellä: \n" + luontilomake + "\n");
            
            map.put("reseptinNimi", luontilomake.getNimi());
            map.put("raakaAineet", luontilomake.getRaakaAineet());
            map.put("ohje", luontilomake.getOhje());
            
            return new ModelAndView(map, "luo_resepti");
        }, new ThymeleafTemplateEngine());

        // --------- yllä reseptin luontiin liittyvät jutut ------------------
    }
}
