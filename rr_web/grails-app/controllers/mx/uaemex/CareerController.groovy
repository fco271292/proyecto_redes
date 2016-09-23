package mx.uaemex

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional
import grails.plugin.springsecurity.annotation.Secured
import grails.converters.*

@Secured(['ROLE_USER','ROLE_ADMIN'])
@Transactional(readOnly = true)
class CareerController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Career.list(params), model:[careerCount: Career.count()]
    }

    def show(Career career) {
        respond career
    }

    def create() {
        respond new Career(params)
    }

    @Transactional
    def save(Career career) {
        if (career == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (career.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond career.errors, view:'create'
            return
        }

        career.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'career.label', default: 'Career'), career.id])
                redirect career
            }
            '*' { respond career, [status: CREATED] }
        }
    }

    def edit(Career career) {
        respond career
    }

    @Transactional
    def update(Career career) {
        if (career == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (career.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond career.errors, view:'edit'
            return
        }

        career.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'career.label', default: 'Career'), career.id])
                redirect career
            }
            '*'{ respond career, [status: OK] }
        }
    }

    @Transactional
    def delete(Career career) {

        if (career == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        career.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'career.label', default: 'Career'), career.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    def list(){
        render Career.list() as JSON
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'career.label', default: 'Career'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
