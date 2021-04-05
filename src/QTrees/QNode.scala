package QTrees

case class QNode[A](value: A, one: QTree[A], two: QTree[A], three: QTree[A], four: QTree[A]) extends QTree[A]{
 //porquê conter o value a? Se "one" for uma folha não devia simplesmente ser uma qleaf?
}

