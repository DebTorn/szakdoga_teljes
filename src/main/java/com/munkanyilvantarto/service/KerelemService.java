package com.munkanyilvantarto.service;

import java.util.ArrayList;

import com.munkanyilvantarto.entity.Kerelem;

public interface KerelemService {

	public boolean save(Kerelem kerelem);
	
	public boolean findByTokenAndEmailDolgozo(String token);
	
	public String findByTokenAndEmail(String token, String tipus);
	
	public boolean existsByTokenAndEmailAndTipus(String token, String email, String tipus);
	
	public void deleteByToken(String token);
	
}
