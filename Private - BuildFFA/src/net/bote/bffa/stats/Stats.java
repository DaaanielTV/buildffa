package net.bote.bffa.stats;

import java.sql.ResultSet;
import java.sql.SQLException;

import net.bote.bffa.main.Main;

public class Stats {
	
	public static Double getKD(String uuid) {
		if(getDeaths(uuid) == 0) {
			return Double.valueOf(getDeaths(uuid).intValue());
		} else {
			double kd = Double.valueOf(getKills(uuid).intValue() / getDeaths(uuid).intValue());
			kd = Double.valueOf(Math.round(100.0D * kd) / 100.0D);
			return kd;
		}
	}
	
	public static Integer getKills(String uuid)
	  {
	    Integer i = Integer.valueOf(0);
	    if(MySQL.playerExists(uuid))
	    {
	      try
	      {
	        ResultSet rs = Main.mysql.query("SELECT * FROM BFFAStats WHERE UUID= '" + uuid + "'");
	        if ((rs.next()) && (Integer.valueOf(rs.getInt("KILLS")) == null)) {}
	        i = Integer.valueOf(rs.getInt("KILLS"));
	      }
	      catch (SQLException e)
	      {
	        e.printStackTrace();
	      }
	    }
	    else
	    {
	    MySQL.createPlayer(uuid);
	      getKills(uuid);
	    }
	    return i;
	  }
	  
	  public static Integer getDeaths(String uuid)
	  {
	    Integer i = Integer.valueOf(0);
	    if (MySQL.playerExists(uuid))
	    {
	      try
	      {
	        ResultSet rs = Main.mysql.query("SELECT * FROM BFFAStats WHERE UUID= '" + uuid + "'");
	        if ((rs.next()) && (Integer.valueOf(rs.getInt("DEATHS")) == null)) {}
	        i = Integer.valueOf(rs.getInt("DEATHS"));
	      }
	      catch (SQLException e)
	      {
	        e.printStackTrace();
	      }
	    }
	    else
	    {
	    MySQL.createPlayer(uuid);
	      getKills(uuid);
	    }
	    return i;
	  }
	  
	  public static void setKills(String uuid, Integer kills)
	  {
	    if (MySQL.playerExists(uuid))
	    {
	      Main.mysql.update("UPDATE BFFAStats SET KILLS= '" + kills + "' WHERE UUID= '" + uuid + "';");
	    }
	    else
	    {
	    	MySQL.createPlayer(uuid);
	      setKills(uuid, kills);
	    }
	  }
	  
	  public static void setDeaths(String uuid, Integer deaths)
	  {
	    if (MySQL.playerExists(uuid))
	    {
	      Main.mysql.update("UPDATE BFFAStats SET DEATHS= '" + deaths + "' WHERE UUID= '" + uuid + "';");
	    }
	    else
	    {
	    MySQL.createPlayer(uuid);
	      setDeaths(uuid, deaths);
	    }
	  }
	  
	  public static void addKill(String uuid)
	  {
	    Integer i = Integer.valueOf(1);
	    if (MySQL.playerExists(uuid))
	    {
	      setKills(uuid, Integer.valueOf(getKills(uuid).intValue() + i.intValue()));
	    }
	    else
	    {
	    MySQL.createPlayer(uuid);
	      addKill(uuid);
	    }
	  }
	  
	  public static void addDeath(String uuid)
	  {
	    Integer i = Integer.valueOf(1);
	    if (MySQL.playerExists(uuid))
	    {
	      setDeaths(uuid, Integer.valueOf(getDeaths(uuid).intValue() + i.intValue()));
	    }
	    else
	    {
	    MySQL.createPlayer(uuid);
	      addDeath(uuid);
	    }
	  }

}
