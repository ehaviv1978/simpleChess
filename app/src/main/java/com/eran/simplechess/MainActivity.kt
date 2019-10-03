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
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Handler
import kotlinx.coroutines.*
import android.widget.CompoundButton



class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        data class Cordinate(val row: Int, val column: Int)
        data class BestMove(var first: ImageButton, var last: ImageButton, var score: Int, var checkmate: Boolean)
        data class CheckerPar(var tag: String, var contentDescription: String, var scrollBarSize: Int, var image: Bitmap)
        data class BackPar(var index: Int, var tag: String, var contentDescription: String, var scrollBarSize: Int, var image: Bitmap)
        data class TempPiece (var tag:String,var contentDescription: String,var scrollBarSize: Int,var K0:Boolean)
        val handler =Handler()

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
        var totalScore=0
        //  val mp_error = MediaPlayer.create(this, R.raw.error)
        //  val mp_move = MediaPlayer.create(this, R.raw.click)
        var n = 0
        var counter =0
        var back1=BackPar(99,"","",4,checker30.drawable.toBitmap())
        var back2=BackPar(99,"","",4,checker30.drawable.toBitmap())
        var back3=BackPar(99,"","",4,checker30.drawable.toBitmap())
        var back4=BackPar(99,"","",4,checker30.drawable.toBitmap())

        button_back.isEnabled=false
        vsSwitch.isChecked= true
        sound_switch.isEnabled=false

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
        var column0= column(0)
        var column1= column(1)
        var column2= column(2)
        var column3= column(3)
        var column4= column(4)
        var column5= column(5)
        var column6= column(6)
        var column7= column(7)


        //return the board x,y cordinate of a single checker
        fun cordinate (item: ImageButton):Cordinate{
            if (item in row0){
                return Cordinate(0,row0.indexOf(item))
            }
            else if (item in row1){
                return Cordinate(1,row1.indexOf(item))
            }
            else if (item in row2){
                return Cordinate(2,row2.indexOf(item))
            }
            else if (item in row3){
                return Cordinate(3,row3.indexOf(item))
            }
            else if (item in row4){
                return Cordinate(4,row4.indexOf(item))
            }
            else if (item in row5){
                return Cordinate(5,row5.indexOf(item))
            }
            else if (item in row6) {
                return Cordinate(6, row6.indexOf(item))
            }
            return Cordinate(7,row7.indexOf(item))
        }

        fun specialMove(oldChecker:ImageButton,checker:ImageButton){
            if (checker.contentDescription=="p"){
                if (checker in row3 && oldChecker in row1){
                    row2[row3.indexOf(checker)].scrollBarSize=2
                }
                else if(checker in row4 && oldChecker in row6){
                    row5[row4.indexOf(checker)].scrollBarSize=5
                }
                else if (checker.scrollBarSize==2){
                    row3[row2.indexOf(checker)].tag=""
                    row3[row2.indexOf(checker)].contentDescription=""
                    row3[row2.indexOf(checker)].setImageResource(R.drawable.empty)
                }
                else if (checker.scrollBarSize==5){
                    row4[row5.indexOf(checker)].tag=""
                    row4[row5.indexOf(checker)].contentDescription=""
                    row4[row5.indexOf(checker)].setImageResource(R.drawable.empty)
                }
                else if (checker in row0 || checker in row7){
                    checker.contentDescription = "q"
                    checker.scrollBarSize = 1
                }
            }
            else if (checker.contentDescription =="K0") {
                checker.contentDescription="K"
                if (checker in column6){
                    board1d[board1d.indexOf(checker)-1].tag=board1d[board1d.indexOf(checker)+1].tag
                    board1d[board1d.indexOf(checker)-1].contentDescription="r"
                    if(board1d[board1d.indexOf(checker)-1].drawable.toBitmap()==checker00.drawable.toBitmap()) {
                        board1d[board1d.indexOf(checker) - 1].setImageBitmap(board1d[board1d.indexOf(checker) + 1].drawable.toBitmap())
                    }
                    board1d[board1d.indexOf(checker)+1].tag=""
                    board1d[board1d.indexOf(checker)+1].contentDescription=""
                    board1d[board1d.indexOf(checker)+1].setImageResource(R.drawable.empty)
                }
                else if (checker in column2){
                    board1d[board1d.indexOf(checker)+1].tag=board1d[board1d.indexOf(checker)-2].tag
                    board1d[board1d.indexOf(checker)+1].contentDescription="r"
                    if(board1d[board1d.indexOf(checker)+1].drawable.toBitmap()==checker00.drawable.toBitmap()) {
                        board1d[board1d.indexOf(checker) + 1].setImageBitmap(board1d[board1d.indexOf(checker) - 2].drawable.toBitmap())
                    }
                    board1d[board1d.indexOf(checker)-2].tag=""
                    board1d[board1d.indexOf(checker)-2].contentDescription=""
                    board1d[board1d.indexOf(checker)-2].setImageResource(R.drawable.empty)
                }
            }
            //else if (checker.contentDescription =="r0"){checker.contentDescription="r"}
        }

        // checks if a move is valid and return true or false
        fun isValidMove(oldChecker:ImageButton, newChecker:ImageButton):Boolean{
            var diff = board1d.indexOf(newChecker) - board1d.indexOf(oldChecker)
            val rowOld = cordinate(oldChecker).row
            val columnOld = cordinate(oldChecker).column
            val rowNew = cordinate(newChecker).row
            val columnNew = cordinate(newChecker).column
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
                    board1d[board1d.indexOf(oldChecker)+3].contentDescription == "r0" &&
                    board1d[board1d.indexOf(oldChecker)+2].contentDescription == "" &&
                    !isThretened(board1d[board1d.indexOf(oldChecker)+2],opositTurn) &&
                    board1d[board1d.indexOf(oldChecker)+1].contentDescription == "" &&
                    !isThretened(board1d[board1d.indexOf(oldChecker)+1],opositTurn)){
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
                    board1d[board1d.indexOf(oldChecker)-4].contentDescription == "r0" &&
                    board1d[board1d.indexOf(oldChecker)-3].contentDescription == "" &&
                    !isThretened(board1d[board1d.indexOf(oldChecker)-3],opositTurn) &&
                    board1d[board1d.indexOf(oldChecker)-2].contentDescription == "" &&
                    !isThretened(board1d[board1d.indexOf(oldChecker)-2],opositTurn) &&
                    board1d[board1d.indexOf(oldChecker)-1].contentDescription == "" &&
                    !isThretened(board1d[board1d.indexOf(oldChecker)-1],opositTurn)){
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
                            if (board2d[cordinate(oldChecker).row + (rowDiff - n)][cordinate(oldChecker).column + (columnDiff - n)].tag != "") {
                                return false
                            }
                    } else if (rowDiff < 0 && columnDiff < 0) {
                            if (board2d[cordinate(oldChecker).row + (rowDiff + n)][cordinate(oldChecker).column + (columnDiff + n)].tag != "") {
                                return false
                            }
                    } else if (rowDiff < 0 && columnDiff > 0) {
                            if (board2d[cordinate(oldChecker).row + (rowDiff + n)][cordinate(oldChecker).column + (columnDiff - n)].tag != "") {
                                return false
                            }
                    } else if (rowDiff > 0 && columnDiff < 0) {
                            if (board2d[cordinate(oldChecker).row + (rowDiff - n)][cordinate(oldChecker).column + (columnDiff + n)].tag != "") {
                                return false
                            }
                    } else if (rowDiff > 0) {
                            if (board2d[cordinate(oldChecker).row + (rowDiff - n)][cordinate(oldChecker).column].tag != "") {
                                return false
                            }
                    } else if (rowDiff < 0) {
                            if (board2d[cordinate(oldChecker).row + (rowDiff + n)][cordinate(oldChecker).column].tag != "") {
                                return false
                            }
                    } else if (columnDiff > 0) {
                            if (board2d[cordinate(oldChecker).row][cordinate(oldChecker).column + (columnDiff - n)].tag != "") {
                                return false
                            }
                    } else if (columnDiff < 0) {
                            if (board2d[cordinate(oldChecker).row][cordinate(oldChecker).column + (columnDiff + n)].tag != "") {
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
            val rowOld = cordinate(checker).row
            val columnOld = cordinate(checker).column
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
                    board1d[board1d.indexOf(checker)+3].contentDescription == "r0" &&
                    board1d[board1d.indexOf(checker)+2].contentDescription == "" &&
                    !isThretened(board1d[board1d.indexOf(checker)+2],opositeColor) &&
                    board1d[board1d.indexOf(checker)+1].contentDescription == "" &&
                    !isThretened(board1d[board1d.indexOf(checker)+1],opositeColor)) {
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
                    board1d[board1d.indexOf(checker)-4].contentDescription == "r0" &&
                    board1d[board1d.indexOf(checker)-3].contentDescription == "" &&
                    !isThretened(board1d[board1d.indexOf(checker)-3],opositeColor) &&
                    board1d[board1d.indexOf(checker)-2].contentDescription == "" &&
                    !isThretened(board1d[board1d.indexOf(checker)-2],opositeColor) &&
                    board1d[board1d.indexOf(checker)-1].contentDescription == "" &&
                    !isThretened(board1d[board1d.indexOf(checker)-1],opositeColor)) {
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


        fun resetMove(item:ImageButton, item1:ImageButton){
            if (item1.contentDescription == "p" && item1 in row3 && item in row1){
                row2[row3.indexOf(item1)].scrollBarSize=4
            }
            else if (item1.contentDescription == "p" && item1 in row4 && item in row6){
                row5[row4.indexOf(item1)].scrollBarSize=4
            }
            else if(item1.scrollBarSize==1&&item1.contentDescription=="q"){
                item1.scrollBarSize=4
                item1.contentDescription="p"
            }

            else if (item1.scrollBarSize == 2 && item.contentDescription=="p"&& item.tag=="white") {
                board1d[board1d.indexOf(item1) + 8].tag = "black"
                board1d[board1d.indexOf(item1) + 8].contentDescription = "p"
                board1d[board1d.indexOf(item1) + 8].setImageResource(R.drawable.pawn_black)
            }
            else if (item1.scrollBarSize == 5 && item.contentDescription=="p"&& item.tag=="black") {
                board1d[board1d.indexOf(item1) - 8].tag = "white"
                board1d[board1d.indexOf(item1) - 8].contentDescription = "p"
                board1d[board1d.indexOf(item1) - 8].setImageResource(R.drawable.pawn_white)
            }
            else if (item1.contentDescription == "K" && board1d.indexOf(item1) == 62 &&
                board1d.indexOf(item) == 60){
                board1d[63].tag = "white"
                board1d[63].contentDescription = "r0"
                board1d[63].setImageResource(R.drawable.rock_white)
                board1d[61].tag = ""
                board1d[61].contentDescription = ""
                if (board1d[61].drawable.toBitmap()==board1d[63].drawable.toBitmap()){
                    board1d[61].setImageResource(R.drawable.empty)
                }
            } else if (item1.contentDescription == "K" && board1d.indexOf(item1) == 58 &&
                board1d.indexOf(item) == 60) {
                board1d[56].tag = "white"
                board1d[56].contentDescription = "r0"
                board1d[56].setImageResource(R.drawable.rock_white)
                board1d[59].tag = ""
                board1d[59].contentDescription = ""
                if (board1d[59].drawable.toBitmap()==board1d[56].drawable.toBitmap()){
                    board1d[59].setImageResource(R.drawable.empty)
                }
            }
            else if (item1.contentDescription == "K" && board1d.indexOf(item1) == 6 &&
                board1d.indexOf(item) == 4) {
                board1d[7].tag = "black"
                board1d[7].contentDescription = "r0"
                board1d[7].setImageResource(R.drawable.rock_black)
                board1d[5].tag = ""
                board1d[5].contentDescription = ""
                if (board1d[5].drawable.toBitmap()==board1d[7].drawable.toBitmap()){
                    board1d[5].setImageResource(R.drawable.empty)
                }
            } else if (item1.contentDescription == "K" && board1d.indexOf(item1) == 2 &&
                board1d.indexOf(item) == 4) {
                board1d[0].tag = "black"
                board1d[0].contentDescription = "r0"
                board1d[0].setImageResource(R.drawable.rock_black)
                board1d[3].tag = ""
                board1d[3].contentDescription = ""
                if (board1d[3].drawable.toBitmap()==board1d[0].drawable.toBitmap()){
                    board1d[3].setImageResource(R.drawable.empty)
                }
            }
        }

        fun tempMove (item: ImageButton,item1:ImageButton):TempPiece{
            var tempPiece=TempPiece("","",4,false)
            if (item.contentDescription=="K0"){tempPiece.K0=true}
            tempPiece.tag = item1.tag.toString()
            tempPiece.contentDescription = item1.contentDescription.toString()
            tempPiece.scrollBarSize=item1.scrollBarSize
            item1.tag = item.tag
            item1.contentDescription = item.contentDescription
            item1.scrollBarSize=item.scrollBarSize
            item.tag = ""
            item.contentDescription = ""
            item.scrollBarSize=4
            return tempPiece
        }

        fun backMove (item: ImageButton,item1:ImageButton,tempPiece:TempPiece){
            if (tempPiece.K0){item1.contentDescription="K0"}
            item.tag = item1.tag
            item.contentDescription = item1.contentDescription
            item.scrollBarSize=item1.scrollBarSize
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

                        specialMove(item,item1)

                        //check if move is ligal
                        if (!isChecked(color)){
                            resetMove(item, item1)
                            backMove(item,item1,tempPiece)
                            return false
                        }

                        resetMove(item, item1)
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

                        specialMove(item,item1)

                        if (!isChecked(color)) {
                            resetMove(item, item1)
                            backMove(item,item1,tempPiece)
                            return false
                        }
                        resetMove(item, item1)
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

            for (item1 in board1d) {
                if (item1.tag == color) {
                    for (item2 in possibleMoves(item1)) {

                        var tempPiece = tempMove(item1,item2)
                        specialMove(item1,item2)

                        if (isChecked(color)){
                            resetMove(item1, item2)
                            backMove(item1,item2,tempPiece)
                            continue
                        }

                        if (depth>1) {
                            if (color == "white") {
                                min = moveScore("black", min, depth - 1)
                            } else {
                                max = moveScore("white", max, depth - 1)
                            }
                        }else{
                            if (color == "white") {
                                if (min>boardScore()){min=boardScore()}
                            } else {
                               if (max<boardScore()){max=boardScore()}
                            }
                        }

                        resetMove(item1, item2)
                        backMove(item1,item2,tempPiece)

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


        radioGroup.setOnCheckedChangeListener { _, _ ->
            if (radio_one.isChecked){
                compLvl=1
            }else if (radio_two.isChecked){
                compLvl=2
            }else{
                compLvl=3
            }
        }

        fun bestMove(): BestMove{
            var minMax = 99999
            var maxMin = -999999
            var first = checker00
            var last = checker00

            for (item1 in boardRandom) {
                if (item1.tag == "black") {
                    for (item2 in possibleMoves(item1)) {

                        var tempPiece = tempMove(item1,item2)
                        specialMove(item1,item2)

                        if (isChecked("white")&&!isChecked("black")) {
                            if (isCheckmate("white")) {
                                first = item1
                                last = item2
                                textView.text = "Computer Checkmate!"
                                for (item in board1d) { item.isClickable = false }
                                return BestMove(first, last, maxMin, true)
                            }
                        }
                        if (isChecked("black")){// || (isDraw("white")&& boardScore()>1000)){
                            resetMove(item1, item2)
                            backMove(item1,item2,tempPiece)
                            continue
                        }

                        minMax = moveScore("white", maxMin, compLvl)

                        if (maxMin < minMax) {
                            maxMin = minMax
                            first = item1
                            last = item2
                        }

                        resetMove(item1, item2)
                        backMove(item1,item2,tempPiece)
                    }
                }
            }
            button_back.text=maxMin.toString()
            if (first.contentDescription=="r0"){first.contentDescription="r"}
            return BestMove(first,last,maxMin,false)
        }


    /**    fun bestMove(): BestMove{
            var minMax = 10000
            var maxMin = -99999
            var max = -10000
            var min = 10000
            var first = checker00
            var last = checker00

            for (item1 in boardRandom) {
                if (item1.tag == "black") {
                    for (item2 in possibleMoves(item1)) {
                        var tempPiece = tempMove(item1,item2)

                        specialMove(item1,item2)

                        if (isChecked("white")&&!isChecked("black")) {
                            if (isCheckmate("white")) {
                                first = item1
                                last = item2
                                textView.text = "Computer Checkmate!"
                                for (item in board1d) { item.isClickable = false }
                                return BestMove(first, last, maxMin, true)
                            }
                        }

                        if (isChecked("black")){// || (isDraw("white")&& boardScore()>1000)){
                            resetMove(item1, item2)
                            backMove(item1,item2,tempPiece)
                            continue
                        }

                        minMax = 99999
                        for (item1 in board1d) {
                            if (item1.tag == "white") {
                                for (item2 in possibleMoves(item1)){
                                    var tempPiece = tempMove(item1,item2)

                                    specialMove(item1,item2)

                                    if (isChecked("white")){
                                        resetMove(item1, item2)
                                        backMove(item1,item2,tempPiece)
                                        continue
                                    }

                                    max = -90000
                                    for (item1 in board1d) {
                                        if (item1.tag == "black") {
                                            for (item2 in possibleMoves(item1)){
                                                var tempPiece = tempMove(item1,item2)

                                                specialMove(item1,item2)

                                                if (isChecked("black")){
                                                    resetMove(item1, item2)
                                                    backMove(item1,item2,tempPiece)
                                                    continue
                                                }

                                                /**  min = 99999
                                                for (item2 in board1d) {
                                                if (item2.tag == "white") {
                                                for (item3 in possibleMoves(item2)) {
                                                var tempPiece = tempMove(item2,item3)

                                                specialMove(item2,item3)

                                                if (isChecked("white")){
                                                resetMove(item2, item3)
                                                backMove(item2,item3,tempPiece)
                                                continue
                                                }

                                                if (min > boardScore()) { min = boardScore()}

                                                resetMove(item2, item3)
                                                backMove(item2,item3,tempPiece)

                                                if (min <= max) { break}
                                                }
                                                }
                                                if (min <= max) { break}
                                                }
                                                if (max < min){ max = min}*/

                                                if(max<boardScore()){max=boardScore()}

                                                resetMove(item1, item2)
                                                backMove(item1,item2,tempPiece)

                                                if (max >= minMax){break}
                                            }
                                        }
                                        if (max >= minMax){break}
                                    }

                                    if (minMax > max) { minMax = max }

                                    resetMove(item1, item2)
                                    backMove(item1,item2,tempPiece)

                                    if (minMax <= maxMin) {break }
                                }
                            }
                            if (minMax <= maxMin) { break }
                        }

                        if (maxMin < minMax) {
                            maxMin = minMax
                            first = item1
                            last = item2
                        }

                        resetMove(item1, item2)
                        backMove(item1,item2,tempPiece)
                    }
                }
            }
            if (first.contentDescription=="r0"){first.contentDescription="r"}
            return BestMove(first,last,maxMin,false)
        }*/


        fun backStore(store:BackPar,item: ImageButton){
            store.index=board1d.indexOf(item)
            store.tag=item.tag.toString()
            store.contentDescription=item.contentDescription.toString()
            store.image=item.drawable.toBitmap()
            store.scrollBarSize=item.scrollBarSize
    }

        fun computerTurn(){
            var cordinate: BestMove
            counter =0
            boardRandom.shuffle()

            for (item in row2){item.scrollBarSize=4}
            if (openining ==0){
                cordinate = BestMove(checker12,checker20,1,false)
                openining=1
            }else {
                    cordinate = bestMove()
            }

            if (cordinate.checkmate) {
                for (item in board1d) {
                    item.backgroundTintList = null
                    cordinate.last.backgroundTintList = ColorStateList.valueOf(Color.RED)
                    if (item.tag == "white" && (item.contentDescription == "K" || item.contentDescription == "K0")) {
                        item.backgroundTintList = ColorStateList.valueOf(Color.RED)
                        button_new.isEnabled = true
                        //   button_back.isEnabled=true
                        vsSwitch.isEnabled = true
                        //    sound_switch.isEnabled=true
                        if (cordinate.last.contentDescription == "q" && cordinate.last.scrollBarSize == 1) {
                            cordinate.last.setImageResource(R.drawable.queen_black)
                            cordinate.first.setImageResource(R.drawable.empty)
                        } else {
                            cordinate.last.setImageBitmap(cordinate.first.drawable.toBitmap())
                            cordinate.first.setImageResource(R.drawable.empty)
                        }
                    }
                }
                return
            }

            if (cordinate.first == checker00) {
                textView.text = "Draw1!"
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
                //    sound_switch.isEnabled=true
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

            backStore(back3,cordinate.first)
            backStore(back4,cordinate.last)

            handler.postDelayed({
                cordinate.last.tag = cordinate.first.tag
                cordinate.last.contentDescription = cordinate.first.contentDescription
                cordinate.last.setImageBitmap(cordinate.first.drawable.toBitmap())

                cordinate.first.tag = ""
                cordinate.first.contentDescription = ""
                cordinate.first.setImageResource(R.drawable.empty)

                specialMove(cordinate.first,cordinate.last)

                if (cordinate.last.scrollBarSize==1&&cordinate.last.contentDescription=="q") {
                    cordinate.last.setImageResource(R.drawable.queen_black)
                    cordinate.last.scrollBarSize=4
                    //for (item in row6){item.scrollBarSize=4}
                    //for (item in row7){item.scrollBarSize=4}
                }

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
            //    sound_switch.isEnabled=true
            }, 500)
            return
        }


        //action when checkers are clicked
        fun checkerClick(checker: ImageButton) {
           // button_back.text=boardScore().toString()
            for (item in board1d) {
                if (item.tag != "" && item.drawable.toBitmap() == checker00.drawable.toBitmap()){
                    item.backgroundTintList = ColorStateList.valueOf(Color.LTGRAY)
                }
            }
            var tempData = CheckerPar("","",4,noPice)
            var otherTurn =""
            if (turn == "white"){ otherTurn="black" }else{ otherTurn="white" }

            if (checker == tempChecker){
             //   mp_move.stop()
             //   mp_move.prepare()
             //   mp_move.start()

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
            else if (handPice == noPice && checker.tag == turn || tempChecker.tag==checker.tag){
              //  mp_move.stop()
              //  mp_move.prepare()
              //  mp_move.start()

                for (item in board1d){
                    if(item.backgroundTintList!=ColorStateList.valueOf(Color.RED)) {
                        item.backgroundTintList = null
                    }
                }
                handPice = checker.drawable.toBitmap()
                checker.backgroundTintList = ColorStateList.valueOf(Color.DKGRAY)

                tempChecker = checker
                button_back.isEnabled=false
                backStore(back1,checker)

                for (item in possibleMoves(tempChecker)) {
                    item.backgroundTintList = ColorStateList.valueOf(Color.LTGRAY)
                }
                return
            }
            else if (handPice != noPice && checker in possibleMoves(tempChecker)){
                backStore(back2,checker)

                if (!vsSwitch.isChecked) {
                    button_back.isEnabled = true
                }
                tempData.image = checker.drawable.toBitmap()
                tempData.tag = checker.tag.toString()
                tempData.contentDescription=checker.contentDescription.toString()
                checker.setImageBitmap(handPice)
                checker.tag = turn
                checker.contentDescription = tempChecker.contentDescription
                tempChecker.setImageResource(R.drawable.empty)
                tempChecker.tag = ""
                tempChecker.contentDescription = ""
                handPice=noPice
                for (item in board1d){
                    if(item.backgroundTintList!=ColorStateList.valueOf(Color.RED)) {
                        item.backgroundTintList = null
                    }
                }
              //  mp_move.stop()
              //  mp_move.prepare()
              //  mp_move.start()

                //check if move is ligal
                if (isChecked(turn)){
                  //  mp_move.stop()
                  //  mp_move.prepare()
                  //  mp_error.start()
                    textView.text ="Illigal move!"
                    tempChecker.setImageBitmap(checker.drawable.toBitmap())
                    tempChecker.tag= checker.tag
                    tempChecker.contentDescription = checker.contentDescription
                    checker.setImageBitmap(tempData.image)
                    checker.tag = tempData.tag
                    checker.contentDescription = tempData.contentDescription
                    tempData.image =noPice
                    tempData.tag =""
                    tempData.contentDescription=""
                    tempChecker =checker00
                    for (item in board1d){
                        if ((item.contentDescription=="K" || item.contentDescription=="K0") &&
                                item.tag == turn){
                            item.backgroundTintList=ColorStateList.valueOf(Color.RED)
                        }
                    }
                    return
                }
                if (checker.contentDescription=="r0"){checker.contentDescription="r"}

                if (turn == "white"){
                    for (item in row5){item.scrollBarSize=4}
                }else{
                    for (item in row2){item.scrollBarSize=4}
                }

                specialMove(tempChecker,checker)

                if (checker.scrollBarSize==1&&checker.contentDescription=="q") {
                    if (checker.tag == "white") {
                        checker.setImageResource(R.drawable.queen_white)
                        for (item in row0){item.scrollBarSize=4}
                        for (item in row1){item.scrollBarSize=4}
                    } else {
                        checker.setImageResource(R.drawable.queen_black)
                        for (item in row6){item.scrollBarSize=4}
                        for (item in row7){item.scrollBarSize=4}
                    }
                }
                checker.scrollBarSize=4
               // checks if oponent is checked
                if (isChecked(otherTurn)){
                 //   mp_move.stop()
                 //   mp_move.prepare()
                 //   mp_error.start()

                    for (item in board1d){item.backgroundTintList = null}
                    turn = otherTurn

                    //check if checkmate
                    if (isCheckmate(turn)){
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
                    textView.text = "Draw!"
                    for (item in board1d){item.isClickable=false}
                    return
                }

                turn = otherTurn

                textView.text = turn + " turn"
                if (turn == "black" && vsSwitch.isChecked) {
                    for (item in board1d){item.isClickable=false}
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
             //   mp_error.start()
                textView.text = "Illigal move!"
            }
        }

        //initiate all checkers on click listeners
        for (checker in board1d){
            checker.setOnClickListener {checkerClick(checker)}
        }

        fun backStore(item:ImageButton, store: BackPar ){
            item.tag = store.tag
            item.contentDescription=store.contentDescription
            item.scrollBarSize=store.scrollBarSize
            item.setImageBitmap(store.image)
        }

        //Back Move
        button_back.setOnClickListener {
            if (back1.index!=99&&back2.index!=99&&!vsSwitch.isChecked){
                if (turn=="white"){turn="black"}else{turn="white"}
                textView.text=turn +" turn"
                resetMove(board1d[back1.index], board1d[back2.index])

                backStore(board1d[back1.index],back1)
                backStore(board1d[back2.index],back2)

                if (back2.scrollBarSize==2){
                    board1d[back2.index+8].tag = "black"
                    board1d[back2.index+8].contentDescription="p"
                    board1d[back2.index+8].scrollBarSize=4
                    board1d[back2.index+8].setImageResource(R.drawable.pawn_black)
                }
                else if(back2.scrollBarSize==5){
                    board1d[back2.index-8].tag = "white"
                    board1d[back2.index-8].contentDescription="p"
                    board1d[back2.index-8].scrollBarSize=4
                    board1d[back2.index-8].setImageResource(R.drawable.pawn_white)
                }

                back1.index=99
                back2.index=99
                button_back.isEnabled=false
                tempChecker=checker00
            }

            if (back1.index!=99&&back2.index!=99&&vsSwitch.isChecked){
                resetMove(board1d[back3.index], board1d[back4.index])

                backStore(board1d[back3.index],back3)
                backStore(board1d[back4.index],back4)

                if(back4.scrollBarSize==5){
                    board1d[back4.index-8].tag = "white"
                    board1d[back4.index-8].contentDescription="p"
                    board1d[back4.index-8].scrollBarSize=4
                    board1d[back4.index-8].setImageResource(R.drawable.pawn_white)
                }

                resetMove(board1d[back1.index], board1d[back2.index])

                backStore(board1d[back1.index],back1)
                backStore(board1d[back2.index],back2)

                if (back2.scrollBarSize==2){
                    board1d[back2.index+8].tag = "black"
                    board1d[back2.index+8].contentDescription="p"
                    board1d[back2.index+8].scrollBarSize=4
                    board1d[back2.index+8].setImageResource(R.drawable.pawn_black)
                }

                back1.index=99
                back2.index=99
                back3.index=99
                back4.index=99
                button_back.isEnabled=false
                tempChecker=checker00
            }
        }


        vsSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (vsSwitch.isChecked) {
                radio_one.isEnabled=true
                radio_two.isEnabled=true
                radio_three.isEnabled=true

            }else{
                radio_one.isEnabled=false
                radio_two.isEnabled=false
                radio_three.isEnabled=false
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
