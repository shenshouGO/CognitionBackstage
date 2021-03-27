package user;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import tools.DB;

public class User {
	DB db = new DB();
	
	public void register(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String name = request.getParameter("name");
		System.out.println(name);
		String password = request.getParameter("password");
		String birthday = request.getParameter("birthday");
		int age;
		if(birthday == "")
			age = 0;
		else
			age = Integer.parseInt(request.getParameter("age"));
		String sexS = request.getParameter("sex");
		int sex;
		if(sexS == "")
			sex = 2;
		else
			sex = Integer.parseInt(sexS);
		String job = request.getParameter("job");
		String telephone = request.getParameter("telephone");
		String email = request.getParameter("email");
		long  register_time = db.getStamp();
		long  login_last_time = db.getStamp();
		String sql;
		int num;
		JSONObject obj;
		
		try
		{
			sql = "select name from user where name = ?";
			obj = db.query(sql, name);
			if(obj.isEmpty())
			{
				sql = "select telephone from user where telephone = ?";
				obj = db.query(sql, telephone);
				if(obj.isEmpty()) 
				{
					sql = "insert into user(name,password,birthday,age,sex,job,telephone,email,register_time,login_last_time) values(?,?,?,?,?,?,?,?,?,?)";
					num = db.update(sql, name,password,birthday,age,sex,job,telephone,email,register_time,login_last_time);
					if(num == 1)
					{
						sql = "select id,name,img_name from user where name = ?";
						obj = db.query(sql, name);
						response.getWriter().write(obj.getJSONObject("0").toString());
					}
					else
						response.getWriter().write("false");
				}
				else
					response.getWriter().write("Telephone Registered");
			}
			else
				response.getWriter().write("Name Registered");
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void checkTelephone(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String telephone = request.getParameter("telephone");
		JSONObject obj;
		
		try
		{
			String sql = "select telephone from user where telephone = ?";
			obj = db.query(sql, telephone);
			if(obj.isEmpty())
				response.getWriter().write("Unregistered");
			else
				response.getWriter().write("Registered");
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void checkName(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String name = request.getParameter("name");
		JSONObject obj;
		
		try
		{
			String sql = "select name from user where name = ?";
			obj = db.query(sql, name);
			if(obj.isEmpty())
				response.getWriter().write("Unregistered");
			else
				response.getWriter().write("Registered");
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void passwordLogin(HttpServletRequest request,HttpServletResponse response) throws IOException{

		String telephone = request.getParameter("telephone");
		String password = request.getParameter("password");
		String realPassword;
		JSONObject obj;
		
		try
		{
			String sql = "select id,name,img_name,password,telephone from user where telephone = ?";
			 obj = db.query(sql, telephone);
			if(obj.isEmpty())
				response.getWriter().write("Unregistered");
			else
			{
				realPassword = obj.getJSONObject("0").get("password").toString();
				System.out.println("password="+password);
				System.out.println("passwordRes="+realPassword);
				if(password.equals(realPassword))
					response.getWriter().write(obj.getJSONObject("0").toString());
				else
					response.getWriter().write("Password error");
			}
				
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void checkPasswordQuestion(HttpServletRequest request,HttpServletResponse response) throws IOException{
		int u_id = Integer.parseInt(request.getParameter("u_id"));
		String sql;
		JSONObject obj;
		
		try
		{
			sql = "select u_id from password_question where u_id = ?";
			obj = db.query(sql, u_id);
			if(obj.isEmpty())
				response.getWriter().write("Unsetted");
			else
				response.getWriter().write("Setted");
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}

	public void setPasswordQuestion(HttpServletRequest request,HttpServletResponse response) throws IOException{
		int u_id = Integer.parseInt(request.getParameter("u_id"));
		String question = request.getParameter("question");
		String answer = request.getParameter("answer");
		String sql;
		int num;
		JSONObject obj;
		
		try
		{
			sql = "select u_id from password_question where u_id = ?";
			obj = db.query(sql, u_id);
			if(obj.isEmpty())
			{
				sql = "insert into password_question(u_id,question,answer) values(?,?,?)";
				num = db.update(sql, u_id,question,answer);
			}
			else
			{
				sql = "update password_question set question = ? ,answer = ? where u_id = ?";
				num = db.update(sql, question, answer, u_id);
				
			}
			if(num == 1)
				response.getWriter().write("Set successfully");
			else
				response.getWriter().write("Set unsuccessfully");
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void answerPasswordQuestion(HttpServletRequest request,HttpServletResponse response) throws IOException{
		int u_id = Integer.parseInt(request.getParameter("u_id"));
		String answer = request.getParameter("answer");
		String realAnswer;
		String sql;
		int num;
		JSONObject obj;
		
		try
		{
			sql = "select answer from password_question where u_id = ?";
			obj = db.query(sql, u_id);
			realAnswer = obj.getJSONObject("0").get("answer").toString();
			if(answer.equals(realAnswer))
				response.getWriter().write("Answer correctly");
			else
				response.getWriter().write("Answer error");
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}

	public void modifyPassword(HttpServletRequest request,HttpServletResponse response) throws IOException{
		int u_id = Integer.parseInt(request.getParameter("u_id"));
		String password = request.getParameter("password");
		String sql;
		int num;
		
		try
		{
			sql = "update user set password = ? where id = ?";
			num = db.update(sql, password, u_id);
			if(num == 1)
			{
				response.getWriter().write("Modify password successfully");
			}
			else
				response.getWriter().write("Modify password unsuccessfully");
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void modifyData(HttpServletRequest request,HttpServletResponse response) throws IOException{
		int u_id = Integer.parseInt(request.getParameter("u_id"));
		String name = request.getParameter("name");
		String birthday = request.getParameter("birthday");
		int age;
		if(birthday == "")
			age = 0;
		else
			age = Integer.parseInt(request.getParameter("age"));
		String sexS = request.getParameter("sex");
		int sex;
		if(sexS == "")
			sex = 2;
		else
			sex = Integer.parseInt(sexS);
		String job = request.getParameter("job");
		String email = request.getParameter("email");
		String sql;
		int num;
		
		try
		{
			sql = "update user set name = ?, birthday = ?,age = ?,sex = ?,job = ?,email = ? where id = ?";
			num = db.update(sql, name, birthday, age, sex, job, email, u_id);
			if(num == 1)
			{
				response.getWriter().write("Modify data successfully");
			}
			else
				response.getWriter().write("Modify data unsuccessfully");
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void follow(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String u_id = request.getParameter("u_id");
		String u_img = request.getParameter("u_img");
		String u_name = request.getParameter("u_name");
		String f_u_id = request.getParameter("f_u_id");
		String f_u_img = request.getParameter("f_u_img");
		String f_u_name = request.getParameter("f_u_name");
		String sql;
		int num;
		JSONObject obj;
		
		try
		{
			sql = "insert into concern(u_id,u_img,u_name, f_u_id,f_u_img,f_u_name) values(?,?,?,?,?,?)";
			num = db.update(sql,u_id,u_img,u_name, f_u_id,f_u_img,f_u_name);
			if(num == 1)
			{
				sql = "select * from concern where u_id = ? and f_u_id = ?";
				obj = db.query(sql, u_id, f_u_id);
				response.getWriter().write(obj.getJSONObject("0").toString());
			}
			else
				response.getWriter().write("Follow unsuccessfully");
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void displayFollow(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String u_id = request.getParameter("u_id");
		String sql;
		int num;
		JSONObject obj;
		
		try
		{
			sql = "select f_u_id,f_u_img,f_u_name from concern where u_id = ? ";
			obj = db.query(sql, u_id);
			response.getWriter().write(obj.toString());
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void displayFollowMe(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String u_id = request.getParameter("u_id");
		String sql;
		int num;
		JSONObject obj;
		
		try
		{
			sql = "select u_id,u_img,u_name from concern where f_u_id = ? ";
			obj = db.query(sql, u_id);
			response.getWriter().write(obj.toString());
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
}
