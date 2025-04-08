package org.utl.calculadoradosificadora.service;

import org.utl.calculadoradosificadora.model.Medicamento;
import org.utl.calculadoradosificadora.service.ApiResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface MedicamentoService {
    @GET("getAllMedicamento")
    Call<ApiResponse<List<Medicamento>>> getAllMedicamento();
}