package Blind.Sight.community.domain.service.custom;

import Blind.Sight.community.config.security.PasswordEncrypt;
import Blind.Sight.community.domain.entity.Category;
import Blind.Sight.community.domain.entity.Type;
import Blind.Sight.community.domain.entity.User;
import Blind.Sight.community.domain.entity.address.City;
import Blind.Sight.community.domain.entity.address.District;
import Blind.Sight.community.domain.entity.address.Ward;
import Blind.Sight.community.domain.entity.temporary.File;
import Blind.Sight.community.domain.repository.postgresql.*;
import Blind.Sight.community.domain.service.system.DataStatisticService;
import Blind.Sight.community.domain.service.system.TypeService;
import Blind.Sight.community.dto.statistic.UserDataStatistic;
import Blind.Sight.community.util.common.DeleteFlag;
import Blind.Sight.community.util.common.LockFlag;
import Blind.Sight.community.util.common.RoleData;
import Blind.Sight.community.util.format.CustomDateTimeFormatter;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.security.GeneralSecurityException;
import java.util.ArrayList;

@Service
@Slf4j
@RequiredArgsConstructor
public class CSVParserService {
    private final UserRepositoryPg userRepositoryPg;
    private final CategoryRepositoryPg categoryRepositoryPg;
    private final FileRepositoryPg fileRepositoryPg;
    private final CityRepositoryPg cityRepositoryPg;
    private final DistrictRepositoryPg districtRepositoryPg;
    private final WardRepositoryPg wardRepositoryPg;
    private final TypeService typeService;
    private final DataStatisticService dataStatisticService;
    private final GoogleDriveService googleDriveService;
    @Value("${drive.folder.files}")
    private String filePath;

    public void importCSVFile(MultipartFile file, String type) throws IOException, CsvValidationException, GeneralSecurityException {
        String fileName = file.getOriginalFilename();
        com.google.api.services.drive.model.File fileDrive = googleDriveService.uploadToDrive(file, filePath);
        File fileRecord = new File(fileName, file.getSize(), fileDrive.getWebContentLink());

        Type existType = typeService.findTypeByName("file");
        fileRecord.setType(existType);

        fileRepositoryPg.save(fileRecord);
        log.info("Save new file success");

        assert fileName != null;
        String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);
        if (!fileExtension.equalsIgnoreCase("csv")) {
            throw new FileNotFoundException("Invalid file format. Only CSV files are allowed.");
        }

        switch (type) {
            case "user":
                parseUserImport(file.getInputStream());
                log.info("import user's data success");
                break;
            case "city":
                parseCityImport(file.getInputStream());
                log.info("import city's data success");
                break;
            case "district":
                parseDistrictImport(file.getInputStream());
                log.info("import district's data success");
                break;
            case "ward":
                parseWardImport(file.getInputStream());
                log.info("import ward's data success");
                break;
            case "category":
                parseCategoryImport(file.getInputStream());
                log.info("import category's data success");
                break;
            default:
                log.info("No type was found in this case");
                break;
        }
    }

    public void exportCSVFile(Writer writer, String type, String time) throws IOException {
        Iterable<UserDataStatistic> userList = new ArrayList<>();
        String title = "user-" + time;

        if (type.equals("user")) {
            if (time.equals("today")) {
                userList = dataStatisticService.statisticUsersToday();
            }

            if (time.equals("week")) {
                userList = dataStatisticService.statisticUsersThisWeek();
            }

            if (time.equals("month")) {
                userList = dataStatisticService.statisticUsersThisMonth();
            }

            if (time.equals("year")) {
                userList = dataStatisticService.statisticUsersThisYear();
            }

            parseUserExport(writer, title, userList);
        }
    }

    private void parseUserImport(InputStream inputStream) throws IOException, CsvValidationException {
        CSVReader csvReader = new CSVReaderBuilder(new InputStreamReader(inputStream)).build();
        csvReader.skip(1);
        String[] line;
        while ((line = csvReader.readNext()) != null) {
            // Data in CSV file
            String email = line[0];
            String username = line[1];
            String birthday = line[2];
            String password = line[3];

            // Create a new user instance and populate fields
            User user = new User();
            user.setEmail(email);
            user.setUserName(username);
            user.setBirthDate(CustomDateTimeFormatter.dateOfBirthFormatter(birthday));
            user.setPassword(PasswordEncrypt.bcryptPassword(password));
            user.setRole(RoleData.USER.getRole());
            user.setLockFlag(LockFlag.NON_LOCK.getCode());
            user.setDeleteFlag(DeleteFlag.NON_DELETE.getCode());

            userRepositoryPg.save(user);
        }
    }

    private void parseUserExport(Writer writer, String title, Iterable<UserDataStatistic> userList) throws IOException {
        CSVWriter csvWriter = new CSVWriter(writer);

        csvWriter.writeNext(new String[]{title});
        csvWriter.writeNext(new String[]{
                "NAME","BIRTHDATE","EMAIL"
        });

        // write data
        for(UserDataStatistic userDataStatistic: userList) {
            String[] data = {
                    userDataStatistic.getName(),
                    userDataStatistic.getBirthDate().toString(),
                    userDataStatistic.getEmail()
            };
            csvWriter.writeNext(data);
        }
        log.info("Export file csv success");
        csvWriter.close();
    }

    private void parseCityImport(InputStream inputStream) throws IOException, CsvValidationException {
        CSVReader csvReader = new CSVReaderBuilder(new InputStreamReader(inputStream)).build();
        csvReader.skip(1);
        String[] line;
        while ((line = csvReader.readNext()) != null) {
            // Data in CSV file
            String cityName = line[0];

            // Create a new user instance and populate fields
            City city = new City();
            city.setCityName(cityName);

            cityRepositoryPg.save(city);
        }
    }

    private void parseDistrictImport(InputStream inputStream) throws IOException, CsvValidationException {
        CSVReader csvReader = new CSVReaderBuilder(new InputStreamReader(inputStream)).build();
        csvReader.skip(1);
        String[] line;
        while ((line = csvReader.readNext()) != null) {
            // Data in CSV file
            String districtName = line[0];
            String cityName = line[1];

            // Create a new user instance and populate fields
            District district = new District();
            district.setDistrictName(districtName);

            for(City city: cityRepositoryPg.findAll()) {
                if(city.getCityName().equals(cityName)) {
                    district.setCity(city);
                }
            }

            districtRepositoryPg.save(district);
        }
    }

    private void parseWardImport(InputStream inputStream) throws IOException, CsvValidationException {
        CSVReader csvReader = new CSVReaderBuilder(new InputStreamReader(inputStream)).build();
        csvReader.skip(1);
        String[] line;
        while ((line = csvReader.readNext()) != null) {
            // Data in CSV file
            String wardName = line[0];
            String districtName = line[1];

            // Create a new user instance and populate fields
            Ward ward = new Ward();
            ward.setWardName(wardName);

            for(District district: districtRepositoryPg.findAll()) {
                if(district.getDistrictName().equals(districtName)) {
                    ward.setDistrict(district);
                }
            }

            wardRepositoryPg.save(ward);
        }
    }

    private void parseCategoryImport(InputStream inputStream) throws IOException, CsvValidationException {
        CSVReader csvReader = new CSVReaderBuilder(new InputStreamReader(inputStream)).build();
        csvReader.skip(1);
        String[] line;
        while ((line = csvReader.readNext()) != null) {
            // Data in CSV file
            String categoryName = line[0];

            // Create a new user instance and populate fields
            Category category = new Category();
            category.setCategoryName(categoryName);

            categoryRepositoryPg.save(category);
        }
    }
}
