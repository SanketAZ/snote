package com.sxy.snote.controller;

import com.sxy.snote.dto.NoteSetDisplayDTO;
import com.sxy.snote.dto.PageSortDTO;
import com.sxy.snote.service.NoteSetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users/{userId}/note-sets")
public class NoteSetController {
    @Autowired
    private NoteSetService noteSetService;


    @GetMapping
    ResponseEntity<List<NoteSetDisplayDTO>> getNoteSetsDisplay(@PathVariable("userId") UUID userId,
                                                               @RequestParam("pageNo")int pageNo,
                                                               @RequestParam("pageSize")int pageSize,
                                                               @RequestParam("asc")boolean asc)
    {
        PageSortDTO pageSortDTO=PageSortDTO.builder()
                .pageSize(pageSize)
                .pageNo(pageNo)
                .asc(asc)
                .arg("createdDate")
                .build();
        List<NoteSetDisplayDTO> noteSetDisplayDTOS=noteSetService.getNoteSetsDisplay(userId,pageSortDTO);
        return ResponseEntity.status(HttpStatus.OK)
                .body(noteSetDisplayDTOS);
    }

    @PostMapping
    ResponseEntity<NoteSetDisplayDTO>createNoteSet(@PathVariable("userId") UUID userId, @RequestBody NoteSetDisplayDTO noteSetDisplayDTO){

        NoteSetDisplayDTO savedNoteSet=noteSetService.createNoteSet(userId,noteSetDisplayDTO);
        return ResponseEntity.status(HttpStatus.OK)
                .body(savedNoteSet);
    }

    @DeleteMapping("/{notSetId}")
    ResponseEntity<String>deleteNoteSet(@PathVariable("userId") UUID userId,@PathVariable("notSetId") UUID notSetId) {
        noteSetService.deleteNoteSet(userId,notSetId);
        return ResponseEntity.status(HttpStatus.OK)
                .body("NoteSet deleted!!!");
    }

    @PutMapping("/{notSetId}")
    ResponseEntity<NoteSetDisplayDTO>updateNoteSet(@PathVariable("userId") UUID userId,
                                                   @PathVariable("notSetId") UUID notSetId,
                                                   @RequestBody NoteSetDisplayDTO noteSetDisplayDTO){
        NoteSetDisplayDTO updatedNoteSet=noteSetService.updateNoteSet(userId,notSetId,noteSetDisplayDTO);
        return ResponseEntity.status(HttpStatus.OK)
                .body(updatedNoteSet);
    }


}