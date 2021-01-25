package org.farmtec.res.rules.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.farmtec.res.enums.Operation;
import org.farmtec.res.predicate.factory.PredicateFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class IntegerRuleLeafTest {

    private final static int VALUE = 10;
    private final static String jsonString="{\"tag1\":\"predicate1\",\"tag2\":\"predicate2\",\"tag3\":11}";
    @Mock
    Predicate<Integer> predicateIntegerMock;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testRule() throws Exception{

        //given
        Mockito.when(predicateIntegerMock.test(any(Integer.class))).thenReturn(true);
        IntegerRuleLeaf integerRuleLeaf = ImmutableIntegerRuleLeaf.of("tag3", Operation.GT, VALUE, Integer.class, predicateIntegerMock);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = mapper.readTree(jsonString);

        //when
        boolean result = integerRuleLeaf.testRule(actualObj);
        assertThat(result).isTrue();
        verify(predicateIntegerMock, times(1)).test(any(Integer.class));
    }

    @Test
    void testRule_UnkownTag_shouldBeFalseAndInvokeOnce() throws Exception {
        //given
        Mockito.when(predicateIntegerMock.test(any(Integer.class))).thenReturn(true);
        IntegerRuleLeaf integerRuleLeafRuleLeaf = ImmutableIntegerRuleLeaf
                .of("tag", Operation.EQ, VALUE, String.class, predicateIntegerMock);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = mapper.readTree(jsonString);

        //when
        boolean result = integerRuleLeafRuleLeaf.testRule(actualObj);
        assertThat(result).isFalse();
        verify(predicateIntegerMock, times(0)).test(any(Integer.class));
    }
}