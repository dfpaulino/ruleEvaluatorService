package org.farmtec.res.rules.impl;

import com.fasterxml.jackson.databind.JsonNode;
import org.farmtec.res.enums.LogicalOperation;
import org.farmtec.res.rules.RuleComponent;
import org.immutables.value.Value;

import java.util.List;
import java.util.stream.Collectors;

@Value.Immutable
public abstract class RuleGroupComposite implements RuleComponent {

    @Value.Parameter
    public abstract List<RuleComponent> getRules();
    @Value.Parameter
    public abstract LogicalOperation getLogicalOperation();

    @Override
    public boolean testRule(JsonNode jsonNode) {
        List<Boolean> booleanList = this.getRules().stream().map((rule) -> rule.testRule(jsonNode)).collect(Collectors.toList());
        boolean bool=false;

        if (booleanList.size()==1) {
            if (this.getLogicalOperation().equals(LogicalOperation.NOT)) {
                return !booleanList.get(0);
            }
            return booleanList.get(0);

        } else if (booleanList.size()>=2) {
            boolean identity = false;
            if(this.getLogicalOperation() == LogicalOperation.AND) {
                identity = true;
            }
            return booleanList.stream()
                .reduce(identity,
                    (totalBool,currentBool) ->this.getLogicalOperation().test(totalBool,currentBool)
                );
        }
        // if it gets here means list is empty...
        return bool;
    }
}
