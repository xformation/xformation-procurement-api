package com.synectiks.procurement.controllers;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.synectiks.procurement.config.Constants;
import com.synectiks.procurement.domain.Committee;
import com.synectiks.procurement.domain.Contact;
import com.synectiks.procurement.domain.Requisition;
import com.synectiks.procurement.domain.RequisitionLineItem;
import com.synectiks.procurement.domain.Status;
import com.synectiks.procurement.domain.Vendor;
import com.synectiks.procurement.repository.CommitteeRepository;
import com.synectiks.procurement.repository.ContactRepository;
import com.synectiks.procurement.repository.RequisitionLineItemRepository;
import com.synectiks.procurement.repository.RequisitionRepository;

import io.github.jhipster.web.util.HeaderUtil;

@RestController
@RequestMapping("/api")
public class ContactController {
	private static final Logger logger = LoggerFactory.getLogger(ContactController.class);
	private static final String ENTITY_NAME = "contact";
	
	@Value("${jhipster.clientApp.name}")
	private String applicationName;
	@Autowired
	private ContactRepository contactRepository;
	
	@PostMapping("/addContact")
	public  ResponseEntity<Object> addCommittee(@RequestBody ObjectNode obj) throws JSONException {
//		 String id =(obj.get("ownerId")).asText();
//		Optional<Vendor> ur = vendorRepository.findById(Long.parseLong(obj.get("id").asText()));
		Status st = new Status();
		try {
			Contact contact = new Contact();

			if (obj.get("firstName") != null) {
				contact.setFirstName(obj.get("firstName").asText());
			}
			if (obj.get("middleName") != null) {
				contact.setMiddleName(obj.get("middleName").asText());
			}
			if (obj.get("lastName") != null) {
				contact.setLastName(obj.get("lastName").asText());
			}
			if (obj.get("phoneNumber") != null) {
				contact.setPhoneNumber(obj.get("phoneNumber").asText());
			}
			if (obj.get("email") != null) {
				contact.setEmail(obj.get("email").asText());
			}
			
			if (obj.get("isActive") != null) {
				contact.setIsActive(obj.get("isActive").asText());
			}
			if (obj.get("inviteStatus") != null) {
				contact.setInviteStatus(obj.get("inviteStatus").asText());
			}
			if (obj.get("invitationLink") != null) {
				contact.setInvitationLink(obj.get("invitationLink").asText());
			}
//			if (obj.get("inviteSentOn") != null) {
//				contact.setInviteSentOn(obj.get("inviteSentOn"));
//			}
			if (obj.get("jobType") != null) {
				contact.setJobType(obj.get("jobType").asText());
			}
			if (obj.get("notes") != null) {
				contact.setNotes(obj.get("notes").asText());
			}
		contact.setCreatedBy(Constants.SYSTEM_ACCOUNT);
		contact.setUpdatedBy(Constants.SYSTEM_ACCOUNT);
		Instant now = Instant.now();
		contact.setCreatedOn(now);
		contact.setUpdatedOn(now);
		contact = contactRepository.save(contact);
		logger.info("Add New contact SUCCESS");
		st.setCode(HttpStatus.OK.value());
		st.setType("SUCCESS");
		st.setMessage("Add New contact SUCCESS : "+contact);
		st.setObject(contact);
		return ResponseEntity.status(HttpStatus.OK).body(st);
		
		}catch (Exception e) {
			logger.error("Add New contact failed. Exception: ", e);
			st.setCode(HttpStatus.EXPECTATION_FAILED.value());
			st.setType("ERROR");
			st.setMessage("Add New contact failed");
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
		}
		
	}
	
	@PutMapping("/updateContact")
	public  ResponseEntity<Object> updateContact(@RequestBody ObjectNode obj) throws JSONException, URISyntaxException {
//		 String id =(obj.get("id")).asText();
		Status st = new Status();
		Optional<Contact> ur = contactRepository.findById(Long.parseLong(obj.get("id").asText()));
		 if(!ur.isPresent()) {
				return ResponseEntity.created(new URI("/api/updateContact/")).headers(HeaderUtil
						.createEntityCreationAlert(applicationName, false, ENTITY_NAME, ""))
						.body(null);
			 }
		try {
			Contact contact = new Contact();
			if (obj.get("firstName") != null) {
				contact.setFirstName(obj.get("firstName").asText());
			}
			if (obj.get("middleName") != null) {
				contact.setMiddleName(obj.get("middleName").asText());
			}
			if (obj.get("lastName") != null) {
				contact.setLastName(obj.get("lastName").asText());
			}
			if (obj.get("phoneNumber") != null) {
				contact.setPhoneNumber(obj.get("phoneNumber").asText());
			}
			if (obj.get("email") != null) {
				contact.setEmail(obj.get("email").asText());
			}
			
			if (obj.get("isActive") != null) {
				contact.setIsActive(obj.get("isActive").asText());
			}
			if (obj.get("inviteStatus") != null) {
				contact.setInviteStatus(obj.get("inviteStatus").asText());
			}
			if (obj.get("invitationLink") != null) {
				contact.setInvitationLink(obj.get("invitationLink").asText());
			}
//			if (obj.get("inviteSentOn") != null) {
//				contact.setInviteSentOn(obj.get("inviteSentOn"));
//			}
			if (obj.get("jobType") != null) {
				contact.setJobType(obj.get("jobType").asText());
			}
			if (obj.get("notes") != null) {
				contact.setNotes(obj.get("notes").asText());
			}
		 
			 Instant now = Instant.now();
			 contact.setUpdatedOn(now);
			 contact = contactRepository.save(contact);
		logger.info("Updating requisition completed");
		
		st.setCode(HttpStatus.OK.value());
		st.setType("SUCCESS");
		st.setMessage("Updating contact SUCCESS : "+contact);
		st.setObject(contact);
		return ResponseEntity.status(HttpStatus.OK).body(st);
		
		}catch (Exception e) {
			logger.error("Updating contact failed. Exception: ", e);
			st.setCode(HttpStatus.EXPECTATION_FAILED.value());
			st.setType("ERROR");
			st.setMessage("Updating contact failed");
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
		}
		
	}
	
	@GetMapping("/searchContact")
	public List<Contact> searchContact(@RequestParam Map<String, String> requestObj) {
		logger.info("Request to get requisitionLineItem on given filter criteria");
		Contact contact = new Contact();
		
		try {
		boolean isFilter = false;
		if (requestObj.get("id") != null) {
			contact.setId(Long.parseLong(requestObj.get("id")));
			isFilter = true;
		}
//		if (requestObj.get("departmentId") != null) {
//			requisition.setDepartment(requestObj.get("departmentId"));
//			isFilter = true;
//		}
		if (requestObj.get("firstName") != null) {
			contact.setFirstName(requestObj.get("firstName"));
			isFilter = true;
		}
		if (requestObj.get("lastName") != null) {
			contact.setLastName(requestObj.get("lastName"));
			isFilter = true;
		}
		if (requestObj.get("phoneNumber") != null) {
			contact.setPhoneNumber(requestObj.get("phoneNumber"));
			isFilter = true;
		}
		if (requestObj.get("email") != null) {
			contact.setEmail(requestObj.get("email"));
			isFilter = true;
		}
		if (requestObj.get("isActive") != null) {
			contact.setIsActive(requestObj.get("isActive"));
			isFilter = true;
		}
		if (requestObj.get("inviteStatus") != null) {
			contact.setInviteStatus(requestObj.get("inviteStatus"));
			isFilter = true;
		}
		if (requestObj.get("invitationLink") != null) {
			contact.setInvitationLink(requestObj.get("invitationLink"));
			isFilter = true;
		}
//		if (requestObj.get("inviteSentOn") != null) {
//			contact.setInviteSentOn(requestObj.get("inviteSentOn"));
//			isFilter = true;
//		}
		if (requestObj.get("jobType") != null) {
			contact.setJobType(requestObj.get("jobType"));
			isFilter = true;
		}
		if (requestObj.get("notes") != null) {
			contact.setNotes(requestObj.get("notes"));
			isFilter = true;
		}
		List<Contact> list = null;
		if (isFilter) {
			list = this.contactRepository.findAll(Example.of(contact), Sort.by(Direction.DESC, "id"));
		} else {
			list = this.contactRepository.findAll(Sort.by(Direction.DESC, "id"));
		}
		
        logger.info("search data ");
		
		Status st = new Status();
		st.setCode(HttpStatus.OK.value());
		st.setType("SUCCESS");
		st.setMessage("search data get SUCCESS : "+list);
		st.setObject(list);
		return (List<Contact>) ResponseEntity.status(HttpStatus.OK).body(st);
		
		}catch (Exception e) {
			logger.error("Updating RequisitionLineItem failed. Exception: ", e);
			Status st = new Status();
			st.setCode(HttpStatus.EXPECTATION_FAILED.value());
			st.setType("ERROR");
			st.setMessage("search data get failed");
			return (List<Contact>) ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
		}
		
	}
	 @DeleteMapping("/contact/{id}")
	    public ResponseEntity<Object> deletePost(@PathVariable Long id) {
		 try {
			 contactRepository.deleteById(id);
			} catch (Throwable th) {
				logger.error(th.getMessage(), th);
				return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(th);
			}
			return ResponseEntity.status(HttpStatus.OK)
					.body("User removed Successfully");
		}	
}
	 
