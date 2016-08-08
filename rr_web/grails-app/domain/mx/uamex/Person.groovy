package mx.uamex

class Person {

	static constraints = {
		dateCreate(nullable: true)
		lastUpdate(nullable: true)
		house unique: true
	}

	String name
	Date dateCreate
	Date lastUpdate

	static hasOne = [house:House]
	static hasMany = [cars:Car]
}
