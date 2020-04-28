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
    public abstract PredicateFactory<Integer> getPredicateFactory();

    private Predicate<Integer> predicate;


    @Override
    public boolean testRule(JsonNode jsonNode) {
        // create predicate if not created already. this is lazy load
        if (predicate == null) {
            this.predicate = this.getPredicateFactory().getPredicate(this.getOperation(), this.getValue());
        }
        boolean bool=false;
        if (jsonNode.has(this.getTag())) {
            //TODO vaalidate that the value is really integer?...or keep on going?
            bool = predicate.test(jsonNode.get(this.getTag()).asInt());
        }
        return bool;
    }
}