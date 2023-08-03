package ir.ehsan.asmrchatgpt.database

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [AnswerEntity::class], version = 1)
abstract class AppDatabase:RoomDatabase(){
    abstract fun answerDao():AnswerDao
}