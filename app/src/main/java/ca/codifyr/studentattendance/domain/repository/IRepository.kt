package ca.codifyr.studentattendance.domain.repository

interface IRepository<T> {
    fun add(item: T): Boolean
    fun getSingle(): T
    fun getCollection(): List<T>
    fun deleteSingle(id: Int): Void
    fun updateSingle(item: T): Void
}