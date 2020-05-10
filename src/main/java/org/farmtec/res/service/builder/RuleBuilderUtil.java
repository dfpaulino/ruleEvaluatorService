package org.farmtec.res.service.builder;

import com.fasterxml.jackson.core.JsonParser;
import org.farmtec.res.enums.LogicalOperation;
import org.farmtec.res.enums.Operation;
import org.farmtec.res.predicate.factory.PredicateFactory;
import org.farmtec.res.rules.RuleComponent;
import org.farmtec.res.rules.impl.ImmutableIntegerRuleLeaf;
import org.farmtec.res.rules.impl.ImmutableRuleGroupComposite;
import org.farmtec.res.rules.impl.ImmutableStringRuleLeaf;
import org.farmtec.res.rules.impl.RuleGroupComposite;
import org.farmtec.res.service.exceptions.InvalidOperation;
import org.farmtec.res.service.model.Rule;

import java.util.List;


public class RuleBuilderUtil {

    /**
     * Builder for {@code Rule}.
     * Rule is a simple Object that contains the RuleComposite created by {@code RuleComponentBuilder}
     * Should set a Name, priority and a {@code RuleGroupComposite}
     */
    public static class RuleBuilder {
        private String name;
        private int priority;
        private RuleComponent ruleComponent;

        private RuleBuilder() {
        }

        public static RuleBuilder newInstance() {
            return new RuleBuilder();
        }

        public RuleBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public RuleBuilder setPriority(int priority) {
            this.priority = priority;
            return this;
        }

        public RuleBuilder setRuleComponent(RuleGroupComposite ruleGroupComposite) {
            this.ruleComponent = ruleGroupComposite;
            return this;
        }

        public Rule build() {
            return null;
        }

    }

    /**
     * Builder for {@code RuleGroupComposite}.
     * RuleComposite is a group of rules, that can be base rules (created from {@code RulePredicateBuilder}) or
     * or other RuleComposites created by {@code RuleComponentBuilder}
     */
    public static class RuleComponentBuilder {

        private LogicalOperation logicalOperation;
        private List<RuleComponent> ruleComponentList;


        private RuleComponentBuilder() {
        }

        public static RuleComponentBuilder newInstance() {
            return new RuleComponentBuilder();
        }

        /**
         * set logical operation between all {@code RuleComponent}
         *
         * @param logicalOperation logical operation between {@code RuleComponent} defined in {@code LogicalOperation}
         * @return {@code RuleComponentBuilder}
         */
        public RuleComponentBuilder setLogicalOperation(LogicalOperation logicalOperation) {
            this.logicalOperation = logicalOperation;
            return this;
        }

        /**
         * Add {@code RuleComposite}, this can be either base rules {@code RuleComponent} or
         * composite group of base rules {@code RuleGroupComposite}
         *
         * @param ruleComponentList
         * @return {@code RuleComponentBuilder}
         */
        public RuleComponentBuilder setRuleComponentList(List<RuleComponent> ruleComponentList) {
            this.ruleComponentList = ruleComponentList;
            return this;
        }

        /**
         * @return {@code RuleGroupComposite}
         */
        public RuleComponent build() {
            return ImmutableRuleGroupComposite.of(this.ruleComponentList, this.logicalOperation);
        }
    }

    /**
     * Builder for RuleComponentLeaf. This is the base rule
     * This will create predicate based on the {@code PredicateFactory} injected
     */
    public static class RulePredicateBuilder {
        private Operation operation;
        private String tag;
        private Class<?> type;
        private String value;
        private PredicateFactory<Integer> integerPredicateFactory;
        private PredicateFactory<String> stringPredicateFactory;


        private RulePredicateBuilder(PredicateFactory<Integer> integerPredicateFactory,
                                     PredicateFactory<String> stringPredicateFactory) {
            this.integerPredicateFactory = integerPredicateFactory;
            this.stringPredicateFactory = stringPredicateFactory;
        }

        /**
         * RulePredicateBuilder allows to create a base rule {@code RuleComponent}
         * Its required to inject {@code PredicateFactory} for each type
         *
         * @param integerPredicateFactory of type {@code PredicateFactory<Integer>}
         * @param stringPredicateFactory  of type {@code PredicateFactory<String>}
         * @return {@code RuleComponent}
         */
        public static RulePredicateBuilder newInstance(PredicateFactory<Integer> integerPredicateFactory,
                                                       PredicateFactory<String> stringPredicateFactory) {
            return new RulePredicateBuilder(integerPredicateFactory, stringPredicateFactory);
        }

        /**
         * set the Operation
         *
         * @param operation {@code Operation}
         * @return {@code RulePredicateBuilder}
         */
        public RulePredicateBuilder setOperation(Operation operation) {
            this.operation = operation;
            return this;
        }

        /**
         * set the json parameter/tag to compare
         *
         * @param tag to search for
         * @return {@code RulePredicateBuilder}
         */
        public RulePredicateBuilder setTag(String tag) {
            this.tag = tag;
            return this;
        }

        /**
         * Set the Type of the variable eg String.class, Integer.class
         * Ensure there is a {@code PredicateFactory} for the relevant type
         *
         * @param type {@code class} type of variable
         * @return {@code RulePredicateBuilder}
         */
        public RulePredicateBuilder setType(Class<?> type) {
            this.type = type;
            return this;
        }

        /**
         * set the value to compare with
         *
         * @param value to comapre with
         * @return {@code RulePredicateBuilder}
         */
        public RulePredicateBuilder setValue(String value) {
            this.value = value;
            return this;
        }

        public RuleComponent build() throws InvalidOperation, NumberFormatException {
            RuleComponent ruleComponent;
            //bit of validation

            if (this.type == Integer.class) {
                int valueInt = Integer.valueOf(this.value);
                if (null == integerPredicateFactory.getPredicate(this.operation, valueInt)) {
                    String error = String.format("Operation Not supported for tag [%s] operation [%s] value [%s]", this.tag, this.operation.name(), this.value);
                    throw new InvalidOperation(error);
                }
                ruleComponent = ImmutableIntegerRuleLeaf.of(this.tag, this.operation, Integer.valueOf(this.value), integerPredicateFactory);
            } else if (this.type == String.class) {
                if (null == stringPredicateFactory.getPredicate(this.operation, this.value)) {
                    String error = String.format("Operation Not supported for tag [%s] operation [%s] value [%s]", this.tag, this.operation.name(), this.value);
                    throw new InvalidOperation(error);
                }
                ruleComponent = ImmutableStringRuleLeaf.of(this.tag, this.operation, this.value, stringPredicateFactory);
            } else {
                String error = String.format("Type not Supported Predicate for tag [%s] operation [%s] value [%s]", this.tag, this.operation.name(), this.value);
                throw new InvalidOperation(error);
            }
            return ruleComponent;
        }
    }
}
