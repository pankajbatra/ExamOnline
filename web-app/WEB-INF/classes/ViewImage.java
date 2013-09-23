import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
public class ViewImage extends HttpServlet{
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
	public void doGet(HttpServletRequest req,HttpServletResponse res) throws ServletException,IOException{		
		Connection con=null;
		String table_name=null;
		int id=0;
		table_name = req.getParameter("table_name");
		String temp_id=req.getParameter("id");
		id=Integer.parseInt(temp_id);
		ServletOutputStream out=res.getOutputStream();
		try{	
		    con = pool.getConnection();			
			Statement stmt=con.createStatement();			
			ResultSet rs = stmt.executeQuery("select * FROM question_images where table_name='"+table_name+"' AND id="+id);
			if (rs.next()) {
				String file_name=rs.getString("file_name");
				int ex=file_name.indexOf('.');
				String exten=file_name.substring(ex).toLowerCase();
			    if(exten.equals(".gif")) res.setContentType("image/gif");
				else if(exten.equals(".png")) res.setContentType("image/png");
				else if(exten.equals(".jpg")||exten.equals(".jpeg")) res.setContentType("image/jpeg");								
				InputStream in = rs.getBinaryStream("image");
				if(in != null)
                {
                    byte abyte0[] = new byte[1024];
                    int j;
                    while((j = in.read(abyte0)) > 0) 
                    {
                        if(out == null)
                            out = res.getOutputStream();
                        out.write(abyte0, 0, j);
                    }
                }
			} 
			rs.close();	
			stmt.close();
		}
		catch(Exception e){
			try{				
				System.out.println(e.getMessage());				
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
		return "Servlet to view Image";
	}	
}