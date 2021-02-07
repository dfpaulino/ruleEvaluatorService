package org.farmtec.res.service.rule.loader;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.farmtec.res.service.model.Rule;
import org.farmtec.res.service.rule.loader.dto.GroupCompositeDto;
import org.farmtec.res.service.rule.loader.dto.LeafDto;
import org.farmtec.res.service.rule.loader.dto.RuleDto;
import org.farmtec.res.service.rule.loader.file.RulesParserFileImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * Created by dp on 23/01/2021
 */
class RuleLoaderServiceImplTest {

    @Mock
    RulesParser rulesParser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    void getRules_testPredicate() throws Exception {
        // Rule_1 ( (number1 EQ Long.Max) || (number2 GT 2 && string1 EQ myString) ) && string2 contains char
        // Rule 2 !(number2 GT 2 && string1 EQ myString)
        // Rule_3  (myTime>21:00:00  && myTime>22:00:00)

        //given
        Map<String, LeafDto> leafDtoMap = new HashMap<>();
        Map<String, GroupCompositeDto> groupCompositeDtoMap;
        Map<String, RuleDto> ruleDtoMap;

        setleafDtoMap(leafDtoMap);
        groupCompositeDtoMap = setgroupCompositeDtoMap();
        ruleDtoMap = setRuleDtoMap();

        when(rulesParser.getRuleLeafsDto()).thenReturn(leafDtoMap);
        when(rulesParser.getGroupCompositesDto()).thenReturn(groupCompositeDtoMap);
        when(rulesParser.getRulesDto()).thenReturn(ruleDtoMap);
        when(rulesParser.loadRules()).thenReturn(true);

        //when
        RuleLoaderService ruleLoaderService = new RuleLoaderServiceImpl(rulesParser);

        //then
        ruleLoaderService.refreshRules();

        //wait 3 sec to load the rules...
        Thread.sleep(3000);

        assertThat(ruleLoaderService.getRules().size()).isEqualTo(3);

        //test couple criterias
        String criteriaStr = "{\"number1\":9223372036854775807,\"number2\":\"1\",\"string1\":\"myString\",\"string2\":\"has a char\"}";

        ObjectMapper mapper = new ObjectMapper();
        JsonNode criteria1 = mapper.readTree(criteriaStr);

        //test each rule
        for (Rule r : ruleLoaderService.getRules()) {
            switch (r.getName()) {
                case "Rule_1":
                    assertThat(r.test(criteria1)).isTrue();
                    break;
                case "Rule_2":
                    assertThat(r.test(criteria1)).isTrue();
                    break;
                case "Rule_3":
                    assertThat(r.test(criteria1)).isFalse();
                    break;
            }
        }

        //criteria2
        criteriaStr = "{\"number1\":2,\"number2\":\"3\",\"string1\":\"myString\",\"string2\":\"has a char\",\"myTime\":\"22:30:00\"}";
        criteria1 = mapper.readTree(criteriaStr);
        for (Rule r : ruleLoaderService.getRules()) {
            switch (r.getName()) {
                case "Rule_1":
                    assertThat(r.test(criteria1)).isTrue();
                    break;
                case "Rule_2":
                    assertThat(r.test(criteria1)).isFalse();
                    break;
                case "Rule_3":
                    assertThat(r.test(criteria1)).isFalse();
                    break;
            }
        }

        //criteria3
        criteriaStr = "{\"number1\":2,\"number2\":\"3\",\"string1\":\"myString\",\"string2\":\"has a int\",\"myTime\":\"21:30:00\"}";
        criteria1 = mapper.readTree(criteriaStr);

        for (Rule r : ruleLoaderService.getRules()) {
            switch (r.getName()) {
                case "Rule_1":
                    assertThat(r.test(criteria1)).isFalse();
                    break;
                case "Rule_2":
                    assertThat(r.test(criteria1)).isFalse();
                    break;
                case "Rule_3":
                    assertThat(r.test(criteria1)).isTrue();
                    break;
            }
        }

    }


    /**
     * The tests bellow use the File RuleParser implementation for the sake of
     * reducing the amount of setup. just configure the file rulesNestsGroups.cfg
     */

    @Test
    void getRules_UsingFileParser() throws Exception {

        //given
        RuleLoaderService ruleLoaderService = new RuleLoaderServiceImpl(
                new RulesParserFileImpl("src/test/resources/rulesNestsGroups.cfg"));
        //when
        ruleLoaderService.refreshRules();
        while (!ruleLoaderService.isLoadRuleDone()) {
            Thread.sleep(200);
        }
        //then
        assertThat(ruleLoaderService.getRules().size()).isEqualTo(2);
    }

    @Test
    void refreshRules_UsingFileParser_Test1Rule1() throws Exception {

        //given
        //                   G1
        //               G11           ||      G12
        //      (G111    &&    G112)   || (P_3 && P4)
        //    (P_1&&P_2) && (P_5 & P6) || (P_3 && P_4)
        // ((age >18 && name contains joaquim) && (address == Mexico && car <> ford) )|| (surname == El Chapo && weight > 180)
        String criteriaStr = "{\"age\":32,\"name\":\"joaquim\",\"car\":\"dodge\",\"address\":\"Mexico\"," +
                "\"weight\":100,\"surname\":\"El Arturu\"}";

        ObjectMapper mapper = new ObjectMapper();
        JsonNode criteria = mapper.readTree(criteriaStr);

        RuleLoaderService ruleLoaderService = new RuleLoaderServiceImpl(
                new RulesParserFileImpl("src/test/resources/rulesNestsGroups.cfg"));
        //when
        ruleLoaderService.refreshRules();
        while (!ruleLoaderService.isLoadRuleDone()) {
            Thread.sleep(200);
        }

        List<Rule> applicableRules = ruleLoaderService.getRules().stream().filter(rule -> rule.test(criteria)).collect(Collectors.toList());
        System.out.println(applicableRules.size());
        assertThat(applicableRules.size()).isEqualTo(1);
        assertThat(applicableRules.get(0).getName()).isEqualTo("Rule_1");
    }

    @Test
    void refreshRules_UsingFileParser_Test1Rule_R1R2Apply() throws Exception {

        //given
        //     G2
        //     G12
        //     (P_3 || P4)
        //
        //  (weight GT 180 || surname EQ escobar)
        String criteriaStr = "{\"age\":10,\"name\":\"cano\",\"car\":\"renault\",\"address\":\"Spain\"," +
                "\"weight\":181,\"surname\":\"El Chapo\"}";

        ObjectMapper mapper = new ObjectMapper();
        JsonNode criteria = mapper.readTree(criteriaStr);

        RuleLoaderService ruleLoaderService = new RuleLoaderServiceImpl(
                new RulesParserFileImpl("src/test/resources/rulesNestsGroups.cfg"));
        //when
        ruleLoaderService.refreshRules();
        while (!ruleLoaderService.isLoadRuleDone()) {
            Thread.sleep(200);
        }

        List<Rule> applicableRules = ruleLoaderService.getRules().stream().filter(rule -> rule.test(criteria)).collect(Collectors.toList());
        System.out.println(applicableRules.size());
        assertThat(applicableRules.size()).isEqualTo(2);
        assertThat(applicableRules.stream().map(Rule::getName).collect(Collectors.toList()))
                .containsExactlyInAnyOrder("Rule_1", "Rule_2");
    }

    @Test
    void refreshRules_UsingFileParser_Test0RuleApply() throws Exception {

        //given
        //     G2
        //     G12
        //     (P_3 || P4)
        //
        //  (weight GT 180 || surname EQ escobar)
        String criteriaStr = "{\"age\":10,\"name\":\"cano\",\"car\":\"renault\",\"address\":\"Spain\"," +
                "\"weight\":181,\"surname\":\"Al Capone\"}";

        ObjectMapper mapper = new ObjectMapper();
        JsonNode criteria = mapper.readTree(criteriaStr);

        RuleLoaderService ruleLoaderService = new RuleLoaderServiceImpl(
                new RulesParserFileImpl("src/test/resources/rulesNestsGroups.cfg"));
        //when
        ruleLoaderService.refreshRules();
        while (!ruleLoaderService.isLoadRuleDone()) {
            Thread.sleep(200);
        }

        List<Rule> applicableRules = ruleLoaderService.getRules().stream().filter(rule -> rule.test(criteria)).collect(Collectors.toList());
        System.out.println(applicableRules.size());
        assertThat(applicableRules.size()).isEqualTo(0);
    }

    private void setleafDtoMap(Map<String, LeafDto> leafDtoMap) {
        LeafDto leafDto1 = new LeafDto();
        leafDto1.setType("long");
        leafDto1.setOperation("EQ");
        leafDto1.setTag("number1");
        leafDto1.setValue(String.valueOf(Long.MAX_VALUE));

        LeafDto leafDto2 = new LeafDto();
        leafDto2.setType("integer");
        leafDto2.setOperation("GT");
        leafDto2.setTag("number2");
        leafDto2.setValue("2");

        LeafDto leafDto3 = new LeafDto();
        leafDto3.setType("string");
        leafDto3.setOperation("EQ");
        leafDto3.setTag("string1");
        leafDto3.setValue("myString");

        LeafDto leafDto4 = new LeafDto();
        leafDto4.setType("string");
        leafDto4.setOperation("CONTAINS");
        leafDto4.setTag("string2");
        leafDto4.setValue("char");

        LeafDto leafDto5 = new LeafDto();
        leafDto5.setType("time");
        leafDto5.setOperation("GT");
        leafDto5.setTag("myTime");
        leafDto5.setValue("21:00:00");

        LeafDto leafDto6 = new LeafDto();
        leafDto6.setType("time");
        leafDto6.setOperation("LT");
        leafDto6.setTag("myTime");
        leafDto6.setValue("22:00:00");

        leafDtoMap.put("P1", leafDto1);
        leafDtoMap.put("P2", leafDto2);
        leafDtoMap.put("P3", leafDto3);
        leafDtoMap.put("P4", leafDto4);
        leafDtoMap.put("P5", leafDto5);
        leafDtoMap.put("P6", leafDto6);

    }


    private Map<String, GroupCompositeDto> setgroupCompositeDtoMap() {

        // p1 number1 =1
        // p2 number2 > 2
        // p3 string1 eq myString
        // p4 string2 contain char
        //         G11
        //  ( G111||   G112 )   && G2
        // ((p1) || (p2 and p3)) && P4
        // 9223372036854775807
        //( (number1 EQ Long.Max) || (number2 GT 2 && string1 EQ myString) ) && string2 contains char

        //G112
        GroupCompositeDto groupCompositeDto112 = new GroupCompositeDto();
        groupCompositeDto112.setOperation("AND");
        groupCompositeDto112.setPredicateNames(Arrays.asList("P2", "P3"));

        //G111
        GroupCompositeDto groupCompositeDto111 = new GroupCompositeDto();
        groupCompositeDto111.setOperation("AND");
        groupCompositeDto111.setPredicateNames(Collections.singletonList("P1"));

        //G2
        GroupCompositeDto groupCompositeDto2 = new GroupCompositeDto();
        groupCompositeDto2.setOperation("AND");
        groupCompositeDto2.setPredicateNames(Collections.singletonList("P4"));

        //G11
        GroupCompositeDto groupCompositeDto11 = new GroupCompositeDto();
        groupCompositeDto11.setOperation("OR");
        groupCompositeDto11.setPredicateNames(Arrays.asList("G111", "G112"));


        //G1
        GroupCompositeDto groupCompositeDto1 = new GroupCompositeDto();
        groupCompositeDto1.setOperation("AND");
        groupCompositeDto1.setPredicateNames(Arrays.asList("G11", "G2"));

        //G3
        GroupCompositeDto groupCompositeDto3 = new GroupCompositeDto();
        groupCompositeDto3.setOperation("NOT");
        groupCompositeDto3.setPredicateNames(Arrays.asList("G112"));

        //G4
        GroupCompositeDto groupCompositeDto4 = new GroupCompositeDto();
        groupCompositeDto4.setOperation("AND");
        groupCompositeDto4.setPredicateNames(Arrays.asList("P5", "P6"));


        Map<String, GroupCompositeDto> groupCompositeDtoMap = new HashMap<>();
        groupCompositeDtoMap.put("G112", groupCompositeDto112);
        groupCompositeDtoMap.put("G111", groupCompositeDto111);
        groupCompositeDtoMap.put("G11", groupCompositeDto11);
        groupCompositeDtoMap.put("G2", groupCompositeDto2);
        groupCompositeDtoMap.put("G1", groupCompositeDto1);
        groupCompositeDtoMap.put("G3", groupCompositeDto3);
        groupCompositeDtoMap.put("G4", groupCompositeDto4);


        return groupCompositeDtoMap;
    }

    private Map<String, RuleDto> setRuleDtoMap() {
        RuleDto ruleDto = new RuleDto();
        ruleDto.setPredicateName("G1");
        ruleDto.setActions(new ArrayList<>());
        ruleDto.setPriority(1);

        RuleDto ruleDto2 = new RuleDto();
        ruleDto2.setPredicateName("G3");
        ruleDto2.setActions(new ArrayList<>());
        ruleDto2.setPriority(1);

        RuleDto ruleDto3 = new RuleDto();
        ruleDto3.setPredicateName("G4");
        ruleDto3.setActions(new ArrayList<>());
        ruleDto3.setPriority(1);

        Map<String, RuleDto> ruleDtoMap = new HashMap<>();
        ruleDtoMap.put("Rule_1", ruleDto);
        ruleDtoMap.put("Rule_2", ruleDto2);
        ruleDtoMap.put("Rule_3", ruleDto3);

        return ruleDtoMap;
    }
}