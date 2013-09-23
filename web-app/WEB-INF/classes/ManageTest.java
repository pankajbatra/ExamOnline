import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;
import java.util.*;

public class ManageTest extends HttpServlet{
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
		Connection con = null;
		res.setContentType("text/html");
		res.setHeader("Cache-Control","no-store");
		PrintWriter out=res.getWriter();
		HttpSession session=req.getSession(false);
		int counter=0,test_code=0,subject_no_ques=0;
		String ad_user=null,table_name=null,subject_name=null;
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
			String tcode = req.getParameter("test_code");
			test_code = Integer.parseInt(tcode);
			Statement stmt = con.createStatement();
			ResultSet rs=stmt.executeQuery("select * from test_master where aduser='"+ad_user+"' AND test_code="+test_code);
			int c=0;
			counter=0;
			while(rs.next()){
			 	c++;
			 	counter= rs.getInt("test_no_subject");
			}
			if(c==0){
				session.invalidate();
		    	out.println("<H2>Your Session has expired </H2>");
				out.println("<a href='../examonline/admin.html'>Click Here</a> To Re-Login");
				return;
			}
			int i=0;
			String s[] = new String[counter];
			for(i=0;i<counter;i++){
				s[i]="test_"+test_code+"_"+i;
			}
			int q[] = new int[counter];
			for(i=0;i<counter;i++){				
				c=0;
				rs = stmt.executeQuery("select COUNT(*) from "+s[i]);
				while(rs.next()){
			 		c=rs.getInt("COUNT(*)");
				}
				q[i]=c;
			}
		    i=0;
			out.println("<html>");
			out.println("<head>");
			out.println("<title>Getting Started With ExamOnline</title>");
			out.println("</head>");
			out.println("<body topmargin='0' leftmargin='0' link='#FFFFFF' vlink='#FFFFFF' alink='#FFFFFF'>");
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
			out.println("<table border='1' width='97%' bgcolor='#C0C0C0' bordercolor='#C0C0C0' cellspacing='0' cellpadding='0' bordercolorlight='#C0C0C0'>");
			out.println("<tr>");
			out.println("<td width='100%'><font face='Verdana' color='#FFFFFF'><small><strong>Manage Test</strong></small></font></td>");
			out.println("</tr>");
			out.println("</table>");
			out.println("</div><div align='right'>");
			out.println("<table border='1' width='97%' bordercolor='#C0C0C0' cellspacing='0' cellpadding='0' bordercolorlight='#C0C0C0' bordercolordark='#FFFFFF' bgcolor='#F0F0F0'>");
			out.println("<tr>");
			out.println("<td width='25%'><p align='center'><small><small><font face='Verdana'>Subject Name</font></small></small></td>");
			out.println("<td width='25%'><p align='center'><small><small><font face='Verdana'>No. Of Questions Present</font></small></small></td>");
			out.println("<td width='27%'><p align='left'><small><small><font face='Verdana'>Minimum No.Of Questions</font></small></small></td>");
			out.println("<td width='25%'><p align='center'><small><small><font face='Verdana'>Actions</font></small></small></td>");
			out.println("</tr>");			
		    for(i=0;i<counter;i++){
		    	rs = stmt.executeQuery("select * from test_info where table_name ='"+s[i]+"'");
		    	if(rs.next()){
		    		subject_name = rs.getString("subject_name");
					subject_no_ques = rs.getInt("subject_no_ques");
				}				
				out.println("</table>");
				out.println("</div><div align='right'>");
				out.println("<table border='1' width='97%' bordercolor='#C0C0C0' cellspacing='0' cellpadding='0' bordercolorlight='#C0C0C0' bordercolordark='#FFFFFF' bgcolor='#F0F0FF' height='20'>");
				out.println("<tr>");
				out.println("<td width='25%' valign='middle'><p align='left'><small><small><strong><font face='Verdana' color='#8000FF'>"+subject_name+"</strong></font></small></small></td>");
				out.println("<td width='25%' valign='middle'><p align='center'><small><small><font face='Verdana'>"+q[i]+"</font></small></small></td>");
				out.println("<td width='27%' valign='middle'><p align='center'><small><small><font face='Verdana'>"+subject_no_ques+"</font></small></small></td>");
				out.println("<td width='25%' valign='top' bgcolor='#FFFFFF'><p align='left'>&nbsp;");
				out.println("<a href='/servlet/AddQuestion?table_name="+s[i]+"' target='_self'><img src='../examonline/images/pages.gif' width='16' height='16' alt='Add Questions To This Subject.' align='top'></a>");
				out.println("&nbsp;&nbsp; <a href='/servlet/EditQuestion?table_name="+s[i]+"' target='_self'><img src='../examonline/images/edit_1.gif' width='16' height='16' alt='Edit Questions.' align='top'></a>");
				out.println("&nbsp;&nbsp; <a href='/servlet/ViewQuestion?table_name="+s[i]+"&min=1&flag=0' target='_self'><img src='../examonline/images/view.gif' width='16' height='16' alt='View Questions.' align='top'></a>");
				out.println(" &nbsp;&nbsp;<a href='/servlet/DeleteQuestion?table_name="+s[i]+"&min=1' target='_self'><img src='../examonline/images/D.gif' width='16' height='16' alt='Delete Questions.' border='0' align='middle'></a></td>");
				out.println("</tr>");
				out.println("</table>");
				out.println("</div>");
			}
			out.println("</body>");
			out.println("</html>");
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
			out.close();		
		}
	}
	public String getServletInfo(){
		return "Servlet to manage a test";
	}	
}