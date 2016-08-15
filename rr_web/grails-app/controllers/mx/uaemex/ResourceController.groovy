package mx.uaemex

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class ResourceController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Resource.list(params), model:[resourceCount: Resource.count()]
    }

    def show(Resource resource) {
        respond resource
    }

    def create() {
        respond new Resource(params)
    }

    @Transactional
    def save(Resource resource) {
        if (resource == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (resource.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond resource.errors, view:'create'
            return
        }

        resource.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'resource.label', default: 'Resource'), resource.id])
                redirect resource
            }
            '*' { respond resource, [status: CREATED] }
        }
    }

    def edit(Resource resource) {
        respond resource
    }

    @Transactional
    def update(Resource resource) {
        if (resource == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (resource.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond resource.errors, view:'edit'
            return
        }

        resource.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'resource.label', default: 'Resource'), resource.id])
                redirect resource
            }
            '*'{ respond resource, [status: OK] }
        }
    }

    @Transactional
    def delete(Resource resource) {

        if (resource == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        resource.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'resource.label', default: 'Resource'), resource.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'resource.label', default: 'Resource'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
