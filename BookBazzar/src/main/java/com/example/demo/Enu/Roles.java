package com.example.demo.Enu;

import java.util.Set;

public enum Roles {
	ADMIN(Set.of(Permissions.WRITE,Permissions.READ,Permissions.UPDDATE,Permissions.DELETE)),
    USER(Set.of(Permissions.READ));
	
	public Set<Permissions> getPermissions() {
		return permissions;
	}

	private final Set<Permissions> permissions;

	private Roles(Set<Permissions> permissions) {
		this.permissions = permissions;
	}
	
	
	
}
