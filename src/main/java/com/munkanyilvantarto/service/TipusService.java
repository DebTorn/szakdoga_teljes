package com.munkanyilvantarto.service;

import java.util.ArrayList;
import java.util.Map;

import com.munkanyilvantarto.entity.Tipus;

public interface TipusService {

	public boolean save(Tipus tipus);
	
	public Tipus findById(Long id);
	
	public ArrayList<Tipus> findAll();
	
	public boolean update(Map<String,String> allRequestDatas);
	
}
