package com.java.assetmanagement.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.java.assetmanagement.model.Asset;
import com.java.assetmanagement.model.AssetAllocationTest;
import com.java.assetmanagement.model.AssetStatus;
import com.java.assetmanagement.model.MaintenanceRecord;
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
	public boolean updateAsset(Asset asset) throws ClassNotFoundException, SQLException, AssetNotFoundException {
	    if (asset == null || asset.getAssetId() <= 0 ) {
	        throw new IllegalArgumentException("❌ Invalid asset data provided for update.");
	    }

	    Connection connection = ConnectionHelper.getConnection();

	    PreparedStatement checkStmt = connection.prepareStatement("select * from Assets where asset_id = ?");
	    checkStmt.setInt(1, asset.getAssetId());
	    ResultSet rs = checkStmt.executeQuery();
	    if (!rs.next()) {
	        throw new AssetNotFoundException("❌ Asset ID not found: " + asset.getAssetId());
	    }

	    String cmd = "update Assets set name=?, serial_number=?, purchase_date=?, status=? where asset_id =?";
	    PreparedStatement pst = connection.prepareStatement(cmd);
	    pst.setString(1, asset.getName());
	    pst.setString(2, asset.getSerialNumber());
	    pst.setDate(3, asset.getPurchaseDate());
	    pst.setString(4, asset.getStatus().toString());
	    pst.setInt(5, asset.getAssetId());

	    int rowsUpdated = pst.executeUpdate();
	    return rowsUpdated > 0;
	}


	@Override
	public boolean deleteAsset(int assetId) throws ClassNotFoundException, SQLException {
	    if (assetId <= 0) {
	        throw new IllegalArgumentException("❌ Invalid asset ID for deletion.");
	    }

	    Connection connection = ConnectionHelper.getConnection();
	    String cmd = "delete from Assets where asset_id = ?";
	    PreparedStatement pst = connection.prepareStatement(cmd);
	    pst.setInt(1, assetId);

	    int rowsDeleted = pst.executeUpdate();
	    return rowsDeleted > 0;
	}

	
	@Override
	public Asset searchAsset(int assetId) throws SQLException, AssetNotFoundException, ClassNotFoundException {
	    connection = ConnectionHelper.getConnection();
	    String cmd = "select * from assets where asset_id = ?";
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
	public List<AssetAllocationTest> showAllocations() throws ClassNotFoundException, SQLException {
		connection = ConnectionHelper.getConnection();
		String cmd = "select * from asset_allocations;";
		pst = connection.prepareStatement(cmd);
		
		ResultSet rs = pst.executeQuery();
		List<AssetAllocationTest> assetAllocatedList = new ArrayList<AssetAllocationTest>();
		AssetAllocationTest allocation = null;
		while(rs.next()) {
			allocation = new AssetAllocationTest();
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
	public boolean allocateAsset(int assetId, int employeeId, String allocationDate) throws ClassNotFoundException, SQLException, AssetNotFoundException, AssetNotMaintainException
	    {
	    
	    connection = ConnectionHelper.getConnection();

		String cmd = "select max(maintenance_date) as last_maintained from maintenance_records where asset_id = ?";
	    pst = connection.prepareStatement(cmd);
	    pst.setInt(1, assetId);
	    ResultSet rs = pst.executeQuery();
	    if (rs.next() && rs.getInt(1) == 0) {
	        throw new AssetNotFoundException("❌ Asset with ID " + assetId + " not found.");
	    }
	    
	    if (rs.next() && rs.getInt(1) > 0) {
	        throw new SQLException("❌ Asset is already allocated to another employee. Please deallocate it first.");
	    }
	    
	    try {
			if (isAssetReserved(assetId, allocationDate)) {
			    throw new SQLException("❌ Asset is reserved by another employee on the requested allocation date.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	    
	    String checkCmd = "select max(maintenance_date) as last_maintained from maintenance_records where asset_id = ?";
	    pst = connection.prepareStatement(checkCmd);
	    pst.setInt(1, assetId);
	    rs = pst.executeQuery();

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
	    updateAssetStatus(assetId, AssetStatus.IN_USE);

	    cmd = "insert into asset_allocations (asset_id, employee_id, allocation_date) values (?, ?, ?)";
	    pst = connection.prepareStatement(cmd);
	    pst.setInt(1, assetId);
	    pst.setInt(2, employeeId);
	    pst.setDate(3, java.sql.Date.valueOf(allocationDate));

	    boolean allocated = pst.executeUpdate() > 0;

	    if (allocated) {
	        updateAssetStatus(assetId, AssetStatus.IN_USE);
	    }

	    return allocated;
	}
	
	@Override
	public boolean deallocateAsset(int assetId, int employeeId, String returnDate)
	        throws SQLException, ClassNotFoundException, AssetNotFoundException {

	    try (Connection con = ConnectionHelper.getConnection()) {

	        String cmd = "select count(*) from asset_allocations where asset_id = ? and employee_id = ? and return_date IS NULL";
	        try (PreparedStatement checkPst = con.prepareStatement(cmd)) {
	            checkPst.setInt(1, assetId);
	            checkPst.setInt(2, employeeId);

	            try (ResultSet rs = checkPst.executeQuery()) {
	                if (rs.next() && rs.getInt(1) == 0) {
	                    throw new AssetNotFoundException("❌ No active allocation found for Asset ID " + assetId + " and Employee ID " + employeeId);
	                }
	            }
	        }

	        cmd = "update asset_allocations set return_date = ? where asset_id = ? and employee_id = ? and return_date IS NULL";
	        try (PreparedStatement pst = con.prepareStatement(cmd)) {
	            pst.setDate(1, java.sql.Date.valueOf(returnDate));
	            pst.setInt(2, assetId);
	            pst.setInt(3, employeeId);

	            boolean updated = pst.executeUpdate() > 0;

	            if (updated) {
	                updateAssetStatus(assetId, AssetStatus.AVAILABLE);
	            }

	            return updated;
	        }
	    }
	}
	
	private boolean updateAssetStatus(int assetId, AssetStatus status) throws SQLException {
	    String query = "update assets set status = ? where asset_id = ?";
	    pst = connection.prepareStatement(query);
	    pst.setString(1, status.name().toUpperCase()); 
	    pst.setInt(2, assetId);
	    return pst.executeUpdate() > 0;
	}


	
	@Override
	public List<MaintenanceRecord> showMaintenanceRecord() throws ClassNotFoundException, SQLException {
		connection = ConnectionHelper.getConnection();
		if (connection == null) {
			throw new SQLException("Database connection is not available.");
		}

		List<MaintenanceRecord> maintenanceList = new ArrayList<>();

		String cmd = "select * from Maintenance;";
		pst = connection.prepareStatement(cmd);

		ResultSet rs = pst.executeQuery();
		if (rs == null) {
			throw new SQLException("No data retrieved from the Maintenance table.");
		}

		MaintenanceRecord record = null;
		while (rs.next()) {
			double cost = rs.getDouble("cost");
			if (cost < 0) continue; 

			String description = rs.getString("description");
			if (description == null || description.trim().isEmpty()) continue;

			record = new MaintenanceRecord();
			record.setMaintenanceId(rs.getInt("maintenance_id"));
			record.setAssetId(rs.getInt("asset_id"));
			record.setMaintenanceDate(rs.getDate("maintenance_date"));
			record.setDescription(description);
			record.setCost(cost);

			maintenanceList.add(record);
		}

		if (maintenanceList.isEmpty()) {
			System.out.println("⚠️ No maintenance records found.");
		}

		return maintenanceList;
	}

	@Override
	public boolean performMaintenance(int assetId, String maintenanceDate, String description, double cost)
	        throws SQLException, AssetNotMaintainException, ClassNotFoundException, AssetNotFoundException {

	    connection = ConnectionHelper.getConnection();

	    String cmd = "select status from assets where asset_id = ?";
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
	public List<AssetAllocationTest> showReservations() throws ClassNotFoundException, SQLException {
		connection = ConnectionHelper.getConnection();
	    String cmd = "select r.reservation_id, r.asset_id, r.employee_id, e.name,"
	    		+ " r.reservation_date, r.start_date, r.end_date "
	    		+ "from reservations r "
	    		+ "join employees e on r.employee_id = e.employee_id ";

	    pst = connection.prepareStatement(cmd);

	    ResultSet rs = pst.executeQuery();
	    List<AssetAllocationTest> reservedAssets = new ArrayList<>();
	    AssetAllocationTest assetAllocation;

	    while (rs.next()) {
	        assetAllocation = new AssetAllocationTest();
	        assetAllocation.setAllocationId(rs.getInt("reservation_id")); 
	        assetAllocation.setAssetId(rs.getInt("asset_id"));
	        assetAllocation.setEmployeeId(rs.getInt("employee_id"));
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
	        	throw new AssetNotMaintainException("❌ Cannot reserve a decommissioned asset.");
		        }
		} else {
	        throw new AssetNotFoundException("❌ Asset not found.");
	    }
		
	    cmd = "insert into reservations (asset_id, employee_id, reservation_date, start_date, end_date) values (?, ?, ?, ?, ?)";
	    pst = connection.prepareStatement(cmd);
	    pst.setInt(1, assetId);
	    pst.setInt(2, employeeId);
	    pst.setDate(3, java.sql.Date.valueOf(reservationDate));
	    pst.setDate(4, java.sql.Date.valueOf(startDate));
	    pst.setDate(5, java.sql.Date.valueOf(endDate));

	    boolean reserved = pst.executeUpdate() > 0;

	    if (reserved) {
	        updateAssetStatus(assetId, AssetStatus.RESERVED);
	    }

	    return reserved;
	}

	@Override
	public boolean withdrawReservation(int reservationId) throws SQLException, ClassNotFoundException {
	    connection = ConnectionHelper.getConnection();

	    String fetchCmd = "select asset_id from reservations where reservation_id = ?";
	    pst = connection.prepareStatement(fetchCmd);
	    pst.setInt(1, reservationId);
	    ResultSet rs = pst.executeQuery();

	    int assetId = -1;
	    if (rs.next()) {
	        assetId = rs.getInt("asset_id");
	    } else {
	        System.out.println("❌ Reservation not found.");
	        return false;
	    }

	    String deleteCmd = "delete from reservations where reservation_id = ?";
	    pst = connection.prepareStatement(deleteCmd);
	    pst.setInt(1, reservationId);
	    boolean reserved = pst.executeUpdate() > 0;

	    if (reserved) {
	        updateAssetStatus(assetId, AssetStatus.AVAILABLE);
	    }

	    return reserved;
	}


	@Override
	public boolean isAssetAllocated(int assetId) throws SQLException, ClassNotFoundException {
	    Connection con = ConnectionHelper.getConnection();
	    String sql = "select count(*) from asset_allocation where asset_id = ? and return_date is null";
	    PreparedStatement pst = con.prepareStatement(sql);
	    pst.setInt(1, assetId);
	    ResultSet rs = pst.executeQuery();
	    if (rs.next()) {
	        return rs.getInt(1) > 0;
	    }
	    return false;
	}

}
