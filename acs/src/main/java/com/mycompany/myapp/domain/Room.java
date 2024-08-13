package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Room.
 */
@Entity
@Table(name = "room")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Room implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "room_name")
    private String roomName;

    @Column(name = "uid")
    private String uid;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "room")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "employee", "room" }, allowSetters = true)
    private Set<AccessRule> rules = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Room id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoomName() {
        return this.roomName;
    }

    public Room roomName(String roomName) {
        this.setRoomName(roomName);
        return this;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getUid() {
        return this.uid;
    }

    public Room uid(String uid) {
        this.setUid(uid);
        return this;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Set<AccessRule> getRules() {
        return this.rules;
    }

    public void setRules(Set<AccessRule> accessRules) {
        if (this.rules != null) {
            this.rules.forEach(i -> i.setRoom(null));
        }
        if (accessRules != null) {
            accessRules.forEach(i -> i.setRoom(this));
        }
        this.rules = accessRules;
    }

    public Room rules(Set<AccessRule> accessRules) {
        this.setRules(accessRules);
        return this;
    }

    public Room addRule(AccessRule accessRule) {
        this.rules.add(accessRule);
        accessRule.setRoom(this);
        return this;
    }

    public Room removeRule(AccessRule accessRule) {
        this.rules.remove(accessRule);
        accessRule.setRoom(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Room)) {
            return false;
        }
        return getId() != null && getId().equals(((Room) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Room{" +
            "id=" + getId() +
            ", roomName='" + getRoomName() + "'" +
            ", uid='" + getUid() + "'" +
            "}";
    }
}
