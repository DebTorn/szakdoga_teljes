package com.munkanyilvantarto.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="roles")
public class Role {

	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Id
	private Long id;
	
	@Column(unique=true, nullable=false)
	private String name;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column( nullable=false, columnDefinition="Timestamp default CURRENT_TIMESTAMP" )
	private Date created_at;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column( nullable=true )
	private Date updated_at;
	
	@ManyToMany( mappedBy="roles" )
	private Set<User> users = new HashSet<>();
	
	public Role() {}
	
	public Role(String name) {
		this.name = name;
	}

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

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	@Override
	public String toString() {
		return "Role [id=" + id + ", name=" + name + ", created_at=" + created_at + ", updated_at=" + updated_at
				+ ", users=" + users + "]";
	}
	
}
