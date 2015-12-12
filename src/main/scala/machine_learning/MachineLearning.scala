package machine_learning

import java.util

import weka.classifiers.Evaluation
import weka.classifiers.meta.AdditiveRegression
import weka.classifiers.trees.RandomForest
import weka.core._
import scala.collection.JavaConversions._


trait MachineLearning {

  implicit class FunctionInstances(instances: Instances) {
    def addInstance(doubles: Double*): Instances = {
      instances.add(new DenseInstance(1.0, doubles.toArray))
      instances
    }

  }


  def createEmptyInstanses(attributesNames: List[String], indexAttributeName: String) ={
    val atts = new util.ArrayList[Attribute]()
    attributesNames.foreach(name => atts.add(new Attribute(name)))
    val instances = new Instances("test", atts, 0)
    instances.setClassIndex(instances.attribute(indexAttributeName).index)
    instances
  }

  def getInstancesDescription(instances: Instances,attributesNames: List[String]) : List[String]={
    instances.iterator().toList.map{instance =>
      val values = instance.toDoubleArray
      attributesNames.zipWithIndex.map(name => s"""${name._1} = ${values.apply(name._2)}""").mkString("\t")
    }

  }

  def show(instances: Instances, attributes: List[String], predictions: Array[Double]) = println(
    getInstancesDescription(instances,  attributes)
      .zip(predictions.map(p => s"prediction = $p"))
      .map(_.swap.productIterator.mkString("\t"))
      .mkString("\n")
  )

   val model = {
    val rf = new RandomForest
    rf.setNumTrees(10)
    rf.setMaxDepth(5)
    rf.setNumFeatures(6)
    rf.setDebug(true)
   /* val boostingModel = new AdditiveRegression()
    boostingModel.setClassifier(rf)
    boostingModel.setShrinkage(0.02)
    boostingModel.setNumIterations(100)
    boostingModel.setDebug(true)
    boostingModel*/
     rf
  }




  def countAUC(actualValue: Array[Int], predictionValue: Array[Double]): Double ={
    val iterator = predictionValue.iterator
    val zeros = actualValue.count(i => i == 0)
    val ones = actualValue.count(i => i == 1)
    val v =  actualValue.map(i => iterator.next() -> i )
    val b =  v.toSeq.sortBy(_._1)

    var auc = 0d
    var argument = 0d
    for(line <- b){
      if (line._2 ==0)
        argument = argument + 1d/zeros
      else
        auc = auc + argument/ones
    }

    auc
  }

  def countSSE(actualValue: Array[Int], predictionValue: Array[Double]): Double =

  Math.pow(( for(i <- 0 until  actualValue.length)
   yield {
     Math.pow(predictionValue.apply(i) - actualValue.apply(i), 2)
   }).toList.sum / actualValue.length, 0.5)




  val attributes:List[String]
}
