package com.example.erp.repository.project;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.erp.entity.project.TaskList;

public interface TaskListRepository extends JpaRepository<TaskList, Long>{

}
