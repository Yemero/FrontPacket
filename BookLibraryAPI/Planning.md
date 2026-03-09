# Book Library API Plan

## Project Structure

```
book-library-api/
├── pom.xml                                      # Maven build config & dependencies
├── README.md
└── src/
    └── main/
        ├── java/
        │   └── com/booklibrary/
        │       ├── BookLibraryApplication.java  # JAX-RS app entry point (@ApplicationPath)
        │       ├── entity/
        │       │   └── Book.java                # JPA entity (maps to "books" DB table)
        │       ├── resource/
        │       │   └── BookResource.java        # REST endpoints (CRUD operations)
        │       └── util/
        │           └── JPAUtil.java             # EntityManagerFactory singleton helper
        └── resources/
            └── META-INF/
                └── persistence.xml              # JPA/Hibernate/SQLite configuration
```

## API Endpoints

Base URL: `http://localhost:8080/api`

### Book Object Structure

```json
{
  "id": 1,
  "title": "Dune",
  "author": "Frank Herbert",
  "genre": "Science Fiction",
  "yearPublished": 1965
}
```

---

### `GET /books` — Get all books

Returns a list of all books in the library.

**Request:**
```bash
curl http://localhost:8080/api/books
```

**Response:** `200 OK`
```json
[
  {
    "id": 1,
    "title": "Dune",
    "author": "Frank Herbert",
    "genre": "Science Fiction",
    "yearPublished": 1965
  },
  {
    "id": 2,
    "title": "The Hobbit",
    "author": "J.R.R. Tolkien",
    "genre": "Fantasy",
    "yearPublished": 1937
  }
]
```

---

### `GET /books/{id}` — Get a book by ID

Returns a single book matching the given ID.

**Request:**
```bash
curl http://localhost:8080/api/books/1
```

**Response:** `200 OK`
```json
{
  "id": 1,
  "title": "Dune",
  "author": "Frank Herbert",
  "genre": "Science Fiction",
  "yearPublished": 1965
}
```

**Not found response:** `404 Not Found`
```json
{ "error": "Book with ID 99 not found" }
```

---

### `POST /books` — Add a new book

Creates a new book. `title` and `author` are required.

**Request:**
```bash
curl -X POST http://localhost:8080/api/books \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Dune",
    "author": "Frank Herbert",
    "genre": "Science Fiction",
    "yearPublished": 1965
  }'
```

**Response:** `201 Created`
```json
{
  "id": 3,
  "title": "Dune",
  "author": "Frank Herbert",
  "genre": "Science Fiction",
  "yearPublished": 1965
}
```

---

### `PUT /books/{id}` — Update a book

Updates an existing book. Only include the fields you want to change.

**Request:**
```bash
curl -X PUT http://localhost:8080/api/books/1 \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Dune Messiah",
    "yearPublished": 1969
  }'
```

**Response:** `200 OK`
```json
{
  "id": 1,
  "title": "Dune Messiah",
  "author": "Frank Herbert",
  "genre": "Science Fiction",
  "yearPublished": 1969
}
```

---

### `DELETE /books/{id}` — Delete a book

Deletes the book with the given ID.

**Request:**
```bash
curl -X DELETE http://localhost:8080/api/books/1
```

**Response:** `200 OK`
```json
{ "message": "Book with ID 1 successfully deleted" }
```

---