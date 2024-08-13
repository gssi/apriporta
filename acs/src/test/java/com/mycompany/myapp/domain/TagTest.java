package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.EmployeeTestSamples.*;
import static com.mycompany.myapp.domain.TagTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TagTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Tag.class);
        Tag tag1 = getTagSample1();
        Tag tag2 = new Tag();
        assertThat(tag1).isNotEqualTo(tag2);

        tag2.setId(tag1.getId());
        assertThat(tag1).isEqualTo(tag2);

        tag2 = getTagSample2();
        assertThat(tag1).isNotEqualTo(tag2);
    }

    @Test
    void employeeTest() {
        Tag tag = getTagRandomSampleGenerator();
        Employee employeeBack = getEmployeeRandomSampleGenerator();

        tag.setEmployee(employeeBack);
        assertThat(tag.getEmployee()).isEqualTo(employeeBack);

        tag.employee(null);
        assertThat(tag.getEmployee()).isNull();
    }
}
