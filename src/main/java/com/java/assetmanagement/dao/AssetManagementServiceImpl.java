package com.java.assetmanagement.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.java.assetmanagement.model.Asset;
import com.java.assetmanagement.model.AssetAllocation;
import com.java.assetmanagement.model.AssetStatus;
import com.java.assetmanagement.myexceptions.AssetNotFoundException;
import com.java.assetmanagement.myexceptions.AssetNotMaintainException;
import com.java.assetmanagement.util.ConnectionHelper;

public class AssetManagementServiceImpl implements AssetManagementService{
	
	Connection connection;
    PreparedStatement pst;

	@Override
	public List<Asset> showAsset() throws ClassNotFoundException, SQLException {
		connection = ConnectionHelper.getConnection();
		String cmd = "select * from Assets;";
		pst = connection.prepareStatement(cmd);
		
		ResultSet rs = pst.executeQuery();
		List<Asset> assetList = new ArrayList<Asset>();
		Asset asset = null;
		while(rs.next()) {
			asset = new Asset();
			asset.setAssetId(rs.getInt("asset_id"));
			asset.setName(rs.getString("name"));
			asset.setType(rs.getString("type"));
			asset.setSerialNumber(rs.getString("serial_number"));
			asset.setPurchaseDate(rs.getDate("purchase_date"));
			asset.setLocation(rs.getString("location"));
			asset.setStatus(AssetStatus.valueOf(rs.getString("status")));
			asset.setOwnerId(rs.getInt("owner_id"));
			
			assetList.add(asset);
		}
		
		return assetList;
	}

	@Override
	public boolean addAsset(Asset asset) throws SQLException, ClassNotFoundException{
		try {
			connection = ConnectionHelper.getConnection();
			String cmd = "insert into Assets (name, type, serial_number, purchase_date, location, status, owner_id) "
					+ "values (?, ?, ?, ?, ?, ?, ?)";
			
			pst = connection.prepareStatement(cmd);

			pst.setString(1, asset.getName());
			pst.setString(2, asset.getType());
			pst.setString(3, asset.getSerialNumber());
			pst.setDate(4, asset.getPurchaseDate());
			pst.setString(5, asset.getLocation());
			pst.setString(6, asset.getStatus().toString());
			pst.setInt(7, asset.getOwnerId());
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		return pst.executeUpdate() > 0;
	}

	@Override
	public boolean updateAsset(Asset asset) throws SQLException, AssetNotFoundException, ClassNotFoundException {
		connection = ConnectionHelper.getConnection();
		String cmd = "update Assets set name=?, type=?, serial_number=?, purchase_date=?, location=?, status=?, owner_id=? where asset_id =?";
		pst = connection.prepareStatement(cmd);
		
		pst.setString(1, asset.getName());
		pst.setString(2, asset.getType());
		pst.setString(3, asset.getSerialNumber());
		pst.setDate(4, asset.getPurchaseDate());
		pst.setString(5, asset.getLocation());
		pst.setString(6, asset.getStatus().toString());
		pst.setInt(7, asset.getOwnerId());
		pst.setInt(8, asset.getAssetId());
		pst.executeUpdate();
		
		int rowsAffected = pst.executeUpdate();

	    if (rowsAffected == 0) {
	        throw new AssetNotFoundException("❌ No asset found with ID: " + asset.getAssetId());
	    }

	    return pst.executeUpdate() > 0;
	}

	@Override
	public boolean deleteAsset(int assetId) throws SQLException, AssetNotFoundException, ClassNotFoundException {
		try {
			connection = ConnectionHelper.getConnection();
			String cmd = "delete from assets where asset_id = ?";
			pst = connection.prepareStatement(cmd);
			pst.setInt(1, assetId);
			int rowsAffected = pst.executeUpdate();
			if (rowsAffected == 0) {
			    throw new AssetNotFoundException("❌ Asset with ID " + assetId + " not found.");
			}
			return pst.executeUpdate() > 0;
		} catch (ClassNotFoundException | AssetNotFoundException | SQLException e) {
			e.printStackTrace();
		} 
		return false;
	}	
	
	@Override
	public Asset searchAsset(int assetId) throws SQLException, AssetNotFoundException, ClassNotFoundException {
		try {
			connection = ConnectionHelper.getConnection();
			String cmd = "select * from assets where asset_id = ?";
	        pst = connection.prepareStatement(cmd);
	        pst.setInt(1, assetId);
	        ResultSet rs = pst.executeQuery();
        
			if (rs.next() && rs.getInt(1) == 0) {
			    throw new AssetNotFoundException("❌ Asset with ID " + assetId + " not found.");
			}
		} catch (SQLException | AssetNotFoundException | ClassNotFoundException e) {
			e.printStackTrace();
		} 
		return null;
	}
	
	
	@Override
	public List<AssetAllocation> showAllocations() throws ClassNotFoundException, SQLException {
		connection = ConnectionHelper.getConnection();
		String cmd = "select * from asset_allocations;";
		pst = connection.prepareStatement(cmd);
		
		ResultSet rs = pst.executeQuery();
		List<AssetAllocation> assetAllocatedList = new ArrayList<AssetAllocation>();
		AssetAllocation allocation = null;
		while(rs.next()) {
			allocation = new AssetAllocation();
            allocation.setAllocationId(rs.getInt("allocation_id"));
            allocation.setAssetId(rs.getInt("asset_id"));
            allocation.setEmployeeId(rs.getInt("employee_id"));
            allocation.setAllocationDate(rs.getDate("allocation_date"));
            allocation.setReturnDate(rs.getDate("return_date")); // Might be null

            assetAllocatedList.add(allocation);
		}
		
		return assetAllocatedList;
	}

	@Override
	public boolean allocateAsset(int assetId, int employeeId, String allocationDate) throws SQLException, ClassNotFoundException {
		connection = ConnectionHelper.getConnection();

	    String cmd = "INSERT INTO asset_allocations (asset_id, employee_id, allocation_date) VALUES (?, ?, ?)";
	    pst = connection.prepareStatement(cmd);

	    pst.setInt(1, assetId);
	    pst.setInt(2, employeeId);
	    pst.setDate(3, java.sql.Date.valueOf(allocationDate));

	    return pst.executeUpdate() > 0;
	}

	@Override
	public boolean deallocateAsset(int assetId, int employeeId, String returnDate) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public List<Asset> showMaintenanceRecord() throws ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean performMaintenance(int assetId, String maintenanceDate, String description, double cost)
			throws SQLException, AssetNotMaintainException {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public List<Asset> showReservations() throws ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean reserveAsset(int assetId, int employeeId, String reservationDate, String startDate, String endDate)
			throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean withdrawReservation(int reservationId) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

}
