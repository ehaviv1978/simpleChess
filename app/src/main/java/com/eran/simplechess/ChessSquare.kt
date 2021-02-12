package com.eran.simplechess

data class ChessSquare (
    var pieceType: PieceType = PieceType.non,
    var pieceColor: PieceColor = PieceColor.non,
    var enPassant: Boolean = false
    )