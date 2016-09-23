package mx.uaemex

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional
import grails.plugin.springsecurity.annotation.Secured
import grails.converters.*

@Secured(['ROLE_USER','ROLE_ADMIN'])
@Transactional(readOnly = true)
class LaboratoryController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Laboratory.list(params), model:[laboratoryCount: Laboratory.count()]
    }

    def show(Laboratory laboratory) {
        respond laboratory
    }

    def create() {
        respond new Laboratory(params)
    }

    @Transactional
    def save(Laboratory laboratory) {
        if (laboratory == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (laboratory.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond laboratory.errors, view:'create'
            return
        }

        laboratory.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'laboratory.label', default: 'Laboratory'), laboratory.id])
                redirect laboratory
            }
            '*' { respond laboratory, [status: CREATED] }
        }
    }

    def edit(Laboratory laboratory) {
        respond laboratory
    }

    @Transactional
    def update(Laboratory laboratory) {
        if (laboratory == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (laboratory.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond laboratory.errors, view:'edit'
            return
        }

        laboratory.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'laboratory.label', default: 'Laboratory'), laboratory.id])
                redirect laboratory
            }
            '*'{ respond laboratory, [status: OK] }
        }
    }

    @Transactional
    def delete(Laboratory laboratory) {

        if (laboratory == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        laboratory.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'laboratory.label', default: 'Laboratory'), laboratory.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    def list(){
        render Laboratory.list() as JSON
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'laboratory.label', default: 'Laboratory'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
