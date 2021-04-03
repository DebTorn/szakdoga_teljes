package com.munkanyilvantarto.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.munkanyilvantarto.entity.Koltseg;
import com.munkanyilvantarto.entity.Munka;
import com.munkanyilvantarto.entity.Projekt;
import com.munkanyilvantarto.entity.Tipus;
import com.munkanyilvantarto.entity.Ugyfel;
import com.munkanyilvantarto.entity.UgyfelekProjektek;
import com.munkanyilvantarto.entity.User;
import com.munkanyilvantarto.entity.UsersProjektek;
import com.munkanyilvantarto.serviceimp.ElszamolasServiceImplementation;
import com.munkanyilvantarto.serviceimp.KerelemServiceImplementation;
import com.munkanyilvantarto.serviceimp.KoltsegServiceImplementation;
import com.munkanyilvantarto.serviceimp.MunkaServiceImplementation;
import com.munkanyilvantarto.serviceimp.ProjektServiceImplementation;
import com.munkanyilvantarto.serviceimp.TipusServiceImplementation;
import com.munkanyilvantarto.serviceimp.UgyfelServiceImplementation;
import com.munkanyilvantarto.serviceimp.UgyfelekProjektekServiceImplementation;
import com.munkanyilvantarto.serviceimp.UserServiceImplementation;
import com.munkanyilvantarto.serviceimp.UsersProjektekImplementation;

@RestController
public class ApiController {

	private final static String NULLPOINTERTEXT = "Az érték nem lehet null", SUCCESSTEXT = "success", UNSUCCESSTEXT = "unsuccess", ISMERETLENTEXT = "ismeretlen";
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private UgyfelekProjektekServiceImplementation UgyProImp;
	private ProjektServiceImplementation ProImp;
	private UgyfelServiceImplementation UgyImp;
	private UserServiceImplementation UserImp;
	private UsersProjektekImplementation UserProImp;
	private KoltsegServiceImplementation KoltsegImp;
	private MunkaServiceImplementation MunkaImp;
	private KerelemServiceImplementation KerelemImp;
	private ElszamolasServiceImplementation ElszamolasImp;
	private TipusServiceImplementation TipusImp;
	
	/************************
	 * 	  IMPLEMENTÁCIÓK	*
	 ************************/
	@Autowired
	public void setImps(
			UgyfelekProjektekServiceImplementation ugyProImp, 
			ProjektServiceImplementation proImp, 
			UgyfelServiceImplementation ugyImp,
			UserServiceImplementation userImp,
			UsersProjektekImplementation userProImp,
			KoltsegServiceImplementation koltsegImp,
			MunkaServiceImplementation munkaImp,
			KerelemServiceImplementation kerelemImp,
			ElszamolasServiceImplementation elszamolasImp,
			TipusServiceImplementation tipusImp
			) {
		UgyProImp = ugyProImp;
		ProImp = proImp;
		UgyImp = ugyImp;
		UserImp = userImp;
		UserProImp = userProImp;
		KoltsegImp = koltsegImp;
		MunkaImp = munkaImp;
		KerelemImp = kerelemImp;
		ElszamolasImp = elszamolasImp;
		TipusImp = tipusImp;
	}

	
	/************************
	 *       KÖTÉSEK    	*
	 ************************/
	@PostMapping("/admin/kotes/projektek")
	public @ResponseBody ResponseEntity<ArrayList<Projekt>> KotesProjektekLekerdezes(@RequestParam Map<String, String> allRequestDatas){

			if(allRequestDatas == null || allRequestDatas.get("id").isEmpty() || allRequestDatas.get("tipus").isEmpty()) {
				throw new NullPointerException(NULLPOINTERTEXT);
			}
		
			ArrayList<Projekt> mindenProjekt = ProImp.findAll();
			
			if(allRequestDatas.get("tipus").equalsIgnoreCase("dolgozo")) { //DOLGOZÓHOZ PROJEKT KÖTÉS
				
		        Set<UsersProjektek> kotesek =  UserProImp.getAllProjektByUserId(Long.parseLong(allRequestDatas.get("id")));
				
		        for(UsersProjektek up : kotesek) {
			        for(int i = 0; i < mindenProjekt.size(); i++) {
				        	if(up.getProjekt().getId().toString().equalsIgnoreCase(mindenProjekt.get(i).getId().toString())) {
				        		mindenProjekt.remove(i);
				        	}
			        }
		        }
		   
		        
			}else if(allRequestDatas.get("tipus").equalsIgnoreCase("ugyfel")) { //ÜGYFÉLHEZ PROJEKT KÖTÉS
				
				Set<UgyfelekProjektek> kotesek =  UgyProImp.getAllProjektByUgyfelId(Long.parseLong(allRequestDatas.get("id")));
		        
		        for(UgyfelekProjektek up : kotesek) {
			        for(int i = 0; i < mindenProjekt.size(); i++) {
				        	if(up.getProjekt().getId().toString().equalsIgnoreCase(mindenProjekt.get(i).getId().toString())) {
				        		mindenProjekt.remove(i);
				        	}
			        }
		        }  
		        
			}
	        
	        return ResponseEntity.ok(mindenProjekt);
	        
	}
	
	
	/************************
	 * 	    AKTIVITÁSOK 	*
	 ************************/
	@PostMapping("/admin/aktivitas")
	public @ResponseBody ResponseEntity<String> Aktivitas(@RequestParam Map<String, String> allRequestDatas){
		
		if(allRequestDatas == null || allRequestDatas.get("tipus").isEmpty()) {
			throw new NullPointerException(NULLPOINTERTEXT);
		}
		
		if(allRequestDatas.get("tipus").equalsIgnoreCase("ugyfel")) { //ÜGYFÉL AKTIVITÁS
			if(UgyImp.existsById(Long.parseLong(allRequestDatas.get("id")))) {
				if(UgyImp.aktivitas(allRequestDatas)) {
					return ResponseEntity.ok(SUCCESSTEXT);
				}else {
					return ResponseEntity.badRequest().body(UNSUCCESSTEXT);
				}
			}else {
				return ResponseEntity.badRequest().body("nincs_ugyfel");
			}
			
		}else if(allRequestDatas.get("tipus").equalsIgnoreCase("dolgozo")) { //DOLGOZÓ AKTIVITÁS
			if(UserImp.existsById(Long.parseLong(allRequestDatas.get("id")))) {
				if(UserImp.aktivitas(allRequestDatas)) {
					return ResponseEntity.ok(SUCCESSTEXT);
				}else {
					return ResponseEntity.badRequest().body(UNSUCCESSTEXT);
				}
			}else {
				return ResponseEntity.badRequest().body("nincs_user");
			}
			
		}else {
			return ResponseEntity.badRequest().body(ISMERETLENTEXT);
		}
		
	}
	
	
	/************************
	 * 	ÜGYFÉL LEKÉRDEZÉS	*
	 ************************/
	@GetMapping("/admin/ugyfel/{id}")
	public @ResponseBody ResponseEntity<Ugyfel> UgyfelLekerdezes(@PathVariable(value="id") Long id){
		
			if(id == null) {
				throw new NullPointerException(NULLPOINTERTEXT);
			}
			
			Ugyfel ugyfel = UgyImp.findById(id);
			
			if(ugyfel != null) {
				return ResponseEntity.ok(ugyfel);
			}else {
				return ResponseEntity.badRequest().body(null);
			}
			
	}
	
	
	/************************************
	 * 	DOLGOZÓ LEKÉRDEZÉS ADMIN OLDAL	*
	 ************************************/
	@GetMapping("/admin/dolgozo/{id}")
	public @ResponseBody ResponseEntity<User> AdminDolgozoLekerdezes(@PathVariable(value="id") Long id){
		
		if(id == null) {
			throw new NullPointerException(NULLPOINTERTEXT);
		}
		
		User user = UserImp.findById(id);
		if(user != null) {
			return ResponseEntity.ok(user);
		}else {
			return ResponseEntity.badRequest().body(null);
		}
		
	}
	
	/****************************************
	 * 	DOLGOZÓ LEKÉRDEZÉS DOLGOZÓ OLDAL	*
	 ****************************************/
	@GetMapping("/dolgozo/{id}")
	public @ResponseBody ResponseEntity<User> DolgozoLekerdezes(@PathVariable("id") Long id){
		if(id == null) {
			throw new NullPointerException(NULLPOINTERTEXT);
		}
		
		User user = UserImp.findById(id);
		if(user != null) {
			user.setEmail("");
			return ResponseEntity.ok(user);
		}else {
			return ResponseEntity.badRequest().body(null);
		}
		
	}
	
	/************************
	 * 	PROJEKT LEKÉRDEZÉS	*
	 ************************/
	@GetMapping("/admin/projekt/{id}")
	public @ResponseBody ResponseEntity<Projekt> ProjektLekerdezes(@PathVariable(value="id") Long id){
		
		if(id == null) {
			throw new NullPointerException(NULLPOINTERTEXT);
		}
		
		Projekt projekt = ProImp.findById(id);
		if(projekt != null) {
			return ResponseEntity.ok(projekt);
		}else {
			return ResponseEntity.badRequest().body(null);
		}
		
	}
	
	/************************
	 * 	KÖLTSÉG LEKÉRDEZÉS	*
	 ************************/
	@GetMapping("/admin/koltseg/{id}")
	public @ResponseBody ResponseEntity<Koltseg> KoltsegLekerdezes(@PathVariable(value="id") Long id){
		
		if(id == null) {
			throw new NullPointerException(NULLPOINTERTEXT);
		}
		
		Koltseg koltseg = KoltsegImp.findKoltsegById(id);
		if(koltseg != null) {
			return ResponseEntity.ok(koltseg);
		}else {
			return ResponseEntity.badRequest().body(null);
		}
		
	}
	
	/************************
	 * 	 MUNKA LEKÉRDEZÉS	*
	 ************************/
	@GetMapping("/admin/munka/{id}")
	public @ResponseBody ResponseEntity<Munka> MunkaLekerdezes(@PathVariable(value="id") Long id){
		
		if(id == null) {
			throw new NullPointerException(NULLPOINTERTEXT);
		}
		
		Munka munka = MunkaImp.findMunkaById(id);
		
		if(munka != null) {
			return ResponseEntity.ok(munka);
		}else {
			return ResponseEntity.badRequest().body(null);
		}
		
	}
	
	/************************
	 * 	 TÍPUS LEKÉRDEZÉS	*
	 ************************/
	@GetMapping("/admin/munkatipus/{id}")
	public @ResponseBody ResponseEntity<Tipus> TipusLekerdezes(@PathVariable(value="id") Long id){
		if(id == null) {
			throw new NullPointerException(NULLPOINTERTEXT);
		}
		
		Tipus tipus = TipusImp.findById(id);
		
		if(tipus != null) {
			return ResponseEntity.ok(tipus);
		}else {
			return ResponseEntity.badRequest().body(null);
		}
		
	}
	
	@PostMapping("/admin/tipus/feltoltes")
	public @ResponseBody ResponseEntity<String> TipusFeltotles(@RequestParam Map<String,String> allRequestDatas){
		
			if(allRequestDatas == null || allRequestDatas.get("megnevezes").isEmpty()) {
				throw new NullPointerException(NULLPOINTERTEXT);
			}
		
			Tipus tipus = new Tipus();
			tipus.setMegnevezes(allRequestDatas.get("megnevezes"));
			if(TipusImp.save(tipus)) {
				return ResponseEntity.ok("sikeres");
			}else {
				return ResponseEntity.badRequest().body("sikertelen");
			}
	}
	
	@GetMapping("/dolgozo/munka/{id}")
	public @ResponseBody ResponseEntity<Munka> DolgozoMunkaLekerdezes(@PathVariable(value="id") Long id){
		
		if(id == null) {
			throw new NullPointerException(NULLPOINTERTEXT);
		}
		
		Munka munka = MunkaImp.findMunkaById(id);
		
		if(munka != null) {
			return ResponseEntity.ok(munka);
		}else {
			return ResponseEntity.badRequest().body(null);
		}
		
	}
	
	
	/************************
	 * 	    MÓDOSÍTÁSOK		*
	 ************************/
	@PostMapping("/admin/modositas")
	public @ResponseBody ResponseEntity<String> Modositas(@RequestParam Map<String, String> allRequestDatas){
		
		if(allRequestDatas == null || allRequestDatas.get("tipus").isEmpty()) {
			throw new NullPointerException(NULLPOINTERTEXT);
		}
		
		if(allRequestDatas.get("tipus").equalsIgnoreCase("dolgozo")) { //DOLGOZÓ MÓDOSÍTÁS
			if(UserImp.update(allRequestDatas)) {
				return ResponseEntity.ok(SUCCESSTEXT);
			}else {
				return ResponseEntity.badRequest().body(UNSUCCESSTEXT);
			}
		}else if(allRequestDatas.get("tipus").equalsIgnoreCase("ugyfel")) { //ÜGYFÉL MÓDOSÍTÁS
			if(UgyImp.update(allRequestDatas)) {
				return ResponseEntity.ok(SUCCESSTEXT);
			}else {
				return ResponseEntity.badRequest().body(UNSUCCESSTEXT);
			}
		}else if(allRequestDatas.get("tipus").equalsIgnoreCase("projekt")) { //PROJEKT MÓDOSÍTÁS
			if(ProImp.update(allRequestDatas)) {
				return ResponseEntity.ok(SUCCESSTEXT);
			}else {
				return ResponseEntity.badRequest().body(UNSUCCESSTEXT);
			}
		}else if(allRequestDatas.get("tipus").equalsIgnoreCase("koltseg")) {
			if(KoltsegImp.update(allRequestDatas)) {
				return ResponseEntity.ok(SUCCESSTEXT);
			}else {
				return ResponseEntity.badRequest().body(UNSUCCESSTEXT);
			}
		}else if(allRequestDatas.get("tipus").equalsIgnoreCase("munka")) {
			String[] raforditas = allRequestDatas.get("raforditas").split("\\.");
			if(raforditas.length > 1) {
				int perc = Integer.parseInt(raforditas[1]);
				if(perc == 25 || perc == 5 || perc == 75) {		
					if(MunkaImp.update(allRequestDatas)) {
						return ResponseEntity.ok(SUCCESSTEXT);
					}else {
						return ResponseEntity.badRequest().body(UNSUCCESSTEXT);
					}
				}else {
					return ResponseEntity.badRequest().body("hibas_ido");
				}	
			}else if(raforditas.length == 1){
				if(MunkaImp.update(allRequestDatas)) {
					return ResponseEntity.ok(SUCCESSTEXT);
				}else {
					return ResponseEntity.badRequest().body(UNSUCCESSTEXT);
				}
			}else {
				return ResponseEntity.badRequest().body(UNSUCCESSTEXT);
			}
		}else if(allRequestDatas.get("tipus").equalsIgnoreCase("tipus")) {
			if(TipusImp.update(allRequestDatas)) {
				return ResponseEntity.ok(SUCCESSTEXT);
			}else {
				return ResponseEntity.badRequest().body(UNSUCCESSTEXT);
			}
		}else {
				return ResponseEntity.badRequest().body(ISMERETLENTEXT);
		}
		
	}
	
	@PostMapping("/dolgozo/modositas")
	public @ResponseBody ResponseEntity<String> DolgozoModositas(@RequestParam Map<String, String> allRequestDatas){
		
		if(allRequestDatas == null || allRequestDatas.get("tipus").isEmpty()) {
			throw new NullPointerException(NULLPOINTERTEXT);
		}
		
		if(allRequestDatas.get("tipus").equalsIgnoreCase("dolgmunka")) {
			String[] raforditas = allRequestDatas.get("raforditas").split("\\.");
			Date jelenlegiDat = new Date();
			System.out.println("jelenlegi: "+jelenlegiDat);
			Date created = MunkaImp.findCreatedAtById(Long.parseLong(allRequestDatas.get("id")));
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(created);
			calendar.add(Calendar.DATE, 1);
			if(jelenlegiDat.before(calendar.getTime())) {
				if(raforditas.length > 1) {
					int perc = Integer.parseInt(raforditas[1]);
					if(perc == 25 || perc == 5 || perc == 75) {		
						if(MunkaImp.update(allRequestDatas)) {
							return ResponseEntity.ok(SUCCESSTEXT);
						}else {
							return ResponseEntity.badRequest().body(UNSUCCESSTEXT);
						}
					}else {
						return ResponseEntity.badRequest().body("hibas_ido");
					}	
				}else if(raforditas.length == 1){
					if(MunkaImp.update(allRequestDatas)) {
						return ResponseEntity.ok(SUCCESSTEXT);
					}else {
						return ResponseEntity.badRequest().body(UNSUCCESSTEXT);
					}
				}else {
					return ResponseEntity.badRequest().body(UNSUCCESSTEXT);
				}
			}else {
				return ResponseEntity.badRequest().body("lejart");
			}

		}else {
				return ResponseEntity.badRequest().body(ISMERETLENTEXT);
		}
		
	}
	
	
	/************************
	 * 	  KÖTÉSEK TÖRLÉSE 	*
	 ************************/
	@PostMapping("/admin/kotes/torles")
	public @ResponseBody ResponseEntity<String> KotesTorles(@RequestParam Map<String, String> allRequestDatas){
		
		if(allRequestDatas == null || allRequestDatas.get("tipus").isEmpty()) {
			throw new NullPointerException(NULLPOINTERTEXT);
		}
		
		if(allRequestDatas.get("tipus").equalsIgnoreCase("ugyfel")) { //ÜGYFÉL KÖTÉS TÖRLÉS
			if(UgyProImp.existsById(Long.parseLong(allRequestDatas.get("id")))) {
				UgyProImp.deleteById(Long.parseLong(allRequestDatas.get("id")));
				if(UgyProImp.existsById(Long.parseLong(allRequestDatas.get("id")))) {
					return ResponseEntity.badRequest().body(UNSUCCESSTEXT);
				}else {
					return ResponseEntity.ok(SUCCESSTEXT);
				}
			}else {
				return ResponseEntity.badRequest().body("nincs_kotes");
			}
		}else if(allRequestDatas.get("tipus").equalsIgnoreCase("dolgozo")) { //DOLGOZÓ KÖTÉS TÖRLÉS
			if(MunkaImp.findByKotesId(Long.parseLong(allRequestDatas.get("id")))) {
				return ResponseEntity.badRequest().body("van_munka");
			}else {
				if(UserProImp.existsById(Long.parseLong(allRequestDatas.get("id")))) {
					UserProImp.deleteById(Long.parseLong(allRequestDatas.get("id")));
					if(UserProImp.existsById(Long.parseLong(allRequestDatas.get("id")))) {
						return ResponseEntity.badRequest().body(UNSUCCESSTEXT);
					}else {
						return ResponseEntity.ok(SUCCESSTEXT);
					}
				}else {
					return ResponseEntity.badRequest().body("nincs_kotes");
				}
			}
		}else{
			return ResponseEntity.badRequest().body(ISMERETLENTEXT);
		}
		
	}
	
	
	/************************
	 * 	 KÖTÉS FELTÖLTÉSE	*
	 ************************/
	@PostMapping("/admin/kotes/feltoltes")
	public @ResponseBody ResponseEntity<String> KotesFeltoltes(@RequestParam Map<String, String> allRequestDatas){
			if(allRequestDatas == null || allRequestDatas.get("tipus").isEmpty()) {
				throw new NullPointerException(NULLPOINTERTEXT);
			}
				
			if(allRequestDatas.get("tipus").equalsIgnoreCase("ugyfel")) { //ÜGYFÉL KÖTÉS FELTÖLTÉS
				if(UgyImp.existsById(Long.parseLong(allRequestDatas.get("ugyfel")))){
					if(ProImp.existsById(Long.parseLong(allRequestDatas.get("projekt")))) {
						if(UgyProImp.existsByUgyfelIdAndProjektId(Long.parseLong(allRequestDatas.get("ugyfel")), Long.parseLong(allRequestDatas.get("projekt")))) {
							return ResponseEntity.badRequest().body("van_kotes");
						}else {
							if(UgyProImp.save(allRequestDatas)) {
								return ResponseEntity.ok(SUCCESSTEXT);
							}else {
								return ResponseEntity.badRequest().body(UNSUCCESSTEXT);
							}
						}
					}else {
						return ResponseEntity.badRequest().body("nincs_projekt");
					}
				}else {
					return ResponseEntity.badRequest().body("nincs_ugyfel");
				}
			}else if(allRequestDatas.get("tipus").equalsIgnoreCase("dolgozo")) { //DOLGOZÓ KÖTÉS FELTÖLTÉS
				if(UserImp.existsById(Long.parseLong(allRequestDatas.get("dolgozo")))) {
					if(ProImp.existsById(Long.parseLong(allRequestDatas.get("projekt")))) {
						if(UserProImp.existsByUserIdAndProjektId(Long.parseLong(allRequestDatas.get("dolgozo")), Long.parseLong(allRequestDatas.get("projekt")))) {
							return ResponseEntity.badRequest().body("van_kotes");
						}else {
							if(UserProImp.save(allRequestDatas)) {
								return ResponseEntity.ok(SUCCESSTEXT);
							}else {
								return ResponseEntity.badRequest().body(UNSUCCESSTEXT);
							}
						}
					}else {
						return ResponseEntity.badRequest().body("nincs_projekt");
					}
				}else {
					return ResponseEntity.badRequest().body("nincs_dolgozo");
				}
			}else {
				return ResponseEntity.badRequest().body(ISMERETLENTEXT);
			}

	}
	
	
	/************************
	 * 	    FELTÖLTÉSEK 	*
	 ************************/
	@PostMapping("/admin/feltoltes")
	public @ResponseBody ResponseEntity<String> Feltoltes(@RequestParam Map<String, String> allRequestDatas) {
			if(allRequestDatas == null || allRequestDatas.get("tipus").isEmpty()) {
				throw new NullPointerException(NULLPOINTERTEXT);
			}
			
			if(allRequestDatas.get("tipus").equalsIgnoreCase("dolgozo")) { //DOLGOZÓ FELTÖLTÉS
				if(allRequestDatas.get("ujrajelszo").equalsIgnoreCase(allRequestDatas.get("jelszo"))) {
					if(UserImp.save(allRequestDatas)) {
						return ResponseEntity.ok(SUCCESSTEXT);
					}else {
						return ResponseEntity.badRequest().body(UNSUCCESSTEXT);
					}
				}else {
					return ResponseEntity.badRequest().body("nem_egyezik");
				}
			}else if(allRequestDatas.get("tipus").equalsIgnoreCase("ugyfel")) { //ÜGYFÉL FELTÖLTÉS
				if(UgyImp.existsByName(allRequestDatas.get("nev"))) {
					return ResponseEntity.badRequest().body("van_ugyfel");
				}else {
					if(UgyImp.save(allRequestDatas)) {
						return ResponseEntity.ok(SUCCESSTEXT);
					}else {
						return ResponseEntity.badRequest().body(UNSUCCESSTEXT);
					}
				}
			}else if(allRequestDatas.get("tipus").equalsIgnoreCase("projekt")) { //PROJEKT FELTÖLTÉS
				if(ProImp.existsByName(allRequestDatas.get("nev"))) {
					return ResponseEntity.badRequest().body("van_projekt");
				}else {
					if(ProImp.save(allRequestDatas)) {
						return ResponseEntity.ok(SUCCESSTEXT);
					}else {
						return ResponseEntity.badRequest().body(UNSUCCESSTEXT);
					}
				}
			}else if(allRequestDatas.get("tipus").equalsIgnoreCase("koltseg")) { //KOLTSEG FELTOLTES
				if(KoltsegImp.save(allRequestDatas)) {
					return ResponseEntity.ok(SUCCESSTEXT);
				}else {
					return ResponseEntity.badRequest().body(UNSUCCESSTEXT);
				}
			}else if(allRequestDatas.get("tipus").equalsIgnoreCase("munka")) { //MUNKA FELTOLTES
				String[] raforditas = allRequestDatas.get("raforditas").split("\\.");
				if(raforditas.length > 0) {
					int ora = Integer.parseInt(raforditas[0]);
					if(ora < 1 || ora > 23) {
						return ResponseEntity.badRequest().body("hibas_ora");
					}
					if(raforditas.length > 1) {
						int perc = Integer.parseInt(raforditas[1]);
						if(perc == 25 || perc == 5 || perc == 75) {
							if(MunkaImp.save(allRequestDatas)) {
								return ResponseEntity.ok(SUCCESSTEXT);
							}else {
								return ResponseEntity.badRequest().body(UNSUCCESSTEXT);
							}
						}else {
							return ResponseEntity.badRequest().body("hibas_perc");
						}
					}else if(raforditas.length == 1) {
							if(MunkaImp.save(allRequestDatas)) {
								return ResponseEntity.ok(SUCCESSTEXT);
							}else {
								return ResponseEntity.badRequest().body(UNSUCCESSTEXT);
							}
					}else {
						return ResponseEntity.badRequest().body("hibas_ido");
					}
				}else {
					return ResponseEntity.badRequest().body(UNSUCCESSTEXT);
				}
			}else{
				return ResponseEntity.badRequest().body(ISMERETLENTEXT);
			}
	}
	
	@PostMapping("/dolgozo/feltoltes")
	public @ResponseBody ResponseEntity<String> DolgozoFeltoltes(@RequestParam Map<String,String> allRequestDatas, Principal principal){
		if(allRequestDatas == null) {
			throw new NullPointerException(NULLPOINTERTEXT);
		}
		
		if(allRequestDatas.get("tipus").equalsIgnoreCase("munka")) { //MUNKA FELTOLTES
			allRequestDatas.put("dolgozo", UserImp.findByEmail(principal.getName()).getId().toString());
			String[] raforditas = allRequestDatas.get("raforditas").split("\\.");
			if(raforditas.length > 0) {
				int ora = Integer.parseInt(raforditas[0]);
				if(ora < 1 || ora > 23) {
					return ResponseEntity.badRequest().body("hibas_ora");
				}
				if(raforditas.length > 1) {
					int perc = Integer.parseInt(raforditas[1]);
					if(perc == 25 || perc == 5 || perc == 75) {
						if(MunkaImp.save(allRequestDatas)) {
							return ResponseEntity.ok(SUCCESSTEXT);
						}else {
							return ResponseEntity.badRequest().body(UNSUCCESSTEXT);
						}
					}else {
						return ResponseEntity.badRequest().body("hibas_perc");
					}
				}else if(raforditas.length == 1) {
						if(MunkaImp.save(allRequestDatas)) {
							return ResponseEntity.ok(SUCCESSTEXT);
						}else {
							return ResponseEntity.badRequest().body(UNSUCCESSTEXT);
						}
				}else {
					return ResponseEntity.badRequest().body("hibas_ido");
				}
			}else {
				return ResponseEntity.badRequest().body(UNSUCCESSTEXT);
			}
		}else{
			return ResponseEntity.badRequest().body(ISMERETLENTEXT);
		}
	}
	
	@PostMapping("/admin/torles")
	public @ResponseBody ResponseEntity<String> Torles(@RequestParam Map<String, String> allRequestDatas){
		if(allRequestDatas == null || allRequestDatas.get("tipus").isEmpty() || allRequestDatas.get("id").isEmpty()) {
			throw new NullPointerException(NULLPOINTERTEXT);
		}
		if(allRequestDatas.get("tipus").equalsIgnoreCase("munka")) {
			if(MunkaImp.existsById(Long.parseLong(allRequestDatas.get("id")))) {
				MunkaImp.deleteById(Long.parseLong(allRequestDatas.get("id")));
				if(MunkaImp.existsById(Long.parseLong(allRequestDatas.get("id")))) {
					return ResponseEntity.badRequest().body(UNSUCCESSTEXT);
				}else {
					return ResponseEntity.ok(SUCCESSTEXT);
				}
			}else {
				return ResponseEntity.badRequest().body("nincs_munka");
			}
		}else {
			return ResponseEntity.badRequest().body(ISMERETLENTEXT);
		}
	}
	
	/************************
	 * 	  KÖLTSÉG TÖRLÉS 	*
	 ************************/
	@PostMapping("/admin/koltseg/torles")
	public @ResponseBody ResponseEntity<String> KoltsegTorles(@RequestParam Map<String, String> allRequestDatas){
		if(allRequestDatas == null || allRequestDatas.get("tipus").isEmpty() || allRequestDatas.get("id").isEmpty()) {
			throw new NullPointerException(NULLPOINTERTEXT);
		}
		if(allRequestDatas.get("tipus").equalsIgnoreCase("projekt")) {
			if(KoltsegImp.existsById(Long.parseLong(allRequestDatas.get("id")))) {
				KoltsegImp.deleteById(Long.parseLong(allRequestDatas.get("id")));
				if(KoltsegImp.existsById(Long.parseLong(allRequestDatas.get("id")))) {
					return ResponseEntity.ok(UNSUCCESSTEXT);
				}else {
					return ResponseEntity.badRequest().body(SUCCESSTEXT);
				}
			}else {
				return ResponseEntity.badRequest().body("nincs_koltseg");
			}
		}else {
			return ResponseEntity.badRequest().body(ISMERETLENTEXT);
		}
	}
	
	
	@PostMapping("/admin/elszamolas/dolgozotabla")
	public @ResponseBody ResponseEntity<String> ElszamolasDolgozoTabla(@RequestParam Map<String,String> allRequestDatas){
		if(allRequestDatas == null || allRequestDatas.get("honap").isEmpty() || allRequestDatas.get("ev").isEmpty() || allRequestDatas.get("id").isEmpty()) {
			throw new NullPointerException(NULLPOINTERTEXT);
		}
		String datum = "";
		if(!allRequestDatas.get("honap").equalsIgnoreCase("nincs")) {
			if(Integer.parseInt(allRequestDatas.get("honap")) < 10) {
				datum = allRequestDatas.get("ev")+"-0"+allRequestDatas.get("honap")+"%";
			}else {
				datum = allRequestDatas.get("ev")+"-"+allRequestDatas.get("honap")+"%";
			}
		}else {
			datum = allRequestDatas.get("ev")+"%";
		}
		ObjectMapper objectMapper = new ObjectMapper();
		String json = "";
        try {
            json = objectMapper.writeValueAsString(MunkaImp.elszamTablaDolgozoOsszesito(Long.parseLong(allRequestDatas.get("id")), datum));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
		return ResponseEntity.ok(json);
		
	}
	
	@PostMapping("/dolgozo/munka/tabla")
	public @ResponseBody ResponseEntity<ArrayList<Munka>> DolgozoTabla(@RequestParam Map<String,String> allRequestDatas, Principal principal){
		if(allRequestDatas == null) {
			throw new NullPointerException(NULLPOINTERTEXT);
		}
		if(UserImp.existsById(UserImp.findByEmail(principal.getName()).getId())) {
			String[] datumSplit;
			String datum = "";
			String datum2 = "";
			String jelenlegiDatum = "";
			if(Calendar.getInstance().get(Calendar.MONTH)+1 < 10) {
				jelenlegiDatum = String.valueOf(Calendar.getInstance().get(Calendar.YEAR))+"-0"+String.valueOf(Calendar.getInstance().get(Calendar.MONTH)+1);
			}else {
				jelenlegiDatum = String.valueOf(Calendar.getInstance().get(Calendar.YEAR))+"-"+String.valueOf(Calendar.getInstance().get(Calendar.MONTH)+1);
			}
			
			ArrayList<Munka> munkak = new ArrayList<>();
			for(UsersProjektek up : UserImp.findById(UserImp.findByEmail(principal.getName()).getId()).getUserpro()) {
				for(Munka mun : MunkaImp.findMunkaByKotesId(up.getId())) {
					if(allRequestDatas.get("tipus").equalsIgnoreCase("evhonaptab")) {
						datumSplit = mun.getDatum().split("-");
						if(allRequestDatas.get("honap").equalsIgnoreCase("nincs")) {
							datum = allRequestDatas.get("ev");
							datum2 = datumSplit[0];
						}else {
							datum = allRequestDatas.get("ev")+"-"+allRequestDatas.get("honap");
							datum2 = datumSplit[0]+"-"+datumSplit[1];
						}
						if(datum2.equalsIgnoreCase(datum)) {
							munkak.add(mun);
						}
					}else {
						datumSplit = mun.getDatum().split("-");
						datum = datumSplit[0]+"-"+datumSplit[1];
						if(datum.equalsIgnoreCase(jelenlegiDatum)) {
							munkak.add(mun);
						}
					}
				}
			}
			
			return ResponseEntity.ok(munkak);
		}else {
			return null;
		}
	}
	
	@PostMapping("/admin/dolgozo/munka/tabla")
	public @ResponseBody ResponseEntity<ArrayList<Munka>> AdminDolgozoTabla(@RequestParam Map<String,String> allRequestDatas){
		/*if(allRequestDatas == null || allRequestDatas.get("id").isEmpty() || allRequestDatas.get("tipus").isEmpty() || allRequestDatas.get("honap").isEmpty() || allRequestDatas.get("ev").isEmpty()) {
			throw new NullPointerException(NULLPOINTERTEXT);
		}*/
		if(UserImp.existsById(UserImp.findByEmail(UserImp.findById(Long.parseLong(allRequestDatas.get("id"))).getEmail()).getId())) {
			String[] datumSplit;
			String datum = "";
			String datum2 = "";
			String jelenlegiDatum = "";
			if(Calendar.getInstance().get(Calendar.MONTH)+1 < 10) {
				jelenlegiDatum = String.valueOf(Calendar.getInstance().get(Calendar.YEAR))+"-0"+String.valueOf(Calendar.getInstance().get(Calendar.MONTH)+1);
			}else {
				jelenlegiDatum = String.valueOf(Calendar.getInstance().get(Calendar.YEAR))+"-"+String.valueOf(Calendar.getInstance().get(Calendar.MONTH)+1);
			}
			
			ArrayList<Munka> munkak = new ArrayList<>();
			for(UsersProjektek up : UserImp.findById(UserImp.findByEmail(UserImp.findById(Long.parseLong(allRequestDatas.get("id"))).getEmail()).getId()).getUserpro()) {
				for(Munka mun : MunkaImp.findMunkaByKotesId(up.getId())) {
					if(allRequestDatas.get("tipus").equalsIgnoreCase("evhonaptab")) {
						datumSplit = mun.getDatum().split("-");
						if(allRequestDatas.get("honap").equalsIgnoreCase("nincs")) {
							datum = allRequestDatas.get("ev");
							datum2 = datumSplit[0];
						}else {
							datum = allRequestDatas.get("ev")+"-"+allRequestDatas.get("honap");
							datum2 = datumSplit[0]+"-"+datumSplit[1];
						}
						if(datum2.equalsIgnoreCase(datum)) {
							munkak.add(mun);
						}
					}else {
						datumSplit = mun.getDatum().split("-");
						datum = datumSplit[0]+"-"+datumSplit[1];
						if(datum.equalsIgnoreCase(jelenlegiDatum)) {
							munkak.add(mun);
						}
					}
				}
			}
			return ResponseEntity.ok(munkak);
		}else {
			return null;
		}
	}
	
	@GetMapping("/dolgozo/munka/ev")
	public @ResponseBody ResponseEntity<ArrayList<String>> DolgozoTablaEv(Principal principal){
			if(UserImp.existsById(UserImp.findByEmail(principal.getName()).getId())) {
				return ResponseEntity.ok(MunkaImp.findByUserId(UserImp.findByEmail(principal.getName()).getId()));
			}else {
				return null;
			}
	}
	
	@PostMapping("/admin/dolgozo/munka/ev")
	public @ResponseBody ResponseEntity<ArrayList<String>> AdminDolgozoTablaEv(@RequestParam Map<String,String> allRequestDatas){
		if(allRequestDatas == null || allRequestDatas.get("id").isEmpty()) {
			throw new NullPointerException(NULLPOINTERTEXT);
		}
		if(UserImp.existsById(UserImp.findById(Long.parseLong(allRequestDatas.get("id"))).getId())) {
			return ResponseEntity.ok(MunkaImp.findByUserId(UserImp.findById(Long.parseLong(allRequestDatas.get("id"))).getId()));
		}else {
			return null;
		}
	}
	
	@PostMapping("/dolgozo/munka/honap")
	public @ResponseBody ResponseEntity<ArrayList<String>> DolgozoTablaHonap(@RequestParam Map<String,String> allRequestDatas, Principal principal){
		if(allRequestDatas == null || allRequestDatas.get("ev").isEmpty()) {
			throw new NullPointerException(NULLPOINTERTEXT);
		}
		
		if(UserImp.existsById(UserImp.findByEmail(principal.getName()).getId()) && allRequestDatas.get("ev") != null) {
			return ResponseEntity.ok(MunkaImp.findHonapByUserId(UserImp.findByEmail(principal.getName()).getId(), allRequestDatas.get("ev")));
		}else {
			return ResponseEntity.badRequest().build();
		}
	}
	
	@PostMapping("/admin/dolgozo/munka/honap")
	public @ResponseBody ResponseEntity<ArrayList<String>> AdminDolgozoTablaHonap(@RequestParam Map<String,String> allRequestDatas){
		if(allRequestDatas == null || allRequestDatas.get("id").isEmpty() || allRequestDatas.get("ev").isEmpty()) {
			throw new NullPointerException(NULLPOINTERTEXT);
		}
		if(UserImp.existsById(UserImp.findById(Long.parseLong(allRequestDatas.get("id"))).getId()) && allRequestDatas.get("ev") != null) {
			return ResponseEntity.ok(MunkaImp.findHonapByUserId(Long.parseLong(allRequestDatas.get("id")), allRequestDatas.get("ev")));
		}else {
			return ResponseEntity.badRequest().build();
		}
	}
	
	@PostMapping("/admin/dolgozo/elszamolas")
	public @ResponseBody ResponseEntity<String> AdminDolgozoElszamolas(@RequestParam Map<String,String> allRequestDatas){
		if(allRequestDatas.size() == 0) {
			throw new NullPointerException(NULLPOINTERTEXT);
		}
		if(ElszamolasImp.save(allRequestDatas)) {
			return ResponseEntity.ok().body("sikeres");
		}else {
			return ResponseEntity.ok().body("sikertelen");
		}
	}
	
	@PostMapping("/admin/dolgozo/elszamolasok")
	public @ResponseBody ResponseEntity<Integer> AdminDolgozoElszamolasok(@RequestParam Map<String,String> allRequestDatas){
		if(allRequestDatas == null || allRequestDatas.get("mid").isEmpty()) {
			throw new NullPointerException(NULLPOINTERTEXT);
		}
		if(ElszamolasImp.findByMunkaId(Long.parseLong(allRequestDatas.get("mid")))) {
			return ResponseEntity.ok().body(1);
		}else {
			return ResponseEntity.ok().body(0);
		}
	}
	
	@PostMapping("/admin/ugyfel/projektek")
	public @ResponseBody ResponseEntity<Set<UgyfelekProjektek>> UgyfelekProjektAlapjan(@RequestParam Map<String,String> allRequestDatas){
		if(allRequestDatas == null || allRequestDatas.get("ugyid").isEmpty()) {
			throw new NullPointerException(NULLPOINTERTEXT);
		}
		Ugyfel ugyfel = UgyImp.findById(Long.parseLong(allRequestDatas.get("ugyid")));
		if(ugyfel.getUgypro() != null || ugyfel != null) {
			return ResponseEntity.ok().body(ugyfel.getUgypro());
		}else {
			return ResponseEntity.badRequest().build();
		}
	}
	
	@PostMapping("/admin/ugyfel/ev")
	public @ResponseBody ResponseEntity<String> UgyfelekEv(@RequestParam Map<String,String> allRequestDatas){
		if(allRequestDatas == null || allRequestDatas.get("projekt").isEmpty()) {
			throw new NullPointerException(NULLPOINTERTEXT);
		}
		String kuldes = "";
		if(MunkaImp.findProjektEvByProjektId(Long.parseLong(allRequestDatas.get("projekt"))) != null) {
			for(Integer ev : MunkaImp.findProjektEvByProjektId(Long.parseLong(allRequestDatas.get("projekt")))) {
				kuldes += ev+",";
			}
			if(kuldes != null || !kuldes.equalsIgnoreCase("") || kuldes.isEmpty()) {
				return ResponseEntity.ok(kuldes);
			}else {
				return ResponseEntity.badRequest().body(UNSUCCESSTEXT);
			}
		}else {
			return ResponseEntity.badRequest().body(ISMERETLENTEXT);
		}
	}
	
	@PostMapping("/admin/ugyfel/honap")
	public @ResponseBody ResponseEntity<String> UgyfelekHonap(@RequestParam Map<String,String> allRequestDatas){
		if(allRequestDatas == null || allRequestDatas.get("projekt").isEmpty() || allRequestDatas.get("ev").isEmpty()) {
			throw new NullPointerException(NULLPOINTERTEXT);
		}
		String kuldes = "";
		if(MunkaImp.findProjektHonapByProjektIdAndProjektEv(Long.parseLong(allRequestDatas.get("projekt")), allRequestDatas.get("ev")) != null) {
			for(Integer honap : MunkaImp.findProjektHonapByProjektIdAndProjektEv(Long.parseLong(allRequestDatas.get("projekt")), allRequestDatas.get("ev"))) {
				kuldes += honap+",";
			}
			if(kuldes != null || !kuldes.equalsIgnoreCase("") || !kuldes.isEmpty()) {
				return ResponseEntity.ok(kuldes);
			}else {
				return ResponseEntity.badRequest().body(UNSUCCESSTEXT);
			}
		}else {
			return ResponseEntity.badRequest().body(ISMERETLENTEXT);
		}
	}
	
	@PostMapping("/admin/ugyfel/elszamolas")
	public @ResponseBody ResponseEntity<String> UgyfelekElszamolas(@RequestParam Map<String, String> allRequestDatas){
		if(allRequestDatas == null || allRequestDatas.get("projekt").isEmpty() || allRequestDatas.get("ugyfel").isEmpty() || allRequestDatas.get("ev").isEmpty() || allRequestDatas.get("honap").isEmpty()) {
			throw new NullPointerException(NULLPOINTERTEXT);
		}
		if(ProImp.existsById(Long.parseLong(allRequestDatas.get("projekt")))) {
			if(UgyImp.existsById(Long.parseLong(allRequestDatas.get("ugyfel")))) {
				Long oraber = UgyProImp.findOrbarByProjektIdAndUgyfelId(Long.parseLong(allRequestDatas.get("projekt")), Long.parseLong(allRequestDatas.get("ugyfel")));
				Double raforditasok = MunkaImp.findSumRaforditasByProjektIdAndEvAndHonap(Long.parseLong(allRequestDatas.get("projekt")), allRequestDatas.get("ev"), allRequestDatas.get("honap"));
				String datum = "";
				String kuldes = "";
				if(Integer.parseInt(allRequestDatas.get("honap")) < 10) {
					datum = allRequestDatas.get("ev") +"-0"+ allRequestDatas.get("honap")+"%";
				}else {
					datum = allRequestDatas.get("ev") +"-"+ allRequestDatas.get("honap")+"%";
				}
				if(oraber != null && raforditasok != null && datum != null ) {
					Long egyebOsszeg = KoltsegImp.findSumKoltsegByCreatedAt(datum,Long.parseLong(allRequestDatas.get("projekt")));
					if(egyebOsszeg == null) {
						kuldes = oraber +","+ raforditasok+",0";
					}else {
						kuldes = oraber +","+ raforditasok+","+egyebOsszeg;
					}
					return ResponseEntity.ok(kuldes);
				}else {
					return ResponseEntity.badRequest().body(UNSUCCESSTEXT);
				}
			}else {
				return ResponseEntity.badRequest().body(ISMERETLENTEXT);
			}
		}else {
			return ResponseEntity.badRequest().body(ISMERETLENTEXT);
		}
	}
}
