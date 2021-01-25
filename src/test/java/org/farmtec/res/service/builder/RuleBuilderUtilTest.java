package org.farmtec.res.service.builder;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.farmtec.res.enums.LogicalOperation;
import org.farmtec.res.predicate.factory.PredicateFactory;
import org.farmtec.res.predicate.factory.impl.PredicateGeneratorForInt;
import org.farmtec.res.predicate.factory.impl.PredicateGeneratorForStr;
import org.farmtec.res.rules.RuleComponent;
import org.farmtec.res.service.builder.utils.RuleBuilderUtil;
import org.farmtec.res.service.exceptions.InvalidOperation;
import org.farmtec.res.service.model.Action;
import org.farmtec.res.service.model.Rule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.farmtec.res.enums.Operation.*;

class RuleBuilderUtilTest {

    @Test
    public void RulePredicateBuilderTest_Integer_isTrue() throws Exception {

        String VALUE = String.valueOf(10);
        String jsonString = "{\"tag1\":\"predicate1\",\"tag2\":\"predicate2\",\"tag3\":10}";

        //given
        RuleComponent ruleComponent = RuleBuilderUtil.RulePredicateBuilder
                .newInstance()
                .setType(Integer.class)
                .setTag("tag3")
                .setOperation(EQ)
                .setValue(VALUE).build();

        //when
        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = mapper.readTree(jsonString);
        boolean result = ruleComponent.testRule(actualObj);

        //then
        assertThat(result).isTrue();
    }

    @Test
    public void RulePredicateBuilderTest_Long_isTrue() throws Exception {

        String VALUE = String.valueOf(Long.MAX_VALUE);
        String jsonString = "{\"tag1\":\"predicate1\",\"tag2\":\"predicate2\",\"tag3\":9223372036854775807}";

        //given
        RuleComponent ruleComponent = RuleBuilderUtil.RulePredicateBuilder
                .newInstance()
                .setType(Long.class)
                .setTag("tag3")
                .setOperation(EQ)
                .setValue(VALUE).build();

        //when
        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = mapper.readTree(jsonString);
        boolean result = ruleComponent.testRule(actualObj);

        //then
        assertThat(result).isTrue();
    }

    @Test
    public void RulePredicateBuilderTest_String_isTrue() throws Exception {

        String VALUE = "pred";
        String jsonString = "{\"tag1\":\"predicate1\",\"tag2\":\"predicate2\",\"tag3\":10}";

        //given
        PredicateFactory<Integer> integerPredicateFactory = new PredicateGeneratorForInt();
        PredicateFactory<String> stringPredicateFactory = new PredicateGeneratorForStr();
        RuleComponent ruleComponent = RuleBuilderUtil.RulePredicateBuilder
                .newInstance()
                .setType(String.class)
                .setTag("tag1")
                .setOperation(CONTAINS)
                .setValue(VALUE).build();

        //when
        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = mapper.readTree(jsonString);
        boolean result = ruleComponent.testRule(actualObj);

        //then
        assertThat(result).isTrue();
    }

    @Test
    public void RulePredicateBuilderTest_String_WhenOperationNotDefinedInFactory() {

        String VALUE = "pred";
        String jsonString = "{\"tag1\":\"predicate1\",\"tag2\":\"predicate2\",\"tag3\":10}";

        //given
        PredicateFactory<Integer> integerPredicateFactory = new PredicateGeneratorForInt();
        PredicateFactory<String> stringPredicateFactory = new PredicateGeneratorForStr();

        Assertions.assertThrows(InvalidOperation.class, () -> {
            RuleBuilderUtil.RulePredicateBuilder
                    .newInstance()
                    .setType(String.class)
                    .setTag("tag1")
                    .setOperation(GT)
                    .setValue(VALUE).build();

        });
    }

    /**
     * To be removed when type is supported
     */
    @Test
    public void RulePredicateBuilderTest_WhenNotDefinedType() {

        String VALUE = "pred";
        String jsonString = "{\"tag1\":\"predicate1\",\"tag2\":\"predicate2\",\"tag3\":10}";

        //given
        PredicateFactory<Integer> integerPredicateFactory = new PredicateGeneratorForInt();
        PredicateFactory<String> stringPredicateFactory = new PredicateGeneratorForStr();

        Assertions.assertThrows(InvalidOperation.class, () -> {
            RuleBuilderUtil.RulePredicateBuilder
                    .newInstance()
                    .setType(Date.class)
                    .setTag("tag1")
                    .setOperation(GT)
                    .setValue(VALUE).build();

        });
    }

    @Test
    public void RuleComponentBuilder() throws Exception {

        String jsonString = "{\"tag1\":\"predicate1\",\"tag2\":\"predicate2\",\"tag3\":10},\"tag4\":5}";

        //given
        //tag1 EQ predicate1
        PredicateFactory<Integer> integerPredicateFactory = new PredicateGeneratorForInt();
        PredicateFactory<String> stringPredicateFactory = new PredicateGeneratorForStr();

        String value1 = "predicate1";
        RuleComponent p1 = RuleBuilderUtil.RulePredicateBuilder
                .newInstance()
                .setType(String.class)
                .setTag("tag1")
                .setOperation(EQ)
                .setValue(value1).build();

        //tag3 > 5
        String value2 = String.valueOf(5);
        RuleComponent p2 = RuleBuilderUtil.RulePredicateBuilder
                .newInstance()
                .setType(Integer.class)
                .setTag("tag3")
                .setOperation(GT)
                .setValue(value2).build();

        // tag2 CONTAINS "pred"
        String value3 = "pred";
        RuleComponent p3 = RuleBuilderUtil.RulePredicateBuilder
                .newInstance()
                .setType(String.class)
                .setTag("tag1")
                .setOperation(CONTAINS)
                .setValue(value3).build();

        // tag4 < 2
        String value4 = String.valueOf(2);
        RuleComponent p4 = RuleBuilderUtil.RulePredicateBuilder
                .newInstance()
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

        //(tag1 EQ predicate1 && tag3>5) || (tag2 CONTAINS "pred" && tag4 < 2)
        RuleComponent ruleGroup1 = RuleBuilderUtil.RuleComponentBuilder
                .newInstance()
                .setLogicalOperation(LogicalOperation.OR)
                .setRuleComponentList(Arrays.asList(ruleGroup11, ruleGroup12)).build();

        //when
        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = mapper.readTree(jsonString);
        boolean result = ruleGroup1.testRule(actualObj);

        assertThat(ruleGroup11.testRule(actualObj)).isTrue();
        assertThat(ruleGroup12.testRule(actualObj)).isFalse();
        assertThat(ruleGroup1.testRule(actualObj)).isTrue();
    }

    @Test
    public void RuleComponentBuilder_ruleFalse() throws Exception {

        //given
        String jsonString = "{\"tag1\":\"bla\",\"tag2\":\"predicate2\",\"tag3\":10},\"tag4\":5}";
        //(tag1 EQ predicate1 && tag3>5) || (tag2 CONTAINS "pred" && tag4 < 2)
        RuleComponent ruleGroup = buildRuleGroup();
        //when
        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = mapper.readTree(jsonString);
        assertThat(ruleGroup.testRule(actualObj)).isFalse();

    }

    private RuleComponent buildRuleGroup() {
        //given
        //tag1 EQ predicate1
        PredicateFactory<Integer> integerPredicateFactory = new PredicateGeneratorForInt();
        PredicateFactory<String> stringPredicateFactory = new PredicateGeneratorForStr();

        String value1 = "predicate1";
        RuleComponent p1 = RuleBuilderUtil.RulePredicateBuilder
                .newInstance()
                .setType(String.class)
                .setTag("tag1")
                .setOperation(EQ)
                .setValue(value1).build();

        //tag3 > 5
        String value2 = String.valueOf(5);
        RuleComponent p2 = RuleBuilderUtil.RulePredicateBuilder
                .newInstance()
                .setType(Integer.class)
                .setTag("tag3")
                .setOperation(GT)
                .setValue(value2).build();

        // tag2 CONTAINS "pred"
        String value3 = "pred";
        RuleComponent p3 = RuleBuilderUtil.RulePredicateBuilder
                .newInstance()
                .setType(String.class)
                .setTag("tag1")
                .setOperation(CONTAINS)
                .setValue(value3).build();

        // tag4 < 2
        String value4 = String.valueOf(2);
        RuleComponent p4 = RuleBuilderUtil.RulePredicateBuilder
                .newInstance()
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

    @Test
    public void RuleBuilderTest() throws Exception {
        String jsonString = "{\"tag1\":\"predicate1\",\"tag2\":\"predicate2\",\"tag3\":10},\"tag4\":5}";

        //given
        RuleComponent ruleComponent = this.buildRuleGroup();
        Rule rule = RuleBuilderUtil.RuleBuilder
                .newInstance()
                .setPriority(1)
                .setName("Rule-1")
                .setActions(new ArrayList<Action>())
                .setRuleComponent(ruleComponent).build();

        //when
        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = mapper.readTree(jsonString);
        boolean result = rule.test(actualObj);
        //then
        assertThat(result).isTrue();
    }

    @Test
    public void test() {
        Predicate<String> p1 = (s) -> {
            return s.contains("s");
        };
        Predicate<String> p2 = (s) -> {
            return 4 < s.length();
        };
        Predicate<String> p3 = (s) -> {
            return s.contains("g");
        };
        List<Predicate<String>> predicateList = Arrays.asList(p1, p2, p3);

        Predicate<String> pfinal = predicateList.stream().reduce(x -> true, Predicate::and);
        String testStr = "sunny Day";
        assertThat(pfinal.test(testStr)).isFalse();
        String testStr2 = "springFrameWork";
        assertThat(pfinal.test(testStr2)).isTrue();


    }
}