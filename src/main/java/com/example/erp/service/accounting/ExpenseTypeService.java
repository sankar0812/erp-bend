package com.example.erp.service.accounting;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.erp.entity.accounting.ExpenseType;
import com.example.erp.repository.accounting.ExpenseTypeRepository;



@Service
public class ExpenseTypeService {
	
	@Autowired
	private ExpenseTypeRepository repo;
	
	public Iterable<ExpenseType> listAll(){
		return  this.repo.findAll();
		
		
	}
	public void SaveorUpdate(ExpenseType expenseType) {
		repo.save(expenseType);
	}
	
	
	public void save(ExpenseType expenseType) {
		repo.save(expenseType);

		}
	
	public ExpenseType findById(Long expenseType_id) {
		return repo.findById(expenseType_id).get();

		}
	
	public void deleteExpenseTypeIdById(Long expenseType_id) {
		repo.deleteById(expenseType_id);
	}
	

	public Optional<ExpenseType> getExpenseTypeById(Long expenseType_id) {
		return	repo.findById(expenseType_id);
		 
	}

}
