package rr_web

class UrlMappings {

    static mappings = {
        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }

        "/"(view:"/index")
        "500"(view:'/error')
        "404"(view:'/notFound')
        "/bitacoras"(resources:'bitacora')
        "/users"(resources:'user')
        "/teachers"(controller: "teacher", action: "list", method: "GET")
        "/laboratories"(controller: "laboratory", action: "list", method: "GET")
        "/careers"(controller: "career", action: "list", method: "GET")
        "/user"(controller: "user", action: "getUserByUsername", method: "GET")
    }
}
