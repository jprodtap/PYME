package dav.PreguntasPEP;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import java.util.Set;

import dav.c360.PageInicioC360;
import dav.c360.PageLogin;
import dav.c360.moduloPersonas.PagePersona;
import dav.pymes.moduloCrearTx.PageOrigen;
import dxc.execution.BasePageWeb;
import dxc.library.reporting.Evidence;
import dxc.library.reporting.Reporter;
import dxc.library.settings.SettingsRun;
//import dxc.execution.Evidence;
//import dxc.execution.Reporter;
//import dxc.execution.SettingsRun;
import dxc.util.DXCUtil;

public class ControllerPreguntasPep {

// =======================================================================================================================
	// PAGES
	PageLogin pageLoginC360 = null;
	PageInicioC360 pageInicioC360 = null;
	PagePersona pagePersonaC360 = null;
	PageOrigen pageOrigen = null;
	PagePreguntas pagePreguntas = null;

	// VARIABLES NECESARIAS
	String docType, idNum, requisite, reportMsg, previousAnswer, newAnswer, FILE_PATH;
	int eventStatus = Reporter.MIC_PASS;
	static LinkedHashMap<String, String> previousAnswers = null;
	static LinkedHashMap<String, String> newAnswers = null;
	static String[] ENCABEZADO = null;
	boolean values;

	public ControllerPreguntasPep(BasePageWeb parentPage) throws Exception {
		this.pageInicioC360 = new PageInicioC360(parentPage);
		this.pagePersonaC360 = new PagePersona(parentPage);
		this.pagePreguntas = new PagePreguntas(parentPage);
		this.pageOrigen = new PageOrigen(parentPage);
		this.docType = SettingsRun.getTestData().getParameter("Tipo de documento").toUpperCase();
		this.idNum = SettingsRun.getTestData().getParameter("Id usuario");
		this.requisite = SettingsRun.getTestData().getParameter("Selección");
	}

	/**
	 * En cliente 360 realiza validación para
	 * 
	 * @throws Exception
	 */
	public void validarPreguntasPersonas() throws Exception {

		reportMsg = this.pagePersonaC360.buscarPersona(this.docType, this.idNum);

		if (!reportMsg.isEmpty() && (!reportMsg.contains("El cliente no ha actualizado")))
			this.pageInicioC360.terminarIteracion(Reporter.MIC_NOEXEC, "[ERROR DATA] " + reportMsg);

		Reporter.reportEvent(eventStatus, "Se ha encontrado el usuario con número de identificación [" + idNum + "]");

		String[] arrRiesgo = this.pagePersonaC360.getMasInfoCliente(this.requisite);

		if (arrRiesgo[0].isEmpty() || (arrRiesgo[0].contains("El cliente no ha actualizado"))) {
			reportMsg = "Se esperaba la pantalla de [Más Información]";
			if (arrRiesgo[0].contains("El cliente no ha actualizado")) {
				eventStatus = Reporter.MIC_WARNING;
				reportMsg += " pero se presentó [" + arrRiesgo[0] + "].";
				Reporter.reportEvent(eventStatus, reportMsg);
			}
			eventStatus = Reporter.MIC_PASS;
			Reporter.reportEvent(eventStatus,
					"Los datos de riesgo en la pantalla 'Más Información', según lo esperado.");
		}
		reportMsg = "Riesgo y fecha";
		boolean[] riesgosContenidos = new boolean[] { (!arrRiesgo[1].isEmpty() && !arrRiesgo[2].isEmpty()),
				(!arrRiesgo[1].isEmpty() || !arrRiesgo[2].isEmpty()),
				(arrRiesgo[1].isEmpty() && arrRiesgo[2].isEmpty()) };

		if (riesgosContenidos[0]) {
			reportMsg = " presentados: Riesgo [" + arrRiesgo[1] + "] - fecha [" + arrRiesgo[2] + "]";
		} else if (riesgosContenidos[1]) {
			reportMsg += ", presentado: " + arrRiesgo[1] + arrRiesgo[2];
		} else if (riesgosContenidos[2]) {
			reportMsg += ", no aplica para este usuario";
		}
		Reporter.reportEvent(Reporter.MIC_INFO, reportMsg);

		previousAnswers = this.pagePersonaC360.getInfoLegal(this.requisite);
		for (String respuesta : previousAnswers.values()) {
			if (!respuesta.isEmpty()) {
				break;
			}
			values = respuesta.isEmpty();
		}

		// SI NO TIENE RIESGO O RIESGO BAJO, HAY QUE CAMBIAR LAS PREGUNTAS CADA 1 AÑOS
		// SI TIENE RIESGO MEDIO 2, HAY QUE CAMBIAR LAS PREGUNTAS CADA 2 AÑOS
		// SI TIENE RIESGO ALTO 1, HAY QUE CAMBIAR LAS PREGUNTAS CADA 4 AÑOS
		boolean alertaExist = PagePersona.existeAlerta();
		String tipoPrueba = SettingsRun.getTestData().getParameter("Tipo de Prueba");
		if (values) {
			reportMsg = "No se encontraron values registrados en las preguntas PEP (SE RESPONDERAN LAS PREGUNTAS)";

			if (alertaExist == false && tipoPrueba.equalsIgnoreCase("Responder")) {
				reportMsg = "No se esperaban preguntas registradas";
				this.pageInicioC360.terminarIteracion(Reporter.MIC_FAIL,
						"No se puede continuar con la prueba: " + reportMsg);
			}
		} else {
			reportMsg = "Se encontraron values registrados en las preguntas PEP"
					+ (alertaExist == true ? " >>> (SE ACTUALIZARAN LAS PREGUNTAS EN FRONT)" : "");
			if (alertaExist && tipoPrueba.equalsIgnoreCase("Actualizar")) {
				eventStatus = Reporter.MIC_WARNING;
				reportMsg = "El usuario no esta actualizado: " + reportMsg + ", segun lo esperado";
			} else
				this.pageInicioC360.terminarIteracion(Reporter.MIC_FAIL,
						"No se puede continuar con la prueba: " + reportMsg);
		}

		Reporter.reportEvent(eventStatus, reportMsg);
	}

	/**
	 * 
	 * @throws Exception
	 */
	public void ingresarPreguntasPep() throws Exception {
		String tratamientoDatos = SettingsRun.getTestData().getParameter("Tratamiento de datos");

		reportMsg = "Pantalla presentada 'Preguntas PEP', según lo esperado";

		String msgError = this.pageOrigen.irAOpcion(null, "Administración", "Administración Portal",
				"Parametros Generales");

		if (msgError != null) {
			msgError = this.pageOrigen.irAOpcion(null, "Pagos, Transferencias y Otros", "Crear Transacción");

			if (msgError != null)
				this.pageOrigen.terminarIteracion(Reporter.MIC_FAIL, "No se esperaba: " + msgError);
		}
		String arrResp[] = this.pagePreguntas.responderPreguntas();
		if (arrResp[0] != null && arrResp.length == 1) {
			this.pageOrigen.terminarIteracion(Reporter.MIC_FAIL, "No se esperaba: " + arrResp[0]);
		} else {
			Reporter.reportEvent(eventStatus, reportMsg);
			for (int i = 0; i < arrResp.length; i++) {
				Reporter.reportEvent(eventStatus, arrResp[i]);
			}
		}

		msgError = this.pagePreguntas.tratamientoDatos(tratamientoDatos);

		if (!reportMsg.isEmpty())
			msgError += ", " + tratamientoDatos + " ha sido aceptado según lo esperado";
		else
			msgError = "Se ha guardado y aceptado el tratamiendo de datos, según lo esperado";

		Reporter.reportEvent(eventStatus, msgError);
	}

	/**
	 * Verifica si las preguntas se comparan con las nuevas o son si nuevas
	 * preguntas
	 * 
	 * @throws Exception
	 */
	public void getValidacionPreguntas() throws Exception {

		reportMsg = this.pagePersonaC360.buscarPersona(this.docType, this.idNum);
		if (!reportMsg.isEmpty() && (!reportMsg.contains("El cliente no ha actualizado")))
			this.pageInicioC360.terminarIteracion(Reporter.MIC_NOEXEC, "[ERROR DATA] " + reportMsg);

		newAnswers = this.pagePersonaC360.getInfoLegal(this.requisite);

		reportMsg = "Las respuestas presentadas han sido revisadas, según lo esperado";
		String tratamientoDatos = SettingsRun.getTestData().getParameter("Tratamiento de datos");
		String solicitudPreguntas = SettingsRun.getTestData().getParameter("Tipo de Prueba");
		boolean[] resultados = comparacionRespuestas(previousAnswers);
		if (tratamientoDatos.contains("NO")) {
			for (int i = 0; i < resultados.length; i++) {
				if (!resultados[i]) {
					values = !resultados[i];
					break;
				}
			}
			if (values) {
				reportMsg = "No se esperaba cambio de respuestas,";
				this.pageOrigen.terminarIteracion(Reporter.MIC_FAIL,
						reportMsg + " >>> Preguntas y respuestas (Antiguas y Nuevas) en carpeta de evidencias ***");
			}
		}

		if (tratamientoDatos.contains("SI") && solicitudPreguntas.equalsIgnoreCase("Actualizar")) {
			reportMsg = "Se esperaba actualización de preguntas";
		} else if (tratamientoDatos.contains("SI") && solicitudPreguntas.equalsIgnoreCase("Responder")) {
			reportMsg = "Se esperaba nuevas respuestas de preguntas";
		}

		reportMsg += " >>> Preguntas y respuestas (Antiguas y Nuevas) en carpeta de evidencias ***";
		Reporter.reportEvent(eventStatus, reportMsg);
	}

	/**
	 * Metodo encagado de retornar un arreglo de booleanos, si las preguntas son
	 * iguales almacenara true, de lo contrario false al mismo tiempo genera un
	 * archivo excel que va adicionando la información de las preguntas, respuesta
	 * antigua y nueva(si hay nueva);
	 * 
	 * @param hashMap
	 * @return
	 * @throws Exception
	 */
	public boolean[] comparacionRespuestas(LinkedHashMap<String, String> linkedHashMap) throws Exception {
		boolean[] respuestasComparadas = new boolean[linkedHashMap.size()];
		int i = 0;
		for (String key : previousAnswers.keySet()) {
			previousAnswer = previousAnswers.get(key);
			newAnswer = newAnswers.get(key);
			respuestasComparadas[i] = previousAnswer.equals(newAnswer);
			this.writeDataExcel(key, previousAnswer, newAnswer);
			i++;
		}
		return respuestasComparadas;
	}

	/**
	 * Genera un excel si no existe, si existe ira adicionando información que
	 * recibe para irla adicionandola
	 * 
	 * @param pregunta
	 * @param respuestaAnterior
	 * @param respuestaNueva
	 * @throws Exception
	 */
	public void writeDataExcel(String pregunta, String respuestaAnterior, String respuestaNueva) throws Exception {
		// almacenara la ruta del archivo con las preguntas
		FILE_PATH = Evidence.getNbEvidenceDirectory() + "\\Resultado_Preguntas_PEP.xlsx";

		// Hace un nuevo archivo para usarse
		File archivo = new File(FILE_PATH);
		Workbook libro = null;
		Sheet hoja = null;
		FileOutputStream archivoSalida = null;

		// si el archivo no existe
		if (!archivo.exists()) {
			// arreglo de encabezado que se generara
			ENCABEZADO = new String[] { "Pregunta", "Respuesta Anterior", "NuevaRespuesta" };
			// se genera un nuevo libro
			libro = new XSSFWorkbook();
			// se crea una nueva hoja asignandole un nombre
			hoja = libro.createSheet("Preguntas PEP");
			// se utilizara la fila 0
			Row filaEncabezado = hoja.createRow(0);
			// almacena los estilos que se aplicaran al encabezado recibidos por un metodo
			XSSFCellStyle estiloEncabezado = getEstilos(libro);
			// for itera en arreglo encabezado, seteando y adicionando estilo a la celda
			// para diferenciarlos de la data
			for (int i = 0; i < ENCABEZADO.length; i++) {
				Cell celda = filaEncabezado.createCell(i);
				// setea el valor de la celda
				celda.setCellValue(ENCABEZADO[i]);
				// setea el estilo de la celda
				celda.setCellStyle(estiloEncabezado);
			}
			// genera el archivo de salida para que escriba la información implementada
			// dando el parametro de la ruta
			archivoSalida = new FileOutputStream(FILE_PATH);
			// el libro aplica los cambios en la variable archivo salida
			libro.write(archivoSalida);
			// cierra el archivo de salida
			archivoSalida.close();
			// cierra el libro
			libro.close();
		}

		// si el archivo existe se salta el paso anterior
		// se genera un archivo de entrada que es el que ya existe
		FileInputStream archivoEntrada = new FileInputStream(FILE_PATH);
		// se crea un nuevo libro asignandole el archivo de entrada que necesitamos
		libro = new XSSFWorkbook(archivoEntrada);
		// entra a la hoja que se menciona como parametro
		hoja = libro.getSheet("Preguntas PEP");
		// se obtiene la ultima fila con información
		int filaDisponible = hoja.getLastRowNum();
		// la variable colocara su información luego de la ultima fila con información
		Row filaData = hoja.createRow(filaDisponible + 1);
		// cada fila creara una celda con una posición de acuerdo a la necesidad seguido
		// colocando el valor
		// que recibe el metodo
		filaData.createCell(0).setCellValue(pregunta);
		filaData.createCell(1).setCellValue(respuestaAnterior);
		filaData.createCell(2).setCellValue(respuestaNueva);

		// Ajusta el alto de la fila automáticamente para que se vea todo el texto
		filaData.setHeight((short) -1);
		// Ajusta el ancho de la columna automaticamentepara que se ajuste al contenido
		hoja.autoSizeColumn(0);

		// cierra el archivo de entrada
		archivoEntrada.close();
		// genera el archivo de salida para que escriba la información implementada
		// dando el parametro de la ruta
		archivoSalida = new FileOutputStream(archivo);
		// el libro aplica los cambios en la variable archivo salida
		libro.write(archivoSalida);
		// cierra el archivo de salida
		archivoSalida.close();
		// cierra el libro
		libro.close();
	}

	/**
	 * Genera estilos para las celdas header de nuestro libro que se estan generando
	 * 
	 * @param libro
	 * @return
	 */
	private static XSSFCellStyle getEstilos(Workbook libro) {
		XSSFCellStyle estiloEncabezado = (XSSFCellStyle) libro.createCellStyle();
		Font fuente = libro.createFont();
		fuente.setBold(true);
		estiloEncabezado.setFont(fuente);
		estiloEncabezado.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
		estiloEncabezado.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		return estiloEncabezado;
	}

}
