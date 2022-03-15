package io.legacyfighter.cabs.integration;

import io.legacyfighter.cabs.common.Fixtures;
import io.legacyfighter.cabs.dto.AddressDTO;
import io.legacyfighter.cabs.dto.ClientDTO;
import io.legacyfighter.cabs.dto.TransitDTO;
import io.legacyfighter.cabs.entity.Address;
import io.legacyfighter.cabs.entity.Client;
import io.legacyfighter.cabs.entity.Transit;
import io.legacyfighter.cabs.ui.ClientController;
import io.legacyfighter.cabs.ui.TransitController;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static java.time.LocalDateTime.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class TariffRecognizingIntegrationTest {

    @Autowired
    Fixtures fixtures;

    @Autowired
    TransitController transitController;

    @MockBean
    Clock clock;

    @Test
    void newYearsEveTariffShouldBeDisplayed() {
        //given
        TransitDTO transitDTO = createTransit(of(2021, 12, 31, 8, 30).toInstant(ZoneOffset.UTC));

        //when
        transitDTO = transitController.getTransit(transitDTO.getId());

        //then
        assertEquals("Sylwester", transitDTO.getTariff());
        assertEquals(3.5f, transitDTO.getKmRate());

    }

    @Test
    void weekendTariffShouldBeDisplayed() {
        //given
        TransitDTO transitDTO = createTransit(of(2021, 4, 17, 8, 30).toInstant(ZoneOffset.UTC));

        //when
        transitDTO = transitController.getTransit(transitDTO.getId());

        //then
        assertEquals("Weekend", transitDTO.getTariff());
        assertEquals(1.5f, transitDTO.getKmRate());
    }

    @Test
    void weekendPlusTariffShouldBeDisplayed() {
        //given
        TransitDTO transitDTO = createTransit(of(2021, 4, 17, 22, 30).toInstant(ZoneOffset.UTC));

        //when
        transitDTO = transitController.getTransit(transitDTO.getId());

        //then
        assertEquals("Weekend+", transitDTO.getTariff());
        assertEquals(2.5f, transitDTO.getKmRate());
    }

    @Test
    void standardTariffShouldBeDisplayed() {
        //given
        TransitDTO transitDTO = createTransit(of(2021, 4, 13, 22, 30).toInstant(ZoneOffset.UTC));

        //when
        transitDTO = transitController.getTransit(transitDTO.getId());

        //then
        assertEquals("Standard", transitDTO.getTariff());
        assertEquals(1.0f, transitDTO.getKmRate());
    }

    TransitDTO createTransit(Instant when) {
        Client client = fixtures.aClient();
        Mockito.when(clock.instant()).thenReturn(when);
        TransitDTO transitDTO = new TransitDTO();
        AddressDTO destination = new AddressDTO("Polska", "Warszawa", "Zytnia", 20);
        AddressDTO from = new AddressDTO("Polska", "Warszawa", "Młynarska", 20);
        transitDTO.setFrom(from);
        transitDTO.setTo(destination);
        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setId(client.getId());
        transitDTO.setClientDTO(clientDTO);
        return transitController.createTransit(transitDTO);
    }

}