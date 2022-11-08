package uz.gita.fooddeliverydemo.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uz.gita.fooddeliverydemo.domain.repository.AuthRepository
import uz.gita.fooddeliverydemo.domain.repository.FoodRepository
import uz.gita.fooddeliverydemo.domain.repository.LocalRepository
import uz.gita.fooddeliverydemo.domain.repository.impl.AuthRepositoryImpl
import uz.gita.fooddeliverydemo.domain.repository.impl.FoodRepositoryImpl
import uz.gita.fooddeliverydemo.domain.repository.impl.LocalRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @[Binds Singleton]
    fun getFoodRepository(impl: FoodRepositoryImpl): FoodRepository

    @[Binds Singleton]
    fun getAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @[Binds Singleton]
    fun getLocalRepository(impl: LocalRepositoryImpl): LocalRepository
}