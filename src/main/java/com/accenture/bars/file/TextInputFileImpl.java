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

public class TextInputFileImpl extends AbstractInputFile{
	private static final Logger log = 
			LoggerFactory.getLogger(TextInputFileImpl.class);
	public static final int SD_END_BILL_STRING = 2;
	public static final int SD_START_MONTH_STRING = 4;
	public static final int SD_END_MONTH_STRING = 6;
	public static final int SD_END_YEAR_STRING = 10;
	public static final int ED_START_MONTH_STRING = 12;
	public static final int ED_END_MONTH_STRING = 14;
	public static final int ED_END_YEAR_STRING = 18;
	public TextInputFileImpl(){
		//Do something
	}
	@Override
	public List<Request> readFile() throws IOException, BarsException{
		List<Request> requests = new ArrayList<>();
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		String tempHolder = ""; 
		try(BufferedReader bufferedReader = 
				new BufferedReader(new FileReader(getFile()))){
			int index = 0;
			while((tempHolder = bufferedReader.readLine()) != null){
				index++;
				try{
					int billCycle = Integer.parseInt(
								tempHolder.substring(0, SD_END_BILL_STRING));
					if (billCycle >= MIN_BILLING_CYCLE && billCycle <= MAX_BILLING_CYCLE){
                        try{
                        	LocalDate startDate = LocalDate.parse(
										tempHolder.substring(
												SD_END_BILL_STRING, SD_START_MONTH_STRING) + "/"
										+ tempHolder.substring(
												SD_START_MONTH_STRING, SD_END_MONTH_STRING) + "/"
										+ tempHolder.substring(
												SD_END_MONTH_STRING, SD_END_YEAR_STRING), dateFormatter);
								
							try{
                                LocalDate endDate = LocalDate.parse(
                                		tempHolder.substring(
											SD_END_YEAR_STRING, ED_START_MONTH_STRING) + "/"
											+ tempHolder.substring(
													ED_START_MONTH_STRING, ED_END_MONTH_STRING) + "/"
											+ tempHolder.substring(
													ED_END_MONTH_STRING, ED_END_YEAR_STRING), dateFormatter);
									
								log.info("==> Processing Request ROW: " + index + " <==");
								requests.add(new Request(billCycle, startDate, endDate));
								}
								catch(DateTimeParseException endDateExeption){
								log.info("ERROR: Invalid End Date format at row " + index);
								throw new BarsException(BarsException.INVALID_END_DATE_FORMAT + index);
								}
							}catch(DateTimeParseException startDateExeption){
							log.info("ERROR: Invalid Start Date format at row " + index);
							throw new BarsException(
										BarsException.INVALID_START_DATE_FORMAT 
										+ index);
							}
						}else{
						log.info("ERROR: Billing Cycle not on range at row " + index);
						throw new BarsException(
									BarsException.BILLING_CYCLE_NOT_ON_RANGE + index);
						}
				}catch(NumberFormatException e){
					log.info(e.toString());
				}
			}
			if (requests.size() == 0){
				return requests;
			} else {
				log.info("Successfully processed Request File");
				return requests;
			}
		}
	}
}
