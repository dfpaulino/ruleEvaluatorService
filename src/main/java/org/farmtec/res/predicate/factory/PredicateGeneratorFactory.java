package org.farmtec.res.predicate.factory;

import org.farmtec.res.predicate.factory.PredicateGenerator;

import java.time.LocalTime;

/**
 * Created by dp on 27/01/2021
 */
public interface PredicateGeneratorFactory {
    PredicateGenerator<Integer> getIntPredicateGenerator();

    PredicateGenerator<Long> getLongPredicateGenerator();

    PredicateGenerator<String> getStringPredicateGenerator();

    PredicateGenerator<LocalTime> getTimePredicateGenerator();
}
