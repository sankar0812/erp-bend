package com.example.erp.repository.eRecruitments;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.erp.entity.eRecruitments.TraineeDetails;

public interface TraineeDetailsRepository extends JpaRepository<TraineeDetails, Long>{

	long countByStatusTrue();

}
