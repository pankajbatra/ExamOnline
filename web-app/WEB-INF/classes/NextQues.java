import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import java.io.*;
import java.util.*;

public class NextQues extends HttpServlet
{
	private ConnectionPool pool;	
	public void init(ServletConfig config)throws ServletException
	{
		super.init(config);
		try
		{
			pool=new ConnectionPool(5,2);
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
		String username="",question="",ch1="",ch2="",ch3="",ch4="";
		int ques_id[];
		int ques_no=0,sess_ques=0,answer=0,no_ques=0;
		int total_time=0,time_passed=0;
		java.util.Date old_time=null;
		int id=0;
		Random r=new Random();		
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
		out.println("<html>");
		out.println("<head><title>ExamOnline - Your Test Question</title></head>");
		out.println("<body bgcolor='#B3B3D9'>");		
		try{
			con=pool.getConnection();
			Statement stmt=con.createStatement();
			ResultSet rs;
			ques_id=(int[])session.getValue("ques_id");
			if(ques_id==null){ 		//means that it is first question
			    String noq=null;
				rs=stmt.executeQuery("select * from global where field='no_ques'"); 
				while(rs.next())noq=rs.getString("value");
				no_ques=Integer.parseInt(noq);
				rs.close();	
				rs=stmt.executeQuery("select * from global where field='total_time'"); 
				while(rs.next())noq=rs.getString("value");
				float temptime=3600*(Float.parseFloat(noq));
				total_time=(int)temptime;
				time_passed=0;
				rs.close();	
				old_time=new java.util.Date();		
			    ques_id=new int[no_ques];
			    ques_no=1;
			    sess_ques=1;				
				rs=stmt.executeQuery("select * from result where username='"+username+"'"); 
				int tempc=0;
				while(rs.next())tempc++;
				if(tempc==0){
					stmt.executeUpdate("Insert into result values('"+username+"',0,"+no_ques+",0,0)"); 
				}
				else{
					stmt.executeUpdate("Update result set total="+no_ques+",attempt=0,correct=0,test_code=0 where username='"+username+"'"); 
				}
				rs=stmt.executeQuery("select count(*) from paper");
				int total_ques=0;
				while(rs.next()){
					total_ques=rs.getInt("count(*)");
				}
				rs.close();
				int ques_c=0;
				int val=0;
				boolean exist;
				int tempvar=0;
				for(ques_c=0;ques_c<no_ques;ques_c++){
					exist=true;
					while(exist){
						val=(int)(total_ques*r.nextDouble());
						rs=stmt.executeQuery("select * from paper");
						tempvar=0;
						while(rs.next()&&tempvar!=val){
							tempvar++;
							id=rs.getInt("id");
						}
						rs.close();
						exist=false;
						for(int i=0;i<ques_c;i++){
							if(ques_id[i]==id)exist=true;
						}
					}
					ques_id[ques_c]=id;					
				}
				session.putValue("ques_id",ques_id);
				session.putValue("total_time",""+total_time);
				session.putValue("time_passed",""+time_passed);
				session.putValue("old_time",old_time);
				String tempqu=""+sess_ques;
				session.putValue("sess_ques",tempqu);
			}
			else{ //not first question
				String tempid=req.getParameter("id");
				String tempans=req.getParameter("answer");
				String temp_ques_no=req.getParameter("ques_no");
				String temp_sess_ques=(String)session.getValue("sess_ques");
				ques_id=(int[])session.getValue("ques_id");
				total_time=Integer.parseInt((String)session.getValue("total_time"));
				time_passed=Integer.parseInt((String)session.getValue("time_passed"));
				old_time=(java.util.Date)session.getValue("old_time");
				java.util.Date new_time=new java.util.Date();
				time_passed+=(int)((new_time.getTime()-old_time.getTime())/1000);
				old_time=new_time;				
				no_ques=ques_id.length;
				if(tempid==null||tempans==null||ques_id==null||temp_ques_no==null){
					out.println("<H2>An Error Has Been Occured</H2>");
					session.invalidate();
				    out.println("<a href='../examonline/user.html'>Click Here</a>  to Relogin & Start The Test Again");
				    return;
				}
				if(time_passed>=total_time){
					out.println("Test Ended:Your total Time has elapsed");
		    		out.println("<p align='center'><a href='/servlet/EndTest'>Click Here</a>to View result</p>");//end test here
		    		out.println("");
		       		out.println("</body></html>");
		       		stmt.close();
		    		return;	    		
				}
				else{
					session.putValue("total_time",""+total_time);
					session.putValue("time_passed",""+time_passed);
					session.putValue("old_time",old_time);
				}
				id=Integer.parseInt(tempid);
				answer=Integer.parseInt(tempans);
				ques_no=Integer.parseInt(temp_ques_no);
				sess_ques=Integer.parseInt(temp_sess_ques);
				ques_no++;
				sess_ques++;;
				String tempqu=""+sess_ques;
				session.putValue("sess_ques",tempqu);	
				if(sess_ques!=ques_no){
					sess_ques--;
					tempqu=""+sess_ques;
					session.putValue("sess_ques",tempqu);	
				}
				else{
					rs=stmt.executeQuery("select answer from paper where id="+id);
					rs.next();
					int anstemp=rs.getInt("answer");
					if(anstemp==answer){
						stmt.executeUpdate("Update result set attempt=attempt+1,correct=correct+1 where username='"+username+"'");
					}
					else{									
						stmt.executeUpdate("Update result set attempt=attempt+1 where username='"+username+"'");	
					}				
			    }			    
			}
			id=ques_id[ques_no-1];			
		   	rs=stmt.executeQuery("select * from paper where id="+id);
		    rs.next();
		    question=rs.getString("question");
		    ch1=rs.getString("ch1");
		    ch2=rs.getString("ch2");
		    ch3=rs.getString("ch3");
		    ch4=rs.getString("ch4");
		    out.println("<p align='center'><big><big><strong>Take The Question</strong></big></big></p>");
		    out.println("<hr>");
		    out.println("<p>Your Question No."+ques_no+" is:<br>");
		    out.println("<big><strong>"+question+"</strong></big></p>");
		    if(ques_no>=no_ques)out.println("<form method='POST' action='/servlet/EndTest'>");
		    else out.println("<form method='POST' action='/servlet/NextQues'>");
		    out.println("<input type='hidden' value="+id+" name='id'>");
		    out.println("<input type='hidden' value="+ques_no+" name='ques_no'>");		    
		    out.println("<p><input type='radio' value='1' checked name='answer'>");
		    out.println("<big><strong>"+ch1+"</strong></big></p>");
		    out.println("<p><input type='radio' value='2' name='answer'>");
		    out.println("<big><strong>"+ch2+"</strong></big></p>");
		    out.println("<p><input type='radio' value='3' name='answer'>");
		    out.println("<big><strong>"+ch3+"</strong></big></p>");
		    out.println("<p><input type='radio' value='4' name='answer'>");
		    out.println("<big><strong>"+ch4+"</strong></big></p>");
		    if(ques_no>=no_ques){
		    	out.println("<p>This was the last qusetion in your test <br>Press End Exam button below to answer this qusetion &amp; end test</p>");
		    	out.println("<p>");
		    	out.println("<input type='submit' value='End Test & View Scores'name='Submit'></p>");
		    }
		    else{
		    	out.println("<p>");
		    	out.println("<input type='submit' value='Next Question'name='Submit'></p>");
		    }	
		    out.println("</form>");
		    out.println("<p>Total No of qusetions:"+no_ques+" &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Attempted till now:"+(ques_no-1));
		    out.println("<br>Total Time:"+total_time+" Seconds &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Time elapsed:"+time_passed+" Seconds</p>");
		    out.println("<p align='center'><a href='/servlet/EndTest'>Quit Test</a></p>");
		    out.println("</body></html>");
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
    public String getServletInfo(){
		return "This servlet Delivers next question to test taker";
	}
}