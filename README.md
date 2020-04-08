# Kníhkupectvo
Systém pre kníhkupectvo. Do tohto systému sa vedia dostať zákazníci aj administrátor.
Zákazníci si vedia pozrieť dostupné knihy a informácie o nich, pričom si dokážu vyhľadať a triediť podľa požadovaných atribútov.
Po vybraní kníh si zákazníci vedia vytvoriť objednávku a tú si následne pozrieť v histórii.
Admin si vie takisto prezerať dostupné knihy, no vie ich aj pridávať a editovať.
Administrátor má na starosti aj správu objednávok, a teda vybavovanie a zamietanie.

### Obsah
**[Technická realizácia](#technicka-realizacia)**<br>
**[Dátový model](#datovy-model)**<br>
**[Scenáre](#scenare)** <br>


## Technická realizácia <a name="technicka-realizacia"></a>
### Frontend
Frontend predstavuje [JavaFX](https://openjfx.io/) aplikácia.
Aplikácia komunikuje s *backend-om* pomocou vlastnoručne písaných *controllerov*.
Pri vytváraní scén bol použitý [Scene Builder](https://gluonhq.com/products/scene-builder/), 
ktorý generuje [FXML](https://en.wikipedia.org/wiki/FXML) súbor.
Ten je napojený na *controller* danej scény. 
Pre štýl aplikácie bol použitý [CSS](https://en.wikipedia.org/wiki/Cascading_Style_Sheets).
### Backend
Backend je naprogramovaný v [Jave](https://www.java.com/en/).
Využívame [PostgreSQL](https://www.postgresql.org/) databázu.
Na migrácie využívame [Flyway](https://flywaydb.org/).
Na generovanie databázy využívame vlastnoručne písané seedery a [Java Faker](https://github.com/DiUS/java-faker).

## Dátový model <a name="datovy-model"></a>
### Logický model
![Logicky model](images/logicky_model.png)
### Fyzický model
![Fyzicky model](images/fyzicky_model.png)

## Scenáre <a name="scenare"></a>
1. **Výber kníh**(implementované v 1. etape)<br>
Tento scenár zahŕňa prezeranie si dostupných kníh zákazníkom aj administrátorom.<br>
Knihy si môžu zoradiť podľa názvu, vydavateľa, dátumu vydania, ceny ale vedia si knihu aj vyhľadať podľa názvu.
Po vybraní knihy sa im ukáže jej popis, autori a žáner.<br>
Výber kníh<br>
(a) **Zákaznikom** - 
zobrazí sa mu jej popis a následne bude vedieť prejsť na jej objednanie ([Scenár 3](3.)). <br>
(b) **Adminstrátorom** - 
bude si ju vedieť takisto pozrieť ale ju aj spravovať ([Scenár 2](2.))
2. **Správa kníh** <br> <a name="2."></a>
K tomuto vie pristúpiť iba administrátor. <br>
Knihu vie: <br>
(a) **Pridať** -
pre pridanie bude musieť vyplniť potrebné informácie – názov, cenu, počet kusov, rok vydania, vydavateľa a popis knihy.
Vydavateľa aj autora si bude vedieť vybrať z dostupných alebo pridať nového.<br>
(b) **Odobrať** - odobratie knihy ju vymaže z databázy. <br>
(c) **Upraviť** - pri úprave bude môcť zmeniť jej množstvo alebo cenu.
3. **Objednanie knihy**<br> <a name="3."></a>
Objednať knihu si vie zákazník. Ku objednávke pristúpi po vybraní knihy zo zoznamu.
Vyplní jej množstvo a potvrdí objednanie.
Následne sa objednávka zaradí do histórie jeho objednávok a bude si vedieť pozrieť jej informácie – číslo, dátum, cenu, stav.
Taktiež v prípade že objednávka ešte nie je vybavená, bude ju vedieť zákazník zrušiť.
4. **Správa objednávok** <br>
Pristupuje k tomu administrátor. <br> 
Správa objednávok zahŕňa menenie jej stavu:<br>
(a) **Vybavená** - k vybaveniu dôjde v prípade, že nedošlo k žiadnemu problému na strane kníhkupectva. 
Závisí to od administrátora a vypĺňa sa to manuálne. <br>
(b) **Nevybavená** - začiatočný stav objednávky. <br>
(c) **Zamietnutá** - zamietnutá môže byť objednávka v prípade, že nastala situácia ktorá spôsobila vyčerpanie zásob danej knihy. 
Tento stav vyhodnocuje administrátor.
5. **Prihlásenie** <br>
Prihlasovania sú dve. Jedno pre administrátora a jedno pre zákazníka.
Na základe prihlasovacích údajov sa určia práva, ktoré budú používatelia v systéme mať. Tie sú popísané vo zvyšných scenároch.
