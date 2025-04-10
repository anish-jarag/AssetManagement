package com.java.assetmanagement.model;

import static org.junit.Assert.assertEquals;

import java.sql.Date;

import org.junit.Test;

public class AssetAllocationTest {

    @Test
    public void testDefaultConstructorSettersGetters() {
        AssetAllocation allocation = new AssetAllocation();

        Date allocDate = Date.valueOf("2023-03-01");
        Date returnDate = Date.valueOf("2023-03-15");

        allocation.setAllocationId(1);
        allocation.setAssetId(100);
        allocation.setEmployeeId(200);
        allocation.setAllocationDate(allocDate);
        allocation.setReturnDate(returnDate);

        assertEquals(1, allocation.getAllocationId());
        assertEquals(100, allocation.getAssetId());
        assertEquals(200, allocation.getEmployeeId());
        assertEquals(allocDate, allocation.getAllocationDate());
        assertEquals(returnDate, allocation.getReturnDate());
    }

    @Test
    public void testParameterizedConstructor() {
        Date allocDate = Date.valueOf("2023-05-10");
        Date returnDate = Date.valueOf("2023-06-01");

        AssetAllocation allocation = new AssetAllocation(2, 101, 201, allocDate, returnDate);

        assertEquals(2, allocation.getAllocationId());
        assertEquals(101, allocation.getAssetId());
        assertEquals(201, allocation.getEmployeeId());
        assertEquals(allocDate, allocation.getAllocationDate());
        assertEquals(returnDate, allocation.getReturnDate());
    }

    @Test
    public void testToString() {
        Date allocDate = Date.valueOf("2023-04-05");
        Date returnDate = Date.valueOf("2023-04-20");

        AssetAllocation allocation = new AssetAllocation(3, 102, 202, allocDate, returnDate);

        String expected = "AssetAllocation [allocationId=3, assetId=102, employeeId=202, allocationDate=2023-04-05, returnDate=2023-04-20]";
        assertEquals(expected, allocation.toString());
    }
}
