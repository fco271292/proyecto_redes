import mx.uaemex.*
import grails.util.Environment

class BootStrap {

	def init = { servletContext ->
		createUser("user","user","user@gmail.com","ROLE_USER")
		createUser("admin","admin","admin@gmail.com","ROLE_ADMIN")
	}

	def destroy = {
	}

	def createUser(String username, String password,String email, String authority ){
		Role role = new Role(authority).save()
		User user = new User(username:username, password:password, email:email, name:'name',lastName:'lastname').save()
		UserRole.create user, role
		UserRole.withSession {userRole->
			userRole.flush()
			userRole.clear()
		}
	}

}
