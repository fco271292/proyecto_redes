package mx.uamex

class House {

    static constraints = {
    }

    String direccion

    static belongsTo = [person : Person]
}
