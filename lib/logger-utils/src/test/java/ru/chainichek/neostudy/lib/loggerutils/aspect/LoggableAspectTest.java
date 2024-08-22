package ru.chainichek.neostudy.lib.loggerutils.aspect;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import ru.chainichek.neostudy.lib.loggerutils.annotation.Loggable;
import ru.chainichek.neostudy.lib.loggerutils.annotation.TransactionLoggable;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LoggableAspectTest {

    LoggableAspect loggableAspect;
    ProceedingJoinPoint joinPoint;
    ListAppender<ILoggingEvent> listAppender;

    SampleService sampleService = new SampleService();

    @BeforeEach
    void setUp() {
        loggableAspect = new LoggableAspect();
        joinPoint = mock(ProceedingJoinPoint.class);

        // Set up ListAppender for capturing log messages
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        listAppender = new ListAppender<>();
        listAppender.setContext(loggerContext);
        listAppender.start();

        ch.qos.logback.classic.Logger logger = loggerContext.getLogger(SampleService.class);
        logger.setLevel(ch.qos.logback.classic.Level.DEBUG);
        logger.addAppender(listAppender);
    }

    @Test
    void logAround_whenNoArgs_thenLogsMethodExecution() throws Throwable {
        Method method = SampleService.class.getMethod("noArgsMethod");
        MethodSignature signature = mock(MethodSignature.class);
        when(signature.getMethod()).thenReturn(method);
        when(joinPoint.getSignature()).thenReturn(signature);
        when(joinPoint.getTarget()).thenReturn(sampleService);
        when(joinPoint.getArgs()).thenReturn(new Object[]{});

        when(joinPoint.proceed()).thenReturn(null);

        loggableAspect.logAround(joinPoint);

        // Check the captured logs
        listAppender.stop();
        var logs = listAppender.list;
        assertFalse(logs.isEmpty());
    }

    @Test
    void logAround_whenWithArgs_thenLogsMethodExecution() throws Throwable {
        Method method = SampleService.class.getMethod("methodWithArgs", String.class);
        MethodSignature signature = mock(MethodSignature.class);
        when(signature.getMethod()).thenReturn(method);
        when(joinPoint.getSignature()).thenReturn(signature);
        when(joinPoint.getTarget()).thenReturn(sampleService);
        when(joinPoint.getArgs()).thenReturn(new Object[]{"arg1"});

        Loggable loggable = mock(Loggable.class);
        when(loggable.value()).thenReturn(Level.INFO);
        when(loggable.invokeMessage()).thenReturn("Invoked method with args");
        when(loggable.invokeParametersMessage()).thenReturn("arg1");
        when(loggable.executeMessage()).thenReturn("Executed method with args");
        when(loggable.executeParametersMessage()).thenReturn("arg1");

        when(joinPoint.proceed()).thenReturn("result");

        loggableAspect.logAround(joinPoint);

        // Check the captured logs
        listAppender.stop();
        var logs = listAppender.list;
        assertFalse(logs.isEmpty());
    }

    @Test
    void logAround_whenNoLoggableAnnotation_thenDoesNotLog() throws Throwable {
        Method method = SampleService.class.getMethod("noLoggableMethod");
        MethodSignature signature = mock(MethodSignature.class);
        when(signature.getMethod()).thenReturn(method);
        when(joinPoint.getSignature()).thenReturn(signature);
        when(joinPoint.getTarget()).thenReturn(sampleService);

        when(joinPoint.proceed()).thenReturn(null);

        loggableAspect.logAround(joinPoint);

        // Check that no logs were captured
        listAppender.stop();
        var logs = listAppender.list;
        assertTrue(logs.isEmpty());
    }

    @Test
    void getEffectiveLoggable() throws NoSuchMethodException {
        Method method = SampleService.class.getMethod("transactionMethod");
        Loggable loggable = loggableAspect.getEffectiveLoggable(method, SampleService.class);
        assertNotNull(loggable);
    }

    @Test
    void getMethodName() throws NoSuchMethodException {
        Method method = SampleService.class.getMethod("methodWithArgs", String.class);
        String methodName = loggableAspect.getMethodName(method);
        assertEquals("method with args", methodName);
    }

    // Sample service class for testing
    static class SampleService {

        @Loggable
        public void noArgsMethod() {
        }

        @Loggable(value = Level.DEBUG)
        public void methodWithArgs(String arg) {
        }

        public void noLoggableMethod() {
        }

        @TransactionLoggable
        public void transactionMethod() {
        }
    }
}