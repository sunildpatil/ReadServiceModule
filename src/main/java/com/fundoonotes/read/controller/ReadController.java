package com.fundoonotes.read.controller;

import java.util.List;
import java.util.Map;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fundoonotes.read.repository.NoteRepository;
import com.fundoonotes.read.repository.UserRepository;
import com.fundoonotes.read.util.ResourceNotFoundException;

@RestController
@PropertySource({ "classpath:exception.properties" })
public class ReadController {

	private static final Logger LOGGER = Logger.getLogger(ReadController.class);

	@Autowired
	private Environment env;

	@Autowired
	private NoteRepository noteRepository;

	@Autowired
	private UserRepository userRepository;

	@RequestMapping(value = "/fundoonotes/{key}/{hashKey}", method = RequestMethod.GET)
	public ResponseEntity<Object> getUserById(@PathVariable("key") String key, @PathVariable("hashKey") String hashKey)
			throws JsonProcessingException {
		Object user = userRepository.getUserById(key, hashKey);
		if (user == null) {
			throw new ResourceNotFoundException(env.getProperty("resource.not.found"));
		}
		return new ResponseEntity<Object>(user, HttpStatus.OK);
	}

	@RequestMapping(value = "/{index}/{type}/{field}/{value}", method = RequestMethod.GET)
	public ResponseEntity<List<Map<String, Object>>> getNotesByUserId(@PathVariable Map<String, String> pathValues) {
		String index = pathValues.get("index");
		String type = pathValues.get("type");
		String fieldName = pathValues.get("field");
		String value = pathValues.get("value");
		List<Map<String, Object>> output = noteRepository.getNotesByUserId(index, type, fieldName, value);
		if (output.size() == 0) {
			throw new ResourceNotFoundException(env.getProperty("resource.not.found"));
		}
		return new ResponseEntity<List<Map<String, Object>>>(output, HttpStatus.OK);
	}

	@RequestMapping(value = "/{index}/{type}/{field}/search", method = RequestMethod.GET)
	public ResponseEntity<List<Map<String, Object>>> getNotesByState(@PathVariable Map<String, String> pathValues,
			@RequestParam("userid") String user_id) {
		String index = pathValues.get("index");
		String type = pathValues.get("type");
		String field = pathValues.get("field");
		List<Map<String, Object>> output = noteRepository.getNotesByState(index, type, field, user_id);
		if (output.size() == 0) {
			throw new ResourceNotFoundException(env.getProperty("resource.not.found"));
		}
		return new ResponseEntity<List<Map<String, Object>>>(output, HttpStatus.OK);
	}

}
