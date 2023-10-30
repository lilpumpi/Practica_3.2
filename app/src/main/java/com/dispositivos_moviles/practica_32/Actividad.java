package com.dispositivos_moviles.practica_32;

import java.util.Date;

public class Actividad {

    private String nombre;
    private Date fechaLim;

    public Actividad(String nombre, Date fechaLim){
        this.nombre = nombre;
        this.fechaLim = fechaLim;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Date getFechaLim() {
        return fechaLim;
    }

    public void setFechaLim(Date fechaLim) {
        this.fechaLim = fechaLim;
    }

    @Override
    public String toString() {
        return this.getNombre() + "(" + this.getFechaLim().toString() + ")";
    }
}