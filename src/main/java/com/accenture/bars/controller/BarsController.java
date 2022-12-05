package com.accenture.bars.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.accenture.bars.domain.Record;
import com.accenture.bars.exception.BarsException;

@RestController
public class BarsController {
	public static final Logger log = LoggerFactory.getLogger(BarsController.class);
	
	@Autowired
	private FileProcessor fileProcessor;
	
	public BarsController() {
		//Do something
	}
	
	@GetMapping("/bars")
	public List<Record> requestBilling(@RequestParam String fileName) 
			throws BarsException, IOException {
		File file = new File("C:/BARS_TEST/" + fileName);
		
		log.info("===========> FilePath: " + fileName);
		log.info("File to process: C:/BARS_TEST/" + fileName);
		
		List<Record> records = fileProcessor.retreiveRecordfromDB(
				fileProcessor.execute(file));
		
		return records;
	}
}
