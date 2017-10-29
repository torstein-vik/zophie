package io.zophie.api.connection

// A callback that is called whenever some data is received by the associated connection object
trait ConnectionCallback[T] {
    def callback (data : T)
}

// A factory that creates a Connection of type S from a ConnectionCallback with the same type
trait ConnectionFactory[S, T <: Connection[S]] {
    def setupConnection (callback : ConnectionCallback[S]) : T
}

// Connection object for sending and receiving data, e.g. through a socket. Push is for sending, callback when it receives
abstract class Connection[T] (callback : ConnectionCallback[T]) {
    def push (data : T)
}
