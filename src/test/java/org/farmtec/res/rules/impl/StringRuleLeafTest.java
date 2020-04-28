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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.assertj.core.api.Assertions.assertThat;


class StringRuleLeafTest {

    private final static String VALUE = "predicate1";
    private final static String jsonString="{\"tag1\":\"predicate1\",\"tag2\":\"predicate2\",\"tag3\":123}";
    @Mock
    PredicateFactory<String> predicateFactoryMock;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testRule_shouldBeTrueAndInvokeOnce() throws Exception{
        //given
        Mockito.when(predicateFactoryMock.getPredicate(any(Operation.class), anyString())).thenReturn((x) -> x.equals(VALUE));
        StringRuleLeaf stringRuleLeaf = ImmutableStringRuleLeaf.of("tag1", Operation.EQ, VALUE, predicateFactoryMock);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = mapper.readTree(jsonString);

        //when
        boolean result = stringRuleLeaf.testRule(actualObj);
        assertThat(result).isTrue();
        verify(predicateFactoryMock,times(1)).getPredicate(any(Operation.class),anyString());

        //verify that invoking the same leaf, the factory does nto get called again
        result = stringRuleLeaf.testRule(actualObj);
        verify(predicateFactoryMock,times(1)).getPredicate(any(Operation.class),anyString());
    }

    @Test
    void testRule_UnkownTag_shouldBeFalseAndInvokeOnce() throws Exception{
        //given
        Mockito.when(predicateFactoryMock.getPredicate(any(Operation.class), anyString())).thenReturn((x) -> x.equals(VALUE));
        StringRuleLeaf stringRuleLeaf = ImmutableStringRuleLeaf.of("tag", Operation.EQ, VALUE, predicateFactoryMock);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = mapper.readTree(jsonString);

        //when
        boolean result = stringRuleLeaf.testRule(actualObj);
        assertThat(result).isFalse();
        verify(predicateFactoryMock,times(1)).getPredicate(any(Operation.class),anyString());

        //verify that invoking the same leaf, the factory does nto get called again
        result = stringRuleLeaf.testRule(actualObj);
        verify(predicateFactoryMock,times(1)).getPredicate(any(Operation.class),anyString());
    }


}