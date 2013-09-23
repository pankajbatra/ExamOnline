import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import java.io.*;
import java.util.*;

public class TestCenter extends HttpServlet
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
	public void doGet(HttpServletRequest req,HttpServletResponse res)
	throws ServletException,IOException
	{
		Connection con=null;
		res.setContentType("text/html");
		PrintWriter out = res.getWriter();
		res.setHeader("Cache-Control","no-store"); 
		String username=null;
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
						
			con=pool.getConnection();
			Statement stmt=con.createStatement();			
			//int count=0;
			ResultSet rs=null;
			//stmt.executeQuery("select test_code from test_master where test_status=1");
			//while(rs.next())
			//{
			 //count++;
			//}			
			out.println("<html>");
			out.println("<head>");
			out.println("<title>Getting Started With ExamOnline</title>");
			out.println("</head>");
			out.println("<body topmargin='0' leftmargin='0' link='#FFFFFF' vlink='#FFFFFF' alink='#FFFFFF'>");
			out.println("<table border='0' width='100%' cellspacing='0' cellpadding='0' bgcolor='#000000' height='24'>");
			out.println("<tr>");
			out.println("<td width='42%' height='24'><font face='Verdana' color='#FFFFFF'><small>|<a href='../examonline/user_getting_started.htm' target='_self'>Getting Started With ExamOnline</a></small></font></td>");
			out.println("<td width='28%' height='24'><font face='Verdana' color='#FFFFFF'><small></small></font></td>");
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
			out.println("<td width='100%' colspan='2'><strong><small><font face='Verdana'>Welcome To ExamOnline Contol Panel</font></small></strong></td>");
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
			out.println("</div><div align='center'><center>");
			out.println("<table border='1' width='60%' bgcolor='#C0C0C0' bordercolor='#C0C0C0' cellspacing='0' cellpadding='0' bordercolorlight='#C0C0C0'>");
			out.println("<tr>");
			out.println("<td width='100%'><small><font face='Verdana' color='#FFFFFF'><strong>Available Tests</strong></font></small></td>");
			out.println("</tr>");
			out.println("</table>");
			out.println("</center></div><div align='center'><center>");
			out.println("<table border='1' width='60%' cellspacing='0' cellpadding='0' bordercolor='#C0C0C0' bordercolorlight='#C0C0C0' bordercolordark='#FFFFFF' bgcolor='#F3F3F3' height='20'>");
			out.println("<tr>");
			out.println("<td width='8%' bgcolor='#F3F3F3' align='center'><small><small><font face='Verdana'>S.No.</font></small></small></td>");
			out.println("<td width='45%' align='center'><small><small><font face='Verdana'>Test Name</font></small></small></td>");
			out.println("<td width='16%' align='center'><small><small><font face='Verdana'>Test Duration</font></small></small></td>");
			out.println("<td width='9%' align='center'><small><small><font face='Verdana'>Action</font></small></small></td>");
			out.println("</tr>");
			out.println("</table>");			
			//int s[] = new int[count];
			int i=0;
		    rs=stmt.executeQuery("select test_name,test_code,test_duration from test_master where test_status=1");
			while(rs.next())
			{
			 //s[i] = rs.getInt("test_code");	
			 //i++;
			 //test_name = rs.getString("test_name");
			 //test_duration = rs.getInt("test_duration");
			 //if(rs.getInt("test_status")==1)
			 //{
			 out.println("</center></div><div align='center'><center>");
			 out.println("<table border='1' width='60%' cellspacing='0' cellpadding='0' bgcolor='#F0F0FF' bordercolor='#C0C0C0' bordercolorlight='#C0C0C0' bordercolordark='#FFFFFF'>");
			 out.println("<tr>");
			 out.println("<td width='8%' valign='middle' align='center'><small><small><font face='Verdana'>"+(i+1)+".</font></small></small></td>");
			 out.println("<td width='45%'><font face='verdana'><small><small>"+rs.getString("test_name")+"</small></small></font></td>");
			 out.println("<td width='16%' align='center'><font face='Verdana' color='#000000'><small><small>"+rs.getInt("test_duration")+" min.</small></small></font></td>");
			 out.println("<td width='9%' bgcolor='#FFFFFF' align='center'><a href='/servlet/TakeTest?test_code="+rs.getInt("test_code")+"' target='_self'><img src='../examonline/images/start.gif' width='16' height='16' alt='Start Test'></a></td>");
			 out.println("</tr>");
			 out.println("</table>");
			 i++;			 
			}
			rs.close();			
			out.println("</center></div>");
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
		return "This servlet is test Center";
	}	
}		
								
				