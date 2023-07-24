package com.devst.reader.Screens.login

data class LoadingState (val status:Status,val message:String?=null){

    companion object{
        val IDLE= LoadingState(Status.IDLE)
        val SUCESS=LoadingState(Status.SUCESS)
        val  LOADING=LoadingState(Status.LOADING)
        val FAILED= LoadingState(Status.FAILED)
    }

    enum class  Status{
        SUCESS,
        FAILED,
        LOADING,
        IDLE
    }
}

