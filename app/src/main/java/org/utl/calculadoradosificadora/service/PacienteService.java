package org.utl.calculadoradosificadora.service;

import org.utl.calculadoradosificadora.model.Paciente;
import org.utl.calculadoradosificadora.service.ApiResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PacienteService {
    @GET("getAllPacientes")
    Call<ApiResponse<List<Paciente>>> getPacientesByTitular(@Query("idTitular") int idTitular);

    // Añade este nuevo método para actualizar paciente
    @PUT("UpdatePaciente/{id}")
    Call<ApiResponse<Paciente>> updatePaciente(
            @Path("id") int id,
            @Body Paciente paciente
    );
    @PUT("UpdatePaciente/{id}")
    Call<ApiResponse<Paciente>> actualizarPesoPaciente(
            @Path("id") int id,
            @Body Paciente paciente
    );
}