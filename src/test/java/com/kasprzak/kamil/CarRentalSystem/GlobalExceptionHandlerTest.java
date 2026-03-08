package com.kasprzak.kamil.CarRentalSystem;

import static org.junit.jupiter.api.Assertions.*;

import com.kasprzak.kamil.CarRentalSystem.dto.ErrorResponse;
import com.kasprzak.kamil.CarRentalSystem.exceptions.CarNotAvailableException;
import com.kasprzak.kamil.CarRentalSystem.exceptions.ReservationNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @Test
    void shouldReturnConflictWhenCarNotAvailableExceptionIsThrown() {
        // given
        var ex = new CarNotAvailableException("not available");

        // when
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleCarNotAvailableException(ex);

        // then
        assertNotNull(response);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.CONFLICT.value(), response.getBody().status());
        assertEquals("Car Not Available", response.getBody().error());
        assertEquals("not available", response.getBody().message());
        assertNotNull(response.getBody().timestamp());
    }

    @Test
    void shouldReturnNotFoundWhenReservationNotFoundExceptionIsThrown() {
        // given
        var ex = new ReservationNotFoundException("Not found");

        // when
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleReservationNotFoundException(ex);

        // then
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getBody().status());
        assertEquals("Reservation not found", response.getBody().error());
        assertEquals("Not found", response.getBody().message());
        assertNotNull(response.getBody().timestamp());
    }

    @Test
    void shouldReturnBadRequestWhenIllegalArgumentExceptionIsThrown() {
        // given
        var ex = new IllegalArgumentException("Invalid");

        // when
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleIllegalArgumentException(ex);

        // then
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getBody().status());
        assertEquals("Illegal argument", response.getBody().error());
        assertEquals("Invalid", response.getBody().message());
        assertNotNull(response.getBody().timestamp());
    }

    @Test
    void shouldReturnInternalServerErrorExceptionIsThrown() {
        // given
        var ex = new Exception();

        // when
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleException(ex);

        // then
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getBody().status());
        assertEquals("Internal Server Error", response.getBody().error());
        assertEquals("An unexpected error occurred", response.getBody().message());
        assertNotNull(response.getBody().timestamp());
    }
}