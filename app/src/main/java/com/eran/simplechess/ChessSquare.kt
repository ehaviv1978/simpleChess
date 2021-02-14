package com.eran.simplechess

data class ChessSquare (
    var pieceType: PieceType = PieceType.Non,
    var pieceColor: PieceColor = PieceColor.Non,
    var enPassant: Boolean = false
    )