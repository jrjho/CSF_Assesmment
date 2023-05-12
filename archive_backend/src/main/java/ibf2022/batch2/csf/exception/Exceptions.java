package ibf2022.batch2.csf.exception;

public class Exceptions extends RuntimeException {

    public Exceptions(){
        super();
    }
    
    
    public Exceptions(String message) {
        super(message);
    }

    
    public Exceptions(String message, Throwable cause) {
        super(message, cause);
    }

    
}