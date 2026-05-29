package com.parcial.app.entidades;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "resultados_tyt")
public class ResultadoTyt {

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

    private Boolean anulado = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subido_por_usuario_id")
    private Usuario subidoPor;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Estudiante getEstudiante() { return estudiante; }
    public void setEstudiante(Estudiante estudiante) { this.estudiante = estudiante; }
    public String getNumeroRegistro() { return numeroRegistro; }
    public void setNumeroRegistro(String numeroRegistro) { this.numeroRegistro = numeroRegistro; }
    public Integer getPuntajeGlobal() { return puntajeGlobal; }
    public void setPuntajeGlobal(Integer puntajeGlobal) { this.puntajeGlobal = puntajeGlobal; }
    public String getNivelGlobal() { return nivelGlobal; }
    public void setNivelGlobal(String nivelGlobal) { this.nivelGlobal = nivelGlobal; }
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
    public Boolean getAnulado() { return anulado; }
    public void setAnulado(Boolean anulado) { this.anulado = anulado; }
    public Usuario getSubidoPor() { return subidoPor; }
    public void setSubidoPor(Usuario subidoPor) { this.subidoPor = subidoPor; }
}