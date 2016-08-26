package mx.uaemex

import grails.test.mixin.*
import spock.lang.*

@TestFor(ResourceController)
@Mock(Resource)
class ResourceControllerSpec extends Specification {

    def populateValidParams(params) {
        assert params != null

        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
        assert false, "TODO: Provide a populateValidParams() implementation for this generated test suite"
    }
    @Ignore
    void "Test the index action returns the correct model"() {

        when:"The index action is executed"
            controller.index()

        then:"The model is correct"
            !model.resourceList
            model.resourceCount == 0
    }
    @Ignore
    void "Test the create action returns the correct model"() {
        when:"The create action is executed"
            controller.create()

        then:"The model is correctly created"
            model.resource!= null
    }
    @Ignore
    void "Test the save action correctly persists an instance"() {

        when:"The save action is executed with an invalid instance"
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'POST'
            def resource = new Resource()
            resource.validate()
            controller.save(resource)

        then:"The create view is rendered again with the correct model"
            model.resource!= null
            view == 'create'

        when:"The save action is executed with a valid instance"
            response.reset()
            populateValidParams(params)
            resource = new Resource(params)

            controller.save(resource)

        then:"A redirect is issued to the show action"
            response.redirectedUrl == '/resource/show/1'
            controller.flash.message != null
            Resource.count() == 1
    }
    @Ignore
    void "Test that the show action returns the correct model"() {
        when:"The show action is executed with a null domain"
            controller.show(null)

        then:"A 404 error is returned"
            response.status == 404

        when:"A domain instance is passed to the show action"
            populateValidParams(params)
            def resource = new Resource(params)
            controller.show(resource)

        then:"A model is populated containing the domain instance"
            model.resource == resource
    }
    @Ignore
    void "Test that the edit action returns the correct model"() {
        when:"The edit action is executed with a null domain"
            controller.edit(null)

        then:"A 404 error is returned"
            response.status == 404

        when:"A domain instance is passed to the edit action"
            populateValidParams(params)
            def resource = new Resource(params)
            controller.edit(resource)

        then:"A model is populated containing the domain instance"
            model.resource == resource
    }
    @Ignore
    void "Test the update action performs an update on a valid domain instance"() {
        when:"Update is called for a domain instance that doesn't exist"
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'PUT'
            controller.update(null)

        then:"A 404 error is returned"
            response.redirectedUrl == '/resource/index'
            flash.message != null

        when:"An invalid domain instance is passed to the update action"
            response.reset()
            def resource = new Resource()
            resource.validate()
            controller.update(resource)

        then:"The edit view is rendered again with the invalid instance"
            view == 'edit'
            model.resource == resource

        when:"A valid domain instance is passed to the update action"
            response.reset()
            populateValidParams(params)
            resource = new Resource(params).save(flush: true)
            controller.update(resource)

        then:"A redirect is issued to the show action"
            resource != null
            response.redirectedUrl == "/resource/show/$resource.id"
            flash.message != null
    }
    @Ignore
    void "Test that the delete action deletes an instance if it exists"() {
        when:"The delete action is called for a null instance"
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'DELETE'
            controller.delete(null)

        then:"A 404 is returned"
            response.redirectedUrl == '/resource/index'
            flash.message != null

        when:"A domain instance is created"
            response.reset()
            populateValidParams(params)
            def resource = new Resource(params).save(flush: true)

        then:"It exists"
            Resource.count() == 1

        when:"The domain instance is passed to the delete action"
            controller.delete(resource)

        then:"The instance is deleted"
            Resource.count() == 0
            response.redirectedUrl == '/resource/index'
            flash.message != null
    }
}
