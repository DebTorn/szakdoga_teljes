package com.munkanyilvantarto.serviceimp;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.munkanyilvantarto.entity.Kerelem;
import com.munkanyilvantarto.entity.TokenGenerator;
import com.munkanyilvantarto.entity.User;
import com.munkanyilvantarto.repository.KerelemRepository;
import com.munkanyilvantarto.repository.UserRepository;
import com.munkanyilvantarto.service.KerelemService;

@Service
public class KerelemServiceImplementation implements KerelemService{

	private KerelemRepository KerelemRepo;
	private UserRepository UserRepo;
	
	@Autowired
	public void setKerelemRepo(KerelemRepository kerelemRepo, UserRepository userRepo) {
		KerelemRepo = kerelemRepo;
		UserRepo = userRepo;
	}

	@Override
	public boolean save(Kerelem kerelem) {
		
		User user = UserRepo.findByEmail(kerelem.getEmail());
		
		if(kerelem != null) {
			if(user != null) {
				
				TokenGenerator token = new TokenGenerator();
				token.setToken();
				kerelem.setTipus(kerelem.getTipus());
				kerelem.setEmail(user.getEmail());

				if(KerelemRepo.existsByToken(token.getToken())) {
					return false;
				}else {
					kerelem.setToken(token.getToken());
				}
				kerelem.setCreated_at(new Timestamp(new Date().getTime()));
				
				if(KerelemRepo.save(kerelem) != null) {
					return true;
				}else {
					return false;
				}
			}else {
				return false;
			}
		}else {
			return false;
		}
		
	}

	@Override
	public boolean findByTokenAndEmailDolgozo(String token) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Kerelem kerelem = KerelemRepo.findByTokenAndEmail(token, auth.getName());

		if(kerelem != null) {
			return true;
		}else {
			return false;
		}
		
	}

	@Override
	public String findByTokenAndEmail(String token, String tipus) {
		
		Kerelem email = KerelemRepo.findEmailByTokenAndTipus(token, tipus);
		Kerelem kerelem = KerelemRepo.findByTokenAndEmail(token, email.getEmail());
		SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
		Calendar calendar = Calendar.getInstance();
		
		calendar.setTime(kerelem.getCreated_at());
		calendar.add(Calendar.HOUR_OF_DAY, 1);
	
		Date created = null;
		Date expired = null;

		try {
			created = format.parse(format.format(calendar.getTime()));
			expired = format.parse(format.format(new Date()));
		}catch(ParseException pe) {
			System.err.println(pe);
		}

		if(created.compareTo(expired) > 0 && kerelem != null) {
			return email.getEmail();
		}else {
			KerelemRepo.delete(kerelem);
			return null;
		}

	}

	@Override
	public boolean existsByTokenAndEmailAndTipus(String token, String email, String tipus) {
		
		Kerelem kerelem = KerelemRepo.findByEmailAndTipus(email, tipus);
		
		if(kerelem == null) {
			return true;
		}else {
			return false;
		}
		
	}

	@Override
	public void deleteByToken(String token) {
		KerelemRepo.deleteByToken(token);
	}

	
}
