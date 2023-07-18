package com.example.newsapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.newsapp.models.Article

@Database([Article::class], version = 1)
@TypeConverters(Converters::class)
abstract class ArticleDateBase : RoomDatabase() {

    abstract fun getArticleDao(): ArticlesDao

    companion object {
        @Volatile
        private var instance: ArticleDateBase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also { instance = it }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                ArticleDateBase::class.java,
                "article_database"
            ).build()
    }


}