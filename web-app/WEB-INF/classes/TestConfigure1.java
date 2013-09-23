import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;
import java.util.*;
import java.text.*;

public class TestConfigure1 extends HttpServlet{
	private ConnectionPool pool;
	public void init(ServletConfig config)throws ServletException{
		super.init(config);
		try{
			pool = new ConnectionPool(1,1);
		}
		catch(Exception e){
			throw new UnavailableException(this,"could not create a connection");
	    }
	}
	public void doPost(HttpServletRequest req,HttpServletResponse res)
	throws ServletException,IOException{
		res.setContentType("text/html");
		PrintWriter out = res.getWriter();
		res.setHeader("Cache-Control","no-store");
		Connection con = null;
		String aduser;
		HttpSession session=req.getSession(false);
		aduser = (String)session.getValue("aduser");
		java.util.Date time_comp=new java.util.Date(System.currentTimeMillis()-20*60*1000);
		java.util.Date accessed=new java.util.Date(session.getLastAccessedTime());
		if(session==null||aduser==null||accessed.before(time_comp)){
			session.invalidate();
			out.println("<H2>Your Session has expired </H2>");
			out.println("<a href='../examonline/admin.html'>Click Here</a> To Re-Login");
			return;
		}
		String test_name = req.getParameter("test_name");
		String test_duration = req.getParameter("test_duration");
		String test_subjects = req.getParameter("test_subjects");
		String test_details = req.getParameter("test_details");
		if((test_name==null)||(test_duration==null)||(test_subjects==null)||(test_details==null))
		{
		  out.println("<H2>Some field or fields may have been left empty</H2>");
		  out.println("<a href='javascript:history.go(-1)'>Click Here</a>  to go back to previous page & try again");
		  return;		
		}		
		if(test_name.equals("")||test_duration.equals("")||test_subjects.equals("")||test_details.equals(""))
		{
		  out.println("<H2>Some field or fields may have been left empty</H2>");
		  out.println("<a href='javascript:history.go(-1)'>Click Here</a>  to go back to previous page & try again");
		  return;
		}
		if(validate1(test_name)&&validate2(test_duration)&&validate3(test_subjects)&&validate4(test_details));
		else
		{
		  out.println("<H2>Some rules to fill the form have not been followed</H2>");
		  out.println("<a href='javascript:history.go(-1)'>Click Here</a>  to go back to previous page & try again");
		  return;	
		}		
		java.util.Date date = new java.util.Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		int test_code;
		int number = Integer.parseInt(test_subjects);
		String test_created = sdf.format(date);		
		try{
			con = pool.getConnection();
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("Select * from test_master");
			int c=0;
			while(rs.next())
			{
				c++;
			}
			rs.close();
			if(c==0){
				test_code = 1;
			}
			else{
				rs=stmt.executeQuery("select MAX(test_code) from test_master");
				while(rs.next()){
					c=rs.getInt("MAX(test_code)");
				}
				rs.close();
				test_code = ++c;
			}
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
			out.println("<td width='100%'><font face='Verdana' color='#FFFFFF'><small><strong>Configure a new test..</strong></small></font></td>");
			out.println("</tr>");
			out.println("</table>");
			out.println("</div><div align='right'>");
			out.println("<table border='1' width='97%' cellspacing='0' cellpadding='0' height='271' bordercolor='#COCOCO' bordercolorlight='#COCOCO'>");
			out.println("<tr>");
			out.println("<td width='100%' bgcolor='#F0F0FF' height='271' valign='top' align='center'>");
			out.println("<form method='POST' action='/servlet/TestConfiguration'>");
			out.println("<table border='0' width='80%' cellspacing='0' cellpadding='0' height='161' <tr>");
			out.println("<tr>");
			out.println("<td width='100%' height='27' valign='top' align='left' colspan='2'><div align='center'><center><p><font face='Verdana' color='#000080'><small>Please Fill the form below to configure an online test.</small></font></td>");
			out.println("</tr>");
			out.println("<tr align='center'>");
			out.println("<td width='43%' height='19' valign='middle' align='right'><font face='Verdana' color='#000080'><small>&nbsp; Test Name:</small></font></td>");
			out.println("<td width='57%' height='30' valign='middle' align='left'>&nbsp;&nbsp;"+test_name+" </td>");
			out.println("</tr>");
			out.println("<tr align='center'>");
			out.println("<td width='43%' height='14' valign='middle' align='right'><font face='Verdana' color='#000080'><small>Test Code:</small></font></td>");
			out.println("<td width='57%' height='30' valign='middle' align='left'>&nbsp;&nbsp;"+test_code+" </td>");
			out.println("</tr>");
			out.println("<tr align='center'>");
			out.println("<td width='43%' height='13' valign='middle' align='right'><small><font face='Verdana' color='#000080'>Date of creation:</font></small></td>");
			out.println("<td width='57%' height='30' valign='middle' align='left'>&nbsp;&nbsp;"+sdf.format(date)+"</td>");
			out.println("</tr>");
			out.println("<tr align='center'>");
			out.println("<td width='43%' height='25' valign='middle' align='right'><font face='Verdana' color='#000080'><small>Test Duration:</small></font></td>");
			out.println("<td width='57%' height='30' valign='middle' align='left'>&nbsp;&nbsp"+test_duration+" <font face='Verdana' color='#000080'><small>min.</small></font></td>");
			out.println("</tr>");
			out.println("<tr align='center'>");
			out.println("<td width='43%' height='6' valign='middle' align='right'><font face='Verdana' color='#000080'><small>Test Author:</small></font></td>");
			out.println("<td width='57%' height='30' valign='middle' align='left'>&nbsp;&nbsp;"+aduser+" </td>");
			out.println("</tr>");
			int i;
			for(i=0;i<number;i++){
				out.println("<tr align='center'>");
				out.println("<td width='43%' height='6' valign='middle' align='right'><font face='Verdana' color='#000080'><small>subject"+(i+1)+":</small></font></td>");
				out.println("<td width='57%' height='30' valign='middle' align='left'>&nbsp;<input type='text' name='sub"+i+"' size='15' style='border: 1px solid rgb(0,0,128)'>&nbsp;&nbsp;&nbsp; <font face='Verdana' color='#000080'><small>No. Of Ques. </small></font>");
				out.println("<input type='text' name='sub"+i+"_quesno' size='4' style='border: 1px solid rgb(0,0,128)'></td>");
				out.println("</tr>");
			}
			out.println("<tr>");
			out.println("<td align='right' height='30' vAlign='center' width='43%'></td>");
			out.println("<td align='left' height='30' vAlign='center' width='57%'>&nbsp;<font face='Verdana' color='#000000'><small>*subject name maximum-20 chars</small><br>");
			out.println("<small> *no of questions maximum-999</small></font></td></tr>");
			out.println("<tr align='center'>");
			out.println("<td width='43%' height='30' valign='middle' align='right'><font face='Verdana' color='#000080'><small>Other Information:</small></font></td>");
			out.println("<td width='57%' height='30' valign='middle' align='left'>&nbsp;&nbsp;"+test_details+" </td>");
			out.println("</tr>");
			out.println("<tr align='center'>");
			out.println("<td width='43%' height='37' valign='middle' align='right'></td>");
			out.println("<td width='57%' height='37' valign='top' align='left'>&nbsp; <input type='submit' value='Submit' name='B1' style='background-color: rgb(196,196,255); color: rgb(0,0,0); border: 1px solid rgb(0,0,128)'> &nbsp; </td>");
			out.println("</tr>");
			out.println("</table>");
			out.println("<INPUT TYPE='hidden' NAME='test_name' VALUE='"+test_name+"'>");
			out.println("<INPUT TYPE='hidden' NAME='test_code' VALUE='"+test_code+"'>");
			out.println("<INPUT TYPE='hidden' NAME='test_created' VALUE='"+test_created+"'>");
			out.println("<INPUT TYPE='hidden' NAME='test_duration' VALUE='"+test_duration+"'>");
			out.println("<INPUT TYPE='hidden' NAME='number' VALUE="+number+">");
			out.println("<INPUT TYPE='hidden' NAME='test_details' VALUE='"+test_details+"'>");
			out.println("</form>");
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
    	//char st = value.charAt(0);
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
    
    public boolean validate3(String value){
    	String valid="0123456789";
    	boolean ok = true;
    	//char st = value.charAt(0);
    	if(value.equals("0")||value.equals("00"))
    	{
    		return false;
    	}
    	String temp;
    	if (value.length()>2){
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
    
    public boolean validate4(String value){
    	String valid=" .!@#$%^&*()_+|=?/><[]{},:;0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    	boolean ok = true;
    	if (value.length()>200){
    		return false;
    	}
    	String temp;    	
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
	return "Test Configuration Servlet handling test form part 2";
    }
}
                
                

		
	