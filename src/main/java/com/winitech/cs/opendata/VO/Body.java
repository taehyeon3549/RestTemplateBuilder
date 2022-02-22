package com.winitech.cs.opendata.VO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Body {
	@JsonProperty("header")
	private Header header;
	
	@JsonProperty("body")
	private Items items ;
		
	 @Override
	    public String toString() {
		 if(items != null) {
			 return "body{" +
		                " items='" + items + '\'' +
		                '}'; 
		 }else {
			 return "header{" +
		                " header='" + header + '\'' +
		                '}';
		 }
	        
	    }
}
