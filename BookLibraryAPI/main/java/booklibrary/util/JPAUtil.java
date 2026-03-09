package com.booklibrary.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * JPAUtil - A helper class that manages the JPA EntityManagerFactory.
 *
 * The EntityManagerFactory is expensive to create (it reads persistence.xml,
 * sets up the database connection pool, etc.), so we only create it ONCE
 * for the entire application lifetime using the Singleton pattern.
 *
 * Think of it like this:
 *   EntityManagerFactory = the database connection pool (created once)
 *   EntityManager         = a single database session (created per request)
 */
public class JPAUtil {

    /**
     * "BookLibraryPU" matches the persistence-unit name in persistence.xml.
     * This is how JPA knows which database configuration to use.
     */
    private static final String PERSISTENCE_UNIT_NAME = "BookLibraryPU";

    /**
     * The single shared factory instance.
     * Declared 'static' so it's shared across the entire application.
     */
    private static EntityManagerFactory factory;

    // Private constructor — prevents anyone from instantiating this class.
    // All methods are static, so there's no need to create an instance.
    private JPAUtil() {}

    /**
     * Returns the shared EntityManagerFactory.
     * Creates it the first time this method is called (lazy initialization).
     *
     * 'synchronized' ensures thread safety — if two requests come in at the
     * exact same time, only one will create the factory; the other will wait.
     */
    public static synchronized EntityManagerFactory getEntityManagerFactory() {
        if (factory == null) {
            factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        }
        return factory;
    }

    /**
     * Creates and returns a new EntityManager for a single database session.
     *
     * IMPORTANT: The caller is responsible for closing this EntityManager
     * when done (usually in a try/finally block).
     *
     * Each HTTP request should create its own EntityManager and close it
     * when the request finishes.
     */
    public static EntityManager getEntityManager() {
        return getEntityManagerFactory().createEntityManager();
    }

    /**
     * Closes the EntityManagerFactory when the application shuts down.
     *
     * This releases all database connections properly.
     * Called by the AppLifecycleListener on application shutdown.
     */
    public static void shutdown() {
        if (factory != null && factory.isOpen()) {
            factory.close();
        }
    }
}
