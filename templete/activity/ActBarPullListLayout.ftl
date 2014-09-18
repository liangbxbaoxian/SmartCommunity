<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent" >
    
    <com.handmark.pulltorefresh.library.PullToRefreshListView
        android:id="@+id/pull_refresh_list"
        android:layout_width="fill_parent"
        android:layout_height="fill_parent"
        android:cacheColorHint="#00000000"
        android:divider="#19000000"
        android:dividerHeight="4dp"
        android:fadingEdge="none"
        android:fastScrollEnabled="false"
        android:footerDividersEnabled="false"
        android:headerDividersEnabled="false"
        android:smoothScrollbar="true" />
    
</RelativeLayout>