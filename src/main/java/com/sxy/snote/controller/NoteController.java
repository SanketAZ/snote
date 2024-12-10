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
@RequestMapping("/users/{userId}/notes")
public class NoteController {

    @Autowired
    private NoteService noteService;

    @PostMapping
    public ResponseEntity<NoteDTO> createNote(@PathVariable("userId") UUID clientId, @RequestBody NoteDTO noteDTO) {
        NoteDTO noteDTO1=noteService.createNote(clientId,noteDTO);
        return ResponseEntity.status(HttpStatus.OK)
                        .body(noteDTO1);
    }

    @PostMapping("/note-set/{noteSetId}")
    public ResponseEntity<NoteDTO> createNoteWithNoteSet(@PathVariable("userId") UUID clientId,@RequestParam("noteSetId")UUID noteSetId, @RequestBody NoteDTO noteDTO) {
        NoteDTO noteDTO1=noteService.createNote(clientId,noteSetId,noteDTO);
        return ResponseEntity.status(HttpStatus.OK)
                .body(noteDTO1);
    }

    @PutMapping("/{noteId}")
    public ResponseEntity<NoteDTO> updateNote(@PathVariable("userId") UUID clientId,@PathVariable("noteId") UUID noteId, @RequestBody NoteDTO noteDTO) {
        NoteDTO noteDTO1=noteService.updateNote(clientId,noteId,noteDTO);
        return ResponseEntity.status(HttpStatus.OK)
                .body(noteDTO1);
    }

    @GetMapping("/{noteId}")
    public ResponseEntity<NoteDTO> getSpecificNote(@PathVariable("userId") UUID clientId,@PathVariable("noteId") UUID noteId){
        NoteDTO noteDTO1=noteService.getNoteById(noteId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(noteDTO1);
    }

    @GetMapping("/note-sets/{noteSetId}")
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

    @GetMapping
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
    public ResponseEntity<String> deleteNote(@PathVariable("userId") UUID userId,@PathVariable("noteId") UUID noteId)
    {
      noteService.deleteNoteById(noteId);
        return ResponseEntity.status(HttpStatus.OK)
                .body("Note deleted!!!");
    }

    @DeleteMapping
    public ResponseEntity<String> deleteNotes(@PathVariable("userId") UUID userId,@RequestParam("noteIds")List<UUID>noteIds) {
        noteService.deleteNoteByIds(userId,noteIds);
        return ResponseEntity.status(HttpStatus.OK)
                .body("Notes are deleted!!!");
    }

    @PatchMapping("/note-sets/{noteSetId}")
    public ResponseEntity<Integer>updateNotesForMultipleNotes(@PathVariable("noteSetId")UUID noteSetId,@RequestParam("noteIds")List<UUID>noteIds,@RequestParam("unload")Boolean unload) {
        Integer updatedNotes=noteService.assignNoteSetToNotes(noteIds,noteSetId,unload);
        return ResponseEntity.status(HttpStatus.OK)
                .body(updatedNotes);
    }
}
