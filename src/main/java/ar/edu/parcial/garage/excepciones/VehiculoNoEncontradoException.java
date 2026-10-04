package ar.edu.parcial.garage.excepciones;

public final class VehiculoNoEncontradoException extends IllegalArgumentException {
    public VehiculoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
