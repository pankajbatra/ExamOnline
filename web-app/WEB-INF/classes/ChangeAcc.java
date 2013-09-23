import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import java.util.*;
import java.sql.*;

public class ChangeAcc extends HttpServlet
{
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
	public void doGet(HttpServletRequest req,HttpServletResponse res)throws ServletException,IOException{
		Connection con=null;
		res.setContentType("text/html");		
		res.setHeader("Cache-Control","no-store");
		HttpSession session=req.getSession(false); 
		PrintWriter out = res.getWriter();
		String name="",middle="",last="",username,address="",city="",state="",country="",email="";
		long tele=0,pin=0;
		java.sql.Date dob=null;
		int sex;		
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
			ResultSet rs=stmt.executeQuery("Select * from users where username='"+username+"'");
			while(rs.next()){
				name=rs.getString("name");
				middle=rs.getString("middle");
				last=rs.getString("last");
				address=rs.getString("address");
				city=rs.getString("city");
				state=rs.getString("state");
				country=rs.getString("country");
				pin=rs.getLong("pin");
				dob=rs.getDate("dob");
				sex=rs.getInt("sex");
				tele=rs.getLong("tele");
				email=rs.getString("email");
			}
			rs.close();
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
			out.println("<td width='100%'><font face='Verdana' color='#FFFFFF'><small><strong>Change Account Information..</strong></small></font></td>");
			out.println("</tr>");
			out.println("</table>");
			out.println("</div><div align='right'>");				
			out.println("<table border='1' width='97%' cellspacing='0' cellpadding='0' height='230' bordercolor='#COCOCO' bordercolorlight='#COCOCO'>");
			out.println("<tr>");
			out.println("<td width='100%' bgcolor='#F0F0FF' height='230' valign='top' align='center'><form method='POST' action='/servlet/ConfirmChngAcc'><br><br>");
			out.println("<table border='0' width='79%' cellspacing='0' cellpadding='0' height='161' >");
			out.println("<tr align='center'>");
			out.println("<td width='50%' height='19' valign='middle' align='right'><font face='Verdana' color='#000080'><small>&nbsp;First Name:</small></font></td>");
			out.println("<td width='50%' height='30' valign='middle' align='left'>&nbsp; <input type='text' name='name' value='"+name+"' style='border: 1px solid rgb(0,0,128)' size='20'></td>");
			out.println("</tr>");
			out.println("<tr align='center'>");
			out.println("<td width='50%' height='27' valign='middle' align='right'><font face='Verdana' color='#000080'><small>Middle Name:</small></font></td>");
			out.println("<td width='50%' height='30' valign='middle' align='left'>&nbsp; <input type='text' name='middle' ");
			if(middle.equals("null"));
            else out.println("value='"+middle+"'");
			out.println("style='border: 1px solid rgb(0,0,128)' size='20'></td>");
			out.println("</tr>");
			out.println("<tr align='center'>");
			out.println("<td width='50%' height='27' valign='middle' align='right'><font face='Verdana' color='#000080'><small>Last Name:</small></font></td>");
			out.println("<td width='50%' height='30' valign='middle' align='left'>&nbsp; <input type='text' name='last' value='"+last+"' style='border: 1px solid rgb(0,0,128)' size='20'></td>");
			out.println("</tr>");
			out.println("<tr align='center'>");
			out.println("<td width='50%' height='27' valign='middle' align='right'><font face='Verdana' color='#000080'><small>Address:</small></font></td>");
			out.println("<td width='50%' height='30' valign='middle' align='left'>&nbsp;&nbsp;<textarea rows='2' name='address' cols='15' style='border: 1px solid rgb(0,0,128)'>"+address+"</textarea> </td>");
			out.println("</tr>");
			out.println("<tr align='center'>");
			out.println("<td width='50%' height='27' valign='middle' align='right'><font face='Verdana' color='#000080'><small>City:</small></font></td>");
			out.println("<td width='50%' height='30' valign='middle' align='left'>&nbsp; <input type='text' name='city' value='"+city+"' style='border: 1px solid rgb(0,0,128)' size='20'></td>");
			out.println("</tr>");
			out.println("<tr align='center'>");
			out.println("<td width='50%' height='27' valign='middle' align='right'><font face='Verdana' color='#000080'><small>State:</small></font></td>");
			out.println("<td width='50%' height='30' valign='middle' align='left'>&nbsp; <input type='text' name='state' value='"+state+"' style='border: 1px solid rgb(0,0,128)' size='20'></td>");
			out.println("</tr>");
			out.println("<tr align='center'>");
			out.println("<td width='50%' height='27' valign='middle' align='right'><font face='Verdana' color='#000080'><small>Country:</small></font></td>");
			out.println("<td width='50%' height='30' valign='middle' align='left'>&nbsp; <input type='text' name='country' value='"+country+"' style='border: 1px solid rgb(0,0,128)' size='20'></td>");
			out.println("</tr>");
			out.println("<tr align='center'>");
			out.println("<td width='50%' height='27' valign='middle' align='right'><font face='Verdana' color='#000080'><small>Pin:</small></font></td>");
			out.println("<td width='50%' height='30' valign='middle' align='left'>&nbsp; <input type='text' name='pin' value='"+pin+"' style='border: 1px solid rgb(0,0,128)' size='20'></td>");
			out.println("</tr>");
			out.println("<tr align='center'>");
			out.println("<td width='50%' height='27' valign='middle' align='right'><font face='Verdana' color='#000080'><small>Telephone No:</small></font></td>");
			out.println("<td width='50%' height='30' valign='middle' align='left'>&nbsp; <input type='text' name='tele' value='"+tele+"' style='border: 1px solid rgb(0,0,128)' size='20'></td>");
			out.println("</tr>");
			out.println("<tr align='center'>");
			out.println("<td width='50%' height='27' valign='middle' align='right'><font face='Verdana' color='#000080'><small>Date of Birth:</small></font></td>");
			out.println("<td width='50%' height='30' valign='middle' align='left'>&nbsp; <input type='text' name='dob' value='"+(dob.getYear()+1900)+"-"+(dob.getMonth()+1)+"-"+dob.getDate()+"' style='border: 1px solid rgb(0,0,128)' size='20'></td>");
			out.println("</tr>");
			out.println("<tr align='center'>");
			out.println("<td width='50%' height='27' valign='middle' align='right'><font face='Verdana' color='#000080'><small>Sex:</small></font></td>");
			out.println("<td width='50%' height='30' valign='middle' align='left'>&nbsp; <select name='sex' size='1' style='border: 1px solid rgb(0,0,128)'><option selected value='1'>Male</option><option value='2'>Female</option></select></td>");
			out.println("</tr>");
			out.println("<tr align='center'>");
			out.println("<td width='50%' height='27' valign='middle' align='right'><font face='Verdana' color='#000080'><small>Email Address:</small></font></td>");
			out.println("<td width='50%' height='30' valign='middle' align='left'>&nbsp; <input type='text' name='email' value='"+email+"' style='border: 1px solid rgb(0,0,128)' size='20'></td>");
			out.println("</tr>");
			out.println("<tr align='center'>");
			out.println("<td width='100%' height='30' valign='middle' colspan='2' align='middle'>");
			out.println("<input type='submit' value='Submit' name='B1' style='background-color: rgb(196,196,255); color: rgb(0,0,0); border: 1px solid rgb(0,0,128)'></td>");
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
		return "Servlet for Changing User's Account Information";
	}
}		
								
				