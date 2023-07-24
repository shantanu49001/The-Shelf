package com.devst.reader

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp    //intilatte hilt over appli ation lifecycle
class ReaderApplication :Application() {
}