package ua.syt0r.furiganit.core.application

import android.app.Application
import ua.syt0r.furiganit.core.di.DiInjector

class FuraginaApp : Application() {

	override fun onCreate() {
		super.onCreate()
		DiInjector.inject(this)
	}

}
