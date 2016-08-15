package mx.uaemex

class Career {

	static constraints = {
		name(blank: false)
	}

	Date dateCreated 
	Date lastUpdated
	String name

	static hasMany = [bitacoras : Bitacora]

}
