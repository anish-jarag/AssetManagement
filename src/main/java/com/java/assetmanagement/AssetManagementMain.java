package com.java.assetmanagement;

import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Scanner;

import com.java.assetmanagement.dao.AssetManagementService;
import com.java.assetmanagement.dao.AssetManagementServiceImpl;
import com.java.assetmanagement.model.Asset;
import com.java.assetmanagement.model.AssetAllocation;
import com.java.assetmanagement.model.AssetStatus;
import com.java.assetmanagement.myexceptions.AssetNotFoundException;
import com.java.assetmanagement.myexceptions.AssetNotMaintainException;

public class AssetManagementMain {
	static Scanner scanner;
	static AssetManagementService assetDao;
	
	static {
		assetDao = new AssetManagementServiceImpl();
		scanner = new Scanner(System.in);
	}
	
	public static void main(String[] args) {
		int choice;
	    do {
	    	System.out.println("===============");
	        System.out.println("O P T I O N S ");
	        System.out.println("===============");
	        System.out.println("\n📦 Asset Operations:");
	        System.out.println("1. Show Assets");
	        System.out.println("2. Add Asset");
	        System.out.println("3. Update Asset");
	        System.out.println("4. Delete Asset");
	        
	        System.out.println("\n🔄 Allocation Operations:");
	        System.out.println("5. Show Allocations");
	        System.out.println("6. Allocate Asset");
	        System.out.println("7. Deallocate Asset");
	        
	        System.out.println("\n🛠️ Maintenance Operations:");
	        System.out.println("8. Show Maintenance Record");
	        System.out.println("9. Perform Maintenance");

	        System.out.println("\n📅 Reservation Operations:");
	        System.out.println("10. Show Reservation");
	        System.out.println("11. Reserve Asset");
	        System.out.println("12. Withdraw Reservation");

	        System.out.println("\n🚪 13. Exit\n");
	        
	        System.out.print("Enter Your Choice:  ");
	        choice = scanner.nextInt();
	        switch (choice) {
	            case 1:
				showAssets();
	                break;
	            case 2:
					addAssets();
		                break;
	            case 3:
					updateAssets();
		                break;
	            case 4:
					deleteAsset();
		                break;
	            case 5:
					showAllocations();
		                break;
	            case 6:
	            	allocateAsset();
		                break;
	            case 7:
					deallocateAsset();
		                break;
	            case 8:
					showMaintenanceRecords();
		                break;
	            case 9:
					performMaintenance();
		                break;
	            case 10:
					showReservations();
		                break;
	            case 11:
					reserveAsset();
		                break;
	            case 12:
	            	withdrawReservation();
		                break;
	            case 13:
	            	System.out.println("Thank you for using Asset Management System!");
	                return;
	        }
	    } while (choice != 13);
	}
	
	private static void withdrawReservation() {
		System.out.print("Enter Reservation ID to withdraw: ");
	    int reservationId = scanner.nextInt();

	    try {
	        boolean withdrawn = assetDao.withdrawReservation(reservationId);
	        if (withdrawn) {
	            System.out.println("✅ Reservation withdrawn successfully.");
	        } else {
	            System.out.println("❌ Failed to withdraw reservation.");
	        }
	    } catch (Exception e) {
	        System.out.println("❌ Error: " + e.getMessage());
	    }
		
	}

	private static void reserveAsset() {
		System.out.print("Enter Asset ID to reserve: ");
	    int assetId = scanner.nextInt();
	    try {
	        assetDao.searchAsset(assetId); 
	    } catch (ClassNotFoundException | SQLException | AssetNotFoundException e) {
	        System.out.println("❌ Error: " + e.getMessage());
	        return;
	    }

	    System.out.print("Enter Employee ID: ");
	    int employeeId = scanner.nextInt();
	    scanner.nextLine();

	    System.out.print("Enter Reservation Date (YYYY-MM-DD): ");
	    String reservationDate = scanner.nextLine();

	    System.out.print("Enter Start Date (YYYY-MM-DD): ");
	    String startDate = scanner.nextLine();

	    System.out.print("Enter End Date (YYYY-MM-DD): ");
	    String endDate = scanner.nextLine();

	    try {
	        boolean reserved = assetDao.reserveAsset(assetId, employeeId, reservationDate, startDate, endDate);
	        if (reserved) {
	            System.out.println("✅ Asset reserved successfully.");
	        } else {
	            System.out.println("❌ Failed to reserve asset.");
	        }
	    } catch (AssetNotMaintainException e) {
	        System.out.println("⚠️ Maintenance Exception: " + e.getMessage());
	    } catch (Exception e) {
	        System.out.println("❌ Error: " + e.getMessage());
	    }
		
	}

	private static void showReservations() {
	    try {
	        List<AssetAllocation> reservations = assetDao.showReservations();

	        if (reservations.isEmpty()) {
	            System.out.println("⚠️  No reservations found.");
	            return;
	        }

	        System.out.println("--------------------------------------------------------------------------------------");
	        System.out.println("\t\t\tReservation Records");
	        System.out.println("--------------------------------------------------------------------------------------");
	        System.out.printf("%-15s %-10s %-25s %-15s %-15s\n",
	            "| Reservation ID", "| Asset ID", "| Employee (ID - Name)", "| Start Date", "| End Date     |");
	        System.out.println("--------------------------------------------------------------------------------------");

	        for (AssetAllocation reservation : reservations) {
	            System.out.printf("| %-14d | %-9d | %-23s | %-13s | %-12s |\n",
	                              reservation.getAllocationId(),
	                              reservation.getAssetId(),
	                              reservation.getEmployeeId() + " - " + reservation.getEmployeeName(),
	                              reservation.getAllocationDate(),
	                              reservation.getReturnDate());
	        }

	        System.out.println("-------------------------------------------------------------------------------------\n");

	    } catch (Exception e) {
	        System.out.println("❌ Error fetching reservations: " + e.getMessage());
	    }
	}


	private static void performMaintenance() {
		// TODO Auto-generated method stub
		
	}

	private static void showMaintenanceRecords() {
		// TODO Auto-generated method stub
		
	}

	private static void deallocateAsset() {
	    System.out.print("Enter Asset ID to deallocate: ");
	    int assetId = scanner.nextInt();
	    
	    try {
			Asset existing = assetDao.searchAsset(assetId);
		} catch (ClassNotFoundException | SQLException | AssetNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	    System.out.print("Enter Employee ID: ");
	    int employeeId = scanner.nextInt();
	    scanner.nextLine();

	    System.out.print("Enter Return Date (YYYY-MM-DD): ");
	    String returnDate = scanner.nextLine();

	    try {
	        boolean deallocated = assetDao.deallocateAsset(assetId, employeeId, returnDate);
	        if (deallocated) {
	            System.out.println("✅ Asset deallocated successfully.");
	        } else {
	            System.out.println("❌ Failed to deallocate asset. Maybe already deallocated?");
	        }
	    } catch (Exception e) {
	        System.out.println("❌ Error: " + e.getMessage());
	    }
	}


	private static void allocateAsset() {
	    System.out.print("Enter Asset ID to allocate: ");
	    int assetId = scanner.nextInt();
	    try {
	        Asset existing = assetDao.searchAsset(assetId); 
	    } catch (ClassNotFoundException | SQLException | AssetNotFoundException e) {
	        System.out.println("❌ Error: " + e.getMessage());
	        return;
	    }

	    System.out.print("Enter Employee ID: ");
	    int employeeId = scanner.nextInt();
	    scanner.nextLine(); 

	    System.out.print("Enter Allocation Date (YYYY-MM-DD): ");
	    String allocationDate = scanner.nextLine();

	    try {
	        if (assetDao.isAssetReserved(assetId, allocationDate)) {
	            System.out.println("❌ Allocation failed. Asset is reserved on " + allocationDate);
	            return;
	        }

	        boolean allocated = assetDao.allocateAsset(assetId, employeeId, allocationDate);
	        if (allocated) {
	            System.out.println("✅ Asset allocated successfully.");
	        } else {
	            System.out.println("❌ Failed to allocate asset.");
	        }
	    } catch (Exception e) {
	        System.out.println("❌ Error: " + e.getMessage());
	    }
	}


	private static void showAllocations() {
	    try {
	        List<AssetAllocation> allocations = assetDao.showAllocations();

	        if (allocations.isEmpty()) {
	            System.out.println("⚠️  No asset allocations found.");
	            return;
	        }

	        System.out.println("---------------------------------------------------------------------------------");
            System.out.println("\t\t\t\tAsset Allocation LIST");
            System.out.println("---------------------------------------------------------------------------------");
            System.out.printf("%-15s %-10s %-10s %-15s %-15s \n", 
                "|  Allocation ID", "| Asset ID  ", " | Employee ID ", "| Allocation Date", " | ReturnDate   |");
            System.out.println("---------------------------------------------------------------------------------");

	        for (AssetAllocation allocation : allocations) {
	            System.out.printf("| %-15d | %-10d | %-10d | %-15s | %-15s |\n",
	                              allocation.getAllocationId(),
	                              allocation.getAssetId(),
	                              allocation.getEmployeeId(),
	                              allocation.getAllocationDate(),
	                              allocation.getReturnDate() != null ? allocation.getReturnDate() : "Not Returned");
	        }

	        System.out.println("---------------------------------------------------------------------------------\n");

	    } catch (Exception e) {
	        System.out.println("❌ Error fetching allocations: " + e.getMessage());
	    }
	}


	public static Date convertSql(java.util.Date utilDate) {
        return new Date(utilDate.getTime());
    }
	
	private static void deleteAsset() {
		System.out.print("Enter the Asset_ID for the asset you want to delete: ");
	    int assetId = scanner.nextInt();

	    try {
	        boolean deleted = assetDao.deleteAsset(assetId);
	        if (deleted) {
	            System.out.println("✅ Asset deleted successfully.");
	        } else {
	            System.out.println("❌ Failed to delete the asset.");
	        }
	    } catch (AssetNotFoundException e) {
	        System.out.println(e.getMessage());
	    } catch (Exception e) {
	        System.out.println("❌ Error: " + e.getMessage());
	    }
	}

	private static void updateAssets() {
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	    

	    try {
	        System.out.print("Enter Asset ID to update: ");
	        int assetId = scanner.nextInt();
	        scanner.nextLine();
	        
	        Asset existing = assetDao.searchAsset(assetId);
	        
	        Asset asset = new Asset();
	        asset.setAssetId(assetId);
	        
	        System.out.print("Enter Updated Name: ");
	        asset.setName(scanner.nextLine());

	        System.out.print("Enter Updated Type: ");
	        asset.setType(scanner.nextLine());

	        System.out.print("Enter Updated Serial Number: ");
	        asset.setSerialNumber(scanner.nextLine());

	        System.out.print("Enter Updated Purchase Date (YYYY-MM-DD): ");
	        java.util.Date date = sdf.parse(scanner.nextLine());
	        asset.setPurchaseDate(convertSql(date));

	        System.out.print("Enter Updated Location: ");
	        asset.setLocation(scanner.nextLine());

	        System.out.print("Enter Updated Status (in_use, decommissioned, under_maintenance): ");
	        asset.setStatus(AssetStatus.valueOf(scanner.nextLine().toLowerCase()));

	        System.out.print("Enter Updated Owner ID: ");
	        asset.setOwnerId(scanner.nextInt());
	        scanner.nextLine();

	        boolean updated = assetDao.updateAsset(asset);
	        if (updated) {
	            System.out.println("✅ Asset updated successfully!");
	        }
	    } catch (AssetNotFoundException e) {
	        System.out.println(e.getMessage());
	    } catch (Exception e) {
	        System.out.println("❌ Error: " + e.getMessage());
	    }
	}


	private static void addAssets() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		try {
	        Asset asset = new Asset();

	        System.out.print("Enter Name: ");
	        scanner.nextLine(); 
	        asset.setName(scanner.nextLine());

	        System.out.print("Enter Type: ");
	        asset.setType(scanner.nextLine());

	        System.out.print("Enter Serial Number: ");
	        asset.setSerialNumber(scanner.nextLine());

	        System.out.print("Enter Purchase Date (YYYY-MM-DD): ");
	        java.util.Date date = sdf.parse(scanner.nextLine());
	        asset.setPurchaseDate(convertSql(date));

	        System.out.print("Enter Location: ");
	        asset.setLocation(scanner.nextLine());

	        System.out.print("Enter Status (in_use, decommissioned, under_maintenance): ");
	        asset.setStatus(AssetStatus.valueOf(scanner.nextLine().toLowerCase()));

	        System.out.print("Enter Owner ID: ");
	        asset.setOwnerId(scanner.nextInt());

	        boolean added = assetDao.addAsset(asset);
	        if (added) {
	            System.out.println("✅ Asset added successfully!");
	        } else {
	            System.out.println("❌ Failed to add asset.");
	        }

	    } catch (Exception e) {
	        System.out.println("❌ Error: " + e.getMessage());
	    }
	}

	private static void showAssets(){
		try {
			List<Asset> assetList = assetDao.showAsset();
            System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------");
            System.out.println("\t\t\t\t\t\t\tAsset LIST");
            System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------");
            System.out.printf("%-10s %-25s %-15s %-15s %-15s %-15s %-17s %-10s\n", 
                "|  Asset ID ", "| Name    ", "  | Type    ", "   | Serial number  ", "  | Purchase date  ", "| Locatoin ", "  | Status ", "  | Owner Id |");
            System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------");

            for (Asset asset : assetList) {
            	System.out.printf("| %-10s | %-25s | %-15s | %-15s | %-15s | %-15s | %-17s | %-10s |\n", 
                    asset.getAssetId(), asset.getName(),asset.getType(), asset.getSerialNumber(), asset.getPurchaseDate(), asset.getLocation(),
                    asset.getStatus(), asset.getOwnerId());
                    }
            System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------\n");
        } catch (ClassNotFoundException | SQLException e) {
        	e.printStackTrace();
		}
		
	}
}



// TO DO
//	1. If the item is decommissioned or under maintenance don't allocate (Only update if its available)
//	2. Create enum available After deallocating update the status to Available
//	3. After allocating, performing maintenance, reserving update the status in asset respectively
//	4. If asset is used by other then first deallocate it from the owner and then alloacate to new
//	5. 