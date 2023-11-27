package Blind.Sight.community.domain.service.system;

import Blind.Sight.community.domain.entity.Point;
import Blind.Sight.community.domain.entity.Profile;
import Blind.Sight.community.domain.repository.postgresql.PointRepositoryPg;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PointService {
    private final PointRepositoryPg pointRepositoryPg;

    public Point findByPointId(String pointId) {
        Point existPoint = pointRepositoryPg.findById(pointId).orElseThrow(
                () -> new NullPointerException("Not found this point: " + pointId)
        );

        log.info("Found this point");
        return existPoint;
    }

    public Point createDefaultPoint(Profile profile) {
        Point point = new Point(500.0,0.0,0.0);
        point.setProfile(profile);
        pointRepositoryPg.save(point);

        log.info("Save point success");
        return point;
    }

    public Point createPoint(Profile profile, Double pointExchange, Double decimalPlace, Double exchangeValue) {
        Point point = new Point(pointExchange,decimalPlace,exchangeValue);
        point.setProfile(profile);

        log.info("Save point success");
        return point;
    }

    public Point updatePoint(String pointId, Double pointExchange, Double decimalPlace, Double exchangeValue) {
        Point existPoint = findByPointId(pointId);
        existPoint.setPointExchange(pointExchange);
        existPoint.setDecimalPlace(decimalPlace);
        existPoint.setExchangeValue(exchangeValue);

        log.info("Update point success");
        return existPoint;
    }

    public void deletePoint(String pointId) {
        Point existPoint = findByPointId(pointId);
        pointRepositoryPg.delete(existPoint);

        log.info("Delete point success");
    }

}
