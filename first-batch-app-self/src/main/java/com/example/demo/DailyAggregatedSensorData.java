package com.example.demo;

import java.util.HashMap;
import java.util.Map;



import com.thoughtworks.xstream.security.ExplicitTypePermission;
import org.springframework.oxm.xstream.XStreamMarshaller;
public class DailyAggregatedSensorData {
     
	
	private double min;
	private double max;
	private double avg;
	private String date;// dd-mm-yy format
	
	
	
	public DailyAggregatedSensorData(double min, double max, double avg, String date) {
		super();
		this.min = min;
		this.max = max;
		this.avg = avg;
		this.date = date;
	}
	
	
	public static XStreamMarshaller getMarshaller() {
		
		XStreamMarshaller marshaller = new XStreamMarshaller();
		
		Map<String , Class> aliases = new HashMap<>();
		
		aliases.put("date", String.class);
		aliases.put("min", Double.class);
		aliases.put("max", Double.class);
		aliases.put("avg", Double.class);
		

      ExplicitTypePermission explicitTypePermission = new ExplicitTypePermission(new Class[] {DailyAggregatedSensorData.class});
      
      marshaller.setAliases(aliases);
      marshaller.setTypePermissions(explicitTypePermission);
		
      return marshaller;
	}
	
	@Override
	public String toString() {
		return "DailyAggregatedSensorData [min=" + min + ", max=" + max + ", avg=" + avg + ", date=" + date + "]";
	}




	/**
	 * @return the min
	 */
	public double getMin() {
		return min;
	}
	/**
	 * @return the max
	 */
	public double getMax() {
		return max;
	}
	/**
	 * @return the avg
	 */
	public double getAvg() {
		return avg;
	}
	/**
	 * @return the date
	 */
	public String getDate() {
		return date;
	}
	/**
	 * @param min the min to set
	 */
	public void setMin(double min) {
		this.min = min;
	}
	/**
	 * @param max the max to set
	 */
	public void setMax(double max) {
		this.max = max;
	}
	/**
	 * @param avg the avg to set
	 */
	public void setAvg(double avg) {
		this.avg = avg;
	}
	/**
	 * @param date the date to set
	 */
	public void setDate(String date) {
		this.date = date;
	}
	
	
	
	
	
	
}
