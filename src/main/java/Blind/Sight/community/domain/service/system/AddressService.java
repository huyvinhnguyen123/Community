package Blind.Sight.community.domain.service.system;

import Blind.Sight.community.domain.entity.address.City;
import Blind.Sight.community.domain.entity.address.District;
import Blind.Sight.community.domain.entity.address.Ward;
import Blind.Sight.community.domain.repository.postgresql.AddressRepositoryPg;
import Blind.Sight.community.domain.repository.postgresql.CityRepositoryPg;
import Blind.Sight.community.domain.repository.postgresql.DistrictRepositoryPg;
import Blind.Sight.community.domain.repository.postgresql.WardRepositoryPg;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AddressService {
    private final AddressRepositoryPg addressRepositoryPg;
    private final CityRepositoryPg cityRepositoryPg;
    private final DistrictRepositoryPg districtRepositoryPg;
    private final WardRepositoryPg wardRepositoryPg;

    public Iterable<City> findAllCities() {
        Iterable<City> cities = cityRepositoryPg.findAll();
        log.info("Find all cities success");
        return cities;
    }

    public Iterable<District> findAllDistricts() {
        Iterable<District> districts = districtRepositoryPg.findAll();
        log.info("Find all districts success");
        return districts;
    }

    public Iterable<Ward> findAllWards() {
        Iterable<Ward> wards = wardRepositoryPg.findAll();
        log.info("Find all wards success");
        return wards;
    }
}
