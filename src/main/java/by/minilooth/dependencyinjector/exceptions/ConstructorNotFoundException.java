package by.minilooth.dependencyinjector.exceptions;

public class ConstructorNotFoundException extends RuntimeException {

    public ConstructorNotFoundException(String message) {
        super(message);
    }

}
