package mx.uaemex

class Document {

	Date dateCreated
	Date lastUpdated
	String fileName
	String fullPath
	String extentionFile

	static constraints = {
		fileName nullable:false
		fullPath nullable:false
		extentionFile nullable:true
		bitacora nullable:true, unique:true
	}
	
	static belongsTo = [bitacora:Bitacora]
	
}