package com.example.erp.service.accounting;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.erp.entity.accounting.ServerMaintenance;
import com.example.erp.repository.accounting.ServerMaintenanceRepository;



@Service
public class ServerMaintenanceService {
	
	
	
	@Autowired
	private ServerMaintenanceRepository Repo;

	public Iterable<ServerMaintenance> listAll() {
		return this.Repo.findAll();

	}

	public void SaveorUpdate(ServerMaintenance ServerMaintenance) {
		Repo.save(ServerMaintenance);
	}

	public void save(ServerMaintenance ServerMaintenance) {
		Repo.save(ServerMaintenance);

	}

	public ServerMaintenance findById(Long ServerMaintenance_id) {
		return Repo.findById(ServerMaintenance_id).get();

	}

	public void deleteAssestIdById(Long ServerMaintenance_id) {
		Repo.deleteById(ServerMaintenance_id);
	}

	public Optional<ServerMaintenance> getServerListById(Long ServerMaintenance_id) {
		return Repo.findById(ServerMaintenance_id);

	}

	public ServerMaintenance getById(long id) {
		return Repo.findById(id).get();
	}
	

}
