package com.example.erp.controller.accounting;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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

import com.example.erp.entity.accounting.Expense;
import com.example.erp.repository.accounting.ExpenseRepository;
import com.example.erp.service.accounting.ExpenseService;




@RestController
@CrossOrigin
public class ExpenseController {
	
	@Autowired
	private ExpenseService expenseService;
	
	@Autowired
	private ExpenseRepository repo;

	@GetMapping("/expense")
	public ResponseEntity<?> getDetails(@RequestParam(required = true) String expense) {
	    try {
	        if ("expense".equals(expense)) {

			Iterable<Expense> expenseDetails = expenseService.listAll();

			return new ResponseEntity<>(expenseDetails, HttpStatus.OK);

		} else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The provided accessories is not supported.");
        }
    }catch (Exception e) {

			String errorMessage = "An error occurred while retrieving l details.";

			return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);

		}

	}

	@PostMapping("/expense/save")
	public ResponseEntity<?> saveBank(@RequestBody Expense expense) {

		try {
			expense.setStatus(true);
			expenseService.SaveorUpdate(expense);

			return ResponseEntity.status(HttpStatus.CREATED).body("expense details saved successfully.");

		} catch (Exception e) {

			String errorMessage = "An error occurred while saving expense details.";

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);

		}

	}

	@RequestMapping("/expense/{expenseId}")
	private Optional<Expense> getExpense(@PathVariable(name = "expenseId") long expenseId) {

		return expenseService.getExpenseById(expenseId);

	}
	
	@PutMapping("/expense/or/{id}")
    public ResponseEntity<Boolean> toggleCustomerStatus(@PathVariable(name = "id") long id) {
        try {
        	Expense expense = expenseService.findById(id);
            if (expense != null) {
                // Customer with the given id exists, toggle the status
                boolean currentStatus = expense.isStatus();
                expense.setStatus(!currentStatus);
                expenseService.SaveorUpdate(expense); // Save the updated customer
            } else {
                // Customer with the given id does not exist, return false
                return ResponseEntity.ok(false);
            }

            return ResponseEntity.ok(expense.isStatus()); // Return the new status (true or false)
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(false); // Set response to false in case of an error
        }
    }

	@PutMapping("/expense/editdeexpense/{expenseId}")
	public ResponseEntity<Expense> updateExpense(@PathVariable("expenseId") Long expenseId,
			@RequestBody Expense expenseDetails) {

		try {

			Expense existingexpense = expenseService.findById(expenseId);

			if (existingexpense == null) {

				return ResponseEntity.notFound().build();

			}

			existingexpense.setExpenseTypeId(expenseDetails.getExpenseTypeId());
			existingexpense.setExpenseName(expenseDetails.getExpenseName());
			existingexpense.setDate(expenseDetails.getDate());		
			existingexpense.setDescription(expenseDetails.getDescription());
			existingexpense.setAmount(expenseDetails.getAmount());

			expenseService.save(existingexpense);

			return ResponseEntity.ok(existingexpense);

		} catch (Exception e) {

			e.printStackTrace();

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

		}

	}

	@DeleteMapping("/expense/delete/{expenseId}")

	public ResponseEntity<String> deleteAmount(@PathVariable("expenseId") Long expenseId) {

		expenseService.deleteExpenseIdById(expenseId);

		return ResponseEntity.ok("expense deleted successfully");

	}

	@GetMapping("/expensedetails/view")
	public List<Map<String, Object>> allDeptDetails(@RequestParam(required = true) String view) {
	    try {
	        if ("expensedetails".equals(view)) {
	            return expenseService.allExpenseDetails();
	        } else {	       
	            return Collections.emptyList();
	        }
	    } catch (Exception e) {	        
	        e.printStackTrace(); 
	        return Collections.emptyList();
	    }
	}

	@GetMapping("/expense/dashboard")
	public List<Map<String, Object>> allDeptDetailsExpance(@RequestParam(required = true) String dashboard) {
	    try {
	        if ("expense".equals(dashboard)) {
	            return repo.allExpenseAndincome();
	        } else {	       
	            return Collections.emptyList();
	        }
	    } catch (Exception e) {	        
	        e.printStackTrace(); 
	        return Collections.emptyList();
	    }
	}

	@GetMapping("/expense/dashboard/currentmonth")
	public List<Map<String, Object>> AllDeptDetailsIncomeAndExpance(@RequestParam(required = true) String dashboard) {
	    try {
	        if ("currentmonth".equals(dashboard)) {
	            return repo.CurrentMonthIncomAndExpances();
	        } else {	       
	            return Collections.emptyList();
	        }
	    } catch (Exception e) {	        
	        e.printStackTrace(); 
	        return Collections.emptyList();
	    }
	}
	@GetMapping("/expense/dashboard/year")
	public List<Map<String, Object>> allDeptDetailsExpancealter(@RequestParam(required = true) String dashboard) {
	    try {
	        if ("dashboard".equals(dashboard)) {
	            return repo.yearlyExpenseexpance();
	        } else {	       
	            return Collections.emptyList();
	        }
	    } catch (Exception e) {	        
	        e.printStackTrace(); 
	        return Collections.emptyList();
	    }
	}
	
	@GetMapping("/expense/dashboard/previous")
	public List<Map<String, Object>> allDeptDetailsprevious(@RequestParam(required = true) String dashboard) {
	    try {
	        if ("previous".equals(dashboard)) {
	            return repo.yearlyExpenseexpanceprevious();
	        } else {	       
	            return Collections.emptyList();
	        }
	    } catch (Exception e) {	        
	        e.printStackTrace(); 
	        return Collections.emptyList();
	    }
	}
	
	@GetMapping("/expense/dashboard/type")
	public List<Map<String, Object>> allDeptDetailspreviousYear(@RequestParam(required = true) String dashboard) {
	    try {
	        if ("type".equals(dashboard)) {
	            return repo.yearlyExpenseexpanceprevioustype();
	        } else {	       
	            return Collections.emptyList();
	        }
	    } catch (Exception e) {	        
	        e.printStackTrace(); 
	        return Collections.emptyList();
	    }
	}
	
	@GetMapping("/income/dashboard/type")
	public List<Map<String, Object>> allDeptDetailspreviousYearincome(@RequestParam(required = true) String dashboard) {
	    try {
	        if ("income".equals(dashboard)) {
	            return repo.yearlyExpenseexpanceprevioustypeincome();
	        } else {	       
	            return Collections.emptyList();
	        }
	    } catch (Exception e) {	        
	        e.printStackTrace(); 
	        return Collections.emptyList();
	    }
	}
	
	@GetMapping("/income/dashboard")
	public List<Map<String, Object>> allDeptDetailspreviousYearincomenisjdbvyu(@RequestParam(required = true) String dashboard) {
	    try {
	        if ("income".equals(dashboard)) {
	            return repo.yearlyExpenseexpanceprevioustypeincome();
	        }
	        else if ("type".equals(dashboard)) {
	            return repo.yearlyExpenseexpanceprevioustype();
	        }
	        else  if ("previous".equals(dashboard)) {
	            return repo.yearlyExpenseexpanceprevious();
	        } 
	        else  if ("dashboard".equals(dashboard)) {
	            return repo.yearlyExpenseexpance();
	        }
	        else    if ("currentmonth".equals(dashboard)) {
	            return repo.CurrentMonthIncomAndExpances();
	        }else {	       
	            return Collections.emptyList();
	        }
	    } catch (Exception e) {	        
	        e.printStackTrace(); 
	        return Collections.emptyList();
	    }
	}
	
	@PostMapping("/expense/manager")
	public ResponseEntity<List<Map<String, Object>>> getAllVoucherBetweenDatesAndYearOrMonth(
	        @RequestBody Map<String, Object> requestBody) {

	    if (!requestBody.containsKey("choose")) {
	        return ResponseEntity.badRequest().build();
	    }

	    String choose = requestBody.get("choose").toString();

	    switch (choose) {
	        case "date":
	            if (requestBody.containsKey("startDate") && requestBody.containsKey("endDate")) {
	                LocalDate startDate = LocalDate.parse(requestBody.get("startDate").toString(), DateTimeFormatter.ISO_DATE);
	                LocalDate endDate = LocalDate.parse(requestBody.get("endDate").toString(), DateTimeFormatter.ISO_DATE);
	                return ResponseEntity.ok(repo.getAllpromotionsBetweenDates(startDate, endDate));
	            }
	            break;

	        case "year":
	            if (requestBody.containsKey("year")) {
	                String year = requestBody.get("year").toString();
	                List<Map<String, Object>> leaveData = repo.getAllReceiptDetailsWithClientIdAnd(year);
	                return ResponseEntity.ok(leaveData);
	            }
	            break;

	        case "month":
	            if (requestBody.containsKey("monthName")) {
	                String monthName = requestBody.get("monthName").toString();
	                List<Map<String, Object>> leaveData1 = repo.getAllReceiptDetailsWithClientIdAndMonth(monthName);
	                return ResponseEntity.ok(leaveData1);
	            }
	            break;

	        default:
	            return ResponseEntity.badRequest().build();
	    }

	    return ResponseEntity.badRequest().build();
	}

	
	
	@GetMapping("/balance/sheet")
	public List<Map<String, Object>> AllDeptDetailspreviousYearincome(@RequestParam(required = true) String dashboard) {
	    try {
	        if ("sheet".equals(dashboard)) {
	            return repo.balanceSheet();
	        } else {	       
	            return Collections.emptyList();
	        }
	    } catch (Exception e) {	        
	        e.printStackTrace(); 
	        return Collections.emptyList();
	    }
	}
	
	
}
