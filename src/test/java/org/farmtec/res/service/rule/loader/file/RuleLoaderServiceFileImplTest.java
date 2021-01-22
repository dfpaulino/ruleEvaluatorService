package org.farmtec.res.service.rule.loader.file;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.farmtec.res.service.model.Rule;
import org.farmtec.res.service.rule.loader.RuleLoaderService;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by dp on 21/01/2021
 */
class RuleLoaderServiceFileImplTest {

    @Test
    void getRules() throws Exception {

        //given
        RuleLoaderService ruleLoaderService = new RuleLoaderServiceFileImpl(
                new FileParserImpl("src/test/resources/rulesNestsGroups.cfg"));
        //when
        ruleLoaderService.refreshRules();
        while (!ruleLoaderService.isLoadRuleDone()) {
            Thread.sleep(200);
        }
        //then
        assertThat(ruleLoaderService.getRules().size()).isEqualTo(2);
    }

    @Test
    void refreshRules_Test1Rule1() throws Exception {

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

        RuleLoaderService ruleLoaderService = new RuleLoaderServiceFileImpl(
                new FileParserImpl("src/test/resources/rulesNestsGroups.cfg"));
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
    void refreshRules_Test1Rule_R1R2Apply() throws Exception {

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

        RuleLoaderService ruleLoaderService = new RuleLoaderServiceFileImpl(
                new FileParserImpl("src/test/resources/rulesNestsGroups.cfg"));
        //when
        ruleLoaderService.refreshRules();
        while (!ruleLoaderService.isLoadRuleDone()) {
            Thread.sleep(200);
        }

        List<Rule> applicableRules = ruleLoaderService.getRules().stream().filter(rule -> rule.test(criteria)).collect(Collectors.toList());
        System.out.println(applicableRules.size());
        assertThat(applicableRules.size()).isEqualTo(2);
        assertThat(applicableRules.stream().map(r -> r.getName()).collect(Collectors.toList()))
                .containsExactlyInAnyOrder("Rule_1", "Rule_2");
    }

    @Test
    void refreshRules_Test0RuleApply() throws Exception {

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

        RuleLoaderService ruleLoaderService = new RuleLoaderServiceFileImpl(
                new FileParserImpl("src/test/resources/rulesNestsGroups.cfg"));
        //when
        ruleLoaderService.refreshRules();
        while (!ruleLoaderService.isLoadRuleDone()) {
            Thread.sleep(200);
        }

        List<Rule> applicableRules = ruleLoaderService.getRules().stream().filter(rule -> rule.test(criteria)).collect(Collectors.toList());
        System.out.println(applicableRules.size());
        assertThat(applicableRules.size()).isEqualTo(0);
    }







}