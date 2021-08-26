package org.farmtec.res.service.builder.utils;

import org.farmtec.res.enums.LogicalOperation;
import org.farmtec.res.enums.Operation;
import org.farmtec.res.predicate.factory.PredicateGenerator;
import org.farmtec.res.predicate.factory.PredicateGeneratorFactory;
import org.farmtec.res.predicate.factory.impl.*;
import org.farmtec.res.rules.RuleComponent;

import org.farmtec.res.rules.impl.ImmutableIntegerRuleLeaf;
import org.farmtec.res.rules.impl.ImmutableLongRuleLeaf;
import org.farmtec.res.rules.impl.ImmutableRuleGroupComposite;
import org.farmtec.res.rules.impl.ImmutableStringRuleLeaf;
import org.farmtec.res.rules.impl.ImmutableTimeRuleLeaf;
import org.farmtec.res.service.exceptions.InvalidOperation;
import org.farmtec.res.service.model.Action;
import org.farmtec.res.service.model.ImmutableRule;
import org.farmtec.res.service.model.Rule;

import java.time.LocalTime;
import java.util.List;


public class RuleBuilderUtil {

    /**
     * Builder for {@link Rule}.
     * Rule is a simple Object that contains the RuleComposite created by {@link RuleComponentBuilder}
     * Should set a Name, priority and a {@link org.farmtec.res.rules.impl.RuleGroupComposite}
     */
    public static class RuleBuilder {
        private String name;
        private int priority;
        private RuleComponent ruleComponent;
        private List<Action> actions;

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

        /**
         * @param ruleGroupComposite {@code RuleComponent} that is composed by? other {@code RuleGroupComposite}
         * @return {@code RuleBuilder}
         */
        public RuleBuilder setRuleComponent(RuleComponent ruleGroupComposite) {
            this.ruleComponent = ruleGroupComposite;
            return this;
        }

        public RuleBuilder setActions(List<Action> actions) {
            this.actions = actions;
            return this;
        }

        public Rule build() {
            return ImmutableRule.of(this.name, this.ruleComponent, new String() ,this.priority, this.actions);
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
         * @param ruleComponentList lis of {@code RuleComponent}
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
     * This will create predicate based on the {@code PredicateGenerator} injected
     */
    public static class RulePredicateBuilder {
        public static PredicateGeneratorFactory DEFAULT_PREDICATE_GENERATOR_FACTORY = new PredicateGeneratorFactoryImpl();

        private Operation operation;
        private String tag;
        private Class<?> type;
        private String value;
        private final PredicateGenerator<Integer> integerPredicateGenerator;
        private final PredicateGenerator<Long> longPredicateGenerator;
        private final PredicateGenerator<String> stringPredicateGenerator;
        private final PredicateGenerator<LocalTime> localTimePredicateGenerator;


        private RulePredicateBuilder(final PredicateGeneratorFactory predicateGeneratorFactory) {

            this.integerPredicateGenerator = predicateGeneratorFactory.getIntPredicateGenerator();
            this.longPredicateGenerator = predicateGeneratorFactory.getLongPredicateGenerator();
            this.stringPredicateGenerator = predicateGeneratorFactory.getStringPredicateGenerator();
            this.localTimePredicateGenerator = predicateGeneratorFactory.getTimePredicateGenerator();
        }


        /**
         * RulePredicateBuilder allows to create a base rule {@code RuleComponent}
         * Its required to inject {@code PredicateGenerator} for each type
         *
         * @param predicateGeneratorFactory of type {@link PredicateGeneratorFactory}
         * @return {@code RuleComponent}
         */
        public static RulePredicateBuilder newInstance(PredicateGeneratorFactory predicateGeneratorFactory) {
            return new RulePredicateBuilder(predicateGeneratorFactory);
        }

        /**
         * RulePredicateBuilder allows to create a base rule {@code RuleComponent}
         * Uses default Predicate Factories
         *
         * @return {@code RuleComponent}
         */
        public static RulePredicateBuilder newInstance() {
            return new RulePredicateBuilder(DEFAULT_PREDICATE_GENERATOR_FACTORY);
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
         * Ensure there is a {@code PredicateGenerator} for the relevant type
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

        public RuleComponent buildDefaultPredicate() {
            return ImmutableIntegerRuleLeaf.of("integer", Operation.EQ, 0, Integer.class, (i)->false);
        }
        public RuleComponent build() throws InvalidOperation, NumberFormatException {
            RuleComponent ruleComponent;
            //bit of validation

            if (this.type == Integer.class) {
                Integer valueInt = Integer.valueOf(this.value);
                if (null == integerPredicateGenerator.getPredicate(this.operation, valueInt)) {
                    String error = String.format("Operation Not supported for tag [%s] operation [%s] value [%s]", this.tag, this.operation.name(), this.value);
                    throw new InvalidOperation(error);
                }
                ruleComponent = ImmutableIntegerRuleLeaf.of(this.tag, this.operation, valueInt, Integer.class, integerPredicateGenerator.getPredicate(this.operation, valueInt));
            } else if (this.type == Long.class) {
                Long valueLong = Long.valueOf(this.value);
                if (null == longPredicateGenerator.getPredicate(this.operation, valueLong)) {
                    String error = String.format("Operation Not supported for tag [%s] operation [%s] value [%s]", this.tag, this.operation.name(), this.value);
                    throw new InvalidOperation(error);
                }
                ruleComponent = ImmutableLongRuleLeaf.of(this.tag, this.operation, valueLong, Integer.class, longPredicateGenerator.getPredicate(this.operation, valueLong));

            } else if (this.type == String.class) {
                if (null == stringPredicateGenerator.getPredicate(this.operation, this.value)) {
                    String error = String.format("Operation Not supported for tag [%s] operation [%s] value [%s]", this.tag, this.operation.name(), this.value);
                    throw new InvalidOperation(error);
                }
                ruleComponent = ImmutableStringRuleLeaf.of(this.tag, this.operation, this.value, String.class, stringPredicateGenerator.getPredicate(this.operation, this.value));
            } else if (this.type == LocalTime.class) {
                LocalTime lt = LocalTime.parse(this.value);
                if (null == localTimePredicateGenerator.getPredicate(this.operation, lt)) {
                    String error = String.format("Operation Not supported for tag [%s] operation [%s] value [%s]", this.tag, this.operation.name(), this.value);
                    throw new InvalidOperation(error);
                }
                ruleComponent = ImmutableTimeRuleLeaf.of(this.tag, this.operation, this.value, LocalTime.class, localTimePredicateGenerator.getPredicate(this.operation, lt));
            } else {
                String error = String.format("Type not Supported Predicate for tag [%s] operation [%s] value [%s]", this.tag, this.operation.name(), this.value);
                throw new InvalidOperation(error);
            }
            return ruleComponent;
        }
    }
}
