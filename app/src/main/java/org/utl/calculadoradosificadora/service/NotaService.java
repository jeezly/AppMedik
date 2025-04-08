package org.utl.calculadoradosificadora.service;

import org.utl.calculadoradosificadora.model.Nota;
import org.utl.calculadoradosificadora.service.ApiResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface NotaService {
    @POST("InsertNota")
    Call<ApiResponse<Nota>> insertNota(@Body Nota nota);

    @GET("getNotasByCita/{idCita}")
    Call<ApiResponse<List<Nota>>> getNotasByCita(@Path("idCita") int idCita);
}