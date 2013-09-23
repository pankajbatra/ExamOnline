import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import java.io.*;
import java.util.*;

public class EndTest extends HttpServlet
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
		HttpSession session=req.getSession(false);
		String username=null;
		int id=0,ans_ch_correct=0;	
		String table_name=null;
		int test_subject_no=0,answer=0,time_elapsed=0,next_subject=0;		
		int questions[][],attempted[],subject_questions[],test_no_subject=0,test_duration=0,test_code=0;
		String test_name=null,subject_names[];
		java.util.Date old_time=null;
		int total_time=0;
		int time_diff=0;
		//String temp_tbl=null;
		//int ques_no=0;	
		int i=0;	
		if(session==null){
			out.println("<H2>Your Session has expired </H2>");
			out.println("<a href='../examonline/user.html'>Click Here</a> To Re-Login");
			return;
		}		
		java.util.Date time_comp=new java.util.Date(System.currentTimeMillis()-60*60*1000);
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
		try{
			con=pool.getConnection();
			Statement stmt=con.createStatement();
			ResultSet rs=null;
			//reterive all session info
			questions=(int[][])session.getValue("questions");
			test_no_subject=Integer.parseInt((String)session.getValue("test_no_subject"));
			test_duration=Integer.parseInt((String)session.getValue("test_duration"));
			test_code=Integer.parseInt((String)session.getValue("test_code"));
			test_name=(String)session.getValue("test_name");
			subject_names=(String[])session.getValue("subject_names");
			subject_questions=(int[])session.getValue("subject_questions");
			attempted=(int[])session.getValue("attempted");
			old_time=(java.util.Date)session.getValue("old_time");
			String temp_time_elapsed=(String)session.getValue("time_elapsed");
			time_elapsed=Integer.parseInt(temp_time_elapsed);
			//reterive all form data from previous page
			String temp_id=req.getParameter("id");
			id=Integer.parseInt(temp_id);
			table_name=req.getParameter("table_name");
			String temp_sub_no=req.getParameter("test_subject_no");
			test_subject_no=Integer.parseInt(temp_sub_no);
			String temp_answer=req.getParameter("answer");
			answer=Integer.parseInt(temp_answer);
			//String temp_next_subject=req.getParameter("next_subject");
			//next_subject=Integer.parseInt(temp_next_subject);
			total_time=(test_duration*60);
			//update time_taken
			old_time=(java.util.Date)session.getValue("old_time");
			java.util.Date new_time=new java.util.Date();
			time_diff=(int)((new_time.getTime()-old_time.getTime())/1000);				
			time_elapsed+=time_diff;
			//time exceeds
			if(time_elapsed>total_time){
				out.println("Test Ended:Your total Time has elapsed");
		    	out.println("<p align='center'><a href='/servlet/EndTest'>Click Here</a>to View result</p>");//end test here		       		
		    	return;	    		
			}
				
			//update result
			/*int time_update=0;
			time_update=(time_elapsed/60);
			time_diff=time_elapsed-(60*time_update);
			if(time_diff>30)time_update++;*/
			int time_update=0;
			time_update=(time_diff/60);
			time_diff=time_diff-(60*time_update);
			if(time_diff>30)time_update++;
			rs=stmt.executeQuery("select ans_ch_correct from "+table_name+" where id="+id);
			int anstemp=0;
			if(rs.next())anstemp=rs.getInt("ans_ch_correct");
			if(anstemp==answer){
				stmt.executeUpdate("Update result set attempt=attempt+1,time_taken=time_taken+"+time_update+", score=score+1.0 where username='"+username+"' AND test_code="+test_code+" AND test_subject_no="+test_subject_no+" ");
			}
			else if(answer==0){
				stmt.executeUpdate("Update result set attempt=attempt+1,time_taken=time_taken+"+time_update+" where username='"+username+"' AND test_code="+test_code+" AND test_subject_no="+test_subject_no+" ");
			}
			else{									
				stmt.executeUpdate("Update result set attempt=attempt+1,time_taken=time_taken+"+time_update+",score=score-0.25 where username='"+username+"' AND test_code="+test_code+" AND test_subject_no="+test_subject_no+" ");
			}
			//return page
			out.println("<html>");
			out.println("<head>");
			out.println("<title>Getting Started With ExamOnline</title>");
			out.println("</head>");
			out.println("<body topmargin='0' leftmargin='0' link='#FFFFFF' vlink='#FFFFFF' alink='#FFFFFF'>");
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
			out.println("<td width='100%'><font face='Verdana' color='#FFFFFF'><small><strong>Test Ended..</strong></small></font></td>");
			out.println("</tr>");
			out.println("</table>");
			out.println("</div><div align='right'>");						
			out.println("<table border='1' width='97%' cellspacing='0' cellpadding='0' height='181' bordercolor='#COCOCO' bordercolorlight='#COCOCO'>");
        	out.println("<tr>");
        	out.println("<td width='100%' bgcolor='#F0F0FF' height='181' valign='top' align='center'><table border='0' width='100%' cellspacing='0' cellpadding='0'>");
			out.println("<tr>");
            out.println("<td width='100%' height='30'><p align='center'><font face='Verdana' color='#8000FF'><small>Your Test has Ended</small></font></td>");
            out.println("</tr>");
			out.println("<tr>");
            out.println("<td width='100%' height='30'><p align='center'><font face='Verdana' color='#8000FF'><small><a href='/servlet/ScoreCard' ><font color='#000000'>Click Here</font></a> to View Scores</small></font></td>");
            out.println("</tr>");
        	out.println("</table>");
	        out.println("</td>");
            out.println("</tr>");
        	out.println("</table>");
    	    out.println("</div>");
	        out.println("</body>");
        	out.println("</html>"); 
		    rs.close();	
		    stmt.close();	    			
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
    public void doGet(HttpServletRequest req,HttpServletResponse res)
	throws ServletException,IOException
	{
		Connection con=null;
		res.setContentType("text/html");
		PrintWriter out = res.getWriter();
		res.setHeader("Cache-Control","no-store");
		HttpSession session=req.getSession(false);
		String username=null;
		if(session==null){
			out.println("<H2>Your Session has expired </H2>");
			out.println("<a href='../examonline/user.html'>Click Here</a> To Re-Login");
			return;
		}		
		java.util.Date time_comp=new java.util.Date(System.currentTimeMillis()-60*60*1000);
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
		//return page
		out.println("<html>");
		out.println("<head>");
		out.println("<title>Getting Started With ExamOnline</title>");
		out.println("</head>");
		out.println("<body topmargin='0' leftmargin='0' link='#FFFFFF' vlink='#FFFFFF' alink='#FFFFFF'>");
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
		out.println("<td width='100%'><font face='Verdana' color='#FFFFFF'><small><strong>Test Ended..</strong></small></font></td>");
		out.println("</tr>");
		out.println("</table>");
		out.println("</div><div align='right'>");						
		out.println("<table border='1' width='97%' cellspacing='0' cellpadding='0' height='181' bordercolor='#COCOCO' bordercolorlight='#COCOCO'>");
        out.println("<tr>");
        out.println("<td width='100%' bgcolor='#F0F0FF' height='181' valign='top' align='center'><table border='0' width='100%' cellspacing='0' cellpadding='0'>");
		out.println("<tr>");
        out.println("<td width='100%' height='30'><p align='center'><font face='Verdana' color='#8000FF'><small>Your Test has Ended</small></font></td>");
        out.println("</tr>");
		out.println("<tr>");
        out.println("<td width='100%' height='30'><p align='center'><font face='Verdana' color='#8000FF'><small><a href='/servlet/ScoreCard' ><font color='#000000'>Click Here</font></a> to View Scores</small></font></td>");
        out.println("</tr>");
        out.println("</table>");
	    out.println("</td>");
        out.println("</tr>");
        out.println("</table>");
    	out.println("</div>");
	    out.println("</body>");
        out.println("</html>"); 
    } 
    public String getServletInfo(){
		return "This servlet Delivers next question to test taker";
	}
}