package com.wb.sc.db;

import java.util.List;

import net.tsz.afinal.FinalDb;

import com.wb.sc.app.SCApp;
import com.wb.sc.bean.User;

public class DbHelper {
	
	/**
	 * 判断用户是否存在
	 * @param user
	 * @return
	 */
	public static boolean checkUserExits(User user) {
		FinalDb finalDb = SCApp.getInstance().getDb();
		List<User> userList = finalDb.findAll(User.class, "userId='" + user.userId + "'" );
		if(userList != null && userList.size() > 0)
			return true;
		return false;
	}
	
	/**
	 * 保存用户信息
	 * @param user
	 */
	public static void saveUser(User user) {
		FinalDb finalDb = SCApp.getInstance().getDb();
		if(checkUserExits(user)) {
			finalDb.update(user, "userId='" + user.userId + "'");
		} else {
			finalDb.save(user);
		}
	}
	
	/**
	 * 读取已登录的用户信息
	 */
	public static User getDbUser() {
		User user = null;
		FinalDb finalDb = SCApp.getInstance().getDb();
		List<User> userList = finalDb.findAllByWhere(User.class, "isLogin=1");
		if(userList != null && userList.size() > 0) {
			user = userList.get(0);			
		}
		return user;
	}
}
