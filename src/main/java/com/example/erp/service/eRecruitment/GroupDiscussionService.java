package com.example.erp.service.eRecruitment;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.erp.entity.erecruitment.GroupDiscussion;
import com.example.erp.entity.erecruitment.TaskAssigned;
import com.example.erp.repository.erecruitment.GroupDiscussionRepository;

@Service
public class GroupDiscussionService {
@Autowired
private GroupDiscussionRepository groupDiscussionRepository;


//view
	public List<GroupDiscussion> listAll() {
		return this.groupDiscussionRepository.findAll();
	}

//save
	public void SaveGroupDiscussion(GroupDiscussion discussion) {
		groupDiscussionRepository.save(discussion);
	}

//edit
	public GroupDiscussion getByCandidateId(Long candidateid) {
		return groupDiscussionRepository.findBycandidateId(candidateid).get();
	}
//delete
	public void deletegroupDiscussionId(Long id) {
		groupDiscussionRepository.deleteById(id);
	}

	

}
