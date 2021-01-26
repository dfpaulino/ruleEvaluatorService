package org.farmtec.res.rules.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.farmtec.res.enums.Operation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.DateTimeException;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by dp on 26/01/2021
 */
class TimeRuleLeafTest {
    private final static String VALUE = "21:00:00";
    private final static String jsonString = "{\"tag1\":\"21:30:00\",\"tag2\":\"predicate2\",\"tag3\":11}";
    @Mock
    Predicate<LocalTime> predicateLocalTimeMock;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testRule() throws Exception {

        //given
        Mockito.when(predicateLocalTimeMock.test(any(LocalTime.class))).thenReturn(true);
        TimeRuleLeaf timeRuleLeaf = ImmutableTimeRuleLeaf.of("tag1", Operation.GT, VALUE, LocalTime.class, predicateLocalTimeMock);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = mapper.readTree(jsonString);

        //when
        boolean result = timeRuleLeaf.testRule(actualObj);
        assertThat(result).isTrue();
        verify(predicateLocalTimeMock, times(1)).test(any(LocalTime.class));
    }

    @Test
    void testRule_UnkownTag_shouldBeFalseAndInvokeOnce() throws Exception {
        //given
        Mockito.when(predicateLocalTimeMock.test(any(LocalTime.class))).thenReturn(true);
        TimeRuleLeaf timeRuleLeaf = ImmutableTimeRuleLeaf
                .of("tag", Operation.EQ, VALUE, LocalTime.class, predicateLocalTimeMock);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = mapper.readTree(jsonString);

        //when
        boolean result = timeRuleLeaf.testRule(actualObj);
        assertThat(result).isFalse();
        verify(predicateLocalTimeMock, times(0)).test(any(LocalTime.class));
    }

    @Test
    void testRule_when_wrongFormat() throws Exception {
        //given
        Mockito.when(predicateLocalTimeMock.test(any(LocalTime.class))).thenReturn(true);
        TimeRuleLeaf timeRuleLeaf = ImmutableTimeRuleLeaf
                .of("tag3", Operation.EQ, VALUE, LocalTime.class, predicateLocalTimeMock);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = mapper.readTree(jsonString);

        //when
        boolean result = timeRuleLeaf.testRule(actualObj);
        assertThat(result).isFalse();
        verify(predicateLocalTimeMock, times(0)).test(any(LocalTime.class));
    }
}