package com.accenture.bars.factory;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.accenture.bars.controller.FileProcessor;
import com.accenture.bars.exception.BarsException;
import com.accenture.bars.file.AbstractInputFile;
import com.accenture.bars.file.CSVInputFileImpl;
import com.accenture.bars.file.TextInputFileImpl;
import com.accenture.bars.repository.BillingRepository;

class InputFileFactoryTest
{
	InputFileFactory factory = InputFileFactory.getInstance();
	
	@Test
	void getInstance()
	{
		assertTrue(factory instanceof InputFileFactory);
	}
	
	@Test
	void getInputFile() throws BarsException, IOException
	{
		assertTrue(factory instanceof InputFileFactory);
		
		File file = new File("C:/BARS_TEST/valid-txt.txt");
		
		AbstractInputFile abstractInputFile = factory.getInputFile(file);
		
		assertTrue(abstractInputFile instanceof TextInputFileImpl);
	}
	
	@Test
	void getInputFileCSV() throws BarsException, IOException
	{
		assertTrue(factory instanceof InputFileFactory);
		
		File file = new File("C:/BARS_TEST/valid-csv.csv");
		
		AbstractInputFile abstractInputFile = factory.getInputFile(file);
		
		assertTrue(abstractInputFile instanceof CSVInputFileImpl);
	}
	
	@Test
	void getInputFileInvalid() throws BarsException, IOException
	{
		FileProcessor fileProcessor = new FileProcessor();
		
		File file = new File("C:/BARS_TEST/unsupported-file.png ");
		
	    try 
		{
	    	 fileProcessor.execute(file);
	    }
	    catch(Exception e)
	    {
	    	String expectedMessage = "File is not supported for processing";
	    	String actualMessage = e.getMessage();
	    	
	    	assertEquals(actualMessage, expectedMessage);
	    }
	}
}