<html>
<head>
  <meta name="layout" content="main"/>
</head>
<body>

  <ol class="breadcrumb">
    <li class="breadcrumb-item active">Home</li>
    <li class="breadcrumb-item">Index</li>
  </ol>
  
  <div id="content" role="main">
    <section class="row colset-2-its">
      <div class="alert alert-success text-center" role="alert">
        <h3>Registro de salas de computo</h3>
      </div>
      <div id="controllers" role="navigation">
        <nav class="nav nav-tabs">
          <ul class="nav nav-pills">
            <li class="nav-item active">
              <g:each var="c" in="${grailsApplication.controllerClasses.sort { it.fullName } }">
                <li class="controller">
                  <g:link controller="${c.logicalPropertyName}">
                    <h3><span class="label label-success">${c.name}</span></h3>
                  </g:link>
                </li>
              </g:each>
            </li>
          </ul>
        </nav>
      </div>
    </section>
  </div>

  <div id="myCarousel" class="carousel slide" data-ride="carousel">
    <ol class="carousel-indicators">
      <li data-target="#myCarousel" data-slide-to="0" class="active"></li>
      <li data-target="#myCarousel" data-slide-to="1"></li>
      <li data-target="#myCarousel" data-slide-to="2"></li>
    </ol>

    <div class="carousel-inner" role="listbox" align="center">
      <div class="item active">
        <asset:image src="CU.jpg" />
      </div>

      <div class="item">
        <asset:image src="CU1.jpg"/>
      </div>

      <div class="item">
        <asset:image src="CU2.jpg"/>
      </div>
    </div>
  </div>

</body>
</html>