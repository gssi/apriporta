package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.AccessRuleTestSamples.*;
import static com.mycompany.myapp.domain.RoomTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class RoomTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Room.class);
        Room room1 = getRoomSample1();
        Room room2 = new Room();
        assertThat(room1).isNotEqualTo(room2);

        room2.setId(room1.getId());
        assertThat(room1).isEqualTo(room2);

        room2 = getRoomSample2();
        assertThat(room1).isNotEqualTo(room2);
    }

    @Test
    void ruleTest() {
        Room room = getRoomRandomSampleGenerator();
        AccessRule accessRuleBack = getAccessRuleRandomSampleGenerator();

        room.addRule(accessRuleBack);
        assertThat(room.getRules()).containsOnly(accessRuleBack);
        assertThat(accessRuleBack.getRoom()).isEqualTo(room);

        room.removeRule(accessRuleBack);
        assertThat(room.getRules()).doesNotContain(accessRuleBack);
        assertThat(accessRuleBack.getRoom()).isNull();

        room.rules(new HashSet<>(Set.of(accessRuleBack)));
        assertThat(room.getRules()).containsOnly(accessRuleBack);
        assertThat(accessRuleBack.getRoom()).isEqualTo(room);

        room.setRules(new HashSet<>());
        assertThat(room.getRules()).doesNotContain(accessRuleBack);
        assertThat(accessRuleBack.getRoom()).isNull();
    }
}
