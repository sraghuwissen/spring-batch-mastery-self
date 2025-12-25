package com.example.demo;

import java.util.stream.Collectors;


import org.springframework.batch.item.ItemProcessor;

public class RawToAggregateSensorDataProcessor implements ItemProcessor<DailySensorData, DailyAggregatedSensorData>{

	@Override
	public DailyAggregatedSensorData process(DailySensorData item) throws Exception {
	
		double max= item.getMeasurements().stream().max((a,b)-> a>b?1:-1).orElse(0.0);
		
		
		double min= item.getMeasurements().stream().min((a,b)-> a>b?1:-1).orElse(0.0);
		double min1= item.getMeasurements().stream().mapToDouble(Double:: doubleValue).min().orElse(0.0);
		
		double avg = item.getMeasurements().stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
		
		DailyAggregatedSensorData result = new DailyAggregatedSensorData(convertToCelsius(min),convertToCelsius(max), convertToCelsius(avg), item.getDate().formatted("dd-mm-yy"));
        return result;
	}
	
	 private static double convertToCelsius(double fahT) {
	        return (5 * (fahT - 32)) / 9;
	    }

	
	
}
