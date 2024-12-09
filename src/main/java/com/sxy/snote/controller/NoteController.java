package com.sxy.snote.controller;

import com.sxy.snote.dto.NoteDTO;
import com.sxy.snote.dto.PageSortDTO;
import com.sxy.snote.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/note")
public class NoteController {

    @Autowired
    private NoteService noteService;

    @PostMapping("/{clientId}")
    public ResponseEntity<NoteDTO> createNote(@PathVariable("clientId") UUID clientId, @RequestBody NoteDTO noteDTO) {
        NoteDTO noteDTO1=noteService.createNote(clientId,noteDTO);
        return ResponseEntity.status(HttpStatus.OK)
                        .body(noteDTO1);
    }

    @PostMapping("/{clientId}/ns")
    public ResponseEntity<NoteDTO> createNoteWithNoteSet(@PathVariable("clientId") UUID clientId,@RequestParam("noteSetId")UUID noteSetId, @RequestBody NoteDTO noteDTO) {
        NoteDTO noteDTO1=noteService.createNote(clientId,noteSetId,noteDTO);
        return ResponseEntity.status(HttpStatus.OK)
                .body(noteDTO1);
    }

    @PutMapping("/{clientId}/{noteId}")
    public ResponseEntity<NoteDTO> updateNote(@PathVariable("clientId") UUID clientId,@PathVariable("noteId") UUID noteId, @RequestBody NoteDTO noteDTO) {
        NoteDTO noteDTO1=noteService.updateNote(clientId,noteId,noteDTO);
        return ResponseEntity.status(HttpStatus.OK)
                .body(noteDTO1);
    }

    @GetMapping("/{noteId}")
    public ResponseEntity<NoteDTO> getSpecificNote(@PathVariable("noteId") UUID noteId){
        NoteDTO noteDTO1=noteService.getNoteById(noteId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(noteDTO1);
    }

    @GetMapping("/{userId}/{noteSetId}")
    public ResponseEntity<List<NoteDTO>> getNotesForNoteSet(@PathVariable("userId") UUID userId,
                                                            @PathVariable("noteSetId") UUID noteSetId,
                                                            @RequestParam("pageNo")int pageNo,
                                                            @RequestParam("pageSize")int pageSize,
                                                            @RequestParam("asc")boolean asc) {
        PageSortDTO pageSortDTO=PageSortDTO.builder()
                .pageSize(pageSize)
                .pageNo(pageNo)
                .asc(asc)
                .arg("createdDate")
                .build();
        List<NoteDTO>noteDTOList=noteService.getNotes(userId,noteSetId,pageSortDTO);
        return ResponseEntity.status(HttpStatus.OK)
                .body(noteDTOList);
    }

    @GetMapping("/m/{userId}")
    public ResponseEntity<List<NoteDTO>> getNotesForUser(@PathVariable("userId") UUID userId,
                                                            @RequestParam("pageNo")int pageNo,
                                                            @RequestParam("pageSize")int pageSize,
                                                            @RequestParam("asc")boolean asc) {
        PageSortDTO pageSortDTO=PageSortDTO.builder()
                .pageSize(pageSize)
                .pageNo(pageNo)
                .asc(asc)
                .arg("createdDate")
                .build();
        List<NoteDTO>noteDTOList=noteService.getNotes(userId,pageSortDTO);
        return ResponseEntity.status(HttpStatus.OK)
                .body(noteDTOList);
    }

    @DeleteMapping("/{noteId}")
    public ResponseEntity<String> deleteNote(@PathVariable("noteId") UUID noteId)
    {
      noteService.deleteNoteById(noteId);
        return ResponseEntity.status(HttpStatus.OK)
                .body("Note deleted!!!");
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteNotes(@PathVariable("userId") UUID userId,@RequestParam("noteIds")List<UUID>noteIds) {
        noteService.deleteNoteByIds(userId,noteIds);
        return ResponseEntity.status(HttpStatus.OK)
                .body("Notes are deleted!!!");
    }

    @PatchMapping("/{noteSetId}")
    public ResponseEntity<Integer>updateNotesForMultipleNotes(@PathVariable("noteSetId")UUID noteSetId,@RequestParam("noteIds")List<UUID>noteIds,@RequestParam("unload")Boolean unload) {
        Integer updatedNotes=noteService.assignNoteSetToNotes(noteIds,noteSetId,unload);
        return ResponseEntity.status(HttpStatus.OK)
                .body(updatedNotes);
    }
}
