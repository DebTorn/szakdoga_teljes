package com.munkanyilvantarto.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name="users")
public class User {
	
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Id
	private Long id;
	
	@Column( unique=true , nullable=false)
	private String email;
	
	@JsonBackReference
	@Column( nullable=false )
	private String password;
	
	@Column( nullable=false )
	private String teljesnev;
	
	@Column( nullable=false )
	private double bruttoszorzo;
	
	@Column( columnDefinition="integer default 0", nullable=false)
	private int aktiv;
	
	@Column( nullable=false )
	private Long alaporaber;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column( nullable=true )
	private Date created_at;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column( nullable=true )
	private Date updated_at;

	@JsonBackReference
	@ManyToMany( cascade = CascadeType.ALL , fetch = FetchType.EAGER ) //Kapcsolat megadása
	@JoinTable(
			name = "users_roles", //kötés tábla neve
			joinColumns = {
					@JoinColumn(name = "users_id")
					}, //kötés tábla első oszlopa (user kötés)
			inverseJoinColumns = {
					@JoinColumn(name = "roles_id"),
					} //kötés tábla második oszlopa (role kötés)
			)
	private Set<Role> roles = new HashSet<>();
	
	@JsonManagedReference
	@OneToMany( cascade = CascadeType.ALL , fetch = FetchType.EAGER, mappedBy="user" )
	private Set<UsersProjektek> userpro = new HashSet<>();
	
	@Column( nullable=true )
	private String token;
	
	public User() {};
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getTeljesnev() {
		return teljesnev;
	}

	public void setTeljesnev(String teljesnev) {
		this.teljesnev = teljesnev;
	}

	public double getBruttoszorzo() {
		return bruttoszorzo;
	}

	public void setBruttoszorzo(double bruttoszorzo) {
		this.bruttoszorzo = bruttoszorzo;
	}

	public Long getAlaporaber() {
		return alaporaber;
	}

	public void setAlaporaber(Long alaporaber) {
		this.alaporaber = alaporaber;
	}

	public Date getCreated_at() {
		return created_at;
	}

	public void setCreated_at(Date date) {
		this.created_at = date;
	}

	public Date getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(Date date) {
		this.updated_at = date;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public Set<UsersProjektek> getUserpro() {
		return userpro;
	}

	public void setUserpro(Set<UsersProjektek> userpro) {
		this.userpro = userpro;
	}

	public void setRole(String name) {
		if(this.roles == null || this.roles.isEmpty())
			this.roles = new HashSet<>();
		this.roles.add(new Role(name));
	}

	public void setAktiv(int aktiv) {
		this.aktiv = aktiv;
	}
	
	public void setToken(String authToken) {
		this.token = authToken;
	}
	
	public int getAktiv() {
		return aktiv;
	}
	
	public String getToken() {
		return token;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", email=" + email + ", password=" + password + ", teljesnev=" + teljesnev
				+ ", bruttoszorzo=" + bruttoszorzo + ", aktiv=" + aktiv + ", alaporaber=" + alaporaber + ", created_at="
				+ created_at + ", updated_at=" + updated_at + ", roles=" + roles + ", userpro=" + userpro + ", token="
				+ token + "]";
	}
	
}
