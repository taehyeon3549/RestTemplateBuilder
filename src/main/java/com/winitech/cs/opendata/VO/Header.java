package com.winitech.cs.opendata.VO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Header {
	@JsonProperty("resultCode")
	String resultCode;
	
	@JsonProperty("resultMsg")
	String resultMsg;
	
	@Override
	public String toString() {
		return "header{" +
                " resultCode='" + resultCode + '\'' +
                " resultMsg='" + resultMsg + '\'' +
                '}'; 
	}
}
