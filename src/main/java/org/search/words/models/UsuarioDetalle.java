/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.search.words.models;

public class UsuarioDetalle {

    private Long id;
    private ServidorDatos servidor;
    private String usuario;
    private String estado;

    public UsuarioDetalle() {
    }

    public UsuarioDetalle(String usuario) {
        this.usuario = usuario;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public ServidorDatos getServidor() {
        return servidor;
    }

    public void setServidor(ServidorDatos servidor) {
        this.servidor = servidor;
    }

    @Override
    public String toString() {
        return "UsuarioInicioSesion{"
                + ", usuario='" + usuario + '\''
                + '}';
    }
}
