import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import java.sql.*;

public class ConfirmChngAcc extends HttpServlet
{
	private ConnectionPool pool;
	public void init(ServletConfig config) throws ServletException
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
	throws ServletException,IOException
	{
		Connection con=null;
		res.setContentType("text/html");
		res.setHeader("Cache-Control","no-store");
		PrintWriter out = res.getWriter();
		HttpSession session = req.getSession(false);
		String name="",middle="",last="",username,address="",city="",state="",country="",email="",sex="",tele="",dob="",pin="";
		try
		{			
			if(session==null)
			{
				out.println("<H2>Your session has expired</H2>");
				out.println("<a href='../examonline/user.html'>Click here</a>to Re-Login");
				return;
			}
			java.util.Date time_comp = new java.util.Date(System.currentTimeMillis()-20*60*1000);
			java.util.Date accessed = new java.util.Date(session.getLastAccessedTime());
			if(accessed.before(time_comp))
			{
				session.invalidate();
				out.println("<H2>Your session has expired</H2>");
				out.println("<a href='../examonline/user.html'>Click here</a>to Re-Login");
				return;
			}
			username = (String)session.getValue("username");
			name = req.getParameter("name");
			middle = req.getParameter("middle");
			last = req.getParameter("last");
			address = req.getParameter("address");
			city = req.getParameter("city");
			state = req.getParameter("state");
			country = req.getParameter("country");
			pin = req.getParameter("pin");
			email = req.getParameter("email");
			sex = req.getParameter("sex");
			tele = req.getParameter("tele");
			dob = req.getParameter("dob");			
			if((name==null)||(last==null)||(address==null)||(city==null)||(state==null)||(country==null)||(pin==null)||(dob==null)||(tele==null)||(email==null)){
				out.println("<H2>Some of the Fields were left blank</H2>");
				out.println("<a href='javascript:history.go(-1)'>Click Here</a>  to go back to previous page & try again");
				return;
			}
			if((name.equals(""))||(last.equals(""))||(address.equals(""))||(city.equals(""))||(state.equals(""))||(country.equals(""))||(pin.equals(""))||(dob.equals(""))||(tele.equals(""))||(email.equals("")))
			{
				out.println("<H2>Some of the Fields were left blank</H2>");
				out.println("<a href='javascript:history.go(-1)'>Click Here</a>  to go back to previous page & try again");
				return;
			}
			if(validate1(name));
			else 
			{
				out.println("<H2>Invalid Name Entered</H2>");
				out.println("<a href='javascript:history.go(-1)'>Click Here</a>  to go back to previous page & try again");
				return;
			}
			
			if(validate1(last));
			else 
			{
				out.println("<H2>Invalid Last Name Entered</H2>");
				out.println("<a href='javascript:history.go(-1)'>Click Here</a>  to go back to previous page & try again");
				return;
			}			
			if(validate5(city));
			else 
			{
				out.println("<H2>Invalid City Name Entered</H2>");
				out.println("<a href='javascript:history.go(-1)'>Click Here</a>  to go back to previous page & try again");
				return;
			}
			if(validate5(state));
			else 
			{
				out.println("<H2>Invalid State Name Entered</H2>");
				out.println("<a href='javascript:history.go(-1)'>Click Here</a>  to go back to previous page & try again");
				return;
			}
			if(validate6(country));
			else 
			{
				out.println("<H2>Invalid Country Name Entered</H2>");
				out.println("<a href='javascript:history.go(-1)'>Click Here</a>  to go back to previous page & try again");
				return;
			}
			if(validate7(pin));
			else 
			{
				out.println("<H2>Invalid PinCode Entered</H2>");
				out.println("<a href='javascript:history.go(-1)'>Click Here</a>  to go back to previous page & try again");
				return;
			}
			if(validate8(tele));
			else 
			{
				out.println("<H2>Invalid TelePhone Number Entered</H2>");
				out.println("<a href='javascript:history.go(-1)'>Click Here</a>  to go back to previous page & try again");
				return;
			}
			if(checkdate(dob));
			else
			{
				out.println("<H2>Invalid Date of birth </H2>");
				out.println("<a href='javascript:history.go(-1)'>Click Here</a>  to go back to previous page & try again");
				return;
			}
			if(checkmail(email));
			else
			{
				out.println("<H2>Invalid Email Address</H2>");
				out.println("<a href='javascript:history.go(-1)'>Click Here</a>  to go back to previous page & try again");
				return;
			}
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
			out.println("<td width='100%'><font face='Verdana' color='#FFFFFF'><small><strong>Confirm Changed Account Information..</strong></small></font></td>");
			out.println("</tr>");
			out.println("</table>");
			out.println("</div><div align='right'>");				
			out.println("<table border='1' width='97%' cellspacing='0' cellpadding='0' height='230' bordercolor='#COCOCO' bordercolorlight='#COCOCO'>");
			out.println("<tr>");
			out.println("<td width='100%' bgcolor='#F0F0FF' height='230' valign='top' align='center'><form method='POST' action='/servlet/InsertChngAcc'><br><br>");
			out.println("<table border='0' width='79%' cellspacing='0' cellpadding='0' height='161' >");
			out.println("<tr align='center'>");
			out.println("<td width='50%' height='19' valign='middle' align='right'><font face='Verdana' color='#000080'><small>&nbsp;First Name:</small></font></td>");
			out.println("<td width='50%' height='30' valign='middle' align='left'>&nbsp; "+name+"</td>");
			out.println("</tr>");
			out.println("<tr align='center'>");
			out.println("<td width='50%' height='27' valign='middle' align='right'><font face='Verdana' color='#000080'><small>Middle Name:</small></font></td>");
			out.println("<td width='50%' height='30' valign='middle' align='left'>&nbsp;");
			if(middle.equals("null"));
            else out.println(middle);
			out.println("</td>");
			out.println("</tr>");
			out.println("<tr align='center'>");
			out.println("<td width='50%' height='27' valign='middle' align='right'><font face='Verdana' color='#000080'><small>Last Name:</small></font></td>");
			out.println("<td width='50%' height='30' valign='middle' align='left'>&nbsp; "+last+"</td>");
			out.println("</tr>");
			out.println("<tr align='center'>");
			out.println("<td width='50%' height='27' valign='middle' align='right'><font face='Verdana' color='#000080'><small>Address:</small></font></td>");
			out.println("<td width='50%' height='30' valign='middle' align='left'>&nbsp;&nbsp;"+address+"</td>");
			out.println("</tr>");
			out.println("<tr align='center'>");
			out.println("<td width='50%' height='27' valign='middle' align='right'><font face='Verdana' color='#000080'><small>City:</small></font></td>");
			out.println("<td width='50%' height='30' valign='middle' align='left'>&nbsp; "+city+"</td>");
			out.println("</tr>");
			out.println("<tr align='center'>");
			out.println("<td width='50%' height='27' valign='middle' align='right'><font face='Verdana' color='#000080'><small>State:</small></font></td>");
			out.println("<td width='50%' height='30' valign='middle' align='left'>&nbsp; "+state+"</td>");
			out.println("</tr>");
			out.println("<tr align='center'>");
			out.println("<td width='50%' height='27' valign='middle' align='right'><font face='Verdana' color='#000080'><small>Country:</small></font></td>");
			out.println("<td width='50%' height='30' valign='middle' align='left'>&nbsp; "+country+"</td>");
			out.println("</tr>");
			out.println("<tr align='center'>");
			out.println("<td width='50%' height='27' valign='middle' align='right'><font face='Verdana' color='#000080'><small>Pin:</small></font></td>");
			out.println("<td width='50%' height='30' valign='middle' align='left'>&nbsp; "+pin+"</td>");
			out.println("</tr>");
			out.println("<tr align='center'>");
			out.println("<td width='50%' height='27' valign='middle' align='right'><font face='Verdana' color='#000080'><small>Telephone No:</small></font></td>");
			out.println("<td width='50%' height='30' valign='middle' align='left'>&nbsp; "+tele+"</td>");
			out.println("</tr>");
			out.println("<tr align='center'>");
			out.println("<td width='50%' height='27' valign='middle' align='right'><font face='Verdana' color='#000080'><small>Date of Birth:</small></font></td>");
			out.println("<td width='50%' height='30' valign='middle' align='left'>&nbsp;"+dob+"</td>");
			out.println("</tr>");
			out.println("<tr align='center'>");
			out.println("<td width='50%' height='27' valign='middle' align='right'><font face='Verdana' color='#000080'><small>Sex:</small></font></td>");
			out.println("<td width='50%' height='30' valign='middle' align='left'>&nbsp; ");
			if(Integer.parseInt(sex)==1)out.println("Male");
			else out.println("Female");
			out.println("</td>");
			out.println("</tr>");
			out.println("<tr align='center'>");
			out.println("<td width='50%' height='27' valign='middle' align='right'><font face='Verdana' color='#000080'><small>Email Address:</small></font></td>");
			out.println("<td width='50%' height='27' valign='middle' align='left'>&nbsp;"+email+"</td>");
			out.println("</tr>");
			out.println("<input type=hidden name='name' value='"+name+"'>");
			if(middle!=null&&!middle.equals(""))out.println("<input type=hidden name='middle' value='"+middle+"'>");
			out.println("<input type=hidden name='last' value='"+last+"'>");
			out.println("<input type=hidden name='address' value='"+address+"'>");
			out.println("<input type=hidden name='city' value='"+city+"'>");
			out.println("<input type=hidden name='state' value='"+state+"'>");
			out.println("<input type=hidden name='country' value='"+country+"'>");
			out.println("<input type=hidden name='pin' value='"+pin+"'>");
			out.println("<input type=hidden name='dob' value='"+dob+"'>");
			out.println("<input type=hidden name='sex' value='"+sex+"'>");
			out.println("<input type=hidden name='tele' value='"+tele+"'>");
			out.println("<input type=hidden name='email' value='"+email+"'>");
			out.println("<tr align='center'>");
			out.println("<td width='100%' height='30' colspan='2' valign='center' align='middle'>");
			out.println("<font face='Verdana' color='#000080'><small>If the information displayed above is Correct,<br>Click on confirm to change your information.<br>Or<br> <a href='javascript:history.go(-1)'><font color='#000080'>Click Here</font></a> to go back &amp; fill the information again.</small></font>");
			out.println("</td>");			
			out.println("</tr>");			
			out.println("<tr align='center'>");
			out.println("<td width='100%' height='30' colspan='2' valign='middle' align='middle'>");
			out.println("<input type='submit' value='Confirm' name='B1' style='background-color: rgb(196,196,255); color: rgb(0,0,0); border: 1px solid rgb(0,0,128)'></td>");
			out.println("</tr>");
			out.println("</table>");
			out.println("</form>");
			out.println("</td>");
			out.println("</tr>");
			out.println("</table>");
			out.println("</div>");
			out.println("</body>");
			out.println("</html>");
			out.close();			
		}
		catch(Exception e)
		{
			try{
				out.println("<H2>An Error has occured: "+e.getMessage()+"</H2>");
				out.println("<br><br><a href='javascript:history.go(-1)'>Click Here</a> to go back to previous page & Try Again");
				con.rollback();				
			   }
			catch (Exception ignored) { }
			
		}
		finally
		{
			
				if(con!=null) pool.returnConnection(con);
				out.close();		
		}
    }
    public boolean validate1(String value){
    	String valid=" abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    	boolean ok = true;
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
    public boolean validate5(String value){
    	String valid=" .0123456789-abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    	boolean ok = true;
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
    public boolean validate6(String value){
    	String valid=" .abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    	boolean ok = true;
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
    public boolean validate7(String value){
    	String valid="0123456789";
    	boolean ok = true;
    	String temp;
    	if (value.length()>6){
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
    public boolean validate8(String value){
    	String valid="0123456789";
    	boolean ok = true;
    	String temp;
    	if (value.length()>20){
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
    public boolean checkdate(String value){    	    	
    	boolean ok=true;
    	String date,month,year;
    	int d=value.indexOf("-");
    	int m=value.lastIndexOf("-");
    	if(d==-1||d==m) return false;    	
    	year=value.substring(0,d);
    	month=value.substring(d+1,m);
    	date=value.substring(m+1);    	
    	if(Integer.parseInt(date)>31||Integer.parseInt(date)<1) return false;
    	if(Integer.parseInt(month)>12||Integer.parseInt(month)<1) return false;
    	java.util.Date today=new java.util.Date();
    	int y=today.getYear()+1900;    	
    	if(Integer.parseInt(year)>y-3||Integer.parseInt(year)<y-120) return false;
    	return true;
    }
    public boolean checkmail(String value) {
    	int atsign,lastsign;
    	atsign=value.indexOf("@");
    	lastsign=value.lastIndexOf("@");    	
    	if(atsign==-1||lastsign!=atsign) return false;    	
    	atsign=value.lastIndexOf(".");
    	if(atsign==-1) return false;
    	int len=value.length();
    	if(atsign==len-2);
    	else{
    		String domain=value.substring(atsign+1);
    		if(domain.equals("com")||domain.equals("COM")||domain.equals("NET")||domain.equals("ORG")||domain.equals("net")||domain.equals("org")||domain.equals("edu")||domain.equals("EDU")||domain.equals("int")||domain.equals("INT")||domain.equals("MIL")||domain.equals("mil")||domain.equals("gov")||domain.equals("GOV")||domain.equals("arpa")||domain.equals("ARPA")||domain.equals("biz")||domain.equals("BIZ")||domain.equals("aero")||domain.equals("name")||domain.equals("coop")||domain.equals("info")||domain.equals("INFO")||domain.equals("pro")||domain.equals("museum"));
    		else return false;
    	}
    	atsign=value.indexOf(" ");    	
    	if(atsign!=-1)return false;
    	return true;
    }
    public String getServletInfo(){
		return "This servlet validates all information sent for changing user information & asks user for confirmation";
	}
}
		
		