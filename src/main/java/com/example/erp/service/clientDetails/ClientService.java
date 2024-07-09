package com.example.erp.service.clientDetails;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.erp.entity.clientDetails.ClientProfile;
import com.example.erp.entity.employee.Complaints;
import com.example.erp.repository.clientDetails.ClientRepository;

@Service
public class ClientService {
	@Autowired
	private ClientRepository clientRepository;

//view
	public List<ClientProfile> listAll() {
		return this.clientRepository.findAll();
	}

//save
	public void SaveClientProfile(ClientProfile client) {
		clientRepository.save(client);
	}

//edit
	public ClientProfile findById(Long id) {
		return clientRepository.findById(id).get();
	}

//delete
	public void deleteClientId(Long id) {
		clientRepository.deleteById(id);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	public Optional<ClientProfile> getById1(long id) {
        return Optional.of(clientRepository.findById(id).get());
    }
}
