package com.java.assetmanagement;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import com.java.assetmanagement.dao.AssetManagementService;
import com.java.assetmanagement.dao.AssetManagementServiceImpl;
import com.java.assetmanagement.model.Asset;

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
	        
	        System.out.println("1. Show Assets");
	        System.out.println("2. Add Asset");
	        System.out.println("3. Update Asset");
	        System.out.println("4. Delete Asset");

	        System.out.println("5. Show Allocations");
	        System.out.println("6. Allocate Asset");
	        System.out.println("7. Deallocate Asset");

	        System.out.println("8. Show Maintenance Record");
	        System.out.println("9. Perform Maintenance");

	        System.out.println("10. Show Reservation");
	        System.out.println("11. Reserve Asset");
	        System.out.println("12. Withdraw Reservation");

	        System.out.println("13. Exit");
	        System.out.println();
	        System.out.print("Enter Your Choice:  ");
	        choice = scanner.nextInt();
	        switch (choice) {
	            case 1:
				showAssets();
	                break;
	            case 10:
	                return;
	        }
	    } while (choice != 10);
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
