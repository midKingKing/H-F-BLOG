package com.hf.util

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.JsonNode
import org.apache.log4j.Logger
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.lang.reflect.Type

object JsonConverter {
    val log: Logger = Logger.getLogger(JsonConverter::class.java)

    @JvmStatic
    fun <T> serialize(obj: T?): String? {
        return try {
            JsonMapperFactory.defaultMapper.writeValueAsString(obj)
        } catch (e: IOException) {
            log.error("Serialize json failed.", e)
            null
        }
    }

    @JvmStatic
    fun <T> serializeTo(obj: T?, output: OutputStream) {
        JsonMapperFactory.defaultMapper.writeValue(output, obj)
    }

    /**
     * Serialize to byte array using UTF-8.
     */
    @JvmStatic
    fun <T> serializeAsBytes(obj: T?): ByteArray? {
        return try {
            JsonMapperFactory.defaultMapper.writeValueAsBytes(obj)
        } catch (e: IOException) {
            log.error("Serialize json failed.", e)
            null
        }
    }

    @JvmStatic
    @JvmOverloads
    fun <T> deserialize(json: String, clazz: Class<T>, silent: Boolean = false): T? {
        return try {
            JsonMapperFactory.defaultMapper.readValue(json, clazz)
        } catch (e: IOException) {
            if (!silent) {
                log.error("Deserialize json failed: $json", e)
            }
            null
        }
    }

    @JvmStatic
    inline fun <reified T> deserialize(json: String): T? {
        return try {
            JsonMapperFactory.defaultMapper.readValue(json, T::class.java)
        } catch (e: IOException) {
            log.error("Deserialize json failed: $json", e)
            null
        }
    }

    @JvmStatic
    inline fun <reified T> deserialize(json: String, silent: Boolean): T? {
        return try {
            JsonMapperFactory.defaultMapper.readValue(json, T::class.java)
        } catch (e: IOException) {
            if (!silent) {
                log.error("Deserialize json failed: $json", e)
            }
            null
        }
    }

    @JvmStatic
    @JvmOverloads
    fun <T> deserialize(bytes: ByteArray, clazz: Class<T>, silent: Boolean = false): T? {
        return try {
            JsonMapperFactory.defaultMapper.readValue(bytes, clazz)
        } catch (e: IOException) {
            if (!silent) {
                log.error("Deserialize json failed.", e)
            }
            null
        }
    }

    @JvmStatic
    inline fun <reified T> deserialize(bytes: ByteArray): T? {
        return deserialize(bytes, T::class.java)
    }

    @JvmStatic
    @JvmOverloads
    fun deserialize(json: String, type: Type, silent: Boolean = false): Any? {
        return try {
            JsonMapperFactory.defaultMapper.readValue<Any>(
                json,
                JsonMapperFactory.getType(type)
            )
        } catch (e: IOException) {
            if (!silent) {
                log.error("Deserialize json failed: $json", e)
            }
            null
        }
    }

    @JvmStatic
    @JvmOverloads
    fun deserialize(json: InputStream, type: Type, silent: Boolean = false): Any? {
        return try {
            JsonMapperFactory.defaultMapper.readValue<Any>(
                json,
                JsonMapperFactory.getType(type)
            )
        } catch (e: IOException) {
            if (!silent) {
                log.error("Deserialize json failed: $json", e)
            }
            null
        }
    }

    @JvmStatic
    @JvmOverloads
    fun deserialize(json: ByteArray, type: Type, silent: Boolean = false): Any? {
        return try {
            JsonMapperFactory.defaultMapper.readValue<Any>(
                json,
                JsonMapperFactory.getType(type)
            )
        } catch (e: IOException) {
            if (!silent) {
                log.error("Deserialize json failed: $json", e)
            }
            null
        }
    }

    @JvmStatic
    @JvmOverloads
    fun <T> deserializeList(json: String, elementClazz: Class<T>, silent: Boolean = false): List<T>? {
        return try {
            JsonMapperFactory.defaultMapper.readValue<List<T>>(
                json,
                JsonMapperFactory.getListType(elementClazz)
            )
        } catch (e: IOException) {
            if (!silent) {
                log.error("Deserialize json failed: $json", e)
            }
            null
        }
    }

    @JvmStatic
    inline fun <reified T> deserializeList(json: String): List<T>? {
        return try {
            JsonMapperFactory.defaultMapper.readValue<List<T>>(
                json,
                JsonMapperFactory.getListType(T::class.java)
            )
        } catch (e: IOException) {
            log.error("Deserialize json failed: $json", e)
            null
        }
    }

    @JvmStatic
    @JvmOverloads
    fun <T, E> deserializeMap(json: String, key: Class<T>, value: Class<E>, silent: Boolean = false): Map<T, E>? {
        return try {
            JsonMapperFactory.defaultMapper.readValue<Map<T, E>>(
                json,
                JsonMapperFactory.getMapType(key, value)
            )
        } catch (e: IOException) {
            if (!silent) {
                log.error("Deserialize json failed: $json", e)
            }
            null
        }
    }

    @Suppress("UNCHECKED_CAST")
    @JvmStatic
    @JvmOverloads
    fun <T, E> deserializeGenerics(json: String, type: Class<T>, parameterType: Class<E>, silent: Boolean = false): T? {
        return try {
            val valueType = JsonMapperFactory.defaultMapper.typeFactory
                .constructParametricType(type, parameterType)
            JsonMapperFactory.defaultMapper.readValue<T>(json, valueType)
        } catch (e: IOException) {
            if (!silent) {
                log.error("Deserialize json failed: $json", e)
            }
            null
        }
    }

    @JvmStatic
    @JvmOverloads
    fun readTree(json: String, silent: Boolean = false): JsonNode? {
        return try {
            JsonMapperFactory.defaultMapper.readTree(json)
        } catch (e: IOException) {
            if (!silent) {
                log.error("Read tree from json failed: $json", e)
            }
            null
        }
    }

    @JvmStatic
    @JvmOverloads
    fun <T> valueToTree(obj: T, silent: Boolean = false): JsonNode? {
        return try {
            JsonMapperFactory.defaultMapper.valueToTree(obj)
        } catch (e: IllegalArgumentException) {
            if (!silent) {
                log.error("Convert value to tree failed.", e)
            }
            null
        }
    }

    @JvmStatic
    @JvmOverloads
    fun <T> treeToValue(node: JsonNode, clazz: Class<T>, silent: Boolean = false): T? {
        return try {
            JsonMapperFactory.defaultMapper.treeToValue(node, clazz)
        } catch (e: JsonProcessingException) {
            if (!silent) {
                log.error("Convert tree to value failed.", e)
            }
            null
        }
    }
}
