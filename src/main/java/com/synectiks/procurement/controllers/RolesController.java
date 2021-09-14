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
import com.synectiks.procurement.business.service.RolesService;
import com.synectiks.procurement.domain.Roles;
import com.synectiks.procurement.domain.Status;
import com.synectiks.procurement.repository.RolesGroupRepository;
import com.synectiks.procurement.repository.RolesRepository;
import com.synectiks.procurement.web.rest.errors.UniqueConstraintException;

@RestController
@RequestMapping("/api")
public class RolesController {
	private static final Logger logger = LoggerFactory.getLogger(RolesController.class);

	@Autowired
	RolesService rolesService;

	@Autowired
	RolesGroupRepository rolesGroupRepository;

	@Autowired
	RolesRepository rolesRepository;

	@PostMapping("/addRoles")
	public ResponseEntity<Status> addRoles(@RequestBody ObjectNode obj) throws JSONException {
		logger.info("Request to add a role");
		Status st = new Status();
		try {
			Roles roles = rolesService.addRoles(obj);
			if(roles==null) {
				st.setCode(HttpStatus.OK.value());
				st.setType("FIALED");
				st.setMessage("Add role failed");
				st.setObject(roles);
				return ResponseEntity.status(HttpStatus.CONFLICT).body(st);
			}
			st.setCode(HttpStatus.OK.value());
			st.setType("SUCCESS");
			st.setMessage("Role added successfully");
			st.setObject(roles);
			return ResponseEntity.status(HttpStatus.OK).body(st);
		}catch (UniqueConstraintException e) {
			logger.error("Add role failed. Exception: ", e.getMessage());
			st.setCode(HttpStatus.EXPECTATION_FAILED.value());
			st.setType("ERROR");
			st.setMessage(e.getMessage());
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
		} 
		catch (Exception e) {
			logger.error("Add role failed. Exception: ", e);
			st.setCode(HttpStatus.EXPECTATION_FAILED.value());
			st.setType("ERROR");
			st.setMessage("Add role failed");
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
		}
	}

	@PostMapping("/updateRoles")
	public ResponseEntity<Status> updateRoles(@RequestBody ObjectNode obj) throws JSONException, URISyntaxException {
		logger.info("Request to update a role");
		Status st = new Status();
		try {
			Roles roles = rolesService.updateRoles(obj);
			if(roles==null) {
				st.setCode(HttpStatus.OK.value());
				st.setType("FIALED");
				st.setMessage("Updated role failed");
				st.setObject(roles);
				return ResponseEntity.status(HttpStatus.CONFLICT).body(st);
			}
			st.setCode(HttpStatus.OK.value());
			st.setType("SUCCESS");
			st.setMessage("Role updated successfully");
			st.setObject(roles);
			return ResponseEntity.status(HttpStatus.OK).body(st);
		}catch (UniqueConstraintException e) {
			logger.error("Update role failed. Exception: ", e.getMessage());
			st.setCode(HttpStatus.EXPECTATION_FAILED.value());
			st.setType("ERROR");
			st.setMessage(e.getMessage());
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
		} 
		catch (Exception e) {
			logger.error("Updating role failed. Exception: ", e);
			st.setCode(HttpStatus.EXPECTATION_FAILED.value());
			st.setType("ERROR");
			st.setMessage("Updating role failed");
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
		}
	}

	@GetMapping("/searchRoles")
	public ResponseEntity<Status> searchRoles(@RequestParam Map<String, String> requestObj) {
		logger.info("Request to search role on given filter criteria");
		Status st = new Status();
		try {
			List<Roles> list = rolesService.searchRoles(requestObj);
			st.setCode(HttpStatus.OK.value());
			st.setType("SUCCESS");
			st.setMessage("Search role successful");
			st.setObject(list);
			return ResponseEntity.status(HttpStatus.OK).body(st);
		} catch (Exception e) {
			logger.error("Search role failed. Exception: ", e);
			st.setCode(HttpStatus.EXPECTATION_FAILED.value());
			st.setType("ERROR");
			st.setMessage("Search role failed");
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
		}
	}

	@DeleteMapping("/deleteRoles/{id}")
	public ResponseEntity<Status> deleteRoles(@PathVariable Long id) {
		logger.info("Request to delete a role");
		Status st = new Status();
		try {
			rolesService.deleteRoles(id);
			st.setCode(HttpStatus.OK.value());
			st.setType("SUCCESS");
			st.setMessage("Role deleted successfully");
			return ResponseEntity.status(HttpStatus.OK).body(st);
		} catch (Throwable e) {
			st.setCode(HttpStatus.EXPECTATION_FAILED.value());
			st.setType("ERROR");
			st.setMessage("Delete vendor failed");
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
		}
	}

	@GetMapping("/getRoles/{id}")
	public ResponseEntity<Status> getRoles(@PathVariable Long id) {
		logger.info("Getting roles by id: " + id);
		Status st = new Status();
		try {
			Roles roles = rolesService.getRoles(id);
			if (roles == null) {
				logger.warn("Roles not found.");
				st.setCode(HttpStatus.EXPECTATION_FAILED.value());
				st.setType("ERROR");
				st.setMessage("Role not found");
				return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
			}
			st.setCode(HttpStatus.OK.value());
			st.setType("SUCCESS");
			st.setMessage("Role found");
			st.setObject(roles);
			return ResponseEntity.status(HttpStatus.OK).body(st);
		} catch (Exception e) {
			logger.error("Role not found. Exception: ", e);
			st.setCode(HttpStatus.EXPECTATION_FAILED.value());
			st.setType("ERROR");
			st.setMessage("Role not found");
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
		}
	}
}
