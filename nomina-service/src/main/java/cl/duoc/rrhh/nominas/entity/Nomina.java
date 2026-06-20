package cl.duoc.rrhh.nominas.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "nominas", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"empleadoId", "periodo"})
})
public class Nomina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long empleadoId;

    @Column(nullable = false, length = 7)
    private String periodo;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal sueldoBase;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal bono;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal descuento;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal sueldoLiquido;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    private EstadoPago estadoPago;

    private LocalDate fechaPago;

    public Nomina() {
    }

    public Nomina(Long id, Long empleadoId, String periodo, BigDecimal sueldoBase, BigDecimal bono,
                  BigDecimal descuento, BigDecimal sueldoLiquido, EstadoPago estadoPago, LocalDate fechaPago) {
        this.id = id;
        this.empleadoId = empleadoId;
        this.periodo = periodo;
        this.sueldoBase = sueldoBase;
        this.bono = bono;
        this.descuento = descuento;
        this.sueldoLiquido = sueldoLiquido;
        this.estadoPago = estadoPago;
        this.fechaPago = fechaPago;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEmpleadoId() {
        return empleadoId;
    }

    public void setEmpleadoId(Long empleadoId) {
        this.empleadoId = empleadoId;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public BigDecimal getSueldoBase() {
        return sueldoBase;
    }

    public void setSueldoBase(BigDecimal sueldoBase) {
        this.sueldoBase = sueldoBase;
    }

    public BigDecimal getBono() {
        return bono;
    }

    public void setBono(BigDecimal bono) {
        this.bono = bono;
    }

    public BigDecimal getDescuento() {
        return descuento;
    }

    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento;
    }

    public BigDecimal getSueldoLiquido() {
        return sueldoLiquido;
    }

    public void setSueldoLiquido(BigDecimal sueldoLiquido) {
        this.sueldoLiquido = sueldoLiquido;
    }

    public EstadoPago getEstadoPago() {
        return estadoPago;
    }

    public void setEstadoPago(EstadoPago estadoPago) {
        this.estadoPago = estadoPago;
    }

    public LocalDate getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(LocalDate fechaPago) {
        this.fechaPago = fechaPago;
    }
}
