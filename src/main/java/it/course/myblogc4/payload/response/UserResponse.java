package it.course.myblogc4.payload.response;

import java.util.Set;

import it.course.myblogc4.entity.AuthorityName;
import it.course.myblogc4.entity.DbFile;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class UserResponse {
	
	private String username;
	private String email;
	private Set<AuthorityName> authorities;
	private DbFile avatar;

}
