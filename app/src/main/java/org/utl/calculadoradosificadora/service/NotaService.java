package org.utl.calculadoradosificadora.service;

<<<<<<< HEAD
public class NotaService {
    //Cuidadooo no es tipo Interface
}
=======
import org.utl.calculadoradosificadora.model.Nota;
import org.utl.calculadoradosificadora.service.ApiResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface NotaService {
    @POST("InsertNota")
    Call<ApiResponse<Nota>> insertNota(@Body Nota nota);
}
>>>>>>> 971eba21309322345b9001c1aa473d0e14aab02a
