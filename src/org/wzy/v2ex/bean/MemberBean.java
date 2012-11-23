package org.wzy.v2ex.bean;

public class MemberBean {
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getUserName() {
		return username;
	}
	
	public void setUserName(String username) {
		this.username = username;
	}
	
	public String getTagLine() {
		return tagline;
	}
	
	public void setTagLine(String tagline) {
		this.tagline = tagline;
	}
	
	public String getAvatarMini() {
		return avatar_mini;
	}
	
	public void setAvatarMini(String avatar_mini) {
		this.avatar_mini = avatar_mini;
	}
	
	public String getAvatarNormal() {
		return avatar_normal;
	}
	
	public void setAvatarNormal(String avatar_normal) {
		this.avatar_normal = avatar_normal;
	}
	
	public String getAvatarLarge() {
		return avatar_large;
	}
	
	public void setAvatarLarge(String avatar_large) {
		this.avatar_large = avatar_large;
	}
	
	private String id;
	private String username;
	private String tagline;
	private String avatar_mini;
	private String avatar_normal;
	private String avatar_large;
}