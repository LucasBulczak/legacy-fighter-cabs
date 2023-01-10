package io.legacyfighter.cabs.entity;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.*;

class DriverLicenseTest {
    
    @Test
    void cannotCreateInvalidLicense() {
        //expect
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> DriverLicense.withLicense("invalid"));
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> DriverLicense.withLicense(""));
    }
    
    @Test
    void canCreateValidLicense() {
        //when
        DriverLicense license = DriverLicense.withLicense("FARME100165AB5EW");

        //then
        assertThat(license.asString()).isEqualTo("FARME100165AB5EW");
    }
    
    @Test
    void canCreateInvalidLicenseExplicitly() {
        //when
        DriverLicense license = DriverLicense.withoutValidation("invalid");

        //then
        assertThat(license.asString()).isEqualTo("invalid");
    }

}
