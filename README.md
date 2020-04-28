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

Operation Enum|logical equivalent |String|Int
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
1. OR
1. XOR (future)

### future Supported types
1. DateTime 
1. Date
1. Long



### configure rules
1st configure your leafs, AKA predicate.
For each type there is a predicate factory, that generates the supported operations for the given type

each type requires the equivalent type of predicate factory
StringRuleLeaf, requires predicateFactory<String>
IntegerRuleLeaf, requires predicateFactory<Integer>
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