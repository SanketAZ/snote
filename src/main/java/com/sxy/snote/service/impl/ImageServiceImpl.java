package com.sxy.snote.service.impl;

import com.sxy.snote.dto.ImageDTO;
import com.sxy.snote.dto.PageSortDTO;
import com.sxy.snote.exception.*;
import com.sxy.snote.helper.MapperService;
import com.sxy.snote.model.Image;
import com.sxy.snote.model.Note;
import com.sxy.snote.repository.ImageRepo;
import com.sxy.snote.repository.NoteRepo;
import com.sxy.snote.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ImageServiceImpl implements ImageService {
    @Autowired
    private ImageRepo imageRepo;
    @Autowired
    private NoteRepo noteRepo;

    @Value("${file.upload-dir}")
    private String FilePath;

    @Override
    public ImageDTO uploadImage(MultipartFile file, UUID noteId) {
        String uniqueName= UUID.randomUUID().toString();
        String path=FilePath+uniqueName+file.getOriginalFilename();
        Image savedImage=imageRepo.save(
                buildImage(file,path,noteId));
        return MapperService.getImageDTO(savedImage);
    }

    @Override
    public List<ImageDTO> uploadImages(List<MultipartFile> files, UUID noteId) {
        if(files.size()>10)
            throw new TooManyImagesException("More than 10 images uploaded");
        if(files.isEmpty())
            throw new NoImagesUploadedException("images Not uploaded");

        List<Image> unsavedImages=files.stream().map((file -> {
            String uniqueName= UUID.randomUUID().toString();
            String path=FilePath+uniqueName+file.getOriginalFilename();
            return buildImage(file,path,noteId);
        })).toList();

        List<Image> savedImages=imageRepo.saveAll(unsavedImages);
        return savedImages.stream().map(MapperService::getImageDTO).toList();
    }

    private Image buildImage(MultipartFile file,String path,UUID noteId) {
        try {
            file.transferTo(new File(path));
        } catch (IOException e) {
            throw new ImageUploadException(e.getMessage());
        }

        Note note=noteRepo.findById(noteId).orElseThrow(()->new ResourceNotFoundException("Note not found"));
        Image image=Image.builder().name(file.getOriginalFilename())
                .link(path)
                .note(note)
                .build();
        return image;
    }


    @Override
    public ImageDTO getImage(UUID imageId) {
        Optional<Image>imageOptional=imageRepo.findById(imageId);

        if(imageOptional.isEmpty()) {
            throw new ImageGetException("Image not found");
        }
        return MapperService.getImageDTO(imageOptional.get());
    }

    @Override
    public List<ImageDTO> getImagesForNote(UUID noteId) {
        Optional<Note> noteOptional=noteRepo.findById(noteId);
        if(noteOptional.isEmpty())
            throw new ResourceNotFoundException("Note not found");
        List<Image> images=imageRepo.findAllByNoteId(noteId);
        return images.stream().map(MapperService::getImageDTO).toList();
    }

    @Override
    public void deleteImage(UUID imageId, UUID noteId) {
        //Find note by id
        Note note=noteRepo.findById(noteId)
                .orElseThrow(()->new ResourceNotFoundException("Note not found"));

        //Find Image by id
        Image image=imageRepo.findById(imageId)
                .orElseThrow(()->new ResourceNotFoundException("Image not found"));

        if(!image.getNote().getId().equals(note.getId())) {
            throw new ImageUnauthorizedException("Unauthorized to delete this image");
        }

        File file=new File(image.getLink());

        if (file.exists() && file.isFile()) {
            if(!file.delete()) {
                throw  new FileDeletionException("Failed to delete the file");
            }
        }else {
            throw new NotFileException("This is not a file");
        }
        imageRepo.delete(image);
    }

    @Override
    public List<ImageDTO> getPageImagesForNote(UUID noteId, PageSortDTO pageSortDTO) {
        //Find note by id
        Note note=noteRepo.findById(noteId)
                .orElseThrow(()->new ResourceNotFoundException("Note not found"));

        Sort sort= Sort.by(pageSortDTO.isAsc()?Sort.Direction.ASC:Sort.Direction.DESC, pageSortDTO.getArg());
        Pageable pageable =PageRequest.of(pageSortDTO.getPageNo(),pageSortDTO.getPageSize(),sort);
        Page<Image>imagePage=imageRepo.findAllByNoteId(noteId,pageable);
        List<Image> images=imagePage.getContent();
        return images.stream().map(MapperService::getImageDTO).toList();
    }

    @Override
    public void deleteImages(List<UUID> imageIds, UUID noteId) {
        //Find note by id
        Note note=noteRepo.findById(noteId)
                .orElseThrow(()->new ResourceNotFoundException("Note not found"));

        if(imageIds.isEmpty())
            throw new NoImagesUploadedException("No images uploaded for deletion");

        List<Image> images=new ArrayList<>();
        for(var imageId:imageIds){
            Image image=imageRepo.findById(imageId)
                    .orElseThrow(()->new ResourceNotFoundException("Image not found"));
            images.add(image);
            if(!image.getNote().getId().equals(note.getId())) {
                throw new ImageUnauthorizedException("Unauthorized to delete this image");
            }
        }
        for(var image:images){
            File file=new File(image.getLink());
            if (file.exists() && file.isFile()) {
                if(!file.delete()) {
                    throw  new FileDeletionException("Failed to delete the file");
                }
            }else {
                throw new NotFileException("This is not a file");
            }
        }
        imageRepo.deleteAllInBatch(images);
    }
}
