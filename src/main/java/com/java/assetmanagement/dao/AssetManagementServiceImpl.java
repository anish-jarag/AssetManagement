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
			return true;
		} catch (ClassNotFoundException | AssetNotFoundException | SQLException e) {
			e.printStackTrace();
		} 
		return false;
	}	
	
	@Override
	public Asset searchAsset(int assetId) throws SQLException, AssetNotFoundException, ClassNotFoundException {
	    connection = ConnectionHelper.getConnection();
	    String cmd = "SELECT * FROM assets WHERE asset_id = ?";
	    pst = connection.prepareStatement(cmd);
	    pst.setInt(1, assetId);
	    ResultSet rs = pst.executeQuery();

	    if (rs.next()) {
	        Asset asset = new Asset();
	        asset.setAssetId(rs.getInt("asset_id"));
	        asset.setName(rs.getString("name"));
	        asset.setType(rs.getString("type"));
	        asset.setSerialNumber(rs.getString("serial_number"));
	        asset.setPurchaseDate(rs.getDate("purchase_date"));
	        asset.setLocation(rs.getString("location"));
	        asset.setStatus(AssetStatus.valueOf(rs.getString("status")));
	        asset.setOwnerId(rs.getInt("owner_id"));
	        return asset;
	    } else {
	        throw new AssetNotFoundException("❌ Asset with ID " + assetId + " not found.");
	    }
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
            allocation.setReturnDate(rs.getDate("return_date")); 

            assetAllocatedList.add(allocation);
		}
		
		return assetAllocatedList;
	}

	@Override
	public boolean allocateAsset(int assetId, int employeeId, String allocationDate) throws SQLException, ClassNotFoundException, AssetNotMaintainException {
		connection = ConnectionHelper.getConnection();

		String checkCmd = "select max(maintenance_date) as last_maintained from maintenance_records where asset_id = ?";
	    pst = connection.prepareStatement(checkCmd);
	    pst.setInt(1, assetId);
	    ResultSet rs = pst.executeQuery();

	    if (rs.next()) {
	        java.sql.Date lastMaintainedDate = rs.getDate("last_maintained");
	        if (lastMaintainedDate != null) {
	            long diffMillis = System.currentTimeMillis() - lastMaintainedDate.getTime();
	            long diffYears = diffMillis / (1000L * 60 * 60 * 24 * 365);

	            if (diffYears >= 2) {
	                throw new AssetNotMaintainException(lastMaintainedDate.toString());
	            }
	        }
	    }
	    
	    String cmd = "insert into asset_allocations (asset_id, employee_id, allocation_date) values (?, ?, ?)";
	    pst = connection.prepareStatement(cmd);
	    pst.setInt(1, assetId);
	    pst.setInt(2, employeeId);
	    pst.setDate(3, java.sql.Date.valueOf(allocationDate));

	    return pst.executeUpdate() > 0;
	}

	@Override
	public boolean deallocateAsset(int assetId, int employeeId, String returnDate) throws SQLException, ClassNotFoundException {
		connection = ConnectionHelper.getConnection();
	    String cmd = "UPDATE asset_allocations SET return_date = ? WHERE asset_id = ? AND employee_id = ? AND return_date IS NULL";
	    pst = connection.prepareStatement(cmd);

	    pst.setDate(1, java.sql.Date.valueOf(returnDate));
	    pst.setInt(2, assetId);
	    pst.setInt(3, employeeId);

	    return pst.executeUpdate() > 0;
	}
	
	@Override
	public List<Asset> showMaintenanceRecord() throws ClassNotFoundException, SQLException {
		connection = ConnectionHelper.getConnection();
		List<Asset> maintenanceList = new ArrayList<Asset>();
		
		String cmd = "select * from Maintenance;";
		pst = connection.prepareStatement(cmd);
		
		ResultSet rs = pst.executeQuery();
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
	        maintenanceList.add(asset);
		}
		
		return maintenanceList;
	}

	@Override
	public boolean performMaintenance(int assetId, String maintenanceDate, String description, double cost)
			throws SQLException, AssetNotMaintainException, ClassNotFoundException, AssetNotFoundException {
		 connection = ConnectionHelper.getConnection();

		 String cmd = "select Status from assets where asset_id = ?";
	     pst = connection.prepareStatement(cmd);
		 pst.setInt(1, assetId);
	     ResultSet rs = pst.executeQuery();

	    if (rs.next()) {
	        String status = rs.getString("status");
	        if (status.equalsIgnoreCase("decommissioned")) {
	        	throw new AssetNotMaintainException("❌ Cannot maintain a decommissioned asset.");
		        }
		} else {
	        throw new AssetNotFoundException("❌ Asset not found.");
	    }

	    cmd = "insert into maintenance_records (asset_id, maintenance_date, description, cost) values (?, ?, ?, ?)";
	    pst = connection.prepareStatement(cmd);
	    pst.setInt(1, assetId);
	    pst.setDate(2, java.sql.Date.valueOf(maintenanceDate));
	    pst.setString(3, description);
	    pst.setDouble(4, cost);

	    return pst.executeUpdate() > 0;
	}
	
	@Override
	public List<AssetAllocation> showReservations() throws ClassNotFoundException, SQLException {
		connection = ConnectionHelper.getConnection();
	    String cmd = "select r.reservation_id, r.asset_id, r.employee_id, e.name,"
	    		+ " r.reservation_date, r.start_date, r.end_date "
	    		+ "from reservations r "
	    		+ "join employees e on r.employee_id = e.employee_id ";

	    pst = connection.prepareStatement(cmd);

	    ResultSet rs = pst.executeQuery();
	    List<AssetAllocation> reservedAssets = new ArrayList<>();
	    AssetAllocation assetAllocation;

	    while (rs.next()) {
	        assetAllocation = new AssetAllocation();
	        assetAllocation.setAllocationId(rs.getInt("reservation_id")); 
	        assetAllocation.setAssetId(rs.getInt("asset_id"));
	        assetAllocation.setEmployeeId(rs.getInt("employee_id"));
	        assetAllocation.setEmployeeName(rs.getString("name"));
	        assetAllocation.setAllocationDate(rs.getDate("start_date"));
	        assetAllocation.setReturnDate(rs.getDate("end_date"));

	        reservedAssets.add(assetAllocation);
	    }

	    return reservedAssets;
	}
	
	@Override
	public boolean isAssetReserved(int assetId, String allocationDate) throws Exception {
		connection = ConnectionHelper.getConnection();
	    String cmd = "select count(*) from reservations where asset_id = ? and start_date <= ? and end_date >= ?";
	    PreparedStatement stmt = connection.prepareStatement(cmd);
	    stmt.setInt(1, assetId);
	    stmt.setString(2, allocationDate);
	    stmt.setString(3, allocationDate);

	    ResultSet rs = stmt.executeQuery();
	    if (rs.next()) {
	        return rs.getInt(1) > 0;
	    }
	    return false;
	}


	@Override
	public boolean reserveAsset(int assetId, int employeeId, String reservationDate, String startDate, String endDate)
			throws SQLException, ClassNotFoundException, AssetNotMaintainException, AssetNotFoundException {
		connection = ConnectionHelper.getConnection();
		
		String cmd = "select Status from assets where asset_id = ?";
	     pst = connection.prepareStatement(cmd);
		 pst.setInt(1, assetId);
	     ResultSet rs = pst.executeQuery();

	    if (rs.next()) {
	        String status = rs.getString("status");
	        if (status.equalsIgnoreCase("decommissioned")) {
	        	throw new AssetNotMaintainException("❌ Cannot maintain a decommissioned asset.");
		        }
		} else {
	        throw new AssetNotFoundException("❌ Asset not found.");
	    }
		
	    cmd = "insert into asset_reservations (asset_id, employee_id, reservation_date, start_date, end_date) values (?, ?, ?, ?, ?)";
	    pst = connection.prepareStatement(cmd);
	    pst.setInt(1, assetId);
	    pst.setInt(2, employeeId);
	    pst.setDate(3, java.sql.Date.valueOf(reservationDate));
	    pst.setDate(4, java.sql.Date.valueOf(startDate));
	    pst.setDate(5, java.sql.Date.valueOf(endDate));

	    return pst.executeUpdate() > 0;
	}

	@Override
	public boolean withdrawReservation(int reservationId) throws SQLException, ClassNotFoundException {
		connection = ConnectionHelper.getConnection();
	    String cmd = "delete from asset_reservations where reservation_id = ?";
	    pst = connection.prepareStatement(cmd);
	    pst.setInt(1, reservationId);

	    return pst.executeUpdate() > 0;
	}

}
