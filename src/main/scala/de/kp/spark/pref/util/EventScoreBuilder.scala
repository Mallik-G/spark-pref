package de.kp.spark.pref.util
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

import scala.xml._
import scala.collection.mutable.ArrayBuffer

import de.kp.spark.pref.model._

object EventScoreBuilder extends Serializable {
  
  private val path = "scores.xml"
  private val eventScores = build()

  private def build():List[EventScore] = {
 
    val buffer = ArrayBuffer.empty[EventScore]
    
    try {
 
      val root = XML.load(getClass.getClassLoader.getResource(path))     
      for (element <- root \ "EventScore") {
      
        val event = (element \ "type").text.toInt
        val desc  = (element \ "description").text.toString

        val scores = (element \ "scores").text.split(",").toList.map(_.toInt)
      
        buffer += new EventScore(event,desc,scores)
     }
      
    } catch {
      case e:Exception => {}
    }
    
    buffer.toList
    
  }

  def get() = eventScores
  
}