package com.sxy.snote.service.impl;

import com.sxy.snote.dto.NoteDTO;
import com.sxy.snote.dto.PageSortDTO;
import com.sxy.snote.exception.NoteSetUnauthorizedException;
import com.sxy.snote.exception.ResourceNotFoundException;
import com.sxy.snote.exception.UserNotFoundException;
import com.sxy.snote.helper.MapperService;
import com.sxy.snote.model.Client;
import com.sxy.snote.model.Note;
import com.sxy.snote.model.NoteSet;
import com.sxy.snote.repository.ClientRepo;
import com.sxy.snote.repository.NoteRepo;
import com.sxy.snote.repository.NoteSetRepo;
import com.sxy.snote.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class NoteServiceImpl implements NoteService {

    @Autowired
    private ClientRepo clientRepo;

    @Autowired
    private NoteRepo noteRepo;

    @Autowired
    private NoteSetRepo noteSetRepo;

    @Override
    public NoteDTO createNote(UUID userId, NoteDTO noteDTO) {
        System.out.println("Note creation");
        Client user=clientRepo.findById(userId).orElseThrow(()->new UserNotFoundException("User does not exists"));
        Note note= MapperService.getNote(noteDTO);
        note.setUser(user);
        note=noteRepo.save(note);
        System.out.println("Note creation");
        return MapperService.getNoteDTO(note);
    }

    //Method to create note in noteSet
    @Override
    @Transactional
    public NoteDTO createNote(UUID userId, UUID noteSetId, NoteDTO noteDTO){
        // Validate user and noteSet association
        NoteSet noteSet = noteSetRepo.findByIdAndUserId(noteSetId, userId)
                .orElseThrow(() -> new NoteSetUnauthorizedException("User or NoteSet not found:userId and noteSetId are not associated:" +userId+"-"+noteSetId));
        //Finding user
        Client user=noteSet.getUser();
        Note note= MapperService.getNote(noteDTO);
        //Setting user and NoteSet for note
        note.setUser(user);
        note.setNoteSet(noteSet);
        Note savedNote=noteRepo.save(note);
        return MapperService.getNoteDTO(savedNote);
    }

    @Override
    public NoteDTO updateNote(UUID userId,UUID noteId, NoteDTO noteDTO) {

        Note opNote=noteRepo.findById(noteId)
                .orElseThrow(()->new ResourceNotFoundException("Note does not exists"));

        Client opUser=opNote.getUser();

        if(!opUser.getId().equals(userId))
            throw new UserNotFoundException("User does not exists");

        updateNoteFromDTO(opNote,noteDTO);
        opNote=noteRepo.save(opNote);
        return MapperService.getNoteDTO(opNote);
    }

    private void updateNoteFromDTO(Note note,NoteDTO noteDTO){
        note.setCode(noteDTO.getCode());
        note.setContent(noteDTO.getContent());
        note.setTitle(noteDTO.getTitle());
    }

    @Override
    public NoteDTO getNoteById(UUID noteId) {
        Optional<Note> opNote=noteRepo.findById(noteId);
        if(opNote.isEmpty())
        {
            throw new ResourceNotFoundException("Note does not exists");
        }
        return MapperService.getNoteDTO(opNote.get());
    }

    @Override
    public void deleteNoteById(UUID noteId) {
        Optional<Note> opNote=noteRepo.findById(noteId);
        if(opNote.isEmpty())
        {
            throw new ResourceNotFoundException("Note does not exists");
        }
        noteRepo.delete(opNote.get());
    }

    //Get Notes for give noteSet with pagination and sorting
    @Override
    @Transactional
    public List<NoteDTO> getNotes(UUID userId, UUID noteSetId,PageSortDTO pageSortDTO) {
        // Validate user and noteSet association
        if(!noteSetRepo.existsByUserIdAndNoteSetId(userId,noteSetId))
            throw new NoteSetUnauthorizedException("User or NoteSet not found");

        Pageable pageable= generatePageable(pageSortDTO);
        Page<Note> notePage=noteRepo.findAllByUserIdAndNoteSetId(userId,noteSetId,pageable);
        return notePage.getContent().stream()
                                    .map(MapperService::getNoteDTO)
                                    .toList();

    }

    //Getting notes for user
    @Override
    @Transactional
    public List<NoteDTO> getNotes(UUID userId, PageSortDTO pageSortDTO) {
        if(!clientRepo.existsById(userId))
            throw new UserNotFoundException("User not found");
        Pageable pageable=generatePageable(pageSortDTO);

        Page<Note> notePage=noteRepo.findByUserId(userId,pageable);
        return notePage.getContent()
                .stream()
                .map(note -> MapperService.getNoteDTO(note))
                .toList();
    }

    @Override
    public void deleteNoteByIds(UUID userId,List<UUID> noteIds) {
        // Validate input
        if (CollectionUtils.isEmpty(noteIds)) {
            throw new IllegalArgumentException("The list of note IDs cannot be null or empty.");
        }
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null.");
        }
        // Fetch notes to delete
        final List<Note> notesToDelete = noteRepo.findByUserIdAndIdIn(userId, noteIds);
        // Perform batch deletion
        noteRepo.deleteAllInBatch(notesToDelete);
    }

    @Override
    @Transactional
    public Integer assignNoteSetToNotes(List<UUID> noteIds, UUID noteSetId,Boolean unload) {
        UUID userId = noteSetRepo.getUserIdForNoteSet(noteSetId);
        if(userId==null)
            throw new ResourceNotFoundException("Notes Set Not found with notSetId:" +noteSetId);
        if(unload)
            noteSetId=null;
        int updatedRows=noteRepo.updateNoteSetIdForMultipleNotes(userId,noteSetId,noteIds);
        return updatedRows;
    }

    private Pageable generatePageable(PageSortDTO pageSortDTO){
        Sort sort=Sort.
                by(pageSortDTO.isAsc()?Sort.Direction.ASC: Sort.Direction.DESC, pageSortDTO.getArg());

        return PageRequest.of(pageSortDTO.getPageNo(), pageSortDTO.getPageSize(),sort);
    }
}
