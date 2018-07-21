package controllers;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.inject.Inject;

import edu.isu.adl.datafilter.DataFilter;
import play.db.Database;
import play.db.NamedDatabase;
import play.mvc.*;


public class UploadFilesController extends Controller{
	private Database db;
	@Inject
	public UploadFilesController(@NamedDatabase("default") Database db) {
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
        List<Http.MultipartFormData.FilePart<File>> fileParts = body.getFiles();
        //Http.MultipartFormData.FilePart<File> statusFiles = body.getFile("statusFiles[0]");
        //The POST request send files in pair: (.csv, _motion.csv)
        for(int i=0; i < fileParts.size(); i++) {
        	File statusFile = fileParts.get(i).getFile();
        	File motionFile = fileParts.get(i+1).getFile();
        	String user = fileParts.get(i).getFilename().split("_")[0];
        	String timeStamp = (fileParts.get(i).getFilename().split("_"))[1] + fileParts.get(i).getFilename().split("_")[2].substring(0, 6);
  	
			try {
				Connection conn = db.getConnection();
	        	DataFilter.filterData(user, timeStamp, motionFile, statusFile, conn);
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			i++;
        }
        return ok("UploadFiles Success!");
    }

}

