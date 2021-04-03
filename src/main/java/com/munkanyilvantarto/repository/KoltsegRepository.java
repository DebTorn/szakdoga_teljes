package com.munkanyilvantarto.repository;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.munkanyilvantarto.entity.Koltseg;

public interface KoltsegRepository extends CrudRepository<Koltseg,Long> {

	public ArrayList<Koltseg> findAll();
	
	public Koltseg findKoltsegById(Long id);

	@Query(value = "SELECT SUM(koltseg) FROM koltsegek WHERE created_at LIKE :datum AND projekt_id = :proid",nativeQuery=true)
	public Long findSumKoltsegByCreatedAt(@Param("datum") String datum, @Param("proid") Long id);
}
