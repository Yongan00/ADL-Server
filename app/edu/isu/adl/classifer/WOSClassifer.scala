package edu.isu.adl.classifer

import java.util.Properties
import java.util.Date
import java.lang.Object
import java.sql.Date
import java.sql.Connection
import com.fasterxml.jackson.databind._
import com.fasterxml.jackson.databind.node.ArrayNode
import org.apache.spark.SparkContext._
import org.apache.log4j._
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.SaveMode
import org.apache.spark.ml.PipelineModel
import org.apache.spark.ml.classification.DecisionTreeClassificationModel

class WOSClassifer {
  def classify(username:String, date:java.util.Date): JsonNode = {
    // Set the log level to only print errors
    Logger.getLogger("org").setLevel(Level.ERROR)
    
    //Set up database Conf
    val url = "jdbc:mysql://localhost:3306/adl?useSSL=false"
    val tableName = "adl.record"
    val dbUsername = "adlDev_yongan"
    val dbPassword = "2375033"
    
    val ss = SparkSession
            .builder()
            .appName("WOSClassifer")
            .master("local[*]")
            .getOrCreate()

    //Define SQL Query Statement
    val dateInSQL = new java.sql.Date(date.getTime)
    val startTime = dateInSQL.toString() + " 00:00:00"
    val endTime = dateInSQL.toString() + " 23:59:59"
    val query = """
  (select r.*, UNIX_timeStamp(timestamp) as timestampInUnix
  from adl.record as r join adl.profile as p on r.userID=p.id
  where p.username='""" + username + """' AND
  unix_timestamp(timestamp) >= UNIX_TIMESTAMP('""" + startTime + """') AND 
  unix_timestamp(timestamp) < UNIX_TIMESTAMP('""" + endTime + """')) foo
  """
    
    //Connect
    val connectionProperties = new Properties()
    connectionProperties.setProperty("user", dbUsername)
    connectionProperties.setProperty("password", dbPassword)
    val jdbcDF = ss.read.jdbc(url, query, connectionProperties)
    //jdbcDF.printSchema()
    
    //load trained model from disk
    val workOrSleepModel = PipelineModel.load("C:\\Users\\yongan\\CCLearning\\CC\\spark\\SparkScala_Workspace\\ADLServer\\data\\workOrSleepModel")
    val bodyActionModel = PipelineModel.load("C:\\Users\\yongan\\CCLearning\\CC\\spark\\SparkScala_Workspace\\ADLServer\\data\\bodyAction")
    //val treeModel = model.stages(4).asInstanceOf[DecisionTreeClassificationModel]
    //println(s"Learned classification tree model:\n ${treeModel.toDebugString}")
    
    val workOrSleepResult = workOrSleepModel.transform(jdbcDF).drop("indexedActionLabel","workOrSleepFeatures", "indexedWOSFeatures", "rawPrediction", "probability", "indexedWorkOrSleepPredic")
    val finalResult = bodyActionModel.transform(workOrSleepResult)
    //finalResult.printSchema()
    
    //Write result to a formatted Json file
    //finalResult.select("userID", "timeStamp", "workOrSleepPredic", "bodyActionPredic")
          //.write.csv("C:\\Users\\yongan\\CCLearning\\CC\\RecognitionOfADL\\data\\PhilAmes20160706_20160829\\PhilSamsang20160706_20160803\\myTrainingModel\\report")
    
    val jsonDS = finalResult.select("userID", "timeStampInUnix", "workOrSleepPredic", "bodyActionPredic").toJSON.collect()
    val mapper: ObjectMapper = new ObjectMapper()
    val linesAsJson: ArrayNode = mapper.createArrayNode()
    jsonDS.foreach(x => {
      val lineAsJson: JsonNode = mapper.readTree(x)
      linesAsJson.add(lineAsJson)
    })

    ss.close()
    //System.out.println("Json format: " + mapper.writerWithDefaultPrettyPrinter().writeValueAsString(linesAsJson))
    return linesAsJson
  }
  
}