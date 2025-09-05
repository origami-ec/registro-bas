/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.documental.ejbs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.origami.config.SisVars;
import com.origami.documental.models.Token;
import com.origami.documental.services.OrigamiGTService;
import com.origami.session.UserSession;
import com.origami.sgr.util.JsonDateDeserializer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Singleton;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author jesus
 */
@Singleton(name = "origamiGTEjb")
@ApplicationScoped
public class OrigamiGTEjb implements OrigamiGTService {

    @Inject
    private UserSession us;
    protected Gson gson;
    protected GsonBuilder builder;

    public OrigamiGTEjb() {
        builder = new GsonBuilder().setDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz")
                .registerTypeAdapter(Date.class, new JsonDateDeserializer()).disableHtmlEscaping();
        gson = builder.create();
    }

    @Override
    public Object methodGET(String url, Class resultClazz) {
        try {
            System.out.println("url: " + url);

            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + us.getToken()); //accessToken can be the secret key you generate.
            headers.set("user", us.getName_user()); //username
            headers.set("ip", us.getIpClient()); //ipclient
            headers.set("mac", us.getMacClient()); //macclient
            headers.set("os", us.getOsClient()); //oscliente
            headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
            URI uri = new URI(url);
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<Object> response = restTemplate.exchange(uri, HttpMethod.GET, entity, resultClazz);
            if (response != null) {
                return response.getBody();
            }
            return null;
        } catch (URISyntaxException | RestClientException e) {
            System.out.println(e);
        }
        return null;
    }

    @Override
    public List methodListGET(String url, Class resultClazz) {
        try {
            System.out.println("url: " + url);
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + us.getToken()); //accessToken can be the secret key you generate.
            headers.set("user", us.getName_user()); //username
            headers.set("ip", us.getIpClient()); //ipclient
            headers.set("mac", us.getMacClient()); //macclient
            headers.set("os", us.getOsClient()); //oscliente
            headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
            URI uri = new URI(url);
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<Object[]> response = restTemplate.exchange(uri, HttpMethod.GET, entity, resultClazz);
            if (response != null) {
                return Arrays.asList(response.getBody());
            } else {
                return null;
            }
        } catch (URISyntaxException | RestClientException e) {
            Logger.getLogger(OrigamiGTEjb.class.getName()).log(Level.SEVERE, "{0}", e);
        }
        return null;
    }

    @Override
    public Object methodPOST(Object data, String url, Class resultClass) {
        System.out.println("url: " + url);
        System.out.println("gson.toJson(data): " + gson.toJson(data));
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + us.getToken()); //accessToken can be the secret key you generate.
            headers.set("user", us.getName_user()); //username
            headers.set("ip", us.getIpClient()); //ipclient
            headers.set("mac", us.getMacClient()); //macclient
            headers.set("os", us.getOsClient()); //oscliente
            headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
            URI uri = new URI(url);
            HttpEntity<String> entity = new HttpEntity<>(gson.toJson(data), headers);
            ResponseEntity<Object> response = restTemplate.exchange(uri, HttpMethod.POST, entity, resultClass);
            if (response != null) {
                return response.getBody();
            }
            return null;
        } catch (URISyntaxException | RestClientException e) {
            Logger.getLogger(OrigamiGTEjb.class.getName()).log(Level.SEVERE, "{0}", e);
        }
        return null;
    }

    @Override
    public List methodListPOST(List data, String url, Class resultClass) {
        System.out.println("url: " + url);
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + us.getToken()); //accessToken can be the secret key you generate.
            headers.set("user", us.getName_user()); //username
            headers.set("ip", us.getIpClient()); //ipclient
            headers.set("mac", us.getMacClient()); //macclient
            headers.set("os", us.getOsClient()); //oscliente
            headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
            URI uri = new URI(url);
            HttpEntity<String> entity = new HttpEntity<>(gson.toJson(data), headers);
            ResponseEntity<Object[]> response = restTemplate.exchange(uri, HttpMethod.POST, entity, resultClass);
            if (response != null) {
                return Arrays.asList(response.getBody());
            } else {
                return null;
            }
        } catch (URISyntaxException | RestClientException e) {
            Logger.getLogger(OrigamiGTEjb.class.getName()).log(Level.SEVERE, "{0}", e);
        }
        return null;
    }

    @Override
    public List methodListPOST(Object data, String url, Class resultClass) {
        System.out.println("url: " + url);
        System.out.println("gson.toJson(data): " + gson.toJson(data));
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + us.getToken()); //accessToken can be the secret key you generate.
            headers.set("user", us.getName_user()); //username
            headers.set("ip", us.getIpClient()); //ipclient
            headers.set("mac", us.getMacClient()); //macclient
            headers.set("os", us.getOsClient()); //oscliente
            headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
            URI uri = new URI(url);
            HttpEntity<String> entity = new HttpEntity<>(gson.toJson(data), headers);
            ResponseEntity<Object[]> response = restTemplate.exchange(uri, HttpMethod.POST, entity, resultClass);
            if (response != null) {
                return Arrays.asList(response.getBody());
            } else {
                return null;
            }
        } catch (URISyntaxException | RestClientException e) {
            Logger.getLogger(OrigamiGTEjb.class.getName()).log(Level.SEVERE, "{0}", e);
        }
        return null;
    }

    @Override
    public Object methodPUT(Object data, String url, Class resultClass) {
        System.out.println("url: " + url);
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + us.getToken()); //accessToken can be the secret key you generate.
            headers.set("user", us.getName_user()); //username
            headers.set("ip", us.getIpClient()); //ipclient
            headers.set("mac", us.getMacClient()); //macclient
            headers.set("os", us.getOsClient()); //oscliente
            headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
            URI uri = new URI(url);
            HttpEntity<String> entity = new HttpEntity<>(gson.toJson(data), headers);
            ResponseEntity<Object> response = restTemplate.exchange(uri, HttpMethod.PUT, entity, resultClass);
            if (response != null) {
                return response.getBody();
            }
            return null;
        } catch (URISyntaxException | RestClientException e) {
            Logger.getLogger(OrigamiGTEjb.class.getName()).log(Level.SEVERE, "{0}", e);
        }
        return null;
    }

    @Override
    public List methodListPUT(List data, String url, Class resultClass) {
        System.out.println("url: " + url);
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + us.getToken()); //accessToken can be the secret key you generate.
            headers.set("user", us.getName_user()); //username
            headers.set("ip", us.getIpClient()); //ipclient
            headers.set("mac", us.getMacClient()); //macclient
            headers.set("os", us.getOsClient()); //oscliente
            headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
            URI uri = new URI(url);
            HttpEntity<String> entity = new HttpEntity<>(gson.toJson(data), headers);
            ResponseEntity<Object[]> response = restTemplate.exchange(uri, HttpMethod.PUT, entity, resultClass);
            if (response != null) {
                return Arrays.asList(response.getBody());
            } else {
                return null;
            }
        } catch (URISyntaxException | RestClientException e) {
            Logger.getLogger(OrigamiGTEjb.class.getName()).log(Level.SEVERE, "{0}", e);
        }
        return null;
    }

    @Override
    public Object methodPOST(String url, Class resultClass) {
        System.out.println("url: " + url);
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + us.getToken()); //accessToken can be the secret key you generate.
            headers.set("user", us.getName_user()); //username
            headers.set("ip", us.getIpClient()); //ipclient
            headers.set("mac", us.getMacClient()); //macclient
            headers.set("os", us.getOsClient()); //oscliente
            headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
            URI uri = new URI(url);
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<Object> response = restTemplate.exchange(uri, HttpMethod.POST, entity, resultClass);
            if (response != null) {
                return response.getBody();
            }
            return null;
        } catch (URISyntaxException | RestClientException e) {
            Logger.getLogger(OrigamiGTEjb.class.getName()).log(Level.SEVERE, "{0}", e);
        }
        return null;
    }

    @Override
    public List methodListPOST(String url, Class resultClass) {
        System.out.println("url: " + url);
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + us.getToken()); //accessToken can be the secret key you generate.
            headers.set("user", us.getName_user()); //username
            headers.set("ip", us.getIpClient()); //ipclient
            headers.set("mac", us.getMacClient()); //macclient
            headers.set("os", us.getOsClient()); //oscliente
            headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
            URI uri = new URI(url);
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<Object[]> response = restTemplate.exchange(uri, HttpMethod.POST, entity, resultClass);
            if (response != null) {
                return Arrays.asList(response.getBody());
            } else {
                return null;
            }
        } catch (URISyntaxException | RestClientException e) {
            Logger.getLogger(OrigamiGTEjb.class.getName()).log(Level.SEVERE, "{0}", e);
        }
        return null;
    }

    @Override
    public String autenticate(String user, String pass) {

        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(SisVars.urlOrigamiZuul + "/authenticate");
        httpPost.setEntity(new StringEntity("{\"username\" : \"" + user + "\", \"password\" : \"" + pass + "\" }", "UTF-8"));
        httpPost.setHeader("Content-type", "application/json; charset=utf-8");

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<HttpResponse> futureResponse = executorService.submit(() -> httpClient.execute(httpPost));
        try {
            HttpResponse httpResponse = futureResponse.get(30, TimeUnit.SECONDS);
            if (httpResponse != null) {
                StringBuilder sb;
                try (BufferedReader in = new BufferedReader(
                        new InputStreamReader(httpResponse.getEntity().getContent(), "UTF-8"))) {
                    String inputLine;
                    sb = new StringBuilder();
                    while ((inputLine = in.readLine()) != null) {
                        sb.append(inputLine);
                    }
                }
                try {
                    Token t = gson.fromJson(sb.toString(), Token.class);
                    System.out.println("t.getToken() " + t.getToken());
                    return t.getToken();
                } catch (JsonSyntaxException e) {
                    Logger.getLogger(OrigamiGTEjb.class.getName()).log(Level.SEVERE, null, e);
                    return sb.toString();
                }
            } else {
                return null;
            }
        } catch (InterruptedException | ExecutionException | TimeoutException | IOException ex) {

            Logger.getLogger(OrigamiGTEjb.class.getName()).log(Level.SEVERE, "{0}", ex);

        }
        return null;
    }

    @Override
    public Object methodPOSTwithouAuth(Object data, String url, Class resultClass) {
        System.out.println("url: " + url);
        System.out.println("gson.toJson(data): " + gson.toJson(data));

        //String auth = "Basic " + Base64.getEncoder().encodeToString(creds.getBytes());
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(new ByteArrayEntity(gson.toJson(data).getBytes(Charset.defaultCharset()), ContentType.APPLICATION_JSON));
        httpPost.setHeader("Content-type", "application/json; charset=utf-8");

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<HttpResponse> futureResponse = executorService.submit(() -> httpClient.execute(httpPost));
        try {
            HttpResponse httpResponse = futureResponse.get(30, TimeUnit.SECONDS);
            if (httpResponse != null) {
                StringBuilder sb;
                try (BufferedReader in = new BufferedReader(
                        new InputStreamReader(httpResponse.getEntity().getContent(), "UTF-8"))) {
                    String inputLine;
                    sb = new StringBuilder();
                    while ((inputLine = in.readLine()) != null) {
                        sb.append(inputLine);
                    }
                }
                try {
                    return gson.fromJson(sb.toString(), resultClass);
                } catch (JsonSyntaxException e) {
                    Logger.getLogger(OrigamiGTEjb.class.getName()).log(Level.SEVERE, null, e);
                    return sb.toString();
                }
            } else {
                return null;
            }
        } catch (InterruptedException | ExecutionException | TimeoutException | IOException ex) {
            Logger.getLogger(OrigamiGTEjb.class.getName()).log(Level.SEVERE, "{0}", ex);
        }
        return null;
    }

    @Override
    public Object methodGETwithouAuth(String url, Class resultClazz) {
        try {
            System.out.println("url: " + url);
            //RestTemplate restTemplate = new RestTemplate(IrisUtil.getClientHttpRequestFactory(null, null));
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
            URI uri = new URI(url);
            HttpEntity<String> entity = new HttpEntity<>(headers);
            //ResponseEntity<Object> response = restTemplate.getForEntity(uri, resultClazz);
            ResponseEntity<Object> response = restTemplate.exchange(uri, HttpMethod.GET, entity, resultClazz);
            if (response != null) {
                return response.getBody();
            }
            return null;
        } catch (URISyntaxException | RestClientException ex) {
            Logger.getLogger(OrigamiGTEjb.class.getName()).log(Level.SEVERE, "{0}", ex);
        }
        return null;
    }

    @Override
    public Object methodPUTwithouAuth(Object data, String url, Class resultClass) {
        System.out.println("url: " + url);

        //String auth = "Basic " + Base64.getEncoder().encodeToString(creds.getBytes());
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPut httpPut = new HttpPut(url);
        httpPut.setEntity(new ByteArrayEntity(gson.toJson(data).getBytes(Charset.defaultCharset()), ContentType.APPLICATION_JSON));
        httpPut.setHeader("Content-type", "application/json; charset=utf-8");

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<HttpResponse> futureResponse = executorService.submit(() -> httpClient.execute(httpPut));
        try {
            HttpResponse httpResponse = futureResponse.get(30, TimeUnit.SECONDS);
            if (httpResponse != null) {
                StringBuilder sb;
                try (BufferedReader in = new BufferedReader(
                        new InputStreamReader(httpResponse.getEntity().getContent()))) {
                    String inputLine;
                    sb = new StringBuilder();
                    while ((inputLine = in.readLine()) != null) {
                        sb.append(inputLine);
                    }
                }
                return gson.fromJson(sb.toString(), resultClass);
            } else {
                return null;
            }
        } catch (InterruptedException | ExecutionException | TimeoutException | IOException ex) {
            Logger.getLogger(OrigamiGTEjb.class.getName()).log(Level.SEVERE, "{0}", ex);
        }
        return null;
    }

    @Override
    public Object methodUploadFile(Object data, Object indice, String formato, String filename, 
            byte[] file, String url, Class resultClass) {
        String nombreArchivo;
        try {
            nombreArchivo = new String(filename
                    .getBytes(Charset.defaultCharset()), "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(OrigamiGTEjb.class.getName()).log(Level.SEVERE, "{0}", ex);
            nombreArchivo = filename;
        }
        //nombreArchivo = StringUtils.stripAccents(nombreArchivo); 
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.set("Authorization", "Bearer " + us.getToken());
        headers.set("user", us.getName_user()); //username
        headers.set("ip", us.getIpClient()); //ipclient
        headers.set("mac", us.getMacClient()); //macclient
        headers.set("os", us.getOsClient()); //oscliente
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        // This nested HttpEntiy is important to create the correct
        // Content-Disposition entry with metadata "name" and "filename"
        MultiValueMap<String, String> fileMap = new LinkedMultiValueMap<>();
        ContentDisposition contentDisposition = ContentDisposition
                .builder("form-data")
                .name("document")
                .filename(nombreArchivo)
                .build();
        fileMap.add(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString());
        HttpEntity<byte[]> fileEntity = new HttpEntity<>(file, fileMap);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("document", fileEntity);
        body.add("usuario", gson.toJson(data));
        body.add("formato", formato);
        body.add("indexacion", gson.toJson(indice));

        HttpEntity<MultiValueMap<String, Object>> requestEntity
                = new HttpEntity<>(body, headers);
        try {
            ResponseEntity<Object> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    requestEntity,
                    resultClass);
            if (response != null) {
                return response.getBody();
            }
            return null;
        } catch (HttpClientErrorException ex) {
            Logger.getLogger(OrigamiGTEjb.class.getName()).log(Level.SEVERE, "{0}", ex);
        }
        return null;
    }

}
