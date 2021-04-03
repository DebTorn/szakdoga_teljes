package com.munkanyilvantarto.serviceimp;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.munkanyilvantarto.entity.Ugyfel;
import com.munkanyilvantarto.repository.UgyfelRepository;
import com.munkanyilvantarto.service.UgyfelService;

@Service
public class UgyfelServiceImplementation implements UgyfelService {

	private UgyfelRepository ugyRepo;
	
	@Autowired
	public void setUgyRepo(UgyfelRepository ugyRepo) {
		this.ugyRepo = ugyRepo;
	}

	@Override
	public ArrayList<Ugyfel> ugyfelek() {
		return ugyRepo.findAll();
	}

	@Override
	public boolean save(Map<String, String> allRequestDatas) {
		
		Ugyfel ugyfel = new Ugyfel();
		ugyfel.setName(allRequestDatas.get("nev"));
		ugyfel.setCreated_at(new Timestamp(new Date().getTime()));
		ugyfel.setAktiv(1);
		
		if(ugyRepo.save(ugyfel) != null) {
			return true;
		}else {
			return false;
		}
	}
	

	@Override
	public boolean aktivitas(Map<String, String> allRequestDatas) {
		
		Ugyfel ugy = ugyRepo.findUgyfelById(Long.parseLong(allRequestDatas.get("id")));
		
		ugy.setAktiv(Integer.parseInt(allRequestDatas.get("aktiv")));
		ugy.setCreated_at(ugy.getCreated_at());
		
		if(ugyRepo.save(ugy) == null) {
			return false;
		}else {
			return true;
		}

	}

	@Override
	public Ugyfel findById(Long id) {
		return ugyRepo.findUgyfelById(id);
	}

	@Override
	public boolean update(Map<String, String> allRequestDatas) {

		Ugyfel ugyfel = ugyRepo.findUgyfelById(Long.parseLong(allRequestDatas.get("id")));
		
		ugyfel.setName(allRequestDatas.get("nev"));
		ugyfel.setUpdated_at(new Timestamp(new Date().getTime()));
		ugyfel.setCreated_at(ugyfel.getCreated_at());
		
		if(ugyRepo.save(ugyfel) != null) {
			return true;
		}else {
			return false;
		}
	}

	@Override
	public boolean existsById(Long id) {
		return ugyRepo.existsById(id);
	}

	@Override
	public boolean existsByName(String name) {
		return ugyRepo.existsByName(name);
	}

}
