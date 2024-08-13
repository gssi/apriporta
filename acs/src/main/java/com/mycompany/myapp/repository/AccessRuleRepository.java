package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.AccessRule;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AccessRule entity.
 */
@Repository
public interface AccessRuleRepository extends JpaRepository<AccessRule, Long> {
    default Optional<AccessRule> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<AccessRule> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<AccessRule> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select accessRule from AccessRule accessRule left join fetch accessRule.employee left join fetch accessRule.room",
        countQuery = "select count(accessRule) from AccessRule accessRule"
    )
    Page<AccessRule> findAllWithToOneRelationships(Pageable pageable);

    @Query("select accessRule from AccessRule accessRule left join fetch accessRule.employee left join fetch accessRule.room")
    List<AccessRule> findAllWithToOneRelationships();

    @Query(
        "select accessRule from AccessRule accessRule left join fetch accessRule.employee left join fetch accessRule.room where accessRule.id =:id"
    )
    Optional<AccessRule> findOneWithToOneRelationships(@Param("id") Long id);

}
