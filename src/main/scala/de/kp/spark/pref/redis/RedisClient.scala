package de.kp.spark.pref.redis
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

import redis.clients.jedis.Jedis
import de.kp.spark.pref.Configuration

object RedisClient {

  def apply():Jedis = {

    val (host,port) = Configuration.redis
    new Jedis(host,port.toInt)
    
  }
  
}