package com.origami.sgr.util;

import com.origami.config.SisVars;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import com.itextpdf.text.pdf.parser.ImageRenderInfo;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.RenderListener;
import com.itextpdf.text.pdf.parser.TextRenderInfo;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.RandomAccessFileOrArray;
import java.util.TimeZone;
import javax.faces.context.FacesContext;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.file.UploadedFile;
import org.primefaces.model.filter.FilterConstraint;
import org.primefaces.util.LocaleUtils;

/**
 *
 * @author User
 */
public class Utils {

    private static final int[] PATTERN = {2, 1, 2, 1, 2, 1, 2, 1, 2};
    private static final int[] CASO_9 = {4, 3, 2, 7, 6, 5, 4, 3, 2};
    private static final int[] CASO_6 = {3, 2, 7, 6, 5, 4, 3, 2};
    private static final String NUMERIC_REGEX = "^[0-9]+$";
    private static final String DECIMAL_REGEX = "^[+]?\\d+([.]\\d+)?$";
    private static final String EMAIL_REGEX = "^[\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+)*(\\.[A-Za-z]{1,})$";

    public static BigDecimal bigdecimalTo2Decimals(BigDecimal inNumber) {
        String temp = inNumber.toString();
        BigDecimal outNumber;
        int indice = temp.indexOf('.');
        if (((inNumber.toString().length() - 1) - indice) > 2) {
            String tempNew = temp.substring(0, indice + 3);
            outNumber = new BigDecimal(tempNew);
            if (((temp.length()) - (indice + 1)) >= 3) {
                if (Integer.parseInt(temp.substring(tempNew.length(), tempNew.length() + 1)) >= 5) {
                    outNumber = outNumber.add(new BigDecimal("0.01"));
                }
            }
        } else {
            outNumber = inNumber;
        }
        return outNumber;
    }

    public static List<String> separadorComas(String correos) {
        List<String> correosResulList = new ArrayList<>();
        String temp = correos;
        int indice = temp.indexOf(',');
        if (indice > 0) {
            do {
                String correo1 = temp.substring(0, indice);
                correosResulList.add(correo1);
                String correoRestante = temp.substring(indice + 1, temp.length());
                temp = correoRestante;
                indice = temp.indexOf(',');

            } while (indice > 0);
            correosResulList.add(temp);
        } else {
            correosResulList.add(correos);
        }
        return correosResulList;
    }

    public static Boolean isRepetido(Collection<String> val, Object nuevo) {
        boolean i = false;
        for (String x : val) {
            if (x.equals(nuevo)) {
                i = true;
                return i;
            }
        }
        return i;
    }

    public static synchronized boolean validarEmailConExpresion(String email) {
        return validatePattern(EMAIL_REGEX, email);
    }

    public static Long restarFechas(Date fechaMenor, Date fechaMayor) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(fechaMenor);
        cal2.setTime(fechaMayor);
        long milis1 = cal1.getTimeInMillis();
        long milis2 = cal2.getTimeInMillis();
        long diff = milis2 - milis1;
        long diffDays = diff / (24 * 60 * 60 * 1000);
        /*
        long diffHour = diff / (60 * 60 * 1000);
        diffHour = diffHour - (diffDays * 24);
        if (diffHour > 0) {
            diffDays++;
        }
         */
        return diffDays;
    }

    public static synchronized boolean validateCCRuc(final String identificacion) {
        if (identificacion == null) {
            return false;
        }
        if (identificacion.trim().isEmpty()) {
            return false;
        }
        if (!validateNumberPattern(identificacion)) {
            return false;
        }
        if (identificacion.length() != 10 & identificacion.length() != 13) {
            return false;
        }
        int[] coeficientes = null;
        int indiceDigitoVerificador = 9;
        int modulo = 11;

        if ((identificacion.length() == 13) && !identificacion.substring(10, 13).equals("001")) {
            return false;
        }
        if (identificacion.charAt(2) == '9') {
            coeficientes = CASO_9;
        } else if (identificacion.charAt(2) == '6') {
            coeficientes = CASO_6;
            indiceDigitoVerificador = 8;
        } else if (identificacion.charAt(2) < '6') {
            coeficientes = PATTERN;
            modulo = 10;
        }
        return verify(identificacion.toCharArray(), coeficientes, indiceDigitoVerificador, modulo);
    }

    private static boolean verify(final char[] array, final int[] coeficientes,
            final int indiceDigitoVerificador, final int modulo) {
        if (coeficientes == null) {
            return false;
        }
        int sum = 0;
        int aux;
        for (int i = 0; i < coeficientes.length; i++) {
            aux = new Integer(String.valueOf(array[i])) * coeficientes[i];
            if ((modulo == 10) && (aux > 9)) {
                aux -= 9;
            }
            sum += aux;
        }
        int mod = sum % modulo;
        mod = mod == 0 ? modulo : mod;
        final int res = (modulo - mod);
        Integer valorVerificar = null;
        if (array.length == 13) {
            valorVerificar = Integer.valueOf(String.valueOf(array[array.length - (13 - indiceDigitoVerificador)]));
        } else if (array.length == 10) {
            valorVerificar = Integer.valueOf(String.valueOf(array[array.length - (10 - indiceDigitoVerificador)]));
        }
        return res == valorVerificar;
    }

    public static synchronized boolean validateNumberPattern(final String valor) {
        return validatePattern(NUMERIC_REGEX, valor);
    }

    public static synchronized boolean validateDecimalPattern(final String valor) {
        return validatePattern(DECIMAL_REGEX, valor);
    }

    public static synchronized boolean validatePattern(final String patron, final String valor) {
        final Pattern patter = Pattern.compile(patron);
        final Matcher matcher = patter.matcher(valor);
        return matcher.matches();
    }

    public static Archivo crearDocumentoArchivo(InputStream is, String ruta, String nombreArchivo, String tipoArchivo) throws IOException {
        File file = new File(ruta);
        try (OutputStream out = new FileOutputStream(file)) {
            byte buf[] = new byte[1024];
            int len;
            while ((len = is.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            is.close();
        }
        Archivo documento = new Archivo();
        documento.setNombre(nombreArchivo);
        documento.setTipo(tipoArchivo);
        documento.setRuta(ruta);
        return documento;
    }

    public static Date sumarRestarDiasFecha(Date fecha, int dias) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha);
        calendar.add(Calendar.DAY_OF_YEAR, dias);
        return calendar.getTime();
    }

    public static Date sumarDiasFechaSinWeekEnd(Date fecha, int dias) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(fecha);
        int hoy;
        do {
            cal.add(Calendar.DAY_OF_YEAR, 1);
            hoy = cal.get(Calendar.DAY_OF_WEEK);
            if (hoy != Calendar.SATURDAY && hoy != Calendar.SUNDAY) {
                dias--;
            }
        } while (dias > 0);
        return cal.getTime();
    }

    public static String pasarUtf(String cadena) throws UnsupportedEncodingException {
        if (cadena != null) {
            byte[] bytes = cadena.getBytes("ISO-8859-1");
            cadena = new String(bytes, "UTF-8");
        }
        return cadena;
    }

    public static String randomNumericString() {
        int i = (int) (Math.random() * 100000);
        return String.valueOf(i);
    }

    public static Integer getDateValues(String formatValue, Date value) {
        Integer res = 0;
        SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
        f.format(value);
        switch (formatValue.toUpperCase()) {
            case "Y":
                res = f.getCalendar().get(Calendar.YEAR);
                break;
            case "M":
                res = f.getCalendar().get(Calendar.MONTH);
                break;
            case "D":
                res = f.getCalendar().get(Calendar.DAY_OF_MONTH);
                break;
        }
        return res;
    }

    public static String completarCadenaConCeros(String cadena, Integer longitud) {
        if (cadena == null) {
            return "";
        }
        if (cadena.length() > longitud) {
            return cadena.substring(0, longitud);
        }
        String ceros = "";
        for (int i = 0; i < longitud; i++) {
            ceros = ceros + "0";
        }
        int tamanio = cadena.length();
        ceros = ceros.substring(0, longitud - tamanio);
        cadena = ceros + cadena;
        return cadena;
    }

    public static String convertirMesALetra(Integer fechames) {
        String mes;
        switch (fechames) {
            case 0:
                mes = "ENERO";
                break;
            case 1:
                mes = "FEBRERO";
                break;

            case 2:
                mes = "MARZO";
                break;

            case 3:
                mes = "ABRIL";
                break;

            case 4:
                mes = "MAYO";
                break;

            case 5:
                mes = "JUNIO";
                break;

            case 6:
                mes = "JULIO";
                break;

            case 7:
                mes = "AGOSTO";
                break;

            case 8:
                mes = "SEPTIEMBRE";
                break;

            case 9:
                mes = "OCTUBRE";
                break;

            case 10:
                mes = "NOVIEMBRE";
                break;

            case 11:
                mes = "DICIEMBRE";
                break;

            default:
                mes = "DICIEMBRE";
        }
        return mes;
    }

    public static String quitarSaltos(String cadena) {
        return cadena.replace("\r", "").replace("\n", "");
    }

    public static int getNumberOfPagesDocumento(byte[] bytes) {
        int ret = -2;
        try {
            RandomAccessFileOrArray pdfFile = new RandomAccessFileOrArray(bytes);
            PdfReader reader = new PdfReader(pdfFile, new byte[0]);
            ret = reader.getNumberOfPages();
            reader.close();
        } catch (IOException ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ret;
    }

    public static boolean isDecimal(String cad) {
        try {
            Double.parseDouble(cad);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static boolean isEmpty(Collection l) {
        return l == null || l.isEmpty();
    }

    public static boolean isNotEmpty(Collection l) {
        return !Utils.isEmpty(l);
    }

    /**
     * Si el String es nulo returna vacio, caso contrario el mismo valor.
     *
     * @param nombres Object
     * @return Object
     */
    public static String isEmpty(String nombres) {
        if (nombres == null || nombres.trim().isEmpty()) {
            return "";
        }
        return nombres;
    }

    /**
     * Verifica que el valor numerico no sea nulo <code>value</code> y retorna
     * el mismo valor de <code>value</code> caso contrario retorna -1.
     *
     * @param <T> Object
     * @param value Valor a verificar.
     * @return si el valor de <code>value</code> es nulo retorna -1 caso
     * contrario el valor de <code>value</code>
     */
    public static <T> T isNull(T value) {
        if (value == null || value.toString().trim().length() < 0) {
            return (T) new BigInteger("-1");
        }
        return (T) value;
    }

    /**
     * Verifica si <code>value</code> es nulo y retorna <code>true</code>, caso
     * contrario retorna <code>false</code>.
     *
     * @param value Tipo de Dato Númerico de cualquier tipo primitivo o objecto.
     * @return True si el null caso contrario false.
     */
    public static Boolean isNumberNull(Number value) {
        if (value == null || value.longValue() < 0L) {
            return true;
        }
        return false;
    }

    public static <T> T get(final List<T> values, int idx) {
        if (values.size() > idx) {
            return values.get(idx);
        }
        return null;
    }

    public static <T> T get(final Collection<T> values, int idx) {
        if (values.size() > idx) {
            List<T> result = new ArrayList<>(values);
            return result.get(idx);
        }
        return null;
    }

    public static Date validateDate(String fecha) throws ParseException {
        SimpleDateFormat parser = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
        Date date = parser.parse(fecha);
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = formatter.format(date);
        date = formatter.parse(formattedDate);
        return date;
    }

    /**
     * Le da formato a la fecha con el pattern que se le pasa como parametro
     *
     * @param pattern Formato que se desea obtener.
     * @param fechaFin Fecha a dar formato.
     * @return Fecha con el formato esperado.
     */
    public static String dateFormatPattern(String pattern, Date fechaFin) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(fechaFin);
    }

    public static boolean isNum(String nom) {
        try {
            Long.parseLong(nom);
        } catch (Exception ex) {
            return false;
        }
        return true;
    }

    public static Integer getAnio(Date fechaIngreso) {
        Calendar c = Calendar.getInstance();
        c.setTime(fechaIngreso);
        return c.get(Calendar.YEAR);
    }

    public static Integer getMes(Date fechaIngreso) {
        Calendar c = Calendar.getInstance();
        c.setTime(fechaIngreso);
        return c.get(Calendar.MONTH);
    }

    public static Integer getDia(Date fechaIngreso) {
        Calendar c = Calendar.getInstance();
        c.setTime(fechaIngreso);
        return c.get(Calendar.DAY_OF_MONTH);
    }

    public static HashMap sortByValues(HashMap map) {
        List list = new LinkedList(map.entrySet());
        Collections.sort(list, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                return ((Comparable) ((Map.Entry) (o1)).getValue())
                        .compareTo(((Map.Entry) (o2)).getValue());
            }
        });
        HashMap sortedHashMap = new LinkedHashMap();
        for (Iterator it = list.iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry) it.next();
            sortedHashMap.put(entry.getKey(), entry.getValue());
        }
        return sortedHashMap;
    }

    public static String encriptaEnMD5(String stringAEncriptar) {
        return DigestUtils.md5Hex(stringAEncriptar);
    }

    public static String encriptSHAHex(String dato) {
        System.out.println("/encrriptSHA1 " + DigestUtils.sha512Hex(dato));
        return DigestUtils.sha512Hex(dato);
    }

    public static int boolean2int(Boolean x) {
        if (x) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     *
     * @param <T> Object
     * @param object Lista de objecto principal
     * @param previousValues lista a retornar
     * @param duplicateArray Lista a comparar si estan repetidos.
     * @param compare 0 para realizar comparacion en binario, para hacer la
     * comparacion como texto 1
     * @return Object
     */
    public static <T> List<T> verificarRepetidos(final List<T> object, final List<T> previousValues, final List<T> duplicateArray, final int compare) {
        Iterator<T> iterator = object.iterator();
        while (iterator.hasNext()) {
            T next = iterator.next();
            int count = 0;
            for (T t : duplicateArray) {
                if (compare == 0) {
                    if (next.equals(t)) {
                        count++;
                    }
                } else {
                    if (String.valueOf(next).equalsIgnoreCase(String.valueOf(t))) {
                        count++;
                    }
                }
            }
            if (count == 0 && !previousValues.contains(next)) {
                previousValues.add(next);
            }

        }
        return previousValues;
    }

    public static String getValorUuid() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    public static String quitarTildes(String cadena) {
        if (cadena == null) {
            return "";
        } else {
            cadena = Normalizer.normalize(cadena, Normalizer.Form.NFD);
            cadena = cadena.replaceAll("[^\\p{ASCII}(N\u0303)(n\u0303)(\u00A1)(\u00BF)(\u00B0)(U\u0308)(u\u0308)]", "");
            return cadena;
        }
    }

    public static String quitarCaracteresEspeciales(String cadena) {
        try {
            if (cadena == null) {
                return "";
            } else {
                return cadena.replaceAll("[^\\dA-Za-zñÑ\\s]", "");
            }
        } catch (Exception e) {
            System.out.println(e);
            return "";
        }
    }

    /**
     * TRANSFORMA LA PRIMERA LETRA EN MAYUSCULA
     *
     * @param text Texto a procesar.
     * @return Texto con la primera letra en mayucula y despues de cada _ la
     * pasa a mayucula
     */
    public static String transformUpperCase(String text) {
        if (text == null || text.isEmpty()) {
            return null;
        }
        String[] aux = text.split("_");
        if (aux.length > 1) {
            return text;
        }
        String result = "";
        int count = 0;
        for (String string : aux) {
            if (count > 0) {
                result += string.substring(0, 1).toUpperCase().concat(string.substring(1));
            } else {
                result = string;
            }
            count++;
        }
        return result;
    }

    /**
     * Realiza un split a los apellidos si encuenta un de o del lo ubica como
     * parte de uno de los apellidos.
     *
     * @param apellidos
     * @return
     */
    public static List<String> obtenerApellidos(String apellidos) {
        if (apellidos == null) {
            return null;
        }
        // Reemplazamos todos los espacion en blanco por uno solo
        apellidos = reemplazarEspacionEnBlanco(apellidos, " ");
        String[] split = apellidos.split(" ");
        int countApellidos = 2;
        List<String> result = new LinkedList<>();
        String cadenaAux = "";

        for (String cadena : split) {
            if (countApellidos != 0) {
                switch (split.length) {
                    case 2:
                        result.add(cadena);
                        countApellidos--;
                        break;
                    default:
                        if (cadena.equalsIgnoreCase("del") || cadena.equalsIgnoreCase("de")) {
                            cadenaAux = cadena + " ";
                            continue;
                        }
                        result.add(cadenaAux + cadena);
                        cadenaAux = "";
                        countApellidos--;
                        break;
                }
            } else {
                break;
            }
        }
        return result;
    }

    /**
     * Busca los apellidos en toda la cadena si existe en la cadena 'de' o 'del'
     * los concatena a apellidos siguiente.
     *
     * @param nombresCompletos Nombres y apellidos completos
     * @param inicioApellidos0 Si es false indica que empezara a recorrer desde
     * la ultima cadena hacia atras
     * @return Los 2 apellidos
     */
    public static List<String> obtenerApellidos(String nombresCompletos, boolean inicioApellidos0) {
        if (nombresCompletos == null) {
            return null;
        }
        // Reemplazamos todos los espacion en blanco por uno solo
        nombresCompletos = reemplazarEspacionEnBlanco(nombresCompletos, " ");
        String[] split = nombresCompletos.split(" ");
        int countApellidos = 2;
        List<String> result = new LinkedList<>();
        String cadenaAux = "";
        if (inicioApellidos0) {
            for (String cadena : split) {
                if (countApellidos != 0) {
                    switch (split.length) {
                        case 4:
                            result.add(cadena);
                            countApellidos--;
                            break;
                        default:
                            if (cadena.equalsIgnoreCase("del") || cadena.equalsIgnoreCase("de")) {
                                cadenaAux = cadena + " ";
                                continue;
                            }
                            result.add(cadenaAux + cadena);
                            cadenaAux = "";
                            countApellidos--;
                            break;
                    }
                } else {
                    break;
                }
            }
        } else {
            for (int i = split.length - 1; i >= 0; i--) {
                if (countApellidos > -1 || (split[i].equalsIgnoreCase("del") || split[i].equalsIgnoreCase("de"))) {
                    switch (split.length) {
                        case 4:
                            if (countApellidos > 0) {
                                result.add(0, split[i]);
                                countApellidos--;
                            }
                            break;
                        default:
                            if (split[i].equalsIgnoreCase("del") || split[i].equalsIgnoreCase("de")) {
                                cadenaAux = split[i] + " ";
                                System.out.println("Cadena " + cadenaAux);
                                if (result.size() > 0) {
                                    result.set(0, cadenaAux + result.get(0));
                                    cadenaAux = "";
                                    countApellidos--;
                                }
                            } else {
                                result.add(0, cadenaAux + split[i]);
                                cadenaAux = "";
                                countApellidos--;
                            }
                            break;
                    }
                } else {
                    break;
                }
            }
        }
        return result;
    }

    public static String reemplazarEspacionEnBlanco(String cadena, String caracterNuevo) {
        return cadena.replaceAll("\\s+", caracterNuevo);
    }

    public static String convertirFechaLetra(Date fecha) {
        String mesLetra = Utils.convertirMesALetra(Utils.getMes(fecha));
        String fechaLetra = Utils.getDia(fecha).toString() + " de "
                + mesLetra.substring(0, 1).toUpperCase() + mesLetra.substring(1)
                + " del " + Utils.getAnio(fecha).toString();
        return fechaLetra;
    }

    public static File copyFileServer(List<UploadedFile> files, String directorio) throws IOException {
        try {
            Path path = Paths.get(SisVars.rutaTemporales);
            Files.createDirectories(path);
            for (UploadedFile uFile : files) {
                File file = new File(SisVars.rutaTemporales + "/" + uFile.getFileName());
//                System.out.println(file.getName() + " >> " + file.length()); // tamanio de foto.
                try (InputStream is = uFile.getInputStream(); OutputStream out = new FileOutputStream(file)) {
                    byte buf[] = new byte[2048];
                    int len;
                    while ((len = is.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                }
                return path.toFile();
            }
        } catch (IOException e) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, "Copiar Archivo al servidor", e);
        }
        return null;
    }

    public static File copyFileServer(UploadedFile file, String directorio) throws IOException {
        try {
            Path path = Paths.get(SisVars.rutaTemporales);
            Files.createDirectories(path);
            File f = new File(SisVars.rutaTemporales + "/" + file.getFileName());
//                System.out.println(file.getName() + " >> " + file.length()); // tamanio de foto.
            try (InputStream is = file.getInputStream(); OutputStream out = new FileOutputStream(f)) {
                byte buf[] = new byte[2048];
                int len;
                while ((len = is.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            }
            return path.toFile();
        } catch (IOException e) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, "Copiar Archivo al servidor", e);
        }
        return null;
    }

    public static String createDirectoryIfNotExist(String directorio) {
        Path path = Paths.get(directorio);
        if (path.toFile().exists()) {
            return directorio;
        } else {
            try {
                Files.createDirectories(path);
            } catch (IOException ex) {
                Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return directorio;
    }

    public static String completarCadenaL(String cadena, int longitud, String addCadena) {
        if (cadena == null) {
            return "";
        }
        if (cadena.length() > longitud) {
            return cadena.substring(0, longitud);
        }
        String temp = addCadena;
        for (int i = 0; i < longitud; i++) {
            addCadena = addCadena + temp;
        }
        int tamanio = cadena.length();
        addCadena = addCadena.substring(0, longitud - tamanio);
        cadena = addCadena + cadena;
        return cadena;
    }

    public static String completarCadenaR(String cadena, int longitud, String addCadena) {
        if (cadena == null) {
            return "";
        }
        if (cadena.length() > longitud) {
            return cadena.substring(0, longitud);
        }
        String temp = addCadena;
        for (int i = 0; i < longitud; i++) {
            addCadena = addCadena + temp;
        }
        int tamanio = cadena.length();
        addCadena = addCadena.substring(0, longitud - tamanio);
        cadena = cadena + addCadena;
        return cadena;
    }

    public static HttpComponentsClientHttpRequestFactory getClientHttpRequestFactory(String user, String pass) {
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        clientHttpRequestFactory.setHttpClient(httpClient(user, pass));
        return clientHttpRequestFactory;
    }

    public static HttpClient httpClient(String user, String pass) {
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(user, pass));
        HttpClient client1 = HttpClientBuilder.create().setDefaultCredentialsProvider(credentialsProvider).build();
        return client1;
    }

    public static String colorRandom() {
        Random random = new Random();
        int nextInt = random.nextInt(0xffffff + 1);
        return String.format("#%06x", nextInt);
    }

    public static Integer convertirLetraAMes(String mes) {
        Integer mesNumerito;
        switch (mes) {
            case "Ene":
            case "ENERO":
                mesNumerito = 1;
                break;
            case "Feb":
            case "FEBRERO":
                mesNumerito = 2;
                break;
            case "Mar":
            case "MARZO":
                mesNumerito = 3;
                break;
            case "Abr":
            case "ABRIL":
                mesNumerito = 4;
                break;
            case "May":
            case "MAYO":
                mesNumerito = 5;
                break;
            case "Jun":
            case "JUNIO":
                mesNumerito = 6;
                break;
            case "Jul":
            case "JULIO":
                mesNumerito = 7;
                break;
            case "Ago":
            case "AGOSTO":
                mesNumerito = 8;
                break;
            case "Sep":
            case "SEPTIEMBRE":
                mesNumerito = 9;
                break;
            case "Oct":
            case "OCTUBRE":
                mesNumerito = 10;
                break;
            case "Nov":
            case "NOVIEMBRE":
                mesNumerito = 11;
                break;
            default:
                mesNumerito = 12;
        }
        return mesNumerito;
    }

    public static boolean isEmptyString(String l) {
        return l == null || l.isEmpty();
    }

    public static boolean isNotEmptyString(String l) {
        return !Utils.isEmptyString(l);
    }

    public static void unirPdf(List<File> archivos, String nombreUnido) {
        try {
            PDFMergerUtility pdfmerger = new PDFMergerUtility();
            for (File f : archivos) {
                PDDocument doc = PDDocument.load(f, MemoryUsageSetting.setupTempFileOnly());
                pdfmerger.setDestinationFileName(nombreUnido);
                pdfmerger.addSource(f);
                doc.close();
            }
            pdfmerger.mergeDocuments(MemoryUsageSetting.setupMainMemoryOnly());
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    /*public static void convertPDFtoTIFF(String archivoPDF, String fileTIFF) throws Exception {
        try {
            try (PDDocument document = PDDocument.load(new File(archivoPDF))) {
                BufferedImage[] images = new BufferedImage[document.getNumberOfPages()];
                PDFRenderer pdfRenderer = new PDFRenderer(document);
                for (int page = 0; page < document.getNumberOfPages(); ++page) {
                    BufferedImage bim = pdfRenderer.renderImageWithDPI(page, 200, ImageType.RGB);
                    images[page] = bim;
                }
                try (FileOutputStream fos = new FileOutputStream(fileTIFF);
                        RandomAccessOutputStream rout = new FileCacheRandomAccessOutputStream(fos)) {
                    ImageParam.ImageParamBuilder builder = ImageParam.getBuilder();
                    ImageParam[] param = new ImageParam[1];
                    TIFFOptions tiffOptions = new TIFFOptions();
                    //tiffOptions.setTiffCompression(TiffFieldEnum.Compression.LZW);
                    tiffOptions.setTiffCompression(TiffFieldEnum.Compression.DEFLATE);
                    builder.imageOptions(tiffOptions);
                    //builder.colorType(ImageColorType.FULL_COLOR).ditherMatrix(DitherMatrix.getBayer8x8Diag()).applyDither(true).ditherMethod(DitherMethod.BAYER);
                    builder.colorType(ImageColorType.GRAY_SCALE).ditherMatrix(DitherMatrix.getBayer8x8Diag()).applyDither(true).ditherMethod(DitherMethod.BAYER);
                    param[0] = builder.build();
                    TIFFTweaker.writeMultipageTIFF(rout, images, param);
                }
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }*/
    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    public static String quitarPleca(String value) {
        return value.replace("|", "");
    }

    public static boolean filter(FacesContext context, Collection<FilterMeta> filterBy, Object o) {
        boolean matching = true;

        for (FilterMeta filter : filterBy) {
            FilterConstraint constraint = filter.getConstraint();
            Object filterValue = filter.getFilterValue();

            try {
                Object columnValue = String.valueOf(ReflexionEntity.getCampo(o, filter.getField()));
                matching = constraint.isMatching(context, columnValue, filterValue, LocaleUtils.getCurrentLocale());
            } catch (Exception e) {
                matching = false;
            }

            if (!matching) {
                break;
            }
        }

        return matching;
    }

    public static File copyFileServerHidden(UploadedFile uFile, String directory, String fileName) throws IOException {
        try {
            Path path = Paths.get(directory);

            Files.createDirectories(path);
            File file = new File(directory + File.separator + fileName);
            try (InputStream is = uFile.getInputStream(); OutputStream out = new FileOutputStream(file)) {
                byte buf[] = new byte[2048];
                int len;
                while ((len = is.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            }
            path = Paths.get(directory + File.separator + fileName);
            Files.setAttribute(path, "dos:hidden", true);
            return path.toFile();
        } catch (IOException e) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, "Copiar Archivo al servidor", e);
        }
        return null;
    }

    public static Integer[] getFontPosition(String filePath, final String palabra) throws IOException {
        final Integer[] result = new Integer[3];
        File f = new File(filePath);
        try {
            com.itextpdf.text.pdf.PdfReader pdfReader = new com.itextpdf.text.pdf.PdfReader(filePath);
            Integer pagina = pdfReader.getNumberOfPages();
            new PdfReaderContentParser(pdfReader).processContent(pagina, new RenderListener() {
                public void beginTextBlock() {
                }

                public void renderText(TextRenderInfo textRenderInfo) {
                    String text = textRenderInfo.getText();
                    if (text != null && text.contains(palabra)) {
                        com.itextpdf.awt.geom.Rectangle2D.Float textFloat = textRenderInfo.getBaseline().getBoundingRectange();
                        float x = textFloat.x;
                        float y = textFloat.y;
                        result[0] = (int) x;
                        result[1] = (int) y - 10;
                    }
                }

                public void endTextBlock() {

                }

                public void renderImage(ImageRenderInfo renderInfo) {

                }
            });
            result[2] = pagina;
            pdfReader.close();
            // System.out.println(f.getAbsolutePath());
            //boolean del = f.delete();
            //System.out.println("delete: " + del);
        } catch (Exception e) {
            result[0] = 70;
            result[1] = 70;
            result[2] = 1;
        }
        return result;
    }

    protected Date cvtToGmt(Date date) {
        TimeZone tz = TimeZone.getDefault();
        Date ret = new Date(date.getTime() - tz.getRawOffset());

        // if we are now in DST, back off by the delta.  Note that we are checking the GMT date, this is the KEY.
        if (tz.inDaylightTime(ret)) {
            Date dstDate = new Date(ret.getTime() - tz.getDSTSavings());

            // check to make sure we have not crossed back into standard time
            // this happens when we are on the cusp of DST (7pm the day before the change for PDT)
            if (tz.inDaylightTime(dstDate)) {
                ret = dstDate;
            }
        }
        return ret;
    }

    public static String getDate(Date fecha) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        return dateFormat.format(fecha);
    }

    public static String getUrlViewFile(String archivo) {
        return SisVars.urlOrigamiMedia + "resource/pdf/" + archivo + "/descarga/" + SisVars.VIEW_DOC;
    }

    public static String getUrlViewImage(String archivo) {
        return SisVars.urlOrigamiMedia + "resource/image/" + archivo;
    }

    public static String getUrlDownloadFile(String archivo) {
        String[] tipo = archivo.split("\\.");
        String lastOne = tipo[tipo.length - 1];
        String ruta;
        switch (lastOne) {
            case "pdf":
                ruta = SisVars.urlOrigamiMedia + "resource/pdf/" + archivo + "/descarga/"
                        + SisVars.DOWNLOAD_DOC;
                break;
            case "xlsx":
            case "xls":
                ruta = SisVars.urlOrigamiMedia + "resource/xlxs/" + archivo;
                break;
            case "docs":
            case "docx":
            case "doc":
            case "txt":
            case "csv":
                ruta = SisVars.urlOrigamiMedia + "resource/docs/" + archivo + "/descarga/" + SisVars.DOWNLOAD_DOC;
                break;
            case "tiff":
            case "bmp":
            case "wmf":
            case "dib":
            case "gif":
            case "jpg":
                ruta = SisVars.urlOrigamiMedia + "resource/descargarImage/" + archivo;
                break;
            default:
                ruta = SisVars.urlOrigamiMedia + "resource/image/" + archivo;
                break;
        }
        return ruta;
    }

    public static String cantidadAletras(BigDecimal cantidad) {
        String temporal = cantidad.toString();
        NumeroLetra n = new NumeroLetra();
        return n.Convertir(temporal, true) + " CON " + temporal.substring(temporal.indexOf('.') + 1) + "/100";
    }

}
