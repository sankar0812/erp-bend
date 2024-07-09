package com.example.erp.service.attendance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.erp.entity.attendance.ShiftType;
import com.example.erp.repository.attendance.ShiftTypeRepository;

@Service
public class ShiftTypeService {

	@Autowired
	private ShiftTypeRepository shiftTypeRepository;

	public void addShiftTypeService() {

		ShiftType regular = new ShiftType();
		regular.setShiftTypeId(1);
		regular.setShiftName("Regular");
		shiftTypeRepository.save(regular);

		ShiftType shift = new ShiftType();
		shift.setShiftTypeId(2);
		shift.setShiftName("Shift");
		shiftTypeRepository.save(shift);

	}
}
