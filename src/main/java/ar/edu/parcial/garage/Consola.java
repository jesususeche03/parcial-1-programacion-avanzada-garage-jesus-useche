package ar.edu.parcial.garage;

import java.io.PrintStream;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Scanner;

public final class Consola {
    private final Scanner entrada;
    private final PrintStream salida;

    public Consola(Scanner entrada, PrintStream salida) {
        this.entrada = Objects.requireNonNull(entrada);
        this.salida = Objects.requireNonNull(salida);
    }

    public static void main(String[] args) {
        new Consola(new Scanner(System.in), System.out).ejecutar();
    }

    public void ejecutar() {
        try {
            Garage garage = solicitarGarage();
            boolean continuar = true;
            while (continuar) {
                mostrarMenu();
                try {
                    int opcion = leerEntero("Opcion: ");
                    switch (opcion) {
                        case 1 -> registrarIngreso(garage);
                        case 2 -> registrarSalida(garage);
                        case 3 -> listarVehiculos(garage);
                        case 4 -> mostrarEstado(garage);
                        case 5 -> mostrarReportes(garage);
                        case 6 -> {
                            salida.println("Hasta luego.");
                            continuar = false;
                        }
                        default -> salida.println("Error: opcion de menu invalida.");
                    }
                } catch (IllegalArgumentException | IllegalStateException e) {
                    salida.println("Error: " + e.getMessage());
                }
            }
        } catch (NoSuchElementException e) {
            salida.println("Fin de la entrada. Programa finalizado.");
        }
    }

    private Garage solicitarGarage() {
        while (true) {
            try {
                return new Garage(leerEntero("Capacidad total del garage (espacios): "));
            } catch (IllegalArgumentException e) {
                salida.println("Error: " + e.getMessage());
            }
        }
    }

    private void mostrarMenu() {
        salida.println("""
                === SISTEMA DE GARAGE ===
                1. Registrar ingreso
                2. Registrar salida
                3. Listar vehículos
                4. Estado del garage
                5. Reportes
                6. Salir""");
    }

    private String leerTexto(String mensaje) {
        salida.print(mensaje);
        return entrada.nextLine();
    }

    private int leerEntero(String mensaje) {
        String texto = leerTexto(mensaje);
        try {
            return Integer.parseInt(texto.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Debe ingresar un numero entero valido.", e);
        }
    }

    private void registrarIngreso(Garage garage) {
        int tipo = leerEntero("Tipo (1. Moto, 2. Auto, 3. Camion): ");
        if (tipo < 1 || tipo > 3) {
            throw new IllegalArgumentException("Tipo de vehiculo invalido.");
        }
        String patente = leerTexto("Patente: ");
        String marca = leerTexto("Marca: ");
        String modelo = leerTexto("Modelo: ");
        int horas = leerEntero("Horas estimadas (enteras): ");
        Vehiculo vehiculo = switch (tipo) {
            case 1 -> new Moto(patente, marca, modelo, horas);
            case 2 -> new Auto(patente, marca, modelo, horas);
            case 3 -> new Camion(patente, marca, modelo, horas);
            default -> throw new IllegalArgumentException("Tipo de vehiculo invalido.");
        };
        garage.registrarIngreso(vehiculo);
        salida.println("Ingreso registrado. " + vehiculo.mostrarDatos());
    }

    private void registrarSalida(Garage garage) {
        Vehiculo vehiculo = garage.registrarSalida(leerTexto("Patente de salida: "));
        salida.println("Salida registrada. " + vehiculo.mostrarDatos());
    }

    private void listarVehiculos(Garage garage) {
        if (garage.getCantidadTotal() == 0) {
            salida.println("No hay vehiculos en el garage.");
        }
        for (Vehiculo vehiculo : garage.listarVehiculos()) {
            salida.println(vehiculo.mostrarDatos());
        }
    }

    private void mostrarEstado(Garage garage) {
        salida.println("Capacidad total: " + garage.getCapacidadTotal());
        salida.println("Espacio ocupado: " + garage.getEspacioOcupado());
        salida.println("Espacio libre: " + garage.getEspacioDisponible());
    }

    private void mostrarReportes(Garage garage) {
        salida.println("Cantidad total: " + garage.getCantidadTotal());
        salida.println("Motos: " + garage.getCantidadPorTipo("Moto"));
        salida.println("Autos: " + garage.getCantidadPorTipo("Auto"));
        salida.println("Camiones: " + garage.getCantidadPorTipo("Camion"));
        mostrarEstado(garage);
        salida.printf(Locale.ROOT, "Recaudacion total estimada: $%.2f%n", garage.getRecaudacionEstimada());
    }
}
