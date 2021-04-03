package com.munkanyilvantarto.repository;

import java.util.ArrayList;

import org.springframework.data.repository.CrudRepository;

import com.munkanyilvantarto.entity.Tipus;

public interface TipusRepository extends CrudRepository<Tipus, Long>{

	public Tipus findTipusById(Long id);
	
	public ArrayList<Tipus> findAll();
	
}
