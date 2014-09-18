package com.wb.sc.db;

import net.tsz.afinal.FinalDb.DbUpdateListener;
import android.database.sqlite.SQLiteDatabase;

/**
 * 数据库版本更新处理类，主要负责对数据库表进行修改
 * 注：修改时只能通过SQL语句执行，即db.execSQL()
 * @author liangbx
 *
 */
public class DbUpdateHandler implements DbUpdateListener {

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}

}
