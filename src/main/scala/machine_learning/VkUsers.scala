package machine_learning

import tools.Cassandra
import weka.classifiers.Evaluation
import scala.collection.JavaConversions._


object VkUsers extends MachineLearning with Cassandra{

  val attributesWithType  = List(
    ("can_write_private_message", "int"),

    ("user_id", "long"),
    ("albums", "int"),
    ("audios", "int"),
    ("videos", "int"),
    ("followers", "int"),
    ("ismale", "int"),
    ("last_seen", "long"),
    ("photos", "int"),

    //groups
    ("security_count", "int"),
    ("dataart_count", "int")
  )

  val attributes = attributesWithType.map(_._1)

  val indexAttribute = "can_write_private_message"

  val instancesCount: Int = 40000

  def createInstances = session.execute(s"select * from user_data limit $instancesCount").all()
    .filterNot(_.isNull("can_write_private_message"))
    .foldLeft(
    createEmptyInstanses(attributes, indexAttribute)
  ){
    (instances, line) =>
     val data = attributesWithType.map(
       field =>
         field._2 match {
           case "int" => Option(line.getInt(field._1)).getOrElse(0).toDouble
           case "long" => Option(line.getLong(field._1)).getOrElse(0l).toDouble

         }
     )
      instances.addInstance(data:_*)
  }


  def teach() = {

    val instances = createInstances

    val trainSet = instances.trainCV(5, 0)

    model.buildClassifier(trainSet)

    val testSet = instances.testCV(5, 0)

    val predictions: Array[Double] = new Evaluation(trainSet).evaluateModel(model, testSet)

    show(testSet, attributes, predictions)

    val actualValues = testSet.attributeToDoubleArray(0).map(_.toInt)

    println("auc = " + countAUC(actualValues,predictions) )

  }

}
