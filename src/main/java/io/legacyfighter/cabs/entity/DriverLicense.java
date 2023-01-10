package io.legacyfighter.cabs.entity;

public class DriverLicense {

    private static final String DRIVER_LICENSE_REGEX = "^[A-Z9]{5}\\d{6}[A-Z9]{2}\\d[A-Z]{2}$";

    private final String license;

    private DriverLicense(String license) {this.license = license;}

    public static DriverLicense withLicense(String license) {
        if (license == null || license.isEmpty() || !license.matches(DRIVER_LICENSE_REGEX)) {
            throw new IllegalArgumentException("Illegal license no = " + license);
        }

        return new DriverLicense(license);
    }

    public static DriverLicense withoutValidation(String license) {
        return new DriverLicense(license);
    }

    public String asString() {
        return license;
    }

}
