package com.java.assetmanagement.model;

import java.sql.Date;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MaintenanceRecordTest {

    @Test
    public void testDefaultConstructorAndSettersGetters() {
        MaintenanceRecord record = new MaintenanceRecord();

        Date maintenanceDate = Date.valueOf("2024-10-10");

        record.setMaintenanceId(101);
        record.setAssetId(202);
        record.setMaintenanceDate(maintenanceDate);
        record.setDescription("Replaced motherboard");
        record.setCost(1500.75);

        assertEquals(101, record.getMaintenanceId());
        assertEquals(202, record.getAssetId());
        assertEquals(maintenanceDate, record.getMaintenanceDate());
        assertEquals("Replaced motherboard", record.getDescription());
        assertEquals(1500.75, record.getCost(), 0.001);
    }

    @Test
    public void testParameterizedConstructor() {
        Date date = Date.valueOf("2024-12-01");
        MaintenanceRecord record = new MaintenanceRecord(501, 302, date, "Battery replacement", 499.99);

        assertEquals(501, record.getMaintenanceId());
        assertEquals(302, record.getAssetId());
        assertEquals(date, record.getMaintenanceDate());
        assertEquals("Battery replacement", record.getDescription());
        assertEquals(499.99, record.getCost(), 0.001);
    }

    @Test
    public void testToString() {
        Date date = Date.valueOf("2025-01-01");
        MaintenanceRecord record = new MaintenanceRecord(601, 403, date, "Fan cleaning", 199.50);
        String expected = "MaintenanceRecord [maintenanceId=601, assetId=403, maintenanceDate=2025-01-01, description=Fan cleaning, cost=199.5]";
        assertEquals(expected, record.toString());
    }
}
