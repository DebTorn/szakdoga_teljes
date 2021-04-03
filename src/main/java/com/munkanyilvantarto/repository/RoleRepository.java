package com.munkanyilvantarto.repository;

import org.springframework.data.repository.CrudRepository;

import com.munkanyilvantarto.entity.Role;

public interface RoleRepository extends CrudRepository<Role, Long> {

	Role findByName(String name);
	
}
