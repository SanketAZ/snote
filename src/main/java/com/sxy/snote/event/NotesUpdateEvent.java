package com.sxy.snote.event;

import com.sxy.snote.dto.NoteDTO;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.List;

@Getter
public class NotesUpdateEvent extends ApplicationEvent {
    private final List<NoteDTO> noteDTOList;
    private final List<String> noteIDList;
    private final String NoteSetId;
    public NotesUpdateEvent(Object source,List<NoteDTO>noteDTOList) {
        super(source);
        this.noteDTOList = noteDTOList;
        this.noteIDList=null;
        this.NoteSetId=null;
    }

    public NotesUpdateEvent(Object source,List<String> noteIDList,String NoteSetId) {
        super(source);
        this.noteDTOList = null;
        this.noteIDList=noteIDList;
        this.NoteSetId=NoteSetId;

    }
}
