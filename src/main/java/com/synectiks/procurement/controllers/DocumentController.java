package com.synectiks.procurement.controllers;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
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
import com.synectiks.procurement.domain.Contact;
import com.synectiks.procurement.domain.Document;
import com.synectiks.procurement.domain.RequisitionLineItem;
import com.synectiks.procurement.domain.Status;
import com.synectiks.procurement.repository.ContactRepository;
import com.synectiks.procurement.repository.DocumentRepository;
import com.synectiks.procurement.repository.QuotationRepository;
import com.synectiks.procurement.repository.RequisitionLineItemRepository;

import io.github.jhipster.web.util.HeaderUtil;

@RestController
@RequestMapping("/api")
public class DocumentController {
	private static final Logger logger = LoggerFactory.getLogger(DocumentController.class);
	private static final String ENTITY_NAME = "document";
	
	@Value("${jhipster.clientApp.name}")
	private String applicationName;
	@Autowired
	private RequisitionLineItemRepository requisitionLineItemRepository;
	@Autowired
	private ContactRepository contactRepository;
	@Autowired
	private DocumentRepository documentRepository;
	@Autowired
	private QuotationRepository quotationRepository;

	@PostMapping("/addDocument")
	public  ResponseEntity<Object> addDocument(@RequestBody ObjectNode obj) throws JSONException {
		Status st = new Status(); 
//		String contactId =(obj.get("contactId")).asText();
		 Optional<Contact> ur = contactRepository.findById(Long.parseLong(obj.get("contactId").asText()));
		 if (!ur.isPresent()) {
			// String requisitionLineItemId =(obj.get("requisitionLineItemId")).asText();
			 st.setCode(HttpStatus.EXPECTATION_FAILED.value());
				st.setType("ERROR");
				st.setMessage("Contact not found");
				return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
		
		 }
			 Optional<RequisitionLineItem> cd = requisitionLineItemRepository.findById(Long.parseLong(obj.get("requisitionLineItemId").asText()));
			 if (!cd.isPresent()) {
					st.setCode(HttpStatus.EXPECTATION_FAILED.value());
					st.setType("ERROR");
					st.setMessage("RequisitionLineItem not found");
					return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
				 
			 }
				 
					try {
						Document document = new Document();
						if (obj.get("contactId") != null) {
							document.setContact(ur.get());
						}
						if (obj.get("requisitionLineItemId") != null) {
							document.setRequisitionLineItem(cd.get());
						}
						if (obj.get("fileName") != null) {
							document.setFileName(obj.get("fileName").asText());
						}
						if (obj.get("fileType") != null) {
							document.setFileType(obj.get("fileType").asText());
						}
						if (obj.get("fileSize") != null) {
							document.setFileSize(obj.get("fileSize").asLong());
						}
						if (obj.get("localFilePath") != null) {
							document.setLocalFilePath(obj.get("localFilePath").asText());
						}
						if (obj.get("s3Bucket") != null) {
							document.sets3Bucket(obj.get("s3Bucket").asText());
						}
						if (obj.get("sourceOfOrigin") != null) {
							document.setSourceOfOrigin(obj.get("sourceOfOrigin").asText());
						}
						
						document.setCreatedBy(Constants.SYSTEM_ACCOUNT);
						document.setUpdatedBy(Constants.SYSTEM_ACCOUNT);
						Instant now = Instant.now();
						document.setCreatedOn(now);
						document.setUpdatedOn(now);
						
						document = documentRepository.save(document);
						
						logger.info("Add New document SUCCESS");
						
						st.setCode(HttpStatus.OK.value());
						st.setType("SUCCESS");
						st.setMessage("Add New document SUCCESS : "+document);
						st.setObject(document);
						return ResponseEntity.status(HttpStatus.OK).body(st);
						
						
						}catch (Exception e) {
							logger.error("Add New document failed. Exception: ", e);
							st.setCode(HttpStatus.EXPECTATION_FAILED.value());
							st.setType("ERROR");
							st.setMessage("Add New document failed");
							return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
						}
			 }
			 
	
	@PutMapping("/updateDocument")
	public  ResponseEntity<Object> updateDocument(@RequestBody ObjectNode obj) throws JSONException, URISyntaxException {
//		 String id =(obj.get("id")).asText();
		Status st = new Status();
		 Optional<Document> ur = documentRepository.findById(Long.parseLong(obj.get("id").asText()));
		 if(!ur.isPresent()) {
				return ResponseEntity.created(new URI("/api/updateDocument/")).headers(HeaderUtil
						.createEntityCreationAlert(applicationName, false, ENTITY_NAME, ""))
						.body(null);
			 }
		try {
			Document document = new Document();
		
			if (obj.get("fileName") != null) {
				document.setFileName(obj.get("fileName").asText());
			}
			if (obj.get("fileType") != null) {
				document.setFileType(obj.get("fileType").asText());
			}
			if (obj.get("fileSize") != null) {
				document.setFileSize(obj.get("fileSize").asLong());
			}
			if (obj.get("localFilePath") != null) {
				document.setLocalFilePath(obj.get("localFilePath").asText());
			}
			if (obj.get("s3Bucket") != null) {
				document.sets3Bucket(obj.get("s3Bucket").asText());
			}
			if (obj.get("sourceOfOrigin") != null) {
				document.setSourceOfOrigin(obj.get("sourceOfOrigin").asText());
			}
		
		 if (obj.get("user") != null) {
			 document.setUpdatedBy(obj.get("user").asText());
			 } else {
				 document.setUpdatedBy(Constants.SYSTEM_ACCOUNT);
			 }
		 
			 Instant now = Instant.now();
			 document.setUpdatedOn(now);
		document = documentRepository.save(document);
		logger.info("Updating document completed");
		
		st.setCode(HttpStatus.OK.value());
		st.setType("SUCCESS");
		st.setMessage("Updating document SUCCESS : "+document);
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
	public List<Document> searchdocument(@RequestParam Map<String, String> requestObj) {
		logger.info("Request to get document on given filter criteria");
		Document document = new Document();
		
		try {
		boolean isFilter = false;
		if (requestObj.get("id") != null) {
			document.setId(Long.parseLong(requestObj.get("id")));
			isFilter = true;
		}

		if (requestObj.get("fileName") != null) {
			document.setFileName(requestObj.get("fileName"));
			isFilter = true;
		}
		if (requestObj.get("fileType") != null) {
			document.setFileType(requestObj.get("fileType"));
			isFilter = true;
		}
		if (requestObj.get("fileSize") != null) {
			document.setFileSize(Long.valueOf(requestObj.get("fileSize")));
			isFilter = true;
		}
		if (requestObj.get("localFilePath") != null) {
			document.setLocalFilePath(requestObj.get("localFilePath"));
			isFilter = true;
		}
		if (requestObj.get("s3Bucket") != null) {
			document.sets3Bucket(requestObj.get("s3Bucket"));
			isFilter = true;
		}
		if (requestObj.get("sourceOfOrigin") != null) {
			document.setSourceOfOrigin(requestObj.get("sourceOfOrigin"));
			isFilter = true;
		}
		
		List<Document> list = null;
		if (isFilter) {
			list = this.documentRepository.findAll(Example.of(document), Sort.by(Direction.DESC, "id"));
		} else {
			list = this.documentRepository.findAll(Sort.by(Direction.DESC, "id"));
		}
		
        logger.info("search data ");
		
		Status st = new Status();
		st.setCode(HttpStatus.OK.value());
		st.setType("SUCCESS");
		st.setMessage("search data get SUCCESS : "+list);
		st.setObject(list);
		return (List<Document>) ResponseEntity.status(HttpStatus.OK).body(st);
		
		}catch (Exception e) {
			logger.error("Updating document failed. Exception: ", e);
			Status st = new Status();
			st.setCode(HttpStatus.EXPECTATION_FAILED.value());
			st.setType("ERROR");
			st.setMessage("search data get failed");
			return (List<Document>) ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
		}
		
	}
	 @DeleteMapping("/document/{id}")
	    public ResponseEntity<Object> deletePost(@PathVariable Long id) {
		 try {
			 documentRepository.deleteById(id);
			} catch (Throwable th) {
				logger.error(th.getMessage(), th);
				return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(th);
			}
			return ResponseEntity.status(HttpStatus.OK)
					.body("User removed Successfully");
		}	
}
