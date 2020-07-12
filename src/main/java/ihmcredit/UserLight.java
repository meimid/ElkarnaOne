package ihmcredit;

import java.io.Serializable;

import com.meimid.core.model.Users;

public class UserLight implements Serializable {
	String userLogin;
	String password;
	String code;
	String value;	
	long stamp;
	public String getUserLogin() {
		return userLogin;
	}
	public void setUserLogin(String userLogin) {
		this.userLogin = userLogin;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	public void setUserLight(Users user){
		
		this.password=user.getPassword();
		this.userLogin=user.getUserLogin();
		this.code="ok";
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public long getStamp() {
		return stamp;
	}
	public void setStamp(long stamp) {
		this.stamp = stamp;
	}
	
	
	
	
	

}
