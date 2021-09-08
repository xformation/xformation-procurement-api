package com.synectiks.procurement.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.synectiks.procurement.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RolesGroupTest {

  @Test
  void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(RolesGroup.class);
    RolesGroup rolesGroup1 = new RolesGroup();
    rolesGroup1.setId(1L);
    RolesGroup rolesGroup2 = new RolesGroup();
    rolesGroup2.setId(rolesGroup1.getId());
    assertThat(rolesGroup1).isEqualTo(rolesGroup2);
    rolesGroup2.setId(2L);
    assertThat(rolesGroup1).isNotEqualTo(rolesGroup2);
    rolesGroup1.setId(null);
    assertThat(rolesGroup1).isNotEqualTo(rolesGroup2);
  }
}
