package org.farmtec.res.service.rule.loader.dto;


/**
 * Created by dp on 20/01/2021
 */

public class LeafDto {

    private String type;

    private String tag;

    private String value;

    private String operation;

    public boolean isComplete() {
        return (type != null && tag != null && value != null && operation != null);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    @Override
    public String toString() {
        return "LeafDto{" +
                "type='" + type + '\'' +
                ", tag='" + tag + '\'' +
                ", value='" + value + '\'' +
                ", operation='" + operation + '\'' +
                '}';
    }
}
