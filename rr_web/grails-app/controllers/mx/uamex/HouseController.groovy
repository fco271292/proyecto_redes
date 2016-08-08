package mx.uamex

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class HouseController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond House.list(params), model:[houseCount: House.count()]
    }

    def show(House house) {
        respond house
    }

    def create() {
        respond new House(params)
    }

    @Transactional
    def save(House house) {
        if (house == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (house.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond house.errors, view:'create'
            return
        }

        house.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'house.label', default: 'House'), house.id])
                redirect house
            }
            '*' { respond house, [status: CREATED] }
        }
    }

    def edit(House house) {
        respond house
    }

    @Transactional
    def update(House house) {
        if (house == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (house.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond house.errors, view:'edit'
            return
        }

        house.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'house.label', default: 'House'), house.id])
                redirect house
            }
            '*'{ respond house, [status: OK] }
        }
    }

    @Transactional
    def delete(House house) {

        if (house == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        house.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'house.label', default: 'House'), house.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'house.label', default: 'House'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
