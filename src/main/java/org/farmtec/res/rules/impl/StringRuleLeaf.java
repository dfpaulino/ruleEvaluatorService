package org.farmtec.res.rules.impl;

import com.fasterxml.jackson.databind.JsonNode;
import org.farmtec.res.enums.Operation;
import org.farmtec.res.predicate.factory.PredicateFactory;
import org.farmtec.res.rules.RuleComponent;
import org.immutables.value.Value;

import java.util.function.Predicate;

@Value.Immutable
public abstract class StringRuleLeaf implements RuleComponent {

    @Value.Parameter
    public abstract String getTag();

    @Value.Parameter
    public abstract Operation getOperation();

    @Value.Parameter
    public abstract String getValue();

    @Value.Parameter
    public abstract PredicateFactory<String> getPredicateFactory();

    private Predicate<String> predicate=null;

    @Override
    public boolean testRule(JsonNode jsonNode) {
        // create predicate if not created already. this is lazy load
        if (predicate == null) {
            this.predicate = this.getPredicateFactory().getPredicate(this.getOperation(), this.getValue());
        }

        boolean bool = false;
        if (jsonNode.has(this.getTag())) {
            bool = predicate.test(jsonNode.get(this.getTag()).asText());
        }
        return bool;
    }
}

