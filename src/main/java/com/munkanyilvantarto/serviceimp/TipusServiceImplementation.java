package com.munkanyilvantarto.serviceimp;

import java.util.ArrayList;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.munkanyilvantarto.entity.Tipus;
import com.munkanyilvantarto.repository.TipusRepository;
import com.munkanyilvantarto.service.TipusService;

@Service
public class TipusServiceImplementation implements TipusService{

	TipusRepository tipusRep;
	
	@Autowired
	public void setTipusRep(TipusRepository tipusRep) {
		this.tipusRep = tipusRep;
	}

	@Override
	public boolean save(Tipus tipus) {
		if(tipusRep.save(tipus) != null) {
			return true;
		}else {
			return false;
		}
	}

	@Override
	public Tipus findById(Long id) {
		return tipusRep.findTipusById(id);
	}

	@Override
	public ArrayList<Tipus> findAll() {
		return tipusRep.findAll();
	}

	@Override
	public boolean update(Map<String, String> allRequestDatas) {
		Tipus tipus = tipusRep.findTipusById(Long.parseLong(allRequestDatas.get("id")));
		tipus.setMegnevezes(allRequestDatas.get("megnevezes"));
		
		if(tipusRep.save(tipus) != null) {
			return true;
		}else {
			return false;
		}
	}

}
