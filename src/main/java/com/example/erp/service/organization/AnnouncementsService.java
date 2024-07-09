package com.example.erp.service.organization;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.erp.entity.organization.Announcements;
import com.example.erp.repository.organization.AnnouncementsRepository;



@Service
public class AnnouncementsService {

	@Autowired
	private AnnouncementsRepository announcementsRepository;

	public List<Announcements> listAll() {
		return this.announcementsRepository.findAll();

	}

	public void SaveorUpdate(Announcements announcement) {
		announcementsRepository.save(announcement);
	}

	public void save(Announcements announcement) {
		announcementsRepository.save(announcement);

	}

	public Announcements findById(Long announcement_id) {
		return announcementsRepository.findById(announcement_id).get();

	}

	public void deleteAnnouncementsIdById(Long announcement_id) {
		announcementsRepository.deleteById(announcement_id);
	}

	public Optional<Announcements> getAnnouncementsById(Long announcement_id) {
		return announcementsRepository.findById(announcement_id);

	}





}
