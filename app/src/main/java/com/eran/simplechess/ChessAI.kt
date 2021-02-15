package com.eran.simplechess

class ChessAI (val game: ChessGame, val colorAI: PieceColor) {

    val oppositColor: PieceColor = if (colorAI ==PieceColor.Black) PieceColor.White else PieceColor.Black
    private val piecesAI = if (colorAI ==PieceColor.Black) game.blackPieces else game.whitePieces

    fun makeMove(){
        for (piece in piecesAI){
            for (move in game.possibleMoves(piece)){
                game.makeMove(piece,move)
                return
            }
        }
    }

}