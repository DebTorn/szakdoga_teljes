package com.munkanyilvantarto.serviceimp;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.munkanyilvantarto.entity.Role;
import com.munkanyilvantarto.entity.TokenGenerator;
import com.munkanyilvantarto.entity.Ugyfel;
import com.munkanyilvantarto.entity.User;
import com.munkanyilvantarto.repository.RoleRepository;
import com.munkanyilvantarto.repository.UserRepository;
import com.munkanyilvantarto.service.UserService;

@Service
public class UserServiceImplementation implements UserService, UserDetailsService {

	private UserRepository userRepository;
	private RoleRepository roleRepository;
	private EmailServiceImplementation EmailImp;
	
	private final String USER_ROLE = "USER";
	
	@Autowired
	public UserServiceImplementation(UserRepository userRepository, RoleRepository roleRepository, EmailServiceImplementation emailImp){
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.EmailImp = emailImp;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = findByEmail(email);
		if( user == null ){
			throw new UsernameNotFoundException(email);
		}
		
		return new UserDetailsImplementation(user);
	}

	@Override
	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	public ArrayList<User> getAllUser() {
		return userRepository.findAll();
	}

	@Override
	public boolean save(Map<String, String> userRequestDatas) {
		
		User user = new User();
		Role role = roleRepository.findByName(USER_ROLE);
		
		TokenGenerator token = new TokenGenerator();
		token.setToken();
		
		if(role != null) {
			user.getRoles().add(role);
		}else {
			user.setRole(USER_ROLE);
		}

		user.setTeljesnev(userRequestDatas.get("nev"));
		user.setAlaporaber(Long.parseLong(userRequestDatas.get("oraber")));
		user.setBruttoszorzo(Double.parseDouble(userRequestDatas.get("szorzo")));
		user.setEmail(userRequestDatas.get("email"));
		user.setPassword(userRequestDatas.get("jelszo"));
		user.setAktiv(3);
		user.setToken(token.getToken());
		user.setCreated_at(new Timestamp(new Date().getTime()));
		
		if(userRepository.save(user) != null) {
			EmailImp.SendAuthMail(user);
			return true;
		}else {
			return false;
		}
		
	}
	
	@Override
	public boolean aktivitas(Map<String, String> allRequestDatas) {
		
		User user = userRepository.findUserById(Long.parseLong(allRequestDatas.get("id")));
		
		user.setAktiv(Integer.parseInt(allRequestDatas.get("aktiv")));
		user.setCreated_at(user.getCreated_at());
		
		if(userRepository.save(user) == null) {
			return false;
		}else {
			return true;
		}

	}

	@Override
	public User findById(Long id) {
		return userRepository.findUserById(id);
	}

	@Override
	public boolean update(Map<String, String> allRequestsDatas) {
		
		User user = userRepository.findUserById(Long.parseLong(allRequestsDatas.get("id")));
		
		user.setAlaporaber(Long.parseLong(allRequestsDatas.get("oraber")));
		user.setBruttoszorzo(Double.parseDouble(allRequestsDatas.get("szorzo")));
		user.setTeljesnev(allRequestsDatas.get("nev"));
		user.setEmail(allRequestsDatas.get("email"));
		user.setUpdated_at(new Timestamp(new Date().getTime()));
		user.setCreated_at(user.getCreated_at());
		
		if(userRepository.save(user) != null) {
			return true;
		}else {
			return false;
		}
		
	}

	@Override
	public boolean existsById(Long id) {
		return userRepository.existsById(id);
	}

	@Override
	public boolean simaSave(User user) {
		
		if(user != null) {
			if(userRepository.save(user) != null) {
				return true;
			}else {
				return false;
			}
		}else {
			return false;
		}
	}

	@Override
	public User findByToken(String token) {
		return userRepository.findByToken(token);
	}
	
}
