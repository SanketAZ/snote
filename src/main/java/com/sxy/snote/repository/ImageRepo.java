package com.sxy.snote.repository;

import com.sxy.snote.model.Image;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ImageRepo extends JpaRepository<Image, UUID> {
    List<Image>findAllByNoteId(UUID noteId);
    Page<Image> findAllByNoteId(UUID noteId, Pageable pageable);
}
