package com.munkanyilvantarto.serviceimp;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.munkanyilvantarto.entity.Projekt;
import com.munkanyilvantarto.entity.Ugyfel;
import com.munkanyilvantarto.repository.ProjektRepository;
import com.munkanyilvantarto.service.ProjektService;

@Service
public class ProjektServiceImplementation implements ProjektService{

	private ProjektRepository proRepo;
	
	@Autowired
	public void setProRepo(ProjektRepository proRepo) {
		this.proRepo = proRepo;
	}

	@Override
	public ArrayList<Projekt> findAll() {
		return proRepo.findAll();
	}

	@Override
	public Set<Projekt> findNameById(Long id) {
		return proRepo.findNameById(id);
	}

	@Override
	public boolean save(Map<String, String> allRequestDatas) {
		Projekt projekt = new Projekt();
		
		projekt.setLeiras(allRequestDatas.get("leiras"));
		projekt.setName(allRequestDatas.get("nev"));
		projekt.setStatus(Integer.parseInt(allRequestDatas.get("status")));
		projekt.setTervezettoraber(Long.parseLong(allRequestDatas.get("oradij")));
		projekt.setType(Integer.parseInt(allRequestDatas.get("ptipus")));
		projekt.setCreated_at(new Timestamp(new Date().getTime()));
		
		if(proRepo.save(projekt) != null) {
			return true;
		}else {
			return false;
		}
		
	}

	@Override
	public boolean update(Map<String, String> allRequestDatas) {
		Projekt projekt = proRepo.findProjektById(Long.parseLong(allRequestDatas.get("id")));
		
		projekt.setName(allRequestDatas.get("nev"));
		projekt.setLeiras(allRequestDatas.get("leiras"));
		projekt.setStatus(Integer.parseInt(allRequestDatas.get("status")));
		projekt.setTervezettoraber(Long.parseLong(allRequestDatas.get("oraber")));
		projekt.setType(Integer.parseInt(allRequestDatas.get("ptipus")));
		projekt.setUpdated_at(new Timestamp(new Date().getTime()));
		projekt.setCreated_at(projekt.getCreated_at());
		
		if(proRepo.save(projekt) != null) {
			return true;
		}else {
			return false;
		}
	}

	@Override
	public Projekt findById(Long id) {
		return proRepo.findProjektById(id);
	}

	@Override
	public boolean existsById(Long id) {
		return proRepo.existsById(id);
	}

	@Override
	public boolean existsByName(String name) {
		return proRepo.existsByName(name);
	}

}
