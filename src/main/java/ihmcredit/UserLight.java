package ihmcredit;

import java.io.Serializable;
import java.util.Date;

import com.meimid.core.model.Users;

public class UserLight implements Serializable {
	String userLogin;
	String password;
	String code;
	String value;	
	long stamp;
	int expire;
	String name;
	Boolean enabled;
	Boolean tempUser;
	Date expiryDate;
	String message;
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
	public int getExpire() {
		return expire;
	}
	public void setExpire(int expire) {
		this.expire = expire;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
	
	

	public Boolean getEnabled() {
		return enabled;
	}
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
	public Boolean getTempUser() {
		return tempUser;
	}
	public void setTempUser(Boolean tempUser) {
		this.tempUser = tempUser;
	}
	public Date getExpiryDate() {
		return expiryDate;
	}
	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	
	
	
	
	

}
