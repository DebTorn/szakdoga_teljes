package com.munkanyilvantarto.serviceimp;

import java.security.SecureRandom;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.munkanyilvantarto.entity.Role;
import com.munkanyilvantarto.entity.User;

public class UserDetailsImplementation implements UserDetails{

	private static final long serialVersionUID = 976945489012604288L;
	
	private User user;

	public UserDetailsImplementation(User user) {
		this.user = user;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
		Set<Role> roles = user.getRoles();
		for (Role role : roles) {
			authorities.add(new SimpleGrantedAuthority(role.getName()));
		}
		return authorities;
	}

	@Override
	public String getPassword() {
		PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder(); 
        return encoder.encode(this.user.getPassword());
//		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(15, new SecureRandom());
//        return bCryptPasswordEncoder.encode(this.user.getPassword());
	}

	@Override
	public String getUsername() {
		return user.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		if(user.getAktiv() == 1) {
			return true;
		}else {
			return false;
		}
		
	}

}
