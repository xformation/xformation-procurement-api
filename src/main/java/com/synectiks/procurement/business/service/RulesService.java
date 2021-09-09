package com.synectiks.procurement.business.service;

import java.net.URISyntaxException;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.synectiks.procurement.config.Constants;
import com.synectiks.procurement.domain.Rules;
import com.synectiks.procurement.repository.RulesRepository;

@Service
public class RulesService {

	private static final Logger logger = LoggerFactory.getLogger(RulesService.class);

	@Autowired
	private RulesRepository rulesRepository;

	public Rules addRules(ObjectNode obj) throws JSONException {
		Rules rules = new Rules();
		
		if (obj.get("name") != null) {
			rules.setName(obj.get("name").asText().toUpperCase());
		}
		
		Optional<Rules> optional = rulesRepository.findOne(Example.of(rules));
		
		if (optional.isPresent()) {
			return null;
		}

		if (obj.get("description") != null) {
			rules.setDescription(obj.get("description").asText());
		}

		if (obj.get("status") != null) {
			rules.setStatus(obj.get("status").asText());
		}
		if (obj.get("rule") != null) {
			rules.setRule(obj.get("rule").asText());
		}

		if (obj.get("user") != null) {
			rules.setCreatedBy(obj.get("user").asText());
			rules.setUpdatedBy(obj.get("user").asText());
		} else {
			rules.setCreatedBy(Constants.SYSTEM_ACCOUNT);
			rules.setUpdatedBy(Constants.SYSTEM_ACCOUNT);

			Instant now = Instant.now();
			rules.setCreatedOn(now);
			rules.setUpdatedOn(now);
			rules = rulesRepository.save(rules);
			logger.info("Adding rule completed" + rules.toString());

		}
		return rules;
	}

	public Rules updateRules(ObjectNode obj) throws JSONException, URISyntaxException {
		Optional<Rules> ur = rulesRepository.findById(Long.parseLong(obj.get("id").asText()));
		if (!ur.isPresent()) {
			logger.error("Rule not found");
			return null;
		}
		Rules rules = ur.get();

		if (obj.get("name") != null) {
			rules.setName(obj.get("name").asText().toUpperCase());
		}
		Optional<Rules> optional = rulesRepository.findOne(Example.of(rules));
		if (optional.isPresent()) {
			return null;
		}
		if (obj.get("description") != null) {
			rules.setDescription(obj.get("description").asText());
		}

		if (obj.get("status") != null) {
			rules.setStatus(obj.get("status").asText());
		}
		if (obj.get("rule") != null) {
			rules.setRule(obj.get("rule").asText());
		}

		if (obj.get("user") != null) {
			rules.setCreatedBy(obj.get("user").asText());
			rules.setUpdatedBy(obj.get("user").asText());
		} else {
			rules.setCreatedBy(Constants.SYSTEM_ACCOUNT);
			rules.setUpdatedBy(Constants.SYSTEM_ACCOUNT);

			Instant now = Instant.now();
			rules.setCreatedOn(now);
			rules.setUpdatedOn(now);
			rules = rulesRepository.save(rules);
			logger.info("Updateing rule completed" + rules.toString());

		}
		return rules;
	}

	public List<Rules> searchRules(@RequestParam Map<String, String> requestObj) {
		Rules rules = new Rules();
		boolean isFilter = false;
		if (requestObj.get("id") != null) {
			rules.setId(Long.parseLong(requestObj.get("id")));
			isFilter = true;
		}
		if (requestObj.get("name") != null) {
			rules.setName(requestObj.get("name"));
			isFilter = true;
		}
		if (requestObj.get("rule") != null) {
			rules.setRule(requestObj.get("rule"));
			isFilter = true;
		}
		if (requestObj.get("description") != null) {
			rules.setDescription(requestObj.get("description"));
			isFilter = true;
		}
		List<Rules> list = null;
		if (isFilter) {
			list = this.rulesRepository.findAll(Example.of(rules), Sort.by(Direction.DESC, "id"));
		} else {
			list = this.rulesRepository.findAll(Sort.by(Direction.DESC, "id"));
		}

		logger.info("Rule search completed. Total records: " + list.size());

		return list;
	}

	public Rules getRules(Long id) {
		logger.info("Getting rules by id: " + id);
		Optional<Rules> rul = rulesRepository.findById(id);
		if (rul.isPresent()) {
			Rules rules = rul.get();
			logger.info("Rules: " + rul.get().toString());
			return rules;
		}
		logger.warn("Rules not found");
		return null;
	}

	public Rules getRulesByName(String name) {
		logger.info("Getting rules by name: " + name);
		Rules rules = new Rules();
		rules.setName(name.toUpperCase());
		Optional<Rules> rul = rulesRepository.findOne(Example.of(rules));
		if (rul.isPresent()) {
			rules = rul.get();
			logger.info("Rules: " + rules);
			return rules;
		}
		logger.warn("Name not found");
		return null;
	}

	public void deleteRules(Long id) {
		rulesRepository.deleteById(id);
		logger.info("Rule deleted successfully");
	}

}
