package com.example.erp.service.project;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.erp.entity.project.TaskList;
import com.example.erp.repository.project.TaskListRepository;

@Service
public class TaskListService {

	@Autowired
	private TaskListRepository taskListRepository;

	// view
	public List<TaskList> listAll() {
		return this.taskListRepository.findAll();
	}

	// save
	public TaskList SaveTaskList(TaskList taskList) {
		return taskListRepository.save(taskList);
	}

	public TaskList findTaskListById(Long projectTypeListId) {
		return taskListRepository.findById(projectTypeListId).get();
	}

	// delete
	public void deleteprojectTypeListById(Long id) {
		taskListRepository.deleteById(id);
	}
}
