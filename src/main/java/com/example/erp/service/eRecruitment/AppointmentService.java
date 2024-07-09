package com.example.erp.service.eRecruitment;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.erp.entity.erecruitment.Appointment;
import com.example.erp.entity.erecruitment.Offer;
import com.example.erp.repository.erecruitment.AppointmentRepository;

@Service
public class AppointmentService {
	@Autowired
	private AppointmentRepository appointmentRepository;

//view
	public List<Appointment> listAll() {
		return this.appointmentRepository.findAll();
	}

//save
	public void SaveAppointment(Appointment appointment) {
		appointmentRepository.save(appointment);
	}

//edit
	public Appointment getByCandidateId(Long candidateid) {
		return appointmentRepository.findBycandidateId(candidateid).get();
	}

//delete
	public void deleteAppointmentId(Long id) {
		appointmentRepository.deleteById(id);
	}



}
