package com.example.erp.service.accounting;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.erp.entity.accounting.Expense;
import com.example.erp.repository.accounting.ExpenseRepository;





@Service
public class ExpenseService {
	
	@Autowired
	private ExpenseRepository expenseRepository;
	
	public Iterable<Expense> listAll(){
		return  this.expenseRepository.findAll();
		
		
	}
	public void SaveorUpdate(Expense expense) {
		expenseRepository.save(expense);
	}
	
	
	public void save(Expense expense) {
		expenseRepository.save(expense);

		}
	
	public Expense findById(Long expense_id) {
		return expenseRepository.findById(expense_id).get();

		}
	
	public void deleteExpenseIdById(Long expense_id) {
		expenseRepository.deleteById(expense_id);
	}
	

	public Optional<Expense> getExpenseById(Long expense_id) {
		return	expenseRepository.findById(expense_id);
		 
	}
	public 	List <Map<String,Object>> allExpenseDetails(){
		return expenseRepository.allExpenseDetails();
	}
	
	public 	List <Map<String,Object>>  findAllByExpenseId(Long expense_id){
		return expenseRepository.allDetailsOfExpense(expense_id);
		
	}
	
//	public List<Map<String, Object>> allExpenseDetailsByDate(LocalDate date) {
//		return expenseRepository.allExpenseDetailsByDate(date);
//	}
	
	public List<Map<String, Object>> dailyExpenseByCurrentDate() {
		return expenseRepository.dailyExpenseByCurrentDate();
	}

}
