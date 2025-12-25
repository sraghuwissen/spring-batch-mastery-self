package com.example.demo;

import org.springframework.batch.item.ItemProcessor;

public class RawToAggregateSensorDataProcessor implements ItemProcessor<DailySensorData, String>{

	@Override
	public String process(DailySensorData item) throws Exception {
		System.out.println(item.toString());
        return item.toString();
	}

	
	
}
