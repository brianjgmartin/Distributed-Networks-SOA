/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.memory;

import javax.jws.Oneway;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.mysql.jdbc.*;

/**
 *
 * @author Brian Martin
 */
@WebService(serviceName = "MemoryWS")
public class MemoryWS extends ConnectionDB{
    
   
	public String currentUser="";
            // The newInstance() call is a work around for some
            // broken Java implementations

            
        
            // handle the error
        
    


    /**
     * Web service operation
     */
    @WebMethod(operationName = "test")
    public String test() {
           
   
        //TODO write your implementation code here:
    	//Connection conn = null;
			
			String result ="ii";
			try{
                            
				Connection conn = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
                               if(conn == null){
                                   result = result  +"No C";
                               }
                               else{
                                   result = result + "yes";
                               }
		

			
			}
			catch(SQLException e){
				System.err.println(e+"kkk");
			
			} return result;
	}

    /**
     * Web service operation
     */
    @WebMethod(operationName = "register")
    public String register(@WebParam(name = "username") String username, @WebParam(name = "password") String password) {
   
        Connection conn = null;
    Statement stmt = null;
    currentUser = username;
        try {
			conn = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
                        
			System.out.println("connected Memories");
			// STEP 4: Execute a query
			System.out.println("Inserting records into the table...");
			stmt = conn.createStatement();

			String sql = "INSERT INTO Users(USERNAME,PASSWORD,RESOURCE_POINTS)"
					+ "VALUES ('" + username + "','" + password + "',20)";
			stmt.executeUpdate(sql);
		}

		catch (SQLException e) {
			System.err.println(e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
        return null;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "getUser")
    public String getUser() {
        //TODO write your implementation code here:
        return currentUser;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "getUserID")
    public Integer getUserID() {
        //TODO write your implementation code here:
        	Connection conn = null;
		Statement stmt = null;
		int result = 0;

		try {
			conn = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
			stmt = conn.createStatement();
			String sql = "select U_ID from USERS " + "where username = '"+getUser()+"'";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {

				result = rs.getInt(1);
			}
		} catch (SQLException e) {
			System.err.println(e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
		return result;
	}
    

    /**
     * Web service operation
     */
    @WebMethod(operationName = "login")
    public Boolean login(@WebParam(name = "username") String username, @WebParam(name = "password") String password) {
       Connection conn = null;
		Statement stmt = null;
		currentUser = username;
		System.out.println(currentUser);
		boolean log = false;
		try {
			conn = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
			System.out.println("connected Tble");
			// STEP 4: Execute a query
			System.out.println("Login...");
			stmt = conn.createStatement();

			String sql = "SELECT * FROM Users WHERE USERNAME = '" + username
					+ "'" + "and PASSWORD = '" + password + "'";

			ResultSet rs = stmt.executeQuery(sql);
			// System.out.println(rs);
			if (rs.next()) {
				log = true;
			} else {
				log = false;

			}
			rs.close();

		} catch (SQLException e) {
			System.err.println(e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
		return log;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "addMemory")
    @Oneway
    public void addMemory(@WebParam(name = "memory") String memory) {
        Connection conn = null;
		Statement stmt = null;
		try {
			conn = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
			System.out.println("connected Tble");
			// STEP 4: Execute a query
			System.out.println("Inserting records into the Memory table...");
			stmt = conn.createStatement();

			String sql = "INSERT INTO Memories(Memory,U_ID) " + "VALUES ('"
					+ memory + "',(Select U_ID from users where USERNAME = '"
					+ getUser() + "'))";
			stmt.executeUpdate(sql);
			System.out.println("Congrats you have added a memory");
		} catch (SQLException e) {
			System.err.println(e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "delete")
    public String delete(@WebParam(name = "memory") String memory) {
       Connection conn = null;
		Statement stmt = null;
		try {
			conn = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);

			stmt = conn.createStatement();

			String sql = "DELETE FROM Memories " + "where memory = '" + memory
					+ "'"
					+ "and U_ID = (select U_ID from users where username ='"
					+ getUser() + "')";
			stmt.executeUpdate(sql);

		} catch (SQLException e) {
			System.err.println(e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}

			}
		}return "Memory " + memory+ "Deleted";
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "addResource")
    @Oneway
    public void addResource(@WebParam(name = "memory") String memory, @WebParam(name = "resource") String resource) {
        Connection conn = null;
		Statement stmt = null;
		int rp;
		boolean flag = false;

		try {
			conn = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
			stmt = conn.createStatement();
			String sql = "SELECT RESOURCE_POINTS FROM USERS WHERE U_ID = (SELECT U_ID FROM Memories where Memory = '"
					+ memory + "') ";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				// Retrieve by column name
				rp = rs.getInt("Resource_Points");
				System.out.println(rp);
				if (rp > 2) {
					flag = true;
				} else {
					System.out.println("Sorry you do not have enough points");
				}
			}
rs.close();
			do {
				sql = "INSERT INTO Resources(Resource,M_ID) " + "VALUES ('"
						+ resource
						+ "',(Select M_ID from Memories where Memory = '"
						+ memory + "'))";
				stmt.executeUpdate(sql);

				sql = "SELECT RESOURCE_POINTS FROM USERS WHERE U_ID ="
						+ " (SELECT U_ID FROM Memories where Memory = '"
						+ memory + "') ";
				rs = stmt.executeQuery(sql);
				rs = stmt.executeQuery(sql);
				while (rs.next()) {
					// Retrieve by column name
					int id = rs.getInt("Resource_Points");
					id = id - 2;
					System.out.println(id + "mm");

					sql = "UPDATE Users " + "SET Resource_Points = " + id + " "
							+ "where U_ID = (SELECT U_ID FROM Memories where'"
							+ memory + "' = '" + memory + "')";
				}
				rs.close();
				stmt.executeUpdate(sql);
				flag = false;

				System.out.println("Congrats you have added a resource");
				// while goes here
			} while (flag);

		}

		catch (SQLException e) {
			System.err.println(e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "viewMemories")
    public String viewMemories() {
       Connection conn = null;
		Statement stmt = null;

		String result = "";

		try {
			conn = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);

			stmt = conn.createStatement();

			String sql = "Select memory FROM Memories "
					+ "where U_ID = (SELECT U_ID from Users where username ='"
					+ getUser() + "')";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {

				result = rs.getString(1) + " " + result;
			}

		} catch (SQLException e) {
			System.err.println(e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}

			}
		}
		return "Your current stored Memories are" + "\n" + result;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "viewInvites")
    public boolean viewInvites() {
        //TODO write your implementation code here:
        Connection conn = null;
		Statement stmt = null;
		boolean flag = false;

		try {
			conn = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);

			stmt = conn.createStatement();

			String sql = "Select request from friends " + "where FRIEND_ID = '"
					+ getUserID() + "'" + "and status = 0";

			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				int id = rs.getInt("Request");
				if (id != 0) {
					flag = true;
				} else {
					flag = false;
				}
			}

		} catch (SQLException e) {

			System.err.println(e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}

			}
		}
		return flag;
	}

    /**
     * Web service operation
     */
    @WebMethod(operationName = "sendInvite")
    @Oneway
    public void sendInvite(@WebParam(name = "friendUsername") String friendUsername) {
 Connection conn = null;
		Statement stmt = null;

		try {
			conn = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);

			stmt = conn.createStatement();

			stmt = conn.createStatement();
			String sql = "INSERT INTO Friends(Sender_ID,Friend_ID,Request,status)"
					+ "VALUES ('"
					+ getUserID()
					+ "',(SELECT U_ID from Users where username = '"
					+ friendUsername + "'),1,0)";

			stmt.executeUpdate(sql);

		} catch (SQLException e) {
			System.err.println(e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}

			}

		}
	}

    /**
     * Web service operation
     */
    @WebMethod(operationName = "viewInviteSender")
    public String viewInviteSender() {
        Connection conn = null;
		Statement stmt = null;
		String sender = "";

		try {
			conn = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);

			stmt = conn.createStatement();

			String sql = "Select username from users "
					+ "where U_ID = (Select sender_ID from friends)";

			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				sender = rs.getString(1);
				;

			}

		} catch (SQLException e) {

			System.err.println(e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}

			}
		}
		return sender;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "acceptInvite")
    @Oneway
    public void acceptInvite() {
        
		Connection conn = null;
		Statement stmt = null;

		try {
			conn = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);

			stmt = conn.createStatement();

			String sql = " Update Friends "
			+ "set status = 1";
			//+ "where F_ID=1";
			
			stmt.executeUpdate(sql);
			
			stmt = conn.createStatement();
			sql = "SELECT RESOURCE_POINTS FROM USERS WHERE U_ID ="
					+ " (SELECT Sender_Id FROM friends) ";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				// Retrieve by column name
				int points = rs.getInt("Resource_Points");
				points = points + 10;
				System.out.println(points + "mm");

				sql = "UPDATE Users " + "SET Resource_Points = " + points + " "
						+ "where U_ID = (SELECT Sender_ID FROM Friends )";
			}
			rs.close();
			stmt.executeUpdate(sql);
			

		} catch (SQLException e) {
			System.err.println(e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
    }
    

   
    }

    

  

  

