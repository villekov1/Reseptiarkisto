
package tikape.runko.tmp;

public class Annos_TMP {
    public Integer id;
    public String nimi;
    
    public Annos_TMP(Integer id, String nimi) {
        this.id=id;
        this.nimi=nimi;
    }
    
    @Override
    public String toString() {
        return this.nimi;
    }
}
