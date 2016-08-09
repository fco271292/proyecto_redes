package mx.uamex

class Car {

    static constraints = {
    }

    String marca

    static belongsTo = [person : Person]
    
}
