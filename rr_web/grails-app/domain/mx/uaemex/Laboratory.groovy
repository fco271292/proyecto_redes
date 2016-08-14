package mx.uaemex

class Laboratory {

	static constraints = {
		name(blank: false)
	}

	Date dateCreated 
	Date lastUpdated
	String name

}
