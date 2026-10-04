package ar.edu.parcial.garage.excepciones;

public final class GarageLlenoException extends IllegalStateException {
    public GarageLlenoException(String mensaje) {
        super(mensaje);
    }
}
