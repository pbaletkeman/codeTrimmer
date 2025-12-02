package com.codetrimmer;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.shell.Shell;

@TestConfiguration
public class TestShellConfig {
    @Bean
    @Primary
    public Shell shell() {
        // Return a Mockito mock shell to avoid interactive context
        return org.mockito.Mockito.mock(Shell.class);
    }
}
