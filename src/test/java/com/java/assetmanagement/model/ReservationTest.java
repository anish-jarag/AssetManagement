package com.java.assetmanagement.model;

import static org.junit.Assert.assertEquals;

import java.sql.Date;

import org.junit.Test;


public class ReservationTest {

    @Test
    public void testDefaultConstructorAndSettersGetters() {
        Reservation reservation = new Reservation();

        Date reservationDate = Date.valueOf("2025-04-01");
        Date startDate = Date.valueOf("2025-04-05");
        Date endDate = Date.valueOf("2025-04-10");

        reservation.setReservationId(101);
        reservation.setAssetId(501);
        reservation.setEmployeeId(301);
        reservation.setReservationDate(reservationDate);
        reservation.setStartDate(startDate);
        reservation.setEndDate(endDate);
        reservation.setStatus("confirmed");

        assertEquals(101, reservation.getReservationId());
        assertEquals(501, reservation.getAssetId());
        assertEquals(301, reservation.getEmployeeId());
        assertEquals(reservationDate, reservation.getReservationDate());
        assertEquals(startDate, reservation.getStartDate());
        assertEquals(endDate, reservation.getEndDate());
        assertEquals("confirmed", reservation.getStatus());
    }

    @Test
    public void testParameterizedConstructor() {
        Date reservationDate = Date.valueOf("2025-03-01");
        Date startDate = Date.valueOf("2025-03-05");
        Date endDate = Date.valueOf("2025-03-08");

        Reservation reservation = new Reservation(102, 502, 302, reservationDate, startDate, endDate, "pending");

        assertEquals(102, reservation.getReservationId());
        assertEquals(502, reservation.getAssetId());
        assertEquals(302, reservation.getEmployeeId());
        assertEquals(reservationDate, reservation.getReservationDate());
        assertEquals(startDate, reservation.getStartDate());
        assertEquals(endDate, reservation.getEndDate());
        assertEquals("pending", reservation.getStatus());
    }

    @Test
    public void testToString() {
        Date reservationDate = Date.valueOf("2025-05-01");
        Date startDate = Date.valueOf("2025-05-03");
        Date endDate = Date.valueOf("2025-05-07");

        Reservation reservation = new Reservation(103, 503, 303, reservationDate, startDate, endDate, "approved");

        String expected = "Reservation [reservationId=103, assetId=503, employeeId=303, reservationDate=2025-05-01, startDate=2025-05-03, endDate=2025-05-07, status=approved]";
        assertEquals(expected, reservation.toString());
    }
}
