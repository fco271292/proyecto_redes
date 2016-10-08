package mx.uaemex

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class BitacoraController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def exportService
    def documentExportService

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

    def generatePDF() {
        documentExportService.exportDocumentPDF()
    }
}
