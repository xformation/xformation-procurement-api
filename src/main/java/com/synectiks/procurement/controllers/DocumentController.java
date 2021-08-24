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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.synectiks.procurement.business.service.DocumentService;
import com.synectiks.procurement.domain.Document;
import com.synectiks.procurement.domain.Status;

@RestController
@RequestMapping("/api")
public class DocumentController {
	private static final Logger logger = LoggerFactory.getLogger(DocumentController.class);
	
	@Autowired
	private DocumentService documentService;
	
	@PostMapping("/addDocument")
	public  ResponseEntity<Status> addDocument(@RequestBody ObjectNode obj) throws JSONException {
		logger.info("Request to add a new document");
		Status st = new Status(); 
		try {
			Document document = documentService.addDocument(obj);
			st.setCode(HttpStatus.OK.value());
			st.setType("SUCCESS");
			st.setMessage("Document added successfully");
			st.setObject(document);
			return ResponseEntity.status(HttpStatus.OK).body(st);
		}catch (Exception e) {
			logger.error("Document add failed. Exception: ", e);
			st.setCode(HttpStatus.EXPECTATION_FAILED.value());
			st.setType("ERROR");
			st.setMessage("Document add failed");
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
		}
	}
	
	@PostMapping("/updateDocument")
	public ResponseEntity<Object> updateDocument(@RequestBody ObjectNode obj) throws JSONException, URISyntaxException {
		logger.info("Request to update a document");
		Status st = new Status();
		try {
			Document document = documentService.updateDocument(obj);
			st.setCode(HttpStatus.OK.value());
			st.setType("SUCCESS");
			st.setMessage("Document updated successfully");
			st.setObject(document);
			return ResponseEntity.status(HttpStatus.OK).body(st);
		}catch (Exception e) {
			logger.error("Updating document failed. Exception: ", e);
			st.setCode(HttpStatus.EXPECTATION_FAILED.value());
			st.setType("ERROR");
			st.setMessage("Updating document failed");
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
		}
	}
	
	@GetMapping("/searchDocument")
	public ResponseEntity<Status> searchdocument(@RequestParam Map<String, String> requestObj) {
		logger.info("Request to get document on given filter criteria");
		Status st = new Status();
		try {
			List<Document> list = documentService.searchdocument(requestObj);	
	        st.setCode(HttpStatus.OK.value());
			st.setType("SUCCESS");
			st.setMessage("Search document list successful");
			st.setObject(list);
			return ResponseEntity.status(HttpStatus.OK).body(st);
		}catch (Exception e) {
			logger.error("Search document list failed. Exception: ", e);
			st.setCode(HttpStatus.EXPECTATION_FAILED.value());
			st.setType("ERROR");
			st.setMessage("Search document list failed");
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
		}
		
	}
		
}
