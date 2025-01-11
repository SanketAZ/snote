package com.sxy.snote.event;

import com.sxy.snote.dto.NoteDTO;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class NoteChangeEvent extends ApplicationEvent {
    private final NoteDTO noteDTO;
    private final String action;

    public NoteChangeEvent(Object source, NoteDTO noteDTO,String Action) {
        super(source);
        this.noteDTO = noteDTO;
        this.action = Action;
    }
}
