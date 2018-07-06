package edu.isu.adl.datafilter

import java.io._
import scala.io._
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.sql.Timestamp
import java.sql.Connection
import java.sql.DriverManager
import play.api.db._

object DataFilter {
  
  def filterData(userName: String, timeStamp: String, motionFile: File, statusFile: File, connection:Connection){
    val statusData = new StatusDataFilter(statusFile)
    
    //get light, xAcce, yAcce, zAccr, angle, azimuth, pitch, roll, latitude, longitude, altitude
    val basicStatusData = statusData.extractBasicStatus()
    //println(basicStatusData)
    
    //get hour
    val hour = timeStamp.substring(9, 11)
    //println(hour)
    
    //get moving, turning, lightChanging, dark, accel
    val motionData = MotionDataFilter.extractMotionData(motionFile)
    //println(motionData)
    
    //get status, screenOn, earPlug
    val batStatusData = statusData.extractBatStatus()
    //println(batStatusData)
    
    //get actionLabel
    val actionLabel = statusData.extractActionLabel()
    
    //get timeStamp
    val timeFormat: DateFormat = new SimpleDateFormat("yyyyMMddkkmmss")
    val sqlTimeStamp:Timestamp = new Timestamp(timeFormat.parse(timeStamp).getTime)
    //println(sqlTimeStamp.toString())
    //write to local file
/*    val writer = new FileWriter("C:\\Users\\yongan\\CCLearning\\CC\\RecognitionOfADL\\data\\PhilAmes20160706_20160829\\PhilSamsang20160706_20160803\\dataResult.csv", true)
    writer.write(basicStatusData._1 + "," +
                basicStatusData._2 + "," +
                basicStatusData._3 + "," +
                basicStatusData._4 + "," +
                basicStatusData._5 + "," +
                basicStatusData._6 + "," +
                basicStatusData._7 + "," +
                basicStatusData._8 + "," +
                basicStatusData._9 + "," +
                basicStatusData._10 + "," +
                basicStatusData._11 + "," +
                hour + "," + 
                motionData._1 + "," +
                motionData._2 + "," +
                motionData._3 + "," +
                motionData._4 + "," +
                motionData._5 + "," +
                batStatusData._1 + "," +
                batStatusData._2 + "," +
                batStatusData._3 + "," +
                actionLabel + "," +
                username + "_" + timeStamp + '\n')
     writer.close()*/
    
    
    //write to MySQL Database through JDBC
    try{
    val sql = "INSERT INTO adl.record(light, xAcce, yAcce, zAccr, angle, azimuth, pitch, roll, latitude, longitude, altitude, hour, moving, turning, lightChanging, dark, accel, chargingStatus, screenOn, earPlug, actionLabel, timeStamp, userID) " +
                                      "values(" + 
                                      basicStatusData._1 + "," +
                                      basicStatusData._2 + "," +
                                      basicStatusData._3 + "," +
                                      basicStatusData._4 + "," +
                                      basicStatusData._5 + "," +
                                      basicStatusData._6 + "," +
                                      basicStatusData._7 + "," +
                                      basicStatusData._8 + "," +
                                      basicStatusData._9 + "," +
                                      basicStatusData._10 + "," +
                                      basicStatusData._11 + "," +
                                      hour + "," +
                                      motionData._1 + "," +
                                      motionData._2 + "," +
                                      motionData._3 + "," +
                                      motionData._4 + "," +
                                      motionData._5 + "," +
                                      batStatusData._1 + "," +
                                      batStatusData._2 + "," +
                                      batStatusData._3 + ",?,?,?)"
    val pStatement = connection.prepareStatement(sql)
    pStatement.setString(1, actionLabel)
    pStatement.setTimestamp(2, sqlTimeStamp)
    pStatement.setInt(3, 0)
    pStatement.execute()
     println("successfully insert")
    }catch{
      case e: Exception => e.printStackTrace()
    }
  }
  
/*  
  def mainFilter(username:String, timeStamp:String, motionFile:File, statusFile:File){ 
    //Database connection setting
    //Use controller to access the database
    //Check User in database
    
    //for(timeStamp <- timeStamps)
      filterData(username, timeStamp, motionFile, statusFile, connection)
      
    connection.close()
  }
*/
}