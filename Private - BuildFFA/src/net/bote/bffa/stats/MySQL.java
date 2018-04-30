package net.bote.bffa.stats;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import net.bote.bffa.main.Main;

public class MySQL
{
  private String HOST = "";
  private String DATABASE = "";
  private String USER = "";
  private String PASSWORD = "";
  private Connection con;
  
  public MySQL(String host, String database, String user, String password)
  {
    this.HOST = host;
    this.DATABASE = database;
    this.USER = user;
    this.PASSWORD = password;
    
    connect();
  }
  
  public void connect()
  {
    try
    {
      this.con = DriverManager.getConnection("jdbc:mysql://" + this.HOST + ":3306/" + this.DATABASE + "?autoReconnect=true", 
        this.USER, this.PASSWORD);
      System.out.println("[MySQL] Verbindung hergestellt!");
    }
    catch (SQLException e)
    {
      System.out.println("[MySQL] Es konnte keine Verbindung aufgebaut werden! Fehler: " + e.getMessage());
    }
  }
  
  public void close()
  {
    try
    {
      if (this.con != null)
      {
        this.con.close();
        System.out.println("[MySQL] Verbindung getrennt!");
      }
    }
    catch (SQLException e)
    {
      System.out.println("[MySQL] Konnte nicht richtig beenden! Fehler: " + e.getMessage());
    }
  }
  
  public void update(String qry)
  {
    try
    {
      Statement st = this.con.createStatement();
      st.executeUpdate(qry);
      st.close();
    }
    catch (SQLException e)
    {
      connect();
      System.err.println(e);
    }
  }
  
  public boolean isConnected() {
	  if(con != null) {
		  return true;
	  } else {
		  return false;
	  }
  }
  
  public ResultSet query(String qry)
  {
    ResultSet rs = null;
    try
    {
      Statement st = this.con.createStatement();
      rs = st.executeQuery(qry);
    }
    catch (SQLException e)
    {
      connect();
      System.err.println(e);
    }
    return rs;
  }
  
  public static boolean playerExists(String uuid)
  {
    try
    {
      ResultSet rs = Main.mysql.query("SELECT * FROM BFFAStats WHERE UUID= '" + uuid + "'");
      if (rs.next()) {
        return rs.getString("UUID") != null;
      }
      return false;
    }
    catch (SQLException e)
    {
      e.printStackTrace();
    }
    return false;
  }
  
  public static void createPlayer(String uuid)
  {
    if (!playerExists(uuid)) {
      Main.mysql.update("INSERT INTO BFFAStats (UUID, KILLS, DEATHS) VALUES ('" + uuid + 
        "', '0', '0');");
    }
  }
  }
