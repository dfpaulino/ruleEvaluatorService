package org.farmtec.res.predicate.factory;

import org.farmtec.res.enums.Operation;
import org.farmtec.res.predicate.factory.impl.PredicateGeneratorForInt;
import org.junit.jupiter.api.Test;

import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class PredicateGeneratorForIntTest {

    PredicateFactory<Integer> predicateFactory = new PredicateGeneratorForInt();

    @Test
    void getPredicate_equals() {
        //given
        Predicate<Integer> predicate = predicateFactory.getPredicate(Operation.EQ, 5);

        assertAll(() -> assertThat(predicate.test(5)).isTrue(),
                () -> assertThat(predicate.test(6)).isFalse(),
                () -> assertThat(predicate.test(4)).isFalse()
        );
    }

    @Test
    void getPredicate_NotEquals() {
        //given
        Predicate<Integer> predicate = predicateFactory.getPredicate(Operation.NEQ, 5);

        assertAll(() -> assertThat(predicate.test(5)).isFalse(),
                () -> assertThat(predicate.test(6)).isTrue()
        );
    }

    @Test
    void getPredicate_GreaterThan() {
        //given
        Predicate<Integer> predicate = predicateFactory.getPredicate(Operation.GT, 5);

        assertAll(() -> assertThat(predicate.test(10)).isTrue(),
                () -> assertThat(predicate.test(4)).isFalse()
        );
    }
    @Test
    void getPredicate_GreaterEqualsThan() {
        //given
        Predicate<Integer> predicate = predicateFactory.getPredicate(Operation.GTE, 5);

        assertAll(() -> assertThat(predicate.test(10)).isTrue(),
                () -> assertThat(predicate.test(5)).isTrue(),
                () -> assertThat(predicate.test(4)).isFalse()
        );
    }
    @Test
    void getPredicate_LessThan() {
        //given
        Predicate<Integer> predicate = predicateFactory.getPredicate(Operation.LT, 5);

        assertAll(() -> assertThat(predicate.test(4)).isTrue(),
                () -> assertThat(predicate.test(5)).isFalse()
        );
    }
    @Test
    void getPredicate_LessEqualsThan() {
        //given
        Predicate<Integer> predicate = predicateFactory.getPredicate(Operation.LTE, 5);

        assertAll(() -> assertThat(predicate.test(4)).isTrue(),
                () -> assertThat(predicate.test(5)).isTrue(),
                () -> assertThat(predicate.test(6)).isFalse()
        );
    }

    @Test
    void getPredicate_Contais_shouldBeNull() {
        //given
        Predicate<Integer> predicate = predicateFactory.getPredicate(Operation.CONTAINS, 5);
        assertThat(predicate).isNull();
    }
}