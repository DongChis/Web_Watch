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

/**
 *
 * @author trinh
 */
public class DAO {

	static Connection conn = null;
	PreparedStatement ps = null;
	ResultSet rs = null;

	private static DAO instance = null;

	private DAO() {
	};




}
