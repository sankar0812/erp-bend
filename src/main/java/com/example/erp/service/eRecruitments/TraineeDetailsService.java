package com.example.erp.service.eRecruitments;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.erp.entity.eRecruitments.TraineeDetails;
import com.example.erp.repository.eRecruitments.TraineeDetailsRepository;

@Service
public class TraineeDetailsService {
	@Autowired
	private TraineeDetailsRepository traineeDetailsRepository;

//view
	public List<TraineeDetails> listAll() {
		return this.traineeDetailsRepository.findAll();
	}

//save
	public TraineeDetails SaveTraineeDetails(TraineeDetails trainee) {
		return traineeDetailsRepository.save(trainee);
	}

	public TraineeDetails findById(Long traineeId) {
		return traineeDetailsRepository.findById(traineeId).get();
	}

	// delete
	public void deleteTraineeIdId(Long id) {
		traineeDetailsRepository.deleteById(id);
	}
}
