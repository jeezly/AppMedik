package org.utl.calculadoradosificadora.service;

import org.utl.calculadoradosificadora.model.InicioSesion;
import org.utl.calculadoradosificadora.model.Medico;
import org.utl.calculadoradosificadora.model.Titular;
import org.utl.calculadoradosificadora.service.ApiResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface TitularService {
    @GET("getAllTitulares")
    Call<ApiResponse<List<Titular>>> getAllTitulares();

    @POST("InsertTitular")
    Call<ApiResponse<Titular>> insertTitular(@Body Titular titular);

    @PUT("UpdateTitular/{id}")
    Call<ApiResponse<Titular>> updateTitular(@Path("id") int id, @Body Titular titular);

    @PATCH("DeleteTitular/{id}/inhabilitar")
    Call<ApiResponse<Void>> deleteTitular(@Path("id") int id);

    @POST("loginTitular")
    Call<ApiResponse<Titular>> loginTitular(@Body InicioSesion inicioSesion);
}