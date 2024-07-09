package com.example.erp.repository.employee;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


import com.example.erp.entity.employee.AssetsList;



public interface AssetsListRepository extends JpaRepository<AssetsList, Long>{

	List<AssetsList> findAllByAccessoriesIdAndBrandId(Long accessoriesId, Long brandId);



}
