package mx.uaemex

class User {

	static constraints = {
		name(blank: false)
		lastName(blank: false)
		email(blank: false, email: true)
		username(size: 3..20)
		password(size: 3..20)

	}

	Date dateCreated 
	Date lastUpdated 
	String name
	String lastName
	String email
	String username
	String password

	static hasMany = [bitacoras : Bitacora]
	
}
