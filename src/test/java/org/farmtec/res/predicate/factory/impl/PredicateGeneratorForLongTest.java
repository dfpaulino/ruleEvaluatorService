package org.farmtec.res.predicate.factory.impl;

import org.farmtec.res.enums.Operation;
import org.farmtec.res.predicate.factory.PredicateFactory;
import org.junit.jupiter.api.Test;

import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by dp on 25/01/2021
 */
class PredicateGeneratorForLongTest {

    PredicateFactory<Long> predicateFactory = new PredicateGeneratorForLong();

    @Test
    void getPredicate_equals() {
        //given
        Predicate<Long> predicate = predicateFactory.getPredicate(Operation.EQ, Long.MAX_VALUE - 2);

        assertAll(() -> assertThat(predicate.test(Long.MAX_VALUE - 2)).isTrue(),
                () -> assertThat(predicate.test(Long.MAX_VALUE)).isFalse(),
                () -> assertThat(predicate.test(Long.MAX_VALUE - 1)).isFalse()
        );
    }

    @Test
    void getPredicate_NotEquals() {
        //given
        Predicate<Long> predicate = predicateFactory.getPredicate(Operation.NEQ, Long.MAX_VALUE - 2);

        assertAll(() -> assertThat(predicate.test(Long.MAX_VALUE - 2)).isFalse(),
                () -> assertThat(predicate.test(Long.MAX_VALUE)).isTrue()
        );
    }

    @Test
    void getPredicate_GreaterThan() {
        //given
        Predicate<Long> predicate = predicateFactory.getPredicate(Operation.GT, Long.MAX_VALUE - 2);

        assertAll(() -> assertThat(predicate.test(Long.MAX_VALUE - 1)).isTrue(),
                () -> assertThat(predicate.test(Long.MAX_VALUE - 2)).isFalse()
        );
    }

    @Test
    void getPredicate_GreaterEqualsThan() {
        //given
        Predicate<Long> predicate = predicateFactory.getPredicate(Operation.GTE, Long.MAX_VALUE - 2);

        assertAll(() -> assertThat(predicate.test(Long.MAX_VALUE - 2)).isTrue(),
                () -> assertThat(predicate.test(Long.MAX_VALUE)).isTrue(),
                () -> assertThat(predicate.test(Long.MAX_VALUE - 3)).isFalse()
        );
    }

    @Test
    void getPredicate_LessThan() {
        //given
        Predicate<Long> predicate = predicateFactory.getPredicate(Operation.LT, Long.MAX_VALUE - 2);

        assertAll(() -> assertThat(predicate.test(Long.MAX_VALUE - 3)).isTrue(),
                () -> assertThat(predicate.test(Long.MAX_VALUE)).isFalse()
        );
    }

    @Test
    void getPredicate_LessEqualsThan() {
        //given
        Predicate<Long> predicate = predicateFactory.getPredicate(Operation.LTE, Long.MAX_VALUE - 2);

        assertAll(() -> assertThat(predicate.test(Long.MAX_VALUE - 2)).isTrue(),
                () -> assertThat(predicate.test(Long.MAX_VALUE - 3)).isTrue(),
                () -> assertThat(predicate.test(Long.MAX_VALUE - 1)).isFalse()
        );
    }

    @Test
    void getPredicate_Contais_shouldBeNull() {
        //given
        Predicate<Long> predicate = predicateFactory.getPredicate(Operation.CONTAINS, Long.MAX_VALUE - 2);
        assertThat(predicate).isNull();
    }
}