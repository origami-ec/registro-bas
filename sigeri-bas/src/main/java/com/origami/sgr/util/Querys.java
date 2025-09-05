/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.util;

/**
 *
 * @author Origami
 */
public class Querys {

    /* CONSULTAS PARA ACLSERVICE */
    public static String getUsuariobyUserPass = "select e from AclUser e where e.usuario = :user and e.pass = :pass and e.sisEnabled=true";
    public static String getUsuariobyUserClave = "select e from AclUser e where e.usuario = :user and e.clave = :clave and e.sisEnabled=true";
    public static String getAclUserByCiRucEnte = "select u from AclUser u where u.ente.ciRuc = :id";
    public static String getAclUserByUser = "select u from AclUser u where u.usuario = :user";
    public static String getAclUserByID = "select u from AclUser u where u.id = :id";
    public static String getUsuarios = "select u from AclUser u where u.sisEnabled is true and u.ente is not null order by u.usuario";
    public static String getDigitalizadorByTipoTramite = "select u from ParametrosDisparador u where u.tipoTramite = :tipoTramite and u.varResp = :var";
    public static String getMsgNotificacionByTipo = "select m from MsgFormatoNotificacion m where m.tipo.id = :tipo and m.estado=1";
    public static String getMsgNotificacion = "select m from MsgFormatoNotificacion m where m.tipo.descripcion = :tipo and m.estado = 1";
    public static String getListEnteByCorreo = "select e.ente from EnteCorreo e where e.email = :email";
    public static String getListEnteByTelefono = "select e.ente from EnteTelefono e where e.telefono = :telefono";
    public static String getEnteByIdent = "select e from CatEnte e where e.ciRuc = :ciRuc";
    public static String getPersonaByCi = "select e from CatEnte e where e.ciRuc = :ciRuc and e.esPersona = :persona";
    public static String getAclRolByEstado = "select a from AclRol a where a.estado = :estado order by a.nombre";
    public static String getAclRolAsignacion = "select a from AclRol a where a.esSubDirector = :estado order by a.nombre";

    public static String getAclRolByIdDirector = "select a from AclRol a where a.estado = true and a.id = :idRol and a.isDirector = true";
    public static String getAclRolByNombre = "select e from AclRol e where e.nombre=:nombre and e.estado = true";
    public static String getListAclRolByDep = "select e from AclRol e where e.departamento.id=:departamento and e.estado = true ORDER BY e.nombre ASC";
    public static String getListAclRolByDepNull = "select e from AclRol e where e.departamento IS NULL and e.estado = true ORDER BY e.nombre ASC";
    public static String getPropietariosByCiOrRUC = "select e from CatPredioPropietario e where e.ente.ciRuc = :ciRuc";
    public static String getGeDepartamentoByEstado = "select a from GeDepartamento a where a.estado = :estado";
    public static String getGeDepartamentos = "select a from GeDepartamento a where a.estado is true";
    public static String getRegCatPapelByPapel = "select pa from RegPapel pa where lower(pa.papel) like :papel ORDER BY pa.papel ASC";
    public static String getHistoricoTramiteById = "SELECT ht FROM HistoricoTramites ht WHERE ht.id = :id";
    public static String getHistoricoTramiteByIdNew = "SELECT ht FROM HistoricoTramites ht WHERE ht.idTramite = :id";
    public static String getObservacionesActivasHistoricoTramites = "select ob from Observaciones ob where ob.idTramite = :idTramite and ob.estado = :estado order by ob.fecCre";
    public static String getObservacionesByIdTramite = "select ob from Observaciones ob where ob.idTramite = :idTramite and tarea like 'Analisis Registral' ";
    public static String getVuItemsByCatalogo = "SELECT v FROM VuItems v WHERE v.catalogo.nombre = :nombre";
    public static String getCtlgItemList = "Select e from CtlgItem e";
    public static String getCantonList = "Select e from CatCanton e";
    public static String getCatCategoriasPropConstruccionList = "Select e from CatEdfCategProp e ORDER BY e.guiOrden ASC";
    public static String getTipoDominioList = "Select e from CatTiposDominio e";
    public static String getCtlgItemListByNombreDeCatalogo = "Select e from CtlgItem e Where e.catalogo.nombre=:catalogo ORDER BY e.valor ASC";
    public static String getCatCantonById = "SELECT c FROM CatCanton c WHERE c.id = :id";
    public static String getMatFormulaByTipoTramiteID = "SELECT c FROM MatFormulaTramite c WHERE c.tipoTramite = :idTipoTramite AND c.estado = true";
    public static String getCatEdfPropListByID = "SELECT c FROM CatEdfProp c WHERE c.id = :id";
    public static String getCatEntexEstado = "select e from CatEnte e where e.estado = :estado";
    public static String getUsersEntes = "select e from AclUser e where e.ente is not null";
    public static String getUsuariosActivos = "select e from AclUser e where e.ente is not null and e.sisEnabled is true";
    public static String getUsersEntesDirectores = "select e from AclUser e where e.ente is not null and e.userIsDirector = true";
    public static String getEntexCodSac = "select e from CatEnte e where e.codUsuario = :codUsuario";
    public static String CtlgTipocargoOrderByNombre = "SELECT tp FROM CtlgCargo tp where tp.estado = true order by tp.nombre";
    public static String CtlgTipoParticipacionOrderByNombre = "SELECT tp FROM CtlgTipoParticipacion tp where tp.estado = true order by tp.nombre";

    /* CONSULTAS PARA VENTANILLAPUBLICA */
    public static String getRpubUsusariobyUserPass = "select u from RpubUsuario u where u.numeroIdentificador = :user and u.contrasena = :pass and u.cuentaActivada = true";
    public static String getRpubUsuarioById = "select u from RpubUsuario u where u.id = :id";
    public static String getRpubUsuarioByCedRuc = "select u from RpubUsuario u where u.numeroIdentificador = :cedruc";

    /*CONSULTAS TRAMITES*/
    public static String getHistoricProceduresByProcId = "SELECT ht FROM HistoricoTramites ht Where ht.idProceso = :idprocess";
    public static String getHTByProcIdAndFecha = "SELECT ht FROM HistoricoTramites ht Where ht.idProceso = :idprocess and (ht.blocked is false or ht.fechaEntrega < :entrega)";
    public static String getHistoricProceduresByProcIdTemp = "SELECT ht FROM HistoricoTramites ht Where ht.idProcesoTemp = :idprocess";
    public static String getProcedureNumberById = "SELECT d.numTramiteRp FROM RegpLiquidacionDerechosAranceles d WHERE d.historicTramite.id = :tramite";
    public static String getGeTipoTramites = "select e from GeTipoTramite e";
    public static String getGeTipoTramitesByState = "select e from GeTipoTramite e where e.estado = :estado";
    public static String getGeTipoTramiteById = "select u from GeTipoTramite u where u.tipoProceso.id = :id and u.estado = true order by u.descripcion asc";
    public static String getGeTipoTramitesByActKey_State = "select e from GeTipoTramite e where e.estado = :estado and e.activitykey = :key";
    public static String getGeTipoTramiteByTipoProc = "select e from GeTipoTramite e where e.tipoProceso.id = :tipo";
    public static String getPeFirmas = "select e from PeFirma e where e.estado = :estado";
    public static String getPeFirmaByID = "select e from PeFirma e where e.id = :id";
    public static String getHistoricoTramitesByEnte = "select e from HistoricoTramites e where e.solicitante = :persona";
    public static String getOtrosTramites = "select e from OtrosTramites e where e.estado = 'A' ORDER BY e.tipoTramite ASC";
    public static String getTipoTramitexAbreviatura = "select e from GeTipoTramite e where e.abreviatura = :abreviatura";

    /*CONSULTAS CATASTRO*/
    public static String getPredioByNum = "SELECT cp1 FROM CatPredio cp1 WHERE cp1.numPredio = :numPredio AND cp1.estado='A'";
    public static String getPredioByNumPredio = "SELECT cp1 FROM CatPredio cp1 WHERE cp1.numPredio = :numPredio";
    public static String getPredioHijosByFatherID = "SELECT cp1 FROM CatPredio cp1 WHERE cp1.predioRaiz = :numPredio AND cp1.estado='T'";
    public static String getPredioByCod = "SELECT cp1 FROM CatPredio cp1 WHERE cp1.sector=:sectorp AND cp1.mz=:mzp AND cp1.cdla=:cdlap AND cp1.mzdiv=:mzdivp AND cp1.solar=:solarp AND cp1.div1=:div1p AND cp1.div2=:div2p AND cp1.div3=:div3p AND cp1.div4=:div4p AND cp1.div5=:div5p AND cp1.div6=:div6p AND cp1.div7=:div7p AND cp1.div8=:div8p AND cp1.div9=:div9p AND cp1.phh=:phhp AND cp1.phv=:phvp";
    public static String getPredioByCodPredByEstado = "SELECT cp1 FROM CatPredio cp1 WHERE cp1.sector=:sectorp AND cp1.mz=:mzp AND cp1.cdla=:cdlap AND cp1.mzdiv=:mzdivp AND cp1.solar=:solarp AND cp1.div1=:div1p AND cp1.div2=:div2p AND cp1.div3=:div3p AND cp1.div4=:div4p AND cp1.div5=:div5p AND cp1.div6=:div6p AND cp1.div7=:div7p AND cp1.div8=:div8p AND cp1.div9=:div9p AND cp1.phh=:phhp AND cp1.phv=:phvp AND cp1.estado = :estado";
    public static String getPrediosActivos = "SELECT cp1 FROM CatPredio cp1 WHERE cp1.estado='A'";
    public static String getMaxCatPredio = "SELECT MAX(cp.numPredio) FROM CatPredio cp ";
    public static String getCatPropiedadItemByOrden = "select e from CatPropiedadItem e where e.orden = :orden";
    public static String gerMaxCertificados = "select max(e.numCert+1) from CatCertificadoAvaluo e ";
    public static String getParametrosValPredios = "select e from ParametrosValoracionPredio where e.prefijo = :prefijo and e.estado = true";
    public static String getIdPredioByNumPredio = "SELECT c.id FROM CatPredio c WHERE c.numPredio = :numPredio";
    public static String getPrediosByPropietario = "select p.predio from CatPredioPropietario p where p.ente = :idEnte";
    public static String CatEnteCount = "SELECT COUNT(c) FROM CatEnte c WHERE c.ciRuc = :ciRuc";
    public static String getMovimientoIds = "SELECT m.id FROM RegMovimiento m WHERE (CAST(m.fechaInscripcion AS date) "
            + "BETWEEN :fechaInscripcionDesde AND :fechaInscripcionHasta) AND m.userCreador = :user";
    public static String getMovimientoIds1 = "SELECT m.id FROM RegMovimiento m WHERE (CAST(m.fechaInscripcion AS date) "
            + "BETWEEN :fechaInscripcionDesde AND :fechaInscripcionHasta)";

    public static String enteTieneTramite(Boolean tipo) {
        StringBuilder sql = new StringBuilder("SELECT a.nombre, l.num_tramite_rp, l.fecha_ingreso, rl.tipo ");
        sql.append("FROM flow.regp_liquidacion l \n");
        sql.append("INNER JOIN flow.regp_liquidacion_detalles ld ON ld.liquidacion = l.id \n");
        sql.append("INNER JOIN flow.regp_tareas_tramite tt ON tt.detalle = ld.id \n");
        sql.append("INNER JOIN flow.tareas_activas ta ON ta.id = tt.tramite \n");
        sql.append("INNER JOIN app.reg_movimiento m ON m.num_tramite = tt.id \n");
        sql.append("INNER JOIN app.reg_movimiento_cliente mc ON mc.movimiento = m.id\n");
        sql.append("INNER JOIN app.reg_ente_interviniente ei ON ei.id = mc.ente_interv\n");
        sql.append("INNER JOIN app.reg_acto a ON a.id = m.acto \n");
        sql.append("INNER JOIN app.reg_libro rl ON rl.id = a.libro ");
        sql.append("WHERE ei.ced_ruc = ? ");
        if (tipo) {
            sql.append("AND rl.tipo = ? ");
        }
        sql.append("ORDER BY l.fecha_ingreso ASC");
        return sql.toString();
    }

    public static String getValorCiudadela(int periodo) {
        return "select e from CatValoresCiudadela e where e.ciudadela.id = :ciudadela and " + periodo + " between e.anioDesde and e.anioHasta";
    }

    public static String getValorCategoriaConst(int periodo) {
        return "select e from CatPredioEdificacion e inner join e.categoria.catCategoriasConstruccionValoresCollection cc where e.predio.id = :predio and " + periodo + " between cc.anioDesde and cc.anioHasta";
    }

    /*ACL MENU DEL TEMPLATE*/
    public static String getMenuBar = "SELECT mb1 FROM PubGuiMenubar mb1 WHERE mb1.identificador = :ident";
    public static String getMenusOrdenadosPadre = "SELECT m1 FROM PubGuiMenu m1 WHERE m1.menuPadre = :menuPadre ORDER BY m1.numPosicion ASC";
    public static String getMenusOrdenadosBar = "SELECT m1 FROM PubGuiMenu m1 WHERE m1.menubar = :menuBar ORDER BY m1.numPosicion ASC ";

    // Otros tipos de tramites
    public static String getTipoOtrosTramitesByIdentificacion = "SELECT mb1 FROM TipoOtrosTramites mb1 WHERE mb1.identificacion = :ident";
    public static String getBaseCalculoOT = "SELECT e FROM BaseCalculoOtrosTramites e ORDER BY e.id asc";

    // Permiso Contruccion
    public static String getListRequisitosTipoTramitesByTipTra = "SELECT rq FROM GeRequisitosTipoTramite rq WHERE rq.tipoTramite.id = :tipo";
    public static String getCatEdfPropList = "SELECT cp1 FROM CatEdfProp cp1 WHERE cp1.categoria.id=:idCateg ORDER BY cp1.nombre ASC";
    public static String getCatEdfPropListByNom = "SELECT cp1 FROM CatEdfProp cp1 WHERE cp1.categoria.nombre=:nombreCateg ORDER BY cp1.nombre ASC";
    public static String getCatEdfPropListByCategoria = "SELECT cp1 FROM CatEdfProp cp1 WHERE cp1.categoria=:categ ORDER BY cp1.nombre ASC";
    public static String getPeUnidadMedidaList = "Select tp from PeUnidadMedida tp";

    // PeTipoPermiso
    public static String getTipoPermisoDes = "SELECT tp FROM PeTipoPermiso tp WHERE tp.descripcion = :des";
    public static String getPeTipoPermisoCodigo = "SELECT tp1 FROM PeTipoPermiso tp1 where tp1.codigo in ('AR','CN','RN','RM','RP')";
    public static String getPeTipoPermisoCodigoAnt = "SELECT tp1 FROM PeTipoPermiso tp1 where tp1.codigo = :codigo";

    //PePermiso
    public static String getPePermisoByEstado = "Select permiso From PePermiso permiso Where permiso.estado = :estado AND permiso.idPredio != null";
    /**
     * SELECT MAX(p2.numReporte) FROM PePermiso p2 WHERE p2.anioPermiso =
     * :anioPermiso
     */
    public static String getNumerosReportes = "SELECT MAX(p2.numReporte) FROM PePermiso p2 WHERE p2.anioPermiso = :anioPermiso";
    public static String getCountNumerosReportes = "SELECT COUNT(p2.numReporte) FROM PePermiso p2 WHERE p2.anioPermiso = :anioPermiso";
    public static String getPePermisoByNumTra = "SELECT p FROM PePermiso p WHERE p.tramite = :numTramite";
    public static String getPePermisoAntiguoByEstado = "SELECT p.id FROM PePermiso p WHERE p.numReporte = :numReporte AND p.anioPermiso = :anio AND p.estado = :estado";
    public static String getPePermisoById = "SELECT p FROM PePermiso p WHERE p.id = :id";
    public static String getPeInspeccionFinalById = "SELECT p FROM PeInspeccionFinal p WHERE p.id = :id";
    public static String getPeInspeccionByPermisoID = "SELECT p FROM PeInspeccionFinal p WHERE p.numPermisoConstruc = :idPermiso";
    public static String getPeInspeccionList = "SELECT p FROM PeInspeccionFinal p ";
    public static String getPePermisoCabEdificacionByPePermisoID = "SELECT p FROM PePermisoCabEdificacion p WHERE p.idPermiso = :permisoId AND p.estado=TRUE";
    public static String getCatEdificacionesByPredio = "SELECT p FROM CatPredioEdificacion p WHERE p.predio = :predioId AND p.estado = TRUE";
    public static String getPePermisosAdicionalesByTramiteID = "SELECT p FROM PePermisosAdicionales p WHERE p.numTramite = :tramiteId";
    public static String getValorOrdenanzaNew = "SELECT p FROM CatEdifInspeccionValores p WHERE p.id = :tipoId and :numPisos BETWEEN p.desdeNumPisos AND p.hastaNumPisos AND p.estado = true";

    //REGISTRO PROPIEDAD
    public static String getListRegpActosInscripByNom = "SELECT ac FROM RegpActosIngreso ac WHERE ac.tipoActo.id = :idTipo AND UPPER(ac.nombre) LIKE :nombre ORDER BY ac.nombre ASC";
    public static String getListRegpActosCertifByNom = "SELECT ac FROM RegpActosIngreso ac WHERE ac.tipoActo.id = :idTipo AND UPPER(ac.nombre) LIKE :nombre ORDER BY ac.nombre ASC";
    public static String getVuCatalogoByNombre = "SELECT v FROM VuCatalogo v WHERE v.nombre = :nombre";
    public static String getNumeroTramiteRegistro = "SELECT MAX(r.numTramiteRp) FROM RegpLiquidacionDerechosAranceles r where to_char(r.fecha, 'yyyy') = :anio";

    public static String getRegEnteIntervinienteByCedRuc = "select e from RegEnteInterviniente e where e.cedRuc=:cedula order by e.id DESC";
    public static String getRegEnteIntervinienteByCedRucByNombreByTipo = "select e from RegEnteInterviniente e where e.cedRuc=:cedula and e.nombre=:nombre and e.tipoInterv=:tipoInterv";
    public static String getRegIntervByCedRucByNombre = "select e from RegEnteInterviniente e where e.cedRuc = :cedula and e.nombre = :nombre";
    public static String getRegIntervByNombre = "select e from RegEnteInterviniente e where e.nombre = :nombre";
    public static String getRegIntervByCedRuc = "select e from RegEnteInterviniente e where e.cedRuc = :cedula";

    public static String getCtlgCatalogos = "SELECT c FROM CtlgCatalogo c ORDER BY c.nombre ASC";
    public static String getCtlgItemaASC = "SELECT i1 FROM CtlgItem i1 WHERE i1.catalogo.nombre=:catalogo ORDER BY i1.valor ASC";
    public static String getCtlgItemaDESC = "SELECT i1 FROM CtlgItem i1 WHERE i1.catalogo.nombre=:catalogo ORDER BY i1.valor DESC";
    public static String getCtlgItemByCatalogoValor = "select e from CtlgItem e where e.catalogo.nombre = :catalogo and e.valor = :valor";
    public static String getCtlgItemByCatalogoCodeName = "select e from CtlgItem e where e.catalogo.nombre = :catalogo and e.codename = :codename";
    public static String getCltgCargos = "Select c from CtlgCargo c WHERE c.estado=true";
    public static String CtlgTipoParticipacionOrberByNombre = "SELECT tp FROM CtlgTipoParticipacion tp where tp.estado=true order by tp.nombre";
    public static String getRegCapital = "Select c from RegCapital c where c.estado=true";
    public static String getUserConMenosTareas = "Select u from UserConTareas u where u.estado='A' and u.rolUser=:codigo and u.peso in (select min(t.peso) from UserConTareas t where t.estado='A' and t.rolUser=:codigorol)";
    public static String getUserTareasByUser = "Select u from UserConTareas u where u.username = :user";

    public static String getRegMovimientoReferenciaByIdMov = "select r from RegMovimientoReferencia r where r.movimiento=:idmov";
    public static String getRegDomicilioList = "Select do from RegDomicilio do where do.estado=true ORDER BY do.nombre ASC";
    public static String getRegEstadosInscripcion = "Select do from RegEstadoMovimiento do where do.estado='A' ORDER BY do.id";
    public static String getCatParroquiaList = "Select pa from CatParroquia pa where pa.estado = true ORDER BY pa.descripcion ASC";
    public static String getBarriosList = "Select ba from Barrios ba where ba.estado = true ORDER BY ba.nombre";
    public static String getRegLibroListall = "Select lb from RegLibro lb where lb.estado is true ORDER BY lb.nombre ASC";
    public static String getRegLibros = "Select lb from RegLibro lb ORDER BY lb.nombre ASC";

    public static String getCtlgItemListCargos = "Select c from CtlgItem c where c.catalogo.nombre = 'registro.cargo_representante' ORDER BY c.valor ASC";
    public static String getCtlgItemListUsosDocs = "Select c from CtlgItem c where c.catalogo.nombre = 'proforma.uso_documento' and c.estado = 'A' ORDER BY c.valor ASC";
    public static String getCtlgItemListEstudiosJuridicoa = "Select c from CtlgItem c where c.catalogo.nombre = 'proforma.estudio_juridico' ORDER BY c.valor ASC";
    public static String getCtlgItemUnidades = "Select c from CtlgItem c where c.catalogo.nombre = 'ficha.unidades_medida' ORDER BY c.valor ASC";
    public static String getCtlgItemCertificados = "Select c from CtlgItem c where c.catalogo.nombre = 'registro.tipo_certificado' ORDER BY c.id ASC";
    public static String getRegTipoFicha = "Select tf from RegTipoFicha tf order by tf.id";

    public static String getRegAllLibros = "Select lb from RegLibro lb ORDER BY lb.nombre ASC";
    public static String getRegLibroList = "Select lb from RegLibro lb where lb.estado is true ORDER BY lb.nombre ASC";
    public static String getRegLibroListOrd = "Select lb from RegLibro lb where lb.estado is true ORDER BY lb.propiedad, lb.nombre";
    public static String getRegActoList = "Select a from RegActo a where a.estado=true and a.id not in (114, 121) ORDER BY a.nombre ASC";
    public static String getRegActosPropiedad = "Select a from RegActo a where a.estado is true AND a.tipoCobro.id = 1 ORDER BY a.nombre ASC";
    public static String getRegActosMercantil = "Select a from RegActo a where a.estado is true AND a.tipoCobro.id = 2 ORDER BY a.nombre ASC";
    public static String getMaxNumRepertRegMovimiento = "Select max(m.num_repertorio) FROM app.reg_movimiento m where m.is_mercantil = ? and to_char(m.fecha_ingreso,'YYYY')= '?'";
    public static String getRegFichaByCodPredial = "Select f from RegFicha f where f.codigoPredial=:cadena and f.tipo.id=:tipo";
    public static String getRegMovimientosPorLibroAnio = "SELECT m FROM RegMovimiento m WHERE m.estado='AC' and m.libro.id=:libroid and to_char(m.fechaInscripcion, 'yyyy') =:anio ORDER BY m.fechaInscripcion, m.numInscripcion";
    public static String getRegMovimientoById = "SELECT m FROM RegMovimiento m WHERE m.id=:movId";
    public static String getCantRegFichaByMovTramiteTaskID = "SELECT COUNT(mf) FROM RegMovimientoFicha mf WHERE mf.movimiento.numTramite = :numTramite and mf.movimiento.taskId = :taskId "
            + "and mf.movimiento.transferenciaDominio = true and mf.movimiento.estado = 'AC'";
    public static String getRegMovsFichaByMovTramiteTaskID = "SELECT mf FROM RegMovimientoFicha mf WHERE mf.movimiento.numTramite = :numTramite "
            + "and mf.movimiento.transferenciaDominio = true and mf.movimiento.estado = 'AC' ORDER BY mf.movimiento.fechaInscripcion";
    public static String getRegFichasByMovimientoId = "Select m.ficha from RegMovimientoFicha m WHERE m.movimiento.id=:idmov ORDER BY m.ficha.fechaApe DESC";
    public static String getRegFichasByPropietarioId = "Select m.ficha from RegFichaPropietarios m WHERE m.propietario.id=:prop ORDER BY m.ficha.fechaApe DESC";
    public static String getPropietariosByFichaId = "Select m.propietario from RegFichaPropietarios m WHERE m.ficha.id=:ficha ORDER BY m.propietario.nombre ASC";
    public static String getRegPropietariosFicha = "Select fp from RegFichaPropietarios fp where fp.ficha.id = :ficha and fp.estado = 'A'";
    public static String getRegMovMargByMovimientoId = "Select m from RegMovimientoMarginacion m WHERE m.movimiento.id=:idmov order by m.fechaIngreso";
    public static String getRegMovFichasByMovimientoId = "Select m from RegMovimientoFicha m WHERE m.movimiento.id=:idmov";
    public static String getRegMovCapitByMovimientoId = "Select m from RegMovimientoCapital m WHERE m.movimiento.id=:idmov";
    public static String getRegMovRepreByMovimientoId = "Select m from RegMovimientoRepresentante m WHERE m.movimiento.id=:idmov";
    public static String getRegMovSocioByMovimientoId = "Select m from RegMovimientoSocios m WHERE m.movimiento.id=:idmov";
    public static String getRegMovimientoClienteByMovimiento = "Select mc from RegMovimientoCliente mc where mc.movimiento.id=:movid ORDER BY mc.id ASC";
    public static String getRegRegMovimientoRepresentanteByMovimiento = "Select mr from RegMovimientoRepresentante mr where mr.movimiento.id=:movid";
    public static String getRegMovimientoSociosByMovimiento = "Select ms from RegMovimientoSocios ms where ms.movimiento.id=:movid";
    public static String getRegMovimientoCapitalByMovimiento = "Select mc from RegMovimientoCapital mc where mc.movimiento.id=:movid";
    public static String getEnteCorreoByEnteId = "select ec from EnteCorreo ec where ec.ente.id = :enteId";
    public static String getListIdMovsByIdInterv = "select mc.movimiento.id from RegMovimientoCliente as mc where mc.enteInterv.id = :codigo";
    public static String getListIdMovsByCedRucInterv = "select mc.movimiento.id from RegMovimientoCliente as mc "
            + "where mc.enteInterv.cedRuc = :codigo";
    public static String getListIdMovsByCedRucIntervTipoLibro = "select mc.movimiento.id from RegMovimientoCliente as mc "
            + "where mc.movimiento.fechaInscripcion is not null and mc.enteInterv.cedRuc = :codigo "
            + "and mc.movimiento.libro.tipo = :tipoLibro";
    public static String getListMovsByCedRucInterv = "select mc.movimiento from RegMovimientoCliente as mc where mc.enteInterv.cedRuc = :codigo and mc.movimiento.estado = 'IN'";
    public static String getListMovsByTramite = "select mc from RegMovimientoCliente as mc where mc.movimiento.tramite = :numero order by mc.movimiento.numRepertorio, mc.movimiento.numInscripcion";
    public static String getParametroDisparadorByTipoTramite = "select pd from ParametrosDisparador pd where pd.tipoTramite.id = :tipoTramite";
    public static String getListaRegpCertificadosInscripcionesByLiquidacionAndTipoTarea = "SELECT C FROM RegpCertificadosInscripciones C WHERE C.liquidacion.id=:liquidacion AND C.tipoTarea=:tipoTarea";
    public static String getRegCatPapelCollectionByActo = "select pa from app.reg_cat_papel pa left join app.reg_acto_has_papel acpa on(acpa.papel = pa.id) left join app.reg_acto ac on(ac.id = acpa.acto) where ac.id = ";
    public static String getListTipoCertificados = "select t from RegTipoCertificado t where t.estado = true";
    public static String getCertificadoByTarea = "select t from RegCertificado t where t.tareaTramite = :tarea";
    public static String getMaxNumCertificadoByTramite = "SELECT max(c.secuencia) FROM RegCertificado c WHERE c.numTramite = :tramite";
    public static String getCertificadoByNumCertif = "SELECT c FROM RegCertificado c WHERE c.numCertificado = :certificado";
    public static String getMaxNumCertificadoSine = "SELECT max(c.secuencia) FROM RegCertificado c WHERE c.numTramite is null and to_char(c.fechaEmision, 'yyyy') = :anio";
    public static String getCtlgCargo = "SELECT c FROM CtlgCargo c ORDER BY c.nombre ASC";
    public static String getPapelByMovimientoInterviniente = "select mc.papel.papel from RegMovimientoCliente as mc where mc.movimiento.id= :mov and mc.enteInterv.id = :inter";
    public static String getPapelByMovYDocInterv = "select mc.papel.papel from RegMovimientoCliente as mc where mc.movimiento.id= :mov and mc.enteInterv.cedRuc = :doc";
    public static String getLibroByCodigoAnterior = "select li.nombre from RegLibro li where li.codigoAnterior = :codigo";
    public static String getFechaInscripcionMenor = "SELECT MIN(m.fechaInscripcion) FROM RegMovimiento m";
    public static String getFechaInscripcionMayor = "SELECT MAX(m.fechaInscripcion) FROM RegMovimiento m";
    public static String getCantRegMovimientoByTramite = "select count(mov) from RegMovimiento mov where mov.estado = 'AC' and mov.numTramite = :tramite";
    public static String getRegMovimientoByTramite = "select mov from RegMovimiento mov where mov.estado = 'AC' and mov.tramite = :tramite";
    public static String getRegMovimientoEspecifico = "SELECT m FROM RegMovimiento m WHERE m.libro.id=:libro AND m.numInscripcion=:inscripcion AND m.numRepertorio=:repertorio AND to_date(to_char(m.fechaInscripcion,'YYYY/MM/DD'),'YYYY/MM/DD')=:fecha AND m.indice=:secuencia";
    public static String getRegMovimientoEspecificoMaximo = "SELECT MAX(m.indice) FROM RegMovimiento m WHERE m.libro.id=:libro AND m.numInscripcion=:inscripcion AND m.numRepertorio=:repertorio AND to_date(to_char(m.fechaInscripcion,'YYYY/MM/DD'),'YYYY/MM/DD')=:fecha";

    public static String getMovimientoByInscr = "Select m from RegMovimiento m where m.libro.id = :libro and m.numInscripcion = :inscripcion and m.indice = :indice and cast(to_char(m.fechaInscripcion, 'yyyy') as int) = :anio";
    public static String getMovimientoByInscRep = "Select m from RegMovimiento m where m.libro.id = :libro and m.numInscripcion = :inscripcion and m.numRepertorio = :repertorio and m.indice = :indice and cast(to_char(m.fechaInscripcion, 'yyyy') as int) = :anio";
    public static String getMovimientoByAnioInscRep = "Select m from RegMovimiento m where m.libro.id = :libro AND m.numInscripcion = :inscripcion AND cast(to_char(m.fechaInscripcion, 'yyyy') as int) = :anio";
    public static String getMovimientoByFecInscRep = "Select m from RegMovimiento m where m.libro.id = :libro AND m.numInscripcion = :inscripcion AND to_char(m.fechaInscripcion, 'YYYY-MM-DD') = :fechaInsc ";

    public static String getMovimientoByInscrSec = "Select m from RegMovimiento m where m.libro.id = :libro and m.numInscripcion = :inscripcion and m.secuenciaInscripcion = :inscripcionSec and m.indice = :indice and cast(to_char(m.fechaInscripcion, 'yyyy') as int) = :anio";
    public static String getMovimientoByInscRepSec = "Select m from RegMovimiento m where m.libro.id = :libro and m.numInscripcion = :inscripcion and m.secuenciaInscripcion = :inscripcionSec and m.numRepertorio = :repertorio and m.secuenciaRepertorio = :inscripcionSec and m.indice = :indice and cast(to_char(m.fechaInscripcion, 'yyyy') as int) = :anio";
    public static String getCorreoFromSolicitante = "Select rl.beneficiario.correo1 from RegpLiquidacion rl where rl.numTramiteRp = :numtramite";
    public static String getRegEnteJudicialByAbrev = "select e from RegEnteJudiciales e where e.abreviatura like :abrev";
    public static String getTodasTareasByLiq = "Select r from RegpCertificadosInscripciones r where r.liquidacion = :idLiq and r.estado = true and r.realizado = true order by r.fechaFin";
    public static String getTareasByLiquidacion = "select c from RegpCertificadosInscripciones c where c.liquidacion = :idLiq and c.tipoTarea = :tipo and c.estado = true";
    public static String getTareasDinardap = "select c from RegpCertificadosInscripciones c where c.tareaDinardap = :idTarea and c.tipoTarea = :tipo and c.estado = true";
    public static String getListRegRegistrador = "select r from RegRegistrador r order by id";
    public static String getRegRegistrador = "select r from RegRegistrador r where r.actual = true";
    public static String getJuridico = "select r from RegRegistrador r where r.firmaJuridico = true";
    public static String getRegRegistradorFD = "select r from RegRegistrador r where r.firmaElectronica = true";
    public static String getRegMantenimientoMenu = "select r from RegMantenimientoMenu r where r.estado = :estado and r.tipo = :tipo";
    public static String getRegpLiquidacionByFechaYTramite = "select r from RegpLiquidacionDerechosAranceles r where r.numTramiteRp = :numTramite and to_char(r.fecha, 'yyyy') = :fechaLiq";
    public static String getTotalPagarByNumComprebanteSAC = "SELECT TOTAL_PAGAR FROM dbo.RT_LIQUIDACION WHERE NUM_COMPROBANTE = ?";
    public static String getTipoCertificadoByTipoPlantilla = "select r from RegTipoCertificado r where r.tipoPlantilla = :tipo";
    public static String getExisteFichaTareaRegistro = "select c.id from RegpCertificadosInscripciones c where c.liquidacion = :idTarea and c.numFicha = :ficha";
    public static String getExisteFichaTareaDinardap = "select c.id from RegpCertificadosInscripciones c where c.tareaDinardap = :idTarea and c.numFicha = :ficha";
    public static String getHistoricoTramitesByTipoYFecha = "SELECT h FROM HistoricoTramites h WHERE h.tipoTramite.id = :tipo AND TO_CHAR(h.fecha, 'dd/MM/yyyy') = :fecha ORDER BY h.fecha";
    public static String getTramitesAsignadosCertificadosRp = "select ht.id_proceso, ht.nombre_propietario, liq.num_tramite_rp, ht.id, liq.fecha "
            + "from flow.regp_liquidacion_derechos_aranceles as liq left join flow.historico_tramites as ht on (ht.id_tramite = liq.historic_tramite) "
            + "where (liq.estado = 2 or liq.estado = 4) and to_char(liq.fecha, 'dd/MM/yyyy') = ? and liq.certificado = true and liq.inscripcion = false order by liq.fecha";
    public static String getTramitesAsignadosContratosRp = "select ht.id_proceso, ht.nombre_propietario, liq.num_tramite_rp, ht.id, liq.fecha "
            + "from flow.regp_liquidacion_derechos_aranceles as liq left join flow.historico_tramites as ht on (ht.id_tramite = liq.historic_tramite) "
            + "where (liq.estado = 2 or liq.estado = 4) and to_char(liq.fecha, 'dd/MM/yyyy') = ? and liq.inscripcion = true order by liq.fecha";
    public static String getRegPropietariosByFicha = "Select fp from RegFichaPropietarios fp where fp.ficha.id=:idficha and fp.estado = true";
    public static String getCatTransferenciaByTarea = "SELECT td FROM CatTransferenciaDominio td WHERE td.tareaRegistro = :idTarea";
    /*Esta es una consulta nativa y no se envian parametros de hibernate, sino que se concatena a la consulta,
     esto fue probado asi y funciona asi, favor de no molestar.*/
    public static String getListIdFichasByIdInterv = "select distinct mf.ficha from app.reg_movimiento_ficha mf inner join app.reg_movimiento_cliente mc on(mc.movimiento = mf.movimiento) where mc.ente_interv = ";
    public static String getRegInterviniente = "select e from RegEnteInterviniente e where e.cedRuc = :cedula and e.nombre = :nomb and e.tipoInterv = :tipointr";
    public static String getCantidadCertificadosRealizados = "select count(ce) from RegpCertificadosInscripciones ce where (ce.tipoTarea = 'CE' or ce.tipoTarea = 'DC' or ce.tipoTarea = 'DW') and ce.estado = true and ce.realizado = true and ce.fechaFin between "
            + "to_timestamp(:desde, 'dd-mm-yyyy') and to_timestamp(:hasta, 'dd-mm-yyyy')";
    public static String getCantSolicitantesCertLiq = "select count(distinct(ht.solicitante)) from flow.regp_liquidacion_derechos_aranceles as liq "
            + "left join flow.historico_tramites as ht on (ht.id_tramite = liq.historic_tramite) "
            + "where liq.certificado = true and liq.fecha between to_timestamp(?, 'dd-mm-yyyy') "
            + "and to_timestamp(?, 'dd-mm-yyyy')";
    public static String getCantSolicitantesCertSine = "select count(distinct(td.solicitante)) from flow.regp_tareas_dinardap as td "
            + "left join flow.regp_certificados_inscripciones as ce on (td.id = ce.tarea_dinardap) "
            + "where (ce.tipo_tarea = 'DC' or ce.tipo_tarea = 'DW') and td.fecha between to_timestamp(?, 'dd-mm-yyyy') "
            + "and to_timestamp(?, 'dd-mm-yyyy')";
    public static String getCantSolInscResolucion = "select count(distinct(ht.solicitante)) from app.reg_movimiento as m "
            + "inner join flow.regp_certificados_inscripciones as ce on (ce.id = m.regp_certificados_inscripciones) "
            + "inner join flow.regp_liquidacion_derechos_aranceles as lq on (lq.id = ce.liquidacion) "
            + "inner join flow.historico_tramites as ht on (ht.id_tramite = lq.historic_tramite) "
            + "where (m.acto = 1457 or m.acto = 1490) and m.estado = 'AC' "
            + "and lq.fecha between to_timestamp(?, 'dd-mm-yyyy') and to_timestamp(?, 'dd-mm-yyyy')";
    public static String getCantSolInscResolucionSine = "select count(distinct(td.solicitante)) from app.reg_movimiento as m "
            + "inner join flow.regp_certificados_inscripciones as ce on (ce.id = m.regp_certificados_inscripciones) "
            + "inner join flow.regp_tareas_dinardap as td on (td.id = ce.tarea_dinardap) "
            + "where (m.acto = 1457 or m.acto = 1490) and m.estado = 'AC' "
            + "and td.fecha between to_timestamp(?, 'dd-mm-yyyy') and to_timestamp(?, 'dd-mm-yyyy')";
    public static String getCantSolInscGravamen = "select count(distinct(ht.solicitante)) from app.reg_movimiento as m "
            + "inner join flow.regp_certificados_inscripciones as ce on (ce.id = m.regp_certificados_inscripciones) "
            + "inner join flow.regp_liquidacion_derechos_aranceles as lq on (lq.id = ce.liquidacion) "
            + "inner join flow.historico_tramites as ht on (ht.id_tramite = lq.historic_tramite) "
            + "where (m.acto <> 1457 and m.acto <> 1490) and m.escrit_juic_prov_resolucion is not null "
            + "and m.estado = 'AC' and lq.fecha between to_timestamp(?, 'dd-mm-yyyy') "
            + "and to_timestamp(?, 'dd-mm-yyyy')";
    public static String getCantSolInscGravamenSine = "select count(distinct(td.solicitante)) from app.reg_movimiento as m "
            + "inner join flow.regp_certificados_inscripciones as ce on (ce.id = m.regp_certificados_inscripciones) "
            + "inner join flow.regp_tareas_dinardap as td on (td.id = ce.tarea_dinardap) "
            + "where (m.acto <> 1457 and m.acto <> 1490) and m.estado = 'AC' and m.escrit_juic_prov_resolucion is not null "
            + "and td.fecha between to_timestamp(?, 'dd-mm-yyyy') and to_timestamp(?, 'dd-mm-yyyy')";
    public static String getCantSolInscPropiedad = "select count(distinct(ht.solicitante)) from app.reg_movimiento as m "
            + "inner join flow.regp_certificados_inscripciones as ce on (ce.id = m.regp_certificados_inscripciones) "
            + "inner join flow.regp_liquidacion_derechos_aranceles as lq on (lq.id = ce.liquidacion) "
            + "inner join flow.historico_tramites as ht on (ht.id_tramite = lq.historic_tramite) "
            + "where (m.acto <> 1457 and m.acto <> 1490) and m.escrit_juic_prov_resolucion is null "
            + "and m.estado = 'AC' and lq.fecha between to_timestamp(?, 'dd-mm-yyyy') "
            + "and to_timestamp(?, 'dd-mm-yyyy')";
    public static String getCantSolInscPropiedadSine = "select count(distinct(td.solicitante)) from app.reg_movimiento as m "
            + "inner join flow.regp_certificados_inscripciones as ce on (ce.id = m.regp_certificados_inscripciones) "
            + "inner join flow.regp_tareas_dinardap as td on (td.id = ce.tarea_dinardap) "
            + "where (m.acto <> 1457 and m.acto <> 1490) and m.estado = 'AC' "
            + "and m.escrit_juic_prov_resolucion is null and td.fecha "
            + "between to_timestamp(?, 'dd-mm-yyyy') and to_timestamp(?, 'dd-mm-yyyy')";
    /*Agrupa los actos ingresados por liquidacion obetern la suma de cantidades y valores*/
    //public static String getActosIngresadosAgrupadosPorLiquidacion = "SELECT acto,codigo_rubro_sac codsac,SUM(cantidad) cantidad,SUM(valor_total) valor FROM flow.regp_liquidacion_detalles d INNER JOIN flow.regp_actos_ingreso a ON(d.acto=a.id) WHERE liquidacion=:liquidacion GROUP BY acto,codigo_rubro_sac";
    public static String getActosIngresadosAgrupadosPorLiquidacion = "SELECT acto, rubro_liquidacion as rubro, codigo_rubro_sac as codsac, SUM(cantidad) as cantidad, SUM(valor_total) as valor "
            + "FROM flow.regp_liquidacion_detalles d INNER JOIN flow.regp_actos_ingreso a ON(d.acto=a.id) "
            + "WHERE liquidacion = :liquidacion GROUP BY acto, codigo_rubro_sac, rubro_liquidacion";
    /**
     * Este grupo de querys se usan para traer liquidaciones de la base
     * postgresql anterior smbcatas y pasarlas a sgm
     */
    public static String getHistoricoTramiteAntiguo = "select per.ci, per.nombres, per.apellidos, ht.id_tramite, ht.fecha, ht.nombre_propietario, ht.correos, ht.telefonos, "
            + "ht.num_liquidacion, us.usuario from flow.historico_tramites ht left join app.cat_ente_persona per on(per.ente=ht.solicitante) left join app.acl_user us on(us.id=ht.user_creador) where ht.id = ?";
    public static String getLiquidacionBaseAnterior = "select liq.id, liq.tasa_catastro, liq.inf_adicional, liq.num_tramite_rp, liq.parentesco_solicitante, liq.cantidad_tasas_catastro, liq.total_pagar, "
            + "liq.is_exoneracion from flow.regp_liquidacion_derechos_aranceles liq  left join flow.historico_tramites ht on(ht.id_tramite=liq.historic_tramite) "
            + "where ht.id = ?";
    public static String getDetallesLiquidacionAnterior = "select tipo_acto, num_valor, valor_pagar, cuantia, avaluo, observacion from flow.regp_tramitesingresados_has_tipotramite where liquidacion_derec_aranceles = ?";
    public static String getIntervinientesLiquidacion = "select inte.papel, inte.es_beneficiario, ente.is_persona, "
            + "case ente.is_persona when true then per.ci else ente.ci_ruc end documento, "
            + "case ente.is_persona when true then per.nombres else ente.nombre end frist_name, "
            + "case ente.is_persona when true then per.apellidos else '' end second_name "
            + "from flow.regp_intervinientes inte left join app.cat_ente ente on(ente.id=inte.ente) "
            + "left  join app.cat_ente_persona per on(per.ente=ente.id) where inte.regp_liq_der_aranc_id = ?";

    public static String getHistoricoTramitexEstado = "select ht.id_tramite, (case when ce.is_persona = true then per.ci else ce.ci_ruc end) as ciruc, per.nombres, per.apellidos, ht.num_predio, ht.fecha, ht.nombre_propietario, ht.num_liquidacion, us.usuario, ce.nombre as razon_social, ce.is_persona, "
            + "(case when ce.is_persona = true then(select ec.email from app.ente_correo ec where ec.ente = per.ente limit 1) else ce.email_main end) as correo, "
            + "(case when ce.is_persona = true then(select ec.telefono from app.ente_telefono ec where ec.ente = per.ente limit 1) else ce.telefono_main end) as telefono, "
            + "ht.estado, ht.tipo_tramite "
            + "from flow.historico_tramites ht "
            + "left join app.cat_ente_persona per on(per.ente=ht.solicitante) "
            + "left join app.cat_ente ce on(ce.id = ht.solicitante) "
            + "left join app.acl_user us on(us.id=ht.user_creador) "
            + "where ht.id = ? and ht.estado in ('inactivo','pendiente')";

    public static String getPeInspeccionFotos = "select foto.imagen_nombre, foto.imagen_file, foto.inspeccion from app.pe_inspeccion_fotos as foto";
    public static String getPeInspeccionFotosByInspeccion = "select foto.imagen_nombre, foto.imagen_file, foto.inspeccion from app.pe_inspeccion_fotos as foto where foto.inspeccion = ?";
    //public static String getTramitexPredio = "";

    //SECUENCIAS
    public static String getGenSecByPeriodo = "select e from GeneSecuencia e where e.anio = :periodo";
    public static String getMaxRegEnteInterviniente = "select max(e.secuencia) from RegEnteInterviniente e where e.cedRuc = :cedula and e.tipoInterv = :tipointr";
    public static String MaxSecuenciaTipoTramite = "SELECT s FROM SecuenciaTramite s WHERE s.anio = :anio and s.tramiteDepartamento.id = :idTipoTramite";
    public static String MaxSecuenciaTipoLiquidacion = "SELECT s FROM RenSecuenciaNumLiquidicacion s WHERE s.anio = :anio and s.tipoLiquidacion = :idTipoLiquidacion";
    public static String MaxSecuenciaTipoLiquidacionSinAnio = "SELECT s FROM RenSecuenciaNumLiquidicacion s WHERE s.tipoLiquidacion.id = :idTipoLiquidacion";

    //REPORTES
    public static String getReporteByNombreTarea = "select e from HistoricoReporteTramite e where e.nombreTarea = :nombreTarea and e.proceso = :idProceso and e.estado = :estado";
    public static String getReporteByNombreTareaSinEstado = "select e from HistoricoReporteTramite e where e.proceso = :idProceso";
    public static String getHistoricoReporteTramiteByEstadoAndTramiteID = "select e from HistoricoReporteTramite e where e.tramite = :idTramite AND e.estado = TRUE";

    // RegFicha                                 Select f from RegFicha f where f.numFicha = :numFicha and f.tipo.id = :tipo
    public static String getRegFichaNumFacha = "Select f from RegFicha f where f.numFicha = :numFicha and f.tipo.id = :tipo";
    public static String getRegFichaNumFicha = "Select f from RegFicha f where f.numFicha = :numFicha ";
    public static String getRegFichaCodPredial = "Select f from RegFicha f where f.claveCatastral = :codigo";
    public static String getRegMovimientoFichaByFicha = "SELECT m FROM RegMovimientoFicha m where m.ficha.id = :idFicha ORDER BY m.movimiento.fechaInscripcion DESC, m.movimiento.numRepertorio DESC";
    public static String getMovimientosByIdFicha = "SELECT m.movimiento FROM RegMovimientoFicha m where m.ficha.id = :idFicha ORDER BY m.movimiento.fechaInscripcion DESC, m.movimiento.numRepertorio DESC";
    public static String getMarginacionesByIdFicha = "SELECT fm FROM RegFichaMarginacion fm where fm.ficha.id = :idFicha and fm.tramite.fechaIngreso is not null ORDER BY fm.tramite.numTramite";
    public static String getMovsByCertificado = "SELECT m FROM RegCertificadoMovimiento m where m.certificado.id = :id ORDER BY m.movimiento.fechaInscripcion DESC, m.movimiento.numRepertorio DESC";
    public static String getPropsByCertificado = "SELECT m FROM RegCertificadoPropietario m where m.certificado.id = :id ORDER BY m.nombres ASC";
    public static String getRegFichaNumPredio = "SELECT reg1 FROM RegFicha reg1 WHERE reg1.predio.id = :numPredio";
    public static String getRegFichaByEscrituraRural = "SELECT reg1 FROM RegFicha reg1 WHERE reg1.catEscrituraRural.id = :rural";
    public static String getRegFichaByPredio = "SELECT reg1 FROM RegFicha reg1 WHERE reg1.predio.id=:predio";
    public static String getRegFichaByLindero = "SELECT rf FROM RegFicha rf WHERE rf.linderos like :linderos AND rf.tipo.id=:tipo";
    public static String getMaxNumFichaByTipo = "SELECT max(reg1.numFicha) FROM RegFicha reg1 WHERE reg1.tipo.id=:tipo";
    public static String getRegFichaRangoNumFicha = "Select f from RegFicha f where f.numFicha between :numFichaInicial and :numFichaFinal and f.tipo.id = :tipo ORDER BY f.numFicha ASC";
    // CatParroquia, CatCiudadela, CatCanton
    public static String getCiudadelasByParroquia = "SELECT cp1 FROM CatCiudadela cp1 where cp1.codParroquia = :parroquia ORDER BY cp1.nombre ASC";
    public static String getParroquiaByCanton = "SELECT p FROM CatParroquia p WHERE p.idCanton=:idCanton";
    public static String getListNombresCdla = "SELECT cp1.nombre FROM CatCiudadela cp1 ORDER BY cp1.nombre ASC";
    public static String getCatCantonList = "SELECT cp1 FROM CatCanton cp1 ORDER BY cp1.nombre ASC";
    public static String getCatCiudadelaList = "Select e from CatCiudadela e ORDER BY e.nombre ASC";
    public static String getCatCiudadelaById = "SELECT c FROM CatCiudadela c WHERE c.id = :id";
    public static String getCatCiudadelaByCodigo = "SELECT c FROM CatCiudadela c WHERE c.codigo = :codigo";

    // CatEscrituraRural
    public static String getCatEscrituraRural = "SELECT reg1 FROM CatEscrituraRural reg1 WHERE reg1.registroCatastral=:regCatastral and reg1.identificacionPredial=:idPredial";

    //CatEscritura
    public static String getCatEscrituraByPredio = "SELECT e FROM CatEscritura e WHERE e.predio.id = :id and e.estado = 'A'";
    public static String getCatEscriturasPendientes = "SELECT cpe FROM CatEscritura cpe WHERE cpe.predio.id = :id AND cpe.estado='P'";

    //CatPredioPropietario
    public static String getCatPropietariosByPredio = "SELECT p FROM CatPredioPropietario p WHERE p.predio.id=:id AND p.estado = 'A'";
    public static String getCatPropietariosByEnte = "SELECT p FROM CatPredioPropietario p WHERE p.ente=:enteId AND p.estado = 'A'";

    // RegTipoBien
    public static String getRegTipoBienList = "SELECT tb FROM RegTipoBien tb WHERE tb.estado=:estado";

    //RegLibro
    public static String getRegLibroById = "SELECT l FROM RegLibro l WHERE l.id=:idlibro";

    // Los Movimiento
    public static String getRegMovimientoReferencia = "SELECT r FROM RegMovimientoReferencia r WHERE r.movimiento=:idMov ORDER BY r.secuencia";
    public static String getRegMovimientoReferenciaByMovReffByIdMov = "SELECT mr FROM RegMovimientoReferencia mr WHERE mr.movimientoReff.id = :movReff AND mr.movimiento = :idMov";
    public static String getRegMovimientoBytaskId = "Select m FROM RegMovimiento m where m.taskId=:task";
    public static String getRegCatPapelByActo = "select l from RegActo l where l.id=:idacto";
    public static String getRegMovimientoByRegpCertificadoInscripcion = "Select m FROM RegMovimiento m where m.regpCertificadoInscripcion.id=:idTarea";

    // Actos
    public static String getActobyIdSencillo = "Select r from RegActo r where r.id=:id";
    public static String getActobyAbreviatura = "Select r from RegActo r where r.abreviatura like :abrev";
    public static String getActobyNombre = "Select r from RegActo r where Lower(r.nombre) like :nombre and r.estado = true";
    public static String getActoOrdered = "Select r from RegActo r where r.estado = true order by r.nombre";

    /**
     * Normas Construccion
     */
    public static String getCatNormasConstruccionTipoByIsEspecial = "SELECT c FROM CatNormasConstruccionTipo c WHERE c.isEspecial = :isEspecial ORDER BY c.id ASC";
    public static String getCatNormasConstruccionByTipoNormaByCiudadela = "SELECT cn FROM CatNormasConstruccion cn WHERE cn.estado='A' AND cn.idCiudadela.id=:ciudadela AND cn.tipoNorma=:tipoNorma";
    public static String getCatNormasConstruccionByTipoNorma = "SELECT cn FROM CatNormasConstruccion cn WHERE cn.estado='A' AND cn.idCiudadela is null AND cn.tipoNorma=:tipoNorma";
    public static String getProcesoReporteByTramite = "SELECT p FROM ProcesoReporte p WHERE p.numTramite = :numTramite";
    public static String getHistoricoTramiteDetByTramite = "SELECT h FROM HistoricoTramiteDet h WHERE h.tramite.idTramite = :numTramite and h.estado=TRUE";

    // SAC
    public static String getRT_RUBROS_X_TITULOS_186 = "SELECT * FROM RT_RUBROS_X_TITULOS r where r.CODIGO_TITULO_REPORTE = 186 AND r.CODIGO_RUBRO = (SELECT MAX(t.CODIGO_RUBRO) FROM RT_RUBROS_X_TITULOS t where t.CODIGO_TITULO_REPORTE = 186)";
    public static String getRT_RUBROS_X_TITULOS_189 = "SELECT * FROM RT_RUBROS_X_TITULOS r where r.CODIGO_TITULO_REPORTE = 189 AND r.CODIGO_RUBRO = (SELECT MAX(t.CODIGO_RUBRO) FROM RT_RUBROS_X_TITULOS t where t.CODIGO_TITULO_REPORTE = 189)";
    public static String saveRT_RUBROS_X_TITULOS = "insert into RT_RUBROS_X_TITULOS(CODIGO_TRANSACCION,CODIGO_SUBTRANSACCION,CODIGO_DETALLE_SUBTRAN,CODIGO_TITULO_REPORTE,CODIGO_RUBRO,FORMA_CALCULAR,CTA_CONTABLE,CTA_ORDEN,CTA_PRESUPUESTO,DESCRIPCION,ES_PROPIO,TIPO,VALOR,ES_TASA,ESTADO,PRIORIDAD,USUARIO_INGRESO,FECHA_INGRESO,USUARIO_MODIFICACION,FECHA_MODIFICACION) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

    // SAM
    public static String consultarCodCiu = "select public.consulta_ciu_rp(?);";
    public static String registrarCodCiu = "select public.rp_grabar_ciu(?, ?, ?, ?, ?, ?, ?, ?, ?, to_timestamp(?, 'dd-MM-yyyy')::timestamp without time zone);";
    public static String consultarProforma = "select id from public.regp_liquidacion where num_tramite_rp = ?;";
    public static String insertLiquidacion = "INSERT INTO public.regp_liquidacion(id, tramite, is_registro_propiedad, descuento_porc, "
            + "descuento_valor, num_tramite_rp, total_pagar, reingreso, inscripcion, certificado, valor_actos, ingresado, estado, "
            + "desc_limit_cobro, sub_total, solicitante, estado_pago, estado_liquidacion, user_creacion, fecha_creacion, "
            + "inf_adicional_prof, tramitador, beneficiario, adicional, tramite_online) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
    public static String insertDetalles = "INSERT INTO public.regp_liquidacion_detalles VALUES (?, ?, ?, ?, ?, ?, ?, ?, "
            + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
    public static String verificarPago = "select rld.fecha_pago, rld.num_emision, rld.login_cobro "
            + "from public.regp_liquidacion_detalles rld "
            + "left join public.regp_liquidacion liq on liq.id = rld.liquidacion "
            + "where liq.num_tramite_rp = ? order by rld.id limit 1;";
    public static String consultarDetalles = "select id, fecha_pago, num_emision, login_cobro from public.regp_liquidacion_detalles "
            + "where to_char(fecha_pago, 'dd-MM-yyyy') = ? order by 1;";
    public static String tramitesNoPagados = "select liquidacion from public.regp_liquidacion_detalles "
            + "where num_emision is null group by liquidacion;";
    public static String deleteLiquidacion = "delete from public.regp_liquidacion where id = ?;";
    public static String deleteDetalles = "delete from public.regp_liquidacion_detalles where liquidacion = ?;";

    //FINANCIERO
    public static String getTitulos = "SELECT r FROM FinRubros r WHERE r.titulo=:titulo AND r.prioridad=0 ORDER BY r.descripcion";
    public static String getRubros = "SELECT r FROM FinRubros r WHERE r.titulo=:titulo  AND r.prioridad!=0 ORDER BY r.descripcion";
    public static String getMaxNumeroActa = "SELECT MAX(ac.numActa) FROM RecActasEspecies ac WHERE ac.anio = :valor";

    // HistoricoArchivo
    public static String getHistoricoArchivosList = "SELECT r FROM HistoricoArchivo r WHERE r.tramite=:tramiteId AND r.carpetaContenedora=:carpeta";
    public static String getHistoricoArchivoByArchivoId = "SELECT ha from HistoricoArchivo ha WHERE ha.idArchivo = :idArchivo AND ha.carpetaContenedora = :carpeta and ha.estado=TRUE";
    public static String getRegistroProfesionalByCaTEnte = "SELECT r FROM RegistroProfesionalTecnico r WHERE r.ente=:id";
    public static String getSvSolicitudServicion = "SELECT s FROM SvSolicitudServicios s WHERE s.id IN (SELECT sd.solicitud.id FROM SvSolicitudDepartamento sd WHERE sd.departamento.id = :id)";
    public static String existeHistoricoTramite = "SELECT (1) FROM flow.historico_tramites h WHERE LOWER(h.urbmz) = ? AND LOWER(h.urbsolar) = ? AND CAST(h.urbanizacion AS bigint) = (SELECT c.id FROM app.cat_ciudadela c WHERE c.id = ?) AND h.estado = ?";

    //AgendaTrabajo
    public static String getAgendaDetxInvolucrado = "select e from AgendaDet e where e.involucrado.id = :involucrado";
    public static String getAgendaPendiente = "select e from AgendaDet e where e.agenda.finalizado is null";

    public static String estadisticaDepartamento = "SELECT gd.nombre AS departamento, COUNT(HT.ID) AS numerotramites, cast(EXTRACT(YEAR FROM ht.fecha) as Integer) As anio FROM app.ge_departamento gd LEFT JOIN app.ge_tipo_procesos gp ON (gd.id = gp.departamento) LEFT JOIN app.ge_tipo_tramite gt ON(gp.id = gt.tipo_proceso) LEFT JOIN flow.historico_tramites ht ON(ht.tipo_tramite = gt.id) WHERE EXTRACT(YEAR FROM ht.fecha) = :periodo GROUP BY gd.nombre, anio ORDER BY gd.nombre ASC;";
    public static String estadisticaDepartamentoAnio = "SELECT gd.nombre AS departamento, COUNT(HT.ID) AS numerotramites, cast(EXTRACT(YEAR FROM ht.fecha) as Integer) As anio FROM app.ge_departamento gd LEFT JOIN app.ge_tipo_procesos gp ON (gd.id = gp.departamento) LEFT JOIN app.ge_tipo_tramite gt ON(gp.id = gt.tipo_proceso) LEFT JOIN flow.historico_tramites ht ON(ht.tipo_tramite = gt.id) WHERE EXTRACT(YEAR FROM ht.fecha) = :periodo AND gd.id = :dep GROUP BY gd.nombre, anio ORDER BY gd.nombre ASC;";
    public static String estadisticaDepartamentoEntreFecha = "SELECT gd.nombre AS departamento, COUNT(HT.ID) AS numerotramites, cast(EXTRACT(YEAR FROM ht.fecha) as Integer) As anio FROM app.ge_departamento gd LEFT JOIN app.ge_tipo_procesos gp ON (gd.id = gp.departamento) LEFT JOIN app.ge_tipo_tramite gt ON(gp.id = gt.tipo_proceso) LEFT JOIN flow.historico_tramites ht ON(ht.tipo_tramite = gt.id) WHERE ht.fecha BETWEEN :periodo1 and :periodo2 GROUP BY gd.nombre, anio ORDER BY gd.nombre ASC;";
    public static String estadisticaDepartamentoEntreFechaDep = "SELECT gd.nombre AS departamento, COUNT(HT.ID) AS numerotramites, cast(EXTRACT(YEAR FROM ht.fecha) as Integer) As anio FROM app.ge_departamento gd LEFT JOIN app.ge_tipo_procesos gp ON (gd.id = gp.departamento) LEFT JOIN app.ge_tipo_tramite gt ON(gp.id = gt.tipo_proceso) LEFT JOIN flow.historico_tramites ht ON(ht.tipo_tramite = gt.id) WHERE ht.fecha BETWEEN :periodo1 and :periodo2 AND gd.id = :dep GROUP BY gd.nombre, anio ORDER BY gd.nombre ASC;";
    public static String getCatPredioByCdlByMzBySolar = "SELECT c FROM CatPredio c WHERE UPPER(c.urbMz) = :urbMz AND UPPER(c.urbSolarnew) = :urbSolarnew AND c.ciudadela.id = :cdla AND c.estado = 'A'";
    public static String maxEnteSecuencia = "SELECT max(es.secuencia) FROM EnteSecuencia es";
    public static String CatEnteByRazonSocialList = "SELECT c FROM CatEnte c WHERE UPPER(c.razonSocial) LIKE UPPER(:razonSocial)";

    //Estadística
    public static String getTramitesByTipoAndEstado = "select e from HistoricoTramites e where e.tipoTramite = :tipo and lower(e.estado) =:estado";
    public static String getGeTipoTramiteListByEstado = "select e from GeTipoTramite e where e.estado=true and e.mostrarEstadistica = true";
    public static String getCountTramitesMuertosPorTipoTramite = "select count(*) from act_hi_taskinst as task where task.task_def_key_='revisionDocumentoGeneralRentas' and task.end_time_ IS NULL and lower(task.description_)=";
    public static String getPermisoAdicional = "SELECT p FROM PePermisosAdicionales p WHERE p.numTramite = :numTramite";

    //Correccion Predio
    public static String getFechaSolicitudMenor = "SELECT MIN(s.fechaSolicitud) FROM SolicitudCorreccionPredio s";
    public static String getFechaSolicitudMayor = "SELECT MAX(s.fechaSolicitud) FROM SolicitudCorreccionPredio s";

    /**
     * El query realiza una busqueda con 'LIKE' y los que tengan estado 'A'.
     * Necesita los parametros nombres, esPersona. Los oredna por el nombre.
     */
    public static String CatEnteByNombresList = "SELECT c FROM CatEnte c WHERE UPPER(c.nombres) LIKE UPPER(:nombres) AND c.esPersona = :esPersona AND c.estado = 'A' ORDER BY c.nombres ASC";
    /**
     * El query realiza una busqueda con 'LIKE' y los que tengan estado 'A'.
     * Necesita los parametros apellidos, esPersona. Los ordena por el apellido.
     */
    public static String CatEnteByApellidoslList = "SELECT c FROM CatEnte c WHERE UPPER(c.apellidos) LIKE UPPER(:apellidos) AND c.esPersona = :esPersona AND c.estado = 'A' ORDER BY c.apellidos ASC";

    /**
     * El query realiza una busqueda con 'LIKE' y los que tengan estado 'A', los
     * ordena por el campo razonSocial. Necesita los parametros apellidos,
     * esPersona.
     */
    public static String CatEnteByRazonSocialListAct = "SELECT c FROM CatEnte c WHERE UPPER(c.razonSocial) LIKE UPPER(:razonSocial) AND c.estado = 'A' ORDER BY c.razonSocial ASC";
    public static String getIdEnte = "SELECT c.id FROM CatEnte c WHERE c.ciRuc = :ciRuc";

    public static String getServicioExternoByIdent = "select e from ServicioExterno e where e.identificador = :identificador and e.estado = true";

    //RECAUDACIONES
    public static String getEspeciesActivas = "select es from RecEspecies es where es.tipo = 1 order by es.code";
    public static String getActasEspeciesActivas = "select ac from RecActasEspeciesDet ac where ac.estado = 'A' order by ac.acta.fechaIngreso";
    public static String getActaByEspecieUser = "select es from RecActasEspeciesDet es where es.especie = :idEspecie and es.acta.usuarioAsignado = :idUser and es.disponibles > 0 and es.estado = 'A' order by es.acta.fechaIngreso";
    public static String getDesripcionRubro = "SELECT r.descripcion FROM RenRubrosLiquidacion r WHERE r.id=:idRubro";
    public static String getParametrosInteresMulta = "SELECT p FROM RenParametrosInteresMulta p WHERE p.tipoLiquidacion=:tipoLiquidacion AND p.dia<=:dia AND p.mes<=:mes";
    public static String getRenCajeros = "Select c from RenCajero c order by c.id";

    //COACTIVA
    public static String getAbogadosJuicios = "Select c from CoaAbogado c where c.estado = true order by c.detalle";
    public static String getEstadosJuicios = "Select c from CoaEstadoJuicio c where c.estado = true order by c.secuencia";
    public static String getMimFechaJuicio = "SELECT MIM(cp.fechajuicio) FROM CoaJuicio c ";
    public static String getMimFechaIngreso = "SELECT MIN(cp.fechaIngreso) FROM CoaJuicio c";
    // SE CONSULTA POR ESTADO_COACTIVA = 1 POR MOTIVO DE QUE SOLO SE NECESITAN MOSTRAR EN PANTALLA
    // LAS EMISIONES PREDIALES QUE NO ESTAN EN COACTIVA
    public static String getLiquidacionNoPagadaByPredio = "select r from RenLiquidacion r where r.estadoCoactiva = 1 and r.tipoLiquidacion = :tipo and r.estadoLiquidacion = :estado and r.anio < :anio "
            + "and r.predio = :idPredio order by r.anio desc";
    public static String getLiquidacionesActivasByPredio = "select r from RenLiquidacion r where r.estadoCoactiva = 1 and (r.estadoLiquidacion = 1 or r.estadoLiquidacion = 2) and r.tipoLiquidacion = :tipo and r.anio < :anio "
            + "and r.predio = :idPredio order by r.anio desc";
    public static String getLiquidacionesNoIngresadas = "Select r from RegpLiquidacion r where r.estadoLiquidacion = 1";
    public static String getLiquidacionesOrdenadas = "Select r from RegpLiquidacion r where r.estadoLiquidacion = 2 "
            + "and r.estadoPago = 7 and r.tramite.entregado is false order by r.fechaIngreso";
    public static String getLiquidacionesSinActiviti = "Select r from RegpLiquidacion r where r.estadoLiquidacion = 2 "
            + "and r.inscriptor is not null and r.tramite.idProceso is null order by r.fechaIngreso";
    public static String getEmisionesEnCoactiva = "select r from RenLiquidacion r where r.tipoLiquidacion = 13 and (r.estadoLiquidacion = 1 or r.estadoLiquidacion = 2) and r.estadoCoactiva = 2 and r.predio = :idPredio and r.anio <= :anio";
    public static String getLiquidacionesCoactivaByJuicio = "select jp.liquidacion from CoaJuicioPredio jp where jp.juicio = :idJuicio";
    public static String getMinAnioImpuestoCoactiva = "select min(anio) from financiero.ren_liquidacion where tipo_liquidacion = 13 and estado_liquidacion = 2 and estado_coactiva = 2 and predio = ?";

    //IMPUESTO PREDIAL
    public static String rangoAvaluo = "SELECT r FROM CatRangoAvaluos r WHERE (:anio BETWEEN r.anioDesde AND r.anioHasta) AND (:avaluoMunicipal BETWEEN r.valorDesde AND r.valorHasta)";
    public static String salario = "SELECT s FROM CtlgSalario s WHERE s.anio=:anio";
    public static String emisionExistente = "SELECT l FROM RenLiquidacion l WHERE l.predio.id=:idPredio AND l.anio=:anio AND l.tipoLiquidacion.id=13";
    public static String getLiquidacionByTramite = "SELECT l FROM RenLiquidacion l WHERE l.tramite = :idTramite and l.tipoLiquidacion = :tipoLiq";
    public static String getLiquidacionNoPagadaByTramite = "SELECT l FROM RenLiquidacion l WHERE l.tramite = :idTramite and l.tipoLiquidacion = :tipoLiq and l.estadoLiquidacion = 2"; // ESTADO 2 NO PAGADO
    public static String getLiquidacionByTramiteActivo = "SELECT l FROM RenLiquidacion l WHERE l.tramite = :idTramite and l.tipoLiquidacion = :tipoLiq and l.estadoLiquidacion = :estadoLiq";
    public static String descuentoFecha = "SELECT d FROM CtlgDescuentoEmision d WHERE d.numMes=:mes AND d.numQuincena=:quincena";
    public static String predios = "SELECT p FROM CatPredio p";
    public static String numeroPredios = "SELECT COUNT(p) FROM CatPredio p WHERE p.estado='A'";
    public static String totalAvaluosMunicipal = "SELECT SUM(p.avaluoMunicipal) FROM CatPredio p WHERE p.estado='A'";
    public static String totalAreaSolar = "SELECT SUM(s4.areaCalculada) FROM CatPredioS4 s4 WHERE s4.predio.estado='A'";
    public static String totalAvaluosMunicipalByParroquia = "SELECT SUM(p.avaluoMunicipal) FROM CatPredio p WHERE p.estado='A' AND p.ciudadela.ubicacion.id=:idUbicacion";
    public static String totalAreaSolarByParroquia = "SELECT SUM(s4.areaCalculada) FROM CatPredioS4 s4 WHERE s4.predio.estado='A' AND s4.predio.ciudadela.ubicacion.id=:idUbicacion";
    public static String totalEmisionByPredio = "SELECT SUM(e.saldo) FROM RenLiquidacion e WHERE e.tipoLiquidacion.id=13 AND e.estadoLiquidacion.id=2 AND e.predio=:predio AND e.anio<=:anio ";
    public static String emisionByPredio = "SELECT e FROM RenLiquidacion e WHERE e.tipoLiquidacion.id=13 AND e.estadoLiquidacion.id=2 AND e.predio=:predio AND e.anio<=:anio ORDER BY e.anio DESC";
    public static String getEmisionesUrbanasEnCoactiva = "Select l from RenLiquidacion l where l.tipoLiquidacion = 13 and l.estadoLiquidacion = 2 and l.estadoCoactiva = 2 and l.predio =: idPredio and l.anio <= :anio";

    // IMPUESTO PREDIAL RURAL
    public static String parroquiasRurales = "SELECT p FROM CatParroquia p WHERE p.id IN (2,3) AND p.estado=true ORDER BY p.descripcion";
    public static String emisionByPredioRural = "SELECT e FROM RenLiquidacion e WHERE e.tipoLiquidacion.id=7 AND e.estadoLiquidacion.id=2 AND e.predioRustico=:predioRustico AND e.anio<=:anio ORDER BY e.anio DESC";

    //EMISIONES
    public static String emisionesByTipoAnio = "SELECT COUNT(e) FROM RenLiquidacion e WHERE e.tipoLiquidacion.id=:tipoLiquidacion AND e.anio=:anio";
    public static String cantidadObrasByAnio = "SELECT COUNT(o) FROM MejObra o WHERE o.anio=:anio";
    public static String cantidadSumasAnuales = "SELECT COUNT(s) FROM CatPredioSumasAnualesUbicacion s WHERE s.anio=:anio";
    public static String rangosAnualesUrb = "SELECT COUNT(*) FROM app.cat_rango_avaluos  WHERE ? BETWEEN anio_desde AND anio_hasta";
    public static String rangosAnualesRur = "SELECT COUNT(*) FROM app.cat_rango_avaluos_rusticos  WHERE ? BETWEEN anio_desde AND anio_hasta";
    //PAGOS
    public static String bancos = "SELECT b FROM RenEntidadBancaria b WHERE b.estado = true and b.tipo = 1";
    public static String tarjetas = "SELECT b FROM RenEntidadBancaria b WHERE b.estado = true and b.tipo = 2";

    //FORMATO REPORTES
    public static String getFormatoxCodigo = "select e from FormatoReporte e where e.codigo = :codigo and e.estado = true";

    //CERTIFICADO AVALUOS
    public static String getCertificadoAval = "select e from CatCertificadoAvaluo e where e.codigo = :codigo and e.estado = true";

    //CAT_EXCEPCIONES
    public static String getCatExcParams = "select e from CatExcepcionesParam e where e.prefijo = :prefijo";

    // CONSULTAS RPP
    public static String getMinRepertorioDisponible = "select min(s.repertorio) from SecuenciaRepertorio s "
            + "where s.anio = :anio and s.disponible = true";
    public static String getMaxRepertorioNoDisponible = "select max(s.repertorio) from SecuenciaRepertorio s "
            + "where s.anio = :anio and s.disponible = false";
    public static String getMinRepMercDisponible = "select min(s.repertorio) from SecuenciaRepertorioMercantil s "
            + "where s.anio = :anio and s.disponible = true";
    public static String getMaxRepMercNoDisponible = "select max(s.repertorio) from SecuenciaRepertorioMercantil s "
            + "where s.anio = :anio and s.disponible = false";
    public static String getSecuenciaRepertorio = "SELECT secuencias.sec_repertorio(?);";
    
    public static String getMinInscripcionDisponible = "select min(s.inscripcion) from SecuenciaInscripcion s "
            + "where s.anio = :anio and s.libro = :idLibro and s.disponible = true";
    public static String getMaxInscripcionNoDisponible = "select max(s.inscripcion) from SecuenciaInscripcion s "
            + "where s.anio = :anio and s.libro = :idLibro and s.disponible = false";
    public static String getUserForTramite = "Select u from TarUsuarioTareas u where u.estado = true and "
            + "u.rol = :rolUsuario and u.cantidad = (Select min(ut.cantidad) from TarUsuarioTareas ut "
            + "where ut.estado = true and ut.rol = :rolUsuario)";
    public static String getUsuarioTramite = "Select u from TarTareasAsignadas u "
            + "where u.usuarioTareas.estado = true and u.usuarioTareas.rol.id = :rolUsuario "
            + "and u.fecha = :fecha and u.cantidad = (Select min(ta.cantidad) "
            + "from TarTareasAsignadas ta where ta.usuarioTareas.estado = true and "
            + "ta.usuarioTareas.rol = :rolUsuario and ta.fecha = :fecha)";
    public static String getMinDateUsers = "Select min(ut.fecha) from TarUsuarioTareas ut where ut.estado = true and ut.rol.id = :rol";
    public static String getMinUser = "Select ut from TarUsuarioTareas ut where ut.estado = true and ut.rol.id = :rol and "
            + "ut.fecha = :fecha and ut.cantidad = (Select min(u.cantidad) from TarUsuarioTareas u where u.estado = true and "
            + "u.rol.id = :rol and fecha = :fecha)";
    public static String getUsuarioTareas = "Select u from TarTareasAsignadas u "
            + "where u.usuarioTareas.estado = true and u.fecha = :fecha and u.usuarioTareas.usuario = :idUsuario";
    public static String getMaxDiasByTramite = "Select max(de.acto.dias) from RegpLiquidacionDetalles de "
            + "where de.liquidacion.id = :idLiquidacion";
    public static String getFacturasNoEnviadas = "Select li from RegpLiquidacion li where li.numeroComprobante > 0 "
            + "and to_char(li.fechaIngreso, 'dd/MM/yyyy') = :fecha and li.userIngreso = :idUsuario "
            + " and li.estadoLiquidacion = 2 order by li.fechaIngreso";
    public static String getCantidadTramitesIngresados = "select count(*) from flow.regp_liquidacion where numero_comprobante > 0 and "
            + "fecha_ingreso between to_date(?, 'dd/MM/yyyy') and to_date(?, 'dd/MM/yyyy')";
    public static String getCantidadTramitesIngresadosUser = "select count(*) from flow.regp_liquidacion where numero_comprobante > 0 and "
            + "user_ingreso = ? and fecha_ingreso between to_date(?, 'dd/MM/yyyy') and to_date(?, 'dd/MM/yyyy')";
    public static String getTotalTramitesIngresados = "select sum(total_pagar) from flow.regp_liquidacion where numero_comprobante > 0 and "
            + "fecha_ingreso between to_date(?, 'dd/MM/yyyy') and to_date(?, 'dd/MM/yyyy')";
    public static String getTotalTramitesIngresadosUser = "select sum(total_pagar) from flow.regp_liquidacion where numero_comprobante > 0 and "
            + "user_ingreso = ? and fecha_ingreso between to_date(?, 'dd/MM/yyyy') and to_date(?, 'dd/MM/yyyy')";
    public static String getRegPapelesList = "Select pa from RegPapel pa where pa.estado = true ORDER BY pa.papel ASC";
    public static String getRegDomicilioByName = "select do from RegDomicilio do where lower(do.nombre) like :ciudad ORDER BY do.nombre ASC";
    public static String getRegFichaPropietarios = "select pr from RegFichaPropietarios pr where pr.estado = 'A' and pr.propietario = :interviniente";
    public static String getHistoricoTramitesByFechasIngreso = "Select ht.tramite from RegpLiquidacion ht where ht.estadoLiquidacion = 2 and ht.fechaIngreso between :inicio and :fin order by ht.numTramiteRp";
    public static String getTotalFichasByUser = "select count(*) from app.reg_ficha where user_ingreso = ? and "
            + "fecha_ape between to_date(?, 'dd/MM/yyyy') and to_date(?, 'dd/MM/yyyy')";
    public static String getTotalMovimientosByUser = "select count(*) from app.reg_movimiento where user_creador = ? and "
            + "fecha_ingreso between to_date(?, 'dd/MM/yyyy') and to_date(?, 'dd/MM/yyyy')";
    public static String getHistoricoTramiteByNumTramite = "SELECT ht FROM HistoricoTramites ht WHERE ht.numTramite = :tramite";
    public static String getIdProcesoByNumTramite = "SELECT ht.idProceso FROM HistoricoTramites ht WHERE ht.numTramite = :tramite";
    public static String getActosByFechaIngreso = "select distinct det.acto from RegpLiquidacionDetalles det where "
            + "det.liquidacion.estadoLiquidacion = 2 and det.liquidacion.fechaIngreso "
            + "between to_date(:desde, 'dd/MM/yyyy') and to_date(:hasta, 'dd/MM/yyyy')";
    public static String getTotalIngresadosByContrato = "select count(det) from flow.regp_liquidacion_detalles det "
            + "left join flow.regp_liquidacion li on (li.id = det.liquidacion) where li.estado_liquidacion = 2 and det.acto = ? "
            + "and li.fecha_ingreso between to_date(?, 'dd/MM/yyyy') and to_date(?, 'dd/MM/yyyy')";
    public static String getMovsSinFicha = "Select m from RegMovimiento m where m.estado = 'IN' and m.libro.id = 11 "
            + "and m.numInscripcion is not null and m.fechaInscripcion is not null and m.acto.id <> 12 "
            + "and (m.fechaInscripcion between :desde and :hasta) "
            + "and m not in (select distinct(mf.movimiento) from RegMovimientoFicha mf) "
            + "order by m.fechaInscripcion, m.numInscripcion";
    public static String getMovsSinFichaPeriodo = "Select m from RegMovimiento m where m.estado = 'IN' and m.libro.id = 11 "
            + "and m.numInscripcion is not null and m.fechaInscripcion is not null and m.acto.id <> 12 "
            + "and to_char(m.fechaInscripcion, 'MM/yyyy') = :periodo "
            + "and m not in (select distinct(mf.movimiento) from RegMovimientoFicha mf) "
            + "order by m.fechaInscripcion, m.numInscripcion";
    public static String getDetallesOrden = "Select d from OrdenFichasDetalle d where d.ordenFicha.id = : idOrder order by d.fechaInicio desc";
    public static String getHistoricoTramiteByIdProcessFechaEntrega = "SELECT ht FROM HistoricoTramites ht Where ht.idProceso = :idprocess and ht.fechaEntrega between :desde and :hasta";
    public static String getCertificadoByMi = "Select ce from Cercontrato ce where ce.cerNumero = (Select max(c.cerNumero) from Cercontrato c where c.cerMi = :matricula)";
    public static String getCantidadTramites = "select count(*) from flow.regp_liquidacion where estado_liquidacion = ? and to_char(fecha_ingreso, 'dd/MM/yyyy') = ?";
    public static String getCantIngCert = "select count(distinct(li.num_tramite_rp)) from flow.regp_liquidacion li "
            + "left join flow.regp_liquidacion_detalles de on (de.liquidacion = li.id) "
            + "left join app.reg_acto ac on (ac.id = de.acto) where li.estado_liquidacion = 2 and "
            + "to_char(li.fecha_ingreso, 'dd/MM/yyyy') = ? and li.num_tramite_rp not in "
            + "(select distinct(li.num_tramite_rp) from flow.regp_liquidacion li "
            + "left join flow.regp_liquidacion_detalles de on (de.liquidacion = li.id) "
            + "left join app.reg_acto ac on (ac.id = de.acto) where li.estado_liquidacion = 2 and "
            + "to_char(li.fecha_ingreso, 'dd/MM/yyyy') = ? and ac.solvencia is false)";
    public static String getCantIngInsc = "select count(distinct(li.num_tramite_rp)) from flow.regp_liquidacion li "
            + "left join flow.regp_liquidacion_detalles de on (de.liquidacion = li.id) "
            + "left join app.reg_acto ac on (ac.id = de.acto) where li.estado_liquidacion = 2 and "
            + "to_char(li.fecha_ingreso, 'dd/MM/yyyy') = ? and ac.solvencia is false";
    public static String getCantOutCert = "select count(distinct(li.num_tramite_rp)) from flow.regp_liquidacion li "
            + "left join flow.regp_liquidacion_detalles de on (de.liquidacion = li.id) "
            + "left join flow.historico_tramites ht on (ht.id = li.tramite) "
            + "left join app.reg_acto ac on (ac.id = de.acto) where li.estado_liquidacion = 2 and "
            + "to_char(ht.fecha_entrega, 'dd/MM/yyyy') = ? and li.num_tramite_rp not in "
            + "(select distinct(li.num_tramite_rp) from flow.regp_liquidacion li "
            + "left join flow.historico_tramites ht on (ht.id = li.tramite) "
            + "left join flow.regp_liquidacion_detalles de on (de.liquidacion = li.id) "
            + "left join app.reg_acto ac on (ac.id = de.acto) where li.estado_liquidacion = 2 and "
            + "to_char(ht.fecha_entrega, 'dd/MM/yyyy') = ? and ac.solvencia is false)";
    public static String getCantOutInsc = "select count(distinct(li.num_tramite_rp)) from flow.regp_liquidacion li "
            + "left join flow.historico_tramites ht on (ht.id = li.tramite) "
            + "left join flow.regp_liquidacion_detalles de on (de.liquidacion = li.id) "
            + "left join app.reg_acto ac on (ac.id = de.acto) where li.estado_liquidacion = 2 and "
            + "to_char(ht.fecha_entrega, 'dd/MM/yyyy') = ? and ac.solvencia is false";
    public static String getFichasByUserAndFecha = "Select f from RegFicha f where f.userIngreso = :usuario and "
            + "f.fechaApe between :desde and :hasta order by f.numFicha";
    public static String getAntecedentesByUserAndFecha = "Select m from RegMovimiento m where m.estado = 'AC' "
            + "and m.userCreador.usuario = :usuario and m.tramite is null and m.taskId is null and m.fechaIngreso between :desde and :hasta "
            + "order by m.fechaIngreso";
    public static String getInscripcionesByUserAndFecha = "Select m from RegMovimiento m where m.estado = 'AC' "
            + "and m.userCreador.usuario = :usuario and m.tramite is not null and m.taskId is not null and m.fechaIngreso between :desde and :hasta "
            + "order by m.fechaIngreso";
    public static String getCertificadosByUserAndFecha = "Select c from RegCertificado c where c.userCreador = :usuario "
            + "and c.tareaTramite.fechaFin between :desde and :hasta order by c.numCertificado";
    public static String getTarUsuarioTareasByUser = "Select t from TarUsuarioTareas t where t.estado and t.usuario.id = :user";

    public static String getCantActosByLiquidacion = "SELECT count(dl.acto) FROM flow.regp_liquidacion_detalles dl WHERE dl.liquidacion = ? ";
    public static String getDetallesByLiquidacion = "Select d from RegpLiquidacionDetalles d where d.liquidacion.id = :parametro";
    public static String getLiquidacionInscripcion = "select distinct(li.num_tramite_rp) from flow.regp_liquidacion li "
            + "inner join flow.regp_liquidacion_detalles de on de.liquidacion = li.id inner join app.reg_acto ac on ac.id = de.acto "
            + "where ac.solvencia is false and li.id = ?";
    public static String getDocsTareasByTramite = "Select dt from RegpDocsTarea dt where dt.estado = true and dt.tarea.tramite.id = :idTramite";
    public static String getDocsTareasByTareaTramite = "Select dt from RegpDocsTarea dt where dt.estado = true and dt.tarea.id = :idTarea";
    public static String getDocsTramiteByTramite = "Select dt from RegpDocsTramite dt where dt.estado = true and dt.tramite.id = :idTramite";
    public static String updateDocsEnables = "update flow.regp_docs_tramite set estado = false where tramite = ?";
    public static String updateDocsTareas = "update flow.regp_docs_tarea set estado = false where tarea = ?";
    public static String updateTareasTramite = "update flow.regp_tareas_tramite set realizado = false, revisado = false where tramite = "
            + "(select id from flow.historico_tramites where num_tramite = ?)";
    public static String getRegpLiquidacionByNumTramite = "Select li from RegpLiquidacion li where li.numTramiteRp = :numTramite ";
    public static String consultaAnexo2Side = "select mo.id as idmovimiento, st.tramite as contrato, st.tipo as tipoacto, st.tipo_acto_contrato as tipotramite, "
            + "st.libro as tipolibro, mo.num_repertorio as repertorio, mo.fecha_repertorio as fecharepertorio, "
            + "mo.num_inscripcion as inscripcion, mo.fecha_inscripcion as fechainscripcion, trim(re.nombre) as interviniente, "
            + "re.ced_ruc as documento, mc.estado as estadocivil, mc.nombres as conyugue, mc.cedula as docconyugue, cc.calidad, "
            + "(select de.cuantia from flow.regp_liquidacion_detalles de inner join flow.regp_tareas_tramite tt on tt.detalle = de.id "
            + "where tt.id = mo.num_tramite) as cuantia, mo.valor_uuid as uuid, (select libro from app.reg_acto where id = mo.acto) as libro, "
            + "mo.fecha_mod as fechamodificacion, ju.nombre as entidad, ca.nombre as domicilio, mo.fecha_oto as fechacelebracion, mo.estado, mo.observacion, "
            + "mo.folio_inicio as folioinicio, mo.folio_fin as foliofin "
            + "from app.reg_movimiento mo inner join ctlg.sedi_tramite st on st.acto = mo.acto "
            + "left join app.reg_ente_judiciales ju on ju.id = mo.ente_judicial and sedi is true "
            + "left join app.reg_domicilio ca on ca.id = mo.domicilio "
            + "left join app.reg_movimiento_cliente mc on mc.movimiento = mo.id "
            + "left join app.reg_ente_interviniente re on re.id = mc.ente_interv "
            + "left join ctlg.sedi_calidad_compareciente cc on cc.id = mc.papel "
            + "where mo.estado IN ('IN', 'AC') AND CAST(fecha_inscripcion as date) between CAST(? AS DATE) AND CAST(? AS DATE) "
            + "order by mo.fecha_inscripcion, mo.num_repertorio";
    public static String getFacturasAutorizadas = "Select r from RegpLiquidacion r where r.estadoWs = 'RECIBIDA;AUTORIZADO' "
            + "and r.userIngreso = :usuario and to_char(r.fechaIngreso, 'dd/MM/yyyy') = :ingreso order by r.fechaIngreso";
    public static String getFacturasNoAutorizadas = "Select r from RegpLiquidacion r where r.reingreso = false and (r.estadoWs <> 'RECIBIDA;AUTORIZADO' or r.estadoWs is null)"
            + "and r.userIngreso = :usuario and to_char(r.fechaIngreso, 'dd/MM/yyyy') = :ingreso and r.totalPagar > 0 order by r.fechaIngreso";

    public static String getFacturasAutorizadasAllUser = "Select r from RegpLiquidacion r "
            + "where r.estadoWs = 'RECIBIDA;AUTORIZADO' "
            + " and to_char(r.fechaIngreso, 'dd/MM/yyyy') = :ingreso order by r.fechaIngreso";

    public static String getFacturasNoAutorizadasAllUser = "Select r from RegpLiquidacion r "
            + "where r.reingreso = false and (r.estadoWs <> 'RECIBIDA;AUTORIZADO' or r.estadoWs is null)"
            + " and r.estadoPago.id = 2 and r.estadoLiquidacion.id = 2 "
            + " and to_char(r.fechaIngreso, 'dd/MM/yyyy') = :ingreso and r.totalPagar > 0 order by r.fechaIngreso";

    public static String getFacturasNoAutorizadasReenvio = "Select r from RegpLiquidacion r "
            + "where r.reingreso = false and (r.estadoWs <> 'RECIBIDA;AUTORIZADO' or r.estadoWs is null)"
            + " and r.totalPagar > 0 "
            + " and r.estadoPago.id = 2 and r.estadoLiquidacion.id = 2 order by r.fechaIngreso";

    public static String getNotaCreditoByTramite = "Select r from RenNotaCredito r where r.factura.numTramiteRp = :tramite";
    public static String getSumNCbyTramite = "Select sum(r.valorTotal) from RenNotaCredito r where r.factura.numTramiteRp = :tramite";
    public static String getMaxFichaFromMov = "Select f from RegFicha f where f.id = (Select max(mf.ficha.id) from RegMovimientoFicha mf where mf.movimiento.id = :idFicha)";
    public static String consultaAnexo1Side12 = "select rei.tipo_interv as tipopersona, rei.nombre AS apellidos, rei.nombre AS nombres,  "
            + "rei.ced_ruc AS ci, rp.papel as tipocompareciente, ac.nombre as tipocontrato,  "
            + "m.num_inscripcion as numinscripcion, m.fecha_inscripcion as fechainsripcion,  "
            + "fi.clave_catastral as clavecatastral, li.nombre as libro,  "
            + "coalesce('Santo Domingo','') as provincia, fi.tipo_predio as zona, cast(fi.area_terreno as character varying) as superficie, fi.linderos as lindero,  "
            + "parr.descripcion as parroquia, coalesce('Santo Domingo','') as canton, "
            + "(select de.base_imponible from flow.regp_liquidacion_detalles de inner join flow.regp_tareas_tramite tt on tt.detalle = de.id "
            + "where tt.id = m.num_tramite) as cuantia, "
            + "coalesce('$','') as unidad, m.id as identificador, coalesce('','') as numjuicio, m.estado as estado,  "
            + "coalesce('RP.SantoDomingo','') as ubicaciondato, m.fecha_inscripcion as ultimamodificacion,  "
            + "enj.nombre as notaria, cton.nombre as cantonnotaria, m.fecha_oto as fechaescritura,  "
            + "m.valor_uuid as valoruuid, COALESCE(tbien.descripcion, '') AS descripcionbien "
            + "from app.reg_movimiento as m  "
            + "left join app.reg_movimiento_cliente as mc on (mc.movimiento = m.id) "
            + "left join app.reg_ente_interviniente as rei on (rei.id = mc.ente_interv)  "
            + "left join app.reg_papel as rp on (rp.id = mc.papel) "
            + "left join app.reg_movimiento_ficha as mf on (mf.movimiento = m.id) "
            + "left join app.reg_acto as ac on (ac.id=m.acto) "
            + "left join app.reg_libro as li on (li.id=m.libro) "
            + "left join app.reg_ficha as fi on (fi.id=mf.ficha) "
            + "left join app.cat_parroquia as parr on (parr.id=fi.parroquia) "
            + "left join app.reg_ente_judiciales as enj on (enj.id=m.ente_judicial) "
            + "left join app.reg_domicilio as cton on (cton.id=m.domicilio) "
            + "LEFT JOIN ctlg.uaf_tipo_bien tbien ON (tbien.id = fi.uaf_tipo_bien) "
            + "where m.estado = 'AC' AND m.num_repertorio is not null AND m.num_repertorio > 0 AND li.anexo_uno_reg_propiedad is true AND "
            + "TO_CHAR(m.fecha_inscripcion, 'DD/MM/YYYY') = ? ";

    public static String consultaAnexo2Side12 = "select CASE WHEN rei.tipo_interv = 'N' THEN rei.nombre ELSE '' END AS apellidos, "
            + "CASE WHEN rei.tipo_interv = 'N' THEN rei.nombre ELSE '' END AS nombres, "
            + "CASE WHEN rei.tipo_interv = 'J' THEN rei.nombre ELSE '' END AS razonsocial, "
            + "rei.ced_ruc AS ci, rp.papel as tipocompareciente, ac.nombre as tipocontrato, "
            + "m.num_inscripcion as numinscripcion, m.fecha_inscripcion as fechainsripcion, cast('' as character varying) as nombrerepresentante, "
            + "m.fecha_inscripcion as fechacancelacion, fi.clave_catastral AS chasis, fi.linderos as datosbien, "
            + "coalesce('R.P. Santo Domingo','') as registrador, m.fecha_inscripcion as ultimamodificacion, "
            + "m.valor_uuid as codigounico,enj.nombre as entidapublica, cton.nombre as cantonnombre, "
            + "m.fecha_oto as fechaescritura, cast('VIGENTE' as character varying) as estado "
            + "from app.reg_movimiento as m "
            + "left join app.reg_movimiento_cliente as mc on (mc.movimiento = m.id) "
            + "left join app.reg_ente_interviniente as rei on (rei.id = mc.ente_interv) "
            + "left join app.reg_papel as rp on (rp.id = mc.papel) "
            + "left join app.reg_movimiento_ficha as mf on (mf.movimiento = m.id) "
            + "left join app.reg_acto as ac on (ac.id = m.acto) "
            + "left join app.reg_libro as li on (li.id = m.libro) "
            + "left join app.reg_ficha as fi on (fi.id = mf.ficha) "
            + "left join app.reg_ente_judiciales as enj on (enj.id = m.ente_judicial) "
            + "left join app.reg_domicilio as cton on (cton.id = m.domicilio) "
            + "LEFT JOIN ctlg.uaf_tipo_bien tbien ON (tbien.id = fi.uaf_tipo_bien) "
            + "where m.estado = 'AC' AND li.anexo_dos_mercantil is TRUE AND "
            + "TO_CHAR(m.fecha_inscripcion, 'DD/MM/YYYY') = ?";
    public static String consultaAnexo3Side12 = "SELECT rei.nombre AS clienombre, rei.ced_ruc AS ruc, "
            + "cast('ANONIMA' as character varying) as tipocompania, "
            + "m.fecha_inscripcion as fechainsripcion, comp.nombre AS apellidos, comp.nombre AS nombres, comp.ced_ruc numeroidentificacion,"
            + "crg.nombre AS cargonombre, rp.papel as tipocompareciente, m.observacion disposicion, "
            + "cast('Ninguno' as character varying) as autoridaddisposicion, m.fecha_oto as fechaescritura, enj.nombre as notaria, "
            + "cton.nombre as cantonnombre, ac.nombre as tipotramite, coalesce('R.P. Santo Domingo','') as ubicaciondato,"
            + "m.valor_uuid as codigounico, cast('VIGENTE' as character varying) as estado "
            + "from app.reg_movimiento as m "
            + "left join app.reg_movimiento_cliente as mc on (mc.movimiento=m.id)  "
            + "left join app.reg_ente_interviniente as rei on (rei.id=mc.ente_interv)  "
            + "left join app.reg_papel as rp on (rp.id=mc.papel)  "
            + "left join app.reg_movimiento_ficha as mf on (mf.movimiento=m.id)  "
            + "left join app.reg_acto as ac on (ac.id=m.acto)  "
            + "left join app.reg_libro as li on (li.id=m.libro)  "
            + "left join app.reg_ficha as fi on (fi.id=mf.ficha)  "
            + "left join app.reg_ente_judiciales as enj on (enj.id=m.ente_judicial)  "
            + "left join app.reg_domicilio as cton on (cton.id=m.domicilio) "
            + "LEFT JOIN ctlg.uaf_tipo_bien tbien ON (tbien.id = fi.uaf_tipo_bien) "
            + "LEFT JOIN app.reg_movimiento_representante mr ON (mr.movimiento = m.id) "
            + "LEFT JOIN app.ctlg_cargo crg ON (mr.cargo = crg.id) "
            + "LEFT JOIN app.reg_ente_interviniente comp ON (comp.id = mr.ente_interv) "
            + "where m.estado = 'AC' AND li.anexo_tres_mercantil is TRUE AND "
            + "TO_CHAR(m.fecha_inscripcion, 'DD/MM/YYYY') = ?;";
    public static String getMovsAnexoUaf = "select mo.id, mo.num_repertorio as repertorio, mo.num_inscripcion as inscripcion, "
            + "mo.fecha_inscripcion as fechainscripcion, tr.codigo as tramite, "
            + "case when (length(mo.observacion) > 14) then substring(mo.observacion from 1 for 245) "
            + "else 'LIBRO: '||li.nombre||', ACTO: '||ac.nombre end as descripcion, "
            + "fi.clave_catastral as catastro, de.cuantia, tb.codigo, fi.direccion_bien as direccion "
            + "from app.reg_movimiento mo inner join ctlg.uaf_tramite tr on tr.acto = mo.acto "
            + "left join flow.regp_tareas_tramite tt on tt.id = mo.num_tramite "
            + "left join flow.regp_liquidacion_detalles de on de.id = tt.detalle "
            + "left join app.reg_libro li on li.id = mo.libro "
            + "left join app.reg_acto ac on ac.id = mo.acto "
            + "left join app.reg_ficha fi on fi.id = "
            + "(select max(ficha) from app.reg_movimiento_ficha where movimiento = mo.id) "
            + "left join ctlg.uaf_tipo_bien tb on tb.id = fi.uaf_tipo_bien "
            + "where de.cuantia >= 10000 and mo.estado = 'AC' and "
            + "to_char(mo.fecha_inscripcion, 'MM-yyyy') = ? "
            + "order by mo.num_repertorio, mo.fecha_inscripcion";
    public static String getTiposBienUaf = "Select t from UafTipoBien t where t.estado is true order by t.id";
    public static String getUafPapeles = "Select u from UafPapelInterv u where u.estado = 1 order by u.nombre";
    public static String getCantidadTramitesUaf = "select count(mo.*) from app.reg_movimiento mo "
            + "inner join ctlg.uaf_tramite tr on tr.acto = mo.acto "
            + "left join flow.regp_tareas_tramite tt on tt.id = mo.num_tramite "
            + "left join flow.regp_liquidacion_detalles de on de.id = tt.detalle "
            + "where de.cuantia >= 10000 and mo.estado = 'AC' and "
            + "to_char(mo.fecha_inscripcion, 'MM-yyyy') = ?";
    public static String getCantidadPersonasUaf = "select count(mc.*) from app.reg_movimiento mo "
            + "inner join ctlg.uaf_tramite tr on tr.acto = mo.acto "
            + "left join flow.regp_tareas_tramite tt on tt.id = mo.num_tramite "
            + "left join flow.regp_liquidacion_detalles de on de.id = tt.detalle "
            + "left join app.reg_movimiento_cliente mc on mc.movimiento = mo.id "
            + "where de.cuantia >= 10000 and mo.estado = 'AC' and "
            + "to_char(mo.fecha_inscripcion, 'MM-yyyy') = ?";
    public static String getTotalCuantiUaf = "select sum(de.cuantia) from app.reg_movimiento mo "
            + "inner join ctlg.uaf_tramite tr on tr.acto = mo.acto "
            + "left join flow.regp_tareas_tramite tt on tt.id = mo.num_tramite "
            + "left join flow.regp_liquidacion_detalles de on de.id = tt.detalle "
            + "where de.cuantia >= 10000 and mo.estado = 'AC' and "
            + "to_char(mo.fecha_inscripcion, 'MM-yyyy') = ?";
    public static String getParticipantesNrpm = "Select m from RegMovimientoParticipante m "
            + "where to_char(m.movimiento.fechaInscripcion, 'MM-yyyy') = :fechacorte";
    public static String getIntervinientesByMov = "Select mc.enteInterv from RegMovimientoCliente mc "
            + "where mc.movimiento.id = :idmovimiento";
    public static String getComprobantesBySolicitante = "Select l from RegpLiquidacion l where l.solicitante.id = :codigosolicitante "
            + "and l.numeroAutorizacion is not null and l.codigoComprobante is not null and l.claveAcceso is not null "
            + "and (l.fechaIngreso between :desde and :hasta) order by l.numeroComprobante";
    public static String getParticipanteByMov = "Select m from RegMovimientoParticipante m where m.movimiento.id = :idmovimiento";
    public static String getCuantiaFromMov = "select de.cuantia from flow.regp_liquidacion_detalles de "
            + "left join flow.regp_tareas_tramite tt on tt.detalle = de.id "
            + "left join app.reg_movimiento mo on mo.num_tramite = tt.id where mo.id = ?";
    public static String getTramiteUafMov = "select tr.acto from app.reg_movimiento mo "
            + "inner join ctlg.uaf_tramite tr on tr.acto = mo.acto where mo.id = ?";
    public static String getValorNotasCredito = "Select sum(n.valorTotal) from RenNotaCredito n where n.fechaEmision between :desde and :hasta";
    public static String getValorNotasCreditoByCaja = "Select sum(n.valorTotal) from RenNotaCredito n where n.cajaEmision.usuario.id = :caja and "
            + "n.fechaEmision between :desde and :hasta";
    public static String beneficiariosLiquidacion = "Select distinct rei.ente from RegpIntervinientes rei where rei.liquidacion.liquidacion = :proforma";
    public static String inactivarExoneraciones = "update flow.regp_liquidacion_exoneracion set estado = false where liquidacion = ?";
    public static String updateRegistradores = "update app.reg_registrador set actual = false, firma_juridico = false, firma_electronica = false";
    public static String updateRegistradoresJuridico = "update app.reg_registrador set firma_juridico = false";
    public static String getContratosByNombre = "select id, nombre, fijo, valor from app.reg_acto where estado is true and nombre like ? order by nombre";
    public static String getCertificadosByEnte = "select ce.id as idcertificado, ce.num_certificado as numerocertificado, "
            + "fi.num_ficha as numeroficha, ht.num_tramite as numerotramite, ce.fecha_emision as fechaemision, ce.tipo_certificado as tipocertificado "
            + "from app.reg_certificado ce left join app.reg_ficha fi on fi.id = ce.ficha left join flow.regp_tareas_tramite tt on tt.id = ce.tarea_tramite "
            + "left join flow.historico_tramites ht on ht.id = tt.tramite left join flow.regp_intervinientes vi on vi.liquidacion = tt.detalle "
            + "left join app.cat_ente en on en.id = vi.ente ";
    public static String getEstadoPagoLiquidacion = "select estado_pago from flow.regp_liquidacion where num_tramite_rp = ?";
    public static String getCorreoSolicitudOnLine = "select sol_correo from flow.pub_solicitud where tramite = ?";
    public static String getEstadoByLiquidacion = "select estado from flow.regp_liquidacion where id = ?";
    public static String getCantidadUafINT = "select count(mc.*) from app.reg_movimiento mo inner join ctlg.uaf_tramite tr on tr.acto = mo.acto "
            + "left join app.reg_movimiento_cliente mc on mc.movimiento = mo.id inner join ctlg.uaf_rol_interv ro on ro.id = mc.uaf_rol "
            + "inner join ctlg.uaf_papel_interv pa on pa.id = mc.uaf_papel left join app.reg_ente_interviniente ein on ein.id = mc.ente_interv "
            + "left join flow.regp_tareas_tramite tt on tt.id = mo.num_tramite left join flow.regp_liquidacion_detalles de on de.id = tt.detalle "
            + "where de.cuantia >= 10000 and mo.estado = 'AC' and to_char(mo.fecha_inscripcion, 'MM-yyyy') = ? and "
            + "(length(ein.ced_ruc) = 10 or length(ein.ced_ruc) = 13)";
    public static String getCantidadUafTRA = "select count(distinct(mo)) from app.reg_movimiento mo inner join ctlg.uaf_tramite tr on tr.acto = mo.acto "
            + "left join app.reg_movimiento_cliente mc on mc.movimiento = mo.id inner join ctlg.uaf_rol_interv ro on ro.id = mc.uaf_rol "
            + "inner join ctlg.uaf_papel_interv pa on pa.id = mc.uaf_papel left join flow.regp_tareas_tramite tt on tt.id = mo.num_tramite "
            + "left join flow.regp_liquidacion_detalles de on de.id = tt.detalle left join app.reg_libro li on li.id = mo.libro "
            + "left join app.reg_acto ac on ac.id = mo.acto left join app.reg_ficha fi on fi.id = "
            + "(select max(ficha) from app.reg_movimiento_ficha where movimiento = mo.id) left join ctlg.uaf_tipo_bien tb on tb.id = fi.uaf_tipo_bien "
            + "where de.cuantia >= 10000 and mo.estado = 'AC' and to_char(mo.fecha_inscripcion, 'MM-yyyy') = ?";
    public static String updateLiquidacionBlock = "update flow.regp_liquidacion set estado = 0 where id = ?";
    public static String getTramiteMuniMov = "select tramite_muni from app.reg_movimiento where tramite_muni = ?";
    public static String getCertificadosToDownload = "select ce from RegCertificado ce where ce.codVerificacion is not null and ce.numTramite = :tramite";
    public static String getMovimintosToDownload = "select rg from RegMovimiento rg where rg.codVerificacion is not null and rg.tramite.tramite.numTramite = :tramite";
    public static String getCertificados = "select ce from RegCertificado ce where ce.numTramite = :tramite";
    public static String getOidSolicitud = "select oid_document from flow.pub_solicitud where tramite = ?";
    public static String getOid1Solicitud = "select oid_document1 from flow.pub_solicitud where tramite = ?";
    public static String getOid2Solicitud = "select oid_document2 from flow.pub_solicitud where tramite = ?";
    public static String getOid3Solicitud = "select oid_document3 from flow.pub_solicitud where tramite = ?";
    public static String updateCertificadosRevisados = "update app.reg_certificado set secuencia = 1, registrador = ? where num_tramite = ?";
    public static String updateCertificadosCorreccion = "update app.reg_certificado set secuencia = 0, print_online = 0 where num_tramite = ?";
    public static String consultaEstadoTramite = "select * , (case when hoy > resta then 1 else hoy/resta end) as avance from "
            + "(select ht. num_tramite as numerotramite, "
            + "cast(extract('epoch' from ht.fecha_ingreso) as bigint) as fechaingreso, "
            + "cast(extract('epoch' from ht.fecha_entrega) as bigint) as fechaentrega, "
            + "liq.certificado as tipocertificado, extract(day from (ht.fecha_entrega - ht.fecha_ingreso)) as resta, "
            + "extract(day from (now() - ht.fecha_ingreso)) as hoy from flow.historico_tramites ht "
            + "left join app.cat_ente en on en.id = ht.solicitante left join flow.regp_liquidacion liq on liq.tramite = ht.id "
            + "where ht.id_proceso is not null and ht.fecha_ingreso is not null and ht.fecha_entrega is not null and en.ci_ruc = ? "
            + "order by ht.fecha_ingreso desc) as consulta ";

    public static String provincias = "SELECT nombre_provincia FROM ctlg.nrpm_canton GROUP BY 1;";
    public static String cantonesProvincias = "SELECT nombre_canton FROM ctlg.nrpm_canton WHERE nombre_provincia = ? GROUP BY 1;";
    public static String updateLiqudiacion = "update flow.regp_liquidacion set estado = ? where id = ?";

    public static String getNotaCreditoById = "Select r from RenNotaCredito r where r.id = :id";

    public static String getAranceles = "SELECT r FROM RegArancel r ORDER BY r.denominacion";
    public static String getNotaDevolutivaByTramite = "SELECT n FROM RegpNotaDevolutiva n WHERE n.tramite.id = :idTramite ORDER BY n.id DESC";

    public static String getObservacionesByTramite = "select ob from Observaciones ob where ob.idTramite.id = :idTramite and ob.estado = true";

    //REN PAGO DETALLES
    public static String getPagoDetalles = "SELECT pd FROM RenPagoDetalle pd WHERE pd.pago.id = :idPago";

    public static String getRegActoListByTipo = "Select a from RegActo a where a.estado=true and a.tipoActo.id = :idTipoActo ORDER BY a.nombre ASC";
    public static String getTramitesBandeja = "SELECT ta.id, num_tramite \"numTramite\", id_proceso_temp \"idProcesoTemp\", entregado, "
            + "ta.fecha_ingreso \"fechaIngreso\", fecha_entrega \"fechaEntrega\", nombre_propietario \"nombrePropietario\", "
            + "blocked, task_id \"taskId\", form_key \"formKey\", priority, name_ \"name\", candidate, nd.esta_reingresado \"estaReingresado\" "
            + ", l.actos, l.num_actos \"numActos\", ta.rev_ revisiones "
            + "FROM flow.tareas_activas ta LEFT JOIN (SELECT MAX(fecha_ingreso), tramite, esta_reingresado FROM flow.regp_nota_devolutiva GROUP BY 2,3) nd ON nd.tramite = ta.id "
            + "LEFT JOIN (SELECT rl.num_tramite_rp, string_agg(ra.nombre, ', ') actos, COUNT(rld) num_actos "
            + "FROM flow.regp_liquidacion rl INNER JOIN flow.regp_liquidacion_detalles rld ON rld.liquidacion = rl.id "
            + "INNER JOIN app.reg_acto ra ON ra.id = rld.acto GROUP BY 1) l ON ta.num_tramite = l.num_tramite_rp "
            + "WHERE assignee = ? order by fecha_entrega";
    public static String getTramitesBandeja1 = "SELECT ta.id, num_tramite \"numTramite\", id_proceso_temp \"idProcesoTemp\", entregado, "
            + "ta.fecha_ingreso \"fechaIngreso\", fecha_entrega \"fechaEntrega\", nombre_propietario \"nombrePropietario\", "
            + "blocked, task_id \"taskId\", form_key \"formKey\", priority, name_ \"name\", candidate, nd.esta_reingresado \"estaReingresado\" "
            + ", l.actos, l.num_actos \"numActos\", ta.rev_ revisiones, assignee "
            + "FROM flow.tareas_activas ta LEFT JOIN (SELECT MAX(fecha_ingreso), tramite, esta_reingresado FROM flow.regp_nota_devolutiva GROUP BY 2,3) nd ON nd.tramite = ta.id "
            + "LEFT JOIN (SELECT rl.num_tramite_rp, string_agg(ra.nombre, ', ') actos, COUNT(rld) num_actos "
            + "FROM flow.regp_liquidacion rl INNER JOIN flow.regp_liquidacion_detalles rld ON rld.liquidacion = rl.id "
            + "INNER JOIN app.reg_acto ra ON ra.id = rld.acto GROUP BY 1) l ON ta.num_tramite = l.num_tramite_rp order by fecha_entrega";
    public static String getTramitesBandeja1_1 = "SELECT ta.id, num_tramite \"numTramite\", id_proceso_temp \"idProcesoTemp\", entregado, "
            + "ta.fecha_ingreso \"fechaIngreso\", fecha_entrega \"fechaEntrega\", nombre_propietario \"nombrePropietario\", "
            + "blocked, task_id \"taskId\", form_key \"formKey\", priority, name_ \"name\", candidate, nd.esta_reingresado \"estaReingresado\" "
            + ", l.actos, l.num_actos \"numActos\", ta.rev_ revisiones, assignee "
            + "FROM flow.tareas_activas ta LEFT JOIN (SELECT MAX(fecha_ingreso), tramite, esta_reingresado FROM flow.regp_nota_devolutiva GROUP BY 2,3) nd ON nd.tramite = ta.id "
            + "LEFT JOIN (SELECT rl.num_tramite_rp, string_agg(ra.nombre, ', ') actos, COUNT(rld) num_actos "
            + "FROM flow.regp_liquidacion rl INNER JOIN flow.regp_liquidacion_detalles rld ON rld.liquidacion = rl.id "
            + "INNER JOIN app.reg_acto ra ON ra.id = rld.acto GROUP BY 1) l ON ta.num_tramite = l.num_tramite_rp order by fecha_ingreso";
    public static String getTramitesBandeja2 = "SELECT ta.id, num_tramite \"numTramite\", id_proceso_temp \"idProcesoTemp\", entregado, "
            + "ta.fecha_ingreso \"fechaIngreso\", fecha_entrega \"fechaEntrega\", nombre_propietario \"nombrePropietario\", "
            + "blocked, task_id \"taskId\", form_key \"formKey\", priority, name_ \"name\", candidate, nd.esta_reingresado \"estaReingresado\" "
            + ", l.actos, l.num_actos \"numActos\", ta.rev_ revisiones "
            + "FROM flow.tareas_activas ta LEFT JOIN (SELECT MAX(fecha_ingreso), tramite, esta_reingresado FROM flow.regp_nota_devolutiva GROUP BY 2,3) nd ON nd.tramite = ta.id "
            + "LEFT JOIN (SELECT rl.num_tramite_rp, string_agg(ra.nombre, ', ') actos, COUNT(rld) num_actos "
            + "FROM flow.regp_liquidacion rl INNER JOIN flow.regp_liquidacion_detalles rld ON rld.liquidacion = rl.id "
            + "INNER JOIN app.reg_acto ra ON ra.id = rld.acto GROUP BY 1) l ON ta.num_tramite = l.num_tramite_rp "
            + "WHERE assignee = ? order by fecha_ingreso";
    public static String getTramitesBandejaDesdeHasta = "SELECT ta.id, num_tramite \"numTramite\", id_proceso_temp \"idProcesoTemp\", entregado, "
            + "ta.fecha_ingreso \"fechaIngreso\", fecha_entrega \"fechaEntrega\", nombre_propietario \"nombrePropietario\", "
            + "blocked, task_id \"taskId\", form_key \"formKey\", priority, name_ \"name\", candidate, nd.esta_reingresado \"estaReingresado\" "
            + ", l.actos, l.num_actos \"numActos\", ta.rev_ revisiones "
            + "FROM flow.tareas_activas ta LEFT JOIN (SELECT MAX(fecha_ingreso), tramite, esta_reingresado FROM flow.regp_nota_devolutiva GROUP BY 2,3) nd ON nd.tramite = ta.id "
            + "LEFT JOIN (SELECT rl.num_tramite_rp, string_agg(ra.nombre, ', ') actos, COUNT(rld) num_actos "
            + "FROM flow.regp_liquidacion rl INNER JOIN flow.regp_liquidacion_detalles rld ON rld.liquidacion = rl.id "
            + "INNER JOIN app.reg_acto ra ON ra.id = rld.acto GROUP BY 1) l ON ta.num_tramite = l.num_tramite_rp "
            + "WHERE assignee = ? AND fecha_entrega BETWEEM ? AND ? order by fecha_entrega";
    public static String getTramitesBandeja1DesdeHasta = "SELECT ta.id, num_tramite \"numTramite\", id_proceso_temp \"idProcesoTemp\", entregado, "
            + "ta.fecha_ingreso \"fechaIngreso\", fecha_entrega \"fechaEntrega\", nombre_propietario \"nombrePropietario\", "
            + "blocked, task_id \"taskId\", form_key \"formKey\", priority, name_ \"name\", candidate, nd.esta_reingresado \"estaReingresado\" "
            + ", l.actos, l.num_actos \"numActos\", ta.rev_ revisiones, assignee "
            + "FROM flow.tareas_activas ta LEFT JOIN (SELECT MAX(fecha_ingreso), tramite, esta_reingresado FROM flow.regp_nota_devolutiva GROUP BY 2,3) nd ON nd.tramite = ta.id "
            + "LEFT JOIN (SELECT rl.num_tramite_rp, string_agg(ra.nombre, ', ') actos, COUNT(rld) num_actos "
            + "FROM flow.regp_liquidacion rl INNER JOIN flow.regp_liquidacion_detalles rld ON rld.liquidacion = rl.id "
            + "INNER JOIN app.reg_acto ra ON ra.id = rld.acto GROUP BY 1) l ON ta.num_tramite = l.num_tramite_rp "
            + "WHERE  AND fecha_entrega BETWEEM ? AND ? order by fecha_entrega";

    public static String getTramitesBandeja1Group(String usuario) {
        StringBuilder sql = new StringBuilder("SELECT ta.id, num_tramite \"numTramite\", id_proceso_temp \"idProcesoTemp\", entregado, ");
        sql.append("ta.fecha_ingreso \"fechaIngreso\", fecha_entrega \"fechaEntrega\", nombre_propietario \"nombrePropietario\", ");
        sql.append("blocked, task_id \"taskId\", form_key \"formKey\", priority, name_ \"name\", candidate, nd.esta_reingresado \"estaReingresado\" ");
        sql.append(", l.actos, l.num_actos \"numActos\", ta.rev_ revisiones, CASE WHEN assignee IS NULL THEN candidate ELSE assignee END assignee ");
        sql.append(", COALESCE(e.nombres, '') || ' ' || COALESCE(e.apellidos, '') nombres ");
        sql.append("FROM flow.tareas_activas ta LEFT JOIN (SELECT MAX(fecha_ingreso), tramite, esta_reingresado ");
        sql.append("FROM flow.regp_nota_devolutiva GROUP BY 2,3) nd ON nd.tramite = ta.id ");
        sql.append("LEFT JOIN (SELECT rl.num_tramite_rp, string_agg(ra.nombre, ', ') actos, COUNT(rld) num_actos ");
        sql.append("FROM flow.regp_liquidacion rl INNER JOIN flow.regp_liquidacion_detalles rld ON rld.liquidacion = rl.id ");
        sql.append("INNER JOIN app.reg_acto ra ON ra.id = rld.acto GROUP BY 1) l ON ta.num_tramite = l.num_tramite_rp ");
        sql.append("LEFT JOIN app.acl_user us ON us.usuario = assignee LEFT JOIN app.cat_ente e ON e.id = us.ente ");
        sql.append("WHERE name_ NOT IN ('Entrega Tramite', 'Entrega Certificado', 'Nota Devolutiva','Entrega de Trámite') ");
        if (usuario != null) {
            sql.append("AND assignee = ? ");
        }
        sql.append("ORDER BY assignee, fecha_entrega");
        return sql.toString();
    }

    public static String getTramitesBandeja2Group(int filter, int order) {
        StringBuilder sql = new StringBuilder("SELECT ta.id, num_tramite \"numTramite\", id_proceso_temp \"idProcesoTemp\", entregado, ");
        sql.append("ta.fecha_ingreso \"fechaIngreso\", fecha_entrega \"fechaEntrega\", nombre_propietario \"nombrePropietario\", ");
        sql.append("to_date(to_char(ta.fecha_ingreso, 'yyyy-MM-dd'), 'yyyy-MM-dd') \"fecIngSH\", ");
        sql.append("to_date(to_char(ta.fecha_entrega, 'yyyy-MM-dd'), 'yyyy-MM-dd') \"fecEntSH\", ");
        sql.append("blocked, task_id \"taskId\", form_key \"formKey\", priority, name_ \"name\", candidate, nd.esta_reingresado \"estaReingresado\" ");
        sql.append(", l.actos, l.num_actos \"numActos\", ta.rev_ revisiones, CASE WHEN assignee IS NULL THEN candidate ELSE assignee END assignee ");
        sql.append(", COALESCE(e.nombres, '') || ' ' || COALESCE(e.apellidos, '') nombres ");
        sql.append("FROM flow.tareas_activas ta LEFT JOIN (SELECT MAX(fecha_ingreso), tramite, esta_reingresado ");
        sql.append("FROM flow.regp_nota_devolutiva GROUP BY 2,3) nd ON nd.tramite = ta.id ");
        sql.append("LEFT JOIN (SELECT rl.num_tramite_rp, string_agg(ra.nombre, ', ') actos, COUNT(rld) num_actos ");
        sql.append("FROM flow.regp_liquidacion rl INNER JOIN flow.regp_liquidacion_detalles rld ON rld.liquidacion = rl.id ");
        sql.append("INNER JOIN app.reg_acto ra ON ra.id = rld.acto GROUP BY 1) l ON ta.num_tramite = l.num_tramite_rp ");
        sql.append("LEFT JOIN app.acl_user us ON us.usuario = assignee LEFT JOIN app.cat_ente e ON e.id = us.ente ");
        sql.append("WHERE name_ IN ('Emitir Certificado', 'Analisis Y Busqueda', 'Completar Datos', 'Inscribir') ");
        switch (filter) {
            case 1:
                sql.append("AND assignee = ? ");
                break;
            case 2:
                sql.append("AND (ta.fecha_ingreso between ? and ?)");
                break;
            case 3:
                sql.append("AND (ta.fecha_entrega between ? and ?)");
                break;
            case 4:
                sql.append("AND assignee = ? ");
                sql.append("AND (ta.fecha_ingreso between ? and ?)");
                break;
            case 5:
                sql.append("AND assignee = ? ");
                sql.append("AND (ta.fecha_entrega between ? and ?)");
                break;
        }
        switch (order) {
            case 1:
                sql.append("ORDER BY assignee, fecha_entrega");
                break;
            case 2:
                sql.append("ORDER BY \"fecIngSH\", assignee, id_proceso_temp, num_tramite");
                break;
            case 3:
                sql.append("ORDER BY \"fecEntSH\", assignee, id_proceso_temp, num_tramite");
                break;
        }
        return sql.toString();
    }

    public static String getRegpIntervinientes = "SELECT r  FROM RegpIntervinientes r WHERE r.liquidacion.id = :idRegpIntervinientes";

    //TEMP
    public static String getMinDateUsersFicha = "Select min(ut.fechaFicha) from TarUsuarioTareas ut where ut.estadoFicha = true and ut.rol.id = :rol";
    public static String getMinUserFicha = "Select ut from TarUsuarioTareas ut where ut.estadoFicha = true and ut.rol.id = :rol and "
            + "ut.fechaFicha = :fecha and ut.cantidad = (Select min(u.cantidad) from TarUsuarioTareas u where u.estadoFicha = true and "
            + "u.rol.id = :rol and fechaFicha = :fecha)";

    public static String getUsersMinData = "SELECT c FROM TarUsuarioTareas c WHERE c.estado = true AND c.rol.id = :rol "
            + "AND c.id NOT IN (SELECT x.id FROM TarUsuarioTareas x WHERE :fechaEntrega BETWEEN x.fechaSalida AND x.fechaIngreso) AND c.fecha <= :fechaEntrega1 "
            + "AND ((CASE WHEN c.cantidad = c.peso THEN c.cantidadAux ELSE c.cantidad END) < c.peso OR c.dias >= 0) "
            + "ORDER BY c.fecha ASC, c.dias ASC, (CASE WHEN c.peso = c.cantidad THEN c.cantidadAux ELSE c.cantidad END) ASC";

    public static String getUsersUpdateFecha = "UPDATE conf.tar_usuario_tareas SET fecha = ?,cantidad = 0, cantidad_aux = 0, dias = 0 WHERE rol = ? AND estado = true AND fecha <= ?;";
    public static String getMaxFechaEntrega = "SELECT MAX(c.fecha) FROM TarUsuarioTareas c WHERE c.estado = true AND c.rol.id = :rol";
    public static String getFichaFromTramite = "Select rfm.ficha from RegFichaMarginacion rfm where rfm.tramite.numTramite = :numero";
    public static String getFichaFromMovimiento = "Select rmf.ficha from RegMovimientoFicha rmf where rmf.movimiento.id = :idmovimiento";
    public static String getFichaFromLiquidacion = "Select l.ficha from RegpLiquidacion l where l.numTramiteRp = :numero";
    public static String RegFichaCount = "SELECT COUNT(c) FROM RegFicha c INNER JOIN c.regMovimientoFichaCollection mf INNER JOIN mf.movimiento mv WHERE mv.numInscripcion = :numInscripcion AND TO_CHAR(mv.fechaInscripcion, 'YYYY') = :anioInscripcion";
    public static String getMovsByTramite = "Select mo from RegMovimiento mo WHERE mo.tramite.tramite.numTramite = :numeroTramite order by mo.fechaInscripcion";
    public static String getInscripcionesByTramite = "Select mo from RegMovimiento mo WHERE mo.numInscripcion is not null and mo.estado = 'AC' "
            + "and mo.tramite.tramite.numTramite = :numeroTramite";

    public static String getSqlFicha(String claveCatastral, String claveCatastralOld, Long numFicha) {
        StringBuilder buffer = new StringBuilder("SELECT STRING_AGG(f.num_ficha||'', ',' ORDER BY f.num_ficha) FROM app.reg_ficha f WHERE ");
        if (numFicha != null) {
            buffer.append(" f.num_ficha <> ");
            buffer.append(numFicha);
            if (!Utils.isEmpty(claveCatastral).isEmpty() || !Utils.isEmpty(claveCatastralOld).isEmpty()) {
                buffer.append(" AND ");
            }
        }
        if (!Utils.isEmpty(claveCatastral).isEmpty()) {
            buffer.append("(f.clave_catastral = '");
            buffer.append(claveCatastral);
            buffer.append("' OR f.clave_catastral_old = '");
            buffer.append(claveCatastral);
            buffer.append("')");
        }
        if (!Utils.isEmpty(claveCatastralOld).isEmpty()) {
            if (!Utils.isEmpty(claveCatastral).isEmpty()) {
                buffer.append(" OR ");
            }
            buffer.append("(f.clave_catastral = '");
            buffer.append(claveCatastralOld);
            buffer.append("' OR f.clave_catastral_old = '");
            buffer.append(claveCatastralOld);
            buffer.append("')");
        }
        return buffer.toString();
    }

    public static String getFacturasAutorizadasComprobacion = "Select r from RegpLiquidacion r "
            + "where r.reingreso = false and r.estadoWs = 'RECIBIDA;AUTORIZADO' and r.totalPagar > 0 "
            + " and to_char(r.fechaIngreso, 'dd/MM/yyyy') = :fecha "
            + " and r.estadoPago.id = 2 and r.estadoLiquidacion.id = 2 order by r.fechaIngreso";

    public static String getTramitesRecibidos = "select count(*) from flow.regp_liquidacion "
            + "where inscripcion = ? "
            + "and estado_liquidacion in (1,2) "
            + "and CAST(fecha_creacion as date) between CAST(? AS DATE) AND CAST(? AS DATE)";

    public static String getTramitesIngresados = "select count(*) from flow.regp_liquidacion "
            + "where inscripcion = ? "
            + "and estado_liquidacion = 2 "
            + "and CAST(fecha_ingreso as date) between CAST(? AS DATE) AND CAST(? AS DATE)";

    public static String getTramitesRealizados = "select count(*) from flow.estado_tramite st "
            + "where st.inscripcion = ? "
            + "and st.estado = 'FINALIZADO' "
            + "and CAST(st.fecha_ingreso as date) between CAST(? AS DATE) AND CAST(? AS DATE)";

    public static String getTramitesPendientes = "select count(*) from flow.estado_tramite st "
            + "where st.inscripcion = ? "
            + "and st.estado = 'PENDIENTE' "
            + "and CAST(st.fecha_ingreso as date) between CAST(? AS DATE) AND CAST(? AS DATE)";

    public static String getTramitesByEstado = "select count(*) from flow.estado_tramite st "
            + "where st.inscripcion = ? "
            + "and st.estado = ? "
            + "and CAST(st.fecha_ingreso as date) between CAST(? AS DATE) AND CAST(? AS DATE)";

    public static String getLibrosPorActo = "select li.id, li.nombre, count(acto.*) cantidad "
            + "from flow.regp_liquidacion_detalles det "
            + "inner join flow.regp_liquidacion liq on det.liquidacion = liq.id "
            + "inner join flow.estado_tramite st on st.num_tramite = liq.num_tramite_rp "
            + "inner join app.reg_acto acto on det.acto = acto.id "
            + "inner join app.reg_libro li on li.id = acto.libro "
            + "where liq.inscripcion = true "
            + "and st.estado = ? "
            + "and CAST(liq.fecha_ingreso as date) between CAST(? AS DATE) AND CAST(? AS DATE) "
            + "group by 1, 2 order by 3 desc";

    public static String getLibrosPorActoTotales = "select li.id, li.nombre, count(acto.*) cantidad "
            + "from flow.regp_liquidacion_detalles det "
            + "inner join flow.regp_liquidacion liq on det.liquidacion = liq.id "
            + "inner join flow.estado_tramite st on st.num_tramite = liq.num_tramite_rp "
            + "inner join app.reg_acto acto on det.acto = acto.id "
            + "inner join app.reg_libro li on li.id = acto.libro "
            + "where liq.inscripcion = true "
            + "and CAST(liq.fecha_ingreso as date) between CAST(? AS DATE) AND CAST(? AS DATE) "
            + "group by 1, 2 order by 3 desc";

    public static String getTotalTramitesLibro = "select * from flow.totales_estado_tramites(?, ?, ?, ?)";

    public static String getActosPorLibro = "select distinct acto.id, acto.nombre "
            + "from flow.regp_liquidacion_detalles det "
            + "inner join flow.regp_liquidacion liq on det.liquidacion = liq.id "
            + "inner join app.reg_acto acto on det.acto = acto.id "
            + "where liq.inscripcion = true "
            + "and acto.libro = ? "
            + "and CAST(liq.fecha_ingreso as date)  between CAST(? AS DATE) AND CAST(? AS DATE)";

    public static String getTotalActosPorDia = "select count(*) "
            + "from flow.regp_liquidacion_detalles det "
            + "inner join flow.regp_liquidacion liq on det.liquidacion = liq.id "
            + "inner join flow.estado_tramite st on st.num_tramite = liq.num_tramite_rp "
            + "inner join app.reg_acto acto on det.acto = acto.id "
            + "where liq.inscripcion = ? "
            + "and acto.id = ? "
            + "and st.estado = ? "
            + "and CAST(liq.fecha_ingreso as date) = CAST(? AS DATE)";
    public static String getTotalActosPorDia1 = "select count_ from (CAST(liq.fecha_ingreso as date) fecha, select count(*) count_ "
            + "from flow.regp_liquidacion_detalles det "
            + "inner join flow.regp_liquidacion liq on det.liquidacion = liq.id "
            + "inner join flow.estado_tramite st on st.num_tramite = liq.num_tramite_rp "
            + "inner join app.reg_acto acto on det.acto = acto.id "
            + "where liq.inscripcion = ? "
            + "and acto.id = ? "
            + "and st.estado = ? "
            + "and CAST(liq.fecha_ingreso as date) IN ((SELECT dias::DATE "
            + "FROM generate_series(DATE ?, DATE ? - interval '1 day' "
            + ", interval '1 day') dias WHERE extract('ISODOW' FROM dias) < 6)) GROUP BY 1 ) xx ORDER BY fecha ";

    public static String getTotalActosIngresadosPorDia = "select count(*) "
            + "from flow.regp_liquidacion_detalles det "
            + "inner join flow.regp_liquidacion liq on det.liquidacion = liq.id "
            + "inner join flow.estado_tramite st on st.num_tramite = liq.num_tramite_rp "
            + "inner join app.reg_acto acto on det.acto = acto.id "
            + "where liq.inscripcion = ? "
            + "and acto.id = ? "
            + "and st.estado in ('PENDIENTE', 'FINALIZADO') "
            + "and CAST(liq.fecha_ingreso as date) = CAST(? AS DATE)";

    public static String getTramitesEstadistica = "select distinct det.liquidacion from RegpLiquidacionDetalles det where "
            + "det.acto.id = :actoID AND to_date(to_char(det.liquidacion.fechaIngreso,'dd-MM-yyyy'),'dd-MM-yyyy')=:fechaJS";

    public static String getTramitesEstadisticaUsuario = "select num_tramite "
            + "FROM flow.tramites_asignados ta "
            + "WHERE ta.usuario = ? "
            + "and ta.estado = ? "
            + "and CAST(ta.fecha_estado_tramite as date) = CAST(? AS DATE) ";

    public static String getTramitesEstadisticaPendientesUsuario = "select num_tramite "
            + "FROM flow.tramites_asignados ta "
            + "WHERE ta.usuario = ? "
            + "and CAST(ta.fecha_ingreso_tramite as date) = CAST(? AS DATE) ";

    public static String getRenLiquidacionTramitesByEstado = "select count(*) from flow.estado_tramite st "
            + "where st.num_tramite = ? "
            + "and st.estado = ? ";

    public static String getTotalTramitesEstadoXUSusario = "SELECT COUNT(ta.*)  "
            + "FROM flow.tramites_asignados ta "
            + "WHERE ta.usuario = ? "
            + "and ta.estado = ? "
            + "and CAST(ta.fecha_estado_tramite as date) = CAST(? AS DATE) ";

    public static String getTotalTramitesPendienteXUSusario = "SELECT COUNT(ta.*)  "
            + "FROM flow.tramites_asignados ta "
            + "WHERE ta.usuario = ? "
            + "and CAST(ta.fecha_ingreso_tramite as date) = CAST(? AS DATE) ";

    public static String getTiempoEstimadoUsuario
            = "SELECT "
            + "ROUND (AVG (("
            + "SELECT count(*) "
            + "FROM  generate_series(st.fecha_ingreso_tramite, "
            + "st.fecha_entrega_tramite, interval  '1 day') the_day "
            + "WHERE  extract('ISODOW' FROM the_day) < 6) - "
            + "(CASE WHEN xpress = false THEN "
            + "(SELECT count(*) "
            + "FROM conf.feriados feriado "
            + "WHERE feriado.fecha between st.fecha_ingreso_tramite AND st.fecha_entrega_tramite) ELSE 0 END)),2)"
            + " AS tiempo_estimado "
            + "FROM flow.tramites_asignados st "
            + "WHERE st.usuario ~ ? AND "
            + "CAST(st.fecha_ingreso_tramite as date) between CAST(? AS DATE) AND CAST(? AS DATE) ";

    public static String getTiempoPromedioUsuario
            = "SELECT "
            + "ROUND (AVG (("
            + "SELECT count(*) "
            + "FROM  generate_series(st.fecha_ingreso_tramite, "
            + "st.fecha_estado_tramite, interval  '1 day') the_day "
            + "WHERE  extract('ISODOW' FROM the_day) < 6) - "
            + "(CASE WHEN xpress = false THEN "
            + "(SELECT count(*) "
            + "FROM conf.feriados feriado "
            + "WHERE feriado.fecha between st.fecha_ingreso_tramite AND st.fecha_estado_tramite) ELSE 0 END)),2)"
            + " AS tiempo_estimado "
            + "FROM flow.tramites_asignados st "
            + "WHERE st.usuario ~ ? AND "
            + "st.estado = 'FINALIZADO' AND "
            + "CAST(st.fecha_ingreso_tramite as date) between CAST(? AS DATE) AND CAST(? AS DATE) ";

    public static String getTiempoEstimadoUsuarioEstadoFinalizado
            = "SELECT "
            + "ROUND (AVG (("
            + "SELECT count(*) "
            + "FROM  generate_series(st.fecha_ingreso_tramite, "
            + "st.fecha_entrega_tramite, interval  '1 day') the_day "
            + "WHERE  extract('ISODOW' FROM the_day) < 6) - "
            + "(CASE WHEN xpress = false THEN "
            + "(SELECT count(*) "
            + "FROM conf.feriados feriado "
            + "WHERE feriado.fecha between st.fecha_ingreso_tramite AND st.fecha_entrega_tramite) ELSE 0 END)),2)"
            + " AS tiempo_estimado "
            + "FROM flow.tramites_asignados st "
            + "WHERE st.usuario ~ ? AND "
            + "st.estado = 'FINALIZADO' AND "
            + "CAST(st.fecha_ingreso_tramite as date) between CAST(? AS DATE) AND CAST(? AS DATE) ";

    ///ESTADISTICAS PARA CERTIFICADOS
    public static String getCertificadosStadistica = "select acto.id, acto.nombre, count(acto.*) cantidad "
            + "from flow.regp_liquidacion_detalles det "
            + "inner join flow.regp_liquidacion liq on det.liquidacion = liq.id "
            + "inner join flow.estado_tramite st on st.num_tramite = liq.num_tramite_rp "
            + "inner join app.reg_acto acto on det.acto = acto.id "
            + "where acto.solvencia = true "
            + "and st.estado = ? "
            + "and CAST(liq.fecha_ingreso as date) between CAST(? AS DATE) AND CAST(? AS DATE) "
            + "group by 1, 2 order by 3 desc";

    public static String getCertificadosStadisticaTotales = "select acto.id, acto.nombre, count(acto.*) cantidad "
            + "from flow.regp_liquidacion_detalles det "
            + "inner join flow.regp_liquidacion liq on det.liquidacion = liq.id "
            + "inner join flow.estado_tramite st on st.num_tramite = liq.num_tramite_rp "
            + "inner join app.reg_acto acto on det.acto = acto.id "
            + "where acto.solvencia = true "
            + "and CAST(liq.fecha_ingreso as date) between CAST(? AS DATE) AND CAST(? AS DATE) "
            + "group by 1, 2 order by 3 desc";

    public static String getActosCertificados = "select distinct acto.id, acto.nombre "
            + "from flow.regp_liquidacion_detalles det "
            + "inner join flow.regp_liquidacion liq on det.liquidacion = liq.id "
            + "inner join app.reg_acto acto on det.acto = acto.id "
            + "where liq.inscripcion = true "
            + "and acto.libro = ? "
            + "and CAST(liq.fecha_ingreso as date)  between CAST(? AS DATE) AND CAST(? AS DATE)";

    public static String getRegActoRequisitos = "select  ra.id \"requisitoActo\",  ac.id \"idActo\", ac.nombre acto, req.id \"idRequisito\", req.requisito, ra.requerido from app.reg_requisitos_actos ra inner join app.reg_acto ac on ra.acto = ac.id inner join app.reg_requisitos req ON req.id = ra.requisito where ra.estado = true and ac.solvencia = false and ac.id = ?";
    public static String getRegActoInscripcionesList = "select id,  nombre as acto,   descripcion_acto   \"descripcion\", false as certificado, false as \"tramiteBanca\" from app.reg_acto where estado = true and solvencia = false and acto_enlinea = true";
    public static String deleteRegMovimientoFile = "delete from app.reg_movimiento_file;";
    public static String alterSequenceRegMovimientoFile = "alter sequence app.reg_movimiento_file_id_seq restart 1;";
    public static String deleteRegpDocsTramite = "delete from flow.regp_docs_tramite;";
    public static String alterSequenceRegDocsTramite = "alter sequence flow.regp_docs_tramite_id_seq restart 1;";
    public static String getRegTablaCuantia = "Select tc from RegTablaCuantia tc where :cantidad1 >= tc.valor1 and :cantidad2 <= tc.valor2";
    public static String getRegTablaCuantiaLimite = "Select tc from RegTablaCuantia tc where tc.valor2 is null";
    public static String getRegMovimientoAnexoCotad = "SELECT "
            + "mov.id \"movimiento_id\" , mov.num_inscripcion \"num_inscripcion\", mov.fecha_inscripcion \"fecha_inscripcion\", mov.folio_inicio \"folio_inicio\" , mov.folio_fin \"folio_fin\", mov.num_tomo \"num_tomo\", mov.num_repertorio \"num_repertorio\", "
            + "mov.fecha_oto \"fecha_oto\", mov.escrit_juic_prov_resolucion \"escrit_juic_prov_resolucion\", mov.fecha_resolucion \"fecha_resolucion\",mov.observacion \"observacion\","
            + "acto.nombre \"nombre_acto\", libro.nombre \"nombre_libro\", ente.nombre \"ente_judicial\",canton.nombre \"nombre_canton\","
            + "mov.avaluo_municipal_cadena \"avaluo_municipal_cadena\", mov.cuantia_cadena \"cuantia_cadena\", mov.avaluo_municipal \"avaluo_municipal\",mov.cuantia \"cuantia\" "
            + "FROM app.reg_movimiento as mov LEFT JOIN app.reg_libro as libro on (libro.id = mov.libro) "
            + "LEFT JOIN app.reg_acto as acto on (acto.id = mov.acto) LEFT JOIN app.reg_ente_judiciales as ente on (ente.id = mov.ente_judicial) "
            + "LEFT JOIN app.reg_domicilio as canton on (canton.id = mov.domicilio) WHERE to_char(mov.fecha_inscripcion, 'MM-yyyy') = ? and libro.anexo_cotad = true "
            + "ORDER BY mov.fecha_inscripcion asc,mov.num_repertorio asc,mov.num_inscripcion desc";

    public static String getRegMovimientoAnexoCotadIntervinientes = "SELECT calidad.papel \"papel\" , int.ced_ruc \"ced_ruc\", int.nombre \"nombre\","
            + "c.nombre \"domicilio\",mov_cli.estado \"estado\" , int.tipo_interv \"tipo_interv\" from app.reg_movimiento_cliente as mov_cli left join app.reg_papel as calidad on (calidad.id = mov_cli.papel) "
            + "left join app.reg_ente_interviniente int on (int.id = mov_cli.ente_interv) left join app.reg_domicilio c on (mov_cli.domicilio=c.id) where mov_cli.movimiento = ?";

    public static String getRegMovimientoAnexoCotadFichas = "SELECT reg_ficha.clave_catastral \"codigo\" , CAST(reg_ficha.num_ficha AS INTEGER) \"numFicha\" , reg_ficha.linderos \"linderos\" "
            + "from app.reg_ficha as reg_ficha LEFT JOIN app.reg_movimiento_ficha as reg_mov_ficha ON (reg_ficha.id = reg_mov_ficha.ficha) "
            + "LEFT JOIN app.reg_movimiento as mov ON (mov.id = reg_mov_ficha.movimiento) LEFT JOIN app.reg_libro as libro on (libro.id = mov.libro) WHERE reg_mov_ficha.movimiento = ?";

    public static String getRegMovimientoAnexoSupercia = "SELECT "
            + "mov.id \"movimiento_id\" , mov.num_inscripcion \"num_inscripcion\", mov.fecha_inscripcion \"fecha_inscripcion\", mov.folio_inicio \"folio_inicio\" , mov.folio_fin \"folio_fin\", mov.num_tomo \"num_tomo\", mov.num_repertorio \"num_repertorio\", "
            + "mov.fecha_oto \"fecha_oto\", mov.escrit_juic_prov_resolucion \"escrit_juic_prov_resolucion\", mov.fecha_resolucion \"fecha_resolucion\",mov.observacion \"observacion\","
            + "acto.nombre \"nombre_acto\", libro.nombre \"nombre_libro\", ente.nombre \"ente_judicial\",canton.nombre \"nombre_canton\","
            + "mov.avaluo_municipal_cadena \"avaluo_municipal_cadena\", mov.cuantia_cadena \"cuantia_cadena\", mov.avaluo_municipal \"avaluo_municipal\",mov.cuantia \"cuantia\" "
            + "FROM app.reg_movimiento as mov LEFT JOIN app.reg_libro as libro on (libro.id = mov.libro) "
            + "LEFT JOIN app.reg_acto as acto on (acto.id = mov.acto) LEFT JOIN app.reg_ente_judiciales as ente on (ente.id = mov.ente_judicial) "
            + "LEFT JOIN app.reg_domicilio as canton on (canton.id = mov.domicilio) WHERE to_char(mov.fecha_inscripcion, 'MM-yyyy') = ? and libro.anexo_supercia = true "
            + "ORDER BY mov.fecha_inscripcion asc,mov.num_repertorio asc,mov.num_inscripcion desc";

    public static String getRegMovimientoAnexoSuperciaRepresentantes = "SELECT int.nombre \"nombres\", int.ced_ruc \"ced_ruc\", c.nombre \"domicilio\","
            + "ca.nombre \"cargo\",mc.tiempo \"tiempo\" ,(select valor from app.ctlg_item where id = mc.cod_tiempo) \"cod_tiempo\",(select valor from app.ctlg_item where id = mc.atribucion) \"atribucion\"  "
            + "from app.reg_movimiento m  inner join app.reg_movimiento_representante mc on(m.id=mc.movimiento) left join app.reg_ente_interviniente int on(mc.ente_interv=int.id)"
            + "left join app.reg_domicilio c on (mc.domicilio=c.id) left join app.ctlg_cargo ca on ca.id = mc.cargo where m.id= ?";

    public static String getRegMovimientoAnexoSuperciaCapitales = "SELECT ca.nombre \"nombre\", CAST(mc.valor as varchar) \"valor\" "
            + "from app.reg_movimiento_capital mc left join app.reg_capital ca on ca.id = mc.capital where mc.movimiento = ?";

    public static String getRegMovimientoAnexoSuperciaSocios = "select int.nombre \"nombres\", int.ced_ruc \"ced_ruc\", tp.nombre \"tipo_participacion\", "
            + "ms.cantidad \"cantidad\" , ms.valor \"valor\"  from app.reg_movimiento_socios ms "
            + "left join app.reg_ente_interviniente int on ms.ente_interv = int.id "
            + "left join app.ctlg_tipo_participacion tp on tp.id = ms.tipo_participacion "
            + "where ms.movimiento = ?";

    public static String getRemanentes = "select case when ac.solvencia then 'CERTIFICACIONES' ELSE 'INSCRIPCIONES' END \"tipo\","
            + "liq.num_tramite_rp \"num_tramite\" ,case when ac.solvencia then 'N/A' ELSE CAST(liq.repertorio AS TEXT) END \"num_repertorio\" ,"
            + "CAST(to_char(liq.fecha_ingreso, 'YYYY-MM-DD') AS TEXT) \"fecha_ingreso\", liq.numero_comprobante \"num_comprobante\",ld.valor_total \"valor_total\" "
            + ",ac.nombre \"acto\" ,ld.cantidad \"cantidad\" from flow.regp_liquidacion_detalles ld inner join flow.regp_liquidacion liq ON liq.id = ld.liquidacion "
            + "inner join app.reg_acto ac on ac.id = ld.acto inner join app.reg_tipo_acto ins ON ins.id = ac.tipo_acto "
            + "WHERE (CASE WHEN ?>0 THEN liq.user_ingreso=? ELSE liq.user_ingreso>0 END) AND ld.fecha_ingreso between to_timestamp(?, 'DD/MM/YYYY HH24:MI') "
            + "AND to_timestamp(?, 'DD/MM/YYYY HH24:MI') AND ins.id = ? AND ld.valor_total > 0 ";

    public static String getComprobantesBySolicitanteVentanilla = "Select l from RegpLiquidacion l where l.beneficiario.ciRuc =:codigosolicitante "
            + "and l.numeroAutorizacion is not null and l.codigoComprobante is not null and l.claveAcceso is not null "
            + " order by l.numeroComprobante";

    public static String getLibRegistroMercantil = "select * from lib_registro_mercantil_playas where libro = :libro and numero_de_repertorio = :repertorio "
            + "and numero_de_inscripcion = :inscripcion and to_char(fecha_de_inscripcion, 'yyyy') = :anioinsc";
    public static String getLibRegistroPropiedad = "select * from lib_registro_propiedad_playas where libro = :libro and numero_de_repertorio = :repertorio "
            + "and numero_de_inscripcion = :inscripcion and to_char(fecha_de_inscripcion, 'yyyy') = :anioinsc";
    public static String getIndicePropiedadDSM = "select string_agg(rei.nombre, ', ' order by rei.id) as nombres, "
            + "string_agg(rei.ced_ruc, ', ' order by rei.id) as ci, "
            + "ac.nombre as tipocontrato,  m.num_inscripcion as numinscripcion, "
            + "m.fecha_inscripcion as fechainsripcion, "
            + "fi.clave_catastral as clavecatastral, coalesce(li.codigo_anterior || '') as libro, "
            + "fi.tipo_predio as zona, fi.linderos as lindero, fi.descripcion_bien as linderodescrip, "
            + "parr.descripcion as parroquia, m.base_imponible as cuantia, "
            + "m.id as identificador, m.escrit_juic_prov_resolucion as numjuicio, m.estado as estado, "
            + "m.fecha_inscripcion as ultimamodificacion, "
            + "enj.nombre as notaria, cton.nombre as cantonnotaria, m.fecha_oto as fechaescritura, "
            + "m.valor_uuid as valoruuid, COALESCE(tbien.descripcion, '') AS descripcionbien "
            + "from app.reg_movimiento as m "
            + "left join app.reg_movimiento_cliente as mc on (mc.movimiento=m.id) "
            + "left join app.reg_ente_interviniente as rei on (rei.id=mc.ente_interv) "
            + "left join app.reg_papel as rp on (rp.id=mc.papel) "
            + "left join app.reg_movimiento_ficha as mf on (mf.movimiento=m.id) "
            + "left join app.reg_acto as ac on (ac.id=m.acto) "
            + "left join app.reg_libro as li on (li.id=m.libro) "
            + "left join app.reg_ficha as fi on (fi.id=mf.ficha) "
            + "left join app.cat_parroquia as parr on (parr.id=fi.parroquia) "
            + "left join app.reg_ente_judiciales as enj on (enj.id=m.ente_judicial) "
            + "left join app.reg_domicilio as cton on (cton.id=m.domicilio) "
            + "left join ctlg.uaf_tipo_bien tbien ON (tbien.id = fi.uaf_tipo_bien) "
            + "where m.id = ? group by 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21 ";
    public static String getIndiceMercantilDSM = "select string_agg(rei.nombre, ', ' order by rei.id) as nombres, "
            + "string_agg(rei.ced_ruc, ', ' order by rei.id) as ci, "
            + "ac.nombre as tipocontrato, m.num_inscripcion as numinscripcion, "
            + "m.fecha_inscripcion as fechainsripcion, m.fecha_inscripcion as fechacancelacion, "
            + "fi.clave_catastral AS chasis, fi.linderos as datosbien, "
            + "coalesce(m.escrit_juic_prov_resolucion,'') as registrador, m.fecha_inscripcion as ultimamodificacion, "
            + "m.valor_uuid as codigounico, enj.nombre as entidapublica, cton.nombre as cantonnombre, "
            + "m.fecha_oto as fechaescritura, m.estado "
            + "from app.reg_movimiento as m "
            + "left join app.reg_movimiento_cliente as mc on (mc.movimiento=m.id) "
            + "left join app.reg_ente_interviniente as rei on (rei.id=mc.ente_interv) "
            + "left join app.reg_movimiento_ficha as mf on (mf.movimiento=m.id) "
            + "left join app.reg_acto as ac on (ac.id=m.acto) "
            + "left join app.reg_libro as li on (li.id=m.libro) "
            + "left join app.reg_ficha as fi on (fi.id=mf.ficha) "
            + "left join app.reg_ente_judiciales as enj on (enj.id=m.ente_judicial) "
            + "left join app.reg_domicilio as cton on (cton.id=m.domicilio) "
            + "where m.id = ? "
            + "group by 3, 4, 5, 7, 8, 9, 11, 12, 13, 14, 15";

    public static String getPubSolicitudActos = "SELECT r  FROM PubSolicitudActo r WHERE r.solicitud.id = :solicitud";
    public static String getCertificadosXficha = "select ce from RegCertificado ce where ce.ficha.numFicha = :numFicha";
    public static String findContentTypeDoc = "select content_type from archivos.doc_file where archivo_oid = ?;";
    
    //SERVICIOS 
    public static String getServiciosAbrv = "SELECT s FROM Servicio s WHERE abreviatura=:abreviatura AND activo = true";
    public static String getServiciosActivos = "SELECT c FROM Servicio c WHERE c.activo = TRUE ORDER BY c.nombre";
    public static String getServiciosTipos = "SELECT st FROM ServicioTipo st WHERE st.estado=true AND st.servicio.id=:servicio";

    public static String getAclUserByDepartamento = "select u from AclUser u where u.cargo.departamento.id = :departamento";
    public static String getResponsableDepartamento = "select u from DepartamentoUsuario u where u.usuario.id = :usuario";
    public static String getResponsableDepartamentoUs = "select u from DepartamentoUsuario u where u.departamento.id = :departamento";
    public static String getServiciosDep = "SELECT s FROM Servicio s  WHERE departamento.id = :departamento AND activo = true";
    public static String getServiciosTipo = "SELECT st FROM ServicioTipo st WHERE st.servicio.id = :servicio AND st.tipoContribuyentes.id = :tc";
    public static String getRequisitoBySer = "SELECT s FROM ServicioRequisito s  WHERE servicioTipo.id=:servicio ORDER BY s.posicion";

    public static String getDocumentosPendienteFirma = "Select m from DocumentoFirma m WHERE m.estado.id=:estado AND m.usuario=:usuario";
    public static String getDocumentosUsuarioFirma = "Select m from DocumentoFirma m WHERE m.usuario=:usuario";
    public static String getDocumentosUsuarioTramiteFirma = "Select m from DocumentoFirma m WHERE m.usuario=:usuario AND m.numTramite=:numTramite";
    public static String getDocumentosUsuarioEstadoTramiteFirma = "Select m from DocumentoFirma m WHERE m.estado.id=:estado AND m.usuario=:usuario AND m.numTramite=:numTramite";
    public static String getDocumentosTramiteFirma = "Select m from DocumentoFirma m WHERE m.estado.id <> 22190  AND m.numTramite=:numTramite";

    // EMAIL WEBSERVICE
    public static String getCorreoDisponible = "Select u from CorreoCargas u "
            + "where u.correoUsuario.estado = true and u.fecha = :fecha and u.cantidad = (Select min(ta.cantidad) "
            + "from CorreoCargas ta where ta.correoUsuario.estado = true and ta.fecha = :fecha)";
    public static String getTiposNotificacionesMail = "Select tf from MsgTipoFormatoNotificacion tf order by tf.id";

}
