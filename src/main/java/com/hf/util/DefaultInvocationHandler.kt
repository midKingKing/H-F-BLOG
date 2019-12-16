package com.hf.util

import java.lang.reflect.InvocationHandler

/**
 * Default [InvocationHandler] which contains the target object.
 *
 * @author Yue Jun(yuejun@alo7.com)
 */
abstract class DefaultInvocationHandler(val targetObject: Any?) : InvocationHandler