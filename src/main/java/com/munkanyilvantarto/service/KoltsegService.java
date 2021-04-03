package com.munkanyilvantarto.service;

import java.util.ArrayList;
import java.util.Map;

import org.springframework.data.repository.query.Param;

import com.munkanyilvantarto.entity.Koltseg;

public interface KoltsegService {

	public boolean save(Map<String,String> koltseg);
	
	public ArrayList<Koltseg> findAll();
	
	public boolean update(Map<String, String> allRequestDatas);
	
	public Koltseg findKoltsegById(Long id);
	
	public void deleteById(Long id);
	
	public boolean existsById(Long id);
	
	public Long findSumKoltsegByCreatedAt(String datum, Long id);
	
}
