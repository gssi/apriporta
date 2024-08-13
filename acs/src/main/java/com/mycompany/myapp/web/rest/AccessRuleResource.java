package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.AccessRule;
import com.mycompany.myapp.repository.AccessRuleRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.AccessRule}.
 */
@RestController
@RequestMapping("/api/access-rules")
@Transactional
public class AccessRuleResource {

    private static final Logger log = LoggerFactory.getLogger(AccessRuleResource.class);

    private static final String ENTITY_NAME = "accessRule";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AccessRuleRepository accessRuleRepository;

    public AccessRuleResource(AccessRuleRepository accessRuleRepository) {
        this.accessRuleRepository = accessRuleRepository;
    }

    /**
     * {@code POST  /access-rules} : Create a new accessRule.
     *
     * @param accessRule the accessRule to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new accessRule, or with status {@code 400 (Bad Request)} if the accessRule has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AccessRule> createAccessRule(@RequestBody AccessRule accessRule) throws URISyntaxException {
        log.debug("REST request to save AccessRule : {}", accessRule);
        if (accessRule.getId() != null) {
            throw new BadRequestAlertException("A new accessRule cannot already have an ID", ENTITY_NAME, "idexists");
        }
        accessRule = accessRuleRepository.save(accessRule);
        return ResponseEntity.created(new URI("/api/access-rules/" + accessRule.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, accessRule.getId().toString()))
            .body(accessRule);
    }

    /**
     * {@code PUT  /access-rules/:id} : Updates an existing accessRule.
     *
     * @param id the id of the accessRule to save.
     * @param accessRule the accessRule to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated accessRule,
     * or with status {@code 400 (Bad Request)} if the accessRule is not valid,
     * or with status {@code 500 (Internal Server Error)} if the accessRule couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AccessRule> updateAccessRule(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AccessRule accessRule
    ) throws URISyntaxException {
        log.debug("REST request to update AccessRule : {}, {}", id, accessRule);
        if (accessRule.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, accessRule.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!accessRuleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        accessRule = accessRuleRepository.save(accessRule);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, accessRule.getId().toString()))
            .body(accessRule);
    }

    /**
     * {@code PATCH  /access-rules/:id} : Partial updates given fields of an existing accessRule, field will ignore if it is null
     *
     * @param id the id of the accessRule to save.
     * @param accessRule the accessRule to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated accessRule,
     * or with status {@code 400 (Bad Request)} if the accessRule is not valid,
     * or with status {@code 404 (Not Found)} if the accessRule is not found,
     * or with status {@code 500 (Internal Server Error)} if the accessRule couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AccessRule> partialUpdateAccessRule(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AccessRule accessRule
    ) throws URISyntaxException {
        log.debug("REST request to partial update AccessRule partially : {}, {}", id, accessRule);
        if (accessRule.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, accessRule.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!accessRuleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AccessRule> result = accessRuleRepository
            .findById(accessRule.getId())
            .map(existingAccessRule -> {
                if (accessRule.getStartDate() != null) {
                    existingAccessRule.setStartDate(accessRule.getStartDate());
                }
                if (accessRule.getEndDate() != null) {
                    existingAccessRule.setEndDate(accessRule.getEndDate());
                }
                if (accessRule.getNotes() != null) {
                    existingAccessRule.setNotes(accessRule.getNotes());
                }

                return existingAccessRule;
            })
            .map(accessRuleRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, accessRule.getId().toString())
        );
    }

    /**
     * {@code GET  /access-rules} : get all the accessRules.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of accessRules in body.
     */
    @GetMapping("")
    public List<AccessRule> getAllAccessRules(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get all AccessRules");
        if (eagerload) {
            return accessRuleRepository.findAllWithEagerRelationships();
        } else {
            return accessRuleRepository.findAll();
        }
    }

    /**
     * {@code GET  /access-rules/:id} : get the "id" accessRule.
     *
     * @param id the id of the accessRule to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the accessRule, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AccessRule> getAccessRule(@PathVariable("id") Long id) {
        log.debug("REST request to get AccessRule : {}", id);
        Optional<AccessRule> accessRule = accessRuleRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(accessRule);
    }

    /**
     * {@code DELETE  /access-rules/:id} : delete the "id" accessRule.
     *
     * @param id the id of the accessRule to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccessRule(@PathVariable("id") Long id) {
        log.debug("REST request to delete AccessRule : {}", id);
        accessRuleRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
