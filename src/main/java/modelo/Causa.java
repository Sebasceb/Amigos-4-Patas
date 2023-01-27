/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Gladys
 */
public class Causa implements Serializable, Comparable<Causa> {

    private String nombre;
    private String imagen;

    public Causa(String nombre, String imagen) {
        this.nombre = nombre;
        this.imagen = imagen;
    }

    public static List<Causa> leerCausas() {
        List<Causa> causas = new ArrayList<>();
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("causas.dat"))) {
            causas = (ArrayList<Causa>) in.readObject();
        } catch (FileNotFoundException ex) {
            System.out.println("No se encuentra el archivo" + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("IOException:" + ex.getMessage());
        } catch (ClassNotFoundException ex) {
            System.out.println("ClassNotFoundException:" + ex.getMessage());
        }

        return causas;
    }

    @Override
    public int compareTo(Causa o) {
        return nombre.compareToIgnoreCase(o.nombre);
    }

    public String getNombre() {
        return nombre;
    }

    public String getImagen() {
        return imagen;
    }

    public double calcularTotal() {
        double total = 0;
        List<Donacion> donaciones = Donacion.leerDonaciones();
        for (Donacion d : donaciones) {
            if (nombre.equals(d.getCausa().getNombre())) {
                total += d.getMonto();
            }
        }
        return total;
    }

    @Override
    public String toString() {
        return "Causa{" + "nombre=" + nombre + '}';
    }

}
