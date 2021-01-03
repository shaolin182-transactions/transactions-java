package org.transactions.exception;

public class TransactionProcessException extends RuntimeException {
    public TransactionProcessException(String message, Exception exception) {
        super(message, exception);
    }
}
