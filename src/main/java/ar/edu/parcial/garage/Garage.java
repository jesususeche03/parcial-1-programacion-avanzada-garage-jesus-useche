package ar.edu.parcial.garage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class Garage {
    private final int capacidadTotal;
    private final List<Vehiculo> vehiculos;

    public Garage(int capacidadTotal) {
        if (capacidadTotal <= 0) {
            throw new IllegalArgumentException("La capacidad debe ser mayor que cero.");
        }
        this.capacidadTotal = capacidadTotal;
        this.vehiculos = new ArrayList<>();
    }

    public void registrarIngreso(Vehiculo vehiculo) {
        Objects.requireNonNull(vehiculo, "El vehiculo es obligatorio.");
        for (Vehiculo actual : vehiculos) {
            if (actual.getPatente().equals(vehiculo.getPatente())) {
                throw new IllegalArgumentException("La patente ya esta registrada: " + vehiculo.getPatente());
            }
        }
        if (vehiculo.getEspaciosOcupados() > getEspacioDisponible()) {
            throw new IllegalStateException("No hay espacio suficiente para el vehiculo.");
        }
        vehiculos.add(vehiculo);
    }

    public Vehiculo buscarPorPatente(String patente) {
        String normalizada = Vehiculo.normalizarPatente(patente);
        for (Vehiculo vehiculo : vehiculos) {
            if (vehiculo.getPatente().equals(normalizada)) {
                return vehiculo;
            }
        }
        throw new IllegalArgumentException("No se encontro la patente: " + normalizada);
    }

    public Vehiculo registrarSalida(String patente) {
        Vehiculo vehiculo = buscarPorPatente(patente);
        vehiculos.remove(vehiculo);
        return vehiculo;
    }

    public List<Vehiculo> listarVehiculos() {
        return List.copyOf(vehiculos);
    }

    public int getCapacidadTotal() { return capacidadTotal; }

    public int getEspacioOcupado() {
        int ocupado = 0;
        for (Vehiculo vehiculo : vehiculos) {
            ocupado += vehiculo.getEspaciosOcupados();
        }
        return ocupado;
    }

    public int getEspacioDisponible() {
        return capacidadTotal - getEspacioOcupado();
    }

    public int getCantidadTotal() { return vehiculos.size(); }

    public int getCantidadPorTipo(String tipo) {
        if (tipo == null || tipo.isBlank()) {
            throw new IllegalArgumentException("El tipo es obligatorio.");
        }
        int cantidad = 0;
        for (Vehiculo vehiculo : vehiculos) {
            if (vehiculo.getTipo().equalsIgnoreCase(tipo.trim())) {
                cantidad++;
            }
        }
        return cantidad;
    }

    public double getRecaudacionEstimada() {
        double total = 0;
        for (Vehiculo vehiculo : vehiculos) {
            total += vehiculo.calcularCosto();
        }
        return total;
    }
}
