import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;

public class UseChngPass1 extends HttpServlet
{
	public void init(ServletConfig config)throws ServletException{
		super.init(config);
	}
	public void doGet(HttpServletRequest req,HttpServletResponse res)throws ServletException,IOException{
		res.setContentType("text/html");
		PrintWriter out = res.getWriter();
		res.setHeader("Cache-Control","no-store");
		HttpSession session=req.getSession(false); 
		String username,passwd;
		out.println("<html>");
		out.println("<head><title>ExamOnline - Change User Password</title></head>");
		out.println("<body bgcolor='#B3B3D9'>");		
		if(session==null){
			out.println("<H2>Your Session has expired </H2>");
				out.println("<a href='../examonline/user.html'>Click Here</a> To Re-Login");
				return;
		}		
		Date time_comp=new Date(System.currentTimeMillis()-20*60*1000);
		Date accessed=new Date(session.getLastAccessedTime());
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
		out.println("<p align='center'><strong>&nbsp;</strong></p>");
		out.println("<p align='center'><strong>&nbsp;</strong></p>");
		out.println("<Form method='POST' action='/servlet/UseChngPass2'>");
		out.println("<div align='center'><center><p><strong>Your Old Password:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</strong><input type='password' name='passwd' size='20'></p></center></div>");
		out.println("<div align='center'><center><p><strong>New Password:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</strong><input type='password' name='newpasswd' size='20'></p></center></div>");
		out.println("<div align='center'><center><p><strong>Confirm New Password:&nbsp;</strong><input type='password' name='vnewpasswd' size='20'></p></center></div>");
		out.println("<div align='center'><center><p><input type='submit' value='Submit' name='B1'><input type='reset' value='Reset' name='B2'></p></center></div>");
		out.println("</form></body></html>");
		out.close();
    }
    public String getServletInfo(){
		return "Servlet for Changing User's Password";
	}
}		
								
				