package TUI

import QTrees.QuadTree._
import QTrees.{BitMap, IO_Utils, QTree, QuadTree}

import java.io.File
import scala.annotation.tailrec
import scala.collection.SortedMap

object TextualUserInterface extends App {

  val path = IO_Utils.prompt("Insert image path")
  if (!new File(path).isFile)
    throw new IllegalArgumentException("Invalid file path.")

  val qt = BitMap.makeQTree(path)
  val options = SortedMap[Int, CommandLineOption](
    0 ->  CommandLineOption("Exit", _ => sys.exit),
    1 ->  CommandLineOption("Scale", QuadTree.scale(IO_Utils.getUserInputDouble("Scale factor").get)),
    2 ->  CommandLineOption("Vertical Mirror", QuadTree.mirrorV),
    3 ->  CommandLineOption("Horizontal Mirror", QuadTree.mirrorH),
    4 ->  CommandLineOption("Rotate 90º Clockwise", QuadTree.rotateR),
    5 ->  CommandLineOption("Rotate 90º Counter-clockwise", QuadTree.rotateL),
    6 ->  CommandLineOption("Sepia-Effect", QuadTree.mapColourEffect(sepiaEffect)),
    7 ->  CommandLineOption("Contrast-Effect", QuadTree.mapColourEffect(contrastEffect)),
    8 ->  CommandLineOption("Impure Noise-Effect", QuadTree.mapColourEffect(noiseEffect)),
    9 ->  CommandLineOption("Pure Noise-Effect", QuadTree.mapColourEffectWithState(noiseEffectWithState)),
    10 -> CommandLineOption("Save Image", QuadTree.qTreeToImage(IO_Utils.prompt("Path")))
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
