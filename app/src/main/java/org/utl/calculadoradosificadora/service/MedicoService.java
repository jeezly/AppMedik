package org.utl.calculadoradosificadora.service;

import org.utl.calculadoradosificadora.model.Medico;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface MedicoService {
    @GET("getAllMedicos")
    Call<List<Medico>> getAllMedicos();
    @POST("InsertMedico")
    Call<Medico> insertMedico(@Body Medico medico);

    @PUT("UpdateMedico/{id}")
    Call<Medico> updateMedico(@Path("id") int id, @Body Medico medico);

    @DELETE("DeleteMedico/{id}/inhabilitar")
    Call<Void> deleteMedico(@Path("id") int id);
}
