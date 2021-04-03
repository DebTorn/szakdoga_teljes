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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name="ugyfelek")
public class Ugyfel {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column( nullable=false, unique=true )
	private String name;
	
	@Column( columnDefinition="integer default 1", nullable=false)
	private int aktiv;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column( nullable=true )
	private Date updated_at;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column( nullable=false )
	private Date created_at;
	
	@JsonBackReference
	@OneToMany( cascade = CascadeType.ALL , fetch = FetchType.EAGER, mappedBy="ugyfel" )
	private Set<UgyfelekProjektek> ugypro = new HashSet<>();

	
	public Ugyfel() {};
	
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

	public Date getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(Date date) {
		this.updated_at = date;
	}

	public int getAktiv() {
		return aktiv;
	}

	public void setAktiv(int aktiv) {
		this.aktiv = aktiv;
	}

	public Set<UgyfelekProjektek> getUgypro() {
		return ugypro;
	}

	public void setUgypro(Set<UgyfelekProjektek> ugypro) {
		this.ugypro = ugypro;
	}
	
	public Date getCreated_at() {
		return created_at;
	}

	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}

	@Override
	public String toString() {
		return "Ugyfel [id=" + id + ", name=" + name + ", aktiv=" + aktiv + ", updated_at=" + updated_at
				+ ", created_at=" + created_at + ", ugypro=" + ugypro + "]";
	}
	
}
