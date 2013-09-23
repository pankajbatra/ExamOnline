import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import java.io.*;
import java.util.*;

public class NewAdminAcc extends HttpServlet
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
		String aduser,adpasswd,mainadmin,vadpasswd;
		HttpSession session=req.getSession(false);	
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
		mainadmin=(String)session.getValue("aduser");
		if(mainadmin==null){
			out.println("<H2>Your Session has expired </H2>");
			out.println("<a href='../examonline/admin.html'>Click Here</a> To Re-Login");
			return;
		}
		if(!mainadmin.equals("admin")){
			out.println("<H2>Your are not authorized to view this page.</H2>");
			out.println("<a href='../examonline/admin.html'>Click Here</a> To Re-Login");
			return;
		}
		try
		{
			aduser=req.getParameter("aduser");
			adpasswd=req.getParameter("adpasswd");
			vadpasswd=req.getParameter("vadpasswd");
			if(aduser==null||adpasswd==null||vadpasswd==null){
				out.println("<H2>Some of the fields were left blank</H2>");
				out.println("<a href='javascript:history.go(-1)'>Click Here</a> to go back & Try Again");
				return;
			}
			if(aduser.equals("")||adpasswd.equals("")||vadpasswd.equals("")){
				out.println("<H2>Some of the fields were left blank</H2>");
				out.println("<a href='javascript:history.go(-1)'>Click Here</a> to go back & Try Again");
				return;
			}			
			if(validate1(aduser));
			else{
				out.println("<H2>Invalid Username.</H2>");
				out.println("<p><center>Underscore, Numerals, Alphabets are allowed only.<br>Minimum 5 & Maximum 20  characters are allowed, And it should start with an alphabet</center>"); 
				out.println("<p><a href='javascript:history.go(-1)'>Click Here</a>  to go back to previous page & try again");
				return;
			}
			if(validate2(adpasswd));
			else{
				out.println("<H2>Invalid Pasword.</H2>");
				out.println("<p><center>Minimum 5 & Maximum 10  characters are allowed.</center>"); 
				out.println("<a href='javascript:history.go(-1)'>Click Here</a>  to go back to previous page & try again");
				return;
			}			
			if(!vadpasswd.equals(adpasswd)){
				out.println("<H2>Two Passwords does not match</H2>");
				out.println("<a href='javascript:history.go(-1)'>Click Here</a>  to go back to previous page & try again");
				return;
			}					
			con=pool.getConnection();
			Statement stmt=con.createStatement();
			ResultSet rs=stmt.executeQuery("select * from admin where aduser='"+aduser+"'");
			int c=0;
			while(rs.next()) c++;			
			if(c!=0){
				out.println("<H2>This Username Already Exists</H2>");
				out.println("<a href='javascript:history.go(-1)'>Click Here</a> to go back & Try again");
				return;
			}
			rs.close();
			stmt.executeUpdate("insert into admin values('"+aduser+"','"+adpasswd+"')");			
			out.println("<html>");
			out.println("<head>");
			out.println("<title>Getting Started With ExamOnline</title>");
			out.println("</head>");
			out.println("<body topmargin='0' leftmargin='0' link='#000000' vlink='#000000' alink='#000000'>");
			out.println("<table border='0' width='100%' cellspacing='0' cellpadding='0' bgcolor='#000000' height='24'>");
			out.println("<tr>");
			out.println("<td width='42%' height='24'><font face='Verdana' color='#FFFFFF'><small>|<a href='../examonline/getting_started.htm' target='_self'><font color='#FFFFFF'>Getting Started With ExamOnline</font></a></small></font></td>");
			out.println("<td width='28%' height='24'><font face='Verdana' color='#FFFFFF'><small>| <a href='../examonline/guidelines.html' target='_self'><font color='#FFFFFF'>Test GuideLines</font></a></small></font></td>");
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
			out.println("<td width='100%'><font face='Verdana' color='#FFFFFF'><small><strong>New Administrator Added..</strong></small></font></td>");
			out.println("</tr>");
			out.println("</table>");
			out.println("</div><div align='right'>");
			out.println("<table border='1' width='97%' cellspacing='0' cellpadding='0' height='181' bordercolor='#COCOCO' bordercolorlight='#COCOCO'>");
        	out.println("<tr>");
        	out.println("<td width='100%' bgcolor='#F0F0FF' height='181' valign='top' align='center'><table border='0' width='100%' cellspacing='0' cellpadding='0'>");
			out.println("<tr>");
            out.println("<td width='100%' height='30'><p align='center'><font face='Verdana' color='#8000FF'><small>New Administrator has been Sucessfully Added With Username: "+aduser+"</small></font></td>");
            out.println("</tr>");
			out.println("<tr>");
            out.println("<td width='100%' height='30'><p align='center'><font face='Verdana' color='#8000FF'><small><a href='/servlet/AdminControlMiddle'>Click Here</a> to continue...</small></font></td>");
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
    public boolean validate2(String value){    	    	
    	if (value.length()>10||value.length()<5) return false;    	
    	return true;
    }
    public boolean validate1(String value){
    	String valid="0123456789_abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    	String start="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    	boolean ok = true;
    	String temp;
    	if (value.length()>20||value.length()<5){
    		return false;
    	}
    	char st = value.charAt(0);
    	if (start.indexOf(st) == -1){
    		return false;
    	}    	
    	for (int i=0; i<value.length(); i++) {
    		temp = "" + value.substring(i, i+1);
    		if (valid.indexOf(temp) == -1){
    			ok =false;
    			break;
    		}
    	}
    	return ok;
    }
    public String getServletInfo(){
		return "This servlet Adds New Center Administrator";
	}	
}