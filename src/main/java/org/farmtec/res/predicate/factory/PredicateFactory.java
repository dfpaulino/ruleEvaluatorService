package org.farmtec.res.predicate.factory;

import org.farmtec.res.enums.Operation;

import java.util.function.Predicate;

public  interface PredicateFactory<T> {
    public Predicate<T> getPredicate(Operation operation,T value);

}
