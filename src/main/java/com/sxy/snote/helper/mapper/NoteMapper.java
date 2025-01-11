package com.sxy.snote.helper.mapper;

import com.sxy.snote.dto.NoteDTO;
import com.sxy.snote.elasticSearch.document.NoteDoc;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NoteMapper {
    String defaultNoteSetId = "00000000-0000-0000-0000-ffffffffffff";

    NoteDTO noteDocToNoteDTO(NoteDoc noteDoc);
    @Mapping(target = "noteSetId",defaultValue = defaultNoteSetId)
    NoteDoc noteDtoToNoteDoc(NoteDTO noteDTO);
}