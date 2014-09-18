<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent" 
    android:background="#f8f8f8">
    
	<#if title != "null">
	<include layout="@layout/${TitleLayout}"/>
	</#if> 

    <LinearLayout
        android:id="@+id/loadLayout"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_centerInParent="true"
        android:orientation="vertical" >

        <ProgressBar
            android:id="@+id/loadingContentBar"
            style="?android:attr/progressBarStyleLarge"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content" />
    </LinearLayout>

    <LinearLayout
        android:id="@+id/contentLayout"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:orientation="vertical" >
        
        <!-- 
        	Please add XListViewLib project
        	修改样式请关注：
			android:divider
			android:dividerHeight			
			android:listSelector
			android:drawSelectorOnTop
        -->
        <com.common.widget.XListView
            android:id="@+id/contentXListview"
            android:layout_width="match_parent"
            android:layout_height="wrap_content" />
        
    </LinearLayout>

</RelativeLayout>