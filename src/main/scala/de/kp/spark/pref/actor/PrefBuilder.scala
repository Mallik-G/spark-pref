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

import org.apache.spark.SparkContext

import akka.actor.{ActorRef,Props}
import akka.pattern.ask
import akka.util.Timeout

import de.kp.spark.core.model._

import de.kp.spark.pref.Configuration
import de.kp.spark.pref.model._

import scala.concurrent.Future
import scala.concurrent.duration.DurationInt

class PrefBuilder(@transient val sc:SparkContext) extends BaseActor {

  implicit val ec = context.dispatcher

  def receive = {

    case req:ServiceRequest => {
      
      val origin = sender    
      val uid = req.data("uid")
          
      val response = validate(req) match {
            
        case None => build(req).mapTo[ServiceResponse]            
        case Some(message) => Future {failure(req,message)}
            
      }

      response.onSuccess {
            
        case result => {
              
          origin ! result
          context.stop(self)
              
        }
      
      }

      response.onFailure {
        
        case throwable => {             
              
          val resp = failure(req,throwable.toString)
              
          origin ! failure(req,throwable.toString)	 
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
  
  private def build(req:ServiceRequest):Future[Any] = {

    val (duration,retries,time) = Configuration.actor      
    implicit val timeout:Timeout = DurationInt(time).second
    
    ask(actor(req), req)
    
  }

  private def validate(req:ServiceRequest):Option[String] = {

    val uid = req.data("uid")
    
    if (cache.statusExists(req)) {            
      return Some(Messages.TASK_ALREADY_STARTED(uid))   
    }

    req.data.get("algorithm") match {
        
      case None => {
        return Some(Messages.NO_ALGORITHM_PROVIDED(uid))              
      }
        
      case Some(algorithm) => {
        if (Algorithms.isAlgorithm(algorithm) == false) {
          return Some(Messages.ALGORITHM_IS_UNKNOWN(uid,algorithm))    
        }
          
      }
    
    }  
    
    req.data.get("source") match {
        
      case None => {
        return Some(Messages.NO_SOURCE_PROVIDED(uid))       
      }
        
      case Some(source) => {
        if (Sources.isSource(source) == false) {
          return Some(Messages.SOURCE_IS_UNKNOWN(uid,source))    
        }          
      }
        
    }

    None
    
  }

  private def actor(req:ServiceRequest):ActorRef = {

    val algorithm = req.data("algorithm")
    if (algorithm == Algorithms.EPREF) {      
      context.actorOf(Props(new EPrefActor(sc)))      
    } else {
     context.actorOf(Props(new NPrefActor(sc)))
    }
  }

}