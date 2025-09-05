/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.origami.sgr.services.ejbs;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.origami.config.SisVars;
import com.origami.sgr.models.Predio;
import com.origami.sgr.services.interfaces.CatastroService;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;

/**
 *
 * @author eduar
 */
@Stateless
public class CatastroEjb implements CatastroService {

    @Override
    public List<Predio> buscarPredioCatastro(Integer tipo, String codigo) {
        try {
            String definicion = "";
            switch (tipo) {
                case 1:
                    definicion = "cedula/";
                    break;
                case 2:
                    definicion = "clave/";
                    break;
            }

            URL obj = new URL(SisVars.urlWsCatastro + definicion + codigo);
            HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            StringBuilder response;
            try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                String inputLine;
                response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
            }

            Type collectionType = new TypeToken<List<Predio>>() {
            }.getType();
            return (List<Predio>) new Gson().fromJson(response.toString(), collectionType);
        } catch (IOException e) {
            System.out.println(e);
        }
        return new ArrayList<>();
    }

}
