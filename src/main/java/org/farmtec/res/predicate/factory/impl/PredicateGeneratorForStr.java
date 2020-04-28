package org.farmtec.res.predicate.factory.impl;

import org.farmtec.res.enums.Operation;
import org.farmtec.res.predicate.factory.PredicateFactory;

import java.util.function.Predicate;

public class PredicateGeneratorForStr implements PredicateFactory<String> {
    @Override
    public Predicate<String> getPredicate(Operation operation, String value) {
        Predicate<String> predicate;
        switch (operation) {
            case EQ:
                predicate = (x) -> x.equals(value);
                break;
            case NEQ:
                predicate = (x) -> !x.equals(value);
                break;
            case CONTAINS:
                predicate = (x) -> x.contains(value);
                break;
            case NOT_CONTAINS:
                predicate = (x) -> !x.contains(value);
                break;
            default:
                predicate = null;
        }
        return predicate;
    }
}
