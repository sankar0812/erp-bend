package com.example.erp.service.eRecruitment;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.erp.entity.erecruitment.GroupDiscussion;
import com.example.erp.entity.erecruitment.HrInterview;
import com.example.erp.repository.erecruitment.HrInterviewRepository;

@Service
public class HrInterviewService {
@Autowired
private HrInterviewRepository hrInterviewRepository;
//view
	public List<HrInterview> listAll() {
		return this.hrInterviewRepository.findAll();
	}

//save
	public void SaveHrInterview(HrInterview hrInterview) {
		hrInterviewRepository.save(hrInterview);
	}

//edit
	public HrInterview getByCandidateId(Long candidateid) {
		return hrInterviewRepository.findBycandidateId(candidateid).get();
	}

//delete
	public void deleteHrInterviewId(Long id) {
		hrInterviewRepository.deleteById(id);
	}
}
