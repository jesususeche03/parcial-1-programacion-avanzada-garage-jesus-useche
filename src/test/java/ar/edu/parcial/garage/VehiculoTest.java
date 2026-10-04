package ar.edu.parcial.garage;

import ar.edu.parcial.garage.excepciones.HorasInvalidasException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class VehiculoTest {
    @ParameterizedTest
    @ValueSource(ints = {1, 3, 24, Integer.MAX_VALUE})
    void costosYEspaciosDeLosTresTipos(int horas) {
        Vehiculo moto = new Moto("M1", "Honda", "Wave", horas);
        Vehiculo auto = new Auto("A1", "Ford", "Fiesta", horas);
        Vehiculo camion = new Camion("C1", "Volvo", "FH", horas);
        assertAll(
                () -> assertEquals(horas * 700.0, moto.calcularCosto()),
                () -> assertEquals(horas * 1000.0, auto.calcularCosto()),
                () -> assertEquals(horas * 1500.0, camion.calcularCosto()),
                () -> assertEquals(1, moto.getEspaciosOcupados()),
                () -> assertEquals(2, auto.getEspaciosOcupados()),
                () -> assertEquals(4, camion.getEspaciosOcupados()));
    }

    @Test
    void calculaPolimorficamenteMedianteCalculable() {
        Calculable[] calculables = {
                new Moto("M1", "Honda", "Wave", 2),
                new Auto("A1", "Ford", "Fiesta", 3),
                new Camion("C1", "Volvo", "FH", 4)};
        double total = 0;
        for (Calculable calculable : calculables) {
            total += calculable.calcularCosto();
        }
        assertEquals(10400, total);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, Integer.MIN_VALUE})
    void rechazaHorasInvalidasEnTodosLosTipos(int horas) {
        assertAll(
                () -> assertThrows(HorasInvalidasException.class, () -> new Moto("M", "Honda", "Wave", horas)),
                () -> assertThrows(HorasInvalidasException.class, () -> new Auto("A", "Ford", "Fiesta", horas)),
                () -> assertThrows(HorasInvalidasException.class, () -> new Camion("C", "Volvo", "FH", horas)));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\t\n", "\u2003"})
    void rechazaCamposObligatorios(String valor) {
        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> new Moto(valor, "Honda", "Wave", 1)),
                () -> assertThrows(IllegalArgumentException.class, () -> new Auto("A", valor, "Fiesta", 1)),
                () -> assertThrows(IllegalArgumentException.class, () -> new Camion("C", "Volvo", valor, 1)));
    }

    @Test
    void normalizaPatenteYConservaDatosEncapsulados() {
        Vehiculo vehiculo = new Auto("  ab123cd  ", " Ford ", " Fiesta ", 2);
        assertAll(
                () -> assertEquals("AB123CD", vehiculo.getPatente()),
                () -> assertEquals("Ford", vehiculo.getMarca()),
                () -> assertEquals("Fiesta", vehiculo.getModelo()),
                () -> assertEquals(2, vehiculo.getHorasEstimadas()),
                () -> assertEquals("Auto", vehiculo.getTipo()),
                () -> assertEquals("Auto | Patente: AB123CD | Marca: Ford | Modelo: Fiesta | Horas: 2 | Espacios: 2 | Costo estimado: $2000.00", vehiculo.mostrarDatos()));
    }
}
