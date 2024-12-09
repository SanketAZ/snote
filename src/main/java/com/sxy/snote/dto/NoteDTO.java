package com.sxy.snote.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoteDTO {
    private UUID id;
    private String title;
    private String content;
    private String code;
    private UUID userId;
    private UUID noteSetId;
}
