package com.example.erp.repository.message;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.erp.entity.message.Message;



public interface MessageRepository extends JpaRepository<Message, Long> {

	
	@Query(value = "select c.employee_id  as memberListId,c.email,c.phone_number as phoneNumber,c.role_type as roleType,c.user_name as receiverName,ml.user_name as senderName,m.message_id as messageId,m.date,m.intime,m.message,m.receiver_id as receiverId,m.sender_id as senderId\r\n"
			+ "			 from chat_table as m\r\n"
			+ "			 join employee as  c on c.employee_id=m.receiver_id\r\n"
			+ "			  join employee as  ml on ml.employee_id=m.sender_id;",nativeQuery = true)
	List<Map<String, Object>> MessageList();


	
	
//	@Query(value = ""
//			+ " select e.employee_id as employeeId,e.user_name as userName,e.phone_number as phoneNumber,e.role_type,e.email  from employee as e",nativeQuery = true)
//	List<Map<String, Object>> MessageListEmployee();
	
	
	@Query(value = ""
			+ " select e.member_list_id,e.email,e.phone_number,e.role_id,e.role_type,e.user_name,e.id from member_list as e",nativeQuery = true)
	List<Map<String, Object>> MessageListEmployee();
	
	
	@Query(value = "select c.employee_id  as memberListId,c.email,c.phone_number as phoneNumber,c.role_type as roleType,c.user_name as receiverName,ml.user_name as senderName,m.message_id as messageId,m.date,m.intime,m.message,m.receiver_id as receiverId,m.sender_id as senderId"
			+ " from chat_table as m"
			+ " join employee as  c on c.employee_id=m.receiver_id"
			+ "   join employee as  ml on ml.employee_id=m.sender_id"
			+ "   join employee as  e on e.employee_id=m.receiver_id"
			+ "   join employee as  ee on ee.employee_id=m.sender_id"
			+ "   where (m.receiver_id =:id OR m.sender_id =:id) AND  (m.receiver_id =:receiver_id OR m.sender_id =:receiver_id);",nativeQuery = true)
	List<Map<String, Object>> getAllTraineeandEmployeeWithRole(@Param("id")Long sender_id, @Param("receiver_id")Long receiver_id);

	@Query(value = "select c.employee_id  as memberListId,c.email,c.phone_number as phoneNumber,c.role_type as roleType,c.user_name as receiverName,ml.user_name as senderName,m.message_id as messageId,m.date,m.intime,m.message,m.receiver_id as receiverId,m.sender_id as senderId"
			+ "		 from chat_table as m"
			+ "			 join employee as  c on c.employee_id=m.receiver_id"
			+ "			  join employee as  ml on ml.employee_id=m.sender_id"
			+ "			   join employee as  e on e.employee_id=m.receiver_id"
			+ "			  join employee as  ee on ee.employee_id=m.sender_id"
			+ "			   where (m.receiver_id =:id OR m.sender_id =:id)",nativeQuery = true)
	List<Map<String, Object>> getAllTraineeandEmployeeWith(@Param("id")Long sender_id);



	List<Message> findBySenderId(Long senderId);




	List<Message> findByReceiverId(Long receiverId);


	@Query(value = " select c.member_list_id  as memberListId,c.email,c.phone_number as phoneNumber,c.role_type as roleType,c.user_name as receiverName,ml.user_name as senderName,m.message_id as messageId,m.date,m.intime,m.message,m.receiver_id as receiverId,m.sender_id as senderId"
			+ "	from chat_table as m"
			+ "	left join member_list as  c on c.member_list_id=m.receiver_id"
			+ "	left join member_list as  ml on ml.member_list_id=m.sender_id"
			+ "	left join member_list as  e on e.member_list_id=m.receiver_id"
			+ "	join member_list as  ee on ee.member_list_id=m.sender_id"
			+ "	where (m.receiver_id =:id OR m.sender_id =:id)",nativeQuery = true)
	List<Map<String, Object>> getAllReceiptDetailsWithClientId(Long id);
	
	@Query(value = " SELECT member_list_id, NULL AS message_id, NULL AS sender_id, NULL AS receiver_id, NULL AS message, NULL AS receivername, NULL AS senderName\r\n"
			+ " FROM member_list\r\n"
			+ " UNION ALL\r\n"
			+ " SELECT c.member_list_id, m.message_id, m.sender_id, m.receiver_id, m.message, c.user_name AS receivername, ml.user_name AS senderName\r\n"
			+ " FROM chat_table AS m\r\n"
			+ " LEFT JOIN \r\n"
			+ "    member_list AS c ON c.member_list_id = m.receiver_id\r\n"
			+ " LEFT JOIN \r\n"
			+ "    member_list AS ml ON ml.member_list_id = m.sender_id"
			+ "	where (m.receiver_id =:id OR m.sender_id =:id)",nativeQuery = true)
	List<Map<String, Object>> getAllReceiptDetailsWithClientId1(Long id);
	@Query(value = "select c.* from chat_table as c"
			+ " where c.sender_id = :id or c.receiver_id = :id",nativeQuery = true)
	List<Map<String, Object>> getAllMessageDetailsWithId(Long id);
	
	@Query(value = "select * from member_list",nativeQuery = true)
	List<Map<String, Object>> getAllMessageDetails();


//	@Query(value = "select c.employee_id  as memberListId,c.email,c.phone_number as phoneNumber,c.role_type as roleType,c.user_name as receiverName,ml.user_name as senderName,m.message_id as messageId,m.date,m.intime,m.message,m.receiver_id as receiverId,m.sender_id as senderId\r\n"
//			+ "from chat_table as m\r\n"
//			+ "join employee as  c on c.employee_id=m.receiver_id\r\n"
//			+ "   join employee as  ml on ml.employee_id=m.sender_id\r\n"
//			+ "where  m.sender_id =:id AND  m.receiver_id =:receiver_id  ;",nativeQuery = true)
//	List<Map<String, Object>> getAllTraineeandEmployeeWithRole(@Param("id")Long sender_id, @Param("receiver_id")Long receiver_id);


//	@Query(value = "select m.member_list_id,m.email,m.phone_number,m.role_id,m.role_type,m.user_name,"
//			+ " cs.sender_id,cs.sender_name,cs.receiver_id,cs.receiver_name,cs.message_id,cs.intime,cs.message from member_list as m"
//			+ " left join chat_table as c on c.sender_id = m.member_list_id"
//			+ " left join chat_table as cs on cs.receiver_id = m.member_list_id"
//			+ " where m.member_list_id = :member_list_id", nativeQuery = true)
//	List<Map<String, Object>> getAllDetailsWithId(Long member_list_id);
	
	
	
	@Query(value = "SELECT m.member_list_id,m.email,m.phone_number,m.role_id,m.role_type,m.user_name,"
			+ " c.sender_id,c.sender_name,c.receiver_id,c.receiver_name,c.message_id,c.intime,c.message,c.date "
			+ " FROM  member_list AS m"
			+ " LEFT JOIN chat_table AS c ON c.sender_id = m.member_list_id OR c.receiver_id = m.member_list_id"
			+ " WHERE  m.member_list_id = :member_list_id ", nativeQuery = true)
	List<Map<String, Object>> getAllDetailsWithId(Long member_list_id);
	
	
	
	@Query(value = "select m.member_list_id,m.email,m.phone_number,m.role_id,m.role_type,"
			+ "m.user_name from member_list as m", nativeQuery = true)
	List<Map<String, Object>> getAllDetails();
}
