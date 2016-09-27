package mx.uaemex

class Document {

	Date dateCreated
	Date lastUpdated
	String fileName
	String fullPath
	String extentionFile

	static constraints = {
		filename nullable:false
		fullPath nullable:false
		extentionFile nullable:true
	}
	
}