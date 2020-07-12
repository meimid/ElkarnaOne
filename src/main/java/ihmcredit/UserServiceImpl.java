package ihmcredit;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserServiceImpl implements UserDetailsService {

	public UserDetails loadUserByUsername(final String username)
	        throws UsernameNotFoundException {

		final List<GrantedAuthority> AUTHORITIES = new ArrayList<GrantedAuthority>();

		if (username.equals("rod")) {

			AUTHORITIES.add(new SimpleGrantedAuthority("supervisor"));
			AUTHORITIES.add(new SimpleGrantedAuthority("user"));
			AUTHORITIES.add(new SimpleGrantedAuthority("teller"));
			return new User(
			        "rod",
			        "4efe081594ce25ee4efd9f7067f7f678a347bccf2de201f3adf2a3eb544850b465b4e51cdc3fcdde",
			        AUTHORITIES);

		} else if (username.equals("scott")) {

			AUTHORITIES.add(new SimpleGrantedAuthority("user"));
			return new User(
			        "scott",
			        "fb1f9e48058d30dc21c35ab4cf895e2a80f2f03fac549b51be637196dfb6b2b7276a89c65e38b7a1",
			        AUTHORITIES);
		} else {
			throw new UsernameNotFoundException("User not found: " + username);
		}

	}
}
