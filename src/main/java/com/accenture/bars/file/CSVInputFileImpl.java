package com.accenture.bars.file;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.accenture.bars.domain.Request;
import com.accenture.bars.exception.BarsException;

public class CSVInputFileImpl extends AbstractInputFile{
	private static final Logger log = LoggerFactory.getLogger(
			CSVInputFileImpl.class);
	public static final int END_DATE_DATA_INDEX = 2;
	public CSVInputFileImpl(){
		//Do something
	}
	@Override
	public List<Request> readFile() throws IOException, BarsException{	
		List<Request> requests = new ArrayList<>();
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		String tempHolder = "";
		
		try(BufferedReader 
				bufferedReader = new BufferedReader(new FileReader(getFile()))){
			int index = 0;
			
			while ((tempHolder = bufferedReader.readLine()) != null){
				String[] data = tempHolder.split(",");
				index++;
				try{
					int billCycle = Integer.parseInt(data[0]);
                    if(Integer.parseInt(data[0]) >= MIN_BILLING_CYCLE 
								&& Integer.parseInt(data[0]) <= MAX_BILLING_CYCLE){
                    	try{
                    		LocalDate startDate = LocalDate.parse(
                    				data[1], dateFormatter);
                    		try{LocalDate endDate = LocalDate.parse(
											data[END_DATE_DATA_INDEX], dateFormatter);
                                log.info("==> Processing Request ROW: " 
											+ index + " <==");
                                requests.add(new Request(
                                		billCycle, startDate, endDate));
								}
								catch(DateTimeParseException 
										endDateExeption){
                                log.info(
                                		"ERROR: Invalid End Date format at row " 
										+ index);
                                throw new BarsException(
                                		BarsException.INVALID_END_DATE_FORMAT 
											+ index);
								}
							}catch(DateTimeParseException startDateExeption){
                            log.info("ERROR: Invalid Start Date format at row " 
							+ index);
                            throw new BarsException(
                            		BarsException.INVALID_START_DATE_FORMAT 
										+ index);
							}
						}else{
                        log.info("ERROR: Billing Cycle not on range at row " 
						+ index);
                        throw new BarsException(
									BarsException.BILLING_CYCLE_NOT_ON_RANGE + index);
						}
				}catch(NumberFormatException e){
					log.info(e.toString());
				}
			}
			if (requests.size() == 0){
				return requests;
			}else{
				log.info("Successfully processed Request File");
				return requests;
			}
		}
	}
}
