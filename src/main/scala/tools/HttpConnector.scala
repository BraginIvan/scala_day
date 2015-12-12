package tools

import org.apache.commons.io.IOUtils
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClients

/**
 * Created by ivan on 11/24/15.
 */
trait HttpConnector {

  val httpClient = HttpClients.createDefault()

  def getContent(url: String) ={
    println(url)
    Thread.sleep(50)
    val get = new HttpGet(url)
    val response  = httpClient.execute(get)
    val is = response.getEntity.getContent
    val output = IOUtils.toString(is)
    is.close()
    output
  }
}
