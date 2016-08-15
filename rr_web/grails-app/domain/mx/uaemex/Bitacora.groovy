package mx.uaemex

class Bitacora {

	static constraints = {
		groupName(nullable:true)
		resourcePicture(nullable:true)
	}

	Date dateCreated 
	Date lastUpdated
	String groupName
	String resourcePicture

	static belongsTo = [user:User, teacher:Teacher, laboratory:Laboratory, career:Career]

}
