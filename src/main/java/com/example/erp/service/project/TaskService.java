package com.example.erp.service.project;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.erp.entity.project.Task;
import com.example.erp.repository.project.TaskRepository;

@Service
public class TaskService {

	@Autowired
	private TaskRepository taskRepository;

	// view
	public List<Task> listTask() {
		return this.taskRepository.findAll();
	}

	// save
	public Task SaveTaskDetails(Task task) {
		return taskRepository.save(task);
	}

	public Task findProjectListDemoById(Long projectListDemoId) {
		return taskRepository.findById(projectListDemoId).get();
	}

	// delete
	public void deleteProjectListDemoById(Long id) {
		taskRepository.deleteById(id);
	}
}
