package com.simformsolutions.app.domain.remote.apiresult

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * Execute [executable] if the [ApiResult] is of type [ApiSuccess]
 *
 * @param executable    Block to execute on [ApiSuccess]
 *
 * @return [ApiResult] instance
 */
@OptIn(ExperimentalContracts::class)
suspend fun <T : Any?> ApiResult<T>.onSuccess(
    executable: suspend (T) -> Unit
): ApiResult<T> {
    contract {
        callsInPlace(executable, InvocationKind.AT_MOST_ONCE)
    }
    return apply {
        if (this is ApiSuccess<T>) {
            executable(data)
        }
    }
}

/**
 * Execute [executable] if the [ApiResult] is of type [ApiError]
 *
 * @param executable    Block to execute on [ApiError]
 *
 * @return [ApiResult] instance
 */
@OptIn(ExperimentalContracts::class)
suspend fun <T : Any?> ApiResult<T>.onError(
    executable: suspend (code: Int, message: String?) -> Unit
): ApiResult<T> {
    contract {
        callsInPlace(executable, InvocationKind.AT_MOST_ONCE)
    }
    return apply {
        if (this is ApiError<T>) {
            executable(code, message)
        }
    }
}

/**
 * Execute [executable] if the [ApiResult] is of type [ApiException]
 *
 * @param executable Block to execute on [ApiException]
 *
 * @return [ApiResult] instance
 */
@OptIn(ExperimentalContracts::class)
suspend fun <T : Any?> ApiResult<T>.onException(
    executable: suspend (e: Throwable) -> Unit
): ApiResult<T> {
    contract {
        callsInPlace(executable, InvocationKind.AT_MOST_ONCE)
    }
    return apply {
        if (this is ApiException<T>) {
            executable(exception)
        }
    }
}

/**
 * Returns [ApiResult] containing result of [transform] applied to [ApiSuccess].
 *
 * Use this extension when you want to transform a value to another value on [ApiSuccess].
 *
 * @param transform     Apply transformation to [ApiSuccess]
 *
 * @return [ApiResult] instance
 */
@OptIn(ExperimentalContracts::class)
suspend fun <T : Any?, R : Any?> ApiResult<T>.mapOnSuccess(
    transform: suspend (T) -> R
): ApiResult<R> {
    contract {
        callsInPlace(transform, InvocationKind.AT_MOST_ONCE)
    }
    return when (this) {
        is ApiError -> ApiError(code, message)
        is ApiException -> ApiException(exception)
        is ApiSuccess -> ApiSuccess(transform(data))
    }
}

/**
 * Extension function on [ApiResult] object and, depending on its state (success, error or exception),
 * invokes the corresponding suspending function.
 *
 * @param R The type of the result returned by the success, error, or exception functions.
 * @param T The type of the value contained in the [ApiResult] on success.
 * @param onSuccess A suspending lambda to be executed if the [ApiResult] is successful.
 * @param onException A suspending lambda to be executed if the [ApiResult] is an exception.
 * @param onError A suspending lambda to be executed if the [ApiResult] is an error.
 * @return The result of either the [onSuccess] or [onError] or [onException] function.
 */
@OptIn(ExperimentalContracts::class)
suspend inline fun <R, T : Any?> ApiResult<T>.foldSuspend(
    crossinline onSuccess: suspend (value: T) -> R,
    crossinline onException: suspend (exception: Throwable) -> R,
    crossinline onError: suspend (code: Int, message: String?) -> R,
): R {
    contract {
        callsInPlace(onSuccess, InvocationKind.AT_MOST_ONCE)
        callsInPlace(onException, InvocationKind.AT_MOST_ONCE)
        callsInPlace(onError, InvocationKind.AT_MOST_ONCE)
    }

    return when (this) {
        is ApiError -> onError(code, message)
        is ApiException -> onException(exception)
        is ApiSuccess -> onSuccess(data)
    }
}

/**
 * Returns [T] if [ApiResult] is [ApiSuccess], null otherwise.
 *
 * @return [T] or null
 */
@OptIn(ExperimentalContracts::class)
fun <T : Any?> ApiResult<T>.getOrNull(): T? {
    contract {
        returns(null) implies (this@getOrNull !is ApiSuccess)
        returnsNotNull() implies (this@getOrNull is ApiSuccess)
    }
    return if (this !is ApiSuccess) null else data
}

/**
 * Returns success data if [ApiResult] is [ApiSuccess], default value otherwise.
 *
 * @return [T]
 */
fun <T : Any?> ApiResult<T>.getOrDefault(defaultValue: T): T {
    if (isFailure()) return defaultValue
    return data
}

/**
 * Returns [T] if [ApiResult] is [ApiSuccess], throws [Throwable] otherwise.
 *
 * @return [T] or throws [Throwable]
 */
@OptIn(ExperimentalContracts::class)
fun <T : Any?> ApiResult<T>.getOrThrow(): T {
    contract {
        returnsNotNull() implies (this@getOrThrow is ApiSuccess)
    }
    return when (this) {
        is ApiSuccess -> data
        is ApiError -> throw Throwable(message)
        is ApiException -> throw exception
    }
}

/**
 * Returns [R] if [ApiResult] is [ApiSuccess], [onFailure] otherwise.
 *
 * @param onFailure Block to execute on [ApiError] or [ApiException]
 *
 * @return [R] or [onFailure]
 */
@OptIn(ExperimentalContracts::class)
inline fun <R, T : R> ApiResult<T>.getOrElse(onFailure: (exception: Throwable) -> R): R {
    contract {
        callsInPlace(onFailure, InvocationKind.AT_MOST_ONCE)
        returnsNotNull() implies (this@getOrElse is ApiSuccess)
    }
    return when (this) {
        is ApiSuccess -> data
        is ApiError -> onFailure(Throwable(message))
        is ApiException -> onFailure(exception)
    }
}

/**
 * Returns [Throwable] if [ApiResult] is not [ApiSuccess], null otherwise.
 *
 * @return [Throwable] or null
 */
@OptIn(ExperimentalContracts::class)
fun <T : Any?> ApiResult<T>.exceptionOrNull(): Throwable? {
    contract {
        returns(null) implies (this@exceptionOrNull is ApiSuccess)
        returnsNotNull() implies (this@exceptionOrNull !is ApiSuccess)
    }
    return when (this) {
        is ApiSuccess -> null
        is ApiError -> Throwable(message)
        is ApiException -> exception
    }
}

/**
 * Returns true if [ApiResult] is of type [ApiSuccess] else false.
 */
@OptIn(ExperimentalContracts::class)
fun <T : Any?> ApiResult<T>.isSuccess(): Boolean {
    contract {
        returns(true) implies (this@isSuccess is ApiSuccess)
        returns(false) implies (this@isSuccess !is ApiSuccess)
    }
    return this is ApiSuccess
}

/**
 * Returns true if [ApiResult] is of type [ApiError] else false.
 */
@OptIn(ExperimentalContracts::class)
fun <T : Any?> ApiResult<T>.isFailure(): Boolean {
    contract {
        returns(true) implies (this@isFailure !is ApiSuccess)
        returns(false) implies (this@isFailure is ApiSuccess)
    }
    return this !is ApiSuccess
}

/**
 * Returns true if [ApiResult] is of type [ApiException] else false.
 */
@OptIn(ExperimentalContracts::class)
fun <T : Any?> ApiResult<T>.isError(): Boolean {
    contract { returns(true) implies (this@isError is ApiError) }
    return this is ApiError
}

/**
 * Returns true if [ApiResult] is of type [ApiException] else false.
 */
@OptIn(ExperimentalContracts::class)
fun <T : Any?> ApiResult<T>.isException(): Boolean {
    contract { returns(true) implies (this@isException is ApiException) }
    return this is ApiException
}