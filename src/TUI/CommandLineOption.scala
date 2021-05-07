package TUI

import QTrees.QTree
import QTrees.QuadTree.Coords

case class CommandLineOption(name: String, exec: QTree[Coords] => QTree[Coords])
