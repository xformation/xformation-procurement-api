package com.synectiks.procurement.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.synectiks.procurement.web.rest.TestUtil;

public class RulesTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Rules.class);
        Rules rules1 = new Rules();
        rules1.setId(1L);
        Rules rules2 = new Rules();
        rules2.setId(rules1.getId());
        assertThat(rules1).isEqualTo(rules2);
        rules2.setId(2L);
        assertThat(rules1).isNotEqualTo(rules2);
        rules1.setId(null);
        assertThat(rules1).isNotEqualTo(rules2);
    }
}
