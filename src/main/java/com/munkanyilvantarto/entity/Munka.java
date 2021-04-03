package com.munkanyilvantarto.entity;

import java.util.Date;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name="munkak")
public class Munka {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne( cascade = CascadeType.ALL , fetch = FetchType.EAGER ) //Kapcsolat megadása
	@JoinColumn(name="kotes_id")
	private UsersProjektek userpro;
	
	@Column( nullable=false, columnDefinition="Text" )
	private String munkaleiras;
	
	@Column( nullable=false, columnDefinition="Date" )
	private String datum;
	
	@Column( nullable=false )
	private Float raforditas;

	@Column( nullable=true )
	private String megjegyzes;
	
	@ManyToOne( cascade = CascadeType.ALL , fetch = FetchType.EAGER ) //Kapcsolat megadása
	private Tipus tipus;
	
	@JsonBackReference
	@Temporal( TemporalType.TIMESTAMP )
	@Column( nullable=false, columnDefinition="Timestamp default CURRENT_TIMESTAMP" )
	private Date created_at;
	
	@JsonBackReference
	@Temporal( TemporalType.TIMESTAMP )
	@Column( nullable=true )
	private Date updated_at;

	public Munka() {};
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public UsersProjektek getUserpro() {
		return userpro;
	}

	public void setUserpro(UsersProjektek userpro) {
		this.userpro = userpro;
	}

	public String getMunkaleiras() {
		return munkaleiras;
	}

	public void setMunkaleiras(String munkaleiras) {
		this.munkaleiras = munkaleiras;
	}

	public String getDatum() {
		return datum;
	}

	public void setDatum(String datum) {
		this.datum = datum;
	}

	public double getRaforditas() {
		return raforditas;
	}

	public void setRaforditas(Float raforditas) {
		this.raforditas = raforditas;
	}

	public String getMegjegyzes() {
		return megjegyzes;
	}

	public void setMegjegyzes(String megjegyzes) {
		this.megjegyzes = megjegyzes;
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

	public Tipus getTipus() {
		return tipus;
	}

	public void setTipus(Tipus tipus) {
		this.tipus = tipus;
	}
	
	public String raforditasAlakitas() {
		String[] raford = String.valueOf(this.raforditas).split("\\."); 
		String kifele = "";
		if(Integer.parseInt(raford[1]) == 5) {
			kifele = raford[0] +" óra "+ ((Integer.parseInt(raford[1])*60)/100)*10 + " perc";
		}else {
			kifele = raford[0] +" óra "+ (Integer.parseInt(raford[1])*60)/100 + " perc";
		}
		return kifele;
	}

	@Override
	public String toString() {
		return "Munka [id=" + id + ", userpro=" + userpro + ", munkaleiras=" + munkaleiras + ", datum=" + datum
				+ ", raforditas=" + raforditas + ", megjegyzes=" + megjegyzes + ", tipus=" + tipus + ", created_at="
				+ created_at + ", updated_at=" + updated_at + "]";
	}
	
}
