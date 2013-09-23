import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import java.util.*;
import java.sql.*;

public class UseChngAcc1 extends HttpServlet
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
		out.println("<html>");
		out.println("<head><title>ExamOnline - Change User Account Infromation</title></head>");
		out.println("<body bgcolor='#B3B3D9'>");		
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
			out.println("<p align='center'><font color='#000000' face='Verdana'><big><big><strong>Change User Information</strong></big></big></font></p><hr>");
			out.println("<p align='center'><small><strong><font face='Verdana'>Please re-fill the form below as per the instructions(<small><font color='#FF0000'>*</font> indicates a compulsory field.</small>)<small>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</small></font></strong></small></p>");
            out.println("<Form method='POST' action='/servlet/ConfirmChngAcc'>");
            out.println("<div align='center'><center><table border='0' width='100%' cellspacing='0' cellpadding='0'>");
            out.println("<tr>");
            out.println("<td width='37%' align='right' valign='top'><small><strong><font face='Verdana'><font color='#FF0000'>*</font>First Name:</font></strong></small></td>");
            out.println("<td width='36%' height='30' align='left' valign='top'><blockquote><div align='left'><p><input type='text' name='name' value='"+name+"' size='20' tabindex='1' maxlength='15'></p></div></blockquote></td>");
            out.println("<td width='34%' height='30' align='left' valign='top'></td>");
            out.println("</tr>");
            out.println("<tr align='center'>");
            out.println("<td width='30%' align='right' valign='top'><small><strong><font face='Verdana'>Middle Name:</font></strong></small></td>");
            out.println("<td width='36%' height='30' align='left' valign='top'><blockquote><div align='left'><p><input type='text' name='middle' ");
            if(middle.equals("null"));
            else out.println("value='"+middle+"'");
            out.println("size='20' tabindex='2'></p></div></blockquote></td>");
            out.println("<td width='34%' height='30' align='left' valign='top'></td>");
            out.println("</tr>");
            out.println("<tr align='center'>");
            out.println("<td width='30%' align='right' valign='top'><small><strong><font face='Verdana'      color='#FF0000'>*</font><font face='Verdana'>Last Name:</font></strong></small></td>");
            out.println("<td width='36%' height='30' align='left' valign='top'><blockquote><div align='left'><p><input type='text' name='last' value='"+last+"' size='20' tabindex='3'></p></div></blockquote></td>");
            out.println("<td width='34%' height='30' align='left' valign='top'></td>");
            out.println("</tr>"); 
            out.println("<tr align='center'>");
            out.println("<td width='30%' align='right' valign='top'><small><strong><font face='Verdana'      color='#FF0000'>*</font><font face='Verdana'>Address:</font></strong></small></td>");
            out.println("<td width='36%' height='30' align='left' valign='top'><blockquote>        <div align='left'><p><textarea rows='2' name='address' cols='15'>"+address+"</textarea></p>        </div>      </blockquote>      </td>");
            out.println("<td width='34%' height='30' align='left' valign='top'></td>");
            out.println("</tr>");
            out.println("<tr align='center'>");
            out.println("<td width='30%' align='right' valign='top'><small><strong><font face='Verdana'      color='#FF0000'>*</font><font face='Verdana'>City:</font></strong></small></td>");
            out.println("<td width='36%' height='30' align='left' valign='top'><blockquote>        <div align='left'><p><input type='text' name='city' value='"+city+"' size='20'></p>        </div>      </blockquote>      </td>");
            out.println("<td width='34%' height='30' align='left' valign='top'></td>");
            out.println("</tr>");
            out.println("<tr align='center'>");
            out.println("<td width='30%' align='right' valign='top'><small><strong><font face='Verdana'      color='#FF0000'>*</font><font face='Verdana'>State:</font></strong></small></td>");
            out.println("<td width='36%' height='30' align='left' valign='top'><blockquote>        <div align='left'><p><input type='text' name='state' value='"+state+"' size='20'></p>        </div>      </blockquote>      </td>");
            out.println("<td width='34%' height='30' align='left' valign='top'></td>");
            out.println("</tr>");
            out.println("<tr align='center'>");
            out.println("<td width='30%' align='right' valign='top'><small><strong><font face='Verdana'      color='#FF0000'>*</font><font face='Verdana'>Country:</font></strong></small></td>");
            out.println("<td width='36%' height='30' align='left' valign='top'><blockquote>        <div align='left'><p><input type='text' name='country' value='"+country+"' size='20'></p>        </div>      </blockquote>      </td>");
            out.println("<td width='34%' height='30' align='left' valign='top'></td>");
            out.println("</tr>");
            out.println("<tr align='center'>");
            out.println("<td width='30%' align='right' valign='top'><small><strong><font face='Verdana'      color='#FF0000'>*</font><font face='Verdana'>Pin:</font></strong></small></td>");
            out.println("<td width='70%' height='30' align='left' valign='top' colspan='2'><blockquote>        <div align='left'><p><input type='text' name='pin' value='"+pin+"' size='20'><font face='Verdana' color='#FFFF00'><small><small><small><strong> Only numeral characters &amp; max.6 digits.</strong></small></small></small></font></p></div></blockquote></td>");
            out.println("</tr>");
            out.println("<tr align='center'>");
            out.println("<td width='30%' align='right' valign='top'><small><strong><font face='Verdana' color='#FF0000'>*</font><font face='Verdana'>Telephone No:</font></strong></small></td>");
            out.println("<td width='70%' height='30' align='left' valign='top' colspan='2'><blockquote><div align='left'><p><input name='tele' value='"+tele+"' size='20'><font face='Verdana' color='#FFFF00'><small><small><small><strong>&nbsp; Only numerals characters, maximum 20        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;        digits, for example if country code is&nbsp; 91,        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;        STD code is 11 &amp; local number is 7253539,        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;        Then It will be written as&nbsp; 91117253539 .</strong></small></small></small></font></p></div></blockquote></td>");
            out.println("</tr>");
            out.println("<tr align='center'>");
            out.println("<td width='30%' align='right' valign='top'><strong><small><font face='Verdana'      color='#FF0000'>*</font><font face='Verdana'>Date of Birth</small> <small><small>(YYYY-MM-DD):</small></small></font></strong></td>");
            out.println("<td width='36%' height='30' align='left' valign='top'><blockquote>        <div align='left'><p><input type='text' name='dob' value='"+(dob.getYear()+1900)+"-"+(dob.getMonth()+1)+"-"+dob.getDate()+"'size='20'></p>        </div>      </blockquote>      </td>");
            out.println("<td width='34%' height='30' align='left' valign='top'></td>");
            out.println("</tr>");
            out.println("<tr align='center'>");
            out.println("<td width='30%' align='right' valign='top'><small><strong><font face='Verdana'      color='#FF0000'>*</font><font face='Verdana'>Sex:</font></strong></small></td>");
            out.println("<td width='36%' height='30' align='left' valign='top'><blockquote><div align='left'><p><select name='sex' size='1'><option selected value='1'>Male</option><option value='2'>Female</option></select></p></div></blockquote></td>");
            out.println("<td width='34%' height='30' align='left' valign='top'></td>");
            out.println("</tr>");
            out.println("<tr align='center'>");
            out.println("<td width='30%' align='right' valign='top'><small><strong><font face='Verdana'      color='#FF0000'>*</font><font face='Verdana'>Email Address:</font></strong></small></td>");
            out.println("<td width='36%' height='30' align='left' valign='top'><blockquote>        <div align='left'><p><input type='text' name='email' value='"+email+"' size='20'></p>        </div>      </blockquote>      </td>");
            out.println("<td width='34%' height='30' align='left' valign='top'></td>");
            out.println("</tr>");
            out.println("<tr align='center'>");
            out.println("<td width='30%' align='right' valign='top'><div align='right'></div></td>");
            out.println("<td width='70%' valign='top' height='30' align='left' colspan='2'><div align='left'><p><input      type='submit' value='Submit' name='B1'> <input type='reset' value=' Reset ' name='B2'></td>");
            out.println("</tr>");
            out.println("</table></center></div>");
			out.println("</form></body></html>");
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
								
				