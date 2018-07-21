package controllers;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;

import javax.inject.Inject;

import edu.isu.adl.datafilter.DataFilter;
import play.db.Database;
import play.db.NamedDatabase;
import play.mvc.*;


public class UploadFileController extends Controller{
	private Database db;
	@Inject
	public UploadFileController(@NamedDatabase("default") Database db) {
		this.db = db;
	}
    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    public Result index() {
    	
        return Results.ok(views.html.upload.render());
    }
    
    public Result upload() {
        Http.MultipartFormData<File> body = Controller.request().body().asMultipartFormData();
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
            	File statusFile = statusFilePart.getFile();
            	File motionFile = motionFilePart.getFile();
            	
            	//set Database connection
            	try {
            		Connection conn = db.getConnection();
                	DataFilter.filterData(statusFileUser, statusFileTimeStamp, motionFile, statusFile, conn);
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	
                return Results.ok("File uploaded");
            }else {
            	System.out.println("Two Files Do Not Match");
            	return Results.redirect(routes.UploadFileController.index());
            }
            
        } else {
            Controller.flash("error", "Missing file");
            return Results.badRequest();
        }
    }

}
