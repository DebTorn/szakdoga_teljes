package com.munkanyilvantarto.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.view.RedirectView;

import com.munkanyilvantarto.entity.Kerelem;
import com.munkanyilvantarto.entity.Munka;
import com.munkanyilvantarto.entity.Ugyfel;
import com.munkanyilvantarto.entity.UgyfelekProjektek;
import com.munkanyilvantarto.entity.User;
import com.munkanyilvantarto.entity.UserJelszo;
import com.munkanyilvantarto.entity.UsersProjektek;
import com.munkanyilvantarto.serviceimp.KerelemServiceImplementation;
import com.munkanyilvantarto.serviceimp.KoltsegServiceImplementation;
import com.munkanyilvantarto.serviceimp.MunkaServiceImplementation;
import com.munkanyilvantarto.serviceimp.ProjektServiceImplementation;
import com.munkanyilvantarto.serviceimp.TipusServiceImplementation;
import com.munkanyilvantarto.serviceimp.UgyfelServiceImplementation;
import com.munkanyilvantarto.serviceimp.UgyfelekProjektekServiceImplementation;
import com.munkanyilvantarto.serviceimp.UserServiceImplementation;
import com.munkanyilvantarto.serviceimp.UsersProjektekImplementation;

@Controller
public class ViewController {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private UgyfelServiceImplementation UgyfelImp;
	private ProjektServiceImplementation ProjekImp;
	private UserServiceImplementation UserImp;
	private UsersProjektekImplementation UPImp;
	private UgyfelekProjektekServiceImplementation UgyPImp;
	private KoltsegServiceImplementation KoltsegImp;
	private MunkaServiceImplementation MunkaImp;
	private KerelemServiceImplementation KerelemImp;
	private TipusServiceImplementation TipusImp;
	
	@Autowired
	public void setImps(
	UgyfelServiceImplementation ugyfelImp, 
	ProjektServiceImplementation proImp, 
	UserServiceImplementation userImp, 
	UsersProjektekImplementation uPImp, 
	UgyfelekProjektekServiceImplementation ugyPImp,
	KoltsegServiceImplementation koltsegImp,
	MunkaServiceImplementation munkaImp,
	KerelemServiceImplementation kerelemImp,
	TipusServiceImplementation tipusImp
	) {
		UgyfelImp = ugyfelImp;
		ProjekImp = proImp;
		UserImp = userImp;
		UPImp = uPImp;
		UgyPImp = ugyPImp;
		KoltsegImp = koltsegImp;
		MunkaImp = munkaImp;
		KerelemImp = kerelemImp;
		TipusImp = tipusImp;
	}
	
	@GetMapping("admin/ugyfel")
	public String Ugyfel(Model model) {
		if(UgyfelImp.ugyfelek() != null) {
			logger.debug("Ugyfel adatok nem null es atadasa");
			model.addAttribute("ugyfelek", UgyfelImp.ugyfelek());
			logger.debug("Ugyfel adatok atadva");
		}
		if(UgyPImp.getAllUgyfelKotes() != null) {
			logger.debug("Ugyfel-projekt nem null es kotesek atadasa");
			model.addAttribute("ugykotes", UgyPImp.getAllUgyfelKotes());
			Set<Ugyfel> kUgyfelek = new HashSet<>();
			for(UgyfelekProjektek up : UgyPImp.getAllUgyfelKotes()) {
				kUgyfelek.add(up.getUgyfel());
			}
			model.addAttribute("kotottugyfelek", kUgyfelek);
		}
		logger.debug("ugyfel.html megjelenites!");
		return "ugyfel";
	}
	
	@GetMapping("admin/dolgozo")
	public String Dolgozo(Model model) {
		if(UserImp.getAllUser() != null) {
			logger.debug("User adatok nem null es atadas");
			model.addAttribute("dolgozok", UserImp.getAllUser());
		}
		if(UPImp.getAllKotes() != null) {
			logger.debug("User-projekt adatok nem null es atadas");
			model.addAttribute("dolgkotes", UPImp.getAllKotes());
		}
		if(TipusImp.findAll() != null) {
			logger.debug("Tipus adatok nem null es atadas");
			model.addAttribute("tipusok", TipusImp.findAll());
		}
		return "dolgozo";
	}
	
	@GetMapping("admin/projekt")
	public String Projekt(Model model) {
		if(ProjekImp.findAll() != null) {
			logger.debug("Projekt adatok nem null es atadas");
			model.addAttribute("projektek", ProjekImp.findAll());
		}
		if(KoltsegImp.findAll() != null) {
			logger.debug("Koltseg adatok nem null es atadas");
			model.addAttribute("koltsegek", KoltsegImp.findAll());
		}
		return "projekt";
	}
	
	@GetMapping("admin/elszamolas")
	public String Elszamolas(Model model) {
		if(MunkaImp.findAll() != null) {
			logger.debug("Munka adatok nem null es atadas");
			model.addAttribute("munkak", MunkaImp.findAll());
		}
		if(ProjekImp.findAll() != null) {
			logger.debug("Projekt adatok nem null es atadas");
			model.addAttribute("projektek", ProjekImp.findAll());
		}
		if(TipusImp.findAll() != null) {
			logger.debug("Tipus adatok nem null es atadas");
			model.addAttribute("tipusok", TipusImp.findAll());
		}
		return "elszamolas";
	}
	
	@GetMapping("/jelszo/{token}")
	public String JelszoVisszaallitasAdmin(@PathVariable("token") String token, Model model) {
		if(KerelemImp.findByTokenAndEmail(token, "jelszo") != null) {
			logger.debug("Kerelem kereses email es token alapjan nem null es atadas");
			UserJelszo uJelszo = new UserJelszo();
			uJelszo.setEmail(KerelemImp.findByTokenAndEmail(token, "jelszo"));
			uJelszo.setToken(token);
			model.addAttribute("user", uJelszo);
			model.addAttribute("token", token);
			logger.debug("jelszo.html megjelenites");
			return "jelszo";
		}else {
			logger.debug("Kerelem kereses email es token alapjan null es error.html megjelenites");
			return "error";
		}
	}
	
	@GetMapping("/visszaigazolas/{token}")
	public RedirectView EmailVisszaigazolas(@PathVariable("token") String token) {
		if(UserImp.findByToken(token) != null) {
			logger.debug("User token alapjan nem null es atadas");
			User user = UserImp.findByToken(token);
			user.setAktiv(1);
			user.setToken(null);
			if(UserImp.simaSave(user)) {
				logger.debug("User modositas token alapjan sikeres");
				return new RedirectView("visszaigazolas?sikeres=1");
			}else {
				logger.debug("User modositas token alapjan sikertelen");
				return new RedirectView("visszaigazolas?sikeres=2");
			}
		}else {
			logger.debug("User token alapjan null");
			return null;
		}
	}
	
	@GetMapping("/elfelejtett")
	public String ElfelejtettJelszo(Model model) {
		Kerelem kerelem = new Kerelem();
		kerelem.setTipus("jelszo");
		model.addAttribute("kerelem", kerelem);
		logger.debug("elfelejtett.html megjelenites");
		return "elfelejtett";
	}
	
	@GetMapping("admin/munka/dolgozo/{id}")
	public String AdminDolgozokMunkakMegjelenites(@PathVariable("id") Long id, Model model) {
		if(UserImp.existsById(id)) {
			logger.debug("Letezik a user az id alapjan");
			ArrayList<Munka> munkak = new ArrayList<>();
			if(UserImp.findById(id) != null) {
				logger.debug("Talalt user nem null");
				for(UsersProjektek up : UserImp.findById(id).getUserpro()) {
					for(Munka mun : MunkaImp.findMunkaByKotesId(up.getId())) {
						munkak.add(mun);
					}
				}
				model.addAttribute("munkak", munkak);
				model.addAttribute("id", id);
				if(TipusImp.findAll() != null) {
					model.addAttribute("tipusok", TipusImp.findAll());
				}
			}else {
				logger.debug("Talalt user null es error.html megjelenites");
				return "error";
			}
			logger.debug("Minden ok es munka.html megjelenites");
			return "munka";
		}else {
			logger.debug("Nincs user, ezert error.html megjelenites");
			return "error";
		}
	}
	
	@GetMapping("dolgozo/munka")
	public String DolgozokMunkakMegjelenites(Model model, Principal principal) {
		if(UserImp.existsById(UserImp.findByEmail(principal.getName()).getId())) {
			logger.debug("Talalt user-t principal alapjan.");
			ArrayList<Munka> munkak = new ArrayList<>();
			if(UserImp.findById(UserImp.findByEmail(principal.getName()).getId()) != null) {
				logger.debug("User nem null e-mail alapjan");
				for(UsersProjektek up : UserImp.findById(UserImp.findByEmail(principal.getName()).getId()).getUserpro()) {
					for(Munka mun : MunkaImp.findMunkaByKotesId(up.getId())) {
						munkak.add(mun);
					}
				}
				model.addAttribute("munkak", munkak);
				model.addAttribute("principal", UserImp.findByEmail(principal.getName()).getId());
				if(TipusImp.findAll() != null) {
					logger.debug("Tipusok megtalalva");
					model.addAttribute("tipusok", TipusImp.findAll());
				}
			}
			logger.debug("munka.html megleneites es atadas");
			return "munka";
		}else {
			logger.debug("User nincs principal alapjan ezert error.html megjelenites");
			return "error";
		}
	}
	
	@GetMapping("dolgozo/profil")
	public String DolgozoProfilMegjelenites(Model model, Principal principal) {
		if(UserImp.existsById(UserImp.findByEmail(principal.getName()).getId())) {
			logger.debug("User letezik principal alapjan2");
			User user = UserImp.findByEmail(principal.getName());
			if(user != null) {
				logger.debug("user nem null");
				Kerelem kerelem = new Kerelem();
				kerelem.setTipus("jelszo");
				kerelem.setEmail(user.getEmail());
				logger.debug("kerelem atadas");
				model.addAttribute("kerelem", kerelem);
			}
			logger.debug("profil.html megjelenites");
			return "profil";
		}else {
			logger.debug("Nincs user principal alapjan ezert error.html megjelenites2");
			return "error";
		}
	}
	
	@GetMapping("/dolgozo/jelszo/{token}")
	public String JelszoVisszaallitasDolgozo(@PathVariable("token") String token, Model model) {
		if(KerelemImp.findByTokenAndEmail(token, "jelszo") != null) {
			logger.debug("token es tipus alapjan talalt kerelmet");
			UserJelszo uJelszo = new UserJelszo();
			uJelszo.setEmail(KerelemImp.findByTokenAndEmail(token, "jelszo"));
			uJelszo.setToken(token);
			logger.debug("User atadas");
			model.addAttribute("user", uJelszo);
			logger.debug("token atadas");
			model.addAttribute("token", token);
			logger.debug("djelszo.html megjelenites");
			return "djelszo";
		}else {
			logger.debug("Kerelem nincs token es tipus alapjan ezert error.html megjelenites");
			return "error";
		}
	}
	
}
