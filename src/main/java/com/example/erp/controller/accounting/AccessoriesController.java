package com.example.erp.controller.accounting;

import java.io.IOException;
import java.sql.Blob;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.erp.entity.accounting.Accessories;
import com.example.erp.entity.accounting.CompanyAssets;
import com.example.erp.repository.accounting.AccessoriesRepository;
import com.example.erp.service.accounting.AccessoriesService;

@RestController
@CrossOrigin
public class AccessoriesController {

	@Autowired

	private AccessoriesService keyboardBrandService;

	@Autowired
	private AccessoriesRepository repo;
	
	
	@GetMapping("/accessories1")
	public ResponseEntity<?> displayAllImages(@RequestParam(required = true) String accessories) {
	    try {
	        if ("accessories".equals(accessories)) {
	            List<Accessories> announcements = keyboardBrandService.listAll();
	            List<Accessories> announcementResponses = new ArrayList<>();

	            for (Accessories announcement : announcements) {
	                int randomNumber = generateRandomNumber(); 
	              

	                String imageUrl = "accessories/" + randomNumber + "/" + announcement.getAccessoriesId() ;

	                Accessories announcementResponse = new Accessories();	            
	                announcementResponse.setAccessoriesId(announcement.getAccessoriesId());
	                announcementResponse.setUrl(imageUrl);
	                announcementResponse.setAccessoriesName(announcement.getAccessoriesName());
	                announcementResponse.setColor(announcement.getColor());

	                announcementResponses.add(announcementResponse);
	            }

	            return ResponseEntity.ok().body(announcementResponses);
	        } else {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	                .body("The provided announcementType is not supported.");
	        }
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	    }
	}

	@GetMapping("/accessories")
	public ResponseEntity<List<Map<String, Object>>> displayAllImages2(
			@RequestParam(required = true) String accessories) {
		try {
			if ("accessories".equals(accessories)) {
				List<Map<String, Object>> employeeDetails = repo.AllEmployeesaccessories();
				List<Map<String, Object>> employeeResponses = new ArrayList<>();
				for (Map<String, Object> employeeDetail : employeeDetails) {
					int randomNumber = generateRandomNumber();

					String imageUrl = "accessories/" + randomNumber + "/" + employeeDetail.get("accessories_id") ;
							
					Map<String, Object> employeeResponse = new HashMap<>();
					employeeResponse.put("accessoriesId", employeeDetail.get("accessories_id"));
					employeeResponse.put("accessoriesName", employeeDetail.get("accessories_name"));
					employeeResponse.put("color", employeeDetail.get("color"));
		
					employeeResponse.put("image", imageUrl);
					employeeResponses.add(employeeResponse);
				}
				return ResponseEntity.ok(employeeResponses);
			} else {

				return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
			}
		} catch (Exception e) {
			e.printStackTrace(); 
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	@PostMapping("/accessories/save")
	public ResponseEntity<String> addAnnouncementWithImage(
	        @RequestParam("image") MultipartFile file,
	        @RequestParam("accessoriesName") String accessoriesName,
	        @RequestParam("color") String color

	) {
	    try {
	      
	        byte[] bytes = file.getBytes();
	        Blob blob = new javax.sql.rowset.serial.SerialBlob(bytes);

	        Accessories accessories = new Accessories();
	        accessories.setImage(blob);
	        accessories.setAccessoriesName(accessoriesName);
	        accessories.setColor(color);

	    

	        keyboardBrandService.SaveorUpdate(accessories);

	        return ResponseEntity.status(HttpStatus.CREATED).body("Announcement details saved successfully.");
	    } catch (IOException | SQLException e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding announcement.");
	    }
	}




	private int generateRandomNumber() {
	    Random random = new Random();
	    return random.nextInt(1000000); // Change the upper limit if needed
	}

	@GetMapping("accessories/{randomNumber}/{id}")
	public ResponseEntity<ByteArrayResource> serveFile(@PathVariable("randomNumber") int randomNumber,
			@PathVariable("id") Long id) {
		Optional<Accessories> companyAssetsOptional = keyboardBrandService.getKeyboardBrandById(id);

		if (companyAssetsOptional.isPresent()) {
			Accessories companyAssets = companyAssetsOptional.get();
			String filename = "file_" + randomNumber + "_" + id;

			try {
				byte[] fileBytes = companyAssets.getImage().getBytes(1, (int) companyAssets.getImage().length());

				String extension = determineFileExtension(fileBytes);
				MediaType mediaType = determineMediaType(extension);

				ByteArrayResource resource = new ByteArrayResource(fileBytes);

				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(mediaType);
				headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + filename + "." + extension);

				return ResponseEntity.ok().headers(headers).body(resource);
			} catch (SQLException e) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
			}
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	private String determineFileExtension(byte[] fileBytes) {
		try {
			String fileSignature = bytesToHex(Arrays.copyOfRange(fileBytes, 0, 4));
			if (fileSignature.startsWith("89504E47")) {
				return "png";
			} else if (fileSignature.startsWith("FFD8FF")) {
				return "jpg";
			} else if (fileSignature.startsWith("52494646")) {
				return "webp";
			} else if (fileSignature.startsWith("47494638")) {
				return "gif";
			} else if (fileSignature.startsWith("66747970")) {
				return "mp4";
			} else if (fileSignature.startsWith("25504446")) {
				return "pdf";
			}
		} catch (Exception e) {
		}
		return "unknown";
	}

	private MediaType determineMediaType(String extension) {
		switch (extension) {
		case "png":
			return MediaType.IMAGE_PNG;
		case "jpg":
			return MediaType.IMAGE_JPEG;
		case "pdf":
			return MediaType.APPLICATION_PDF;
		case "webp":
			return MediaType.parseMediaType("image/webp");
		case "gif":
			return MediaType.parseMediaType("image/gif");
		case "mp4":
			return MediaType.parseMediaType("video/mp4");
		default:
			return MediaType.APPLICATION_OCTET_STREAM;
		}
	}

	private String bytesToHex(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (byte b : bytes) {
			sb.append(String.format("%02X", b));
		}
		return sb.toString();
	}
	@PutMapping("/accessories/edit/{accessoriesId}")
	public ResponseEntity<?> updateAnnouncement(  @PathVariable Long accessoriesId, 
			@RequestParam(value ="image", required = false) MultipartFile file,
            @RequestParam(value ="accessoriesName", required = false) String accessoriesName,
            @RequestParam(value ="color", required = false) String color
			) {
		  try {
	            Accessories existingAnnouncement = keyboardBrandService.findById(accessoriesId);

	           

	            // Update other fields
	            existingAnnouncement.setAccessoriesName(accessoriesName);
	            existingAnnouncement.setColor(color);
	
	            
	            if (file != null && !file.isEmpty()) {
	                byte[] bytes = file.getBytes();
	                Blob blob = new javax.sql.rowset.serial.SerialBlob(bytes);
	                existingAnnouncement.setImage(blob);
	            }
	           keyboardBrandService.save(existingAnnouncement);
	            return ResponseEntity.ok(existingAnnouncement);
	        } catch (IOException | SQLException e) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating employee.");
	        }
	    }

	

	@DeleteMapping("/accessories/delete/{accessoriesId}")
	public ResponseEntity<String> deleteKeyboardBrand(@PathVariable("accessoriesId") Long accessoriesId) {
		keyboardBrandService.deleteKeyboardBrandById(accessoriesId);
		return ResponseEntity.ok("accessories deleted successfully");

	}
	
	
}
