package com.munkanyilvantarto.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="koltsegek")
public class Koltseg {

	@Id
	@GeneratedValue
	private Long id;
	
	@ManyToOne
	private Projekt projekt;
	
	@Column( nullable=false )
	private String megnevezes;
	
	@Column( nullable=false )
	private Long koltseg;
	
	@Temporal( TemporalType.TIMESTAMP )
	@Column( nullable=false )
	private Date created_at;
	
	@Temporal( TemporalType.TIMESTAMP )
	@Column( nullable=true )
	private Date updated_at;
	
	public Koltseg() {}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Projekt getProjekt() {
		return projekt;
	}

	public void setProjekt(Projekt projekt) {
		this.projekt = projekt;
	}

	public String getMegnevezes() {
		return megnevezes;
	}

	public void setMegnevezes(String megnevezes) {
		this.megnevezes = megnevezes;
	}

	public Long getKoltseg() {
		return koltseg;
	}

	public void setKoltseg(Long koltseg) {
		this.koltseg = koltseg;
	}

	public Date getCreated_at() {
		return created_at;
	}

	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}

	public Date getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(Date updated_at) {
		this.updated_at = updated_at;
	}

	@Override
	public String toString() {
		return "Koltseg [id=" + id + ", projekt=" + projekt + ", megnevezes=" + megnevezes + ", koltseg=" + koltseg
				+ ", created_at=" + created_at + ", updated_at=" + updated_at + "]";
	};
	
}
