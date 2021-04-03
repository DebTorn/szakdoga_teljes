package com.munkanyilvantarto.entity;

import java.sql.Timestamp;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name="elszamolasok")
public class Elszamolas {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne( cascade = CascadeType.ALL , fetch = FetchType.EAGER ) //Kapcsolat megadása
	@JoinColumn(name="munka_id")
	private Munka munka;
	
	private Long osszeg;
	
	private String datum;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Munka getMunka() {
		return munka;
	}

	public void setMunka(Munka munka) {
		this.munka = munka;
	}

	public Long getOsszeg() {
		return osszeg;
	}

	public void setOsszeg(Long osszeg) {
		this.osszeg = osszeg;
	}

	public String getDatum() {
		return datum;
	}

	public void setDatum(String datum) {
		this.datum = datum;
	}

	@Override
	public String toString() {
		return "Elszamolas [id=" + id + ", munka=" + munka + ", osszeg=" + osszeg + ", datum=" + datum + "]";
	}
	
}
