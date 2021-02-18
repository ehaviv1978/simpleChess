package com.eran.simplechess

class ChessAI (private val game: ChessGame, private val colorAI: PieceColor, var compLvl: Int) {

    private val piecesAI = if (colorAI == PieceColor.Black) game.blackPieces else game.whitePieces


    fun makeMove(): ChessMove {
        var bestMoveScore = -999999
        var bestIndex = 99
        var bestIndex2 = 99
        var computerMove = ChessMove(bestIndex, bestIndex2)

        for (index in piecesAI.toMutableList().shuffled()) {
            for (index2 in game.possibleMoves(index).toMutableList().shuffled()) {
                game.makeMove(index, index2);
                if (game.isDoingCheck(PieceColor.White)) {
                    game.moveBack()
                    continue
                }
                var tempScore = bestMove(PieceColor.White, bestMoveScore, compLvl)
//                var tempScore = whiteBestMove(bestMoveScore)
                if (tempScore > bestMoveScore) {
                    bestMoveScore = tempScore
                    bestIndex = index
                    bestIndex2 = index2
                }
                game.moveBack()
            }
        }
        if (bestIndex == 99) return computerMove
        game.makeMove(bestIndex, bestIndex2)
        computerMove = ChessMove(bestIndex, bestIndex2)
        return computerMove
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


    private fun bestMove(color: PieceColor, score: Int, depth: Int): Int {
        val oppositeColor = if (color==PieceColor.White) PieceColor.Black else PieceColor.White
        var bestMoveScore = if (color==PieceColor.White) 99999 else -99999
        val pieces = if (color==PieceColor.White) game.whitePieces.toList() else game.blackPieces.toList()

        for (index in pieces) {
            for (index2 in game.possibleMoves(index)) {
                game.makeMove(index, index2);
                if (game.isDoingCheck(oppositeColor)) {
                    game.moveBack()
                    continue
                }
                val tempScore = if (depth > 1) bestMove(oppositeColor, bestMoveScore, depth - 1) else boardScore()
                if (color==PieceColor.White){
                    if (tempScore <= score) {
                        game.moveBack()
                        return score
                    }
                    if (tempScore < bestMoveScore) {
                        bestMoveScore = tempScore
                    }
                }else{
                    if (tempScore >= score) {
                        game.moveBack()
                        return score
                    }
                    if (tempScore > bestMoveScore) {
                        bestMoveScore = tempScore
                    }
                }
                game.moveBack()
            }
        }
        return bestMoveScore
    }

//    private fun bestMove(color: PieceColor, score: Int, depth: Int): Int {
//        if (color==PieceColor.White){
//            var bestMoveScore = 99999
//
//            for (index in game.whitePieces.toList()) {
//                for (index2 in game.possibleMoves(index)) {
//                    game.makeMove(index, index2);
//                    if (game.isDoingCheck(PieceColor.Black)) {
//                        game.moveBack()
//                        continue
//                    }
//                    var tempScore = if (depth<=1) boardScore() else bestMove(PieceColor.Black, bestMoveScore,depth-1)
//                    if (tempScore<=score){
//                        game.moveBack()
//                        return score
//                    }
//                    if (tempScore < bestMoveScore) {
//                        bestMoveScore = tempScore
//                    }
//                    game.moveBack()
//                }
//            }
//            return bestMoveScore
//        }else{
//            var bestMoveScore = -99999
//
//            for (index in game.blackPieces.toList()) {
//                for (index2 in game.possibleMoves(index)) {
//                    game.makeMove(index, index2);
//                    if (game.isDoingCheck(PieceColor.White)) {
//                        game.moveBack()
//                        continue
//                    }
//                    var tempScore = if (depth<=1) boardScore() else bestMove(PieceColor.White, bestMoveScore,depth-1)
//                    if (tempScore>=score){
//                        game.moveBack()
//                        return score
//                    }
//                    if (tempScore > bestMoveScore) {
//                        bestMoveScore = tempScore
//                    }
//                    game.moveBack()
//                }
//            }
//            return bestMoveScore
//        }
//    }


//    private fun whiteBestMove(score: Int): Int {
//        var bestMoveScore = 99999
//
//        for (index in game.whitePieces.toList()) {
//            for (index2 in game.possibleMoves(index)) {
//                game.makeMove(index, index2);
//                if (game.isDoingCheck(PieceColor.Black)) {
//                    game.moveBack()
//                    continue
//                }
//                var tempScore = if (compLvl==1) boardScore() else blackBestMove(bestMoveScore)
//                if (tempScore<=score){
//                    game.moveBack()
//                    return score
//                }
//                if (tempScore < bestMoveScore) {
//                    bestMoveScore = tempScore
//                }
//                game.moveBack()
//            }
//        }
//        return bestMoveScore
//    }
//
//    private fun blackBestMove(score: Int): Int {
//        var bestMoveScore = -99999
//
//        for (index in game.blackPieces.toList()) {
//            for (index2 in game.possibleMoves(index)) {
//                game.makeMove(index, index2);
//                if (game.isDoingCheck(PieceColor.White)) {
//                    game.moveBack()
//                    continue
//                }
//                var tempScore = if (compLvl==2) boardScore() else whiteBestMove2(bestMoveScore)
//                if (tempScore>=score){
//                    game.moveBack()
//                    return score
//                }
//                if (tempScore > bestMoveScore) {
//                    bestMoveScore = tempScore
//                }
//                game.moveBack()
//            }
//        }
//        return bestMoveScore
//    }
//
//    private fun whiteBestMove2(score: Int): Int {
//        var bestMoveScore = 99999
//
//        for (index in game.whitePieces.toList()) {
//            for (index2 in game.possibleMoves(index)) {
//                game.makeMove(index, index2);
//                if (game.isDoingCheck(PieceColor.Black)) {
//                    game.moveBack()
//                    continue
//                }
//                var tempScore = if (compLvl==3) boardScore() else blackBestMove2(bestMoveScore)
//                if (tempScore<=score){
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
//        return bestMoveScore
//    }
//
//    private fun blackBestMove2(score: Int): Int {
//        var bestMoveScore = -99999
//
//        for (index in game.blackPieces.toList()) {
//            for (index2 in game.possibleMoves(index)) {
//                game.makeMove(index, index2);
//                if (game.isDoingCheck(PieceColor.White)) {
//                    game.moveBack()
//                    continue
//                }
//                var tempScore = boardScore()
//                if (tempScore>=score){
//                    game.moveBack()
//                    return score
//                }
//                if (tempScore > bestMoveScore) {
//                    bestMoveScore = tempScore
//                }
//                game.moveBack()
//            }
//        }
//        return bestMoveScore
//    }
}
