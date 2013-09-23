import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import java.io.*;
import java.util.*;

public class EditQuesSingle extends HttpServlet{
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
		int id=0,table_image=0,ans_ch_correct=0; 
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
			//rs=stmt.executeQuery("select * from "+table_name);
			int c=0;
			//while(rs.next()&&ques_number!=c){
			//	c++;
			//	id=rs.getInt("id");
			//}
			int affect=0;
			//if(c==ques_number){
				rs=stmt.executeQuery("select * from "+table_name+" where id="+id);
				while(rs.next()){
					c++;
					table_question=rs.getString("table_question");
					table_statement=rs.getString("table_statement");
					table_image=rs.getInt("table_image");
					table_code=rs.getString("table_code");
					ans_ch_1=rs.getString("ans_ch_1");
					ans_ch_2=rs.getString("ans_ch_2");
					ans_ch_3=rs.getString("ans_ch_3");
					ans_ch_4=rs.getString("ans_ch_4");
					ans_ch_correct=rs.getInt("ans_ch_correct");
				}
				if(c==0){
					out.println("<html>");
		        	out.println("<head><title>ExamOnline - Edit Question </title></head>");
		        	out.println("<body>");
	            	out.println("<center><p><strong>Invalid Question Number.</strong>");
					out.println("<a href='javascript:history.go(-1)'>Click Here</a>  to go back to previous page & try again");
					out.println("</p></center>");				
					out.println("</body></html>");
					return;
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
				out.println("<table border='0' width='98%' cellspacing='0' cellpadding='0' height='10'>");
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
				out.println("<table border='0' width='93%' height='10' cellspacing='0' cellpadding='0'>");
				out.println("<tr>");
				out.println("<td width='100%'></td>");
				out.println("</tr>");
				out.println("</table>");
				out.println("</div><div align='right'>");
				out.println("<table border='1' width='97%' bgcolor='#C0C0C0' bordercolor='#C0C0C0' cellspacing='0' cellpadding='0' bordercolorlight='#C0C0C0' height='10'>");
				out.println("<tr>");
				out.println("<td width='100%'><small><small><font face='Verdana' color='#FFFFFF'><small><strong>Edit Question...</strong></small></font></small></small></td>");
				out.println("</tr>");
				out.println("</table>");
				out.println("</div><div align='right'>");
				out.println("<table border='1' width='97%' cellspacing='0' cellpadding='0' height='60' bordercolor='#COCOCO' bordercolorlight='#COCOCO' bordercolordark='#FFFFFF'>");
				out.println("<tr>");
				out.println("<td width='100%' bgcolor='#F0F0FF' height='60' valign='top' align='center'><form method='POST' action='/servlet/EditedQuestion' ENCTYPE='multipart/form-data'>");
				out.println("<table border='0' width='97%' cellspacing='0' cellpadding='0' height='201'>");
				out.println("<tr>");
				out.println("<td width='23%' align='right' height='70'><small><small><font face='Verdana'>Statement :</font></small></small></td>");
				out.println("<td width='77%' height='70'>&nbsp; <textarea rows='3' name='table_statement' cols='55' style='background-color: rgb(255,255,255); background-attachment: scroll; border: 1px solid rgb(0,0,0)'>");
				if(table_statement!=null&&(!table_statement.equals("")))out.println(table_statement);
				out.println("</textarea></td>");
				out.println("</tr>");
				out.println("<tr>");
				out.println("<td width='23%' align='right' height='60'><small><small><font face='Verdana'>Question :</font></small></small></td>");
				out.println("<td width='77%' height='60'>&nbsp; <textarea rows='3' name='table_question' cols='55' style='background-color: rgb(255,255,255); border: 1px solid rgb(0,0,0)'>"+table_question+"</textarea></td>");
				out.println("</tr>");
				out.println("<tr>");
				out.println("<td width='23%' align='right' height='20' valign='top'><font face='Verdana'><small><small>&nbsp;</small></small><input type='checkbox' name='table_image' value='0'><small><small> &nbsp; Image :</small></small></font></td>");
				out.println("<td width='77%' height='30'>&nbsp; <input type='file' name='image' size='20' style='background-color: rgb(255,255,255); border: 1px solid rgb(0,0,0)'></td>");
				out.println("</tr>");
				out.println("<tr>");
				out.println("<td width='23%' align='right' height='60'><small><small><font face='Verdana'>Code :</font></small></small></td>");
				out.println("<td width='77%' height='30'>&nbsp; <textarea rows='3' name='table_code' cols='55' style='background-color: rgb(255,255,255); border: 1px solid rgb(0,0,0)'>");
				if(table_code!=null&&(!table_code.equals("")))out.println(table_code);
				out.println("</textarea></td>");
				out.println("</tr>");
				out.println("<tr>");
				out.println("<td width='23%' align='right' height='50'><small><small><font face='Verdana'>Answer Choice 1:</font></small></small></td>");
				out.println("<td width='77%' height='50'>&nbsp; <textarea rows='2' name='ans_ch_1' cols='55' style='border: 1px solid rgb(0,0,0)'>"+ans_ch_1+"</textarea></td>");
				out.println("</tr>");
				out.println("<tr>");
				out.println("<td width='23%' align='right' height='50'><small><small><font face='Verdana'>Answer Choice 2:</font></small></small></td>");
				out.println("<td width='77%' height='50'>&nbsp; <textarea rows='2' name='ans_ch_2' cols='55' style='border: 1px solid rgb(0,0,0)'>"+ans_ch_2+"</textarea></td>");
				out.println("</tr>");
				out.println("<tr>");
				out.println("<td width='23%' align='right' height='50'><small><small><font face='Verdana'>Answer Choice 3:</font></small></small></td>");
				out.println("<td width='77%' height='50'>&nbsp; <textarea rows='2' name='ans_ch_3' cols='55' style='border: 1px solid rgb(0,0,0)'>"+ans_ch_3+"</textarea></td>");
				out.println("</tr>");
				out.println("<tr>");
				out.println("<td width='23%' align='right' height='50'><small><small><font face='Verdana'>Answer Choice 4:</font></small></small></td>");
				out.println("<td width='77%' height='50'>&nbsp; <textarea rows='2' name='ans_ch_4' cols='55' style='border: 1px solid rgb(0,0,0)'>"+ans_ch_4+"</textarea></td>");
				out.println("</tr>");
				out.println("<tr>");
				out.println("<td width='23%' align='right' height='25'><small><small><font face='Verdana'>Correct Choice :</font></small></small></td>");
				out.println("<td width='77%' height='35' valign='middle'><div align='left'><p>&nbsp;&nbsp;");
				if(ans_ch_correct==1)
				out.println("<input type='radio' value='1' checked name='ans_ch_correct' style='background-position:bottom'>");
				else
				out.println("<input type='radio' value='1'  name='ans_ch_correct' style='background-position:bottom'>");		
        		out.println("<strong><font face='Verdana'>1</font></strong> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ");
        		if(ans_ch_correct==2)
        		out.println("<input type='radio' checked name='ans_ch_correct' value='2'> <strong><font face='Verdana'>2</font>");
        		else
        		out.println("<input type='radio'  name='ans_ch_correct' value='2'> <strong><font face='Verdana'>2</font>");        
        		out.println("</strong>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
        		if(ans_ch_correct==3)
        		out.println("<input type='radio' checked name='ans_ch_correct' value='3'>&nbsp;<strong><font face='Verdana'>3</font></strong>");
        		else
        		out.println("<input type='radio'  name='ans_ch_correct' value='3'>&nbsp;<strong><font face='Verdana'>3</font></strong>");  
        		out.println("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
        		if(ans_ch_correct==4)
        		out.println("<input type='radio' checked name='ans_ch_correct' value='4'> <strong><font face='Verdana'>4</font></strong>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>");
        		else
        		out.println("<input type='radio'  name='ans_ch_correct' value='4'> <strong><font face='Verdana'>4</font></strong>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>");
       	        out.println("</tr>");
				out.println("</tr>");
				out.println("<INPUT TYPE='hidden' NAME='id' VALUE='"+id+"'>");
				out.println("<INPUT TYPE='hidden' NAME='table_name' VALUE='"+table_name+"'>");
				out.println("<tr>");
				out.println("<td width='23%' align='right' height='15'></td>");
				out.println("<td width='77%' height='15'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type='submit' value='Submit' name='B1' style='background-color: rgb(218,218,218); color: rgb(0,0,0); border: 1px solid rgb(0,0,0)'></td>");
				out.println("</tr>");
				out.println("</table>");
				out.println("</form>");
				out.println("</td>");
				out.println("</tr>");
				out.println("</table>");
				out.println("</div>");
				out.println("</body>");
				out.println("</html>");
			//}			
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
		return "This servlet Show all fields of question for editing";
	}	
}