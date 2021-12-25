package com.skillbox.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;
import java.util.Set;

@Data
@AllArgsConstructor
public class CalendarResponseDTO {

    private Set<Integer> years;

    private Map<String, Integer> posts;
}
