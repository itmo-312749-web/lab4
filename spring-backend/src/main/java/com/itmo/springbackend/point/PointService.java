package com.itmo.springbackend.point;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;

@Service
public class PointService {
    private final PointRepository pointRepository;

    @Autowired
    public PointService(PointRepository pointRepository) {
        this.pointRepository = pointRepository;
    }

    public Collection<Point> getAllPoints() {
        return pointRepository.findAll();
    }

    public Point getPointById(UUID id) throws PointNotFoundException {
        return pointRepository.findById(id)
                .orElseThrow(() -> new PointNotFoundException("Point with id " + id + " does not exist."));
    }

    public Point processPoint(final Point point) {
        point.check();
        point.setCreatedAt(LocalDateTime.now());
        save(point);
        return point;
    }

    public void save(Point point) {
        pointRepository.save(point);
    }

}
