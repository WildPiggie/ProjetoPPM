package QTrees

import QTrees.QuadTree._
import java.awt.Color
import scala.annotation.tailrec
import scala.collection.SortedMap

object TextualUserInterface extends App {

  val path = IO_Utils.prompt("Insira o caminho para a imagem")
  //verificar se o path existe
  val qt = BitMap.makeQTree(path)
  val options = SortedMap[Int, CommandLineOption](
    0 -> new CommandLineOption("Exit", _ => sys.exit),
    1 -> new CommandLineOption("Scale", QuadTree.scale(IO_Utils.getUserInputDouble("Scale factor").get) ),
    2 -> new CommandLineOption("Vertical Mirror", QuadTree.mirrorV),
    3 -> new CommandLineOption("Horizontal Mirror", QuadTree.mirrorH),
    4 -> new CommandLineOption("Rotate 90º Clockwise", QuadTree.rotateR),
    5 -> new CommandLineOption("Rotate 90º Counter-clockwise", QuadTree.rotateL),
    6 -> new CommandLineOption("Sepia-Effect", QuadTree.mapColourEffect(sepiaEffect) ),
    7 -> new CommandLineOption("Contrast-Effect", QuadTree.mapColourEffect(contrastEffect) ),
    8 -> new CommandLineOption("Impure Noise-Effect", QuadTree.mapColourEffect(noiseEffect) ),
    9 -> new CommandLineOption("Pure Noise-Effect", QuadTree.mapColourEffectWithState(noiseEffectWithState) ),
    10 -> new CommandLineOption("Save Image", QuadTree.toImage(IO_Utils.prompt("Path"),IO_Utils.prompt("Format")))
  )
  mainLoop(qt)

  @tailrec
  def mainLoop(qt: QTree[Coords]) {
    IO_Utils.optionPrompt(options) match {
      case Some(opt) => val newQT = opt.exec(qt); mainLoop(newQT)
      case _ => println("Invalid option"); mainLoop(qt)
    }
  }
}
