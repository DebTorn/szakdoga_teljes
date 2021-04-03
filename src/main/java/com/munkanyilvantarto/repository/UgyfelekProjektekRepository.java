package com.munkanyilvantarto.repository;

import java.util.ArrayList;
import java.util.Set;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.munkanyilvantarto.entity.UgyfelekProjektek;

public interface UgyfelekProjektekRepository extends CrudRepository<UgyfelekProjektek, Long> {

	public boolean existsByUgyfelIdAndProjektId(Long ugyid, Long proid);
	
	public ArrayList<UgyfelekProjektek> findAll();
	
	public Set<UgyfelekProjektek> findByUgyfelId(Long id);
	
	public UgyfelekProjektek save(UgyfelekProjektek ugypro);
	
	public UgyfelekProjektek findUgyfelekProjektekById(Long id);
	
	@Query(value="SELECT oraber FROM ugyfelek_projektek WHERE projekt_id = :proid AND ugyfel_id = :ugyid", nativeQuery=true)
	public Long findOrbarByProjektIdAndUgyfelId(@Param("proid") Long id, @Param("ugyid") Long ugyid);
	
	@Modifying
	@Transactional
	@Query(value = "DELETE FROM ugyfelek_projektek WHERE id = :id", nativeQuery=true)
	public void deleteRow(@Param("id") Long id);
	
}
