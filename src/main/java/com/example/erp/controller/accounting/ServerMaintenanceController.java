package com.example.erp.controller.accounting;

import java.io.IOException;
import java.sql.Blob;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import javax.sql.rowset.serial.SerialBlob;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.erp.entity.accounting.ServerMaintenance;
import com.example.erp.repository.accounting.ServerMaintenanceRepository;
import com.example.erp.service.accounting.ServerMaintenanceService;

@RestController
@CrossOrigin
public class ServerMaintenanceController {
	
	@Autowired
 private ServerMaintenanceService service;
	
	@Autowired 
	private ServerMaintenanceRepository repo;
	
	@PostMapping("/serverMaintenance/save")
	public ResponseEntity<String> saveComplaints(@RequestParam(value="serverBilling",required = false) MultipartFile file,
	                                            @RequestParam(value="serverTypeId",required = false) long serverTypeId,
	                                            @RequestParam(value="amount",required = false) int amount,
	                                            @RequestParam(value="date",required = false) Date date
	                                   ) {
	    try {
	        byte[] bytes = file.getBytes();
	        Blob blob = new SerialBlob(bytes);
	        ServerMaintenance server = new ServerMaintenance();
	        server.setStatus(true);
	        server.setServerBilling(blob);
	        server.setDate(date);
	        server.setAmount(amount);
	        server.setServerTypeId(serverTypeId);    
	        service.SaveorUpdate(server);

	        return ResponseEntity.ok("ServerMaintenance saved with id: " + server.getServerMaintenanceId());
	    } catch (IOException | SQLException e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("Error saving Complaints: " + e.getMessage());
	    }
	}



	  @PutMapping("/serverMaintenance/status/{id}")
	    public ResponseEntity<Boolean> toggleComplaintsStatus(@PathVariable(name = "id") long id) {
	        try {
	        	ServerMaintenance complaints = service.getById(id);
	            if (complaints != null) {
	                // Toggle the status
	                boolean currentStatus = complaints.isStatus();
	                complaints.setStatus(!currentStatus);
	                service.SaveorUpdate(complaints); // Save the updated complaints
	            } else {
	                return ResponseEntity.ok(false); // Complaints with the given ID does not exist, return false
	            }

	            return ResponseEntity.ok(complaints.isStatus()); // Return the new status (true or false)
	        } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                    .body(false); // Set response to false in case of an error
	        }
	    }
	  
	  @GetMapping("/serverMaintenance")
	  public ResponseEntity<?> displayAllImages(@RequestParam(required = true) String serverMaintenance) {
	      try {
	          if ("serverMaintenance".equals(serverMaintenance)) {
	              Iterable<ServerMaintenance> images = service.listAll();

	              List<ServerMaintenance> imageObjects = new ArrayList<>();
	              for (ServerMaintenance image : images) {
	                  int randomNumber = generateRandomNumber(); 
	                  String imageUrl = "serverMaintenance/" + randomNumber + "/" + image.getServerMaintenanceId();
	                  ServerMaintenance imageObject = new ServerMaintenance();
	                  imageObject.setServerMaintenanceId(image.getServerMaintenanceId());
	                  imageObject.setServerTypeId(image.getServerTypeId());
	                  imageObject.setAmount(image.getAmount());
	                  imageObject.setDate(image.getDate());
	                  imageObject.setStatus(image.isStatus());
	                  imageObject.setUrl(imageUrl);	                  	                 
	                  imageObjects.add(imageObject);
	              }

	              return ResponseEntity.ok().body(imageObjects);
	          } else {
	             
	              String errorMessage = "The provided viewType is not supported.";
	              return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
	          }
	      } catch (Exception e) {
	          e.printStackTrace(); 
	          return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	      }
	  }

		private int generateRandomNumber() {
		    Random random = new Random();
		    return random.nextInt(1000000); // Change the upper limit if needed
		}


		@GetMapping("serverMaintenance/{randomNumber}/{id}")
		public ResponseEntity<ByteArrayResource> serveFile(
		        @PathVariable("randomNumber") int randomNumber,
		        @PathVariable("id") Long id
		) {
		    Optional<ServerMaintenance> complaintsOptional = service.getServerListById(id);
		    if (complaintsOptional.isPresent()) {
		    	ServerMaintenance complaints = complaintsOptional.get();
		        String filename = "file_" + randomNumber + "_" + id;
		        byte[] fileBytes;
		        try {
		            fileBytes = complaints.getServerBilling().getBytes(1, (int) complaints.getServerBilling().length());
		        } catch (SQLException e) {
		            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		        }

		        String extension = determineFileExtension(fileBytes);
		        MediaType mediaType = determineMediaType(extension);

		        ByteArrayResource resource = new ByteArrayResource(fileBytes);
		        HttpHeaders headers = new HttpHeaders();
		        headers.setContentType(mediaType);
		        headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + filename + "." + extension);
		        return ResponseEntity.ok().headers(headers).body(resource);
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
		        } else if (fileSignature.startsWith("66747970") || fileSignature.startsWith("00000020")) {
		            return "mp4";
		        } else if (fileSignature.startsWith("25504446")) {
		            return "pdf";
		        }
		    } catch (Exception e) {
		        // Handle or log the exception
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
		

	@PutMapping("/serverMaintenance/edit/{id}")
	public ResponseEntity<?> updateComplaints( @PathVariable Long id ,@RequestParam(value="serverBilling",required = false) MultipartFile file,
                     @RequestParam(value="serverTypeId",required = false) long serverTypeId,
                     @RequestParam(value="amount",required = false) int amount,
                     @RequestParam(value="date",required = false) Date date )
             {
		try {
			ServerMaintenance existingComplaints = service.getById(id);
			       

			if (existingComplaints == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee not found.");
			}
			existingComplaints.setAmount(amount);
			existingComplaints.setDate(date);
			existingComplaints.setServerTypeId(serverTypeId);
						
			  if (file != null && !file.isEmpty()) {
	                byte[] bytes = file.getBytes();
	                Blob blob = new javax.sql.rowset.serial.SerialBlob(bytes);
	                existingComplaints.setServerBilling(blob);
	            }
	            service.SaveorUpdate(existingComplaints);

	            return ResponseEntity.ok("serverMaintenance updated successfully.Complaints ID: " + id);
	        } catch (IOException | SQLException e) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating employee.");
	        }
	    }

	@DeleteMapping("/serverMaintenance/delete/{id}")
	public ResponseEntity<String> deleteComplaints(@PathVariable("id") long id) {
		try {
			service.deleteAssestIdById(id);
			return ResponseEntity.ok("serverMaintenance deleted successfully");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error deleting serverMaintenance: " + e.getMessage());
		}
	}
	
	private String getFileExtensionForImage(Map<String, Object> employeeDetail) {
		if (employeeDetail == null || !employeeDetail.containsKey("url") || employeeDetail.get("url") == null) {
			return "jpg";
		}
		String url = (String) employeeDetail.get("url");
		if (url.endsWith(".png")) {
			return "png";
		} else if (url.endsWith(".jpg")) {
			return "jpg";
		} else {
			return "jpg";
		}
	}
	
	@GetMapping("/serverMaintenance/view")
	public ResponseEntity<List<Map<String, Object>>> displayAllImages1(@RequestParam(required = true) String view) {
	    try {
	        if ("serverMaintenance".equals(view)) {
	            List<Map<String, Object>> images = repo.AllserverMaintenance1();
	            List<Map<String, Object>> imageResponses = new ArrayList<>();

	            for (Map<String, Object> image : images) {
	                int randomNumber = generateRandomNumber();
	                String imageUrl = "serverMaintenance/" + randomNumber + "/" + image.get("server_maintenance_id");
	                Map<String, Object> imageResponse = new HashMap<>();
	                imageResponse.put("amount", image.get("amount"));
	                imageResponse.put("serverMaintenanceId", image.get("server_maintenance_id"));
	                imageResponse.put("date", image.get("date"));
	                imageResponse.put("serverTypeId", image.get("server_type_id"));
	                imageResponse.put("status", image.get("status"));
	                imageResponse.put("serverTypeName", image.get("server_type_name"));
	                imageResponse.put("serverBilling", imageUrl);
	                imageResponses.add(imageResponse);
	            }
	            return ResponseEntity.ok(imageResponses);
	        } else {	        
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	    }
	}
	
	@PostMapping("/serverMaintenance/manager")
	public ResponseEntity<List<Map<String, Object>>> getAllVoucherBetweenDatesAndYearOrMonth(
	        @RequestBody Map<String, Object> requestBody) {

	    if (!requestBody.containsKey("choose")) {
	        return ResponseEntity.badRequest().build();
	    }

	    String choose = requestBody.get("choose").toString();

	    switch (choose) {
	    
	    case "sixMonth":
            List<Map<String, Object>> leaveData3 = repo.getAllReceiptDetailsWithClientIdAndSixMonths();
            return ResponseEntity.ok(leaveData3);
	        case "year":
	            if (requestBody.containsKey("year")) {
	                String year = requestBody.get("year").toString();
	                List<Map<String, Object>> leaveData = repo.getAllReceiptDetailsWithClientIdAnd(year);
	                return ResponseEntity.ok(leaveData);
	            }
	            break;

	        case "month":
	            if (requestBody.containsKey("monthName")) {
	                String monthName = requestBody.get("monthName").toString();
	                List<Map<String, Object>> leaveData1 = repo.getAllReceiptDetailsWithClientIdAndMonth(monthName);
	                return ResponseEntity.ok(leaveData1);
	            }
	            break;

	        default:
	            return ResponseEntity.badRequest().build();
	    }

	    return ResponseEntity.badRequest().build();
	}



}
