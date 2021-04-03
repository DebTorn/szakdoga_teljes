package com.munkanyilvantarto.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="ugyfelek_projektek")
public class UgyfelekProjektek {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne //Kapcsolat megadása
	@JoinColumn(name="ugyfel_id")
	private Ugyfel ugyfel;
	
	@ManyToOne //Kapcsolat megadása
	@JoinColumn(name="projekt_id")
	private Projekt projekt;
	
	@Column( nullable=false )
	private int oraber;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Ugyfel getUgyfel() {
		return ugyfel;
	}

	public void setUgyfel(Ugyfel ugyfel) {
		this.ugyfel = ugyfel;
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

	@Override
	public String toString() {
		return "UgyfelekProjektek [id=" + id + ", ugyfel=" + ugyfel + ", projekt=" + projekt + ", oraber=" + oraber
				+ "]";
	}
	
}
