package com.skillbox.controller.dto.response;

import lombok.Data;
import lombok.NonNull;

import java.util.Map;
import java.util.Set;

@Data
public class CalendarResponseDTO {

    @NonNull
    private Set<Integer> years;

    @NonNull
    private Map<String, Integer> posts;
}
