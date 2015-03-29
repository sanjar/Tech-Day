package com.sanjar.gcm;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class RegisterUserServlet
 */
@WebServlet("/saveReg")
public class RegisterUserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegisterUserServlet() {
        super();
        // TODO Auto-generated constructor stub
    }


    
    @Override
    public void init() throws ServletException {
    	super.init();
	      
    }
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		//initializeDriver();
		PrintWriter printWriter = response.getWriter();
		printWriter.println("<h1>No Get Request Defined for this. :)</h1>");
		//saveReg("APA91bFI_hc3Crhm-wSZdkPugndddgNpl4l6L2nTQijr5-PZ5vUtW7EKEwrq9kmQD7XmaRPRXPIBUSVNHYtWor86_MpItwBCiS1JyNhJS2ADYUbUFOPYddm0WldlNjtqc6KQ9X_rV-Im9HBfi3NgE0bluK-lyAAMr2P1A", "aasssa@sss.com", "PDM", response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		saveReg(request.getParameter("regId"),request.getParameter("Email_id"),request.getParameter("groups"),response);
	
	}

	private void saveReg(String regId, String email_id, String groups, HttpServletResponse response) {

		//Connection conn = null;
		Statement stmt = null;
		
		try{
		      //STEP 4: Execute a query
		      System.out.println("Creating statement...");
		      if(DatabaseConnectionManager.getInstance().getConn()!=null){
			      stmt = DatabaseConnectionManager.getInstance().getConn().createStatement();
			      String sql;
			      sql = "SELECT * FROM reviewme_users where reviewme_users.user_email="+"'"+email_id+"'";
			      ResultSet rs = stmt.executeQuery(sql);
			      if(rs.next()){
			    	  System.out.println("Email: "+email_id+" already registered");
			    	  response.setHeader("userAlreadyReg", "true");
			      }
			      else{
			    	  DateFormat dateFormat = new SimpleDateFormat();
			    	  String date = new Date().toString();
			    	String insertQuery = "INSERT INTO myowndatabase.reviewme_users(user_id,user_email,user_reg_id,user_group,user_reg_date) VALUES (NULL, "+"'"+email_id+"'"+", "+"'"+regId+"'"+", "+"'"+groups+"'"+", "+"'"+date+"'"+")" ;
			    	System.out.println(insertQuery);
			    	stmt.executeUpdate(insertQuery);
			      }
		      }else{
		    	  response.setStatus(500);
		      }
		}catch(Exception ex){
			ex.printStackTrace();
		}
		   
	}

}
