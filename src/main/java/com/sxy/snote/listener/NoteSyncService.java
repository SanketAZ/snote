package com.sxy.snote.listener;

import com.sxy.snote.event.NoteChangeEvent;
import com.sxy.snote.event.NotesDeleteEvent;
import com.sxy.snote.event.NotesUpdateEvent;
import com.sxy.snote.exception.ES.SaveEsException;
import com.sxy.snote.service.NoteIndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class NoteSyncService {

    @Autowired
    private NoteIndexService noteIndexService;


    //Event listener to process NoteChangeEvent save,delete,update single document in elastic search
    @Async("ESThreadExecutor")
    @EventListener
    @Retryable(retryFor = {SaveEsException.class},
               maxAttempts = 3,
               backoff = @Backoff(delay = 5000, multiplier = 2),
               recover ="recoverSave")
    public void sync(NoteChangeEvent noteChangeEvent) {
        switch (noteChangeEvent.getAction()){
            case "CREATE": {
                noteIndexService.saveNoteDoc(noteChangeEvent);
            }
                break;
            case "UPDATE": {
                noteIndexService.updateNoteDoc(noteChangeEvent);
            }
                break;
            case "DELETE":
                noteIndexService.deleteNoteDoc(noteChangeEvent);
                break;
            default:
        }
    }

    //Event listener to process NotesDeleteEvent for deleting multiple documents provided in event
    @Async("ESThreadExecutor")
    @EventListener
    @Retryable(retryFor = {SaveEsException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 5000, multiplier = 2),
            recover ="recoverSave")
    public void sync(NotesDeleteEvent notesDeleteEvent) {
        noteIndexService.deleteNotesDoc(notesDeleteEvent);

    }

    //Event listener to process NotesUpdateEvent for updating multiple documents provided in event
    @Async("ESThreadExecutor")
    @EventListener
    @Retryable(retryFor = {SaveEsException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 5000, multiplier = 2),
            recover ="recoverSave")
    public void sync(NotesUpdateEvent notesUpdateEvent) {
        //For getNoteIDList( ) value is null this means this request is for to just update the notsetid toggle
        if(notesUpdateEvent.getNoteIDList()!=null){
            noteIndexService.updateNoteSetIds(notesUpdateEvent);
        }

    }

    //007Task implement the reliable recover method
    @Recover
    public void recoverSave(SaveEsException saveEsException,NoteChangeEvent noteChangeEvent){
        System.out.println("Recover method called: "+ noteChangeEvent.getNoteDTO().toString());
        System.out.println(saveEsException.getMessage());
    }
}
