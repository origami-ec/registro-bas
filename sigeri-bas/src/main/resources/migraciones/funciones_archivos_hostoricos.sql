-- BASE ACTUAL
DROP FUNCTION get_archivos_movimiento(bigint,integer,integer,character varying);
CREATE OR REPLACE FUNCTION public.get_archivos_movimiento(id_mov_ bigint, repertorio_ int, inscripcion_ int, fecha_inscripcion_ VARCHAR)
  RETURNS TABLE (extension VARCHAR, "idBLob" BIGINT, "idTransaccion" BIGINT, imagenes INTEGER) AS
$BODY$

DECLARE
BEGIN
	RETURN QUERY SELECT formato AS extension, CAST(cod_reg AS BIGINT) AS "idBLob", id_mov_ AS "idTransaccion", d.imagenes
					FROM arch_dia_dia d
					WHERE repertorio = repertorio_ AND inscripcion = inscripcion_
					AND fecha_inscripcion = CAST(fecha_inscripcion_ AS DATE);
END;

$BODY$
  LANGUAGE plpgsql VOLATILE COST 100 ROWS 1000;
-- ARCHIVOS
CREATE OR REPLACE FUNCTION public.get_image_movimiento(cod_reg_ bigint)
  RETURNS TABLE ("idTransaccion" BIGINT, imagen bytea, anota bytea, "idBLob" BIGINT) AS
$BODY$

DECLARE
BEGIN
	RETURN QUERY SELECT CAST(img_.cod_imag AS BIGINT) "idTransaccion", img_.imagen, img_.anota, CAST(img_.cod_reg AS BIGINT) "idBLob"
					FROM imag_dia_dia img_ WHERE cod_reg = cod_reg_;
END;

$BODY$
  LANGUAGE plpgsql VOLATILE COST 100 ROWS 1000;

-- BASE 2003-2015
DROP FUNCTION get_archivos_movimiento(bigint,bigint,bigint,character varying);
CREATE OR REPLACE FUNCTION public.get_archivos_movimiento(id_mov_ bigint, repertorio_ bigint, inscripcion_ bigint, fecha_inscripcion_ VARCHAR)
RETURNS TABLE (extension VARCHAR, "idBLob" BIGINT, "idTransaccion" BIGINT, imagenes INTEGER) AS
$BODY$
DECLARE
	gavet RECORD;
BEGIN
	SELECT nombre::int, cod_gaveta, cod_archivador INTO gavet
	FROM "public".gavetas WHERE nombre ~ '\d' AND (nombre ~ 'TEST' ) = FALSE
	AND nombre::int = EXTRACT(YEAR FROM fecha_inscripcion_::DATE) ORDER BY 1;

	IF gavet.cod_archivador = 'LOJAHISTORICO1' THEN
		--RAISE NOTICE '>> LOJAHISTORICO1';
		RETURN QUERY SELECT h1.formato AS extension, CAST(h1.cod_reg AS BIGINT) AS "idBLob", id_mov_ AS "idTransaccion", h1.imagenes
					FROM "public".arch_lojahistorico1 AS h1
					WHERE h1.repertorio = repertorio_ AND h1.inscripcion = inscripcion_  AND h1.fecha_inscripcion = fecha_inscripcion_::DATE;
	ELSIF gavet.cod_archivador = 'LOJAHISTORICO2' THEN
		--RAISE NOTICE '>> LOJAHISTORICO2';
		RETURN QUERY SELECT h2.formato AS extension, CAST(h2.cod_reg AS BIGINT) AS "idBLob", id_mov_ AS "idTransaccion", h2.imagenes
					FROM "public".arch_lojahistorico2 AS h2
					WHERE h2.repertorio = repertorio_ AND h2.inscripcion = inscripcion_  AND h2.fecha_inscripcion = fecha_inscripcion_::DATE;
	ELSIF gavet.cod_archivador = 'LOJAHISTORICO3' THEN
		--RAISE NOTICE '>> LOJAHISTORICO3';
		RETURN QUERY SELECT h3.formato AS extension, CAST(h3.cod_reg AS BIGINT) AS "idBLob", id_mov_ AS "idTransaccion", h3.imagenes
					FROM "public".arch_lojahistorico3 AS h3
					WHERE h3.repertorio = repertorio_ AND h3.inscripcion = inscripcion_  AND h3.fecha_inscripcion = fecha_inscripcion_::DATE;
	ELSE
	--RAISE NOTICE '>> LOJAHISTORICO4';
		RETURN QUERY SELECT h4.formato AS extension, CAST(h4.cod_reg AS BIGINT) AS "idBLob", id_mov_ AS "idTransaccion", h4.imagenes
					FROM "public".arch_lojahistorico4 AS h4
					WHERE h4.repertorio = repertorio_ AND h4.inscripcion = inscripcion_  AND h4.fecha_inscripcion = fecha_inscripcion_::DATE;
	END IF;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE COST 100 ROWS 1000;

CREATE OR REPLACE FUNCTION public.get_image_movimiento(cod_reg_ bigint, fecha_inscripcion_ VARCHAR)
  RETURNS TABLE ("idTransaccion" BIGINT, imagen bytea, anota bytea, "idBLob" BIGINT) AS
$BODY$
DECLARE
	gavet RECORD;
BEGIN
	SELECT nombre::int, cod_gaveta, cod_archivador INTO gavet
	FROM "public".gavetas  WHERE nombre ~ '\d' AND (nombre ~ 'TEST' ) = FALSE
	AND nombre::int = EXTRACT(YEAR FROM fecha_inscripcion_::DATE) ORDER BY 1;

	IF gavet.cod_archivador = 'LOJAHISTORICO1' THEN
		--RAISE NOTICE '>> LOJAHISTORICO1';
		RETURN QUERY SELECT CAST(img_.cod_imag AS BIGINT) "idTransaccion", img_.imagen, img_.anota, CAST(img_.cod_reg AS BIGINT) "idBLob"
					FROM public.imag_lojahistorico1 img_ WHERE img_.cod_reg = cod_reg_;
	ELSIF gavet.cod_archivador = 'LOJAHISTORICO2' THEN
		--RAISE NOTICE '>> LOJAHISTORICO2';
		RETURN QUERY SELECT CAST(img_2.cod_imag AS BIGINT) "idTransaccion", img_2.imagen, img_2.anota, CAST(img_2.cod_reg AS BIGINT) "idBLob"
					FROM public.imag_lojahistorico2 img_2 WHERE img_2.cod_reg = cod_reg_;
	ELSIF gavet.cod_archivador = 'LOJAHISTORICO3' THEN
		--RAISE NOTICE '>> LOJAHISTORICO3';
		RETURN QUERY SELECT CAST(img_3.cod_imag AS BIGINT) "idTransaccion", img_3.imagen, img_3.anota, CAST(img_3.cod_reg AS BIGINT) "idBLob"
					FROM public.imag_lojahistorico3 img_3 WHERE img_3.cod_reg = cod_reg_;
	ELSE
		--RAISE NOTICE '>> LOJAHISTORICO4';
		RETURN QUERY SELECT CAST(img_4.cod_imag AS BIGINT) "idTransaccion", img_4.imagen, img_4.anota, CAST(img_4.cod_reg AS BIGINT) "idBLob"
					FROM public.imag_lojahistorico4 img_4 WHERE img_4.cod_reg = cod_reg_;
	END IF;

END;
$BODY$
  LANGUAGE plpgsql VOLATILE COST 100 ROWS 1000;

	-- SELECT * FROM public.get_image_movimiento(12304);

	-- SELECT * FROM WHERE UPPER(campo1) = 'TEXT'


DROP FUNCTION get_archivos_movimiento(bigint,bigint,bigint,character varying);
CREATE OR REPLACE FUNCTION public.get_archivos_movimiento(id_mov_ bigint, repertorio_ bigint, inscripcion_ bigint, fecha_inscripcion_ VARCHAR)
RETURNS TABLE (extension VARCHAR, "idBLob" BIGINT, "idTransaccion" BIGINT, imagenes INTEGER) AS
$BODY$
DECLARE
	gavet RECORD;
	anio int;
BEGIN
	anio = EXTRACT(YEAR FROM fecha_inscripcion_::DATE) ;
	IF (anio >= 1973 AND anio <= 2002) THEN
		--RAISE NOTICE '>> LOJAHISTORICO1';
		RETURN QUERY SELECT h1.formato AS extension, CAST(h1.cod_reg AS BIGINT) AS "idBLob", id_mov_ AS "idTransaccion", h1.imagenes
					FROM "public".arch_segunda_fase AS h1
					WHERE h1.repertorio = repertorio_ AND h1.inscripcion = inscripcion_  AND h1.fecha_inscripcion = fecha_inscripcion_::DATE;
-- 1871 A 1972'
	ELSIF anio >= 1871 AND anio <= 1972 THEN
		--RAISE NOTICE '>> LOJAHISTORICO2';
		RETURN QUERY SELECT h2.formato AS extension, CAST(h2.cod_reg AS BIGINT) AS "idBLob", id_mov_ AS "idTransaccion", h2.imagenes
					FROM "public".arch_segunda_fase2 AS h2
					WHERE h2.repertorio = repertorio_ AND h2.inscripcion = inscripcion_  AND h2.fecha_inscripcion = fecha_inscripcion_::DATE;

	END IF;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE COST 100 ROWS 1000;



CREATE OR REPLACE FUNCTION public.get_image_movimiento(cod_reg_ bigint, fecha_inscripcion_ VARCHAR)
  RETURNS TABLE ("idTransaccion" BIGINT, imagen bytea, anota bytea, "idBLob" BIGINT) AS
$BODY$
DECLARE
	gavet RECORD;
	anio int;
BEGIN
	anio = EXTRACT(YEAR FROM fecha_inscripcion_::DATE) ;
	IF (anio >= 1973 AND anio <= 2002) THEN
		--RAISE NOTICE '>> LOJAHISTORICO1';
		RETURN QUERY SELECT CAST(img_.cod_imag AS BIGINT) "idTransaccion", img_.imagen, img_.anota, CAST(img_.cod_reg AS BIGINT) "idBLob"
					FROM public.imag_segunda_fase img_ WHERE img_.cod_reg = cod_reg_;

	ELSIF anio >= 1871 AND anio <= 1972 THEN
		--RAISE NOTICE '>> LOJAHISTORICO2';
		RETURN QUERY SELECT CAST(img_2.cod_imag AS BIGINT) "idTransaccion", img_2.imagen, img_2.anota, CAST(img_2.cod_reg AS BIGINT) "idBLob"
					FROM public.imag_segunda_fase2 img_2  WHERE img_2.cod_reg = cod_reg_;

	END IF;

END;
$BODY$
  LANGUAGE plpgsql VOLATILE COST 100 ROWS 1000;



	-- SELECT * FROM public.get_image_movimiento(12304);








