package org.farmtec.res.rules.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.farmtec.res.enums.LogicalOperation;

import org.farmtec.res.rules.RuleComponent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


class RuleGroupCompositeTest {

    private final static String VALUE = "predicate1";
    private final static String jsonString = "{\"tag1\":\"predicate1\",\"tag2\":\"predicate2\",\"tag3\":123}";

    @Mock
    RuleComponent ruleComponentMock1;
    @Mock
    RuleComponent ruleComponentMock2;
    @Mock
    RuleComponent ruleComponentMock3;
    @Mock
    RuleComponent ruleComponentMock4;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    void testRule_ruleListEqOne_AND_shouldBeTrue() throws Exception {
        //given
        Mockito.when(ruleComponentMock1.testRule(any(JsonNode.class))).thenReturn(true);
        Mockito.when(ruleComponentMock2.testRule(any(JsonNode.class))).thenReturn(true);
        Mockito.when(ruleComponentMock3.testRule(any(JsonNode.class))).thenReturn(true);
        List<RuleComponent> ruleComponentList = Arrays.asList(ruleComponentMock1);
        RuleComponent ruleComponent = ImmutableRuleGroupComposite.of(ruleComponentList, LogicalOperation.AND);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = mapper.readTree(jsonString);

        //when
        assertThat(ruleComponent.testRule(actualObj)).isTrue();
        verify(ruleComponentMock1, times(1)).testRule(any(JsonNode.class));
    }

    @Test
    void testRule_ruleListGte2_AND_shouldBeTrue() throws Exception {
        //given
        Mockito.when(ruleComponentMock1.testRule(any(JsonNode.class))).thenReturn(true);
        Mockito.when(ruleComponentMock2.testRule(any(JsonNode.class))).thenReturn(true);
        Mockito.when(ruleComponentMock3.testRule(any(JsonNode.class))).thenReturn(true);
        List<RuleComponent> ruleComponentList = Arrays.asList(ruleComponentMock1, ruleComponentMock2, ruleComponentMock3);
        RuleComponent ruleComponent = ImmutableRuleGroupComposite.of(ruleComponentList, LogicalOperation.AND);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = mapper.readTree(jsonString);

        //when
        assertThat(ruleComponent.testRule(actualObj)).isTrue();
        verify(ruleComponentMock1, times(1)).testRule(any(JsonNode.class));
        verify(ruleComponentMock2, times(1)).testRule(any(JsonNode.class));
        verify(ruleComponentMock3, times(1)).testRule(any(JsonNode.class));
    }

    @Test
    void testRule_ruleListGte2_AND_shouldBeFalse() throws Exception {
        //given
        Mockito.when(ruleComponentMock1.testRule(any(JsonNode.class))).thenReturn(true);
        Mockito.when(ruleComponentMock2.testRule(any(JsonNode.class))).thenReturn(true);
        Mockito.when(ruleComponentMock3.testRule(any(JsonNode.class))).thenReturn(false);
        List<RuleComponent> ruleComponentList = Arrays.asList(ruleComponentMock1, ruleComponentMock2, ruleComponentMock3);
        RuleComponent ruleComponent = ImmutableRuleGroupComposite.of(ruleComponentList, LogicalOperation.AND);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = mapper.readTree(jsonString);

        //when
        assertThat(ruleComponent.testRule(actualObj)).isFalse();
        verify(ruleComponentMock1, times(1)).testRule(any(JsonNode.class));
        verify(ruleComponentMock2, times(1)).testRule(any(JsonNode.class));
        verify(ruleComponentMock3, times(1)).testRule(any(JsonNode.class));
    }

    @Test
    void testRule_ruleListGte2_OR_shouldBeTrue() throws Exception {
        //given
        Mockito.when(ruleComponentMock1.testRule(any(JsonNode.class))).thenReturn(false);
        Mockito.when(ruleComponentMock2.testRule(any(JsonNode.class))).thenReturn(true);
        Mockito.when(ruleComponentMock3.testRule(any(JsonNode.class))).thenReturn(false);
        List<RuleComponent> ruleComponentList = Arrays.asList(ruleComponentMock1, ruleComponentMock2, ruleComponentMock3);
        RuleComponent ruleComponent = ImmutableRuleGroupComposite.of(ruleComponentList, LogicalOperation.OR);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = mapper.readTree(jsonString);

        //when
        assertThat(ruleComponent.testRule(actualObj)).isTrue();
        verify(ruleComponentMock1, times(1)).testRule(any(JsonNode.class));
        verify(ruleComponentMock2, times(1)).testRule(any(JsonNode.class));
        verify(ruleComponentMock3, times(1)).testRule(any(JsonNode.class));
    }

    @Test
    void testRule_ruleListGte2_OR_shouldBeFalse() throws Exception {
        //given
        Mockito.when(ruleComponentMock1.testRule(any(JsonNode.class))).thenReturn(false);
        Mockito.when(ruleComponentMock2.testRule(any(JsonNode.class))).thenReturn(false);
        Mockito.when(ruleComponentMock3.testRule(any(JsonNode.class))).thenReturn(false);
        List<RuleComponent> ruleComponentList = Arrays.asList(ruleComponentMock1, ruleComponentMock2, ruleComponentMock3);
        RuleComponent ruleComponent = ImmutableRuleGroupComposite.of(ruleComponentList, LogicalOperation.OR);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = mapper.readTree(jsonString);

        //when
        assertThat(ruleComponent.testRule(actualObj)).isFalse();
        verify(ruleComponentMock1, times(1)).testRule(any(JsonNode.class));
        verify(ruleComponentMock2, times(1)).testRule(any(JsonNode.class));
        verify(ruleComponentMock3, times(1)).testRule(any(JsonNode.class));
    }

    @Test
    void testRule_ruleNOT_shouldBeTrue() throws Exception {
        //given
        Mockito.when(ruleComponentMock1.testRule(any(JsonNode.class))).thenReturn(false);
        List<RuleComponent> ruleComponentList = Arrays.asList(ruleComponentMock1);
        RuleComponent ruleComponent = ImmutableRuleGroupComposite.of(ruleComponentList, LogicalOperation.NOT);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = mapper.readTree(jsonString);

        //when
        assertThat(ruleComponent.testRule(actualObj)).isTrue();
        verify(ruleComponentMock1, times(1)).testRule(any(JsonNode.class));
    }




    /*
     upto here, we only tested simple group's, ie, groups composed by leafs
    now we need to test groups composed by other group that are composed by leafs
    This is the composite pattern

    rule : (ruleComponentMock1 && ruleComponentMock2) && (ruleComponentMock3 || ruleComponentMock4)
    ruleComponentMockX are leafs!
    */
    @Test
    void testRule_groupOfGroupOfRules_shouldBeTrue() throws Exception {
        //given
        Mockito.when(ruleComponentMock1.testRule(any(JsonNode.class))).thenReturn(true);
        Mockito.when(ruleComponentMock2.testRule(any(JsonNode.class))).thenReturn(true);
        Mockito.when(ruleComponentMock3.testRule(any(JsonNode.class))).thenReturn(false);
        Mockito.when(ruleComponentMock4.testRule(any(JsonNode.class))).thenReturn(true);
        //leafs of childGroup1 of parent group1
        List<RuleComponent> ruleComponentList1 = Arrays.asList(ruleComponentMock1, ruleComponentMock2);
        RuleComponent group11 = ImmutableRuleGroupComposite.of(ruleComponentList1, LogicalOperation.AND);
        //leafs of childGroup2 of parent group1
        List<RuleComponent> ruleComponentList2 = Arrays.asList(ruleComponentMock3, ruleComponentMock4);
        RuleComponent group12 = ImmutableRuleGroupComposite.of(ruleComponentList2, LogicalOperation.OR);

        //Parent Group1, composed by chileGroup1 and childGroup2
        List<RuleComponent> ruleComponentList3 = Arrays.asList(group11, group12);
        RuleComponent group1 = ImmutableRuleGroupComposite.of(ruleComponentList3, LogicalOperation.AND);


        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = mapper.readTree(jsonString);

        //when
        assertThat(group1.testRule(actualObj)).isTrue();
        verify(ruleComponentMock1, times(1)).testRule(any(JsonNode.class));
        verify(ruleComponentMock2, times(1)).testRule(any(JsonNode.class));
        verify(ruleComponentMock3, times(1)).testRule(any(JsonNode.class));
        verify(ruleComponentMock4, times(1)).testRule(any(JsonNode.class));
    }
    @Test
    void testRule_groupOfGroupOfRules_shouldBeFalse() throws Exception {
        //given
        Mockito.when(ruleComponentMock1.testRule(any(JsonNode.class))).thenReturn(false);
        Mockito.when(ruleComponentMock2.testRule(any(JsonNode.class))).thenReturn(true);
        Mockito.when(ruleComponentMock3.testRule(any(JsonNode.class))).thenReturn(false);
        Mockito.when(ruleComponentMock4.testRule(any(JsonNode.class))).thenReturn(true);
        //leafs of childGroup1 of parent group1
        List<RuleComponent> ruleComponentList1 = Arrays.asList(ruleComponentMock1, ruleComponentMock2);
        RuleComponent group11 = ImmutableRuleGroupComposite.of(ruleComponentList1, LogicalOperation.AND);
        //leafs of childGroup2 of parent group1
        List<RuleComponent> ruleComponentList2 = Arrays.asList(ruleComponentMock3, ruleComponentMock4);
        RuleComponent group12 = ImmutableRuleGroupComposite.of(ruleComponentList2, LogicalOperation.OR);

        //Parent Group1, composed by chileGroup1 and childGroup2
        List<RuleComponent> ruleComponentList3 = Arrays.asList(group11, group12);
        RuleComponent group1 = ImmutableRuleGroupComposite.of(ruleComponentList3, LogicalOperation.AND);


        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = mapper.readTree(jsonString);

        //when
        assertThat(group1.testRule(actualObj)).isFalse();
    }

    @Test
    void testRule_groupOfGroupOfRules_NOT_shouldBeFalse() throws Exception {
        //given
        Mockito.when(ruleComponentMock1.testRule(any(JsonNode.class))).thenReturn(true);
        Mockito.when(ruleComponentMock2.testRule(any(JsonNode.class))).thenReturn(true);
        Mockito.when(ruleComponentMock3.testRule(any(JsonNode.class))).thenReturn(false);
        Mockito.when(ruleComponentMock4.testRule(any(JsonNode.class))).thenReturn(true);
        //leafs of childGroup1 of parent group1
        List<RuleComponent> ruleComponentList1 = Arrays.asList(ruleComponentMock1, ruleComponentMock2);
        RuleComponent group111 = ImmutableRuleGroupComposite.of(ruleComponentList1, LogicalOperation.AND);
        //leafs of childGroup2 of parent group1
        List<RuleComponent> ruleComponentList2 = Arrays.asList(ruleComponentMock3, ruleComponentMock4);
        RuleComponent group112 = ImmutableRuleGroupComposite.of(ruleComponentList2, LogicalOperation.OR);

        //Parent Group1, composed by chileGroup1 and childGroup2
        List<RuleComponent> ruleComponentList3 = Arrays.asList(group111, group112);
        RuleComponent group11 = ImmutableRuleGroupComposite.of(ruleComponentList3, LogicalOperation.AND);

        //
        List<RuleComponent> ruleComponentList4 = Arrays.asList(group11);
        RuleComponent group1 = ImmutableRuleGroupComposite.of(ruleComponentList4, LogicalOperation.NOT);


        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = mapper.readTree(jsonString);

        //when
        assertThat(group1.testRule(actualObj)).isFalse();
    }

}