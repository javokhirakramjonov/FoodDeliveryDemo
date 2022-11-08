package uz.gita.fooddeliverydemo.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uz.gita.fooddeliverydemo.domain.useCase.AuthUseCase
import uz.gita.fooddeliverydemo.domain.useCase.FoodUseCase
import uz.gita.fooddeliverydemo.domain.useCase.impl.AuthUseCaseImpl
import uz.gita.fooddeliverydemo.domain.useCase.impl.FoodUseCaseImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface UseCaseModule {

    @[Binds Singleton]
    fun getFoodUseCase(impl: FoodUseCaseImpl): FoodUseCase

    @[Binds Singleton]
    fun getAuthUseCase(impl: AuthUseCaseImpl): AuthUseCase
}