	package com.example.erp.controller.accounting;

import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Map.Entry;
import java.util.stream.Collectors;

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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.erp.entity.accounting.Server;
import com.example.erp.entity.accounting.ServerList;
import com.example.erp.repository.accounting.ServerRepository;
import com.example.erp.service.accounting.ServerListService;
import com.example.erp.service.accounting.ServerService;

@RestController
@CrossOrigin
public class ServerController {

	@Autowired
	private ServerService service;

	@Autowired
	private ServerRepository serverRepository;

	@Autowired
	private ServerListService serverListService;

	@GetMapping("/server")
	public ResponseEntity<Object> getAllRoleByEmployee(@RequestParam(required = true) String serverParam) {
		if ("server".equals(serverParam)) {
			List<Map<String, Object>> serverList = new ArrayList<>();
			List<Map<String, Object>> serverRole = serverRepository.getAllServerDetails();
			Map<String, List<Map<String, Object>>> serverGroupMap = serverRole.stream()
					.collect(Collectors.groupingBy(action -> action.get("server_id").toString()));

			for (Entry<String, List<Map<String, Object>>> serverLoop : serverGroupMap.entrySet()) {
				Map<String, Object> serverMap = new HashMap<>();
				serverMap.put("serverId", Long.parseLong(serverLoop.getKey()));
				serverMap.put("status", serverLoop.getValue().get(0).get("status"));
				serverMap.put("date", serverLoop.getValue().get(0).get("date"));
				serverMap.put("serverName", serverLoop.getValue().get(0).get("server_name"));

				int randomNumber = generateRandomNumber();
				String imageUrl = "server/" + randomNumber + "/" + Long.parseLong(serverLoop.getKey());
				serverMap.put("serverTypeUrl", imageUrl);
				List<Map<String, Object>> serverSubList = new ArrayList<>();
				for (Map<String, Object> serverSubLoop : serverLoop.getValue()) {

					Map<String, Object> serverSubMap = new HashMap<>();
					serverSubMap.put("serverListId", serverSubLoop.get("server_list_id"));
					serverSubMap.put("amount", serverSubLoop.get("amount"));
					serverSubMap.put("serverTypeName", serverSubLoop.get("server_type_name"));
					serverSubMap.put("serverTypeId", serverSubLoop.get("server_type_id"));
					serverSubList.add(serverSubMap);
				}
				serverMap.put("serverList", serverSubList);
				serverList.add(serverMap);
			}

			return ResponseEntity.ok(serverList);
		} else {
			String errorMessage = "Invalid value for 'department'. Expected 'Department'.";
			return ResponseEntity.badRequest().body(errorMessage);
		}
	}

	private int generateRandomNumber() {
		Random random = new Random();
		return random.nextInt(1000000);
	}

	@PostMapping("/server/save")
	public ResponseEntity<?> saveBank(@RequestBody Server server) {
		try {

			String base64Image = server.getServerTypeUrl();
			byte[] imageBytes = Base64.getDecoder().decode(base64Image);
			Blob blob = null;
			try {
				blob = new javax.sql.rowset.serial.SerialBlob(imageBytes);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			server.setServerFileUpload(blob);
			server.setStatus(true);
			service.SaveorUpdate(server);
			return ResponseEntity.status(HttpStatus.CREATED).body(server);
		} catch (Exception e) {
			String errorMessage = "An error occurred while saving server details.";
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);

		}

	}

	@GetMapping("server/{randomNumber}/{id}")
	public ResponseEntity<ByteArrayResource> serveFile(@PathVariable("randomNumber") int randomNumber,
			@PathVariable("id") Long id) {
		Optional<Server> serverAssestOptional = service.getServerById(id);

		if (serverAssestOptional.isPresent()) {
			Server companyAssets = serverAssestOptional.get();
			String filename = "file_" + randomNumber + "_" + id;

			try {
				byte[] fileBytes = companyAssets.getServerFileUpload().getBytes(1,
						(int) companyAssets.getServerFileUpload().length());

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

	@RequestMapping("/server/{serverId}")
	private Server getAssest(@PathVariable(name = "serverId") long serverId) {
		return service.findById(serverId);
	}



	@PutMapping("/server/edit/{id}")
	public ResponseEntity<?> updateServerPatch(@PathVariable Long id, @RequestBody Server updatedServer) {
		try {
			Optional<Server> existingServerOptional = service.getServerById(id);

			if (existingServerOptional.isPresent()) {
				Server existingServer = existingServerOptional.get();

				// Update other fields as needed
				if (updatedServer.getDate() != null) {
					existingServer.setDate(updatedServer.getDate());
				}
				if (updatedServer.getPaying() != null) {
					existingServer.setPaying(updatedServer.getPaying());
				}
				if (updatedServer.getServerName() != null) {
					existingServer.setServerName(updatedServer.getServerName());
				}
				if (updatedServer.getServerTypeUrl() != null) {
					existingServer.setServerTypeUrl(updatedServer.getServerTypeUrl());

					String base64Image = updatedServer.getServerTypeUrl();
					byte[] imageBytes = Base64.getDecoder().decode(base64Image);
					Blob blob = new SerialBlob(imageBytes);
					existingServer.setServerFileUpload(blob);
				}

				if (updatedServer.getServerList() != null && !updatedServer.getServerList().isEmpty()) {
					List<ServerList> existingServerList = existingServer.getServerList();
					List<ServerList> updatedServerList = updatedServer.getServerList();

					for (ServerList updatedList : updatedServerList) {
						Optional<ServerList> existingListOptional = existingServerList.stream().filter(
								serverList -> serverList.getServerListId().equals(updatedList.getServerListId()))
								.findFirst();

						existingListOptional.ifPresent(existingList -> {
							existingList.setServerTypeId(
									updatedList.getServerTypeId() != null ? updatedList.getServerTypeId()
											: existingList.getServerTypeId());
							existingList.setAmount(
									updatedList.getAmount() != 0 ? updatedList.getAmount() : existingList.getAmount());
						});
					}
				}

				existingServer.setStatus(updatedServer.isStatus());

				service.save(existingServer);

				return ResponseEntity.ok(existingServer);
			} else {
				return ResponseEntity.notFound().build();
			}
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = "An error occurred while updating server details.";
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
		}

	}

	@DeleteMapping("/server/delete/{serverId}")
	public ResponseEntity<String> deleteprojectName(@PathVariable("serverId") Long serverId) {
		service.deleteAssestIdById(serverId);
		return ResponseEntity.ok("server deleted successfully");

	}
	
	
	@GetMapping("/server/report")
	public List<Map<String, Object>> allDeptDetailspreviousYearincome(@RequestParam(required = true) String server) {
	    try {
	        if ("report".equals(server)) {
	            return serverRepository.getAllpromotionsBetweenDatesserver();
	        } else {	       
	            return Collections.emptyList();
	        }
	    } catch (Exception e) {	        
	        e.printStackTrace(); 
	        return Collections.emptyList();
	    }
	}
	
	@PostMapping("/server/manager")
	public ResponseEntity<List<Map<String, Object>>> getAllVoucherBetweenDatesAndYearOrMonth(
	        @RequestBody Map<String, Object> requestBody) {

	    if (!requestBody.containsKey("choose")) {
	        return ResponseEntity.badRequest().build();
	    }

	    String choose = requestBody.get("choose").toString();

	    switch (choose) {
	        case "date":
	            if (requestBody.containsKey("startDate") && requestBody.containsKey("endDate")) {
	                LocalDate startDate = LocalDate.parse(requestBody.get("startDate").toString(), DateTimeFormatter.ISO_DATE);
	                LocalDate endDate = LocalDate.parse(requestBody.get("endDate").toString(), DateTimeFormatter.ISO_DATE);
	                return ResponseEntity.ok(serverRepository.getAllpromotionsBetweenDates(startDate, endDate));
	            }
	            break;

	        case "year":
	            if (requestBody.containsKey("year")) {
	                String year = requestBody.get("year").toString();
	                List<Map<String, Object>> leaveData = serverRepository.getAllReceiptDetailsWithClientIdAnd(year);
	                return ResponseEntity.ok(leaveData);
	            }
	            break;

	        case "month":
	            if (requestBody.containsKey("monthName")) {
	                String monthName = requestBody.get("monthName").toString();
	                List<Map<String, Object>> leaveData1 = serverRepository.getAllReceiptDetailsWithClientIdAndMonth(monthName);
	                return ResponseEntity.ok(leaveData1);
	            }
	            break;

	        default:
	            return ResponseEntity.badRequest().build();
	    }

	    return ResponseEntity.badRequest().build();
	}


}
