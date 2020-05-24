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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class IntegerRuleLeafTest {

    private final static int VALUE = 10;
    private final static String jsonString="{\"tag1\":\"predicate1\",\"tag2\":\"predicate2\",\"tag3\":11}";
    @Mock
    PredicateFactory<Integer> predicateFactoryMock;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }



    @Test
    void testRule() throws Exception{

        //given
        Mockito.when(predicateFactoryMock.getPredicate(any(Operation.class), anyInt())).thenReturn((x) -> x > VALUE );
        IntegerRuleLeaf integerRuleLeaf = ImmutableIntegerRuleLeaf.of("tag3", Operation.GT, VALUE, Integer.class, predicateFactoryMock);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = mapper.readTree(jsonString);

        //when
        boolean result = integerRuleLeaf.testRule(actualObj);
        assertThat(result).isTrue();
        verify(predicateFactoryMock,times(1)).getPredicate(any(Operation.class),anyInt());

        //verify that invoking the same leaf, the factory does nto get called again
        result = integerRuleLeaf.testRule(actualObj);
        verify(predicateFactoryMock,times(1)).getPredicate(any(Operation.class),anyInt());
    }

}