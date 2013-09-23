import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;
import java.util.*;

public class DeleteQuestion extends HttpServlet{
	public void doGet(HttpServletRequest req,HttpServletResponse res)
	throws ServletException,IOException{
		res.setContentType("text/html");
		res.setHeader("Cache-Control","no-store");
		PrintWriter out=res.getWriter();
		HttpSession session=req.getSession(false);
		String ad_user=null;
		String table_name = req.getParameter("table_name");
		String t_min = req.getParameter("min");
		int tmin = Integer.parseInt(t_min);
		ad_user = (String)session.getValue("aduser");
		java.util.Date time_comp=new java.util.Date(System.currentTimeMillis()-20*60*1000);
		java.util.Date accessed=new java.util.Date(session.getLastAccessedTime());
		if(session==null||ad_user==null||accessed.before(time_comp)){
			    session.invalidate();
			    out.println("<H2>Your Session has expired </H2>");
				out.println("<a href='../examonline/admin.html'>Click Here</a> To Re-Login");
				return;
		}
		
				
			/*out.println("<html>");
			out.println("<head>");
			out.println("<title>Getting Started With ExamOnline</title>");
			out.println("</head>");
			out.println("<body>"); */
			RequestDispatcher dispatcher = req.getRequestDispatcher("/servlet/ViewQuestion?table_name="+table_name+"&min="+tmin+"&flag=2");
			dispatcher.include(req,res);
			//out.println("have fun");
			/*out.println("<div align='center'><center>");
			out.println("<table border='1' width='60%' cellspacing='0' cellpadding='0' height='100' bordercolor='#COCOCO' bordercolorlight='#COCOCO'>");
			out.println("<tr>");
			out.println("<td width='100%' bgcolor='#F0F0FF' height='100' valign='top' align='center'><table border='0' width='100%' cellspacing='0' cellpadding='0' height='115'>");
			out.println("<tr>");
			out.println("<td width='42%' height='25' align='right'><p align='center'><font face='Verdana' color='#004000'><small>Enter the question number you want to edit:</small></font></td>");
			out.println("</tr>");
			out.println("<tr>");
			out.println("<td width='42%' height='60' align='left'><form method='POST' action='/servlet/EditQuesSingle'>");
            out.println("<div align='center'><center><p>&nbsp;<input type='text' name='ques_number' size='14' style='border: 1px solid rgb(0,0,0)'><br>");
           	out.println("<INPUT TYPE='hidden' NAME='table_name' VALUE='"+table_name+"'>");
	
            out.println("<input type='submit' value='Submit' name='B1' style='background-color: rgb(238,238,238); color: rgb(0,0,0); border: 1px solid rgb(0,0,0)'></p>");
            out.println("</center></div>");
            out.println("</form>");
            out.println("</td>");
            out.println("</tr>");
            out.println("</table>");
            out.println("</td>");
            out.println("</tr>");
            out.println("</table>");
            out.println("</center></div>");
            out.println("</body>");
			out.println("</html>");		*/								
		
	}
	public String getServletInfo(){
		return "Servlet to edit question from a list of questions";
	}	
}