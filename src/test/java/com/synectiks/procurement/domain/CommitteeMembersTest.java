package com.synectiks.procurement.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.synectiks.procurement.web.rest.TestUtil;

public class CommitteeMembersTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CommitteeMembers.class);
        CommitteeMembers committeeMembers1 = new CommitteeMembers();
        committeeMembers1.setId(1L);
        CommitteeMembers committeeMembers2 = new CommitteeMembers();
        committeeMembers2.setId(committeeMembers1.getId());
        assertThat(committeeMembers1).isEqualTo(committeeMembers2);
        committeeMembers2.setId(2L);
        assertThat(committeeMembers1).isNotEqualTo(committeeMembers2);
        committeeMembers1.setId(null);
        assertThat(committeeMembers1).isNotEqualTo(committeeMembers2);
    }
}
