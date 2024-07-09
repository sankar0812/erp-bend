package com.example.erp.service.accounting;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.erp.entity.accounting.CompanyAssetsList;
import com.example.erp.repository.accounting.CompanyAssestListRepository;

@Service
public class CompanyAssestListService {

	@Autowired
	private CompanyAssestListRepository companyAssestListRepository;
	
	public void SaveorUpdate(CompanyAssetsList companyAssestList) {
		companyAssestListRepository.save(companyAssestList);
	}

	
//	public CompanyAssetsList findByAccessoriesIdAndBrandId(Long accessoriesId, Long brandId) {
//		return companyAssestListRepository.findByAccessoriesIdAndBrandId(accessoriesId, brandId);
//	}


	public List<CompanyAssetsList> findAllByAccessoriesIdAndBrandId(Long accessoriesId, Long brandId) {
		return companyAssestListRepository.findByAccessoriesIdAndBrandId(accessoriesId, brandId);
	}

}
