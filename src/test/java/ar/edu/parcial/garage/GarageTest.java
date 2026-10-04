package ar.edu.parcial.garage;

import ar.edu.parcial.garage.excepciones.GarageLlenoException;
import ar.edu.parcial.garage.excepciones.PatenteDuplicadaException;
import ar.edu.parcial.garage.excepciones.VehiculoNoEncontradoException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GarageTest {
    @ParameterizedTest
    @ValueSource(ints = {0, -1, Integer.MIN_VALUE})
    void rechazaCapacidadInvalida(int capacidad) {
        assertThrows(IllegalArgumentException.class, () -> new Garage(capacidad));
    }

    @Test
    void garageVacioTieneReportesEnCero() {
        Garage garage = new Garage(10);
        assertAll(
                () -> assertEquals(10, garage.getCapacidadTotal()),
                () -> assertEquals(0, garage.getCantidadTotal()),
                () -> assertEquals(0, garage.getCantidadPorTipo("Moto")),
                () -> assertEquals(0, garage.getCantidadPorTipo("Auto")),
                () -> assertEquals(0, garage.getCantidadPorTipo("Camion")),
                () -> assertEquals(0, garage.getEspacioOcupado()),
                () -> assertEquals(10, garage.getEspacioDisponible()),
                () -> assertEquals(0, garage.getRecaudacionEstimada()),
                () -> assertTrue(garage.listarVehiculos().isEmpty()));
    }

    @Test
    void ingresoBusquedaYListadoConservanVehiculo() {
        Garage garage = new Garage(5);
        Vehiculo auto = new Auto(" ab123cd ", "Ford", "Fiesta", 3);
        garage.registrarIngreso(auto);
        assertSame(auto, garage.buscarPorPatente("  Ab123cd "));
        assertEquals(List.of(auto), garage.listarVehiculos());
        assertEquals(1, garage.getCantidadTotal());
        assertEquals(2, garage.getEspacioOcupado());
        assertEquals(3, garage.getEspacioDisponible());
    }

    @Test
    void patenteDuplicadaEntreTiposNoModificaEstado() {
        Garage garage = new Garage(10);
        Vehiculo moto = new Moto("AB123", "Honda", "Wave", 2);
        garage.registrarIngreso(moto);
        assertThrows(PatenteDuplicadaException.class,
                () -> garage.registrarIngreso(new Camion(" ab123 ", "Volvo", "FH", 8)));
        assertEquals(List.of(moto), garage.listarVehiculos());
        assertEquals(1, garage.getEspacioOcupado());
        assertEquals(1400, garage.getRecaudacionEstimada());
    }

    @Test
    void rechazaIngresoSiEspacioRestanteEsInsuficiente() {
        Garage garage = new Garage(5);
        garage.registrarIngreso(new Auto("A", "Ford", "Fiesta", 2));
        assertThrows(GarageLlenoException.class,
                () -> garage.registrarIngreso(new Camion("C", "Volvo", "FH", 1)));
        assertEquals(1, garage.getCantidadTotal());
        assertEquals(3, garage.getEspacioDisponible());
    }

    @Test
    void admiteCapacidadExactaYRechazaSiguienteIngreso() {
        Garage garage = new Garage(4);
        garage.registrarIngreso(new Camion("C", "Volvo", "FH", 1));
        assertEquals(0, garage.getEspacioDisponible());
        assertThrows(GarageLlenoException.class,
                () -> garage.registrarIngreso(new Moto("M", "Honda", "Wave", 1)));
        assertEquals(1, garage.getCantidadTotal());
    }

    @Test
    void salidaLiberaEspacioYPermiteReingresoDePatente() {
        Garage garage = new Garage(4);
        Vehiculo camion = new Camion("C", "Volvo", "FH", 3);
        garage.registrarIngreso(camion);
        assertSame(camion, garage.registrarSalida(" c "));
        assertAll(
                () -> assertEquals(0, garage.getCantidadTotal()),
                () -> assertEquals(0, garage.getEspacioOcupado()),
                () -> assertEquals(4, garage.getEspacioDisponible()),
                () -> assertEquals(0, garage.getRecaudacionEstimada()),
                () -> assertThrows(VehiculoNoEncontradoException.class, () -> garage.buscarPorPatente("C")));
        garage.registrarIngreso(new Auto("C", "Ford", "Fiesta", 2));
        assertEquals(2, garage.getEspacioDisponible());
        assertEquals(2000, garage.getRecaudacionEstimada());
    }

    @Test
    void busquedaYSalidaInexistentesNoModificanGarage() {
        Garage garage = new Garage(4);
        Vehiculo auto = new Auto("A", "Ford", "Fiesta", 1);
        garage.registrarIngreso(auto);
        assertThrows(VehiculoNoEncontradoException.class, () -> garage.buscarPorPatente("X"));
        assertThrows(VehiculoNoEncontradoException.class, () -> garage.registrarSalida("X"));
        assertEquals(List.of(auto), garage.listarVehiculos());
        assertEquals(2, garage.getEspacioDisponible());
    }

    @Test
    void reportesReflejanFlotaMixtaYSuSalida() {
        Garage garage = new Garage(12);
        garage.registrarIngreso(new Moto("M1", "Honda", "Wave", 2));
        garage.registrarIngreso(new Moto("M2", "Yamaha", "FZ", 3));
        garage.registrarIngreso(new Auto("A", "Ford", "Fiesta", 4));
        garage.registrarIngreso(new Camion("C", "Volvo", "FH", 5));
        assertAll(
                () -> assertEquals(4, garage.getCantidadTotal()),
                () -> assertEquals(2, garage.getCantidadPorTipo(" moto ")),
                () -> assertEquals(1, garage.getCantidadPorTipo("AUTO")),
                () -> assertEquals(1, garage.getCantidadPorTipo("Camion")),
                () -> assertEquals(0, garage.getCantidadPorTipo("Bicicleta")),
                () -> assertEquals(8, garage.getEspacioOcupado()),
                () -> assertEquals(4, garage.getEspacioDisponible()),
                () -> assertEquals(15000, garage.getRecaudacionEstimada()));
        garage.registrarSalida("M1");
        assertEquals(3, garage.getCantidadTotal());
        assertEquals(1, garage.getCantidadPorTipo("Moto"));
        assertEquals(7, garage.getEspacioOcupado());
        assertEquals(5, garage.getEspacioDisponible());
        assertEquals(13600, garage.getRecaudacionEstimada());
    }

    @Test
    void listadoEsInstantaneaInmutable() {
        Garage garage = new Garage(4);
        garage.registrarIngreso(new Moto("M", "Honda", "Wave", 1));
        List<Vehiculo> listado = garage.listarVehiculos();
        assertThrows(UnsupportedOperationException.class, listado::clear);
        garage.registrarSalida("M");
        assertEquals(1, listado.size());
        assertEquals(0, garage.getCantidadTotal());
    }

    @Test
    void rechazaVehiculoNulo() {
        Garage garage = new Garage(4);
        assertThrows(NullPointerException.class, () -> garage.registrarIngreso(null));
        assertEquals(0, garage.getCantidadTotal());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\t"})
    void rechazaPatentesYTiposVaciosEnConsultas(String valor) {
        Garage garage = new Garage(4);
        assertThrows(IllegalArgumentException.class, () -> garage.buscarPorPatente(valor));
        assertThrows(IllegalArgumentException.class, () -> garage.registrarSalida(valor));
        assertThrows(IllegalArgumentException.class, () -> garage.getCantidadPorTipo(valor));
    }
}
