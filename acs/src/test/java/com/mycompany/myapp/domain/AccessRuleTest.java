package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.AccessRuleTestSamples.*;
import static com.mycompany.myapp.domain.EmployeeTestSamples.*;
import static com.mycompany.myapp.domain.RoomTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AccessRuleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AccessRule.class);
        AccessRule accessRule1 = getAccessRuleSample1();
        AccessRule accessRule2 = new AccessRule();
        assertThat(accessRule1).isNotEqualTo(accessRule2);

        accessRule2.setId(accessRule1.getId());
        assertThat(accessRule1).isEqualTo(accessRule2);

        accessRule2 = getAccessRuleSample2();
        assertThat(accessRule1).isNotEqualTo(accessRule2);
    }

    @Test
    void employeeTest() {
        AccessRule accessRule = getAccessRuleRandomSampleGenerator();
        Employee employeeBack = getEmployeeRandomSampleGenerator();

        accessRule.setEmployee(employeeBack);
        assertThat(accessRule.getEmployee()).isEqualTo(employeeBack);

        accessRule.employee(null);
        assertThat(accessRule.getEmployee()).isNull();
    }

    @Test
    void roomTest() {
        AccessRule accessRule = getAccessRuleRandomSampleGenerator();
        Room roomBack = getRoomRandomSampleGenerator();

        accessRule.setRoom(roomBack);
        assertThat(accessRule.getRoom()).isEqualTo(roomBack);

        accessRule.room(null);
        assertThat(accessRule.getRoom()).isNull();
    }
}
