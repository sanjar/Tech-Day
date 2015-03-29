package com.sanjar.gcm;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Sender;

/**
 * Servlet implementation class GCMBroadcast
 */
@WebServlet("/GCMBroadcast")
public class GCMBroadcast extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	// The SENDER_ID here is the "Browser Key" that was generated when I
	// created the API keys for my Google APIs project.
	private static final String SENDER_ID = "AIzaSyBkVyQCP8kKNRxUkJJjvfgVt0AAuWPHp8o";
	
	// This is a *cheat*  It is a hard-coded registration ID from an Android device
	// that registered itself with GCM using the same project id shown above.
	private static final String ANDROID_DEVICE = "APA91bFI_hc3Crhm-wSZdkPugngNpl4l6L2nTQijr5-PZ5vUtW7EKEwrq9kmQD7XmaRPRXPIBUSVNHYtWor86_MpItwBCiS1JyNhJS2ADYUbUFOPYddm0WldlNjtqc6KQ9X_rV-Im9HBfi3NgE0bluK-lyAAMr2P1A";
	// This array will hold all the registration ids used to broadcast a message.
	// for this demo, it will only have the ANDROID_DEVICE id that was captured 
	// when we ran the Android client app through Eclipse.
	private List<String> androidTargets = new ArrayList<String>();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GCMBroadcast() {
    	
        super();

        // we'll only add the hard-coded *cheat* target device registration id 
        // for this demo.
        androidTargets.add(ANDROID_DEVICE);
        androidTargets.add("APA91bG5gqf-XN4QYmJABZIRDeoYctC5_mcQCgYhxsctWfufnQejLqjyW4dMVvkRZWx7TTHu5izm098ln_H1rqJtkUCDubg80qGWmhbO3wATsCrmdeRAWZMESc_zMMIDRJ9OhFfVlZNh297Yts2N3tSpg7Hk-hh6-w");
        androidTargets.add("APA91bFI_hc3Crhm-wSZdkPugngNpl4l6L2nTQijr5-PZ5vUtW7EKEwrq9kmQD7XmaRPRXPIBUSVNHYtWor86_MpItwBCiS1JyNhJS2ADYUbUFOPYddm0WldlNjtqc6KQ9X_rV-Im9HBfi3NgE0bluK-lyAAMr2P1A");
        
    }
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
    		throws ServletException, IOException {
    	
    	//super.doGet(req, resp);
    	List<String> groupList = getGroupList();
    	req.setAttribute("groupList", groupList);
    	req.getRequestDispatcher("adminPage.jsp").forward(req, resp);
    }
    
    private List<String> getGroupList() {
		Connection conn = DatabaseConnectionManager.getInstance().getConn();
		Statement stmt;
		List<String> groups = new ArrayList<String>();
		try {
			stmt = conn.createStatement();
			String sql;
		      sql = "SELECT * FROM reviewme_groups";
		      ResultSet rs = stmt.executeQuery(sql);
		      while(rs.next()){
		    	  groups.add(rs.getString("group_name"));
		      }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	      
		return groups;
	}

	// This doPost() method is called from the form in our index.jsp file.
    // It will broadcast the passed "Message" value.
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestURL();
		// We'll collect the "CollapseKey" and "Message" values from our JSP page
		String collapseKey = "";
		String userMessage = "";
		String[] selectedOption;
		try {
			userMessage = request.getParameter("Message");
			collapseKey = request.getParameter("CollapseKey");
			selectedOption = request.getParameterValues("groups");
 			populateTargetDevice(selectedOption[0]);
 			saveBroadcatedQuestion(userMessage, Constants.DEFAULT_OPTIONS, selectedOption[0]);
 			boolean x=true;
 			if(x)
 			return;
			
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		// Instance of com.android.gcm.server.Sender, that does the
		// transmission of a Message to the Google Cloud Messaging service.
		Sender sender = new Sender(SENDER_ID);
		request.setAttribute("CollapseKey", collapseKey);
		request.setAttribute("Message", userMessage);
		String fileName="question"+(new Date()).toString();
		Util.createNewQuestionFile(fileName,userMessage);
		request.setAttribute("questoinId", fileName);
		// This Message object will hold the data that is being transmitted
		// to the Android client devices.  For this demo, it is a simple text
		// string, but could certainly be a JSON object.
		Message message = new Message.Builder()
		
		// If multiple messages are sent using the same .collapseKey()
		// the android target device, if it was offline during earlier message
		// transmissions, will only receive the latest message for that key when
		// it goes back on-line.
		.collapseKey(collapseKey)
		.timeToLive(30)
		.delayWhileIdle(true)
		.addData("message", userMessage).addData("questionId", fileName)
		.build();
		
		try {
			// use this for multicast messages.  The second parameter
			// of sender.send() will need to be an array of register ids.
			MulticastResult result = sender.send(message, androidTargets, 1);
			
			if (result.getResults() != null) {
				int canonicalRegId = result.getCanonicalIds();
				if (canonicalRegId != 0) {
					saveBroadcatedQuestion(userMessage,Constants.DEFAULT_OPTIONS,selectedOption[0]);
				}
				else{
					System.out.println("Broadcast failure: " + result.getFailure()+": "+result.getResults().get(0).getErrorCodeName());
				}
			} else {
				int error = result.getFailure();
				System.out.println("Broadcast failure: " + error);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		request.setAttribute("starting_url", request.getParameter("starting_url"));
		request.getRequestDispatcher("success.jsp").forward(request, response);
				
	}

	private void saveBroadcatedQuestion(String userMessage,
			String[] defaultOptions, String selectedGroup) {
		String options="";
		for(String s : defaultOptions){
			options=options.concat(s)+";;";
		}
		Connection conn = DatabaseConnectionManager.getInstance().getConn();
		try {
			Statement stmt = conn.createStatement();
			String date = new Date().toString();
			String sqlQuery="INSERT INTO myowndatabase.reviewme_admin_question(question_id,question,question_options,question_date,question_asked_for_group) VALUES (NULL, "+"\""+userMessage+"\""+", "+"\""+options+"\""+", "+"\""+date+"\""+", "+"\""+selectedGroup+"\""+")" ;
			System.out.println(sqlQuery);
			int success=stmt.executeUpdate(sqlQuery);
			System.out.println(success);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void populateTargetDevice(String selectedGroup) {
		// TODO Auto-generated method stub
		Connection conn = DatabaseConnectionManager.getInstance().getConn();
		try {
			Statement stmt=conn.createStatement();
			String sql = "select * from reviewme_users";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				String[] groups = rs.getString("user_group").split(";");
				if (Arrays.asList(groups).contains(selectedGroup)) {
					androidTargets.add(rs.getString("user_reg_id"));
				}
				
			}
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		
	}

}
