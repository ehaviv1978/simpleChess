package com.eran.simplechess

class ChessAI (private val game: ChessGame, private val colorAI: PieceColor, var compLvl: Int) {

    private val piecesAI = if (colorAI ==PieceColor.Black) game.blackPieces else game.whitePieces


    fun makeMove() : ChessMove {
        var bestMoveScore = -99999
        var bestIndex =99
        var bestIndex2 =99
        var computerMove = ChessMove(bestIndex,bestIndex2)

        for (index in piecesAI.toMutableList().shuffled()) {
            for (index2 in game.possibleMoves(index).toMutableList().shuffled()) {
                game.makeMove(index, index2);
                if (game.isDoingCheck(PieceColor.White)) {
                    game.moveBack()
                    continue
                }
                var tempScore = bestMove(PieceColor.White,bestMoveScore,compLvl)
                if (tempScore> bestMoveScore) {
                    bestMoveScore = tempScore
                    bestIndex = index
                    bestIndex2 = index2
                }
                game.moveBack()
            }
        }
        if (bestIndex==99) return computerMove
        game.makeMove(bestIndex,bestIndex2)
        computerMove = ChessMove(bestIndex,bestIndex2)
        return computerMove
    }


    private fun bestMove(color: PieceColor, score: Int, depth:Int): Int{
        var whiteBestMoveScore = 99999
        var blackBestMoveScore = -99999

        val oppositeColor = if (color == PieceColor.Black) PieceColor.White else PieceColor.Black

        val piecesArray =  if (color==PieceColor.Black) game.blackPieces; else game.whitePieces;

        for (index in piecesArray.toIntArray()) {
            for (index2 in game.possibleMoves(index).toTypedArray()) {
                game.makeMove(index, index2);
                if (game.isDoingCheck(oppositeColor)) {
                    game.moveBack()
                    continue
                }
                if (color == PieceColor.White) {
                    var tempScore = if (depth > 1) bestMove(oppositeColor, whiteBestMoveScore, depth - 1) else boardScore()
                    if (tempScore <= score) {
                        game.moveBack()
                        return score
                    }
                    if (tempScore < whiteBestMoveScore) {
                        whiteBestMoveScore = tempScore
                    }
                } else {
                    var tempScore = if (depth > 1) bestMove(oppositeColor, blackBestMoveScore, depth - 1) else boardScore()
                    if (tempScore >= score) {
                        game.moveBack()
                        return score
                    }
                    if (tempScore > blackBestMoveScore) {
                        blackBestMoveScore = tempScore
                    }
                }
                game.moveBack()
            }
        }

        return if (color== PieceColor.White) whiteBestMoveScore else blackBestMoveScore
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


//
//    fun whiteBestMove(score:Int): Int{
//        var bestMoveScore = 99999
//
//        for (index in game.whitePieces.toMutableList().shuffled()) {
//            for (index2 in game.possibleMoves(index).toMutableList().shuffled()) {
//                game.makeMove(index, index2);
//                if (game.isDoingCheck(PieceColor.Black)) {
//                    game.moveBack()
//                    continue
//                }
//                var tempScore = blackBestMove(bestMoveScore)
//                if (tempScore<=score) {
//                    game.moveBack()
//                    return score
//                }
//                if (tempScore < bestMoveScore) {
//                    bestMoveScore = tempScore
//
//                }
//                game.moveBack()
//            }
//        }
//
//        return bestMoveScore
//    }
//
//
//    fun blackBestMove(score:Int): Int{
//        var bestMoveScore = -99999
//
//        for (index in game.blackPieces.toMutableList().shuffled()) {
//            for (index2 in game.possibleMoves(index).toMutableList().shuffled()) {
//                game.makeMove(index, index2);
//                if (game.isDoingCheck(PieceColor.White)) {
//                    game.moveBack()
//                    continue
//                }
//                var tempScore = whiteBestMove2(bestMoveScore)
//                if (tempScore>=score) {
//                    game.moveBack()
//                    return score
//                }
//                if (tempScore > bestMoveScore) {
//                    bestMoveScore = tempScore
//                }
//                game.moveBack()
//            }
//        }
//
//        return bestMoveScore
//    }
//
//
//    fun whiteBestMove2(score:Int): Int{
//        var bestMoveScore = 99999
//
//        for (index in game.whitePieces.toMutableList().shuffled()) {
//            for (index2 in game.possibleMoves(index).toMutableList().shuffled()) {
//                game.makeMove(index, index2);
//                if (game.isDoingCheck(PieceColor.Black)) {
//                    game.moveBack()
//                    continue
//                }
//                var tempScore = boardScore()
//                if (tempScore<=score) {
//                    game.moveBack()
//                    return score
//                }
//                if (tempScore < bestMoveScore) {
//                    bestMoveScore = tempScore
//
//                }
//                game.moveBack()
//            }
//        }
//
//        return bestMoveScore
//    }

//    fun makeMove() : ChessMove{
//        val oppositeColor: PieceColor = if (colorAI ==PieceColor.Black) PieceColor.White else PieceColor.Black
//        var minMax = 99999;
//        var maxMin = -999999;
//        var bestIndex =99
//        var bestIndex2 =99
//        var computerMove = ChessMove(bestIndex,bestIndex2)
//
//        for (index in piecesAI.toMutableList().shuffled()) {
//            for (index2 in game.possibleMoves(index).toMutableList().shuffled()) {
//                game.makeMove(index, index2);
//                if (game.isDoingCheck(oppositeColor)) {
//                    game.moveBack();
//                    continue;
//                }
//                minMax = moveScore(oppositeColor, maxMin, compLvl);
//                if (maxMin < minMax) {
//                    maxMin = minMax;
//                    bestIndex = index;
//                    bestIndex2 = index2;
//                }
//                game.moveBack();
//            }
//        }
//        if (bestIndex==99) return computerMove
//        game.makeMove(bestIndex,bestIndex2)
//        computerMove = ChessMove(bestIndex,bestIndex2)
//        return computerMove
//    }
//
//
//    private fun moveScore(color: PieceColor, score :Int, depth: Int): Int {
//        var min = 99999;
//        var max = -99999;
//        val oppositeColor: PieceColor = if (color ==PieceColor.Black) PieceColor.White else PieceColor.Black
//
//        val piecesArray =  if (color==PieceColor.Black) {
//            game.blackPieces;
//        } else {
//            game.whitePieces;
//        }
//
//        for (index in piecesArray.toIntArray())
//        {
//            for (index2 in 0..63) {
//                if (game.isValidMove(index,index2)){
//                    game.makeMove(index, index2);
//                    if (game.isDoingCheck(oppositeColor)) {
//                        game.moveBack();
//                        continue;
//                    }
//                    if (depth > 1) {
//                        if (color == PieceColor.White) {
//                            min = moveScore(PieceColor.Black, min, depth - 1)
//                        } else {
//                            max = moveScore(PieceColor.White, max, depth - 1)
//                        }
//                    } else {
//                        if (color == PieceColor.White) {
//                            if (min > boardScore()) {min = boardScore()}
//                        } else {
//                            if (max < boardScore()) {max = boardScore()}
//                        }
//                    }
//                    game.moveBack();
//                    if (color == PieceColor.White) {
//                        if (min <= score)  {return score}
//                    } else {
//                        if (max >= score)  {return score}
//                    }
//                }
//            }
//        }
////        if (min == 99999) {
////            if (!game.isDoingCheck(PieceColor.Black)) {min = 0}
////        }
////        if (max == -99999) {
////            if (!game.isDoingCheck(PieceColor.White)) {max = 0}
////        }
//        return if (color==PieceColor.White) {
//            min;
//        }else {
//            max;
//        }
//    }
