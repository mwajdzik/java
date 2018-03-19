package org.am061.java.camel.services;

import org.am061.java.camel.model.JobDefinition;

import java.time.LocalDate;
import java.util.Iterator;

import static org.am061.java.camel.model.JobDefinition.from;

public class JobSplitterIteratorFactory {

    public Iterator<JobDefinition> createIterator(JobDefinition jobDefinition) {
        final LocalDate startDate = jobDefinition.getStartDate();
        final LocalDate endDate = jobDefinition.getEndDate();

        return new Iterator<JobDefinition>() {
            private LocalDate nextDate = startDate;

            @Override
            public boolean hasNext() {
                return !nextDate.isAfter(endDate);
            }

            @Override
            public JobDefinition next() {
                LocalDate date = nextDate;
                nextDate = nextDate.plusDays(1);
                return from(jobDefinition, date, date);
            }
        };
    }
}

