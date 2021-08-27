package com.synectiks.procurement.business.service;

import java.net.URISyntaxException;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.synectiks.procurement.config.Constants;
import com.synectiks.procurement.domain.Committee;
import com.synectiks.procurement.domain.CommitteeActivity;
import com.synectiks.procurement.repository.CommitteeActivityRepository;
import com.synectiks.procurement.repository.CommitteeRepository;

@Service
public class CommitteeService {
	private static final Logger logger = LoggerFactory.getLogger(CommitteeService.class);

	@Autowired
	private CommitteeRepository committeeRepository;

	@Autowired
	private CommitteeActivityRepository committeeActivityRepository;

	public Committee getCommittee(Long id) {
		logger.info("Getting committee by id: " + id);
		Optional<Committee> ovn = committeeRepository.findById(id);
		if (ovn.isPresent()) {
			logger.info("Committee: " + ovn.get().toString());
			return ovn.get();
		}
		logger.warn("Committee not found");
		return null;
	}

	public Committee addCommittee(ObjectNode obj) throws JSONException {
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
		committee.setStatus(Constants.Status);
		committee.setCreatedBy(Constants.SYSTEM_ACCOUNT);
		committee.setUpdatedBy(Constants.SYSTEM_ACCOUNT);
		Instant now = Instant.now();
		committee.setCreatedOn(now);
		committee.setUpdatedOn(now);
		committee = committeeRepository.save(committee);
		logger.info("Committee added successfully");

		if (committee.getId() != null) {
			CommitteeActivity committeeActivity = new CommitteeActivity();
			BeanUtils.copyProperties(committee, committeeActivity);
			committeeActivity.setCommittee(committee);
			committeeActivity = committeeActivityRepository.save(committeeActivity);
			logger.info("Committee activity added successfully");
		}
		logger.info("Committee added successfully" + committee.toString());
		return committee;

	}

	public Committee updateCommittee(ObjectNode obj) throws JSONException, URISyntaxException {
		Optional<Committee> ur = committeeRepository.findById(Long.parseLong(obj.get("id").asText()));
		if (!ur.isPresent()) {
			logger.info("Committee id not found");
			return null;
		}

		Committee committee = ur.get();

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
		logger.info("Updating committee completed : " + committee);
		if (committee.getId() != null) {
			CommitteeActivity committeeActivity = new CommitteeActivity();
			BeanUtils.copyProperties(committee, committeeActivity);
			committeeActivity.setCommittee(committee);
			committeeActivity = committeeActivityRepository.save(committeeActivity);
			logger.info("Committee activity update successfully");
		}
		return committee;
	}

	public List<Committee> searchCommittee(Map<String, String> requestObj) {
		logger.info("Request to get committee on given filter criteria");
		Committee committee = new Committee();
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
		logger.info("Committee search completed. Total records: " + list.size());
		return list;
	}

	public void deleteCommittee(Long id) {
		Optional<Committee> oc = committeeRepository.findById(id);
		if (oc.isPresent()) {
			CommitteeActivity committeeActivity = new CommitteeActivity();
			committeeActivity.setCommittee(oc.get());
			committeeActivityRepository.deleteAll(committeeActivityRepository.findAll(Example.of(committeeActivity)));
			committeeRepository.deleteById(id);
		}
	}
}