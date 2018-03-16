package org.am061.java.vavr;

import io.vavr.collection.Seq;
import io.vavr.control.Validation;
import org.am061.java.vavr.validation.Person;
import org.am061.java.vavr.validation.PersonValidator;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/*
    Vavr brings the concept of Applicative Functor to Java from the functional programming world.
    Applicative Functor enables us to perform a sequence of actions while accumulating the results.

    A valid value is contained in a Validation.Valid instance,
    a list of validation errors is contained in a Validation.Invalid instance.
 */
public class ValidatorTest {

    @Test
    public void whenValidationWorks_thenCorrect() {
        PersonValidator personValidator = new PersonValidator();

        Validation<Seq<String>, Person> valid = personValidator.validatePerson("John Doe", 30);
        Validation<Seq<String>, Person> invalid = personValidator.validatePerson("John? Doe!4", -1);

        assertEquals("Valid(Person(name=John Doe, age=30))", valid.toString());
        assertEquals("Invalid(List(Invalid characters in name: ?!4, Age must be at least 0))", invalid.toString());
    }
}