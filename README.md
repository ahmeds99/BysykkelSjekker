# BysykkelSjekker
App laget i Android Studio som benytter data fra Oslo Bysykkel sitt API og viser frem tilgjengelig sykler og parkeringer basert på brukerens valg av sykkelstasjon. 
Appen er langt fra ferdig, og er opprinnelig ment som et sideprosjekt som skal gi trening i apputvikling, i tillegg til å hente ut data fra et API. 

Motivasjonen for akkurat Oslo Bysykkel er at det er et kult konsept som ønsker å få flere til å sykle, gjennom tilgjengelighet og brukervennlighet. 

# Database
Applikasjonen benytter et relativt simpelt databaseskjema, der eneste entiteten er stasjonsobjektene. Her er id-en til stasjonene primærnøkkel. Databasen som benyttes er Room, som tilbys av Android Studio. En del knoting her og der for å sette opp databasen, men DAO-interfacet gir muligheter til CRUD-operasjoner på en fin og brukervennlig måte. 

Under følger et diagram laget i Dia som viser hvordan jeg tenker å bygge opp Stasjons-entiteten og selve databasen.

![Databasen](https://github.com/ahmeds99/BysykkelSjekker/blob/master/app/db_documentation/database_img.PNG)

Mulige utvidelser: User-entitet som lar brukere logge inn og lagre sine favorittstasjoner.

# User Stories
"Som bruker ønsker jeg å kunne filtrere stasjoner basert på input"

"Som bruker ønsker jeg å se tydelig dersom en stasjon har ledige sykler"

# Pågående arbeid
Implementere ferdig RecyclerView, og lage cards som både ser bra ut og viser informasjonen på en gitt stasjon. I tillegg må det legges til et søkefelt som filtrer ut stasjoner basert på input. Database-spørringen for dette er klar (findByName() i StationDao), men må koordineres med layouten og XML-filen. 

# Fremtidige utvidelser
Opprette egen bruker til applikasjonen, slik at en bruker kan klikke inn på de ulike stasjonene, og legge til sine favorittstasjoner, som vises på "Min side". Databasen må nok dermed oppdaters, og legge til en User-entitet som har en relasjon til Station-entiteten. 
En ytterligere utvidelse av dette kan være å la brukeren gi ulike "tags" til stasjonene, eksempelvis "trening", "hjem", eller "studier", og sortere stasjonsvisningene basert på dette. Vi får se!
