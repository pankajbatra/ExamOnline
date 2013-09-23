import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import java.io.*;
import java.util.*;

public class DeletedQuestion extends HttpServlet{
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
	throws ServletException,IOException{
		Connection con=null;
		res.setContentType("text/html");
		PrintWriter out = res.getWriter();
		res.setHeader("Cache-Control","no-store");
		int id=0,image=0,ans_ch_correct=0; 
		int ques_number=0;
		String ad_user=null,table_name=null,table_question=null,table_statement=null,table_code=null,ans_ch_1=null,ans_ch_2=null,ans_ch_3=null,ans_ch_4=null;
		HttpSession session=req.getSession(false);				
		if(session==null){
			out.println("<H2>Your Session has expired </H2>");
			out.println("<a href='../examonline/admin.html'>Click Here</a> To Re-Login");
			return;
		}		
		java.util.Date time_comp=new java.util.Date(System.currentTimeMillis()-20*60*1000);
		java.util.Date accessed=new java.util.Date(session.getLastAccessedTime());
		if(accessed.before(time_comp)){
			session.invalidate();
			out.println("<H2>Your Session has expired </H2>");
			out.println("<a href='../examonline/admin.html'>Click Here</a> To Re-Login");
			return;
		}  		
		ad_user=(String)session.getValue("aduser");
		if(ad_user==null){
			out.println("<H2>Your Session has expired </H2>");
			out.println("<a href='../examonline/admin.html'>Click Here</a> To Re-Login");
			return;
		}		
		try{
			String ques_no=req.getParameter("id");
			if(ques_no==null){
				out.println("<H2>Qusetion Number Field was left blank</H2>");
				out.println("<a href='javascript:history.go(-1)'>Click Here</a>  to go back to previous page & try again");
				return;
			}
			if(ques_no.equals("")){
				out.println("<H2>Qusetion Number Field was left blank</H2>");
				out.println("<a href='javascript:history.go(-1)'>Click Here</a> to go back to previous page & try again");
				return;
			}
			id=Integer.parseInt(ques_no);	
			table_name = req.getParameter("table_name");
			con=pool.getConnection();
			Statement stmt=con.createStatement();
			ResultSet rs=null;
			//stmt.executeQuery("select * from "+table_name+" where id="+ques_number+"");
			int c=0;
			//while(rs.next()){
			//	c++;
				//id=rs.getInt("id");
			//}
			//rs.close();
			int affect=0;
			//if(c>0){
				affect=stmt.executeUpdate("delete from "+table_name+" where id="+id);
				stmt.executeUpdate("delete from question_images where table_name='"+table_name+"' AND id="+id);
				//out.println("success");
			//}
			//else
			//out.println("fail");
			if(affect==0){
				//no such id
	            out.println("<center><p><strong>Invalid Question ID</strong>");
				out.println("<a href='javascript:history.go(-1)'>Click Here</a>  to go back to previous page & try again");
				return;
			}
			else{
				//success
				out.println("<html>");
				out.println("<head>");
				out.println("<title>Getting Started With ExamOnline</title>");
				out.println("</head>");
				out.println("<body topmargin='0' leftmargin='0' link='#FFFFFF' vlink='#FFFFFF' alink='#FFFFFF'>");
				out.println("<table border='0' width='100%' cellspacing='0' cellpadding='0' bgcolor='#000000' height='24'>");
				out.println("<tr>");
				out.println("<td width='42%' height='24'><font face='Verdana' color='#FFFFFF'><small>|<a href='../examonline/getting_started.htm' target='_self'>Getting Started With ExamOnline</a></small></font></td>");
        		out.println("<td width='28%' height='24'><font face='Verdana' color='#FFFFFF'><small>| <a href='../examonline/guidelines.html' target='_self'>Test GuideLines</a></small></font></td>");
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
        		out.println("<td width='100%'><font face='Verdana' color='#FFFFFF'><small><strong>Question Deleted Successfully...</strong></small></font></td>");
        		out.println("</tr>");
        		out.println("</table>");
        		out.println("</div><div align='right'>");
        		out.println("<table border='1' width='97%' cellspacing='0' cellpadding='0' height='181' bordercolor='#COCOCO' bordercolorlight='#COCOCO'>");
        		out.println("<tr>");
        		out.println("<td width='100%' bgcolor='#F0F0FF' height='181' valign='top' align='center'><table border='0' width='100%' cellspacing='0' cellpadding='0'>");
	        	out.println("<tr>");
        		out.println("<td width='100%' height='30'><p align='center'><small><font face='Verdana' color='#8000FF'><strong>");
        		out.println("</strong></font><font face='Verdana' color='#6767B4'>The question has been successfully Deleted.</font></small></td>");
        		out.println("</tr>");
    	    	out.println("<tr>");
	        	out.println("<td width='100%' height='30'><p align='center'><a href='/servlet/AdminControlMiddle' target='_self'><font face='Verdana' color='#6767B4'><small>Home</small></font></a></td>");
	        	out.println("</tr>");
    	    	out.println("</table>");
        		out.println("</td>");
        		out.println("</tr>");
        		out.println("</table>");
        		out.println("</div>");
        		out.println("</body>");
        		out.println("</html>");
			}		
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
		return "This servlet Show all fields of question for editing";
	}	
}