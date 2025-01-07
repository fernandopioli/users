package com.pioli.users.domain.base;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AggregateTest {
    static class TestAggregate extends Aggregate {}

    @Test
    void shouldBeASubclassOfEntity() {
        Aggregate aggregate = new TestAggregate();

        // Verifica se Aggregate é uma subclasse de Entity
        assertTrue(aggregate instanceof Aggregate);
        assertTrue(aggregate instanceof Entity);
    }

    @Test
    void shouldInheritPropertiesFromEntity() {
        Aggregate aggregate = new TestAggregate();

        assertNotNull(aggregate.getId());
        assertNotNull(aggregate.getCreatedAt());
        assertNotNull(aggregate.getUpdatedAt());
        assertNull(aggregate.getDeletedAt());
    }
}
