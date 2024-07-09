package com.example.erp.repository.accounting;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.erp.entity.accounting.CompanyAssetsList;

public interface CompanyAssestListRepository extends JpaRepository<CompanyAssetsList, Long> {

	List<CompanyAssetsList> findByAccessoriesIdAndBrandId(Long accessoriesId, Long brandId);

}
