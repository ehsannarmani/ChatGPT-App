package ir.ehsan.asmrchatgpt.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AnswerDao {

    @Insert
    fun addAnswer(answerEntity: AnswerEntity)

    @Query("SELECT * FROM `answers`")
    fun getAnswers(): Flow<List<AnswerEntity>>
}