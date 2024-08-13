package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.AccessRuleTestSamples.*;
import static com.mycompany.myapp.domain.EmployeeTestSamples.*;
import static com.mycompany.myapp.domain.TagTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class EmployeeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Employee.class);
        Employee employee1 = getEmployeeSample1();
        Employee employee2 = new Employee();
        assertThat(employee1).isNotEqualTo(employee2);

        employee2.setId(employee1.getId());
        assertThat(employee1).isEqualTo(employee2);

        employee2 = getEmployeeSample2();
        assertThat(employee1).isNotEqualTo(employee2);
    }

    @Test
    void ruleTest() {
        Employee employee = getEmployeeRandomSampleGenerator();
        AccessRule accessRuleBack = getAccessRuleRandomSampleGenerator();

        employee.addRule(accessRuleBack);
        assertThat(employee.getRules()).containsOnly(accessRuleBack);
        assertThat(accessRuleBack.getEmployee()).isEqualTo(employee);

        employee.removeRule(accessRuleBack);
        assertThat(employee.getRules()).doesNotContain(accessRuleBack);
        assertThat(accessRuleBack.getEmployee()).isNull();

        employee.rules(new HashSet<>(Set.of(accessRuleBack)));
        assertThat(employee.getRules()).containsOnly(accessRuleBack);
        assertThat(accessRuleBack.getEmployee()).isEqualTo(employee);

        employee.setRules(new HashSet<>());
        assertThat(employee.getRules()).doesNotContain(accessRuleBack);
        assertThat(accessRuleBack.getEmployee()).isNull();
    }

    @Test
    void tagTest() {
        Employee employee = getEmployeeRandomSampleGenerator();
        Tag tagBack = getTagRandomSampleGenerator();

        employee.addTag(tagBack);
        assertThat(employee.getTags()).containsOnly(tagBack);
        assertThat(tagBack.getEmployee()).isEqualTo(employee);

        employee.removeTag(tagBack);
        assertThat(employee.getTags()).doesNotContain(tagBack);
        assertThat(tagBack.getEmployee()).isNull();

        employee.tags(new HashSet<>(Set.of(tagBack)));
        assertThat(employee.getTags()).containsOnly(tagBack);
        assertThat(tagBack.getEmployee()).isEqualTo(employee);

        employee.setTags(new HashSet<>());
        assertThat(employee.getTags()).doesNotContain(tagBack);
        assertThat(tagBack.getEmployee()).isNull();
    }
}
