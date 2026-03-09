package com.booklibrary;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

/**
 * BookLibraryApplication - The JAX-RS application configuration class.
 *
 * This class acts as the entry point for the JAX-RS framework (Jersey).
 * The @ApplicationPath annotation defines the base URL path for all API endpoints.
 *
 * With @ApplicationPath("/api"), all our endpoints will be accessible under:
 *   http://localhost:8080/api/...
 *
 * So our book endpoints become:
 *   http://localhost:8080/api/books
 *   http://localhost:8080/api/books/{id}
 *
 * By extending Application and using @ApplicationPath, we don't need a web.xml file —
 * Jersey will automatically detect this class and configure itself.
 */
@ApplicationPath("/api")
public class BookLibraryApplication extends Application {
    // No code needed here — Jersey auto-scans and registers all
    // classes annotated with @Path in the same package (and sub-packages).
}
