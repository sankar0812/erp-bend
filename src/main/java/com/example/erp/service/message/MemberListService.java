package com.example.erp.service.message;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.erp.entity.erecruitment.Training;
import com.example.erp.entity.message.MemberList;

import com.example.erp.repository.message.MemberListRepository;

@Service
public class MemberListService {
	
	
	@Autowired
	private MemberListRepository MemberListsRepository;
	

//view
	public List<MemberList> listAll() {
		return this.MemberListsRepository.findAll();
	}

//save
	public MemberList Save(MemberList trainee) {
		return MemberListsRepository.save(trainee);
	}

	public MemberList findById(Long traineeId) {
		return MemberListsRepository.findById(traineeId).get();
	}

	// delete
	public void deleteTraineeIdId(Long id) {
		MemberListsRepository.deleteById(id);
	}
	
	   public Optional<MemberList> getById1(long id) {
	        return Optional.of(MemberListsRepository.findById(id).get());
	    }

}
