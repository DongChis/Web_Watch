/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import context.DBContext;
import entity.Product;
import entity.User;

/**
 *
 * @author trinh
 */
public class DAO {

	Connection conn = null;
	PreparedStatement ps = null;
	ResultSet rs = null;
    public static DAO instance = null;

	public DAO() {
	};

	public static DAO getInstance() {
		if (instance == null) {
			instance = new DAO();
		}
		return instance;
	}


    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM Products";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                products.add(new Product(rs.getInt("ProductID"), 
                                         rs.getString("Title"), 
                                         rs.getString("Name"), 
                                         rs.getString("Description"),
                                         rs.getDouble("Price"),
                                         rs.getString("ImageURL"),
                                         rs.getString("Gender")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return products;
    }

    public User Login(String user, String pass) {
    	String query = "SELECT * FROM Users WHERE [Username] = ? AND Password = ?";

		try {
			conn = new DBContext().getConnection();
			ps = conn.prepareStatement(query);
			ps.setString(1, user);
			ps.setString(2, pass);
			rs = ps.executeQuery();
			while (rs.next()) {
				return new User(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5),rs.getDate(6));

			}	
		} catch (Exception e) {
		}

		return null;

	}

	public void SignUp(String user, String pass) {
		String query = "insert into Users \n" + " values (?,?,0,0)";
		try {
			conn = new DBContext().getConnection();
			ps = conn.prepareStatement(query);
			ps.setString(1, user);
			ps.setString(2, pass);
			ps.executeUpdate();
		} catch (Exception e) {
		}

	}



}
