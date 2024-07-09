package com.example.erp.service.accounting;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.erp.entity.accounting.CompanyAssets;
import com.example.erp.entity.accounting.Server;
import com.example.erp.repository.accounting.ServerRepository;



@Service
public class ServerService {
	
	@Autowired
	private ServerRepository  Repo;

	public Iterable<Server> listAll() {
		return this.Repo.findAll();

	}

	public void SaveorUpdate(Server server) {
		Repo.save(server);
	}

	public void save(Server server) {
		Repo.save(server);

	}

	public Server findById(long serverId) {
		return Repo.findById(serverId).get();

	}

	public void deleteAssestIdById(long server_id) {
		Repo.deleteById(server_id);
	}

	 public Server getById(long id) {
	        return Repo.findById(id).get();
	    }



		public Optional<Server> getServerById(Long server_id) {
			return Repo.findById(server_id);

		}
	

}



