package org.farmtec.res.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.farmtec.res.enums.LogicalOperation;
import org.farmtec.res.predicate.factory.PredicateFactory;
import org.farmtec.res.predicate.factory.impl.PredicateGeneratorForInt;
import org.farmtec.res.predicate.factory.impl.PredicateGeneratorForStr;
import org.farmtec.res.rules.RuleComponent;
import org.farmtec.res.service.builder.utils.RuleBuilderUtil;
import org.farmtec.res.service.model.ImmutableRule;
import org.farmtec.res.service.model.Rule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.farmtec.res.enums.Operation.*;
import static org.farmtec.res.enums.Operation.LT;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

class RuleServiceTest {

    private final static String jsonString = "{\"tag1\":\"predicate1\"," +
            "\"tag2\":\"predicate2\",\"tag3\":10,\"tag4\":1," +
            "\"tag5\":\"Hello World\",\"tag6\":1,\"tag7\":\"sunny day\",\"tag8\":20}";

    @Mock
    private Rule r1;
    @Mock
    private Rule r2;
    @Mock
    private Rule r3;
    @Mock
    private Rule r4;
    @Mock
    private Rule r5;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    void getRuleList() {
        //given
        Mockito.when(r1.test(any(JsonNode.class))).thenReturn(true);
        Mockito.when(r2.test(any(JsonNode.class))).thenReturn(false);
        Mockito.when(r3.test(any(JsonNode.class))).thenReturn(true);
        Mockito.when(r4.test(any(JsonNode.class))).thenReturn(false);
        List<Rule> ruleList = Arrays.asList(r1, r2, r3, r4);
        RuleService ruleService = new RuleService();
        ruleService.setRuleList(ruleList);
        //when
        assertThat(ruleService.getRuleList()).isNotNull();
        assertThat(ruleService.getRuleList()).isNotEmpty();
        //ensure object reference is different. The service create a unmodifiable list
        assertThat(ruleService.getRuleList()).isNotSameAs(ruleList);
        //but the contents are the same .equals()
        assertThat(ruleService.getRuleList()).isEqualTo(ruleList);
    }

    @Test
    void getRuleList_returnImmutableList() {
        //given
        Mockito.when(r1.test(any(JsonNode.class))).thenReturn(true);
        Mockito.when(r2.test(any(JsonNode.class))).thenReturn(false);
        Mockito.when(r3.test(any(JsonNode.class))).thenReturn(true);
        Mockito.when(r4.test(any(JsonNode.class))).thenReturn(false);
        List<Rule> ruleList = Arrays.asList(r1, r2, r3, r4);
        RuleService ruleService = new RuleService();
        ruleService.setRuleList(ruleList);
        //when
        List<Rule> rules = ruleService.getRuleList();
        Assertions.assertThrows(UnsupportedOperationException.class, () -> rules.add(r5));
    }

    @Test
    void getLastUpdate() throws Exception {
        //given
        Mockito.when(r1.test(any(JsonNode.class))).thenReturn(true);
        Mockito.when(r2.test(any(JsonNode.class))).thenReturn(false);
        Mockito.when(r3.test(any(JsonNode.class))).thenReturn(true);
        Mockito.when(r4.test(any(JsonNode.class))).thenReturn(false);
        List<Rule> ruleList = Arrays.asList(r1, r2, r3, r4);
        RuleService ruleService = new RuleService();
        ruleService.setRuleList(ruleList);
        //when
        Date d1 = ruleService.getLastUpdate();
        Thread.sleep(1000);
        ruleService.setRuleList(ruleList);
        Date d2 = ruleService.getLastUpdate();
        //then
        assertThat(d1).isNotNull();
        assertThat(d2).isAfter(d1);
    }

    @Test
    void test_Rule() throws Exception {
        //given
        Mockito.when(r1.test(any(JsonNode.class))).thenReturn(true);
        Mockito.when(r1.getName()).thenReturn("r1");
        Mockito.when(r2.test(any(JsonNode.class))).thenReturn(false);
        Mockito.when(r2.getName()).thenReturn("r2");
        Mockito.when(r3.test(any(JsonNode.class))).thenReturn(true);
        Mockito.when(r3.getName()).thenReturn("r3");
        Mockito.when(r4.test(any(JsonNode.class))).thenReturn(false);
        Mockito.when(r4.getName()).thenReturn("r4");
        List<Rule> ruleList = Arrays.asList(r1, r2, r3, r4);
        RuleService ruleService = new RuleService();
        ruleService.setRuleList(ruleList);

        //when
        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = mapper.readTree(jsonString);
        List<Rule> ruleMatched = ruleService.test(actualObj);

        //then
        assertThat(ruleMatched.size()).isEqualTo(2);
        List<String> ruleNameMatched = ruleMatched.stream().map(rule -> rule.getName()).collect(Collectors.toList());
        assertThat(ruleNameMatched).containsExactlyInAnyOrder("r1", "r3");
    }

    @Test
    public void test_rule2() throws Exception {
        //given
        RuleComponent rc1 = buildRuleGroup1();
        RuleComponent rc2 = buildRuleGroup2();
        Rule r1 = ImmutableRule.of("r1", rc1, 1, new ArrayList<>());
        Rule r2 = ImmutableRule.of("r2", rc2, 2, new ArrayList<>());
        RuleService ruleService = new RuleService();
        ruleService.setRuleList(Arrays.asList(r2, r1));
        //when
        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = mapper.readTree(jsonString);
        List<Rule> ruleList = ruleService.test(actualObj);
        //then
        assertThat(ruleList.size()).isEqualTo(2);
    }

    @Test
    public void test_rule3() throws Exception {
        //given
        RuleComponent rc1 = buildRuleGroup1();
        RuleComponent rc2 = buildRuleGroup2();
        Rule r1 = ImmutableRule.of("r1", rc1, 1, new ArrayList<>());
        Rule r2 = ImmutableRule.of("r2", rc2, 2, new ArrayList<>());
        RuleService ruleService = new RuleService();
        ruleService.setRuleList(Arrays.asList(r2, r1));
        //when
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.writeValue(new File("/tmp/rule.json"), ruleService.getRuleList());

    }

    private RuleComponent buildRuleGroup1() {
        //given
        //tag1 EQ predicate1
        PredicateFactory<Integer> integerPredicateFactory = new PredicateGeneratorForInt();
        PredicateFactory<String> stringPredicateFactory = new PredicateGeneratorForStr();

        String value1 = "predicate1";
        RuleComponent p1 = RuleBuilderUtil.RulePredicateBuilder
                .newInstance(integerPredicateFactory, stringPredicateFactory)
                .setType(String.class)
                .setTag("tag1")
                .setOperation(EQ)
                .setValue(value1).build();

        //tag3 > 5
        String value2 = String.valueOf(5);
        RuleComponent p2 = RuleBuilderUtil.RulePredicateBuilder
                .newInstance(integerPredicateFactory, stringPredicateFactory)
                .setType(Integer.class)
                .setTag("tag3")
                .setOperation(GT)
                .setValue(value2).build();

        // tag2 CONTAINS "pred"
        String value3 = "pred";
        RuleComponent p3 = RuleBuilderUtil.RulePredicateBuilder
                .newInstance(integerPredicateFactory, stringPredicateFactory)
                .setType(String.class)
                .setTag("tag2")
                .setOperation(CONTAINS)
                .setValue(value3).build();

        // tag4 < 2
        String value4 = String.valueOf(2);
        RuleComponent p4 = RuleBuilderUtil.RulePredicateBuilder
                .newInstance(integerPredicateFactory, stringPredicateFactory)
                .setType(Integer.class)
                .setTag("tag4")
                .setOperation(LT)
                .setValue(value4).build();


        //tag1 EQ predicate1 && tag3>5
        RuleComponent ruleGroup11 = RuleBuilderUtil.RuleComponentBuilder
                .newInstance()
                .setLogicalOperation(LogicalOperation.AND)
                .setRuleComponentList(Arrays.asList(p1, p2)).build();

        //tag2 CONTAINS "pred" && tag4 < 2
        RuleComponent ruleGroup12 = RuleBuilderUtil.RuleComponentBuilder
                .newInstance()
                .setLogicalOperation(LogicalOperation.AND)
                .setRuleComponentList(Arrays.asList(p3, p4)).build();

        RuleComponent ruleGroup1 = RuleBuilderUtil.RuleComponentBuilder
                .newInstance()
                .setLogicalOperation(LogicalOperation.OR)
                .setRuleComponentList(Arrays.asList(ruleGroup11, ruleGroup12)).build();

        return ruleGroup1;
    }

    private RuleComponent buildRuleGroup2() {
        //given
        //tag5 EQ predicate1
        PredicateFactory<Integer> integerPredicateFactory = new PredicateGeneratorForInt();
        PredicateFactory<String> stringPredicateFactory = new PredicateGeneratorForStr();

        String value1 = "Hello World";
        RuleComponent p1 = RuleBuilderUtil.RulePredicateBuilder
                .newInstance(integerPredicateFactory, stringPredicateFactory)
                .setType(String.class)
                .setTag("tag5")
                .setOperation(EQ)
                .setValue(value1).build();

        //tag6 > 0
        String value2 = String.valueOf(0);
        RuleComponent p2 = RuleBuilderUtil.RulePredicateBuilder
                .newInstance(integerPredicateFactory, stringPredicateFactory)
                .setType(Integer.class)
                .setTag("tag6")
                .setOperation(GT)
                .setValue(value2).build();

        // tag7 CONTAINS "pred"
        String value3 = "sunny";
        RuleComponent p3 = RuleBuilderUtil.RulePredicateBuilder
                .newInstance(integerPredicateFactory, stringPredicateFactory)
                .setType(String.class)
                .setTag("tag7")
                .setOperation(CONTAINS)
                .setValue(value3).build();

        // tag8 < 2
        String value4 = String.valueOf(25);
        RuleComponent p4 = RuleBuilderUtil.RulePredicateBuilder
                .newInstance(integerPredicateFactory, stringPredicateFactory)
                .setType(Integer.class)
                .setTag("tag8")
                .setOperation(LT)
                .setValue(value4).build();


        //tag5 EQ Hello World && tag6>0
        RuleComponent ruleGroup11 = RuleBuilderUtil.RuleComponentBuilder
                .newInstance()
                .setLogicalOperation(LogicalOperation.AND)
                .setRuleComponentList(Arrays.asList(p1, p2)).build();

        //tag7 CONTAINS "sunny" && tag8 < 25
        RuleComponent ruleGroup12 = RuleBuilderUtil.RuleComponentBuilder
                .newInstance()
                .setLogicalOperation(LogicalOperation.AND)
                .setRuleComponentList(Arrays.asList(p3, p4)).build();

        RuleComponent ruleGroup1 = RuleBuilderUtil.RuleComponentBuilder
                .newInstance()
                .setLogicalOperation(LogicalOperation.OR)
                .setRuleComponentList(Arrays.asList(ruleGroup11, ruleGroup12)).build();

        return ruleGroup1;
    }
}