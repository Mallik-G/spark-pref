package de.kp.spark.pref.model
/* Copyright (c) 2014 Dr. Krusche & Partner PartG
* 
* This file is part of the Spark-Pref project
* (https://github.com/skrusche63/spark-pref).
* 
* Spark-Pref is free software: you can redistribute it and/or modify it under the
* terms of the GNU General Public License as published by the Free Software
* Foundation, either version 3 of the License, or (at your option) any later
* version.
* 
* Spark-Pref is distributed in the hope that it will be useful, but WITHOUT ANY
* WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
* A PARTICULAR PURPOSE. See the GNU General Public License for more details.
* You should have received a copy of the GNU General Public License along with
* Spark-Pref. 
* 
* If not, see <http://www.gnu.org/licenses/>.
*/

import de.kp.spark.core.model._

case class EventScore(
  event:Int,desc:String,scores:List[Int]
)

/**
 * Preference & Preferences specify basic user item ratings,
 * with no reference to contextual information
 */
case class NPref(
  site:String,user:String,item:Int,score:Int
)

case class NPrefs(items:List[NPref])

object Algorithms {
  
  val EPREF:String = "EPREF"
  val NPREF:String = "NPREF"
    
  private val algorithms = List(EPREF,NPREF)
  def isAlgorithm(algorithm:String):Boolean = algorithms.contains(algorithm)
  
}

object Messages extends BaseMessages {

  def BUILDING_STARTED(uid:String) = 
    String.format("""[UID: %s] Preference building task started.""", uid)
  
  def MISSING_PARAMETERS(uid:String):String = 
    String.format("""[UID: %s] Preference building task has missing parameters.""", uid)
   
}

object ResponseStatus extends BaseStatus 

object Formats {

  val FM:String = "FM"
  
  private val formats = List(FM)
  def isFormat(format:String):Boolean = formats.contains(format)

}

object Sinks {

  val FILE:String  = "FILE"
  val REDIS:String = "REDIS" 
  
  private val sinks = List(FILE,REDIS)
  def isSink(sink:String):Boolean = sinks.contains(sink)
  
}

object Sources {

  val FILE:String    = "FILE"
  val ELASTIC:String = "ELASTIC" 
  val JDBC:String    = "JDBC"    
  val PIWIK:String   = "PIWIK"    
  
  private val sources = List(FILE,ELASTIC,JDBC,PIWIK)
  def isSource(source:String):Boolean = sources.contains(source)
  
}

object Serializer extends BaseSerializer
