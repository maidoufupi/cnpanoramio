package com.cnpanoramio.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.appfuse.model.User;

@Entity
@Table(name = "circle")
public class Circle extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1997979468367592456L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
		
	@ManyToOne
	@JoinColumn(name="owner_id")
	private User owner;
	
	@OneToMany
	@JoinTable(name="circle_users")
	private Set<User> users = new HashSet<User>(0);
	
	@Column
	private String name;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}	
	
}
