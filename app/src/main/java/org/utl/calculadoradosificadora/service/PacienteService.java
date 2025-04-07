package org.utl.calculadoradosificadora.service;

import org.utl.calculadoradosificadora.model.Paciente;
import org.utl.calculadoradosificadora.service.ApiResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PacienteService {
    @GET("getAllPacientes")
    Call<ApiResponse<List<Paciente>>> getPacientesByTitular(@Query("idTitular") int idTitular);
}