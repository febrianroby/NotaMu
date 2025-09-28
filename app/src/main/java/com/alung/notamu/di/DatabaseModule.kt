package com.alung.notamu.di


import android.content.Context
import androidx.room.Room
import com.alung.notamu.data.AppDatabase
import com.alung.notamu.data.dao.BarangDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides @Singleton
    fun provideDb(@ApplicationContext ctx: Context): AppDatabase =
        Room.databaseBuilder(ctx, AppDatabase::class.java, "kasir_db").build()


    @Provides
    fun provideBarangDao(db: AppDatabase): BarangDao = db.barangDao()
}