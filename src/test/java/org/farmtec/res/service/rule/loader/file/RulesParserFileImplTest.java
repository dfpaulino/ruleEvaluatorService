package org.farmtec.res.service.rule.loader.file;

import org.farmtec.res.service.rule.loader.RulesParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by dp on 20/01/2021
 */
class RulesParserFileImplTest {

    // positive tests
    @Test
    void loadFile() {
        //given
        RulesParser rulesParser = new RulesParserFileImpl("src/test/resources/rules101.cfg");
        //when
        assertThat(rulesParser.loadFile()).isTrue();
    }

    @Test
    void getRuleLeafsDto() {
        //given
        RulesParser rulesParser = new RulesParserFileImpl("src/test/resources/rules101.cfg");
        //when
        rulesParser.loadFile();
        //then
        assertThat(rulesParser.getRuleLeafsDto().size()).isEqualTo(4);
        //pick any predicate and validate
        Assertions.assertAll(
                () -> assertThat(rulesParser.getRuleLeafsDto().get("P_1").getValue()).isEqualTo("18"),
                () -> assertThat(rulesParser.getRuleLeafsDto().get("P_1").getTag()).isEqualTo("age"),
                () -> assertThat(rulesParser.getRuleLeafsDto().get("P_1").getType()).isEqualTo("int"),
                () -> assertThat(rulesParser.getRuleLeafsDto().get("P_1").getOperation()).isEqualTo("GT")
        );

    }

    @Test
    void getGroupCompositesDto() {
        //given
        RulesParser rulesParser = new RulesParserFileImpl("src/test/resources/rules101.cfg");
        //when
        rulesParser.loadFile();
        //then
        assertThat(rulesParser.getGroupCompositesDto().size()).isEqualTo(3);
        // pick any group and validate
        Assertions.assertAll(
                () -> assertThat(rulesParser.getGroupCompositesDto().get("G11").getPredicateNames())
                        .containsExactlyInAnyOrder("P_1", "P_2"),
                () -> assertThat(rulesParser.getGroupCompositesDto().get("G11").getOperation())
                        .isEqualTo("AND")
        );
    }

    @Test
    void getRulesDto() {
        //given
        RulesParser rulesParser = new RulesParserFileImpl("src/test/resources/rules101.cfg");
        //when
        rulesParser.loadFile();
        //then
        assertThat(rulesParser.getRulesDto().size()).isEqualTo(2);
        // pick any Rule and validate
        Assertions.assertAll(
                () -> assertThat(rulesParser.getRulesDto().get("Rule_2").getPredicateName())
                        .isEqualTo("G12"),
                () -> assertThat(rulesParser.getRulesDto().get("Rule_2").getPriority())
                        .isEqualTo(1)
        );
    }

    //Negative Tests
    @Test
    void loadFile_notExists() {
        //given
        RulesParser rulesParser = new RulesParserFileImpl("/tmp/rules101.cfg");
        //when
        assertThat(rulesParser.loadFile()).isFalse();
        assertThat(rulesParser.getRuleLeafsDto().size()).isEqualTo(0);
        assertThat(rulesParser.getGroupCompositesDto().size()).isEqualTo(0);
        assertThat(rulesParser.getRuleLeafsDto().size()).isEqualTo(0);
    }

    @Test
    void loadFile_incompleteRules() {
        //given

        RulesParser rulesParser = new RulesParserFileImpl("src/test/resources/rules_incompleteGroups.cfg");
        //when
        assertThat(rulesParser.loadFile()).isTrue();
        assertThat(rulesParser.getRuleLeafsDto().size()).isEqualTo(3);
        assertThat(rulesParser.getGroupCompositesDto().size()).isEqualTo(2);
        assertThat(rulesParser.getRulesDto().size()).isEqualTo(2);
    }

    @Test
    void load2files_shouldHaveLastInfo() throws Exception {
        //given

        RulesParser rulesParser = new RulesParserFileImpl("src/test/resources/rules_incompleteGroups.cfg");
        //when
        assertThat(rulesParser.loadFile()).isTrue();
        assertThat(rulesParser.getRuleLeafsDto().size()).isEqualTo(3);
        assertThat(rulesParser.getGroupCompositesDto().size()).isEqualTo(2);
        assertThat(rulesParser.getRulesDto().size()).isEqualTo(2);

        //when
        ((RulesParserFileImpl) rulesParser).loadFile("src/test/resources/rules101.cfg");
        //then
        assertThat(rulesParser.getGroupCompositesDto().size()).isEqualTo(3);
        assertThat(rulesParser.getRuleLeafsDto().size()).isEqualTo(4);
        assertThat(rulesParser.getRulesDto().size()).isEqualTo(2);

    }
}