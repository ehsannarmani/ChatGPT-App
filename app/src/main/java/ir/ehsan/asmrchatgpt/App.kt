package ir.ehsan.asmrchatgpt

import android.app.Application
import androidx.room.Room
import ir.ehsan.asmrchatgpt.database.AppDatabase
import ir.ehsan.asmrchatgpt.network.Api
import ir.ehsan.asmrchatgpt.repositories.AppRepo
import ir.ehsan.asmrchatgpt.repositories.AppRepoImpl
import org.koin.core.context.startKoin
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin{
            modules(module {
                single {
                    Retrofit.Builder()
                        .baseUrl("https://api.openai.com/v1/chat/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                }
                single {
                    val retrofit:Retrofit = get()
                    retrofit.create(Api::class.java)
                }
                single {
                    Room
                        .databaseBuilder(this@App,AppDatabase::class.java,"db")
                        .build()
                }

                single {
                    val api:Api = get()
                    val db:AppDatabase = get()

                    AppRepoImpl(
                        api = api,
                        dao = db.answerDao()
                    )
                } bind AppRepo::class

            })
        }
    }
}