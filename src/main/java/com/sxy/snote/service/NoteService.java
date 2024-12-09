package com.sxy.snote.service;

import com.sxy.snote.dto.NoteDTO;
import com.sxy.snote.dto.PageSortDTO;

import java.util.List;
import java.util.UUID;

public interface NoteService {
    NoteDTO createNote(UUID userId,NoteDTO noteDTO);
    NoteDTO createNote(UUID userId,UUID noteSetId,NoteDTO noteDTO);
    NoteDTO updateNote(UUID userId,UUID noteId,NoteDTO noteDTO);
    NoteDTO getNoteById(UUID noteId);
    void deleteNoteById(UUID noteId);

    //get notes of the noteSet with pagination and sorting
    List<NoteDTO> getNotes(UUID userId, UUID noteSetId, PageSortDTO pageSortDTO);

    //get notes of the user with pagination and sorting
    List<NoteDTO>getNotes(UUID userId,PageSortDTO pageSortDTO);

    //Delete multiple notes
    void deleteNoteByIds(UUID userId,List<UUID> noteIds);

    //Move notes to one noteSet
    Integer assignNoteSetToNotes(List<UUID> noteIds,UUID noteSetId,Boolean unload);
}
