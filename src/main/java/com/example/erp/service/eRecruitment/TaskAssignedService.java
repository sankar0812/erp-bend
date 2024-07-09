package com.example.erp.service.eRecruitment;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.erp.entity.erecruitment.TaskAssigned;
import com.example.erp.repository.erecruitment.TaskAssignedRepository;

@Service
public class TaskAssignedService {
@Autowired
private TaskAssignedRepository taskAssignedRepository;

//view
	public List<TaskAssigned> listAll() {
		return this.taskAssignedRepository.findAll();
	}

//save
	public TaskAssigned SaveTaskAssigned(TaskAssigned taskAssigned) {
		return taskAssignedRepository.save(taskAssigned);
	}
	
	public TaskAssigned getByCandidateId(Long candidateid) {
		return taskAssignedRepository.findBycandidateId(candidateid).get();
	}

	//delete
		public void deleteTaskAssignedId(Long id) {
			taskAssignedRepository.deleteById(id);
		}

		public Optional<TaskAssigned> getByCandidateId1(long candidateId) {
		    return taskAssignedRepository.findById(candidateId);
		}

}
