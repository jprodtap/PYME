package dav.Libranza;

import dxc.execution.BasePageWeb;
import dxc.library.reporting.Reporter;
//import dxc.execution.Reporter;
//import dxc.execution.SettingsRun;
import dxc.library.settings.SettingsRun;

public class ControllerLibranza {
	PageLibranza pagLibranza;
	String[] ValidarAprobacionMsj = null;
	String[] consultaHistoricos = null;

	public ControllerLibranza(BasePageWeb parentPage) {
		pagLibranza = new PageLibranza(parentPage);

	}

	Integer validarTipoDato;
	boolean identificarNumero = false;
	int eventStatus = 0;

	public String[] AprobLibranza(boolean firmas) throws Exception {
		String numeroCreditosLibranza = SettingsRun.getTestData().getParameter("Números de Aprobaciones");
		this.pagLibranza = new PageLibranza(pagLibranza);
		String[] ValidarAprobacionMsj = new String[1];
		ValidarAprobacionMsj = this.pagLibranza.AprobarLibranza(null, null);
		if (ValidarAprobacionMsj[0].contains("Se visualiza mensaje")
				|| ValidarAprobacionMsj[0].contains("No se encontró")
				|| ValidarAprobacionMsj[0].contains("NO se encontró")
				|| ValidarAprobacionMsj[0].contains("No fue posible")
				|| ValidarAprobacionMsj[0].contains("No se esperaba") || ValidarAprobacionMsj[0].contains("No permitio")
				|| ValidarAprobacionMsj[0].contains("Por favor verifique parametrización de número de firmas")
				|| ValidarAprobacionMsj[0].contains("No se actualizó ninguna solicitud")) {
			eventStatus = Reporter.MIC_FAIL;
			Reporter.reportEvent(eventStatus, ValidarAprobacionMsj[0]);
			SettingsRun.exitTestIteration();
		} else if (ValidarAprobacionMsj[0].contains("Se realiza descarga de formato")) {
			eventStatus = Reporter.MIC_PASS;
			Reporter.reportEvent(eventStatus, ValidarAprobacionMsj[0]);
			SettingsRun.exitTestIteration();
		}

		eventStatus = Reporter.MIC_PASS;
		Reporter.reportEvent(eventStatus, "Se realiza Aprobación de créditos de libranza exitosamente.");
		if (!numeroCreditosLibranza.contains("1")) {
			Reporter.reportEvent(Reporter.MIC_INFO,
					"Se procede a ingresar con siguiente usuario para realizar el proceso de aprobación");
		}
		return ValidarAprobacionMsj;

	}

	/**
	 * Metodo para realizar aprobación de solicitudes de credito con más de una
	 * firma y verificar si la prueba es exitosa o fallida
	 * 
	 * @param datosAprobados Array con información de créditos aprobados con 1 firma
	 * @return Array con información de créditos aprobados exitosamente o mensaje de
	 *         error en caso de presentarse
	 * @throws Exception
	 */
	public String[] aprobarSegundaFirma(String[] datosAprobados) throws Exception {

		String[] numerSolicitud = obtenerSolicitud(datosAprobados);

		String[] ValidarAprobacionMsj = new String[1];
		ValidarAprobacionMsj = this.pagLibranza.AprobarLibranza(numerSolicitud, datosAprobados);
		if (ValidarAprobacionMsj[0].contains("Se visualiza mensaje")
				|| ValidarAprobacionMsj[0].contains("No se encontró")
				|| ValidarAprobacionMsj[0].contains("NO se encontró")
				|| ValidarAprobacionMsj[0].contains("No fue posible")
				|| ValidarAprobacionMsj[0].contains("No se esperaba") || ValidarAprobacionMsj[0].contains("No permitio")
				|| ValidarAprobacionMsj[0].contains("Por favor verifique parametrización de número de firmas")) {
			eventStatus = Reporter.MIC_FAIL;
			Reporter.reportEvent(eventStatus, ValidarAprobacionMsj[0]);
			SettingsRun.exitTestIteration();
		} else if (ValidarAprobacionMsj[0].contains("Se realiza descarga de formato")) {
			eventStatus = Reporter.MIC_PASS;
			Reporter.reportEvent(eventStatus, ValidarAprobacionMsj[0]);
			SettingsRun.exitTestIteration();
		}
		eventStatus = Reporter.MIC_PASS;
		Reporter.reportEvent(eventStatus, "Se realiza Aprobación de créditos de libranza exitosamente");

		return ValidarAprobacionMsj;

	}

	/**
	 * Metodo para extraer el número de solicitud del array de cada una de sus
	 * posiciones
	 * 
	 * @param texto Parametro Array que contiene la información de los creditos de
	 *              libranza aprobados
	 * @return
	 */
	public static String[] obtenerSolicitud(String[] texto) {
		String[] extraerSolicitudes = new String[texto.length];
		for (int i = 0; i < texto.length; i++) {

			String[] numeroSolic = texto[i].split(",\\s*");
			String valorSolicitud = numeroSolic[1];
			extraerSolicitudes[i] = valorSolicitud;

		}

		return extraerSolicitudes;
	}

	/**
	 * consultaHistoricos: busqueda en consulta historico de las solicitudes
	 * aprobadas en solicitudes pendientes y verificación si prueba es exitosa o
	 * fallida
	 * 
	 * @param DatosFilasComparacion Array que contiene la información de los
	 *                              creditos de libranza aprobados
	 * @return Valida si la información diligenciada en solicitudes pendientes es
	 *         igual a la que se presenta en consulta historicos
	 * @throws Exception
	 */
	public String[] consultaHistoricos(String[] DatosFilasComparacion) throws Exception {

		this.pagLibranza = new PageLibranza(pagLibranza);
		String[] numerSolicitud = obtenerSolicitud(DatosFilasComparacion);

		consultaHistoricos = this.pagLibranza.consultaHistoricos(numerSolicitud, DatosFilasComparacion);

		for (int i = 0; i < consultaHistoricos.length; i++) {
			if (consultaHistoricos[i].contains("Incorrecto")) {
				eventStatus = Reporter.MIC_FAIL;
				Reporter.reportEvent(eventStatus,
						"No coincide información con la diligenciada en solicitudes pendientes con solicitud "
								+ numerSolicitud[i] + " [" + consultaHistoricos[i] + "]");

			} else if (consultaHistoricos[i].contains("No se encontró registros con el número de solicitud")) {
				eventStatus = Reporter.MIC_FAIL;
				Reporter.reportEvent(eventStatus, consultaHistoricos[i]);
			} else if (consultaHistoricos[0].contains("No se esperaba el mensaje")
					|| consultaHistoricos[0].contains("No se encontró")
					|| consultaHistoricos[0].contains("NO se encontró")
					|| consultaHistoricos[0].contains("Existen muchos") || consultaHistoricos[0].contains("menú")) {
				eventStatus = Reporter.MIC_FAIL;
				Reporter.reportEvent(eventStatus, consultaHistoricos[i]);
				SettingsRun.exitTestIteration();

			} else {
				eventStatus = Reporter.MIC_PASS;
				Reporter.reportEvent(eventStatus,
						"Coincide la misma información con la diligenciada en solicitudes pendientes con solicitud "
								+ numerSolicitud[i] + " [" + consultaHistoricos[i] + "]");
			}
		}

		return consultaHistoricos;

	}
}
