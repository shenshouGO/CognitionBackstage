package study;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import net.sf.json.JSONObject;
import tools.DB;

public class Comment {
DB db = new DB();
	
	public void createComment(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String c_r_id = request.getParameter("c_r_id");
		String u_id = request.getParameter("u_id");
		System.out.println("u_id = " + u_id);
		String u_name = request.getParameter("u_name");
		String u_img = request.getParameter("u_img");
		String c_c_id = request.getParameter("c_c_id");
		String r_u_id = request.getParameter("r_u_id");
		System.out.println("r_u_id = " + r_u_id);
		String r_u_name = request.getParameter("r_u_name");
		String r_u_img = request.getParameter("r_u_img");
		String comment = request.getParameter("comment");
		System.out.println("comment = " + comment);
		long  time = db.getStamp();
		String sql;
		int num;
		JSONObject obj;
		
		try
		{
			sql = "insert into cognition_comment(c_r_id,u_id,u_name,u_img,c_c_id,r_u_id,r_u_name,r_u_img,comment,time) values(?,?,?,?,?,?,?,?,?,?)";
			num = db.update(sql, c_r_id,u_id,u_name,u_img,c_c_id,r_u_id,r_u_name,r_u_img,comment,time);
			if(num == 1)
			{
				sql = "update cognition_result set comment = comment + 1, score = score + 2 where id = ?";
				num = db.update(sql, c_r_id);
				if(num == 1)
				{
					sql = "insert into integral(score,u_id,source,time) values(1,?,'发表评论或回复',?)";
					num = db.update(sql,u_id , time);
					if(num == 1)
					{
						sql = "update user set integral_sum = integral_sum + 1 , integral_forum = integral_forum + 1 where id = ?";
						num = db.update(sql,u_id);
						if(num == 1)
						{
							response.getWriter().write("Create successfully");
						}
					}
					else
						response.getWriter().write("Upload unsuccessfully");
				}
				else
					response.getWriter().write("unsuccessfully");
			}
			else
				response.getWriter().write("unsuccessfully");
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void deleteComment(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String c_id = request.getParameter("ID");
		System.out.println("c_id = " + c_id);
		String sql;
		int num;
		JSONObject obj;
		
		try
		{
			sql = "select c_r_id from cognition_comment where id = ?";
			obj = db.query(sql, c_id);
			if(obj.isEmpty())
				response.getWriter().write("Delete unsuccessfully");
			else
			{
				int c_r_id = obj.getJSONObject("0").getInt("c_r_id");
				sql = "delete from cognition_comment where id = ?";
				num = db.update(sql, c_id);
				if(num == 1)
				{
					sql = "update cognition_result set comment = comment - 1 where id = ?";
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
	
	public void displayComment(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String cognition_id = request.getParameter("ID");
		String sql;
		JSONObject obj;
		String c_c_id;
		
		try
		{
			sql = "select id,u_id,u_name,u_img,comment,time,good from cognition_comment where c_r_id = ? and c_c_id = 0 order by time";
			obj = db.query(sql,cognition_id);
			for(int i=0; i<obj.length();i++)
			{
				c_c_id = obj.getJSONObject(Integer.toString(i)).get("id").toString();
				obj.getJSONObject(Integer.toString(i)).put("replys", addReplyToComment(c_c_id));
			}
			response.getWriter().write(obj.toString());
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	private JSONObject addReplyToComment(String c_c_id) {
		JSONObject obj;
		String sql;
		try
		{
			sql = "select * from cognition_comment where c_c_id = ? order by time";
			obj = db.query(sql,c_c_id);
			return obj;
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void createStudyComment(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String s_r_id = request.getParameter("s_r_id");
		String u_id = request.getParameter("u_id");
		System.out.println("u_id = " + u_id);
		String u_name = request.getParameter("u_name");
		String u_img = request.getParameter("u_img");
		String s_c_id = request.getParameter("s_c_id");
		String r_u_id = request.getParameter("r_u_id");
		System.out.println("r_u_id = " + r_u_id);
		String r_u_name = request.getParameter("r_u_name");
		String r_u_img = request.getParameter("r_u_img");
		String comment = request.getParameter("comment");
		System.out.println("comment = " + comment);
		long  time = db.getStamp();
		String sql;
		int num;
		JSONObject obj;
		
		try
		{
			sql = "insert into study_comment(s_r_id,u_id,u_name,u_img,s_c_id,r_u_id,r_u_name,r_u_img,comment,time) values(?,?,?,?,?,?,?,?,?,?)";
			num = db.update(sql, s_r_id,u_id,u_name,u_img,s_c_id,r_u_id,r_u_name,r_u_img,comment,time);
			if(num == 1)
			{
				sql = "update cognition_resource set comment = comment + 1,score = score + 2 where id = ?";
				num = db.update(sql, s_r_id);
				if(num == 1)
				{
					sql = "insert into integral(score,u_id,source,time) values(1,?,'发表评论或回复',?)";
					num = db.update(sql,u_id , time);
					if(num == 1)
					{
						sql = "update user set integral_sum = integral_sum + 1 , integral_forum = integral_forum + 1 where id = ?";
						num = db.update(sql,u_id);
						if(num == 1)
						{
							response.getWriter().write("Create successfully");
						}else
							response.getWriter().write("update user unsuccessfully");
					}
					else
						response.getWriter().write("insert into integral unsuccessfully");
				}
				else
					response.getWriter().write("Update cognition_resource unsuccessfully");
			}
			else
				response.getWriter().write("Insert comment unsuccessfully");
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void deleteStudyComment(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String c_id = request.getParameter("ID");
		System.out.println("c_id = " + c_id);
		String sql;
		int num;
		JSONObject obj;
		
		try
		{
			sql = "select s_r_id from study_comment where id = ?";
			obj = db.query(sql, c_id);
			if(obj.isEmpty())
				response.getWriter().write("comment isn't existed");
			else
			{
				int s_r_id = obj.getJSONObject("0").getInt("s_r_id");
				sql = "delete from study_comment where id = ?";
				num = db.update(sql, c_id);
				if(num == 1)
				{
					sql = "update cognition_resource set comment = comment - 1 where id = ?";
					num = db.update(sql, s_r_id);
					if(num == 1)
						response.getWriter().write("Delete successfully");
					else
						response.getWriter().write("update cognition_resource unsuccessfully");
				}
				else
					response.getWriter().write("Delete unsuccessfully");
			}
			
				
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void displayStudyComment(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String resource_id = request.getParameter("ID");
		String sql;
		JSONObject obj;
		String s_c_id;
		
		try
		{
			sql = "select id,u_id,u_name,u_img,comment,time,good from study_comment where s_r_id = ? and s_c_id = 0 order by time";
			obj = db.query(sql,resource_id);
			for(int i=0; i<obj.length();i++)
			{
				s_c_id = obj.getJSONObject(Integer.toString(i)).get("id").toString();
				obj.getJSONObject(Integer.toString(i)).put("replys", addStudyReplyToComment(s_c_id));
			}
			response.getWriter().write(obj.toString());
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	private JSONObject addStudyReplyToComment(String s_c_id) {
		JSONObject obj;
		String sql;
		try
		{
			sql = "select * from study_comment where s_c_id = ? order by time";
			obj = db.query(sql,s_c_id);
			return obj;
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
