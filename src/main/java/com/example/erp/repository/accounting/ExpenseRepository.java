package com.example.erp.repository.accounting;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.erp.entity.accounting.Expense;



public interface ExpenseRepository extends JpaRepository<Expense, Long>{
	 @Query(value=
			  
			 "  select e.date ,e.amount,e.description,e.expense_name as expenseName,et.expense_type as expenseType,"
			 + " e.expense_id as expenseId,e.expense_type_id as expenseTypeId"
			 + "			 from expense as e "
			 + "			 join expense_type as et on et.expense_type_id=e.expense_type_id "
			
				,nativeQuery = true)
	List<Map<String, Object>> allExpenseDetails();
	   
		@Query(value=
				 " select e.*,et.expense_type "
							+ " from expense as e "
							+ " join expense_type as et on et.expense_type_id=e.expense_type_id where e.expense_id=:expense_id"
				
				,nativeQuery = true)
		List <Map<String,Object>>  allDetailsOfExpense(@Param("expense_id")Long expense_id);
	
		  @Query(value =
		            " select e.*,et.expense_type"
		            + "				 from expense as e"
		            + "                 join expense_type as et on et.expense_type_id=e.expense_type_id" 
		          +  "  WHERE e.date BETWEEN :startdate AND :enddate",nativeQuery = true)
		    List<Map<String, Object>> allExpenseDetailsByDate(
		            @Param("startdate") LocalDate startdate, @Param("enddate") LocalDate enddate);
		@Query(value =
				"  SELECT e.*, et.expense_type"
				+ " FROM expense AS e"
				+ " JOIN expense_type AS et ON et.expense_type_id = e.expense_type_id"
				+ " WHERE e.date = CURRENT_DATE()"							
				, nativeQuery = true)
		List<Map<String, Object>> dailyExpenseByCurrentDate();
		@Query(value =
				"   select date,sum(amount)"
				+ "			 from expense where date=current_date()"
				+ "				 group by date"							
				, nativeQuery = true)
		List<Map<String, Object>> dailyExpenseByCurrentDate1();
		@Query(value =
				" select  e.*,month(date),et.expense_type"
				+ "				 from expense as e"
				+ "                 JOIN expense_type as et on et.expense_type_id= e.expense_type_id "
				
				, nativeQuery = true)
		List<Map<String, Object>> mothlyExpenseDetails();
		
		@Query(value =
				" select  month(date),sum(amount)"
				+ " from expense "
				+ " group by month(date)"
				+ " order by month(date) "
				
				, nativeQuery = true)
		List<Map<String, Object>> monthlyExpense();
		
		@Query(value =
				" select  e.*,year(date),et.expense_type"
				+ " from expense as e "
				+ " join expense_type as et on et.expense_type_id=e.expense_type_id"
				
				, nativeQuery = true)
		List<Map<String, Object>> yearlyExpenseDetails();
	

		@Query(value =
				 " select year(date),sum(amount) "
				+ " from expense "
				+ " group by year(date) "
                + " order by  year(date) "
				
				, nativeQuery = true)
		List<Map<String, Object>> yearlyExpense();
		
		@Query(value =
				   "    select e.*,et.expense_type"
				   + "			 from expense as e"
				   + "	     join expense_type as et on et.expense_type_id=e.expense_type_id"
				   + "          where year(date)=:year and MONTHNAME(date)=:monthname "				
				, nativeQuery = true)
		List<Map<String, Object>> findByYearAndMonth(Integer year, String monthname);
		
		
		 @Query(value=  
				 "  select"
				 + "    monthname(date_column) as month,sum(expense) as total_expense, sum(income) as total_income"
				 + " from ("
				 + "    select s.date as date_column,sum(sl.amount) as expense, 0 as income from server as s"
				 + "    join server_list as sl on sl.fk_server_id = s.server_id"
				 + "    where year(curdate()) = year(s.date) group by date_column, year(s.date), month(s.date)"
				 + "    union all"
				 + "    select s.date as date_column, sum(s.amount) as expense, 0 as income"
				 + "    from  server_maintenance as s  where  year(curdate()) = year(s.date) group by  date_column, year(s.date), month(s.date)"
				 + "    union all"
				 + "    select   e.date as date_column,   sum(e.amount) as expense,  0 as income"
				 + "    from   expense as e  where    year(curdate()) = year(e.date) group by  date_column, year(e.date), month(e.date)"
				 + "    union all"
				 + "    select st.salary_date as date_column,   sum(st.salary_amount) as expense, 0 as income from salary_type_list as st"
				 + "    where  year(curdate()) = year(st.salary_date) group by    date_column, year(st.salary_date), month(st.salary_date)"
				 + "    union all"
				 + "    select cs.date as date_column,sum(cs.asset_values) as expense,   0 as income from  company_assets as cs"
				 + "    where  year(curdate()) = year(cs.date) group by  date_column, year(cs.date), month(cs.date)"
				 + "    union all"
				 + "    select mp.pay_date as date_column,  0 as expense,sum(mp.received_amount) as income from  maintenance_payment as mp"
				 + "    where  year(curdate()) = year(mp.pay_date)  group by    date_column, year(mp.pay_date), month(mp.pay_date)"
				 + "    union all"
				 + "    select  r.payment_date as date_column, 0 as expense, sum(r.received_amount) as income  from receipt as r"
				 + "    where year(curdate()) = year(r.payment_date) group by date_column, year(r.payment_date), month(r.payment_date)) as combined_expense_income"
				 + " group by month order by month;"		
					,nativeQuery = true)
		List<Map<String, Object>> allExpenseAndincome();
		 
			@Query(value =
					 " select "
					 + "    monthname(curdate()) as month,"
					 + "    sum(expense) as total_expense,"
					 + "    sum(income) as total_income"
					 + "from ("
					 + "    select "
					 + "        s.date as date_column,"
					 + "        sum(sl.amount) as expense,"
					 + "        0 as income"
					 + "    from "
					 + "        server as s"
					 + "    join "
					 + "        server_list as sl on sl.fk_server_id = s.server_id"
					 + "    where "
					 + "        year(curdate()) = year(s.date) and month(curdate()) = month(s.date)"
					 + "    group by "
					 + "        date_column, year(s.date), month(s.date), s.date  -- include s.date in group by"
					 + "    union all"
					 + "    select "
					 + "        s.date as date_column,"
					 + "        sum(s.amount) as expense,"
					 + "        0 as income"
					 + "    from "
					 + "        server_maintenance as s"
					 + "    where "
					 + "        year(curdate()) = year(s.date) and month(curdate()) = month(s.date)"
					 + "    group by "
					 + "        date_column, year(s.date), month(s.date), s.date  -- include s.date in group by"
					 + "    union all"
					 + "    select "
					 + "        e.date as date_column,"
					 + "        sum(e.amount) as expense,"
					 + "        0 as income"
					 + "    from "
					 + "        expense as e"
					 + "    where "
					 + "        year(curdate()) = year(e.date) and month(curdate()) = month(e.date)"
					 + "    group by "
					 + "        date_column, year(e.date), month(e.date), e.date  -- include e.date in group by"
					 + "    union all"
					 + "    select "
					 + "        mp.pay_date as date_column,"
					 + "        0 as expense,"
					 + "        sum(mp.received_amount) as income"
					 + "    from "
					 + "        maintenance_payment as mp"
					 + "    where "
					 + "        year(curdate()) = year(mp.pay_date) and month(curdate()) = month(mp.pay_date)"
					 + "    group by "
					 + "        date_column, year(mp.pay_date), month(mp.pay_date), mp.pay_date  -- include mp.pay_date in group by"
					 + "    union all"
					 + "    select "
					 + "        r.payment_date as date_column,"
					 + "        0 as expense,"
					 + "        sum(r.received_amount) as income"
					 + "    from "
					 + "        receipt as r"
					 + "    where "
					 + "        year(curdate()) = year(r.payment_date) and month(curdate()) = month(r.payment_date)"
					 + "    group by "
					 + "        date_column, year(r.payment_date), month(r.payment_date), r.payment_date  -- include r.payment_date in group by"
					 + ") as combined_expense_income"
					 + "group by "
					 + "    month"
					 + "order by "
					 + "    month; "					
					, nativeQuery = true)
			List<Map<String, Object>> yearlyExpenseexpance();
			

			@Query(value =
					   "      select"
					   + "    year(curdate()) as currentyear, year(curdate()) - 1 as previousyear,monthname(cp.date) as month,"
					   + "    sum(case when year(curdate()) = year(cp.date) then 1 else 0 end) as currentcount,"
					   + "    sum(case when year(curdate()) - 1 = year(cp.date) then 1 else 0 end) as previouscount"
					   + " from client_profile as cp"
					   + " where year(cp.date) in (year(curdate()), year(curdate()) - 1)"
					   + " group by month"
					   + " order by min(cp.date);"				
					, nativeQuery = true)
			List<Map<String, Object>> yearlyExpenseexpanceprevious();

			@Query(value =
					   ""
					   + " SELECT 'server' AS category,COALESCE(SUM(sl.amount), 0) AS expense"
					   + " FROM server AS s JOIN server_list AS sl ON sl.fk_server_id = s.server_id"
					   + " WHERE YEAR(CURDATE()) = YEAR(s.date)"
					   + " UNION ALL"
					   + " SELECT 'expense' AS category,COALESCE(SUM(e.amount), 0) AS expense"
					   + " FROM expense AS e"
					   + " WHERE YEAR(CURDATE()) = YEAR(e.date)"
					   + " UNION ALL"
					   + " SELECT 'salary' AS category,COALESCE(SUM(st.salary_amount), 0) AS expense"
					   + " FROM salary_type_list AS st"
					   + " WHERE YEAR(CURDATE()) = YEAR(st.salary_date)"
					   + " UNION ALL"
					   + " SELECT 'company assets' AS category,COALESCE(SUM(cs.asset_values), 0) AS expense"
					   + " FROM company_assets AS cs"
					   + " WHERE YEAR(CURDATE()) = YEAR(cs.date)"
					   + " UNION ALL"
					   + " SELECT 'servermaintenance' AS category,COALESCE(SUM(sm.amount), 0) AS expense"
					   + " FROM server_maintenance AS sm"
					   + " WHERE YEAR(CURDATE()) = YEAR(sm.date);", nativeQuery = true)
			List<Map<String, Object>> yearlyExpenseexpanceprevioustype();
			
			
			@Query(value =
					   ""
					   + " select"
					   + "    monthname(date_column) as month, sum(expense) as total_expense, sum(income) as total_income"
					   + " from ("
					   + "    select  s.date as date_column,  sum(sl.amount) as expense,   0 as income from    server as s join   server_list as sl on sl.fk_server_id = s.server_id"
					   + "    where year(curdate()) = year(s.date) and month(curdate()) = month(s.date)group by   date_column, year(s.date), month(s.date)"
					   + "    union all  select   s.date as date_column,  sum(s.amount) as expense, 0 as income from  server_maintenance as s"
					   + "    where   year(curdate()) = year(s.date) and month(curdate()) = month(s.date) group by   date_column, year(s.date), month(s.date)"
					   + "    union all select  e.date as date_column,  sum(e.amount) as expense,  0 as income from  expense as e"
					   + "    where year(curdate()) = year(e.date) and month(curdate()) = month(e.date)group by date_column, year(e.date), month(e.date)"
					   + "    union all select  st.salary_date as date_column,   sum(st.salary_amount) as expense, 0 as income from salary_type_list as st"
					   + "    where  year(curdate()) = year(st.salary_date) and month(curdate()) = month(st.salary_date) group by date_column, year(st.salary_date), month(st.salary_date)"
					   + "    union all  select  cs.date as date_column, sum(cs.asset_values) as expense,   0 as income from company_assets as cs"
					   + "    where   year(curdate()) = year(cs.date) and month(curdate()) = month(cs.date) group by  date_column, year(cs.date), month(cs.date)"
					   + "    union all select   mp.pay_date as date_column,  0 as expense,sum(mp.received_amount) as income from   maintenance_payment as mp"
					   + "    where year(curdate()) = year(mp.pay_date) and month(curdate()) = month(mp.pay_date)group by date_column, year(mp.pay_date), month(mp.pay_date)"
					   + "    union all select  r.payment_date as date_column,  0 as expense, sum(r.received_amount) as income"
					   + "    from receipt as r where   year(curdate()) = year(r.payment_date) and month(curdate()) = month(r.payment_date)"
					   + "    group by date_column, year(r.payment_date), month(r.payment_date)) as combined_expense_income group by month order by month;", nativeQuery = true)
			List<Map<String, Object>>CurrentMonthIncomAndExpances();
			
			
			@Query(value =
					   ""
					   + " select 'invoice' as category,sum(r.received_amount) as expense"
					   + "  from receipt as r"
					   + " where year(curdate()) = year(r.payment_date)"
					   + " union all"
					   + " select 'servicemaintenance' as category,sum(sm.received_amount) as expense"
					   + "  from maintenance_payment as sm"
					   + " where year(curdate()) = year(sm.payment_date);"
					   + "", nativeQuery = true)
			List<Map<String, Object>> yearlyExpenseexpanceprevioustypeincome();
	
			
			
			
			@Query(value =  "  select e.date ,e.amount,e.description,e.expense_name as expenseName,et.expense_type as expenseType,"
					 + " e.expense_id as expenseId,e.expense_type_id as expenseTypeId"
					 + "			 from expense as e "
					 + "			 join expense_type as et on et.expense_type_id=e.expense_type_id "
			        + " where e.date between :startDate and :endDate", nativeQuery = true)
			List<Map<String, Object>> getAllpromotionsBetweenDates(@Param("startDate") LocalDate startDate,@Param("endDate") LocalDate endDate);

			
			@Query(value= "  select e.date ,e.amount,e.description,e.expense_name as expenseName,et.expense_type as expenseType,"
					 + " e.expense_id as expenseId,e.expense_type_id as expenseTypeId"
					 + "			 from expense as e "
					 + "			 join expense_type as et on et.expense_type_id=e.expense_type_id "
					+ " WHERE  YEAR(e.date) = :year  ;"
					+ "",nativeQuery = true)
			List<Map<String, Object>> getAllReceiptDetailsWithClientIdAnd(@Param("year") String year);
			
			
			@Query(value= "  select e.date ,e.amount,e.description,e.expense_name as expenseName,et.expense_type as expenseType,"
					 + " e.expense_id as expenseId,e.expense_type_id as expenseTypeId"
					 + "			 from expense as e "
					 + "			 join expense_type as et on et.expense_type_id=e.expense_type_id "
					+ " WHERE  monthname(e.date) = :monthName ;"
					+ "",nativeQuery = true)
			List<Map<String, Object>> getAllReceiptDetailsWithClientIdAndMonth(@Param("monthName") String monthName);
			
			@Query(value= "SELECT 1 AS id,'Server' AS category, COALESCE(SUM(sl.amount), 0) AS debit, 0 AS credit\r\n"
					+ "					FROM server AS s \r\n"
					+ "					JOIN server_list AS sl ON sl.fk_server_id = s.server_id\r\n"
					+ "					WHERE DATE_FORMAT(s.date, '%m-%d') <= '04-01' OR DATE_FORMAT(s.date, '%m-%d') >= '03-31'\r\n"
					+ "					AND YEAR(s.date) = IF(MONTH(s.date) >= 4, YEAR(CURDATE()), YEAR(CURDATE()) - 1)					\r\n"
					+ "					UNION ALL                       					\r\n"
					+ "					SELECT  2 AS id,'Expense' AS category, COALESCE(SUM(e.amount), 0) AS debit, 0 AS credit\r\n"
					+ "					FROM expense AS e        \r\n"
					+ "					WHERE DATE_FORMAT(e.date, '%m-%d')<= '04-01' OR DATE_FORMAT(e.date, '%m-%d') >= '03-31' \r\n"
					+ "					AND YEAR(e.date) = IF(MONTH(e.date) >= 4, YEAR(CURDATE()), YEAR(CURDATE()) - 1)\r\n"
					+ "				UNION ALL         					\r\n"
					+ "					SELECT  3 AS id, 'Salary' AS category, COALESCE(SUM(st.net_pay), 0) AS debit, 0 AS credit\r\n"
					+ "					FROM payroll_type AS st         \r\n"
					+ "					WHERE DATE_FORMAT(st.payment_date, '%m-%d') <= '04-01' OR DATE_FORMAT(st.payment_date, '%m-%d') >= '03-31' \r\n"
					+ "					AND YEAR(st.payment_date) = IF(MONTH(st.payment_date) >= 4, YEAR(CURDATE()), YEAR(CURDATE()) - 1)\r\n"
					+ "					UNION ALL        \r\n"
					+ "					SELECT  4 AS id,'Company Assets' AS category, COALESCE(SUM(cs.asset_values), 0) AS debit, 0 AS credit\r\n"
					+ "					FROM company_assets AS cs         \r\n"
					+ "					WHERE DATE_FORMAT(cs.date, '%m-%d') <= '04-01' OR DATE_FORMAT(cs.date, '%m-%d') >= '03-31' \r\n"
					+ "					AND YEAR(cs.date) = IF(MONTH(cs.date) >= 4, YEAR(CURDATE()), YEAR(CURDATE()) - 1)\r\n"
					+ "					UNION ALL          \r\n"
					+ "					SELECT  5 AS id,'Server Maintenance' AS category, COALESCE(SUM(sm.amount), 0) AS debit, 0 AS credit\r\n"
					+ "					FROM server_maintenance AS sm        \r\n"
					+ "					WHERE DATE_FORMAT(sm.date, '%m-%d') <= '04-01' OR DATE_FORMAT(sm.date, '%m-%d') >= '03-31' \r\n"
					+ "				AND YEAR(sm.date) = IF(MONTH(sm.date) >= 4, YEAR(CURDATE()), YEAR(CURDATE()) - 1)\r\n"
					+ "					UNION ALL              \r\n"
					+ "					SELECT   6 AS id,'Invoice' AS category, 0 AS debit, COALESCE(SUM(sm.amount), 0) AS credit\r\n"
					+ "					FROM invoice AS sm        \r\n"
					+ "					WHERE DATE_FORMAT(sm.invoice_date, '%m-%d') <= '04-01' OR DATE_FORMAT(sm.invoice_date, '%m-%d') >= '03-31' \r\n"
					+ "					AND YEAR(sm.invoice_date) = IF(MONTH(sm.invoice_date) >= 4, YEAR(CURDATE()), YEAR(CURDATE()) - 1)\r\n"
					+ "                    UNION ALL              \r\n"
					+ "					SELECT  7 AS id,'Maintenance Payment' AS category, 0 AS debit, COALESCE(SUM(sm.total), 0) AS credit\r\n"
					+ "					FROM maintenance_invoice AS sm        \r\n"
					+ "					WHERE DATE_FORMAT(sm.invoice_date, '%m-%d') <= '04-01' OR DATE_FORMAT(sm.invoice_date, '%m-%d') >= '03-31' \r\n"
					+ "					AND YEAR(sm.invoice_date) = IF(MONTH(sm.invoice_date) >= 4, YEAR(CURDATE()), YEAR(CURDATE()) - 1) ;"
					+ "",nativeQuery = true)
			List<Map<String, Object>> balanceSheet();


			
			
			
			
			
			
			
}
