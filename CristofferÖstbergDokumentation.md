# Cristoffer Östberg

- [Planering](#planering)
- [Arbetet och dess genomförande](#arbetet-och-dess-genomförande)
- [Egna reflektioner](#egna-reflektioner)


## Projekt - TODO-Applikation i MongoDB

### Beskrivning av projektet
En Todo-Applikation vilken kan hantera olika TODO:s (i textform, inmatat) mha CRUD emot MongoDB.
I programmet kan man även skapa en egen användare som kan kopplas till de olika varierande TODO:sen. 

Varje TODO och Användare är unik med ett unikt id hanterat av egna metoder i programmet.

### Vad du har gjort
I ca 1,5 vecka (emellan 2023-01-19 --> 2023-06-27) har utvecklat detta program. Mer ingående och detaljerad information kan du läsa här nedanför.



## Planering

### Lösningsförslag innan uppgiften påbörjas

#### Hur du tänker försöka lösa uppgiften.(exempelvis)
* Har laddat ned en färdig uppgiftsrepo-mall, med färdiga pom.xml-dependencies vilken vår lärare Marcus gjort och tänker arbeta på den därifrån. Div. uppdateringar som görs t-o-f kommer att ses i realtid av både ansv. lärare just nu Marcus + Lars isånafall.

Öppnat IntelliJ och created 'new project from version control' där jag kopierade in URL-adressen ifrån repot skapat på Github.  
Har den sedan i min egna IDE och tänker arbeta på den här.
Tänker försöka börja bygga testklasserna imorgon, för att bygga den korrekt, bakvägen.
Bygger sedan huvudklasserna och skapar mongoConnection och sätter upp en basMongoDBFacade.

#### Pseudokod.(exempelvis)
Använde mig mest av Miro+Github för att få ur mig min skriv-frustration och få ned idéer fysiskt utskrivna, så det mesta finns där och här i bifogade bilder som länk precis nedanför (planningRes som folder i projektet)

- [Bilder](#bilder)


## Arbetet och dess genomförande
Tydligare detaljinfo om hela projektet i byggplan går att se i Miro-bilder + Projects-bilder ovanför.


* Dag 1 - 
2023-06-19 Grundplan upprättad


* Dag 2 - 2023-06-20 Fortsätter bygga vidare på grundplan, testar implementera lite mer saker i programmet


* Dag 3 - 2023-06-21 Fortsätter med grundplan, men börjar ev. känna börjar köra fast litegrann, tycker programmet är rörigt och inte så jättelätt att förstå sig på alla gånger, börjar känna lite "sådär.." med strukturen just nu, + många test som strular/inte går igenom i testklasserna trots mock+Junit som ser tydligt och rätt ut genomfört. Kanske refaktorerar/gör om hela strukturen strax, gör om med User/VG-delen inbakad direkt så känns det rättare och riktigt och lugnare med för den delen också


* Dag 4 - 2023-06-22 Kände att projektet mest var skit just nu. Tester som bara fortsätter och fortsätter krångla, frågat chatGPT 200+ gånger känns det som, men får ingen hjälp varför just mockningen av databas-klassen strular så j*-igt. Skrotar hela programidén och påbörjar ny sent på kvällen, där både Todo-samt User- är inbakad byggd på min tidigare 'Connection' och 'KeyHandler'-klass ifrån tidigare projekt. Väldigt nöjd med de just 2 klasserna iallafall vilka är verklige enkla och överskådliga just nu.


* Dag 5 - 2023-06-23 Fortsätter bygga på dag innan upprättad ny idé, känns 950 gånger bättre (ca, ungefär då, iaf.. :) Fått grundprogram att fungera, med alla implementeringar som krävs för VG-nivå. Håller nu på att refaktorerar, samt undersöker varför 'Text' och 'Done'-value är null på User när man visar den.. nästa issue att baka sig ned i. Får nog bli en chatGPT på den också ev framåt, om jag inte kan hitta felet själv mha lite lo-fi tex.


* Dag 6 -

### Vad som varit svårt
Få till grundprogrammet vilket jag började med först, där jag bara hade Todos skapade ifrån en MongoDBFacade-klass eg, + en meny som skulle hantera allting, och testklasser. Jag hade jättesvårt att få testerna att gå igenom/fungera emot databas (vilket kanske ändå är lite förståeligt, då det är ganska nytt/helt nytt att testa emot en databas för eg. någon av oss)

### Beskriv lite olika lösningar du gjort
* Testade först att göra lösning ovan enligt Rubriken Arbetet och dess genomförande. Insåg sen+märkte efter dag 3 att fasen, det här programmet kanske inte kommer hgå ihop så bra.. + kännas rörigare och rörigare och det är så att jag knappt själv börjar först vad jag kodat (o ingen annan heller ev.. ehe..) 


* Så efter lite svordomar, frustration och div. ångest hemma (ursäkta alla grannar + Mighty-duck hemma (Gummianka vi fått av Marcus))
Skrotade den och började om med ett mycket cleanare, finare program vilken centrerar kring en todapplikation, med en getter+setter class för både Todo:s och User:s (Jag ville nog ha med både Todo+User för att vilja utmana mig själv litegrann) och det gick, mycket lättare.

### Beskriv något som var besvärligt att få till
Kan bara referera till rubrik Vad som varit svårt ovanför. Testa emot databas, testa hela programmet tog också lite tid/extra planering och annat att tänka på. Github CI var väl också lite struligt när man väl inte visste vad det var´hur det skulle implementas, men när man väl vet och visste det är det ju ganska logiskt och fungerar lätt och bra.

### Beskriv om du fått byta lösning och varför i sådana fall
Refererar igen eg. bara till rubriken lite olika du gjort ovanför, och fick byta lösning för tyckte programmet började bli rörigt+svårt att förstå sig på. Både jag+någon annan ska förstå sig på vad detta program var byggt på ca 2 månader framåt.



## Egna reflektioner

### Vad gick bra
Göra om programmet till den nya lösningen med Getters+Setters, refaktorera koden, förminska den i fler klasser och göra den tydligare och enklare att läsa. Gör den mer läsbar så man fattar vad tusan testerna skall testa och inte etc med, så klasserna blir mindre/kan man både leta+finna fel mycket enklare också. Känns svinbra.

### Vad gick dåligt
Den första lösningen, som beskrivet ganska omfattande här ovan. Den blev bara rörig, och kände blev mer och mer kaotisk till slut och kände mig själv gå ned i kaotiskt mående dessutom på snudd, så bästa där var egentligen bara skit i det mesta (OBS: ENBART - program då alltså, inga andra hemska personliga destruktiva tankar, de existerar inte :)  
och börja om med det och skriv det bra mycket enklare och finare. Känns jättebra.

### Vad har du lärt dig
Implementera getters+setters mer, göra Connection-klassen + KeyHandler-klasserna 'ren' för framtida bruk mer (kommer jag och skall använda i framtida MongoDB-projekt då jag hittils nu fastnat jättemycket i just MongoDB - tycker det är en svinkul, och bra och rolig DB-lösning med.  

Jag har tänkt implementera den i ett eget 2D-spel jag håller på och bygger på fritiden :) Jag är ganska kreativ sedan barnsben (Musik+Instrument sedan 6 års ålder, Bygga/snickra det mesta, laga bilar, 3D-skriva, måla etc) som person så gillar att både baka in + slå ihop flera olika div. program och saker. Tycker det är superkul.

### Vad hade du gjort annorlunda om ni gjort om projektet
Troligtvis läst på mer om enklare struktur i program, googlat lite hur man enklast, möjligast kan lägga upp det enklaste 'struktur'-tänket i det hela, med bara ett visuellt diagram framför en. Hade nog hjälpt mycket. Jag tyckte jag själv hade en ganska bra grundplan i detta hela, med både väl förberedd innan och tänkte att detta skall jag bara ta det lugnt och bra, och ta det i rätt ände och bara börja med G-delen först, och kände så mycket pepp+inspiration för detta, men visade sig sen då att programmet mer blev rörigare och rörigare. Så jag hade nog behövt en tydligare struktur ifrån början, med både User+Todo med, då jag nog ändå ville+ville med det i projektet. Jag vill utmana mig litegrann.

### Vilka möjligheter ser du med de kunskaper du fått under kursen.
Bygga mer tydligare och enklare program, mer överskådliga och tusan chansen kanske finns nu, dessutom att det kanske är så att jag kommer ihåg ca 1 år framåt vad tusan det är jag har skrivit just här idag, och kommer ihåg + kan förfina det dessutom.

### Varför jag valde just denna specifika lösning
Som jag har förklarat lite ytterligare här ovanför i koden, så tyckte ja att just denna lösning med getters+setters för både User och Todo, och sedan en 'Manager' för var och en där CRUD-operationerna sköttes verkade ganska logiskt och lättläst. Både i programform samt i diagramform. Man förstår eg. vart alla pilar etc tar vägen och hur de går fram-och-tillbaka, och är ganska lätta att förstå sig på, iaf, tycker jag. Sedan en central TodoApplication vilken sköter texten och menyval och hämtar metoderna ifrån de 2 olika klasserna.

### Förslag på förbättringar av koden
Implementera ett Interface, göra både USerManager och TodoManager ännu mer lättläst och abstrakt. Tyckte det blev lite grötigt ändå (trots att jag refaktorerat nu, och fått det att se ännu bättre ut än tidigare dock), och funderade på ev. lösningar, men man får ta lite vad tid man har också. Man kan inte alltid ändra världen och rädda den helt och bygga ett nytt Rom på en och samma gång etc. På knapp 1,5 vecka utföra detta program med fulla tester på hela programmet + dokumentation, testning, samtestning av klasskamrat på egen hand totalt, dessutom, får man ändå väga in lite vad man själv kan samt skulle hunnit med.

### Exempel på lösningar jag valde att inte implementera
Interface, mer abstrakta lösningar, hade jag gärna velat haft med, men som ovan beskrivet så på grund av tidsbrist (midsommar med, faktiskt) så får man ibland väga tiden. Programmet kan säkerligen förbättras mer i framtiden, bara man lägger tid+tid på det.

### Förslag på förbättringar av UI/UX för design + reflektion av den
Har gjort en ganska tydlig/fin förbättring av utprintade menyvalen här i terminal iaf (så gott det går, tycker jag) men göra ett FX/GUI här i IntelliJ med, fram till presentation+Inlämning hade väl varit kul. Får se om det hinns med nu i dagarna, ev, tar det sist. Just nu är prio på att få alla testerna klara och fungerande, samt programmet med. Dagen är nu 2023-06-23, Midsommarafton.


## Bilder

#### Miro-Plan med inkluderade diagram
Påbörjar lite skisser idag - Miro 2023-06-19
![MiroPlan 2023-06-19.png](planningRes%2FMiroPlan%202023-06-19.png)

Dag 2 Miro - 2023-06-20
![MiroPlan 2023-06-20.png](planningRes%2FMiroPlan%202023-06-20.png)

Dag 3 Miro - 2023-06-21
![MiroPlan 2023-06-21.png](planningRes%2FMiroPlan%202023-06-21.png)

Dag 4 Miro - 2023-06-22
(F*- it, blev lite sur idag så skrotade hela grundprogrammet+iden)

började om med Getters+Setters då jag ändå ville testa+lära mig mer om det och använda det, + implementera lite snyggare kod (Interfaces kanske kommer senare, med...? Då jag gärna vill ha med det+lära mig mer.
Jag vill konstant lära mig bli bättre o hur man skriver riktigt snygg kod.
![MiroPlan 2023-06-22.png](planningRes%2FMiroPlan%202023-06-22.png)

Dag 5 Miro - 2023-06-23 - Nya programidén + test-environmenten
![MiroPlan 2023-06-23.png](planningRes%2FMiroPlan%202023-06-23.png)

### Github Project
Github Projects - 2023-06-21
![Github Projects 2023-06-21.png](planningRes%2FGithub%20Projects%202023-06-21.png)

Github Projects - 2023-06-22 (New idea implemented+tested = Got f*-ing angry at the last one, scrapped it and tried a new more harmonical one)
![Github Projects 2023-06-22.png](planningRes%2FGithub%20Projects%202023-06-22.png)

Github Projects - 2023-06-23 (1)
![Github Projects 2023-06-23.png](planningRes%2FGithub%20Projects%202023-06-23.png)

Github Projects - 2023-06-23 (2)
![Github Projects 2023-06-23 (2).png](planningRes%2FGithub%20Projects%202023-06-23%20%282%29.png)

Github Projects - 2023-06-24 (The TodoManagerTest-class is now complete as well.. Phew.. Aah.. finally.. Feels so f... great.)
![Github Projects 2023-06-24.png](planningRes%2FGithub%20Projects%202023-06-24.png)