Progetto a cura di Luana Pulignano (s314156) e Salvatore Tartaglione (s317815)

Il progetto consiste nell'analisi delle vulnerabilità di un'applicazione web (https://github.com/purshink/ReactJS-Spring-Boot-Full-Stack-App), comprensiva di fix ed exploit.
L'app è suddivisa in 4 cartelle:
- La versione originale con i bug: App_original
- La versione origiale con i fix dei bug: App_original_patched
- La versione modificata con l'aggiunta di vulnerabilità non presenti inizalmente: App_new_vulnerabilities
- La versione fixed delle vulnerabilità non presenti inizialmente: App_new_vulnerabilities_patched

Prerequisiti:
- Java v11+
- Npm v10.5.0
- NodeJs v12.22.9
- JDK v11.0.11
- Docker v24.0.5
- Docker-compose v1.29.2

Ova file per la VM Ubuntu con tutto già preinstallato: https://www.dropbox.com/scl/fi/t4p4bwzi0dhjxncxv9aaw/Ubuntuuu.ova?rlkey=1jfy05zgu4uhe5s97bwjl0swr&dl=0


L'applicazione è stata testata su Windows 11.

NOTA:
affinchè l' applicazione possa funzionare sul front-end in un sistema basato su Linux (testato su Ubuntu 22.04), posizionarsi nella cartella "react-frontend" e sostituire il package.json di quella cartella con quello fornito nella directory "package_da_sostituire_se_si_usa_Linux" di questa repo.

Per avviare il front-end: posizionarsi nella cartella react-frontend e digitare "npm i", accettare tutto e poi digitare "npm start"
Per avviare il back-end: posizionarsi nella cartella spring-backend compilare con maven eseguendo "mvn package" e digitare "sudo docker-compose up --build"

Utenti di default:
1) username: user  password: topsecret
2) username: business  password: topsecret

Il front-end viene eseguito all' indirizzo http://localhost:4200
Il back-end viene eseguito all' indirizzo http://localhost:8080
