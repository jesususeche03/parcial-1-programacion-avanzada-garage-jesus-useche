package ar.edu.parcial.garage;

import ar.edu.parcial.garage.excepciones.HorasInvalidasException;
import java.util.Locale;

public abstract class Vehiculo implements Calculable {
    private final String patente;
    private final String marca;
    private final String modelo;
    private final int horasEstimadas;

    protected Vehiculo(String patente, String marca, String modelo, int horasEstimadas) {
        this.patente = normalizarPatente(patente);
        this.marca = validarTexto(marca, "marca");
        this.modelo = validarTexto(modelo, "modelo");
        if (horasEstimadas <= 0) {
            throw new HorasInvalidasException("Las horas estimadas deben ser mayores que cero.");
        }
        this.horasEstimadas = horasEstimadas;
    }

    public static String normalizarPatente(String patente) {
        return validarTexto(patente, "patente").toUpperCase(Locale.ROOT);
    }

    private static String validarTexto(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("El campo " + campo + " es obligatorio.");
        }
        return valor.trim();
    }

    public String getPatente() { return patente; }
    public String getMarca() { return marca; }
    public String getModelo() { return modelo; }
    public int getHorasEstimadas() { return horasEstimadas; }

    @Override
    public abstract double calcularCosto();
    public abstract int getEspaciosOcupados();
    public abstract String getTipo();

    public String mostrarDatos() {
        return String.format(Locale.ROOT,
                "%s | Patente: %s | Marca: %s | Modelo: %s | Horas: %d | Espacios: %d | Costo estimado: $%.2f",
                getTipo(), patente, marca, modelo, horasEstimadas, getEspaciosOcupados(), calcularCosto());
    }
}
