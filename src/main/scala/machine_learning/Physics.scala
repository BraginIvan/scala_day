package machine_learning

import weka.classifiers.Evaluation

import scala.util.Random

object Physics extends MachineLearning{

  val attributes = List("x", "a", "t", "v0")

  val indexAttribute = "x"


  def createInstances(size:Int) = (0 until size).foldLeft(
    createEmptyInstanses(attributes, indexAttribute)
  ){
    (instances, line) =>
      val a = Random.nextDouble() * 10
      val t = Random.nextDouble() * 100
      val v0 = Random.nextDouble() * 1000
      instances.addInstance(a*t*t/2 + v0*t, a, t, v0)
  }

  def teach() = {

    val instances = createInstances(20000)

    val trainSet = instances.trainCV(5, 0)

    model.buildClassifier(trainSet)

    val testSet = instances.testCV(5, 0)

    val predictions: Array[Double] = new Evaluation(trainSet).evaluateModel(model, testSet)

    show(testSet, attributes, predictions)

    val actualValues = testSet.attributeToDoubleArray(0).map(_.toInt)

    println("sse = " + countSSE(actualValues,predictions) )

  }


}
