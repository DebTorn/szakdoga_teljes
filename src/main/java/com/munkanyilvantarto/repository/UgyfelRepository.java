package com.munkanyilvantarto.repository;

import java.util.ArrayList;

import org.springframework.data.repository.CrudRepository;

import com.munkanyilvantarto.entity.Ugyfel;

public interface UgyfelRepository extends CrudRepository<Ugyfel, Long> {

	public ArrayList<Ugyfel> findAll();
	
	public Ugyfel findUgyfelById(Long id);
	
	public Ugyfel save(Ugyfel ugyfel);
	
	public boolean existsByName(String name);
	
	
}
