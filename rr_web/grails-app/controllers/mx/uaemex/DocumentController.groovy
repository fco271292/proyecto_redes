package mx.uaemex

import grails.converters.*
import grails.transaction.Transactional
import static org.springframework.http.HttpStatus.*
import grails.plugin.springsecurity.annotation.Secured

@Secured(['ROLE_USER','ROLE_ADMIN'])
class DocumentController {

	static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]
	
	def documentService
	
	def index(Integer max) {
    params.max = Math.min(max ?: 10, 100)
    respond Document.list(params), model:[documentCount: Document.count()]
  }

  def show(Document document) {
  	respond document
  }

  def create() {
  	respond new Document(params)
  }

	@Transactional
  def save(Document document) {
  	if (document == null) {
  		transactionStatus.setRollbackOnly()
  		notFound()
  		return
  	}

  	if (document.hasErrors()) {
  		transactionStatus.setRollbackOnly()
  		respond teacher.errors, view:'create'
  		return
  	}

  	document.save flush:true

  	request.withFormat {
  		form multipartForm {
  			flash.message = message(code: 'default.created.message', args: [message(code: 'document.label', default: 'Document'), document.id])
  			redirect document
  		}
  		'*' { respond teacher, [status: CREATED] }
  	}
  }

  def edit(Document document) {
  	respond document
  }

  @Transactional
  def update(Document document) {
  	if (document == null) {
  		transactionStatus.setRollbackOnly()
  		notFound()
  		return
  	}

  	if (document.hasErrors()) {
  		transactionStatus.setRollbackOnly()
  		respond document.errors, view:'edit'
  		return
  	}

  	document.save flush:true

  	request.withFormat {
  		form multipartForm {
  			flash.message = message(code: 'default.updated.message', args: [message(code: 'document.label', default: 'Document'), document.id])
  			redirect document
  		}
  		'*'{ respond document, [status: OK] }
  	}
  }

  @Transactional
  def delete(Document document) {

  	if (document == null) {
  		transactionStatus.setRollbackOnly()
  		notFound()
  		return
  	}

  	document.delete flush:true

  	request.withFormat {
  		form multipartForm {
  			flash.message = message(code: 'default.deleted.message', args: [message(code: 'document.label', default: 'Document'), document.id])
  			redirect action:"index", method:"GET"
  		}
  		'*'{ render status: NO_CONTENT }
  	}
  }

  protected void notFound() {
  	request.withFormat {
  		form multipartForm {
  			flash.message = message(code: 'default.not.found.message', args: [message(code: 'document.label', default: 'Document'), params.id])
  			redirect action: "index", method: "GET"
  		}
  		'*'{ render status: NOT_FOUND }
  	}
  }

  def uploadFile(){
		def file = request.getFile('file') 
		if(file) {
			String originalFilename = file.filename
	  	String extentionFile = originalFilename.reverse().takeWhile{character-> character != "." }
	  	File convertFile = new File("${grailsApplication.config.local}${originalFilename}")
	  	file.transferTo(convertFile)
	  	documentService.uploadFile(convertFile.getAbsolutePath(),"${grailsApplication.config.server}${originalFilename}")
	  	Document document = new Document(fileName:originalFilename,fullPath:"${grailsApplication.config.server}${originalFilename}",extentionFile:extentionFile.reverse())
	  	saveDocument(document)
		} 
		else {
			render(status: 400, text: "BadRequest") as JSON
		}
  }

  def downloadFile(){
  	if(params.file){
  		String fileName = params.file
  		String remoteFilePath = "${grailsApplication.config.server}${params.file}"
  		String localFilePath = remoteFilePath.reverse().takeWhile{character-> character != "." }
  		String extentionFile = "${grailsApplication.config.local}${fileName}"
  		documentService.downloadFile(remoteFilePath,localFilePath)
      File file = new File(localFilePath)
      if(file.exists()){
        byte[] contentFile = file.bytes
        response.setContentType("APPLICATION/OCTET-STREAM")
        response.setHeader("Content-Disposition", "Attachment;Filename=\"${fileName}\"")
        response.outputStream << contentFile
        response.outputStream.flush()
      }
    }
    else{
      render(status: 400, text: "BadRequest") as JSON	
    }
  }

	@Transactional
	def saveDocument(Document document) {
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
	  render(status: 200, text: "${document?.id}") as JSON
	}
	
}