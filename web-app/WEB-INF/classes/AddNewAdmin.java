package com.vippan.examonline.admin;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import java.util.*;
import java.sql.*;
import com.vippan.db.*;

public class AddNewAdmin extends HttpServlet
{
	private ConnectionPool pool;
	public void init(ServletConfig config)throws ServletException{
		super.init(config);
		try{
			pool=new ConnectionPool(1,1);			
		}
		catch(Exception e){
			throw new UnavailableException(this,"could not create connection pool");
		}
	}
	public void doGet(HttpServletRequest req,HttpServletResponse res)throws ServletException,IOException{
		Connection con=null;
		res.setContentType("text/html");
		PrintWriter out = res.getWriter();
		res.setHeader("Cache-Control","no-store");
		HttpSession session=req.getSession(false); 
		String aduser;		
		if(session==null){
			out.println("<H2>Your Session has expired </H2>");
			out.println("<a href='../examonline/admin.html'>Click Here</a> To Re-Login");
			return;
		}		
		java.util.Date time_comp=new java.util.Date(System.currentTimeMillis()-20*60*1000);
		java.util.Date accessed=new java.util.Date(session.getLastAccessedTime());
		if(accessed.before(time_comp)){
			session.invalidate();
			out.println("<H2>Your Session has expired </H2>");
			out.println("<a href='../examonline/admin.html'>Click Here</a> To Re-Login");
			return;
		}  		
		aduser=(String)session.getValue("aduser");
		if(aduser==null){
			out.println("<H2>Your Session has expired </H2>");
			out.println("<a href='../examonline/admin.html'>Click Here</a> To Re-Login");
			return;
		}
		if(!aduser.equals("admin")){
			out.println("<H2>Your are not authorized to view this page.</H2>");
			out.println("<a href='../examonline/admin.html'>Click Here</a> To Re-Login");
			return;
		}
		out.println("<html>");
		out.println("<head>");
		out.println("<title>Getting Started With ExamOnline</title>");
		out.println("</head>");
		out.println("<body topmargin='0' leftmargin='0' link='#FFFFFF' vlink='#FFFFFF' alink='#FFFFFF'>");
		out.println("<table border='0' width='100%' cellspacing='0' cellpadding='0' bgcolor='#000000' height='24'>");
		out.println("<tr>");
		out.println("<td width='42%' height='24'><font face='Verdana' color='#FFFFFF'><small>|<a href='../examonline/getting_started.htm' target='_self'>Getting Started With ExamOnline</a></small></font></td>");
		out.println("<td width='28%' height='24'><font face='Verdana' color='#FFFFFF'><small>| <a href='../examonline/guidelines.html' target='_self'>Test GuideLines</a></small></font></td>");
		out.println("<td width='5%' height='24'></td>");
		out.println("<td width='25%' height='24'></td>");
		out.println("</tr>");
		out.println("</table>");
		out.println("<div align='right'>");
		out.println("<table border='0' width='98%' cellspacing='0' cellpadding='0'>");
		out.println("<tr>");
		out.println("<td width='100%' height='10' colspan='2'></td>");
		out.println("</tr>");
		out.println("<tr>");
		out.println("<td width='100%' colspan='2'><strong><small><font face='Verdana'>Welcome To ExamOnline  Contol Panel</font></small></strong></td>");
		out.println("</tr>");
		out.println("<tr>");
		out.println("<td width='64%' bgcolor='#C0C0C0' height='1'></td>");
		out.println("<td width='16%' bgcolor='#FFFFFF' height='1'></td>");
		out.println("</tr>");
		out.println("</table>");
		out.println("</div><div align='right'>");
		out.println("<table border='0' width='93%' height='20' cellspacing='0' cellpadding='0'>");
		out.println("<tr>");
		out.println("<td width='100%'></td>");
		out.println("</tr>");
		out.println("</table>");
		out.println("</div><div align='right'>");
		out.println("<table border='1' width='97%' bgcolor='#C0C0C0' bordercolor='#C0C0C0' cellspacing='0' cellpadding='0' bordercolorlight='#C0C0C0'>");
		out.println("<tr>");
		out.println("<td width='100%'><font face='Verdana' color='#FFFFFF'><small><strong>Add New Administrator..</strong></small></font></td>");
		out.println("</tr>");
		out.println("</table>");
		out.println("</div><div align='right'>");		
		out.println("<table border='1' width='97%' cellspacing='0' cellpadding='0' height='230' bordercolor='#COCOCO' bordercolorlight='#COCOCO'>");
		out.println("<tr>");
		out.println("<td width='100%' bgcolor='#F0F0FF' height='230' valign='top' align='center'><form method='POST' action='/servlet/NewAdminAcc'><br><br>");
		out.println("<table border='0' width='79%' cellspacing='0' cellpadding='0' height='161' >");
		out.println("<tr align='center'>");
		out.println("<td width='50%' height='19' valign='middle' align='right'><font face='Verdana' color='#000080'><small>&nbsp; New Admin's Username:</small></font></td>");
		out.println("<td width='50%' height='30' valign='middle' align='left'>&nbsp; <input type='text' name='aduser' style='border: 1px solid rgb(0,0,128)' size='20'></td>");
		out.println("</tr>");
		out.println("<tr align='center'>");
		out.println("<td width='50%' height='27' valign='middle' align='right'><font face='Verdana' color='#000080'><small>New Admin's Password:</small></font></td>");
		out.println("<td width='50%' height='30' valign='middle' align='left'>&nbsp; <input type='password' name='adpasswd' style='border: 1px solid rgb(0,0,128)' size='10'></td>");
		out.println("</tr>");
		out.println("<tr align='center'>");
		out.println("<td width='50%' height='27' valign='middle' align='right'><font face='Verdana' color='#000080'><small>Confirm Password:</small></font></td>");
		out.println("<td width='50%' height='30' valign='middle' align='left'>&nbsp; <input type='password' name='vadpasswd' style='border: 1px solid rgb(0,0,128)' size='10'></td>");
		out.println("</tr>");
		out.println("<tr align='center'>");
		out.println("<td width='50%' height='30' valign='middle' align='right'></td>");
		out.println("<td width='50%' height='30' valign='top' align='left'>&nbsp; <input type='submit' value='Submit' name='B1' style='background-color: rgb(196,196,255); color: rgb(0,0,0); border: 1px solid rgb(0,0,128)'></td>");
		out.println("</tr>");
		out.println("</table>");
		out.println("</form>");
		out.println("</td>");
		out.println("</tr>");
		out.println("</table>");
		out.println("</div>");
		out.println("</body>");
		out.println("</html>");
    }
    public String getServletInfo(){
		return "Servlet for Adding New Administrator";
	}
}		
								
				