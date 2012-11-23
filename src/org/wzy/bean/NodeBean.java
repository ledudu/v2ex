package org.wzy.bean;

public class NodeBean {
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getTitleAlternative() {
		return title_alternative;
	}
	
	public void setTitleAlternative(String title_alternative) {
		this.title_alternative = title_alternative;
	}
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getTopics() {
		return topics;
	}
	
	public void setTopics(String topics) {
		this.topics = topics;
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
	private String name;
	private String title;
	private String title_alternative;
	private String url;
	private String topics;
	private String avatar_mini;
	private String avatar_normal;
	private String avatar_large;
}
