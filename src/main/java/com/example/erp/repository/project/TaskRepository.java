package com.example.erp.repository.project;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.erp.entity.project.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {

//	@Query(value = "select t.task_id,t.completed,t.project_id,"
//			+ "    t.date,t.development,t.project_status as task_status,t.not_completed,tl.project_status,t.research,t.testing,tl.trainee_id,t.type_of_project,cr.project_name,"
//			+ "    tl.cancellation_reason,tl.task_list_id,tl.cancelled,tl.category,tl.comments,tl.completed as taskcompleted,tl.completed_date,tl.created,tl.department_id,tl.due_date,"
//			+ "    tl.employee_id,tl.employee_report_id,tl.hold,tl.hold_reson,tl.label,tl.on_process,tl.pending,tl.priority,tl.projectkey,tl.start_date,tl.status,tl.summary,"
//			+ "    tl.todo,tl.type,tl.updated,r.user_name as empname,r.user_id as empid,d.department_name,coalesce( e.user_id,  td.user_id) as user_id,"
//			+ "    coalesce( e.user_name, td.user_name) as user_name,coalesce(rl.role_id, rf.role_id) as role_id,coalesce(rl.role_name, rf.role_name) as role_name"
//			+ " from task as t"
//			+ " join client_requirements as cr on cr.project_id = t.project_id"
//			+ " join task_list as tl on tl.task_id = t.task_id"
//			+ " left join employee as e on e.employee_id = tl.employee_id"
//			+ " left join training_details as td on td.trainee_id = tl.trainee_id"
//			+ " left join employee as r on r.employee_id = tl.employee_report_id"
//			+ " left join role as rl on rl.role_id = e.role_id"
//			+ " left join role as rf on rf.role_id = td.role_id"
//			+ " left join  department as d on d.department_id = tl.department_id;"
//			+ "", nativeQuery = true)
//	List<Map<String, Object>> getAllprojectTask();

	@Query(value = "select t.task_id,t.completed,t.project_id,"
			+ "    t.date,t.development,t.project_status as task_status,t.not_completed,tl.project_status,t.research,t.testing,tl.trainee_id,t.type_of_project,cr.project_name,"
			+ "    tl.cancellation_reason,tl.task_list_id,tl.cancelled,tl.category,tl.comments,tl.completed as taskcompleted,tl.completed_date,tl.created,tl.department_id,tl.due_date,"
			+ "    tl.employee_id,tl.employee_report_id,tl.hold,tl.hold_reson,tl.label,tl.on_process,tl.pending,tl.priority,tl.projectkey,tl.start_date,tl.status,tl.summary,"
			+ "    tl.todo,tl.type,tl.updated,r.user_name as empname,r.user_id as empid,d.department_name,coalesce( e.user_id,  td.user_id) as user_id,"
			+ "    coalesce( e.user_name, td.user_name) as user_name,coalesce(rl.role_id, rf.role_id) as role_id,coalesce(rl.role_name, rf.role_name) as role_name,"
			+ " td.user_id as traineeUserId,td.user_name as traineeUserName,e.user_id as empUserId,e.user_name as empUserName, e.employee_id as employeeId,td.trainee_id as traineeId"
			+ " from task as t"
			+ " join client_requirements as cr on cr.project_id = t.project_id"
			+ " left join task_list as tl on tl.task_id = t.task_id"
			+ " left join employee as e on e.employee_id = tl.employee_id"
			+ " left join training_details as td on td.trainee_id = tl.trainee_id"
			+ " left join employee as r on r.employee_id = tl.employee_report_id"
			+ " left join role as rl on rl.role_id = e.role_id"
			+ " left join role as rf on rf.role_id = td.role_id"
			+ " left join  department as d on d.department_id = tl.department_id", nativeQuery = true)
	List<Map<String, Object>> getAllprojectTask();

@Query(value="select tl.department_id,d.department_name from task_list as tl"
			+ " join task as t on t.task_id = tl.task_id"
			+ " join department as d on d.department_id = tl.department_id"
			+ " where t.task_id = :task_id", nativeQuery =  true)
	List<Map<String, Object>> getAllDepartmentDetails(Long task_id);
	

	@Query(value = " SELECT \r\n"
			+ "    t.task_id, \r\n"
			+ "    t.completed, \r\n"
			+ "    t.project_id, \r\n"
			+ "    t.date, \r\n"
			+ "    t.project_status AS task_status, \r\n"
			+ "    t.development, \r\n"
			+ "    t.not_completed, \r\n"
			+ "    tl.project_status, \r\n"
			+ "    t.research, \r\n"
			+ "    t.testing, \r\n"
			+ "    t.type_of_project, \r\n"
			+ "    cr.project_name, \r\n"
			+ "    tl.cancellation_reason, \r\n"
			+ "    tl.task_list_id, \r\n"
			+ "    tl.cancelled, \r\n"
			+ "    tl.category, \r\n"
			+ "    tl.comments, \r\n"
			+ "    tl.completed AS taskCompleted, \r\n"
			+ "    tl.completed_date, \r\n"
			+ "    tl.created, \r\n"
			+ "    tl.department_id, \r\n"
			+ "    tl.due_date, \r\n"
			+ "    NULLIF(tl.employee_id, 0) AS employee_id, \r\n"
			+ "    tl.employee_report_id, \r\n"
			+ "    tl.hold, \r\n"
			+ "    tl.hold_reson, \r\n"
			+ "    tl.label, \r\n"
			+ "    tl.on_process, \r\n"
			+ "    tl.pending, \r\n"
			+ "    tl.priority, \r\n"
			+ "    tl.projectkey, \r\n"
			+ "    tl.start_date, \r\n"
			+ "    tl.status, \r\n"
			+ "    tl.summary, \r\n"
			+ "    tl.todo, \r\n"
			+ "    tl.type, \r\n"
			+ "    tl.updated, \r\n"
			+ "    er.user_name AS empName, \r\n"
			+ "    er.user_id AS empId, \r\n"
			+ "    d.department_name, \r\n"
			+ "    NULLIF(tl.trainee_id, 0) AS trainee_id, \r\n"
			+ "    tr.role_id, \r\n"
			+ "    CASE \r\n"
			+ "        WHEN tl.employee_id = 1 AND tl.trainee_id = 0 THEN 1 \r\n"
			+ "        WHEN tl.employee_id = 0 AND tl.trainee_id = 1 THEN 1 \r\n"
			+ "        ELSE \r\n"
			+ "            CASE \r\n"
			+ "                WHEN tl.employee_id <> 0 THEN tl.employee_id \r\n"
			+ "                ELSE tl.trainee_id \r\n"
			+ "            END \r\n"
			+ "    END AS task_id_alias, \r\n"
			+ "    COALESCE(r.role_id, tr.role_id) AS role_id_coalesce, \r\n"
			+ "    COALESCE(r.role_name, tr.role_name) AS role_name_coalesce ,\r\n"
			+ "    COALESCE(e.user_id, td.user_id) AS userId, \r\n"
			+ "    COALESCE(e.user_name, td.user_name) AS userName\r\n"
			+ "FROM "
			+ "    task AS t "
			+ "LEFT JOIN "
			+ "    client_requirements AS cr ON cr.project_id = t.project_id "
			+ "LEFT JOIN "
			+ "    task_list AS tl ON tl.task_id = t.task_id "
			+ "LEFT JOIN "
			+ "    employee AS e ON e.employee_id = tl.employee_id "
			+ "LEFT JOIN "
			+ "    role AS r ON r.role_id = e.role_id "
			+ "LEFT JOIN "
			+ "    employee AS er ON er.employee_id = tl.employee_report_id "
			+ "LEFT JOIN "
			+ "    role AS rl ON rl.role_id = er.role_id "
			+ "LEFT JOIN "
			+ "    training_details AS td ON td.trainee_id = tl.trainee_id "
			+ "LEFT JOIN "
			+ "    role AS tr ON tr.role_id = td.role_id "
			+ "LEFT JOIN "
			+ "    department AS d ON d.department_id = tl.department_id "
	        + " WHERE t.task_id = :task_id", nativeQuery = true)
	List<Map<String, Object>> getProjectTask(@Param("task_id") Long task_id);

	@Query(value = "select t.task_id,t.completed,t.project_id,t.date,t.project_status as task_status,t.development,t.not_completed,tl.project_status ,t.research,t.testing,"
			+ " t.type_of_project,cr.project_name ,tl.cancellation_reason,tl.task_list_id, tl.cancelled,tl. category, tl.comments, tl.completed as taskCompleted,tl. completed_date,tl. created,"
			+ " tl.department_id, tl.due_date, tl.employee_id, tl.employee_report_id,tl. hold, tl.hold_reson, tl.label, tl.on_process, tl.pending,tl. priority,"
			+ " tl. projectkey, tl.start_date, tl.status, tl.summary, tl.todo, tl.type,tl.updated ,e.user_name,e.user_id,r.user_name as empName,r.user_id as empId,d.department_name,rl.role_id,rl.role_name"
			+ " from task as t"
			+ " left join client_requirements as cr on cr.project_id=t.project_id"
			+ " left join task_list as tl on tl.task_id=t.task_id"
			+ " left join employee as e on e.employee_id=tl.employee_id"
			+ " left join employee as r on r.employee_id=tl.employee_report_id"
			+ " left join role as rl on rl.role_id = e.role_id"
			+ " left join department as d on d.department_id=tl.department_id"
			+ " where e.employee_id = :employee_id and rl.role_id = :role_id",nativeQuery = true)
	List<Map<String, Object>> getAllEmployeeWithRole(@Param("employee_id")Long employee_id, @Param("role_id")Long role_id);

	@Query(value = "select t.task_id,t.completed,t.project_id,t.date,t.project_status as task_status,t.development,t.not_completed,tl.project_status,"
			+ " t.research,t.testing,t.type_of_project,cr.project_name,tl.cancellation_reason,tl.task_list_id,tl.cancelled,tl.category,tl.comments,"
			+ " tl.completed as taskcompleted,tl.completed_date,tl.created,tl.department_id,tl.due_date,tl.trainee_id,tl.employee_report_id,tl.hold,"
			+ " tl.hold_reson,tl.label,tl.on_process,tl.pending,tl.priority,tl.projectkey,tl.start_date,tl.status,tl.summary,tl.todo,tl.type,tl.updated,"
			+ " d.department_name,er.user_name as employeereportername,re.role_id as empreporterroleid,"
			+ " case when tl.employee_id = 1 and tl.trainee_id = 0 then 1"
			+ " when tl.employee_id = 0 and tl.trainee_id = 1 then 1"
			+ " else "
			+ " case when tl.employee_id <> 0 then tl.employee_id"
			+ " else tl.trainee_id"
			+ " end"
			+ " end as id,"
			+ " coalesce(r.role_id, rt.role_id) as role_id,"
			+ " coalesce(e.user_name, td.user_name) as user_name,"
			+ " coalesce(r.role_name, rt.role_name) as role_name"
			+ " from task as t"
			+ " join client_requirements as cr on cr.project_id = t.project_id"
			+ " join task_list as tl on tl.task_id = t.task_id"
			+ " left join employee as e on e.employee_id = tl.employee_id"
			+ " left join role as r on r.role_id = e.role_id"
			+ " left join training_details as td on td.trainee_id = tl.trainee_id"
			+ " left join role as rt on rt.role_id = td.role_id"
			+ " left join employee as er on er.employee_id = tl.employee_report_id"
			+ " left join role as re on re.role_id = er.role_id"
			+ " left join department as d on d.department_id = tl.department_id"
			+ " where"
			+ "(case when tl.employee_id = 1 and tl.trainee_id = 0 then 1"
			+ " when tl.employee_id = 0 and tl.trainee_id = 1 then 1"
			+ " else"
			+ " case when tl.employee_id <> 0 then tl.employee_id"
			+ " else tl.trainee_id end end) = :id"
			+ " and coalesce(r.role_id, rt.role_id) = :role_id",nativeQuery = true)
	List<Map<String, Object>> getAllTraineeandEmployeeWithRole(@Param("id")Long id, @Param("role_id")Long role_id);


	@Query(value = "SELECT\r\n"
			+ "    e.employee_id,e.role_id,\r\n"
			+ "    SUM(CASE WHEN tl.project_status = 'completed' AND tl.completed = 1 THEN 1 ELSE 0 END) AS completedCount,\r\n"
			+ "    SUM(CASE WHEN tl.project_status IN ('todo', 'onprocess') THEN 1 ELSE 0 END) AS onProcessCount,\r\n"
			+ "    SUM(CASE WHEN tl.project_status = 'pending' THEN 1 ELSE 0 END) AS pendingCount\r\n"
			+ " FROM task_list AS tl\r\n"
			+ " LEFT JOIN employee AS e ON e.employee_id = tl.employee_id\r\n"
			+ " LEFT JOIN role AS rl ON rl.role_id = e.role_id\r\n"
			+ " WHERE tl.employee_id = :id AND e.role_id = :role_id\r\n"
			+ " GROUP BY e.employee_id, e.role_id;", nativeQuery = true)
	Map<String, Object> getAllProjectWorktraningWithTraineeEmployee(@Param("id") long employee_id,
			@Param("role_id") long role_id);

	@Query(value = "SELECT\r\n"
			+ "    e.trainee_id,\r\n"
			+ "    e.role_id,\r\n"
			+ "    SUM(CASE WHEN tl.project_status = 'completed' AND tl.completed = 1 THEN 1 ELSE 0 END) AS completedCount,\r\n"
			+ "    SUM(CASE WHEN tl.project_status IN ('todo', 'onprocess') THEN 1 ELSE 0 END) AS onProcessCount,\r\n"
			+ "    SUM(CASE WHEN tl.project_status = 'pending' AND tl.pending = 1 THEN 1 ELSE 0 END) AS pendingCount\r\n"
			+ "FROM\r\n"
			+ "    task_list AS tl\r\n"
			+ "LEFT JOIN\r\n"
			+ "    training_details AS e ON e.trainee_id = tl.trainee_id\r\n"
			+ "LEFT JOIN\r\n"
			+ "    role AS rl ON rl.role_id = e.role_id\r\n"
			+ "WHERE\r\n"
			+ "    tl.trainee_id = :id AND e.role_id = :role_id\r\n"
			+ "GROUP BY e.trainee_id, e.role_id", nativeQuery = true)
	Map<String, Object> getAllProjectWorktraningWithTraineeTrainee(@Param("id") long trainee_id,
			@Param("role_id") long role_id);

	@Query(value = "SELECT cr.project_name,t.client_id,d.department_name,e.user_name,e.user_id" + " FROM task AS t"
			+ " join task_list as tl on tl.task_id=t.task_id"
			+ " JOIN client_requirements as cr ON cr.project_id = t.project_id"
			+ " left join employee as e on e.employee_id=tl.employee_id"
			+ " left join training_details as td on td.trainee_id=tl.trainee_id"
			+ " join department as d on d.department_id=tl.department_id"
			+ " where  d.department_id=:department_id", nativeQuery = true)
	List<Map<String, Object>> GetEmployeeProjectMember(@Param("department_id") Long department_id);

	@Query(value = "select e.employee_id,e.user_name,r.role_id,r.role_name,d.department_id,d.department_name,dt.designation_id,dt.designation_name from employee as e"
			+ " join department as d on d.department_id = e.department_id"
			+ " join designation as dt on dt.designation_id = e.designation_id"
			+ " join role as r on r.role_id = e.role_id" + " where r.role_id = 6", nativeQuery = true)
	List<Map<String, Object>> getAllProjectHeadDetails();

	@Query(value = "select t.task_id,t.completed,t.project_id,t.date,t.project_status as task_status,t.development,t.not_completed,tl.project_status ,t.research,t.testing,"
			+ " t.type_of_project,cr.project_name ,tl.cancellation_reason,tl.task_list_id, tl.cancelled,tl. category, tl.comments, tl.completed as taskcompleted,tl. completed_date,tl. created,"
			+ " tl.department_id, tl.due_date, tl.employee_id, tl.employee_report_id,tl. hold, tl.hold_reson, tl.label, tl.on_process, tl.pending,tl. priority,"
			+ " tl. projectkey, tl.start_date, tl.status, tl.summary, tl.todo, tl.type,tl.updated ,e.user_name,e.user_id,r.user_name as empname,r.user_id as empid,d.department_name,rl.role_id,rl.role_name,"
			+ " case when tl.employee_id = 1 and tl.trainee_id = 0 then 1"
			+ " when tl.employee_id = 0 and tl.trainee_id = 1 then 1"
			+ " else"
			+ " case when tl.employee_id <> 0 then tl.employee_id"
			+ " else tl.trainee_id"
			+ " end"
			+ " end as id,"
			+ " coalesce(re.role_id, tr.role_id) as role_id,"
			+ " coalesce(re.role_name, tr.role_name) as role_name"
			+ " from task as t"
			+ " join client_requirements as cr on cr.project_id=t.project_id"
			+ " join task_list as tl on tl.task_id=t.task_id"
			+ " left join employee as e on e.employee_id=tl.employee_id"
			+ " left join role as re on re.role_id = e.role_id"
			+ " left join training_details as td on td.trainee_id = tl.trainee_id"
			+ " left join role as tr on tr.role_id = td.role_id"
			+ " left join employee as r on r.employee_id=tl.employee_report_id"
			+ " left join role as rl on rl.role_id = r.role_id"
			+ " left join department as d on d.department_id=tl.department_id"
			+ " where tl.employee_report_id = :employee_id and rl.role_id = :role_id and t.task_id = :task_id", nativeQuery = true)
	List<Map<String, Object>> getEmployeeTaskReportId(@Param("employee_id") Long employee_id,
			@Param("role_id") Long role_id, @Param("task_id") Long task_id);

	@Query(value = "select t.task_id,t.completed,t.project_id,t.date,t.project_status as task_status,t.development,t.not_completed,tl.project_status ,t.research,t.testing,"
			+ " t.type_of_project,cr.project_name ,NULLIF(tl.trainee_id, 0) AS trainee_id,NULLIF(tl.employee_id, 0) AS employee_id,tl.cancellation_reason,tl.task_list_id, tl.cancelled,tl. category, tl.comments, tl.completed as taskcompleted,tl. completed_date,tl. created,"
			+ " tl.department_id, tl.due_date, tl.employee_report_id,tl. hold, tl.hold_reson, tl.label, tl.on_process, tl.pending,tl. priority,"
			+ " tl. projectkey, tl.start_date, tl.status, tl.summary, tl.todo, tl.type,tl.updated ,coalesce(e.user_name, td.user_name)  AS user_name,e.user_id,d.department_name,re.role_id as emproleid,re.role_name as emprolename,er.user_name as empName,"
			+ " case when tl.employee_id = 1 and tl.trainee_id = 0 then 1"
			+ " when tl.employee_id = 0 and tl.trainee_id = 1 then 1"
			+ " else"
			+ " case when tl.employee_id <> 0 then tl.employee_id"
			+ " else tl.trainee_id"
			+ " end"
			+ " end as id,"
			+ " coalesce(r.role_id, tr.role_id) as role_id,"
			+ " coalesce(r.role_name, tr.role_name) as role_name"
			+ " from task as t"
			+ " join client_requirements as cr on cr.project_id=t.project_id"
			+ " join task_list as tl on tl.task_id=t.task_id"
			+ " left join employee as e on e.employee_id=tl.employee_id"
			+ " left join role as r on r.role_id = e.role_id"
			+ " left join employee as er on er.employee_id=tl.employee_report_id"
			+ " left join role as re on re.role_id = er.role_id"
			+ " left join training_details as td on td.trainee_id = tl.trainee_id"
			+ " left join role as tr on tr.role_id = td.role_id"
			+ " left join department as d on d.department_id=tl.department_id"
			+ " where"
			+ " (case"
			+ " when tl.employee_id = 1 and tl.trainee_id = 0 then 1"
			+ " when tl.employee_id = 0 and tl.trainee_id = 1 then 1"
			+ " else"
			+ " case"
			+ " when tl.employee_id <> 0 then tl.employee_id"
			+ " else tl.trainee_id end"
			+ " end) = :id"
			+ " and coalesce(r.role_id, tr.role_id) = :role_id and t.task_id = :task_id", nativeQuery = true)
	List<Map<String, Object>> getAllEmployeeTaskWithRole(@Param("id") Long id,
			@Param("role_id") Long role_id, @Param("task_id") Long task_id);

	@Query(value = " select t.task_id,t.completed,t.project_id,t.date,t.project_status as task_status,t.development,t.not_completed,tl.project_status ,t.research,t.testing,tl.trainee_id,"
			+ "			 t.type_of_project,cr.project_name ,tl.cancellation_reason,tl.task_list_id, tl.cancelled,tl. category, tl.comments, tl.completed as taskCompleted,tl. completed_date,tl. created,\r\n"
			+ "			 tl.department_id, tl.due_date, tl.employee_id, tl.employee_report_id,tl. hold, tl.hold_reson, tl.label, tl.on_process, tl.pending,tl. priority,\r\n"
			+ "			 tl. projectkey, tl.start_date, tl.status, tl.summary, tl.todo, tl.type,tl.updated ,r.user_name as empName,r.user_id as empId,d.department_name,rl.role_id,rl.role_name,\r\n"
			+ "             COALESCE( e.user_id,  td.user_id) AS user_id,\r\n"
			+ "    COALESCE( e.user_name, td.user_name) AS user_name,\r\n"
			+ "    CASE \r\n"
			+ "        WHEN tl.employee_id = 1 AND tl.trainee_id = 0 THEN 1\r\n"
			+ "        WHEN tl.employee_id = 0 AND tl.trainee_id = 1 THEN 1\r\n"
			+ "        ELSE \r\n"
			+ "            CASE\r\n"
			+ "                WHEN tl.employee_id <> 0 THEN tl.employee_id\r\n"
			+ "                ELSE tl.trainee_id\r\n"
			+ "            END\r\n"
			+ "    END AS id\r\n"
			+ "			from task as t\r\n"
			+ "		 join client_requirements as cr on cr.project_id=t.project_id\r\n"
			+ "			 join task_list as tl on tl.task_id=t.task_id\r\n"
			+ "			 left join employee as e on e.employee_id=tl.employee_id\r\n"
			+ "			 left join employee as r on r.employee_id=tl.employee_report_id\r\n"
			+ "             LEFT JOIN training_details AS td ON td.trainee_id = tl.trainee_id\r\n"
			+ "			 left join role as rl on rl.role_id = r.role_id\r\n"
			+ "			 left join department as d on d.department_id=tl.department_id"
			+ " where tl.employee_report_id = :employee_id and rl.role_id = :role_id",nativeQuery = true)
	List<Map<String, Object>> getEmployeeReportId(@Param("employee_id")Long employee_id, @Param("role_id")Long role_id);
	
	
	@Query(value = " SELECT 	    cr.project_name,   t.client_id,  d.department_name,\r\n"
			+ "			   td.trainee_id,  e.employee_id,  COALESCE(e.user_name, td.user_name) AS user_name, COALESCE(e.user_id, td.user_id) AS user_id\r\n"
			+ "			FROM   task AS t\r\n"
			+ "			 JOIN  task_list AS tl ON tl.task_id = t.task_id\r\n"
			+ "		 JOIN   client_requirements AS cr ON cr.project_id = t.project_id\r\n"
			+ "		LEFT JOIN   employee AS e ON e.employee_id = tl.employee_id\r\n"
			+ "			 LEFT JOIN     training_details AS td ON td.trainee_id = tl.trainee_id\r\n"
			+ "			 JOIN 	 department AS d ON d.department_id = tl.department_id\r\n"
			+ "			WHERE d.department_id =:departmentId\r\n"
			+ "			 GROUP BY cr.project_name, t.client_id, d.department_name,  td.trainee_id,e.user_name, e.user_id, e.employee_id, tl.trainee_id, td.user_name, td.user_id;"
			+ "",nativeQuery = true)
	List<Map<String, Object>> GetClientProjectWorkingMember( @Param("departmentId")Long departmentId);
	


	@Query(value = " SELECT \r\n"
			+ "    cr.project_name, \r\n"
			+ "    t.client_id,\r\n"
			+ "    d.department_id,\r\n"
			+ "    cr.project_id,\r\n"
			+ "    d.department_name,\r\n"
			+ "    tl.trainee_id,\r\n"
			+ "    tl.employee_id,\r\n"
			+ "    tl.projectkey,\r\n"
			+ "    tl.priority,\r\n"
			+ "    COALESCE(e.user_name, td.user_name) AS user_name,\r\n"
			+ "    COALESCE(e.user_id, td.user_id) AS user_id,\r\n"
			+ "    tl.category,\r\n"
			+ "    tl.project_status,\r\n"
			+ "    COALESCE(r.role_name, rt.role_name) as role_name\r\n"
			+ " FROM task AS t\r\n"
			+ " JOIN task_list AS tl ON tl.task_id = t.task_id\r\n"
			+ " JOIN client_requirements AS cr ON cr.project_id = t.project_id\r\n"
			+ " LEFT JOIN employee AS e ON e.employee_id = tl.employee_id\r\n"
			+ " LEFT JOIN role AS r ON r.role_id = e.role_id\r\n"
			+ " LEFT JOIN training_details AS td ON td.trainee_id = tl.trainee_id\r\n"
			+ " LEFT JOIN role AS rt ON rt.role_id = td.role_id  -- Corrected alias here\r\n"
			+ " JOIN department AS d ON d.department_id = tl.department_id\r\n"
			+ " WHERE\r\n"
			+ "    (CASE \r\n"
			+ "        WHEN tl.employee_id = 1 AND tl.trainee_id = 0 THEN 1\r\n"
			+ "        WHEN tl.employee_id = 0 AND tl.trainee_id = 1 THEN 1\r\n"
			+ "        ELSE CASE \r\n"
			+ "            WHEN tl.employee_id <> 0 THEN tl.employee_id\r\n"
			+ "            ELSE tl.trainee_id \r\n"
			+ "        END\r\n"
			+ "    END) = :id\r\n"
			+ "    AND COALESCE(r.role_id, rt.role_id) = :roleId\r\n"
			+ "    AND tl.department_id =:department_id"
			+ "",nativeQuery = true)
	List<Map<String, Object>> GetClientProjectWorkingStatus( @Param("id")Long id,@Param("roleId")Long roleId,@Param("department_id")Long department_id);

	
	@Query(value = " SELECT 	    cr.project_name,   t.client_id,  d.department_name,t.type_of_project,\r\n"
			+ "			   td.trainee_id,  e.employee_id,  COALESCE(e.user_name, td.user_name) AS user_name, COALESCE(e.user_id, td.user_id) AS user_id\r\n"
			+ "			FROM   task AS t"
			+ "			 JOIN  task_list AS tl ON tl.task_id = t.task_id\r\n"
			+ "             	LEFT JOIN   employee AS e ON e.employee_id = tl.employee_id\r\n"
			+ "			 LEFT JOIN     training_details AS td ON td.trainee_id = tl.trainee_id\r\n"
			+ "		 JOIN   client_requirements AS cr ON cr.project_id = t.project_id	\r\n"
			+ "         LEFT JOIN employee AS r ON r.employee_id = tl.employee_report_id\r\n"
			+ "                     		 LEFT JOIN role AS ru ON ru.role_id = r.role_id\r\n"
			+ "			 JOIN 	 department AS d ON d.department_id = tl.department_id\r\n"
			+ "				 where ru.role_id=:roleId and r.employee_id=:employee_id\r\n"
			+ "			 GROUP BY cr.project_name, t.client_id, d.department_name, t.type_of_project, td.trainee_id,e.user_name, e.user_id, e.employee_id, tl.trainee_id, td.user_name, td.user_id;"
			+ "",nativeQuery = true)
	List<Map<String, Object>> GetProjectHeadWorkingMember(@Param("roleId")Long roleId,@Param("employee_id")Long employee_id);


	@Query(value = "   SELECT\r\n"
			+ "    NULLIF(COUNT(CASE WHEN tl.priority = 'high' AND YEAR(tl.start_date) = YEAR(CURDATE()) THEN 1 END), 0) AS high_priority_count,NULLIF(tl.employee_id, 0) AS employee_id,NULLIF(tl.trainee_id, 0) AS trainee_id,\r\n"
			+ "    MIN(tl.start_date) AS start_date,COALESCE(MAX(r.role_id), MAX(tr.role_id), 0) AS role_id,COALESCE(MAX(r.role_name), MAX(tr.role_name), 'DefaultRoleName') AS role_name,COALESCE(MAX(e.user_id), MAX(td.user_id), 0) AS user_id,\r\n"
			+ "    COALESCE(MAX(e.user_name), MAX(td.user_name), 'DefaultUserName') AS user_name,tl.employee_report_id,emp.user_name AS employee_user_name,ru.role_name AS report_role_name,t.project_id,cr.project_name\r\n"
			+ " FROM task AS t\r\n"
			+ " JOIN client_requirements AS cr ON cr.project_id = t.project_id\r\n"
			+ " LEFT JOIN task_list AS tl ON tl.task_id = t.task_id\r\n"
			+ " LEFT JOIN employee AS e ON e.employee_id = tl.employee_id\r\n"
			+ " LEFT JOIN employee AS emp ON emp.employee_id = tl.employee_report_id\r\n"
			+ " LEFT JOIN training_details AS td ON td.trainee_id = tl.trainee_id\r\n"
			+ " LEFT JOIN role AS r ON r.role_id = e.role_id\r\n"
			+ " LEFT JOIN role AS tr ON tr.role_id = td.role_id\r\n"
			+ " LEFT JOIN   role AS ru ON ru.role_id = emp.role_id\r\n"
			+ " WHERE YEAR(tl.start_date) = YEAR(CURDATE())AND ru.role_id = 6 AND tl.employee_report_id = 15\r\n"
			+ " GROUP BY tl.employee_id, tl.trainee_id, tl.employee_report_id, emp.user_name, ru.role_name, t.project_id, cr.project_name\r\n"
			+ " ORDER BY high_priority_count DESC, tl.employee_id\r\n"
			+ " LIMIT 10;\r\n"
			+ "",
	        nativeQuery = true)
	List<Map<String, Object>> GetProjectpriority(@Param("roleId") Long roleId, @Param("employee_id") Long employee_id);


	@Query(value = "SELECT"
			+ "  COUNT(CASE WHEN t.type_of_project = 'hosting' THEN 1 END) as completedProjectCount,"
			+ "  COUNT(CASE WHEN t.type_of_project = 'research' THEN 1 END) as researchProjectCount,"
			+ "  COUNT(CASE WHEN t.type_of_project = 'development' THEN 1 END) as onprocessCount"
			+ " FROM task AS t"
			+ " LEFT JOIN employee AS r ON r.employee_id = t.employee_report_id"
			+ " LEFT JOIN role AS ru ON ru.role_id = r.role_id"
			+ " WHERE ru.role_id =:roleId AND r.employee_id =:employee_id ;",
	        nativeQuery = true)
	Map<String, Object> ALLCountprojectCount(@Param("roleId") Long roleId, @Param("employee_id") Long employee_id);

	
	@Query(value = "SELECT"
			+ " YEAR(t.date) AS year,"
			+ "    SUM(CASE WHEN t.type_of_project = 'hosting' AND YEAR(t.hosting_date) = YEAR(CURDATE()) THEN 1 ELSE 0 END) AS completedProjectCount,"
			+ "    SUM(CASE WHEN YEAR(t.date) = YEAR(CURDATE()) THEN 1 ELSE 0 END) AS  totalCount,"
			+ "    SUM(CASE WHEN t.type_of_project = 'development' AND YEAR(t.today_date) = YEAR(CURDATE()) THEN 1 ELSE 0 END) AS onprocessCount"
			+ " FROM task AS t"
			+ " LEFT JOIN employee AS r ON r.employee_id = t.employee_report_id"
			+ " LEFT JOIN role AS ru ON ru.role_id = r.role_id"
			+ " WHERE ru.role_id =:roleId  AND r.employee_id =:employee_id"
			+ " GROUP BY"
			+ " year",
	        nativeQuery = true)
	List<Map<String, Object>> GetProjectYearVize(@Param("roleId") Long roleId, @Param("employee_id") Long employee_id);

	@Query(value = "SELECT DISTINCT tl.trainee_id,null as employee_id , e.user_name AS reporting_manager, tra.user_name, el.leave_type, el.date, el.to_date\r\n"
			+ " FROM task AS t\r\n"
			+ "JOIN task_list AS tl ON tl.task_id = t.task_id\r\n"
			+ "JOIN employee AS e ON e.employee_id = tl.employee_report_id\r\n"
			+ "JOIN training_details AS tra ON tra.trainee_id = tl.trainee_id\r\n"
			+ "JOIN role AS r ON r.role_id = e.role_id\r\n"
			+ "JOIN employeeleave AS el ON el.trainee_id = tl.trainee_id\r\n"
			+ "WHERE el.to_date >= CURDATE() AND el.approved_by = true AND r.role_id =:roleId AND tl.employee_report_id =:id \r\n"
			+ "UNION ALL\r\n"
			+ " SELECT DISTINCT  NULL AS trainee_id,tl.employee_id, e.user_name, tra.user_name, el.leave_type, el.date, el.to_date"
			+ " FROM task AS t\r\n"
			+ "JOIN task_list AS tl ON tl.task_id = t.task_id\r\n"
			+ "JOIN employee AS e ON e.employee_id = tl.employee_report_id\r\n"
			+ "JOIN employee AS tra ON tra.employee_id = tl.employee_id\r\n"
			+ "JOIN role AS r ON r.role_id = e.role_id\r\n"
			+ "JOIN employeeleave AS el ON el.employee_id = tl.employee_id\r\n"
			+ "WHERE el.to_date >= CURDATE() AND r.role_id =:roleId AND e.employee_id =:id",
	        nativeQuery = true)
	List<Map<String, Object>> GetProjectYearLeaveList(@Param("roleId") Long roleId, @Param("id") Long employee_id);

	@Query(value = ""
			+ " SELECT  tl.trainee_id,t.role_id,YEAR(tl.created) AS year,"
			+ " SUM(CASE WHEN tl.project_status = 'pending' AND YEAR(tl.created) = YEAR(CURRENT_DATE) THEN 1 ELSE 0 END) AS pendingCount,"
			+ " SUM(CASE WHEN tl.project_status = 'completed' AND YEAR(tl.completed_date) = YEAR(CURRENT_DATE) THEN 1 ELSE 0 END) AS completedCount,"
			+ " SUM(CASE WHEN tl.project_status = 'onprocess' AND YEAR(tl.on_process_date) = YEAR(CURRENT_DATE) THEN 1 ELSE 0 END) AS onprocessCount"
			+ " FROM"
			+ " task_list AS tl"
			+ " left join training_details as t on t.trainee_id=tl.trainee_id"
			+ " LEFT JOIN role AS r ON r.role_id = t.role_id"
			+ " WHERE tl.trainee_id =:id AND  t.role_id =:roleId  "
			+ " GROUP BY  tl.trainee_id,t.role_id,YEAR(tl.created);",
	        nativeQuery = true)
	List<Map<String, Object>> taskfortrainee( @Param("id") Long employee_id,@Param("roleId") Long roleId);



	@Query(value = "SELECT\r\n"
			+ "    tl.employee_id,e.role_id,YEAR(tl.created) AS year,\r\n"
			+ "    SUM(CASE WHEN tl.project_status = 'pending' AND YEAR(tl.created) = YEAR(CURRENT_DATE) THEN 1 ELSE 0 END) AS pending_count,\r\n"
			+ "    SUM(CASE WHEN tl.project_status = 'completed' AND YEAR(tl.completed_date) = YEAR(CURRENT_DATE) THEN 1 ELSE 0 END) AS completed_count,\r\n"
			+ "    SUM(CASE WHEN tl.project_status IN ('todo', 'onprocess') AND YEAR(tl.todo_date) = YEAR(CURRENT_DATE) THEN 1 ELSE 0 END) AS onprocess_count"
			+ " FROM task_list AS tl\r\n"
			+ " LEFT JOIN employee AS e ON e.employee_id = tl.employee_id\r\n"
			+ " LEFT JOIN role AS r ON r.role_id = e.role_id\r\n"
			+ " WHERE tl.employee_id =:id AND e.role_id =:roleId"
			+ " GROUP BY tl.employee_id, e.role_id, YEAR(tl.created);",
	        nativeQuery = true)
	List<Map<String, Object>> taskforEmployeeId1( @Param("id") Long employee_id,@Param("roleId") Long roleId);
	
	
	@Query(value="select d.*,e.user_name as empUserName,e.user_id as empUserId,td.user_name as traineeUserName,td.user_id as traineeUserId,e.role_type as empRoleType,"
			+ " td.role_name as traineeRoleType,e.employee_id,td.trainee_id from department as d"
			+ " left join employee as e on e.department_id = d.department_id"
			+ " left join training_details as td on td.department_id = d.department_id",nativeQuery = true)
	List<Map<String, Object>> getALLEmpWithTaskListDetailsAndList();
	
	@Query(value="SELECT tl.projectkey, tl.start_date, tl.updated, tl.project_status, tl.category,  COALESCE(e.user_name, td.user_name) AS user_name,tl.trainee_id,tl.employee_id,"
			+ "		   COALESCE(e.user_id, td.user_id) AS user_id,t.project_id,cr.project_name, COALESCE(e.role_id, e.role_id) AS role_id, COALESCE(e.department_id,de.department_id) AS department_id\r\n"
			+ "FROM task AS t\r\n"
			+ "JOIN task_list AS tl ON tl.task_id = t.task_id\r\n"
			+ "left join employee as e on e.employee_id=tl.employee_id\r\n"
			+ "left join training_details as td on td.trainee_id=tl.trainee_id\r\n"
			+ "join client_requirements as cr on cr.project_id =t.project_id\r\n"
			+ "join client_profile as cp on cp.client_id=t.client_id\r\n"
			+ "left join role as r on r.role_id=e.role_id\r\n"
			+ "left join department as d on d.department_id=e.department_id\r\n"
			+ "left join department as de on de.department_id=e.department_id\r\n"
			+ "WHERE tl.updated BETWEEN curdate() AND curdate() + interval 2 day\r\n"
			+ "AND tl.completed = false and COALESCE(e.department_id,de.department_id)=:department_id ;",nativeQuery = true)
	List<Map<String, Object>> GetProjectYearLeaveListDepartment(@Param("department_id")Long employeeId);

	
	@Query(value=" WITH ranked_tasks AS (\r\n"
			+ "		 SELECT ta.employee_id,t.client_id,t.employee_report_id,t.type_of_project,cr.date,cr.durationdate,e.user_id,e.user_name,cr.project_name,cr.project_id,t.task_id,\r\n"
			+ "			    ROW_NUMBER() OVER (PARTITION BY t.client_id, t.project_id ORDER BY t.task_id DESC) AS rn\r\n"
			+ "  FROM task AS t\r\n"
			+ "  join task_list as ta on ta.task_id=t.task_id\r\n"
			+ "			    JOIN client_requirements AS cr ON cr.project_id = t.project_id\r\n"
			+ "			    JOIN client_profile AS cp ON cp.client_id = t.client_id\r\n"
			+ "			    JOIN employee AS e ON e.employee_id = t.employee_report_id\r\n"
			+ "			  WHERE t.type_of_project IN ('research', 'development', 'project', 'testing', 'hosting') and ta.employee_id =:employee_id"
			+ "			)SELECT  client_id,employee_report_id,type_of_project,date,durationdate,user_id,employee_id, user_name,project_name,  project_id,task_id\r\n"
			+ "			 FROM  ranked_tasks\r\n"
			+ "			 WHERE rn = 1;",nativeQuery = true)
	List<Map<String, Object>> attendanceForEmployeeIdDD(@Param("employee_id")Long employeeId);
	
	
	@Query(value = " SELECT DISTINCT cr.project_name,t.project_id, t.client_id,d.department_name,td.trainee_id,e.employee_id, COALESCE(e.user_name, td.user_name) AS user_name,COALESCE(e.user_id, td.user_id) AS user_id\r\n"
			+ " FROM task AS t\r\n"
			+ " JOIN task_list AS tl ON tl.task_id = t.task_id\r\n"
			+ " LEFT JOIN employee AS e ON e.employee_id = tl.employee_id\r\n"
			+ " LEFT JOIN training_details AS td ON td.trainee_id = tl.trainee_id\r\n"
			+ " JOIN client_requirements AS cr ON cr.project_id = t.project_id\r\n"
			+ " LEFT JOIN employee AS r ON r.employee_id = tl.employee_report_id\r\n"
			+ " LEFT JOIN role AS ru ON ru.role_id = r.role_id\r\n"
			+ " JOIN department AS d ON d.department_id = tl.department_id\r\n"
//			+ " WHERE t.project_id =:project_id\r\n"
			+ " GROUP BY cr.project_name,t.client_id,d.department_name,td.trainee_id,t.project_id,e.user_name,e.user_id,e.employee_id, \r\n"
			+ "    tl.trainee_id,td.user_name,td.user_id;\r\n"
			+ ""
			+ "",nativeQuery = true)
	List<Map<String, Object>> GetProjectHeadWorkingMemberproject();

}