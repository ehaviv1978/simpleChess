package com.eran.simplechess

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import androidx.core.graphics.drawable.toBitmap
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.abs
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Handler
import kotlinx.coroutines.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        data class BestMove(var first: ImageButton, var last: ImageButton, var score: Int, var checkmate: Boolean)
        data class TempPiece (var tag:String,var contentDescription: String,var scrollBarSize: Int,var K0:Boolean,
                              var r0: Boolean,var q:Boolean)
        data class CheckerStatus(var tag: String, var contentDescription: String, var scrollBarSize: Int)
        val handler =Handler()

        var boardsStatus = arrayOf<Array<CheckerStatus>>()

        val board1d = arrayOf(checker0,checker1,checker2, checker3,checker4,checker5, checker6, checker7,
            checker8,checker9,checker10,checker11,checker12,checker13,checker14,checker15,checker16,
            checker17,checker18,checker19,checker20,checker21,checker22,checker23,checker24,checker25,
            checker26,checker27,checker28,checker29,checker30,checker31,checker32,checker33,checker34,
            checker35, checker36,checker37,checker38,checker39,checker40,checker41,checker42,checker43,
            checker44,checker45,checker46, checker47,checker48,checker49,checker50,checker51,checker52,
            checker53,checker54,checker55,checker56,checker57, checker58,checker59,checker60,checker61,
            checker62,checker63)
        val boardRandom = mutableListOf(checker0,checker1,checker2, checker3,checker4,checker5, checker6, checker7,
            checker8,checker9,checker10,checker11,checker12,checker13,checker14,checker15,checker16,
            checker17,checker18,checker19,checker20,checker21,checker22,checker23,checker24,checker25,
            checker26,checker27,checker28,checker29,checker30,checker31,checker32,checker33,checker34,
            checker35, checker36,checker37,checker38,checker39,checker40,checker41,checker42,checker43,
            checker44,checker45,checker46, checker47,checker48,checker49,checker50,checker51,checker52,
            checker53,checker54,checker55,checker56,checker57, checker58,checker59,checker60,checker61,
            checker62,checker63)
        boardRandom.shuffle()
        var compLvl =2
        var board2d = arrayOf<Array<ImageButton>>()
        var handPice = checker30.drawable.toBitmap()
        val noPice = checker30.drawable.toBitmap()
        var tempChecker = checker00
        var turn = "white"
        var openining = 1
        var boardStatusPointer=0
        val mp_error = MediaPlayer.create(this, R.raw.error)
        val mp_move = MediaPlayer.create(this, R.raw.click)
        val mp_win=MediaPlayer.create(this,R.raw.player_win)
        val mp_loos=MediaPlayer.create(this,R.raw.computer_win)
        val mp_draw=MediaPlayer.create(this,R.raw.draw)
        var counter =0

        button_back.isEnabled=false
        button_play.isEnabled=false
        button_forward.isEnabled=false
        vsSwitch.isChecked= true
        sound_switch.isChecked=false

        checker00.tag =""
        checker00.contentDescription=""

        for (i in 16..47){
            board1d[i].tag =""
            board1d[i].contentDescription=""
        }

        for (item in board1d){
            item.scrollBarSize = 4
        }

        // fill board2d array with elements
        var n = 0
        for (i in 0..7) {
            var array = arrayOf<ImageButton>()
            for (j in 0..7) {
                array += board1d[n]
                n++
            }
            board2d += array
        }

        // call a row from the board as an array of checkers
        fun row(n: Int): Array<ImageButton>{
            var array = arrayOf<ImageButton>()
            for(i in 0..7){
                array += board2d[n][i]
            }
            return array
        }

        // call a column from the board as an array of checkers
        fun column(n: Int): Array<ImageButton>{
            var array = arrayOf<ImageButton>()
            for(i in 0..7){
                array += board2d[i][n]
            }
            return array
        }

        // all the rows on the board as arrays
        var row0= row(0)
        var row1= row(1)
        var row2= row(2)
        var row3= row(3)
        var row4= row(4)
        var row5= row(5)
        var row6= row(6)
        var row7= row(7)

        // all the column on the board as arrays
    //    var column0= column(0)
    //    var column1= column(1)
        var column2= column(2)
    //    var column3= column(3)
    //    var column4= column(4)
    //    var column5= column(5)
        var column6= column(6)
    //    var column7= column(7)


        // checks if a move is valid and return true or false
        fun isValidMove(oldChecker:ImageButton, newChecker:ImageButton):Boolean{
            var diff = newChecker.transitionName.toInt() - oldChecker.transitionName.toInt()
            val rowOld = oldChecker.transitionName.toInt()/8
            val columnOld = oldChecker.transitionName.toInt()%8
            val rowNew = newChecker.transitionName.toInt()/8
            val columnNew = newChecker.transitionName.toInt()%8
            val rowDiff = rowNew - rowOld
            val columnDiff = columnNew -columnOld
            var num =2
            var opositTurn = "str"
            if (oldChecker.tag == "white"){opositTurn="black"} else {opositTurn ="white"}
            if (oldChecker.tag == "white") {num = -1} else {num = 1}

            // checks if specific square is under attack
            fun isThretened(checker: ImageButton, color: String):Boolean{
                for (square in board1d){
                    if (square.tag==color){
                        if (isValidMove(square, checker)){return true}
                    }
                }
                return false
            }

            //checks knight move
            if (oldChecker.contentDescription == "k"){
                if ((abs(rowDiff)==2 && abs(columnDiff)==1) || (abs(rowDiff)==1 && abs(columnDiff)==2)){
                    return true
                }
            }

            //checks pawn move
            if (oldChecker.contentDescription == "p") {
                if ((newChecker.tag == "" && columnDiff == 0 && rowDiff == 1 * num) ||
                    (newChecker.tag == opositTurn && abs(columnDiff) == 1 && rowDiff == num * 1)){
                        return true
                    }
                else if (((rowOld == 4 && newChecker.scrollBarSize == 5 && oldChecker.tag == "black") ||
                            (rowOld == 3 && newChecker.scrollBarSize == 2 && oldChecker.tag == "white")) &&
                            abs(columnDiff) == 1){
                        return true
                    }
                else if ((row6.contains(oldChecker))|| row1.contains(oldChecker)){
                      if ((newChecker.tag == ""&& abs(rowDiff)==2 && columnDiff ==0) &&
                          ((oldChecker.tag=="white"&&row5[columnOld].tag == ""&&
                                  row6.contains(oldChecker))|| (row2[columnOld].tag == "" &&
                                  oldChecker.tag=="black"&& row1.contains(oldChecker)))) {
                          return true
                      }
                }

            }

            //checks king move
            if (oldChecker.contentDescription == "K" ||oldChecker.contentDescription == "K0") {

                if (((abs(rowDiff) == 1) && (abs(columnDiff) == 1)) || ((abs(rowDiff) == 1) && (columnDiff==0)) ||
                    ((rowDiff == 0) && (abs(columnDiff) == 1))){
                    return true
                }

                //checks kingside castling validity
                else if (oldChecker.contentDescription=="K0" && columnOld== 4 &&
                    (rowOld==0 || rowOld == 7) &&diff == 2 && !isThretened(oldChecker,opositTurn) &&
                    board1d[oldChecker.transitionName.toInt()+3].contentDescription == "r0" &&
                    board1d[oldChecker.transitionName.toInt()+2].contentDescription == "" &&
                    !isThretened(board1d[oldChecker.transitionName.toInt()+2],opositTurn) &&
                    board1d[oldChecker.transitionName.toInt()+1].contentDescription == "" &&
                    !isThretened(board1d[oldChecker.transitionName.toInt()+1],opositTurn)){
                    if (oldChecker.tag == "white"){
                        if (row6[3].tag == "black"&& row6[3].contentDescription =="p"||
                            row6[4].tag == "black"&& row6[4].contentDescription =="p"||
                            row6[5].tag == "black"&& row6[5].contentDescription =="p"||
                            row6[6].tag == "black"&& row6[6].contentDescription =="p"||
                            row6[7].tag == "black"&& row6[7].contentDescription =="p"){
                            return false
                        }
                    }
                    else{
                        if (row1[3].tag == "white"&& row1[3].contentDescription =="p"||
                            row1[4].tag == "white"&& row1[4].contentDescription =="p"||
                            row1[5].tag == "white"&& row1[5].contentDescription =="p"||
                            row1[6].tag == "white"&& row1[6].contentDescription =="p"||
                            row1[7].tag == "white"&& row1[7].contentDescription =="p"){
                            return false
                        }
                    }
                    return true
                }

                //checks queenside castling validity
                else if (oldChecker.contentDescription=="K0" && columnOld== 4 &&
                    (rowOld==0 || rowOld == 7)&& diff == -2 &&
                    !isThretened(oldChecker,opositTurn) &&
                    board1d[oldChecker.transitionName.toInt()-4].contentDescription == "r0" &&
                    board1d[oldChecker.transitionName.toInt()-3].contentDescription == "" &&
                    !isThretened(board1d[oldChecker.transitionName.toInt()-3],opositTurn) &&
                    board1d[oldChecker.transitionName.toInt()-2].contentDescription == "" &&
                    !isThretened(board1d[oldChecker.transitionName.toInt()-2],opositTurn) &&
                    board1d[oldChecker.transitionName.toInt()-1].contentDescription == "" &&
                    !isThretened(board1d[oldChecker.transitionName.toInt()-1],opositTurn)){
                    if (oldChecker.tag == "white"){
                        if (row6[1].tag == "black"&& row6[1].contentDescription =="p"||
                            row6[2].tag == "black"&& row6[2].contentDescription =="p"||
                            row6[3].tag == "black"&& row6[3].contentDescription =="p"||
                            row6[4].tag == "black"&& row6[4].contentDescription =="p"||
                            row6[5].tag == "black"&& row6[5].contentDescription =="p"){
                            return false
                        }
                    }
                   else{
                        if (row1[1].tag == "white"&& row1[1].contentDescription =="p"||
                            row1[2].tag == "white"&& row1[2].contentDescription =="p"||
                            row1[3].tag == "white"&& row1[3].contentDescription =="p"||
                            row1[4].tag == "white"&& row1[4].contentDescription =="p"||
                            row1[5].tag == "white"&& row1[5].contentDescription =="p"){
                            return false
                        }
                    }
                    return true
                }
            }

            //checks if ways is clear of peaces
            fun checkWay(distance:Int):Boolean{
                for (n in 1..(distance - 1)) {
                    if (rowDiff > 0 && columnDiff > 0) {
                            if (board2d[rowOld + (rowDiff - n)][columnOld + (columnDiff - n)].tag != "") {
                                return false
                            }
                    } else if (rowDiff < 0 && columnDiff < 0) {
                            if (board2d[rowOld + (rowDiff + n)][columnOld + (columnDiff + n)].tag != "") {
                                return false
                            }
                    } else if (rowDiff < 0 && columnDiff > 0) {
                            if (board2d[rowOld + (rowDiff + n)][columnOld + (columnDiff - n)].tag != "") {
                                return false
                            }
                    } else if (rowDiff > 0 && columnDiff < 0) {
                            if (board2d[rowOld + (rowDiff - n)][columnOld + (columnDiff + n)].tag != "") {
                                return false
                            }
                    } else if (rowDiff > 0) {
                            if (board2d[rowOld + (rowDiff - n)][columnOld].tag != "") {
                                return false
                            }
                    } else if (rowDiff < 0) {
                            if (board2d[rowOld + (rowDiff + n)][columnOld].tag != "") {
                                return false
                            }
                    } else if (columnDiff > 0) {
                            if (board2d[rowOld][columnOld + (columnDiff - n)].tag != "") {
                                return false
                            }
                    } else if (columnDiff < 0) {
                            if (board2d[rowOld][columnOld + (columnDiff + n)].tag != "") {
                                return false
                            }
                    }
                }
                // return true if way is clear
                return true
            }

            //checks rock move
            if (oldChecker.contentDescription == "r" || oldChecker.contentDescription == "r0") {
                for (i in (1..7)){
                    if (((abs(rowDiff) == i) && (columnDiff==0)) || ((rowDiff == 0) && (abs(columnDiff) == i))){
                        if (i >1) {
                            if (checkWay(i)) {
                                return true
                            }
                        }
                        else {
                            return true
                        }
                    }
                }
            }

            //checks bishop move
            if (oldChecker.contentDescription == "b") {
                for (i in (1..7)){
                    if (((abs(rowDiff) == i) && (abs(columnDiff)==i))){
                        if (i >1) {
                            return checkWay(i)
                        }
                        else {return true}
                    }
                }
            }

            //checks queen move
            if (oldChecker.contentDescription == "q") {
                for (i in (1..7)) {
                    if (((abs(rowDiff) == i) && (abs(columnDiff) == i)) ||((abs(rowDiff) == i) && (columnDiff==0)) ||
                        ((rowDiff == 0) && (abs(columnDiff) == i))) {
                        if (i > 1) {
                            return checkWay(i)
                        }
                        else {return true}
                    }
                }
            }
            //return false if move is invalid
            return false
        }


        // checks if specific square is under attack
        fun isThretened(checker: ImageButton, color: String):Boolean{
            for (square in board1d){
                if (square.tag==color){
                    if (isValidMove(square, checker)){return true}
                }
            }
            return false
        }


        // retrun an array of legal moves for a given pice
        fun possibleMoves(checker:ImageButton): ArrayList<ImageButton>{
            val color = checker.tag
            var opositeColor :String
            val rowOld = checker.transitionName.toInt()/8
            val columnOld = checker.transitionName.toInt()%8
            if (color == "white"){opositeColor="black"} else {opositeColor ="white"}
            var possibleMoves = ArrayList<ImageButton>()

            if (checker.contentDescription == "k"){
                try {
                    if (board2d[rowOld + 2][columnOld + 1].tag != color) {
                        possibleMoves.add(board2d[rowOld + 2][columnOld + 1])
                    }
                }
                catch (e: ArrayIndexOutOfBoundsException){null}
                try {
                    if (board2d[rowOld+2][columnOld-1].tag!=color){
                        possibleMoves.add(board2d[rowOld+2][columnOld-1])
                    }
                }
                catch (e: ArrayIndexOutOfBoundsException){null}
                try {
                    if (board2d[rowOld-2][columnOld+1].tag!=color){
                        possibleMoves.add(board2d[rowOld-2][columnOld+1])
                    }
                }
                catch (e: ArrayIndexOutOfBoundsException){null}
                try {
                    if (board2d[rowOld-2][columnOld-1].tag!=color){
                        possibleMoves.add(board2d[rowOld-2][columnOld-1])
                    }
                }
                catch (e: ArrayIndexOutOfBoundsException){null}
                try {
                    if (board2d[rowOld+1][columnOld+2].tag!=color){
                        possibleMoves.add(board2d[rowOld+1][columnOld+2])
                    }
                }
                catch (e: ArrayIndexOutOfBoundsException){null}
                try {
                    if (board2d[rowOld+1][columnOld-2].tag!=color){
                        possibleMoves.add(board2d[rowOld+1][columnOld-2])
                    }
                }
                catch (e: ArrayIndexOutOfBoundsException){null}
                try {
                    if (board2d[rowOld-1][columnOld+2].tag!=color){
                        possibleMoves.add(board2d[rowOld-1][columnOld+2])
                    }
                }
                catch (e: ArrayIndexOutOfBoundsException){null}
                try {
                    if (board2d[rowOld-1][columnOld-2].tag!=color){
                        possibleMoves.add(board2d[rowOld-1][columnOld-2])
                    }
                }
                catch (e: ArrayIndexOutOfBoundsException){null}
            }

            else if(checker.contentDescription == "K" ||checker.contentDescription == "K0"){
                if (rowOld-1>-1 && columnOld-1>-1){
                    if (board2d[rowOld-1][columnOld-1].tag!=color){
                        possibleMoves.add(board2d[rowOld-1][columnOld-1])
                    }
                }
                if (rowOld-1>-1 && columnOld+1<8){
                    if (board2d[rowOld-1][columnOld+1].tag!=color){
                        possibleMoves.add(board2d[rowOld-1][columnOld+1])
                    }
                }
                if (rowOld+1<8 && columnOld-1>-1){
                    if (board2d[rowOld+1][columnOld-1].tag!=color){
                        possibleMoves.add(board2d[rowOld+1][columnOld-1])
                    }
                }
                if (rowOld+1<8 && columnOld+1<8){
                    if (board2d[rowOld+1][columnOld+1].tag!=color){
                        possibleMoves.add(board2d[rowOld+1][columnOld+1])
                    }
                }
                if (rowOld-1>-1){
                    if (board2d[rowOld-1][columnOld].tag!=color){
                        possibleMoves.add(board2d[rowOld-1][columnOld])
                    }
                }
                if (rowOld+1<8){
                    if (board2d[rowOld+1][columnOld].tag!=color){
                        possibleMoves.add(board2d[rowOld+1][columnOld])
                    }
                }
                if (columnOld-1>-1){
                    if (board2d[rowOld][columnOld-1].tag!=color){
                        possibleMoves.add(board2d[rowOld][columnOld-1])
                    }
                }
                if (columnOld+1<8){
                    if (board2d[rowOld][columnOld+1].tag!=color){
                        possibleMoves.add(board2d[rowOld][columnOld+1])
                    }
                }
                if (checker.contentDescription=="K0" && columnOld== 4 &&
                    (rowOld==0 || rowOld == 7) && !isThretened(checker,opositeColor) &&
                    board1d[checker.transitionName.toInt()+3].contentDescription == "r0" &&
                    board1d[checker.transitionName.toInt()+2].contentDescription == "" &&
                    !isThretened(board1d[checker.transitionName.toInt()+2],opositeColor) &&
                    board1d[checker.transitionName.toInt()+1].contentDescription == "" &&
                    !isThretened(board1d[checker.transitionName.toInt()+1],opositeColor)) {
                    if (checker.tag == "white") {
                        if (row6[3].tag == "black" && row6[3].contentDescription == "p" ||
                            row6[4].tag == "black" && row6[4].contentDescription == "p" ||
                            row6[5].tag == "black" && row6[5].contentDescription == "p" ||
                            row6[6].tag == "black" && row6[6].contentDescription == "p" ||
                            row6[7].tag == "black" && row6[7].contentDescription == "p") {
                            null
                        } else{
                            possibleMoves.add(board2d[rowOld][columnOld+2])
                        }
                    }
                    else {
                        if (row1[3].tag == "white" && row1[3].contentDescription == "p" ||
                            row1[4].tag == "white" && row1[4].contentDescription == "p" ||
                            row1[5].tag == "white" && row1[5].contentDescription == "p" ||
                            row1[6].tag == "white" && row1[6].contentDescription == "p" ||
                            row1[7].tag == "white" && row1[7].contentDescription == "p") {
                            null
                        } else{
                            possibleMoves.add(board2d[rowOld][columnOld+2])
                        }
                    }
                }
                if (checker.contentDescription=="K0" && columnOld== 4 &&
                    (rowOld==0 || rowOld == 7)&& !isThretened(checker,opositeColor) &&
                    board1d[checker.transitionName.toInt()-4].contentDescription == "r0" &&
                    board1d[checker.transitionName.toInt()-3].contentDescription == "" &&
                    !isThretened(board1d[checker.transitionName.toInt()-3],opositeColor) &&
                    board1d[checker.transitionName.toInt()-2].contentDescription == "" &&
                    !isThretened(board1d[checker.transitionName.toInt()-2],opositeColor) &&
                    board1d[checker.transitionName.toInt()-1].contentDescription == "" &&
                    !isThretened(board1d[checker.transitionName.toInt()-1],opositeColor)) {
                    if (checker.tag == "white") {
                        if (row6[1].tag == "black" && row6[1].contentDescription == "p" ||
                            row6[2].tag == "black" && row6[2].contentDescription == "p" ||
                            row6[3].tag == "black" && row6[3].contentDescription == "p" ||
                            row6[4].tag == "black" && row6[4].contentDescription == "p" ||
                            row6[5].tag == "black" && row6[5].contentDescription == "p"){
                            null
                            } else{
                            possibleMoves.add(board2d[rowOld][columnOld-2])
                        }
                    }
                    else {
                        if (row1[1].tag == "white" && row1[1].contentDescription == "p" ||
                            row1[2].tag == "white" && row1[2].contentDescription == "p" ||
                            row1[3].tag == "white" && row1[3].contentDescription == "p" ||
                            row1[4].tag == "white" && row1[4].contentDescription == "p" ||
                            row1[5].tag == "white" && row1[5].contentDescription == "p"){
                            null
                            } else{
                            possibleMoves.add(board2d[rowOld][columnOld-2])
                        }
                    }
                }
            }


            else if (checker.contentDescription == "p"){
                var num =-1
                if (checker.tag=="black"){num=1}
                if (((checker.tag=="white" && checker in row6) || (checker.tag=="black" && checker in row1)) &&
                    board2d[rowOld+num*2][columnOld].tag=="" && board2d[rowOld+num][columnOld].tag==""){
                    possibleMoves.add(board2d[rowOld+num*2][columnOld])
                }
                try {
                    if (board2d[rowOld+num][columnOld].tag==""){
                        possibleMoves.add(board2d[rowOld+num][columnOld])
                    }
                }
                catch (e: ArrayIndexOutOfBoundsException){null}
                try {
                    if (board2d[rowOld+num][columnOld+1].tag==opositeColor ||
                        (board2d[rowOld+num][columnOld+1].scrollBarSize==2 && checker.tag=="white") ||
                        (board2d[rowOld+num][columnOld+1].scrollBarSize==5 && checker.tag=="black")){
                        possibleMoves.add(board2d[rowOld+num][columnOld+1])
                    }
                }
                catch (e: ArrayIndexOutOfBoundsException){null}
                try {
                    if (board2d[rowOld+num][columnOld-1].tag==opositeColor ||
                        (board2d[rowOld+num][columnOld-1].scrollBarSize==2 && checker.tag=="white") ||
                        (board2d[rowOld+num][columnOld-1].scrollBarSize==5 && checker.tag=="black")  ){
                        possibleMoves.add(board2d[rowOld+num][columnOld-1])
                    }
                }
                catch (e: ArrayIndexOutOfBoundsException){null}
            }

            else if(checker.contentDescription == "r" ||checker.contentDescription == "r0") {
                for (n in 1..7){
                    if(rowOld+n<8) {
                        if (board2d[rowOld+n][columnOld].tag==""){
                            possibleMoves.add(board2d[rowOld+n][columnOld])
                        }else if (board2d[rowOld+n][columnOld].tag==opositeColor){
                            possibleMoves.add(board2d[rowOld+n][columnOld])
                            break
                        }else {break}
                    }else {break}
                }
                for (n in 1..7){
                    if(rowOld-n>-1) {
                        if (board2d[rowOld-n][columnOld].tag==""){
                            possibleMoves.add(board2d[rowOld-n][columnOld])
                        }else if (board2d[rowOld-n][columnOld].tag==opositeColor){
                            possibleMoves.add(board2d[rowOld-n][columnOld])
                            break
                        }else {break}
                    }else {break}
                }
                for (n in 1..7){
                    if(columnOld+n<8) {
                        if (board2d[rowOld][columnOld+n].tag==""){
                            possibleMoves.add(board2d[rowOld][columnOld+n])
                        }else if (board2d[rowOld][columnOld+n].tag==opositeColor){
                            possibleMoves.add(board2d[rowOld][columnOld+n])
                            break
                        }else {break}
                    }else {break}
                }
                for (n in 1..7){
                    if(columnOld-n>-1) {
                        if (board2d[rowOld][columnOld-n].tag==""){
                            possibleMoves.add(board2d[rowOld][columnOld-n])
                        }else if (board2d[rowOld][columnOld-n].tag==opositeColor){
                            possibleMoves.add(board2d[rowOld][columnOld-n])
                            break
                        }else {break}
                    }else {break}
                }
            }

            else if(checker.contentDescription == "b") {
                for (n in 1..7){
                    if(rowOld+n<8&& columnOld+n<8) {
                        if (board2d[rowOld+n][columnOld+n].tag==""){
                            possibleMoves.add(board2d[rowOld+n][columnOld+n])
                        }else if (board2d[rowOld+n][columnOld+n].tag==opositeColor){
                            possibleMoves.add(board2d[rowOld+n][columnOld+n])
                            break
                        }else {break}
                    }else {break}
                }
                for (n in 1..7){
                    if(rowOld-n>-1&& columnOld+n<8) {
                        if (board2d[rowOld-n][columnOld+n].tag==""){
                            possibleMoves.add(board2d[rowOld-n][columnOld+n])
                        }else if (board2d[rowOld-n][columnOld+n].tag==opositeColor){
                            possibleMoves.add(board2d[rowOld-n][columnOld+n])
                            break
                        }else {break}
                    }else {break}
                }
                for (n in 1..7){
                    if(rowOld+n<8&& columnOld-n>-1) {
                        if (board2d[rowOld+n][columnOld-n].tag==""){
                            possibleMoves.add(board2d[rowOld+n][columnOld-n])
                        }else if (board2d[rowOld+n][columnOld-n].tag==opositeColor){
                            possibleMoves.add(board2d[rowOld+n][columnOld-n])
                            break
                        }else {break}
                    }else {break}
                }
                for (n in 1..7){
                    if(rowOld-n>-1&& columnOld-n>-1) {
                        if (board2d[rowOld-n][columnOld-n].tag==""){
                            possibleMoves.add(board2d[rowOld-n][columnOld-n])
                        }else if (board2d[rowOld-n][columnOld-n].tag==opositeColor){
                            possibleMoves.add(board2d[rowOld-n][columnOld-n])
                            break
                        }else {break}
                    }else {break}
                }
            }

            else if(checker.contentDescription == "q") {
                for (n in 1..7){
                    if(rowOld+n<8) {
                        if (board2d[rowOld+n][columnOld].tag==""){
                            possibleMoves.add(board2d[rowOld+n][columnOld])
                        }else if (board2d[rowOld+n][columnOld].tag==opositeColor){
                            possibleMoves.add(board2d[rowOld+n][columnOld])
                            break
                        }else {break}
                    }else {break}
                }
                for (n in 1..7){
                    if(rowOld-n>-1) {
                        if (board2d[rowOld-n][columnOld].tag==""){
                            possibleMoves.add(board2d[rowOld-n][columnOld])
                        }else if (board2d[rowOld-n][columnOld].tag==opositeColor){
                            possibleMoves.add(board2d[rowOld-n][columnOld])
                            break
                        }else {break}
                    }else {break}
                }
                for (n in 1..7){
                    if(columnOld+n<8) {
                        if (board2d[rowOld][columnOld+n].tag==""){
                            possibleMoves.add(board2d[rowOld][columnOld+n])
                        }else if (board2d[rowOld][columnOld+n].tag==opositeColor){
                            possibleMoves.add(board2d[rowOld][columnOld+n])
                            break
                        }else {break}
                    }else {break}
                }
                for (n in 1..7){
                    if(columnOld-n>-1) {
                        if (board2d[rowOld][columnOld-n].tag==""){
                            possibleMoves.add(board2d[rowOld][columnOld-n])
                        }else if (board2d[rowOld][columnOld-n].tag==opositeColor){
                            possibleMoves.add(board2d[rowOld][columnOld-n])
                            break
                        }else {break}
                    }else {break}
                }
                for (n in 1..7){
                    if(rowOld+n<8&& columnOld+n<8) {
                        if (board2d[rowOld+n][columnOld+n].tag==""){
                            possibleMoves.add(board2d[rowOld+n][columnOld+n])
                        }else if (board2d[rowOld+n][columnOld+n].tag==opositeColor){
                            possibleMoves.add(board2d[rowOld+n][columnOld+n])
                            break
                        }else {break}
                    }else {break}
                }
                for (n in 1..7){
                    if(rowOld-n>-1&& columnOld+n<8) {
                        if (board2d[rowOld-n][columnOld+n].tag==""){
                            possibleMoves.add(board2d[rowOld-n][columnOld+n])
                        }else if (board2d[rowOld-n][columnOld+n].tag==opositeColor){
                            possibleMoves.add(board2d[rowOld-n][columnOld+n])
                            break
                        }else {break}
                    }else {break}
                }
                for (n in 1..7){
                    if(rowOld+n<8&& columnOld-n>-1) {
                        if (board2d[rowOld+n][columnOld-n].tag==""){
                            possibleMoves.add(board2d[rowOld+n][columnOld-n])
                        }else if (board2d[rowOld+n][columnOld-n].tag==opositeColor){
                            possibleMoves.add(board2d[rowOld+n][columnOld-n])
                            break
                        }else {break}
                    }else {break}
                }
                for (n in 1..7){
                    if(rowOld-n>-1&& columnOld-n>-1) {
                        if (board2d[rowOld-n][columnOld-n].tag==""){
                            possibleMoves.add(board2d[rowOld-n][columnOld-n])
                        }else if (board2d[rowOld-n][columnOld-n].tag==opositeColor){
                            possibleMoves.add(board2d[rowOld-n][columnOld-n])
                            break
                        }else {break}
                    }else {break}
                }
            }
            return possibleMoves
        }


        fun tempMove (item: ImageButton,item1:ImageButton):TempPiece{
            var tempPiece=TempPiece("","",4,false, false, false)
            if (item.contentDescription=="K0"){tempPiece.K0=true
            }else if (item.contentDescription=="r0"){tempPiece.r0=true}
            tempPiece.tag = item1.tag.toString()
            tempPiece.contentDescription = item1.contentDescription.toString()
            tempPiece.scrollBarSize=item1.scrollBarSize
            item1.tag = item.tag
            item1.contentDescription = item.contentDescription
            item.tag = ""
            item.contentDescription = ""
            if (item1.contentDescription=="p"){
                val column = item1.transitionName.toInt()%8
                if (item1 in row3 && item in row1){
                    row2[column].scrollBarSize=2
                }
                else if(item1 in row4 && item in row6){
                    row5[column].scrollBarSize=5
                }
                else if (tempPiece.scrollBarSize==2){
                    row3[column].tag=""
                    row3[column].contentDescription=""
                }
                else if (tempPiece.scrollBarSize==5){
                    row4[column].tag=""
                    row4[column].contentDescription=""
                }
                else if (item1 in row0 || item1 in row7){
                    item1.contentDescription = "q"
                    tempPiece.q=true
                }
            }
            else if (item1.contentDescription =="K0") {
                item1.contentDescription="K"
                if (item1 in column6){
                    board1d[item1.transitionName.toInt()-1].tag=board1d[item1.transitionName.toInt()+1].tag
                    board1d[item1.transitionName.toInt()-1].contentDescription="r"
                    board1d[item1.transitionName.toInt()+1].tag=""
                    board1d[item1.transitionName.toInt()+1].contentDescription=""
                }
                else if (item1 in column2){
                    board1d[item1.transitionName.toInt()+1].tag=board1d[item1.transitionName.toInt()-2].tag
                    board1d[item1.transitionName.toInt()+1].contentDescription="r"
                    board1d[item1.transitionName.toInt()-2].tag=""
                    board1d[item1.transitionName.toInt()-2].contentDescription=""
                }
            }
            else if (item1.contentDescription =="r0"){item1.contentDescription="r"}
            return tempPiece
        }


        fun resetMove(item:ImageButton, item1:ImageButton,tempPiece:TempPiece){
            if (item1.contentDescription == "p" && item1 in row3 && item in row1){
                row2[row3.indexOf(item1)].scrollBarSize=4
            }
            else if (item1.contentDescription == "p" && item1 in row4 && item in row6){
                row5[row4.indexOf(item1)].scrollBarSize=4
            }
            else if (tempPiece.scrollBarSize == 2){
                board1d[item1.transitionName.toInt() + 8].tag = "black"
                board1d[item1.transitionName.toInt() + 8].contentDescription = "p"
            }
            else if (tempPiece.scrollBarSize == 5){
                board1d[item1.transitionName.toInt() - 8].tag = "white"
                board1d[item1.transitionName.toInt() - 8].contentDescription = "p"
            }
            else if (item1.contentDescription == "K" && item1.transitionName.toInt() == 62 &&
                item.transitionName.toInt() == 60){
                board1d[63].tag = "white"
                board1d[63].contentDescription = "r0"
                board1d[61].tag = ""
                board1d[61].contentDescription = ""
            } else if (item1.contentDescription == "K" && item1.transitionName.toInt() == 58 &&
                item.transitionName.toInt() == 60) {
                board1d[56].tag = "white"
                board1d[56].contentDescription = "r0"
                board1d[59].tag = ""
                board1d[59].contentDescription = ""
            }
            else if (item1.contentDescription == "K" && item1.transitionName.toInt() == 6 &&
                item.transitionName.toInt() == 4) {
                board1d[7].tag = "black"
                board1d[7].contentDescription = "r0"
                board1d[5].tag = ""
                board1d[5].contentDescription = ""
            } else if (item1.contentDescription == "K" && item1.transitionName.toInt() == 2 &&
                item.transitionName.toInt() == 4) {
                board1d[0].tag = "black"
                board1d[0].contentDescription = "r0"
                board1d[3].tag = ""
                board1d[3].contentDescription = ""
            }
        }


        fun backMove (item: ImageButton,item1:ImageButton,tempPiece:TempPiece){
            if (tempPiece.K0){item1.contentDescription="K0"
            }else if (tempPiece.r0){item1.contentDescription="r0"
            }else if(tempPiece.q){ item1.contentDescription="p"}
            item.tag = item1.tag
            item.contentDescription = item1.contentDescription
            item1.tag = tempPiece.tag
            item1.contentDescription = tempPiece.contentDescription
            item1.scrollBarSize=tempPiece.scrollBarSize
        }

        // checks if king in specified color is under check
        fun isChecked(color :String):Boolean{
            var otherColor =""
            if (color =="white"){
                otherColor = "black"
            }else{
                otherColor="white"
            }
            for (square in board1d){
                if ((square.contentDescription == "K0" ||square.contentDescription == "K") && square.tag == color){
                    for (item in board1d){
                        if (item.tag==otherColor){
                            if (isValidMove(item,square)){
                                item.backgroundTintList=ColorStateList.valueOf(Color.RED)
                                return true
                            }
                        }
                    }
                }
            }
            return false
        }


        fun isCheckmate(color: String):Boolean{
            for (item in board1d){
                if (item.tag == color){
                    for (item1 in possibleMoves(item)){
                        var tempPiece = tempMove(item,item1)

                        //check if move is ligal
                        if (!isChecked(color)){
                            resetMove(item, item1,tempPiece)
                            backMove(item,item1,tempPiece)
                            return false
                        }

                        resetMove(item, item1,tempPiece)
                        backMove(item,item1,tempPiece)
                    }
                }
            }
            return true
        }


        fun isDraw(color:String):Boolean{
            for (item in board1d){
                if (item.tag==color) {
                    for (item1 in possibleMoves(item)) {
                        var tempPiece = tempMove(item,item1)

                        if (!isChecked(color)) {
                            resetMove(item, item1,tempPiece)
                            backMove(item,item1,tempPiece)
                            return false
                        }
                        resetMove(item, item1,tempPiece)
                        backMove(item,item1,tempPiece)
                    }
                }
            }
            return true
        }


        // calculate board score
        fun boardScore():Int{
            var score=0
            for (item in board1d) {
                if (item.tag=="black"){
                    if (item.contentDescription == "K" || item.contentDescription == "K0"){score += 10000}
                    else if (item.contentDescription == "r" || item.contentDescription == "r0"){score += 525}
                    else if (item.contentDescription == "q") { score += 1000 }
                    else if (item.contentDescription == "b") { score += 350 }
                    else if (item.contentDescription == "k") { score += 350 }
                    else if (item.contentDescription == "p") { score += 100}
                } else if (item.tag == "white"){
                    if (item.contentDescription == "K" || item.contentDescription == "K0"){score -= 10000}
                    else if (item.contentDescription == "r" || item.contentDescription == "r0"){score -= 525}
                    else if (item.contentDescription == "q") { score -= 1000 }
                    else if (item.contentDescription == "b") { score -= 350 }
                    else if (item.contentDescription == "k") { score -= 350 }
                    else if (item.contentDescription == "p") { score -= 100 }
                }
            }
            return score
        }


        fun moveScore(color:String,score:Int,depth :Int):Int{
            var min = 99999
            var max =-99999
            var index=99
            if (color=="black") {
                for (item in row5) {
                    if (item.scrollBarSize == 5) {
                        index = row5.indexOf(item)
                    }
                }
            }else{
                for (item in row2) {
                    if (item.scrollBarSize == 2) {
                        index = row2.indexOf(item)
                    }
                }
            }

            for (item1 in board1d) {
                if (item1.tag == color) {
                    for (item2 in possibleMoves(item1)) {

                        var tempPiece = tempMove(item1,item2)

                        if (isChecked(color)){
                            resetMove(item1, item2,tempPiece)
                            backMove(item1,item2,tempPiece)
                            continue
                        }

                        if (depth>1) {
                            if (color == "white") {
                                if (index!=99){row2[index].scrollBarSize=4}
                                min = moveScore("black", min, depth - 1)
                            } else {
                                if (index!=99){row5[index].scrollBarSize=4}
                                max = moveScore("white", max, depth - 1)
                            }
                        }else{
                            if (color == "white") {
                                if (index!=99){row2[index].scrollBarSize=4}
                                if (min>boardScore()){min=boardScore()}
                            } else {
                                if (index!=99){row5[index].scrollBarSize=4}
                                if (max<boardScore()){max=boardScore()}
                            }
                        }

                        resetMove(item1, item2,tempPiece)
                        backMove(item1,item2,tempPiece)
                        if (index!=99) {
                            if (color == "black") {
                                row5[index].scrollBarSize=5
                            } else {
                                row2[index].scrollBarSize=2
                            }
                        }
                        if (color == "white") {
                            if (min <=score) {return score}
                        }else{
                            if (max>=score){return score}
                        }
                    }
                }
            }
            if(color=="white"){return min}else{return max}
        }


        fun bestMove(): BestMove{
            var minMax = 99999
            var maxMin = -999999
            var first = checker00
            var last = checker00
            var index=99
            for (item in row5){
                if (item.scrollBarSize==5){
                    index=row5.indexOf(item)
                }
            }

            for (item1 in boardRandom) {
                if (item1.tag == "black") {
                    for (item2 in possibleMoves(item1)) {

                        var tempPiece = tempMove(item1,item2)
                        if (index!=99){row5[index].scrollBarSize=4}

                        if (isChecked("white")&&!isChecked("black")) {
                            if (isCheckmate("white")) {
                                first = item1
                                last = item2
                                resetMove(item1, item2,tempPiece)
                                backMove(item1,item2,tempPiece)
                                textView.text = "Computer Checkmate!"
                                for (item in board1d) { item.isClickable = false }
                                return BestMove(first, last, maxMin, true)
                            }
                        }
                        if (isChecked("black")){// || (isDraw("white")&& boardScore()>1000)){
                            resetMove(item1, item2,tempPiece)
                            backMove(item1,item2,tempPiece)
                            continue
                        }

                        minMax = moveScore("white", maxMin, compLvl)

                        if (maxMin < minMax) {
                            maxMin = minMax
                            first = item1
                            last = item2
                        }

                        resetMove(item1, item2,tempPiece)
                        backMove(item1,item2,tempPiece)
                        if (index!=99){row5[index].scrollBarSize=5}
                    }
                }
            }
            //button_new.text=maxMin.toString()
            return BestMove(first,last,maxMin,false)
        }

        fun boardDraw(){
            for (item in board1d){
                if (item.tag==""){
                    item.setImageResource(R.drawable.empty)
                }
                else if (item.tag=="white"){
                    if (item.contentDescription=="p"){
                        item.setImageResource(R.drawable.pawn_white)
                    }
                    else if (item.contentDescription=="q"){
                        item.setImageResource(R.drawable.queen_white)
                    }
                    else if (item.contentDescription=="k"){
                        item.setImageResource(R.drawable.knight_white)
                    }
                    else if (item.contentDescription=="b"){
                        item.setImageResource(R.drawable.bishop_white)
                    }
                    else if (item.contentDescription=="r"|| item.contentDescription=="r0"){
                        item.setImageResource(R.drawable.rock_white)
                    }
                    else if (item.contentDescription=="K"|| item.contentDescription=="K0"){
                        item.setImageResource(R.drawable.king_white)
                    }
                }
                else{
                    if (item.contentDescription=="p"){
                        item.setImageResource(R.drawable.pawn_black)
                    }
                    else if (item.contentDescription=="q"){
                        item.setImageResource(R.drawable.queen_black)
                    }
                    else if (item.contentDescription=="k"){
                        item.setImageResource(R.drawable.knight_black)
                    }
                    else if (item.contentDescription=="b"){
                        item.setImageResource(R.drawable.bishop_black)
                    }
                    else if (item.contentDescription=="r"|| item.contentDescription=="r0"){
                        item.setImageResource(R.drawable.rock_black)
                    }
                    else if (item.contentDescription=="K"|| item.contentDescription=="K0"){
                        item.setImageResource(R.drawable.king_black)
                    }
                }
            }
        }

        fun boardStatusStore(){
            boardDraw()
            button_forward.isEnabled=false
            if (sound_switch.isChecked) {
                mp_move.start()
            }
            var boardStatus = Array(64){i -> CheckerStatus(board1d[i].tag.toString(),
                board1d[i].contentDescription.toString(),board1d[i].scrollBarSize)}
            if (boardsStatus.size> boardStatusPointer+1){
                boardsStatus=boardsStatus.sliceArray(0..boardStatusPointer)
            }
            boardsStatus +=boardStatus
            boardStatusPointer=boardsStatus.size-1
        }
        boardStatusStore()


        fun computerTurn(){
            counter =0
            var cordinate: BestMove
          //  boardRandom.shuffle()

            for (item in row2){item.scrollBarSize=4}
            if (openining ==0){
                cordinate = BestMove(checker12,checker20,1,false)
                openining=1
            }else {
                    cordinate = bestMove()
            }

            if (cordinate.checkmate) {
                if (sound_switch.isChecked) {
                    mp_loos.start()
                }
                handler.postDelayed({
                    tempMove(cordinate.first,cordinate.last)
                    for (item in row5){item.scrollBarSize=4}

                    boardStatusStore()

                    for (item in board1d) { item.backgroundTintList = null}
                    turn = "white"

                    if (isChecked("white")) {
                        for (item in board1d) {
                            if ((item.contentDescription == "K" || item.contentDescription == "K0") &&
                                item.tag == "white"
                            ) {
                                item.backgroundTintList = ColorStateList.valueOf(Color.RED)
                            }
                        }
                    }
                    button_new.isEnabled=true
                    button_back.isEnabled=true
                    vsSwitch.isEnabled=true
                    radio_one.isEnabled=true
                    radio_two.isEnabled=true
                    radio_three.isEnabled=true
                    sound_switch.isEnabled=true
                }, 500)
                return
            }

            if (cordinate.first == checker00) {
                if (sound_switch.isChecked) {
                    mp_win.start()
                }
                textView.text = "Computer surrender!"
                for (item in board1d) {
                    item.backgroundTintList = null
                    item.isClickable = false
                }
                button_new.isEnabled=true
                button_back.isEnabled=true
                vsSwitch.isEnabled=true
                radio_one.isEnabled=true
                radio_two.isEnabled=true
                radio_three.isEnabled=true
                sound_switch.isEnabled=true
                return
            }

            for (item in board1d) {
                item.backgroundTintList = null
            }

            cordinate.last.backgroundTintList=ColorStateList.valueOf(Color.GRAY)
            cordinate.first.backgroundTintList=ColorStateList.valueOf(Color.GRAY)
            if (cordinate.last.tag=="white"){
                cordinate.last.backgroundTintList=ColorStateList.valueOf(Color.RED)
            }

            handler.postDelayed({
                tempMove(cordinate.first,cordinate.last)
                for (item in row5){item.scrollBarSize=4}

                boardStatusStore()

                for (item in board1d) { item.backgroundTintList = null}
                textView.text = "white turn"
                turn = "white"

                if (isChecked("white")) {
                    for (item in board1d) {
                        if ((item.contentDescription == "K" || item.contentDescription == "K0") &&
                            item.tag == "white"
                        ) {
                            item.backgroundTintList = ColorStateList.valueOf(Color.RED)
                        }
                    }
                    textView.text = "white under check!!"
                }
                for (item in board1d){item.isClickable=true}

                if (isDraw("white")){
                    textView.text = "Draw!"
                    for (item in board1d){item.isClickable=false}
                }
                button_new.isEnabled=true
                button_back.isEnabled=true
                vsSwitch.isEnabled=true
                radio_one.isEnabled=true
                radio_two.isEnabled=true
                radio_three.isEnabled=true
                sound_switch.isEnabled=true
            }, 500)
            return
        }


        //action when checkers are clicked
        fun checkerClick(checker: ImageButton) {
            //button_new.text=checker.scrollBarSize.toString()
            var tempData :TempPiece
            var otherTurn =""
            if (turn == "white"){ otherTurn="black" }else{ otherTurn="white" }

            if (checker == tempChecker){
                textView.text = turn + " turn"
                handPice = noPice
                for (item in board1d){
                    if(item.backgroundTintList!=ColorStateList.valueOf(Color.RED)) {
                        item.backgroundTintList = null
                    }
                }
                tempChecker = checker00
                return
            }
            else if (handPice == noPice && checker.tag == turn || checker.tag!="" && tempChecker.tag==checker.tag){
                for (item in board1d){
                    if(item.backgroundTintList!=ColorStateList.valueOf(Color.RED)) {
                        item.backgroundTintList = null
                    }
                }
                handPice = checker.drawable.toBitmap()
                checker.backgroundTintList = ColorStateList.valueOf(Color.DKGRAY)

                tempChecker = checker

                for (item in possibleMoves(tempChecker)) {
                    item.backgroundTintList = ColorStateList.valueOf(Color.LTGRAY)
                }
                return
            }
            else if (handPice != noPice && checker in possibleMoves(tempChecker)){
                if (!vsSwitch.isChecked) {
                    button_back.isEnabled = true
                }
                tempData=tempMove(tempChecker,checker)
                handPice=noPice
                for (item in board1d){
                    if(item.backgroundTintList!=ColorStateList.valueOf(Color.RED)) {
                        item.backgroundTintList = null
                    }
                }

                //check if move is ligal
                if (isChecked(turn)){
                    if (sound_switch.isChecked) {
                        mp_error.start()
                    }
                    textView.text ="Illigal move!"
                    resetMove(tempChecker, checker,tempData)
                    backMove(tempChecker,checker,tempData)
                    tempChecker =checker00
                    for (item in board1d){
                        if ((item.contentDescription=="K" || item.contentDescription=="K0") &&
                                item.tag == turn){
                            item.backgroundTintList=ColorStateList.valueOf(Color.RED)
                        }
                    }
                    return
                }

                if (turn == "black"){
                    for (item in row5){item.scrollBarSize=4}
                }else{
                    for (item in row2){item.scrollBarSize=4}
                }

                boardStatusStore()

               // checks if oponent is checked
                if (isChecked(otherTurn)){
                    if (sound_switch.isChecked) {
                        mp_error.start()
                    }
                    for (item in board1d){item.backgroundTintList = null}
                    turn = otherTurn

                    //check if checkmate
                    if (isCheckmate(turn)){
                        if (sound_switch.isChecked) {
                            mp_win.start()
                        }
                        textView.text = "Chakemate!!"
                        for (item in board1d) {
                            if ((item.contentDescription == "K" || item.contentDescription == "K0") &&
                                item.tag == turn
                            ) {
                                item.backgroundTintList = ColorStateList.valueOf(Color.RED)
                            }
                        }
                        for (item in board1d){
                            item.isClickable = false
                        }
                        return
                    }

                    for (item in board1d){
                        if ((item.contentDescription == "K" || item.contentDescription == "K0") &&
                                item.tag == otherTurn){
                            item.backgroundTintList=ColorStateList.valueOf(Color.RED)
                        }
                    }
                    textView.text = otherTurn + " under check"

                    if (turn == "black" && vsSwitch.isChecked) {
                        for (item in board1d){item.isClickable=false}
                        textView.text="computer thinking"
                        button_new.isEnabled=false
                        button_back.isEnabled=false
                        vsSwitch.isEnabled=false
                        sound_switch.isEnabled=false
                        radio_one.isEnabled=false
                        radio_two.isEnabled=false
                        radio_three.isEnabled=false
                        handler.postDelayed({
                            computerTurn()
                        }, 50)
                    }
                    return
                }

                for (item in board1d){item.backgroundTintList = null}

                if (isDraw(otherTurn)){
                    if (sound_switch.isChecked) {
                        mp_draw.start()
                    }
                    textView.text = "Draw!"
                    turn=otherTurn
                    for (item in board1d){item.isClickable=false}
                    return
                }

                turn = otherTurn

                textView.text = turn + " turn"
                if (turn == "black" && vsSwitch.isChecked) {
                    for (item in board1d){item.isClickable=false}
                    textView.text="computer thinking"
                    button_new.isEnabled=false
                    button_back.isEnabled=false
                    vsSwitch.isEnabled=false
                    sound_switch.isEnabled=false
                    radio_one.isEnabled=false
                    radio_two.isEnabled=false
                    radio_three.isEnabled=false
                    handler.postDelayed({
                        computerTurn()
                    }, 50)
                }
                return
            }
            else if(handPice == noPice && checker.tag != turn){
                return
            }
            else if (handPice != noPice || checker.tag != ""){
                if (sound_switch.isChecked) {
                    mp_error.start()
                }
                textView.text = "Illigal move!"
            }
        }

        //initiate all checkers on click listeners
        for (checker in board1d){
            checker.setOnClickListener {checkerClick(checker)}
        }


        //Back Move
        button_back.setOnClickListener {
            var status=boardStatusPointer-1
            handPice=noPice
            if (status >-1){
                var i =0
                for (item in boardsStatus[status]){
                    board1d[i].tag = item.tag
                    board1d[i].contentDescription=item.contentDescription
                    board1d[i].scrollBarSize=item.scrollBarSize
                    board1d[i].backgroundTintList=null
                    i++
                }

                button_forward.isEnabled=true
                boardDraw()
                if (turn=="white"){turn = "black"}else{turn="white"}
                if (isChecked(turn)){
                    for (item in board1d){
                        if (item.tag==turn && (item.contentDescription=="K"||item.contentDescription=="K0")){
                            item.backgroundTintList=ColorStateList.valueOf(Color.RED)
                        }
                    }
                    textView.text= turn + " under check!"
                }else {
                    textView.text = turn + " turn"
                }
                boardStatusPointer -= 1
                if (boardStatusPointer ==0){
                    button_back.isEnabled=false
                }
                if (vsSwitch.isChecked && turn=="black"){
                    button_play.isEnabled=true
                    textView.text ="Press > for computer to move"
                }else{
                    button_play.isEnabled=false
                }
            }
            for (item in board1d){item.isClickable=true}
        }


        button_forward.setOnClickListener {
            var status=boardStatusPointer+1
            handPice=noPice
            if (status <boardsStatus.size){
                button_back.isEnabled=true
                var i =0
                for (item in boardsStatus[status]){
                    board1d[i].tag = item.tag
                    board1d[i].contentDescription=item.contentDescription
                    board1d[i].scrollBarSize=item.scrollBarSize
                    board1d[i].backgroundTintList=null
                    i++
                }

                boardDraw()

                if (turn=="white"){turn = "black"}else{turn="white"}
                if (isChecked(turn)){
                    for (item in board1d){
                        if (item.tag==turn && (item.contentDescription=="K"||item.contentDescription=="K0")){
                            item.backgroundTintList=ColorStateList.valueOf(Color.RED)
                        }
                    }
                    textView.text= turn + " under check!"
                    if (isCheckmate(turn)){
                        for(item in board1d){item.isClickable=false}
                        if (turn=="white"&&vsSwitch.isChecked){
                            textView.text="Computer Checkmate!!"
                        }else{
                            textView.text="Checkmate!!"
                        }
                    }
                }else {
                    textView.text = turn + " turn"
                }
                boardStatusPointer += 1
                if (boardStatusPointer +1==boardsStatus.size){
                    button_forward.isEnabled=false
                }
                if (vsSwitch.isChecked && turn=="black"){
                    button_play.isEnabled=true
                    textView.text ="Press > for computer to move"
                }else{
                    button_play.isEnabled=false
                }
            }
        }


        button_play.setOnClickListener {
            if (vsSwitch.isChecked && turn=="black"){
                textView.text="computer thinking"
                button_play.isEnabled=false
                button_new.isEnabled=false
                button_back.isEnabled=false
                button_forward.isEnabled=false
                vsSwitch.isEnabled=false
                sound_switch.isEnabled=false
                radio_one.isEnabled=false
                radio_two.isEnabled=false
                radio_three.isEnabled=false
                handler.postDelayed({
                    computerTurn()
                }, 50)
            }
        }


        vsSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (vsSwitch.isChecked) {
                radio_one.isEnabled=true
                radio_two.isEnabled=true
                radio_three.isEnabled=true
                if (turn=="black"){
                    button_play.isEnabled=true
                    textView.text ="Press > for computer to move"
                }

            }else{
                button_play.isEnabled=false
                textView.text =turn +" turn"
                radio_one.isEnabled=false
                radio_two.isEnabled=false
                radio_three.isEnabled=false
            }
        }


        radioGroup.setOnCheckedChangeListener { _, _ ->
            if (radio_one.isChecked){
                compLvl=1
            }else if (radio_two.isChecked){
                compLvl=2
            }else{
                compLvl=3
            }
        }

        //Start new game
        button_new.setOnClickListener {
            val i = baseContext.packageManager
                .getLaunchIntentForPackage(baseContext.packageName)
            i!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(i)
        }
    }
}
