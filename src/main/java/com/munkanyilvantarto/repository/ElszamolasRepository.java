package com.munkanyilvantarto.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.munkanyilvantarto.entity.Elszamolas;

public interface ElszamolasRepository extends CrudRepository<Elszamolas, Long>{
	
	public Elszamolas findByMunkaId(Long id);
	
}
