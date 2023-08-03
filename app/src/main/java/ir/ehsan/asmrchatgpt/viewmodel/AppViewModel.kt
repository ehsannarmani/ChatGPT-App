package ir.ehsan.asmrchatgpt.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.ehsan.asmrchatgpt.database.AnswerEntity
import ir.ehsan.asmrchatgpt.database.AppDatabase
import ir.ehsan.asmrchatgpt.models.BaseModel
import ir.ehsan.asmrchatgpt.models.Message
import ir.ehsan.asmrchatgpt.repositories.AppRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AppViewModel :ViewModel(),KoinComponent{
    private val db:AppDatabase by inject()
    private val repo:AppRepo by inject()

    private val _messages:MutableStateFlow<List<Message>> = MutableStateFlow(emptyList())
    val messages = _messages.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    init {
        viewModelScope.launch {
            repo.getMessages().collect{data->
                _messages.update { data }
            }
        }
    }

    fun askQuestion(question:String){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                db.answerDao().addAnswer(AnswerEntity(
                    role = "user",
                    content = question
                ))
            }
            _loading.update { true }
            repo.askQuestion(
                previousQuestions = messages.value,
                question = question
            ).also {
                _loading.update { false }
                when(val result = it){
                    is BaseModel.Success->{
                        withContext(Dispatchers.IO){
                            db.answerDao().addAnswer(AnswerEntity(
                                role = "assistant",
                                content = result.data.choices.first().message.content
                            ))
                        }
                    }
                    is BaseModel.Error->{
                        println("error: ${result.error}")
                    }
                    else->{

                    }
                }
            }
        }
    }

}