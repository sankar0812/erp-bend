package com.example.erp.service.accounting;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.erp.entity.accounting.CompanyAssets;
import com.example.erp.repository.accounting.CompanyAssetsRepository;


@Service
public class CompanyAssetsService {
	@Autowired
	private CompanyAssetsRepository repo;

	
	public List<CompanyAssets> listAll() {
		return this.repo.findAll();

	}

	public void SaveorUpdate(CompanyAssets announcement) {
		repo.save(announcement);
	}

	public void save(CompanyAssets announcement) {
		repo.save(announcement);

	}

	public CompanyAssets findById(Long companyAssets_id) {
		return repo.findById(companyAssets_id).get();
	}
	
//	public CompanyAssets findByAccessoriesIdAndBrantId(Long accessoriesId, Long brantId) {
//		return repo.findByAccessoriesIdAndBrantId(accessoriesId, brantId);
//	}

	public void deleteCompanyAssetsIdById(Long companyAssets_id) {
		repo.deleteById(companyAssets_id);
	}

	public Optional<CompanyAssets> getCompanyAssetsById(Long companyAssets_id) {
		return repo.findById(companyAssets_id);

	}


}
