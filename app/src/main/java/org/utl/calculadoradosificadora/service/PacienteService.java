package org.utl.calculadoradosificadora.service;

import org.utl.calculadoradosificadora.model.Paciente;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PacienteService {
    @GET("getPacientesByTitular")
    Call<List<Paciente>> getPacientesByTitular(@Query("idTitular") int idTitular);
}