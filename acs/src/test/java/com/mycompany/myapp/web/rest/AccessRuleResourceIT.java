package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.AccessRuleAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.AccessRule;
import com.mycompany.myapp.repository.AccessRuleRepository;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link AccessRuleResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class AccessRuleResourceIT {

    private static final Instant DEFAULT_START_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/access-rules";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AccessRuleRepository accessRuleRepository;

    @Mock
    private AccessRuleRepository accessRuleRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAccessRuleMockMvc;

    private AccessRule accessRule;

    private AccessRule insertedAccessRule;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AccessRule createEntity(EntityManager em) {
        AccessRule accessRule = new AccessRule().startDate(DEFAULT_START_DATE).endDate(DEFAULT_END_DATE).notes(DEFAULT_NOTES);
        return accessRule;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AccessRule createUpdatedEntity(EntityManager em) {
        AccessRule accessRule = new AccessRule().startDate(UPDATED_START_DATE).endDate(UPDATED_END_DATE).notes(UPDATED_NOTES);
        return accessRule;
    }

    @BeforeEach
    public void initTest() {
        accessRule = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedAccessRule != null) {
            accessRuleRepository.delete(insertedAccessRule);
            insertedAccessRule = null;
        }
    }

    @Test
    @Transactional
    void createAccessRule() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AccessRule
        var returnedAccessRule = om.readValue(
            restAccessRuleMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(accessRule)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AccessRule.class
        );

        // Validate the AccessRule in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertAccessRuleUpdatableFieldsEquals(returnedAccessRule, getPersistedAccessRule(returnedAccessRule));

        insertedAccessRule = returnedAccessRule;
    }

    @Test
    @Transactional
    void createAccessRuleWithExistingId() throws Exception {
        // Create the AccessRule with an existing ID
        accessRule.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAccessRuleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(accessRule)))
            .andExpect(status().isBadRequest());

        // Validate the AccessRule in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAccessRules() throws Exception {
        // Initialize the database
        insertedAccessRule = accessRuleRepository.saveAndFlush(accessRule);

        // Get all the accessRuleList
        restAccessRuleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(accessRule.getId().intValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAccessRulesWithEagerRelationshipsIsEnabled() throws Exception {
        when(accessRuleRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAccessRuleMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(accessRuleRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAccessRulesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(accessRuleRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAccessRuleMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(accessRuleRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getAccessRule() throws Exception {
        // Initialize the database
        insertedAccessRule = accessRuleRepository.saveAndFlush(accessRule);

        // Get the accessRule
        restAccessRuleMockMvc
            .perform(get(ENTITY_API_URL_ID, accessRule.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(accessRule.getId().intValue()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES));
    }

    @Test
    @Transactional
    void getNonExistingAccessRule() throws Exception {
        // Get the accessRule
        restAccessRuleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAccessRule() throws Exception {
        // Initialize the database
        insertedAccessRule = accessRuleRepository.saveAndFlush(accessRule);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the accessRule
        AccessRule updatedAccessRule = accessRuleRepository.findById(accessRule.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAccessRule are not directly saved in db
        em.detach(updatedAccessRule);
        updatedAccessRule.startDate(UPDATED_START_DATE).endDate(UPDATED_END_DATE).notes(UPDATED_NOTES);

        restAccessRuleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAccessRule.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedAccessRule))
            )
            .andExpect(status().isOk());

        // Validate the AccessRule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAccessRuleToMatchAllProperties(updatedAccessRule);
    }

    @Test
    @Transactional
    void putNonExistingAccessRule() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        accessRule.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAccessRuleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, accessRule.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(accessRule))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccessRule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAccessRule() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        accessRule.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccessRuleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(accessRule))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccessRule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAccessRule() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        accessRule.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccessRuleMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(accessRule)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AccessRule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAccessRuleWithPatch() throws Exception {
        // Initialize the database
        insertedAccessRule = accessRuleRepository.saveAndFlush(accessRule);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the accessRule using partial update
        AccessRule partialUpdatedAccessRule = new AccessRule();
        partialUpdatedAccessRule.setId(accessRule.getId());

        restAccessRuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAccessRule.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAccessRule))
            )
            .andExpect(status().isOk());

        // Validate the AccessRule in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAccessRuleUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAccessRule, accessRule),
            getPersistedAccessRule(accessRule)
        );
    }

    @Test
    @Transactional
    void fullUpdateAccessRuleWithPatch() throws Exception {
        // Initialize the database
        insertedAccessRule = accessRuleRepository.saveAndFlush(accessRule);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the accessRule using partial update
        AccessRule partialUpdatedAccessRule = new AccessRule();
        partialUpdatedAccessRule.setId(accessRule.getId());

        partialUpdatedAccessRule.startDate(UPDATED_START_DATE).endDate(UPDATED_END_DATE).notes(UPDATED_NOTES);

        restAccessRuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAccessRule.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAccessRule))
            )
            .andExpect(status().isOk());

        // Validate the AccessRule in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAccessRuleUpdatableFieldsEquals(partialUpdatedAccessRule, getPersistedAccessRule(partialUpdatedAccessRule));
    }

    @Test
    @Transactional
    void patchNonExistingAccessRule() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        accessRule.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAccessRuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, accessRule.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(accessRule))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccessRule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAccessRule() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        accessRule.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccessRuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(accessRule))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccessRule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAccessRule() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        accessRule.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccessRuleMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(accessRule)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AccessRule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAccessRule() throws Exception {
        // Initialize the database
        insertedAccessRule = accessRuleRepository.saveAndFlush(accessRule);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the accessRule
        restAccessRuleMockMvc
            .perform(delete(ENTITY_API_URL_ID, accessRule.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return accessRuleRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected AccessRule getPersistedAccessRule(AccessRule accessRule) {
        return accessRuleRepository.findById(accessRule.getId()).orElseThrow();
    }

    protected void assertPersistedAccessRuleToMatchAllProperties(AccessRule expectedAccessRule) {
        assertAccessRuleAllPropertiesEquals(expectedAccessRule, getPersistedAccessRule(expectedAccessRule));
    }

    protected void assertPersistedAccessRuleToMatchUpdatableProperties(AccessRule expectedAccessRule) {
        assertAccessRuleAllUpdatablePropertiesEquals(expectedAccessRule, getPersistedAccessRule(expectedAccessRule));
    }
}
