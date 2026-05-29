package com.parcial.app.entidades;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "beneficios_resolucion")
public class BeneficioResolucion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String codigo;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_examen")
    private TipoExamen tipoExamen;

    @Column(name = "puntaje_minimo")
    private Integer puntajeMinimo;

    @Column(name = "puntaje_maximo")
    private Integer puntajeMaximo;

    @Column(name = "exime_informe_grado")
    private Boolean eximeInformeGrado;

    @Column(name = "exime_seminario")
    private Boolean eximeSeminario;

    @Column(name = "seminario_nivel")
    private String seminarioNivel;

    @Column(name = "nota_seminario")
    private BigDecimal notaSeminario;

    @Column(name = "beca_porcentaje")
    private Integer becaPorcentaje;

    @Column(name = "descripcion_corta")
    private String descripcionCorta;

    public enum TipoExamen { TYT, SABER_PRO }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public TipoExamen getTipoExamen() { return tipoExamen; }
    public void setTipoExamen(TipoExamen v) { this.tipoExamen = v; }
    public Integer getPuntajeMinimo() { return puntajeMinimo; }
    public void setPuntajeMinimo(Integer v) { this.puntajeMinimo = v; }
    public Integer getPuntajeMaximo() { return puntajeMaximo; }
    public void setPuntajeMaximo(Integer v) { this.puntajeMaximo = v; }
    public Boolean getEximeInformeGrado() { return eximeInformeGrado; }
    public void setEximeInformeGrado(Boolean v) { this.eximeInformeGrado = v; }
    public Boolean getEximeSeminario() { return eximeSeminario; }
    public void setEximeSeminario(Boolean v) { this.eximeSeminario = v; }
    public String getSeminarioNivel() { return seminarioNivel; }
    public void setSeminarioNivel(String v) { this.seminarioNivel = v; }
    public BigDecimal getNotaSeminario() { return notaSeminario; }
    public void setNotaSeminario(BigDecimal v) { this.notaSeminario = v; }
    public Integer getBecaPorcentaje() { return becaPorcentaje; }
    public void setBecaPorcentaje(Integer v) { this.becaPorcentaje = v; }
    public String getDescripcionCorta() { return descripcionCorta; }
    public void setDescripcionCorta(String v) { this.descripcionCorta = v; }
}