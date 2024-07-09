package com.example.erp.repository.project;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.erp.entity.project.Research;

public interface ResearchRepository extends JpaRepository<Research, Long> {
	
	@Query(value = "select r.research_id as researchId ,r.accepted,r.date,r.department_id as departmentId,r.employee_id as employeeId,"
			+ " r.project_id as projectId,r.project_status as projectStatus,r.rejected,d.department_name as departmentName,e.user_id as userId,"
			+ " e.user_name as userName,cr.project_name as projectName,r.type_of_project as typeOfProject"
			+ " from  research as r"
			+ " join client_requirements as cr on cr.project_id=r.project_id"
			+ " left join department as d on d.department_id=r.department_id"
			+ " left join employee as e on e.employee_id=r.employee_id;",nativeQuery = true)
	List<Map<String, Object>> getAllprojectresearch();
	
	
	@Query(value = "WITH ranked_tasks AS (\r\n"
			+ "  SELECT t.client_id,t.employee_report_id,t.type_of_project,cr.date,cr.durationdate,e.user_id,e.user_name,cr.project_name,cr.project_id,t.task_id,\r\n"
			+ "    ROW_NUMBER() OVER (PARTITION BY t.client_id, t.project_id ORDER BY t.task_id DESC) AS rn\r\n"
			+ "  FROM task AS t\r\n"
			+ "    JOIN client_requirements AS cr ON cr.project_id = t.project_id\r\n"
			+ "    JOIN client_profile AS cp ON cp.client_id = t.client_id\r\n"
			+ "    JOIN employee AS e ON e.employee_id = t.employee_report_id\r\n"
			+ "  WHERE t.type_of_project IN ('research', 'development', 'project', 'testing', 'hosting')\r\n"
			+ " )SELECT  client_id,employee_report_id,type_of_project,date,durationdate,user_id,  user_name,project_name,  project_id,task_id\r\n"
			+ " FROM  ranked_tasks\r\n"
			+ " WHERE rn = 1;",nativeQuery = true)
	List<Map<String, Object>> ProjectManagerShowingprojecthead();

	@Query(value = " SELECT YEAR(CURDATE()) AS currentyear,YEAR(CURDATE()) - 1 AS previousyear,SUBSTRING(MONTHNAME(cp.date), 1, 3) AS month,"
			+ "    SUM(CASE WHEN YEAR(CURDATE()) = YEAR(cp.date) AND cp.status=true THEN 1 ELSE 0 END) AS currentcount,"
			+ "    SUM(CASE WHEN YEAR(CURDATE()) - 1 = YEAR(cp.date) AND cp.status=true THEN 1 ELSE 0 END) AS previouscount"
			+ " FROM client_requirements AS cp"
			+ " WHERE YEAR(cp.date) IN (YEAR(CURDATE()), YEAR(CURDATE()) - 1)"
			+ " GROUP BY SUBSTRING(MONTHNAME(cp.date), 1, 3)"
			+ " ORDER BY MIN(cp.date);",nativeQuery = true)
	List<Map<String, Object>> ALLCountproject();
	
	@Query(value = " select cr.date,cr.durationdate,cr.project_name,pt.project_type from client_requirements as cr\r\n"
			+ " join project_type as pt on pt.project_type_id=cr.project_type_id\r\n"
			+ " where cr.status=true\r\n"
			+ "  AND cr.durationdate >= CURRENT_DATE;",nativeQuery = true)
	List<Map<String, Object>> ALLCountprojectEnddate();

	@Query(value = " SELECT\r\n"
			+ "    COUNT(CASE WHEN tl.priority = 'high' AND YEAR(tl.start_date) = YEAR(CURDATE()) THEN 1 ELSE NULL END) AS high_priority_count,\r\n"
			+ "    NULLIF(tl.employee_id, 0) AS employee_id,\r\n"
			+ "    NULLIF(tl.trainee_id, 0) AS trainee_id,\r\n"
			+ "    MIN(tl.start_date) AS start_date,\r\n"
			+ "    COALESCE(MAX(r.role_id), MAX(tr.role_id), 0) AS role_id,\r\n"
			+ "    COALESCE(MAX(r.role_name), MAX(tr.role_name), 'DefaultRoleName') AS role_name,\r\n"
			+ "    COALESCE(MAX(e.user_id), MAX(td.user_id), 0) AS userId,\r\n"
			+ "    COALESCE(MAX(e.user_name), MAX(td.user_name), 'DefaultUserName') AS userName\r\n"
			+ " FROM\r\n"
			+ "    task AS t\r\n"
			+ " LEFT JOIN\r\n"
			+ "    task_list AS tl ON tl.task_id = t.task_id\r\n"
			+ " LEFT JOIN\r\n"
			+ "    employee AS e ON e.employee_id = tl.employee_id\r\n"
			+ " LEFT JOIN\r\n"
			+ "    training_details AS td ON td.trainee_id = tl.trainee_id\r\n"
			+ " LEFT JOIN\r\n"
			+ "    role AS r ON r.role_id = e.role_id\r\n"
			+ " LEFT JOIN\r\n"
			+ "    role AS tr ON tr.role_id = td.role_id\r\n"
			+ " WHERE\r\n"
			+ "    YEAR(tl.start_date) = YEAR(CURDATE())  "
			+ " GROUP BY\r\n"
			+ "    tl.employee_id, tl.trainee_id\r\n"
			+ " ORDER BY\r\n"
			+ "    high_priority_count DESC, tl.employee_id\r\n"
			+ " LIMIT 10;\r\n"
			+ "",nativeQuery = true)
	List<Map<String, Object>> AllHighPriorityCountINtask();
	
	@Query(value = " SELECT\r\n"
			+ "    COUNT(CASE WHEN tl.priority = 'high' AND YEAR(tl.start_date) = YEAR(CURDATE()) THEN 1 ELSE NULL END) AS high_priority_count,\r\n"
			+ "    NULLIF(tl.employee_id, 0) AS employee_id,\r\n"
			+ "    NULLIF(tl.trainee_id, 0) AS trainee_id,\r\n"
			+ "    MIN(tl.start_date) AS start_date,\r\n"
			+ "    COALESCE(MAX(r.role_id), MAX(tr.role_id), 0) AS role_id,\r\n"
			+ "    COALESCE(MAX(r.role_name), MAX(tr.role_name), 'DefaultRoleName') AS role_name,\r\n"
			+ "    COALESCE(MAX(e.user_id), MAX(td.user_id), 0) AS userId,\r\n"
			+ "    COALESCE(MAX(e.user_name), MAX(td.user_name), 'DefaultUserName') AS userName\r\n"
			+ "FROM\r\n"
			+ "    task AS t\r\n"
			+ "LEFT JOIN\r\n"
			+ "    task_list AS tl ON tl.task_id = t.task_id\r\n"
			+ "LEFT JOIN\r\n"
			+ "    employee AS e ON e.employee_id = tl.employee_id\r\n"
			+ "LEFT JOIN\r\n"
			+ "    training_details AS td ON td.trainee_id = tl.trainee_id\r\n"
			+ "LEFT JOIN\r\n"
			+ "    role AS r ON r.role_id = e.role_id\r\n"
			+ "LEFT JOIN\r\n"
			+ "    role AS tr ON tr.role_id = td.role_id\r\n"
			+ "WHERE\r\n"
			+ "    YEAR(tl.start_date) = YEAR(CURDATE())  -- Filter for the current year"
			+ "GROUP BY\r\n"
			+ "    tl.employee_id, tl.trainee_id\r\n"
			+ "ORDER BY\r\n"
			+ "    high_priority_count DESC, tl.employee_id\r\n"
			+ "LIMIT 10;\r\n"
			+ "",nativeQuery = true)
	List<Map<String, Object>> All();

	@Query(value = " SELECT YEAR(cr.date) AS year,COUNT(cr.status) AS totalProjectCount,0 AS completedProjectCount"
			+ " FROM client_requirements cr"
			+ " WHERE cr.status = true AND YEAR(cr.date) >= YEAR(CURDATE()) - 4"
			+ " GROUP BY YEAR(cr.date)"
			+ " UNION ALL"
			+ " SELECT YEAR(t.completed_date) AS year,0 AS totalProjectCount,COUNT(t.type_of_project) AS completedProjectCount"
			+ " FROM task t"
			+ " WHERE t.completed = true AND t.type_of_project = 'hosting' AND YEAR(t.completed_date) >= YEAR(CURDATE()) - 4"
			+ " GROUP BY YEAR(t.completed_date);"
			+ ""
		,nativeQuery = true)
	List<Map<String, Object>> GetAllNewProjectandcompletedPeoject();

	@Query(value = "SELECT"
			+ "    (SELECT COUNT(*) FROM employee WHERE status = true AND role_type = 'ProjectHead') as projectHeadCount,"
			+ "    (SELECT COUNT(*) FROM training_details WHERE status = true) as traineeCount,"
			+ "    (SELECT COUNT(*) FROM employee WHERE status = true AND role_type IN ('Employee', 'TL')) as employeeCount,"
			+ "    (SELECT COUNT(*) FROM client_requirements WHERE status = true) as totalProjectCount,"
			+ "    (SELECT COUNT(*) FROM task WHERE completed = true AND type_of_project = 'hosting') as completedProjectCount,"
			+ "    (SELECT COUNT(*) FROM quotation WHERE accepted = true) as onProcessProject;"
		,nativeQuery = true)
	Map<String, Object> GetAllNewProjectandcompletedPeojectcount();

}
