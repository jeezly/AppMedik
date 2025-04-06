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
import retrofit2.http.Query;

public interface CitaService {
    @GET("getAllCitas")
    Call<ApiResponse<List<Cita>>> getAllCitas();

    @GET("getCitasByMedico")
    Call<ApiResponse<List<Cita>>> getCitasByMedico(@Query("idMedico") int idMedico);

    @GET("getCitasByTitular")
    Call<ApiResponse<List<Cita>>> getCitasByTitular(@Query("idTitular") int idTitular);

    @POST("InsertCita")
    Call<ApiResponse<Cita>> agendarCita(@Body Cita cita);

    @PUT("UpdateCita/{id}")
    Call<ApiResponse<Cita>> actualizarCita(@Path("id") int id, @Body Cita cita);

    @PATCH("DeleteCita/{id}/cancelar")
    Call<ApiResponse<Void>> cancelarCita(@Path("id") int id);

    @PATCH("UpdateCita/{id}/atender")
    Call<ApiResponse<Void>> marcarComoAtendida(@Path("id") int id);
}