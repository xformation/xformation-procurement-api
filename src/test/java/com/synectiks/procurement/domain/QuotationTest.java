package com.synectiks.procurement.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.synectiks.procurement.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class QuotationTest {

  @Test
  void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(Quotation.class);
    Quotation quotation1 = new Quotation();
    quotation1.setId(1L);
    Quotation quotation2 = new Quotation();
    quotation2.setId(quotation1.getId());
    assertThat(quotation1).isEqualTo(quotation2);
    quotation2.setId(2L);
    assertThat(quotation1).isNotEqualTo(quotation2);
    quotation1.setId(null);
    assertThat(quotation1).isNotEqualTo(quotation2);
  }
}
