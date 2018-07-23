package controllers;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Inject;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import edu.isu.adl.classifer.WOSClassifer;
import play.mvc.*;
import play.data.DynamicForm;
import play.data.FormFactory;


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
		JsonNode jsResult = classifer.classify(username, date);
		
		//Load json and display
    	if(jsResult.size() != 0) {
    		return ok(views.html.analyzeResult.render(jsResult.toString(), username, dateFormat.format(date)));
    	}
    	return ok("Cannot find json file");
    }
}
