package com.parcial.app.entidades;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "estudiantes")
public class Estudiante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_documento")
    private TipoDocumento tipoDocumento = TipoDocumento.CC;

    @NotBlank(message = "La cédula es obligatoria")
    private String cedula;

    @NotBlank(message = "El primer nombre es obligatorio")
    @Column(name = "primer_nombre")
    private String primerNombre;

    @Column(name = "segundo_nombre")
    private String segundoNombre;

    @NotBlank(message = "El primer apellido es obligatorio")
    @Column(name = "primer_apellido")
    private String primerApellido;

    @Column(name = "segundo_apellido")
    private String segundoApellido;
    
    @ManyToOne
    @JoinColumn(name = "facultad_id")
    private Facultad facultad;

    @Email @NotBlank(message = "El correo es obligatorio")
    private String correo;

    private String telefono;

    private String contrasena;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "programa_id")
    private ProgramaAcademico programa;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_examen")
    private TipoExamen tipoExamen;

    @Column(name = "aprobado_saberpro")
    private Boolean aprobadoSaberpro = false;

    @Column(name = "comprobante_pago")
    private String comprobantePago;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creado_por")
    private Usuario creadoPor;

    private Boolean activo = true;

    public enum TipoDocumento { CC, TI, CE, PA }
    public enum TipoExamen { TYT, SABER_PRO }

    // Getter helper para nombre completo
    public String getNombreCompleto() {
        return primerNombre + " "
            + (segundoNombre != null ? segundoNombre + " " : "")
            + primerApellido + " "
            + (segundoApellido != null ? segundoApellido : "");
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public TipoDocumento getTipoDocumento() { return tipoDocumento; }
    public void setTipoDocumento(TipoDocumento tipoDocumento) { this.tipoDocumento = tipoDocumento; }
    public String getCedula() { return cedula; }
    public void setCedula(String cedula) { this.cedula = cedula; }
    public String getPrimerNombre() { return primerNombre; }
    public void setPrimerNombre(String primerNombre) { this.primerNombre = primerNombre; }
    public String getSegundoNombre() { return segundoNombre; }
    public void setSegundoNombre(String segundoNombre) { this.segundoNombre = segundoNombre; }
    public String getPrimerApellido() { return primerApellido; }
    public void setPrimerApellido(String primerApellido) { this.primerApellido = primerApellido; }
    public String getSegundoApellido() { return segundoApellido; }
    public void setSegundoApellido(String segundoApellido) { this.segundoApellido = segundoApellido; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }
    public ProgramaAcademico getPrograma() { return programa; }
    public void setPrograma(ProgramaAcademico programa) { this.programa = programa; }
    public TipoExamen getTipoExamen() { return tipoExamen; }
    public void setTipoExamen(TipoExamen tipoExamen) { this.tipoExamen = tipoExamen; }
    public Boolean getAprobadoSaberpro() { return aprobadoSaberpro; }
    public void setAprobadoSaberpro(Boolean aprobadoSaberpro) { this.aprobadoSaberpro = aprobadoSaberpro; }
    public String getComprobantePago() { return comprobantePago; }
    public void setComprobantePago(String comprobantePago) { this.comprobantePago = comprobantePago; }
    public Usuario getCreadoPor() { return creadoPor; }
    public void setCreadoPor(Usuario creadoPor) { this.creadoPor = creadoPor; }
    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }
    public Facultad getFacultad() {
        return facultad;
    }

    public void setFacultad(Facultad facultad) {
        this.facultad = facultad;
    }
}