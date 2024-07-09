package com.example.erp.service.accounting;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.erp.entity.accounting.Server;
import com.example.erp.entity.accounting.ServerList;
import com.example.erp.repository.accounting.ServerListRepository;

@Service
public class ServerListService {
	@Autowired
	private ServerListRepository Repo;

	public Iterable<ServerList> listAll() {
		return this.Repo.findAll();

	}

	public void SaveorUpdate(ServerList ServerList) {
		Repo.save(ServerList);
	}

	public void save(ServerList ServerList) {
		Repo.save(ServerList);

	}

	public ServerList findById(Long ServerList_id) {
		return Repo.findById(ServerList_id).get();

	}

	public void deleteAssestIdById(Long ServerList_id) {
		Repo.deleteById(ServerList_id);
	}

	public Optional<ServerList> getServerListById(Long ServerList_id) {
		return Repo.findById(ServerList_id);

	}

	public ServerList getById(long id) {
		return Repo.findById(id).get();
	}

	public Optional<ServerList> findServertListById(Long productListId) {
		return Repo.findById(productListId);
	}

}
