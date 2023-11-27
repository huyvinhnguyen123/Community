package Blind.Sight.community.domain.service.custom;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.media.MediaHttpUploader;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Service
@Slf4j
public class GoogleDriveService {
    @Value("${drive.application.name}")
    private String applicationName;
    @Value("${drive.credentials.file}")
    private String credentialsFilePath;
    @Value("${drive.token.directory}")
    private java.io.File tokenDirectoryPath;
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    /**
     * Load client secret key from credential json file
     *
     * @return - secret key
     * @throws IOException - error
     */
    private GoogleClientSecrets loadClientSecrets() throws IOException {
        InputStream inputStream = GoogleDriveService.class.getResourceAsStream(credentialsFilePath);
        if (inputStream != null) {
            return GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(inputStream));
        }
        return new GoogleClientSecrets();
    }

    /**
     * Get drive service
     *
     * @return - drive after built
     * @throws IOException - error
     * @throws GeneralSecurityException - error
     */
    private Drive getDriveService() throws IOException, GeneralSecurityException {
        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        GoogleClientSecrets clientSecrets = loadClientSecrets();

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, JSON_FACTORY, clientSecrets, Collections.singletonList(DriveScopes.DRIVE))
                .setDataStoreFactory(new FileDataStoreFactory(tokenDirectoryPath))
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setHost("localhost").setPort(2222).build();
        Credential credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize("atifarlunar.official@gmail.com");

        return new Drive.Builder(httpTransport, JSON_FACTORY, credential)
                .setApplicationName(applicationName)
                .build();
    }

    /**
     * Upload file to drive
     *
     * @param file - input file(image, file)
     * @return -  file after upload
     * @throws IOException - error
     * @throws GeneralSecurityException -error
     */
    public File uploadToDrive(MultipartFile file, String folderId) throws NullPointerException, IOException, GeneralSecurityException {
        Drive drive = getDriveService();
        String fileName = file.getOriginalFilename();
        String mimeType = file.getContentType();

        File fileMetadata = new File();

        if(fileExistsInDrive(folderId, fileName)) {
            String baseName = FilenameUtils.getBaseName(fileName);
            String extension = FilenameUtils.getExtension(fileName);
            // Check if the file with the same name already exists
            int counter = 1;
            while (fileExistsInDrive(folderId, fileName)) {
                fileName = String.format("%s(%d).%s", baseName, counter, extension);
                counter++;
            }
            fileMetadata.setName(fileName);
        } else {
            fileMetadata.setName(fileName);
        }

        fileMetadata.setMimeType(mimeType);
        fileMetadata.setParents(Collections.singletonList(folderId));

        InputStreamContent mediaContent = new InputStreamContent(mimeType, file.getInputStream());
        Drive.Files.Create createRequest = drive.files().create(fileMetadata, mediaContent).setFields("id, webContentLink");
        MediaHttpUploader uploader = createRequest.getMediaHttpUploader();
        uploader.setDirectUploadEnabled(false);

        log.info("upload file success!");
        return createRequest.execute();
    }

    /**
     * Delete file in drive
     *
     * @param fileId - input drive's id
     * @throws IOException - error
     * @throws GeneralSecurityException - error
     */
    public void deleteFileFromDrive(String fileId) throws IOException, GeneralSecurityException {
        Drive driveService = getDriveService();
        if(driveService.files().get(fileId).isEmpty()) {
            log.info("File not found on drive.");
            log.error("An error occurred while deleting the file: 404 Not Found.");
        } else {
            driveService.files().delete(fileId).execute();
            log.info("File deleted successfully.");
        }
    }

    /**
     * Check if file is existed on drive
     *
     * @param folderId - input folder's id
     * @param fileName - input file's name
     * @return - boolean
     * @throws GeneralSecurityException - error
     * @throws IOException - error
     */
    private boolean fileExistsInDrive(String folderId, String fileName) throws GeneralSecurityException, IOException {
        Drive driveService = getDriveService();
        // Use the Drive API to search for files with the same name in the specified folder
        FileList result = driveService.files().list()
                .setQ("'" + folderId + "' in parents and name='" + fileName + "'")
                .execute();
        return result.getFiles() != null && !result.getFiles().isEmpty();
    }
}
