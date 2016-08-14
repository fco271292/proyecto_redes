package mx.uaemex

class Teacher {

	static constraints = {
		name(blank: false)
		lastName(blank: false)
	}

	Date dateCreated 
	Date lastUpdated 
	String name
	String lastName

}
