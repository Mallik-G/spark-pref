package de.kp.spark.pref.actor
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

import akka.actor.{ActorRef,Props}
import akka.pattern.ask
import akka.util.Timeout

import de.kp.spark.core.model._
import de.kp.spark.pref.model._

import scala.concurrent.Future
import scala.concurrent.duration.DurationInt

class PrefMonitor extends BaseActor {

  def receive = {

    case req:ServiceRequest => {
      
      val origin = sender    
      val uid = req.data("uid")

      req.task match {
       
        case "status" => {
          
          val resp = if (cache.statusExists(req) == false) {           
            failure(req,Messages.TASK_DOES_NOT_EXIST(uid))           
          } else {            
            status(req)
            
          }
           
          origin ! resp
          context.stop(self)
          
        }
        
        case _ => {
          
          val msg = Messages.TASK_IS_UNKNOWN(uid,req.task)
          
          origin ! failure(req,msg)
          context.stop(self)
          
        }
        
      }
      
    }
    
    case _ => {
      
      val origin = sender               
      val msg = Messages.REQUEST_IS_UNKNOWN()          
          
      origin ! failure(null,msg)
      context.stop(self)

    }
  
  }

  private def status(req:ServiceRequest):ServiceResponse = {
    
    val uid = req.data("uid")
    val data = Map("uid" -> uid)
                
    new ServiceResponse(req.service,req.task,data,cache.status(req))	

  }

}