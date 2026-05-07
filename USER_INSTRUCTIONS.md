# E-Commerce Desktop Client

## Description
A desktop storefront application for browsing products, managing a shopping cart, placing orders, processing payments, and tracking delivery status.

## Prerequisites
Before running this project, ensure you have the following installed:

- **Java JDK 21** or later — [Download](https://adoptium.net/)
- **Apache Maven 3.9+** — [Download](https://maven.apache.org/download.cgi)
- **JavaFX 21** (handled automatically by Maven dependencies)

### Verify Installation
```bash
java -version    # Should show 21 or later
mvn -version     # Should show 3.9+
```

## How to Build & Run

### 1. Navigate to the project directory
```bash
cd ECommerceDesktopClient
```

### 2. Build the project
```bash
mvn clean compile
```

### 3. Run the application
```bash
mvn javafx:run
```

### 4. Package as JAR (optional)
```bash
mvn clean package
```

## Project Structure
```
ECommerceDesktopClient/
├── pom.xml                          # Maven configuration & dependencies
└── src/main/
    ├── java/com/example/ecommerce/
    │   ├── MainApp.java             # Application entry point
    │   ├── controllers/             # FXML controllers (UI logic)
    │   ├── models/                  # Data model classes (JavaFX properties)
    │   └── services/                # Business logic & database operations
    └── resources/
        ├── fxml/                    # FXML view files
        ├── css/                     # Stylesheets
        └── images/                  # Icons and images
```

## Features
- Product catalog with search and category filters
- Product ratings and details view
- Shopping cart with quantity management
- Order placement and checkout
- Payment simulation (Cash, Card, etc.)
- Order tracking with status updates
- User profile management

## Technologies Used
- JavaFX 21
- SQLite (local cache)
- Gson (JSON parsing)
- HttpClient (REST API ready)

## Application Modules
- **Products** — Browse and search the product catalog
- **Cart** — View and manage shopping cart items
- **Orders** — Track order status and history

## Database
This application uses **SQLite** for data storage. The database file `ecommerce.db`
is automatically created in the project root directory when you first run the application.
All tables are initialized automatically — no manual database setup is required.

### Resetting the Database
To reset all data, simply delete the `ecommerce.db` file and restart the application.

## Navigation
The application uses a **sidebar navigation** layout:
1. Launch the application
2. Use the **left sidebar buttons** to switch between modules
3. Each module loads in the **center content area**
4. Use the **top menu bar** for file operations and help

## Common Operations

### Adding a Record
1. Navigate to the desired module
2. Fill in the form fields at the bottom
3. Click **"Add"**

### Updating a Record
1. Click on a row in the table to select it
2. The form fields will populate with the selected data
3. Modify the fields as needed
4. Click **"Update"**

### Deleting a Record
1. Click on a row in the table to select it
2. Click **"Delete"**
3. Confirm the deletion in the dialog

### Searching / Filtering
Use the search field (where available) to filter records by keywords.

## Shopping Workflow
1. Browse products in the **Products** module
2. Add items to your **Cart**
3. Review cart and adjust quantities
4. Proceed to **Checkout** — enter shipping address and payment
5. Track your order in the **Orders** module

## Troubleshooting

| Issue | Solution |
|-------|----------|
| `mvn: command not found` | Install Maven and add it to your system PATH |
| `java.lang.module` errors | Ensure you're using JDK 21+, not an older version |
| JavaFX not found | Run with `mvn javafx:run` (not `java -jar`) |
| Database locked | Close other instances of the application |
| Blank window | Check that FXML files exist in `src/main/resources/fxml/` |

## License
This project is developed for educational purposes as part of CSE215 coursework.
