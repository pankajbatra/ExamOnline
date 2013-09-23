import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;
import java.util.*;

public class ViewQuestion extends HttpServlet{
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
		int min=0,n=0,t=0,flag=0,table_image =0;
		int id=0,ans_ch_correct=0;
		String ad_user=null,ans_correct=null,table_name=null,table_statement=null,table_question=null,table_code=null,ans_ch_1=null,ans_ch_2=null,ans_ch_3=null,ans_ch_4=null;
		String t_min=null;
		String t_flag = req.getParameter("flag");
		flag=Integer.parseInt(t_flag);
		t_min = req.getParameter("min");
		if(t_min!=null){
			min = Integer.parseInt(t_min);
		}
		table_name = req.getParameter("table_name");
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
			int test_code=0;
			Statement stmt = con.createStatement();
			ResultSet rs=stmt.executeQuery("select * from test_info where table_name='"+table_name+"'");
			if(rs.next()) test_code=rs.getInt("test_code");
			rs=stmt.executeQuery("select * from test_master where aduser='"+ad_user+"' AND test_code="+test_code);	
			int c=0;
			while(rs.next()){
			 	c++;
			}
			if(c==0){
				session.invalidate();
		   		out.println("<H2>Your Session has expired </H2>");
				out.println("<a href='../examonline/admin.html'>Click Here</a> To Re-Login");
				rs.close();
				stmt.close();
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
			out.println("<td width='100%'><font face='Verdana' color='#FFFFFF'><small><small><small><strong>View Questions...</strong></small></small></small></font></td>");
			out.println("</tr>");
			out.println("</table>");
			out.println("</div><div align='right'>");
			rs = stmt.executeQuery("select Count(*) from "+table_name);
			if(rs.next())n=rs.getInt("Count(*)");
			if(n>10){
				t=n/10;
				if((n%10)==0); 
				else t++;
				/*t=n%10;
				if(t==0)t=n/10;
				else{
					t=n%10;
					n=n-t;
					t=(n/10)+1;
				}*/		 	
			}
			else t=1;		
			rs = stmt.executeQuery("select * from "+table_name+" order by id LIMIT "+(min-1)+",10");
			while(rs.next()){
				id=rs.getInt("id");
				table_statement = rs.getString("table_statement");	
				table_question = rs.getString("table_question");
				table_image =rs.getInt("table_image");
				table_code = rs.getString("table_code");
				ans_ch_1 = rs.getString("ans_ch_1");
				ans_ch_2 = rs.getString("ans_ch_2");
				ans_ch_3 = rs.getString("ans_ch_3");
				ans_ch_4 = rs.getString("ans_ch_4");
				ans_ch_correct=rs.getInt("ans_ch_correct");
				out.println("<table border='1' width='97%' cellspacing='0' cellpadding='0' height='60' bordercolor='#COCOCO' bordercolorlight='#COCOCO' bordercolordark='#FFFFFF'>");
				out.println("<tr>");
				out.println("<td width='100%' bgcolor='#F0F0FF' height='60' valign='top' align='center'><table border='1' width='97%' cellspacing='0' cellpadding='0' height='201' bordercolor='#C0C0C0' bordercolorlight='#C0C0C0' bordercolordark='#FFFFFF'>");
				out.println("<tr>");
				out.println("<td width='100%' align='left' height='30' colspan='2'><font face='Verdana' color='#FF0000'><small><strong>Question Id:"+id+"</strong></small></font></td>");
				out.println("</tr>");
				if(table_statement!=null&&(!table_statement.equals(""))){
					out.println("<tr>");
					out.println("<td width='23%' align='right' height='70'><small><small><font face='Verdana'>Statement :</font></small></small></td>");
					out.println("<td width='77%' height='70'>&nbsp;"+table_statement+" </td>");
					out.println("</tr>");
				}
				out.println("<tr>");
				out.println("<td width='23%' align='right' height='60'><small><small><font face='Verdana'>Question :</font></small></small></td>");
				out.println("<td width='77%' height='60'>&nbsp;"+table_question+" </td>");
				out.println("</tr>");
				out.println("<tr>");
				if(table_image==1){
					out.println("<td width='23%' align='right' height='20' valign='top'><small><small><font face='Verdana'>&nbsp;&nbsp; &nbsp; Image :</font></small></small></td>");
					out.println("<td width='77%' height='30'><img src='/servlet/ViewImage?table_name="+table_name+"&id="+id+"'></td>");
					out.println("</tr>");
				}
				if(table_code!=null&&(!table_code.equals(""))){
					out.println("<tr>");
					out.println("<td width='23%' align='right' height='60'><small><small><font face='Verdana'>Code :</font></small></small></td>");
					out.println("<td width='77%' height='30'>&nbsp;"+table_code+" </td>");
					out.println("</tr>");
				}
				out.println("<tr>");
				out.println("<td width='23%' align='right' height='50'><small><small><font face='Verdana'>Answer Choice 1:</font></small></small></td>");
				out.println("<td width='77%' height='50'>&nbsp;"+ans_ch_1+" </td>");
				out.println("</tr>");
				out.println("<tr>");
				out.println("<td width='23%' align='right' height='50'><small><small><font face='Verdana'>Answer Choice 2:</font></small></small></td>");
				out.println("<td width='77%' height='50'>&nbsp;"+ans_ch_2+" </td>");
				out.println("</tr>");
				out.println("<tr>");
				out.println("<td width='23%' align='right' height='50'><small><small><font face='Verdana'>Answer Choice 3:</font></small></small></td>");
				out.println("<td width='77%' height='50'>&nbsp;"+ans_ch_3+" </td>");
				out.println("</tr>");
				out.println("<tr>");
				out.println("<td width='23%' align='right' height='50'><small><small><font face='Verdana'>Answer Choice 4:</font></small></small></td>");
				out.println("<td width='77%' height='50'>&nbsp;"+ans_ch_4+" </td>");
				out.println("</tr>");
				out.println("<tr>");
				out.println("<td width='23%' align='right' height='25'><small><small><font face='Verdana'>Correct Choice :</font></small></small></td>");
				out.println("<td width='77%' height='35' valign='middle'><p align='left'>&nbsp;&nbsp;");
				out.println("&nbsp;&nbsp;<strong><font face='Verdana'>"+ans_ch_correct+"</font></strong> ");
				out.println("</tr>");
				out.println("</table>");
				out.println("</td>");
				out.println("</tr>");
				out.println("</table>");
			}
			rs.close();
			stmt.close();
			int a=0;
			out.println("</div><div align='right'>");
			out.println("<table border='1' width='97%' cellspacing='0' cellpadding='0' bordercolor='#C0C0C0' bordercolorlight='#C0C0C0' bordercolordark='#FFFFFF'>");
			out.println("<tr>");
			out.println("<td width='85%'><p align='center'><small><font face='Verdana'>More Questions:- ");
			if(flag==1){
				for(int i=0;i<t;i++){
					a=(i*10)+1;
		 			out.println("<a href='/servlet/EditQuesData?table_name="+table_name+"&min="+a+"' target='_self'><strong>"+(i+1)+"</strong></a>");
		 		}
	    	}
	    	if(flag==0){
	    	 	for(int i=0;i<t;i++){
	    	 		a=(i*10)+1;
		 			out.println("<a href='/servlet/ViewQuestion?table_name="+table_name+"&min="+a+"&flag=0' target='_self'><strong>"+(i+1)+"</strong></a>");		
	    		}
	        }
	        if(flag==2){
	    	 	for(int i=0;i<t;i++){
	    	 		a=(i*10)+1;
		 			out.println("<a href='/servlet/DeleteQuestion?table_name="+table_name+"&min="+a+"' target='_self'><strong>"+(i+1)+"</strong></a>");		
	    		}
	        }
			out.println("</font></small></td>");
			out.println("<td width='15%'><small><small><font face='Verdana'>Page-"+(((min-1)/10)+1)+"of"+t+"</font></small></small></td>");
			out.println("</tr>");
			out.println("</table>");
			out.println("</div>");
			if(flag==1){
				out.println("<div align='center'><center>");
				out.println("<table border='1' width='60%' cellspacing='0' cellpadding='0' height='100' bordercolor='#COCOCO' bordercolorlight='#COCOCO'>");
				out.println("<tr>");
				out.println("<td width='100%' bgcolor='#F0F0FF' height='100' valign='top' align='center'><table border='0' width='100%' cellspacing='0' cellpadding='0' height='115'>");
				out.println("<tr>");
				out.println("<td width='42%' height='25' align='right'><p align='center'><font face='Verdana' color='#004000'><small>Enter the question ID you want to edit:</small></font></td>");
				out.println("</tr>");
				out.println("<tr>");
				out.println("<td width='42%' height='60' align='left'><form method='POST' action='/servlet/EditQuesSingle'>");
            	out.println("<div align='center'><center><p>&nbsp;<input type='text' name='id' size='14' style='border: 1px solid rgb(0,0,0)'><br>");
           		out.println("<INPUT TYPE='hidden' NAME='table_name' VALUE='"+table_name+"'>");	
            	out.println("<input type='submit' value='Submit' name='B1' style='background-color: rgb(238,238,238); color: rgb(0,0,0); border: 1px solid rgb(0,0,0)'></p>");
            	out.println("</center></div>");
            	out.println("</form>");
            	out.println("</td>");
            	out.println("</tr>");
            	out.println("</table>");
            	out.println("</td>");
            	out.println("</tr>");
            	out.println("</table>");
            	out.println("</center></div>");
			}
			if(flag==2){
				out.println("<div align='center'><center>");
				out.println("<table border='1' width='60%' cellspacing='0' cellpadding='0' height='100' bordercolor='#COCOCO' bordercolorlight='#COCOCO'>");
				out.println("<tr>");
				out.println("<td width='100%' bgcolor='#F0F0FF' height='100' valign='top' align='center'><table border='0' width='100%' cellspacing='0' cellpadding='0' height='115'>");
				out.println("<tr>");
				out.println("<td width='42%' height='25' align='right'><p align='center'><font face='Verdana' color='#004000'><small>Enter the ID for question you want to delete:</small></font></td>");
				out.println("</tr>");
				out.println("<tr>");
				out.println("<td width='42%' height='60' align='left'><form method='POST' action='/servlet/DeletedQuestion'>");
            	out.println("<div align='center'><center><p>&nbsp;<input type='text' name='id' size='14' style='border: 1px solid rgb(0,0,0)'><br>");
           		out.println("<INPUT TYPE='hidden' NAME='table_name' VALUE='"+table_name+"'>");	
            	out.println("<input type='submit' value='Submit' name='B1' style='background-color: rgb(238,238,238); color: rgb(0,0,0); border: 1px solid rgb(0,0,0)'></p>");
            	out.println("</center></div>");
            	out.println("</form>");
            	out.println("</td>");
            	out.println("</tr>");
            	out.println("</table>");
            	out.println("</td>");
            	out.println("</tr>");
            	out.println("</table>");
            	out.println("</center></div>");
			}
			out.println("</body>");
			out.println("</html>");										
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
		return "Servlet to view list of questions";
	}	
}