package com.bombadu.rxtime2

import java.util.*

object DataSource {
    @JvmStatic
    fun createTasksList(): List<Task> {
        val tasks: MutableList<Task> = ArrayList()
        tasks.add(Task("Take out the trash", true, 3))
        tasks.add(Task("Walk the dog", false, 2))
        tasks.add(Task("Make my bed", true, 1))
        tasks.add(Task("Unload the dishwasher", false, 0))
        tasks.add(Task("Make dinner", true, 5))
        return tasks
    }
}