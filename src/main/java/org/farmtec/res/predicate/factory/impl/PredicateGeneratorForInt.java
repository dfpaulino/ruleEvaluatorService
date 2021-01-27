package org.farmtec.res.predicate.factory.impl;

import org.farmtec.res.enums.Operation;
import org.farmtec.res.predicate.factory.PredicateGenerator;


import java.util.function.Predicate;

public class PredicateGeneratorForInt implements PredicateGenerator<Integer> {


    public Predicate<Integer> getPredicate(Operation operation, Integer value) {
        Predicate<Integer> predicate;
        switch (operation) {
            case EQ:
                predicate = (x) -> x.equals(value);
                break;
            case NEQ:
                predicate = (x) -> !x.equals(value);
                break;
            case GT:
                predicate = (x) -> x > value.intValue();
                break;
            case LT:
                predicate = (x) -> x < value.intValue();
                break;
            case GTE:
                predicate = (x) -> x.intValue() >= value.intValue();
                break;
            case LTE:
                predicate = (x) -> x.intValue() <= value.intValue();
                break;
            default:
                predicate = null;
        }
        return predicate;
    }
}
