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
import com.synectiks.procurement.business.service.RulesService;
import com.synectiks.procurement.domain.Rules;
import com.synectiks.procurement.domain.Status;
import com.synectiks.procurement.web.rest.errors.UniqueConstraintException;

@RestController
@RequestMapping("/api")
public class RulesController {
	private static final Logger logger = LoggerFactory.getLogger(RulesController.class);

	@Autowired
	RulesService rulesService;

	@PostMapping("/addRules")
	public ResponseEntity<Status> addRules(@RequestBody ObjectNode obj) throws JSONException {
		logger.info("Request to add a rule");
		Status st = new Status();
		try {
			Rules rules = rulesService.addRules(obj);
			if(rules==null) {
				st.setCode(HttpStatus.OK.value());
				st.setType("FIALED");
				st.setMessage("Add rule failed");
				st.setObject(rules);
				return ResponseEntity.status(HttpStatus.CONFLICT).body(st);
			}
			st.setCode(HttpStatus.OK.value());
			st.setType("SUCCESS");
			st.setMessage("Rule added successfully");
			st.setObject(rules);
			return ResponseEntity.status(HttpStatus.OK).body(st);
		}catch (UniqueConstraintException e) {
			logger.error("Add rule failed. Exception: ", e.getMessage());
			st.setCode(HttpStatus.EXPECTATION_FAILED.value());
			st.setType("ERROR");
			st.setMessage(e.getMessage());
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
		} 
		catch (Exception e) {
			logger.error("Add rule failed. Exception: ", e);
			st.setCode(HttpStatus.EXPECTATION_FAILED.value());
			st.setType("ERROR");
			st.setMessage("Add rule failed");
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
		}
	}

	@PostMapping("/updateRules")
	public ResponseEntity<Status> updateRules(@RequestBody ObjectNode obj) throws JSONException, URISyntaxException {
		logger.info("Request to update a rule");
		Status st = new Status();
		try {
			Rules rules = rulesService.updateRules(obj);
			if(rules==null) {
				st.setCode(HttpStatus.OK.value());
				st.setType("FIALED");
				st.setMessage("Updating rule failed");
				st.setObject(rules);
				return ResponseEntity.status(HttpStatus.CONFLICT).body(st);
			}
			st.setCode(HttpStatus.OK.value());
			st.setType("SUCCESS");
			st.setMessage("Rule updated successfully");
			st.setObject(rules);
			return ResponseEntity.status(HttpStatus.OK).body(st);
		} catch (UniqueConstraintException e) {
			logger.error("Update Rule failed. Exception: ", e.getMessage());
			st.setCode(HttpStatus.EXPECTATION_FAILED.value());
			st.setType("ERROR");
			st.setMessage(e.getMessage());
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
		} catch (Exception e) {
			logger.error("Updating rule failed. Exception: ", e);
			st.setCode(HttpStatus.EXPECTATION_FAILED.value());
			st.setType("ERROR");
			st.setMessage("Updating rule failed");
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
		}
	}

	@GetMapping("/searchRules")
	public ResponseEntity<Status> searchRules(@RequestParam Map<String, String> requestObj) {
		logger.info("Request to search rule on given filter criteria");
		Status st = new Status();
		try {
			List<Rules> list = rulesService.searchRules(requestObj);
			st.setCode(HttpStatus.OK.value());
			st.setType("SUCCESS");
			st.setMessage("Search rule successful");
			st.setObject(list);
			return ResponseEntity.status(HttpStatus.OK).body(st);
		} catch (Exception e) {
			logger.error("Search rule failed. Exception: ", e);
			st.setCode(HttpStatus.EXPECTATION_FAILED.value());
			st.setType("ERROR");
			st.setMessage("Search rule failed");
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
		}
	}

	@DeleteMapping("/deleteRules/{id}")
	public ResponseEntity<Status> deleteRules(@PathVariable Long id) {
		logger.info("Request to delete a rules");
		Status st = new Status();
		try {
			rulesService.deleteRules(id);
			st.setCode(HttpStatus.OK.value());
			st.setType("SUCCESS");
			st.setMessage("Rule deleted successfully");
			return ResponseEntity.status(HttpStatus.OK).body(st);
		} catch (Throwable e) {
			st.setCode(HttpStatus.EXPECTATION_FAILED.value());
			st.setType("ERROR");
			st.setMessage("Delete rule failed");
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
		}
	}

	@GetMapping("/getRules/{id}")
	public ResponseEntity<Status> getRules(@PathVariable Long id) {
		logger.info("Getting rule by id: " + id);
		Status st = new Status();
		try {
			Rules rules = rulesService.getRules(id);
			if (rules == null) {
				logger.warn("Rule not found.");
				st.setCode(HttpStatus.EXPECTATION_FAILED.value());
				st.setType("ERROR");
				st.setMessage("Rule not found");
				return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
			}
			st.setCode(HttpStatus.OK.value());
			st.setType("SUCCESS");
			st.setMessage("Rule found");
			st.setObject(rules);
			return ResponseEntity.status(HttpStatus.OK).body(st);
		} catch (Exception e) {
			logger.error("Rule not found. Exception: ", e);
			st.setCode(HttpStatus.EXPECTATION_FAILED.value());
			st.setType("ERROR");
			st.setMessage("Rule not found");
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
		}
	}
	@GetMapping("/getRulesByName/{name}")
	public ResponseEntity<Status> getRulesByName(@PathVariable String name) {
		logger.info("Getting rule by name: " + name);
		Status st = new Status();
		try {
			List<Rules> rules = rulesService.getRulesByName(name);
			if (rules == null) {
				logger.warn("Rule not found.");
				st.setCode(HttpStatus.EXPECTATION_FAILED.value());
				st.setType("ERROR");
				st.setMessage("Rule not found");
				return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
			}
			st.setCode(HttpStatus.OK.value());
			st.setType("SUCCESS");
			st.setMessage("Rule found");
			st.setObject(rules);
			return ResponseEntity.status(HttpStatus.OK).body(st);
		} catch (Exception e) {
			logger.error("Rule not found. Exception: ", e);
			st.setCode(HttpStatus.EXPECTATION_FAILED.value());
			st.setType("ERROR");
			st.setMessage("Rule not found");
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
		}
	}
}