package com.accenture.bars.file;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;
import org.springframework.web.context.annotation.RequestScope;

import com.accenture.bars.controller.FileProcessor;
import com.accenture.bars.domain.Request;
import com.accenture.bars.exception.BarsException;
import com.accenture.bars.factory.InputFileFactory;

import net.bytebuddy.asm.Advice.Local;

class TextInputFileImplTest 
{
	@Test
	void readFile() throws BarsException, IOException
	{
		File file = new File("C:/BARS_TEST/valid-txt.txt");
		 
		InputFileFactory instance = InputFileFactory.getInstance();
		 
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		 
		AbstractInputFile abstractInputFile = instance.getInputFile(file);
		abstractInputFile.setFile(file);
		 
		List<Request> actual = abstractInputFile.readFile();
		List<Request> expected = new ArrayList<>();
		
		expected.add(new Request(1, LocalDate.parse("2013-01-15"), LocalDate.parse("2013-02-14")));
		expected.add(new Request(1, LocalDate.parse("2016-01-15"), LocalDate.parse("2016-02-14")));
		
		assertEquals(expected, actual);
	}

	@Test
	void readFileBillingNotOnRange() throws BarsException, IOException 
	{
		File file = new File("C:/BARS_TEST/billing-cycle-not-on-range-txt.txt");
	
		FileProcessor fileProcessor = new FileProcessor();
	
		try 
		{
			fileProcessor.execute(file);
		} 
		catch (Exception e) 
		{
			String expectedMessage = "ERROR: Billing Cycle not on range at row 3";
			String actualMessage = e.getMessage();
			
			assertEquals(actualMessage, expectedMessage);
		}
	}
	
	@Test
	void readFileInvalidStartDate() throws BarsException, IOException 
	{
		File file = new File("C:/BARS_TEST/invalid-start-date-txt.txt");
		
		FileProcessor fileProcessor = new FileProcessor();
		
		try
		{
			fileProcessor.execute(file);
		}
		catch(Exception e)
		{
			String expectedMessage = "ERROR: Invalid Start Date format at row 3";
	    	String actualMessage = e.getMessage();
	    	
	    	assertEquals(actualMessage, expectedMessage);
		}
	}
	
	@Test
	void readFileInvalidEndDate() throws BarsException, IOException 
	{
		File file = new File("C:/BARS_TEST/invalid-end-date-txt.txt");
		
		FileProcessor fileProcessor = new FileProcessor();
		
		try
		{
			fileProcessor.execute(file);
		}
		catch(Exception e)
		{
			String expectedMessage = "ERROR: Invalid End Date format at row 1";
	    	String actualMessage = e.getMessage();
	    	
	    	assertEquals(actualMessage, expectedMessage);
		}
	}
}