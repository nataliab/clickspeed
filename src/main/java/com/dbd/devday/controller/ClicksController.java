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

import com.dbd.devday.db.ClicksDAO;
import com.dbd.devday.model.Timestamp;
import com.dbd.devday.model.UserClicks;

@Controller
@RequestMapping("/clicks")
public class ClicksController {

	private final ClicksDAO clicksDAO;
	
	@Autowired
	public ClicksController(ClicksDAO clicksDAO) {
		this.clicksDAO = clicksDAO;
	}
	

	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public List<UserClicks> getResults() throws Exception {
		return this.clicksDAO.getResults();
	}
	
	@RequestMapping(value = "{userId}", method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.ACCEPTED)
	public void save(@PathVariable String userId, 
			@RequestBody Timestamp timestamp) throws Exception {
		this.clicksDAO.click(userId, Long.parseLong(timestamp.getTimestamp()));
	}
	
}
