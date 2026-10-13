package com.dio.dio_itau_design_patterns_spring.service;

import com.dio.dio_itau_design_patterns_spring.model.Address;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Client HTTP, criado pelo <b>OpenFeign</b>, para consumo da API do <b>ViaCEP</b>
 *
 * @see <a href="https://viacep.com.br">ViaCEP</a>
 *
 * @author Pedro-Ramon2608
 */
@FeignClient(name = "viacep", url = "https://viacep.com.br/ws")
public interface ViaCepService {

    @GetMapping("/{cep}/json/")
    Address consultCep(@PathVariable("cep") String cep);

}
