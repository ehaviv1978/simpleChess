package com.eran.simplechess

class ChessAI (private val game: ChessGame, private val colorAI: PieceColor) {

    private val piecesAI = if (colorAI ==PieceColor.Black) game.blackPieces else game.whitePieces
    private val compLvl=3


    fun makeMove() : ChessMove{
        val oppositeColor: PieceColor = if (colorAI ==PieceColor.Black) PieceColor.White else PieceColor.Black
        var minMax = 99999;
        var maxMin = -999999;
        var bestIndex =99
        var bestIndex2 =99
        var computerMove = ChessMove(bestIndex,bestIndex2)

        for (index in piecesAI.toMutableList().shuffled()) {
            for (index2 in game.possibleMoves(index).toMutableList().shuffled()) {
                game.makeMove(index, index2);
                if (game.isCheck(oppositeColor)) {
                    game.moveBack();
                    continue;
                }
                minMax = moveScore(oppositeColor, maxMin, compLvl);
                if (maxMin < minMax) {
                    maxMin = minMax;
                    bestIndex = index;
                    bestIndex2 = index2;
                }
                game.moveBack();
            }
        }
        if (bestIndex==99) return computerMove
        game.makeMove(bestIndex,bestIndex2)
        computerMove = ChessMove(bestIndex,bestIndex2)
        return computerMove
    }


    private fun moveScore(color: PieceColor, score :Int, depth: Int): Int {
        var min = 99999;
        var max = -99999;
        val oppositeColor: PieceColor = if (color ==PieceColor.Black) PieceColor.White else PieceColor.Black

        val piecesArray =  if (color==PieceColor.Black) {
            game.blackPieces;
        } else {
            game.whitePieces;
        }

        for (index in piecesArray.toIntArray())
        {
            for (index2 in 0..63) {
                if (game.isValidMove(index,index2)){
                    game.makeMove(index, index2);
                    if (game.isCheck(oppositeColor)) {
                        game.moveBack();
                        continue;
                    }
                    if (depth > 1) {
                        if (color == PieceColor.White) {
                            min = moveScore(PieceColor.Black, min, depth - 1)
                        } else {
                            max = moveScore(PieceColor.White, max, depth - 1)
                        }
                    } else {
                        if (color == PieceColor.White) {
                            if (min > boardScore()) {min = boardScore()}
                        } else {
                            if (max < boardScore()) {max = boardScore()}
                        }
                    }
                    game.moveBack();
                    if (color == PieceColor.White) {
                        if (min <= score)  {return score}
                    } else {
                        if (max >= score)  {return score}
                    }
                }
            }
//            for (index2 in game.possibleMoves(index)) {
//                game.makeMove(index, index2);
//                if (game.isCheck(oppositeColor)) {
//                    game.moveBack();
//                    continue;
//                }
//                if (depth > 1) {
//                    if (color == PieceColor.White) {
//                        min = moveScore(PieceColor.Black, min, depth - 1)
//                    } else {
//                        max = moveScore(PieceColor.White, max, depth - 1)
//                    }
//                } else {
//                    if (color == PieceColor.White) {
//                        if (min > boardScore()) {min = boardScore()}
//                    } else {
//                        if (max < boardScore()) {max = boardScore()}
//                    }
//                }
//                game.moveBack();
//                if (color == PieceColor.White) {
//                    if (min <= score)  {return score}
//                } else {
//                    if (max >= score)  {return score}
//                }
//            }
        }
        if (min == 99999) {
            if (!game.isCheck(PieceColor.Black)) {min = 0}
        }
        if (max == -99999) {
            if (!game.isCheck(PieceColor.White)) {max = 0}
        }
        return if (color==PieceColor.White) {
            min;
        }else {
            max;
        }
    }

    // calculate board score
    private fun boardScore(): Int {
        var score = 0
        for (index in game.blackPieces.toIntArray()) {
            when (game.board1d[index].pieceType) {
                PieceType.Pawn -> score += 100
                PieceType.Knight -> score += 350
                PieceType.Bishop -> score += 350
                PieceType.Rock, PieceType.Rock0 -> score += 525
                PieceType.Queen -> score += 1000
                PieceType.King, PieceType.King0 -> score += 10000
            }
        }
        for (index in game.whitePieces.toIntArray()) {
            when (game.board1d[index].pieceType) {
                PieceType.Pawn -> score -= 100
                PieceType.Knight -> score -= 350
                PieceType.Bishop -> score -= 350
                PieceType.Rock, PieceType.Rock0 -> score -= 525
                PieceType.Queen -> score -= 1000
                PieceType.King, PieceType.King0 -> score -= 10000
            }
        }
        return score
    }
}

