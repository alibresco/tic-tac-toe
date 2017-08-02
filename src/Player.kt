

sealed class Player {
  abstract fun makeMove(board: Board) : Board
}

class Human : Player() {
  override fun makeMove(board: Board): Board {
    var index: Index
    while (true) {
      print("Make your move [1-9]: ")
      val input = readLine()?.trim()?.toInt() ?: 0
      try {
        index = Index.of((input - 1) / 3, (input - 1) % 3)
        if (input < 1 || input > 9 || board.get(index) != Tile.EMPTY) {
          println("Invalid input")
        } else {
          break
        }
      } catch (e: ArrayIndexOutOfBoundsException) {
        println("Invalid input")
      }
    }
    return board.add(index, Tile.X)
  }
}

class Ai : Player() {
  override fun makeMove(board: Board): Board {
    return board.adjacent(Tile.O)
        .map { Pair(score(it), it) }
        .maxBy { it.first }!!.second
  }

  private fun score(board: Board) : Int {
    if (board.isWin(Tile.O)) {
      return 1
    } else if (board.isWin(Tile.X)) {
      return -1
    } else if (board.isFull()) {
      return 0
    } else {
      return board.adjacent(Tile.X)
          .map {
            if (it.isWin(Tile.X)) {
              -1
            } else if (it.isFull()) {
              0
            } else {
              it.adjacent(Tile.O)
                  .map { score(it) }
                  .max()!!
            }
          }
          .min()!!
    }
  }
}

fun checkOver(board: Board): Boolean {
  if (board.isWin(Tile.X)) {
    println("You Win!")
    return true
  } else if (board.isWin(Tile.O)) {
    println("You Lose...")
    return true
  } else if (board.isFull()) {
    println("It's a draw.")
    return true
  } else {
    return false
  }
}

fun main(args: Array<String>) {
  val human = Human()
  val ai = Ai()
  var board = Board()
  println(board)
  while (true) {
    board = human.makeMove(board)
    println("\n" + board)
    if (checkOver(board)) break
    board = ai.makeMove(board)
    println("\n" + board)
    if (checkOver(board)) break
  }
}