package hello;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class User {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	private String name;
	private String email;
	private String password;
	private Integer remain;

	public boolean equals(User user) {
		if(		this.id.equals(user.getId())&&
				this.name.equals(user.getName())&&
				this.email.equals(user.getEmail())&&
				this.password.equals(user.getPassword())&&
				this.remain.equals(user.getRemain())
		){
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		return "User{" +
				"id=" + id +
				", name='" + name + '\'' +
				", email='" + email + '\'' +
				", password='" + password + '\'' +
				", remain=" + remain +
				'}';
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getRemain() {
		return remain;
	}

	public void setRemain(Integer remain) throws Exception{
		if(remain<0){
			throw new Exception("not enough money");
		}
		this.remain = remain;
	}
}

