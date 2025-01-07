package com.pioli.users;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.SpringApplication;

public class UsersApplicationTest {
    @Test
    void mainMethodShouldRunWithoutExceptions() {
        MockedStatic<SpringApplication> utilities = Mockito.mockStatic(SpringApplication.class);
        utilities.when((MockedStatic.Verification) SpringApplication.run(UsersApplication.class, new String[]{})).thenReturn(null);
        UsersApplication.main(new String[]{});
        assertThat(SpringApplication.run(UsersApplication.class, new String[]{})).isEqualTo(null);
    }
}
