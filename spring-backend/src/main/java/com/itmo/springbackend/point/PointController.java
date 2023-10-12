package com.itmo.springbackend.point;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/points")
public class PointController {
    private final PointService pointService;

    @Autowired
    public PointController(PointService pointService) {
        this.pointService = pointService;
    }


    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<Point>> getAllPoints() {
        return new ResponseEntity<>(
                pointService.getAllPoints(),
                HttpStatus.OK
        );
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Point> getSpecificPoint(@PathVariable("id") UUID id) {
        try {
            return new ResponseEntity<>(
                    pointService.getPointById(id),
                    HttpStatus.OK
            );
        } catch (PointNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createNewPoint(@RequestBody @Valid final Point point) {
        final Point processedPoint = pointService.processPoint(point);
        return new ResponseEntity<>(processedPoint, HttpStatus.OK);
    }

}
