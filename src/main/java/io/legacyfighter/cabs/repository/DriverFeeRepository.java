package io.legacyfighter.cabs.repository;

import io.legacyfighter.cabs.entity.DriverFee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DriverFeeRepository extends JpaRepository<DriverFee, Long> {

    DriverFee findByDriverId(Long driverId);

}

