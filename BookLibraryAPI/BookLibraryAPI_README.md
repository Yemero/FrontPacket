# Book Library API

Simple RESTful API for managing a personal book library, built as a portfolio project to demonstrate backend development skills with Java.

## Tech Stack

| Technology | Purpose |
|------------|---------|
| **Java 11** | Core programming language |
| **JAX-RS (Jakarta EE)** | REST API standard / annotations |
| **Jersey 3** | JAX-RS implementation (by Eclipse) |
| **JPA (Jakarta Persistence)** | Object-relational mapping standard |
| **Hibernate 6** | JPA implementation (ORM engine) |
| **SQLite** | Lightweight file-based relational database |
| **Jackson** | JSON serialization/deserialization |
| **Maven** | Build tool and dependency management |
| **Jetty** | Embedded web server for local development |

---

## How to Run Locally

### Prerequisites

- **Java 11+** — [Download here](https://adoptium.net/)
- **Maven 3.6+** — [Download here](https://maven.apache.org/download.cgi)

Verify installations:
```bash
java -version
mvn -version
```

### Steps

**1. Clone the repository**
```bash
git clone https://github.com/your-username/book-library-api.git
cd book-library-api
```

**2. Build the project**
```bash
mvn clean package
```

**3. Start the server**
```bash
mvn jetty:run
```

The API will be running at: **`http://localhost:8080/api`**

> A `library.db` SQLite file will be created automatically in the project root on first run.

---

## Key Concepts Demonstrated

- **REST principles** — proper use of HTTP methods (GET, POST, PUT, DELETE) and status codes
- **JPA / ORM** — mapping Java objects to database tables without writing raw SQL
- **Separation of concerns** — entity, resource, and utility layers kept distinct
- **Transaction management** — explicit begin/commit/rollback for data integrity
- **Dependency management** — Maven handles all library downloads and build lifecycle

---

## Possible Extensions

 - **TODO**

---

## 📄 License

This project is open source and available under the [MIT License](LICENSE).