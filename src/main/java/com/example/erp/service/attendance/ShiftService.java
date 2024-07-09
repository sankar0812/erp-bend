package com.example.erp.service.attendance;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.erp.entity.attendance.Shift;
import com.example.erp.repository.attendance.ShiftRepository;

@Service
public class ShiftService {

	@Autowired
	private ShiftRepository shiftRepository;

	public List<Shift> getAllShift() {
		return this.shiftRepository.findAll();
	}
	
	public void saveShift(Shift shift) {
		shiftRepository.save(shift);
	}

	public Shift getShiftById(long id) {
		return shiftRepository.findById(id).get();
	}

	public void deleteShiftById(Long id) {
		shiftRepository.deleteById(id);
	}

}
