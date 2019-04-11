# sliceBar

Animate bottom bar

这个是原图的地址

https://uimovement.com/ui/7430/navigation-menu/ 


[![LICENSE](https://img.shields.io/badge/license-Anti%20996-blue.svg)](https://github.com/996icu/996.ICU/blob/master/LICENSE)[![HitCount](http://hits.dwyl.io/mran/sliceBar.svg)](http://hits.dwyl.io/mran/sliceBar)
    
先放效果图

![](https://github.com/Mran/sliceBar/blob/master/ezgif-1-b194a1e2259d.gif)
![](https://github.com/Mran/sliceBar/blob/master/ezgif-5-0a350aa1adfe.gif)

一个是原图,另一个是demo

# 使用方法
1. 导入项目
   ```
   	
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
    ```
    
    ```
    dependencies {
	        implementation 'com.github.Mran:sliceBar:0.2'
	}
    ```
本控件使用kotlin编写,如果你是纯java的项目,你还需要调整gradle的配置喔

2. 使用
在xml布局中加入

    ```
    
    <com.mran.slicebottombar.SliceBottomBar android:layout_width="match_parent"
                                            android:background="#423ac4"
                                            android:id="@+id/sliceBottomBar"
                                            app:layout_constraintBottom_toBottomOf="parent"
					    android:layout_height="wrap_content"
					    android:paddingTop="10dp"
                                            android:paddingLeft="10dp"
                                            app:itemHeight="45dp"
                                            app:itemRadius="10dp"
                                            app:itemBackgroundColor="#4ABFB5B5"
                                            tools:layout_editor_absoluteX="0dp">

    </com.mran.slicebottombar.SliceBottomBar>
    
    ```
    
在代码中(kotlin 代码作为演示)

 ```
        var icons = listOf(R.drawable.home, R.drawable.inbox, R.drawable.mine)
        var name = listOf("主页", "盒子", "个人")
        for (i in 0 until icons.size) {
            //向控件中加入单个item
	    //addItem(图标,名称)
            findViewById<SliceBottomBar>(R.id.sliceBottomBar).addItem(icons[i], name[i])
	    //设置item的颜色
	    findViewById<SliceBottomBar>(R.id.sliceBottomBar).setItemColor("#423ac4")
	    //设置字号
            findViewById<SliceBottomBar>(R.id.sliceBottomBar).setItemTextSize(30.0f)
	    //设置圆角的大小
            findViewById<SliceBottomBar>(R.id.sliceBottomBar).setRadiu(30.0f)
        }
        //加入选择监听
          var itemSelect: SliceBottomBar.OnItemSelectListener = object : SliceBottomBar.OnItemSelectListener {
            override fun onSelected(index: Int) {
                Log.d("OnItemSelectListener", "${index}")

            }
        }
        findViewById<SliceBottomBar>(R.id.sliceBottomBar).onItemSelectListener = itemSelect
```

