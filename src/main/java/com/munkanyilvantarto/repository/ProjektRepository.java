package com.munkanyilvantarto.repository;

import java.util.ArrayList;
import java.util.Set;

import org.springframework.data.repository.CrudRepository;

import com.munkanyilvantarto.entity.Projekt;

public interface ProjektRepository extends CrudRepository<Projekt, Long> {

	public ArrayList<Projekt> findAll();
	
	public Set<Projekt> findNameById(Long id);
	
	public Projekt findProjektById(Long id);
	
	public boolean existsByName(String name);
	
}
