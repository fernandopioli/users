package com.pioli.users.domain.base;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ValueObjectTest {

    static class TestValueObject extends ValueObject<String> {
        public TestValueObject(String value) {
            super(value);
        }
    }

    @Test
    void shouldBeEqualWhenValuesAreEqual() {
        TestValueObject obj1 = new TestValueObject("value");
        TestValueObject obj2 = new TestValueObject("value");

        assertEquals(obj1, obj2);
        assertEquals(obj1.hashCode(), obj2.hashCode());
    }

    @Test
    void shouldNotBeEqualWhenValuesAreDifferent() {
        TestValueObject obj1 = new TestValueObject("value1");
        TestValueObject obj2 = new TestValueObject("value2");

        assertNotEquals(obj1, obj2);
        assertNotEquals(obj1.hashCode(), obj2.hashCode());
    }

    @Test
    void shouldNotBeEqualToNullOrDifferentClass() {
        TestValueObject obj1 = new TestValueObject("value");

        assertNotEquals(obj1, null);
        assertNotEquals(obj1, "some string");
    }

    @Test
    void shouldReturnValue() {
        TestValueObject obj = new TestValueObject("value");
        assertEquals("value", obj.getValue());
    }

    @Test
    void shouldBeEqualToItself() {
        TestValueObject obj = new TestValueObject("value");

        assertEquals(obj, obj);
    }
} 