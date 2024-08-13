package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Tag;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Tag entity.
 */
@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    default Optional<Tag> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Tag> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Tag> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(value = "select tag from Tag tag left join fetch tag.employee", countQuery = "select count(tag) from Tag tag")
    Page<Tag> findAllWithToOneRelationships(Pageable pageable);

    @Query("select tag from Tag tag left join fetch tag.employee")
    List<Tag> findAllWithToOneRelationships();

    @Query("select tag from Tag tag left join fetch tag.employee where tag.id =:id")
    Optional<Tag> findOneWithToOneRelationships(@Param("id") Long id);

    @Query("select tag from Tag tag left join fetch tag.employee where tag.code =:code")
    Optional<Tag> findByCode(String code);
}
