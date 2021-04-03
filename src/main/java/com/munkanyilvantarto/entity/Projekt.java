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
@Table(name="projektek")
public class Projekt {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Long id;
	
	@Column( unique=true, nullable=false )
	public String name;
	
	@Column( nullable=false )
	public String leiras;
	
	@Column( nullable=false )
	public Long tervezettoraber;
	
	@Column( nullable=false )
	public int status;
	
	@Column( nullable=false )
	public int type;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column( nullable=false )
	public Date created_at;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column( nullable=true )
	private Date updated_at;
	
	@JsonBackReference
	@OneToMany( cascade = CascadeType.ALL , fetch = FetchType.EAGER, mappedBy="projekt" )
	private Set<UsersProjektek> userpro = new HashSet<>();
	
	@JsonBackReference
	@OneToMany( mappedBy="projekt" )
	private Set<UgyfelekProjektek> ugypro = new HashSet<>();
	
	@JsonBackReference
	@OneToMany( mappedBy="projekt" )
	private Set<Koltseg> koltseg = new HashSet<>();
	
	public Projekt() {}

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

	public String getLeiras() {
		return leiras;
	}

	public void setLeiras(String leiras) {
		this.leiras = leiras;
	}

	public Long getTervezettoraber() {
		return tervezettoraber;
	}

	public void setTervezettoraber(Long tervezettoraber) {
		this.tervezettoraber = tervezettoraber;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
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
	
//	public Set<UsersProjektek> getUserpro() {
//		return userpro;
//	}
//
//	public void setUserpro(Set<UsersProjektek> userpro) {
//		this.userpro = userpro;
//	}

	@Override
	public String toString() {
		return "Projekt [id=" + id + ", name=" + name + ", leiras=" + leiras + ", tervezettoraber=" + tervezettoraber
				+ ", status=" + status + ", type=" + type + ", created_at=" + created_at + ", updated_at=" + updated_at
				+ ", userpro=" + userpro + ", ugypro=" + ugypro + "]";
	};
	
}
