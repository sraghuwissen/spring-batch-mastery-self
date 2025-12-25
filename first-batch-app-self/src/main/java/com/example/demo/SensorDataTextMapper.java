package com.example.demo;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import org.springframework.batch.item.file.LineMapper;

public class SensorDataTextMapper implements LineMapper<DailySensorData>{

	@Override
	public DailySensorData mapLine(String line, int lineNumber) throws Exception {
		List parts = (List) Arrays.asList(line.split(":"));
		DailySensorData dailySensorData =new DailySensorData(
				((java.util.List<String>) parts).get(0)   ,
				 Arrays.asList( ((java.util.List<String>) parts).get(1).split(","))
				 .stream().map(s ->  Double.valueOf(s)).toList() 
				 );
		return dailySensorData;
	}
	
	

}
