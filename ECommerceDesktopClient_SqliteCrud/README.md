# E-Commerce Desktop Client (SQLite CRUD)

## Prerequisites
- Java JDK 21+
- Maven 3.9+

## Run
```bash
cd ECommerceDesktopClient_SqliteCrud
mvn clean compile
mvn javafx:run
```

## Project Structure
```
ECommerceDesktopClient_SqliteCrud/
├── pom.xml
└── src/main/
    ├── java/com/example/ecommerce/
    │   ├── MainApp.java
    │   ├── controllers/
    │   ├── models/
    │   ├── services/
    │   └── util/
    └── resources/
        ├── fxml/
        ├── css/
        └── images/
```

## Database
- Uses SQLite database file `ecommerce.db` created automatically in the project root on first run.
- To reset: close the app, delete `ecommerce.db`, run again.

## Features
- Products: SQLite-backed CRUD + search + export to CSV
- Customers: SQLite-backed CRUD + search + export to CSV
- Smooth UI transitions + small hover/click animations

