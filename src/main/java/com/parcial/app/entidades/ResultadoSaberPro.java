package com.parcial.app.entidades;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "resultados_saber_pro")
public class ResultadoSaberPro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estudiante_id")
    private Estudiante estudiante;

    @Column(name = "numero_registro")
    private String numeroRegistro;

    @Column(name = "puntaje_global")
    private Integer puntajeGlobal;

    @Column(name = "nivel_global")
    private String nivelGlobal;

    @Column(name = "comunicacion_escrita")
    private BigDecimal comunicacionEscrita;
    @Column(name = "comunicacion_escrita_nivel")
    private String comunicacionEscritaNivel;

    @Column(name = "razonamiento_cuantitativo")
    private BigDecimal razonamientoCuantitativo;
    @Column(name = "razon_cuant_nivel")
    private String razonCuantNivel;

    @Column(name = "lectura_critica")
    private BigDecimal lecturaCritica;
    @Column(name = "lectura_critica_nivel")
    private String lecturaCriticaNivel;

    @Column(name = "competencias_ciudadanas")
    private BigDecimal competenciasCiudadanas;
    @Column(name = "comp_ciudadanas_nivel")
    private String compCiudadanasNivel;

    private BigDecimal ingles;
    @Column(name = "ingles_nivel")
    private String inglesNivel;

    @Column(name = "nivel_ingles_certificado")
    private String nivelInglesCertificado;

    @Column(name = "formulacion_proyectos_ingenieria")
    private BigDecimal formulacionProyectosIngenieria;
    @Column(name = "form_proyectos_nivel")
    private String formProyectosNivel;

    @Column(name = "pensamiento_cientifico_matematicas")
    private BigDecimal pensamientoCientificoMatematicas;
    @Column(name = "pens_cient_nivel")
    private String pensCientNivel;

    @Column(name = "diseno_software")
    private BigDecimal disenoSoftware;
    @Column(name = "diseno_software_nivel")
    private String disenoSoftwareNivel;

    private Boolean anulado = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subido_por_usuario_id")
    private Usuario subidoPor;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Estudiante getEstudiante() { return estudiante; }
    public void setEstudiante(Estudiante e) { this.estudiante = e; }
    public String getNumeroRegistro() { return numeroRegistro; }
    public void setNumeroRegistro(String v) { this.numeroRegistro = v; }
    public Integer getPuntajeGlobal() { return puntajeGlobal; }
    public void setPuntajeGlobal(Integer v) { this.puntajeGlobal = v; }
    public String getNivelGlobal() { return nivelGlobal; }
    public void setNivelGlobal(String v) { this.nivelGlobal = v; }
    public BigDecimal getComunicacionEscrita() { return comunicacionEscrita; }
    public void setComunicacionEscrita(BigDecimal v) { this.comunicacionEscrita = v; }
    public String getComunicacionEscritaNivel() { return comunicacionEscritaNivel; }
    public void setComunicacionEscritaNivel(String v) { this.comunicacionEscritaNivel = v; }
    public BigDecimal getRazonamientoCuantitativo() { return razonamientoCuantitativo; }
    public void setRazonamientoCuantitativo(BigDecimal v) { this.razonamientoCuantitativo = v; }
    public String getRazonCuantNivel() { return razonCuantNivel; }
    public void setRazonCuantNivel(String v) { this.razonCuantNivel = v; }
    public BigDecimal getLecturaCritica() { return lecturaCritica; }
    public void setLecturaCritica(BigDecimal v) { this.lecturaCritica = v; }
    public String getLecturaCriticaNivel() { return lecturaCriticaNivel; }
    public void setLecturaCriticaNivel(String v) { this.lecturaCriticaNivel = v; }
    public BigDecimal getCompetenciasCiudadanas() { return competenciasCiudadanas; }
    public void setCompetenciasCiudadanas(BigDecimal v) { this.competenciasCiudadanas = v; }
    public String getCompCiudadanasNivel() { return compCiudadanasNivel; }
    public void setCompCiudadanasNivel(String v) { this.compCiudadanasNivel = v; }
    public BigDecimal getIngles() { return ingles; }
    public void setIngles(BigDecimal v) { this.ingles = v; }
    public String getInglesNivel() { return inglesNivel; }
    public void setInglesNivel(String v) { this.inglesNivel = v; }
    public String getNivelInglesCertificado() { return nivelInglesCertificado; }
    public void setNivelInglesCertificado(String v) { this.nivelInglesCertificado = v; }
    public BigDecimal getFormulacionProyectosIngenieria() { return formulacionProyectosIngenieria; }
    public void setFormulacionProyectosIngenieria(BigDecimal v) { this.formulacionProyectosIngenieria = v; }
    public String getFormProyectosNivel() { return formProyectosNivel; }
    public void setFormProyectosNivel(String v) { this.formProyectosNivel = v; }
    public BigDecimal getPensamientoCientificoMatematicas() { return pensamientoCientificoMatematicas; }
    public void setPensamientoCientificoMatematicas(BigDecimal v) { this.pensamientoCientificoMatematicas = v; }
    public String getPensCientNivel() { return pensCientNivel; }
    public void setPensCientNivel(String v) { this.pensCientNivel = v; }
    public BigDecimal getDisenoSoftware() { return disenoSoftware; }
    public void setDisenoSoftware(BigDecimal v) { this.disenoSoftware = v; }
    public String getDisenoSoftwareNivel() { return disenoSoftwareNivel; }
    public void setDisenoSoftwareNivel(String v) { this.disenoSoftwareNivel = v; }
    public Boolean getAnulado() { return anulado; }
    public void setAnulado(Boolean v) { this.anulado = v; }
    public Usuario getSubidoPor() { return subidoPor; }
    public void setSubidoPor(Usuario v) { this.subidoPor = v; }
}