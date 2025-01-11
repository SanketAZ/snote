package com.sxy.snote.event;

import com.sxy.snote.dto.NoteDTO;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.List;

@Getter
public class NotesDeleteEvent extends ApplicationEvent {
    private final List<NoteDTO> noteDTOList;
    public NotesDeleteEvent(Object source, List<NoteDTO>noteDTOList) {
        super(source);
        this.noteDTOList = noteDTOList;
    }
}
