package com.sxy.snote.service;

import com.sxy.snote.dto.NoteSetDisplayDTO;
import com.sxy.snote.dto.PageSortDTO;


import java.util.List;
import java.util.UUID;

public interface NoteSetService {
    //create new noteSet
    NoteSetDisplayDTO createNoteSet(UUID userId,NoteSetDisplayDTO noteSetDisplayDTO);

    //delete one NoteSet
    void deleteNoteSet(UUID userId,UUID noteSetId);

    //update one NoteSet
    NoteSetDisplayDTO updateNoteSet(UUID userId,UUID noteSetId,NoteSetDisplayDTO noteSetDisplayDTO);

    //get noteSets to display for user with pagination and sorting
    List<NoteSetDisplayDTO> getNoteSetsDisplay(UUID userId, PageSortDTO pageSortDTO);
}