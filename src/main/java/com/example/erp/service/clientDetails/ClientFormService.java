package com.example.erp.service.clientDetails;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.erp.entity.clientDetails.ClientForm;
import com.example.erp.repository.clientDetails.ClientFormRepository;

@Service
public class ClientFormService {

	@Autowired
	private ClientFormRepository clientFormRepository;

	// view
	public List<ClientForm> listClientProjects() {
		return this.clientFormRepository.findAll();
	}

	// save
	public void SaveClientProject(ClientForm projectType) {
		clientFormRepository.save(projectType);
	}

	// edit
	public ClientForm findByClientProjectId(Long id) {
		return clientFormRepository.findById(id).get();
	}

	// delete
	public void deleteClientProjectById(Long id) {
		clientFormRepository.deleteById(id);
	}
}
