package com.synectiks.procurement.controllers;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.synectiks.procurement.business.service.ContactService;
import com.synectiks.procurement.domain.Committee;
import com.synectiks.procurement.domain.Contact;
import com.synectiks.procurement.domain.Status;

@RestController

@RequestMapping("/api")
public class ContactController {
	private static final Logger logger = LoggerFactory.getLogger(ContactController.class);
	
	@Autowired
	private ContactService contactService;
	
	@PostMapping("/addContact")
	public ResponseEntity<Status> addContact(@RequestBody ObjectNode obj) throws JSONException {
		logger.info("Request to add New contact");
		Status st = new Status();
		try {
			Contact contact = contactService.addContact(obj);
			if(contact == null) {
				logger.error("Add contact failed");
				st.setCode(HttpStatus.EXPECTATION_FAILED.value());
				st.setType("ERROR");
				st.setMessage("Add contact failed");
				return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
			}
		    st.setCode(HttpStatus.OK.value());
		    st.setType("SUCCESS");
		    st.setMessage("Contact added successfully");
		    st.setObject(contact);
		    return ResponseEntity.status(HttpStatus.OK).body(st);
		}catch (Exception e) {
			logger.error("Adding contact failed. Exception: ", e);
			st.setCode(HttpStatus.EXPECTATION_FAILED.value());
			st.setType("ERROR");
			st.setMessage("Adding contact failed");
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
		}
	}
	
	@PostMapping("/updateContact")
	public  ResponseEntity<Status> updateContact(@RequestBody ObjectNode obj) throws JSONException, URISyntaxException {
		logger.info("Request to Updating Contact");
		Status st = new Status();
		try {
			Contact contact = contactService.updateContact(obj);
			if(contact == null) {
				logger.error("Update contact failed");
				st.setCode(HttpStatus.EXPECTATION_FAILED.value());
				st.setType("ERROR");
				st.setMessage("Update contact failed");
				return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
			}
			st.setCode(HttpStatus.OK.value());
			st.setType("SUCCESS");
			st.setMessage("Contact updated successfully");
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
	public ResponseEntity<Status> searchContact(@RequestParam Map<String, String> requestObj) {
		logger.info("Request to get requisitionLineItem on given filter criteria");
		Status st = new Status();
		try {
			List<Contact> list = contactService.searchContact(requestObj);
			if(list == null) {
				logger.error("Search contact failed");
				st.setCode(HttpStatus.EXPECTATION_FAILED.value());
				st.setType("ERROR");
				st.setMessage("Search contact failed");
				return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
			}
			st.setCode(HttpStatus.OK.value());
			st.setType("SUCCESS");
			st.setMessage("Search contacts successful");
			st.setObject(list);
			return ResponseEntity.status(HttpStatus.OK).body(st);
		}catch (Exception e) {
			logger.error("Searching contact failed. Exception: ", e);
			st.setCode(HttpStatus.EXPECTATION_FAILED.value());
			st.setType("ERROR");
			st.setMessage("search contact failed");
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
		}
	}
	
	@DeleteMapping("/contact/{id}")
	public ResponseEntity<Status> deleteContact(@PathVariable Long id) {
		Status st = new Status();
		try {
		 	contactService.deleteContact(id);
			st.setCode(HttpStatus.OK.value());
			st.setType("SUCCESS");
			st.setMessage("Delete contact successful");
			return ResponseEntity.status(HttpStatus.OK).body(st); 
		} catch (Throwable e) {
			st.setCode(HttpStatus.EXPECTATION_FAILED.value());
			st.setType("ERROR");
			st.setMessage("Delete contact failed");
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st); 
		}
	}
	@GetMapping("/getContact/{id}")
	public ResponseEntity<Status> getContact(@PathVariable Long id) {
		logger.info("Getting contact by id: " + id);
		Status st = new Status();
		try {
			Contact contact = contactService.getContact(id);
			if (contact == null) {
				logger.warn("Contact not found.");
				st.setCode(HttpStatus.EXPECTATION_FAILED.value());
				st.setType("ERROR");
				st.setMessage("Contact not found");
				return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
			}
			st.setCode(HttpStatus.OK.value());
			st.setType("SUCCESS");
			st.setMessage("Contact found");
			st.setObject(contact);
			return ResponseEntity.status(HttpStatus.OK).body(st);
		} catch (Exception e) {
			logger.error("Contact not found. Exception: ", e);
			st.setCode(HttpStatus.EXPECTATION_FAILED.value());
			st.setType("ERROR");
			st.setMessage("Contact not found");
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
		}
	}
	
}
	 
