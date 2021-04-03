package com.munkanyilvantarto.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import org.springframework.data.repository.query.Param;

import com.munkanyilvantarto.entity.Munka;

public interface MunkaService {

	public boolean save(Map<String, String> allRequestDatas);
	
	public ArrayList<Munka> findAll();
	
	public Munka findMunkaById(Long id);
	
	public boolean existsById(Long id);
	
	public void deleteById(Long id);
	
	public boolean update(Map<String,String> allRequestDatas);
	
	public boolean findByKotesId(Long id);
	
	public ArrayList<Munka> findMunkaByKotesId(Long id);
	
	public Set<Integer> findProjektEvByProjektId(Long id);
	
	public Set<Integer> findProjektHonapByProjektIdAndProjektEv(Long id, String ev);
	
	public Map<String, Object> elszamTablaDolgozoOsszesito(Long id, String datum);
	
	public ArrayList<Munka> findByDatum(String datum);
	
	public ArrayList<String> findByUserId(Long id);
	
	public ArrayList<String> findHonapByUserId(Long id, String ev);
	
	public Date findCreatedAtById(@Param("id") Long id);
	
	public Double findSumRaforditasByProjektIdAndEvAndHonap(Long id, String ev, String honap);
	
}
