package com.accenture.bars.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.accenture.bars.entity.Billing;
import com.accenture.bars.domain.Record;
import com.accenture.bars.domain.Request;
import com.accenture.bars.exception.BarsException;
import com.accenture.bars.factory.InputFileFactory;
import com.accenture.bars.file.AbstractInputFile;
import com.accenture.bars.repository.BillingRepository;

@Component
public class FileProcessor{
	@Autowired
	private BillingRepository billingRepository;
	private static final Logger log = LoggerFactory.getLogger(FileProcessor.class);
	public FileProcessor(){
		//Do something
	}
	public List<Request> execute(File file) throws BarsException, IOException{
		InputFileFactory inputFileFactory = InputFileFactory.getInstance();
		AbstractInputFile abstractInputFile = inputFileFactory.getInputFile(file);
		
		abstractInputFile.setFile(file);
		
		List<Request> requests = abstractInputFile.readFile();
		
		return requests;
	}
	public List<Record> retreiveRecordfromDB(List<Request> requests) 
			throws BarsException{
		List<Record> records = new ArrayList<>();
		for (Request request : requests) {
			Billing billing = billingRepository.findByBillingCycleAndStartDateAndEndDate(
					request.getBillingCycle(), 
					request.getStartDate(), 
					request.getEndDate());
			if(billing != null){
				Record record = new Record();
				record.setBillingCycle(billing.getBillingCycle());
				record.setEndDate(billing.getEndDate());
				record.setStartDate(billing.getStartDate());
				record.setAmount(billing.getAmount());
				record.setAccountName(billing.getAccountId().getAccountName());
				record.setFirstName(billing.getAccountId().getCustomerId().getFirstName());
				record.setLastName(billing.getAccountId().getCustomerId().getLastName());
				records.add(record);
			}else{
				log.info("No record(s) to write to the output file.");
				throw new BarsException(BarsException.NO_RECORDS_TO_WRITE);
			}
        }
		if (records.isEmpty()){
			log.info("No request(s) to read from the input file.");
			throw new BarsException(BarsException.NO_REQUEST_TO_READ);
		}else{
			return records;
		}
	}
}
