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
import com.synectiks.procurement.business.service.DepartmentService;
import com.synectiks.procurement.domain.Department;
import com.synectiks.procurement.domain.Status;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/api")
//@Api(value = "/api", tags = "Department Management")
public class DepartmentController {
	private static final Logger logger = LoggerFactory.getLogger(DepartmentController.class);

	@Autowired
	private DepartmentService departmentService;

	@PostMapping("/addDepartment")
	public ResponseEntity<Status> addDepartment(@RequestBody ObjectNode obj) throws JSONException {
		logger.info("Request to add Department");
		Status st = new Status();
		try {
			Department department = departmentService.addDepartment(obj);
			if (department == null) {
				logger.error("Add department failed");
				st.setCode(HttpStatus.EXPECTATION_FAILED.value());
				st.setType("ERROR");
				st.setMessage("Add department failed");
				return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
			}
			st.setCode(HttpStatus.OK.value());
			st.setType("SUCCESS");
			st.setMessage("Department add successful");
			st.setObject(department);
			return ResponseEntity.status(HttpStatus.OK).body(st);
		} catch (Exception e) {
			logger.error("Department add failed. Exception: ", e);
			st.setCode(HttpStatus.EXPECTATION_FAILED.value());
			st.setType("ERROR");
			st.setMessage("Department add failed");
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
		}
	}

	@PostMapping("/updateDepartment")
	public ResponseEntity<Status> updateDepartment(@RequestBody ObjectNode obj)
			throws JSONException, URISyntaxException {
		logger.info("Request to update Department");
		Status st = new Status();
		try {
			Department department = departmentService.updateDepartment(obj);
			if (department == null) {
				logger.error("Department could not be updated.");
				st.setCode(HttpStatus.EXPECTATION_FAILED.value());
				st.setType("ERROR");
				st.setMessage("Update department failed");
				return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
			}
			st.setCode(HttpStatus.OK.value());
			st.setType("SUCCESS");
			st.setMessage("Updating department successful");
			st.setObject(department);
			return ResponseEntity.status(HttpStatus.OK).body(st);
		} catch (Exception e) {
			logger.error("Updating department failed. Exception: ", e);
			st.setCode(HttpStatus.EXPECTATION_FAILED.value());
			st.setType("ERROR");
			st.setMessage("Updating department failed");
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
		}

	}

	@GetMapping("/searchDepartment")
	public ResponseEntity<Status> searchDepartment(@RequestParam Map<String, String> requestObj) {
		logger.info("Request to get department on given filter criteria");
		Status st = new Status();
		try {
			List<Department> list = departmentService.searchDepartment(requestObj);
			if (list == null) {
				logger.error("Search department failed");
				st.setCode(HttpStatus.EXPECTATION_FAILED.value());
				st.setType("ERROR");
				st.setMessage("Search department failed");
				return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
			}
			st.setCode(HttpStatus.OK.value());
			st.setType("SUCCESS");
			st.setMessage("Search department successful");
			st.setObject(list);
			return ResponseEntity.status(HttpStatus.OK).body(st);
		} catch (Exception e) {
			logger.error("Search department failed. Exception: ", e);
			st.setCode(HttpStatus.EXPECTATION_FAILED.value());
			st.setType("ERROR");
			st.setMessage("Search department failed");
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
		}
	}

	@DeleteMapping("/deleteDepartment/{id}")
	public ResponseEntity<Status> deleteDepartment(@PathVariable Long id) {
		Status st = new Status();
		try {
			departmentService.deleteDepartment(id);
			st.setCode(HttpStatus.OK.value());
			st.setType("SUCCESS");
			st.setMessage("Delete department successful");
			return ResponseEntity.status(HttpStatus.OK).body(st);
		} catch (Throwable e) {
			st.setCode(HttpStatus.EXPECTATION_FAILED.value());
			st.setType("ERROR");
			st.setMessage("Delete department failed");
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
		}
	}

//	@GetMapping("/getAllDepartment")
//	private ResponseEntity<Status> getAllDepartment() {
//		logger.info("Request to get all Department");
//		Status st = new Status();
//		try {
//			List<Department> list = departmentService.getAllDepartment();
//			if (list == null) {
//				logger.error("Search all department failed");
//				st.setCode(HttpStatus.EXPECTATION_FAILED.value());
//				st.setType("ERROR");
//				st.setMessage("Search all department failed");
//				return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
//			}
//			st.setCode(HttpStatus.OK.value());
//			st.setType("SUCCESS");
//			st.setMessage("Search all department successful");
//			st.setObject(list);
//			return ResponseEntity.status(HttpStatus.OK).body(st);
//		} catch (Exception e) {
//			logger.error("Search all department failed. Exception: ", e);
//			st.setCode(HttpStatus.EXPECTATION_FAILED.value());
//			st.setType("ERROR");
//			st.setMessage("Search all department failed");
//			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
//		}
//	}

	@GetMapping("/getDepartment/{id}")
	public ResponseEntity<Status> getDepartment(@PathVariable Long id) {
		logger.info("Getting department by id: " + id);
		Status st = new Status();
		try {
			Department department = departmentService.getDepartment(id);
			if (department == null) {
				logger.warn("Department not found.");
				st.setCode(HttpStatus.EXPECTATION_FAILED.value());
				st.setType("ERROR");
				st.setMessage("Department not found");
				return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
			}
			st.setCode(HttpStatus.OK.value());
			st.setType("SUCCESS");
			st.setMessage("Department found");
			st.setObject(department);
			return ResponseEntity.status(HttpStatus.OK).body(st);
		} catch (Exception e) {
			logger.error("Department not found. Exception: ", e);
			st.setCode(HttpStatus.EXPECTATION_FAILED.value());
			st.setType("ERROR");
			st.setMessage("Department not found");
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
		}
	}

}
