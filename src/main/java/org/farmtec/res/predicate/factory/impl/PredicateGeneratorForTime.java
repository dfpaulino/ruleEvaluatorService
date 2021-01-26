package org.farmtec.res.predicate.factory.impl;

import org.farmtec.res.enums.Operation;
import org.farmtec.res.predicate.factory.PredicateFactory;

import java.time.LocalTime;
import java.util.function.Predicate;

public class PredicateGeneratorForTime implements PredicateFactory<LocalTime> {


    public Predicate<LocalTime> getPredicate(Operation operation, LocalTime value) {
        Predicate<LocalTime> predicate;
        switch (operation) {
            case EQ:
                predicate = (x) -> x.equals(value);
                break;
            case NEQ:
                predicate = (x) -> !x.equals(value);
                break;
            case GT:
                predicate = (x) -> x.isAfter(value);
                break;
            case LT:
                predicate = (x) -> x.isBefore(value);
                break;
            case GTE:
                predicate = (x) -> (x.isAfter(value) || x.equals(value));
                break;
            case LTE:
                predicate = (x) -> (x.isBefore(value) || x.equals(value));
                break;
            default:
                predicate = null;
        }
        return predicate;
    }
}
