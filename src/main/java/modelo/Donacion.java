/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Gladys
 */
public class Donacion {

    private Causa causa;
    private String donador;
    private double monto;

    public Donacion(Causa causa, String donador, double monto) {
        this.causa = causa;
        this.donador = donador;
        this.monto = monto;
    }

    public Causa getCausa() {
        return causa;
    }

    public void setCausa(Causa causa) {
        this.causa = causa;
    }

    public String getDonador() {
        return donador;
    }

    public void setDonador(String donador) {
        this.donador = donador;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public static List<Donacion> leerDonaciones() {
        List<Donacion> donaciones = new ArrayList<>();
        //se necesita buscar la causa por el nombre, entonces llamar al metodo leerCausas
        List<Causa> causas = Causa.leerCausas();

        try (BufferedReader br = new BufferedReader(new FileReader("donaciones.txt"))) {
            String linea = "";
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.trim().split(",");
                //buscar la causa
                boolean found = false;
                for (int i = 0; i < causas.size() && !found; i++) {
                    if (causas.get(i).getNombre().equals(datos[0])) {
                        found = true;
                        //creo el objeto donacion y lo agrego a la lista
                        Donacion d = new Donacion(causas.get(i), datos[1].trim(), Double.valueOf(datos[2].trim()));
                        donaciones.add(d);
                    }
                }
            }

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return donaciones;

    }

    @Override
    public String toString() {
        return "Donacion{" + "causa=" + causa + ", donador=" + donador + ", monto=" + monto + '}';
    }

    public static void escribirDonacion(Causa c, String donador, double monto) throws IOException, DonacionInavlidaException{
        if (donador.equals("")){
            throw new DonacionInavlidaException ("Nombre del donador no puede ser vacio");
        }
        if (monto<=0){
            throw new DonacionInavlidaException ("Valor de donacion debe ser mayor a cero");
        }

        FileWriter fw = new FileWriter("donaciones.txt",true);
        fw.write(c.getNombre()+", " + donador+", " + monto+"\n");
        fw.close();
        
    }
    public static void main(String[] args) {
     /*   ArrayList<Causa> causas = new ArrayList<>();
        causas.add(new Causa("Operacion Matias", "causa1.png"));
        causas.add(new Causa("Casa Josefa", "causa2.png"));
        causas.add(new Causa("Transplante Gemelos", "causa3.png"));
        causas.add(new Causa("Casa para Maria", "causa4.jpeg"));

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("causas.dat"))) {
            oos.writeObject(causas);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
*/
   
    }
}
