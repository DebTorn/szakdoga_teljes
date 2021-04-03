package com.munkanyilvantarto.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.munkanyilvantarto.entity.Munka;

public interface MunkaRepository extends CrudRepository<Munka,Long>{
	
	public ArrayList<Munka> findAll();
	
	public Munka findMunkaById(Long id);
	
	@Modifying
	@Transactional
	@Query(value = "DELETE FROM munkak WHERE id = :id", nativeQuery=true)
	public void deleteRow(@Param("id") Long id);
	
	@Query(value = "SELECT * FROM munkak WHERE kotes_id = :id", nativeQuery=true)
	public ArrayList<Munka> findByKotesId(@Param("id") Long id);
	
	@Query(value = "SELECT EXTRACT(YEAR FROM datum) FROM munkak INNER JOIN users_projektek ON users_projektek.id = munkak.kotes_id WHERE users_projektek.projekt_id = :id GROUP BY EXTRACT(YEAR FROM datum)", nativeQuery=true)
	public Set<Integer> findProjektEvByProjektId(@Param("id") Long id);
	
	@Query(value = "SELECT EXTRACT(MONTH FROM datum) FROM munkak INNER JOIN users_projektek ON users_projektek.id = munkak.kotes_id WHERE users_projektek.projekt_id = :id AND EXTRACT(YEAR FROM datum) LIKE :ev GROUP BY EXTRACT(MONTH FROM datum)", nativeQuery=true)
	public Set<Integer> findProjektHonapByProjektIdAndProjektEv(@Param("id") Long id, @Param("ev") String ev);
	
	@Query(value = "SELECT SUM(raforditas) FROM munkak INNER JOIN users_projektek ON users_projektek.id = munkak.kotes_id WHERE users_projektek.projekt_id = :id AND EXTRACT(YEAR FROM datum) LIKE :ev AND EXTRACT(MONTH FROM datum) LIKE :honap", nativeQuery=true)
	public Double findSumRaforditasByProjektIdAndEvAndHonap(@Param("id") Long id, @Param("ev") String ev, @Param("honap") String honap);
	
	@Query(value = "SELECT users.teljesnev as 'dolgozo', projektek.name as 'projekt', ROUND((SUM(munkak.raforditas)*users_projektek.oraber), 0) as 'netto', ROUND(((SUM(munkak.raforditas)*users_projektek.oraber)*users.bruttoszorzo), 0) as 'brutto' FROM munkak INNER JOIN users_projektek ON users_projektek.id = munkak.kotes_id INNER JOIN users ON users_projektek.user_id = users.id INNER JOIN projektek ON users_projektek.projekt_id = projektek.id WHERE users_projektek.projekt_id = :pid AND munkak.datum LIKE :datum GROUP BY users.teljesnev, users_projektek.oraber, users.bruttoszorzo", nativeQuery=true)
	public Map<String,String> elszamTablaDolgozoOsszesito(@Param("pid") Long id, @Param("datum") String datum); 
	
	public ArrayList<Munka> findByDatum(String datum);
	
	@Query(value = "SELECT EXTRACT(YEAR FROM munkak.datum) FROM munkak INNER JOIN users_projektek ON users_projektek.id = munkak.kotes_id WHERE users_projektek.user_id = :id GROUP BY EXTRACT(YEAR FROM munkak.datum)", nativeQuery=true)
	public ArrayList<String> findEvByUserId(@Param("id") Long id);
	
	@Query(value = "SELECT EXTRACT(MONTH FROM munkak.datum) FROM munkak INNER JOIN users_projektek ON users_projektek.id = munkak.kotes_id WHERE users_projektek.user_id = :id AND EXTRACT(YEAR FROM munkak.datum) LIKE :ev GROUP BY EXTRACT(MONTH FROM munkak.datum)", nativeQuery=true)
	public ArrayList<String> findHonapByUserId(@Param("id") Long id, @Param("ev") String ev);
	
	@Query(value = "SELECT created_at FROM munkak WHERE id = :id",nativeQuery=true)
	public Date findCreatedAtById(@Param("id") Long id);
}
