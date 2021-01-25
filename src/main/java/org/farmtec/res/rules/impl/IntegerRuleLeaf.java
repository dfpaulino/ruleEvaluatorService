package org.farmtec.res.rules.impl;

import com.fasterxml.jackson.databind.JsonNode;
import org.farmtec.res.enums.Operation;
import org.farmtec.res.predicate.factory.PredicateFactory;
import org.farmtec.res.rules.RuleComponent;
import org.immutables.value.Value;

import java.util.function.Predicate;

@Value.Immutable
public abstract class IntegerRuleLeaf implements RuleComponent {
    @Value.Parameter
    public abstract String getTag();
    @Value.Parameter
    public abstract Operation getOperation();
    @Value.Parameter
    public abstract int getValue();

    @Value.Parameter
    public abstract Class<?> getType();

    @Value.Parameter
    public abstract Predicate<Integer> getPredicate();

    @Override
    public boolean testRule(JsonNode jsonNode) {
        boolean bool=false;
        if (jsonNode.has(this.getTag())) {
            //TODO validate that the value is really integer?...or keep on going?
            bool = this.getPredicate().test(jsonNode.get(this.getTag()).asInt());
        }
        return bool;
    }
}