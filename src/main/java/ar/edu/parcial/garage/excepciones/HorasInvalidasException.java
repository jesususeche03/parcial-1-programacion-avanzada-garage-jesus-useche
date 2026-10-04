package ar.edu.parcial.garage.excepciones;

public final class HorasInvalidasException extends IllegalArgumentException {
    public HorasInvalidasException(String mensaje) {
        super(mensaje);
    }
}
