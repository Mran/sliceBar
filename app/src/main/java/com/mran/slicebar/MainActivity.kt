package com.mran.slicebar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.mran.slicebottombar.SliceItemView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.button).setOnClickListener {
            findViewById<SliceItemView>(R.id.myslice).move()

        }
        findViewById<Button>(R.id.button_cancle).setOnClickListener {
            findViewById<SliceItemView>(R.id.myslice).calcelMove()

        }
    }
}
