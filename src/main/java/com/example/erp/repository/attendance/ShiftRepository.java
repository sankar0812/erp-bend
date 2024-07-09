package com.example.erp.repository.attendance;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.erp.entity.attendance.Shift;

public interface ShiftRepository extends JpaRepository<Shift, Long> {

}
