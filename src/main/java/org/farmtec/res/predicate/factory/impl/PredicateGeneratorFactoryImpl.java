package org.farmtec.res.predicate.factory.impl;

import org.farmtec.res.predicate.factory.PredicateGenerator;
import org.farmtec.res.predicate.factory.PredicateGeneratorFactory;

import java.time.LocalTime;

/**
 * Created by dp on 27/01/2021
 */
public class PredicateGeneratorFactoryImpl implements PredicateGeneratorFactory {

    private final PredicateGenerator<Integer> integerPredicateGenerator;
    private final PredicateGenerator<Long> longPredicateGenerator;
    private final PredicateGenerator<String> stringPredicateGenerator;
    private final PredicateGenerator<LocalTime> localTimePredicateGenerator;

    public PredicateGeneratorFactoryImpl(PredicateGenerator<Integer> integerPredicateGenerator,
                                         PredicateGenerator<Long> longPredicateGenerator,
                                         PredicateGenerator<String> stringPredicateGenerator,
                                         PredicateGenerator<LocalTime> localTimePredicateGenerator) {
        this.integerPredicateGenerator = integerPredicateGenerator;
        this.longPredicateGenerator = longPredicateGenerator;
        this.stringPredicateGenerator = stringPredicateGenerator;
        this.localTimePredicateGenerator = localTimePredicateGenerator;
    }

    public PredicateGeneratorFactoryImpl() {
        this.integerPredicateGenerator = new PredicateGeneratorForInt();
        this.longPredicateGenerator = new PredicateGeneratorForLong();
        this.stringPredicateGenerator = new PredicateGeneratorForStr();
        this.localTimePredicateGenerator = new PredicateGeneratorForTime();
    }

    @Override
    public PredicateGenerator<Integer> getIntPredicateGenerator() {
        return this.integerPredicateGenerator;
    }

    @Override
    public PredicateGenerator<Long> getLongPredicateGenerator() {
        return this.longPredicateGenerator;
    }

    @Override
    public PredicateGenerator<String> getStringPredicateGenerator() {
        return this.stringPredicateGenerator;
    }

    @Override
    public PredicateGenerator<LocalTime> getTimePredicateGenerator() {
        return this.localTimePredicateGenerator;
    }
}
