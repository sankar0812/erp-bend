package com.example.erp.repository.message;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.erp.entity.message.MemberList;

public interface MemberListRepository extends JpaRepository<MemberList, Long>{
	
	@Query(value=" select m.member_list_id as memberListId,m.email,m.phone_number as phoneNumber,m.role_type as roleType,m.user_name as userName"
			+ " from member_list as m", nativeQuery = true)
	List<Map<String, Object>>countOfCandidatesDetails();
	
	Optional<MemberList>findByIdAndRoleId(long id, long roleId);

	Optional<MemberList> findByEmail(String email);

	Optional<MemberList> findByPhoneNumber(String phoneNumber);
	
	
	

}
