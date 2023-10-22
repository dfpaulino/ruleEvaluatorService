package org.farmtec.res.service.rule.loader.dto;

import java.util.List;

/**
 * Created by dp on 20/01/2021
 */
public class GroupCompositeDto {
    private List<String> predicateNames;
    private String operation;

    public boolean isComplete() {
        return (predicateNames != null) && (operation != null);
    }

    public List<String> getPredicateNames() {
        return predicateNames;
    }

    public void setPredicateNames(List<String> predicateNames) {
        this.predicateNames = predicateNames;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    @Override
    public String toString() {
        return "GroupCompositeDto{" +
                "predicateNames=" + predicateNames +
                ", operation='" + operation + '\'' +
                '}';
    }
}
