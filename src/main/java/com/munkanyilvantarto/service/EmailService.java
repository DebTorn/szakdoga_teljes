package com.munkanyilvantarto.service;

import com.munkanyilvantarto.entity.Kerelem;
import com.munkanyilvantarto.entity.User;

public interface EmailService {

	public void SendValtMail(Kerelem kerelem, String tipus);
	
	public void SendAuthMail(User user);
	
}
