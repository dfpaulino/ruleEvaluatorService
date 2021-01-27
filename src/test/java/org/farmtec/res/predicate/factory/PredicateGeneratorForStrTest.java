package org.farmtec.res.predicate.factory;

import org.farmtec.res.enums.Operation;
import org.farmtec.res.predicate.factory.impl.PredicateGeneratorForStr;
import org.junit.jupiter.api.Test;

import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class PredicateGeneratorForStrTest {

    PredicateGenerator<String> predicateGenerator = new PredicateGeneratorForStr();

    @Test
    void getPredicate_equals() {
    //given
        //given
        Predicate<String> predicate = predicateGenerator.getPredicate(Operation.EQ, "String");

        assertAll(() -> assertThat(predicate.test("String")).isTrue(),
                () -> assertThat(predicate.test("someting Else")).isFalse(),
                () -> assertThat(predicate.test("StRiNg")).isFalse()
        );
    }

    @Test
    void getPredicate_notEquals() {
        //given
        //given
        Predicate<String> predicate = predicateGenerator.getPredicate(Operation.NEQ, "String");

        assertAll(() -> assertThat(predicate.test("String")).isFalse(),
                () -> assertThat(predicate.test("someting Else")).isTrue(),
                () -> assertThat(predicate.test("StRiNg")).isTrue()
        );
    }

    @Test
    void getPredicate_Contains() {
        //given
        //given
        //when
        Predicate<String> predicate = predicateGenerator.getPredicate(Operation.CONTAINS, "ing");

        //then
        assertAll(() -> assertThat(predicate.test("String")).isTrue(),
                () -> assertThat(predicate.test("somein what Else")).isFalse(),
                () -> assertThat(predicate.test("StRiNg")).isFalse()
        );
    }
}