import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class InsertChngAcc extends HttpServlet{
	private ConnectionPool pool;
	public void init(ServletConfig config) throws ServletException
	{
		super.init(config);
		try{
			pool=new ConnectionPool(1,1);
		}
		catch(Exception e)
		{
			throw new UnavailableException(this,"could not create connection pool");
		}
	}	
	public void doPost(HttpServletRequest req,HttpServletResponse res) throws ServletException,IOException{
		Connection con=null;
		res.setContentType("text/html");
		PrintWriter out=res.getWriter();
		res.setHeader("Cache-Control","no-store"); 
		String name,middle,last,username,address,city,state,country,pin,dob,sex,tele,email;
		HttpSession session = req.getSession(false);
		try{			
			if(session==null)
			{
				out.println("<H2>Your session has expired</H2>");
				out.println("<a href='../examonline/user.html'>Click here</a>to Re-Login");
				return;
			}
			java.util.Date time_comp = new java.util.Date(System.currentTimeMillis()-20*60*1000);
			java.util.Date accessed = new java.util.Date(session.getLastAccessedTime());
			if(accessed.before(time_comp))
			{
				session.invalidate();
				out.println("<H2>Your session has expired</H2>");
				out.println("<a href='../examonline/user.html'>Click here</a>to Re-Login");
				return;
			}			
			username = (String)session.getValue("username");
			name=req.getParameter("name");
			middle=req.getParameter("middle");
			last=req.getParameter("last");
			address=req.getParameter("address");
			city=req.getParameter("city");
			state=req.getParameter("state");
			country=req.getParameter("country");
			pin=req.getParameter("pin");
			dob=req.getParameter("dob");
			sex=req.getParameter("sex");
			tele=req.getParameter("tele");
			email=req.getParameter("email");
			con=pool.getConnection();
			Statement stmt=con.createStatement();
			stmt.executeUpdate("update users set name='"+name+"',middle='"+middle+"',last='"+last+"',address='"+address+"',city='"+city+"',state='"+state+"',country='"+country+"',pin="+pin+",dob='"+dob+"',sex="+sex+",tele="+tele+",email='"+email+"' where username='"+username+"'");			
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
			out.println("<td width='100%'><font face='Verdana' color='#FFFFFF'><small><strong>Account Information Changed Successfully..</strong></small></font></td>");
			out.println("</tr>");
			out.println("</table>");
			out.println("</div><div align='right'>");			
			out.println("<table border='1' width='97%' cellspacing='0' cellpadding='0' height='181' bordercolor='#COCOCO' bordercolorlight='#COCOCO'>");
        	out.println("<tr>");
        	out.println("<td width='100%' bgcolor='#F0F0FF' height='181' valign='top' align='center'><table border='0' width='100%' cellspacing='0' cellpadding='0'>");
			out.println("<tr>");
            out.println("<td width='100%' height='30'><p align='center'><font face='Verdana' color='#8000FF'><small>Account Information has been Sucessfully Changed</small></font></td>");
            out.println("</tr>");
			out.println("<tr>");
            out.println("<td width='100%' height='30'><p align='center'><font face='Verdana' color='#8000FF'><small><a href='/servlet/UserControlMiddle' ><font color='#000000'>Click Here</font></a> to go back to Home Page</small></font></td>");
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
				out.println("<a href='javascript:history.go(-2)'>Click Here</a> to go back & Try Again");
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
		return "This servlet Changes Account information for user";
	}
}
			