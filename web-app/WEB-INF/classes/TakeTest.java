import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import java.util.*;

public class TakeTest extends HttpServlet{
	private ConnectionPool pool;
	public void init(ServletConfig config) throws ServletException{
		super.init(config);
		try{
			pool=new ConnectionPool(2,1);			
		}
		catch(Exception e){
			throw new UnavailableException(this,"could not create connection pool");
		}
	}
	public void doGet(HttpServletRequest req,HttpServletResponse res) throws ServletException,IOException{
		Connection con=null;
		res.setContentType("text/html");
		res.setHeader("Cache-Control","no-store");		 
		HttpSession session=req.getSession(false); 		
		PrintWriter out=res.getWriter();		
		String username=null,subject_name=null,test_name=null;;
		int test_code=0,i,test_no_subject=0,subject_no_ques=0,test_duration=0;
		if(session==null){
			out.println("<H2>Your session has expired</H2>");
			out.println("<a href='../examonline/user.html'>Click here</a>to Re-Login");
			return;
		}
		java.util.Date time_comp = new java.util.Date(System.currentTimeMillis()-20*60*1000);
		java.util.Date accessed = new java.util.Date(session.getLastAccessedTime());
		if(accessed.before(time_comp)){
			session.invalidate();
			out.println("<H2>Your session has expired</H2>");
			out.println("<a href='../examonline/user.html'>Click here</a>to Re-Login");
			return;
		}
		username=(String)session.getValue("username");
		if(username==null){
			out.println("<H2>Your Session has expired </H2>");
			out.println("<a href='../examonline/user.html'>Click Here</a> To Re-Login");
			return;
		}
		String temp_code=req.getParameter("test_code");
		test_code=Integer.parseInt(temp_code);			
		try{					
			con=pool.getConnection();
			Statement stmt=con.createStatement();
			ResultSet rs=stmt.executeQuery("select * from test_master where test_code="+test_code);
			if(rs.next()){
				test_no_subject=rs.getInt("test_no_subject");
				test_name=rs.getString("test_name");
				test_duration=rs.getInt("test_duration");
			}
			out.println("<html>");
			out.println("<head>");
			out.println("<title>Getting Started With ExamOnline</title>");
			out.println("</head>");
			out.println("<body topmargin='0' leftmargin='0' link='#FFFFFF' vlink='#FFFFFF' alink='#FFFFFF'>");
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
			out.println("<td width='100%'><font face='Verdana' color='#FFFFFF'><small><strong>Test OverView..</strong></small></font></td>");
			out.println("</tr>");
			out.println("</table>");
			out.println("</div><div align='right'>");
			out.println("<table border='1' width='97%' cellspacing='0' cellpadding='0' height='181' bordercolor='#COCOCO' bordercolorlight='#COCOCO'>");
        	out.println("<tr>");
        	out.println("<td width='100%' bgcolor='#F0F0FF' height='181' valign='top' align='center'><table border='0' width='100%' cellspacing='0' cellpadding='0'>");        	
			out.println("<tr>");
            out.println("<td width='100%' height='30'><p align='center'><font face='Verdana' color='#8000FF'><small>&nbsp;&nbsp;&nbsp;You are about to begin the <strong>"+test_name+"</strong> test.</small></font></td>");
            out.println("</tr>");
			out.println("<tr>");
            out.println("<td width='100%' height='30'><p align='left'><font face='Verdana' color='#000000'><small>Your test includes the following test modules:</small></font></td>");
            out.println("</tr>");
            int temp_total=0;			
			for(i=0;i<test_no_subject;i++){
				rs=stmt.executeQuery("select * from test_info where table_name='test_"+test_code+"_"+i+"'");
				if(rs.next()){
					subject_name=rs.getString("subject_name");
					subject_no_ques=rs.getInt("subject_no_ques");					
					temp_total=temp_total+subject_no_ques;
				}
				out.println("<tr>");	
            	out.println("<td width='100%' height='30'><p align='left'><font face='Verdana' color='#8000FF'><small><strong>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+(i+1)+". "+subject_name+" - "+subject_no_ques+" Questions </strong></small></font></td>");
            	out.println("</tr>");
            }
            out.println("<tr>");	
            out.println("<td width='100%' height='30'><p align='left'><font face='Verdana' color='#000000'><small>&nbsp;&nbsp;Please read the following instructions carefully:</small></font></td>");
            out.println("</tr>");
            out.println("<tr>");	
            out.println("<td width='100%' height='30'><p align='left'><font face='Verdana' color='#000000'><small>&nbsp;&nbsp;1.&nbsp;You will be asked a total of "+temp_total+" questions, one at a time.</small></font></td>");
            out.println("</tr>");
            out.println("<tr>");	
            out.println("<td width='100%' height='30'><p align='left'><font face='Verdana' color='#000000'><small>&nbsp;&nbsp;2.&nbsp;Each question is multiple choice with five choices. You must select the  most &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;appropriate answer from the first 4 choices in order to score a correct answer.<br><br></small></font></td>");
            out.println("</tr>");
            out.println("<tr>");	
            out.println("<td width='100%' height='30'><p align='left'><font face='Verdana' color='#000000'><small>&nbsp;&nbsp;3.&nbsp;There will be negative marking of 0.25 marks for each wrong  asnwer. If you don't &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;want to Attempt the question Choose 5th choice.<br><br></small></font></td>");
            out.println("</tr>");
            out.println("<tr>");	
            out.println("<td width='100%' height='30'><p align='left'><font face='Verdana' color='#000000'><small>&nbsp;&nbsp;4.&nbsp;You will have "+test_duration+" minutes to complete the test.The time remaining & time elapsed for &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;test will be displayed along with each question. </small></font></td>");
            out.println("</tr>"); 
            out.println("<tr>");	
            out.println("<td width='100%' height='30'><p align='left'><font face='Verdana' color='#000000'><small>&nbsp;&nbsp;5.&nbsp;If time expires on any question, that question will be taken as unattempted.</small></font></td>");
            out.println("</tr>"); 
            out.println("<tr>");	
            out.println("<td width='100%' height='30'><p align='left'><font face='Verdana' color='#000000'><small>&nbsp;&nbsp;6.&nbsp;Some questions include additional information needed to answer them, such as &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;sample code or variable definitions. This information will be provided in a formatted &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;section just above the question.<br><br></small></font></td>");
            out.println("</tr>");              
            out.println("<tr>");	
            out.println("<td width='100%' height='30'><p align='center'>");
            out.println("<form method='POST' action='/servlet/TakeTest2'>");
            out.println("<input type='hidden' name='test_code' value='"+test_code+"'>");
            out.println("<input type='hidden' name='test_name' value='"+test_name+"'>");
            out.println("<input type='hidden' name='test_no_subject' value='"+test_no_subject+"'>");
            out.println("<input type='hidden' name='test_duration' value='"+test_duration+"'>");
            out.println("<input type='submit' value='Begin Test' name='B1' style='background-color: rgb(196,196,255); color: rgb(0,0,0); border: 1px solid rgb(0,0,128)'>");
            out.println("</form>");
            out.println("</td>");
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
		return "Test Start Page ";
	}
}