package QTrees

import GUI.Container
import TUI.CommandLineOption

import scala.collection.SortedMap
import scala.io.Source
import scala.io.StdIn.readLine
import scala.util.{Failure, Success, Try}
import java.io._
import scala.annotation.tailrec

object IO_Utils {

  /**
   * Gets user integer input given a message.
   * @param msg the message to be printed
   * @return
   */
  def getUserInputInt(msg : String): Try[Int] = {
    print(msg + ": ")
    Try(readLine.trim.toUpperCase.toInt)
  }

  /**
   * Gets user double input given a message.
   * @param msg the message to be printed
   * @return
   */
  def getUserInputDouble(msg : String): Try[Double] = {
    print(msg + ": ")
    Try(readLine.trim.toUpperCase.toDouble)
  }

  /**
   * Gets user input given a message.
   * @param msg the message to be printed
   * @return
   */
  def prompt(msg : String):String = {
    print(msg + ": ")
    scala.io.StdIn.readLine()
  }

  /**
   * Shows the option prompt for Textual User Interface.
   * @param options the sortedmap ([Int, CommandLineOption]) containing the options
   * @return
   */
  @tailrec
  def optionPrompt(options : SortedMap[Int, CommandLineOption]): Option[CommandLineOption] = {
    println("-- Options --")
    options.toList map ((option:(Int, CommandLineOption)) => println(option._1 + ") " + option._2.name))

    getUserInputInt("Select an option") match {
      case Success(i) => options.get(i)
      case Failure(_) => println("Invalid number!");optionPrompt(options)
    }
  }

  /**
   * Reads the content of a given file and inserts it in a new container.
   * @param file the file name
   * @return container initialized with the contents of the file
   */
  def readContainerFromFile(file: String): Container = {
    var container: Container = new Container(file, List())
    val bufferedSource = Source.fromFile(file)
    for (line <- bufferedSource.getLines) {
      val tokens = line.split(">")
      if(tokens.size == 1)
        container = Container.add(tokens(0),"")(container)
      else
        container = Container.add(tokens(0), tokens(1))(container)
    }
    bufferedSource.close
    container
  }


  /**
   * Writes the content of a given container into the specified file.
   * @param file the filename
   * @param container the container
   */
  def writeContainerToFile(file: String, container: Container): Unit = {
    val pw = new PrintWriter(new File(file))
    val wrapWithGreater = wrapFileLine(_: String)(">")(_: String)
    container.data map (tokens => pw.write(wrapWithGreater(tokens._1,tokens._2)))
    pw.close()
  }


  /**
   * Wraps a string given 3 strings. Useful to apply PAF.
   * @param path the path name
   * @param separator the separator
   * @param info the information associated to the image
   * @return
   */
  def wrapFileLine(path: String)(separator: String)(info: String): String = {path + separator + info + "\n"}

}
