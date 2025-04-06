package org.utl.calculadoradosificadora.service;

import org.utl.calculadoradosificadora.model.Titular;
import org.utl.calculadoradosificadora.service.ApiResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface TitularService {
    @GET("getAllTitulares")
    Call<ApiResponse<List<Titular>>> getAllTitulares();
}