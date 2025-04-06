package com.java.assetmanagement.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.java.assetmanagement.model.Asset;
import com.java.assetmanagement.model.AssetStatus;
import com.java.assetmanagement.myexceptions.AssetNotFoundException;
import com.java.assetmanagement.myexceptions.AssetNotMaintainException;
import com.java.assetmanagement.util.ConnectionHelper;

public class AssetManagementServiceImpl implements AssetManagementService{
	
	Connection connection;
    PreparedStatement pst;

	@Override
	public boolean addAsset(Asset asset) throws SQLException, ClassNotFoundException{
		try {
			connection = ConnectionHelper.getConnection();
			String cmd = "insert into Assets (asset_id, name, type, serial_number, purchase_date, location, status, owner_id) "
					+ "values (?, ?, ?, ?, ?, ?, ?, ?)";
			pst.setInt(1, asset.getAssetId());
			pst.setString(2, asset.getName());
			pst.setString(3, asset.getType());
			pst.setString(4, asset.getSerialNumber());
			pst.setDate(5, asset.getPurchaseDate());
			pst.setString(6, asset.getLocation());
			pst.setString(7, asset.getStatus().toString());
			pst.setInt(8, asset.getOwnerId());
			
			pst.executeUpdate(cmd);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		return false;
	}

	@Override
	public boolean updateAsset(Asset asset) throws SQLException, AssetNotFoundException {
		// TODO Auto-generated method stub
		if (pst.executeUpdate() == 0) {
		    throw new AssetNotFoundException("No asset found with ID: " + asset.getAssetId());
		}

		return false;
	}

	@Override
	public boolean deleteAsset(int assetId) throws SQLException {
		if (pst.executeUpdate() == 0) {
		    throw new AssetNotFoundException("No asset found with ID: " + asset.getAssetId());
		}
		return false;
	}
	@Override
	public List<Asset> showAllocations() throws ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean allocateAsset(int assetId, int employeeId, String allocationDate) throws SQLException {
		// TODO Auto-generated method stub
		return false;
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

}
