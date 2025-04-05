package org.utl.calculadoradosificadora.service;

import org.utl.calculadoradosificadora.model.Medico;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface MedicoService {
    @GET("getAllMedicos")
    Call<List<Medico>> getAllMedicos();
}
