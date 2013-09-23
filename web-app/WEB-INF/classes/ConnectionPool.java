package com.vippan.db;

import java.sql.*;
import java.util.*;

public class ConnectionPool{
	private Hashtable connections;
	private int increment;	
	public ConnectionPool(int intialConnections,int increment) throws SQLException,ClassNotFoundException,InstantiationException,IllegalAccessException{
		Class.forName("org.gjt.mm.mysql.Driver").newInstance(); 
		this.increment=increment;
		connections=new Hashtable();
		for(int i=0;i<intialConnections;i++){
			connections.put(DriverManager.getConnection("jdbc:mysql://localhost/net4engineers_com?user=net4engineers&password=database"),Boolean.FALSE);
		}
	}
	public Connection getConnection() throws SQLException{
		Connection con=null;
		Enumeration cons=connections.keys();
		synchronized (connections){
			while(cons.hasMoreElements()){
				con=(Connection)cons.nextElement();
				Boolean b=(Boolean)connections.get(con);
				if(b==Boolean.FALSE){
					try{
						con.setAutoCommit(true);
					}
					catch(SQLException e){
						con=DriverManager.getConnection("jdbc:mysql://localhost/net4engineers_com?user=net4engineers&password=database");
					}
					return con;
				}
			}
		}
		for(int i=0;i<increment;i++){
			connections.put(DriverManager.getConnection("jdbc:mysql://localhost/net4engineers_com?user=net4engineers&password=database"),Boolean.FALSE);
		}
		return getConnection();
	}
	public void returnConnection(Connection returned){
		Connection con;
		Enumeration cons=connections.keys();
		while(cons.hasMoreElements()){
			con=(Connection)cons.nextElement();
			if(con==returned){
				connections.put(con,Boolean.FALSE);
				break;
			}
		}
	}
}