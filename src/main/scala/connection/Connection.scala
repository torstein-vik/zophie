package io.zophie.connection

trait ConnectionCallback {
    def callback (data : String)
}

abstract class Connection (callback : ConnectionCallback) {
    def push (data : String)
}
