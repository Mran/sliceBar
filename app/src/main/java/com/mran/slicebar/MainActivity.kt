package com.mran.slicebar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.mran.slicebottombar.SliceBottomBar
import com.mran.slicebottombar.SliceItemView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var icons = listOf(R.drawable.home, R.drawable.inbox, R.drawable.mine)
        var name = listOf("主页", "盒子", "个人")
        for (i in 0 until icons.size) {
            findViewById<SliceBottomBar>(R.id.sliceBottomBar).addItem(icons[i], name[i], "#ffa3d553", 50f)
        }
        var itemSelect: SliceBottomBar.OnItemSelectListener = object : SliceBottomBar.OnItemSelectListener {
            override fun onSelected(index: Int) {
                Log.d("OnItemSelectListener", "${index}")

            }
        }
        findViewById<SliceBottomBar>(R.id.sliceBottomBar).onItemSelectListener = itemSelect

    }
}
