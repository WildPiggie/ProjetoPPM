package QTrees

import QuadTree.Coords

case class CommandLineOption(name : String, exec : QTree[Coords] => QTree[Coords])

