package mx.uaemex

class Bitacora {

	static constraints = {
		groupName nullable:true
		document nullable:true
	}

	Date dateCreated 
	Date lastUpdated
	String groupName
	Document document
	
	static belongsTo = [user:User, teacher:Teacher, laboratory:Laboratory, career:Career]

}
