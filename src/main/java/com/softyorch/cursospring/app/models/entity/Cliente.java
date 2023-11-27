package com.softyorch.cursospring.app.models.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "clientes")
public class Cliente implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "nombre")
	@NotEmpty
	@Size(min=4, max=45) //Nombre más largo del mundo en 2023 43 caracteres-> Brhadaranyakopanishadvivekachudamani Erreh
	private String name;

	@Column(name = "apellido")
	@NotEmpty
	@Size(min=4, max=35) //Apellido más largo del mundo en 2023 35 caracteres-> Keihanaikukauakahihuliheekahaunaele
	private String surname;

	@Column(name = "email")
	@NotEmpty
	@Email
	private String email;

	@Column(name = "create_at")
	@NotNull
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createAt;

	@PrePersist
	public void prePersist() {
		createAt = new Date();
	}

	@OneToMany( // Un cliente muchas facturas
			mappedBy = "cliente", //Mapeo por el atributo de la otra clase de la realación. Se consigue que cliente tenga vista de las faturas pero las facturas solo de su cliente.
			fetch = FetchType.LAZY, //LAZY carga de datos perezosa, evita traer to.do con una consulta solo obtendrá los datos cuando se le invoque, y no antes.
			cascade = CascadeType.ALL, //Cascade.ALL La persistencia y la eliminación irán en cascada.
			orphanRemoval = true
	)
	@JsonManagedReference
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
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

	@Override
	public String toString() {
		return  name + " " + surname;
	}
}
