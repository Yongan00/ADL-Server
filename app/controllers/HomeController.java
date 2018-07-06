package controllers;

import play.mvc.*;
import play.db.*;
import java.io.File;
import java.sql.*;
import javax.inject.*;
import edu.isu.adl.datafilter.DataFilter;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {
	private Database db;
	@Inject
	public HomeController(@NamedDatabase("default") Database db) {
		this.db = db;
	}
    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    public Result index() {
    	
        return ok(views.html.index.render());
    }
    
    public Result upload() {
        Http.MultipartFormData<File> body = request().body().asMultipartFormData();
        Http.MultipartFormData.FilePart<File> statusFilePart = body.getFile("statusFile");
        Http.MultipartFormData.FilePart<File> motionFilePart = body.getFile("motionFile");
        if (!statusFilePart.getFilename().isEmpty() && !motionFilePart.getFilename().isEmpty()) {
        	//String username = "PhilSamsang";
        	//String timeStamp = "20160706_161756";
        	String statusFileUser = statusFilePart.getFilename().split("_")[0];
        	String statusFileTimeStamp = (statusFilePart.getFilename().split("_"))[1] + statusFilePart.getFilename().split("_")[2].substring(0, 6);
        	String motionFileUser = motionFilePart.getFilename().split("_")[0];
        	String motionFileTimeStamp = motionFilePart.getFilename().split("_")[1] + motionFilePart.getFilename().split("_")[2];
            if(statusFileUser.equals(motionFileUser) && statusFileTimeStamp.equals(motionFileTimeStamp)) {
            	File stutusFile = statusFilePart.getFile();
            	File motionFile = motionFilePart.getFile();
            	
            	//set Database connection
            	try {
            		Connection conn = db.getConnection();
                	DataFilter.filterData(statusFileUser, statusFileTimeStamp, motionFile, stutusFile, conn);
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	
                return ok("File uploaded");
            }else {
            	System.out.println("Two Files Do Not Match");
            	return redirect(routes.HomeController.index());
            }
            
        } else {
            flash("error", "Missing file");
            return badRequest();
        }
    }

}
