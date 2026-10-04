package ar.edu.parcial.garage.excepciones;

public final class PatenteDuplicadaException extends IllegalArgumentException {
    public PatenteDuplicadaException(String mensaje) {
        super(mensaje);
    }
}
