package com.dbd.devday.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.dbd.devday.db.KeyValueStoreDAO;
import com.dbd.devday.model.KeyValue;

@Controller
@RequestMapping("/kv")
public class KeyValueController {

	private final KeyValueStoreDAO keyValueStoreDAO;
	
	@Autowired
	public KeyValueController(KeyValueStoreDAO keyValueStoreDAO) {
		this.keyValueStoreDAO = keyValueStoreDAO;
	}
	
	@RequestMapping(value = "{userId}", method = RequestMethod.GET)
	@ResponseBody
	public List<KeyValue> list(@PathVariable String userId) throws Exception {
		return this.keyValueStoreDAO.findByUser(userId);
	}
	
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public List<KeyValue> listAll() throws Exception {
		return this.keyValueStoreDAO.findAllUsers();
	}
	
	@RequestMapping(value = "{userId}", method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.ACCEPTED)
	public void save(@PathVariable String userId, 
			@RequestBody KeyValue keyValue) throws Exception {
		this.keyValueStoreDAO.save(userId, keyValue);
	}
	
}
