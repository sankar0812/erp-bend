package com.example.erp.service.clientDetails;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.erp.entity.clientDetails.ClientRequirement;
import com.example.erp.repository.clientDetails.ClientRequirementRepository;

@Service
public class ClientRequirementService {
	@Autowired
	private ClientRequirementRepository clientRequirmentRepository;

//view
	public List<ClientRequirement> listAll() {
		return this.clientRequirmentRepository.findAll();
	}
	
//	public ClientRequirement getByClientId(long id) {
//		return clientRequirmentRepository.findByClientId(id).get();
//	}

	   public List<ClientRequirement> getByClientId(long clientId) {
	        return clientRequirmentRepository.findByProjectId(clientId);
	    }
//save
	public ClientRequirement SaveClientRequirmentDetails(ClientRequirement clientRequirment) {
		return clientRequirmentRepository.save(clientRequirment);
	}
	public ClientRequirement getById(long id) {
		return clientRequirmentRepository.findById(id).get();
	}

	// delete
	public void deleteClientRequirmentId(Long id) {
		clientRequirmentRepository.deleteById(id);
	}
}
