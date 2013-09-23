import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import java.io.*;
import java.util.*;

public class PassReminder1 extends HttpServlet
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
		String useremin,username;
		try
		{
			out.println("<html>");
			out.println("<head><title>ExamOnline - Password Reminder</title></head>");
			out.println("<body>");
			username=req.getParameter("username");
			if(username.equals(""))
			{
				out.println("<H2>username field was left empty</H2>");
				out.println("<a href='javascript:history.go(-1)'>Click Here</a>to go back to previous page and try again");
				out.println("</body></html>");
				return;
			}			
			con=pool.getConnection();
			Statement stmt=con.createStatement();
			ResultSet rs=stmt.executeQuery("select * from users where username='"+username+"'");
			int c=0;
			while(rs.next())c++;
			if(c==0)
			{
				out.println("<H2>Invalid Username</H2>");
				out.println("<a href='javascript:history.go(-1)'>Click Here</a> to go back to previous page & Choose a Different User Name");
				out.println("</BODY></HTML>");
				return;
			}
			rs=stmt.executeQuery("select useremin from users where username='"+username+"'");
			int qno=0;
			while(rs.next())
			{
				qno=rs.getInt("useremin");
			}
			rs.close();
			out.println("<p align='center'><strong>&nbsp;</strong></p>");
			out.println("<p align='center'><strong>&nbsp;</strong></p>");
			out.println("<p align='center'><strong>Your password reminder question is: -</strong><br><strong>");
			if(qno==1)
			out.println("What is your pet name?");
			if(qno==2)
			out.println("Which is your favourite team?");
			if(qno==3)
			out.println("Which is your favourite recipe?");
			if(qno==4)
			out.println("Which is your birthplace?");
			out.println("</strong></p>");
			out.println("<Form method='POST' action='/servlet/PassReminder2'><input type=hidden name='username' value='"+username+"'>");
			out.println("<div align='center'><center><p><strong>Your Answer: </strong><input type='password' name='useansr' size='20'></p>");
			out.println("</center></div><div align='center'><center><p><input type='submit' value='Submit' name='B1'><input type='reset' value='Reset' name='B2'></p>");
			out.println("</center></div></form></body></html>");
			}
		    catch(Exception e){
			try{
				out.println("<H2>An Error has occured: "+e.getMessage()+"</H2>");
				out.println("<br><br><a href='javascript:history.go(-1)'>Click Here</a> to go back to previous page & Try Again");
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
		return "This servlet asks the passwd reminder qusetion for forgotten password recovery";
	}
}		
								
				