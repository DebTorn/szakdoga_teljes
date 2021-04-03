package com.munkanyilvantarto.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="kerelmek")
public class Kerelem {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	private String email;
	
	@Column( nullable=false )
	private String tipus;
	
	@Column( columnDefinition="text default NULL", nullable=true)
	private String token;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column( nullable=false, columnDefinition="Timestamp default CURRENT_TIMESTAMP")
	private Date created_at;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column( nullable=true, columnDefinition="Timestamp default CURRENT_TIMESTAMP" )
	private Date updated_at;

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

	public String getTipus() {
		return tipus;
	}

	public void setTipus(String tipus) {
		this.tipus = tipus;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
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
		return "Kerelem [id=" + id + ", email=" + email + ", tipus=" + tipus + ", token=" + token + ", created_at="
				+ created_at + ", updated_at=" + updated_at + "]";
	}
	
}
