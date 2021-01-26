package org.farmtec.res.predicate.factory.impl;

import org.farmtec.res.enums.Operation;
import org.farmtec.res.predicate.factory.PredicateFactory;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by dp on 26/01/2021
 */
class PredicateGeneratorForTimeTest {

    private static PredicateFactory<LocalTime> predicateFactory = new PredicateGeneratorForTime();
    private static LocalTime localTimeMatch = LocalTime.parse("21:00:00");

    @Test
    void getPredicate_equals() {
        //given
        Predicate<LocalTime> predicate = predicateFactory.getPredicate(Operation.EQ, localTimeMatch);

        assertAll(() -> assertThat(predicate.test(LocalTime.parse("21:00:00"))).isTrue(),
                () -> assertThat(predicate.test(LocalTime.parse("21:00:01"))).isFalse()
        );
    }

    @Test
    void getPredicate_NotEquals() {
        //given
        Predicate<LocalTime> predicate = predicateFactory.getPredicate(Operation.NEQ, localTimeMatch);

        assertAll(() -> assertThat(predicate.test(LocalTime.parse("20:00:00"))).isTrue(),
                () -> assertThat(predicate.test(LocalTime.parse("21:00:00"))).isFalse()
        );
    }

    @Test
    void getPredicate_GreaterThan() {
        //given
        Predicate<LocalTime> predicate = predicateFactory.getPredicate(Operation.GT, localTimeMatch);

        assertAll(() -> assertThat(predicate.test(LocalTime.parse("22:00:00"))).isTrue(),
                () -> assertThat(predicate.test(LocalTime.parse("20:00:00"))).isFalse()
        );
    }

    @Test
    void getPredicate_GreaterEqualsThan() {
        //given
        Predicate<LocalTime> predicate = predicateFactory.getPredicate(Operation.GTE, localTimeMatch);

        assertAll(() -> assertThat(predicate.test(LocalTime.parse("22:00:00"))).isTrue(),
                () -> assertThat(predicate.test(localTimeMatch)).isTrue(),
                () -> assertThat(predicate.test(LocalTime.parse("20:00:00"))).isFalse()
        );
    }

    @Test
    void getPredicate_LessThan() {
        //given
        Predicate<LocalTime> predicate = predicateFactory.getPredicate(Operation.LT, localTimeMatch);

        assertAll(() -> assertThat(predicate.test(LocalTime.parse("20:00:00"))).isTrue(),
                () -> assertThat(predicate.test(LocalTime.parse("22:00:00"))).isFalse()
        );
    }

    @Test
    void getPredicate_LessEqualsThan() {
        //given
        Predicate<LocalTime> predicate = predicateFactory.getPredicate(Operation.LTE, localTimeMatch);

        assertAll(() -> assertThat(predicate.test(LocalTime.parse("20:00:00"))).isTrue(),
                () -> assertThat(predicate.test(LocalTime.parse("21:00:00"))).isTrue(),
                () -> assertThat(predicate.test(LocalTime.parse("22:00:00"))).isFalse()
        );
    }

    @Test
    void getPredicate_Contais_shouldBeNull() {
        //given
        Predicate<LocalTime> predicate = predicateFactory.getPredicate(Operation.CONTAINS, localTimeMatch);
        assertThat(predicate).isNull();
    }
}