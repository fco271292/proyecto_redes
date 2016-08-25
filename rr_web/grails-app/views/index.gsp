<html>
<head>
  <meta name="layout" content="main"/>
</head>
<body>

  <ol class="breadcrumb">
    <li class="breadcrumb-item active">Home</li>
    <li class="breadcrumb-item">Index</li>
  </ol>

  <div class="svg" role="presentation">
    <div class="grails-logo-container">
      <asset:image src="edit-document.svg" class="grails-logo"/>
    </div>
  </div>
  <div id="content" role="main">
    <section class="row colset-2-its">
      <div class="alert alert-danger" role="alert">
        <h3>Registro de salas de computo</h3>
      </div>
      <div id="controllers" role="navigation">
        <nav class="navbar-light bg-faded">
          <ul class="nav navbar-nav">
            <li class="nav-item active">
              <g:each var="c" in="${grailsApplication.controllerClasses.sort { it.fullName } }">
                <li class="controller">
                  <g:link controller="${c.logicalPropertyName}">${c.name}</g:link>
                </li>
              </g:each>
            </li>
          </ul>
        </nav>
      </div>
    </section>
  </div>

</body>
</html>