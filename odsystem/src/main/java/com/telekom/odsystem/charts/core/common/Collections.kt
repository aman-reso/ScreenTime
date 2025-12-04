import androidx.annotation.RestrictTo
import com.telekom.odsystem.charts.core.common.ERR_REPEATING_COLLECTION_EMPTY

@JvmName("copyDouble")
internal fun <T> List<List<T>>.copy() = map { it.toList() }

@JvmName("copyTriple")
internal fun <T> List<List<List<T>>>.copy() = map { it.copy() }

internal fun <T> MutableList<T>.setAll(other: Collection<T>) {
    clear()
    addAll(other)
}

// This has been modified customarily
internal fun <T> ArrayList<ArrayList<T>>.copy(): List<List<T>> =
    List(this.size) { index -> ArrayList(this[index]) } // Use 'this.size' to access the size property

internal fun <K, V> MutableMap<K, V>.setAll(other: Map<K, V>) {
    clear()
    other.forEach { (key, value) -> set(key, value) }
}

internal fun <T> Collection<T>.averageOf(selector: (T) -> Float): Float =
    fold(0f) { sum, element -> sum + selector(element) } / size

internal fun <T> mutableListOf(sourceCollection: Collection<T>): MutableList<T> =
    ArrayList<T>(sourceCollection.size).apply { addAll(sourceCollection) }

/** @suppress */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
public fun <T> List<T>.getRepeating(index: Int): T {
    if (isEmpty()) throw IllegalStateException(ERR_REPEATING_COLLECTION_EMPTY)
    return get(index % size.coerceAtLeast(1))
}
