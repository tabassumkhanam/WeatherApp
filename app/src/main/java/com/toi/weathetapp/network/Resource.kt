package com.toi.weathetapp.network

  data class Resource<out T>(
      var status: Status,
      val data: T?,
      val throwable
                             : Throwable?) {
    companion object {
        fun <T> success(data: T?): Resource<T> {
            return Resource(Status.SUCCESS, data, null)
        }

        fun <T> error(msg: Throwable?=null, data: T?=null): Resource<T> {
            return Resource(Status.ERROR, data, msg)
        }

        fun <T> loading(data: T? = null): Resource<T> {
            return Resource(Status.LOADING, data, null)
        }
    }

    public enum class Status {
        SUCCESS,
        ERROR,
        LOADING
    }
}
