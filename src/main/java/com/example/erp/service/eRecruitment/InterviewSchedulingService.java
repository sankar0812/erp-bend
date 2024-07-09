package com.example.erp.service.eRecruitment;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.erp.entity.erecruitment.InterviewSchedule;
import com.example.erp.repository.erecruitment.InterviewSchedulingRepository;

@Service
public class InterviewSchedulingService {
@Autowired
private InterviewSchedulingRepository interviewSchedulingRepository;


//view
	public List<InterviewSchedule> listAll() {
		return this.interviewSchedulingRepository.findAll();
	}

//save
	public void save(InterviewSchedule interviewSchedule) {
		interviewSchedulingRepository.save(interviewSchedule);
	}

//edit
	public InterviewSchedule getByCandidateId(Long id) {
		return interviewSchedulingRepository.findBycandidateId(id).get();
	}

//delete
	public void deleteInterviewSchedulingId(Long id) {
		interviewSchedulingRepository.deleteById(id);
	}




}
