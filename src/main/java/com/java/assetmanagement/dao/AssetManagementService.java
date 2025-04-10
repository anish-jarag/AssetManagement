package com.java.assetmanagement.dao;

import java.sql.SQLException;
import java.util.List;

import com.java.assetmanagement.model.Asset;
import com.java.assetmanagement.model.AssetAllocationTest;
import com.java.assetmanagement.model.MaintenanceRecord;
import com.java.assetmanagement.myexceptions.AssetNotFoundException;
import com.java.assetmanagement.myexceptions.AssetNotMaintainException;

public interface AssetManagementService {
	List<Asset> showAsset() throws ClassNotFoundException, SQLException;
	boolean addAsset(Asset asset) throws ClassNotFoundException, SQLException;
	boolean updateAsset(Asset asset) throws SQLException, AssetNotFoundException, ClassNotFoundException;
	boolean deleteAsset(int assetId) throws AssetNotFoundException, SQLException, ClassNotFoundException;
	Asset searchAsset(int assetId) throws SQLException, AssetNotFoundException, ClassNotFoundException;
	
	List<AssetAllocationTest> showAllocations() throws ClassNotFoundException, SQLException;
	boolean allocateAsset(int assetId, int employeeId, String allocationDate) throws SQLException, ClassNotFoundException, AssetNotMaintainException, AssetNotFoundException, Exception;
	boolean deallocateAsset(int assetId, int employeeId, String returnDate) throws SQLException, ClassNotFoundException, AssetNotFoundException;
	
	List<MaintenanceRecord> showMaintenanceRecord() throws ClassNotFoundException, SQLException;
	boolean performMaintenance(int assetId, String maintenanceDate, String description, double cost) throws SQLException, AssetNotMaintainException, ClassNotFoundException, AssetNotFoundException;
	boolean isAssetAllocated(int assetId) throws SQLException, ClassNotFoundException;

	
	List<AssetAllocationTest> showReservations() throws ClassNotFoundException, SQLException;
	boolean reserveAsset(int assetId, int employeeId, String reservationDate, String startDate, String endDate) throws SQLException, ClassNotFoundException, AssetNotMaintainException, AssetNotFoundException;
	boolean withdrawReservation(int reservationId) throws SQLException, ClassNotFoundException;
	
	boolean isAssetReserved(int assetId, String allocationDate) throws Exception;
}
