package com.example.erp.service.accounting;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.erp.entity.accounting.ServerType;
import com.example.erp.repository.accounting.ServerTypeRepository;



@Service
public class ServerTypeService {
	@Autowired
	private ServerTypeRepository repo;

	public Iterable<ServerType> listAll() {
		return this.repo.findAll();

	}

	public void SaveorUpdate(ServerType serverType) {
		repo.save(serverType);
	}

	public void save(ServerType serverType) {
		repo.save(serverType);

	}

	public ServerType findById(Long serverType) {
		return repo.findById(serverType).get();

	}

	public void deleteDepartmentRollById(Long serverType) {
		repo.deleteById(serverType);
	}

	public Optional<ServerType> getdepartmentRollById(Long serverType) {
		return repo.findById(serverType);

	}

	public void deleteById(Long serverTypeId) {
		repo.deleteById(serverTypeId);

	}

}
