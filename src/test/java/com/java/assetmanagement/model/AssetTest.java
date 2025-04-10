package com.java.assetmanagement.model;

import static org.junit.Assert.assertEquals;

import java.sql.Date;

import org.junit.Test;

public class AssetTest {

    @Test
    public void testDefaultConstructorAndSettersGetters() {
        Asset asset = new Asset();
        Date date = Date.valueOf("2023-01-01");

        asset.setAssetId(1);
        asset.setName("Laptop");
        asset.setType("Electronics");
        asset.setSerialNumber("SN12345");
        asset.setPurchaseDate(date);
        asset.setLocation("Bangalore");
        asset.setStatus(AssetStatus.AVAILABLE);
        asset.setOwnerId(101);

        assertEquals(1, asset.getAssetId());
        assertEquals("Laptop", asset.getName());
        assertEquals("Electronics", asset.getType());
        assertEquals("SN12345", asset.getSerialNumber());
        assertEquals(date, asset.getPurchaseDate());
        assertEquals("Bangalore", asset.getLocation());
        assertEquals(AssetStatus.AVAILABLE, asset.getStatus());
        assertEquals(101, asset.getOwnerId());
    }

    @Test
    public void testParameterizedConstructor() {
        Date date = Date.valueOf("2022-12-15");
        Asset asset = new Asset(2, "Projector", "Electronics", "PRJ67890", date, "Hyderabad", AssetStatus.IN_USE, 202);

        assertEquals(2, asset.getAssetId());
        assertEquals("Projector", asset.getName());
        assertEquals("Electronics", asset.getType());
        assertEquals("PRJ67890", asset.getSerialNumber());
        assertEquals(date, asset.getPurchaseDate());
        assertEquals("Hyderabad", asset.getLocation());
        assertEquals(AssetStatus.IN_USE, asset.getStatus());
        assertEquals(202, asset.getOwnerId());
    }

    @Test
    public void testToString() {
        Date date = Date.valueOf("2023-04-01");
        Asset asset = new Asset(3, "Printer", "Peripherals", "PRT555", date, "Mumbai", AssetStatus.RESERVED, 303);

        String expected = "Asset [assetId=3, name=Printer, type=Peripherals, serialNumber=PRT555, purchaseDate=2023-04-01, location=Mumbai, status=RESERVED, ownerId=303]";
        assertEquals(expected, asset.toString());
    }
}
