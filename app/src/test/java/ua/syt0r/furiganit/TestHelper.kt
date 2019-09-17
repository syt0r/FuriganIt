package ua.syt0r.furiganit

import org.mockito.Mockito

inline fun <reified T: Any> mock() = Mockito.mock(T::class.java)