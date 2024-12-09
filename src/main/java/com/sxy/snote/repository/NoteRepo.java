package com.sxy.snote.repository;

import com.sxy.snote.model.Note;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface NoteRepo extends JpaRepository<Note, UUID> {
    Page<Note>findAllByUserIdAndNoteSetId(UUID userId, UUID noteSetId, Pageable pageable);

    Page<Note> findByUserId(UUID userId, Pageable pageable);

    List<Note> findByUserIdAndIdIn(UUID userId, List<UUID> noteIds);

    //Query to modify the noteSetId in note
    @Modifying
    @Transactional
    @Query("UPDATE Note n SET n.noteSet.id = :noteSetId WHERE n.user.id = :userId AND n.id IN :noteIds")
    int updateNoteSetIdForMultipleNotes(@Param("userId") UUID userId,
                                        @Param("noteSetId") UUID noteSetId,
                                        @Param("noteIds") List<UUID> noteIds);
}
