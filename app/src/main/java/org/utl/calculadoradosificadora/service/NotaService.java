package org.utl.calculadoradosificadora.service;

import org.utl.calculadoradosificadora.model.Nota;
import org.utl.calculadoradosificadora.service.ApiResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
public interface NotaService {
    @POST("InsertNota")
    Call<ApiResponse<Nota>> insertNota(@Body Nota nota);

}


