package mx.uamex

class Person {

	static constraints = {
		dateCreated (nullable: true)
		lastUpdated (nullable: true)
		house unique: true, nullable: true
	}

	static mapping = {
		autoTimestamp true
	}

	String name
	Date dateCreated 
	Date lastUpdated 

	static hasOne = [house : House]
	static hasMany = [cars : Car]

}
