package QTrees

import scala.collection.SortedMap
import scala.io.StdIn.readLine
import scala.util.{Failure, Success, Try}

object IO_Utils {

  def getUserInputInt(msg : String): Try[Int] = {
    print(msg + ": ")
    Try(readLine.trim.toUpperCase.toInt)
  }

  def getUserInputDouble(msg : String): Try[Double] = {
    print(msg + ": ")
    Try(readLine.trim.toUpperCase.toDouble)
  }

  def prompt(msg : String):String = {
    print(msg + ": ")
    scala.io.StdIn.readLine()
  }

  def printMessage(msg : String): Unit = {
    println(msg)
  }

  def optionPrompt(options : SortedMap[Int, CommandLineOption]): Option[CommandLineOption] =
  {
    println("-- Options --")
    options.toList map
      ((option:(Int, CommandLineOption)) => println(option._1 + ") " + option._2.name))

    getUserInputInt("Select an option") match {
      case Success(i) => options.get(i)
      case Failure(_) => println("Invalid number!");optionPrompt(options)
    }
  }

  def printContainer(container: Container) = {
    println("Name:"+container.name)
    container.data.toList map (x => println("Key:"+x._1) + " " + println("Value:"+x._2))
  }
}
