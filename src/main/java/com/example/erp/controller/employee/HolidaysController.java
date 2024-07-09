package com.example.erp.controller.employee;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.erp.entity.employee.Holidays;
import com.example.erp.entity.employee.HolidaysList;
import com.example.erp.repository.employee.HolidaysListRepository;
import com.example.erp.repository.employee.HolidaysRepository;
import com.example.erp.service.employee.HolidaysListService;
import com.example.erp.service.employee.HolidaysService;


@RestController
@CrossOrigin
public class HolidaysController {
	
	@Autowired
	private HolidaysService service;
	@Autowired
	private HolidaysRepository repo;
	
	@Autowired
	private HolidaysListService holidaysListService;
	@Autowired
	private HolidaysListRepository holidaysListRepository;
	
@GetMapping("/leavetype1")
public ResponseEntity<?> getLeaveTypes(@RequestParam(required = true) String leaveType1) {
    try {
        if ("leaveType1".equals(leaveType1)) {
            // Assuming you want to get leave types for the current year
            
            List<HolidaysList> leaveTypes = holidaysListService.listAll(); // Assuming you have a method to retrieve all leave types
            
            LocalDate currentDate = LocalDate.now();
            for (HolidaysList holidayLoop : leaveTypes) {
                LocalDate holidayDate = holidayLoop.getDate().toLocalDate();

                if (holidayDate.isBefore(currentDate)) {
                    holidayLoop.setStatus(false);
                } else {
                    holidayLoop.setStatus(true);
                }
            }
            
            return ResponseEntity.ok(leaveTypes);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("The provided leave parameter is not supported.");
        }
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("Error retrieving LeaveTypes: " + e.getMessage());
    }
}


	

	


	
	@PostMapping("/leavetype/save")
	public ResponseEntity<String> saveLeaveType(@RequestBody Holidays leaveType) {
	    try {
	        leaveType.setStatus(true);
	        List<HolidaysList> holidaysList = leaveType.getLeaveList();

	        String currentYearString = String.valueOf(LocalDate.now().getYear());
	        int currentYear = LocalDate.now().getYear();
	        List<HolidaysList> existingHolidays = holidaysListRepository.findAll();

	        for (HolidaysList holidayLoop : holidaysList) {
	            LocalDate date = holidayLoop.getDate().toLocalDate();

	            if (date.getYear() == currentYear && dateExistsInExistingHolidays(date, existingHolidays)) {
	                return ResponseEntity.badRequest().body("The date already exists in the current year");
	            }
	            
	            

	            holidayLoop.setDate(Date.valueOf(date));
	        }

	        service.saveOrUpdate(leaveType);
	        return ResponseEntity.ok("LeaveType saved with id: " + leaveType.getHolidaysId());
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("Error saving LeaveType: " + e.getMessage());
	    }
	}

	private boolean dateExistsInExistingHolidays(LocalDate date, List<HolidaysList> existingHolidays) {
	    return existingHolidays.stream()
	            .map(HolidaysList::getDate)
	            .map(Date::toLocalDate)
	            .anyMatch(existingDate -> existingDate.equals(date));
	}


	@RequestMapping("/leavetype/{id}")
	public ResponseEntity<?> getLeaveTypeById(@PathVariable(name = "id") long id) {
		try {
			Holidays LeaveType = service.getById(id);
			if (LeaveType != null) {
				return ResponseEntity.ok(LeaveType);
			} else {
				return ResponseEntity.notFound().build();
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error retrieving LeaveType: " + e.getMessage());
		}
	}

	@PutMapping("/leavetype/edit/{id}")
	public ResponseEntity<Holidays> updateLeaveType(@PathVariable("id") long id, @RequestBody Holidays LeaveType) {
		try {
			Holidays existingLeaveType = service.getById(id);
			if (existingLeaveType == null) {
				return ResponseEntity.notFound().build();
			}
			existingLeaveType.setTitle(LeaveType.getTitle());
			existingLeaveType.setLeaveList(LeaveType.getLeaveList());
			
			
			service.saveOrUpdate(existingLeaveType);
			return ResponseEntity.ok(existingLeaveType);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@DeleteMapping("/leavetype/delete/{id}")
	public ResponseEntity<String> deleteLeaveType(@PathVariable("id") long id) {
		try {
			service.deleteById(id);
			return ResponseEntity.ok("LeaveType deleted successfully");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error deleting LeaveType: " + e.getMessage());
		}
	}
	@GetMapping("/holidays/view")
	public List<Map<String, Object>> Gettabledata(@RequestParam(required = true) String holidays) {
	    try {
	        if ("holidays".equalsIgnoreCase(holidays)) {
	            List<Map<String, Object>> holidaysData = repo.AllHolidaysinTable();

	            LocalDate currentDate = LocalDate.now();
	            for (Map<String, Object> holiday : holidaysData) {
	                LocalDate holidayDate = ((Date) holiday.get("date")).toLocalDate();

	                if (holidayDate.isAfter(currentDate)) {
	                    holiday.put("status", false);
	                } else {
	                    holiday.put("status", true);
	                }
	            }

	            return holidaysData;
	        } else {
	            throw new IllegalArgumentException("Invalid parameter value. 'holidays' must be 'holidays'.");
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        return Collections.emptyList();
	    }
	}

	
	
	@PostMapping("/holidays/report")
	public ResponseEntity<List<Map<String, Object>>> getAllVoucherattendancetrainefbgertghe(@RequestBody Map<String, Object> requestBody) {
	    try {
	        if (!requestBody.containsKey("year")) {
	        	  return ResponseEntity.badRequest().body(Collections.emptyList());
	        }
	        String yearNumber = (String) requestBody.get("year");
	        String year = yearNumber;
	        System.out.println("Year: " + year);
	        List<Map<String, Object>> holidays = repo.getAllAllHolidaysinTable(year);
	        System.out.println("Holidays Size: " + holidays.size());

	        if (holidays.isEmpty()) {
	            System.out.println("Holidays list is empty.");
	            return ResponseEntity.ok(Collections.emptyList());
	        }
	        List<Map<String, Object>> departmentList = new ArrayList<>();

	        Map<String, List<Map<String, Object>>> holidaysGroupMap = holidays.stream()
	                .collect(Collectors.groupingBy(holiday -> {
	                    Object holidaysIdObject = holiday.get("holidaysId");
	                    return holidaysIdObject != null ? holidaysIdObject.toString() : "null";
	                }));

	        for (Map.Entry<String, List<Map<String, Object>>> holidaysEntry : holidaysGroupMap.entrySet()) {
	            Map<String, Object> departmentMap = new HashMap<>();

	            // Handle the case where the key is "null"
	            String holidaysIdString = holidaysEntry.getKey();
	            long holidaysId = "null".equals(holidaysIdString) ? 0L : Long.parseLong(holidaysIdString);

	            departmentMap.put("leaveList", holidaysEntry.getValue().stream()
	                    .map(holiday -> {
	                        Map<String, Object> leaveMap = new HashMap<>();
	                        leaveMap.put("date", holiday.get("date"));
	                        leaveMap.put("holidaysListId", holiday.get("holidaysListId"));
	                        leaveMap.put("day", holiday.get("day"));
	                        
	                        return leaveMap;
	                    })
	                    .collect(Collectors.toList()));
	           


	            departmentMap.put("holidaysId", holidaysId);
	            departmentMap.put("monthName", holidaysEntry.getValue().get(0).get("monthName"));
	            departmentMap.put("title", holidaysEntry.getValue().get(0).get("title"));
	            departmentMap.put("status", holidaysEntry.getValue().get(0).get("status"));
	            LocalDate currentDate = LocalDate.now();

                // Assuming "date" is of type LocalDate
                java.sql.Date sqlDate = (java.sql.Date) holidaysEntry.getValue().get(0).get("date");
                LocalDate holidayDate = sqlDate.toLocalDate();
                if (holidayDate.isBefore(currentDate)) {
                    departmentMap.put("status", false);
                } else {
                    departmentMap.put("status", true);
                }

	            departmentList.add(departmentMap);
	        }

	        return ResponseEntity.ok(departmentList);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	    }
	}

	


	@GetMapping("/holidays")
	public List<Map<String, Object>> getnoti(@RequestParam(required = true) String employees) {
		try {
			if ("holidays".equalsIgnoreCase(employees)) {
				return repo.AllHolidays();
			} else {
				throw new IllegalArgumentException("Invalid parameter value. 'employees' must be 'resignations'.");
			}
		} catch (Exception e) {

			e.printStackTrace();
			return Collections.emptyList();

		}
	}
	
	@GetMapping("/leavetype")
	public ResponseEntity<Object> getAllRoleByEmployee(@RequestParam(required = true) String LeaveType) {
	    if ("leaveType".equals(LeaveType)) {
	        try {
	            List<Map<String, Object>> departmentList = new ArrayList<>();
	            List<Map<String, Object>> departmentRole = repo.AllHolidaysinTable();

	            Map<String, List<Map<String, Object>>> departmentGroupMap = departmentRole.stream()
	                    .collect(Collectors.groupingBy(action -> action.get("holidays_id").toString()));

	            for (Entry<String, List<Map<String, Object>>> departmentLoop : departmentGroupMap.entrySet()) {
	                Map<String, Object> departmentMap = new HashMap<>();
	                departmentMap.put("holidaysId", Long.parseLong(departmentLoop.getKey()));
	                departmentMap.put("title", departmentLoop.getValue().get(0).get("title"));
	                
	                LocalDate currentDate = LocalDate.now();

	                // Assuming "date" is of type LocalDate
	                java.sql.Date sqlDate = (java.sql.Date) departmentLoop.getValue().get(0).get("date");
	                LocalDate holidayDate = sqlDate.toLocalDate();
	                if (holidayDate.isBefore(currentDate)) {
	                    departmentMap.put("status", false);
	                } else {
	                    departmentMap.put("status", true);
	                }

	                departmentMap.put("monthName", departmentLoop.getValue().get(0).get("monthName"));
	                List<Map<String, Object>> departmentSubList = new ArrayList<>();

	                for (Map<String, Object> departmentsubLoop : departmentLoop.getValue()) {
	                    Map<String, Object> departmentSubMap = new HashMap<>();
	                    departmentSubMap.put("holidaysListId", departmentsubLoop.get("holidays_list_id"));
	                    departmentSubMap.put("date", departmentsubLoop.get("date"));
	                    departmentSubMap.put("day", departmentsubLoop.get("day"));

	                    departmentSubList.add(departmentSubMap);
	                }

	                departmentMap.put("leaveList", departmentSubList);
	                departmentList.add(departmentMap);
	            }

	            return ResponseEntity.ok(departmentList);
	        } catch (Exception e) {
	            e.printStackTrace();
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("Error retrieving LeaveTypes: " + e.getMessage());
	        }
	    } else {
	        String errorMessage = "Invalid value for 'LeaveType'. Expected 'leaveType'.";
	        return ResponseEntity.badRequest().body(errorMessage);
	    }
	}
	@PostMapping("/leavetype/manager")
	public List<Map<String, Object>> getAllVoucherBetweenDates(@RequestBody Map<String, Object> requestBody) {
	    String monthName = requestBody.get("monthName").toString();


	    return repo.getAllAllHolidaysinTableMonth(monthName);
	}
	
}

