import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;

public class AdminControlLeft extends HttpServlet{
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
        out.println("<title>Home</title>");
        out.println("<meta name='GENERATOR' content='Microsoft FrontPage 3.0'>");
        out.println("<base target='main'>");
        out.println("</head>");
        out.println("<body topmargin='0' leftmargin='0' link='#000080'>");
        out.println("<div align='left'>");
        out.println("<table border='0' width='100%' cellspacing='0' cellpadding='0' bgcolor='#000000' height='20'>");
        out.println("<tr>");
        out.println("<td width='50%' valign='middle' align='left'>&nbsp;<img src='../examonline/images/home.gif' width='17' height='18' alt='Go To Home' align='center'> <a href='/servlet/AdminControlMiddle' target='main'><font face='Verdana' color='#FFFFFF'><small>Home</small></font></a></td>");
        out.println("<td width='50%' valign='middle' align='left'><img src='../examonline/images/logout.gif' width='17' height='18' alt='Logout.' align='center'> <a href='/servlet/Logout' target='_top'><font face='Verdana' color='#FFFFFF'><small>Logout</small></font></a></td>");
        out.println("</tr>");
        out.println("</table>");
        out.println("</div>");
        out.println("<table border='0' width='98%' height='10' cellspacing='0' cellpadding='0'>");
        out.println("<tr>");
        out.println("<td width='100%'></td>");
        out.println("</tr>");
        out.println("</table>");
        out.println("<table border='1' width='100%' height='20' cellspacing='0' cellpadding='0'bordercolor='#C0C0C0' bordercolorlight='#C0C0C0' bordercolordark='#FFFFFF' bgcolor='#F0F0FF'>");
        if(aduser.equals("admin")){
        	out.println("<tr>");
        	out.println("<td width='100%'><a href='/servlet/AddNewAdmin' target='main'><font face='Verdana' color='#000080'><small>Add New Admin</small></font></a></td></tr>");
        	out.println("<tr>");
        	out.println("<tr>");
        	out.println("<td width='100%'><a href='/servlet/DelAdminAcc' target='main'><font face='Verdana' color='#000080'><small>Delete an Admin</small></font></a></td></tr>");
        	out.println("<tr>");
        }       
        out.println("<td width='100%'><a href='/servlet/AdminPass' target='main'><font face='Verdana' color='#000080'><small>Change Password</small></font></a></td>");
        out.println("</tr>");
        out.println("<tr>");
        out.println("<td width='100%'><a href='/servlet/TestConfigure' target='main'><font face='Verdana' color='#000080'><small>Configure New Test</small></font></a></td>");
        out.println("</tr>");
        out.println("</table>");
        out.println("</body>");
        out.println("</html>");		 
	}
	public String getServletInfo(){
		return "Administrator control panel left frame";
	}
}
	