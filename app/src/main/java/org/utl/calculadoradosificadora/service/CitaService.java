package org.utl.calculadoradosificadora.service;

import org.utl.calculadoradosificadora.model.Cita;
import org.utl.calculadoradosificadora.service.ApiResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface CitaService {
    @GET("getAllCitas")
    Call<ApiResponse<List<Cita>>> getAllCitas();

    @POST("InsertCita")
    Call<ApiResponse<Cita>> insertCita(@Body Cita cita);

    @PUT("UpdateCita/{id}")
    Call<ApiResponse<Cita>> updateCita(@Path("id") int id, @Body Cita cita);

    @PATCH("DeleteCita/{id}/cancelar")
    Call<ApiResponse<Void>> cancelarCita(@Path("id") int id);

    @PATCH("AtenderCita/{id}/atender")
    Call<ApiResponse<Void>> atenderCita(@Path("id") int id);

    @PUT("UpdateCita/{id}")
    Call<ApiResponse<Cita>> marcarCitaComoAtendida(
            @Path("id") int id,
            @Body Cita cita
    );

    Call<ApiResponse<List<Cita>>> getCitasByTitular(int idTitular);
}