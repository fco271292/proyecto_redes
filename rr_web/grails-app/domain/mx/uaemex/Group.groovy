package mx.uaemex

class Group {

	static constraints = {
		name(blank: false)
	}

	Date dateCreated 
	Date lastUpdated
	String name

}
