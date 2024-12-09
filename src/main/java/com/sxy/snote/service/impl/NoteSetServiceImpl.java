package com.sxy.snote.service.impl;

import com.sxy.snote.dto.NoteDTO;
import com.sxy.snote.dto.NoteSetDisplayDTO;
import com.sxy.snote.dto.PageSortDTO;
import com.sxy.snote.exception.NoteSetException;
import com.sxy.snote.exception.NoteSetUnauthorizedException;
import com.sxy.snote.exception.UserNotFoundException;
import com.sxy.snote.helper.MapperService;
import com.sxy.snote.model.Client;
import com.sxy.snote.model.NoteSet;
import com.sxy.snote.repository.ClientRepo;
import com.sxy.snote.repository.NoteSetRepo;
import com.sxy.snote.service.NoteSetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class NoteSetServiceImpl implements NoteSetService {
    @Autowired
    private ClientRepo clientRepo;
    @Autowired
    private NoteSetRepo noteSetRepo;

    //create new noteSet for given user
    @Override
    public NoteSetDisplayDTO createNoteSet(UUID userId,NoteSetDisplayDTO NoteSetDisplayDTO) {
        Client user=clientRepo.findById(userId).orElseThrow(()->new UserNotFoundException("User does not exists"));
        NoteSet noteSet= MapperService.getNoteSet(NoteSetDisplayDTO);
        noteSet.setUser(user);

        NoteSet createdNoteSet=noteSetRepo.save(noteSet);
        return MapperService.getNoteSetDisplayDTO(createdNoteSet);
    }

    @Override
    public void deleteNoteSet(UUID userId, UUID noteSetId) {
        if(!clientRepo.existsById(userId))
            throw new UserNotFoundException("User does not exists");

        NoteSet noteSet=noteSetRepo.findById(noteSetId)
                .orElseThrow(()->new NoteSetException("NoteSet not found"));
        noteSetRepo.delete(noteSet);
    }

    @Override
    public NoteSetDisplayDTO updateNoteSet(UUID userId, UUID noteSetId, NoteSetDisplayDTO noteSetDisplayDTO) {

        // Validate user and noteSet association
        NoteSet noteSet = noteSetRepo.findByIdAndUserId(noteSetId, userId)
                .orElseThrow(() -> new NoteSetUnauthorizedException("User or NoteSet not found"));
        //Update name
        noteSet.setName(noteSetDisplayDTO.getName());

        NoteSet updatedNoteSet=noteSetRepo.save(noteSet);
        return MapperService.getNoteSetDisplayDTO(updatedNoteSet);
    }

    //Getting NoteSet list for display with pagination
    @Override
    public List<NoteSetDisplayDTO> getNoteSetsDisplay(UUID userId, PageSortDTO pageSortDTO) {
        if(!clientRepo.existsById(userId))
            throw new UserNotFoundException("User does not exists");

        Pageable pageable= generatePageable(pageSortDTO);
        Page<NoteSet> noteSetPage=noteSetRepo.findAllByUserId(userId,pageable);
        return noteSetPage.stream()
                .map((MapperService::getNoteSetDisplayDTO))
                .toList();
    }

    private Pageable generatePageable(PageSortDTO pageSortDTO){
        Sort sort=Sort.
                by(pageSortDTO.isAsc()?Sort.Direction.ASC: Sort.Direction.DESC, pageSortDTO.getArg());

        return PageRequest.of(pageSortDTO.getPageNo(), pageSortDTO.getPageSize(),sort);
    }
}
