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
import com.synectiks.procurement.business.service.CommitteeService;
import com.synectiks.procurement.domain.Committee;
import com.synectiks.procurement.domain.Status;

@RestController
@RequestMapping("/api")
public class CommitteeController {
	private static final Logger logger = LoggerFactory.getLogger(CommitteeController.class);

	@Autowired
	private CommitteeService committeeService;

	@PostMapping("/addCommittee")
	public ResponseEntity<Status> addCommittee(@RequestBody ObjectNode obj) throws JSONException {
		logger.info("Request to add New committee");
		Status st = new Status();
		try {
			Committee committee = committeeService.addCommittee(obj);
			if (committee == null) {
				logger.error("Committee could not be added.");
				st.setCode(HttpStatus.EXPECTATION_FAILED.value());
				st.setType("ERROR");
				st.setMessage("Add committee failed");
				return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
			}
			st.setCode(HttpStatus.OK.value());
			st.setType("SUCCESS");
			st.setMessage("Committee added successfully");
			st.setObject(committee);
			return ResponseEntity.status(HttpStatus.OK).body(st);
		} catch (Exception e) {
			logger.error("Adding committee failed: ", e);
			st.setCode(HttpStatus.EXPECTATION_FAILED.value());
			st.setType("ERROR");
			st.setMessage("Adding committee failed");
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
		}

	}

	@PostMapping("/updateCommittee")
	public ResponseEntity<Status> updateCommittee(@RequestBody ObjectNode obj)
			throws JSONException, URISyntaxException {
		logger.info("Request to upde committee");
		Status st = new Status();
		try {
			Committee committee = committeeService.updateCommittee(obj);
			if (committee == null) {
				logger.error("Committee could not be updated.");
				st.setCode(HttpStatus.EXPECTATION_FAILED.value());
				st.setType("ERROR");
				st.setMessage("Update committee failed");
				return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
			}
			st.setCode(HttpStatus.OK.value());
			st.setType("SUCCESS");
			st.setMessage("Committee added successfully");
			st.setObject(committee);
			return ResponseEntity.status(HttpStatus.OK).body(st);
		} catch (Exception e) {
			logger.error("Updating committee failed. Exception: ", e);
			st.setCode(HttpStatus.EXPECTATION_FAILED.value());
			st.setType("ERROR");
			st.setMessage("Updating committee failed");
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
		}
	}

	@GetMapping("/searchCommittee")
	public ResponseEntity<Status> searchCommittee(@RequestParam Map<String, String> requestObj) {
		logger.info("Request to get list of committee on given filter criteria");
		Status st = new Status();
		try {
			List<Committee> list = committeeService.searchCommittee(requestObj);
			if (list == null) {
				logger.error("Search committee failed");
				st.setCode(HttpStatus.EXPECTATION_FAILED.value());
				st.setType("ERROR");
				st.setMessage("Search committee failed");
				return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
			}
			st.setCode(HttpStatus.OK.value());
			st.setType("SUCCESS");
			st.setMessage("Search committee successful");
			st.setObject(list);
			return ResponseEntity.status(HttpStatus.OK).body(st);
		} catch (Exception e) {
			logger.error("Searching committee failed. Exception: ", e);
			st.setCode(HttpStatus.EXPECTATION_FAILED.value());
			st.setType("ERROR");
			st.setMessage("Searching committee failed");
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
		}
	}

	@DeleteMapping("/deleteCommittee/{id}")
	public ResponseEntity<Status> deleteCommittee(@PathVariable Long id) {
		Status st = new Status();
		try {
			committeeService.deleteCommittee(id);
			st.setCode(HttpStatus.OK.value());
			st.setType("SUCCESS");
			st.setMessage("Delete Committee successful");
			return ResponseEntity.status(HttpStatus.OK).body(st);
		} catch (Throwable e) {
			st.setCode(HttpStatus.EXPECTATION_FAILED.value());
			st.setType("ERROR");
			st.setMessage("Delete committee failed");
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
		}
	}

	@GetMapping("/getCommittee/{id}")
	public ResponseEntity<Status> getCommittee(@PathVariable Long id) {
		logger.info("Getting committee by id: " + id);
		Status st = new Status();
		try {
			Committee committee = committeeService.getCommittee(id);
			if (committee == null) {
				logger.warn("Committee not found.");
				st.setCode(HttpStatus.EXPECTATION_FAILED.value());
				st.setType("ERROR");
				st.setMessage("Committee not found");
				return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
			}
			st.setCode(HttpStatus.OK.value());
			st.setType("SUCCESS");
			st.setMessage(" Committee found");
			st.setObject(committee);
			return ResponseEntity.status(HttpStatus.OK).body(st);
		} catch (Exception e) {
			logger.error("Committee not found. Exception: ", e);
			st.setCode(HttpStatus.EXPECTATION_FAILED.value());
			st.setType("ERROR");
			st.setMessage("Committee not found");
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
		}
	}

}