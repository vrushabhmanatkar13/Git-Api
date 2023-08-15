package com.payloads;

public class CreateRepoPOJO {
	
	private String name;
	private String description;
	private String homepage;
	
	private boolean has_issues;
	private boolean has_projects;
	private boolean has_wiki;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getHomepage() {
		return homepage;
	}
	public void setHomepage(String homepage) {
		this.homepage = homepage;
	}
	
	public boolean getHas_issues() {
		return has_issues;
	}
	public void setHas_issues(boolean has_issues) {
		this.has_issues = has_issues;
	}
	public boolean getHas_projects() {
		return has_projects;
	}
	public void setHas_projects(boolean has_projects) {
		this.has_projects = has_projects;
	}
	public boolean getHas_wiki() {
		return has_wiki;
	}
	public void setHas_wiki(boolean has_wiki) {
		this.has_wiki = has_wiki;
	}
	

}
