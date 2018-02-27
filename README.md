# Reseptiarkisto
Reseptiarkisto - kaiken kotiruuan koti!

Tämä verkkosivu tarjoaa käyttäjälle mahdollisuuden tarkastella muiden käyttäjien lisäämiä reseptejä, joiden pohjalta luoda vaikkapa koko perheen illallinen. Käyttäjä voi myös itse lisätä omia reseptejään sivustolle muiden käyttäjien nähtäväksi. Arkiston kotisivu: http://reseptiarkisto.herokuapp.com/etusivu. 


Reseptiarkiston toiminnallisuus:

Reseptiarkistolla on neljä eri toiminnallisuutta, jotka tulevat käyttäjän näkyville etusivulla. 
1) “Reseptit” kertoo mitä reseptejä tietokantaan on tallennettu.
2) “Raaka-aineet” kertoo mitä raaka-aineita tietokantaan on tallennettu.
3) “Luo uusi resepti” antaa käyttäjälle mahdollisuuden tallentaa oman reseptinsä tietokantaan.
4) “Haku” hakee käyttäjän syöttämän tekstin perusteella annoksia ja raaka-aineita tietokannasta. 

- “Reseptit” -sivulla käyttäjä näkee listalta kaikki tietokantaan tallennetut reseptit (sisältäen reseptin nimen, tarvittavat raaka-aineet sekä valmistusohjeen). Mikäli käyttäjä havaitsee virheellisen tai aiheettoman reseptin, voi hän poistaa reseptin nimen vierestä löytyvästä “poista” -painikkeesta. Huomaa, että käytetyt raaka-aineet säilyvät edelleen nähtävillä tietokannassa.

- Tietokantaan tallennetut raaka-aineet löytyvät “Raaka-aineet” -linkistä. Raaka-aineet on listattu niin, että aineen alapuolella lukee myös annokset, joista löytyy kyseistä raaka-ainetta.   

- Oman reseptin luominen tapahtuu “Luo uusi resepti” -linkistä. Käyttäjälle aukeaa näkymä, jossa hän voi antaa reseptille nimen, tarvittavat raaka-aineet ja määrät, sekä ohjeen. Jokaisen valinnan jälkeen on tärkeää muistaa tallentaa käyttäjän antama määrite tekstikentän jälkeen löytyvästä tallennusnäppäimestä (esim. Reseptin nimen kirjoittamisen jälkeen painetaan “Anna nimi” -painiketta). Tallennuksen jälkeen käyttäjän lisäykset tulevat näkyviin sivun yläosaan. Kun kaikki valinnat ovat käyttäjän mielestä kunnossa, painaa hän “tallenna” -painiketta sivun alaosasta. "Tyhjennä" -painike tyhjentää lomakkeen sisällön. Ethän paina tätä näppäintä, jos tiedät jonkun muun työstävän uutta reseptiä!

- Hakutoiminnallisuus toimii siten, että käyttäjä syöttää tekstikenttään hakusanan ja järjestelmä etsii sen perusteella tietokannasta osumat, jotka näytetään käyttäjälle. Jos käyttäjä esimerkiksi syöttää pelkän kirjaimen “a”, hakutoiminnallisuus etsii tietokannasta kaikki a-kirjaimen omaavat reseptit sekä raaka-aineet. Tyhjä hakukenttä näyttää kaikki reseptit ja raaka-aineet.


Puutteita:

- Reseptien luontisivu ei ole jokaiselle käyttäjälle yksilöllinen, vaan sivustosta on tehty tarkoituksella kollektiivinen. Tämä mahdollistaa reseptien helpon laatimisen yhteistyönä!
- Yksittäisiä raaka-aineita ei voi poistaa, pelkästään reseptejä. Tämä on tehty sen takia, että yhden raaka-aineen poistaminen vaikuttaa koko reseptin sisältöön merkittävästi.
- Valmiita reseptejä ei voi muokata, mutta niitä voi kuitenkin poistaa. Tässä perusteluna lainataan edellä mainittua, eli yksittäisten raaka-aineiden muokkaaminen / poistaminen vaikuttaa koko reseptin rakenteeseen. 
- Tekstikenttien merkkirajoitteet. Esim. annoksen nimelle on varattu vain 300 merkkiä ja ohjeelle 1000 merkkiä. Tämä on tehty sen takia, ettei tietokanta paisuisi liian suureksi.
