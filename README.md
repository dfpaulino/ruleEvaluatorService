
[![CircleCI](https://circleci.com/gh/dfpaulino/ruleEvaluatorService/tree/master.svg?style=shield)](https://github.com/dfpaulino/ruleEvaluatorService/tree/master)

# Rule Evaluator Service
This is a simple lightweight library to validate rules.
Rules are simple predicates that are grouped by a logical operation ( AND or OR)


example:
(A>1 || B="string") && (C==3 && D Contains "spring")


# Implementation

## Design
The implementation is based on a well known design pattern : Composite pattern.
This pattern was chosen because, when building rules, we are actually creating a well defined tree,
where we have leafs (that are simple predicate) and composites, that are composed by N leafs, OR by other composites.
Looking at the example above, the rule can be broken into leafs and composites.

The Rule () can be broken into child rules as follows
1. childGroup1 => (A>1 || B="string") . This is composed by 2 leafs 
1. childGroup2 => (C==3 && D Contains "spring"). This is composed by 2 leafs
1. Rule => childGroup2 && childGroup1  this is composed by 2 composites.

Note that the A>1 and B="string" and so on are simple predicates, ie, leafs

### Supported types/Operations for predicates
1. Integers (EQ,NEQ,GT,LT,GTE,LTE)
1. Strings (EQ,NEQ,CONTAINS,NOT CONTAINS)
1. Long (EQ,NEQ,GT,LT,GTE,LTE)
1. LocalTime (EQ,NEQ,GT,LT,GTE,LTE)

Operation Enum|logical equivalent |String|Int/Long/LocalTime
--------------|-------------------|------|---
EQ | ==|yes | yes
NEQ | != |yes | yes
GT | > |no | yes
LT | < |no | yes
GTE | >= |no | yes
LTE | <= |no | yes
CONTAINS |.contains |yes | no
NOT CONTAINS|||yes | no


### Supported Logical Operations (between predicates)
1. AND
2. OR
3. XOR (future)
4. NOT
### future Supported types
1. Date
2. BigDecimal

# Rule Configuration

## Load rules from External source
Rules can be loaded from an external source eg: file, database.
For this you can use the provided `RuleLoaderService` implementation proper
`RulesParser` implementation.
Just implement a `RulesParser`, and inject it into the `RulesLoaderService`

To test the rule, create a criteria in JsonNode, and use the RuleLoaderService
to provide the list of configured rules
``` javascript
 String criteriaStr = "{\"age\":10,\"name\":\"cano\",\"car\":\"renault\",\"address\":\"Spain\"," +
                "\"weight\":181,\"surname\":\"Al Capone\"}";

        ObjectMapper mapper = new ObjectMapper();
        JsonNode criteria = mapper.readTree(criteriaStr);
        
List<Rule> applicableRules = ruleLoaderService.getRules().stream().filter(rule -> rule.test(criteria)).collect(Collectors.toList());
```

## Load rules from file
This library supports by default loading rules from a .cfg file.
Just inject the `RulesParserFileImpl` with a well defined file name
Configuration samples can be found in src/test/resources/*.cfg

##


## Configure rules via code
1st configure your leafs, AKA predicate.
For each type there is a predicate factory, that generates the supported operations for the given type

each type requires the equivalent type of predicate factory
StringRuleLeaf, requires predicateFactory\<String\>
IntegerRuleLeaf, requires predicateFactory\<Integer\>
There are already pre defined factories for each type. You can can inject your own factory if needed

Once the predicates/leafs are configured, you then configure the childGroups (composites)
This is done by instantiating RuleGroupComposite, and injecting the list of ruleLeafs and  the logical operation

(A>1 || B="string") && (C==3 && D Contains "spring")

From the example above we would have:
```javascript
 IntegerRuleLeaf p1 = ImmutableIntegerRuleLeaf.of("A", Operation.EQ, 1, predicateGeneratorForInt)
 StringRuleLeaf p2 = ImmutableStringRuleLeaf.of("B", Operation.EQ, "string", predicateGeneratorForStr)
 IntegerRuleLeaf p3 = ImmutableIntegerRuleLeaf.of("C", Operation.EQ, 3, predicateGeneratorForInt)
 StringRuleLeaf p4 = ImmutableStringRuleLeaf.of("D", Operation.CONTAINS, "spring", predicateGeneratorForStr)
 
 RuleComponent group11 = ImmutableRuleGroupComposite.of(Arrays.asList(p1,p2), LogicalOperation.OR);
 RuleComponent group11 = ImmutableRuleGroupComposite.of(Arrays.asList(p3,p4), LogicalOperation.AND);
 
 RuleComponent rule = ImmutableRuleGroupComposite.of(Arrays.asList(group11,group12), LogicalOperation.AND);

``` 
A Utility class is provided to ease the creation of Predicates and Rules

to create a base predicate
```javascript
RuleComponent ruleComponent = RuleBuilderUtil.RulePredicateBuilder
                .newInstance()
                .setType(Integer.class)
                .setTag("tag3")
                .setOperation(EQ)
                .setValue(VALUE).build();
```
to create a composite group
```javascript
RuleComponent ruleGroup12 = RuleBuilderUtil.RuleComponentBuilder
                .newInstance()
                .setLogicalOperation(LogicalOperation.AND)
                .setRuleComponentList(Arrays.asList(p3, p4)).build();

```
where px can be any ruleComponent

to create a Rule
```javascript
Rule rule = RuleBuilderUtil.RuleBuilder
                .newInstance()
                .setPriority(1)
                .setName("Rule-1")
                .setActions(new ArrayList<Action>())
                .setRuleComponent(ruleGroup12).build();
```

up to here, the rule is configured and ready to be tested against a JsonNode

### how to test the rule

The idea here is that a plain JsonNode object is injected so that its fed to the predicates.
example of a jsonNode as String representation:
*{"A"5,"B":"string","C":3,"D":"reaching spring"}* 

so the way to test would be the following:

```javascript
 private final static String jsonString = "{\"A\"5,\"B\":\"string\",\"C\":3,\"D\":\"reaching spring\"}";

        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = mapper.readTree(jsonString);

        //when
        assertThat(group1.testRule(actualObj)).isTrue();
```
Note that the quotes must be escaped to be json alike