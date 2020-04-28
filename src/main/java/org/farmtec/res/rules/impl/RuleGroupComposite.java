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
        //TODO loop the components and test the operationreturn false;
        List<Boolean> booleanList=this.getRules().parallelStream().map((rule)->rule.testRule(jsonNode)).collect(Collectors.toList());
        boolean bool=false;
        if (booleanList.size()==1) {
            return booleanList.get(0);
        } else if (booleanList.size()>=2) {
            bool=this.getLogicalOperation().test(booleanList.get(0),booleanList.get(1));
            for (int i=2;i<booleanList.size();i++) {
                bool=this.getLogicalOperation().test(bool,booleanList.get(i));
            }
        }
        return bool;
    }
}
