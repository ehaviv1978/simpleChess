package com.eran.simplechess

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import kotlinx.android.synthetic.main.activity_main.*
import android.content.res.ColorStateList
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Build
import android.os.Handler
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate


class MainActivity : AppCompatActivity() {

    private val sharedPrefFile = "com.eran.simplechess.theme"

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        vsSwitch.isChecked = true
        button_back.isEnabled = false
        button_play.isEnabled = false
        button_forward.isEnabled = false

        val sharedPreferences: SharedPreferences = this.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        var isDarkTheme = sharedPreferences.getBoolean("themeDark_key", true)

        val handler = Handler()

        val visualBoard = arrayOf(
            checker0,
            checker1,
            checker2,
            checker3,
            checker4,
            checker5,
            checker6,
            checker7,
            checker8,
            checker9,
            checker10,
            checker11,
            checker12,
            checker13,
            checker14,
            checker15,
            checker16,
            checker17,
            checker18,
            checker19,
            checker20,
            checker21,
            checker22,
            checker23,
            checker24,
            checker25,
            checker26,
            checker27,
            checker28,
            checker29,
            checker30,
            checker31,
            checker32,
            checker33,
            checker34,
            checker35,
            checker36,
            checker37,
            checker38,
            checker39,
            checker40,
            checker41,
            checker42,
            checker43,
            checker44,
            checker45,
            checker46,
            checker47,
            checker48,
            checker49,
            checker50,
            checker51,
            checker52,
            checker53,
            checker54,
            checker55,
            checker56,
            checker57,
            checker58,
            checker59,
            checker60,
            checker61,
            checker62,
            checker63
        )

        var game = ChessGame()
        var handPiece: Int? = null
        val computerColor = PieceColor.Black
        var maxComputerLvl = 3
        var computerAI = ChessAI(game, computerColor, maxComputerLvl-1)

        val mpError = MediaPlayer.create(this, R.raw.error)
        val mpMove = MediaPlayer.create(this, R.raw.click)
        val mpWin = MediaPlayer.create(this, R.raw.player_win)
        val mpLoos = MediaPlayer.create(this, R.raw.computer_win)
        val mpDraw = MediaPlayer.create(this, R.raw.draw)
        val mpNew = MediaPlayer.create(this, R.raw.new_game)

        fun theme(isDark : Boolean){
            if (isDark) {
                button_theme.text = "Light"
                mainView.setBackgroundColor(Color.BLACK)
                textGameInfo.setTextColor(Color.LTGRAY)
                sound_switch.setTextColor(Color.LTGRAY)
                sound_switch.trackTintList = ColorStateList.valueOf(Color.LTGRAY)
                vsSwitch.setTextColor(Color.LTGRAY)
                vsSwitch.trackTintList = ColorStateList.valueOf(Color.LTGRAY)
                radio_one.setTextColor(Color.LTGRAY)
                radio_two.setTextColor(Color.LTGRAY)
                radio_three.setTextColor(Color.LTGRAY)
                radio_one.buttonTintList = ColorStateList.valueOf(Color.LTGRAY)
                radio_two.buttonTintList = ColorStateList.valueOf(Color.LTGRAY)
                radio_three.buttonTintList = ColorStateList.valueOf(Color.LTGRAY)
                isDarkTheme = true
                val editor: SharedPreferences.Editor = sharedPreferences.edit()
                editor.putBoolean("themeDark_key", isDarkTheme)
                editor.apply()
                editor.commit()
            } else {
                button_theme.text = "Dark"
                mainView.setBackgroundColor(Color.WHITE)
                textGameInfo.setTextColor(Color.BLACK)
                sound_switch.setTextColor(Color.BLACK)
                sound_switch.trackTintList = ColorStateList.valueOf(Color.DKGRAY)
                vsSwitch.setTextColor(Color.BLACK)
                vsSwitch.trackTintList = ColorStateList.valueOf(Color.DKGRAY)
                radio_one.setTextColor(Color.BLACK)
                radio_two.setTextColor(Color.BLACK)
                radio_three.setTextColor(Color.BLACK)
                radio_one.buttonTintList = ColorStateList.valueOf(Color.GRAY)
                radio_two.buttonTintList = ColorStateList.valueOf(Color.GRAY)
                radio_three.buttonTintList = ColorStateList.valueOf(Color.GRAY)
                isDarkTheme = false
                val editor: SharedPreferences.Editor = sharedPreferences.edit()
                editor.putBoolean("themeDark_key", isDarkTheme)
                editor.apply()
                editor.commit()
            }
        }

        theme(isDarkTheme)


        //Draw chess pieces on board according to game state
        fun drawBord() {
            for (i in 0..63) {
                visualBoard[i].setImageResource(R.drawable.empty)
                when (game.board1d[i].pieceColor) {
                    PieceColor.White -> {
                        when (game.board1d[i].pieceType) {
                            PieceType.King, PieceType.King0 -> visualBoard[i].setImageResource(R.drawable.king_white)
                            PieceType.Queen -> visualBoard[i].setImageResource(R.drawable.queen_white)
                            PieceType.Rock, PieceType.Rock0 -> visualBoard[i].setImageResource(R.drawable.rock_white)
                            PieceType.Knight -> visualBoard[i].setImageResource(R.drawable.knight_white)
                            PieceType.Bishop -> visualBoard[i].setImageResource(R.drawable.bishop_white)
                            PieceType.Pawn -> visualBoard[i].setImageResource(R.drawable.pawn_white)
                        }
                    }
                    PieceColor.Black -> {
                        when (game.board1d[i].pieceType) {
                            PieceType.King, PieceType.King0 -> visualBoard[i].setImageResource(R.drawable.king_black)
                            PieceType.Queen -> visualBoard[i].setImageResource(R.drawable.queen_black)
                            PieceType.Rock, PieceType.Rock0 -> visualBoard[i].setImageResource(R.drawable.rock_black)
                            PieceType.Knight -> visualBoard[i].setImageResource(R.drawable.knight_black)
                            PieceType.Bishop -> visualBoard[i].setImageResource(R.drawable.bishop_black)
                            PieceType.Pawn -> visualBoard[i].setImageResource(R.drawable.pawn_black)
                        }
                    }
                }
            }
        }


        fun colorPossibleMoves(index: Int) {
            if (game.possibleMoves(index).isNotEmpty()) {
                game.possibleMoves(index).forEach {
                    if (game.board1d[it].pieceType == PieceType.King || game.board1d[it].pieceType == PieceType.King0) {
                        visualBoard[it].backgroundTintList = ColorStateList.valueOf(Color.RED)
                    } else if (game.board1d[it].pieceColor != PieceColor.Non || game.board1d[it].enPassant) {
                        visualBoard[it].backgroundTintList = ColorStateList.valueOf(Color.YELLOW)
                    } else {
                        visualBoard[it].backgroundTintList = ColorStateList.valueOf(Color.LTGRAY)
                    }
                }
            }
        }


        fun colorCheckPieces() {
            if (game.isDoingCheck(PieceColor.White)) {
                for (blackPiece in game.blackPieces) {
                    if (game.board1d[blackPiece].pieceType == PieceType.King || game.board1d[blackPiece].pieceType == PieceType.King0) {
                        visualBoard[blackPiece].backgroundTintList =
                            ColorStateList.valueOf(Color.RED)
                        for (whitPiece in game.whitePieces) {
                            if (blackPiece in game.possibleMoves(whitPiece)) {
                                visualBoard[whitPiece].backgroundTintList =
                                    ColorStateList.valueOf(Color.RED)
                            }
                        }
                    }
                }
            } else if (game.isDoingCheck(PieceColor.Black)) {
                for (whitePiece in game.whitePieces) {
                    if (game.board1d[whitePiece].pieceType == PieceType.King || game.board1d[whitePiece].pieceType == PieceType.King0) {
                        visualBoard[whitePiece].backgroundTintList =
                            ColorStateList.valueOf(Color.RED)
                        for (blackPiece in game.blackPieces) {
                            if (whitePiece in game.possibleMoves(blackPiece)) {
                                visualBoard[blackPiece].backgroundTintList =
                                    ColorStateList.valueOf(Color.RED)
                            }
                        }
                    }
                }
            }
        }


        fun clearBackgroundColor() {
            for (i in 0..63) {
                visualBoard[i].backgroundTintList = null
            }
        }


        fun removeHandPiece() {
            clearBackgroundColor()
            if (handPiece != null) {
                visualBoard[handPiece!!].backgroundTintList = null
                handPiece = null
            }
        }

        fun lockBoard() {
            for (item in visualBoard) {
                item.isClickable = false
            }
        }

        fun unlockBoard() {
            for (item in visualBoard) {
                item.isClickable = true
            }
        }


        fun disableButtons() {
            button_play.isEnabled = false
            button_back.isEnabled = false
            button_forward.isEnabled = false
            button_play.imageTintList = ColorStateList.valueOf(Color.parseColor("#ffff8800"))
            button_forward.imageTintList = ColorStateList.valueOf(Color.parseColor("#ffff8800"))
            button_back.imageTintList = ColorStateList.valueOf(Color.parseColor("#ffff8800"))
        }


        fun computerMove() {
            var computerMove = computerAI.makeMove()
            lockBoard()
            if (computerMove.first == 99) {
                textGameInfo.text = "Computer surrender - You Won!!"
                button_back.isEnabled = true
                button_back.imageTintList = null
                return
            }
            clearBackgroundColor()
            visualBoard[computerMove.first].backgroundTintList =
                ColorStateList.valueOf(Color.YELLOW)
            visualBoard[computerMove.last].backgroundTintList = ColorStateList.valueOf(Color.YELLOW)
            handler.postDelayed({
                drawBord()
                if (sound_switch.isChecked) {
                    mpMove.pause()
                    mpMove.stop()
                    mpMove.prepare()
                    mpMove.start()
                }
                handler.postDelayed({
                    visualBoard[computerMove.first].backgroundTintList = null
                    visualBoard[computerMove.last].backgroundTintList = null
                    colorCheckPieces()
                }, 200)
                unlockBoard()
                if (game.isDoingCheck(computerColor)) {
                    if (game.isDoingCheckmate(computerColor)) {
                        lockBoard()
                        textGameInfo.text = "Computer Checkmate!!"
                        if (sound_switch.isChecked) {
                            mpLoos.start()
                        }
                    } else {
                        textGameInfo.text = "Computer Check!!"
                        if (sound_switch.isChecked) {
                            mpError.start()
                        }
                    }
                } else {
                    if (game.isDraw(PieceColor.White)) {
                        lockBoard()
                        textGameInfo.text = "Draw!!"
                        if (sound_switch.isChecked) {
                            mpDraw.start()
                        }
                    } else {
                        textGameInfo.text = "White Turn"
                    }
                }
            }, 400)

            button_back.isEnabled = true
            button_back.imageTintList = null
            button_forward.isEnabled = false
            button_forward.imageTintList = ColorStateList.valueOf(Color.parseColor("#ffff8800"))
        }

        fun checkerClick(checker: ImageButton) {
            if (handPiece != null) {
                if (handPiece == checker.transitionName.toInt()) {
                    clearBackgroundColor()
                    handPiece = null
                    colorCheckPieces()
                    return
                }
                if (game.board1d[handPiece!!].pieceColor == game.board1d[checker.transitionName.toInt()].pieceColor) {
                    clearBackgroundColor()
                    checker.backgroundTintList = ColorStateList.valueOf(Color.DKGRAY)
                    handPiece = checker.transitionName.toInt()
                    colorPossibleMoves(handPiece!!)
                    colorCheckPieces()
                    return
                }
                if (!game.isValidMove(handPiece!!, checker.transitionName.toInt())) {
                    return
                }
                game.makeMove(handPiece!!, checker.transitionName.toInt())
                if (sound_switch.isChecked) {
                    mpMove.start()
                }
                button_back.isEnabled = true
                button_back.imageTintList = null
                button_forward.isEnabled = false
                button_forward.imageTintList = ColorStateList.valueOf(Color.parseColor("#ffff8800"))
                if (game.isDoingCheck(game.otherColor(game.board1d[checker.transitionName.toInt()].pieceColor))) {
                    colorCheckPieces()
                    game.moveBack()
                    if (sound_switch.isChecked) {
                        mpError.start()
                    }
                    textGameInfo.text = "Illegal Move!!!"
                    return
                }
                clearBackgroundColor()
                handPiece = null
                drawBord()
                if (game.isDoingCheck(game.board1d[checker.transitionName.toInt()].pieceColor)) {
                    colorCheckPieces()
                    if (game.isDoingCheckmate(game.board1d[checker.transitionName.toInt()].pieceColor)) {
                        lockBoard()
                        textGameInfo.text =
                            game.board1d[checker.transitionName.toInt()].pieceColor.toString() + " Checkmate!"
                        if (sound_switch.isChecked) {
                            mpWin.start()
                        }
                        return
                    } else {
                        textGameInfo.text =
                            game.board1d[checker.transitionName.toInt()].pieceColor.toString() + " Check!"
                        if (sound_switch.isChecked) {
                            mpError.start()
                        }
                    }
                } else {
                    if (game.isDraw(game.turnColor)) {
                        lockBoard()
                        textGameInfo.text = "Draw!!"
                        if (sound_switch.isChecked) {
                            mpDraw.start()
                        }
                        return
                    }
                    textGameInfo.text = game.turnColor.toString() + " Turn"
                }
                if (vsSwitch.isChecked && game.turnColor == computerColor) {
                    disableButtons()
                    textGameInfo.text = "Computer thinking..."
                    handler.postDelayed({
                        computerMove()
                    }, 10)
                    return
                }//else{
                // textGameInfo.text = game.turnColor.toString() + " Turn"
                //}
            } else {
                if (game.turnColor != game.board1d[checker.transitionName.toInt()].pieceColor) {
                    return
                }
                handPiece = checker.transitionName.toInt()
                checker.backgroundTintList = ColorStateList.valueOf(Color.DKGRAY)
                colorPossibleMoves(handPiece!!)
            }
        }


        //  initiate all checkers on click listeners
        for (checker in visualBoard) {
            checker.setOnClickListener { checkerClick(checker) }
        }


        //Go back one move for each click
        button_back.setOnClickListener {
            unlockBoard()
            removeHandPiece()
            game.moveBack()
            drawBord()
            colorCheckPieces()
            if (game.isDoingCheck(game.otherColor(game.turnColor))) {
                colorCheckPieces()
                if (game.isDoingCheckmate(game.otherColor(game.turnColor))) {
                    lockBoard()
                    textGameInfo.text = game.otherColor(game.turnColor).toString() + " Checkmate!"
                } else {
                    textGameInfo.text = game.otherColor(game.turnColor).toString() + " Check!"
                }
            } else {
                textGameInfo.text = game.turnColor.toString() + " Turn"
            }
            button_forward.isEnabled = true
            button_forward.imageTintList = null
            if (game.moveHistoryPointer == 0) {
                button_back.isEnabled = false
                button_back.imageTintList = ColorStateList.valueOf(Color.parseColor("#ffff8800"))
            }
            if (game.turnColor == computerColor && vsSwitch.isChecked) {
                textGameInfo.text = "Press '>' for computer to play"
                button_play.isEnabled = true
                button_play.imageTintList = null
            } else {
                button_play.isEnabled = false
                button_play.imageTintList = ColorStateList.valueOf(Color.parseColor("#ffff8800"))
            }
        }


        button_forward.setOnClickListener {
            removeHandPiece()
            game.moveForward()
            drawBord()
            colorCheckPieces()
            if (game.isDoingCheck(game.otherColor(game.turnColor))) {
                colorCheckPieces()
                if (game.isDoingCheckmate(game.otherColor(game.turnColor))) {
                    lockBoard()
                    textGameInfo.text = game.otherColor(game.turnColor).toString() + " Checkmate!"
                } else {
                    textGameInfo.text = game.otherColor(game.turnColor).toString() + " Check!"
                }
            } else {
                textGameInfo.text = game.turnColor.toString() + " Turn"
            }
            if (game.moveHistoryPointer > game.moveHistory.size - 2) {
                button_forward.isEnabled = false
                button_forward.imageTintList = ColorStateList.valueOf(Color.parseColor("#ffff8800"))
            }
            button_back.isEnabled = true
            button_back.imageTintList = null
            if (game.turnColor == computerColor && vsSwitch.isChecked) {
                textGameInfo.text = "Press '>' for computer to play"
                button_play.isEnabled = true
                button_play.imageTintList = null
            } else {
                button_play.isEnabled = false
                button_play.imageTintList = ColorStateList.valueOf(Color.parseColor("#ffff8800"))
            }
        }


        button_new.setOnClickListener {
            if (sound_switch.isChecked) {
                mpNew.start()
            }
            button_play.isEnabled = false
            button_back.isEnabled = false
            button_forward.isEnabled = false
            button_play.imageTintList = ColorStateList.valueOf(Color.parseColor("#ffff8800"))
            button_forward.imageTintList = ColorStateList.valueOf(Color.parseColor("#ffff8800"))
            button_back.imageTintList = ColorStateList.valueOf(Color.parseColor("#ffff8800"))
            textGameInfo.text = "White move first"
            removeHandPiece()
            game = ChessGame()
            computerAI = ChessAI(game, PieceColor.Black, computerAI.compLvl)
            drawBord()
            unlockBoard()
        }


        //switch the computer level
        radioGroup.setOnCheckedChangeListener { _, _ ->
            if (radio_one.isChecked) {
                computerAI.compLvl = maxComputerLvl-2
            } else if (radio_two.isChecked) {
                computerAI.compLvl = maxComputerLvl-1
            } else {
                computerAI.compLvl = maxComputerLvl
            }
        }


        //order computer to play after move backword/forword
        button_play.setOnClickListener {
            if (vsSwitch.isChecked && game.turnColor == computerColor) {
                disableButtons()
                textGameInfo.text = "Computer thinking..."
                handler.postDelayed({
                    computerMove()
                }, 10)
            }
        }


        // vs computer play on / off toggle
        vsSwitch.setOnCheckedChangeListener { _, _ ->
            if (vsSwitch.isChecked) {
                if (game.turnColor == computerColor) {
                    disableButtons()
                    textGameInfo.text = "Computer thinking..."
                    handler.postDelayed({
                        computerMove()
                    }, 10)
                }

            } else {
                button_play.isEnabled = false
                button_play.imageTintList = ColorStateList.valueOf(Color.parseColor("#ffff8800"))
                textGameInfo.text = game.turnColor.toString() + " turn"
            }
        }

        
        button_theme.setOnClickListener {
            if (isDarkTheme){
                theme(false)
            } else {
               theme(true)
            }

        }
    }
}