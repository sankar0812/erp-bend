package com.example.erp.service.eRecruitment;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.erp.entity.erecruitment.InterviewProcess;
import com.example.erp.repository.erecruitment.InterviewProcessRepository;

@Service
public class InterviewProcessService {
@Autowired
private InterviewProcessRepository interviewProcessRepository;

//view
public List<InterviewProcess> listAll() {
	return this.interviewProcessRepository.findAll();
}

//save
public void SaveInterviewProcess(InterviewProcess interviewProcess) {
	interviewProcessRepository.save(interviewProcess);
}

//edit
public InterviewProcess findById(Long id) {
	return interviewProcessRepository.findById(id).get();
}

//delete
public void deleteInterviewProcessId(Long id) {
	interviewProcessRepository.deleteById(id);
}


}
