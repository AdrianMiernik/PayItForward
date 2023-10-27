package pl.miernik.payitforward.exception;

import java.util.function.Supplier;

public class NotExistingRecordException extends Exception implements Supplier<NotExistingRecordException> {
    private static final long serialVersionUID = 1L;

    public NotExistingRecordException(String message) {
        super(message);
    }

    @Override
    public NotExistingRecordException get() {
        return this;
    }
}