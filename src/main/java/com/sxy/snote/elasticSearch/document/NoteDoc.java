package com.sxy.snote.elasticSearch.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(indexName = "note")
public class NoteDoc {

    @Id
    @Field(type = FieldType.Keyword)
    private UUID id;

    @Field(type = FieldType.Text)
    private String title;

    @Field(type = FieldType.Text)
    private String content;

    @Field(type = FieldType.Text)
    private String code;

    @Field(type = FieldType.Keyword)
    private UUID userId;

    @Field(type = FieldType.Keyword)
    private UUID noteSetId;
}