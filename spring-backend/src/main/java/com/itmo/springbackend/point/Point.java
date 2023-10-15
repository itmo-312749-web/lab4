package com.itmo.springbackend.point;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.itmo.springbackend.point.check.Checker;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table
@Getter
public class Point {

    @Transient
    private static final List<Checker> checkers = List.of(
            (Point point) -> (-point.getX() / 2) <= point.getX() && point.getX() <= 0 &&
                    0 <= point.getY() && point.getY() <= point.getR(),
            (Point point) -> 0 <= point.getX() && point.getX() <= (point.getR() / 2) &&
                    0 <= point.getY() && point.getY() <= (point.getR() / 2) &&
                    point.getX() <= -point.getY() + (point.getR() / 2),
            (Point point) -> 0 <= point.getX() && point.getX() <= point.getR() / 2 &&
                    -point.getR() / 2 <= point.getY() && point.getY() <= 0 &&
                    (point.getX() * point.getX()) + (point.getY() * point.getY()) <= (point.getR() * point.getR())
    );

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id;

    @Min(value = -5, message = "X value is too negative")
    @Max(value = 5, message = "X value is too positive")
    private double x;

    @Min(value = -5, message = "Y value is too negative")
    @Max(value = 5, message = "Y value is too positive")
    private double y;

    @Min(value = 0, message = "R value is too negative")
    @Max(value = 5, message = "R value is too positive")
    private double r;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Boolean hit;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long computationTime;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime createdAt;

    public Point() {

    }

    public Point(double x, double y, double r) {
        this.x = x;
        this.y = y;
        this.r = r;
    }


    private void setX(double x) {
        this.x = x;
    }

    private void setY(double y) {
        this.y = y;
    }

    private void setR(double r) {
        this.r = r;
    }

    private void setHit(Boolean hit) {
        this.hit = hit;
    }

    public void check() {
        if (hit == null) {
            final Long start = System.nanoTime();
            setHit(checkers.stream().anyMatch(checker -> checker.check(this)));
            final Long end = System.nanoTime();
            setComputationTime(end - start);
        }
    }

    public void setCreatedAt(LocalDateTime timestamp) {
        this.createdAt = timestamp;
    }

    private void setComputationTime(long computationTime) {
        this.computationTime = computationTime;
    }
}
