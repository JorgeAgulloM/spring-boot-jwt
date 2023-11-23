package com.softyorch.cursospring.app.models.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "facturas_items")
public class ItemFatura implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cantidad")
    private Integer cantidad;

    @JoinColumn(name = "producto_id")
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) //Tambien se puede poner sobre la clase Producto. Evita los objetos insertados por proxy al añadirlos al array.
    private Producto producto;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Double calculateImport() {
        return cantidad.doubleValue() * producto.getPrecio();
    }

    public Producto getProducto() {return producto;}

    public void setProducto(Producto producto) {this.producto = producto;}

    private static final long serialVersionUID = 1L;
}
