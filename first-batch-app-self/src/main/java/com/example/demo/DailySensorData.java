package com.example.demo;

import java.util.List;

public class DailySensorData {

	
	
	private final String date;
	private final List<Double> measurements;
	
	
	public DailySensorData(String date, List<Double> measurements) {
		this.date = date;
		this.measurements = measurements;
	}
	/**
	 * @return the date
	 */
	public String getDate() {
		return date;
	}
	/**
	 * @return the measurements
	 */
	public List<Double> getMeasurements() {
		return measurements;
	}
	@Override
	public String toString() {
		return "DailySensorData [date=" + date + ", measurements=" + measurements + "]";
	}
	
		
	
}
