package io.zophie.connection

trait ConnectionCallback[T] {
    def callback (data : T)
}


abstract class Connection[T] (callback : ConnectionCallback[T]) {
    def push (data : T)
}
