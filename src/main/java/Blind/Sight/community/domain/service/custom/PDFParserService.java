package Blind.Sight.community.domain.service.custom;

import Blind.Sight.community.domain.entity.Type;
import Blind.Sight.community.domain.entity.temporary.File;
import Blind.Sight.community.domain.repository.postgresql.FileRepositoryPg;
import Blind.Sight.community.domain.service.system.TypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.GeneralSecurityException;

@Service
@Slf4j
@RequiredArgsConstructor
public class PDFParserService {
    private final FileRepositoryPg fileRepositoryPg;
    private final TypeService typeService;
    private final GoogleDriveService googleDriveService;
    @Value("${drive.folder.files}")
    private String filePath;

    public void importPDFFile(MultipartFile file) throws IOException, GeneralSecurityException {
        String fileName = file.getOriginalFilename();
        com.google.api.services.drive.model.File fileDrive = googleDriveService.uploadToDrive(file, filePath);
        File fileRecord = new File(fileName, file.getSize(), fileDrive.getWebContentLink());

        Type existType = typeService.findTypeByName("file");
        fileRecord.setType(existType);

        fileRepositoryPg.save(fileRecord);
        log.info("Save new file success");

        assert fileName != null;
        String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);
        if (!fileExtension.equalsIgnoreCase("pdf")) {
            throw new FileNotFoundException("Invalid file format. Only PDF files are allowed.");
        }
    }
}
