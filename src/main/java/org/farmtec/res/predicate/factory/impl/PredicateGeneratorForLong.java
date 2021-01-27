package org.farmtec.res.predicate.factory.impl;

import org.farmtec.res.enums.Operation;
import org.farmtec.res.predicate.factory.PredicateGenerator;

import java.util.function.Predicate;

public class PredicateGeneratorForLong implements PredicateGenerator<Long> {


    public Predicate<Long> getPredicate(Operation operation, Long value) {
        Predicate<Long> predicate;
        switch (operation) {
            case EQ:
                predicate = (x) -> x.equals(value);
                break;
            case NEQ:
                predicate = (x) -> !x.equals(value);
                break;
            case GT:
                predicate = (x) -> x > value;
                break;
            case LT:
                predicate = (x) -> x < value;
                break;
            case GTE:
                predicate = (x) -> x >= value;
                break;
            case LTE:
                predicate = (x) -> x <= value;
                break;
            default:
                predicate = null;
        }
        return predicate;
    }
}
