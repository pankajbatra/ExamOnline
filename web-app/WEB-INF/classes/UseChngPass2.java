import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import java.io.*;
import java.util.*;

public class UseChngPass2 extends HttpServlet
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
		out.println("<html>");
		out.println("<head><title>ExamOnline - Change User Password</title></head>");
		out.println("<body bgcolor='#B3B3D9'>");		
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
				out.println("<H2>Old Password in Wrong</H2>");
				out.println("<a href='javascript:history.go(-1)'>Click Here</a> to go back & Try Again");
				return;
			}
			int update=stmt.executeUpdate("update users set passwd='"+newpasswd+"' where username='"+username+"' AND passwd='"+passwd+"'");
			if(update==0){
				out.println("<H2>An Error Has occured</H2>");
				out.println("<a href='javascript:history.go(-1)'>Click Here</a>  to go back to previous page & try again");
				return;
			}		
			out.println("<p align='center'><strong>&nbsp;</strong></p>");
			out.println("<p align='center'><strong>&nbsp;</strong></p>");
			session.invalidate();
			out.println("<p align='center'><strong>Your password has been successfully Changed</strong></p>");						
			out.println("<p align='center'><strong><a href='../examonline/user.html'>Click Here</a> to Login Again!</strong></p></body></html>");			
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
		return "This servlet Changes User's Password";
	}	
}		
								
				