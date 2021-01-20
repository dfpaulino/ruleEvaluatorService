package org.farmtec.res.service.rule.loader.file;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by dp on 20/01/2021
 */
class FileParserImplTest {

    // positive tests
    @Test
    void loadFile() {
        //given
        FileParser fileParser = new FileParserImpl("src/test/resources/rules101.cfg");
        //when
        assertThat(fileParser.loadFile()).isTrue();
    }

    @Test
    void getRuleLeafsDto() {
        //given
        FileParser fileParser = new FileParserImpl("src/test/resources/rules101.cfg");
        //when
        fileParser.loadFile();
        //then
        assertThat(fileParser.getRuleLeafsDto().size()).isEqualTo(4);
        //pick any predicate and validate
        Assertions.assertAll(
                () -> assertThat(fileParser.getRuleLeafsDto().get("P_1").getValue()).isEqualTo("18"),
                () -> assertThat(fileParser.getRuleLeafsDto().get("P_1").getTag()).isEqualTo("age"),
                () -> assertThat(fileParser.getRuleLeafsDto().get("P_1").getType()).isEqualTo("int"),
                () -> assertThat(fileParser.getRuleLeafsDto().get("P_1").getOperation()).isEqualTo("GT")
        );

    }

    @Test
    void getGroupCompositesDto() {
        //given
        FileParser fileParser = new FileParserImpl("src/test/resources/rules101.cfg");
        //when
        fileParser.loadFile();
        //then
        assertThat(fileParser.getGroupCompositesDto().size()).isEqualTo(3);
        // pick any group and validate
        Assertions.assertAll(
                () -> assertThat(fileParser.getGroupCompositesDto().get("G11").getPredicateNames())
                        .containsExactlyInAnyOrder("P_1", "P_2"),
                () -> assertThat(fileParser.getGroupCompositesDto().get("G11").getOperation())
                        .isEqualTo("AND")
        );
    }

    @Test
    void getRulesDto() {
        //given
        FileParser fileParser = new FileParserImpl("src/test/resources/rules101.cfg");
        //when
        fileParser.loadFile();
        //then
        assertThat(fileParser.getRulesDto().size()).isEqualTo(2);
        // pick any Rule and validate
        Assertions.assertAll(
                () -> assertThat(fileParser.getRulesDto().get("Rule_2").getPredicateName())
                        .isEqualTo("G12"),
                () -> assertThat(fileParser.getRulesDto().get("Rule_2").getPriority())
                        .isEqualTo(1)
        );
    }

    //Negative Tests
    @Test
    void loadFile_notExists() {
        //given
        FileParser fileParser = new FileParserImpl("/tmp/rules101.cfg");
        //when
        assertThat(fileParser.loadFile()).isFalse();
        assertThat(fileParser.getRuleLeafsDto().size()).isEqualTo(0);
        assertThat(fileParser.getGroupCompositesDto().size()).isEqualTo(0);
        assertThat(fileParser.getRuleLeafsDto().size()).isEqualTo(0);
    }

    @Test
    void loadFile_incompleteRules() {
        //given

        FileParser fileParser = new FileParserImpl("src/test/resources/rules_incompleteGroups.cfg");
        //when
        assertThat(fileParser.loadFile()).isTrue();
        assertThat(fileParser.getRuleLeafsDto().size()).isEqualTo(3);
        assertThat(fileParser.getGroupCompositesDto().size()).isEqualTo(2);
        assertThat(fileParser.getRulesDto().size()).isEqualTo(2);
    }

    @Test
    void load2files_shouldHaveLastInfo() throws Exception {
        //given

        FileParser fileParser = new FileParserImpl("src/test/resources/rules_incompleteGroups.cfg");
        //when
        assertThat(fileParser.loadFile()).isTrue();
        assertThat(fileParser.getRuleLeafsDto().size()).isEqualTo(3);
        assertThat(fileParser.getGroupCompositesDto().size()).isEqualTo(2);
        assertThat(fileParser.getRulesDto().size()).isEqualTo(2);

        //when
        ((FileParserImpl) fileParser).loadFile("src/test/resources/rules101.cfg");
        //then
        assertThat(fileParser.getGroupCompositesDto().size()).isEqualTo(3);
        assertThat(fileParser.getRuleLeafsDto().size()).isEqualTo(4);
        assertThat(fileParser.getRulesDto().size()).isEqualTo(2);

    }
}