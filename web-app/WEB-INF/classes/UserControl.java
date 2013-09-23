import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import java.util.*;

public class UserControl extends HttpServlet{
	private ConnectionPool pool;
	public void init(ServletConfig config) throws ServletException{
		super.init(config);
		try{
			pool=new ConnectionPool(5,2);			
		}
		catch(Exception e){
			throw new UnavailableException(this,"could not create connection pool");
		}
	}		 	
	public void doPost(HttpServletRequest req,HttpServletResponse res) 
	throws ServletException,IOException
	{
		Connection con=null;
		res.setContentType("text/html");
		res.setHeader("Cache-Control","no-store");		 
		HttpSession session=req.getSession(false); 		
		PrintWriter out=res.getWriter();		
		String username,passwd;
		try{					
			username=req.getParameter("username");
			passwd=req.getParameter("passwd");
			if(username==null||passwd==null){
				out.println("<H2>Some of the Fields were left blank</H2>");
				out.println("<a href='javascript:history.go(-1)'>Click Here</a>  to go back to previous page & try again");
				return;
			}
			if(username.equals("")||passwd.equals("")){
				out.println("<H2>Some of the Fields were left blank</H2>");
				out.println("<a href='javascript:history.go(-1)'>Click Here</a>  to go back to previous page & try again");
				return;
			}			
			con=pool.getConnection();
			Statement stmt=con.createStatement();
			ResultSet rs=stmt.executeQuery("Select * from users where username = '"+username+"' AND passwd = '"+passwd+"'");
			int c=0;
			while(rs.next())
			   c++;
			if(c==0){
				out.println("<H2>Invalid Username Or Password</H2>");
				out.println("<a href='javascript:history.go(-1)'>Click Here</a> to go back to previous page & Choose a Different User Name");
				return;
			}
			if(session!=null)
			  session.invalidate();
			session=req.getSession(true);
			session.putValue("username",username);
			out.println("<html>");
			out.println("<head>");
			out.println("<title>ExamOnline - Test Taker Control Panel</title>");
			out.println("</head>");
			out.println("<frameset framespacing='0' border='false' rows='64,20%' frameborder='0'>");
        	out.println("<frame name='banner' scrolling='no' noresize target='main' src='../examonline/user_control_header.htm' marginwidth='0' marginheight='0'>");
        	out.println("<frameset cols='20%,80%'>");
        	out.println("<frame name='contents' target='main' src='/servlet/UserControlLeft' scrolling='no' marginwidth='0' marginheight='0' noresize>");
        	out.println("<frame name='main' scrolling='auto' marginwidth='0' marginheight='0' noresize src='/servlet/UserControlMiddle'>");
        	out.println("</frameset>");
        	out.println("<noframes>");
        	out.println("<body leftmargin='0' topmargin='0'>");
        	out.println("<p>This page uses frames, but your browser doesn't support them.</p>");
        	out.println("</body>");
        	out.println("</noframes>");
        	out.println("</frameset>");
        	out.println("</html>");
			/*out.println("<body bgcolor='#B3B3D9'>");
			out.println("<p align='center'><font color='#000000' face='verdana'><big><big><strong>ExamOnline Student's Homepage</strong></big></big></font></p>");
			out.println("<hr>");			
			rs=stmt.executeQuery("select * from users where username='"+username+"'");
			String name="";
			while(rs.next()){
				name=rs.getString("name");
			}
			out.println("<p align='left'><small><strong><font face='Verdana' color='#FFFF00'><center>Hi,"+name+" Welcome to Your Homepage at ExamOnline</font></strong></small></p>");              
			out.println("<p align='center'><a href='TakeTest'>Take Test</a></p>");
			int testcheck=0;
			rs=stmt.executeQuery("select * from result where username='"+username+"'");
			while(rs.next())testcheck++;
			if(testcheck==0);
			else out.println("<p align='center'><a href='ViewScore'>View Scores</a></p>");
			out.println("<p align='center'><a href='UseChngPass1'>Change Password</a></p>");
			out.println("<p align='center'><a href='UseChngAcc1'>Change Account Information</a></p>");
			out.println("<p align='center'><a href='Logout'>Logout</a></p>");
		    out.println("</BODY></HTML>");*/
		    rs.close();
		    stmt.close();
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
		return "Test Taker's Login Page for ExamOnline ";
	}
}