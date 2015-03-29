package com.sanjar.gcm;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ReviewHandlerServlet
 */
@WebServlet("/saveMyReview")
public class ReviewHandlerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReviewHandlerServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/*File file = new File("question_01_02_2015");
		file.createNewFile();
		System.out.println(file.getAbsolutePath());*/
		//Util.storeUserResponse(file,"45","Very Good",request.getParameter("comment"));
		response.setContentType("text/html");
		
		PrintWriter printWriter = response.getWriter();
		printWriter.println("<h1>No Get Request Defined for this. :)</h1>");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String questionId= (String) request.getParameter("questionId");
		String email= (String) request.getParameter("email");
		String responseAns= (String) request.getParameter("responseOption");
		String comment= (String) request.getParameter("comment");
		if(questionId!=null && !questionId.isEmpty()){
		//File file = new File(questionId);
		//file.createNewFile();
		//System.out.println(file.getAbsolutePath());
		Util.storeUserResponse(questionId,email,responseAns,comment);
		}
	}

	

}
