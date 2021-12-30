package com.caren.simpletodo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.FileUtils
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.simpletodo.TaskItemAdapter
import java.io.File
import java.io.IOException
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {


    //variable to hold list of tasks
    var listOfTasks = mutableListOf<String>()
    lateinit var adapter : TaskItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //how main activity file is connected to the xml display file
        setContentView(R.layout.activity_main)

        val onLongClickListener = object : TaskItemAdapter.OnLongClickListener{
            override fun onItemLongClicked(position: Int) {
                //remove item from list
                listOfTasks.removeAt(position)
                //notify adapter that data has changed
                adapter.notifyDataSetChanged()

                saveItems()
            }

        }

        // detect when the user clicks the add button
//        findViewById<Button>(R.id.button).setOnClickListener {
//        //code in here is going to be executed when user clicks on a button
//        }


        loadItems()

        // look up recycler view in the layout
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        // create adapter passing in the sample user data
        adapter = TaskItemAdapter(listOfTasks, onLongClickListener)
        //attach adapter to recycler view to populate with list of tasks
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        //set up the button and input field sp that the user can enter a task into the list of todo items
        //to be displayed in the recycler view field

        val inputTextField = findViewById<EditText>(R.id.addTaskField)

        findViewById<Button>(R.id.button).setOnClickListener {
            // grab the text from where the user inputted
            val userInputtedTask = inputTextField.text.toString()
            // add the string to our list of task
            listOfTasks.add(userInputtedTask)
            //notify the adapter that our data has been updated
            adapter.notifyItemInserted(listOfTasks.size - 1)
            //reset the text field to be empty
            inputTextField.setText("")
            saveItems()
        }
    }

    //save the data that the user has inputted by writing and reading from a file

    //create method to get data file we need
    fun getDataFile() : File {

        //every line will represent a specific task
        return File(filesDir, "data.txt")
    }
    //method to load items by reading every line in the file
    fun loadItems(){
        try{
            listOfTasks = org.apache.commons.io.FileUtils.readLines(getDataFile(), Charset.defaultCharset())
        }catch (ioException: IOException){
            ioException.printStackTrace()
        }
    }
    //method that saves items by writing them into our data file
    fun saveItems() {
        try {
            org.apache.commons.io.FileUtils.writeLines(getDataFile(), listOfTasks)
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }
    }
}
