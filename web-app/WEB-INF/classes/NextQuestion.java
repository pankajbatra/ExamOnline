import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import java.io.*;
import java.util.*;

public class NextQuestion extends HttpServlet
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
		String username=null,table_statement=null,table_question=null,table_code=null,ans_ch_1=null,ans_ch_2=null,ans_ch_3=null,ans_ch_4=null;
		int id=0,table_image=0,ans_ch_correct=0;	
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
			String temp_next_subject=req.getParameter("next_subject");
			next_subject=Integer.parseInt(temp_next_subject);
			total_time=(test_duration*60);			
			if(id==0){ 		//means that it is first question
				//id=questions[0][0];
			}
			else{ //not first question			
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
			}	
			//fetch question to be shown
			table_name="test_"+test_code+"_"+(next_subject-1);
			id=questions[(next_subject-1)][attempted[(next_subject-1)]];
			attempted[(next_subject-1)]++;
			rs=stmt.executeQuery("select * from "+table_name+" where id="+id); 
			if(rs.next()){
				table_statement =rs.getString("table_statement");
				table_question =rs.getString("table_question");
				table_code=rs.getString("table_code");
				ans_ch_1=rs.getString("ans_ch_1");
				ans_ch_2=rs.getString("ans_ch_2");
				ans_ch_3=rs.getString("ans_ch_3");
				ans_ch_4=rs.getString("ans_ch_4");
				table_image=rs.getInt("table_image");
			}
			else{
				out.println("An Error has occured:");
				out.println(id);
				out.println(questions[0][0]);
		    	out.println("<p align='center'><a href='/servlet/TestCenter'>Click Here</a>to Go back to test Center & start test again</p>");		       		
		    	return;	 
			}
			
			//store session info again
			old_time=new java.util.Date();
			session.putValue("old_time",old_time);
			session.putValue("time_elapsed",""+time_elapsed);
			session.putValue("attempted",attempted);
			
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
			out.println("<td width='100%'><font face='Verdana' color='#FFFFFF'><small><strong>Your Test Question..</strong></small></font></td>");
			out.println("</tr>");
			out.println("</table>");
			out.println("</div><div align='right'>");
			out.println("<table border='1' width='97%' cellspacing='0' cellpadding='0' height='230' bordercolor='#COCOCO' bordercolorlight='#COCOCO'>");				
			out.println("<tr>");
			out.println("<td width='100%' bgcolor='#F0F0FF' height='230' valign='top' align='center'><form method='POST' action='/servlet/");			
			boolean checkAllDone=true;
			for(i=0;i<test_no_subject;i++){
				if(attempted[i]<subject_questions[i]) checkAllDone=false;	
			}			
			if(checkAllDone)out.println("EndTest");
			else out.println("NextQuestion");
			out.println("'><br><br>");
			out.println("<input type='hidden' value="+id+" name='id'>");
			out.println("<input type='hidden' value="+table_name+" name='table_name'>");
			out.println("<input type='hidden' value="+next_subject+" name='test_subject_no'>");
			//out.println("<input type='hidden' value="+ques_no+" name='ques_no'>");
			out.println("<table border='0' width='79%' cellspacing='0' cellpadding='0' height='161' >");
			out.println("<tr align='center'>");
			out.println("<td align='right' height='38' vAlign='center' width='100%' colspan='2'><p align='center'><font color='#000080' face='arial,helvetica'><small>Total Time:</small></font><font face='Monotype.com, courier'>&nbsp;"+test_duration+" Minutes</font><font color='#000080' face='arial,helvetica'><small> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Time elapsed:</small></font><font face='Monotype.com, courier'>"+(time_elapsed/60)+" Minutes</font></td>");
			out.println("</tr>");
			if(table_code!=null&&(!table_code.equals(""))){
				out.println("<tr align='center'>");
				out.println("<td width='20%' height='19' valign='middle' align='right' bgcolor='#c0c0c0'><font face='arial,helvetica' color='#000080'><small>Code:</small></font></td>");
				out.println("<td width='80%' height='30' valign='middle' align='left'><font face='Monotype.com, courier' >&nbsp;"+table_code+"</font></td>");
				out.println("</tr>");
				out.println("<tr align='center'>");
				out.println("<td width='20%' height='10' valign='middle' align='right' ></td>");
				out.println("<td width='80%' height='10' valign='middle' align='left'> </td>");
				out.println("</tr>");
			}
			if(table_image==1){
				out.println("<tr align='center'>");
				out.println("<td width='20%' height='19' valign='middle' align='right' bgcolor='#c0c0c0'><font face='arial,helvetica' color='#000080'><small>Image:</small></font></td>");
				out.println("<td width='80%' height='30' valign='middle' align='left'><img src='/servlet/ViewImage?table_name="+table_name+"&id="+id+"'></td>");
				out.println("</tr>");
				out.println("<tr align='center'>");
				out.println("<td width='20%' height='10' valign='middle' align='right' ></td>");
				out.println("<td width='80%' height='10' valign='middle' align='left'> </td>");
				out.println("</tr>");
			}
			if(table_statement!=null&&(!table_statement.equals(""))){
				out.println("<tr align='center'>");
				out.println("<td width='20%' height='19' valign='middle' align='right'bgcolor='#c0c0c0'><font face='arial,helvetica' color='#000080'><small>Statement:</small></font></td>");
				out.println("<td width='80%' height='30' valign='middle' align='left'><font face='Monotype.com, courier' >&nbsp;"+table_statement+"</font></td>");
				out.println("</tr>");			
				out.println("<tr align='center'>");
				out.println("<td width='20%' height='10' valign='middle' align='right' ></td>");
				out.println("<td width='80%' height='10' valign='middle' align='left'> </td>");
				out.println("</tr>");
			}
			out.println("<tr align='center'>");
			out.println("<td width='20%' height='19' valign='middle' align='right'bgcolor='#c0c0c0'><font face='Verdana' color='#000080'><small>Question:</small></font></td>");
			out.println("<td width='80%' height='30' valign='middle' align='left'>&nbsp;&nbsp;"+table_question+"</td>");
			out.println("</tr>");			
			out.println("<tr align='center'>");
			out.println("<td width='20%' height='10' valign='middle' align='right' ></td>");
			out.println("<td width='80%' height='10' valign='middle' align='left'> </td>");
			out.println("</tr>");
			out.println("<tr align='center'>");
			out.println("<td width='20%' height='19' valign='middle' align='right' bgcolor='#c0c0c0'><b><font face='arial,helvetica' size='1'>Choice 1</font></b><input name='answer' type='radio' value='1'></td>");
			out.println("<td width='80%' height='30' valign='middle' align='left'>&nbsp;&nbsp;"+ans_ch_1+"</td>");
			out.println("</tr>");		
			out.println("<tr align='center'>");
			out.println("<td width='20%' height='10' valign='middle' align='right' ></td>");
			out.println("<td width='80%' height='10' valign='middle' align='left'> </td>");
			out.println("</tr>");
			out.println("<tr align='center'>");
			out.println("<td width='20%' height='19' valign='middle' align='right' bgcolor='#c0c0c0'><b><font face='arial,helvetica' size='1'>Choice 2</font></b><input name='answer' type='radio' value='2'></td>");
			out.println("<td width='80%' height='30' valign='middle' align='left'>&nbsp;&nbsp;"+ans_ch_2+"</td>");
			out.println("</tr>");		
			out.println("<tr align='center'>");
			out.println("<td width='20%' height='10' valign='middle' align='right' ></td>");
			out.println("<td width='80%' height='10' valign='middle' align='left'> </td>");
			out.println("</tr>");
			out.println("<tr align='center'>");
			out.println("<td width='20%' height='19' valign='middle' align='right' bgcolor='#c0c0c0'><b><font face='arial,helvetica' size='1'>Choice 3</font></b><input name='answer' type='radio' value='3'></td>");
			out.println("<td width='80%' height='30' valign='middle' align='left'>&nbsp;&nbsp;"+ans_ch_3+"</td>");
			out.println("</tr>");		
			out.println("<tr align='center'>");
			out.println("<td width='20%' height='10' valign='middle' align='right' ></td>");
			out.println("<td width='80%' height='10' valign='middle' align='left'> </td>");
			out.println("</tr>");
			out.println("<tr align='center'>");
			out.println("<td width='20%' height='19' valign='middle' align='right' bgcolor='#c0c0c0'><b><font face='arial,helvetica' size='1'>Choice 4</font></b><input name='answer' type='radio' value='4'></td>");
			out.println("<td width='80%' height='30' valign='middle' align='left'>&nbsp;&nbsp;"+ans_ch_4+"</td>");
			out.println("</tr>");		
			out.println("<tr align='center'>");
			out.println("<td width='20%' height='10' valign='middle' align='right' ></td>");
			out.println("<td width='80%' height='10' valign='middle' align='left'> </td>");
			out.println("</tr>");
			out.println("<tr align='center'>");
			out.println("<td width='20%' height='19' valign='middle' align='right' bgcolor='#c0c0c0'><b><font face='arial,helvetica' size='1'>Choice 5</font></b><input name='answer' type='radio' checked value='0'></td>");
			out.println("<td width='80%' height='30' valign='middle' align='left'>&nbsp;&nbsp;Don\'t Attempt</td>");
			out.println("</tr>");		
			out.println("<tr align='center'>");
			out.println("<td width='20%' height='10' valign='middle' align='right' ></td>");
			out.println("<td width='80%' height='10' valign='middle' align='left'> </td>");
			out.println("</tr>");
			out.println("<tr align='center'>");
			out.println("<td width='20%' height='19' valign='middle' align='right' ><input type='submit' value=");
			if(checkAllDone)out.println("'EndTest'");
			else out.println("'Submit'");
			out.println("name='B1' style='background-color: rgb(196,196,255); color: rgb(0,0,0); border: 1px solid rgb(0,0,128)'></td>");
			out.println("<td width='80%' height='30' valign='middle' align='left'>&nbsp;&nbsp;"+subject_names[(next_subject-1)]+": Question "+(attempted[(next_subject-1)])+" of "+subject_questions[(next_subject-1)]+"</td>");
			out.println("</tr>");				
			out.println("</table>");
			out.println("<table border='0' width='79%' cellspacing='0' cellpadding='0' height='20' ><tr align='center'><td width='100%' height='19' valign='middle' align='middle' >");
			boolean checkedonce=false;
			for(i=0;i<test_no_subject;i++){
				if(attempted[next_subject-1]<subject_questions[next_subject-1]){
					if(attempted[i]<subject_questions[i]){
						if((i+1)==next_subject){
							out.println("<b><font face='arial,helvetica' size='1'>Continue "+subject_names[i]+"</font></b><input name='next_subject' type='radio' checked value='"+(i+1)+"'>");
						}
						else{
							out.println("<b><font face='arial,helvetica' size='1'>Switch to "+subject_names[i]+"</font></b><input name='next_subject' type='radio' value='"+(i+1)+"'>");
						}
					}
				}
				else{
					if(attempted[i]<subject_questions[i]){
						out.println("<b><font face='arial,helvetica' size='1'>Switch to "+subject_names[i]+"</font></b><input name='next_subject' type='radio' ");
						if(checkedonce==false){
							out.println("checked");
							checkedonce=true;
						}
						out.println("value='"+(i+1)+"'>");						
					}
				}
			}				
			out.println("</td></tr></table>");
			out.println("</form>");
			out.println("</td>");
			out.println("</tr>");
			out.println("</table>");
			out.println("</div>");
			out.println("<p align='middle'><center><font color='#8000ff' face='Verdana'><small><a href='/servlet/EndTest'><font color='#000000'>Click Here</font></a> to Quit Test</small></font></center></p><br<br>");
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
    public String getServletInfo(){
		return "This servlet Delivers next question to test taker";
	}
}