/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.search.words.models;

import java.util.Objects;

public class PdfMarcador {

    private Integer movimientoX;
    private Integer movimientoY;
    private Integer numeroPagina;
    private Integer vecesRepetidas;
    private String texto;
    private UsuarioDetalle usuarioDetalle;

    public PdfMarcador() {
    }

    public PdfMarcador(Integer movimientoX, Integer movimientoY, Integer numeroPagina, String texto) {
        this.movimientoX = movimientoX;
        this.movimientoY = movimientoY;
        this.numeroPagina = numeroPagina;
        this.texto = texto;
    }

    public void setMovimientoX(Integer movimientoX) {
        this.movimientoX = movimientoX;
    }

    public void setMovimientoY(Integer movimientoY) {
        this.movimientoY = movimientoY;
    }

    public void setNumeroPagina(Integer numeroPagina) {
        this.numeroPagina = numeroPagina;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public Integer getMovimientoX() {
        return movimientoX;
    }

    public Integer getMovimientoY() {
        return movimientoY;
    }

    public Integer getNumeroPagina() {
        return numeroPagina; // Método para obtener el número de página
    }

    public String getTexto() {
        return texto;
    }

    public UsuarioDetalle getUsuarioDetalle() {
        return usuarioDetalle;
    }

    public void setUsuarioDetalle(UsuarioDetalle usuarioDetalle) {
        this.usuarioDetalle = usuarioDetalle;
    }

    public Integer getVecesRepetidas() {
        return vecesRepetidas;
    }

    public void setVecesRepetidas(Integer vecesRepetidas) {
        this.vecesRepetidas = vecesRepetidas;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 13 * hash + Objects.hashCode(this.numeroPagina);
        hash = 13 * hash + Objects.hashCode(this.texto);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PdfMarcador other = (PdfMarcador) obj;
        if (!Objects.equals(this.texto, other.texto)) {
            return false;
        }
        return Objects.equals(this.numeroPagina, other.numeroPagina);
    }

    @Override
    public String toString() {
        return "PdfMarcador{" + "movimientoX=" + movimientoX + ", movimientoY=" + movimientoY + ", numeroPagina=" + numeroPagina + ", vecesRepetidas=" + vecesRepetidas + ", texto=" + texto + '}';
    }

}
