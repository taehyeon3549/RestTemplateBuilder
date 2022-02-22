package com.winitech.cs.opendata.VO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenModel {

@JsonProperty("response")
 private Body body ;
 
 @Override
    public String toString() {
        return "response{" +
                " Body='" + body + '\'' +
                '}';
    }
}
