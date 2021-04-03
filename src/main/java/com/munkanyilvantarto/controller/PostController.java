package com.munkanyilvantarto.controller;

import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;

import com.munkanyilvantarto.entity.Kerelem;
import com.munkanyilvantarto.entity.User;
import com.munkanyilvantarto.entity.UserJelszo;
import com.munkanyilvantarto.serviceimp.EmailServiceImplementation;
import com.munkanyilvantarto.serviceimp.KerelemServiceImplementation;
import com.munkanyilvantarto.serviceimp.ProjektServiceImplementation;
import com.munkanyilvantarto.serviceimp.UgyfelServiceImplementation;
import com.munkanyilvantarto.serviceimp.UgyfelekProjektekServiceImplementation;
import com.munkanyilvantarto.serviceimp.UserServiceImplementation;

@Controller
public class PostController {

	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	UgyfelServiceImplementation ugyImp;
	ProjektServiceImplementation proImp;
	UserServiceImplementation UserImp;
	UgyfelekProjektekServiceImplementation UgyProImp;
	KerelemServiceImplementation KerelemImp;
	EmailServiceImplementation EmailService;
	
	@Autowired
	public void setUgyImp(
			UgyfelServiceImplementation ugyImp, 
			ProjektServiceImplementation proImp, 
			UserServiceImplementation userImp,
			UgyfelekProjektekServiceImplementation ugyProImp,
			KerelemServiceImplementation kerelemImp,
			EmailServiceImplementation emailService) {
		this.ugyImp = ugyImp;
		this.proImp = proImp;
		this.UserImp = userImp;
		this.UgyProImp = ugyProImp;
		this.KerelemImp = kerelemImp;
		this.EmailService = emailService;
	}

	/*@PostMapping("/admin/profil/passmod")
	public String PasswordModositas() {
		return null;
	}
	
	@PostMapping("/admin/profil/emailmod")
	public String EmailModositas() {
		return null;
	}
	
	@PostMapping("/admin/projekt/koltseg")
	public String PorjektKoltseg() {
		return null;
	}
	
	@PostMapping("/admin/elszamolas/feltoltes")
	public String MunkaFeltoltes() {
		return null;
	}
	
	@PostMapping("/admin/elszamolas/lekerdezes")
	public String ElszamolasLekerdezes() {
		return null;
	}*/
	
	@PostMapping("/jelszo")
	public RedirectView ElfelejtettKerelem(@ModelAttribute Kerelem kerelem) {
		if(kerelem != null) {
			if(KerelemImp.existsByTokenAndEmailAndTipus(kerelem.getToken(), kerelem.getEmail(), kerelem.getTipus())) {
				if(KerelemImp.save(kerelem)) {
					logger.debug(kerelem.toString());
					EmailService.SendValtMail(kerelem,"");
					return new RedirectView("/elfelejtett?succ=1");
				}else {
					return new RedirectView("/elfelejtett?succ=2");
				}
			}else {
				return new RedirectView("/");
			}
		}else {
			return new RedirectView("/");
		}
	}
	
	@PostMapping("/dolgozo/jelszo")
	public RedirectView DolgozoElfelejtettKerelem(@ModelAttribute Kerelem kerelem, Principal principal) {
		if(kerelem != null) {
			if(kerelem.getEmail().equalsIgnoreCase(principal.getName())) {
				if(KerelemImp.existsByTokenAndEmailAndTipus(kerelem.getToken(), kerelem.getEmail(), kerelem.getTipus())) {
					if(KerelemImp.save(kerelem)) {
						logger.debug(kerelem.toString());
						EmailService.SendValtMail(kerelem,"dolgozo");
						return new RedirectView("/dolgozo/profil?succ=1");
					}else {
						return new RedirectView("/dolgozo/profil?succ=2");
					}
				}else {
					return new RedirectView("/dolgozo/profil?err=1");
				}
			}else {
				return new RedirectView("/dolgozo/profil?err=2");
			}
		}else {
			return new RedirectView("/dolgozo/profil?err=3");
		}
	}
	
	@PostMapping("/jelszovalt")
	public RedirectView JelszoValtoztatas(@ModelAttribute UserJelszo uJelszo) {
		if(uJelszo != null) {
			User user = UserImp.findByEmail(uJelszo.getEmail());
			if(user != null) {
				user.setPassword(uJelszo.getPassword());
				if(UserImp.simaSave(user)) {
					KerelemImp.deleteByToken(uJelszo.getToken());
					return new RedirectView("/elfelejtett?valtoztatas=1");
				}else {
					return new RedirectView("/elfelejtett?valtoztatas=2");
				}
			}else {
				return new RedirectView("/");
			}
		}else {
			return new RedirectView("/");
		}
	}
	
	@PostMapping("/dolgozo/jelszovalt")
	public RedirectView JelszoValtoztatasDolgozo(@ModelAttribute UserJelszo uJelszo, Principal principal) {
		if(uJelszo != null) {
			User user = UserImp.findByEmail(uJelszo.getEmail());
			if(user != null) {
				if(principal.getName().equalsIgnoreCase(uJelszo.getEmail())) {
					user.setPassword(uJelszo.getPassword());
					if(UserImp.simaSave(user)) {
						KerelemImp.deleteByToken(uJelszo.getToken());
						return new RedirectView("/dolgozo/profil?valtoztatas=1");
					}else {
						return new RedirectView("/dolgozo/profil?valtoztatas=2");
					}
				}else {
					return new RedirectView("/dolgozo/profil");
				}
			}else {
				return new RedirectView("/dolgozo/profil");
			}
		}else {
			return new RedirectView("/dolgozo/profil");
		}
	}
	
}
