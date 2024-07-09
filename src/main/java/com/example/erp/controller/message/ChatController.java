package com.example.erp.controller.message;

import java.math.BigInteger;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.erp.entity.accounting.Brand;
import com.example.erp.entity.message.MemberList;
import com.example.erp.entity.message.Message;
import com.example.erp.repository.message.MessageRepository;
import com.example.erp.service.message.MemberListService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@CrossOrigin
public class ChatController {

	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;

	@Autowired
	private MemberListService listService;

	private final ObjectMapper objectMapper; // Declare objectMapper

	@Autowired
	public ChatController(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	@Autowired
	private MessageRepository messageRepository;

	@MessageMapping("/message1")
	public void receiveMessage(@Payload Message message) {
		messageRepository.save(message);
		simpMessagingTemplate.convertAndSend("/chatroom/public", message);
	}

	
	@MessageMapping("/message")
	public void receiveMessage1(@Payload Message message) {
	    try {
	        // Save the received message to the repository
	        messageRepository.save(message);
	        
	        // Broadcast the received message to all subscribers
	        simpMessagingTemplate.convertAndSend("/chatroom/public", message);
	    } catch (Exception e) {
	        // Handle any exceptions that occur during message processing
	        e.printStackTrace();
	    }
	}


	@MessageMapping("/private-message")
	public void recMessage(@Payload Message message) {
		messageRepository.save(message);
		simpMessagingTemplate.convertAndSendToUser(message.getReceiverName(), "/private", message);
	}

//    @MessageMapping("/private-message")
//    public void recMessage(Message message) throws JsonProcessingException {
//        messageRepository.save(message);
//        String jsonMessage = objectMapper.writeValueAsString(message);
//        String userDestination = "/user/" + message.getReceiverId() + "/private";
//        simpMessagingTemplate.convertAndSend(userDestination, jsonMessage);
//    }

//    @MessageMapping("/private-message")
//    public void recMessage(Message message) throws JsonProcessingException {
//        // Save the received message to the repository
//        messageRepository.save(message);
//
//        // Convert the message to JSON
//        String jsonMessage = objectMapper.writeValueAsString(message);
//
//        // Send the JSON message back to the sender's private destination
//        String userDestination = "/user/" + message.getSenderId() + "/private";
//        simpMessagingTemplate.convertAndSend(userDestination, jsonMessage);
//    }

	@GetMapping("member/{randomNumber}/{id}")
	public ResponseEntity<ByteArrayResource> serveFile(@PathVariable("randomNumber") int randomNumber,
			@PathVariable("id") Long id) {
		Optional<MemberList> complaintsOptional = listService.getById1(id);
		if (complaintsOptional.isPresent()) {
			MemberList complaints = complaintsOptional.get();

			if (complaints.getProfile() != null) {
				String filename = "file_" + randomNumber + "_" + id;
				byte[] fileBytes;
				try {
					fileBytes = complaints.getProfile().getBytes(1, (int) complaints.getProfile().length());
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
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ByteArrayResource(new byte[0]));
			}
		} else {
			return ResponseEntity.notFound().build();
		}
	}
//  @MessageMapping("/private-message")
//  public void recMessage(@Payload Message message) {
//      messageRepository.save(message);
//      simpMessagingTemplate.convertAndSend(message.getReceiverId(), "/private", message);
//  }

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

//			  @GetMapping("/member")
//			  public ResponseEntity<?> displayAllImages(@RequestParam(required = true) String MemberList) {
//			      try {
//			          if ("MemberList".equals(MemberList)) {
//			              List<MemberList> images = listService.listAll();
//
//			              List<MemberList> imageObjects = new ArrayList<>();
//			              for (MemberList image : images) {
//			                  int randomNumber = generateRandomNumber(); 
//			                  String imageUrl = "member/" + randomNumber + "/" + image.getMemberListId();
//			                  MemberList imageObject = new MemberList();
//			                  imageObject.setRoleType(image.getRoleType());
//			                  imageObject.setMemberListId(image.getMemberListId());
//			                  imageObject.setEmail(image.getEmail());
//			                  imageObject.setPhoneNumber(image.getPhoneNumber());
//			                  imageObject.setRoleId(image.getRoleId());
//			                  imageObject.setUserName(image.getUserName());
//			                  imageObject.setUrl(imageUrl);	                  	                 
//			                  imageObjects.add(imageObject);
//			                  
//
//			              }
//
//			              return ResponseEntity.ok().body(imageObjects);
//			          } else {
//			             
//			              String errorMessage = "The provided viewType is not supported.";
//			              return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
//			          }
//			      } catch (Exception e) {
//			          e.printStackTrace(); 
//			          return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//			      }
//			  }
	private int generateRandomNumber() {
		Random random = new Random();
		return random.nextInt(1000000);
	}

	@GetMapping("/message/list")
	@ResponseBody
	public List<Map<String, Object>> getAllReg(@RequestParam(required = true) String message) {
		try {
			if ("List".equalsIgnoreCase(message)) {
				return messageRepository.MessageList();
			} else {
				throw new IllegalArgumentException("Invalid parameter value. 'message' must be 'List'.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	@GetMapping("/message/{sender_id}/{receiver_id}")
	@ResponseBody
	public ResponseEntity<?> getTaskAssignedDetailsp(@PathVariable Long sender_id, @PathVariable Long receiver_id) {
		try {

			List<Map<String, Object>> tasks = messageRepository.getAllTraineeandEmployeeWithRole(sender_id,
					receiver_id);
			List<Map<String, Object>> taskList = new ArrayList<>();

			for (Map<String, Object> taskAssigned : tasks) {
				Map<String, Object> taskAssignedMap = new HashMap<>();

				int randomNumber = generateRandomNumber();
				String fileExtension = getFileExtensionForImage(taskAssigned);
				String imageUrl = "profile/" + randomNumber + "/" + taskAssigned.get("memberListId") + "."
						+ fileExtension;

				taskAssignedMap.put("profile", imageUrl);
				taskAssignedMap.putAll(taskAssigned);
				taskList.add(taskAssignedMap);
			}
			return ResponseEntity.ok(taskList);

		} catch (Exception e) {
			String errorMessage = "Error occurred while retrieving task assigned details";
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Collections.singletonMap("error", errorMessage));
		}
	}

	@GetMapping("/message1/{sender_id}/{receiver_id}")
	@ResponseBody
	public List<Map<String, Object>> getAllMemberDetailsByMemberId8(@PathVariable Long sender_id,
			@PathVariable Long receiver_id) {
		return messageRepository.getAllTraineeandEmployeeWithRole(sender_id, receiver_id);
	}

	@GetMapping("/chat/{sender_id}")
	@ResponseBody
	public List<Map<String, Object>> getAllMemberDetailsBy(@PathVariable Long sender_id) {
		return messageRepository.getAllTraineeandEmployeeWith(sender_id);
	}
//			    @MessageMapping("/private-message")
//			    public void recMessadfgrth(@Payload Message message) {
//			        messageRepository.save(message);
//			        simpMessagingTemplate.convertAndSendToUser(message.getReceiverId(), "/private", message);
//			    }	

	@GetMapping("/member")
	public ResponseEntity<?> getTaskAssignedToProjectHead(@RequestParam(required = true) String MemberList) {
		try {
			if ("MemberList".equals(MemberList)) {
				List<Map<String, Object>> tasks = messageRepository.MessageListEmployee();
				List<Map<String, Object>> taskList = new ArrayList<>();

				for (Map<String, Object> taskAssigned : tasks) {
					Map<String, Object> taskAssignedMap = new HashMap<>();
					String fileExtension = getFileExtensionForImage(taskAssigned);
					int randomNumber = generateRandomNumber();
					String imageUrl = "profile/" + randomNumber + "/" + taskAssigned.get("member_list_id") + "."
							+ fileExtension;
					taskAssignedMap.put("profile", imageUrl);
					taskAssignedMap.putAll(taskAssigned);
					taskList.add(taskAssignedMap);
				}
				return ResponseEntity.ok(taskList);
			} else {
				String errorMessage = "Invalid value for 'dashboard'. Expected 'projectmanager'.";
				return ResponseEntity.badRequest().body(Collections.singletonMap("error", errorMessage));
			}
		} catch (Exception e) {
			String errorMessage = "Error occurred while retrieving task assigned details";
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Collections.singletonMap("error", errorMessage));
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

	@PostMapping("/message/save")
	public ResponseEntity<?> saveMessage(@RequestBody Message message) {
		try {
			messageRepository.save(message);
			List<Map<String, Object>> savedMessages = messageRepository
					.getAllTraineeandEmployeeWithRole(message.getSenderId(), message.getReceiverId());
			List<Map<String, Object>> responseList = new ArrayList<>();
			for (Map<String, Object> savedMessage : savedMessages) {
				Map<String, Object> messageMap = new HashMap<>();
				messageMap.put("messageId", savedMessage.get("messageId"));
				messageMap.put("receiverId", savedMessage.get("receiverId"));
				messageMap.put("message", savedMessage.get("message"));
				messageMap.put("senderId", savedMessage.get("senderId"));
				messageMap.put("receiverName", savedMessage.get("receiverName"));
				messageMap.put("senderName", savedMessage.get("senderName"));
				responseList.add(messageMap);
			}
			return ResponseEntity.status(HttpStatus.CREATED).body(responseList);
		} catch (Exception e) {
			String errorMessage = "An error occurred while saving or retrieving message details.";
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
		}
	}

	@GetMapping("/member3/message/{id}")
	public ResponseEntity<Object> getAllReceiptDetailsWithId(@PathVariable("id") Long id) {
		List<Map<String, Object>> receiptList = new ArrayList<>();
		List<Map<String, Object>> receiptRole = messageRepository.getAllReceiptDetailsWithClientId(id);
		Map<String, List<Map<String, Object>>> departmentGroupMap = receiptRole.stream()
				.filter(action -> action.get("memberListId") != null) // Add a null check here
				.collect(Collectors.groupingBy(action -> action.get("memberListId").toString()));

		for (Entry<String, List<Map<String, Object>>> receiptLoop : departmentGroupMap.entrySet()) {
			Map<String, Object> receiptMap = new HashMap<>();
			receiptMap.put("memberListId", Long.parseLong(receiptLoop.getKey()));
			receiptMap.put("senderName", receiptLoop.getValue().get(0).get("senderName"));
			receiptMap.put("phoneNumber", receiptLoop.getValue().get(0).get("phoneNumber"));
			receiptMap.put("roleType", receiptLoop.getValue().get(0).get("roleType"));
			receiptMap.put("receiverId", receiptLoop.getValue().get(0).get("receiverId"));
			receiptMap.put("senderId", receiptLoop.getValue().get(0).get("senderId"));
			receiptMap.put("receiverName", receiptLoop.getValue().get(0).get("receiverName"));
			List<Map<String, Object>> receiptSubList = new ArrayList<>();
			for (Map<String, Object> receiptSubLoop : receiptLoop.getValue()) {

				Map<String, Object> receiptSubMap = new HashMap<>();

				receiptSubMap.put("senderName", receiptSubLoop.get("senderName"));
				receiptSubMap.put("receiverName", receiptSubLoop.get("receiverName"));
				receiptSubMap.put("roleType", receiptSubLoop.get("roleType"));
				receiptSubMap.put("phoneNumber", receiptSubLoop.get("phoneNumber"));
				receiptSubMap.put("memberListId", receiptSubLoop.get("memberListId"));
				receiptSubMap.put("messageId", receiptSubLoop.get("messageId"));
				receiptSubMap.put("roleType", receiptSubLoop.get("roleType"));
				receiptSubMap.put("date", receiptSubLoop.get("date"));
				receiptSubMap.put("intime", receiptSubLoop.get("intime"));
				receiptSubMap.put("message", receiptSubLoop.get("message"));
				receiptSubMap.put("receiverId", receiptSubLoop.get("receiverId"));
				receiptSubMap.put("senderId", receiptSubLoop.get("senderId"));
				receiptSubList.add(receiptSubMap);
			}
			receiptMap.put("memberList", receiptSubList);
			receiptList.add(receiptMap);

		}

		return ResponseEntity.ok(receiptList);

	}

//	@GetMapping("/member/message/{id}")
//	public ResponseEntity<Object> getMessageDetailsWithId(@PathVariable("id") Long id) {
//
//		List<Map<String, Object>> memberList = new ArrayList<>();
//
//		List<Map<String, Object>> memberRole = messageRepository.getAllMessageDetailsWithId(id);
//
//		List<Map<String, Object>> chatRole = messageRepository.getAllMessageDetails();
//		
//		Map<String, List<Map<String, Object>>> memberGroupMap = memberRole.stream()
//				.collect(Collectors.groupingBy(action -> action.get("member_list_id").toString()));
//		
//		Map<String, List<Map<String, Object>>> chatGroupMap = chatRole.stream()
//				.collect(Collectors.groupingBy(action -> action.get("message_id").toString()));
//
//		for (Entry<String, List<Map<String, Object>>> memberLoop : memberGroupMap.entrySet()) {
//			Map<String, Object> memberMap = new HashMap<>();
//			memberMap.put("memberListId", Long.parseLong(memberLoop.getKey()));
//			memberMap.put("email", memberLoop.getValue().get(0).get("email"));
//			memberMap.put("phoneNumber", memberLoop.getValue().get(0).get("phone_number"));
//			memberMap.put("id", memberLoop.getValue().get(0).get("id"));
//			memberMap.put("roleId", memberLoop.getValue().get(0).get("role_id"));
//			memberMap.put("roleType", memberLoop.getValue().get(0).get("role_type"));
//			memberMap.put("userName", memberLoop.getValue().get(0).get("user_name"));
//
//			List<Map<String, Object>> chatList = new ArrayList<>();
//			for (Entry<String, List<Map<String, Object>>> chatLoop : chatGroupMap.entrySet()) {
//
//				Map<String, Object> chatMap = new HashMap<>();
//
//				chatMap.put("senderName", chatLoop.getValue().get(0).get("sender_name"));
//				chatMap.put("receiverName", chatLoop.getValue().get(0).get("receiver_name"));
//				chatMap.put("messageId", chatLoop.getValue().get(0).get("message_id"));
//				chatMap.put("date", chatLoop.getValue().get(0).get("date"));
//				chatMap.put("intime", chatLoop.getValue().get(0).get("intime"));
//				chatMap.put("message", chatLoop.getValue().get(0).get("message"));
//				chatMap.put("receiverId", chatLoop.getValue().get(0).get("receiver_id"));
//				chatMap.put("senderId", chatLoop.getValue().get(0).get("sender_id"));
//				chatList.add(chatMap);
//			}
//			memberMap.put("messageList", chatList);
//			memberList.add(memberMap);
//
//		}
//
//		return ResponseEntity.ok(memberList);
//
//	}

	@GetMapping("/member1/message/{id}")
	public ResponseEntity<Object> getAllReceiptDetailsWithIdferhy(@PathVariable("id") Long id) {
		List<Map<String, Object>> receiptList = new ArrayList<>();
		List<Map<String, Object>> receiptRole = messageRepository.getAllReceiptDetailsWithClientId1(id);
		Map<String, List<Map<String, Object>>> departmentGroupMap = receiptRole.stream()
				.filter(action -> action.get("member_list_id") != null) // Add a null check here
				.collect(Collectors.groupingBy(action -> action.get("member_list_id").toString()));

		for (Entry<String, List<Map<String, Object>>> receiptLoop : departmentGroupMap.entrySet()) {
			Map<String, Object> receiptMap = new HashMap<>();
			receiptMap.put("memberListId", Long.parseLong(receiptLoop.getKey()));
			receiptMap.put("senderName", receiptLoop.getValue().get(0).get("senderName"));
			receiptMap.put("phoneNumber", receiptLoop.getValue().get(0).get("phoneNumber"));
//			receiptMap.put("roleType", receiptLoop.getValue().get(0).get("roleType"));
			receiptMap.put("receiverId", receiptLoop.getValue().get(0).get("receiver_id"));
			receiptMap.put("senderId", receiptLoop.getValue().get(0).get("sender_id"));
			receiptMap.put("receiverName", receiptLoop.getValue().get(0).get("receiverName"));
			List<Map<String, Object>> receiptSubList = new ArrayList<>();
			for (Map<String, Object> receiptSubLoop : receiptLoop.getValue()) {

				Map<String, Object> receiptSubMap = new HashMap<>();

				receiptSubMap.put("senderName", receiptSubLoop.get("senderName"));
				receiptSubMap.put("receiverName", receiptSubLoop.get("receiverName"));
//				receiptSubMap.put("roleType", receiptSubLoop.get("roleType"));
//				receiptSubMap.put("phoneNumber", receiptSubLoop.get("phoneNumber"));
				receiptSubMap.put("memberListId", receiptSubLoop.get("member_list_id"));
				receiptSubMap.put("messageId", receiptSubLoop.get("message_id"));
//				receiptSubMap.put("roleType", receiptSubLoop.get("roleType"));
//				receiptSubMap.put("date", receiptSubLoop.get("date"));
//				receiptSubMap.put("intime", receiptSubLoop.get("intime"));
				receiptSubMap.put("message", receiptSubLoop.get("message"));
				receiptSubMap.put("receiverId", receiptSubLoop.get("receiver_id"));
				receiptSubMap.put("senderId", receiptSubLoop.get("sender_id"));
				receiptSubList.add(receiptSubMap);
			}
			receiptMap.put("memberList", receiptSubList);
			receiptList.add(receiptMap);

		}

		return ResponseEntity.ok(receiptList);

	}

	@GetMapping("/member/message/{id}")
	public ResponseEntity<?> getAllMessageDetails(@PathVariable("id") Long id) {

		List<Map<String, Object>> chatList = new ArrayList<>();
		List<Map<String, Object>> chatRole = messageRepository.getAllDetailsWithId(id);
		Map<String, List<Map<String, Object>>> chatGroupMap = chatRole.stream()
				.collect(Collectors.groupingBy(action -> String.valueOf(action.get("message_id"))));

		List<Map<String, Object>> memberList = new ArrayList<>();
		List<Map<String, Object>> memberRole = messageRepository.getAllDetails();
		Map<String, List<Map<String, Object>>> memberGroupMap = memberRole.stream()
				.collect(Collectors.groupingBy(action -> String.valueOf(action.get("member_list_id"))));

		BigInteger memberListIdBigInt = (BigInteger) memberRole.get(0).get("member_list_id");
		long memberChatListId = memberListIdBigInt.longValue();

		for (Entry<String, List<Map<String, Object>>> chatLoop : chatGroupMap.entrySet()) {
			Map<String, Object> chatMap = new HashMap<>();
			chatMap.put("messageId", chatLoop.getKey());
			chatMap.put("receiverId", chatLoop.getValue().get(0).get("receiver_id"));
			chatMap.put("receiverName", chatLoop.getValue().get(0).get("receiver_name"));
			chatMap.put("senderId", chatLoop.getValue().get(0).get("sender_id"));
			chatMap.put("senderName", chatLoop.getValue().get(0).get("sender_name"));
			chatMap.put("message", chatLoop.getValue().get(0).get("message"));
			chatMap.put("intime", chatLoop.getValue().get(0).get("intime"));
			chatMap.put("date", chatLoop.getValue().get(0).get("date"));
			chatList.add(chatMap);
		}

		for (Entry<String, List<Map<String, Object>>> memberLoop : memberGroupMap.entrySet()) {
			Map<String, Object> memberMap = new HashMap<>();
			String fileExtension = getFileExtensionForImage(memberLoop.getValue().get(0));
			int randomNumber = generateRandomNumber();
			String imageUrl = "member/" + randomNumber + "/" + memberLoop.getValue().get(0).get("member_list_id") + "."+ fileExtension;
			memberMap.put("profile", imageUrl);
			memberMap.put("memberListId", memberLoop.getKey());
			memberMap.put("email", memberLoop.getValue().get(0).get("email"));
			memberMap.put("phoneNumber", memberLoop.getValue().get(0).get("phone_number"));
			memberMap.put("roleId", memberLoop.getValue().get(0).get("role_id"));
			memberMap.put("roleType", memberLoop.getValue().get(0).get("role_type"));
			memberMap.put("userName", memberLoop.getValue().get(0).get("user_name"));
			String memberIdString = memberLoop.getKey();
			long memberListId = Long.parseLong(memberIdString);
			if (memberListId == id) {
				memberMap.put("chatList", chatList);
			} else {
				memberMap.put("chatList", Collections.emptyList());
			}
			memberList.add(memberMap);
		}

		return ResponseEntity.ok(memberList);

	}

	@GetMapping("/member2/message/{id}")
	public ResponseEntity<?> getAllMessageDetailseeee(@PathVariable("id") Long id) {
		List<Map<String, Object>> chatList = new ArrayList<>();
		List<Map<String, Object>> chatRole = messageRepository.getAllDetailsWithId(id);
		Map<String, List<Map<String, Object>>> chatGroupMap = chatRole.stream()
				.collect(Collectors.groupingBy(action -> String.valueOf(action.get("member_list_id"))));

		List<Map<String, Object>> memberList = new ArrayList<>();
		List<Map<String, Object>> memberRole = messageRepository.getAllDetails();
		Map<String, List<Map<String, Object>>> memberGroupMap = memberRole.stream()
				.collect(Collectors.groupingBy(action -> String.valueOf(action.get("member_list_id"))));

		for (Entry<String, List<Map<String, Object>>> memberLoop : memberGroupMap.entrySet()) {
			String memberIdString = memberLoop.getKey();
			long memberListId = Long.parseLong(memberIdString);
			Map<String, Object> memberMap = new HashMap<>();
			int randomNumber = generateRandomNumber();
			String imageUrl = "member/" + randomNumber + "/" + memberLoop.getValue().get(0).get("member_list_id") ;

			memberMap.put("profile", imageUrl);
			memberMap.put("memberListId", memberIdString);
			memberMap.put("email", memberLoop.getValue().get(0).get("email"));
			memberMap.put("phoneNumber", memberLoop.getValue().get(0).get("phone_number"));
			memberMap.put("roleId", memberLoop.getValue().get(0).get("role_id"));
			memberMap.put("roleType", memberLoop.getValue().get(0).get("role_type"));
			memberMap.put("userName", memberLoop.getValue().get(0).get("user_name"));
			if (memberListId == id) {
				List<Map<String, Object>> chatMessages = chatGroupMap.get(memberIdString);
				if (chatMessages != null) {
					for (Map<String, Object> chatMessage : chatMessages) {
						Map<String, Object> messageMap = new HashMap<>();
						messageMap.put("messageId", chatMessage.get("messageId"));
						messageMap.put("senderId", chatMessage.get("senderId"));
						messageMap.put("senderName", chatMessage.get("senderName"));
						messageMap.put("receiverId", chatMessage.get("receiverId"));
						messageMap.put("receiverName", chatMessage.get("receiverName"));
						messageMap.put("message", chatMessage.get("message"));
						messageMap.put("intime", chatMessage.get("intime"));
						chatList.add(messageMap);
					}
				}
				memberMap.put("chatList", chatList);
				memberList.add(memberMap);
				return ResponseEntity.ok(memberList);
			}
		}
		return ResponseEntity.notFound().build();
	}

}