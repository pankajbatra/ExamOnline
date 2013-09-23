import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

public class TestConfigure extends HttpServlet{
	
public void doGet(HttpServletRequest req,HttpServletResponse res)
	throws ServletException,IOException
	{
		res.setContentType("text/html");
		res.setHeader("Cache-Control","no-store");
		PrintWriter out=res.getWriter();
		HttpSession session=req.getSession(false);
		String aduser;
		aduser = (String)session.getValue("aduser");
		java.util.Date time_comp=new java.util.Date(System.currentTimeMillis()-20*60*1000);
		java.util.Date accessed=new java.util.Date(session.getLastAccessedTime());
		if(session==null||aduser==null||accessed.before(time_comp)){
			    session.invalidate();
			    out.println("<H2>Your Session has expired </H2>");
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
		out.println("<td width='100%'><font face='Verdana' color='#FFFFFF'><small><strong>Configure a new test..</strong></small></font></td>");
		out.println("</tr>");
		out.println("</table>");
		out.println("</div><div align='right'>");
		out.println("<table border='1' width='97%' cellspacing='0' cellpadding='0' height='271' bordercolor='#COCOCO' bordercolorlight='#COCOCO'>");
		out.println("<tr>");
		out.println("<td width='100%' bgcolor='#F0F0FF' height='271' valign='top' align='center'><form method='POST' action='/servlet/TestConfigure1'>");
		out.println("<table border='0' width='79%' cellspacing='0' cellpadding='0' height='161' <tr>");
		out.println("<tr>");
		out.println("<td width='100%' height='27' valign='top' align='left' colspan='2'><div align='center'><center><p><font face='Verdana' color='#000080'><small>Please Fill the form below to configure an online test.</small></font></td>");
		out.println("</tr>");
		out.println("<tr align='center'>");
		out.println("<td width='50%' height='19' valign='middle' align='right'><font face='Verdana' color='#000080'><small>&nbsp; Test Name</small></font></td>");
		out.println("<td width='50%' height='30' valign='top' align='left'>&nbsp; <input type='text' name='test_name' size='20'><small><font face='verdana'><small><small>*Maximum 20 chars</samll></small></font></small></td>");
		out.println("</tr>");
		out.println("<tr align='center'>");
		out.println("<td width='50%' height='27' valign='middle' align='right'><font face='Verdana' color='#000080'><small>Test Duration</small></font></td>");
		out.println("<td width='50%' height='30' valign='top' align='left'>&nbsp; <input type='text' name='test_duration' size='10'><font face='Verdana' color='#000080'><small>in min.</font><font face='verdana'><small><small>(*Max.999 mins.)</samll></small></small></font></td>");
		out.println("</tr>");
		out.println("<tr align='center'>");
		out.println("<td width='50%' height='25' valign='middle' align='right'><font face='Verdana' color='#000080'><small>Number of subjects/sections</small></font></td>");
		out.println("<td width='50%' height='30' valign='top' align='left'>&nbsp; <input type='text' name='test_subjects' size='10'><font face='Verdana' color='#000080'><small></font><font face='verdana'><small><small>(*Maximum 99)</small></samll></small></font></td>");
		out.println("</tr>");
		out.println("<tr align='center'>");
		out.println("<td width='50%' height='6' valign='middle' align='right'><font face='Verdana' color='#000080'><small>*Other Information</small></font></td>");
		out.println("<td width='50%' height='30' valign='top' align='left'>&nbsp; <textarea rows='7' name='test_details' cols='30'></textarea></td>");
		out.println("</tr>");
		out.println("<tr align='center'>");
		out.println("<td width='50%' height='6' valign='middle' align='right'></td>");
		out.println("<td width='50%' height='30' valign='top' align='left'>&nbsp; <font face='Verdana' color='#000080'><small>*Please limit details to 200 characters.</small></font></td>");
		out.println("</tr>");
		out.println("<tr align='center'>");
		out.println("<td width='50%' height='30' valign='middle' align='right'></td>");
		out.println("<td width='50%' height='30' valign='top' align='left'>&nbsp; <input type='submit' value='Submit' name='B1' style='background-color: rgb(196,196,255); color: rgb(0,0,0); border: 1px solid rgb(0,0,128)'></td>");
		out.println("</tr>");
		out.println("<tr align='center'>");
		out.println("<td width='50%' height='37' valign='middle' align='right'></td>");
		out.println("<td width='50%' height='37' valign='top' align='left'>&nbsp; </td>");
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
		return "Test Cofiguration form generator";
    }
}
	
	