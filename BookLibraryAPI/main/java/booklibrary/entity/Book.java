package com.booklibrary.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Book entity - represents a book in the library database.
 *
 * The @Entity annotation tells JPA that this class maps to a database table.
 * The @Table annotation lets us specify the exact table name.
 *
 * JPA will automatically create the "books" table when the app starts,
 * based on the fields defined in this class.
 */
@Entity
@Table(name = "books")
public class Book {

    /**
     * The primary key (unique ID) for each book.
     *
     * @Id         - marks this field as the primary key
     * @GeneratedValue - JPA will auto-generate the ID for new books
     * IDENTITY strategy works well with SQLite's auto-increment
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The book's title.
     *
     * @Column(nullable = false) means this field is required —
     * JPA will throw an error if you try to save a book without a title.
     */
    @Column(nullable = false)
    private String title;

    /**
     * The book's author.
     * Also required — every book must have an author.
     */
    @Column(nullable = false)
    private String author;

    /**
     * The genre of the book (e.g. "Fantasy", "Science Fiction", "Mystery").
     * Optional field — can be null.
     */
    @Column
    private String genre;

    /**
     * The year the book was published (e.g. 1984, 2023).
     * Optional field — can be null.
     */
    @Column(name = "year_published")
    private Integer yearPublished;

    // ================================================
    //  Constructors
    // ================================================

    /**
     * No-argument constructor — required by JPA.
     * JPA uses this to create Book objects when reading from the database.
     */
    public Book() {}

    /**
     * Convenience constructor for creating a new book with all fields.
     */
    public Book(String title, String author, String genre, Integer yearPublished) {
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.yearPublished = yearPublished;
    }

    // ================================================
    //  Getters and Setters
    //  These allow JAX-RS (Jackson) to serialize/deserialize
    //  this object to/from JSON automatically.
    // ================================================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Integer getYearPublished() {
        return yearPublished;
    }

    public void setYearPublished(Integer yearPublished) {
        this.yearPublished = yearPublished;
    }

    /**
     * toString() makes it easy to print a Book object for debugging.
     */
    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", genre='" + genre + '\'' +
                ", yearPublished=" + yearPublished +
                '}';
    }
}
