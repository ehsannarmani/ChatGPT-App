package ir.ehsan.asmrchatgpt.repositories

import ir.ehsan.asmrchatgpt.database.AnswerDao
import ir.ehsan.asmrchatgpt.database.AnswerEntity
import ir.ehsan.asmrchatgpt.models.Answer
import ir.ehsan.asmrchatgpt.models.BaseModel
import ir.ehsan.asmrchatgpt.models.Message
import ir.ehsan.asmrchatgpt.models.Question
import ir.ehsan.asmrchatgpt.network.Api
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.lang.Exception

class AppRepoImpl(
    private val api: Api,
    private val dao: AnswerDao
):AppRepo {
    override suspend fun askQuestion(
        previousQuestions: List<Message>,
        question: String
    ): BaseModel<Answer> {
        try {
            api.askQuestion(
                question = Question(
                    messages = previousQuestions + Message(
                        role = "user",
                        content = question
                    )
                )
            ).also {
                return if (it.isSuccessful){
                    BaseModel.Success(data = it.body()!!)
                }else{
                    BaseModel.Error(it.errorBody()?.string().toString())
                }
            }
        }catch (e:Exception){
            return BaseModel.Error(e.message.toString())
        }
    }

    override suspend fun getMessages(): Flow<List<Message>> {
        return dao.getAnswers().map {
            it.map {
                Message(it.role,it.content)
            }
        }
    }

    override suspend fun addAnswer(answer: Message) {
        dao.addAnswer(AnswerEntity(
            role = answer.role,
            content = answer.content
        ))
    }
}