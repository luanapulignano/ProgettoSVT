Progetto a cura di Luana Pulignano (s314156) e Salvatore Tartaglione (s317815)

Il progetto consiste nell'analisi delle vulnerabilità di un'applicazione web (https://github.com/purshink/ReactJS-Spring-Boot-Full-Stack-App), comprensiva di fix ed exploit.
L'app è suddivisa in 4 cartelle:
- La versione originale con i bug: App_original
- La versione origiale con i fix dei bug: App_original_patched
- La versione modificata con l'aggiunta di vulnerabilità non presenti inizalmente: App_new_vulnerabilities
- La versione fixed delle vulnerabilità non presenti inizialmente: App_new_vulnerabilities_patched

Prerequisiti:
-Java v11+
-npm v10.5.0
-NodeJs v12.22.9
-JDK v11.0.11
-Docker v24.0.5
-docker-compose v1.29.2

L'applicazione è stata testata su Windows 11.

Per avviare il front-end: posizionarsi nella cartella react-frontend e digitare "npm start"
Per avviare il back-end: posizionarsi nella cartella spring-backend e digitare "docker-compose up --build"

Il front-end viene eseguito all' indirizzo http://localhost:4200
Il back-end viene eseguito all' indirizzo http://localhost:8080