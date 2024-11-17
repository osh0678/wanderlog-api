package com.wanderlog.api.repository;


import com.wanderlog.api.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Long> {
    // 이름으로 위치 검색
    Location findByName(String name);
}