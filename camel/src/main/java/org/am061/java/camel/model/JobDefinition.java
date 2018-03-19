package org.am061.java.camel.model;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

@Value
@Builder
public class JobDefinition {

    private LocalDate startDate;
    private LocalDate endDate;

    public static JobDefinition from(JobDefinition definition, LocalDate startDate, LocalDate endDate) {
        return JobDefinition.builder()
                .startDate(startDate)
                .endDate(endDate)
                .build();
    }
}
