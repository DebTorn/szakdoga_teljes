package com.munkanyilvantarto.serviceimp;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.munkanyilvantarto.entity.Projekt;
import com.munkanyilvantarto.entity.Ugyfel;
import com.munkanyilvantarto.entity.UgyfelekProjektek;
import com.munkanyilvantarto.entity.User;
import com.munkanyilvantarto.entity.UsersProjektek;
import com.munkanyilvantarto.repository.ProjektRepository;
import com.munkanyilvantarto.repository.UgyfelRepository;
import com.munkanyilvantarto.repository.UserRepository;
import com.munkanyilvantarto.repository.UsersProjektekRepository;
import com.munkanyilvantarto.service.UsersProjektekService;

@Service
public class UsersProjektekImplementation implements UsersProjektekService{

	private UsersProjektekRepository UPRepo;
	private ProjektRepository ProRepo;
	private UserRepository UserRepo;
	
	@Autowired
	public void setRepos(UsersProjektekRepository uPImp, ProjektRepository proRepo, UserRepository userRepo) {
		UPRepo = uPImp;
		ProRepo = proRepo;
		UserRepo = userRepo;
	}

	@Override
	public ArrayList<UsersProjektek> getAllKotes() {
		return UPRepo.findAll();
	}

	@Override
	public Set<UsersProjektek> getAllProjektByUserId(Long id) {
		return UPRepo.findByUserId(id);
	}

	@Override
	public UsersProjektek findUserById(Long id) {
		return UPRepo.findUserById(id);
	}
	
	@Override
	public boolean save(Map<String, String> allRequestDatas) {
		
		Projekt projekt = ProRepo.findProjektById(Long.parseLong(allRequestDatas.get("projekt")));
		User user = UserRepo.findUserById(Long.parseLong(allRequestDatas.get("dolgozo")));
		
		UsersProjektek userpro = new UsersProjektek();
		
		userpro.setOraber(Integer.parseInt(allRequestDatas.get("oraber")));
		userpro.setProjekt(projekt);
		userpro.setUser(user);
		
		if(UPRepo.save(userpro) == null) {
			return false;
		}else {
			return true;
		}
	}

	@Override
	public void deleteById(Long id) {
		UPRepo.deleteRow(id);
	}

	@Override
	public boolean existsById(Long id) {
		return UPRepo.existsById(id);
	}

	@Override
	public UsersProjektek findByUserIdAndProjektId(Long userid, Long proid) {
		return UPRepo.findByUserIdAndProjektId(userid, proid);
	}

	@Override
	public boolean existsByUserIdAndProjektId(Long userid, Long proid) {
		return UPRepo.existsByUserIdAndProjektId(userid, proid);
	}

	@Override
	public Set<UsersProjektek> findByUserId(Long id) {
		return UPRepo.findByUserId(id);
	}

}
