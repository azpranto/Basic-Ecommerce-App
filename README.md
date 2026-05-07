# 🛍️ E-Commerce Desktop Client

A modern, elegant, and interactive desktop application for managing e-commerce operations. Built with **JavaFX 21**, **Maven**, and **SQLite**, this client offers a seamless experience for managing products, customers, orders, and sales.

## ✨ Key Features

### 🎨 Modern & Elegant UI
- **Indigo Theme:** A professional and sophisticated color palette.
- **Responsive Layout:** Optimized for high-resolution displays with clean spacing and modern typography.
- **Glassmorphic Sidebar:** A refined navigation area with subtle gradients and depth.
- **Dynamic Tables:** Modern data grids with hover effects and high-contrast readability.

### 🎬 Interactive Animations
- **Smooth Navigation:** Scale and fade transitions when switching between modules.
- **Micro-Interactions:** Interactive hover effects on buttons and cards for intuitive feedback.
- **Count-Up Stats:** Animated numerical statistics on the dashboard for a dynamic feel.
- **Startup Reveal:** A professional fade-in animation when the application launches.

### 🛠 Core Functionality
- **📊 Dashboard:** Quick overview of total products, customers, and pending orders.
- **🛍 Product Management:** Full CRUD operations for products with search and category filtering.
- **👤 Customer Management:** Manage customer profiles, contact info, and addresses.
- **🛒 Shopping Cart:** Add products to cart, adjust quantities, and proceed to checkout.
- **📦 Order Tracking:** Monitor order statuses (Pending, Paid, Shipped, Cancelled) and manage transitions.
- **📄 CSV Export:** Export product, customer, and order data to CSV files for reporting.
- **💾 Local SQLite Database:** Fast, reliable, and serverless data storage.

## 🚀 Getting Started

### Prerequisites
- **Java JDK 21** or higher.
- **Maven** 3.8+.

### Installation & Run
1. Clone the repository:
   ```bash
   git clone <repository-url>
   ```
2. Navigate to the project directory:
   ```bash
   cd ECommerceDesktopClient_SqliteCrud
   ```
3. Run the application:
   ```bash
   mvn clean javafx:run
   ```

## 📂 Project Structure

- `src/main/java`: Java source files (Controllers, Models, Services, Utilities).
- `src/main/resources/fxml`: UI layouts defined in FXML.
- `src/main/resources/css`: Modern Indigo theme styles (`app.css`).
- `ecommerce.db`: Local SQLite database file.

## 🛠 Tech Stack
- **Language:** Java 21
- **UI Framework:** JavaFX 21
- **Build Tool:** Maven
- **Database:** SQLite (JDBC)
- **Styling:** Vanilla CSS (JavaFX CSS)
