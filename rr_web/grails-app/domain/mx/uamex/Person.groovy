package mx.uamex

class Person {

    static constraints = {
    	dateCreate(nullable: true)
    	lastUpdate(nullable: true)
    }
    
    String name
    Date dateCreate
    Date lastUpdate
}
