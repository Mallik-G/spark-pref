package de.kp.spark.pref.spec
/* Copyright (c) 2014 Dr. Krusche & Partner PartG
* 
* This file is part of the Spark-Pref project
* (https://github.com/skrusche63/spark-fsm).
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
import de.kp.spark.core.redis.RedisCache

import scala.xml._
import scala.collection.mutable.HashMap

object Fields {
  
  val path = "fieldspec.xml"
  val cache = new RedisCache()

  def get(req:ServiceRequest):Map[String,(String,String)] = {
    
    val fields = HashMap.empty[String,(String,String)]

    try {
          
      if (cache.fieldsExist(req)) {   
        
        val fieldspec = cache.fields(req)
        for (field <- fieldspec.items) {
        
          val _name = field.name
          val _type = field.datatype
          
          val _mapping = field.value
          fields += _name -> (_mapping,_type) 
          
        }
        
      } else {

        val root = XML.load(getClass.getClassLoader.getResource(path))     
        for (field <- root \ "field") {
      
          val _name  = (field \ "@name").toString
          val _type  = (field \ "@type").toString

          val _mapping = field.text
          fields += _name -> (_mapping,_type) 
      
        }
      
     }
      
    } catch {
      case e:Exception => {}
    }
    
    fields.toMap
  }

}