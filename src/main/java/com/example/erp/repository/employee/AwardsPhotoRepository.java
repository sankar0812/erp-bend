package com.example.erp.repository.employee;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.erp.entity.employee.AwardsPhoto;



public interface AwardsPhotoRepository extends JpaRepository<AwardsPhoto, Long>{

	List<AwardsPhoto> findByAwardsAwardsId(long awardsId);

}
