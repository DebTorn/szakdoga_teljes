package com.munkanyilvantarto.serviceimp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.munkanyilvantarto.entity.Kerelem;
import com.munkanyilvantarto.entity.User;
import com.munkanyilvantarto.service.EmailService;

@Service
public class EmailServiceImplementation implements EmailService{

	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private JavaMailSender mailSender;

	@Value("${spring.mail.username}")
	private String MAIL_FROM;
	
	@Autowired
	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}
	
	@Override
	public void SendValtMail(Kerelem kerelem, String tipus) {
		SimpleMailMessage msg = null;

		try {
			msg = new SimpleMailMessage();
			msg.setFrom(MAIL_FROM);
			msg.setTo(kerelem.getEmail());
			msg.setSubject(kerelem.getTipus() + " változtatás");
			if(tipus.equalsIgnoreCase("dolgozo")) {
				msg.setText("http://localhost:9001/dolgozo/"+kerelem.getTipus()+"/"+kerelem.getToken());
			}else {
				msg.setText("http://localhost:9001/"+kerelem.getTipus()+"/"+kerelem.getToken());
			}
			mailSender.send(msg);
		}catch(Exception e) {
			logger.debug("mail: " + String.valueOf(e));
		}
	}
	
	@Override
	public void SendAuthMail(User user) {
		SimpleMailMessage msg = null;

		try {
			msg = new SimpleMailMessage();
			msg.setFrom(MAIL_FROM);
			msg.setTo(user.getEmail());
			msg.setSubject("Visszaigazoló e-mail");
			msg.setText("http://localhost:9001/visszaigazolas/"+user.getToken());
			mailSender.send(msg);
		}catch(Exception e) {
			logger.debug("authmail: " + String.valueOf(e));
		}
	}
	
}
