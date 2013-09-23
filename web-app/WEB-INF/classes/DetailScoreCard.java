import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import java.io.*;
import java.util.*;

public class DetailScoreCard extends HttpServlet
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
	throws ServletException,IOException{
		Connection con=null;
		res.setContentType("text/html");
		PrintWriter out = res.getWriter();
		res.setHeader("Cache-Control","no-store"); 
		String username=null,test_name=null,subject_name=null;
		int test_code=0,time_taken=0,attempt=0,total=0,test_no_subject=0;
		float score;
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
		try{
			con=pool.getConnection();
			Statement stmt=con.createStatement();
			String temp_code = req.getParameter("test_code");
			//test_name = req.getParameter("test_name");
			String temp_score = req.getParameter("score");
			String temp_time = req.getParameter("time_taken");
			String temp_attempt=req.getParameter("attempt");
			String temp_total=req.getParameter("total");
		    score = Float.parseFloat(temp_score);
			time_taken = Integer.parseInt(temp_time);
			test_code = Integer.parseInt(temp_code);
			attempt = Integer.parseInt(temp_attempt);
			total = Integer.parseInt(temp_total);
			ResultSet rs=stmt.executeQuery("select test_name,test_no_subject from test_master where test_code="+test_code);
			if(rs.next()){
				test_name=rs.getString("test_name");
				test_no_subject=rs.getInt("test_no_subject");
			}		
			out.println("<html>");
			out.println("<head>");
			out.println("<title>Getting Started With ExamOnline</title>");
			out.println("</head>");
			out.println("<body topmargin='0' leftmargin='0'>");
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
			out.println("<td width='100%'><p align='center'><font face='Verdana' color='#FFFFFF'><small><strong>Detailed Score Card</strong></small></font></td>");
			out.println("</tr>");
			out.println("</table>");
			out.println("</center></div><div align='center'><center>");
			out.println("<table border='1' width='60%' cellspacing='0' cellpadding='0' bordercolor='#C0C0C0' bordercolorlight='#C0C0C0' bordercolordark='#FFFFFF' bgcolor='#F3F3F3' height='20'>");
			out.println("<tr>");
			out.println("<td width='75%' bgcolor='#F3F3F3'><p align='center'><small><small><font face='Verdana'>"+test_name+"</font></small></small></td>");
			out.println("</tr>");
			out.println("</table>");
			out.println("</center></div><div align='center'><center>");
			int i=1;
			out.println("<table border='1' width='60%' cellspacing='0' cellpadding='0' bgcolor='#F0F0FF' bordercolor='#C0C0C0' bordercolorlight='#C0C0C0' bordercolordark='#FFFFFF'>");
			out.println("<tr>");
			out.println("<td width='5%' valign='middle' align='center'><p align='center'><small><small><font face='Verdana'>"+i+".</font></small></small></td>");
			out.println("<td width='47%'><small><small><font face='Verdana'>Total Score</font></small></small></td>");
			out.println("<td width='23%'><small><small><font face='Verdana'>"+score+"</font></small></small></td>");
			out.println("</tr>");
			i++;
			/*out.println("<tr>");
			out.println("<td width='5%' valign='middle' align='center'><small><small><font face='Verdana'>"+i+".</font></small></small></td>");
			out.println("<td width='47%'><small><small><font face='Verdana'>Total Time Taken</font></small></small></td>");
			out.println("<td width='23%'><small><small><font face='Verdana'>"+time_taken+" min.</font></small></small></td>");
			out.println("</tr>");
			i++;
			*/
			out.println("<tr>");
			out.println("<td width='5%' valign='middle' align='center'><small><small><font face='Verdana'>"+i+".</font></small></small></td>");
			out.println("<td width='47%'><small><small><font face='Verdana'>Total No. Of Questions</font></small></small></td>");
			out.println("<td width='23%'><small><small><font face='Verdana'>"+total+"</font></small></small></td>");
			out.println("</tr>");
			i++;
			out.println("<tr>");
			out.println("<td width='5%' valign='middle' align='center'><small><small><font face='Verdana'>"+i+".</font></small></small></td>");
			out.println("<td width='47%'><small><small><font face='Verdana'>No. Of Questions Attempted</font></small></small></td>");
			out.println("<td width='23%'><small><small><font face='Verdana'>"+attempt+"</font></small></small></td>");
			out.println("</tr>");
			i++;
			/*
			out.println("<tr>");
			out.println("<td width='5%' valign='middle' align='center'><small><small><font face='Verdana'>"+i+".</font></small></small></td>");
			out.println("<td width='47%'><small><small><font face='Verdana'>Avg. Time Taken Per Question</font></small></small></td>");
			out.println("<td width='23%'><small><small><font face='Verdana'>"+(int)(((float)time_taken/attempt)*60)+" sec.</font></small></small></td>");
			out.println("</tr>");
			i++;
			*/
			out.println("<tr>");
			out.println("<td width='5%' valign='middle' align='center'><small><small><font face='Verdana'>"+i+".</font></small></small></td>");
			out.println("<td width='47%'><small><small><font face='Verdana'>Percentage Score</font></small></small></td>");
			out.println("<td width='23%'><small><small><font face='Verdana'>"+((score/(float)total)*100)+" % </font></small></small></td>");
			out.println("</tr>");
			out.println("</table>");			
			out.println("</center></div><div align='center'><center>");
			out.println("<table border='0' width='80%' bgcolor='#C0C0C0' bordercolor='#C0C0C0' cellspacing='0' cellpadding='0' bordercolorlight='#C0C0C0'>");
			out.println("<tr>");
			out.println("<td width='100%' bgcolor='#FFFFFF' height='10'><p align='center'></td>");
			out.println("</tr>");
			out.println("<tr>");
			out.println("<td width='100%'><font face='Verdana' color='#FFFFFF'><small><strong>Subject Score Card</strong></small></font></td>");
			out.println("</tr>");
			out.println("</table>");
			out.println("</center></div>");	
			for(i=0;i<test_no_subject;i++){	
				rs=stmt.executeQuery("select subject_name from test_info where test_code ="+test_code+" AND table_name ='test_"+test_code+"_"+i+"'");		
				if(rs.next()) subject_name=rs.getString("subject_name");
				out.println("<div align='center'><center>");
				out.println("<table border='1' width='80%' cellspacing='0' cellpadding='0' bordercolor='#C0C0C0' bordercolorlight='#C0C0C0' bordercolordark='#FFFFFF' bgcolor='#F3F3F3' height='20'>");
				out.println("<tr>");
				out.println("<td width='75%' bgcolor='#F3F3F3'><p align='center'><small><small><font face='Verdana'>"+subject_name+"</font></small></small></td>");
				out.println("</tr>");
				out.println("</table>");
				out.println("</center></div>");
				rs=stmt.executeQuery("select * from result where test_code ="+test_code+" AND test_subject_no ="+(i+1)+" AND username='"+username+"'");
				if(rs.next()){
					total=rs.getInt("total");
					attempt=rs.getInt("attempt");
					score=rs.getFloat("score");
					time_taken=rs.getInt("time_taken");
					out.println("<div align='center'><center>");
					int j=1;
					out.println("<table border='1' width='80%' cellspacing='0' cellpadding='0' bgcolor='#F0F0FF' bordercolor='#C0C0C0' bordercolorlight='#C0C0C0' bordercolordark='#FFFFFF'>");
					out.println("<tr>");
					out.println("<td width='5%' valign='middle' align='center'><p align='center'><small><small><font face='Verdana'>"+j+".</font></small></small></td>");
					out.println("<td width='47%'><small><small><font face='Verdana'>Score</font></small></small></td>");
					out.println("<td width='23%'><small><small><font face='Verdana'>"+score+"</font></small></small></td>");
					out.println("</tr>");
					j++;
					/*
					out.println("<tr>");
					out.println("<td width='5%' valign='middle' align='center'><small><small><font face='Verdana'>"+j+".</font></small></small></td>");
					out.println("<td width='47%'><small><small><font face='Verdana'>Time Taken</font></small></small></td>");
					out.println("<td width='23%'><small><small><font face='Verdana'>"+time_taken+" min.</font></small></small></td>");
					out.println("</tr>");
					j++;
					*/
					out.println("<tr>");
					out.println("<td width='5%' valign='middle' align='center'><small><small><font face='Verdana'>"+j+".</font></small></small></td>");
					out.println("<td width='47%'><small><small><font face='Verdana'>No. Of Questions</font></small></small></td>");
					out.println("<td width='23%'><small><small><font face='Verdana'>"+total+"</font></small></small></td>");
					out.println("</tr>");
					j++;
					out.println("<tr>");
					out.println("<td width='5%' valign='middle' align='center'><small><small><font face='Verdana'>"+j+".</font></small></small></td>");
					out.println("<td width='47%'><small><small><font face='Verdana'>No. Of Questions Attempted</font></small></small></td>");
					out.println("<td width='23%'><small><small><font face='Verdana'>"+attempt+"</font></small></small></td>");
					out.println("</tr>");
					j++;
					/*
					out.println("<tr>");
					out.println("<td width='5%' valign='middle' align='center'><small><small><font face='Verdana'>"+j+".</font></small></small></td>");
					out.println("<td width='47%'><small><small><font face='Verdana'>Avg. Time Taken Per Question</font></small></small></td>");
					out.println("<td width='23%'><small><small><font face='Verdana'>"+(int)(((float)time_taken/attempt)*60)+" sec.</font></small></small></td>");
					out.println("</tr>");
					j++;
					*/
					out.println("<tr>");
					out.println("<td width='5%' valign='middle' align='center'><small><small><font face='Verdana'>"+j+".</font></small></small></td>");
					out.println("<td width='47%'><small><small><font face='Verdana'>Percentage Score</font></small></small></td>");
					out.println("<td width='23%'><small><small><font face='Verdana'>"+((score/(float)total)*100)+" % </font></small></small></td>");
					out.println("</tr>");
					j++;
					out.println("<tr>");
					out.println("<td width='5%' valign='middle' align='center'><small><small><font face='Verdana'>"+j+".</font></small></small></td>");
					out.println("<td width='47%'><small><small><font face='Verdana'>Remarks</font></small></small></td>");
					out.println("<td width='23%'><small><small><font face='Verdana'>");
					float perc=((score/(float)total)*100);
					if(perc<50)out.println("Need substantial improvement");
					else if(perc>=50&&perc<60) out.println("Need some improvement");
					else if(perc>=60&&perc<70) out.println("Good");
					else if(perc>=70&&perc<80) out.println("Very Good");
					else if(perc>=80) out.println("Excellent");
					out.println("</font></small></small></td>");
					out.println("</tr>");
					out.println("</table>");
					out.println("</center></div>");
				}				
			}
			out.println("<br><br>");
			out.println("</body>");
			out.println("</html>");
			rs.close();
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
		return "This servlet displays the detailed score card";
	}	
}		
								
				