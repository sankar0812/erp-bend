package com.example.erp.controller.project;

import java.sql.Date;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.erp.entity.project.TaskList;
import com.example.erp.service.project.TaskListService;

@RestController
@CrossOrigin
public class TaskListController {

	@Autowired
	private TaskListService taskListService;

	@GetMapping("/taskListDetail/view")
	public ResponseEntity<Object> getTaskListDetails(@RequestParam(required = true) String taskList) {
		if ("taskListDetails".equals(taskList)) {
			return ResponseEntity.ok(taskListService.listAll());
		} else {
			String errorMessage = "Invalid value for ' taskList'. Expected ' taskListDetails'.";
			return ResponseEntity.badRequest().body(errorMessage);
		}
	}

	@DeleteMapping("/taskList/delete/{id}")
	public ResponseEntity<String> deleteprojectTypeListId(@PathVariable("id") Long projectTypeListId) {
		taskListService.deleteprojectTypeListById(projectTypeListId);
		return ResponseEntity.ok("TaskList detail deleted successfully With Id :" + projectTypeListId);

	}

	@PutMapping("/taskList/edit/{id}")
	public ResponseEntity<?> updateResearch(@PathVariable("id") Long taskListId, @RequestBody TaskList taskList) {
		try {
			TaskList existingTaskList = taskListService.findTaskListById(taskListId);

			if (existingTaskList == null) {
				return ResponseEntity.notFound().build();
			}

//			if (existingTaskList.isCompleted()) {
//				String errorMessage = "A task is completed";
//				return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMessage);
//			}
//
//			if (existingTaskList.isCancelled()) {
//				String errorMessage = "A task is cancelled";
//				return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMessage);
//			}
			if (existingTaskList.getStartDate() != null && existingTaskList.getUpdated() != null
					&& existingTaskList.getStartDate().after(existingTaskList.getUpdated())) {
				String errorMessage = "FromDate cannot be later than ToDate.";
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
			}


			existingTaskList.setCancellationReason(taskList.getCancellationReason());
			existingTaskList.setHoldReson(taskList.getHoldReson());
			existingTaskList.setCategory(taskList.getCategory());
			existingTaskList.setComments(taskList.getComments());
			existingTaskList.setCompletedDate(taskList.getCompletedDate());
			existingTaskList.setEmployeeId(taskList.getEmployeeId());
			existingTaskList.setTraineeId(taskList.getTraineeId());
			existingTaskList.setLabel(taskList.getLabel());
			existingTaskList.setPriority(taskList.getPriority());
			existingTaskList.setSummary(taskList.getSummary());
			existingTaskList.setType(taskList.getType());
			existingTaskList.setStartDate(taskList.getStartDate());
			existingTaskList.setUpdated(taskList.getUpdated());
			taskListService.SaveTaskList(existingTaskList);
			return ResponseEntity.ok(existingTaskList);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@PutMapping("/taskList/status/edit/{id}")
	public ResponseEntity<?> updateStatus(@PathVariable("id") Long taskListId, @RequestBody TaskList taskList) {
		try {
			TaskList existingTaskList = taskListService.findTaskListById(taskListId);

			if (existingTaskList == null) {
				return ResponseEntity.notFound().build();
			}

			if (existingTaskList.isCompleted()) {
				String errorMessage = "A task is completed";
				return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMessage);
			}

			if (existingTaskList.isCancelled()) {
				String errorMessage = "A task is cancelled";
				return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMessage);
			}
			existingTaskList.setProjectStatus(taskList.getProjectStatus());

			long currentTimeMillis = System.currentTimeMillis();
			Date currentDate = new Date(currentTimeMillis);

			if (taskList.getProjectStatus().equals("completed")) {
				existingTaskList.setCompleted(true);
				existingTaskList.setPending(false);
				existingTaskList.setOnProcess(false);
				existingTaskList.setTodo(false);
				existingTaskList.setHold(false);
				existingTaskList.setCancelled(false);
				existingTaskList.setCompletedDate(currentDate);
			} else if (taskList.getProjectStatus().equals("cancelled")) {
				existingTaskList.setCancelled(true);
				existingTaskList.setCompleted(false);
				existingTaskList.setPending(false);
				existingTaskList.setOnProcess(false);
				existingTaskList.setTodo(false);
				existingTaskList.setHold(false);			
				
//				existingTaskList.setCompletedDate(currentDate);
			} else if (taskList.getProjectStatus().equals("pending")) {
				existingTaskList.setPending(true);
				existingTaskList.setCompleted(false);
				existingTaskList.setOnProcess(false);
				existingTaskList.setTodo(false);
				existingTaskList.setHold(false);
				existingTaskList.setCancelled(false);
//				existingTaskList.setCompletedDate(currentDate);
			} else if (taskList.getProjectStatus().equals("onProcess")) {
				existingTaskList.setOnProcess(true);
			    existingTaskList.setOnProcessDate(LocalDate.now());
				existingTaskList.setCompleted(false);
				existingTaskList.setTodo(false);
				existingTaskList.setHold(false);
				existingTaskList.setCancelled(false);
				existingTaskList.setPending(false);
			} else if (taskList.getProjectStatus().equals("todo")) {
				existingTaskList.setTodoDate(LocalDate.now());
				existingTaskList.setTodo(true);
				existingTaskList.setOnProcess(false);
				existingTaskList.setCompleted(false);
				existingTaskList.setHold(false);
				existingTaskList.setCancelled(false);
				existingTaskList.setPending(false);
//				existingTaskList.setCompletedDate(currentDate);
			} else if (taskList.getProjectStatus().equals("hold")) {
				existingTaskList.setHoldDate(LocalDate.now());
				existingTaskList.setHold(true);
				existingTaskList.setTodo(false);
				existingTaskList.setOnProcess(false);
				existingTaskList.setCompleted(false);
				existingTaskList.setCancelled(false);
				existingTaskList.setPending(false);
//				existingTaskList.setCompletedDate(currentDate);
			}

			taskListService.SaveTaskList(existingTaskList);
			return ResponseEntity.ok(existingTaskList);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

}
