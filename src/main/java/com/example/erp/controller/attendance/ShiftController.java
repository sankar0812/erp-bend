package com.example.erp.controller.attendance;

import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.erp.entity.attendance.Shift;
import com.example.erp.service.attendance.ShiftService;

@RestController
@CrossOrigin
public class ShiftController {

	@Autowired
	private ShiftService shiftService;

	@GetMapping("/shift")
	public ResponseEntity<Object> getPayroll(@RequestParam(required = true) String shift) {
		if ("Shift".equals(shift)) {
			return ResponseEntity.ok(shiftService.getAllShift());
		} else {
			String errorMessage = "Invalid value for 'shift'. Expected 'Shift'.";
			return ResponseEntity.badRequest().body(errorMessage);
		}
	}

	@PostMapping("/shift/save")
	private ResponseEntity<?> savePayroll(@RequestBody Shift shift) {
		SimpleDateFormat timeFormatter = new SimpleDateFormat("hh:mm:ss a");
		if (shift.getInTime() != null && !shift.getInTime().isEmpty()) {
			try {
				String formattedIntime = formatTime(shift.getInTime(), timeFormatter);
				shift.setInTime(formattedIntime);
			} catch (Exception e) {
			}
		}

		if (shift.getOutTime() != null && !shift.getOutTime().isEmpty()) {
			try {
				String formattedOuttime = formatTime(shift.getOutTime(), timeFormatter);
				shift.setOutTime(formattedOuttime);
			} catch (Exception e) {
			}
		}
		shiftService.saveShift(shift);
		long id = shift.getShiftId();
		return ResponseEntity.ok("Shift saved successfully with id :" + id);
	}

	private String formatTime(String time, SimpleDateFormat formatter) throws Exception {
		SimpleDateFormat inputFormat = new SimpleDateFormat("hh:mm:ss a");
		java.util.Date date = inputFormat.parse(time);
		return formatter.format(date);
	}

	@PutMapping("/shift/edit/{id}")
	public ResponseEntity<Shift> updatePayroll(@PathVariable("id") Long shiftId, @RequestBody Shift shift) {
		try {
			Shift existingShiftType = shiftService.getShiftById(shiftId);
			if (existingShiftType == null) {
				return ResponseEntity.notFound().build();
			}
			existingShiftType.setInTime(shift.getInTime());
			existingShiftType.setOutTime(shift.getOutTime());
			existingShiftType.setShiftType(shift.getShiftType());

			shiftService.saveShift(existingShiftType);
			return ResponseEntity.ok(existingShiftType);

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@DeleteMapping("/shift/delete/{id}")
	public ResponseEntity<String> deletePayroll(@PathVariable("id") Long payrollId) {
		shiftService.deleteShiftById(payrollId);
		return ResponseEntity.ok("Shift was deleted successfully");
	}
}
