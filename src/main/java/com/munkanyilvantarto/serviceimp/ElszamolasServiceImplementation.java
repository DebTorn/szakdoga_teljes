package com.munkanyilvantarto.serviceimp;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.munkanyilvantarto.entity.Elszamolas;
import com.munkanyilvantarto.entity.Munka;
import com.munkanyilvantarto.repository.ElszamolasRepository;
import com.munkanyilvantarto.repository.MunkaRepository;
import com.munkanyilvantarto.service.ElszamolasService;

@Service
public class ElszamolasServiceImplementation implements ElszamolasService{

	private MunkaRepository MunkaRepo;
	private ElszamolasRepository ElszamolasRepo;
		
	@Autowired
	public void setMunkaRepo(MunkaRepository munkaRepo, ElszamolasRepository elszamolasRepo) {
		MunkaRepo = munkaRepo;
		ElszamolasRepo = elszamolasRepo;
	}

	@Override
	public boolean save(Map<String, String> allRequestDatas) {
		Elszamolas elszamolas = new Elszamolas();
		Munka munka = MunkaRepo.findMunkaById(Long.parseLong(allRequestDatas.get("id")));
		
		elszamolas.setMunka(munka);
		elszamolas.setOsszeg(Long.parseLong(allRequestDatas.get("osszeg")));
		elszamolas.setDatum(allRequestDatas.get("datum"));
		
		if(ElszamolasRepo.save(elszamolas) != null) {
			return true;
		}else {
			return false;
		}
	}

	@Override
	public boolean megszamolById(Long id) {
		System.out.println(ElszamolasRepo.findById(id));
		if(ElszamolasRepo.findById(id) != null && !ElszamolasRepo.findById(id).isEmpty()) {
			System.out.println(ElszamolasRepo.findById(id));	
			return true;
		}else {
			return false;
		}
	}

	@Override
	public boolean findByMunkaId(Long id) {
		if(ElszamolasRepo.findByMunkaId(id) != null) {
			return true;
		}else {
			return false;
		}
	}

}
