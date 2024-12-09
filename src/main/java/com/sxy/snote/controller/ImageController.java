package com.sxy.snote.controller;

import com.sxy.snote.dto.ImageDTO;
import com.sxy.snote.dto.PageSortDTO;
import com.sxy.snote.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/images")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @PostMapping("/{noteId}")
    private ResponseEntity<?>saveImage(@PathVariable("noteId")UUID noteId, @RequestParam("image")MultipartFile image)
    {
        if (image.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No file uploaded");
        }
        ImageDTO imageDTO=imageService.uploadImage(image,noteId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(imageDTO);
    }

    @PostMapping("/{noteId}/multi")
    private ResponseEntity<?>saveImages(@PathVariable("noteId")UUID noteId, @RequestParam("images")List<MultipartFile>images)
    {
        if (images.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No file uploaded");
        }
        List<ImageDTO> imageDTOList=imageService.uploadImages(images,noteId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(imageDTOList);
    }

    @GetMapping("/{imageId}")
    private ResponseEntity<?>getImage(@PathVariable("imageId")UUID imageId)
    {
        ImageDTO imageDTO=imageService.getImage(imageId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(imageDTO);
    }

    @GetMapping("/all/{noteId}/")
    private ResponseEntity<List<ImageDTO>>getImagesUsingNoteId(@PathVariable("noteId")UUID noteId)
    {
        List<ImageDTO> images=imageService.getImagesForNote(noteId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(images);
    }

    @GetMapping("/{noteId}/")
    private ResponseEntity<List<ImageDTO>>getPageImageUsingNoteId(@PathVariable("noteId")UUID noteId,
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

        List<ImageDTO> images=imageService.getPageImagesForNote(noteId,pageSortDTO);
        return ResponseEntity.status(HttpStatus.OK)
                .body(images);
    }

    @DeleteMapping("/{noteId}/{imageId}")
    private ResponseEntity<?>deleteImage(@PathVariable("noteId")UUID noteId,@PathVariable("imageId")UUID imageId)
    {
        imageService.deleteImage(imageId,noteId);
        return ResponseEntity.status(HttpStatus.OK)
                .body("deleted successfully.");
    }

    @DeleteMapping("/multi/{noteId}/")
    private ResponseEntity<?>deleteImage(@PathVariable("noteId")UUID noteId,@RequestParam("imageIds")List<UUID> imageIds)
    {
        imageService.deleteImages(imageIds,noteId);
        return ResponseEntity.status(HttpStatus.OK)
                .body("deleted successfully.");
    }

}
