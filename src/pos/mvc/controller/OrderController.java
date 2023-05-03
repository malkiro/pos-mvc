/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pos.mvc.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;
import pos.mvc.db.DbConnection;
import pos.mvc.model.Order;
import pos.mvc.model.OrderDetail;

/**
 *
 * @author anjanathrishakya
 */
public class OrderController {
    
    public String placeOrder(Order order, List<OrderDetail> orderDetails) throws SQLException{
        Connection connection = DbConnection.getInstance().getConnection();
        try {
            connection.setAutoCommit(false);
            String orderInsertQuery = "INSERT INTO `order` VALUES (?,?,?,?)";
            
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            
            PreparedStatement preparedStatementForOrder = connection.prepareStatement(orderInsertQuery);
            preparedStatementForOrder.setInt(1, order.getId());
            preparedStatementForOrder.setInt(2, order.getCustId());
            preparedStatementForOrder.setString(3, sdf.format(order.getOrderDate()));
            preparedStatementForOrder.setDouble(4, order.getTotal());
            
            if(preparedStatementForOrder.executeUpdate()>0){
                String orderDetailInsertQuery = "INSERT INTO orderdetail (order_id, item_id, unit_price, qty, sub_total) VALUES (?,?,?,?,?)";
                
                boolean isOrderDetailsSaved = true;
                
                for (OrderDetail orderDetail : orderDetails) {
                    PreparedStatement preparedStatementForOrderDetail = connection.prepareStatement(orderDetailInsertQuery);
                    preparedStatementForOrderDetail.setInt(1, order.getId());
                    preparedStatementForOrderDetail.setInt(2, orderDetail.getItemId());
                    preparedStatementForOrderDetail.setDouble(3, orderDetail.getUnitPrice());
                    preparedStatementForOrderDetail.setInt(4, orderDetail.getQty());
                    preparedStatementForOrderDetail.setDouble(5, orderDetail.getUnitPrice() * orderDetail.getQty());
                    
                    if(!(preparedStatementForOrderDetail.executeUpdate() > 0)){
                        isOrderDetailsSaved = false;
                    }
                }
                if(isOrderDetailsSaved){
                    
                    String itemUpdateQuery = "UPDATE ITEM SET qoh = qoh - ? WHERE  id = ?";
                    boolean isItemUpdated = true;
                    
                    for (OrderDetail orderDetail : orderDetails) {
                        PreparedStatement preparedStatementForItem = connection.prepareStatement(itemUpdateQuery);
                        preparedStatementForItem.setInt(1, orderDetail.getQty());
                        preparedStatementForItem.setInt(2, orderDetail.getItemId());
                        
                        if(!(preparedStatementForItem.executeUpdate()>0)){
                            isItemUpdated = false;
                        }
                        
                    }
                    
                    if(isItemUpdated){
                        connection.commit();
                        return "Success";
                    } else {
                        connection.rollback();
                        return "Item Update Error";
                    }
                    
                } else {
                    connection.rollback();
                    return "Order Details Save Error";
                }
            } else{
                connection.rollback();
                return "Order Save Error";
            }
            
        } catch (Exception e) {
            connection.rollback();
            throw e;
            
        } finally {
            connection.setAutoCommit(true);
        }
    }
}
