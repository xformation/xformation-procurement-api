package com.synectiks.procurement.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.synectiks.procurement.web.rest.TestUtil;

public class CommitteeMembersStatusTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CommitteeMembersStatus.class);
        CommitteeMembersStatus committeeMembersStatus1 = new CommitteeMembersStatus();
        committeeMembersStatus1.setId(1L);
        CommitteeMembersStatus committeeMembersStatus2 = new CommitteeMembersStatus();
        committeeMembersStatus2.setId(committeeMembersStatus1.getId());
        assertThat(committeeMembersStatus1).isEqualTo(committeeMembersStatus2);
        committeeMembersStatus2.setId(2L);
        assertThat(committeeMembersStatus1).isNotEqualTo(committeeMembersStatus2);
        committeeMembersStatus1.setId(null);
        assertThat(committeeMembersStatus1).isNotEqualTo(committeeMembersStatus2);
    }
}
