package com.eran.simplechess

class ChessGame {
    var board1d = Array<ChessSquare>(64){ChessSquare()}
    var moveHistory = mutableListOf<Array<ChessSquare>>()
    var moveHistoryPointer:Int = 0

    init {
        newBoard()
    }

    fun newBoard(){
        for (i in 0..63){
            board1d[i].enPassant=false
            board1d[i].pieceColor = PieceColor.non
            board1d[i].pieceType = PieceType.non
        }

        for (i in 0..15) {
            board1d[i].pieceColor = PieceColor.black
        }

        for (i in 8..15){
            board1d[i].pieceType = PieceType.p
        }

        for (i in 48..63) {
            board1d[i].pieceColor = PieceColor.white
        }

        for (i in 48..55){
            board1d[i].pieceType = PieceType.p
        }

        board1d[0].pieceType=PieceType.r0
        board1d[7].pieceType=PieceType.r0
        board1d[56].pieceType=PieceType.r0
        board1d[63].pieceType=PieceType.r0

        board1d[1].pieceType=PieceType.n
        board1d[6].pieceType=PieceType.n
        board1d[57].pieceType=PieceType.n
        board1d[62].pieceType=PieceType.n

        board1d[2].pieceType=PieceType.b
        board1d[5].pieceType=PieceType.b
        board1d[58].pieceType=PieceType.b
        board1d[61].pieceType=PieceType.b

        board1d[3].pieceType=PieceType.Q
        board1d[59].pieceType=PieceType.Q

        board1d[4].pieceType=PieceType.K0
        board1d[60].pieceType=PieceType.K0
    }


    fun makeMove (fist: Int, second: Int){
        moveHistory.add(board1d.copyOf())
        moveHistoryPointer= moveHistory.size

        board1d[second].pieceType = board1d[fist].pieceType
        board1d[second].pieceColor = board1d[fist].pieceColor

        board1d[fist].pieceType = PieceType.non
        board1d[fist].pieceColor = PieceColor.non

        if (board1d[second].pieceType == PieceType.r0){
            board1d[second].pieceType = PieceType.r
        }

        if (board1d[second].pieceType == PieceType.K0){
            board1d[second].pieceType = PieceType.K
        }
    }

    fun moveBack(){
        if (moveHistory.isNotEmpty() && moveHistoryPointer>0){
            moveHistoryPointer--
            board1d = moveHistory[moveHistoryPointer].copyOf()
        }
    }
}