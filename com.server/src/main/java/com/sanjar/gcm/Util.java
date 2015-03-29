package com.sanjar.gcm;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

public class Util {

	public static void storeUserResponse(String questionId, String email, String responseAns,
			String comment) {
		/*PrintWriter printWriter = null;
		try {
			printWriter= new PrintWriter(new FileWriter(questionId,true));
			printWriter.append("RegId:"+regId+" | " +"Option Chosen:"+responseAns+" | "+"Comment:"+comment+"\n");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			printWriter.close();
		}*/

		Connection connection = DatabaseConnectionManager.getInstance().getConn();
		try {
			Statement stmt = connection.createStatement();
			String date = new Date().toString();
			String sqlQuery= "INSERT INTO myowndatabase.reviewme_question_response(response_id,response_question_id,response_by_user,response,response_date) VALUES (NULL, "+"\""+questionId+"\""+", "+"\""+email+"\""+", "+"\""+responseAns+"\""+", "+"\""+date+"\""+")" ;
			int success=stmt.executeUpdate(sqlQuery);
			System.out.println("success: "+success);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void createNewQuestionFile(String fileName, String userMessage) {
		File file = new File(fileName);
		
		PrintWriter printWriter = null;
		try {
			file.createNewFile();
			System.out.println(file.getAbsolutePath());
			printWriter= new PrintWriter(new FileWriter(file,true));
			printWriter.append("Feedback Question: "+userMessage+"\n");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			printWriter.close();
		}
		
	}
}
