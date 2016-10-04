<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'document.label', default: 'Document')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
        <a href="#list-document" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
        <div class="nav" role="navigation">
            <ul>
                <li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
                <li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
            </ul>
        </div>
        <div id="list-document" class="content scaffold-list" role="main">
            <h1><g:message code="default.list.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
                <div class="message" role="status">${flash.message}</div>
            </g:if>
            
            <table>
                <thead>
                    <tr>
                        <g:sortableColumn property="fileName" title="File Name" />
                        <g:sortableColumn property="fullPath" title="Full Path" />
                        <g:sortableColumn property="extentionFile" title="Extention File" />
                        <g:sortableColumn property="bitacora" title="Bitacora" />
                        <g:sortableColumn property="dateCreated" title="Date created" />
                        <g:sortableColumn property="lastUpdated" title="Last Updated" />
                    </tr>
                </thead>
                <tbody>
                    <g:each in="${documentList}" var="documentInstance">
                        <tr>
                            <td><g:link action="downloadFile" params="[file: documentInstance.fileName]">${documentInstance.fileName}</g:link></td>
                            <td><g:link action="show" id="${documentInstance.id}">${documentInstance.fullPath}</g:link></td>
                            <td>${documentInstance.extentionFile}</td>
                            <td>${documentInstance.bitacora}</td>
                            <td><g:formatDate date="${documentInstance.dateCreated}" /></td>
                            <td><g:formatDate date="${documentInstance.lastUpdated}" /></td>
                        </tr>
                    </g:each>
                </tbody>
            </table>

            <div class="pagination">
                <g:paginate total="${documentCount ?: 0}" />
            </div>
        </div>
    </body>
</html>