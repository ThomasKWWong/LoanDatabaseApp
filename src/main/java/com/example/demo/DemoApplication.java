package com.example.demo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

    public static String query_creator(ArrayList<String> filters) {
        String SQL_SELECT = "Select * from SMALL_PRELIMINARY";
        if (filters.size() != 0) {
            SQL_SELECT = SQL_SELECT + " WHERE ";
        }
        for (String filter : filters){
         SQL_SELECT = SQL_SELECT + filter + " AND ";
        }
        //Remove trailing AND
        SQL_SELECT = SQL_SELECT.substring(0, SQL_SELECT.length() - 5);
        return SQL_SELECT;
    }   

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);

        //static example for query_creator
        ArrayList<String> filters = new ArrayList<>(Arrays.asList("MSAMD = 41180", "COUNTY_CODE = 189"));
		String SQL_SELECT = query_creator(filters);

		//Open database connection
		try (Connection conn = DriverManager.getConnection(
                "jdbc:postgresql://127.0.0.1:5432/postgres", "postgres", "Tkw321123$")) {

            if (conn != null) {
                System.out.println("Connected to the database!");
                System.out.println(SQL_SELECT);
				PreparedStatement preparedStatement = conn.prepareStatement(SQL_SELECT);
				ResultSet resultSet = preparedStatement.executeQuery();
				while (resultSet.next()){
					System.out.println(resultSet.getString("RESPONDENT_ID"));
				}
            } else {
                System.out.println("Failed to make connection!");
            }

        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }

	}

}
