package Blind.Sight.community.domain.service.write;

import Blind.Sight.community.domain.entity.Point;
import Blind.Sight.community.domain.entity.Profile;
import Blind.Sight.community.domain.entity.User;
import Blind.Sight.community.domain.entity.address.Address;
import Blind.Sight.community.domain.repository.postgresql.AddressRepositoryPg;
import Blind.Sight.community.domain.repository.postgresql.PointRepositoryPg;
import Blind.Sight.community.domain.repository.postgresql.ProfileRepositoryPg;
import Blind.Sight.community.domain.service.system.PointService;
import Blind.Sight.community.dto.profile.ProfileInput;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProfileServicePg {
    private final ProfileRepositoryPg profileRepositoryPg;
    private final PointRepositoryPg pointRepositoryPg;
    private final AddressRepositoryPg addressRepositoryPg;
    private final PointService pointService;

    public Profile findProfileByUserId(String userId) {
        Profile existProfile = profileRepositoryPg.findByUserId(userId).orElseThrow(
                () -> new NullPointerException("")
        );

        log.info("Found this profile");
        return existProfile;
    }

    public void createProfile(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            User userLogin = (User) authentication.getPrincipal();

            Profile profile = new Profile();
            profile.setUser(userLogin);
            profile.setTotalPoint(0.0);
            profileRepositoryPg.save(profile);
            log.info("Save profile success");

            Point point = pointService.createDefaultPoint(profile);

            profile.setTotalPoint(point.getPointExchange());
            profileRepositoryPg.save(profile);
            log.info("Update profile success");

            log.info("Create profile success");

        } else {
            log.error("Authentication failed");
        }
    }

    public void updateProfile(Authentication authentication, ProfileInput profileInput) {
        if (authentication != null && authentication.isAuthenticated()) {
            User userLogin = (User) authentication.getPrincipal();

            Profile existProfile = findProfileByUserId(userLogin.getUserId());
            existProfile.setUpdateAt(LocalDate.now());
            existProfile.setPhone(profileInput.getPhone());

            Address address = new Address();
            address.setAddressName(profileInput.getAddress());
            addressRepositoryPg.save(address);
            log.info("Create address success");

            profileRepositoryPg.save(existProfile);
            log.info("Update profile success");
        }
    }

    public void updateProfileOnlyPoint(Authentication authentication, ProfileInput profileInput) {
        if (authentication != null && authentication.isAuthenticated()) {
            User userLogin = (User) authentication.getPrincipal();

            Profile existProfile = findProfileByUserId(userLogin.getUserId());
            existProfile.setUpdateAt(LocalDate.now());

            Point point = pointService.createPoint(existProfile,
                    profileInput.getPointExchange(), profileInput.getDecimalPlace(), profileInput.getExchangeValue());


        }
    }
}
