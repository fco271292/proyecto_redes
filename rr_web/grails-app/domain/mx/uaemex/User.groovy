package mx.uaemex

class User {

	static constraints = {
		name(blank: false)
		lastName(blank: false)
		email(blank: false, email: true)
		username(blank: false, min: 3)
		password(blank: false, min: 6)

	}

	Date dateCreated 
	Date lastUpdated 
	String name
	Strina lastName
	String email
	String username
	String password
	
}
