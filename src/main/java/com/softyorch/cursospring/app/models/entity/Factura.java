package com.softyorch.cursospring.app.models.entity;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "facturas")
public class Factura implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "observacion")
    private String observacion;
    @Column(name = "create_at")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createAdt;

    //Muchas facturas para un solo cliente.
    @ManyToOne(fetch = FetchType.LAZY)  //LAZY carga de datos perezosa, evita traer to.do con una consulta solo obtendrá los datos cuando se le invoque, y no antes.
    @Column(name = "cliente")
    private Cliente cliente;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "factura_id")
    private List<ItemFatura> items;

    public Factura() {
        this.items = new ArrayList<>();
    }

    @PrePersist
    public void prePersist() {
        createAdt = new Date();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Date getCreateAdt() {
        return createAdt;
    }

    public void setCreateAdt(Date createAdt) {
        this.createAdt = createAdt;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<ItemFatura> getItems() {
        return items;
    }

    public void setItems(List<ItemFatura> items) {
        this.items = items;
    }

    public void addItemFactura(ItemFatura item) {
        this.items.add(item);
    }

    private static final long serialVersionUID = 1L;

}
