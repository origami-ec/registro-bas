/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.origami.mail.services;

import com.google.gson.Gson;
import com.origami.config.SisVars;
import com.origami.mail.models.CorreoDto;
import java.io.IOException;
import java.nio.charset.UnsupportedCharsetException;
import javax.ejb.Stateless;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

/**
 *
 * @author eduar
 */
@Stateless
public class EnviarCorreoEjb implements EnviarCorreoService {

    @Override
    public Boolean enviarCorreo(CorreoDto correo) {
        Gson gson = new Gson();
        try {
            RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(20 * 1000).setSocketTimeout(20 * 1000).build();
            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpPost httpPost = new HttpPost(SisVars.urlWsEmail + "enviarCorreo");
            httpPost.setEntity(new StringEntity(gson.toJson(correo), "UTF-8"));
            httpPost.setHeader("Content-type", "application/json; charset=utf-8");
            httpPost.setConfig(requestConfig);
            httpClient.execute(httpPost);
        } catch (IOException | UnsupportedCharsetException e) {
            System.out.println(e);
            return false;
        }
        return true;
    }
    
    @Override
    public Boolean reenviarCorreo(CorreoDto correo) {
        Gson gson = new Gson();
        try {
            RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(20 * 1000).setSocketTimeout(20 * 1000).build();
            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpPost httpPost = new HttpPost(SisVars.urlWsEmail + "correo/reenviar");
            httpPost.setEntity(new StringEntity(gson.toJson(correo), "UTF-8"));
            httpPost.setHeader("Content-type", "application/json; charset=utf-8");
            httpPost.setConfig(requestConfig);
            httpClient.execute(httpPost);
        } catch (IOException | UnsupportedCharsetException e) {
            System.out.println(e);
            return false;
        }
        return true;
    }

}
