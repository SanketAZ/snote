package com.sxy.snote.service.impl;

import co.elastic.clients.json.JsonData;
import com.sxy.snote.dto.NoteDTO;
import com.sxy.snote.elasticSearch.document.NoteDoc;
import com.sxy.snote.elasticSearch.repo.NoteDocRepo;
import com.sxy.snote.event.NoteChangeEvent;
import com.sxy.snote.event.NotesDeleteEvent;
import com.sxy.snote.event.NotesUpdateEvent;
import com.sxy.snote.exception.ES.SaveEsException;
import com.sxy.snote.helper.mapper.NoteMapper;
import com.sxy.snote.service.NoteIndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.RestStatusException;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.RefreshPolicy;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class NoteIndexServiceImpl implements NoteIndexService {

    @Autowired
    private NoteDocRepo noteDocRepo;

    @Autowired
    private NoteMapper noteMapper;

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;


    //create the new document in index
    @Override
    public void saveNoteDoc(NoteChangeEvent noteChangeEvent) throws SaveEsException {
        try{
            NoteDoc noteDoc = noteMapper.noteDtoToNoteDoc(noteChangeEvent.getNoteDTO());

            //forming the index query
            IndexQuery indexQuery=new IndexQueryBuilder()
                    .withId(noteDoc.getId().toString())
                    .withRouting(noteDoc.getId().toString())
                    .withObject(noteDoc)
                    .build();

            //saving the doc in index
            System.out.println("saving document in index");
            elasticsearchOperations.index(indexQuery, IndexCoordinates.of("note"));
            System.out.println("document save in index");

        } catch (RestStatusException exception) {
            int statusCode=exception.getStatus();
            //retryable exceptions
            if(statusCode>=500 && statusCode<600){
                throw new SaveEsException(exception.getMessage());
            }
            else{
                printClientException(exception);
            }
        }catch (Exception exception){
            printClientException(exception);
        }
    }

    @Override
    public void updateNoteDoc(NoteChangeEvent noteChangeEvent) throws SaveEsException {
        try{
            NoteDoc noteDoc = noteMapper.noteDtoToNoteDoc(noteChangeEvent.getNoteDTO());


            UpdateQuery updateQuery=UpdateQuery.builder(noteDoc.getId().toString())
                    .withRouting(noteDoc.getId().toString())
                    .withDocument(getDocForUpdate(noteDoc))
                    .build();

            //saving the doc in index
            System.out.println("update document in index");
            elasticsearchOperations.update(updateQuery, IndexCoordinates.of("note"));
            System.out.println("document updated in index");

        } catch (RestStatusException exception) {
            int statusCode=exception.getStatus();
            //retryable exceptions
            if(statusCode>=500 && statusCode<600){
                throw new SaveEsException(exception.getMessage());
            }
            else{
                printClientException(exception);
            }
        }catch (Exception exception){
            printClientException(exception);
        }
    }

    @Override
    public void deleteNoteDoc(NoteChangeEvent noteChangeEvent) throws SaveEsException {
        try{
            System.out.println("deleting document from index");
            NoteDoc noteDoc=noteMapper.noteDtoToNoteDoc(noteChangeEvent.getNoteDTO());
            elasticsearchOperations.delete(noteDoc);
            System.out.println("document deleted from index");
        }
        catch (RestStatusException exception) {
            int statusCode=exception.getStatus();
            //retryable exceptions
            if(statusCode>=500 && statusCode<600){
                throw new SaveEsException(exception.getMessage());
            }
            else{
                printClientException(exception);
            }
        }catch (Exception exception){
            printClientException(exception);
        }
    }

    //This method deletes the noteDocs provided in NotesDeleteEvent
    @Override
    public void deleteNotesDoc(NotesDeleteEvent notesDeleteEvent) throws SaveEsException {
        List<NoteDTO>noteDTOList=notesDeleteEvent.getNoteDTOList();
        List<String>noteDocIds=noteDTOList.stream()
                .map(noteDTO ->noteDTO.getId().toString()).toList();


        // Create a query matching the IDs
        Criteria criteria=Criteria.where("_id").in(noteDocIds);
        CriteriaQuery query = new CriteriaQuery(criteria);

        //Building delete
        DeleteQuery deleteQuery=DeleteQuery.builder(query)
                                            .withRefresh(true)
                                            .build();

        try {
            System.out.println("Deleting the notes from index");
            elasticsearchOperations.delete(deleteQuery,NoteDoc.class);
            System.out.println("Notes are deleted from index");

        }catch (RestStatusException exception) {
            int statusCode=exception.getStatus();
            //retryable exceptions
            if(statusCode>=500 && statusCode<600){
                throw new SaveEsException(exception.getMessage());
            }
            else{
                printClientException(exception);
            }
        }catch (Exception exception){
            printClientException(exception);
        }
    }

    //This method to update multiple notes in NotesUpdateEvent
    @Override
    public void updateNoteDoc(NotesUpdateEvent notesUpdateEvent) throws SaveEsException {


    }

    //To update the noteSetId of provided notes
    @Override
    public void updateNoteSetIds(NotesUpdateEvent notesUpdateEvent) throws SaveEsException {
        List<String>noteSetIdList=notesUpdateEvent.getNoteIDList();
        String noteSetId=notesUpdateEvent.getNoteSetId();

        // Create a query matching the IDs
        Criteria criteria=Criteria.where("_id").in(noteSetIdList);
        CriteriaQuery query = new CriteriaQuery(criteria);

        Map<String, Object> params = Map.of(
                "fieldName", "noteSetId",
                "fieldValue", noteSetId
        );

        //Update query with script in builder we provide query and in other parms in build
        UpdateQuery updateQuery= UpdateQuery.builder(query)
                                        .withScript("ctx._source[params.fieldName] = params.fieldValue")
                                        .withScriptType(ScriptType.INLINE)
                                        .withParams(params)
                                        .withRefreshPolicy(RefreshPolicy.IMMEDIATE)
                                        .build();

        try {
            System.out.println("updating document from index");
            //Using update with query
            elasticsearchOperations.updateByQuery(updateQuery, IndexCoordinates.of("note"));
            System.out.println("document updated from index");

        } catch (RestStatusException exception) {
            int statusCode=exception.getStatus();
            //retryable exceptions
            if(statusCode>=500 && statusCode<600){
                throw new SaveEsException(exception.getMessage());
            }
            else{
                printClientException(exception);
            }
        }catch (Exception exception){
            printClientException(exception);
        }
    }

    private Map<String,JsonData> convertToJsonData(Map<String,String>stringMap){
        return stringMap.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry->JsonData.of(entry.getValue())
                ));
    }

    private Document getDocForUpdate(NoteDoc noteDoc){
        Map<String,Object> updates=new HashMap<>();
        updates.put("title",noteDoc.getTitle());
        updates.put("content",noteDoc.getContent());
        updates.put("code",noteDoc.getCode());
        updates.put("userId",noteDoc.getUserId().toString());
        updates.put("noteSetId",noteDoc.getNoteSetId().toString());
        return Document.from(updates);
    }

    private void printClientException(Exception exception){
        System.out.println("Client side exception!!!!!");
        System.out.println(exception.getMessage());
        System.out.println(exception.getCause());
    }
}
