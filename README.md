# CoinWise - Personal Expense Sharing & Settlement System

## Setup Instructions

1. **Database Setup**
   - By default, the application is set to connect to MySQL on `localhost:3306`.
   - Ensure MySQL is running and accessible with `root`/`root` credentials.
   - The database `coinwise_db` will be created automatically.
   - *Fallback:* If you don't have MySQL installed, open `src/main/resources/application.properties`, comment out the MySQL lines, and uncomment the H2 lines to use the in-memory database.

2. **Stop running instances**
   - If you have an existing terminal running `mvn spring-boot:run`, press `Ctrl + C` to stop it completely. Since we changed dependencies in `pom.xml`, a clean restart is necessary.

3. **Run the Project**
   - Open a terminal in the project root (`CoinWise/`).
   - Run: `mvn spring-boot:run`

4. **Test Credentials**
   - Head to `http://localhost:8080/` in your browser.
   - The initial seed data contains three test users:
     - Username: `alice` | Password: `password123`
     - Username: `bob` | Password: `password123`
     - Username: `charlie` | Password: `password123`
