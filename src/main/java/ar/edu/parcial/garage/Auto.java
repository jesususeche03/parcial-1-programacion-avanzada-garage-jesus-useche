package ar.edu.parcial.garage;

public final class Auto extends Vehiculo {
    public Auto(String patente, String marca, String modelo, int horasEstimadas) {
        super(patente, marca, modelo, horasEstimadas);
    }

    @Override
    public double calcularCosto() { return getHorasEstimadas() * 1000.0; }

    @Override
    public int getEspaciosOcupados() { return 2; }

    @Override
    public String getTipo() { return "Auto"; }
}
