package org.db.tests.api.bdd.exceptions;

/**
 * Created by dmitry on 02.11.18.
 *
 */
public class FrameworkException extends RuntimeException {

    public FrameworkException(String message) {
        super(message);
    }

    public FrameworkException(String message, Throwable t){
        super(message, t);
    }
}
