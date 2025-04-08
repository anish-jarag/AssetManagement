package com.java.assetmanagement.myexceptions;

public class AssetNotMaintainException extends Exception{
	
    public AssetNotMaintainException(String lastMaintainedDate) {
        super("❌ Asset has not been maintained for over 2 years. Last maintained on: " + lastMaintainedDate);
    }
	
	public AssetNotMaintainException() {
		super();
	}
}
