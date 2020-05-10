package org.farmtec.res.service.model;

import com.fasterxml.jackson.databind.JsonNode;
import org.farmtec.res.rules.RuleComponent;
import org.immutables.value.Value;

/**
 * This is immutable, as the rule is loaded into th object
 * and will no change. The potential reload would create a new Rule Object
 * and replace the current.
 * <p>
 * Definition of a Rule
 * Its has  the definition of the rule RuleGroupComposite
 * Priority of the Rule
 * Action of the Rule
 */
@Value.Immutable
public abstract class Rule {

    @Value.Parameter
    public abstract int getName();

    @Value.Parameter
    public abstract RuleComponent getRuleGroupComposite();

    @Value.Parameter
    public abstract int getPriority();

    public boolean test(JsonNode jsonNode) {
        return this.getRuleGroupComposite().testRule(jsonNode);
    }
}
