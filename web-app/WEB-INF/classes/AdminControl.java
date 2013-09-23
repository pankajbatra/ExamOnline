import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import java.sql.*;

public class AdminControl extends HttpServlet{
	private ConnectionPool pool;
	public void init(ServletConfig config) throws ServletException{
		super.init(config);
		try{
			pool=new ConnectionPool(1,1);			
		}
		catch(Exception e){
			throw new UnavailableException(this,"could not create connection pool");
		}
	}
	public void doPost(HttpServletRequest req,HttpServletResponse res)
	throws ServletException,IOException
	{
		Connection con = null;
  		res.setContentType("text/html");
  		res.setHeader("Cache-Control","no-store");
  		PrintWriter out = res.getWriter();
  		HttpSession session = req.getSession(false);
  		String aduser,passwd;
  		try{
	  		aduser = req.getParameter("ad_user_name");
			passwd = req.getParameter("ad_passwd");
			if(aduser==null||passwd==null){
		  		out.println("<H2>Some of the Fields were left blank</H2>");
		  		out.println("<a href='javascript:history.go(-1)'>Click Here</a>  to go back to previous page & try again");
				  return;
			}
			if(aduser.equals("")||passwd.equals("")){
				out.println("<H2>Some of the Fields were left blank</H2>");
				out.println("<a href='javascript:history.go(-1)'>Click Here</a>  to go back to previous page & try again");
				return;
			}	
			con=pool.getConnection();
			Statement stmt=con.createStatement();
			ResultSet rs=stmt.executeQuery("Select * from admin where aduser = '"+aduser+"' AND adpasswd = '"+passwd+"'");
			int c=0;
			while(rs.next())
			c++;
			if(c==0){				
				out.println("<H2>Invalid Username Or Password</H2>");
				out.println("<a href='javascript:history.go(-1)'>Click Here</a> to go back to previous page & Choose a Different User Name");
				return;
			}
			rs.close(); 
			if(session!=null) session.invalidate();
			session=req.getSession(true);
			session.putValue("aduser",aduser);					
			out.println("<html>");
			out.println("<head>");
			out.println("<title>ExamOnline - Administrator Control Panel</title>");
			out.println("</head>");
			out.println("<frameset framespacing='0' border='false' rows='64,20%' frameborder='0'>");
        	out.println("<frame name='banner' scrolling='no' noresize target='main' src='../examonline/admin_control_header.htm' marginwidth='0' marginheight='0'>");
        	out.println("<frameset cols='20%,80%'>");
        	out.println("<frame name='contents' target='main' src='/servlet/AdminControlLeft' scrolling='no' marginwidth='0' marginheight='0' noresize>");
        	out.println("<frame name='main' scrolling='auto' marginwidth='0' marginheight='0' noresize src='/servlet/AdminControlMiddle'>");
        	out.println("</frameset>");
        	out.println("<noframes>");
        	out.println("<body leftmargin='0' topmargin='0'>");
        	out.println("<p>This page uses frames, but your browser doesn't support them.</p>");
        	out.println("</body>");
        	out.println("</noframes>");
        	out.println("</frameset>");
        	out.println("</html>");
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
	public String getServletInfo()
	{
	 return "Administrator's Login Page for ExamOnline ";
	}
}
			