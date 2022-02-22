package com.winitech.cs.opendata.VO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Items {
	
	@JsonProperty("items")
	private Item item;
		
	@JsonProperty("totalCount")
	private String totalCount;
	 
	 @Override
	    public String toString() {
		 return "item{" +
	                " items='" + item + '\'' +
	                '}';
	 }
}
