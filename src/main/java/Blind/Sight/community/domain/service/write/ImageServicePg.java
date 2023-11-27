package Blind.Sight.community.domain.service.write;

import Blind.Sight.community.domain.entity.Image;
import Blind.Sight.community.domain.entity.Type;
import Blind.Sight.community.domain.repository.postgresql.ImageRepositoryPg;
import Blind.Sight.community.domain.service.custom.GoogleDriveService;
import Blind.Sight.community.domain.service.system.TypeService;
import com.google.api.services.drive.model.File;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ImageServicePg {
    private final ImageRepositoryPg imageRepositoryPg;
    private final TypeService typeService;
    private final GoogleDriveService googleDriveService;

    @Async
    public Image findImageById(String imageId) {
        Image existImage = imageRepositoryPg.findById(imageId).orElseThrow(
                () -> new NullPointerException("Not found this image: " + imageId)
        );

        log.info("Found image");
        return existImage;
    }

    @Async
    public Boolean checkImageByName(String imageName) {
        Optional<Image> image = imageRepositoryPg.findByImageName(imageName);
        return image.isPresent();
    }

    public Image createImage(MultipartFile file, String path, String type) throws GeneralSecurityException, IOException {
        String originalFilename = file.getOriginalFilename();
        File fileDrive = googleDriveService.uploadToDrive(file, path);

        Image image = new Image();
        image.setImagePathId(fileDrive.getId());
        image.setImagePath(fileDrive.getWebContentLink());

        boolean imageNameExists = false;
        for(Image imageRecord: imageRepositoryPg.findAll()) {
            if (imageRecord.getImageName().equals(originalFilename)) {
                imageNameExists = true;
                break;
            }
        }

        if (imageNameExists) {
            String baseName = FilenameUtils.getBaseName(originalFilename);
            String extension = FilenameUtils.getExtension(originalFilename);

            int counter = 1;
            String newFileName = originalFilename;
            while (Boolean.TRUE.equals(checkImageByName(newFileName))) {
                newFileName = String.format("%s(%d).%s", baseName, counter, extension);
                counter++;
            }

            image.setImageName(newFileName);
        } else {
            image.setImageName(originalFilename);
        }

        log.info(image.getImageId());

        Type existType = typeService.findTypeByName(type);
        image.setType(existType);

        imageRepositoryPg.save(image);
        log.info(image.getImageId());

        log.info("create image success!");
        return image;
    }

    public void updateReplaceImage(String imageId, MultipartFile file, String path) throws GeneralSecurityException, IOException {
        Image existImage = findImageById(imageId);
        googleDriveService.deleteFileFromDrive(existImage.getImagePathId());

        String originalFilename = file.getOriginalFilename();
        existImage.setImageName(originalFilename);

        File fileDrive = googleDriveService.uploadToDrive(file, path);

        existImage.setImagePathId(fileDrive.getId());
        existImage.setImagePath(fileDrive.getWebContentLink());
        imageRepositoryPg.save(existImage);

        log.info("update image success!");
    }

    public void deleteImage(String imageId) throws GeneralSecurityException, IOException {
        Image existImage = findImageById(imageId);
        googleDriveService.deleteFileFromDrive(existImage.getImagePathId());
        imageRepositoryPg.delete(existImage);

        log.info("Delete image success!");
    }
}
