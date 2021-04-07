package QTrees

case class QNode[A](value: A, one: QTree[A], two: QTree[A], three: QTree[A], four: QTree[A]) extends QTree[A]{
  //override def changeValue[A](newValue: A): QTree[A] =
  //  new QNode[A](newValue, one, two, three, four)
}

