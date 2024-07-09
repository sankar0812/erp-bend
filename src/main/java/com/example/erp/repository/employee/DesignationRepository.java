package com.example.erp.repository.employee;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.erp.entity.employee.Designation;


public interface DesignationRepository  extends JpaRepository<Designation, Long> {

}
