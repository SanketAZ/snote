package com.sxy.snote.service;

import com.sxy.snote.event.NoteChangeEvent;
import com.sxy.snote.event.NotesDeleteEvent;
import com.sxy.snote.event.NotesUpdateEvent;
import com.sxy.snote.exception.ES.SaveEsException;

public interface NoteIndexService {
    void saveNoteDoc(NoteChangeEvent noteChangeEvent)throws SaveEsException;
    void updateNoteDoc(NoteChangeEvent noteChangeEvent)throws SaveEsException;
    void deleteNoteDoc(NoteChangeEvent noteChangeEvent)throws SaveEsException;
    void deleteNotesDoc(NotesDeleteEvent notesDeleteEvent)throws SaveEsException;
    void updateNoteDoc(NotesUpdateEvent notesUpdateEvent)throws SaveEsException;
    void updateNoteSetIds(NotesUpdateEvent notesUpdateEvent)throws SaveEsException;
}
