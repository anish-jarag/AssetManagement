package com.java.assetmanagement;

import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Scanner;

import com.java.assetmanagement.dao.AssetManagementService;
import com.java.assetmanagement.dao.AssetManagementServiceImpl;
import com.java.assetmanagement.model.Asset;
import com.java.assetmanagement.model.AssetAllocation;
import com.java.assetmanagement.model.AssetStatus;
import com.java.assetmanagement.model.Employee;
import com.java.assetmanagement.model.MaintenanceRecord;
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
	                              reservation.getEmployeeId(),
	                              reservation.getAllocationDate(),
	                              reservation.getReturnDate());
	        }

	        System.out.println("-------------------------------------------------------------------------------------\n");

	    } catch (Exception e) {
	        System.out.println("❌ Error fetching reservations: " + e.getMessage());
	    }
	}


	private static void performMaintenance() {
	    System.out.print("Enter Asset ID for maintenance: ");
	    int assetId = scanner.nextInt();
	    scanner.nextLine();

	    try {
	        Asset asset = assetDao.searchAsset(assetId);

	        boolean isAllocated = assetDao.isAssetAllocated(assetId);

	        if (isAllocated) {
	            System.out.println("⚠️ Asset is currently allocated. Do you want to deallocate and proceed with maintenance? (yes/no): ");
	            String response = scanner.nextLine();

	            if (!response.equalsIgnoreCase("yes")) {
	                System.out.println("❌ Maintenance aborted.");
	                return;
	            }

	            System.out.print("Enter Employee ID to deallocate: ");
	            int empId = scanner.nextInt();
	            scanner.nextLine();

	            System.out.print("Enter Return Date (YYYY-MM-DD): ");
	            String returnDate = scanner.nextLine();

	            boolean deallocated = assetDao.deallocateAsset(assetId, empId, returnDate);

	            if (!deallocated) {
	                System.out.println("❌ Failed to deallocate. Cannot proceed with maintenance.");
	                return;
	            } else {
	                System.out.println("✅ Asset deallocated successfully.");
	            }
	        }

	        System.out.print("Enter Maintenance Date (YYYY-MM-DD): ");
	        String maintenanceDate = scanner.nextLine();

	        System.out.print("Enter Maintenance Description: ");
	        String description = scanner.nextLine();

	        System.out.print("Enter Maintenance Cost: ");
	        double cost = scanner.nextDouble();
	        scanner.nextLine();

	        boolean success = assetDao.performMaintenance(assetId, maintenanceDate, description, cost);

	        if (success) {
	            System.out.println("✅ Maintenance recorded successfully.");
	        } else {
	            System.out.println("❌ Failed to perform maintenance.");
	        }

	    } catch (AssetNotFoundException e) {
	        System.out.println("❌ Error: " + e.getMessage());
	    } catch (Exception e) {
	        System.out.println("❌ Error: " + e.getMessage());
	    }
	}



	private static void showMaintenanceRecords() {
	    try {
	        List<MaintenanceRecord> maintenanceList = assetDao.showMaintenanceRecord();

	        if (maintenanceList.isEmpty()) {
	            System.out.println("⚠️ No maintenance records found.");
	            return;
	        }

	        System.out.println("----------------------------------------------------------------------------------------------");
	        System.out.println("\t\t\t\tMaintenance Record List");
	        System.out.println("----------------------------------------------------------------------------------------------");
	        System.out.printf("%-15s %-10s %-15s %-40s %-10s\n",
	                "| Maintenance ID", "| Asset ID", "| Date", "| Description", "| Cost    |");
	        System.out.println("----------------------------------------------------------------------------------------------");

	        for (MaintenanceRecord record : maintenanceList) {
	            System.out.printf("| %-14d | %-9d | %-13s | %-38s | ₹%-8.2f |\n",
	                    record.getMaintenanceId(),
	                    record.getAssetId(),
	                    record.getMaintenanceDate(),
	                    record.getDescription(),
	                    record.getCost());
	        }

	        System.out.println("----------------------------------------------------------------------------------------------\n");

	    } catch (Exception e) {
	        System.out.println("❌ Error fetching maintenance records: " + e.getMessage());
	    }
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

	        if (existing.getStatus() == AssetStatus.IN_USE) {
	            System.out.println("⚠️ Cannot update asset while it is currently in use.");
	            return;
	        }

	        Asset asset = new Asset();
	        asset.setAssetId(assetId);

	        System.out.print("Enter Updated Name (" + existing.getName() + "): ");
	        String name = scanner.nextLine();
	        asset.setName(name.isEmpty() ? existing.getName() : name);

	        System.out.print("Enter Updated Type (" + existing.getType() + "): ");
	        String type = scanner.nextLine();
	        asset.setType(type.isEmpty() ? existing.getType() : type);

	        System.out.print("Enter Updated Serial Number (" + existing.getSerialNumber() + "): ");
	        String serialNumber = scanner.nextLine();
	        asset.setSerialNumber(serialNumber.isEmpty() ? existing.getSerialNumber() : serialNumber);

	        System.out.print("Enter Updated Purchase Date (" + existing.getPurchaseDate() + ") [YYYY-MM-DD]: ");
	        String dateInput = scanner.nextLine();
	        if (dateInput.isEmpty()) {
	            asset.setPurchaseDate(existing.getPurchaseDate());
	        } else {
	            java.util.Date date = sdf.parse(dateInput);
	            if (date.after(new java.util.Date())) {
	                System.out.println("❌ Purchase date cannot be in the future.");
	                return;
	            }
	            asset.setPurchaseDate(convertSql(date));
	        }

	        System.out.print("Enter Updated Location (" + existing.getLocation() + "): ");
	        String location = scanner.nextLine();
	        asset.setLocation(location.isEmpty() ? existing.getLocation() : location);

	        System.out.print("Enter Updated Status (" + existing.getStatus() + ") [in_use, decommissioned, under_maintenance]: ");
	        String statusInput = scanner.nextLine();
	        if (statusInput.isEmpty()) {
	            asset.setStatus(existing.getStatus());
	        } else {
	            try {
	                asset.setStatus(AssetStatus.valueOf(statusInput.toUpperCase()));
	            } catch (IllegalArgumentException e) {
	                System.out.println("❌ Invalid status entered. Allowed: IN_USE, DECOMMISSIONED, UNDER_MAINTENANCE");
	                return;
	            }
	        }

	        System.out.print("Enter Updated Owner ID (" + existing.getOwnerId() + "): ");
	        String ownerInput = scanner.nextLine();
	        if (ownerInput.isEmpty()) {
	            asset.setOwnerId(existing.getOwnerId());
	        } else {
	            int ownerId = Integer.parseInt(ownerInput);
	            if (ownerId <= 0) {
	                System.out.println("❌ Owner ID must be a positive number.");
	                return;
	            }
	            asset.setOwnerId(ownerId);
	        }

	        boolean updated = assetDao.updateAsset(asset);
	        if (updated) {
	            System.out.println("✅ Asset updated successfully!");
	        } else {
	            System.out.println("❌ Failed to update asset.");
	        }

	    } catch (AssetNotFoundException e) {
	        System.out.println(e.getMessage());
	    } catch (ParseException e) {
	        System.out.println("❌ Invalid date format. Please use YYYY-MM-DD.");
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

	        System.out.print("Enter Status (AVAILABLE, IN_USE, DECOMISSIONED, UNDER_MAINTENANCE, RESERVED): ");
	        asset.setStatus(AssetStatus.valueOf(scanner.nextLine().toUpperCase()));
	        
	        System.out.print("Enter Owner Id: ");
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
//	3. After allocating, performing maintenance, reserving update the status in asset respectively
//	4. If asset is used by other then asset cant be allocated
//	5. Return date cant be less than allocation date
//  6. Maintenance cant be done if the product is allocated or reserved
//  7. Start date cant be less reservation date




