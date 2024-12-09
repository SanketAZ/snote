package com.sxy.snote.service;

import com.sxy.snote.dto.ImageDTO;
import com.sxy.snote.dto.PageSortDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface ImageService {
    ImageDTO uploadImage(MultipartFile file, UUID imageId);
    List<ImageDTO> uploadImages(List<MultipartFile> files, UUID imageId);
    ImageDTO getImage(UUID imageId);
    List<ImageDTO> getImagesForNote(UUID noteId);
    void deleteImage(UUID imageId,UUID noteId);
    List<ImageDTO>getPageImagesForNote(UUID noteId, PageSortDTO pageSortDTO);
    void deleteImages(List<UUID> imageIds, UUID noteId);
}
