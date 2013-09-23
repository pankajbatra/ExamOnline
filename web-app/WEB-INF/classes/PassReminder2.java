import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import java.io.*;
import java.util.*;

public class PassReminder2 extends HttpServlet
{
	private ConnectionPool pool;
	public void init(ServletConfig config)	throws ServletException
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
		String username,useansr,passwd="";
		try
		{
			out.println("<html>");
			out.println("<head><title>ExamOnline - Password Reminder</title></head>");
			out.println("<body>");
			username=req.getParameter("username");
			useansr=req.getParameter("useansr");
			if(useansr.equals("")){
				out.println("<H2>The Answer field was left blank</H2>");
				out.println("<a href='javascript:history.go(-2)'>Click Here</a> to go back & Try Again");
				out.println("</BODY></HTML>");
				return;
			}		
			con=pool.getConnection();
			Statement stmt=con.createStatement();
			ResultSet rs=stmt.executeQuery("select * from users where username='"+username+"'");
			String answer="";
			while(rs.next())
			{
				answer=rs.getString("useansr");
				passwd=rs.getString("passwd");
			}
			rs.close();
			if(!useansr.equals(answer)){
				out.println("<H2>The Answer is Wrong</H2>");
				out.println("<a href='javascript:history.go(-2)'>Click Here</a> to go back & Try Again");
				out.println("</BODY></HTML>");
				return;
			}	
			out.println("<p align='center'><strong>&nbsp;</strong></p>");
			out.println("<p align='center'><strong>&nbsp;</strong></p>");
			out.println("<p align='center'><strong>Your password is :"+passwd+"</strong></p>");
			out.println("<p align='center'><strong><a href='../examonline/main.html'>Click Here</a> to Login Now!</strong></p></body></html>");
			}
		    catch(Exception e){
			try{
				out.println("<H2>An Error has occured: "+e.getMessage()+"</H2>");
				e.printStackTrace(out);
				out.println("<br><br><a href='javascript:history.go(-1)'>Click Here</a> to go back to previous page & Try Again");
				out.println("</BODY></HTML>");
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
		return "This servlet tells the passwd after user gives right answer to reminder qusetion";
	}
}		
								
				