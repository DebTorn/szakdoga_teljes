package com.munkanyilvantarto.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name="users_projektek")
public class UsersProjektek {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@JsonBackReference
	@ManyToOne( cascade = CascadeType.ALL , fetch = FetchType.EAGER ) //Kapcsolat megadása
	@JoinColumn( name="user_id" )
	private User user;
	
	@ManyToOne( cascade = CascadeType.ALL , fetch = FetchType.EAGER ) //Kapcsolat megadása
	@JoinColumn( name="projekt_id" )
	private Projekt projekt;
	
	@Column( nullable=false )
	private int oraber;
	
	/*@Column( columnDefinition="boolean default false", nullable=false)
	private int aktiv;*/
	
	public UsersProjektek() {}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Projekt getProjekt() {
		return projekt;
	}

	public void setProjekt(Projekt projekt) {
		this.projekt = projekt;
	}

	public int getOraber() {
		return oraber;
	}

	public void setOraber(int oraber) {
		this.oraber = oraber;
	}

	/*public int getAktiv() {
		return aktiv;
	}

	public void setAktiv(int aktiv) {
		this.aktiv = aktiv;
	}*/

	@Override
	public String toString() {
		return "UsersProjektek [id=" + id + ", user=" + user + ", projekt=" + projekt + ", oraber=" + oraber + "]";
	}

}
