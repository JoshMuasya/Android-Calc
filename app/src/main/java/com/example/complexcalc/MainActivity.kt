package com.example.complexcalc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Display
import android.view.View
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    lateinit var working:TextView
    lateinit var results:TextView

    private var addOperation = false
    private var addDecimal = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        working = findViewById(R.id.workingDisplay)
        results = findViewById(R.id.resultsDisplay)
    }

    fun numberAction(view: View)
    {
        if(view is Button)
        {
            if(view.text == ".")
            {
                if(addDecimal)
                    working.append(view.text)

                addDecimal = false
            }
            else
                working.append(view.text)

            addOperation = true
        }
    }

    fun operationAction(view: View)
    {
        if(view is Button && addOperation)
        {
            working.append(view.text)
            addOperation = false
            addDecimal = true
        }
    }

    fun clearAllAction (view: View)
    {
        working.text=""
        results.text=""
    }

    fun backSpace (view: View)
    {
        val length =working.length()
        if (length > 0)
            working.text = working.text.subSequence(0, length - 1)
    }

    fun equalsAction (view: View)
    {
        results.text = calcResults()
    }

    private fun calcResults(): String
    {
        val numberOperation = numberOperator()
        if(numberOperation.isEmpty()) return  ""

        val divideMultiplyOperation = divideMultiCalc(numberOperation)
        if(divideMultiplyOperation.isEmpty()) return ""

        val results = additionSubtractionCalc(divideMultiplyOperation)
        return results.toString()
    }

    private fun additionSubtractionCalc(passedList: MutableList<Any>): Float
    {
        var results = passedList[0] as Float

        for(i in passedList.indices)
        {
            if(passedList[i] is Char && i != passedList.lastIndex)
            {
                val operator = passedList[i]
                val nextNumber =passedList[i + 1] as Float

                if(operator == '+')
                    results += nextNumber
                if(operator == '-')
                    results -= nextNumber
            }
        }

        return results

    }

    private fun divideMultiCalc(passedList: MutableList<Any>): MutableList<Any>
    {
        var list = passedList

        while (list.contains('X') || list.contains('/'))
        {
            list = multDivide(list)
        }
        return list
    }

    private fun multDivide(passedList: MutableList<Any>): MutableList<Any>
    {
        val anotherList = mutableListOf<Any>()
        var newIndex = passedList.size

        for(i in passedList.indices)
        {
            if (passedList[i] is Char && i != passedList.lastIndex && i < newIndex) {
                val operator = passedList[i]
                val nextNumber = passedList[i + 1] as Float
                val prevNumber = passedList[i - 1] as Float

                when (operator) {
                    'x' -> {
                        anotherList.add(prevNumber * nextNumber)
                        newIndex = i + 1
                    }

                    '/' -> {
                        anotherList.add(prevNumber * nextNumber)
                        newIndex = i + 1
                    }

                    else -> {
                        anotherList.add(prevNumber)
                        anotherList.add(operator)
                    }
                }
            }
            if(i > newIndex)
                anotherList.add(passedList[i])
        }
        return anotherList
    }

    private fun numberOperator():MutableList<Any>
    {
        val list = mutableListOf<Any>()
        var currentNumber = ""

        for (number in working.text)
        {
            if(number.isDigit() || number == '.')
                currentNumber += number
            else
            {
                list.add(currentNumber.toFloat())
                currentNumber = ""
                list.add(number)
            }
        }

        if(currentNumber != "")
            list.add(currentNumber.toFloat())

        return list

    }


}