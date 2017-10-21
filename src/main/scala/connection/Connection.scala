package io.zophie.connection

trait ConnectionCallback[T] {
    def callback (data : T)
}

trait ConnectionFactory[S, T <: Connection[S]] {
    def setupConnection (callback : ConnectionCallback[S]) : T
}

abstract class Connection[T] (callback : ConnectionCallback[T]) {
    def push (data : T)
}
