import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;
import java.util.*;

public class TestConfiguration extends HttpServlet{
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
	throws ServletException,IOException
	{
		Connection con = null;
		res.setContentType("text/html");
		res.setHeader("Cache-Control","no-store");
		PrintWriter out=res.getWriter();
		HttpSession session=req.getSession(false);
		String aduser=null,test_name=null,test_details=null,test_created=null,test_updated=null;
		int test_code=0,test_status=0,test_no_subject=0,test_duration=0;
		aduser = (String)session.getValue("aduser");
		java.util.Date time_comp=new java.util.Date(System.currentTimeMillis()-20*60*1000);
		java.util.Date accessed=new java.util.Date(session.getLastAccessedTime());
		if(session==null||aduser==null||accessed.before(time_comp)){
			    session.invalidate();
			    out.println("<H2>Your Session has expired </H2>");
				out.println("<a href='../examonline/admin.html'>Click Here</a> To Re-Login");
				return;
		}
		try{
			String tcode = req.getParameter("test_code");
			test_code=Integer.parseInt(tcode);
			test_name = req.getParameter("test_name");
			test_created = req.getParameter("test_created");
			test_updated = test_created;
			String tdur = req.getParameter("test_duration");
			test_duration=Integer.parseInt(tdur);
			test_details = req.getParameter("test_details");
			String number = req.getParameter("number");
			int count = Integer.parseInt(number);
			test_no_subject=count;		
			int i;
			String q=null;		
			String s[] = new String[count];
			int k[] = new int[count];
			for(i=0;i<count;i++){
				s[i] = req.getParameter("sub"+i);
		    	q= req.getParameter("sub"+i+"_quesno");
		    	if((s[i]==null)||(q==null)){
		  			out.println("<H2>Some field or fields may have been left empty</H2>");
		  			out.println("<a href='javascript:history.go(-1)'>Click Here</a>  to go back to previous page & try again");
		  			return;		
		  		}		
				if(s[i].equals("")||q.equals("")){					
		  			out.println("<H2>Some field or fields may have been left empty</H2>");
		  			out.println("<a href='javascript:history.go(-1)'>Click Here</a>  to go back to previous page & try again");
		  			return;
		  		}
		  		if(validate1(s[i])&&validate2(q));
		  		else{
		  			out.println("<H2>Some rules to fill the form have not been followed</H2>");
		  			out.println("<a href='javascript:history.go(-1)'>Click Here</a>  to go back to previous page & try again");
		  			return;	
				}
		    	k[i]=Integer.parseInt(q);
			}
			con = pool.getConnection();
			Statement stmt = con.createStatement();
			String table_name=null;
			stmt.executeUpdate("insert into test_master values("+test_code+",'"+aduser+"','"+test_name+"','"+test_created+"','"+test_updated+"',"+test_duration+","+test_status+","+test_no_subject+",'"+test_details+"')");		
			for(i=0;i<s.length;i++){
				table_name="test_"+test_code+"_"+i;
				stmt.executeUpdate("insert into test_info values('"+table_name+"',"+test_code+",'"+s[i]+"',"+k[i]+")");
				stmt.executeUpdate("Create table "+table_name+"(id int PRIMARY KEY,table_statement char(200),table_question char(200) NOT NULL,table_image TINYINT,table_code char(200),ans_ch_1 char(200) NOT NULL,ans_ch_2 char(200) NOT NULL,ans_ch_3 char(200) NOT NULL,ans_ch_4 char(200) NOT NULL,ans_ch_correct TINYINT NOT NULL)");
			}
			stmt.close();
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
		    out.println("<td width='100%'><font face='Verdana' color='#FFFFFF'><small><strong>Test configuration complete...</strong></small></font></td>");
		    out.println("</tr>");
		    out.println("</table>");
		    out.println("</div><div align='right'>");
		    out.println("<table border='1' width='97%' cellspacing='0' cellpadding='0' height='181' bordercolor='#COCOCO' bordercolorlight='#COCOCO'>");
		    out.println("<tr>");
		    out.println("<td width='100%' bgcolor='#F0F0FF' height='181' valign='top' align='center'><table border='0' width='100%' cellspacing='0' cellpadding='0'>");
		    out.println("<tr>");
		    out.println("<td width='100%' height='30'><p align='center'><font face='Verdana' color='#8000FF'><strong><small>Congratulations!"+aduser+" </small></strong></font></td>");
		    out.println("</tr>");
		    out.println("<tr>");
		    out.println("<td width='100%' height='30'><p align='center'><font face='Verdana' color='#6767B4'><small>You have successfully configured the test - "+test_name+" </small></font></td>");
		    out.println("</tr>");
		    out.println("<tr>");
		    out.println("<td width='100%' height='30'><p align='center'><font face='Verdana' color='#6767B4'><small>You may now add questions to the test to activate it.</small></font></td>");
		    out.println("</tr>");
		    out.println("<tr>");
		    out.println("<td width='100%' height='30'><p align='center'><a href='/servlet/ManageTest?test_code="+test_code+"' target='_self'><font face='Verdana' color='#6767B4'><small>Manage This Test</small></font></a></td>");
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
	public boolean validate1(String value){
    	String valid=" 0123456789_+-abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    	String start="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    	boolean ok = true;
    	String temp;
    	if (value.length()>20){
    		return false;
    	}
    	char st = value.charAt(0);
    	if (start.indexOf(st) == -1){
    		return false;
    	}    	
    	for (int i=0; i<value.length(); i++) {
    		temp = "" + value.substring(i, i+1);
    		if (valid.indexOf(temp) == -1){
    			ok =false;
    			break;
    		}
    	}
    	return ok;
    }
    public boolean validate2(String value){
    	String valid="0123456789";
    	boolean ok = true;
    	if(value.equals("0")||value.equals("00")||value.equals("000"))
    	{
    		return false;
    	}
    	String temp;
    	if (value.length()>3){
    		return false;
    	}
    	for (int i=0; i<value.length(); i++) {
    		temp = "" + value.substring(i, i+1);
    		if (valid.indexOf(temp) == -1){
    			ok =false;
    			break;
    		}
    	}
    	return ok;
    }
	public String getServletInfo(){
		return "Test Cofiguration complete";
	}	
}