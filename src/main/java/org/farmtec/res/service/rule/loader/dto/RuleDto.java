package org.farmtec.res.service.rule.loader.dto;

import org.farmtec.res.rules.RuleComponent;
import org.farmtec.res.service.model.Action;
import org.immutables.value.Value;

import java.util.List;

/**
 * Created by dp on 20/01/2021
 */

public class RuleDto {

    private String predicateName;
    private int priority;
    private List<Action> actions;

    public boolean isComplete() {
        return predicateName != null && priority > 0;
    }

    public String getPredicateName() {
        return predicateName;
    }

    public void setPredicateName(String predicateName) {
        this.predicateName = predicateName;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public List<Action> getActions() {
        return actions;
    }

    public void setActions(List<Action> actions) {
        this.actions = actions;
    }

    @Override
    public String toString() {
        return "RuleDto{" +
                "predicateName='" + predicateName + '\'' +
                ", priority=" + priority +
                ", actions=" + actions +
                '}';
    }
}
