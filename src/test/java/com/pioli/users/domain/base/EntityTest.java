package com.pioli.users.domain.base;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.MockedStatic;
import static org.mockito.Mockito.mockStatic;


public class EntityTest {
    static class TestEntity extends Entity {}

    private static final LocalDateTime FIXED_NOW = LocalDateTime.of(2025, 1, 5, 12, 0);
    private static final LocalDateTime UPDATED_NOW = LocalDateTime.of(2025, 1, 5, 13, 0);

    @Test
    void shouldCreateEntityWithUUIDAndDates() {
        Entity entity = new TestEntity();
        assertNotNull(entity.getId());
        assertNotNull(entity.getCreatedAt());
        assertNotNull(entity.getUpdatedAt());
        assertNull(entity.getDeletedAt());
    }

    @Test
    void shouldInitializeTimestampsCorrectly() {
        try (MockedStatic<LocalDateTime> mockedStatic = mockStatic(LocalDateTime.class)) {
            mockedStatic.when(LocalDateTime::now).thenReturn(FIXED_NOW);

            TestEntity entity = new TestEntity();

            assertEquals(FIXED_NOW, entity.getCreatedAt());
            assertEquals(FIXED_NOW, entity.getUpdatedAt());
            assertNull(entity.getDeletedAt());
        }
    }

    @Test
    void shouldUpdateTimestampWhenUpdateTimestampsIsCalled() {
        try (MockedStatic<LocalDateTime> mockedStatic = mockStatic(LocalDateTime.class)) {
            mockedStatic.when(LocalDateTime::now).thenReturn(FIXED_NOW);

            TestEntity entity = new TestEntity();

            mockedStatic.when(LocalDateTime::now).thenReturn(UPDATED_NOW);

            entity.updateTimestamps();

            assertEquals(FIXED_NOW, entity.getCreatedAt());
            assertEquals(UPDATED_NOW, entity.getUpdatedAt());
        }
    }

    @Test
    void shouldMarkEntityAsDeleted() {
        try (MockedStatic<LocalDateTime> mockedStatic = mockStatic(LocalDateTime.class)) {
            mockedStatic.when(LocalDateTime::now).thenReturn(FIXED_NOW);

            TestEntity entity = new TestEntity();

            entity.markAsDeleted();

            assertEquals(FIXED_NOW, entity.getDeletedAt());
            assertTrue(entity.isDeleted());
        }
    }

    @Test
    void shouldNotBeDeletedInitially() {
        Entity entity = new TestEntity();
        assertFalse(entity.isDeleted());
    }
}

