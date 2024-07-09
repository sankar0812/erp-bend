package com.example.erp.service.eRecruitment;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.erp.entity.employee.Complaints;
import com.example.erp.entity.erecruitment.Training;
import com.example.erp.repository.erecruitment.TrainingRepository;


@Service
public class TrainingService {
@Autowired
private TrainingRepository trainingRepository;

//view
	public List<Training> listAll() {
		return this.trainingRepository.findAll();
	}

//save
	public void SaveTrainee(Training training) {
		trainingRepository.save(training);
	}

//edit
	public Training findById(Long id) {
		return trainingRepository.findById(id).get();
	}

//delete
	public void deleteTrainingId(Long id) {
		trainingRepository.deleteById(id);
	}

	   public Optional<Training> getById1(long id) {
	        return Optional.of(trainingRepository.findById(id).get());
	    }

}
