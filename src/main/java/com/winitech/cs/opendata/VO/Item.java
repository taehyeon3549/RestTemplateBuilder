package com.winitech.cs.opendata.VO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Item {

	@JsonProperty("item")
	private List<Object> data ;
	 
	@Override
	public String toString() {
		return "Item{" +
				" item='" + data.toString() + '\'' +
				'}';
	}
}