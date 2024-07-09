package com.example.erp.repository.accounting;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.erp.entity.accounting.ExpenseType;



public interface ExpenseTypeRepository extends  JpaRepository<ExpenseType, Long>{

}
