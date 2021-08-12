package com.synectiks.procurement.controllers;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.synectiks.procurement.domain.Department;
import com.synectiks.procurement.domain.Status;
import com.synectiks.procurement.repository.DepartmentRepository;

import io.github.jhipster.web.util.HeaderUtil;

@RestController
@RequestMapping("/api")
public class DepartmentController {
	private static final Logger logger = LoggerFactory.getLogger(DepartmentController.class);
	private static final String ENTITY_NAME = "department";
	
	@Value("${jhipster.clientApp.name}")
	private String applicationName;
	@Autowired
	private DepartmentRepository departmentRepository;
	
	@PostMapping("/addDepartment")
	public  ResponseEntity<Object> addDepartment(@RequestBody ObjectNode obj) throws JSONException {
		
		try {
			Department department = new Department();
		if (obj.get("name") != null) {
		department.setName(obj.get("name").asText());
		}
		department = departmentRepository.save(department);
		logger.info("Add New Requisition SUCCESS");
		
		Status st = new Status();
		st.setCode(HttpStatus.OK.value());
		st.setType("SUCCESS");
		st.setMessage("Add New department SUCCESS : "+department);
		st.setObject(department);
		return ResponseEntity.status(HttpStatus.OK).body(st);
		
		}catch (Exception e) {
			logger.error("Add New department failed. Exception: ", e);
			Status st = new Status();
			st.setCode(HttpStatus.EXPECTATION_FAILED.value());
			st.setType("ERROR");
			st.setMessage("Add New department failed");
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
		}
		
	}
	
	@PutMapping("/updateDepartment")
	public  ResponseEntity<Object> updateDepartment(@RequestBody ObjectNode obj) throws JSONException, URISyntaxException {
//		 String id =(obj.get("id")).asText();
		Status st = new Status();
		Optional<Department> ur = departmentRepository.findById(Long.parseLong(obj.get("id").asText()));
		 if(!ur.isPresent()) {
				return ResponseEntity.created(new URI("/api/updateDepartment/")).headers(HeaderUtil
						.createEntityCreationAlert(applicationName, false, ENTITY_NAME, ""))
						.body(null);
			 }
		try {
			Department department = new Department();
		
			department.setName(obj.get("name").asText());
		
			department = departmentRepository.save(department);
		logger.info("Updating department completed");
	
		st.setCode(HttpStatus.OK.value());
		st.setType("SUCCESS");
		st.setMessage("Updating department SUCCESS : "+department);
		st.setObject(department);
		return ResponseEntity.status(HttpStatus.OK).body(st);
		
		}catch (Exception e) {
			logger.error("Updating department failed. Exception: ", e);
			st.setCode(HttpStatus.EXPECTATION_FAILED.value());
			st.setType("ERROR");
			st.setMessage("Updating department failed");
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
		}
		
	}
	

}
