package com.java.assetmanagement;

import java.sql.Connection;
import java.sql.SQLException;

import com.java.assetmanagement.util.ConnectionHelper;

public class App {

  public static void main(String[] args) throws ClassNotFoundException, SQLException {
    System.out.println("Hello World!");
    Connection connection = ConnectionHelper.getConnection();
  }

}
