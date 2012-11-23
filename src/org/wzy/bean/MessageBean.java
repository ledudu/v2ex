package org.wzy.bean;

import java.io.Serializable;

public class MessageBean {
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	public String getContentRendered() {
		return content_rendered;
	}
	
	public String getReplies() {
		return replies;
	}
	
	public void setReplies(String replies) {
		this.replies = replies;
	}
	
	public MemberBean getMember() {
		return member;
	}
	
	public void setMember(MemberBean member) {
		this.member = member;
	}
	
	public NodeBean getNode() {
		return node;
	}
	
	public void setNode(NodeBean node) {
		this.node = node;
	}
	
	public String getCreated() {
		return created;
	}
	
	public void setCreated(String created) {
		this.created = created;
	}
	
	public String getLastModified() {
		return last_modified;
	}
	
	public void setLastModified(String last_modified) {
		this.last_modified = last_modified;
	}
	
	public String getLastTouched() {
		return last_touched;
	}
	
	public void setLastTouched(String last_touched) {
		this.last_touched = last_touched;
	}
	
	private String id;
	private String title;
	private String url;
	private String content;
	private String content_rendered;
	private String replies;
	
	private MemberBean member;
	private NodeBean node;
	
	private String created;
	private String last_modified;
	private String last_touched;
}
