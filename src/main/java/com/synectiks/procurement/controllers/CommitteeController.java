package com.synectiks.procurement.controllers;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
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
import com.synectiks.procurement.domain.CommitteeActivity;
import com.synectiks.procurement.domain.Status;
import com.synectiks.procurement.repository.CommitteeActivityRepository;
import com.synectiks.procurement.repository.CommitteeRepository;

import io.github.jhipster.web.util.HeaderUtil;

@RestController
@RequestMapping("/api")
public class CommitteeController {
	private static final Logger logger = LoggerFactory.getLogger(CommitteeController.class);
	private static final String ENTITY_NAME = "committee";
	
	@Value("${jhipster.clientApp.name}")
	private String applicationName;
	@Autowired
	private CommitteeRepository committeeRepository;
	
	@Autowired
	private CommitteeActivityRepository committeeActivityRepository;
	
	@PostMapping("/addCommittee")
	public  ResponseEntity<Object> addCommittee(@RequestBody ObjectNode obj) throws JSONException {
		Status st = new Status();
		try {
			Committee committee = new Committee();

		if (obj.get("name") != null) {
			committee.setName(obj.get("name").asText());
		}
		if (obj.get("type") != null) {
			committee.setType(obj.get("type").asText());
		}
		
		if (obj.get("notes") != null) {
			committee.setNotes(obj.get("notes").asText());
		}
		committee.setCreatedBy(Constants.SYSTEM_ACCOUNT);
		committee.setUpdatedBy(Constants.SYSTEM_ACCOUNT);
		Instant now = Instant.now();
		committee.setCreatedOn(now);
		committee.setUpdatedOn(now);
		committee = committeeRepository.save(committee);
		logger.info("Add New Committee SUCCESS");
		
		if(committee.getId() != null) {
			CommitteeActivity committeeActivity = new CommitteeActivity();
				BeanUtils.copyProperties(committee, committeeActivity);	
				committeeActivity.setCommittee(committee);
				committeeActivity = committeeActivityRepository.save(committeeActivity);
				logger.info("Add New invoice Activity");
		}
		
		st.setCode(HttpStatus.OK.value());
		st.setType("SUCCESS");
		st.setMessage("Add New Committee SUCCESS : "+committee);
		st.setObject(committee);
		return ResponseEntity.status(HttpStatus.OK).body(st);
		
		}catch (Exception e) {
			logger.error("Add New Committee failed. Exception: ", e);
			st.setCode(HttpStatus.EXPECTATION_FAILED.value());
			st.setType("ERROR");
			st.setMessage("Add New Committee failed");
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
		}
		
	}
	
	@PutMapping("/updateCommittee")
	public  ResponseEntity<Object> updateCommittee(@RequestBody ObjectNode obj) throws JSONException, URISyntaxException {
//		 String id =(obj.get("id")).asText();
		Status st = new Status();
		 Optional<Committee> ur = committeeRepository.findById(Long.parseLong(obj.get("id").asText()));
		 if(!ur.isPresent()) {
				return ResponseEntity.created(new URI("/api/updateCommittee/")).headers(HeaderUtil
						.createEntityCreationAlert(applicationName, false, ENTITY_NAME, ""))
						.body(null);
			 }
		try {
			Committee committee = new Committee();


			if (obj.get("name") != null) {
				committee.setName(obj.get("name").asText());
			}
			if (obj.get("type") != null) {
				committee.setType(obj.get("type").asText());
			}
			
			if (obj.get("notes") != null) {
				committee.setNotes(obj.get("notes").asText());
			}
		
		 if (obj.get("user") != null) {
			 committee.setUpdatedBy(obj.get("user").asText());
			 } else {
				 committee.setUpdatedBy(Constants.SYSTEM_ACCOUNT);
			 }
		 
			 Instant now = Instant.now();
			 committee.setUpdatedOn(now);
			 committee = committeeRepository.save(committee);
		logger.info("Updating requisition completed");
		st.setCode(HttpStatus.OK.value());
		st.setType("SUCCESS");
		st.setMessage("Updating Committee SUCCESS : "+committee);
		st.setObject(committee);
		return ResponseEntity.status(HttpStatus.OK).body(st);
		
		}catch (Exception e) {
			logger.error("Updating Committee failed. Exception: ", e);
			st.setCode(HttpStatus.EXPECTATION_FAILED.value());
			st.setType("ERROR");
			st.setMessage("Updating Committee failed");
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
		}
		
	}
	
	@GetMapping("/searchCommittee")
	public List<Committee> searchCommittee(@RequestParam Map<String, String> requestObj) {
		logger.info("Request to get requisitionLineItem on given filter criteria");
		Committee committee = new Committee();
		
		try {
		boolean isFilter = false;
		if (requestObj.get("id") != null) {
			committee.setId(Long.parseLong(requestObj.get("id")));
			isFilter = true;
		}
//		if (requestObj.get("departmentId") != null) {
//			requisition.setDepartment(requestObj.get("departmentId"));
//			isFilter = true;
//		}
		
		if (requestObj.get("name") != null) {
			committee.setName(requestObj.get("name"));
			isFilter = true;
		}
		if (requestObj.get("type") != null) {
			committee.setType(requestObj.get("type"));
			isFilter = true;
		}
		if (requestObj.get("notes") != null) {
			committee.setNotes(requestObj.get("notes"));
			isFilter = true;
		}
		List<Committee> list = null;
		if (isFilter) {
			list = this.committeeRepository.findAll(Example.of(committee), Sort.by(Direction.DESC, "id"));
		} else {
			list = this.committeeRepository.findAll(Sort.by(Direction.DESC, "id"));
		}
		
        logger.info("search data ");
		
		Status st = new Status();
		st.setCode(HttpStatus.OK.value());
		st.setType("SUCCESS");
		st.setMessage("search data get SUCCESS : "+list);
		st.setObject(list);
		return (List<Committee>) ResponseEntity.status(HttpStatus.OK).body(st);
		
		}catch (Exception e) {
			logger.error("Updating RequisitionLineItem failed. Exception: ", e);
			Status st = new Status();
			st.setCode(HttpStatus.EXPECTATION_FAILED.value());
			st.setType("ERROR");
			st.setMessage("search data get failed");
			return (List<Committee>) ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
		}	
	}
	 @DeleteMapping("/committee/{id}")
	    public ResponseEntity<Object> deletePost(@PathVariable Long id) {
		 try {
			 committeeRepository.deleteById(id);
			} catch (Throwable th) {
				logger.error(th.getMessage(), th);
				return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(th);
			}
			return ResponseEntity.status(HttpStatus.OK)
					.body("User removed Successfully");
		}	
}