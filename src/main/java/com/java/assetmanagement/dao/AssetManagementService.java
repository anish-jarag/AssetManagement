package com.java.assetmanagement.dao;

import java.sql.SQLException;
import java.util.List;

import com.java.assetmanagement.model.Asset;
import com.java.assetmanagement.myexceptions.AssetNotFoundException;
import com.java.assetmanagement.myexceptions.AssetNotMaintainException;

public interface AssetManagementService {
	List<Asset> showAsset() throws ClassNotFoundException, SQLException;
	boolean addAsset(Asset asset) throws ClassNotFoundException, SQLException;
	boolean updateAsset(Asset asset) throws SQLException;
	boolean deleteAsset(int assetId) throws AssetNotFoundException, SQLException;
	
	List<Asset> showAllocations() throws ClassNotFoundException, SQLException;
	boolean allocateAsset(int assetId, int employeeId, String allocationDate) throws SQLException;
	boolean deallocateAsset(int assetId, int employeeId, String returnDate) throws SQLException;
	
	List<Asset> showMaintenanceRecord() throws ClassNotFoundException, SQLException;
	boolean performMaintenance(int assetId, String maintenanceDate, String description, double cost) throws SQLException, AssetNotMaintainException;
	
	List<Asset> showReservations() throws ClassNotFoundException, SQLException;
	boolean reserveAsset(int assetId, int employeeId, String reservationDate, String startDate, String endDate) throws SQLException;
	boolean withdrawReservation(int reservationId) throws SQLException;

}
