package com.example.erp.repository.employee;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.erp.entity.employee.Holidays;

public interface HolidaysRepository extends JpaRepository<Holidays, Long>{

	
	@Query(value = "  SELECT"
			+ "   l.title as eventName,"
			+ "    DATE_ADD(h.date, INTERVAL YEAR(CURDATE()) - YEAR(h.date) YEAR) as date,"
			+ "    CASE"
			+ "        WHEN DATE_ADD(h.date, INTERVAL YEAR(CURDATE()) - YEAR(h.date) YEAR) = CURDATE() THEN CONCAT(l.title, ' - today is their holidays')"
			+ "        WHEN DATE_ADD(h.date, INTERVAL YEAR(CURDATE()) - YEAR(h.date) YEAR) = CURDATE() + INTERVAL 1 DAY THEN CONCAT(l.title, ' - tomorrow is their holidays')"
			+ "        ELSE 'no special message'"
			+ "    END as message"
			+ " FROM"
			+ "    holidays as l"
			+ "    join holidays_list as h on h.holidays_id=l.holidays_id"
			+ " WHERE"
			+ "    DATE_ADD(h.date, INTERVAL YEAR(CURDATE()) - YEAR(h.date) YEAR) BETWEEN CURDATE() AND CURDATE() + INTERVAL 1 DAY;", nativeQuery = true)
	List<Map<String, Object>> AllHolidays();
	
	
	
	
	@Query(value = "  SELECT h.title, hl.date, hl.day,hl.holidays_list_id,h.holidays_id,hl.status,monthname(hl.date) as monthName "
			+ " FROM holidays AS h"
			+ " JOIN holidays_list AS hl ON hl.holidays_id = h.holidays_id"
			+ " WHERE YEAR(hl.date) = YEAR(CURDATE());", nativeQuery = true)
	List<Map<String, Object>> AllHolidaysinTable();

	
	
	@Query(value ="SELECT h.title , hl.date, hl.day,hl.holidays_list_id as holidaysListId,h.holidays_id as holidaysId,h.status,monthname(hl.date) as monthName "
			+ " FROM holidays AS h"
			+ " JOIN holidays_list AS hl ON hl.holidays_id = h.holidays_id"
			+ " WHERE YEAR(hl.date)=:year ", nativeQuery = true)
	List<Map<String, Object>> getAllAllHolidaysinTable(@Param("year") String year);


	
	@Query(value ="SELECT h.title , hl.date, hl.day,hl.holidays_list_id as holidaysListId,h.holidays_id as holidaysId,h.status,monthname(hl.date) as monthName "
			+ " FROM holidays AS h"
			+ " JOIN holidays_list AS hl ON hl.holidays_id = h.holidays_id"
			+ " WHERE monthname(hl.date)=:monthname ", nativeQuery = true)
	List<Map<String, Object>> getAllAllHolidaysinTableMonth(@Param("monthname") String monthname);







}
