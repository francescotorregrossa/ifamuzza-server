## Installazione per macOS



### Installazione MySQL

```
brew install mysql@5.7
brew tap homebrew/services
brew services start mysql@5.7
brew services list
brew link mysql@5.7 --force
```



### Configurazione MySQL

Configura l'utente root di MySQL con password 'root', se la vuoi cambiare non te la scordare.

```
mysqladmin -u root password 'root'
```
_inserisci la password del computer_



Accedi a MySQL da terminale.

```
mysql -u root -p
```
_inserisci la password di mysql ('root')_



Crea il database e l'utente api per accedere.

```
create database ifamuzza;
create user 'api'@'localhost' identified by 'testpass';
grant all privileges on ifamuzza.* to 'api'@'localhost';
```



### Installazione Insomnia

Per testare le chiamate al server, può tornare utile ma non è necessario.

```
brew cask install insomnia
```



### Configurazione del progetto

Apri la cartella del progetto da visual studio code, poi apri un nuovo terminale (sempre da vs code).

```
mvn install
```



Non so quante di queste siano necessarie, ma le estensioni che ho installato sono:

* di Microsoft
  * Debugger for Java
  * Java Dependency Viewer
  * Java Extension Pack
  * Java Test Runner
  * Maven for Java
  * Spring Initializer Java Support
  * Visual Studio IntelliCode
* di Pivotal
  * Spring Boot Tools
* di Red Hat
  * Language Support for Java



Per il primo avvio, fai in modo che la struttura del database sia creata correttamente. Vai su `src/main/java/.../resources/application.properties` e cambia

```
spring.jpa.hibernate.ddl-auto=none
```

con

```
spring.jpa.hibernate.ddl-auto=create
```

Dopo aver avviato il progetto una volta, rimetti `none`, o al massimo `update`.
Se hai bisogno di cancellare i dati, avvia il progetto mentre il parametro è impostato a `create-drop`.

Per far partire il progetto uso il tastino in basso a sinistra, CodeLens (Launch) e poi la prima opzione. Tra un salvataggio e l'altro il server va riavviato.



## Richieste di esempio

Possono servirti per capire la struttura del database e per inserire alcuni dati di prova. Preparale da Insomnia, lo puoi aprire da spotlight.

Crea una richiesta POST a `127.0.0.1:8080/api/customerSignup` e imposta il body come JSON.
```
{
	"email": "prova1@gmail.com",
	"password": "Maiuscola1Numero%Simbolo",
	"firstName": "Francesco",
	"lastName": "Torregrossa",
	"paymentMethod": {
		"type": "creditcard",
		"holder": "Francesco Torregrossa",
		"address": "Via Archirafi, 34, Palermo",
		"number": "4333535221610574",
		"ccv": "000",
		"expDate": "01/21"
	}
}
```

Crea una richiesta POST a `127.0.0.1:8080/api/restaurantSignup` e imposta il body come JSON.

```
{
	"email": "prova2@gmail.com",
	"password": "Maiuscola1Numero%Simbolo",
	"name": "Il Pezzo",
	"address": "Via Ernesto Basile, 120, Palermo",
	"phone": "+39 091 616 2254",
	"downPayment": "0",
	"openingTime": [
		"mon 11:30-14:30",
		"tue 11:30-14:30",
		"wed 11:30-14:30",
		"thu 11:30-14:30",
		"fri 18:30-00:30",
		"sat 18:30-01:30",
		"sun 11:30-14:30"
	],
	"receiptMethod": {
		"type": "creditcard",
		"holder": "Mario Rossi",
		"address": "Via Ernesto Basile, 120, Palermo",
		"number": "4333535221610574"
	}
}
```

Crea una richiesta GET a `127.0.0.1:8080/api/search` e imposta nel tab Query un `address` e un valore, come `Via Archirafi, 34, Palermo`.
