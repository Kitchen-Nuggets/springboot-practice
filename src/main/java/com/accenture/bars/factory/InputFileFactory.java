package com.accenture.bars.factory;

import java.io.File;
import java.io.IOException;

import com.accenture.bars.exception.BarsException;
import com.accenture.bars.file.AbstractInputFile;
import com.accenture.bars.file.CSVInputFileImpl;
import com.accenture.bars.file.TextInputFileImpl;

public class InputFileFactory{
	private static InputFileFactory factory;
	private InputFileFactory(){
		//DO something
	}
	public static InputFileFactory getInstance(){
		if (factory == null){
			factory = new InputFileFactory();
		}
		return factory;
	}
	public AbstractInputFile getInputFile(File file) 
			throws BarsException, IOException{
		String filePath = file.getPath();
		if (filePath.endsWith(".txt")){
		    return new TextInputFileImpl();
		}else if (filePath.endsWith(".csv")){
		    return new CSVInputFileImpl();
		}else{
			throw new BarsException(BarsException.FILE_NOT_SUPPORTED);
		}
	}
}