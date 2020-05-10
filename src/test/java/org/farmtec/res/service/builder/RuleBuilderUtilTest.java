package org.farmtec.res.service.builder;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.farmtec.res.predicate.factory.PredicateFactory;
import org.farmtec.res.predicate.factory.impl.PredicateGeneratorForInt;
import org.farmtec.res.predicate.factory.impl.PredicateGeneratorForStr;
import org.farmtec.res.rules.RuleComponent;
import org.farmtec.res.service.exceptions.InvalidOperation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.farmtec.res.enums.Operation.*;

class RuleBuilderUtilTest {

    @Test
    public void RulePredicateBuilderTest_Integer_isTrue() throws Exception {

        String VALUE = String.valueOf(10);
        String jsonString = "{\"tag1\":\"predicate1\",\"tag2\":\"predicate2\",\"tag3\":10}";

        //given
        PredicateFactory<Integer> integerPredicateFactory = new PredicateGeneratorForInt();
        PredicateFactory<String> stringPredicateFactory = new PredicateGeneratorForStr();
        RuleComponent ruleComponent = RuleBuilderUtil.RulePredicateBuilder
                .newInstance(integerPredicateFactory, stringPredicateFactory)
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
    public void RulePredicateBuilderTest_String_isTrue() throws Exception {

        String VALUE = "pred";
        String jsonString = "{\"tag1\":\"predicate1\",\"tag2\":\"predicate2\",\"tag3\":10}";

        //given
        PredicateFactory<Integer> integerPredicateFactory = new PredicateGeneratorForInt();
        PredicateFactory<String> stringPredicateFactory = new PredicateGeneratorForStr();
        RuleComponent ruleComponent = RuleBuilderUtil.RulePredicateBuilder
                .newInstance(integerPredicateFactory, stringPredicateFactory)
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
                    .newInstance(integerPredicateFactory, stringPredicateFactory)
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
                    .newInstance(integerPredicateFactory, stringPredicateFactory)
                    .setType(Date.class)
                    .setTag("tag1")
                    .setOperation(GT)
                    .setValue(VALUE).build();

        });

    }

}