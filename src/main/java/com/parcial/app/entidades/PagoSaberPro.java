package com.parcial.app.entidades;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "pagos_saber_pro")
public class PagoSaberPro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estudiante_id")
    private Estudiante estudiante;

    @Column(name = "archivo_path")
    private String archivoPath;

    @Column(name = "nombre_archivo")
    private String nombreArchivo;

    @Enumerated(EnumType.STRING)
    private Estado estado = Estado.PENDIENTE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "verificado_por")
    private Usuario verificadoPor;

    @Column(name = "fecha_verificacion")
    private LocalDate fechaVerificacion;

    private String observaciones;

    public enum Estado { PENDIENTE, VERIFICADO, RECHAZADO }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Estudiante getEstudiante() { return estudiante; }
    public void setEstudiante(Estudiante e) { this.estudiante = e; }
    public String getArchivoPath() { return archivoPath; }
    public void setArchivoPath(String v) { this.archivoPath = v; }
    public String getNombreArchivo() { return nombreArchivo; }
    public void setNombreArchivo(String v) { this.nombreArchivo = v; }
    public Estado getEstado() { return estado; }
    public void setEstado(Estado v) { this.estado = v; }
    public Usuario getVerificadoPor() { return verificadoPor; }
    public void setVerificadoPor(Usuario v) { this.verificadoPor = v; }
    public LocalDate getFechaVerificacion() { return fechaVerificacion; }
    public void setFechaVerificacion(LocalDate v) { this.fechaVerificacion = v; }
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String v) { this.observaciones = v; }
}
