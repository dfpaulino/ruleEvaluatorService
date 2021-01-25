package org.farmtec.res.rules.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.farmtec.res.enums.Operation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.booleanThat;
import static org.mockito.Mockito.*;

/**
 * Created by dp on 25/01/2021
 */
class LongRuleLeafTest {

    private final static long VALUE = Long.MAX_VALUE - 1;
    private final static String jsonString = "{\"tag1\":\"predicate1\",\"tag2\":\"predicate2\",\"tagLong\":9999999999911}";
    @Mock
    Predicate<Long> predicateLongMock;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testRule() throws Exception {
        //given
        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = mapper.readTree(jsonString);

        when(predicateLongMock.test(anyLong())).thenReturn(true);

        LongRuleLeaf longRuleLeaf = ImmutableLongRuleLeaf.builder()
                .operation(Operation.LT)
                .type(Long.class)
                .tag("tagLong")
                .value(Long.MAX_VALUE)
                .predicate(predicateLongMock)
                .build();

        //when
        boolean result = longRuleLeaf.testRule(actualObj);

        //then
        assertThat(result).isTrue();
        verify(predicateLongMock, times(1)).test(any(Long.class));
    }

    @Test
    void testRule_whenTagNotPresent() throws Exception {
        //given
        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = mapper.readTree(jsonString);

        when(predicateLongMock.test(anyLong())).thenReturn(true);

        LongRuleLeaf longRuleLeaf = ImmutableLongRuleLeaf.builder()
                .operation(Operation.LT)
                .type(Long.class)
                .tag("tagLongNotThre")
                .value(Long.MAX_VALUE)
                .predicate(predicateLongMock)
                .build();

        //when
        boolean result = longRuleLeaf.testRule(actualObj);

        //then
        assertThat(result).isFalse();
        verify(predicateLongMock, times(0)).test(any(Long.class));
    }
}