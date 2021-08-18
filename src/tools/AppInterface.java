package tools;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import study.Comment;
import study.Evaluation;
import study.Resource;
import user.User;

/**
 * Servlet implementation class AppInterface
 */
@WebServlet("/*")
public class AppInterface extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private User user;
	private Resource resource;
	private Evaluation evaluation;
	private Comment comment;
	private DB db;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AppInterface() {
        super();
        db = new DB();
        user = new User(db);
        resource = new Resource(db);
        comment = new Comment(db);
        evaluation = new Evaluation(db);
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setHeader("Access-Control-Allow-Origin", "*");   // 改动了*
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=utf-8");
		
		// 获取请求的URI地址信息
        String url = request.getRequestURI();
        // 截取方法名
        //返回"/"之后，"."之前的字符串
        String methodName = StringUtils.substring(url,url.lastIndexOf("/")+1, url.lastIndexOf("."));
        System.out.println(methodName);
        Method method = null;
        
        try {
            // 使用反射机制获取在本类中声明了的方法
            method = this.getClass().getDeclaredMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);
            // 执行方法
            method.invoke(this, request, response);
        } catch (Exception e) {
        	e.printStackTrace();
        }
	}

	//test
	private void hello(HttpServletRequest request, HttpServletResponse response) throws Exception{
		response.getWriter().write(request.getParameter("name"));
		System.out.println(request.getParameter("name"));
//		System.out.println(request.getParameter("img"));
//		long  i = new Date().getTime()/1000;
	}
	
	//register
	private void register(HttpServletRequest request, HttpServletResponse response) throws Exception{
		user.register(request,response);
	}
	
	//check telephone whether is registered
	private void checkTelephone(HttpServletRequest request, HttpServletResponse response) throws Exception{
		user.checkTelephone(request,response);
	}
	
	//check name whether is registered
	private void checkName(HttpServletRequest request, HttpServletResponse response) throws Exception{
		user.checkName(request,response);
	}
	
	//login with password
	private void passwordLogin(HttpServletRequest request, HttpServletResponse response) throws Exception{
		user.passwordLogin(request,response);
	}

	//check the question of password
	private void checkPasswordQuestion(HttpServletRequest request, HttpServletResponse response) throws Exception{
		user.checkPasswordQuestion(request,response);
	}
	
	//set the question of password
	private void setPasswordQuestion(HttpServletRequest request, HttpServletResponse response) throws Exception{
		user.setPasswordQuestion(request,response);
	}
	
	//check the answer of password
	private void answerPasswordQuestion(HttpServletRequest request, HttpServletResponse response) throws Exception{
		user.answerPasswordQuestion(request,response);
	}

	//modify the password
	private void modifyPassword(HttpServletRequest request, HttpServletResponse response) throws Exception{
		user.modifyPassword(request,response);
	}
	
	//modify the password
	private void modifyData(HttpServletRequest request, HttpServletResponse response) throws Exception{
		user.modifyData(request,response);
	}
	
	//write test
	private void write(HttpServletRequest request, HttpServletResponse response) throws Exception{
		resource.write(request,response);
	}
	
	//read test
	private void read(HttpServletRequest request, HttpServletResponse response) throws Exception{
		resource.read(request,response);
	}
	
	//create cognition
	private void createCognition(HttpServletRequest request, HttpServletResponse response) throws Exception{
		resource.createCognition(request,response);
	}
	
	//upload cognition file
	private void uploadCognitionFile(HttpServletRequest request, HttpServletResponse response) throws Exception{
		resource.uploadCognitionFile(request,response);
	}
	
	//delete cognition
	private void deleteCognition(HttpServletRequest request, HttpServletResponse response) throws Exception{
		resource.deleteCognition(request,response);
	}
	
	//modify cognition
	private void modifyCognition(HttpServletRequest request, HttpServletResponse response) throws Exception{
		resource.modifyCognition(request,response);
	}
	
	//display cognition
	private void displayCognition(HttpServletRequest request, HttpServletResponse response) throws Exception{
		resource.displayCognition(request,response);
	}
	
	private void displayVideo(HttpServletRequest request, HttpServletResponse response) throws Exception{
		resource.displayVideo(request,response);
	}
	
	//display list of cognition resource
	private void displayCognitionResource(HttpServletRequest request, HttpServletResponse response) throws Exception{
		resource.displayCognitionResource(request,response);
	}
	
	private void createScene(HttpServletRequest request, HttpServletResponse response) throws Exception{
		resource.createScene(request,response);
	}
	
	private void deleteScene(HttpServletRequest request, HttpServletResponse response) throws Exception{
		resource.deleteScene(request,response);
	}
	
	private void displayScene(HttpServletRequest request, HttpServletResponse response) throws Exception{
		resource.displayScene(request,response);
	}
	
	private void screenCognitionResource(HttpServletRequest request, HttpServletResponse response) throws Exception{
		resource.screenCognitionResource(request,response);
	}
	
	//match user and cognition
	private void matchUserAndCognition(HttpServletRequest request, HttpServletResponse response) throws Exception{
		resource.matchUserAndCognition(request,response);
	}
	
	//display index cognition
	private void indexDisplayCognition(HttpServletRequest request, HttpServletResponse response) throws Exception{
		resource.indexDisplayCognition(request,response);
	}
	
	//share cognition to dynamic module
	private void shareToDynamic(HttpServletRequest request, HttpServletResponse response) throws Exception{
		resource.shareToDynamic(request,response);
	}
	
	//display dynamic cognition
	private void displayDynamic(HttpServletRequest request, HttpServletResponse response) throws Exception{
		resource.displayDynamic(request,response);
	}
	
	// reward dynamic module users
	private void rewardDynamic(HttpServletRequest request, HttpServletResponse response) throws Exception{
		resource.rewardDynamic(request,response);
	}
	
	//create comment
	private void createComment(HttpServletRequest request, HttpServletResponse response) throws Exception{
		comment.createComment(request,response);
	}
	
	//delete comment
	private void deleteComment(HttpServletRequest request, HttpServletResponse response) throws Exception{
		comment.deleteComment(request,response);
	}

	//display comment
	private void displayComment(HttpServletRequest request, HttpServletResponse response) throws Exception{
		comment.displayComment(request,response);
	}
	
	private void createStudyComment(HttpServletRequest request, HttpServletResponse response) throws Exception{
		comment.createStudyComment(request,response);
	}
	
	//delete comment
	private void deleteStudyComment(HttpServletRequest request, HttpServletResponse response) throws Exception{
		comment.deleteStudyComment(request,response);
	}

	//display comment
	private void displayStudyComment(HttpServletRequest request, HttpServletResponse response) throws Exception{
		comment.displayStudyComment(request,response);
	}
	
	//match user and score
	private void matchUserAndScore(HttpServletRequest request, HttpServletResponse response) throws Exception{
		evaluation.matchUserAndScore(request,response);
	}
	
	//create score
	private void createScore(HttpServletRequest request, HttpServletResponse response) throws Exception{
		evaluation.createScore(request,response);
	}
	
	//match user and cognition good
	private void matchUserAndCognitionGood(HttpServletRequest request, HttpServletResponse response) throws Exception{
		evaluation.matchUserAndCognitionGood(request,response);
	}
	
	private void createCognitionGood(HttpServletRequest request, HttpServletResponse response) throws Exception{
		evaluation.createCognitionGood(request,response);
	}
	
	private void deleteCognitionGood(HttpServletRequest request, HttpServletResponse response) throws Exception{
		evaluation.deleteCognitionGood(request,response);
	}
	
	private void matchUserAndResourceGood(HttpServletRequest request, HttpServletResponse response) throws Exception{
		evaluation.matchUserAndResourceGood(request,response);
	}
	
	private void createResourceGood(HttpServletRequest request, HttpServletResponse response) throws Exception{
		evaluation.createResourceGood(request,response);
	}
	
	private void deleteResourceGood(HttpServletRequest request, HttpServletResponse response) throws Exception{
		evaluation.deleteResourceGood(request,response);
	}
	
	private void createResourceDislike(HttpServletRequest request, HttpServletResponse response) throws Exception{
		evaluation.createResourceDislike(request,response);
	}
	
	private void deleteResourceDislike(HttpServletRequest request, HttpServletResponse response) throws Exception{
		evaluation.deleteResourceDislike(request,response);
	}
	
	//share cognition
	private void shareCognition(HttpServletRequest request, HttpServletResponse response) throws Exception{
		evaluation.share(request,response);
	}
	
	//create article
	private void createArticle(HttpServletRequest request, HttpServletResponse response) throws Exception{
		resource.createArticle(request,response);
	}
	
	//delete article
	private void deleteArticle(HttpServletRequest request, HttpServletResponse response) throws Exception{

		resource.deleteArticle(request,response);
	}
	
	private void displayArticle(HttpServletRequest request, HttpServletResponse response) throws Exception{

		resource.displayArticle(request,response);
	}
	
	private void displayConcernArticle(HttpServletRequest request, HttpServletResponse response) throws Exception{

		resource.displayConcernArticle(request,response);
	}
	
	private void displaySql(HttpServletRequest request, HttpServletResponse response) throws Exception{

		resource.displaySql(request,response);
	}
	
	private void createGameIntegral(HttpServletRequest request, HttpServletResponse response) throws Exception{

		evaluation.createGameIntegral(request,response);
	}
	
	private void createAssessScore(HttpServletRequest request, HttpServletResponse response) throws Exception{

		evaluation.createAssessScore(request,response);
	}
	
	//follow user
	private void follow(HttpServletRequest request, HttpServletResponse response) throws Exception{
		user.follow(request,response);
	}
	
	private void displayFollow(HttpServletRequest request, HttpServletResponse response) throws Exception{
		user.displayFollow(request,response);
	}
	
	private void displayFollowMe(HttpServletRequest request, HttpServletResponse response) throws Exception{
		user.displayFollowMe(request,response);
	}
}
