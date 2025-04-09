package org.utl.calculadoradosificadora.service;

import org.utl.calculadoradosificadora.model.InicioSesion;
import org.utl.calculadoradosificadora.model.Medico;
import org.utl.calculadoradosificadora.model.Usuario;
import org.utl.calculadoradosificadora.service.ApiResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface MedicoService {
    @GET("getAllMedicos")
    Call<ApiResponse<List<Medico>>> getAllMedicos();

    @POST("InsertMedico")
    Call<ApiResponse<Medico>> insertMedico(@Body Medico medico);

    @PUT("UpdateMedico/{id}")
    Call<ApiResponse<Medico>> updateMedico(@Path("id") int id, @Body Medico medico);
    @POST("loginMedico")
    Call<ApiResponse<Medico>> loginMedico(@Body InicioSesion inicioSesion);
    @POST("logout")
    Call<Void> logout(@Header("Authorization") String token);
}