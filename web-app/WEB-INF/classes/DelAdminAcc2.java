import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import java.io.*;
import java.util.*;

public class DelAdminAcc2 extends HttpServlet
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
		String mainadmin,aduser;
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
			con=pool.getConnection();
			Statement stmt=con.createStatement();
			if(aduser==null){
				out.println("<H2>Some of the fields were left blank</H2>");
				out.println("<a href='javascript:history.go(-1)'>Click Here</a> to go back & Try Again");
				return;
			}
			if(aduser.equals("")){
				out.println("<H2>Some of the fields were left blank</H2>");
				out.println("<a href='javascript:history.go(-1)'>Click Here</a> to go back & Try Again");
				return;
			}			
			int total=stmt.executeUpdate("Delete from admin where aduser='"+aduser+"'");						
			if(total==0){
				out.println("<H2>There is no Admin Account with this Username.</H2>");
				out.println("<a href='javascript:history.go(-1)'>Click Here</a>  to go back to previous page & try again");
				return;
			}			
			//new code
			stmt.executeUpdate("update test_master set test_status=2 where aduser='"+aduser+"'");
			/*old code;
			ResultSet rs=stmt.executeQuery("Select * from test_master where aduser='"+aduser+"'");				
			int test_code=0,count=0,c=0,i=0;
			while(rs.next()){
				test_code=rs.getInt("test_code");
				count = rs.getInt("test_no_subject");
        		for(i=0;i<count;i++){
	        		stmt.executeUpdate("drop table if exists test_"+test_code+"_"+i);	
    	    	}
        		stmt.executeUpdate("delete from result where test_code="+test_code);
        		stmt.executeUpdate("delete from test_info where test_code="+test_code);
        	}        	
        	stmt.executeUpdate("delete from test_master where aduser='"+aduser+"'");
        	*/
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
			out.println("<td width='100%'><font face='Verdana' color='#FFFFFF'><small><strong>Administrator's Account Deleted...</strong></small></font></td>");
			out.println("</tr>");
			out.println("</table>");
			out.println("</div><div align='right'>");
			out.println("<table border='1' width='97%' cellspacing='0' cellpadding='0' height='181' bordercolor='#COCOCO' bordercolorlight='#COCOCO'>");
        	out.println("<tr>");
        	out.println("<td width='100%' bgcolor='#F0F0FF' height='181' valign='top' align='center'><table border='0' width='100%' cellspacing='0' cellpadding='0'>");
			out.println("<tr>");
            out.println("<td width='100%' height='30'><p align='center'><font face='Verdana' color='#8000FF'><small>Administrator's Account has been Sucessfully Deleted With Username: "+aduser+"</small></font></td>");
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
    public String getServletInfo(){
		return "This servlet Deletes an Existing Center Admin's Account";
	}	
}