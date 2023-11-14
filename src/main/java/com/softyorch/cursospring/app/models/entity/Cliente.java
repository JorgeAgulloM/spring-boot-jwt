package com.softyorch.cursospring.app.models.entity;

import java.io.Serializable;
import java.util.Date;

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

	private static final long serialVersionUID = 1L;

}
