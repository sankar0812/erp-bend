package com.example.erp.repository.organization;
 
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.erp.entity.organization.Announcements;

public interface AnnouncementsRepository extends JpaRepository<Announcements, Long> {

	@Query(value = "  select a.announcement_id as announcementId,a.from_date ,a.informed_by,a.filename as fileName,a.publish,a.published,a.title,a.to_date  ,a.description,a.image_status as imageStatus "
			+ " from announcements as a WHERE a.published = true AND a.to_date > CURRENT_DATE;", nativeQuery = true)
	List<Map<String, Object>> getAllByClientDetails();

	@Query(value = "SELECT a.announcement_id AS announcementId, a.from_date AS fromDate, a.informed_by AS informedBy, a.filename as fileName,a.publish, a.published, a.title, a.to_date AS toDate, a.description, a.image_status AS imageStatus "
			+ "FROM announcements AS a WHERE a.published = true AND a.to_date > CURRENT_DATE AND announcement_id = :announcement_id", nativeQuery = true)
	List<Map<String, Object>> getAllByClientDetailsById(@Param("announcement_id") Long announcement_id);

	@Query(value = "  select a.announcement_id as announcementId,a.from_date as fromDate,a.informed_by as informedBy,a.publish,a.published,"
			+ "a.title,a.to_date as toDate ,a.description,a.image_status as imageStatus,a.filename as fileName"
			+ " from announcements as a" + " WHERE YEAR(a.to_date) = YEAR(CURDATE())", nativeQuery = true)
	List<Map<String, Object>> AllHolidaysinTable();

	@Query(value = "  select a.announcement_id as announcementId,a.from_date as fromDate,a.informed_by as informedBy,a.filename as fileName,a.publish,a.published,a.status,a.title,a.to_date as toDate,a.description,a.image_status as imageStatus "
			+ " from announcements as a" + " WHERE YEAR(a.to_date) = :year", nativeQuery = true)
	List<Map<String, Object>> getAllpromotions(@Param("year") String year);

	@Query(value = "  select a.announcement_id as announcementId,a.from_date as fromDate,a.informed_by as informedBy,a.filename as fileName,a.publish,a.published,a.status,a.title,a.to_date as toDate,a.description,a.image_status as imageStatus"
			+ " from announcements as a" + " WHERE a.published=false ", nativeQuery = true)
	List<Map<String, Object>> getAllpromotionssemployee();

	@Query(value = "  select a.announcement_id as announcementId,a.from_date as fromDate,a.informed_by as informedBy,a.filename as fileName,a.publish,a.published,a.status,a.title,a.to_date as toDate,a.description,a.image_status as imageStatus"
			+ " from announcements as a" + " WHERE a.published=true ", nativeQuery = true)
	List<Map<String, Object>> Allfilter3absentlisttrainee();

}
