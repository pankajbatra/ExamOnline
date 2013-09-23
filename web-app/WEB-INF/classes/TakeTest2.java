import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import java.util.*;

public class TakeTest2 extends HttpServlet{
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
	public void doPost(HttpServletRequest req,HttpServletResponse res) throws ServletException,IOException{
		Connection con=null;
		res.setContentType("text/html");
		res.setHeader("Cache-Control","no-store");		 
		HttpSession session=req.getSession(false); 		
		PrintWriter out=res.getWriter();		
		String username=null,subject_name=null,test_name=null;
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
		test_name=req.getParameter("test_name");
		String temp_no_sub=req.getParameter("test_no_subject");
		test_no_subject=Integer.parseInt(temp_no_sub);
		String temp_duration=req.getParameter("test_duration");
		test_duration=Integer.parseInt(temp_duration);			
		try{					
			con=pool.getConnection();
			Statement stmt=con.createStatement();
			ResultSet rs=null;			
			//Pick question randomly from each section table & store in questions[][] 
			String subject_names[]=new String[test_no_subject];
			int subject_questions[]=new int[test_no_subject];
			for(i=0;i<test_no_subject;i++){
				rs=stmt.executeQuery("select subject_no_ques,subject_name from test_info where table_name='test_"+test_code+"_"+i+"'");
				if(rs.next()){
					subject_no_ques=rs.getInt("subject_no_ques");
					subject_name=rs.getString("subject_name");
					subject_names[i]=subject_name;
					subject_questions[i]=subject_no_ques;
				}
			}
			int questions[][];
			questions=new int[test_no_subject][];
			for(i=0;i<test_no_subject;i++){
				questions[i]=new int[subject_questions[i]];
			}
			Random r=new Random();
			int total_ques=0,ques_c,val,tempvar,id=0;
			boolean exist;
			for(i=0;i<test_no_subject;i++){
				total_ques=0;
				rs=stmt.executeQuery("select count(*) from test_"+test_code+"_"+i);
				while(rs.next()){
					total_ques=rs.getInt("count(*)");
				}
				rs.close();
				val=0;
				tempvar=0;
				for(ques_c=0;ques_c<subject_questions[i];ques_c++){
					exist=true;
					while(exist){
						val=(int)((total_ques+1)*r.nextDouble());
						if(val==0)val=1;
						if(val>total_ques)val=total_ques;
						//val++;
						rs=stmt.executeQuery("select id from test_"+test_code+"_"+i);
						tempvar=0;
						rs.next();
						while(rs.next()&&tempvar<val){
							tempvar++;
							id=rs.getInt("id");
						}
						rs.close();
						exist=false;
						for(int j=0;j<ques_c;j++){
							if(questions[i][j]==id)exist=true;
						}
					}
					questions[i][ques_c]=id;
				}
			}
			//intialize the result for user
			rs=stmt.executeQuery("select * from result where username='"+username+"' AND test_code="+test_code); 
			int tempc=0;
			while(rs.next())tempc++;
			if(tempc==0){
				for(i=0;i<test_no_subject;i++){
					stmt.executeUpdate("Insert into result values('"+username+"',"+test_code+","+(i+1)+", "+subject_questions[i]+",0,0.0,0)"); 
				}
			}
			else{
				for(i=0;i<test_no_subject;i++){
					stmt.executeUpdate("Update result set total="+subject_questions[i]+",attempt=0,score=0.0, time_taken=0 where username='"+username+"'AND test_code="+test_code+" AND test_subject_no="+(i+1)+" ");
				} 
			}
			//now put all info on session
			session.putValue("questions",questions);
			session.putValue("test_no_subject",""+test_no_subject);
			session.putValue("test_duration",""+test_duration);
			session.putValue("test_code",""+test_code);
			session.putValue("test_name",test_name);
			session.putValue("subject_names",subject_names);
			session.putValue("subject_questions",subject_questions);
			session.putValue("time_elapsed","0");
			int attempted[]=new int[test_no_subject];
			for(i=0;i<test_no_subject;i++)attempted[i]=0;
			session.putValue("attempted",attempted);
			//end of all backgroud working		
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
			out.println("<td width='100%'><font face='Verdana' color='#FFFFFF'><small><strong>Test Ready..</strong></small></font></td>");
			out.println("</tr>");
			out.println("</table>");
			out.println("</div><div align='right'>");
			out.println("<table border='1' width='97%' cellspacing='0' cellpadding='0' height='181' bordercolor='#COCOCO' bordercolorlight='#COCOCO'>");
        	out.println("<tr>");
        	out.println("<td width='100%' bgcolor='#F0F0FF' height='181' valign='top' align='center'><table border='0' width='100%' cellspacing='0' cellpadding='0'>");        	
        	out.println("<tr>");	
            out.println("<td width='100%' height='30'><p align='center'><font face='Verdana' color='#8000FF'><small> Test Title: <strong>"+test_name+"</strong> <br><br></small></font></td>");
            out.println("</tr>");
			out.println("<tr>");
            out.println("<td width='100%' height='30'><p align='center'><font face='Verdana' color='#8000FF'><small>Thank you for using ExamOnline Testing Solution.</small></font></td>");
            out.println("</tr>");
            out.println("<tr>");	
            out.println("<td width='100%' height='30'><p align='center'><font face='Verdana' color='#8000FF'><small>You are now ready to begin the test. Please click the Start Test button below to begin the test..<br><br></small></font></td>");
            out.println("</tr>");              
            out.println("<tr>");	
            out.println("<td width='100%' height='30'><p align='center'>");
            out.println("<form method='POST' action='/servlet/NextQuestion'>");
            String table_name="test_"+test_code+"_"+0;
            out.println("<input type='hidden' name='id' value='0'>");
            out.println("<input type='hidden' name='table_name' value='"+table_name+"'>");
            out.println("<input type='hidden' name='test_subject_no' value='1'>");
            out.println("<input type='hidden' name='answer' value='0'>");
            out.println("<input type='hidden' name='next_subject' value='1'>");
            out.println("<input type='submit' value='Start Test' name='B1' style='background-color: rgb(196,196,255); color: rgb(0,0,0); border: 1px solid rgb(0,0,128)'>");
            out.println("</form>");
            out.println("</td>");
            out.println("</tr>");     
			out.println("</table>");
	        out.println("</td>");
            out.println("</tr>");
        	out.println("</table>");	
			out.println("</div>");
			//for testing that session info is rightly stored
			/*questions=(int[][])session.getValue("questions");
			test_no_subject=Integer.parseInt((String)session.getValue("test_no_subject"));
			test_duration=Integer.parseInt((String)session.getValue("test_duration"));
			test_code=Integer.parseInt((String)session.getValue("test_code"));
			test_name=(String)session.getValue("test_name");
			subject_names=(String[])session.getValue("subject_names");
			subject_questions=(int[])session.getValue("subject_questions");
			attempted=(int[])session.getValue("attempted");
			for(i=0;i<test_no_subject;i++){
				out.println("<br>");
				out.println(subject_names[i]);
				out.println("<br>");
				out.println(attempted[i]);
				for(int j=0;j<subject_questions[i];j++){
					out.println("<br>");
					out.println(questions[i][j]);					
				}
			}*/
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