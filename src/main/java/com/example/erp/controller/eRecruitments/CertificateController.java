package com.example.erp.controller.eRecruitments;

import java.io.IOException;
import java.sql.Blob;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.example.erp.entity.eRecruitments.Certificate;
import com.example.erp.repository.eRecruitments.CertificateRepository;
import com.example.erp.service.eRecruitments.CertificateService;

@RestController
@CrossOrigin
public class CertificateController {
@Autowired
private CertificateService certificateService;

@PostMapping("/certificate/save")
public ResponseEntity<String> saveCertificate(@RequestParam("traineeId") long traineeId,
		@RequestParam("trainingProgram") String trainingProgram,
		@RequestParam("hospitalName") String hospitalName,
		@RequestParam("certificateIssuedDate") Date certificateIssuedDate,
		@RequestParam("status") boolean status,
	 @RequestParam("authorizedSignature") MultipartFile authorizedSignature,
	 @RequestParam("officialLogo") MultipartFile officialLogo,
		@RequestParam("traineeSignature") MultipartFile traineeSignature) throws SQLException {

	try {
		Certificate certificate = new Certificate();
		certificate.setTrainingProgram(trainingProgram);
		certificate.setHospitalName(hospitalName);
		certificate.setTraineeId(traineeId);
		certificate.setCertificateIssuedDate(certificateIssuedDate);
		certificate.setStatus(status);
		certificate.setTraineeSignature(convertToBlob(traineeSignature));
		certificate.setAuthorizedSignature(convertToBlob(authorizedSignature));
		certificate.setOfficialLogo(convertToBlob(officialLogo));

		certificateService.SaveCertificate(certificate);

		return ResponseEntity.ok("Certificate Details saved successfully.");
	} catch (IOException e) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body("Error occurred while saving the certificate: " + e.getMessage());
	}
}
private Blob convertToBlob(MultipartFile file) throws IOException, SQLException {
	if (file != null && !file.isEmpty()) {
		byte[] bytes = file.getBytes();
		return new javax.sql.rowset.serial.SerialBlob(bytes);
	} else {
		return null;
	}
}
@GetMapping("/certificate/view")
public ResponseEntity<?> getCertificateDetails() {
	try {
		List<Certificate> certificate = certificateService.listAll();
		List<Certificate> certificateList = new ArrayList<>();
		for (Certificate certificates : certificate) {
			String authorizedSignatureUrl = "/authorizedSignature/" + certificates.getCertificateId();
			String traineeSignatureUrl = "/traineeSignature/" + certificates.getCertificateId();
			String officialLogoUrl = "/officialLogo/" + certificates.getCertificateId();
			
			
			Certificate certificateResponse = new Certificate();
			certificateResponse.setCertificateId(certificates.getCertificateId());
			certificateResponse.setHospitalName(certificates.getHospitalName());
			certificateResponse.setTraineeId(certificates.getTraineeId());
			certificateResponse.setTrainingProgram(certificates.getTrainingProgram());
			certificateResponse.setCertificateIssuedDate(certificates.getCertificateIssuedDate());
			certificateResponse.setStatus(certificates.isStatus());
			
			certificateResponse.setAuthorizedSignatureUrl(authorizedSignatureUrl);
			certificateResponse.setTraineeSignatureUrl(traineeSignatureUrl);
			certificateResponse.setOfficialLogoUrl(officialLogoUrl);

					certificateList.add(certificateResponse);
		}

		return ResponseEntity.ok(certificateList);
	} catch (Exception e) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body("Error occurred while retrieving certificate");
	}
}


@GetMapping("/officialLogo/{certificateId}")
public ResponseEntity<byte[]> downloadOfficialLogo(@PathVariable long certificateId) {
	try {
		Certificate certificate = certificateService.findById(certificateId);
		if (certificate != null) {
			Blob pdfBlob = certificate.getOfficialLogo();

			if (pdfBlob != null) {
				byte[] pdfBytes = pdfBlob.getBytes(1, (int) pdfBlob.length());

				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

				String filename = "officialLogo.pdf";
				headers.setContentDispositionFormData("attachment", filename);

				return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
			}
		}
		return ResponseEntity.notFound().build();
	} catch (SQLException e) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}
}



@GetMapping("/traineeSignature/{certificateId}")
public ResponseEntity<byte[]> downloadTraineeSignature(@PathVariable long certificateId) {
	try {
		Certificate certificate = certificateService.findById(certificateId);
		if (certificate != null) {
			Blob pdfBlob = certificate.getTraineeSignature();

			if (pdfBlob != null) {
				byte[] pdfBytes = pdfBlob.getBytes(1, (int) pdfBlob.length());

				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

				String filename = "traineeSignature.pdf";
				headers.setContentDispositionFormData("attachment", filename);

				return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
			}
		}
		return ResponseEntity.notFound().build();
	} catch (SQLException e) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}
}



@GetMapping("/authorizedSignature/{certificateId}")
public ResponseEntity<byte[]> downloadAuthorizedSignature(@PathVariable long certificateId) {
	try {
		Certificate certificate = certificateService.findById(certificateId);
		if (certificate != null) {
			Blob pdfBlob = certificate.getAuthorizedSignature();

			if (pdfBlob != null) {
				byte[] pdfBytes = pdfBlob.getBytes(1, (int) pdfBlob.length());

				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

				String filename = "authorizedSignature.pdf";
				headers.setContentDispositionFormData("attachment", filename);

				return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
			}
		}
		return ResponseEntity.notFound().build();
	} catch (SQLException e) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}
}
@PutMapping("/certificate/{certificateId}")
public ResponseEntity<String> updateCertificateDetails(@PathVariable long certificateId,
		@RequestParam("traineeId")long traineeId,
		@RequestParam("trainingProgram") String trainingProgram,
		@RequestParam("hospitalName")String hospitalName,
		@RequestParam("certificateIssuedDate") Date certificateIssuedDate,
		@RequestParam("status")boolean status,
		@RequestParam("traineeSignature") MultipartFile traineeSignature, 
		@RequestParam("officialLogo") MultipartFile officialLogo, 
		@RequestParam("authorizedSignature") MultipartFile authorizedSignature) {

	try {
		Certificate certificate = certificateService.findById(certificateId);
		if (certificate != null) {
			if (traineeSignature != null && !traineeSignature.isEmpty()) {
				Blob authorizedSignatureBlob = convertToBlob(traineeSignature);				
				certificate.setAuthorizedSignature(authorizedSignatureBlob);
			}

			if (officialLogo != null && !officialLogo.isEmpty()) {
				Blob officialLogoBlob = convertToBlob(officialLogo);
				certificate.setOfficialLogo(officialLogoBlob);
			}
			
			if (authorizedSignature != null && !authorizedSignature.isEmpty()) {
				Blob authorizedSignatureBlob = convertToBlob(authorizedSignature);
				certificate.setAuthorizedSignature(authorizedSignatureBlob);
			}
			certificate.setCertificateIssuedDate(certificateIssuedDate);
			certificate.setTraineeId(traineeId);
			certificate.setTrainingProgram(trainingProgram);
			certificate.setHospitalName(hospitalName);
			certificate.setStatus(status);

			certificateService.SaveCertificate(certificate);

			return ResponseEntity.ok("Certificate updated successfully.");
		}
		return ResponseEntity.notFound().build();
	} catch (SQLException | IOException e) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}
}


@DeleteMapping("/certificateDetail/delete/{id}")
public ResponseEntity<String> deleteCertificate(@PathVariable("id") Long id) {
	certificateService.deleteCertificateId(id);
	return ResponseEntity.ok("Certificate details deleted successfully");
}

@Autowired
private CertificateRepository certificateRepository;

@GetMapping("/trainingCompletedCertificateDetails")
public ResponseEntity<?> getTrainingCompletedCertificateDetails() {
    try {
        List<Map<String, Object>> certificates = certificateRepository.findTrainingCompletedCertificateDetails();
        List<Map<String, Object>> certificateList = new ArrayList<>();

        for (Map<String, Object> certificate : certificates) {
            Map<String, Object> certificateMap = new HashMap<>();

            String traineeSignatureUrl = "/traineeSignature/" + certificate.get("certificate_id");
            String officialLogoUrl = "/officialLogo/" + certificate.get("certificate_id");
            String authorizedSignatureUrl = "/authorizedSignature/" + certificate.get("certificate_id");
            
            certificateMap.put("traineeSignatureUrl", traineeSignatureUrl);
            certificateMap.put("authorizedSignatureUrl", authorizedSignatureUrl);
            certificateMap.put("officialLogoUrl", officialLogoUrl);
            certificateMap.putAll(certificate);
            certificateList.add(certificateMap);
        }
        return ResponseEntity.ok(certificateList);
    } catch (Exception e) {
        String errorMessage = "Error occurred while retrieving certificate details";
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(Collections.singletonMap("error", errorMessage));
    }
}
} 
