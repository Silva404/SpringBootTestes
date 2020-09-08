package expections;

public class AutenticationError extends RuntimeException{

    public AutenticationError(String message) {
        super(message);
    }
}
