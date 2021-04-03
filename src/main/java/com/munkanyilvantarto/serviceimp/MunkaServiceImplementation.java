package com.munkanyilvantarto.serviceimp;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.munkanyilvantarto.entity.Munka;
import com.munkanyilvantarto.entity.Projekt;
import com.munkanyilvantarto.entity.User;
import com.munkanyilvantarto.entity.UsersProjektek;
import com.munkanyilvantarto.repository.MunkaRepository;
import com.munkanyilvantarto.repository.ProjektRepository;
import com.munkanyilvantarto.repository.TipusRepository;
import com.munkanyilvantarto.repository.UserRepository;
import com.munkanyilvantarto.repository.UsersProjektekRepository;
import com.munkanyilvantarto.service.MunkaService;

@Service
public class MunkaServiceImplementation implements MunkaService{

	private MunkaRepository MunkaRepo;
	private UsersProjektekRepository UserProRepo;
	private TipusRepository TipusRepo;
	private JdbcTemplate jdbc;
	
	@Autowired
	public void setRepos(MunkaRepository munkaRepo, UsersProjektekRepository userProRepo, TipusRepository tipusRepo, JdbcTemplate temp) {
		MunkaRepo = munkaRepo;
		UserProRepo = userProRepo;
		jdbc = temp;
		TipusRepo = tipusRepo;
	}
	
	@Override
	public boolean save(Map<String, String> allRequestDatas) {
		Munka munka = new Munka();
		UsersProjektek userpro = UserProRepo.findByUserIdAndProjektId(Long.parseLong(allRequestDatas.get("dolgozo")), Long.parseLong(allRequestDatas.get("projekt")));
		munka.setUserpro(userpro);
		munka.setRaforditas(Float.parseFloat(allRequestDatas.get("raforditas")));
		munka.setMunkaleiras(allRequestDatas.get("leiras"));
		munka.setTipus(TipusRepo.findTipusById(Long.parseLong(allRequestDatas.get("muntipus"))));
		munka.setCreated_at(new Timestamp(new Date().getTime()));
		munka.setDatum(allRequestDatas.get("datum"));
		
		if(MunkaRepo.save(munka) != null) {
			return true;
		}else {
			return false;
		}
	}
	
	@Override
	public ArrayList<Munka> findAll() {
		return MunkaRepo.findAll();
	}

	@Override
	public Munka findMunkaById(Long id) {
		return MunkaRepo.findMunkaById(id);
	}

	@Override
	public boolean existsById(Long id) {
		return MunkaRepo.existsById(id);
	}

	@Override
	public void deleteById(Long id) {
		MunkaRepo.deleteRow(id);
	}

	@Override
	public boolean update(Map<String, String> allRequestDatas) {
		Munka munka = MunkaRepo.findMunkaById(Long.parseLong(allRequestDatas.get("id")));
		UsersProjektek userpro = UserProRepo.findByUserIdAndProjektId(Long.parseLong(allRequestDatas.get("user")), Long.parseLong(allRequestDatas.get("projekt")));
		
		munka.setDatum(allRequestDatas.get("datum"));
		munka.setMunkaleiras(allRequestDatas.get("leiras"));
		munka.setRaforditas(Float.parseFloat(allRequestDatas.get("raforditas")));
		munka.setTipus(TipusRepo.findTipusById(Long.parseLong(allRequestDatas.get("muntipus"))));
		munka.setCreated_at(munka.getCreated_at());
		munka.setUpdated_at(new Timestamp(new Date().getTime()));
		munka.setUserpro(userpro);
			
		if(MunkaRepo.save(munka) != null) {
			return true;
		}else {
			return false;
		}
	}

	@Override
	public boolean findByKotesId(Long id) {
		if(MunkaRepo.findByKotesId(id).size() > 0) {
			return true;
		}else {
			return false;
		}
	}

	@Override
	public Set<Integer> findProjektEvByProjektId(Long id) {
		return MunkaRepo.findProjektEvByProjektId(id);
	}

	@Override
	public Set<Integer> findProjektHonapByProjektIdAndProjektEv(Long id, String ev) {
		return MunkaRepo.findProjektHonapByProjektIdAndProjektEv(id, ev);
	}

	@Override
	public Map<String, Object> elszamTablaDolgozoOsszesito(Long id, String datum) {
		//return MunkaRepo.elszamTablaDolgozoOsszesito(id, datum);
		
		return jdbc.queryForMap("SELECT users.teljesnev as 'dolgozo', projektek.name as 'projekt', ROUND((SUM(munkak.raforditas)*users_projektek.oraber), 0) as 'netto', ROUND(((SUM(munkak.raforditas)*users_projektek.oraber)*users.bruttoszorzo), 0) as 'brutto' FROM munkak INNER JOIN users_projektek ON users_projektek.id = munkak.kotes_id INNER JOIN users ON users_projektek.user_id = users.id INNER JOIN projektek ON users_projektek.projekt_id = projektek.id WHERE users_projektek.projekt_id = "+id+" AND munkak.datum LIKE '"+datum+"' GROUP BY users.teljesnev, users_projektek.oraber, users.bruttoszorzo");
	}

	@Override
	public ArrayList<Munka> findMunkaByKotesId(Long id) {
		return MunkaRepo.findByKotesId(id);
	}

	@Override
	public ArrayList<Munka> findByDatum(String datum) {
		return MunkaRepo.findByDatum(datum);
	}

	@Override
	public ArrayList<String> findByUserId(Long id) {
		return MunkaRepo.findEvByUserId(id);
	}

	@Override
	public ArrayList<String> findHonapByUserId(Long id, String ev) {
		return MunkaRepo.findHonapByUserId(id, ev);
	}

	@Override
	public Date findCreatedAtById(Long id) {
		return MunkaRepo.findCreatedAtById(id);
	}

	@Override
	public Double findSumRaforditasByProjektIdAndEvAndHonap(Long id, String ev, String honap) {
		return MunkaRepo.findSumRaforditasByProjektIdAndEvAndHonap(id, ev, honap);
	}
	
}
