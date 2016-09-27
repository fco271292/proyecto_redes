package mx.uaemex

import grails.converters.*
import grails.transaction.Transactional

class DocumentController {

	def documentService
	
	def index() { }

	def uploadFile(){
		def file = request.getFile('file') 
		if(file) {
			String originalFilename = file.filename
	  	String extentionFile = originalFilename.reverse().takeWhile{character-> character != "." }
	  	File convertFile = new File("${grailsApplication.config.local}${originalFilename}")
	  	file.transferTo(convertFile)
	  	documentService.uploadFile(convertFile.getAbsolutePath(),"${grailsApplication.config.server}${originalFilename}")
	  	Document document = new Document(fileName:originalFilename,fullPath:"${grailsApplication.config.server}${originalFilename}",extentionFile:extentionFile.reverse())
	  	save(document)
		} 
		else {
			render(status: 200, text: "OK") as JSON
		}
  }

  def downloadFile(){
  	if(params.file){
  		String fileName = params.file
  		String remoteFilePath = "${grailsApplication.config.server}${params.file}"
  		String localFilePath = remoteFilePath.reverse().takeWhile{character-> character != "." }
  		String extentionFile = "${grailsApplication.config.local}${fileName}"
  		documentService.downloadFile(remoteFilePath,localFilePath)
  		if(new File(localFilePath).exists()){
	   		render(status: 200, text: "OK") as JSON
	   	}
   	}
   	else{
   		render(status: 400, text: "BadRequest") as JSON	
   	}
  }

	@Transactional
	def save(Document document) {
		if (document == null) {
			transactionStatus.setRollbackOnly()
			return
		}

		if (document.hasErrors()) {
			transactionStatus.setRollbackOnly()
			respond document.errors
			return
		}
	  document.save flush:true
	  render(status: 200, text: "OK") as JSON
	}
	
}