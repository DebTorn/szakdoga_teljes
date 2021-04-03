package com.munkanyilvantarto.service;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import com.munkanyilvantarto.entity.Projekt;

public interface ProjektService {

	public ArrayList<Projekt> findAll();
	
	public Set<Projekt> findNameById(Long id);
	
	public boolean save(Map<String, String> projekt);
	
	public boolean update(Map<String, String> allRequestDatas);
	
	public Projekt findById(Long id);
	
	public boolean existsById(Long id);
	
	public boolean existsByName(String name);
	
}
