package com.example.quickorderapp.domain.usecase

import com.example.quickorderapp.domain.model.Mesa
import com.example.quickorderapp.domain.repository.MesaRepository
import javax.inject.Inject

class AddMesaUseCase @Inject constructor(
    private val repository: MesaRepository
) {
    suspend operator fun invoke(mesa: Mesa) = repository.addMesa(mesa)
}
