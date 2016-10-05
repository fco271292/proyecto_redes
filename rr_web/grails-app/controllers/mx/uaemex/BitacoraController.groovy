package mx.uaemex

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional
import java.awt.Color

@Transactional(readOnly = true)
class BitacoraController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def exportService

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Bitacora.list(params), model:[bitacoraCount: Bitacora.count()]
    }

    def show(Bitacora bitacora) {
        respond bitacora
    }

    def create() {
        respond new Bitacora(params)
    }

    @Transactional
    def save(Bitacora bitacora) {
        if (bitacora == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (bitacora.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond bitacora.errors, view:'create'
            return
        }

        bitacora.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'bitacora.label', default: 'Bitacora'), bitacora.id])
                redirect bitacora
            }
            '*' { respond bitacora, [status: CREATED] }
        }
    }

    def edit(Bitacora bitacora) {
        respond bitacora
    }

    @Transactional
    def update(Bitacora bitacora) {
        if (bitacora == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (bitacora.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond bitacora.errors, view:'edit'
            return
        }

        bitacora.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'bitacora.label', default: 'Bitacora'), bitacora.id])
                redirect bitacora
            }
            '*'{ respond bitacora, [status: OK] }
        }
    }

    @Transactional
    def delete(Bitacora bitacora) {

        if (bitacora == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        bitacora.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'bitacora.label', default: 'Bitacora'), bitacora.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'bitacora.label', default: 'Bitacora'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }

    def list = {
        if(!params.max) params.max = 10

        if(params?.extension && params.extension != "html"){
            response.contentType = grailsApplication.config.grails.mime.types[params.extension]
            response.setHeader("Content-disposition", "attachment; filename=Bitacora_${new Date().format('dd_MM_yyyy')}.${params.extension}")

            List fields = ["id", "groupName","document","user","teacher","laboratory","career","dateCreated"]
            Map labels = [id: "ID", groupName: "Grupo", document: "Fotografía", user: "Usuario", teacher: "Docente", laboratory: "Laboratorio", career: "Carrera", dateCreated: "Fecha de inicio"]
            Map parameters = [title: "Bitacora","pdf.encoding": "UTF-8","title.font.size": 14, "title.font.style": "bold","separator.color": new Color(255, 255, 255) ]
            def bitacoraList = Bitacora.list()
            
            exportService.export(params.extension, response.outputStream, bitacoraList, fields, labels, [:] , parameters)
        }
    }
}
