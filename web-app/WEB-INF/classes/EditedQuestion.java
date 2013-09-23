import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;
import java.util.*;
import java.text.*;
import com.oreilly.servlet.MultipartRequest;

public class EditedQuestion extends HttpServlet{
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
	public void doPost(HttpServletRequest req,HttpServletResponse res)
	throws ServletException,IOException{
		Connection con = null;
		res.setContentType("text/html");
		res.setHeader("Cache-Control","no-store");
		MultipartRequest multi = new MultipartRequest(req, "/home/virtual/site2/fst/var/www/html/temp");
		PrintWriter out=res.getWriter();
		HttpSession session=req.getSession(false);
		int id=0,table_image=0,ans_ch_correct=0;
		String ad_user=null,table_name=null,table_statement=null,table_question=null,table_code=null,ans_ch_1=null,ans_ch_2=null,ans_ch_3=null,ans_ch_4=null;
		Enumeration file = multi.getFileNames();
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
			Statement stmt = con.createStatement();		
			ResultSet rs=null;
			id = Integer.parseInt(multi.getParameter("id"));
			table_name = multi.getParameter("table_name");
			table_statement = multi.getParameter("table_statement");
			table_question = multi.getParameter("table_question");
			table_code = multi.getParameter("table_code");
			ans_ch_1 = multi.getParameter("ans_ch_1");
			ans_ch_2 = multi.getParameter("ans_ch_2");
			ans_ch_3 = multi.getParameter("ans_ch_3");
			ans_ch_4 = multi.getParameter("ans_ch_4");
			String temp_image=multi.getParameter("table_image");
			if(temp_image==null)table_image=0;
			else table_image=1;
			ans_ch_correct = Integer.parseInt(multi.getParameter("ans_ch_correct"));
			if((table_question==null)||(ans_ch_1==null)||(ans_ch_2==null)||(ans_ch_3==null)||(ans_ch_4==null)){
				out.println("<H2>Some of the Fields were left blank</H2>");
				out.println("<a href='javascript:history.go(-1)'>Click Here</a>  to go back to previous page & try again");
				return;
			}
			if(table_question.equals("")||ans_ch_1.equals("")||ans_ch_2.equals("")||ans_ch_3.equals("")||ans_ch_4.equals("")){
				out.println("<H2>Some of the Fields were left blank</H2>");
				out.println("<a href='javascript:history.go(-1)'>Click Here</a>  to go back to previous page & try again");
				return;
			}
			stmt.executeUpdate("delete from question_images where table_name='"+table_name+"' AND id="+id);
			stmt.executeUpdate("Update "+table_name+" set table_statement='"+table_statement+"',table_question='"+table_question+"',table_image="+table_image+",table_code='"+table_code+"',ans_ch_1='"+ans_ch_1+"',ans_ch_2='"+ans_ch_2+"',ans_ch_3='"+ans_ch_3+"',ans_ch_4='"+ans_ch_4+"',ans_ch_correct="+ans_ch_correct+" where id="+id);		
			java.util.Date date = new java.util.Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String test_updated = sdf.format(date);	
			int test_code=0;
			rs=stmt.executeQuery("select * from test_info where table_name='"+table_name+"'");
			if(rs.next()) test_code=rs.getInt("test_code");
			stmt.executeUpdate("Update test_master set test_updated='"+test_updated+"' where test_code="+test_code);			
			if(table_image==1){
				String fname = (String)file.nextElement();						
				String filename = multi.getFilesystemName(fname);
				if(filename==null&&table_image==1){
					out.println("<H2>The Image Field is left blank</H2>");
					out.println("<a href='javascript:history.go(-1)'>Click Here</a>to go back to previous page & Choose a Image to Upload");
					return;
				}						
				int ex=filename.indexOf('.');
				//String type = multi.getContentType(fname);
				String exten=filename.substring(ex).toLowerCase();			
				boolean goodfile;
				if(exten.equals(".gif")||exten.equals(".png")||exten.equals(".jpg")||exten.equals(".jpeg")) goodfile=true;
				else{
					out.println("<H2>The Image you have selected to upload is not a jpg,png or gif file</H2>");
					out.println("<a href='javascript:history.go(-1)'>Click Here</a>  to go back to previous page & Again Choose Image to Upload");
					return;
				}		
				File f = multi.getFile(fname);
				if(f.length()>(64*1024)){
					out.println("<H2>The Size of image you have selected to upload is greater than 64KB</H2>");
					out.println("<a href='javascript:history.go(-1)'>Click Here</a>to go back to previous page & Again Choose Image of size less than 64 KB");
					return;
				}
				String file_name="image"+exten;
				PreparedStatement ipstmt=con.prepareStatement("insert into question_images values(?,?,?,?)");
				ipstmt.clearParameters();
				ipstmt.setString(1,table_name);
				ipstmt.setInt(2,id);
				ipstmt.setString(3,file_name);
				InputStream inputImage = new FileInputStream(f);
				ipstmt.setBinaryStream(4,inputImage,(int)f.length());
            	ipstmt.executeUpdate();
            	ipstmt.close();
            	f.delete();		
			}
			rs.close();
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
        	out.println("<td width='100%'><font face='Verdana' color='#FFFFFF'><small><strong>Question Edited Successfully...</strong></small></font></td>");
        	out.println("</tr>");
        	out.println("</table>");
        	out.println("</div><div align='right'>");
        	out.println("<table border='1' width='97%' cellspacing='0' cellpadding='0' height='181' bordercolor='#COCOCO' bordercolorlight='#COCOCO'>");
        	out.println("<tr>");
        	out.println("<td width='100%' bgcolor='#F0F0FF' height='181' valign='top' align='center'><table border='0' width='100%' cellspacing='0' cellpadding='0'>");
	        out.println("<tr>");
        	out.println("<td width='100%' height='30'><p align='center'><small><font face='Verdana' color='#8000FF'><strong>");
        	out.println("</strong></font><font face='Verdana' color='#6767B4'>The question has been successfully Edited.</font></small></td>");
        	out.println("</tr>");
        	out.println("<tr>");
        	out.println("<td width='100%' height='30'><p align='center'><font face='Verdana' color='#6767B4'><small>You may now manage test using the link below.</small></font></td>");
        	out.println("</tr>");
    	    out.println("<tr>");
	        out.println("<td width='100%' height='30'><p align='center'><a href='/servlet/ManageTest?test_code="+test_code+"' target='_self'><font face='Verdana' color='#6767B4'><small>Manage Test</small></font></a></td>");
	        out.println("</tr>");
    	    out.println("</table>");
        	out.println("</td>");
        	out.println("</tr>");
        	out.println("</table>");
        	out.println("</div>");
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
		return "Servlet to edit Question in a test table";
	}	
}