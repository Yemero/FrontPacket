package com.booklibrary.resource;

import com.booklibrary.entity.Book;
import com.booklibrary.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

/**
 * BookResource - The main REST API controller for books.
 *
 * @Path("/books") - All endpoints in this class are under the /books URL path.
 *
 * JAX-RS annotations used here:
 *   @GET    - handles HTTP GET requests (read data)
 *   @POST   - handles HTTP POST requests (create data)
 *   @PUT    - handles HTTP PUT requests (update data)
 *   @DELETE - handles HTTP DELETE requests (delete data)
 *
 * @Produces(MediaType.APPLICATION_JSON) - responses will be in JSON format
 * @Consumes(MediaType.APPLICATION_JSON) - request bodies must be in JSON format
 */
@Path("/books")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookResource {

    // ================================================
    //  GET /books
    //  Returns a list of all books in the library
    // ================================================

    /**
     * Get all books.
     *
     * Example request:  GET http://localhost:8080/api/books
     * Example response: 200 OK with a JSON array of all books
     */
    @GET
    public Response getAllBooks() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            // JPQL (JPA Query Language) — similar to SQL but uses entity class names
            TypedQuery<Book> query = em.createQuery("SELECT b FROM Book b ORDER BY b.id", Book.class);
            List<Book> books = query.getResultList();

            return Response.ok(books).build();

        } finally {
            // Always close the EntityManager to release the database connection
            em.close();
        }
    }

    // ================================================
    //  GET /books/{id}
    //  Returns a single book by its ID
    // ================================================

    /**
     * Get a single book by ID.
     *
     * @PathParam("id") extracts the {id} value from the URL path.
     *
     * Example request:  GET http://localhost:8080/api/books/1
     * Example response: 200 OK with the book as JSON, or 404 if not found
     */
    @GET
    @Path("/{id}")
    public Response getBookById(@PathParam("id") Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            // em.find() looks up an entity by its primary key
            // Returns null if no book with that ID exists
            Book book = em.find(Book.class, id);

            if (book == null) {
                // Return 404 Not Found with a descriptive error message
                return Response
                        .status(Response.Status.NOT_FOUND)
                        .entity("{\"error\": \"Book with ID " + id + " not found\"}")
                        .build();
            }

            return Response.ok(book).build();

        } finally {
            em.close();
        }
    }

    // ================================================
    //  POST /books
    //  Adds a new book to the library
    // ================================================

    /**
     * Create a new book.
     *
     * The request body (JSON) is automatically deserialized into a Book object
     * by Jersey + Jackson.
     *
     * Example request:
     *   POST http://localhost:8080/api/books
     *   Body: { "title": "Dune", "author": "Frank Herbert", "genre": "Sci-Fi", "yearPublished": 1965 }
     *
     * Example response: 201 Created with the saved book (including its new ID)
     */
    @POST
    public Response createBook(Book book) {
        // Basic validation — title and author are required
        if (book.getTitle() == null || book.getTitle().isBlank()) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"Title is required\"}")
                    .build();
        }
        if (book.getAuthor() == null || book.getAuthor().isBlank()) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"Author is required\"}")
                    .build();
        }

        EntityManager em = JPAUtil.getEntityManager();
        try {
            // Begin a database transaction — changes aren't saved until we commit
            em.getTransaction().begin();

            // em.persist() tells JPA to INSERT this new book into the database
            em.persist(book);

            // Commit saves the changes permanently
            em.getTransaction().commit();

            // Return 201 Created with the newly saved book (now has an ID)
            return Response
                    .status(Response.Status.CREATED)
                    .entity(book)
                    .build();

        } catch (Exception e) {
            // If something goes wrong, roll back the transaction
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"Failed to create book: " + e.getMessage() + "\"}")
                    .build();
        } finally {
            em.close();
        }
    }

    // ================================================
    //  PUT /books/{id}
    //  Updates an existing book by its ID
    // ================================================

    /**
     * Update an existing book.
     *
     * Replaces all fields of the book with the values from the request body.
     * If the book doesn't exist, returns 404.
     *
     * Example request:
     *   PUT http://localhost:8080/api/books/1
     *   Body: { "title": "Dune Messiah", "author": "Frank Herbert", "genre": "Sci-Fi", "yearPublished": 1969 }
     *
     * Example response: 200 OK with the updated book
     */
    @PUT
    @Path("/{id}")
    public Response updateBook(@PathParam("id") Long id, Book updatedBook) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            // First, check if the book exists
            Book existingBook = em.find(Book.class, id);

            if (existingBook == null) {
                return Response
                        .status(Response.Status.NOT_FOUND)
                        .entity("{\"error\": \"Book with ID " + id + " not found\"}")
                        .build();
            }

            em.getTransaction().begin();

            // Update only the fields we allow to be changed
            // We don't update the ID — that should never change
            if (updatedBook.getTitle() != null && !updatedBook.getTitle().isBlank()) {
                existingBook.setTitle(updatedBook.getTitle());
            }
            if (updatedBook.getAuthor() != null && !updatedBook.getAuthor().isBlank()) {
                existingBook.setAuthor(updatedBook.getAuthor());
            }
            if (updatedBook.getGenre() != null) {
                existingBook.setGenre(updatedBook.getGenre());
            }
            if (updatedBook.getYearPublished() != null) {
                existingBook.setYearPublished(updatedBook.getYearPublished());
            }

            // No need to call persist() here — JPA automatically detects and saves
            // changes to a "managed" entity (one that was loaded from the database)
            em.getTransaction().commit();

            return Response.ok(existingBook).build();

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"Failed to update book: " + e.getMessage() + "\"}")
                    .build();
        } finally {
            em.close();
        }
    }

    // ================================================
    //  DELETE /books/{id}
    //  Deletes a book by its ID
    // ================================================

    /**
     * Delete a book.
     *
     * Example request:  DELETE http://localhost:8080/api/books/1
     * Example response: 200 OK with a success message, or 404 if not found
     */
    @DELETE
    @Path("/{id}")
    public Response deleteBook(@PathParam("id") Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Book book = em.find(Book.class, id);

            if (book == null) {
                return Response
                        .status(Response.Status.NOT_FOUND)
                        .entity("{\"error\": \"Book with ID " + id + " not found\"}")
                        .build();
            }

            em.getTransaction().begin();

            // em.remove() tells JPA to DELETE this record from the database
            em.remove(book);

            em.getTransaction().commit();

            return Response
                    .ok("{\"message\": \"Book with ID " + id + " successfully deleted\"}")
                    .build();

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"Failed to delete book: " + e.getMessage() + "\"}")
                    .build();
        } finally {
            em.close();
        }
    }
}
