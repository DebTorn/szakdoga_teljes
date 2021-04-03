package com.munkanyilvantarto.service;

import java.util.ArrayList;
import java.util.Map;

import com.munkanyilvantarto.entity.Ugyfel;

public interface UgyfelService {

	public ArrayList<Ugyfel> ugyfelek();
	
	public boolean save(Map<String,String> ugyfel);
	
	public boolean update(Map<String, String> ugyfel);
	
	public boolean aktivitas(Map<String, String> ugyfel);
	
	public Ugyfel findById(Long id);
	
	public boolean existsById(Long id);
	
	public boolean existsByName(String name);
	
}
