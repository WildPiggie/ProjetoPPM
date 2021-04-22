package QTrees

case class Container(name:String, data : Map[String, String]){

}

object Container {

  def addEntry(key: => String, value: => String)(container: Container): Container =  {
    new Container(container.name, container.data + (key -> value))
  }

  def remEntry(key: => String)(container: Container): Container = {
    new Container(container.name, container.data - key)
  }

  def showAll(container: Container): Container = {
    IO_Utils.printContainer(container)
    container
  }

}