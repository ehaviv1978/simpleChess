package com.eran.simplechess

import kotlin.math.abs


class ChessGame {
    var board1d = Array(64){ChessSquare()}
    var board2d = arrayOf<Array<ChessSquare>>()
    var moveHistory = arrayOf<Array<ChessSquare>>()
    var moveHistoryPointer:Int = 0
    var turnColor = PieceColor.White
    var whitePieces = mutableListOf<Int>()
    var blackPieces = mutableListOf<Int>()


    init {
        newBoard()
        turnColor = PieceColor.White
    }


    private  fun fillPiecesArrays(){
        whitePieces.clear()
        blackPieces.clear()
        for (i in 0..63){
            if (board1d[i].pieceColor==PieceColor.Non){
                continue
            } else if (board1d[i].pieceColor==PieceColor.White){
                whitePieces.add(i)
            } else{
                blackPieces.add(i)
            }
        }
    }

    // fill board2d array with elements
    private fun board1dToBoard2d(){
        board2d = arrayOf()
        var n = 0
        for (i in 0..7)
        {
            var array = arrayOf<ChessSquare>()
            for (j in 0..7) {
                array += board1d[n]
                n++
            }
            board2d += array
        }
    }


    fun switchTurnColor(){
        if (turnColor== PieceColor.White){
            turnColor= PieceColor.Black
        }else{
            turnColor=PieceColor.White
        }
    }


    fun otherColor(color: PieceColor): PieceColor{
        if (color == PieceColor.White){
            return  PieceColor.Black
        }else{
            return  PieceColor.White
        }
    }


    private fun newBoard(){
        for (i in 0..63){
            board1d[i].enPassant=false
            board1d[i].pieceColor = PieceColor.Non
            board1d[i].pieceType = PieceType.Non
        }

        for (i in 0..15) {
            board1d[i].pieceColor = PieceColor.Black
        }

        for (i in 8..15){
            board1d[i].pieceType = PieceType.Pawn
        }

        for (i in 48..63) {
            board1d[i].pieceColor = PieceColor.White
        }

        for (i in 48..55){
            board1d[i].pieceType = PieceType.Pawn
        }

        board1d[0].pieceType=PieceType.Rock0
        board1d[7].pieceType=PieceType.Rock0
        board1d[56].pieceType=PieceType.Rock0
        board1d[63].pieceType=PieceType.Rock0

        board1d[1].pieceType=PieceType.Knight
        board1d[6].pieceType=PieceType.Knight
        board1d[57].pieceType=PieceType.Knight
        board1d[62].pieceType=PieceType.Knight

        board1d[2].pieceType=PieceType.Bishop
        board1d[5].pieceType=PieceType.Bishop
        board1d[58].pieceType=PieceType.Bishop
        board1d[61].pieceType=PieceType.Bishop

        board1d[3].pieceType=PieceType.Queen
        board1d[59].pieceType=PieceType.Queen

        board1d[4].pieceType=PieceType.King0
        board1d[60].pieceType=PieceType.King0

        if (moveHistory.isNotEmpty()){
            moveHistory =moveHistory.sliceArray(0..0)
        }
        addMoveToHistory()
        board1dToBoard2d()
        fillPiecesArrays()
        turnColor = PieceColor.White
    }


    private fun addMoveToHistory(){
        moveHistory+=Array(64){i ->board1d[i].copy()}
        moveHistoryPointer= moveHistory.size-1
    }


    private fun removeEnPassant(){
        for (i in 16..23){
            board1d[i].enPassant=false
        }
        for (i in 40..47){
            board1d[i].enPassant=false
        }
    }


    fun makeMove (first: Int, second: Int){
        switchTurnColor()
        if (board1d[first].pieceType == PieceType.Pawn && board1d[second].enPassant){
            if (second<first){
                board1d[second+8].pieceColor=PieceColor.Non
                board1d[second+8].pieceType =PieceType.Non
            }else{
                board1d[second-8].pieceColor=PieceColor.Non
                board1d[second-8].pieceType =PieceType.Non
            }
        }
        removeEnPassant()
        if (moveHistory.size>moveHistoryPointer+1){
            moveHistory=moveHistory.sliceArray(0 until moveHistoryPointer+1)
        }

        board1d[second].pieceType = board1d[first].pieceType
        board1d[second].pieceColor = board1d[first].pieceColor

        board1d[first].pieceType = PieceType.Non
        board1d[first].pieceColor = PieceColor.Non

        if (board1d[second].pieceType == PieceType.Rock0) {
            board1d[second].pieceType = PieceType.Rock
        } else if (board1d[second].pieceType == PieceType.King0) {
            board1d[second].pieceType = PieceType.King
            //Do Castling
            if (second == first + 2) {
                makeMove(second + 1, second - 1)
                switchTurnColor()
                return
            } else if (second == first - 2) {
                makeMove(second - 2, second + 1)
                switchTurnColor()
                return
            }
        } else if (board1d[second].pieceType == PieceType.Pawn) {
            if (second==first+16){
                board1d[first+8].enPassant=true
            }else if(second==first-16){
                board1d[first-8].enPassant=true
            }else if (second in 0..7 || second in 56..63){
                board1d[second].pieceType=PieceType.Queen
            }
        }
        addMoveToHistory()
        fillPiecesArrays()
    }


    fun moveBack(){
        if (moveHistory.isNotEmpty() && moveHistoryPointer>0){
            switchTurnColor()
            moveHistoryPointer--
            board1d = Array(64){i -> moveHistory[moveHistoryPointer][i].copy()}
            board1dToBoard2d()
            fillPiecesArrays()
        }
    }


    fun moveForward(){
        if (moveHistory.size>moveHistoryPointer+1){
            switchTurnColor()
            moveHistoryPointer++
            board1d = Array(64){i -> moveHistory[moveHistoryPointer][i].copy()}
            board1dToBoard2d()
            fillPiecesArrays()
        }
    }


    // checks if specific square is under attack
    private fun isThreatened(i: Int, color: PieceColor): Boolean {
        if (color == PieceColor.White) {
            whitePieces.forEach {
                if (isValidMove(it, i)) {
                    return true
                }
            }
            return false
        } else {
            blackPieces.forEach {
                if (isValidMove(it, i)) {
                    return true
                }
            }
            return false
        }
    }


    fun isDraw(color: PieceColor): Boolean{
        var tempHistory = moveHistory.copyOf()
        if (color == PieceColor.Black){
            for (piece in blackPieces){
                for (move in possibleMoves(piece)){
                    makeMove(piece,move)
                    if (!isDoingCheck(PieceColor.White)) {
                        moveBack()
                        moveHistory=tempHistory.copyOf()
                        return false
                    }
                    moveBack()
                }
            }
            moveHistory=tempHistory.copyOf()
            return true
        }else{
            for (piece in whitePieces){
                for (move in possibleMoves(piece)){
                    makeMove(piece,move)
                    if (!isDoingCheck(PieceColor.Black)) {
                        moveBack()
                        moveHistory=tempHistory.copyOf()
                        return false
                    }
                    moveBack()
                }
            }
            moveHistory=tempHistory.copyOf()
            return true
        }
    }

    fun isDoingCheck(color: PieceColor): Boolean{
        if (color == PieceColor.Black){
            for (piece in whitePieces) {
                if (board1d[piece].pieceType==PieceType.King || board1d[piece].pieceType==PieceType.King0){
                    return isThreatened(piece, PieceColor.Black)
                }
            }
        }else{
            for (piece in blackPieces) {
                if (board1d[piece].pieceType==PieceType.King || board1d[piece].pieceType==PieceType.King0){
                    return isThreatened(piece, PieceColor.White)
                }
            }
        }
        return false
    }


    fun isDoingCheckmate(color: PieceColor): Boolean{
        var tempHistory = moveHistory.copyOf()
        val pieces = if (color ==PieceColor.Black) {
            whitePieces
        }else{
            blackPieces
        }

        for (piece in pieces.toList()) {
            for (move in possibleMoves(piece)) {
                makeMove(piece, move)
                if (!isDoingCheck(color)) {
                    moveBack()
                    moveHistory=tempHistory.copyOf()
                    return false
                }
                moveBack()
            }
        }
        moveHistory=tempHistory.copyOf()
        return true
    }


    //return an array of legal moves for a given piece
    fun possibleMoves(index: Int): IntArray {
        val color = board1d[index].pieceColor

        val oppositeColor = if (color == PieceColor.White) {
            PieceColor.Black
        } else {
            PieceColor.White
        }

        val rowOld = index / 8
        val columnOld = index % 8

        var possibleMoves = mutableListOf<Int>()

        if (board1d[index].pieceType == PieceType.Knight) {
            try {
                if (board2d[rowOld - 2][columnOld - 1].pieceColor != color) {
                    possibleMoves.add(index - 17)
                }
            } catch (e: ArrayIndexOutOfBoundsException) {
                null
            }
            try {
                if (board2d[rowOld - 2][columnOld + 1].pieceColor != color) {
                    possibleMoves.add(index - 15)
                }
            } catch (e: ArrayIndexOutOfBoundsException) {
                null
            }
            try {
                if (board2d[rowOld - 1][columnOld - 2].pieceColor != color) {
                    possibleMoves.add(index - 10)
                }
            } catch (e: ArrayIndexOutOfBoundsException) {
                null
            }
            try {
                if (board2d[rowOld - 1][columnOld + 2].pieceColor != color) {
                    possibleMoves.add(index - 6)
                }
            } catch (e: ArrayIndexOutOfBoundsException) {
                null
            }
            try {
                if (board2d[rowOld + 1][columnOld - 2].pieceColor != color) {
                    possibleMoves.add(index + 6)
                }
            } catch (e: ArrayIndexOutOfBoundsException) {
                null
            }
            try {
                if (board2d[rowOld + 1][columnOld + 2].pieceColor != color) {
                    possibleMoves.add(index + 10)
                }
            } catch (e: ArrayIndexOutOfBoundsException) {
                null
            }
            try {
                if (board2d[rowOld + 2][columnOld - 1].pieceColor != color) {
                    possibleMoves.add(index + 15)
                }
            } catch (e: ArrayIndexOutOfBoundsException) {
                null
            }
            try {
                if (board2d[rowOld + 2][columnOld + 1].pieceColor != color) {
                    possibleMoves.add(index + 17)
                }
            } catch (e: ArrayIndexOutOfBoundsException) {
                null
            }
        } else if (board1d[index].pieceType == PieceType.King || board1d[index].pieceType == PieceType.King0) {
            if (rowOld - 1 > -1 && columnOld - 1 > -1) {
                if (board2d[rowOld - 1][columnOld - 1].pieceColor != color) {
                    possibleMoves.add(index - 9)
                }
            }
            if (rowOld - 1 > -1 && columnOld + 1 < 8) {
                if (board2d[rowOld - 1][columnOld + 1].pieceColor != color) {
                    possibleMoves.add(index - 7)
                }
            }
            if (rowOld + 1 < 8 && columnOld - 1 > -1) {
                if (board2d[rowOld + 1][columnOld - 1].pieceColor != color) {
                    possibleMoves.add(index + 7)
                }
            }
            if (rowOld + 1 < 8 && columnOld + 1 < 8) {
                if (board2d[rowOld + 1][columnOld + 1].pieceColor != color) {
                    possibleMoves.add(index + 9)
                }
            }
            if (rowOld - 1 > -1) {
                if (board2d[rowOld - 1][columnOld].pieceColor != color) {
                    possibleMoves.add(index - 8)
                }
            }
            if (rowOld + 1 < 8) {
                if (board2d[rowOld + 1][columnOld].pieceColor != color) {
                    possibleMoves.add(index + 8)
                }
            }
            if (columnOld - 1 > -1) {
                if (board2d[rowOld][columnOld - 1].pieceColor != color) {
                    possibleMoves.add(index - 1)
                }
            }
            if (columnOld + 1 < 8) {
                if (board2d[rowOld][columnOld + 1].pieceColor != color) {
                    possibleMoves.add(index + 1)
                }
            }
            // check for castling
            if (board1d[index].pieceType == PieceType.King0) {
                if (board1d[index + 3].pieceType == PieceType.Rock0 &&
                    board1d[index + 2].pieceType == PieceType.Non &&
                    board1d[index + 1].pieceType == PieceType.Non
                ) {
                    if (!isThreatened(index, oppositeColor) &&
                        !isThreatened(index + 2, oppositeColor) &&
                        !isThreatened(index + 1, oppositeColor)
                    ) {
                        if (board1d[index].pieceColor == PieceColor.White) {
                            if (board1d[52].pieceColor == PieceColor.Black && board1d[52].pieceType == PieceType.Pawn ||
                                board1d[54].pieceColor == PieceColor.Black && board1d[54].pieceType == PieceType.Pawn ||
                                board1d[55].pieceColor == PieceColor.Black && board1d[55].pieceType == PieceType.Pawn
                            ) {
                                null
                            } else {
                                possibleMoves.add(index + 2)
                            }
                        } else {
                            if (board1d[12].pieceColor == PieceColor.White && board1d[12].pieceType == PieceType.Pawn ||
                                board1d[14].pieceColor == PieceColor.White && board1d[14].pieceType == PieceType.Pawn ||
                                board1d[15].pieceColor == PieceColor.White && board1d[15].pieceType == PieceType.Pawn
                            ) {
                                null
                            } else {
                                possibleMoves.add(index + 2)
                            }
                        }
                    }
                }
                if (board1d[index - 4].pieceType == PieceType.Rock0 &&
                    board1d[index - 3].pieceType == PieceType.Non &&
                    board1d[index - 2].pieceType == PieceType.Non &&
                    board1d[index - 1].pieceType == PieceType.Non
                ) {
                    if (!isThreatened(index, oppositeColor) &&
                        !isThreatened(index - 2, oppositeColor) &&
                        !isThreatened(index - 1, oppositeColor)
                    ) {
                        if (board1d[index].pieceColor == PieceColor.White) {
                            if (board1d[49].pieceColor == PieceColor.Black && board1d[49].pieceType == PieceType.Pawn ||
                                board1d[50].pieceColor == PieceColor.Black && board1d[50].pieceType == PieceType.Pawn ||
                                board1d[52].pieceColor == PieceColor.Black && board1d[52].pieceType == PieceType.Pawn
                            ) {
                                null
                            } else {
                                possibleMoves.add(index - 2)
                            }
                        } else {
                            if (board1d[9].pieceColor == PieceColor.White && board1d[9].pieceType == PieceType.Pawn ||
                                board1d[10].pieceColor == PieceColor.White && board1d[10].pieceType == PieceType.Pawn ||
                                board1d[12].pieceColor == PieceColor.White && board1d[12].pieceType == PieceType.Pawn
                            ) {
                                null
                            } else {
                                possibleMoves.add(index - 2)
                            }
                        }
                    }
                }
            }
        }
        //Calculate Pawn possible moves
        else if (board1d[index].pieceType == PieceType.Pawn) {
            var num = -1
            if (board1d[index].pieceColor == PieceColor.Black) {
                num = 1
            }
            if (((board1d[index].pieceColor == PieceColor.White && (index / 8) == 6) ||
                        (board1d[index].pieceColor == PieceColor.Black && (index / 8) == 1)) &&
                board2d[rowOld + num * 2][columnOld].pieceColor == PieceColor.Non &&
                board2d[rowOld + num][columnOld].pieceColor == PieceColor.Non
            ) {
                possibleMoves.add (index + num * 16)
            }
            try {
                if (board2d[rowOld + num][columnOld].pieceColor == PieceColor.Non) {
                    possibleMoves.add (index + num * 8)
                }
            } catch (e: ArrayIndexOutOfBoundsException) {
                null
            }
            try {
                if (board2d[rowOld + num][columnOld + 1].pieceColor == oppositeColor ||
                    (board2d[rowOld + num][columnOld + 1].enPassant)
                ) {
                    possibleMoves.add (index + num * 8 + 1)
                }
            } catch (e: ArrayIndexOutOfBoundsException) {
                null
            }
            try {
                if (board2d[rowOld + num][columnOld - 1].pieceColor == oppositeColor ||
                    (board2d[rowOld + num][columnOld - 1].enPassant)
                ) {
                    possibleMoves.add (index + num * 8 - 1)
                }
            } catch (e: ArrayIndexOutOfBoundsException) {
                null
            }
        }
        //Calculate Rock possible moves
        else if (board1d[index].pieceType == PieceType.Rock || board1d[index].pieceType == PieceType.Rock0) {
            for (n in 1..7) {
                if (rowOld + n < 8) {
                    if (board2d[rowOld + n][columnOld].pieceColor == PieceColor.Non) {
                        possibleMoves.add (index + n * 8)
                    } else if (board2d[rowOld + n][columnOld].pieceColor == oppositeColor) {
                        possibleMoves.add (index + n * 8)
                        break
                    } else {
                        break
                    }
                } else {
                    break
                }
            }
            for (n in 1..7) {
                if (rowOld - n > -1) {
                    if (board2d[rowOld - n][columnOld].pieceColor == PieceColor.Non) {
                        possibleMoves.add (index - n * 8)
                    } else if (board2d[rowOld - n][columnOld].pieceColor == oppositeColor) {
                        possibleMoves.add (index - n * 8)
                        break
                    } else {
                        break
                    }
                } else {
                    break
                }
            }
            for (n in 1..7) {
                if (columnOld + n < 8) {
                    if (board2d[rowOld][columnOld + n].pieceColor == PieceColor.Non) {
                        possibleMoves.add (index + n)
                    } else if (board2d[rowOld][columnOld + n].pieceColor == oppositeColor) {
                        possibleMoves.add (index + n)
                        break
                    } else {
                        break
                    }
                } else {
                    break
                }
            }
            for (n in 1..7) {
                if (columnOld - n > -1) {
                    if (board2d[rowOld][columnOld - n].pieceColor == PieceColor.Non) {
                        possibleMoves.add (index - n)
                    } else if (board2d[rowOld][columnOld - n].pieceColor == oppositeColor) {
                        possibleMoves.add (index - n)
                        break
                    } else {
                        break
                    }
                } else {
                    break
                }
            }
        }
        //Calculate Bishop Possible moves
        else if (board1d[index].pieceType == PieceType.Bishop) {
            for (n in 1..7) {
                if (rowOld + n < 8 && columnOld + n < 8) {
                    if (board2d[rowOld + n][columnOld + n].pieceColor == PieceColor.Non) {
                        possibleMoves.add (index + n * 9)
                    } else if (board2d[rowOld + n][columnOld + n].pieceColor == oppositeColor) {
                        possibleMoves.add (index + n * 9)
                        break
                    } else {
                        break
                    }
                } else {
                    break
                }
            }
            for (n in 1..7) {
                if (rowOld - n > -1 && columnOld + n < 8) {
                    if (board2d[rowOld - n][columnOld + n].pieceColor == PieceColor.Non) {
                        possibleMoves.add (index - n * 7)
                    } else if (board2d[rowOld - n][columnOld + n].pieceColor == oppositeColor) {
                        possibleMoves.add (index - n * 7)
                        break
                    } else {
                        break
                    }
                } else {
                    break
                }
            }
            for (n in 1..7) {
                if (rowOld + n < 8 && columnOld - n > -1) {
                    if (board2d[rowOld + n][columnOld - n].pieceColor == PieceColor.Non) {
                        possibleMoves.add (index + n * 7)
                    } else if (board2d[rowOld + n][columnOld - n].pieceColor == oppositeColor) {
                        possibleMoves.add (index + n * 7)
                        break
                    } else {
                        break
                    }
                } else {
                    break
                }
            }
            for (n in 1..7) {
                if (rowOld - n > -1 && columnOld - n > -1) {
                    if (board2d[rowOld - n][columnOld - n].pieceColor == PieceColor.Non) {
                        possibleMoves.add (index - n * 9)
                    } else if (board2d[rowOld - n][columnOld - n].pieceColor == oppositeColor) {
                        possibleMoves.add (index - n * 9)
                        break
                    } else {
                        break
                    }
                } else {
                    break
                }
            }
        }
        //Calculate Queen Posible moves
        else if (board1d[index].pieceType == PieceType.Queen) {
            for (n in 1..7) {
                if (rowOld + n < 8) {
                    if (board2d[rowOld + n][columnOld].pieceColor == PieceColor.Non) {
                        possibleMoves.add (index + n * 8)
                    } else if (board2d[rowOld + n][columnOld].pieceColor == oppositeColor) {
                        possibleMoves.add (index + n * 8)
                        break
                    } else {
                        break
                    }
                } else {
                    break
                }
            }
            for (n in 1..7) {
                if (rowOld - n > -1) {
                    if (board2d[rowOld - n][columnOld].pieceColor == PieceColor.Non) {
                        possibleMoves.add (index - n * 8)
                    } else if (board2d[rowOld - n][columnOld].pieceColor == oppositeColor) {
                        possibleMoves.add (index - n * 8)
                        break
                    } else {
                        break
                    }
                } else {
                    break
                }
            }
            for (n in 1..7) {
                if (columnOld + n < 8) {
                    if (board2d[rowOld][columnOld + n].pieceColor == PieceColor.Non) {
                        possibleMoves.add (index + n)
                    } else if (board2d[rowOld][columnOld + n].pieceColor == oppositeColor) {
                        possibleMoves.add (index + n)
                        break
                    } else {
                        break
                    }
                } else {
                    break
                }
            }
            for (n in 1..7) {
                if (columnOld - n > -1) {
                    if (board2d[rowOld][columnOld - n].pieceColor == PieceColor.Non) {
                        possibleMoves.add (index - n)
                    } else if (board2d[rowOld][columnOld - n].pieceColor == oppositeColor) {
                        possibleMoves.add (index - n)
                        break
                    } else {
                        break
                    }
                } else {
                    break
                }
            }
            for (n in 1..7) {
                if (rowOld + n < 8 && columnOld + n < 8) {
                    if (board2d[rowOld + n][columnOld + n].pieceColor == PieceColor.Non) {
                        possibleMoves.add (index + n * 9)
                    } else if (board2d[rowOld + n][columnOld + n].pieceColor == oppositeColor) {
                        possibleMoves.add (index + n * 9)
                        break
                    } else {
                        break
                    }
                } else {
                    break
                }
            }
            for (n in 1..7) {
                if (rowOld - n > -1 && columnOld + n < 8) {
                    if (board2d[rowOld - n][columnOld + n].pieceColor == PieceColor.Non) {
                        possibleMoves.add (index - n * 7)
                    } else if (board2d[rowOld - n][columnOld + n].pieceColor == oppositeColor) {
                        possibleMoves.add (index - n * 7)
                        break
                    } else {
                        break
                    }
                } else {
                    break
                }
            }
            for (n in 1..7) {
                if (rowOld + n < 8 && columnOld - n > -1) {
                    if (board2d[rowOld + n][columnOld - n].pieceColor == PieceColor.Non) {
                        possibleMoves.add (index + n * 7)
                    } else if (board2d[rowOld + n][columnOld - n].pieceColor == oppositeColor) {
                        possibleMoves.add (index + n * 7)
                        break
                    } else {
                        break
                    }
                } else {
                    break
                }
            }
            for (n in 1..7) {
                if (rowOld - n > -1 && columnOld - n > -1) {
                    if (board2d[rowOld - n][columnOld - n].pieceColor == PieceColor.Non) {
                        possibleMoves.add (index - n * 9)
                    } else if (board2d[rowOld - n][columnOld - n].pieceColor == oppositeColor) {
                        possibleMoves.add (index - n * 9)
                        break
                    } else {
                        break
                    }
                } else {
                    break
                }
            }
        }
        return possibleMoves.toIntArray()
    }


    fun isValidMove(first: Int, second: Int):Boolean{

        if (board1d[first].pieceColor==board1d[second].pieceColor){
            return false
        }

        val color = board1d[first].pieceColor
        val oppositeColor: PieceColor = if (color == PieceColor.White) PieceColor.Black else PieceColor.White

        val rowOld = first / 8
        val columnOld = first % 8

        val rowNew = second / 8
        val columnNew = second % 8

        val rowDiff = rowNew - rowOld
        val columnDiff = columnNew- columnOld

        //Knight valid move
        if (board1d[first].pieceType == PieceType.Knight) {
            return (abs(rowDiff) ==2 && abs(columnDiff) ==1) ||
                    (abs(rowDiff) ==1 && abs(columnDiff) ==2)
        }
        //King valid move
        else if (board1d[first].pieceType == PieceType.King || board1d[first].pieceType == PieceType.King0) {
            if ((abs(rowDiff) ==1 && abs(columnDiff) ==1) ||
                (abs(rowDiff) ==0 && abs(columnDiff) ==1) ||
                (abs(rowDiff) ==1 && abs(columnDiff) ==0)){
                return true
            }
            // check for castling
            if (board1d[first].pieceType == PieceType.King0 && abs(first-second) ==2 && !isThreatened(first,oppositeColor)) {
                if (second>first){
                    if (board1d[first+3].pieceType==PieceType.Rock0 &&
                        board1d[first+1].pieceType == PieceType.Non && !isThreatened(first + 1,oppositeColor) &&
                        board1d[first+2].pieceType == PieceType.Non && !isThreatened(first + 2,oppositeColor)){
                        return if (color == PieceColor.White) {
                            !(board1d[52].pieceColor == PieceColor.Black && board1d[52].pieceType == PieceType.Pawn ||
                                    board1d[54].pieceColor == PieceColor.Black && board1d[54].pieceType == PieceType.Pawn ||
                                    board1d[55].pieceColor == PieceColor.Black && board1d[55].pieceType == PieceType.Pawn)
                        } else {
                            !(board1d[12].pieceColor == PieceColor.White && board1d[12].pieceType == PieceType.Pawn ||
                                    board1d[14].pieceColor == PieceColor.White && board1d[14].pieceType == PieceType.Pawn ||
                                    board1d[15].pieceColor == PieceColor.White && board1d[15].pieceType == PieceType.Pawn)
                        }
                    }
                    return false
                }else{
                    if (board1d[first-4].pieceType==PieceType.Rock0 &&
                        board1d[first-1].pieceType == PieceType.Non && !isThreatened(first - 1,oppositeColor) &&
                        board1d[first-2].pieceType == PieceType.Non && !isThreatened(first - 2,oppositeColor) &&
                        board1d[first-3].pieceType == PieceType.Non ){
                        return if (color == PieceColor.White) {
                            !(board1d[49].pieceColor == PieceColor.Black && board1d[49].pieceType == PieceType.Pawn ||
                                    board1d[50].pieceColor == PieceColor.Black && board1d[50].pieceType == PieceType.Pawn ||
                                    board1d[52].pieceColor == PieceColor.Black && board1d[52].pieceType == PieceType.Pawn)
                        } else {
                            !(board1d[9].pieceColor == PieceColor.White && board1d[9].pieceType == PieceType.Pawn ||
                                    board1d[10].pieceColor == PieceColor.White && board1d[10].pieceType == PieceType.Pawn ||
                                    board1d[12].pieceColor == PieceColor.White && board1d[12].pieceType == PieceType.Pawn)
                        }
                    }
                    return false
                }
            }else{
                return false
            }
        }
        //Pawn valid move
        else if (board1d[first].pieceType == PieceType.Pawn) {
            if (color==PieceColor.White){
                if (first == second+8 && board1d[second].pieceType==PieceType.Non){
                    return true
                }else if (first == second+16 && board1d[second].pieceType==PieceType.Non &&
                            board1d[first-8].pieceType==PieceType.Non && rowOld==6){
                    return true
                }else if ((first == second+7 || first == second+9)  &&  abs(columnDiff)==1 &&
                    (board1d[second].pieceColor==oppositeColor || board1d[second].enPassant)){
                    return true
                }
            }else{
                if (first==second-8 && board1d[second].pieceType==PieceType.Non){
                    return true
                }else if (first == second-16 && board1d[second].pieceType==PieceType.Non &&
                    board1d[first+8].pieceType==PieceType.Non && rowOld==1){
                    return true
                }else if ((first == second-7 || first == second-9)  && abs(columnDiff)==1 &&
                    (board1d[second].pieceColor==oppositeColor || board1d[second].enPassant)){
                    return true
                }
            }
            return false
        }
        //Rock valid moves
        else if (board1d[first].pieceType == PieceType.Rock || board1d[first].pieceType == PieceType.Rock0) {
            if (rowDiff != 0 && columnDiff !=0){
                return false
            }
            if (rowDiff > 1){
                for (i in 1 until rowDiff){
                    if (board2d[rowOld+i][columnOld].pieceType != PieceType.Non){
                        return false
                    }
                }
            }else if(rowDiff < -1){
                for (i in 1 until abs(rowDiff)){
                    if (board2d[rowOld-i][columnOld].pieceType != PieceType.Non){
                        return false
                    }
                }
            }else if(columnDiff < -1){
                for (i in 1 until abs(columnDiff)){
                    if (board2d[rowOld][columnOld-i].pieceType != PieceType.Non){
                        return false
                    }
                }
            }else if(columnDiff > 1){
                for (i in 1 until abs(columnDiff)){
                    if (board2d[rowOld][columnOld+i].pieceType != PieceType.Non){
                        return false
                    }
                }
            }
            return true
        }
        //Bishop valid moves
        else if (board1d[first].pieceType == PieceType.Bishop) {
            if (abs(rowDiff)!=abs(columnDiff)){
                return false
            }
            if (rowDiff > 1 && columnDiff > 1){
                for (i in 1 until rowDiff){
                    if (board2d[rowOld+i][columnOld+i].pieceType != PieceType.Non){
                        return false
                    }
                }
            }else if(rowDiff > 1 && columnDiff < -1){
                for (i in 1 until rowDiff){
                    if (board2d[rowOld+i][columnOld-i].pieceType != PieceType.Non){
                        return false
                    }
                }
            }else if(rowDiff < -1 && columnDiff < -1){
                for (i in 1 until abs(rowDiff)){
                    if (board2d[rowOld-i][columnOld-i].pieceType != PieceType.Non){
                        return false
                    }
                }
            }else if(rowDiff < -1 && columnDiff > 1){
                for (i in 1 until abs(rowDiff)){
                    if (board2d[rowOld-i][columnOld+i].pieceType != PieceType.Non){
                        return false
                    }
                }
            }
            return true
        }
        //Queen valid moves
        else if (board1d[first].pieceType == PieceType.Queen) {
            if (abs(rowDiff)!=abs(columnDiff) && (rowDiff != 0 && columnDiff !=0)){
                return false
            }
            if (rowDiff ==0 || columnDiff == 0){
                if (rowDiff > 1){
                    for (i in 1 until rowDiff){
                        if (board2d[rowOld+i][columnOld].pieceType != PieceType.Non){
                            return false
                        }
                    }
                }else if(rowDiff < -1){
                    for (i in 1 until abs(rowDiff)){
                        if (board2d[rowOld-i][columnOld].pieceType != PieceType.Non){
                            return false
                        }
                    }
                }else if(columnDiff < -1){
                    for (i in 1 until abs(columnDiff)){
                        if (board2d[rowOld][columnOld-i].pieceType != PieceType.Non){
                            return false
                        }
                    }
                }else if(columnDiff > 1){
                    for (i in 1 until abs(columnDiff)){
                        if (board2d[rowOld][columnOld+i].pieceType != PieceType.Non){
                            return false
                        }
                    }
                }
            }else{
                if (rowDiff > 1 && columnDiff > 1){
                    for (i in 1 until rowDiff){
                        if (board2d[rowOld+i][columnOld+i].pieceType != PieceType.Non){
                            return false
                        }
                    }
                }else if(rowDiff > 1 && columnDiff < -1){
                    for (i in 1 until rowDiff){
                        if (board2d[rowOld+i][columnOld-i].pieceType != PieceType.Non){
                            return false
                        }
                    }
                }else if(rowDiff < -1 && columnDiff < -1){
                    for (i in 1 until abs(rowDiff)){
                        if (board2d[rowOld-i][columnOld-i].pieceType != PieceType.Non){
                            return false
                        }
                    }
                }else if(rowDiff < -1 && columnDiff > 1){
                    for (i in 1 until abs(rowDiff)){
                        if (board2d[rowOld-i][columnOld+i].pieceType != PieceType.Non){
                            return false
                        }
                    }
                }
            }
            return true
        }
        return false
    }
}