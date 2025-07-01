package com.test.library.main.config;

import static org.slf4j.MDC.*;

import org.springframework.core.task.TaskDecorator;
import java.util.Map;

public class MdcTaskDecorator implements TaskDecorator {
    @Override
    public Runnable decorate(Runnable runnable) {
        Map<String, String> contextMap = getCopyOfContextMap(); // Capture MDC from current thread
        return () -> {
            try {
                setContextMap(contextMap); // Set MDC in the async thread
                runnable.run();
            } finally {
                clear(); // Clear MDC after task completion
            }
        };
    }
}
