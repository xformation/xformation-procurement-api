package com.synectiks.procurement.controllers;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.synectiks.procurement.business.service.CommitteeMembersService;
import com.synectiks.procurement.domain.CommitteeMembers;
import com.synectiks.procurement.domain.Status;

@RestController
@RequestMapping("/api")
public class CommitteeMembersController {
	private static final Logger logger = LoggerFactory.getLogger(CommitteeMembersController.class);

	@Autowired
	private CommitteeMembersService committeeMembersService;

	@RequestMapping(value = "/addCommitteeMembers", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Status> addCommitteeMembers(@RequestParam("obj") String obj,
			@RequestParam("file") MultipartFile file) throws JSONException {
		logger.info("Request to add New committee member");
		Status st = new Status();
		try {
			CommitteeMembers committeeMembers = committeeMembersService.addCommitteeMember(obj, file);
			if (committeeMembers == null) {
				logger.error("Committee member could not be added.");
				st.setCode(HttpStatus.EXPECTATION_FAILED.value());
				st.setType("ERROR");
				st.setMessage("Add committee member failed");
				return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
			}
			st.setCode(HttpStatus.OK.value());
			st.setType("SUCCESS");
			st.setMessage("Committee member added successfully");
			st.setObject(committeeMembers);
			return ResponseEntity.status(HttpStatus.OK).body(st);
		} catch (Exception e) {
			logger.error("Adding committee member failed: ", e);
			st.setCode(HttpStatus.EXPECTATION_FAILED.value());
			st.setType("ERROR");
			st.setMessage("Adding committee  member failed");
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
		}

	}

	@RequestMapping(value = "/updateCommitteeMembers", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Status> updateCommitteeMembers(@RequestParam("obj") String obj,
			@RequestParam("file") MultipartFile file) throws JSONException, URISyntaxException {
		logger.info("Request to upde committee members");
		Status st = new Status();
		try {
			CommitteeMembers committeeMembers = committeeMembersService.updateCommitteeMembers(obj, file);
			if (committeeMembers == null) {
				logger.error("Committee members could not be updated.");
				st.setCode(HttpStatus.EXPECTATION_FAILED.value());
				st.setType("ERROR");
				st.setMessage("Update committee members failed");
				return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
			}
			st.setCode(HttpStatus.OK.value());
			st.setType("SUCCESS");
			st.setMessage("Committee members update successfully");
			st.setObject(committeeMembers);
			return ResponseEntity.status(HttpStatus.OK).body(st);
		} catch (Exception e) {
			logger.error("Updating committee members failed. Exception: ", e);
			st.setCode(HttpStatus.EXPECTATION_FAILED.value());
			st.setType("ERROR");
			st.setMessage("Updating committee members failed");
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
		}
	}

	@GetMapping("/searchCommitteeMembers")
	public ResponseEntity<Status> searchCommitteeMembers(@RequestParam Map<String, String> requestObj) {
		logger.info("Request to get list of committee on given filter criteria");
		Status st = new Status();
		try {
			List<CommitteeMembers> list = committeeMembersService.searchCommitteeMembers(requestObj);
			if (list == null) {
				logger.error("Search committee members failed");
				st.setCode(HttpStatus.EXPECTATION_FAILED.value());
				st.setType("ERROR");
				st.setMessage("Search committee members failed");
				return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
			}
			st.setCode(HttpStatus.OK.value());
			st.setType("SUCCESS");
			st.setMessage("Search committee members successful");
			st.setObject(list);
			return ResponseEntity.status(HttpStatus.OK).body(st);
		} catch (Exception e) {
			logger.error("Searching committee members failed . Exception: ", e);
			st.setCode(HttpStatus.EXPECTATION_FAILED.value());
			st.setType("ERROR");
			st.setMessage("Searching committee members failed");
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
		}
	}

	@GetMapping("/getCommitteeMembers/{id}")
	public ResponseEntity<Status> getCommitteeMembers(@PathVariable Long id) {
		logger.info("Getting committee members by id: " + id);
		Status st = new Status();
		try {
			CommitteeMembers committeeMembers = committeeMembersService.getCommitteeMember(id);
			if (committeeMembers == null) {
				logger.warn("Committee members not found.");
				st.setCode(HttpStatus.EXPECTATION_FAILED.value());
				st.setType("ERROR");
				st.setMessage("Committee members not found");
				return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
			}
			st.setCode(HttpStatus.OK.value());
			st.setType("SUCCESS");
			st.setMessage(" Committee members found");
			st.setObject(committeeMembers);
			return ResponseEntity.status(HttpStatus.OK).body(st);
		} catch (Exception e) {
			logger.error("Committee members not found. Exception: ", e);
			st.setCode(HttpStatus.EXPECTATION_FAILED.value());
			st.setType("ERROR");
			st.setMessage("Committee members not found");
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
		}
	}

}