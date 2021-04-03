package com.munkanyilvantarto.serviceimp;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.munkanyilvantarto.entity.Koltseg;
import com.munkanyilvantarto.entity.Projekt;
import com.munkanyilvantarto.repository.KoltsegRepository;
import com.munkanyilvantarto.repository.ProjektRepository;
import com.munkanyilvantarto.service.KoltsegService;

@Service
public class KoltsegServiceImplementation implements KoltsegService{

	private KoltsegRepository KoltsegRepo;
	private ProjektRepository ProjektRepo;
	
	@Autowired
	public void setRepos(KoltsegRepository koltsegRepo, ProjektRepository proRepo) {
		this.KoltsegRepo = koltsegRepo;
		this.ProjektRepo = proRepo;
	}

	@Override
	public boolean save(Map<String,String> allRequestDatas) {
		
		Koltseg koltseg = new Koltseg();
		Projekt projekt = ProjektRepo.findProjektById(Long.parseLong(allRequestDatas.get("proid")));
		
		koltseg.setKoltseg(Long.parseLong(allRequestDatas.get("koltseg")));
		koltseg.setMegnevezes(allRequestDatas.get("megnevezes"));
		koltseg.setProjekt(projekt);
		koltseg.setCreated_at(new Timestamp(new Date().getTime()));
		
		if(KoltsegRepo.save(koltseg) != null) {
			return true;
		}else {
			return false;
		}
		
	}

	@Override
	public ArrayList<Koltseg> findAll() {
		return KoltsegRepo.findAll();
	}

	@Override
	public boolean update(Map<String, String> allRequestDatas) {
		
		Koltseg koltseg = KoltsegRepo.findKoltsegById(Long.parseLong(allRequestDatas.get("id")));
		
		Projekt projekt = ProjektRepo.findProjektById(Long.parseLong(allRequestDatas.get("proid")));
		
		koltseg.setKoltseg(Long.parseLong(allRequestDatas.get("koltseg")));
		koltseg.setMegnevezes(allRequestDatas.get("megnevezes"));
		koltseg.setProjekt(projekt);
		koltseg.setCreated_at(koltseg.getCreated_at());
		koltseg.setUpdated_at(new Timestamp(new Date().getTime()));
		
		if(KoltsegRepo.save(koltseg) != null) {
			return true;
		}else {
			return false;
		}
		
	}

	@Override
	public Koltseg findKoltsegById(Long id) {
		return KoltsegRepo.findKoltsegById(id);
	}

	@Override
	public void deleteById(Long id) {
		KoltsegRepo.deleteById(id);
	}

	@Override
	public boolean existsById(Long id) {
		return KoltsegRepo.existsById(id);
	}

	@Override
	public Long findSumKoltsegByCreatedAt(String datum, Long id) {
		return KoltsegRepo.findSumKoltsegByCreatedAt(datum, id);
	}
	
}
