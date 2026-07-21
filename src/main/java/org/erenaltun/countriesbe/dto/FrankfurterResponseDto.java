package org.erenaltun.countriesbe.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.Map;

@Data
public class FrankfurterResponseDto {
    private Double amount;
    private String base;
    private String date;

    private Map<String, Number> rates;
}