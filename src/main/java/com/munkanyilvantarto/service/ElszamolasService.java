package com.munkanyilvantarto.service;

import java.util.Map;

import org.springframework.data.jpa.repository.Query;

import com.munkanyilvantarto.entity.Elszamolas;

public interface ElszamolasService {

	public boolean save(Map<String, String> allRequestDatas);
	
	public boolean megszamolById(Long id);
	
	public boolean findByMunkaId(Long id);
	
}
