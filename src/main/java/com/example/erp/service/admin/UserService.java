package com.example.erp.service.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.erp.entity.admin.User;
import com.example.erp.entity.clientDetails.ClientProfile;
import com.example.erp.repository.admin.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	public List<User> getTrueUsers() {
		return userRepository.findByStatusTrue();
	}
	
	
	public List<User> getFalseUsers() {
		return userRepository.findByStatusFalse();
	}

	public void saveSingleUser(User user) {
		this.userRepository.save(user);
	}
	public List<User> listAll() {
		return this.userRepository.findAll();
	}
	//////// delete/////////
	public void deleteUserById(Long id) {
		userRepository.deleteById(id);

	}

	/////// edit///////
	public User findUserById(Long id) {
		return userRepository.findById(id).get();
	}

	public void deleteCompanyById(Long userId) {
		userRepository.deleteById(userId);
	}

	public User getCompanyById(Long companyId) {
		return userRepository.findById(companyId).orElse(null);
	}

	public User getUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	public User getUserByMobileNumber(String MobileNumber) {
		return userRepository.findByMobileNumber(MobileNumber);
	}
	public List<User> getUserById(Long userId) {
		return userRepository.findByUserId(userId);
	}

}
