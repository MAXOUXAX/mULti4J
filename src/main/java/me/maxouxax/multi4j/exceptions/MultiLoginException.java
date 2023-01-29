package me.maxouxax.multi4j.exceptions;

public class MultiLoginException extends Exception{

    public MultiLoginException(String message) {
        super(message);
    }

    public MultiLoginException(Exception e) {
        super(e);
    }

}
