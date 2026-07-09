package com.example.quickorderapp.domain.usecase

import com.example.quickorderapp.domain.model.Mesa
import com.example.quickorderapp.domain.repository.MesaRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMesasUseCase @Inject constructor(
    private val repository: MesaRepository
) {
    operator fun invoke(): Flow<List<Mesa>> = repository.getMesas()
}
