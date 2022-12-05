package com.accenture.bars.file;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.accenture.bars.controller.FileProcessor;
import com.accenture.bars.domain.Request;
import com.accenture.bars.exception.BarsException;
import com.accenture.bars.factory.InputFileFactory;
import com.accenture.bars.repository.BillingRepository;

import net.bytebuddy.asm.Advice.Local;

class CSVInputFileImplTest 
{
	@Test
	void readFile() throws BarsException, IOException
	{
		File file = new File("C:/BARS_TEST/valid-csv.csv");
		 
		InputFileFactory instance = InputFileFactory.getInstance();
		 
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		 
		AbstractInputFile abstractInputFile = instance.getInputFile(file);
		abstractInputFile.setFile(file);
		 
		List<Request> actual = abstractInputFile.readFile();
		List<Request> expected = new ArrayList<>();
		 
		expected.add(new Request(1, 
				LocalDate.parse("01/15/2013", dateFormatter), 
				LocalDate.parse("02/14/2013", dateFormatter)));
		
		expected.add(new Request(1, 
				LocalDate.parse("01/15/2016", dateFormatter), 
				LocalDate.parse("02/14/2016", dateFormatter)));
		 
		 assertEquals(expected, actual);
	}

	@Test
	void readFileBillingNotOnRange() throws BarsException, IOException 
	{
		File file = new File("C:/BARS_TEST/billing-cycle-not-on-range-csv.csv");
		
		FileProcessor fileProcessor = new FileProcessor();
		
		try
		{
			fileProcessor.execute(file);
		}
		catch(Exception e)
		{
			String expectedMessage = "ERROR: Billing Cycle not on range at row 4";
	    	String actualMessage = e.getMessage();
	    	
	    	assertEquals(actualMessage, expectedMessage);
		}
	}
	
	@Test
	void readFileInvalidStartDate() throws BarsException, IOException 
	{
		File file = new File("C:/BARS_TEST/invalid-start-date-csv.csv");
		
		FileProcessor fileProcessor = new FileProcessor();
		
		try
		{ 
			fileProcessor.execute(file);
		}
		catch(Exception e)
		{
			String expectedMessage = "ERROR: Invalid Start Date format at row 1";
	    	String actualMessage = e.getMessage();
	    	
	    	assertEquals(actualMessage, expectedMessage);
		}
	}
	
	@Test
	void readFileInvalidEndDate() throws BarsException, IOException 
	{
		File file = new File("C:/BARS_TEST/invalid-end-date-csv.csv");
		
		FileProcessor fileProcessor = new FileProcessor();
		
		try
		{
			fileProcessor.execute(file);
		}
		catch(Exception e)
		{
			String expectedMessage = "ERROR: Invalid End Date format at row 7";
	    	String actualMessage = e.getMessage();
	    	
	    	assertEquals(actualMessage, expectedMessage);
		}
	}
}