package com.example.erp.entity.accounting;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="expenseType")
public class ExpenseType {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	
	private long expenseTypeId;
	private String expenseType;
	
	
	
	public ExpenseType() {
		super();
	}
	public long getExpenseTypeId() {
		return expenseTypeId;
	}
	public void setExpenseTypeId(long expenseTypeId) {
		this.expenseTypeId = expenseTypeId;
	}
	public String getExpenseType() {
		return expenseType;
	}
	public void setExpenseType(String expenseType) {
		this.expenseType = expenseType;
	}


}
