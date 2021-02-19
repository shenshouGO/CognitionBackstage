package tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import net.sf.json.JSONObject;

public class DB {
	
	//数据库连接信息
	private String url = "jdbc:mysql://localhost:3306/cognition?useSSL=false&characterEncoding=utf-8";
//	private String url = "jdbc:mysql://47.95.197.189:3306/cognition?useSSL=false&characterEncoding=utf-8";
	private String user = "root";
	private String password = "";
//	private String password = "0X4ae7d235.";
	private String db_driver = "com.mysql.jdbc.Driver";
	
	//数据库链接对象
	private Connection conn;
	
	//数据库命令执行对象
	private Statement stmt;
	private PreparedStatement pstmt;
	private ResultSet rs;
	private JSONObject obj;
	
	/**
	 * 连接数据库
	 */
	private void connect()
	{
		try
		{
			Class.forName(db_driver);
		}
		catch
		(ClassNotFoundException e1)
		{
			e1.printStackTrace();
		}
		if(conn == null)
		try
		{
			conn = DriverManager.getConnection(url, user, password);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	//数据查找
	public JSONObject query(String sql,Object ...args) throws SQLException
	{
		connect();
		try
		{
			pstmt = conn.prepareStatement(sql);
			for(int i=0;i<args.length;i++)
			{
				pstmt.setObject(i+1,args[i]);
			}
				
			rs = pstmt.executeQuery();
			obj = getMap(rs);
			return obj;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	//数据增删改 运用可变参数
	public int update(String sql, Object ...args) throws SQLException
	{
		connect();
		//创建预处理对象
		pstmt = conn.prepareStatement(sql);
		//为PreparedStatement对象设置SQL参数
		for (int i = 0 ; i < args.length ; ++ i)
		{
			pstmt.setObject(1 + i, args[i]) ; 
		}
		//执行
		return pstmt.executeUpdate();
	}
	
	/**
	 * 关闭连接
	 */
	public void close()
	{
		try
		{
			pstmt.close();
			stmt.close();
			conn.close();
		} 
		catch(SQLException e)
		{
			// 处理 JDBC 错误
			e.printStackTrace();
		}
		catch(Exception e){
	        // 处理 Class.forName 错误
	        e.printStackTrace();
	    }finally{
        // 关闭资源
	    	try{
	    		if(stmt!=null) stmt.close();
	        }
	    	catch(SQLException se2)
	    	{}// 什么都不做
	        try{
	            if(conn!=null) conn.close();
	        }
	        catch(SQLException se)
	        {
	            se.printStackTrace();
	        }
	    }
	}
	
	//获取当前时间戳
	public long getStamp(){
		long timeStamp = System.currentTimeMillis(); 
		return  timeStamp;
	}
	
	/*
	 * 将结果转化为JSON格式
	 */
	public JSONObject getMap(ResultSet data) throws SQLException
	{
		int j = 0;
		//进入循环，指针直接指向第二行
		obj = new JSONObject();
		
		while(data.next())//循环将值存入到数组集合中,使用短路运算符，获得第一条记录
		{
			JSONObject obj2 = new JSONObject();
			ResultSetMetaData  result = data.getMetaData();
			for (int i = 1; i <= result.getColumnCount(); i++)
			{
				obj2.put(result.getColumnName(i), data.getString(result.getColumnName(i)));
			}
			obj.put(String.valueOf(j) , obj2);
			j++;
		}
		data.close();
		return obj;
	}
}
