package com.example.erp.service.admin;

import org.springframework.util.StreamUtils;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.sql.Blob;
import java.sql.SQLException;
import javax.sql.rowset.serial.SerialBlob;
import com.example.erp.entity.admin.AdminLogin;
import com.example.erp.repository.admin.AdminLoginRepository;
import io.jsonwebtoken.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.MalformedInputException;
@Service
public class AdminLoginService {

	@Autowired
	private AdminLoginRepository adminLoginRepository;

	public void addAdminLoginService() {
	    // First AdminLogin
	    AdminLogin login = new AdminLogin();
	    login.setId(1);
	    login.setEmail("hrm.ideaux@gmail.com");
	    login.setPassword("ideaux@9865");
	    login.setConfirmPassword("ideaux@9865");
	    login.setName("ideaux");
	    login.setRoleId(1);
	    login.setStatus(true);
	    login.setRoleType("Admin");
	    try {
	        URL imageUrl = new URL("https://cdn.pixabay.com/photo/2020/07/14/13/07/icon-5404125_1280.png");
	        HttpURLConnection connection = (HttpURLConnection) imageUrl.openConnection();
	        int responseCode = connection.getResponseCode();

	        if (responseCode == HttpURLConnection.HTTP_OK) {
	            byte[] imageBytes = StreamUtils.copyToByteArray(connection.getInputStream());
	            Blob imageBlob = new SerialBlob(imageBytes);
	            login.setImage(imageBlob);
	            
	            // Save the entity with the image to the repository
	            adminLoginRepository.save(login);
	        } else {
	            System.err.println("Failed to download image. HTTP error code: " + responseCode);
	        }

	    } catch (MalformedInputException e) {
	        // Handle URL format issues
	        e.printStackTrace();
	    } catch (IOException e) {
	        // Handle issues with opening the stream or reading data
	        e.printStackTrace();
	    } catch (SQLException e) {
	        // Handle issues with the Blob or database operations
	        e.printStackTrace();
	    } catch (Exception e) {
	        // Catch any other unexpected exceptions
	        e.printStackTrace();
	    }


	    // Second AdminLogin
	    AdminLogin superAdmin = new AdminLogin();
	    superAdmin.setId(2);
	    superAdmin.setEmail("superadmin@gmail.com");
	    superAdmin.setPassword("123456");
	    superAdmin.setConfirmPassword("123456");
	    superAdmin.setName("vijay");
	    superAdmin.setStatus(true);
	    superAdmin.setRoleId(9);
	    superAdmin.setRoleType("SuperAdmin");
	    try {
	        // Download the image from the URL
	        URL imageUrl = new URL("https://cdn.pixabay.com/photo/2014/03/24/17/19/teacher-295387_1280.png");
	        byte[] imageBytes = StreamUtils.copyToByteArray(imageUrl.openStream());
	        Blob imageBlob = new SerialBlob(imageBytes);
	        superAdmin.setImage(imageBlob);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    adminLoginRepository.save(superAdmin);
	}


	public boolean authenticateUser(String email, String password) {
		AdminLogin user = adminLoginRepository.findByEmail(email);
		if (user != null) {
			return compareRawPasswords(password, user.getPassword());
		}
		return false;
	}

	private boolean compareRawPasswords(String rawPassword, String storedPassword) {
		return rawPassword.equals(storedPassword);
	}

	public List<AdminLogin> listAll() {
		return adminLoginRepository.findAll();
	}

	public void saveOrUpdate(AdminLogin complaints) {
		adminLoginRepository.save(complaints);
	}

	public AdminLogin getById(long id) {
		return adminLoginRepository.findById(id).get();
	}

	public void deleteById(long id) {
		adminLoginRepository.deleteById(id);
	}

	public Optional<AdminLogin> getById1(long id) {
		return Optional.of(adminLoginRepository.findById(id).get());
	}
}
