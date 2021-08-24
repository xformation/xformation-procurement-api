package com.synectiks.procurement.controllers;

import java.net.URISyntaxException;

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
import com.synectiks.procurement.business.service.DepartmentService;
import com.synectiks.procurement.domain.Department;
import com.synectiks.procurement.domain.Status;

@RestController
@RequestMapping("/api")
public class DepartmentController {
	private static final Logger logger = LoggerFactory.getLogger(DepartmentController.class);
	
	@Autowired
	private DepartmentService departmentService;
	
	@PostMapping("/addDepartment")
	public  ResponseEntity<Status> addDepartment(@RequestBody ObjectNode obj) throws JSONException {
		logger.info("Request to add Department");
		Status st = new Status();
		try {
		    Department department = departmentService.addDepartment(obj);
			st.setCode(HttpStatus.OK.value());
			st.setType("SUCCESS");
			st.setMessage("Department add successful");
			st.setObject(department);
			return ResponseEntity.status(HttpStatus.OK).body(st);
		}catch (Exception e) {
			logger.error("Department add failed. Exception: ", e);
			st.setCode(HttpStatus.EXPECTATION_FAILED.value());
			st.setType("ERROR");
			st.setMessage("Department add failed");
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
		}
	}
	
	@PostMapping("/updateDepartment")
	public ResponseEntity<Status> updateDepartment(@RequestBody ObjectNode obj) throws JSONException, URISyntaxException {
		logger.info("Request to update Department");
		Status st = new Status();
		try {
			Department department = departmentService.updateDepartment(obj);
			st.setCode(HttpStatus.OK.value());
			st.setType("SUCCESS");
			st.setMessage("Updating department successful");
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
