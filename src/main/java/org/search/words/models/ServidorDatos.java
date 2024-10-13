/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.search.words.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

/**
 *
 * @author ORIGAMI
 */
public class ServidorDatos implements Serializable {

    private Long id;
    private Long personaID;
    private String identificacion;
    private String servidor;
    private String nombres;
    private String apellidos;
    private String titulo;
    private String cargo;
    private Long cargoID;
    private Long unidadAdministrativaID;
    private String unidadAdministrativa;
    private String correoPersonal;
    private String correoInstitucional;
    private String emailInstitucion;
    private String telefonoInstitucional;
    private String extensionTelefonica;
    private String fechaCumpleanio;
    private Boolean cumpleanio;
    private Long escalaSalarialID;
    private Integer anioLaborado;
    private Integer mesLaborado;
    private BigDecimal derechoVacacion;
    private Date ingreso;
    //para biometricos
    private String programa;
    private String centroCosto;
    private String codBiometrico;

    public ServidorDatos() {
    }

    public ServidorDatos(Long id, String identificacion, String servidor) {
        this.id = id;
        this.identificacion = identificacion;
        this.servidor = servidor;
    }

    public ServidorDatos(Long id, String servidor, String titulo, String cargo) {
        this.id = id;
        this.servidor = servidor;
        this.titulo = titulo;
        this.cargo = cargo;
    }

    public ServidorDatos(Long id, String servidor, String titulo, String cargo, String emailInstitucion) {
        this.id = id;
        this.servidor = servidor;
        this.titulo = titulo;
        this.cargo = cargo;
        this.emailInstitucion = emailInstitucion;
    }

    public ServidorDatos(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPersonaID() {
        return personaID;
    }

    public void setPersonaID(Long personaID) {
        this.personaID = personaID;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public String getServidor() {
        return servidor;
    }

    public void setServidor(String servidor) {
        this.servidor = servidor;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public Long getUnidadAdministrativaID() {
        return unidadAdministrativaID;
    }

    public void setUnidadAdministrativaID(Long unidadAdministrativaID) {
        this.unidadAdministrativaID = unidadAdministrativaID;
    }

    public String getUnidadAdministrativa() {
        return unidadAdministrativa;
    }

    public void setUnidadAdministrativa(String unidadAdministrativa) {
        this.unidadAdministrativa = unidadAdministrativa;
    }

    public String getCorreoPersonal() {
        return correoPersonal;
    }

    public void setCorreoPersonal(String correoPersonal) {
        this.correoPersonal = correoPersonal;
    }

    public String getCorreoInstitucional() {
        return correoInstitucional;
    }

    public void setCorreoInstitucional(String correoInstitucional) {
        this.correoInstitucional = correoInstitucional;
    }

    public String getNombresCompletos() {
        String nombresCompletos = "";
        if (apellidos != null) {
            nombresCompletos = apellidos;
        }
        if (nombres != null) {
            nombresCompletos = nombresCompletos.strip().concat(" ").concat(nombres.strip());
        }

        return nombresCompletos.strip();
    }

    public String getEmailInstitucion() {
        return emailInstitucion;
    }

    public void setEmailInstitucion(String emailInstitucion) {
        this.emailInstitucion = emailInstitucion;
    }

    public String getTelefonoInstitucional() {
        return telefonoInstitucional;
    }

    public void setTelefonoInstitucional(String telefonoInstitucional) {
        this.telefonoInstitucional = telefonoInstitucional;
    }

    public String getExtensionTelefonica() {
        return extensionTelefonica;
    }

    public void setExtensionTelefonica(String extensionTelefonica) {
        this.extensionTelefonica = extensionTelefonica;
    }

    public String getFechaCumpleanio() {
        return fechaCumpleanio;
    }

    public void setFechaCumpleanio(String fechaCumpleanio) {
        this.fechaCumpleanio = fechaCumpleanio;
    }

    public Boolean getCumpleanio() {
        return cumpleanio;
    }

    public void setCumpleanio(Boolean cumpleanio) {
        this.cumpleanio = cumpleanio;
    }

    public Long getCargoID() {
        return cargoID;
    }

    public void setCargoID(Long cargoID) {
        this.cargoID = cargoID;
    }

    public Long getEscalaSalarialID() {
        return escalaSalarialID;
    }

    public void setEscalaSalarialID(Long escalaSalarialID) {
        this.escalaSalarialID = escalaSalarialID;
    }

    public Integer getAnioLaborado() {
        return anioLaborado;
    }

    public void setAnioLaborado(Integer anioLaborado) {
        this.anioLaborado = anioLaborado;
    }

    public Integer getMesLaborado() {
        return mesLaborado;
    }

    public void setMesLaborado(Integer mesLaborado) {
        this.mesLaborado = mesLaborado;
    }

    public BigDecimal getDerechoVacacion() {
        return derechoVacacion;
    }

    public void setDerechoVacacion(BigDecimal derechoVacacion) {
        this.derechoVacacion = derechoVacacion;
    }

    public Date getIngreso() {
        return ingreso;
    }

    public void setIngreso(Date ingreso) {
        this.ingreso = ingreso;
    }

    public String getPrograma() {
        return programa;
    }

    public void setPrograma(String programa) {
        this.programa = programa;
    }

    public String getCentroCosto() {
        return centroCosto;
    }

    public void setCentroCosto(String centroCosto) {
        this.centroCosto = centroCosto;
    }

    public String getCodBiometrico() {
        return codBiometrico;
    }

    public void setCodBiometrico(String codBiometrico) {
        this.codBiometrico = codBiometrico;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.personaID);
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
        final ServidorDatos other = (ServidorDatos) obj;
        if (!Objects.equals(this.personaID, other.personaID)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ServidorDatos{" + "id=" + id + ", personaID=" + personaID + ", identificacion=" + identificacion + ", servidor=" + servidor + ", nombres=" + nombres + ", apellidos=" + apellidos + ", titulo=" + titulo + ", cargo=" + cargo + ", unidadAdministrativaID=" + unidadAdministrativaID + ", unidadAdministrativa=" + unidadAdministrativa + ", correoPersonal=" + correoPersonal + ", correoInstitucional=" + correoInstitucional + '}';
    }
}
