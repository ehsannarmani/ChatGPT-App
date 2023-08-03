package ir.ehsan.asmrchatgpt.repositories

import ir.ehsan.asmrchatgpt.models.Answer
import ir.ehsan.asmrchatgpt.models.BaseModel
import ir.ehsan.asmrchatgpt.models.Message
import kotlinx.coroutines.flow.Flow

interface AppRepo {
    suspend fun askQuestion(previousQuestions:List<Message>,question:String):BaseModel<Answer>
    suspend fun getMessages(): Flow<List<Message>>
    suspend fun addAnswer(answer: Message)
}