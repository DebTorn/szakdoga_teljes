package com.munkanyilvantarto.repository;

import java.util.ArrayList;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.munkanyilvantarto.entity.User;

public interface UserRepository extends CrudRepository<User, Long> {

	public User findByEmail(String email);
	
	public ArrayList<User> findAll();
	
	public User findUserById(Long id);
	
	public User findByToken(String token);
	
}
