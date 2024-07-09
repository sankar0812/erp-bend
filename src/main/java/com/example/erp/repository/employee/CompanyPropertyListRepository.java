package com.example.erp.repository.employee;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.erp.entity.accounting.CompanyAssetsList;
import com.example.erp.entity.employee.CompanyPropertyList;



public interface CompanyPropertyListRepository extends JpaRepository<CompanyPropertyList, Long>{

	List<CompanyAssetsList> findAllByAccessoriesIdAndBrandId(Long accessoriesId, Long brandId);

}
