package com.example.newsapp.domain.service

abstract class BaseService {
    abstract fun getServiceName(): String
    
    open fun logOperation(operation: String) {
        println("[${getServiceName()}] Выполняется операция: $operation")
    }
    
    fun validateNotEmpty(value: String?, fieldName: String): Boolean {
        if (value.isNullOrBlank()) {
            println("Ошибка: поле '$fieldName' не может быть пустым")
            return false
        }
        return true
    }
}