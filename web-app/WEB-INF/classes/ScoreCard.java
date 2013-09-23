import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import java.io.*;
import java.util.*;

public class ScoreCard extends HttpServlet
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
	throws ServletException,IOException
	{
		Connection con=null;
		res.setContentType("text/html");
		PrintWriter out = res.getWriter();
		res.setHeader("Cache-Control","no-store"); 
		String username;
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
			int count=0;
			ResultSet rs=stmt.executeQuery("select distinct test_code from result where username='"+username+"'");
			while(rs.next())count++;
			int s[] = new int[count];
			int i=0;
			rs=stmt.executeQuery("select distinct test_code from result where username='"+username+"'");
			while(rs.next())
			{
			 s[i] = rs.getInt("test_code");	
			 i++;
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
			out.println("<table border='1' width='80%' bgcolor='#C0C0C0' bordercolor='#C0C0C0' cellspacing='0' cellpadding='0' bordercolorlight='#C0C0C0'>");
			out.println("<tr>");
			out.println("<td width='100%'><font face='Verdana' color='#FFFFFF'><small><strong>Score Card</strong></small></font></td>");
			out.println("</tr>");
			out.println("</table>");
			out.println("</center></div><div align='center'><center>");
			out.println("<table border='1' width='80%' cellspacing='0' cellpadding='0' bordercolor='#C0C0C0' bordercolorlight='#C0C0C0' bordercolordark='#FFFFFF' bgcolor='#F3F3F3' height='20'>");
			out.println("<tr>");
			out.println("<td width='5%' bgcolor='#F3F3F3'><small><small><font face='Verdana'>S.No.</font></small></small></td>");
			out.println("<td width='19%'><p align='center'><small><small><font face='Verdana'>Test Name</font></small></small></td>");
			out.println("<td width='15%'><p align='center'><small><small><font face='Verdana'>Score</font></small></small></td>");
			//out.println("<td width='12%'><p align='center'><small><small><font face='Verdana'>Time Taken</font></small></small></td>");
			out.println("<td width='16%'><p align='center'><small><small><font face='Verdana'>Result</font></small></small></td>");
			out.println("<td width='20%'><p align='center'><small><small><font face='Verdana'>Other Info</font></small></small></td>");
			out.println("</tr>");
			out.println("</table>");			
			int time=0,attempt=0,total=0;
			float score=0;
			for(int j=0;j<count;j++){
				time=0;
				attempt=0;
				score=0;
				total=0;
				rs=stmt.executeQuery("select * from result where test_code="+s[j]+" AND username='"+username+"'");
				while(rs.next()){
					time = time+rs.getInt("time_taken");
			 		attempt = attempt+rs.getInt("attempt");
			 		score = score+rs.getFloat("score");
			 		total = total+rs.getInt("total");	
				}
				rs = stmt.executeQuery("select test_name from test_master where test_code="+s[j]+"");
				if(rs.next()){
		    		out.println("</center></div><div align='center'><center>");
					out.println("<table border='1' width='80%' cellspacing='0' cellpadding='0' bgcolor='#F0F0FF' bordercolor='#C0C0C0' bordercolorlight='#C0C0C0' bordercolordark='#FFFFFF'>");
					out.println("<tr>");
		    		out.println("<td width='5%' valign='middle' align='center'><p align='center'><small><small><font face='Verdana'>"+(j+1)+"</font></small></small></td>");
					out.println("<td width='19%'><p align='center'><small><small><font face='Verdana'>"+rs.getString("test_name")+"</font></small></small></td>");
					out.println("<td width='15%'><p align='center'><font face='Verdana' color='#8000FF'><small><small>"+score+"</small></small></font></td>");
					//out.println("<td width='12%'><p align='center'><font face='Verdana' color='#8000FF'><small><small>"+time+" min.</small></small></font></td>");
					if(score<(0.4*total))
					out.println("<td width='16%'><p align='center'><font face='Verdana'  color='#FF0000'><small><small>fail</small></small></font></td>");
					else
					out.println("<td width='16%'><p align='center'><font face='Verdana' color='#00C161' ><small><small>Pass</small></small></font></td>");
					out.println("<td width='20%'><p align='center'><a href='/servlet/DetailScoreCard?test_code="+s[j]+"&score="+score+"&time_taken="+time+"&attempt="+attempt+"&total="+total+"' target='_self'><small><small><font face='Verdana'>Detail Analysis</font></small></small></a></td>");
			    	out.println("</tr>");
					out.println("</table>");
					out.println("</center></div>");
				}
			}
			rs.close();
			out.println("</body>");
			out.println("</html>");
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
		return "This servlet displays the score card";
	}	
}		
								
				