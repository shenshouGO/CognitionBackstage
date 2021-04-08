package study;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.StringUtils;

import net.sf.json.JSONObject;
import tools.DB;


public class Resource {
	DB db = new DB();
	
	public void createCognition(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String u_id = request.getParameter("ID");
		System.out.println("u_id = " + u_id);
		String u_name = request.getParameter("u_name");
		String u_img = request.getParameter("u_img");
		String c_r_id = request.getParameter("c_r_id");
		String c_r_file = request.getParameter("c_r_file");
		String cognition = request.getParameter("cognition");
		System.out.println("cognition = " + cognition);
		String file = createFile(u_id,db.getStamp(),cognition);
		long  time = Long.parseLong(StringUtils.substring(file,file.lastIndexOf("_")+1, file.lastIndexOf(".")));
		String sql;
		int num;
		JSONObject obj;
		
		try
		{
			sql = "insert into cognition_result(u_id,u_name,u_img,c_r_id,c_r_file,file,time) values(?,?,?,?,?,?,?)";
			num = db.update(sql, u_id,u_name,u_img,c_r_id,c_r_file,file,time);
			if(num == 1)
			{
				sql = "insert into integral(score,u_id,source,time) values(2,?,'发布认知重评',?)";
				num = db.update(sql,u_id , time);
				if(num == 1)
				{
					sql = "update user set integral_sum = integral_sum + 2 , integral_forum = integral_forum + 2 where id = ?";
					num = db.update(sql,u_id);
					if(num == 1)
					{
						sql = "update cognition_resource set comment = comment + 1, score = score +2  where id = ?";
						num = db.update(sql,c_r_id);
						if(num == 1)
						{
							response.getWriter().write("Create successfully");
						}
						else
							response.getWriter().write("Unsuccessfully");
					}
					else
						response.getWriter().write("Unsuccessfully");
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
	
	public void createArticle(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String u_id = request.getParameter("ID");
		System.out.println("u_id = " + u_id);
		String article = request.getParameter("article");
		System.out.println("cognition = " + article);
		String file = createFile(u_id,db.getStamp(),article);
		long  time = Long.parseLong(StringUtils.substring(file,file.lastIndexOf("_")+1, file.lastIndexOf(".")));
		String sql;
		int num;
		JSONObject obj;
		
		try
		{
			sql = "insert into article(u_id,file,time) values(?,?,?)";
			num = db.update(sql, u_id,file,time);
			if(num == 1)
			{
				response.getWriter().write("Create successfully");
			}
			else
				response.getWriter().write("Unsuccessfully");
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void deleteArticle(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String a_id = request.getParameter("ID");
		System.out.println("a_id = " + a_id);
		String sql;
		int num;
		JSONObject obj;
		String file;
		
		try
		{
			sql = "select file from article where id = ?";
			obj = db.query(sql, a_id);
			file = obj.getJSONObject("0").get("file").toString();
			File f = new File("file/" + file);
			if(f.exists())
				f.delete();
			sql = "delete from article where id = ?";
			num = db.update(sql, a_id);
			if(num == 1)
			{
				response.getWriter().write("Delete successfully");
			}
			else
				response.getWriter().write("Delete unsuccessfully");
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void displayArticle(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String u_id = request.getParameter("ID");
		System.out.println("u_id = " + u_id);
		String sql;
		int num;
		JSONObject obj;
		String file;
		
		try
		{
			sql = "select * from cognition_resource where u_id = ? and type = '动态' ";
			obj = db.query(sql, u_id);
			response.getWriter().write(obj.toString());
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void displayConcernArticle(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String u_id = request.getParameter("ID");
		System.out.println("u_id = " + u_id);
		String sql;
		int num;
		JSONObject obj;
		String where = "";
		
		try
		{
			sql = "select f_u_id from concern where u_id = ?";
			obj = db.query(sql, u_id);
			for(int i=0; i<obj.length();i++)
			{
				if(i == 0)
				{
					where = "where";
					where += " u_id = " + obj.getJSONObject(Integer.toString(i)).get("f_u_id").toString();
				}
				else
					where += " or u_id = " + obj.getJSONObject(Integer.toString(i)).get("f_u_id").toString();
			}
			if(obj.length() == 0)
				response.getWriter().write("Null");
			else
			{
				sql = "select * from article " + where + " order by time desc";
				System.out.println(sql);
				obj = db.query(sql);
				response.getWriter().write(obj.toString());
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void displayVideo(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String type =  request.getParameter("type");
		String sql;
		int num;
		JSONObject obj;
		
		try
		{
			sql = "select * from cognition_resource where type = ? order by time desc";
			System.out.println(sql);
			obj = db.query(sql,type);
			response.getWriter().write(obj.toString());
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void createScene(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String u_id = request.getParameter("u_id");
		String u_name = request.getParameter("u_name");
		String u_img = request.getParameter("u_img");
		String scene = request.getParameter("scene");
		System.out.println(scene);
//		System.out.println(java.net.URLDecoder.decode(scene,"UTF-8"));
		String theme = request.getParameter("theme");
		String type = request.getParameter("type");
		System.out.println(request.getParameter("time"));
		long time = Long.parseLong(request.getParameter("time"));
		
		scene += "|||"+uploadFile(request,response);
		String file = createFile(u_id,time,scene);
		String sql;
		int num;
		JSONObject obj;
		
		try
		{
			if(file != null)
			{
				sql = "insert into cognition_resource(u_id,u_name,u_img,unit,theme,type,file,time) values(?,?,?,?,?,?,?,?)";
				num = db.update(sql, u_id,u_name,u_img,1,theme,type,file,time);
				if(num == 1)
				{
					sql = "insert into integral(score,u_id,source,time) values(2,?,'发表负性情景',?)";
					num = db.update(sql,u_id , time);
					if(num == 1)
					{
						sql = "update user set integral_sum = integral_sum + 2 , integral_forum = integral_forum + 2 where id = ?";
						num = db.update(sql,u_id);
						if(num == 1)
						{
							response.getWriter().write("Create successfully");
						}
						else
							response.getWriter().write("update user unsuccessfully");
					}
					else
						response.getWriter().write("insert integral unsuccessfully");
				}	
				else
					response.getWriter().write("Insert resource unsuccessfully");
			}
			else
				response.getWriter().write("CreateFile unsuccessfully");
				
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void displayScene(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String sql;
		int num;
		JSONObject obj;
		
		try
		{
			sql = "select * from cognition_resource where unit = 1 order by time desc";
			obj = db.query(sql);
			response.getWriter().write(obj.toString());
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void uploadCognitionFile(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String u_id = request.getParameter("u_id");
		System.out.println("u_id = " + u_id);
		String theme = request.getParameter("theme");
		String type = request.getParameter("type");
		String file = uploadFile(request,response);
		long time = db.getStamp();
		String sql;
		int num;
		JSONObject obj;
		
		try
		{
			if(file != null)
			{
				sql = "insert into cognition_resource(u_id,theme,type,file,time) values(?,?,?,?,?)";
				num = db.update(sql, u_id,theme,type,file,time);
				if(num == 1)
				{
					sql = "insert into integral(score,u_id,source,time) values(5,?,'发布材料',?)";
					num = db.update(sql,u_id , time);
					if(num == 1)
					{
						sql = "update user set integral_sum = integral_sum + 5 , integral_forum = integral_forum + 5 where id = ?";
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
					response.getWriter().write("Upload unsuccessfully");
			}
			else
				response.getWriter().write("Upload unsuccessfully");
				
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	private String uploadFile(HttpServletRequest request,HttpServletResponse response) throws UnsupportedEncodingException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setCharacterEncoding("utf-8");
		
		String u_id = request.getParameter("u_id");
		long time = Long.parseLong(request.getParameter("time"));
		
		String fileName = null;
		String files = "";
		String route = "f:/resource/"+u_id+"_"+time+"/";
//		String route = "/usr/local/file/";
		File file = new File(route);
        if (!file.exists()) {
            file.mkdirs();
        }
		
		//创建一个解析器工厂
        DiskFileItemFactory factory=new DiskFileItemFactory();
        //设置工厂的内存缓冲区大小，默认是10K
        factory.setSizeThreshold(1024*1024*1024);
        //设置工厂的临时文件目录：当上传文件的大小大于缓冲区大小时，将使用临时文件目录缓存上传的文件
        factory.setRepository(new File(route));
       //文件上传解析器
        ServletFileUpload upload=new ServletFileUpload(factory);
       //设置所有上传数据的最大值，单位字节long  1M
        upload.setSizeMax(9*1024*1024*1024);
        //设置单个文件上传的最大值
        upload.setFileSizeMax(1024*1024*1024);
        //设置编码格式
        upload.setHeaderEncoding("UTF-8");
        
        try {
            //解析请求，将表单中每个输入项封装成一个FileItem对象
            List<FileItem> itemList=upload.parseRequest(request);
            for(FileItem item:itemList){
                //判断输入的类型是 普通输入项 还是文件
                if(item.isFormField()){
                    //普通输入项 ,得到input中的name属性的值
//                    String name=item.getFieldName();
//                    System.out.println(name);
                    //得到输入项中的值
                    String value=item.getString("UTF-8");
                    System.out.println(value);
//                    fileName= createFile(u_id,value);
                }else{
                    //上传的是文件，获得文件上传字段中的文件名
                    //注意IE或FireFox中获取的文件名是不一样的，IE中是绝对路径，FireFox中只是文件名。
                    fileName=item.getName();
                    System.out.println(fileName);
                    //返回表单标签name属性的值
//                    String namede=item.getFieldName();
//                    System.out.println(namede);

                   //方法一：保存上传文件到指定的文件路径
//                    InputStream is=item.getInputStream();
//                    FileOutputStream fos=new FileOutputStream(route+fileName);
//                    byte[] buff=new byte[1024];
//                    int len=0;
//                    while((len=is.read(buff))>0){
//                        fos.write(buff);
//                    }
//                    is.close();
//                    fos.close();
                    
                    String filePath = route+fileName;
                    File storeFile = new File(filePath);
                    System.out.println(filePath);
                    item.write(storeFile);
                    files += fileName+"|";
                }
            }
            return files;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return files;
	}
	
	private String createFile(String u_id,long time,String cognition) {
		try {
			System.out.println(cognition);
			byte[] bytes = cognition.getBytes("UTF-8");
			String fileName = u_id + "_" + time + ".txt";
			OutputStream os = new FileOutputStream("f:/resource/" + fileName);
//			OutputStream os = new FileOutputStream("/usr/local/file/" + fileName);
			os.write(bytes);
			os.flush();
			if(os!=null)
				os.close();
			return fileName;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public void deleteCognition(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String c_id = request.getParameter("ID");
		System.out.println("c_id = " + c_id);
		String sql;
		int num;
		JSONObject obj;
		String file;
		
		try
		{
			sql = "select file from cognition_result where id = ?";
			obj = db.query(sql, c_id);
			file = obj.getJSONObject("0").get("file").toString();
			File f = new File("file/" + file);
			if(f.exists())
				f.delete();
			sql = "delete from cognition_result where id = ?";
			num = db.update(sql, c_id);
			if(num == 1)
			{
				response.getWriter().write("Delete successfully");
			}
			else
				response.getWriter().write("Delete unsuccessfully");
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void modifyCognition(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String c_id = request.getParameter("ID");
		String cognition = request.getParameter("cognition");
		String sql;
		int num;
		JSONObject obj;
		String file;
		
		try
		{
			sql = "select u_id,file from cognition_result where id = ?";
			obj = db.query(sql, c_id);
			file = obj.getJSONObject("0").get("file").toString();
			System.out.println(file);
			String u_id = obj.getJSONObject("0").get("u_id").toString();
			System.out.println(u_id);
			file = modifyFile(file,u_id,cognition);
			long time = Long.parseLong(StringUtils.substring(file,file.lastIndexOf("_")+1, file.lastIndexOf(".")));
			sql = "update cognition_result set file = ?, time = ? where id = ?";
			num = db.update(sql, file,time,c_id);
			if(num == 1)
			{
				response.getWriter().write("Modify cognition successfully");
			}
			else
				response.getWriter().write("Modify cognition unsuccessfully");
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	private String modifyFile(String file,String u_id,String cognition) {
		File f = new File("cognition/" + file);
		if(f.exists())
			f.delete();
		return createFile(u_id,db.getStamp(),cognition);
	}
	
	public void indexDisplayCognition(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String sql;
		JSONObject obj;
		
		try
		{
			sql = "select * from cognition_result order by time desc";
			obj = db.query(sql);
			response.getWriter().write(obj.toString());
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void displayCognition(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String c_r_id = request.getParameter("ID");
		String sql;
		JSONObject obj;
		
		try
		{
			sql = "select * from cognition_result where c_r_id = ? order by time desc";
			obj = db.query(sql,c_r_id);
			response.getWriter().write(obj.toString());
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void displayCognitionResource(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String sql;
		JSONObject obj;
		
		try
		{
			sql = "select * from cognition_resource order by score desc";
			obj = db.query(sql);
			response.getWriter().write(obj.toString());
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void screenCognitionResource(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String theme = request.getParameter("theme");
		String type = request.getParameter("type");
		String order = request.getParameter("order");
		String where = "";
		String sql;
		JSONObject obj;
		
		if(theme.equals(" "))
		{
			if(type.equals(" "))
				where = "";
			else
			{
				type = "'" + type + "'";
				where = " where type = "+type;
			}
		}
		else
		{
			theme = "'" + theme + "'";
			where = " where theme = "+theme;
			if(!type.equals(" "))
			{
				type = "'" + type + "'";
				where += "and type = "+type;
			}
		}
		
		if(order.equals("最新"))
			order = "time desc";
		else if(order.equals("最热"))
			order = "number desc";
		else
			order = "time desc,number desc";
		
		try
		{
			sql = "select * from cognition_resource" + where +" order by " + order;
			System.out.println(sql);
			obj = db.query(sql);
			response.getWriter().write(obj.toString());
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void shareToDynamic(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String c_r_id = request.getParameter("ID");
		String sql;
		int num;
		
		try
		{
			sql = "update cognition_result set is_dynamic = 1 where id = ?";
			num = db.update(sql,c_r_id);
			if(num == 1)
				response.getWriter().write("Share successfully");
			else
				response.getWriter().write("Share unsuccessfully");
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void displayDynamic(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String sql;
		JSONObject obj;
		
		try
		{
			sql = "select * from cognition_result order by score desc limit 20";
			obj = db.query(sql);
			response.getWriter().write(obj.toString());
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void displaySql(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String sql = request.getParameter("sql");
		JSONObject obj;
		System.out.println(sql);
		
		try
		{
			if(sql == null) {
				response.getWriter().write("");
			}else {
				obj = db.query(sql);
				response.getWriter().write(obj.toString());
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void rewardDynamic(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String sql;
		JSONObject obj;
		int i,num = 0;
		String u_id;
		
		try
		{
			sql = "select * from cognition_result order by score desc limit 20";
			obj = db.query(sql);
//			response.getWriter().write(obj.toString());
			for(i=0;i<20;i++)
			{
				u_id = obj.getJSONObject(Integer.toString(i)).get("u_id").toString();
				if(i == 0){
					num += reward(u_id,20);
				}else if(i == 1) {
					num += reward(u_id,15);
				}else if(i == 2) {
					num += reward(u_id,10);
				}else if(i > 2 && i<10) {
					num += reward(u_id,5);
				}else 
					num += reward(u_id,3);
			}
			if(num == 20)
				response.getWriter().write("Reward successfully");
			else
				response.getWriter().write("Reward unsuccessfully");
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	private int reward(String u_id, int integral) {
		String sql;
		int num;
		long  time = db.getStamp();
		
		try
		{
			sql = "update user set integral_sum = integral_sum + ? , integral_forum = integral_forum + ? where id = ?";
			num = db.update(sql, integral, integral, u_id);
			if(num == 1)
			{
				sql = "insert into integral(score,u_id,source,time) values(?,?,'动态模块奖励',?)";
				num = db.update(sql, integral, u_id, time);
				if(num == 1)
					return 1;
				else
					return 0;
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public void matchUserAndCognition(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String u_id = request.getParameter("u_id");
		String c_r_id = request.getParameter("c_r_id");
		String sql;
		JSONObject obj;
		
		try
		{
			sql = "select * from cognition_result where u_id=? and c_r_id = ?";
			obj = db.query(sql,u_id,c_r_id);
			if(obj.isEmpty())
				response.getWriter().write("Unrecognized");
			else
				response.getWriter().write("Recognized");
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void write(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String file = request.getParameter("file");
		String content = request.getParameter("content");
		byte[] bytes = content.getBytes("UTF-8");
		OutputStream os;
		
		try
		{
			os = new FileOutputStream("f:/resource/" + file);
			//E:\Software\jee-photon\eclipse\file
			os.write(bytes);
			os.flush();
			if(os!=null)
				os.close();
			response.getWriter().write("write successfully");
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void read(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String file = request.getParameter("file");
		byte[] bytes;
		InputStream is;
		String s;
		
		try
		{
			is = new FileInputStream("f:/resource/" + file);
			InputStreamReader reader = new InputStreamReader(is,"UTF-8");
			BufferedReader br = new BufferedReader(reader);
			String line;
			s="";
            while ((line = br.readLine()) != null) {
                s+=line+"\n";
            }
//			bytes = new byte[is.available()];
//			is.read(bytes);
			
			br.close();
            reader.close();
			is.close();
//			System.out.println(new String(bytes));
			response.getWriter().write(s);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

}
