package com.munkanyilvantarto.service;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import com.munkanyilvantarto.entity.UsersProjektek;

public interface UsersProjektekService {

	public ArrayList<UsersProjektek> getAllKotes();
	
	public Set<UsersProjektek> getAllProjektByUserId(Long id);
	
	public UsersProjektek findUserById(Long id);
	
	public boolean save(Map<String, String> ugypro);
	
	public void deleteById(Long id);
	
	public boolean existsById(Long id);
	
	public UsersProjektek findByUserIdAndProjektId(Long userid, Long proid);
	
	public boolean existsByUserIdAndProjektId(Long userid, Long proid);
	
	public Set<UsersProjektek> findByUserId(Long id);
	
}
