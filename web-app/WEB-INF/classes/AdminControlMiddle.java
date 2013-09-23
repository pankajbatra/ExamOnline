import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;
import java.sql.*;

public class AdminControlMiddle extends HttpServlet{
	private ConnectionPool pool;
	public void init(ServletConfig config)throws ServletException{
		super.init(config);
		try{
			pool = new ConnectionPool(1,1);
		}
		catch(Exception e){
			throw new UnavailableException(this,"could not create connection pool");
		} 
    }
	public void doGet(HttpServletRequest req,HttpServletResponse res)
	throws ServletException,IOException
	{
		Connection con = null;
		res.setContentType("text/html");
		res.setHeader("Cache-Control","no-store");
		PrintWriter out=res.getWriter();
		HttpSession session=req.getSession(false);
		String ad_user;
		ad_user = (String)session.getValue("aduser");
		java.util.Date time_comp=new java.util.Date(System.currentTimeMillis()-20*60*1000);
		java.util.Date accessed=new java.util.Date(session.getLastAccessedTime());
		if(session==null||ad_user==null||accessed.before(time_comp)){
			    session.invalidate();
			    out.println("<H2>Your Session has expired </H2>");
				out.println("<a href='../examonline/admin.html'>Click Here</a> To Re-Login");
				return;
		}	
		try{
			con = pool.getConnection();
			Statement stmt = con.createStatement();
			out.println("<html>");
			out.println("<head>");
			out.println("<title>Getting Started With ExamOnline</title>");
			out.println("</head>");
			out.println("<body topmargin='0' leftmargin='0' link='#ffffff' vlink='#ffffff' alink='#ffffff' >");
			out.println("<table border='0' width='100%' cellspacing='0' cellpadding='0' bgcolor='#000000' height='24'>");
			out.println("<tr>");
			out.println("<td width='42%' height='24'><font face='Verdana' color='#FFFFFF'><small>|<a href='../examonline/getting_started.htm' target='_self'>Getting Started With ExamOnline</a></small></font></td>");
			out.println("<td width='28%' height='24'><font face='Verdana' color='#FFFFFF'><small>| <a href='../examonline/configure.htm' target='_self'>Configure ExamOnline</a></small></font></td>");
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
			out.println("</div><div align='right'>");
			out.println("<table border='1' width='100%' bgcolor='#C0C0C0' bordercolor='#C0C0C0' cellspacing='0' cellpadding='0' bordercolorlight='#C0C0C0'>");
			out.println("<tr>");
			out.println("<td width='97%'><small><font face='Verdana' color='#FFFFFF'><strong>Available Tests</strong></font></small></td>");
			out.println("</tr>");
			out.println("</table>");
			out.println("</div><div align='right'>");
			out.println("<table border='1' width='100%' cellspacing='0' cellpadding='0' bordercolor='#C0C0C0' bordercolorlight='#C0C0C0' bordercolordark='#FFFFFF' bgcolor='#F3F3F3' height='20'>");
			out.println("<tr>");
			out.println("<td width='20%' bgcolor='#F3F3F3'><small><small><font face='Verdana'>Actions</font></small></small></td>");
			out.println("<td width='25%'><small><small><font face='Verdana'>Test Name</font></small></small></td>");
			out.println("<td width='5%'><small><small><font face='Verdana'>Test Code</font></small></small></td>");
        	out.println("<td width='15%'><small><small><font face='Verdana'>Created On</font></small></small></td>");
        	out.println("<td width='15%'><small><small><font face='Verdana'>Updated On</font></small></small></td>");
        	out.println("<td width='12%'><small><small><font face='Verdana'>Test Duration</font></small></small></td>");
        	out.println("<td width='12%'><small><small><font face='Verdana'>Test Status</font></small></small></td>");
        	out.println("</tr>");
        	out.println("</table>");
        	out.println("</div>");
            ResultSet rs = stmt.executeQuery("Select * from test_master where aduser='"+ad_user+"'");
       		int i,c=0;
        	while(rs.next()){
        		c++;
        		int test_code = rs.getInt("test_code");
        		String test_name = rs.getString("test_name");        		
        		java.sql.Date test_created = rs.getDate("test_created");
        		java.sql.Date test_updated = rs.getDate("test_updated");
        		int test_duration = rs.getInt("test_duration");
        		int test_status = rs.getInt("test_status");
        		if(test_status!=2){
        			out.println("<div align='right'>");
        			out.println("<table border='1' width='100%' cellspacing='0' cellpadding='0' bgcolor='#F0F0FF' bordercolor='#C0C0C0' bordercolorlight='#C0C0C0' bordercolordark='#FFFFFF'>");
        			out.println("<tr>");
        			out.println("<td width='20%' valign='middle' align='center' bgcolor='#FFFFFF'>");
        			//out.println("<a href='/servlet/EditTest?test_code="+test_code+"' target='_self'><img src='../examonline/images/edit.gif' width='16' height='16' alt='Edit Test.'></a>");
        			out.println("<a href='/servlet/StartTest?test_code="+test_code+"' target='_self'><img src='../examonline/images/start.gif' width='16' height='16' alt='Start Test.'></a> ");
        			out.println("<a href='/servlet/StopTest?test_code="+test_code+"' target='_self'><img src='../examonline/images/stop.gif' width='16' height='16' alt='Stop Test.'></a>");
        			out.println("<a href='/servlet/DeleteTest?test_code="+test_code+"' target='_self'><img src='../examonline/images/delete.gif' width='16' height='16' alt='Delete Test.'></a>");
        			out.println("<a href='/servlet/ManageTest?test_code="+test_code+"' target='_self'><img src='../examonline/images/restart.gif' width='16' height='16' alt='Add,Edit or Delete Questions.'></a></td>");
        			out.println("<td width='25%'valign='middle'><font face='Verdana' color='#8000FF'><small><small><strong>"+test_name+"</strong></small></small></font></td>");
        			out.println("<td width='5%'valign='middle'><font face='Verdana' color='#8000FF'><small><small>"+test_code+"</small></small></font></td>");
        			out.println("<td width='15%'valign='middle'><font face='Verdana' color='#8000FF'><small><small>"+test_created+"</small></small></font></td>");
        			out.println("<td width='15%'valign='middle'><font face='Verdana' color='#8000FF'><small><small>"+test_updated+"</small></small></font></td>");
        			out.println("<td width='12%'valign='middle'><font face='Verdana' color='#8000FF'><small><small>"+test_duration+"&nbsp;min.</small></small></font></td>");
        			if(test_status==1){
	        			out.println("<td width='12%'valign='middle'><font color='#00A400' face='Verdana'><small><small><strong>Active</strong></small></small></font></td>");
				    }
        			else out.println("<td width='12%'valign='middle'><font face='Verdana' color='#FF0000'><small><small><strong>Inactive</strong></small></small></font></td>");
        			out.println("</tr>");
        			out.println("</table>");
        			out.println("</div>");
        		}        		
        	}
        	out.println("</body>");
        	out.println("</html>");
        	rs.close();
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
		return "Administrator control panel middle frame";
	}
}
