package de.kp.spark.pref.model
/* Copyright (c) 2014 Dr. Krusche & Partner PartG
* 
* This file is part of the Spark-pref project
* (https://github.com/skrusche63/spark-pref).
* 
* Spark-pref is free software: you can redistribute it and/or modify it under the
* terms of the GNU General Public License as published by the Free Software
* Foundation, either version 3 of the License, or (at your option) any later
* version.
* 
* Spark-pref is distributed in the hope that it will be useful, but WITHOUT ANY
* WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
* A PARTICULAR PURPOSE. See the GNU General Public License for more details.
* You should have received a copy of the GNU General Public License along with
* Spark-pref. 
* 
* If not, see <http://www.gnu.org/licenses/>.
*/

import org.json4s._

import org.json4s.native.Serialization
import org.json4s.native.Serialization.{read,write}

/**
 * Preference & Preferences specify basic user item ratings,
 * with no reference to contextual information
 */
case class BasePref(
  site:String,user:String,item:Int,score:Int
)

case class BasePrefs(items:List[BasePref])

/**
 * The Field and Fields classes are used to specify the fields with
 * respect to the data source provided, that have to be mapped onto
 * site,timestamp,user,group,item
 */
case class Field(
  name:String,datatype:String,value:String
)
case class Fields(items:List[Field])

/**
 * Service requests are mapped onto job descriptions and are stored
 * in a Redis instance
 */
case class JobDesc(
  service:String,task:String,status:String
)

/**
 * ServiceRequest & ServiceResponse specify the content 
 * sent to and received from an external service
 */
case class ServiceRequest(
  service:String,task:String,data:Map[String,String]
)

case class ServiceResponse(
  service:String,task:String,data:Map[String,String],status:String
)

object Sources {

  val FILE:String    = "FILE"
  val ELASTIC:String = "ELASTIC" 
  val JDBC:String    = "JDBC"    
  val PIWIK:String   = "PIWIK"    
  
  private val sources = List(FILE,ELASTIC,JDBC,PIWIK)
  def isSource(source:String):Boolean = sources.contains(source)
  
}

object Serializer {
    
  implicit val formats = Serialization.formats(NoTypeHints)
  
  def serializeFields(fields:Fields):String = write(fields) 
  def deserializeFields(fields:String):Fields = read[Fields](fields)

  def serializeJob(job:JobDesc):String = write(job)
  def deserializeJob(job:String):JobDesc = read[JobDesc](job)

  def serializeResponse(response:ServiceResponse):String = write(response)
  
  def deserializeRequest(request:String):ServiceRequest = read[ServiceRequest](request)
  def serializeRequest(request:ServiceRequest):String = write(request)
  
}
