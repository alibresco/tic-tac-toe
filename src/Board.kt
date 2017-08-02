
enum class Tile {
  X,
  O,
  EMPTY;

  override fun toString(): String {
    return when(this) {
      X -> "X"
      O -> "O"
      EMPTY -> " "
    }
  }
}

class Index private constructor(val row : Int, val col: Int) {
  companion object {
    private val indices = (0..2).map { i -> (0..2).map { Index(i, it) } }

    fun of(row: Int, col: Int) = indices[row][col]
  }
}

class Board(val board: Array<Array<Tile>>) {

  companion object {

    val win = setOf(
        // rows
        Triple(Index.of(0, 0), Index.of(0, 1), Index.of(0, 2)),
        Triple(Index.of(1, 0), Index.of(1, 1), Index.of(1, 2)),
        Triple(Index.of(2, 0), Index.of(2, 1), Index.of(2, 2)),
        // cols
        Triple(Index.of(0, 0), Index.of(1, 0), Index.of(2, 0)),
        Triple(Index.of(0, 1), Index.of(1, 1), Index.of(2, 1)),
        Triple(Index.of(0, 2), Index.of(1, 2), Index.of(2, 2)),
        // diagonals
        Triple(Index.of(0, 0), Index.of(1, 1), Index.of(2, 2)),
        Triple(Index.of(0, 2), Index.of(1, 1), Index.of(2, 0))
    )
  }

  constructor() : this(Array(3) { arrayOf(Tile.EMPTY, Tile.EMPTY, Tile.EMPTY)})

  fun add(index: Index, tile: Tile) : Board {
    if (get(index) != Tile.EMPTY) {
      throw IllegalArgumentException("Tile is full")
    }
    val newBoard = copyOf()
    newBoard.board[index.row][index.col] = tile
    return newBoard
  }

  fun get(index: Index) = board[index.row][index.col]

  fun isWin(tile: Tile) : Boolean {
    return win.fold(false) { acc, triple -> acc ||
        triple.toList().fold(true) { iAcc, index -> iAcc &&
            get(index) == tile } }
  }

  fun isFull() : Boolean {
    return board.fold(true) { acc, tiles -> acc &&
        tiles.fold(true) {subAcc, tile -> subAcc &&
            tile != Tile.EMPTY } }
  }

  fun copyOf() = Board(Array(3) { board[it].copyOf() })

  fun availableMoves() : List<Index> {
    val list = mutableListOf<Index>()
    for (i in 0..2) {
      (0..2)
          .filter { board[i][it] == Tile.EMPTY }
          .mapTo(list) { Index.of(i, it) }
    }
    return list.toList()
  }

  fun adjacent(tile: Tile) : List<Board> {
    return availableMoves().map { add(it, tile) }
  }

  override fun toString(): String {
    return " ${board[0][0]} | ${board[0][1]} | ${board[0][2]} \n" +
           "---|---|---\n" +
           " ${board[1][0]} | ${board[1][1]} | ${board[1][2]} \n" +
           "---|---|---\n" +
           " ${board[2][0]} | ${board[2][1]} | ${board[2][2]} "
  }
}

fun main(args: Array<String>) {
  var b = Board().add(Index.of(0, 0), Tile.X)
      .add(Index.of(1, 1), Tile.O)
  println(b)
  println(b.isWin(Tile.X))
  b = b.add(Index.of(0, 1), Tile.X)
      .add(Index.of(0, 2), Tile.X)
  println(b)
  println(b.isWin(Tile.X))
  println(b.isFull())
  b = b.add(Index.of(1, 0), Tile.X)
      .add(Index.of(1, 2), Tile.X)
      .add(Index.of(2, 0), Tile.X)
      .add(Index.of(2, 1), Tile.X)
      .add(Index.of(2, 2), Tile.X)
  println(b.isFull())
}