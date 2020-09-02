package com.scootin.extensions

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.channelFlow
import java.util.concurrent.atomic.AtomicBoolean

class AbsentLiveData<T : Any?> private constructor() : LiveData<T>() {
    init {
        // use post instead of set since this can be created on any thread
        postValue(null)
    }

    companion object {
        fun <T> create(): LiveData<T> {
            return AbsentLiveData()
        }
    }
}

class MakeLiveData<T : Any?> private constructor(var data: T) : LiveData<T>() {
    init {
        // use post instead of set since this can be created on any thread
        postValue(data)
    }
    companion object {
        fun <T> create(data: T): LiveData<T> {
            return MakeLiveData(data)
        }
    }
}

/**
 *
 * This live data is create to handle
 * consumeable one time live data.
 * So if its data is updated observer will observe it once!
 * This will useful for when we using liveData as event
 * or trigger should that forget.
 *
 */
class ConsumableLiveData<T>(var consume: Boolean = false) : MutableLiveData<T>() {

    private val pending = AtomicBoolean(false)

    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        super.observe(
            owner,
            Observer<T> {
                if (consume) {
                    if (pending.compareAndSet(true, false)) observer.onChanged(it)
                } else {
                    observer.onChanged(it)
                }
            }
        )
    }

    override fun setValue(value: T) {
        pending.set(true)
        super.setValue(value)
    }
}

@ExperimentalCoroutinesApi
@FlowPreview
fun <T> LiveData<T>.asFlow() = channelFlow {
    offer(value)
    val observer = Observer<T> { t -> offer(t) }
    observeForever(observer)
    invokeOnClose {
        removeObserver(observer)
    }
}

fun <T : Any?> MutableLiveData<T>.default(initialValue: T) = apply { setValue(initialValue) }

fun <T> LiveData<T>.debounce(duration: Long = 1000L) = MediatorLiveData<T>().also { mld ->
    val source = this
    val handler = Handler(Looper.getMainLooper())

    val runnable = Runnable {
        mld.value = source.value
    }

    mld.addSource(source) {
        handler.removeCallbacks(runnable)
        handler.postDelayed(runnable, duration)
    }
}

/**
 * Sets the value to the result of a function that is called when both `LiveData`s have data
 * or when they receive updates after that.
 */
fun <T, A, B> LiveData<A>.combineAndCompute(other: LiveData<B>, onChange: (A?, B?) -> T): MediatorLiveData<T> {

    var source1emitted = false
    var source2emitted = false

    val result = MediatorLiveData<T>()

    val mergeF = {
        if (source1emitted && source2emitted) {
            result.value = onChange.invoke(this.value, other.value)
        }
    }

    result.addSource(this) { source1emitted = true; mergeF.invoke() }
    result.addSource(other) { source2emitted = true; mergeF.invoke() }

    return result
}

class DoubleTrigger<A, B>(a: LiveData<A>?, b: LiveData<B>?) : MediatorLiveData<Pair<A?, B?>>() {
    init {
        a?.let { first ->
            addSource(first) { value = it to b?.value }
        }
        b?.let { second ->
            addSource(second) { value = a?.value to it }
        }
    }
}
