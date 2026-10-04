package ar.edu.parcial.garage;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class ConsolaTest {
    private String ejecutar(String datos) {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        new Consola(new Scanner(datos), new PrintStream(buffer, true, StandardCharsets.UTF_8)).ejecutar();
        return buffer.toString(StandardCharsets.UTF_8);
    }

    @Test
    void recuperaCapacidadYMenuInvalidos() {
        String salida = ejecutar("abc\n0\n-1\n7\ntexto\n99\n4\n6\n");
        assertTrue(salida.contains("Debe ingresar un numero entero valido"));
        assertTrue(salida.contains("La capacidad debe ser mayor que cero"));
        assertTrue(salida.contains("opcion de menu invalida"));
        assertTrue(salida.contains("Capacidad total: 7"));
        assertTrue(salida.contains("Hasta luego."));
    }

    @Test
    void ejecutaIngresoListadoReportesYSalida() {
        String salida = ejecutar("7\n1\n1\nM\nHonda\nWave\n2\n1\n2\nA\nFord\nFiesta\n3\n1\n3\nC\nVolvo\nFH\n4\n3\n5\n2\n a \n4\n6\n");
        assertEquals(3, salida.split("Ingreso registrado", -1).length - 1);
        assertTrue(salida.contains("Cantidad total: 3"));
        assertTrue(salida.contains("Recaudacion total estimada: $10400.00"));
        assertTrue(salida.contains("Salida registrada. Auto | Patente: A"));
        assertTrue(salida.contains("Espacio libre: 2"));
        assertTrue(salida.contains("Hasta luego."));
    }

    @ParameterizedTest
    @ValueSource(strings = {"0", "-3", "abc", "1.5", "2147483648"})
    void horasIncorrectasNoCierranPrograma(String horas) {
        String salida = ejecutar("4\n1\n1\nM\nHonda\nWave\n" + horas + "\n5\n6\n");
        assertTrue(salida.contains("Error:"));
        assertFalse(salida.contains("Ingreso registrado"));
        assertTrue(salida.contains("Cantidad total: 0"));
        assertTrue(salida.contains("Hasta luego."));
    }

    @Test
    void erroresDeNegocioPermitenContinuar() {
        String salida = ejecutar("1\n1\n9\n1\n1\n \nHonda\nWave\n1\n1\n1\nM\nHonda\nWave\n1\n1\n1\n m \nHonda\nWave\n1\n1\n2\nA\nFord\nFiesta\n1\n2\nX\n2\nM\n3\n6\n");
        assertTrue(salida.contains("Tipo de vehiculo invalido"));
        assertTrue(salida.contains("El campo patente es obligatorio"));
        assertTrue(salida.contains("La patente ya esta registrada"));
        assertTrue(salida.contains("No hay espacio suficiente"));
        assertTrue(salida.contains("No se encontro la patente"));
        assertTrue(salida.contains("Salida registrada"));
        assertTrue(salida.contains("No hay vehiculos"));
        assertTrue(salida.contains("Hasta luego."));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "4\n", "4\n1\n1\n"})
    void finDeEntradaTerminaSinExcepcion(String datos) {
        assertTrue(ejecutar(datos).contains("Fin de la entrada. Programa finalizado."));
    }
}
