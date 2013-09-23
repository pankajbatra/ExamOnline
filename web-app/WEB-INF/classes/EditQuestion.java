import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;
import java.util.*;

public class EditQuestion extends HttpServlet{
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
	public void doGet(HttpServletRequest req,HttpServletResponse res)
	throws ServletException,IOException{
		res.setContentType("text/html");
		res.setHeader("Cache-Control","no-store");
		PrintWriter out=res.getWriter();
		HttpSession session=req.getSession(false);
		String ad_user=null,table_name=null;
		Connection con=null;
		ad_user = (String)session.getValue("aduser");
		java.util.Date time_comp=new java.util.Date(System.currentTimeMillis()-20*60*1000);
		java.util.Date accessed=new java.util.Date(session.getLastAccessedTime());
		if(session==null||ad_user==null||accessed.before(time_comp)){
			session.invalidate();
			out.println("<H2>Your Session has expired </H2>");
			out.println("<a href='../examonline/admin.html'>Click Here</a> To Re-Login");
			return;
		}
		table_name = req.getParameter("table_name");
		try{
			con = pool.getConnection();	
			int test_code=0;
			Statement stmt = con.createStatement();
			ResultSet rs=stmt.executeQuery("select * from test_info where table_name='"+table_name+"'");
			if(rs.next()) test_code=rs.getInt("test_code");
			rs=stmt.executeQuery("select * from test_master where aduser='"+ad_user+"' AND test_code="+test_code);
			int c=0;
			while(rs.next()){
			 	c++;
			}
			if(c==0){
				session.invalidate();
		    	out.println("<H2>Your Session has expired </H2>");
				out.println("<a href='../examonline/admin.html'>Click Here</a> To Re-Login");
				rs.close();
				stmt.close();
				return;
			}			
			rs.close();
			stmt.close();
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
		}				
		out.println("<html>");
		out.println("<head>");
		out.println("<title>Getting Started With ExamOnline</title>");
		out.println("</head>");
		out.println("<body topmargin='0' leftmargin='0' link='#000000' vlink='#000000' alink='#000000'>");
		out.println("<table border='0' width='100%' cellspacing='0' cellpadding='0' bgcolor='#000000' height='24'>");
		out.println("<tr>");
		out.println("<td width='42%' height='24'></td>");
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
		out.println("<td width='100%' colspan='2'><strong><small><font face='Verdana'>Welcome To ExamOnline Control Panel</font></small></strong></td>");
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
		out.println("<td width='97%'><font face='Verdana' color='#FFFFFF'><small><strong>Checking status...</strong></small></font></td>");
		out.println("</tr>");
		out.println("</table>");
		out.println("</div><div align='right'>");
		out.println("<table border='1' width='97%' cellspacing='0' cellpadding='0' height='181' bordercolor='#COCOCO' bordercolorlight='#COCOCO'>");
		out.println("<tr>");
		out.println("<td width='100%' bgcolor='#F0F0FF' height='181' valign='top' align='center'><table border='0' width='100%' cellspacing='0' cellpadding='0' height='115'>");
		out.println("<tr>");
		out.println("<td width='42%' height='30' align='right'><p align='center'><font face='Verdana' color='#004040'><strong><small>Edit a single question</small></strong></font></td>");
		out.println("<td width='52%' height='30'><p align='center'>&nbsp; <strong><font face='Verdana' color='#8000FF'><small>Select the question from the database</small></font></strong></td>");
		out.println("</tr>");
		out.println("<tr>");
		out.println("<td width='42%' height='25' align='right'><p align='center'><font face='Verdana' color='#004000'><small>Enter the question ID:</small></font></td>");
		out.println("<td width='52%' height='25'><p align='center'><a href='/servlet/EditQuesData?table_name="+table_name+"&min=1' target='_self'><font face='Verdana' color='#8000FF'><strong><small>Click Here...</small></strong></font></a></td>");
		out.println("</tr>");
		out.println("<tr>");
		out.println("<td width='42%' height='60' align='left'><form method='POST' action='/servlet/EditQuesSingle' <div align='center'><center><p>&nbsp;<input type='text' name='id' size='14' style='border: 1px solid rgb(0,0,0)'><br>");
		out.println("<INPUT TYPE='hidden' NAME='table_name' VALUE='"+table_name+"'>");
		out.println("<input type='submit' value='Submit' name='B1' style='background-color: rgb(238,238,238); color: rgb(0,0,0); border: 1px solid rgb(0,0,0)'></p>");
		out.println("</center></div>");
		out.println("</form>");
		out.println("</td>");
		out.println("<td width='52%' height='60'></td>");
		out.println("</tr>");
		out.println("</table>");
		out.println("</td>");
		out.println("</tr>");
		out.println("</table>");
		out.println("</div>");
		out.println("</body>");
		out.println("</html>");  
        out.close();
	}
	

	public String getServletInfo(){
		return "Servlet to edit a test";
	}	
}