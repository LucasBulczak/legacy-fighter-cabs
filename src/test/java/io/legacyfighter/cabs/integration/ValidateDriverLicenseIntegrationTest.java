package io.legacyfighter.cabs.integration;

import io.legacyfighter.cabs.dto.DriverDTO;
import io.legacyfighter.cabs.entity.Driver;
import io.legacyfighter.cabs.service.DriverService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static io.legacyfighter.cabs.entity.Driver.Status.ACTIVE;
import static io.legacyfighter.cabs.entity.Driver.Status.INACTIVE;
import static io.legacyfighter.cabs.entity.Driver.Type.REGULAR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest
class ValidateDriverLicenseIntegrationTest {

    @Autowired
    private DriverService driverService;

    @Test
    void cannotCreateActiveDriverWithInvalidLicense() {
        //expect
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(
                () -> createActiveDriverWithLicense("invalidLicense"));
    }

    @Test
    void canCreateActiveDriverWithValidLicense() {
        //when
        Driver driver = createActiveDriverWithLicense("FARME100165AB5EW");

        //then
        DriverDTO loaded = load(driver);
        assertThat(loaded.getDriverLicense()).isEqualTo("FARME100165AB5EW");
        assertThat(loaded.getStatus()).isEqualTo(ACTIVE);
    }

    @Test
    void canCreateInactiveDriverWithInvalidLicense() {
        //when
        Driver driver = createInactiveDriverWithLicense("invalidLicense");

        //then
        DriverDTO loaded = load(driver);
        assertThat(loaded.getDriverLicense()).isEqualTo("invalidLicense");
        assertThat(loaded.getStatus()).isEqualTo(INACTIVE);
    }

    @Test
    void canChangeLicenseForValidOne() {
        //given
        Driver driver = createActiveDriverWithLicense("FARME100165AB5EW");

        //when
        changeLicenseTo("99999740614992TL", driver);

        //then
        DriverDTO loaded = load(driver);
        assertThat(loaded.getDriverLicense()).isEqualTo("99999740614992TL");
    }

    @Test
    void cannotChangeLicenseForInvalidOne() {
        //given
        Driver driver = createActiveDriverWithLicense("FARME100165AB5EW");

        //expect
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> changeLicenseTo("invalid", driver));
    }

    @Test
    void canActivateDriverWithValidLicense() {
        //given
        Driver driver = createInactiveDriverWithLicense("FARME100165AB5EW");

        //when
        activate(driver);

        //then
        DriverDTO loaded = load(driver);
        assertThat(loaded.getStatus()).isEqualTo(ACTIVE);
    }

    @Test
    void cannotActivateDriverWithInvalidLicense() {
        //given
        Driver driver = createInactiveDriverWithLicense("invalid");

        //expect
        assertThatExceptionOfType(IllegalStateException.class).isThrownBy(() -> activate(driver));
    }

    Driver createActiveDriverWithLicense(String license) {
        return driverService.createDriver(license, "Kowalski", "Jan", REGULAR, ACTIVE, "photo");
    }

    Driver createInactiveDriverWithLicense(String license) {
        return driverService.createDriver(license, "Kowalski", "Jan", REGULAR, INACTIVE, "photo");
    }

    DriverDTO load(Driver driver) {
        final DriverDTO loaded = driverService.loadDriver(driver.getId());
        return loaded;
    }

    void changeLicenseTo(String newLicense, Driver driver) {
        driverService.changeLicenseNumber(newLicense, driver.getId());
    }

    void activate(Driver driver) {
        driverService.changeDriverStatus(driver.getId(), ACTIVE);
    }

}