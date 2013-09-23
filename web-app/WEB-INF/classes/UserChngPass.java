import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import java.io.*;
import java.util.*;

public class UserChngPass extends HttpServlet
{
	private ConnectionPool pool;
	public void init(ServletConfig config)throws ServletException
	{
		super.init(config);
		try
		{
			pool=new ConnectionPool(1,1);
		}
		catch(Exception e)
		{
			throw new UnavailableException(this,"could not create connection pool");
		}
	}
	public void doPost(HttpServletRequest req,HttpServletResponse res)
	throws ServletException,IOException
	{
		Connection con=null;
		res.setContentType("text/html");
		PrintWriter out = res.getWriter();
		res.setHeader("Cache-Control","no-store"); 
		String username,passwd,newpasswd,vnewpasswd;
		HttpSession session=req.getSession(false);				
		if(session==null){
			out.println("<H2>Your Session has expired </H2>");
			out.println("<a href='../examonline/user.html'>Click Here</a> To Re-Login");
			return;
		}		
		java.util.Date time_comp=new java.util.Date(System.currentTimeMillis()-20*60*1000);
		java.util.Date accessed=new java.util.Date(session.getLastAccessedTime());
		if(accessed.before(time_comp)){
			session.invalidate();
			out.println("<H2>Your Session has expired </H2>");
			out.println("<a href='../examonline/user.html'>Click Here</a> To Re-Login");
			return;
		}  		
		username=(String)session.getValue("username");
		if(username==null){
			out.println("<H2>Your Session has expired </H2>");
			out.println("<a href='../examonline/user.html'>Click Here</a> To Re-Login");
			return;
		}
		try
		{
			passwd=req.getParameter("passwd");
			newpasswd=req.getParameter("newpasswd");
			vnewpasswd=req.getParameter("vnewpasswd");
			if(passwd==null||newpasswd==null||vnewpasswd==null){
				out.println("<H2>Some of The fields were left blank</H2>");
				out.println("<a href='javascript:history.go(-1)'>Click Here</a> to go back & Try Again");
				return;
			}
			if(passwd.equals("")||newpasswd.equals("")||vnewpasswd.equals("")){
				out.println("<H2>Some of The fields were left blank</H2>");
				out.println("<a href='javascript:history.go(-1)'>Click Here</a> to go back & Try Again");
				return;
			}
			if(!newpasswd.equals(vnewpasswd)){
				out.println("<H2>Two Passwords does not match</H2>");
				out.println("<a href='javascript:history.go(-1)'>Click Here</a>  to go back to previous page & try again");
				return;
			}
			if(validate(newpasswd));
			else{
				out.println("<H2>Password can have minimum 5 & maximum 10 characters only</H2>");
				out.println("<a href='javascript:history.go(-1)'>Click Here</a>  to go back to previous page & try again");
				return;
			}				
			con=pool.getConnection();
			Statement stmt=con.createStatement();
			ResultSet rs=stmt.executeQuery("select * from users where username='"+username+"'");
			String pass="";
			while(rs.next())
			{
				pass=rs.getString("passwd");
			}
			rs.close();
			if(!passwd.equals(pass)){
				out.println("<H2>Old Password is Wrong</H2>");
				out.println("<a href='javascript:history.go(-1)'>Click Here</a> to go back & Try Again");
				return;
			}
			int update=stmt.executeUpdate("update users set passwd='"+newpasswd+"' where username='"+username+"' AND passwd='"+passwd+"'");
			if(update==0){
				out.println("<H2>An Error Has occured</H2>");
				out.println("<a href='javascript:history.go(-1)'>Click Here</a>  to go back to previous page & try again");
				return;
			}
			out.println("<html>");
			out.println("<head>");
			out.println("<title>Getting Started With ExamOnline</title>");
			out.println("</head>");
			out.println("<body topmargin='0' leftmargin='0' link='#ffffff' vlink='#ffffff' alink='#ffffff'>");
			out.println("<table border='0' width='100%' cellspacing='0' cellpadding='0' bgcolor='#000000' height='24'>");
			out.println("<tr>");
			out.println("<td width='42%' height='24'><font face='Verdana' color='#FFFFFF'><small>|<a href='../examonline/user_getting_started.htm' target='_self'>Getting Started With ExamOnline</a></small></font></td>");
			out.println("<td width='28%' height='24'></td>");
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
			out.println("<td width='100%'><font face='Verdana' color='#FFFFFF'><small><strong>Password Changed Successfully..</strong></small></font></td>");
			out.println("</tr>");
			out.println("</table>");
			out.println("</div><div align='right'>");			
			out.println("<table border='1' width='97%' cellspacing='0' cellpadding='0' height='181' bordercolor='#COCOCO' bordercolorlight='#COCOCO'>");
        	out.println("<tr>");
        	out.println("<td width='100%' bgcolor='#F0F0FF' height='181' valign='top' align='center'><table border='0' width='100%' cellspacing='0' cellpadding='0'>");
			out.println("<tr>");
            out.println("<td width='100%' height='30'><p align='center'><font face='Verdana' color='#8000FF'><small>Password has been Sucessfully Changed</small></font></td>");
            out.println("</tr>");
            session.invalidate();
			out.println("<tr>");
            out.println("<td width='100%' height='30'><p align='center'><font face='Verdana' color='#8000FF'><small><a href='../examonline/user.html' target='_top'><font color='#000000'>Click Here</font></a> to Login Again!</small></font></td>");
            out.println("</tr>");
        	out.println("</table>");
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
				con.rollback();				
			}
			catch (Exception ignored) { }
			
		}
		finally{
			
				if(con!=null) pool.returnConnection(con);
				out.close();		
		}
    }
    public boolean validate(String value){    	    	
    	if (value.length()>10||value.length()<5) return false;    	
    	return true;
    }
    public String getServletInfo(){
		return "This servlet Changes User Password";
	}	
}		
								
				