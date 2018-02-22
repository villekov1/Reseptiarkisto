package tikape.runko.tmp;

import java.util.ArrayList;
import java.util.List;

public class Aine_TMP {

    public Integer id;
    public String nimi;
    public final List<Annos_TMP> annokset;

    public Aine_TMP(Integer id, String nimi)  {
        this.annokset = new ArrayList<>();
        this.id=id;
        this.nimi=nimi;
    }
    
    @Override
    public String toString() {
        String str = this.nimi + "\n";
        for (Annos_TMP annos : this.annokset) {
            str += annos.toString() + "\n";
        }
        return str;
    }

}
