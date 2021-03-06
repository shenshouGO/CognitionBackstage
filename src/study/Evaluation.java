package study;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import net.sf.json.JSONObject;
import tools.DB;

public class Evaluation {
	DB db = new DB();
	
	public void createScore(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String u_id = request.getParameter("u_id");
		System.out.println("u_id = " + u_id);
		String c_r_id = request.getParameter("c_r_id");
		System.out.println("c_r_id = " + c_r_id);
		int validity_score = Integer.parseInt(request.getParameter("validity_score"));
		System.out.println("validity_score = " + validity_score);
		int novelty_score = Integer.parseInt(request.getParameter("novelty_score"));
		System.out.println("novelty_score = " + novelty_score);
		String sql;
		int num;
		JSONObject obj;
		
		try
		{
			sql = "select validity_score,novelty_score,number from cognition_result where id = ?";
			obj = db.query(sql, c_r_id);
			double v_s = obj.getJSONObject("0").getDouble("validity_score");
			System.out.println("v_s = " + v_s);
			double n_s = obj.getJSONObject("0").getDouble("novelty_score");
			System.out.println("n_s = " + n_s);
			int number = obj.getJSONObject("0").getInt("number");
			System.out.println("number = " + number);
			v_s = (v_s*number+validity_score)/(number+1);
			n_s = (n_s*number+novelty_score)/(number+1);
			number++;
			
			sql = "insert into cognition_score(u_id,c_r_id,validity_score,novelty_score) values(?,?,?,?)";
			num = db.update(sql, u_id,c_r_id,validity_score,novelty_score);
			if( num == 1)
			{
				sql = "update cognition_result set validity_score = ? , novelty_score = ? , number = ? where id = ?";
				num = db.update(sql,v_s,n_s,number,c_r_id);
				if( num == 1)
				{
					sql = "select * from cognition_result where id = ?";
					obj = db.query(sql, c_r_id);
					response.getWriter().write(obj.getJSONObject("0").toString());
				}
				else
					response.getWriter().write("Unsuccessfully");
			}
			else
				response.getWriter().write("Unsuccessfully");
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void matchUserAndScore(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String u_id = request.getParameter("u_id");
		String c_r_id = request.getParameter("c_r_id");
		String sql;
		JSONObject obj;
		
		try
		{
			sql = "select validity_score,novelty_score from cognition_score where u_id=? and c_r_id = ?";
			obj = db.query(sql,u_id,c_r_id);
			if(obj.isEmpty())
				response.getWriter().write("Unevaluated");
			else
				response.getWriter().write(obj.getJSONObject("0").toString());
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void matchUserAndCognitionGood(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String u_id = request.getParameter("u_id");
		String c_r_id = request.getParameter("c_id");
		String sql;
		JSONObject obj;
		
		try
		{
			sql = "select id from cognition_good where u_id=? and c_id = ?";
			obj = db.query(sql,u_id,c_r_id);
			if(obj.isEmpty())
				response.getWriter().write("No give good");
			else
				response.getWriter().write(obj.getJSONObject("0").get("id").toString());
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void createCognitionGood(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String u_id = request.getParameter("u_id");
		String c_r_id = request.getParameter("c_id");
		String sql;
		JSONObject obj;
		int num;
		
		try
		{
			sql = "insert cognition_good(c_id,u_id) values(?,?)";
			num = db.update(sql,c_r_id, u_id);
			if(num == 1)
			{
				sql = "update cognition_result set good = good+1 , score = score+1 where id = ?";
				num = db.update(sql, c_r_id);
				if(num == 1)
				{
					sql = "select id from cognition_good where u_id=? and c_id = ?";
					obj = db.query(sql,u_id,c_r_id);
					if(obj.isEmpty())
						response.getWriter().write("Give good unsuccessfully");
					else
						response.getWriter().write(obj.getJSONObject("0").get("id").toString());
				}
				else
					response.getWriter().write("Give good unsuccessfully");
			}
			else
				response.getWriter().write("Give good unsuccessfully");
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void deleteCognitionGood(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String g_id = request.getParameter("ID");
		System.out.println("g_id = " + g_id);
		String sql;
		int num;
		JSONObject obj;
		
		try
		{
			sql = "select c_id from cognition_good where id = ?";
			obj = db.query(sql, g_id);
			if(obj.isEmpty())
				response.getWriter().write("Delete unsuccessfully");
			else
			{
				int c_r_id = obj.getJSONObject("0").getInt("c_id");
				sql = "delete from cognition_good where id = ?";
				num = db.update(sql, g_id);
				if(num == 1)
				{
					sql = "update cognition_result set good = good - 1, score = score - 1 where id = ?";
					num = db.update(sql, c_r_id);
					if(num == 1)
						response.getWriter().write("Delete successfully");
					else
						response.getWriter().write("Delete unsuccessfully");
				}
				else
					response.getWriter().write("Delete unsuccessfully");
			}
			
				
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void matchUserAndResourceGood(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String u_id = request.getParameter("u_id");
		String r_id = request.getParameter("r_id");
		String sql;
		JSONObject obj;
		
		try
		{
			sql = "select id from resource_good where u_id=? and r_id = ?";
			obj = db.query(sql,u_id,r_id);
			if(obj.isEmpty())
				response.getWriter().write("No give good");
			else
				response.getWriter().write(obj.getJSONObject("0").get("id").toString());
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void createResourceGood(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String u_id = request.getParameter("u_id");
		String r_id = request.getParameter("r_id");
		String sql;
		JSONObject obj;
		int num;
		
		try
		{
			sql = "insert resource_good(r_id,u_id) values(?,?)";
			num = db.update(sql,r_id, u_id);
			if(num == 1)
			{
				sql = "update cognition_resource set good = good+1 , score = score+1 where id = ?";
				num = db.update(sql, r_id);
				if(num == 1)
				{
					sql = "select id from resource_good where u_id=? and r_id = ?";
					obj = db.query(sql,u_id,r_id);
					if(obj.isEmpty())
						response.getWriter().write("Give good unsuccessfully");
					else
						response.getWriter().write(obj.getJSONObject("0").get("id").toString());
				}
				else
					response.getWriter().write("Update good unsuccessfully");
			}
			else
				response.getWriter().write("Insert good unsuccessfully");
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void deleteResourceGood(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String g_id = request.getParameter("ID");
		System.out.println("g_id = " + g_id);
		String sql;
		int num;
		JSONObject obj;
		
		try
		{
			sql = "select r_id from resource_good where id = ?";
			obj = db.query(sql, g_id);
			if(obj.isEmpty())
				response.getWriter().write("Good is unexited");
			else
			{
				int c_r_id = obj.getJSONObject("0").getInt("r_id");
				sql = "delete from resource_good where id = ?";
				num = db.update(sql, g_id);
				if(num == 1)
				{
					sql = "update cognition_resource set good = good - 1, score = score - 1 where id = ?";
					num = db.update(sql, c_r_id);
					if(num == 1)
						response.getWriter().write("Delete successfully");
					else
						response.getWriter().write("update unsuccessfully");
				}
				else
					response.getWriter().write("Delete unsuccessfully");
			}
			
				
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void share(HttpServletRequest request, HttpServletResponse response) throws IOException{
		String id = request.getParameter("ID");
		
		try {
			String sql = "update cognition_result set share = share+1, score = score+2 where id = ?";
			int num = db.update(sql, id);
			if(num == 1)
				response.getWriter().write("Share successfully");
			else
				response.getWriter().write("Share unsuccessfully");
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
}
