package com.sxy.snote.repository;

import com.sxy.snote.model.NoteSet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NoteSetRepo extends JpaRepository<NoteSet, UUID> {

    @Query("SELECT COUNT(ns) > 0 FROM NoteSet ns WHERE ns.id = :noteSetId and ns.user.id= :userId")
    boolean existsByUserIdAndNoteSetId(@Param("userId") UUID userId, @Param("noteSetId") UUID noteSetId);

    Optional<NoteSet> findByIdAndUserId(UUID noteSetId, UUID userId);
    Page<NoteSet> findAllByUserId(UUID userId, Pageable pageable);

    @Query(value = "SELECT user_id from note_set WHERE id=:noteSetId",nativeQuery = true)
    UUID getUserIdForNoteSet(@Param("noteSetId") UUID noteSetId);
}
