package com.munkanyilvantarto.repository;

import java.util.ArrayList;
import java.util.Set;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.munkanyilvantarto.entity.UsersProjektek;

public interface UsersProjektekRepository extends CrudRepository<UsersProjektek, Long>{

	public ArrayList<UsersProjektek> findAll();
	
	public Set<UsersProjektek> findByUserId(Long id);
	
	public UsersProjektek findUserById(Long id);
	
	public UsersProjektek save(UsersProjektek userpro);
	
	@Modifying
	@Transactional
	@Query(value = "DELETE FROM users_projektek WHERE id = :id", nativeQuery=true)
	public void deleteRow(@Param("id") Long id);

	public UsersProjektek findByUserIdAndProjektId(Long userid, Long proid);
	
	public boolean existsByUserIdAndProjektId(Long userid, Long proid);
	
}
