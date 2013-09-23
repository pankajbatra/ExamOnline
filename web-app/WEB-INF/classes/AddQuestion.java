package com.vippan.examonline.admin;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;
import java.util.*;


public class AddQuestion extends HttpServlet{
	private ConnectionPool pool;
	public void init(ServletConfig config) throws ServletException{
		super.init(config);
		try{
			pool=new ConnectionPool(1,1);
		}
		catch(Exception e){
			throw new UnavailableException(this,"could not create connection pool");
		}
	}
	public void doGet(HttpServletRequest req,HttpServletResponse res)
	throws ServletException,IOException{
		res.setContentType("text/html");
		Connection con=null;
		res.setHeader("Cache-Control","no-store");
		PrintWriter out=res.getWriter();
		HttpSession session=req.getSession(false);
		String ad_user=null,table_name=null;
		ad_user = (String)session.getValue("aduser");
		java.util.Date time_comp=new java.util.Date(System.currentTimeMillis()-20*60*1000);
		java.util.Date accessed=new java.util.Date(session.getLastAccessedTime());
		if(session==null||ad_user==null||accessed.before(time_comp)){
			    session.invalidate();
			    out.println("<H2>Your Session has expired </H2>");
				out.println("<a href='../examonline/admin.html'>Click Here</a> To Re-Login");
				return;
		}
		try{
			con = pool.getConnection();						
			table_name = req.getParameter("table_name");
			int test_code=0;
			Statement stmt = con.createStatement();
			ResultSet rs=stmt.executeQuery("select * from test_info where table_name='"+table_name+"'");
			if(rs.next()) test_code=rs.getInt("test_code");
			rs=stmt.executeQuery("select * from test_master where aduser='"+ad_user+"' AND test_code="+test_code);
			int c=0;
			while(rs.next()){
			 	c++;
			}
			if(c==0){
				session.invalidate();			    
		    	out.println("<H2>Your Session has expired </H2>");
				out.println("<a href='../examonline/admin.html'>Click Here</a> To Re-Login");
				rs.close();
				stmt.close();
				return;
			}			
			rs.close();
			stmt.close();
			out.println("<html>");
			out.println("<head>");
			out.println("<title>Getting Started With ExamOnline</title>");
			out.println("</head>");
			out.println("<body topmargin='0' leftmargin='0' link='#000000' vlink='#000000' alink='#000000'>");
			out.println("<table border='0' width='100%' cellspacing='0' cellpadding='0' bgcolor='#000000' height='24'>");
			out.println("<tr>");
			out.println("<td width='42%' height='24'></td>");
			out.println("<td width='28%' height='24'></td>");
			out.println("<td width='5%' height='24'></td>");
			out.println("<td width='25%' height='24'></td>");
			out.println("</tr>");
			out.println("</table>");
			out.println("<div align='right'>");
			out.println("<table border='0' width='98%' cellspacing='0' cellpadding='0' height='10'>");
			out.println("<tr>");
			out.println("<td width='100%' height='10' colspan='2'></td>");
			out.println("</tr>");
			out.println("<tr>");
			out.println("<td width='100%' colspan='2'><strong><small><font face='Verdana'>Welcome To ExamOnline Contol Panel</font></small></strong></td>");
			out.println("</tr>");
			out.println("<tr>");
			out.println("<td width='64%' bgcolor='#C0C0C0' height='1'></td>");
			out.println("<td width='16%' bgcolor='#FFFFFF' height='1'></td>");
			out.println("</tr>");
			out.println("</table>");
			out.println("</div><div align='right'>");
			out.println("<table border='0' width='93%' height='10' cellspacing='0' cellpadding='0'>");
			out.println("<tr>");
			out.println("<td width='100%'></td>");
			out.println("</tr>");
			out.println("</table>");
			out.println("</div><div align='right'>");
			out.println("<table border='1' width='97%' bgcolor='#C0C0C0' bordercolor='#C0C0C0' cellspacing='0' cellpadding='0' bordercolorlight='#C0C0C0' height='10'>");
			out.println("<tr>");
			out.println("<td width='100%'><small><small><font face='Verdana' color='#FFFFFF'><small><strong>Add Questions...</strong></small></font></small></small></td>");
			out.println("</tr>");
			out.println("</table>");
			out.println("</div><div align='right'>");
			out.println("<table border='1' width='97%' cellspacing='0' cellpadding='0' height='60' bordercolor='#COCOCO' bordercolorlight='#COCOCO' bordercolordark='#FFFFFF'>");
			out.println("<tr>");
			out.println("<td width='100%' bgcolor='#F0F0FF' height='60' valign='top' align='center'><form method='POST' action='/servlet/AddQuestion1' ENCTYPE='multipart/form-data' >");
			out.println("<table border='0' width='97%' cellspacing='0' cellpadding='0' height='201'>");
			out.println("<tr>");
			out.println("<td width='23%' align='right' height='70'><small><small><font face='Verdana'>Statement :</font></small></small></td>");
			out.println("<td width='77%' height='70'>&nbsp; <textarea rows='3' name='table_statement' cols='55' style='background-color: rgb(255,255,255); background-attachment: scroll; border: 1px solid rgb(0,0,0)'></textarea></td>");
			out.println("</tr>");
			out.println("<tr>");
			out.println("<td width='23%' align='right' height='60'><small><small><font face='Verdana'>Question :</font></small></small></td>");
			out.println("<td width='77%' height='60'>&nbsp; <textarea rows='3' name='table_question' cols='55' style='background-color: rgb(255,255,255); border: 1px solid rgb(0,0,0)'></textarea></td>");
			out.println("</tr>");
			out.println("<tr>");
			out.println("<td width='23%' align='right' height='20' valign='top'><font face='Verdana'><small><small>&nbsp;</small></small><input type='checkbox' name='table_image' value='0'><small><small> &nbsp; Image :</small></small></font></td>");
			out.println("<td width='77%' height='30'>&nbsp; <input type='file' name='image' size='20' style='background-color: rgb(255,255,255); border: 1px solid rgb(0,0,0)'></td>");
			out.println("</tr>");
			out.println("<tr>");
			out.println("<td width='23%' align='right' height='60'><small><small><font face='Verdana'>Source Code :</font></small></small></td>");
			out.println("<td width='77%' height='30'>&nbsp; <textarea rows='3' name='table_code' cols='55' style='background-color: rgb(255,255,255); border: 1px solid rgb(0,0,0)'></textarea></td>");
			out.println("</tr>");
			out.println("<tr>");
			out.println("<td width='23%' align='right' height='50'><small><small><font face='Verdana'>Answer Choice 1:</font></small></small></td>");
			out.println("<td width='77%' height='50'>&nbsp; <textarea rows='2' name='ans_ch_1' cols='55' style='border: 1px solid rgb(0,0,0)'></textarea></td>");
			out.println("</tr>");
			out.println("<tr>");
			out.println("<td width='23%' align='right' height='50'><small><small><font face='Verdana'>Answer Choice 2:</font></small></small></td>");
			out.println("<td width='77%' height='50'>&nbsp; <textarea rows='2' name='ans_ch_2' cols='55' style='border: 1px solid rgb(0,0,0)'></textarea></td>");
			out.println("</tr>");
			out.println("<tr>");
			out.println("<td width='23%' align='right' height='50'><small><small><font face='Verdana'>Answer Choice 3:</font></small></small></td>");
			out.println("<td width='77%' height='50'>&nbsp; <textarea rows='2' name='ans_ch_3' cols='55' style='border: 1px solid rgb(0,0,0)'></textarea></td>");
			out.println("</tr>");
			out.println("<tr>");
			out.println("<td width='23%' align='right' height='50'><small><small><font face='Verdana'>Answer Choice 4:</font></small></small></td>");
			out.println("<td width='77%' height='50'>&nbsp; <textarea rows='2' name='ans_ch_4' cols='55' style='border: 1px solid rgb(0,0,0)'></textarea></td>");
			out.println("</tr>");
			out.println("<tr>");
			out.println("<td width='23%' align='right' height='25'><small><small><font face='Verdana'>Correct Choice :</font></small></small></td>");
			out.println("<td width='77%' height='35' valign='middle'><div align='left'><p>&nbsp;&nbsp;<input type='radio' value='1' checked name='ans_ch_correct' style='background-position:bottom'>");
        	out.println("<strong><font face='Verdana'>1</font></strong> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <input type='radio' name='ans_ch_correct' value='2'> <strong><font face='Verdana'>2</font>");
        	out.println("</strong>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type='radio' name='ans_ch_correct' value='3'>&nbsp;<strong><font face='Verdana'>3</font></strong>");
        	out.println("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type='radio' name='ans_ch_correct' value='4'> <strong><font face='Verdana'>4</font></strong>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>");
        	out.println("</tr>");				
			out.println("</tr>");
			out.println("<INPUT TYPE='hidden' NAME='table_name' VALUE='"+table_name+"'>");
			out.println("<tr>");
			out.println("<td width='23%' align='right' height='15'></td>");
			out.println("<td width='77%' height='15'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type='submit' value='Submit' name='B1' style='background-color: rgb(218,218,218); color: rgb(0,0,0); border: 1px solid rgb(0,0,0)'></td>");
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
		catch(Exception e){
			try{
				out.println("<H2>An Error has occured: "+e.getMessage()+"</H2>");
				out.println("<a href='javascript:history.go(-2)'>Click Here</a> to go back & Try Again");
				con.rollback();				
			}
			catch (Exception ignored) { }			
		}
		finally{
			if(con!=null) pool.returnConnection(con);
			out.close();		
		}			
	}
	public String getServletInfo(){
		return "Servlet to produce form for adding questions to a test";
	}	
}