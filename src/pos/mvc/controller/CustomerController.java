/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos.mvc.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import pos.mvc.db.DbConnection;
import pos.mvc.model.Customer;

/**
 *
 * @author PC
 */
public class CustomerController {
    public String addCustomer(Customer customer) throws SQLException{
       Connection connection = DbConnection.getInstance().getConnection();
       
       String query = "INSERT INTO customer VALUES(?,?,?,?,?)";
       
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, customer.getId());
        preparedStatement.setString(2, customer.getName());
        preparedStatement.setString(3, customer.getAddress());
        preparedStatement.setString(4, customer.getEmail());
        preparedStatement.setString(5, customer.getPostalCode());
        
        if(preparedStatement.executeUpdate()>0){
            return "Success";
        } else {
            return "fail";
        }
    }
    
    public List<Customer> getCustomers() throws SQLException{
        Connection connection = DbConnection.getInstance().getConnection();
        String query = "SELECT * FROM customer";
        
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        
        ResultSet resultSet = preparedStatement.executeQuery();
        
        List<Customer> customers = new ArrayList<>();
        
        while (resultSet.next()) {
            Customer customer = new Customer(
                    resultSet.getInt("id"), 
                    resultSet.getString("name"), 
                    resultSet.getString("address"), 
                    resultSet.getString("email"), 
                    resultSet.getString("postalCode"));
            
            customers.add(customer);
        }
        
        return customers;
        
    } 
}
