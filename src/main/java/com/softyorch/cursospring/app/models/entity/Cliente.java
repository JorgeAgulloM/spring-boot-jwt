package com.softyorch.cursospring.app.models.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "clientes")
public class Cliente implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "nombre")
	@NotEmpty
	@Size(min=4, max=45) //Nombre más largo del mundo en 2023 43 caracteres-> Brhadaranyakopanishadvivekachudamani Erreh
	private String nombre;

	@Column(name = "apellido")
	@NotEmpty
	@Size(min=4, max=35) //Apellido más largo del mundo en 2023 35 caracteres-> Keihanaikukauakahihuliheekahaunaele
	private String apellido;

	@Column(name = "email")
	@NotEmpty
	@Email
	private String email;

	@Column(name = "create_at")
	@NotNull
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date createAt;

	@OneToMany( // Un cliente muchas facturas
			mappedBy = "cliente", //Mapeo por el atributo de la otra clase de la realación. Se consigue que cliente tenga vista de las faturas pero las facturas solo de su cliente.
			fetch = FetchType.LAZY, //LAZY carga de datos perezosa, evita traer to.do con una consulta solo obtendrá los datos cuando se le invoque, y no antes.
			cascade = CascadeType.ALL, //Cascade.ALL La persistencia y la eliminación irán en cascada.
			orphanRemoval = true
	)
	private List<Factura> facturas;

	public Cliente() {
		facturas = new ArrayList<>();
	}

	@Column(name = "photo")
	private String photo;

	public void addFatura(Factura factura) {
		facturas.add(factura);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public List<Factura> getFacturas() {
		return facturas;
	}

	public void setFacturas(List<Factura> facturas) {
		this.facturas = facturas;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	private static final long serialVersionUID = 1L;

}
