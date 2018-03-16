package org.am061.java.vavr.validation;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class Person {
    public final String name;
    public final int age;
}
