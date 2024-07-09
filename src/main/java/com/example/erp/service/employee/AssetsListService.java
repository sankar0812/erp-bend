package com.example.erp.service.employee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.erp.entity.employee.Assets;
import com.example.erp.entity.employee.AssetsList;
import com.example.erp.repository.employee.AssetsListRepository;



@Service
public class AssetsListService {
	
	@Autowired
	private AssetsListRepository  repo;
	
	
	public void save(AssetsList attendancelist) {
		repo.save(attendancelist);
	}
	public AssetsList findById(Long attendanceListId) {
		return repo.findById(attendanceListId).get();
	}
	
	

}
