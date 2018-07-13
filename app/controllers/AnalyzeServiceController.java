package controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Inject;

import com.fasterxml.jackson.databind.JsonNode;

import edu.isu.adl.classifer.WOSClassifer;
import edu.isu.tools.JsonFileFilter;
import play.mvc.*;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.db.Database;
import play.libs.Json;
import play.Play;
import play.libs.Json;


public class AnalyzeServiceController extends Controller{
	private FormFactory formFactory;
	
	@Inject
	public AnalyzeServiceController(FormFactory formFactory) {
		this.formFactory = formFactory;
	}
    public Result index() {
    	
        return ok(views.html.analyze.render());
    }
    
    public Result analyze() throws ParseException {
    	DynamicForm requestData = formFactory.form().bindFromRequest();
    	String username = requestData.get("username");
    	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    	Date date = dateFormat.parse(requestData.get("analyzeDate"));
    	
    	//Generate json result based on username and date
    	WOSClassifer classifer = new WOSClassifer();
		classifer.classify(username, date);
		
		//Load json file and display
		String path = Play.application().path().getPath() + "/public/tmpFiles/Json";
    	//File jsonFiles = new File(path);
    	//String[] jsonName = jsonFiles.list(new JsonFileFilter());
    	File jsonFile = new File(path + "/part-00000");
    	if(jsonFile.exists()) {
    		System.out.println(jsonFile.getPath());
    		try {
    			InputStream is = new FileInputStream(jsonFile);
    			try {
				JsonNode jsValue = Json.parse(is);
				return ok(jsValue);
    			}finally {
    				is.close();
    			}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	return ok("Cannot find json file");
    }
}
