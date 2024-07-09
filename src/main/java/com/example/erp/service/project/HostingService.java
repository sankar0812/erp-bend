package com.example.erp.service.project;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.erp.entity.project.Hosting;
import com.example.erp.repository.project.HostingRepository;

@Service
public class HostingService {

	@Autowired
	private HostingRepository hostingRepository;
	
	// view
			public List<Hosting> listHosting() {
				return this.hostingRepository.findAll();
			}

			// save
			public Hosting SaveHostingDetails(Hosting hosting) {
				return hostingRepository.save(hosting);
			}

			public Hosting findHostingId(Long hostingId) {
				return hostingRepository.findById(hostingId).get();
			}

			// delete
			public void deleteHostingById(Long id) {
				hostingRepository.deleteById(id);
			}
}
