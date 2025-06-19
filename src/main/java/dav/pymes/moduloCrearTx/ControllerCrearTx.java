package dav.pymes.moduloCrearTx;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import dav.CobrosMiddle.BaseMiddleCobros;
import dav.Consultas_Y_Extractos.PageConsultasyExtractos;
import dav.divisas.*;
import dav.middlePymes.PageServicios;
import dav.transversal.DatosDavivienda;
import dav.transversal.DatosEmpresarial;
import dav.transversal.MotorRiesgo;
import dav.transversal.NotificacionSMS;
import dxc.execution.BasePageWeb;
import dxc.library.reporting.Evidence;
import dxc.library.reporting.Reporter;
//import dxc.execution.Evidence;
//import dxc.execution.Reporter;
import dxc.library.settings.SettingsRun;
import dxc.util.DXCUtil;

public class ControllerCrearTx {

// ====================================[Page & Crontroller]===================================================================================
	PageOrigen pageOrigen;
	PageDestino pageDestino;
	PageValores pageValores;
	PageConfirmacion pageConf;
	PagePendAprobacion pagePendApr;
	PageConsultasyExtractos pageConsultasyExtractos;

	PageDivisas pageDivisas;
	PageRecibirTransferenciasInternacionales pageRecibirTransferenciasInternacionales;
	PageConsultatxInternacional pageConsultatxInternacional;
	PageAprobacionInter pageAprobInter = null;
	PageEnviarTransInternacional pageEnviarTransInternacional;
	PageDocumentos_Y_Formularios pageDocumentos_Y_Formularios;

	BaseMiddleCobros baseMiddleCobros = null;

	ControllerDestinosMasivos controlDest = null;
// ===========================================================================================================================================

// ===========================================================================================================================================
	public static String numAprobaciones = null; // N�mero de Firmas a Probar la TX
	boolean unaFirma;

	String servicio; // Servicio o TX a realizar

	String nombreAmbiente = null;

	public static String tipoPrueba = null; // Tipo de Tx si es en [Login - Tx Linea - Pendiente de transacci�n -
											// Eliminar Tx]

	String pageTitle;

	String descripcionsin;
	String valorsin, valorcon;

	String referenciasin, referenciacon;

	String numerodereferenciaExterna;// Numero de Referencia Externa Recibir Divisas

	String riesgo, prioridaRiesgo, userAgent;// Datos Motor de riesgo
	public static String[] cuentasDesMotor = null, datoNumDestCuentas = null;// Datos Motor de riesgo

	public static String numeroTx = null; // N�mero O Documento de la Transacci�n [TX]

	String valorTx;
	String bancoDesTx;
	String estadoTx;
	String montoReal;

// -----------------------------------------------------------------------------------------------------------------------

	// Ayuda a identificar la cantidad de destinos transados
	String cantidadTxPorTipoDestino = null;

	int incCuentasDavivienda = 0;
	int incCuentasOtrosBancos = 0;
	int incTarjetaPrepago = 0;
	int incDaviplata = 0;
	int incDepositosElectronicos = 0;
	int incPagoServicios = 0;
	int totalDestinos;

	public static int[] contadorTx = null;

// -----------------------------------------------------------------------------------------------------------------------	

	public static String navegador = null,
//			numAprova = null, 
			activityAmount = null, bancoDesMotor = null, riesgoBc = null, riesgoEfm = null, estado = null,
			diaDiaDelpago = null, nombreEmpre = null, nitEmpre = null, uuid = null, tipoIdEm = null,
			desdeElDetalle = null, adenda = null;

// -----------------------------------------------[Transacional y pagos]------------------------------------------------------------------------	

	// Cuenta Origen y destino
	static String numCta = "", tipoCta = "", referencia1 = "", referencia2 = "";

	// Destinos Cuenta Inscrita [bncoDest] y No Insc
	static String bncoDest = null, titul = null, tipoid = null, numid = null;

	// Pagos Automatico
	static String numConvenio = null, tipoCtaSecundario = null, numCtaSecundario = null;

	// AvanceTC
	static String mesTC = null, avenTC = null, plazoDiAv = null;

	// DIVISAS
	static String tipoMoneda = null, modena = null, concepTx = null, concepto = null, valorNumeral1 = null,
			valorNumeral2 = null, tipoEnvio = null, FechaEnvioFrecuente = null, nombreBene = null, paisDestino = null,
			ciudadDestino = null, direccionBene = null, cuentaBene = null, infoPago = null, referenciaPago = null,
			tipoCodigo = null, numeroCodigo = null, intermediario = null, tipoCodigoInter = null,
			numeroCodigoInter = null;

// -----------------------------------------------------------------------------------------------------------------------	

	private String[] arrMsgBuscarEstado = { PageConfirmacion.MSG_EXITO_APROB, PageConfirmacion.MSG_EXITO_PAGO,
			PageConfirmacion.MSG_EXITO_ENV_OTP, PageConfirmacion.MSG_EXITO_GUARD,
			PageConfirmacion.MSG_EXITO_APROB_INTER, };

	private String[] arrMsgTxDeclinada = { PageConfirmacion.MSG_EXITO_DECL1, PageConfirmacion.MSG_EXITO_DECL2,
			PageConfirmacion.MSG_EXITO_DECL3, PageConfirmacion.MSG_EXITO_DECL5, };

	private String[] arrEstadosTxAprob = { "Pagado", "Pago Realizado", "Transferencia Realizada",
			"Pendiente Respuesta de Otros Bancos", "Pagado Parcial" };

	String transaccion = MotorRiesgo.TX_EMP_LOGIN_SUCC; // VALOR POR DEFECTO

// -----------------------------------------------------------------------------------------------------------------------	

	final String TP_LOGIN = "Login";
	final String TP_EN_LINEA = "Tx En L�nea";
	final String TP_PEND_APR = "Tx Pend Aprobaci�n";

//==========================================[ControllerCrearTx]===============================================================================

	public ControllerCrearTx(BasePageWeb parentPage) throws Exception {

		this.pageTitle = parentPage.getTitle();

		pageOrigen = new PageOrigen(parentPage);
		pageDestino = new PageDestino(parentPage);
		pageValores = new PageValores(parentPage);
		pageConf = new PageConfirmacion(parentPage);

		pageConsultasyExtractos = new PageConsultasyExtractos(parentPage);
		pagePendApr = new PagePendAprobacion(parentPage);
		pageAprobInter = new PageAprobacionInter(parentPage);

		// Datos Globales
		this.servicio = SettingsRun.getTestData().getParameter("Servicio").trim();
		this.tipoPrueba = SettingsRun.getTestData().getParameter("Tipo prueba").trim();
		adenda = SettingsRun.getTestData().getParameter("Adenda").trim();
		this.numAprobaciones = SettingsRun.getTestData().getParameter("N�meros de Aprobaciones").trim();
		this.unaFirma = this.numAprobaciones.equals("1");

		riesgoBc = SettingsRun.getTestData().getParameter("Nivel de Riesgo BC").trim();
		riesgoEfm = SettingsRun.getTestData().getParameter("Nivel de Riesgo SAS EFM").trim();

		navegador = SettingsRun.getTestData().getParameter("Navegador").trim();

		this.nombreEmpre = SettingsRun.getTestData().getParameter("Nombre Empresa").trim();

		this.nitEmpre = SettingsRun.getTestData().getParameter("Numero ID Empresa").trim();
		this.tipoIdEm = SettingsRun.getTestData().getParameter("Tipo ID Empresa");

		this.tipoCta = SettingsRun.getTestData().getParameter("Tipo producto origen / Franquicia").trim();
		this.numCta = SettingsRun.getTestData().getParameter("N�mero producto origen").trim();

		this.uuid = SettingsRun.getTestData().getParameter("Hash").trim();

		this.descripcionsin = SettingsRun.getTestData().getParameter("Descripcion").trim();

		this.valorsin = SettingsRun.getTestData().getParameter("Valor a Pagar / Transferir").trim();
		this.valorcon = SettingsRun.getTestData().getParameter("Valor a Pagar / Transferir");
		this.referenciasin = SettingsRun.getTestData().getParameter("Referencia").trim();
		this.desdeElDetalle = SettingsRun.getTestData().getParameter("Desde_el_Detalle").trim();

		// INGRESO DE LA REFERENCIA
		if (!this.servicio.contains("N�mi") && !this.servicio.contains("Proveedores")) {
			this.referencia1 = SettingsRun.getTestData().getParameter("Referencia1 / Tipo Producto Destino").trim();
			this.referencia2 = SettingsRun.getTestData().getParameter("Referencia2 / N�mero Producto Destino").trim();

		}

		if (this.servicio.contains("Pagos Automaticos") || this.servicio.contains("Servicios"))
			this.numConvenio = SettingsRun.getTestData().getParameter("N�mero de Convenio").trim();

		if (this.servicio.contains("Pagos Automaticos")) {
			this.diaDiaDelpago = SettingsRun.getTestData().getParameter("D�a del pago").trim();
			this.titul = SettingsRun.getTestData().getParameter("Nombre del Titular").trim();
			this.tipoCtaSecundario = SettingsRun.getTestData().getParameter("Tipo producto origen Secundario").trim();
			this.numCtaSecundario = SettingsRun.getTestData().getParameter("N�mero producto origen Secundario").trim();
		}

		if (this.servicio.contains("Cuenta No Inscrita") || this.servicio.contains("Cuenta Inscrita")) {

			this.bncoDest = SettingsRun.getTestData().getParameter("Banco Destino").trim();

			if (this.servicio.contains("Cuenta No Inscrita")) {
				this.titul = SettingsRun.getTestData().getParameter("Nombre del Titular").trim();
				this.tipoid = SettingsRun.getTestData().getParameter("Tipo Identificaci�n del Titular").trim();
				this.numid = SettingsRun.getTestData().getParameter("N�mero Identificaci�n del Titular").trim();
			}
		}

		if (this.servicio.contains("Avance TC")) {
			this.mesTC = SettingsRun.getTestData().getParameter("Mes Vencimiento TC").trim();
			this.avenTC = SettingsRun.getTestData().getParameter("A�o Vencimiento TC").trim();
			this.plazoDiAv = SettingsRun.getTestData().getParameter("Plazo a diferir el avance").trim();
		}

		// Divisas
		if (this.servicio.contains("Tx Internacionales Recibir desde el exterior")
				|| this.servicio.contains("Tx Internacionales Enviar al exterior")) {
			this.numerodereferenciaExterna = SettingsRun.getTestData().getParameter("N�mero de referencia Externa")
					.trim();
			pageDivisas = new PageDivisas(parentPage);
			pageEnviarTransInternacional = new PageEnviarTransInternacional(parentPage);
			pageRecibirTransferenciasInternacionales = new PageRecibirTransferenciasInternacionales(parentPage);
			pageDocumentos_Y_Formularios = new PageDocumentos_Y_Formularios(parentPage);
			pageConsultatxInternacional = new PageConsultatxInternacional(parentPage);

			this.tipoMoneda = SettingsRun.getTestData().getParameter("Tipo Moneda");
			this.modena = SettingsRun.getTestData().getParameter("Tipo Moneda").trim();
			this.concepTx = SettingsRun.getTestData().getParameter("Concepto de la transferencia").trim();
			this.concepto = SettingsRun.getTestData().getParameter("Concepto de la transferencia");
			this.valorNumeral1 = SettingsRun.getTestData().getParameter("Valor numeral cambiario 1");
			this.valorNumeral2 = SettingsRun.getTestData().getParameter("Valor numeral cambiario 2");
			this.tipoEnvio = SettingsRun.getTestData().getParameter("Tipo de Envio");
			this.FechaEnvioFrecuente = SettingsRun.getTestData().getParameter("Fecha env�o frecuente");
			this.nombreBene = SettingsRun.getTestData()
					.getParameter("Ordenante / Nombre del beneficiario en el exterior");
			this.paisDestino = SettingsRun.getTestData().getParameter("Pa�s de destino de la transferencia");
			this.ciudadDestino = SettingsRun.getTestData()
					.getParameter("Ciudad y pa�s donde est� ubicado el beneficiario");
			this.direccionBene = SettingsRun.getTestData().getParameter("Direcci�n del beneficiario");
			this.cuentaBene = SettingsRun.getTestData().getParameter("N�mero de cuenta, IBAN o CLABE");
			this.infoPago = SettingsRun.getTestData().getParameter("Informaci�n para el beneficiario");
			this.referenciaPago = SettingsRun.getTestData().getParameter("Informaci�n para el beneficiario Numero");

			this.tipoCodigo = SettingsRun.getTestData().getParameter("Tipo de c�digo");
			this.numeroCodigo = SettingsRun.getTestData().getParameter("N�mero de c�digo");
			this.intermediario = SettingsRun.getTestData().getParameter("Requiere un banco intermediario");
			this.tipoCodigoInter = SettingsRun.getTestData().getParameter("Tipo de c�digo Intermediario");
			this.numeroCodigoInter = SettingsRun.getTestData().getParameter("N�mero de c�digo Intermediario");
		}

		if (!DatosDavivienda.IS_RISKMOTOR) {
			nombreAmbiente = SettingsRun.getGlobalData("AMBIENTE_PYME");

			switch (nombreAmbiente) {
			case "1":
			case "PROYECTOS":
				nombreAmbiente = "PROYECTOS";
				break;
			case "2":
			case "CONTENCION":
				nombreAmbiente = "CONTENCION";
				break;
			case "3":
			case "OBSOLESCENCIA":
				nombreAmbiente = "OBSOLESCENCIA";
				break;
			case "4":
			case "ONPREMISE":
				nombreAmbiente = "ONPREMISE";
				break;
			case "5":
			case "POST_NUBE":
				nombreAmbiente = "POST_NUBE";
				break;
			case "6":
			case "CONTENCION_NUBE":
				nombreAmbiente = "CONTENCION_NUBE";
				break;
			case "7":
			case "PROYECTOS_NUBE":
				nombreAmbiente = "PROYECTOS_NUBE";
				break;
			case "8":
			case "MEJORAS":
				nombreAmbiente = "MEJORAS";
				break;
			default:
				Reporter.reportEvent(Reporter.MIC_FAIL, "Opci�n no v�lida");
				break;
			}
		}

	}

// ===========================================================================================================================================

// ====================================[crearPagoNomina_Proveedores]==============================================================================
	/**
	 * Intenta crear una transacci�n de pago de n�mina o de proveedores.<b> Si
	 * [soloGuardar] es [true] este m�todo llega hasta el punto de guardar sin
	 * aprobar. Si es [false] intenta realizar la transacci�n ingresando el token /
	 * otp.<br>
	 * En ambos casos deja almacenado en par�metro "N�mero Aprobaci�n" el n�mero de
	 * la transacci�n.
	 * 
	 * @throws Exception
	 */
	public void crearPagoNomina_Proveedores(boolean soloGuardar) throws Exception {

		NotificacionSMS.TIPO_NOTIFICACION = NotificacionSMS.TIPO_NOTIF_PYMES_PA_NOMPROV;

		this.inicioCrearTx(); // DEJA LA PANTALLA EN LA SELECCI�N DEL ORIGEN

		this.seleccionarCtaOrigen(false); // SELECCIONA LA CUENTA ORIGEN [false = PORQUE NO ES SERVICIO]

		// INGRESO DE DESTINOS
		if (controlDest == null)
			controlDest = new ControllerDestinosMasivos(pageDestino);

		String msgError = controlDest.cargarDestinos();

		if (msgError != null)
			this.terminarIteracionXError(msgError);

		if (this.servicio.contains("N�mina") || this.servicio.contains("Pago a Proveedores"))
			datoNumDestCuentas = controlDest.CuentasMasivas();

		this.montoReal = String.valueOf(controlDest.getValorTransaccion());

		this.totalDestinos = controlDest.getTotalDestinos();

		// INGRESO DESCRIPCI�N
		String descripcion = "PagoNomProv" + SettingsRun.getCurrentIteration();

		if (!DatosDavivienda.IS_RISKMOTOR)
			descripcion = this.descripcionsin;

		pageValores.ingresarDescripcion(descripcion);

		// CONFIRMAR TX
		msgError = pageConf.waitPantallaConfirmar(this.pageTitle);

		if (msgError != null)
			this.terminarIteracionXError(msgError);

		this.accionConfirmar(soloGuardar);

		if (!DatosDavivienda.IS_RISKMOTOR)
			if (DatosDavivienda.STRATUS != null)
				if (this.unaFirma)
					validacionSaldosStratus();

		// REALIZA LA APROBACION SOLO PARA LAS TRANSACCIONES CON ADENDA Y PEND. DE
		// APROBACION
		if (soloGuardar && adenda.equals("SI")) {
			this.aprobarTxPendiente(soloGuardar);
		}

		if (adenda.equals("SI")) {
			Reporter.reportEvent(Reporter.MIC_PASS, "*********Transferencia del Portal (Adenda)*********");
			if (servicio.equals("N�mina")) {
				this.pageOrigen.TranferenciaPortalAdenda("Pago de N�minas", "Pago de N�minas");
			} else {
				this.pageOrigen.TranferenciaPortalAdenda("Pago a Proveedores", "Pago a Proveedores");
			}

			Reporter.reportEvent(Reporter.MIC_PASS, "*********Movimientos y Extractos (Adenda)*********");

			if (servicio.equals("N�mina")) {
				this.Adenda("Descuento para pago de Nomina", null, "PAGO DE NOMINA");
			} else {
				this.Adenda("Descuento por Pago a Proveedores", null, "PAGO DE PROVEEDORES");
			}
		}
	}

// ===========================================================================================================================================

// ====================================[crearPagoServicios]==============================================================================

	/**
	 * Intenta crear una transacci�n de pago de servicios p�blicos.<b> Si
	 * [soloGuardar] es [true] este m�todo llega hasta el punto de guardar sin
	 * aprobar. Si es [false] intenta realizar la transacci�n ingresando el token /
	 * otp.<br>
	 * En ambos casos deja almacenado en par�metro "N�mero Aprobaci�n" el n�mero de
	 * la transacci�n.
	 * 
	 * @throws Exception
	 */
	public void crearPagoServicios(boolean soloGuardar) throws Exception {

		NotificacionSMS.TIPO_NOTIFICACION = NotificacionSMS.TIPO_NOTIF_PYMES_PA_SERV;

		this.inicioCrearTx(); // DEJA LA PANTALLA EN LA SELECCI�N DEL ORIGEN

		this.seleccionarCtaOrigen(true); // SELECCIONA LA CUENTA ORIGEN [true = PORQUE ES SERVICIO]

		String msgError = pageDestino.ingresarReferencia(this.referencia1, this.referencia2);

		if (msgError != null)
			this.terminarIteracionXError(msgError);

		// INGRESAR O EXTRAER EL VALOR DEL PAGO
		this.montoReal = pageValores.ingresarValor(this.valorsin); // 0-ValorTx, 1-MsgError
		this.totalDestinos = 1;

		SettingsRun.getTestData().setParameter("Valor a Pagar / Transferir", this.montoReal);
		// CONFIRMAR TX
		msgError = pageConf.waitPantallaConfirmar(this.pageTitle);

		if (msgError != null)
			this.terminarIteracionXError(msgError);

		this.accionConfirmar(soloGuardar);

		if (!DatosDavivienda.IS_RISKMOTOR)
			if (DatosDavivienda.STRATUS != null)
				if (this.unaFirma)
					validacionSaldosStratus();
	}

// ===========================================================================================================================================

// ====================================[crearPagosAutomaticos]==============================================================================

	/**
	 * Intenta crear una transacci�n de pagos asutomaticos.<b> Si [soloGuardar] es
	 * [true] este m�todo llega hasta el punto de guardar sin aprobar. Si es [false]
	 * intenta realizar la transacci�n ingresando el token / otp.<br>
	 * En ambos casos deja almacenado en par�metro "N�mero Aprobaci�n" el n�mero de
	 * la transacci�n.
	 * 
	 * @throws Exception
	 */

	public void crearPagosAutomaticos(boolean soloGuardar) throws Exception {

		NotificacionSMS.TIPO_NOTIFICACION = NotificacionSMS.TIPO_NOTIF_PYMES_PA_SERV;

		this.inicioCrearTx(); // DEJA LA PANTALLA EN LA SELECCI�N DEL ORIGEN

		this.seleccionarCtaOrigenPagosAuto(); // SELECCIONA LA CUENTA ORIGEN [true = PORQUE ES SERVICIO]

		String msgError = pageDestino.ingresarReferenciaAutom(this.referencia1, this.referencia2, this.titul);

		if (msgError != null)
			this.terminarIteracionXError(msgError);

		String correo = this.referenciasin;

		if (this.referenciasin == null || this.referenciasin.isEmpty())
			correo = "davivienda@proveedores.davivienda.com";

		this.montoReal = pageValores.ingresarValorAuto(correo, this.valorsin, this.diaDiaDelpago); // 0-ValorTx,
																									// 1-MsgError
		this.totalDestinos = 1;

		SettingsRun.getTestData().setParameter("Valor a Pagar / Transferir", this.montoReal);

		// CONFIRMAR TX
		msgError = pageConf.waitPantallaConfirmar(this.pageTitle);

		if (msgError != null)
			this.terminarIteracionXError(msgError);

		this.accionConfirmar(soloGuardar);

		if (!DatosDavivienda.IS_RISKMOTOR)
			if (DatosDavivienda.STRATUS != null)
				if (this.unaFirma)
					validacionSaldosStratus();
	}

// ===========================================================================================================================================

// ====================================[crearTranferenciaMismoNit]==============================================================================

	/**
	 * Intenta crear una transacci�n de transferencia mismo NIT.<b> Si [soloGuardar]
	 * es [true] este m�todo llega hasta el punto de guardar sin aprobar. Si es
	 * [false] intenta realizar la transacci�n ingresando el token / otp.<br>
	 * En ambos casos deja almacenado en par�metro "N�mero Aprobaci�n" el n�mero de
	 * la transacci�n.
	 */
	public void crearTranferenciaMismoNit(boolean soloGuardar) throws Exception {

		NotificacionSMS.TIPO_NOTIFICACION = NotificacionSMS.TIPO_NOTIF_PYMES_TF_MISMONIT;

		this.inicioCrearTx(); // DEJA LA PANTALLA EN LA SELECCI�N DEL ORIGEN

		this.seleccionarCtaOrigen(false); // SELECCIONA LA CUENTA ORIGEN [false = PORQUE NO ES SERVICIO]
		// SELECCIONA LA CUENTA DESTINO

		String msgError = pageDestino.seleccionarDestino(this.referencia1, this.referencia2);

		if (msgError != null) {
			this.terminarIteracionXError(msgError);
		}

		this.ingresarValores("MismoNit"); // INGRESAR VALOR, DESCRIPCI�N Y REFERENCIA / GARANTIZA PANTALLA "Confirmar
		// CONFIRMAR TX

		msgError = pageConf.waitPantallaConfirmar(this.pageTitle);

		if (msgError != null)
			this.terminarIteracionXError(msgError);

		this.accionConfirmar(soloGuardar);

		if (!DatosDavivienda.IS_RISKMOTOR)
			if (DatosDavivienda.STRATUS != null)
				if (this.unaFirma)
					validacionSaldosStratus();

		// REALIZA LA APROBACION SOLO PARA LAS TRANSACCIONES CON ADENDA Y PEND. DE
		// APROBACION
		if (soloGuardar && adenda.equals("SI")) {
			this.aprobarTxPendiente(soloGuardar);
		}

		if (adenda.equals("SI")) {
			Reporter.reportEvent(Reporter.MIC_PASS, "*********Transferencia del Portal (Adenda)*********");
			this.pageOrigen.TranferenciaPortalAdenda("Transferencias Mismo NIT", "Transferencias Mismo NIT");
			Reporter.reportEvent(Reporter.MIC_PASS, "*********Movimientos y Extractos (Adenda)*********");
			this.Adenda("Dcto por Transferencia de Fondos", "Abono Transferencia", "TRANSFERENCIA TERCEROS");
		}
	}

// ===========================================================================================================================================

// ====================================[crearTransferenciaCtaNoInscrita]==============================================================================

	/**
	 * Intenta crear una transacci�n de transferencia cuenta No inscrita.<b> Si
	 * [soloGuardar] es [true] este m�todo llega hasta el punto de guardar sin
	 * aprobar. Si es [false] intenta realizar la transacci�n ingresando el token /
	 * otp.<br>
	 * En ambos casos deja almacenado en par�metro "N�mero Aprobaci�n" el n�mero de
	 * la transacci�n.
	 * 
	 * @throws Exception
	 */
	public void crearTransferenciaCtaNoInscrita(boolean soloGuardar) throws Exception {
		NotificacionSMS.TIPO_NOTIFICACION = NotificacionSMS.TIPO_NOTIF_PYMES_TF_NOINSC;
		this.inicioCrearTx(); // DEJA LA PANTALLA EN LA SELECCI�N DEL ORIGEN
		this.seleccionarCtaOrigen(false); // SELECCIONA LA CUENTA ORIGEN [false = PORQUE NO ES SERVICIO]
		// SELECCIONA DESTINO
		// Datos destino
		String msgError = pageDestino.ingresarDestino(this.bncoDest, this.referencia1, this.referencia2, this.titul,
				tipoid, numid);
		if (msgError != null)
			this.terminarIteracionXError(msgError);
		this.ingresarValores("CuentaNoInscrita"); // INGRESAR VALOR, DESCRIPCI�N Y REFERENCIA / GARANTIZA PANTALLA
													// "Confirmar
		// CONFIRMAR TX
		this.accionConfirmar(soloGuardar);
		if (!DatosDavivienda.IS_RISKMOTOR)
			if (DatosDavivienda.STRATUS != null)
				if (this.unaFirma)
					validacionSaldosStratus();

		// REALIZA LA APROBACION SOLO PARA LAS TRANSACCIONES CON ADENDA Y PEND. DE
		// APROBACION
		if (soloGuardar && adenda.equals("SI")) {
			this.aprobarTxPendiente(soloGuardar);
		}

		if (adenda.equals("SI")) {
			Reporter.reportEvent(Reporter.MIC_PASS, "*********Transferencia del Portal (Adenda)*********");
			this.pageOrigen.TranferenciaPortalAdenda("Transferencias Cuenta No Inscrita",
					"Transferencias Cuenta No Inscrita");
			Reporter.reportEvent(Reporter.MIC_PASS, "*********Movimientos y Extractos (Adenda)*********");
			this.Adenda("Dcto por Transferencia de Fondos", null, "TRANSFERENCIA TERCEROS");
		}
	}

// ===========================================================================================================================================

// ====================================[crearTransferenciaCtaInscrita]==============================================================================
	/**
	 * Intenta crear una transacci�n de transferencia cuenta inscrita.<b> Si
	 * [soloGuardar] es [true] este m�todo llega hasta el punto de guardar sin
	 * aprobar. Si es [false] intenta realizar la transacci�n ingresando el token /
	 * otp.<br>
	 * En ambos casos deja almacenado en par�metro "N�mero Aprobaci�n" el n�mero de
	 * la transacci�n.
	 * 
	 * @throws Exception
	 */
	public void crearTransferenciaCtaInscrita(boolean soloGuardar) throws Exception {
		NotificacionSMS.TIPO_NOTIFICACION = NotificacionSMS.TIPO_NOTIF_PYMES_TF_CTAINSC;
		this.inicioCrearTx(); // DEJA LA PANTALLA EN LA SELECCI�N DEL ORIGEN
		this.seleccionarCtaOrigen(false); // SELECCIONA LA CUENTA ORIGEN [false = PORQUE NO ES SERVICIO]
		// SELECCIONA DESTINO

//		String bncoDest = SettingsRun.getTestData().getParameter("Banco Destino").trim();
		String msgError = pageDestino.seleccionarCuentaInscri(this.referencia1, referencia2);
		if (msgError != null)
			this.terminarIteracionXError(msgError);
		this.ingresarValores("CuentaInscrita"); // // INGRESAR VALOR, DESCRIPCI�N Y REFERENCIA / GARANTIZA PANTALLA
												// "Confirmar
		// CONFIRMAR TX
		this.accionConfirmar(soloGuardar);
		if (!DatosDavivienda.IS_RISKMOTOR)
			if (DatosDavivienda.STRATUS != null)
				if (this.unaFirma)
					validacionSaldosStratus();
		// REALIZA LA APROBACION SOLO PARA LAS TRANSACCIONES CON ADENDA Y PEND. DE
		// APROBACION
		if (soloGuardar && adenda.equals("SI")) {
			this.aprobarTxPendiente(soloGuardar);
		}
		if (adenda.equals("SI")) {
			Reporter.reportEvent(Reporter.MIC_PASS, "*********Transferencia del Portal (Adenda)*********");
			this.pageOrigen.TranferenciaPortalAdenda("Transferencias Cuenta Inscrita",
					"Transferencias Cuenta Inscrita");
			Reporter.reportEvent(Reporter.MIC_PASS, "*********Movimientos y Extractos (Adenda)*********");
			this.Adenda("Dcto por Transferencia de Fondos", null, "TRANSFERENCIA TERCEROS");
		}
	}

// ===========================================================================================================================================

// ====================================[crearPagosPropios]==============================================================================
	/**
	 * Intenta crear una transacci�n de Pagos Propios.<b> Si [soloGuardar] es [true]
	 * este m�todo llega hasta el punto de guardar sin aprobar. Si es [false]
	 * intenta realizar la transacci�n ingresando el token / otp.<br>
	 * En ambos casos deja almacenado en par�metro "N�mero Aprobaci�n" el n�mero de
	 * la transacci�n.
	 */
	public void crearPagosPropios(boolean soloGuardar) throws Exception {
		NotificacionSMS.TIPO_NOTIFICACION = NotificacionSMS.TIPO_NOTIF_PYMES_TF_MISMONIT;
		this.inicioCrearTx(); // DEJA LA PANTALLA EN LA SELECCI�N DEL ORIGEN
		this.seleccionarCtaOrigen(false); // SELECCIONA LA CUENTA ORIGEN [false = PORQUE NO ES SERVICIO]
		// SELECCIONA LA CUENTA DESTINO
		String msgError = pageDestino.seleccionarDestinoPropios(this.referencia1, this.referencia2);
		if (msgError != null)
			this.terminarIteracionXError(msgError);
		this.ingresarValores("PagosPropios"); // INGRESAR VALOR, DESCRIPCI�N Y REFERENCIA / GARANTIZA PANTALLA
												// "Confirmar
		// CONFIRMAR TX
		msgError = pageConf.waitPantallaConfirmar(this.pageTitle);
		if (msgError != null)
			this.terminarIteracionXError(msgError);
		this.accionConfirmar(soloGuardar);
		if (!DatosDavivienda.IS_RISKMOTOR)
			if (DatosDavivienda.STRATUS != null)
				if (this.unaFirma)
					validacionSaldosStratus();
	}

// ===========================================================================================================================================

// ====================================[crearAvanceTC]==============================================================================
	/**
	 * Intenta crear una transacci�n de Pagos Propios.<b> Si [soloGuardar] es [true]
	 * este m�todo llega hasta el punto de guardar sin aprobar. Si es [false]
	 * intenta realizar la transacci�n ingresando el token / otp.<br>
	 * En ambos casos deja almacenado en par�metro "N�mero Aprobaci�n" el n�mero de
	 * la transacci�n.
	 */
	public void crearAvanceTC(boolean soloGuardar) throws Exception {
		NotificacionSMS.TIPO_NOTIFICACION = NotificacionSMS.TIPO_NOTIF_PYMES_TF_MISMONIT;
		this.inicioCrearTx(); // DEJA LA PANTALLA EN LA SELECCI�N DEL ORIGEN
		this.seleccionarCtaOrigen(false); // SELECCIONA LA CUENTA ORIGEN [false = PORQUE NO ES SERVICIO]
		// SELECCIONA LA CUENTA DESTINO
		String msgError = pageDestino.seleccionarDestino(this.referencia1, this.referencia2);
		if (msgError != null)
			this.terminarIteracionXError(msgError);
		this.ingresarValores("AvanceTC"); // INGRESAR VALOR, DESCRIPCI�N Y REFERENCIA / GARANTIZA PANTALLA "Confirmar
		// CONFIRMAR TX
		msgError = pageConf.waitPantallaConfirmar(this.pageTitle);
		if (msgError != null)
			this.terminarIteracionXError(msgError);
		this.accionConfirmar(soloGuardar);
		if (!DatosDavivienda.IS_RISKMOTOR)
			if (DatosDavivienda.STRATUS != null)
				if (this.unaFirma)
					validacionSaldosStratus();

	}

// ===========================================================================================================================================

// ====================================[inicioCrearTx]==============================================================================

	/**
	 * Metodo se ubica en la ruta de inicio de creacion de [Pagos, Transferencias y
	 * otros] y seleciona el link del servicio a realizar
	 * 
	 * @throws Exception
	 */
	public void inicioCrearTx() throws Exception {

		String msgError = null;
		String pagoyTx = this.pageOrigen.getTextMid();

		if (pagoyTx.contains("Pagos, Transferencias y otros")) {
			if (navegador.contains("CHROME")) {
				msgError = pageOrigen.irAOpcion("Crear Transacci�n", "Pagos, Transferencias y otros",
						"Crear Transacci�n");

			} else {
				msgError = pageOrigen.irAOpcionMoZilla("Crear Transacci�n", "Pagos, Transferencias y otros",
						"Crear Transacci�n", null, null);

			}

		} else if (pagoyTx.contains("Pagos, Transferencias y Otros")) {

			if (navegador.contains("CHROME")) {
				msgError = pageOrigen.irAOpcion("Crear Transacci�n", "Pagos, Transferencias y Otros",
						"Crear Transacci�n");

			} else {
				msgError = pageOrigen.irAOpcionMoZilla("Crear Transacci�n", "Pagos, Transferencias y Otros",
						"Crear Transacci�n", null, null);

			}

		} else if (pagoyTx.contains("Pagos, Transferencias y Otros")) {

			if (navegador.contains("CHROME")) {
				msgError = pageOrigen.irAOpcion("Crear Transacci�n", "Pagos, Transferencias y Otros",
						"Crear Transacci�n");

			} else {
				msgError = pageOrigen.irAOpcionMoZilla("Crear Transacci�n", "Pagos, Transferencias y Otros",
						"Crear Transacci�n", null, null);

			}

		}

		else if (pagoyTx.contains("Pagos, Transferencias e Inscripciones")) {

			if (navegador.contains("CHROME")) {
				msgError = pageOrigen.irAOpcion("Crear Transacci�n", "Pagos, Transferencias e Inscripciones",
						"Crear Transacci�n");

			} else {
				msgError = pageOrigen.irAOpcionMoZilla("Crear Transacci�n", "Pagos, Transferencias e Inscripciones",
						"Crear Transacci�n", null, null);

			}

		} else if (pagoyTx.contains("Pagos, Transferencias e inscripciones")) {

			if (navegador.contains("CHROME")) {
				msgError = pageOrigen.irAOpcion("Crear Transacci�n", "Pagos, Transferencias e inscripciones",
						"Crear Transacci�n");

			} else {
				msgError = pageOrigen.irAOpcionMoZilla("Crear Transacci�n", "Pagos, Transferencias e inscripciones",
						"Crear Transacci�n", null, null);

			}

		}

		else {
			if (navegador.contains("CHROME")) {
				msgError = pageOrigen.irAOpcion("Crear Transacci�n", "Pagos y Transferencias", "Crear Transacci�n");

			} else {
				msgError = pageOrigen.irAOpcionMoZilla("Crear Transacci�n", "Pagos y Transferencias",
						"Crear Transacci�n", null, null);

			}
		}

		if (msgError != null)
			this.terminarIteracionXError(msgError);

		msgError = pageOrigen.irAServicio(this.servicio);
		if (msgError != null)
			this.terminarIteracionXError(msgError);

		// Nivel de riesgo Motor
		this.riesgo = this.prioridaRiesgo;

	}

// =======================================================================================================================

// ===========================================[seleccionarCtaOrigen]===========================================================================

	/**
	 * Metodo seleciona las cta origen dependiendo el servicio
	 * 
	 * @param isServPublico
	 * @throws Exception
	 */
	private void seleccionarCtaOrigen(boolean isServPublico) throws Exception {
		// SELECCIONA LA CUENTA ORIGEN

		String msgError;
		if (isServPublico) {
			Evidence.saveAllScreens("Origentx", pageOrigen);
			msgError = pageOrigen.seleccionarOrigen(this.servicio, this.tipoIdEm, this.nitEmpre, this.numConvenio,
					this.tipoCta, this.numCta);
		} else
			msgError = pageOrigen.seleccionarOrigen(this.servicio, this.tipoIdEm, this.nitEmpre, this.tipoCta,
					this.numCta);

		if (msgError != null)
			this.terminarIteracionXError(msgError);
	}

// =======================================================================================================================

// ===========================================[seleccionarCtaOrigenPagosAuto]===========================================================================

	/**
	 * Metodo seleciona la cta origen de pagos automaticos
	 * 
	 * @throws Exception
	 */
	private void seleccionarCtaOrigenPagosAuto() throws Exception {
		// SELECCIONA LA CUENTA ORIGEN

		String msgError;

		Evidence.saveAllScreens("Origentx", pageOrigen);
		msgError = pageOrigen.seleccionarOrigenAuto(this.numConvenio, this.tipoCta, this.numCta, this.tipoCtaSecundario,
				this.numCtaSecundario);

		if (msgError != null)
			this.terminarIteracionXError(msgError);
	}

// =======================================================================================================================

// ===========================================[ingresarValores]===========================================================================

	/**
	 * Metodo se encarga de ir a la pantalla de Valores para los servicios [MismoNit
	 * - CuentaNoInscrita - CuentaInscrita - PagosPropios - AvanceTC]
	 * 
	 * @param textoDescripcion
	 * @throws Exception
	 */
	private void ingresarValores(String textoDescripcion) throws Exception {

		// INGRESAR VALOR, DESCRIPCI�N Y REFERENCIA

		String descripcion = textoDescripcion + SettingsRun.getCurrentIteration();
		String referencia = "";
		if (!DatosDavivienda.IS_RISKMOTOR) { // @PEDRO CUANDO SEAN PRUEBAS DE ADENDA
			descripcion = this.descripcionsin;

		}

		if (textoDescripcion == "MismoNit")
			pageValores.ingresarValores(this.valorsin, descripcion, this.referenciasin);

		if (textoDescripcion == "CuentaNoInscrita")
			pageValores.ingresarValoresCuentaNo(this.valorsin, descripcion, this.referenciasin);

		if (textoDescripcion == "CuentaInscrita")
			pageValores.ingresarValoresCuentaIsncr(this.valorsin, descripcion, this.referenciasin);

		if (textoDescripcion == "PagosPropios")
			pageValores.ingresarValoresPropios(this.valorsin);

		if (textoDescripcion == "AvanceTC") {

			pageValores.ingresarValoresAvanceTC(this.valorsin, descripcion, this.mesTC, this.avenTC, this.plazoDiAv);
		}
		String msgError = pageConf.waitPantallaConfirmar(this.pageTitle);
		if (msgError != null)
			this.terminarIteracionXError(msgError);
		this.montoReal = this.valorsin;
		this.totalDestinos = 1;
	}

// =======================================================================================================================

// ===========================================[Adenda]===========================================================================

	/**
	 * Realiza el flujo de adenda para la ventana de "Consultas y Extractos"<br>
	 * <b> Primer String es la descripcion de la transaccion que se esta ejecutando,
	 * como aparece en la tabla de "Consultas y Extractos"<br>
	 * Segundo String descripcion para una cuenta destino, si se envia NULL
	 * significa que es una cuenta externa y no se puede validar
	 */
	private void Adenda(String descripcionTxOrigen, String descripcionTxDestino, String detalleVacio) throws Exception {

		String msgError = pageOrigen.irAOpcion("Movimientos y Extractos por Producto", "Consultas",
				"Movimientos y Extractos");

		String xPathRBSelect = "//select[@id='cphCuerpo_ddlproducto']";
		String xPathPO = DXCUtil.right(this.numCta, 4);
		String xPathPD = DXCUtil.right(this.referencia2, 4);

		DXCUtil.wait(5);

		this.pageOrigen.SeleccionarOrigenDetinoAdenda(xPathRBSelect, xPathPO);
		Reporter.reportEvent(Reporter.MIC_PASS, "------Cuenta Origen------");
		this.pageOrigen.NotaDebitoAdenda(descripcionTxOrigen, detalleVacio);

		DXCUtil.wait(6);

		if (descripcionTxDestino != null) {
			this.pageOrigen.SeleccionarOrigenDetinoAdenda(xPathRBSelect, xPathPD);
			Reporter.reportEvent(Reporter.MIC_PASS, "------Cuenta Destino------");
			this.pageOrigen.NotaDebitoAdenda(descripcionTxDestino, detalleVacio);
		}

		this.pageOrigen.terminarIteracion();
	}

// =======================================================================================================================

// ===========================================[terminarIteracionXError]===========================================================================

	/**
	 * Metodo se encarga de Termininar la interacion y guardar el mensaje de error
	 * 
	 * @param msgError
	 * @throws Exception
	 */
	private void terminarIteracionXError(String msgError) throws Exception {

		if (msgError == null) {
			msgError = "Error_1";
		}
		Reporter.write(msgError);

		Evidence.save(msgError.replace(" ", ""));

		if (DatosDavivienda.IS_RISKMOTOR) {
			Reporter.reportEvent(Reporter.MIC_FAIL, "No se adiciona a MR >>> " + msgError);
			Reporter.write("No se adiciona a MR >>> " + msgError);
		} else {
			Reporter.write("En construcci�n XQ no s� si es error >>> " + msgError);
			Reporter.reportEvent(Reporter.MIC_FAIL, msgError);
		}
		this.pageOrigen.terminarIteracion();
	}

	/**
	 * Metodo se encarga de Termininar la interacion y guardar el mensaje de error
	 * 
	 * @param msgError
	 * @throws Exception
	 */
	private void terminarIteracionXErrorMotor(String msgError) throws Exception {

		if (msgError == null || msgError.equals("") || msgError.equals(" ")) {
			msgError = "Error_1";
		}

		Reporter.write(msgError);

		Evidence.save(msgError.replace(" ", ""));
		if (DatosDavivienda.IS_RISKMOTOR) {
			this.riesgo = this.prioridaRiesgo;
			if (this.riesgo.equals("Alto")) {
				Reporter.reportEvent(Reporter.MIC_PASS, "Se adiciona a MR >>> " + msgError);
				Reporter.write("Se adiciona a MR >>> " + msgError);

			} else {
				Reporter.reportEvent(Reporter.MIC_FAIL, "Se adiciona a MR para su validaci�n >>> " + msgError);
				Reporter.write("Se adiciona a MR para su validaci�n >>> " + msgError);
			}
		} else {
			Reporter.write("En construcci�n XQ no s� si es error >>> " + msgError);
			Reporter.reportEvent(Reporter.MIC_FAIL, msgError);
		}

		this.pageOrigen.terminarIteracion();
	}

// =======================================================================================================================

// ===========================================[accionConfirmar]===========================================================================

	/**
	 * Metodo Realiza la acion de guardar sin aprobar Tx o aprobar traansacion
	 * verificando mensajes exitoso o fallidos
	 * 
	 * @param soloGuardar
	 * @throws Exception
	 */
	private void accionConfirmar(boolean soloGuardar) throws Exception {
		this.estadoTx = null;
		// CONFIRMAR TX
		String msgRta, msgError;
		if (soloGuardar) {
			msgRta = pageConf.guardarSinAprobar();

//			this.desdeElDetalle = SettingsRun.getTestData().getParameter("Desde_el_Detalle").trim();

			if (this.desdeElDetalle.equals("NO") && this.servicio.contains("N�mina")
					|| this.servicio.contains("Pago a Proveedores") || this.servicio.contains("AFC")
					|| this.servicio.contains("Cr�dito.3ros")) {
				if (DatosDavivienda.IS_RISKMOTOR)
					if (pageConf.CuentaDesMotor() != null) {
						this.cuentasDesMotor = pageConf.CuentaDesMotor();
					}
			}

			if (msgRta.contains(PageConfirmacion.MSG_EXITO_DECL4) || !msgRta.contains(PageConfirmacion.MSG_EXITO_GUARD)
					&& !msgRta.contains(PageConfirmacion.MSG_EXITO_PAGO)
					&& !msgRta.contains(PageConfirmacion.MSG_EXITO_ENV_OTP)
					&& !msgRta.contains(PageConfirmacion.MSG_EXITO_APROB))
				this.terminarIteracionXError(msgRta);

		} else {
			int numRetosFalla = 0;
			if (DatosDavivienda.IS_RISKMOTOR) {
				String datonumRetosFalla = SettingsRun.getTestData().getParameter("Ingresos Fallidos").trim();
				if (datonumRetosFalla == null || datonumRetosFalla.equals("")) {
					datonumRetosFalla = "0";
				}
				numRetosFalla = Integer.valueOf(datonumRetosFalla);
			}
			if (DatosDavivienda.IS_RISKMOTOR) {
				if (this.servicio.contains("N�mina") || this.servicio.contains("Pago a Proveedores")) {

					if (pageConf.CuentaDesMotor() != null) {
						this.cuentasDesMotor = pageConf.CuentaDesMotor();
					}
				}

			}
			msgRta = pageConf.aprobarTx(numRetosFalla, true);

			if (DatosDavivienda.IS_RISKMOTOR) {
				this.estadoTx = msgRta;
				this.SetEstado(msgRta);

				if (DXCUtil.itemContainsAnyArrayItem(msgRta, arrMsgBuscarEstado)) {
					this.estadoTx = "Transferencia Realizada";// @pedro buscar estado de la transaccion
					Reporter.reportEvent(Reporter.MIC_INFO, msgRta);
				} else if (DXCUtil.itemContainsAnyArrayItem(msgRta, this.arrMsgTxDeclinada)) {
					Reporter.reportEvent(Reporter.MIC_INFO, msgRta);
					this.estadoTx = "DECLINADA";
					if (DatosDavivienda.IS_RISKMOTOR) {

						this.valorTx = pageConf.getValorFinalTx();
						if (this.valorTx != null)
							SetActivityAmount(this.valorTx.replace("$", "").replace(" ", ""));

						this.bancoDesTx = pageConf.getBancoDesTx();
						if (this.bancoDesTx != null)
							SetBancoDesTx(this.bancoDesTx);
					}

					this.numeroTx = pageConf.getNumeroDocumento();

//					if (this.numeroTx != null)
//						SetNumApr(this.numeroTx);

					if (this.servicio.contains("N�mina") || this.servicio.contains("Pago a Proveedores")
							|| this.servicio.contains("AFC") || this.servicio.contains("Cr�dito.3ros")) {
						this.validarIngresoMRDestinosMasivo();
					} else {
						this.validarIngresoMR();
					}
					this.pageOrigen.terminarIteracion();
				}
			} else {
				this.numeroTx = pageConf.getNumeroDocumento();
			}
//			else {
//				System.out.println("En construcci�n depende del tipo de la prueba");
//			}
			if (msgRta.contains(PageConfirmacion.MSG_EXITO_DECL4) || !msgRta.contains(PageConfirmacion.MSG_EXITO_GUARD)
					&& !msgRta.contains(PageConfirmacion.MSG_EXITO_PAGO)
					&& !msgRta.contains(PageConfirmacion.MSG_EXITO_ENV_OTP)
					&& !msgRta.contains(PageConfirmacion.MSG_EXITO_APROB))
				this.terminarIteracionXError(msgRta);
		}
		// SI LLEGA A ESTE PUNTO SE GUARD� SIN APROBAR DE FORMA EXITOSA : EXTRAE EL
		// N�MERO DE DOCUMENTO

		if (DatosDavivienda.IS_RISKMOTOR) {

			this.bancoDesTx = pageConf.getBancoDesTx();

			if (this.bancoDesTx != null)
				SetBancoDesTx(this.bancoDesTx);

			this.valorTx = pageConf.getValorFinalTx();

			if (this.valorTx != null)
				SetActivityAmount(this.valorTx.replace("$", "").replace(" ", ""));

			this.numeroTx = pageConf.getNumeroDocumento();
//			if (this.numeroTx != null)
//				SetNumApr(this.numeroTx);

		} else {
			this.numeroTx = pageConf.getNumeroDocumento();
		}

		if (this.numeroTx == null) {
			if (DatosDavivienda.IS_RISKMOTOR) {
				if (this.servicio.contains("N�mina") || this.servicio.contains("Pago a Proveedores")
						|| this.servicio.contains("AFC") || this.servicio.contains("Cr�dito.3ros")) {
					this.validarIngresoMRDestinosMasivo();
				} else {

					this.validarIngresoMR();
				}
			}
			this.terminarIteracionXError("No se encontr� el N�mero de Documento de la transacci�n");
		}

		this.pageConsultasyExtractos.SetNumApr(this.numeroTx);

		SettingsRun.getTestData().setParameter("N�mero Aprobaci�n", this.numeroTx);

//		SetNumApr(this.numeroTx);
	}

// =======================================================================================================================

// ===========================================[validacionDescuentoTx]===========================================================================

	/**
	 * Se haran los descuentos de acuerdo al cobro
	 * 
	 * @param resultsMiddle
	 * @param cobrosStratus
	 * @return
	 * @throws Exception
	 */
	public double[] validacionDescuentoTx(List<HashMap<String, String>> resultsMiddle,
			HashMap<String, Double> cobrosStratus, String[] estadotxFinal, boolean informacionConsultada)
			throws Exception {
		// se crea una instancia para llamar a base middle cobros que tiene metodos que
		// influyen en este caso
		this.baseMiddleCobros = new BaseMiddleCobros();
		// variables que se usaran para almacenar informaci�n
		String[] datosFinales = null;
		String tipoDestino = null;
		String usarDestino = null;
		String estadoTx = null;
		String descuentoAplicarCombo = null;
		String aplicaCombo = null;
		String motivoCobro = null;
		double valorCobro = 0;
		double valorCobroRechazo = 0;
		double descuentoXRechazo = 0;
		double descuentoXTx = 0;
		double descuentoDentroRango = 0;
		double descuentoSobreRango = 0;
		double descuentoXTxRechazo = 0;
		int totalTxAcumuladas = 0;
		double[] resultado = new double[estadotxFinal.length];
		String tipoCobro = SettingsRun.getTestData().getParameter("Tipo Prueba Cobros");
		// Obtiene el servicio que va a ser usado
		String servicio = PageServicios.getNbLinkServicio(SettingsRun.getTestData().getParameter("Servicio"));
		// Se obtiene el resultado de la lista
		HashMap<String, String> infoMiddle = resultsMiddle.get(0);
		String verificarCombo = infoMiddle.get("Verificacion Combo");
		// Estos datos obtenidos del hashmap, nos trae
		double topeInferiorSaldo = Double.parseDouble(infoMiddle.get("Tope inferior saldo"));
		double topeSuperiosSaldo = Double.parseDouble(infoMiddle.get("Tope superior saldo"));
		double saldoPromedioMesAnt = Double.parseDouble(infoMiddle.get("Saldo Promedio Mes Anterior"));
		// Trae un dato que puede ser "SI" o "NO" que nos ayuda a identificar si en el
		String indicadorCumplimiento = infoMiddle.get("Indicador cumplimiento Saldo Promedio");
		for (int i = 0; i < resultado.length; i++) {
			// Nos ayuda a separar la informaci�n que se encuentra en cada posici�n
			datosFinales = estadotxFinal[i].split("\\|");
			DXCUtil.wait(2);
			if (servicio.equalsIgnoreCase("Pago de N�minas") || servicio.equalsIgnoreCase("Pago a Proveedores")) {
				// Extraemos la posici�n 1 donde identificamos el tipo destino al cual se hace a
				// transacci�n
				tipoDestino = datosFinales[1];
				// Este string nos ayuda a identificar a que tipo destino hace parte la tx por
				// medio del metodo
				usarDestino = this.baseMiddleCobros.tipoDestino(tipoDestino);
				// Este dato se llama para veificar si el check del contador esta seleccionado o
				// no
				String indicaAplicaContador = infoMiddle.get("Aplica contador " + usarDestino);
				if (indicaAplicaContador.equals("S")) {
					// Ayudara para saber cual es la cantidad de transacci�n por el tipo de destino
					switch (usarDestino) {
					case "Davivienda":
						incCuentasDavivienda++;
						break;
					case "Otros Bancos":
						incCuentasOtrosBancos++;
						break;
					case "Tarjeta Prepago Maestro":
						incTarjetaPrepago++;
						break;
					case "Daviplata":
						incDaviplata++;
						break;
					case "Dep�sitos Electr�nicos":
						incDepositosElectronicos++;
						break;
					// No se usa por ahora daviplata + davivienda
					default:
						Reporter.write("No se encontr� ningun destino relacionado para aplicar contador");
						break;
					}
				}
				// Estos double nos ayuda extrayendo informaci�n extraida del servicio en middle
				// para aplicar descuentos si aplica
				descuentoDentroRango = Double.parseDouble(infoMiddle.get("Descuento dentro rango " + usarDestino));
				descuentoSobreRango = Double.parseDouble(infoMiddle.get("Descuento sobre rango " + usarDestino));
				descuentoXTxRechazo = Double.parseDouble(infoMiddle.get("Descuento por Tx rechazada " + usarDestino));
				// Este estado nos permite saber el estado de los pagos
				estadoTx = datosFinales[3];
				String motivoCobroRechazo = "";
				valorCobroRechazo = 0;
				// Este dato nos permite obtener el dato de la transacci�n
				motivoCobro = this.baseMiddleCobros.diccionarioSegmentos(servicio + " " + tipoDestino);
				valorCobro = cobrosStratus.get(motivoCobro);
				if (estadoTx.equals("Pago Rechazado")) {
					// Este dato nos permite obtener el dato de la transacci�n por rechazo
					motivoCobroRechazo = this.baseMiddleCobros
							.diccionarioSegmentos(servicio + " " + tipoDestino + " Rechazo");
					valorCobroRechazo = cobrosStratus.get(motivoCobroRechazo);
				}
			} else {
				// Extraemos la posici�n 1 donde identificamos el tipo destino al cual se hace a
				// transacci�n
				tipoDestino = datosFinales[0];
				usarDestino = this.baseMiddleCobros.tipoDestino(tipoDestino);
				// Este estado nos permite saber el estado de los pagos
				estadoTx = datosFinales[1];
				// Verifica si aplica contador
				String indicaAplicaContador = infoMiddle.get("Aplica contador " + usarDestino);
				if (indicaAplicaContador != null && indicaAplicaContador.equals("S")) {
					// Ayudara para saber cual es la cantidad de transacci�n por el tipo de destino
					switch (usarDestino) {
					case "Davivienda":
						incCuentasDavivienda++;
						break;
					case "Otros Bancos":
						incCuentasOtrosBancos++;
						break;
					case "Tarjeta Prepago Maestro":
						incTarjetaPrepago++;
						break;
					case "Daviplata":
						incDaviplata++;
						break;
					case "Dep�sitos Electr�nicos":
						incDepositosElectronicos++;
						break;
					case "Pago de Servicios":
						incPagoServicios++;
						break;
					// No se usa por ahora daviplata + davivienda
					default:
						Reporter.write("No se encontr� ningun destino relacionado para aplicar contador");
						break;
					}
				}
				// Estos double nos ayuda extrayendo informaci�n extraida del servicio en middle
				// para aplicar descuentos si aplica
				if (servicio.contains("Pago de Servicios")) {
					descuentoDentroRango = Double.parseDouble(infoMiddle.get("Descuento dentro rango " + servicio));
					descuentoSobreRango = Double.parseDouble(infoMiddle.get("Descuento sobre rango " + servicio));
					descuentoXTxRechazo = 0;
				} else {
					// Estos double nos ayuda extrayendo informaci�n extraida del servicio en middle
					// para aplicar descuentos si aplica
					if (infoMiddle.get("Descuento dentro rango " + usarDestino) != null)
						descuentoDentroRango = Double
								.parseDouble(infoMiddle.get("Descuento dentro rango " + usarDestino));
					if (infoMiddle.get("Descuento sobre rango " + usarDestino) != null)
						descuentoSobreRango = Double
								.parseDouble(infoMiddle.get("Descuento sobre rango " + usarDestino));
					if (infoMiddle.get("Descuento por Tx rechazada " + usarDestino) != null)
						descuentoXTxRechazo = Double
								.parseDouble(infoMiddle.get("Descuento por Tx rechazada " + usarDestino));
				}

				if (servicio.equalsIgnoreCase("Pago de servicios"))
					motivoCobro = this.baseMiddleCobros.diccionarioSegmentos(servicio);
				else
					motivoCobro = this.baseMiddleCobros.diccionarioSegmentos(servicio + " " + tipoDestino);
				if (motivoCobro != null)
					valorCobro = cobrosStratus.get(motivoCobro);
			}
			DXCUtil.wait(2);
			// Se verifica si la informaci�n llega cobro con combos
			if (tipoCobro.equals("COBROS CON COMBOS") && informacionConsultada == true
					|| tipoCobro.equals("COBROS SIN COMBOS") && informacionConsultada == true) {
				// Este dato nos permite saber que si la prueba es con combos, que el check se
				// encuentre seleccionado
				descuentoAplicarCombo = "Descuento para combo " + usarDestino;
				aplicaCombo = infoMiddle.get(descuentoAplicarCombo);
				if (estadoTx.equals("Pago Exitoso") || estadoTx.equals("Pendiente")
						|| estadoTx.equals("Enviado a Otro Banco")) {
					if (indicadorCumplimiento.equals("S")) {
						// valida la informaci�n con el indicador en S
						resultado[i] = this.baseMiddleCobros.descuentoIndicadorSConCombo(aplicaCombo,
								saldoPromedioMesAnt, topeInferiorSaldo, topeSuperiosSaldo, descuentoXTx, valorCobro,
								descuentoDentroRango, descuentoSobreRango);
					} else if (indicadorCumplimiento.equals("N")) {
						// valida la informaci�n con el indicador en N
						resultado[i] = this.baseMiddleCobros.descuentoIndicadorNConCombo(aplicaCombo,
								saldoPromedioMesAnt, topeInferiorSaldo, topeSuperiosSaldo, valorCobro, descuentoXTx,
								descuentoSobreRango, descuentoDentroRango);
					} else {
						Reporter.reportEvent(Reporter.MIC_FAIL, "No se encontro la informaci�n relacionada");
						SettingsRun.exitTestIteration();
					}
				} else if (estadoTx.equals("Pago Rechazado")) {
					Reporter.reportEvent(Reporter.MIC_INFO,
							"La transacci�n para el tipo destino: " + tipoDestino + " ha sido rechazada.");
					// Si aplicar el descuento esta activado llegara SI
					if (aplicaCombo.equals("SI")) {
						resultado[i] = this.baseMiddleCobros.descuentoXRechazo(descuentoXRechazo, valorCobroRechazo,
								descuentoXTxRechazo);
					} else if (aplicaCombo.equals("NO")) {
						resultado[i] = valorCobroRechazo;
						Reporter.reportEvent(Reporter.MIC_INFO,
								"Se cobra tarifa completa del cobro por rechazo de transacci�n: [ " + valorCobroRechazo
										+ " ].");
					}
				}
			} else if (tipoCobro.equals("COBROS SIN COMBOS") && informacionConsultada == false
					|| tipoCobro.equals("COBROS CON COMBOS") && informacionConsultada == false) {
				if (estadoTx.equals("Pago Exitoso") || estadoTx.equals("Pendiente")
						|| estadoTx.equals("Enviado a Otro Banco")) {
					if (indicadorCumplimiento.equals("S")) {
						resultado[i] = this.baseMiddleCobros.descuentoIndicadorSSinCombo(saldoPromedioMesAnt,
								topeInferiorSaldo, topeSuperiosSaldo, descuentoXTx, valorCobro, descuentoDentroRango,
								descuentoSobreRango);
					} else if (indicadorCumplimiento.equals("N")) {
						resultado[i] = this.baseMiddleCobros.descuentoIndicadorNSinCombo(saldoPromedioMesAnt,
								topeInferiorSaldo, topeSuperiosSaldo, descuentoXTx, valorCobro, descuentoDentroRango,
								descuentoSobreRango);
					} else {
						Reporter.reportEvent(Reporter.MIC_INFO, "No se encontro la informaci�n relacionada");
						SettingsRun.exitTestIteration();
					}
				} else if (estadoTx.equals("Pago Rechazado")) {
					// Si aplicar el descuento esta activado llegara SI
					Reporter.reportEvent(Reporter.MIC_INFO,
							"La transacci�n para el tipo destino: " + tipoDestino + " ha sido rechazada.");
					resultado[i] = this.baseMiddleCobros.descuentoXRechazo(descuentoXRechazo, valorCobroRechazo,
							descuentoXTxRechazo);
				}
			}
		}
		/*
		 * Nos ayuda a validar que se realice el conteo de transacciones a servicios que
		 * se encuentran en un combo
		 */
		if (tipoCobro.equals("COBROS CON COMBOS") && verificarCombo.contains("Empresa tiene combo")
				|| tipoCobro.equals("COBROS SIN COMBOS") && verificarCombo.contains("Empresa tiene combo")) {
			if (!servicio.equals("Pago de Servicios")) {
				contadorTx = new int[5];
				contadorTx[0] = incCuentasDavivienda;
				contadorTx[1] = incCuentasOtrosBancos;
				contadorTx[2] = incTarjetaPrepago;
				contadorTx[3] = incDaviplata;
				contadorTx[4] = incDepositosElectronicos;
				for (int j = 0; j < contadorTx.length; j++) {
					totalTxAcumuladas = Integer.parseInt(infoMiddle.get("Tx acumuladas " + (j + 1)));
					int conteo = contadorTx[j];
					if (totalTxAcumuladas != 0)
						contadorTx[j] = totalTxAcumuladas + conteo;
					else
						contadorTx[j] = totalTxAcumuladas;
				}
			} else {
				contadorTx = new int[1];
				contadorTx[0] = incPagoServicios;
				totalTxAcumuladas = Integer.parseInt(infoMiddle.get("Tx acumuladas 1"));
				int conteo = contadorTx[0];
				if (totalTxAcumuladas != 0)
					contadorTx[0] = totalTxAcumuladas + conteo;
				else
					contadorTx[0] = totalTxAcumuladas;
			}
		}
		return resultado;
	}

	/**
	 * Se obtiene el resultado de cada destino con su transacci�n acumulada
	 * 
	 * @return - devuelve el contador de las tx realizadas
	 */
	public static int[] getTotalContador() {
		return contadorTx;
	}

// =======================================================================================================================

// ===========================================[aprobarTxPendiente]===========================================================================

	/**
	 * Intenta aprobar una transacci�n cuyo n�mero de transacci�n se encuentra
	 * almacenado en el par�metro "N�mero Aprobaci�n".<br>
	 * Si [desdeDetalle] es [true] este m�todo ingresa al detalle de la transacci�n
	 * para intentar realizar la aprobaci�n, en caso contrario intenta realizar la
	 * aprobaci�n desde el listado de transacciones pendientes de aprobaci�n.
	 * 
	 * @throws Exception
	 */
	public void aprobarTxPendiente(boolean desdeDetalle) throws Exception {

		String msgError = null;

		DXCUtil.wait(10);

		String pagoyTx = this.pageOrigen.getTextMid();

		String navegador = SettingsRun.getTestData().getParameter("Navegador").trim();

		if (pagoyTx.contains("Pagos, Transferencias y otros")) {
			if (navegador.contains("CHROME")) {

				msgError = pageOrigen.irAOpcion("Transacciones Pendientes de Firmas", "Pagos, Transferencias y otros",
						"Pendientes de Aprobaci�n");
			} else {

				msgError = pageOrigen.irAOpcionMoZilla("Transacciones Pendientes de Firmas",
						"Pagos, Transferencias y otros", "Pendientes de Aprobaci�n", null, null);
			}
		} else if (pagoyTx.contains("Pagos, Transferencias y Otros")) {
			if (navegador.contains("CHROME")) {

				msgError = pageOrigen.irAOpcion("Transacciones Pendientes de Firmas", "Pagos, Transferencias y Otros",
						"Pendientes de Aprobaci�n");
			} else {

				msgError = pageOrigen.irAOpcionMoZilla("Transacciones Pendientes de Firmas",
						"Pagos, Transferencias y Otros", "Pendientes de Aprobaci�n", null, null);
			}

		} else {
			if (navegador.contains("CHROME")) {

				msgError = pageOrigen.irAOpcion("Transacciones Pendientes de Firmas", "Pagos y Transferencias",
						"Pendientes de Aprobaci�n");
			} else {

				msgError = pageOrigen.irAOpcionMoZilla("Transacciones Pendientes de Firmas", "Pagos y Transferencias",
						"Pendientes de Aprobaci�n", null, null);
			}

		}
		if (msgError != null)
			this.terminarIteracionXError(msgError);

//		String numCodSeg = null; 
		int numRetosFalla = 0;
		if (DatosDavivienda.IS_RISKMOTOR)
			numRetosFalla = Integer.valueOf(SettingsRun.getTestData().getParameter("Ingresos Fallidos").trim());

		if (desdeDetalle) {
			msgError = pagePendApr.entrarDetalleTx(this.numeroTx);
			if (msgError != null)
				this.terminarIteracionXError(msgError);

//			pageConf.setNbButtonAprobar("Aprobar Transacci�n");
		} else { // DESDE CAJA
			pagePendApr.seleccionarTransaccion(this.numeroTx);

			pageConf.setNbButtonAprobar("Aprobar Transacciones Seleccionadas");
//			pagePendApr.ingresarCodSeguridad(numCodSeg);
		}
//			String msgRta = pageConf.aprobarTx(numRetosFalla, numCodSeg);
		String msgRta = pageConf.aprobarTx(numRetosFalla, desdeDetalle);

		if (DatosDavivienda.IS_RISKMOTOR) {

			this.estadoTx = msgRta;
			this.SetEstado(msgRta);
			if (DXCUtil.itemContainsAnyArrayItem(msgRta, arrMsgBuscarEstado)) {
				this.estadoTx = "Transferencia Realizada";// Buscar estado de la transaccion
				Reporter.reportEvent(Reporter.MIC_INFO, msgRta);
			} else if (DXCUtil.itemContainsAnyArrayItem(msgRta, this.arrMsgTxDeclinada)) {
				Reporter.reportEvent(Reporter.MIC_INFO, msgRta);
				this.estadoTx = "DECLINADA";
				if (DatosDavivienda.IS_RISKMOTOR) {
					this.valorTx = pageConf.getValorFinalTx();
					this.bancoDesTx = pageConf.getBancoDesTx();

					if (this.bancoDesTx != null)
						SetBancoDesTx(this.bancoDesTx);

					if (this.valorTx != null)
						SetActivityAmount(this.valorTx.replace("$", "").replace(" ", ""));

				}

				this.numeroTx = pageConf.getNumeroDocumento();
				if (this.numeroTx != null) {
//					SetNumApr(this.numeroTx);
					if (this.servicio.contains("N�mina") || this.servicio.contains("Pago a Proveedores")) {
						if (pageConf.CuentaDesMotor() != null) {
							this.cuentasDesMotor = pageConf.CuentaDesMotor();
						}
					}
				}

				if (this.servicio.contains("N�mina") || this.servicio.contains("Pago a Proveedores")
						|| this.servicio.contains("AFC") || this.servicio.contains("Cr�dito.3ros")) {
					this.validarIngresoMRDestinosMasivo();
				} else {
					this.validarIngresoMR();
				}
				this.pageOrigen.terminarIteracion();
			}

		}

		if (!msgRta.contains(PageConfirmacion.MSG_EXITO_PAGO) && !msgRta.contains(PageConfirmacion.MSG_EXITO_GUARD)
				&& !msgRta.contains(PageConfirmacion.MSG_EXITO_ENV_OTP)
				&& !msgRta.contains(PageConfirmacion.MSG_EXITO_APROB))
			this.terminarIteracionXError(msgRta);
	}

// =======================================================================================================================

	public void SetPrioridaMr(String riesgo) throws Exception {
		this.prioridaRiesgo = riesgo;

	}

	public void SetuserAgent(String user) throws Exception {
		this.userAgent = user;
	}

//	public void SetNumApr(String numAproTx) {
//		this.numAprova = numAproTx;
//	}

	public void SetActivityAmount(String activityAmountx) {

		this.activityAmount = activityAmountx;
	}

//	public void getActivityAmount2(String activityAmountx) {
//
//		this.activityAmount2 = activityAmountx;
//	}

	public void SetBancoDesTx(String bancoDes) {
		this.bancoDesMotor = bancoDes;
	}

	public void SetEstado(String estado) {
		this.estado = estado;
	}

	public String[] getCuentasMotor() {
		return cuentasDesMotor;
	}

// =======================================================================================================================

// ===========================================[validarIngresoMRDestinosMasivo]===========================================================================
//	/**
//	 * Para el caso de Motor de Riesgo Destino Masivos, dependiendo de la respuesta
//	 * dada por el intento de confirmar la transacci�n, se eval�a, seg�n el riesgo
//	 * del cliente de la iteraci�n actual, la adici�n o no del registro actual y a
//	 * diciona los diferentes destinos uno a uno o uno a varios en el archivo de
//	 * motor consu cuenta Homologada ya registrada en el archivo Tipos TX pyme.xls
//	 * hoja4.
//	 */
//	public void validarIngresoMRDestinosMasivo() throws Exception {
//
//		int intentosFall = 0; // VALOR POR DEFECTO
//
//		String[] cuentas = this.getCuentasMotor();
//
//		for (String datoDes : cuentas) {
//
//			DatosDavivienda.RISKMOTOR.adicionarRegistro();
//			DatosDavivienda.RISKMOTOR.setNumeroTx();
//			DatosDavivienda.RISKMOTOR.setResultado();
//			DatosDavivienda.RISKMOTOR.setObservacion();
//			
//			DatosDavivienda.RISKMOTOR.setCanal("WEB_PYME");
//			DatosDavivienda.RISKMOTOR.setAmbienteDePruebas(this.nombreAmbiente);
//			
//			String uuid = SettingsRun.getTestData().getParameter("Hash").trim();
//
//			DatosDavivienda.RISKMOTOR.setHash(uuid);
//
//			if (!this.tipoPrueba.equals(TP_LOGIN))
//				this.transaccion = this.getTransaccion(this.tipoPrueba, this.servicio);
//			DatosDavivienda.RISKMOTOR.setTransaccion(this.transaccion);
//
//			String userType = "Token"; // VALOR POR DEFECTO
//			if (DatosEmpresarial.TIPO_TOKEN.equals(DatosEmpresarial.TOKEN_OTP))
//				userType = "Virtual";
//
//			String intentos = SettingsRun.getTestData().getParameter("Ingresos Fallidos").trim();
//
//			if (DXCUtil.isInteger(intentos))
//				intentosFall = Integer.valueOf(intentos);
//			// -----------------------------------------------------------------------------------------------------------------------
//			// SETEA LOS DATOS FALTANTES
//			DatosDavivienda.RISKMOTOR.setUserType(userType);
//
//			DatosDavivienda.RISKMOTOR.setRisk(riesgoBc);
//			if (!this.servicio.equals("Tx Internacionales Recibir desde el exterior")
//					&& !this.servicio.equals("Tx Internacionales Enviar al exterior"))
//				DatosDavivienda.RISKMOTOR.setRiskEFM(riesgoEfm);
//
//			if (this.numeroTx != null)
//				DatosDavivienda.RISKMOTOR.setNumeroTx(this.numeroTx);
//
//			DatosDavivienda.RISKMOTOR.setNumRetosFallidos(intentosFall);
//
//			String navegador = SettingsRun.getTestData().getParameter("Navegador").trim();
//
//			DatosDavivienda.RISKMOTOR.setNavegador_SistemaOperativo(navegador);
//
//			DatosDavivienda.RISKMOTOR.setCliente(DatosEmpresarial.CLI_EMPRESAR);
//			DatosDavivienda.RISKMOTOR.setDocumento(DatosEmpresarial.TIPO_ID_LOGUEO, DatosEmpresarial.NUM_ID_LOGUEO);
//
//			String[] datoCtaSinEspacios = datoDes.split("\\|");
//
//			// Verifica que tengas al menos tres partes en tus datos antes de acceder a
//			// datoCtaSinEspacios[2]
//
//			DatosDavivienda.RISKMOTOR.setHash(uuid);
//
//			if (this.desdeElDetalle.contains("SI") && this.tipoPrueba.equals("Tx Pend Aprobaci�n"))
//				this.desdeElDetalle = "Aprobado dentro del detalle";
//
//			else if (this.desdeElDetalle.contains("NO") && this.tipoPrueba.equals("Tx Pend Aprobaci�n"))
//				this.desdeElDetalle = "Aprobado furea del detalle";
//
//			if (this.descripcionsin != null || !this.descripcionsin.isEmpty() || !this.descripcionsin.equals(" "))
//				DatosDavivienda.RISKMOTOR.setObservacion(this.descripcionsin + " - " + this.desdeElDetalle);
//			// Nivel de riesgo Motor
//			this.riesgo = this.prioridaRiesgo;
//
//			switch (this.riesgo.toUpperCase()) {
//			// -----------------------------------------------------------------------------------------------------------------------
//			case MotorRiesgo.RISK_ALTO:
//
//				if (this.estadoTx.equals("DECLINADA")) {
//
////					DatosDavivienda.RISKMOTOR.setTemporalResultado(MotorRiesgo.RESULT_SUCCESS);
//					DatosDavivienda.RISKMOTOR.setTemporalResultado("Declinada");
//					DatosDavivienda.RISKMOTOR.setTemporalObservacion(servicio);
//					DatosDavivienda.RISKMOTOR.setTemporalTime(pageConf.getFechaHoraTx());
//					DatosDavivienda.RISKMOTOR.setTime();
//					DatosDavivienda.RISKMOTOR.setCuentaOrigen(this.TipoProd(this.tipoCta));
//
//					DatosDavivienda.RISKMOTOR.set_CtaOrigenODestMIGRADA(this.numCta);
//
//					if (datoCtaSinEspacios[1].contains("otros") || datoCtaSinEspacios[1].contains("OTROS")
//							|| datoCtaSinEspacios[1].contains("OTROS BANCOS")
//							|| datoCtaSinEspacios[1].contains("otros bancos")
//							|| datoCtaSinEspacios[1].contains("CUENTAS CORRIENTES OTROS BANCOS")
//							|| datoCtaSinEspacios[1].contains("CUENTAS AHORRO OTROS BANCOS")
//							|| datoCtaSinEspacios[1].contains("electronicos")
//							|| datoCtaSinEspacios[1].contains("electr�nicos")
//							|| datoCtaSinEspacios[1].contains("ELECTRONICOS")
//							|| datoCtaSinEspacios[1].contains("ELECTR�NICOS")) {
//
//						DatosDavivienda.RISKMOTOR.setCuentaDestino("Monedero otro Banco");
//
//					} else if (datoCtaSinEspacios[1].contains("cuenta de ahorros")
//							|| datoCtaSinEspacios[1].contains("CUENTA DE AHORROS")) {
//
//						DatosDavivienda.RISKMOTOR.setCuentaDestino("Cuenta Ahorros");
//
//					} else if (datoCtaSinEspacios[1].contains("Cuenta Corriente")
//							|| datoCtaSinEspacios[1].contains("CUENTA CORRIENTE")) {
//
//						DatosDavivienda.RISKMOTOR.setCuentaDestino("Cuenta corriente");
//
//					} else if (datoCtaSinEspacios[1].contains("Daviplata")
//							|| datoCtaSinEspacios[1].contains("DAVIPLATA")) {
//
//						DatosDavivienda.RISKMOTOR.setCuentaDestino("Daviplata");
//
//					} else if (datoCtaSinEspacios[1].contains("TARJETA PREPAGO MAESTRO")
//							|| datoCtaSinEspacios[1].contains("tarjeta prepago maestro")) {
//
////						DatosDavivienda.RISKMOTOR.setCuentaDestino("Tarjeta Cr�dito");
//						DatosDavivienda.RISKMOTOR.setCuentaDestino("Tarjeta Prepago");
//
//					} else if (!datoCtaSinEspacios[1].contains("Davivienda")
//							|| !datoCtaSinEspacios[1].contains("Daviplata")) {
//
//						DatosDavivienda.RISKMOTOR.setCuentaDestino(this.TipoProd(this.referencia1));
//
//					}
//
//					if (datoCtaSinEspacios[2] != null) {
//
//						if (datoCtaSinEspacios[2].contains(",")) {
//
//							datoCtaSinEspacios[2] = datoCtaSinEspacios[2].replace(",", ".").trim();
//						}
//
//						DatosDavivienda.RISKMOTOR.setMonto(datoCtaSinEspacios[2]);
//
//					}
//
//					if (controlDest == null)
//						controlDest = new ControllerDestinosMasivos(pageDestino);
//
//					String cuentaHomologada = controlDest.BuscarCuentasMigrada(this.TipoProd(this.tipoCta),this.numCta);
//					DatosDavivienda.RISKMOTOR.set_CtaOrigenODestMIGRADA(cuentaHomologada);
//
//					// Dato de Esto de la transacion
//
//					if (this.estado.contains("rechazada") || this.estado.contains("denegado")
//							|| this.estado.contains("no")) {
//						DatosDavivienda.RISKMOTOR.setResultado("Declinada");
//
//					} else if (this.estado.contains("EXCEDE") || this.estado.contains("TOPE")
//							|| this.estado.contains("1480")) {
//						DatosDavivienda.RISKMOTOR.setResultado("Rechazado");
//
//					} else {
//						DatosDavivienda.RISKMOTOR.setResultado("Exitosa");
//					}
//
//					if (this.estado.contains("rechazada") || this.estado.contains("no pudo ser realizada")) {
//						DatosDavivienda.RISKMOTOR.setTemporalResultado("Declinada");
//
//					} else if (this.estado.contains("TX EXCEDE TOPE SALDO")) {
//						DatosDavivienda.RISKMOTOR.setTemporalResultado("Rechazado");
//
//					}
//
//					if (datoCtaSinEspacios[5] != null || !datoCtaSinEspacios[5].isEmpty()) {
//
//						if (datoCtaSinEspacios[5].equals("Pago Rechazado")
//								&& (datoCtaSinEspacios[6] == null || datoCtaSinEspacios[6].isEmpty())) {
//
//							if (!datoCtaSinEspacios[5].equals("null") && !datoCtaSinEspacios[5].equals("")&& !datoCtaSinEspacios[5].equals(" ")) {
//								Reporter.reportEvent(Reporter.MIC_FAIL,"Pago Rechazado y el mensaje fue [" + datoCtaSinEspacios[5] + "]");
//								DatosDavivienda.RISKMOTOR.setResultado("Rechazada");
//							}
//
//						} else if (datoCtaSinEspacios[5].equals("Pago Rechazado")&& (datoCtaSinEspacios[6] != null || !datoCtaSinEspacios[6].isEmpty())) {
//
//							if (!datoCtaSinEspacios[6].equals("null") && !datoCtaSinEspacios[6].equals("")
//									&& !datoCtaSinEspacios[6].equals(" ")) {
//								Reporter.reportEvent(Reporter.MIC_FAIL,"Pago Rechazado y el mensaje fue [" + datoCtaSinEspacios[6] + "]");
//								DatosDavivienda.RISKMOTOR.setResultado("Rechazada");
//							}
//
//						} else if (!datoCtaSinEspacios[5].isEmpty()&& (datoCtaSinEspacios[6] != null || !datoCtaSinEspacios[6].isEmpty())) {
//
//							if (!datoCtaSinEspacios[6].equals("null") && !datoCtaSinEspacios[6].equals("")
//									&& !datoCtaSinEspacios[6].equals(" ")) {
//								Reporter.reportEvent(Reporter.MIC_FAIL,"Pago Rechazado y el mensaje fue [" + datoCtaSinEspacios[6] + "]");
//								DatosDavivienda.RISKMOTOR.setResultado("Rechazada");
//							}
//
//						} else if (datoCtaSinEspacios[5].equals("Exitoso")) {
//							DatosDavivienda.RISKMOTOR.setResultado("Exitosa");
//							Reporter.reportEvent(Reporter.MIC_FAIL,
//									"Se esperaba declinaci�n y el mensaje fue [" + datoCtaSinEspacios[5] + "]");
//						}
//
//					}
//
//				} else {
//
//					Reporter.reportEvent(Reporter.MIC_FAIL,
//							"Se esperaba declinaci�n y el mensaje fue [" + datoCtaSinEspacios[5] + "]");
//
//					DatosDavivienda.RISKMOTOR.setTemporalResultado("Exitosa");
//					DatosDavivienda.RISKMOTOR.setTemporalObservacion(servicio);
//					DatosDavivienda.RISKMOTOR.setTemporalTime(pageConf.getFechaHoraTx());
//					DatosDavivienda.RISKMOTOR.setTime();
//
//					DatosDavivienda.RISKMOTOR.setCuentaOrigen(this.TipoProd(this.tipoCta));
//					DatosDavivienda.RISKMOTOR.set_CtaOrigenODestMIGRADA(this.numCta);
//
//					DatosDavivienda.RISKMOTOR.setMonto(datoCtaSinEspacios[2]);
//
//					if (datoCtaSinEspacios[1].contains("otros") || datoCtaSinEspacios[1].contains("OTROS")
//							|| datoCtaSinEspacios[1].contains("OTROS BANCOS")
//							|| datoCtaSinEspacios[1].contains("otros bancos")
//							|| datoCtaSinEspacios[1].contains("CUENTAS CORRIENTES OTROS BANCOS")
//							|| datoCtaSinEspacios[1].contains("CUENTAS AHORRO OTROS BANCOS")
//							|| datoCtaSinEspacios[1].contains("electronicos")
//							|| datoCtaSinEspacios[1].contains("electr�nicos")
//							|| datoCtaSinEspacios[1].contains("ELECTRONICOS")
//							|| datoCtaSinEspacios[1].contains("ELECTR�NICOS")) {
//						DatosDavivienda.RISKMOTOR.setCuentaDestino("Monedero otro Banco");
//
//					} else if (datoCtaSinEspacios[1].contains("cuenta de ahorros")
//							|| datoCtaSinEspacios[1].contains("CUENTA DE AHORROS")) {
//
//						DatosDavivienda.RISKMOTOR.setCuentaDestino("Cuenta Ahorros");
//
//					} else if (datoCtaSinEspacios[1].contains("Cuenta Corriente")
//							|| datoCtaSinEspacios[1].contains("CUENTA CORRIENTE")) {
//
//						DatosDavivienda.RISKMOTOR.setCuentaDestino("Cuenta corriente");
//
//					} else if (datoCtaSinEspacios[1].contains("Daviplata")
//							|| datoCtaSinEspacios[1].contains("DAVIPLATA")) {
//
//						DatosDavivienda.RISKMOTOR.setCuentaDestino("Daviplata");
//
//					} else if (datoCtaSinEspacios[1].contains("TARJETA PREPAGO MAESTRO")
//							|| datoCtaSinEspacios[1].contains("tarjeta prepago maestro")) {
//
////						DatosDavivienda.RISKMOTOR.setCuentaDestino("Tarjeta Cr�dito");
//						DatosDavivienda.RISKMOTOR.setCuentaDestino("Tarjeta Prepago");
//
//					} else if (!datoCtaSinEspacios[1].contains("Davivienda")
//							|| !datoCtaSinEspacios[1].contains("Daviplata")) {
//
//						DatosDavivienda.RISKMOTOR.setCuentaDestino(this.TipoProd(this.referencia1));
//					}
//
//
//					if (controlDest == null)
//						controlDest = new ControllerDestinosMasivos(pageDestino);
//
//					String cuentaHomologada = controlDest.BuscarCuentasMigrada(this.TipoProd(this.tipoCta),this.numCta);
//					DatosDavivienda.RISKMOTOR.set_CtaOrigenODestMIGRADA(cuentaHomologada);
//					
//
//					if (datoCtaSinEspacios[5] != null || !datoCtaSinEspacios[5].isEmpty()) {
//						if (datoCtaSinEspacios[5].equals("Pago Rechazado")
//								&& (datoCtaSinEspacios[6] == null || datoCtaSinEspacios[6].isEmpty())) {
//
//							if (!datoCtaSinEspacios[5].equals("null") && !datoCtaSinEspacios[5].equals("")
//									&& !datoCtaSinEspacios[5].equals(" ")) {
//								Reporter.reportEvent(Reporter.MIC_FAIL,
//										"Pago Rechazado y el mensaje fue [" + datoCtaSinEspacios[5] + "]");
//								DatosDavivienda.RISKMOTOR.setResultado("Rechazada");
//							}
//
//						} else if (datoCtaSinEspacios[5].equals("Pago Rechazado")
//								&& (datoCtaSinEspacios[6] != null || !datoCtaSinEspacios[6].isEmpty())) {
//
//							if (!datoCtaSinEspacios[6].equals("null") && !datoCtaSinEspacios[6].equals("")
//									&& !datoCtaSinEspacios[6].equals(" ")) {
//								Reporter.reportEvent(Reporter.MIC_FAIL,
//										"Pago Rechazado y el mensaje fue [" + datoCtaSinEspacios[6] + "]");
//								DatosDavivienda.RISKMOTOR.setResultado("Rechazada");
//							}
//
//						} else if (!datoCtaSinEspacios[5].isEmpty()
//								&& (datoCtaSinEspacios[6] != null || !datoCtaSinEspacios[6].isEmpty())) {
//
//							if (!datoCtaSinEspacios[6].equals("null") && !datoCtaSinEspacios[6].equals("")
//									&& !datoCtaSinEspacios[6].equals(" ")) {
//								Reporter.reportEvent(Reporter.MIC_FAIL,
//										"Pago Rechazado y el mensaje fue [" + datoCtaSinEspacios[6] + "]");
//								DatosDavivienda.RISKMOTOR.setResultado("Rechazada");
//							}
//
//						} else if (datoCtaSinEspacios[5].equals("Exitoso")) {
//							DatosDavivienda.RISKMOTOR.setResultado("Exitosa");
//							Reporter.reportEvent(Reporter.MIC_FAIL,
//									"Se esperaba declinaci�n y el mensaje fue [" + datoCtaSinEspacios[5] + "]");
//						}
//
//						DatosDavivienda.RISKMOTOR.setResultado("Exitosa");
//
//					}
//				}
//
//				break;
//			// -----------------------------------------------------------------------------------------------------------------------
//			case MotorRiesgo.RISK_MEDIO:
//			case MotorRiesgo.RISK_BAJO:
//
//				if (DXCUtil.itemInArray(this.estadoTx, arrEstadosTxAprob)) {
//
//					DatosDavivienda.RISKMOTOR.setTemporalResultado(MotorRiesgo.RESULT_SUCCESS);
//					DatosDavivienda.RISKMOTOR.setTemporalObservacion(servicio);
//					DatosDavivienda.RISKMOTOR.setTemporalTime(pageConf.getFechaHoraTx());
//					DatosDavivienda.RISKMOTOR.setTime();
//
////					DatosDavivienda.RISKMOTOR.setNumeroTx(this.numAprova);
//					DatosDavivienda.RISKMOTOR.setMonto(datoCtaSinEspacios[2]);
//
//
//					DatosDavivienda.RISKMOTOR.setCuentaOrigen(this.TipoProd(this.tipoCta));
//					DatosDavivienda.RISKMOTOR.set_CtaOrigenODestMIGRADA(this.numCta);
//					
//					DatosDavivienda.RISKMOTOR.setCuentaDestino(this.TipoProd(this.tipoCta));
//
//					if (datoCtaSinEspacios[1].contains("otros") || datoCtaSinEspacios[1].contains("OTROS")
//							|| datoCtaSinEspacios[1].contains("OTROS BANCOS")
//							|| datoCtaSinEspacios[1].contains("otros bancos")
//							|| datoCtaSinEspacios[1].contains("CUENTAS CORRIENTES OTROS BANCOS")
//							|| datoCtaSinEspacios[1].contains("CUENTAS AHORRO OTROS BANCOS")
//							|| datoCtaSinEspacios[1].contains("electronicos")
//							|| datoCtaSinEspacios[1].contains("electr�nicos")
//							|| datoCtaSinEspacios[1].contains("ELECTRONICOS")
//							|| datoCtaSinEspacios[1].contains("ELECTR�NICOS")) {
//
//						DatosDavivienda.RISKMOTOR.setCuentaDestino("Monedero otro Banco");
//
//					} else if (datoCtaSinEspacios[1].contains("cuenta de ahorros")
//							|| datoCtaSinEspacios[1].contains("CUENTA DE AHORROS")) {
//
//						DatosDavivienda.RISKMOTOR.setCuentaDestino("Cuenta Ahorros");
//
//					} else if (datoCtaSinEspacios[1].contains("Cuenta Corriente")
//							|| datoCtaSinEspacios[1].contains("CUENTA CORRIENTE")) {
//
//						DatosDavivienda.RISKMOTOR.setCuentaDestino("Cuenta corriente");
//
//					} else if (datoCtaSinEspacios[1].contains("Daviplata")
//							|| datoCtaSinEspacios[1].contains("DAVIPLATA")) {
//
//						DatosDavivienda.RISKMOTOR.setCuentaDestino("Daviplata");
//
//					} else if (datoCtaSinEspacios[1].contains("TARJETA PREPAGO MAESTRO")
//							|| datoCtaSinEspacios[1].contains("tarjeta prepago maestro")) {
//
////						DatosDavivienda.RISKMOTOR.setCuentaDestino("Tarjeta Cr�dito");
//						DatosDavivienda.RISKMOTOR.setCuentaDestino("Tarjeta Prepago");
//
//					} else if (!datoCtaSinEspacios[1].contains("Davivienda")
//							|| !datoCtaSinEspacios[1].contains("Daviplata")) {
//
//						DatosDavivienda.RISKMOTOR.setCuentaDestino(this.TipoProd(this.referencia1));
//
//					}
//
//
//
//					if (controlDest == null)
//						controlDest = new ControllerDestinosMasivos(pageDestino);
//
//					String cuentaHomologada = controlDest.BuscarCuentasMigrada(this.TipoProd(this.tipoCta),this.numCta);
//					DatosDavivienda.RISKMOTOR.set_CtaOrigenODestMIGRADA(cuentaHomologada);
//					
//					
//
//
//					if (this.estado.contains("rechazada") || this.estado.contains("denegado")
//							|| this.estado.contains("no")) {
//
//						DatosDavivienda.RISKMOTOR.setResultado("Declinada");
//
//					} else if (this.estado.contains("EXCEDE") || this.estado.contains("TOPE")
//							|| this.estado.contains("1480")) {
//
//						DatosDavivienda.RISKMOTOR.setResultado("Rechazado");
//
//					} else {
//
//						DatosDavivienda.RISKMOTOR.setResultado("Exitosa");
//					}
//
//					if (this.estado.contains("rechazada") || this.estado.contains("no pudo ser realizada")) {
//
//						DatosDavivienda.RISKMOTOR.setTemporalResultado("Declinada");
//
//					} else if (this.estado.contains("TX EXCEDE TOPE SALDO")) {
//
//						DatosDavivienda.RISKMOTOR.setTemporalResultado("Rechazado");
//					}
//
//					if (datoCtaSinEspacios[5] != null || !datoCtaSinEspacios[5].isEmpty()) {
//
//						if (datoCtaSinEspacios[5].equals("Pago Rechazado")
//								&& (datoCtaSinEspacios[6] == null || datoCtaSinEspacios[6].isEmpty())) {
//
//							if (!datoCtaSinEspacios[5].equals("null") && !datoCtaSinEspacios[5].equals("")
//									&& !datoCtaSinEspacios[5].equals(" ")) {
//								Reporter.reportEvent(Reporter.MIC_FAIL,
//										"Pago Rechazado y el mensaje fue [" + datoCtaSinEspacios[5] + "]");
//								DatosDavivienda.RISKMOTOR.setResultado("Rechazada");
//							}
//
//						} else if (datoCtaSinEspacios[5].equals("Pago Rechazado")
//								&& (datoCtaSinEspacios[6] != null || !datoCtaSinEspacios[6].isEmpty())) {
//
//							if (!datoCtaSinEspacios[6].equals("null") && !datoCtaSinEspacios[6].equals("")
//									&& !datoCtaSinEspacios[6].equals(" ")) {
//								Reporter.reportEvent(Reporter.MIC_FAIL,
//										"Pago Rechazado y el mensaje fue [" + datoCtaSinEspacios[6] + "]");
//								DatosDavivienda.RISKMOTOR.setResultado("Rechazada");
//							}
//
//						} else if (!datoCtaSinEspacios[5].isEmpty()
//								&& (datoCtaSinEspacios[6] != null || !datoCtaSinEspacios[6].isEmpty())) {
//
//							if (!datoCtaSinEspacios[6].equals("null") && !datoCtaSinEspacios[6].equals("")
//									&& !datoCtaSinEspacios[6].equals(" ")) {
//								Reporter.reportEvent(Reporter.MIC_FAIL,
//										"Pago Rechazado y el mensaje fue [" + datoCtaSinEspacios[6] + "]");
//								DatosDavivienda.RISKMOTOR.setResultado("Rechazada");
//							}
//						}
//
//					}
//
//				} else {
//
//					Reporter.reportEvent(Reporter.MIC_FAIL,"Se esperaba transaccion exitosa y el estado fue [" + this.estadoTx + "]");
//
//					DatosDavivienda.RISKMOTOR.setTemporalObservacion(servicio);
//					DatosDavivienda.RISKMOTOR.setTemporalTime(pageConf.getFechaHoraTx());
//					DatosDavivienda.RISKMOTOR.setTime();
//					DatosDavivienda.RISKMOTOR.setResultado("Declinada");
//					DatosDavivienda.RISKMOTOR.set_CtaOrigenODestMIGRADA(this.numCta);
////					DatosDavivienda.RISKMOTOR.setNumeroTx(this.numAprova);
//					DatosDavivienda.RISKMOTOR.setMonto(datoCtaSinEspacios[2]);
//
//					DatosDavivienda.RISKMOTOR.setCuentaOrigen(this.TipoProd(this.tipoCta));
//
//					if (datoCtaSinEspacios[1].contains("otros") || datoCtaSinEspacios[1].contains("OTROS")
//							|| datoCtaSinEspacios[1].contains("OTROS BANCOS")
//							|| datoCtaSinEspacios[1].contains("otros bancos")
//							|| datoCtaSinEspacios[1].contains("CUENTAS CORRIENTES OTROS BANCOS")
//							|| datoCtaSinEspacios[1].contains("CUENTAS AHORRO OTROS BANCOS")
//							|| datoCtaSinEspacios[1].contains("electronicos")
//							|| datoCtaSinEspacios[1].contains("electr�nicos")
//							|| datoCtaSinEspacios[1].contains("ELECTRONICOS")
//							|| datoCtaSinEspacios[1].contains("ELECTR�NICOS")) {
//
//						DatosDavivienda.RISKMOTOR.setCuentaDestino("Monedero otro Banco");
//
//					} else if (datoCtaSinEspacios[1].contains("cuenta de ahorros")
//							|| datoCtaSinEspacios[1].contains("CUENTA DE AHORROS")) {
//
//						DatosDavivienda.RISKMOTOR.setCuentaDestino("Cuenta Ahorros");
//
//					} else if (datoCtaSinEspacios[1].contains("Cuenta Corriente")
//							|| datoCtaSinEspacios[1].contains("CUENTA CORRIENTE")) {
//
//						DatosDavivienda.RISKMOTOR.setCuentaDestino("Cuenta corriente");
//
//					} else if (datoCtaSinEspacios[1].contains("Daviplata")
//							|| datoCtaSinEspacios[1].contains("DAVIPLATA")) {
//
//						DatosDavivienda.RISKMOTOR.setCuentaDestino("Daviplata");
//
//					} else if (datoCtaSinEspacios[1].contains("TARJETA PREPAGO MAESTRO")
//							|| datoCtaSinEspacios[1].contains("tarjeta prepago maestro")) {
//
//						DatosDavivienda.RISKMOTOR.setCuentaDestino("Tarjeta Cr�dito");
//
//					} else if (!datoCtaSinEspacios[1].contains("Davivienda")
//							|| !datoCtaSinEspacios[1].contains("Daviplata")) {
//
//						DatosDavivienda.RISKMOTOR.setCuentaDestino(this.TipoProd(this.referencia1));
//					}
//
//					
//					
//					if (controlDest == null)
//						controlDest = new ControllerDestinosMasivos(pageDestino);
//
//					String cuentaHomologada = controlDest.BuscarCuentasMigrada(this.TipoProd(this.tipoCta),this.numCta);
//					DatosDavivienda.RISKMOTOR.set_CtaOrigenODestMIGRADA(cuentaHomologada);
//					
//
//					if (datoCtaSinEspacios[5] != null || !datoCtaSinEspacios[5].isEmpty()) {
//
//						if (datoCtaSinEspacios[5].equals("Pago Rechazado")
//								&& (datoCtaSinEspacios[6] == null || datoCtaSinEspacios[6].isEmpty())) {
//
//							if (!datoCtaSinEspacios[5].equals("null") && !datoCtaSinEspacios[5].equals("")
//									&& !datoCtaSinEspacios[5].equals(" ")) {
//
//								Reporter.reportEvent(Reporter.MIC_FAIL,
//										"Pago Rechazado y el mensaje fue [" + datoCtaSinEspacios[5] + "]");
//								DatosDavivienda.RISKMOTOR.setResultado("Rechazada");
//							}
//
//						} else if (datoCtaSinEspacios[5].equals("Pago Rechazado")
//								&& (datoCtaSinEspacios[6] != null || !datoCtaSinEspacios[6].isEmpty())) {
//
//							if (!datoCtaSinEspacios[6].equals("null") && !datoCtaSinEspacios[6].equals("")
//									&& !datoCtaSinEspacios[6].equals(" ")) {
//								Reporter.reportEvent(Reporter.MIC_FAIL,
//										"Pago Rechazado y el mensaje fue [" + datoCtaSinEspacios[6] + "]");
//								DatosDavivienda.RISKMOTOR.setResultado("Rechazada");
//							}
//
//						} else if (!datoCtaSinEspacios[5].isEmpty()
//								&& (datoCtaSinEspacios[6] != null || !datoCtaSinEspacios[6].isEmpty())) {
//
//							if (!datoCtaSinEspacios[6].equals("null") && !datoCtaSinEspacios[6].equals("")
//									&& !datoCtaSinEspacios[6].equals(" ")) {
//								Reporter.reportEvent(Reporter.MIC_FAIL,"Pago Rechazado y el mensaje fue [" + datoCtaSinEspacios[6] + "]");
//								DatosDavivienda.RISKMOTOR.setResultado("Rechazada");
//							}
//						}
//
//					}
//
//				}
//				break;
//			// -----------------------------------------------------------------------------------------------------------------------
//			default:
//				Reporter.reportEvent(Reporter.MIC_FAIL, "Riesgo [" + riesgo + "] NO contemplado");
//				break;
//			}
//		}
//	}

	/**
	 * Para el caso de Motor de Riesgo Destino Masivos, dependiendo de la respuesta
	 * dada por el intento de confirmar la transacci�n, se eval�a, seg�n el riesgo
	 * del cliente de la iteraci�n actual, la adici�n o no del registro actual y a
	 * diciona los diferentes destinos uno a uno o uno a varios en el archivo de
	 * motor consu cuenta Homologada ya registrada en el archivo Tipos TX pyme.xls
	 * hoja4.
	 */

	public void validarIngresoMRDestinosMasivo() throws Exception {

		int intentosFall = 0; // VALOR POR DEFECTO
		String[] cuentas = this.getCuentasMotor();

		for (String datoDes : cuentas) {

			DatosDavivienda.RISKMOTOR.adicionarRegistro();
			DatosDavivienda.RISKMOTOR.setNumeroTx();
			DatosDavivienda.RISKMOTOR.setResultado();
			DatosDavivienda.RISKMOTOR.setObservacion();

			String uuid = SettingsRun.getTestData().getParameter("Hash").trim();
			DatosDavivienda.RISKMOTOR.setHash(uuid);

			if (!this.tipoPrueba.equals(TP_LOGIN))
				this.transaccion = this.getTransaccion(this.tipoPrueba, this.servicio);
			
			DatosDavivienda.RISKMOTOR.setTransaccion(this.transaccion);

			String userType = "Token"; // VALOR POR DEFECTO
			if (DatosEmpresarial.TIPO_TOKEN.equals(DatosEmpresarial.TOKEN_OTP))
				userType = "Virtual";

			String intentos = SettingsRun.getTestData().getParameter("Ingresos Fallidos").trim();

			if (DXCUtil.isInteger(intentos))
				intentosFall = Integer.valueOf(intentos);
			// -----------------------------------------------------------------------------------------------------------------------
			// SETEA LOS DATOS FALTANTES
			DatosDavivienda.RISKMOTOR.setUserType(userType);

			DatosDavivienda.RISKMOTOR.setRisk(riesgoBc);
			if (!this.servicio.equals("Tx Internacionales Recibir desde el exterior")
					&& !this.servicio.equals("Tx Internacionales Enviar al exterior"))
				DatosDavivienda.RISKMOTOR.setRiskEFM(riesgoEfm);

//			if (transaccion.contains("Transferencias mismo nit")) {
//				DatosDavivienda.RISKMOTOR.setIndicadorMismoNit("SI");
//			} else {
//				DatosDavivienda.RISKMOTOR.setIndicadorMismoNit("NO");
//			}

			if (this.numeroTx != null)
				DatosDavivienda.RISKMOTOR.setNumeroTx(this.numeroTx);

			DatosDavivienda.RISKMOTOR.setNumRetosFallidos(intentosFall);
//			DatosDavivienda.RISKMOTOR.setDireccionIp(this.ipPublica);

			String navegador = SettingsRun.getTestData().getParameter("Navegador").trim();

			DatosDavivienda.RISKMOTOR.setNavegador_SistemaOperativo(navegador);

			DatosDavivienda.RISKMOTOR.setCliente(DatosEmpresarial.CLI_EMPRESAR);
			DatosDavivienda.RISKMOTOR.setDocumento(DatosEmpresarial.TIPO_ID_LOGUEO, DatosEmpresarial.NUM_ID_LOGUEO);

			String[] datoCtaSinEspacios = datoDes.split("\\|");

			// Verifica que tengas al menos tres partes en tus datos antes de acceder a
			// datoCtaSinEspacios[2]

			DatosDavivienda.RISKMOTOR.setHash(uuid);

			if (this.desdeElDetalle.contains("SI") && this.tipoPrueba.equals("Tx Pend Aprobaci�n"))
				this.desdeElDetalle = "Aprobado dentro del detalle";

			else if (this.desdeElDetalle.contains("NO") && this.tipoPrueba.equals("Tx Pend Aprobaci�n"))
				this.desdeElDetalle = "Aprobado furea del detalle";

			if (this.descripcionsin != null || !this.descripcionsin.isEmpty() || !this.descripcionsin.equals(" "))
				DatosDavivienda.RISKMOTOR.setObservacion(this.descripcionsin + " - " + this.desdeElDetalle);
			// Nivel de riesgo Motor
			this.riesgo = this.prioridaRiesgo;

			switch (this.riesgo.toUpperCase()) {
			// -----------------------------------------------------------------------------------------------------------------------
			case MotorRiesgo.RISK_ALTO:

				if (this.estadoTx.equals("DECLINADA")) {

//					DatosDavivienda.RISKMOTOR.setTemporalResultado(MotorRiesgo.RESULT_SUCCESS);
					DatosDavivienda.RISKMOTOR.setTemporalResultado("Declinada");
					DatosDavivienda.RISKMOTOR.setTemporalObservacion(servicio);
					DatosDavivienda.RISKMOTOR.setTemporalTime(pageConf.getFechaHoraTx());
					DatosDavivienda.RISKMOTOR.setTime();
					DatosDavivienda.RISKMOTOR.setCuentaOrigen(this.TipoProd(this.tipoCta));
					DatosDavivienda.RISKMOTOR.setNumCuentaOrigen(this.numCta);
//					DatosDavivienda.RISKMOTOR.setTipoIdEmpresa(this.tipoIdEm);
					DatosDavivienda.RISKMOTOR.setNumIdEmpresa(this.nitEmpre);
					DatosDavivienda.RISKMOTOR.setReferencia_1("N/A");

					if (datoCtaSinEspacios[1].contains("otros") || datoCtaSinEspacios[1].contains("OTROS")
							|| datoCtaSinEspacios[1].contains("OTROS BANCOS")
							|| datoCtaSinEspacios[1].contains("otros bancos")
							|| datoCtaSinEspacios[1].contains("CUENTAS CORRIENTES OTROS BANCOS")
							|| datoCtaSinEspacios[1].contains("CUENTAS AHORRO OTROS BANCOS")
							|| datoCtaSinEspacios[1].contains("electronicos")
							|| datoCtaSinEspacios[1].contains("electr�nicos")
							|| datoCtaSinEspacios[1].contains("ELECTRONICOS")
							|| datoCtaSinEspacios[1].contains("ELECTR�NICOS")) {

						DatosDavivienda.RISKMOTOR.setCuentaDestino("Monedero otro Banco");

						if (datoCtaSinEspacios[1].contains("electronicos")|| datoCtaSinEspacios[1].contains("electr�nicos")|| datoCtaSinEspacios[1].contains("ELECTRONICOS")|| datoCtaSinEspacios[1].contains("ELECTR�NICOS")) {
							DatosDavivienda.RISKMOTOR.setTransaccion("Pago a monederos");
						}

					} else if (datoCtaSinEspacios[1].contains("cuenta de ahorros")
							|| datoCtaSinEspacios[1].contains("CUENTA DE AHORROS")) {

						DatosDavivienda.RISKMOTOR.setCuentaDestino("Cuenta Ahorros");

					} else if (datoCtaSinEspacios[1].contains("Cuenta Corriente")
							|| datoCtaSinEspacios[1].contains("CUENTA CORRIENTE")) {

						DatosDavivienda.RISKMOTOR.setCuentaDestino("Cuenta corriente");

					} else if (datoCtaSinEspacios[1].contains("Daviplata")
							|| datoCtaSinEspacios[1].contains("DAVIPLATA")) {

						DatosDavivienda.RISKMOTOR.setCuentaDestino("Daviplata");

					} else if (datoCtaSinEspacios[1].contains("TARJETA PREPAGO MAESTRO")
							|| datoCtaSinEspacios[1].contains("tarjeta prepago maestro")) {

//						DatosDavivienda.RISKMOTOR.setCuentaDestino("Tarjeta Cr�dito");
						DatosDavivienda.RISKMOTOR.setCuentaDestino("Tarjeta Prepago");

					} else if (!datoCtaSinEspacios[1].contains("Davivienda")
							|| !datoCtaSinEspacios[1].contains("Daviplata")) {

						DatosDavivienda.RISKMOTOR.setCuentaDestino(this.TipoProd(this.referencia1));

					}

//				DatosDavivienda.RISKMOTOR.setTipCuentaDestino(tipoCtaDest);
//					DatosDavivienda.RISKMOTOR.setNumeroTx(this.numAprova);

					if (datoCtaSinEspacios[2] != null) {

						if (datoCtaSinEspacios[2].contains(",")) {

							datoCtaSinEspacios[2] = datoCtaSinEspacios[2].replace(",", ".").trim();
						}

						DatosDavivienda.RISKMOTOR.setMonto(datoCtaSinEspacios[2]);

					}

					if (this.servicio.contains("N�mina") || this.servicio.contains("Pago a Proveedores")) {

						String[] datoCtaSinEspacios2 = null;

						for (String desMigrada : datoNumDestCuentas) {
							datoCtaSinEspacios2 = desMigrada.split("\\|");

							if (datoCtaSinEspacios2[0].contains("DEP�SITOS ELECTRONICOS")
									|| datoCtaSinEspacios2[0].contains("DEP�SITOS ELECTR�NICOS"))
								datoCtaSinEspacios2[0] = "DEP�SITOS ELECTRONICOS";

							if (datoCtaSinEspacios2[0].equals(datoCtaSinEspacios[1])
									&& datoCtaSinEspacios2[1].contains(DXCUtil.right(datoCtaSinEspacios[4], 4))) {

								DatosDavivienda.RISKMOTOR.setNumCuentaDestino(datoCtaSinEspacios2[1]);

								if (controlDest == null)
									controlDest = new ControllerDestinosMasivos(pageDestino);

								if (datoCtaSinEspacios2[0].equals(datoCtaSinEspacios[1])
										&& datoCtaSinEspacios2[1].contains(DXCUtil.right(datoCtaSinEspacios[4], 4))) {
									String cuentaHomologada = controlDest.BuscarCuentasMigrada(datoCtaSinEspacios2[0],
											datoCtaSinEspacios2[1]);
									DatosDavivienda.RISKMOTOR.setNumCuentaOrigenMigrada(cuentaHomologada);
								}
							}
						}

					}

					// Dato de Esto de la transacion

					if (this.estado.contains("rechazada") || this.estado.contains("denegado")
							|| this.estado.contains("no")) {
						DatosDavivienda.RISKMOTOR.setResultado("Declinada");
						DatosDavivienda.RISKMOTOR.setCausaRechazo(" ");

					} else if (this.estado.contains("EXCEDE") || this.estado.contains("TOPE")
							|| this.estado.contains("1480")) {
						DatosDavivienda.RISKMOTOR.setResultado("Rechazado");
						DatosDavivienda.RISKMOTOR.setCausaRechazo(this.estado);

					} else {
						DatosDavivienda.RISKMOTOR.setResultado("Exitosa");
						DatosDavivienda.RISKMOTOR.setCausaRechazo(this.estado);
					}

					if (this.estado.contains("rechazada") || this.estado.contains("no pudo ser realizada")) {
						DatosDavivienda.RISKMOTOR.setTemporalResultado("Declinada");

					} else if (this.estado.contains("TX EXCEDE TOPE SALDO")) {
						DatosDavivienda.RISKMOTOR.setTemporalResultado("Rechazado");

					}

					if (datoCtaSinEspacios[5] != null || !datoCtaSinEspacios[5].isEmpty()) {

						if (datoCtaSinEspacios[5].equals("Pago Rechazado")
								&& (datoCtaSinEspacios[6] == null || datoCtaSinEspacios[6].isEmpty())) {

							if (!datoCtaSinEspacios[5].equals("null") && !datoCtaSinEspacios[5].equals("")
									&& !datoCtaSinEspacios[5].equals(" ")) {
								Reporter.reportEvent(Reporter.MIC_FAIL,
										"Pago Rechazado y el mensaje fue [" + datoCtaSinEspacios[5] + "]");
								DatosDavivienda.RISKMOTOR.setCausaRechazo(datoCtaSinEspacios[5]);
								DatosDavivienda.RISKMOTOR.setResultado("Rechazada");
							}

						} else if (datoCtaSinEspacios[5].equals("Pago Rechazado")
								&& (datoCtaSinEspacios[6] != null || !datoCtaSinEspacios[6].isEmpty())) {

							if (!datoCtaSinEspacios[6].equals("null") && !datoCtaSinEspacios[6].equals("")
									&& !datoCtaSinEspacios[6].equals(" ")) {
								Reporter.reportEvent(Reporter.MIC_FAIL,
										"Pago Rechazado y el mensaje fue [" + datoCtaSinEspacios[6] + "]");
								DatosDavivienda.RISKMOTOR.setCausaRechazo(datoCtaSinEspacios[6]);
								DatosDavivienda.RISKMOTOR.setResultado("Rechazada");
							}

						} else if (!datoCtaSinEspacios[5].isEmpty()
								&& (datoCtaSinEspacios[6] != null || !datoCtaSinEspacios[6].isEmpty())) {

							if (!datoCtaSinEspacios[6].equals("null") && !datoCtaSinEspacios[6].equals("")
									&& !datoCtaSinEspacios[6].equals(" ")) {
								Reporter.reportEvent(Reporter.MIC_FAIL,
										"Pago Rechazado y el mensaje fue [" + datoCtaSinEspacios[6] + "]");
								DatosDavivienda.RISKMOTOR.setCausaRechazo(datoCtaSinEspacios[6]);
								DatosDavivienda.RISKMOTOR.setResultado("Rechazada");
							}

						} else if (datoCtaSinEspacios[5].equals("Exitoso")) {
							DatosDavivienda.RISKMOTOR.setResultado("Exitosa");
							Reporter.reportEvent(Reporter.MIC_FAIL,
									"Se esperaba declinaci�n y el mensaje fue [" + datoCtaSinEspacios[5] + "]");
						}

					}

				} else {

					Reporter.reportEvent(Reporter.MIC_FAIL,
							"Se esperaba declinaci�n y el mensaje fue [" + datoCtaSinEspacios[5] + "]");

					DatosDavivienda.RISKMOTOR.setTemporalResultado("Exitosa");
					DatosDavivienda.RISKMOTOR.setTemporalObservacion(servicio);
					DatosDavivienda.RISKMOTOR.setTemporalTime(pageConf.getFechaHoraTx());
					DatosDavivienda.RISKMOTOR.setTime();

					DatosDavivienda.RISKMOTOR.setCausaRechazo(this.estado);
					DatosDavivienda.RISKMOTOR.setCuentaOrigen(this.TipoProd(this.tipoCta));
					DatosDavivienda.RISKMOTOR.setNumCuentaOrigen(this.numCta);

					DatosDavivienda.RISKMOTOR.setMonto(datoCtaSinEspacios[2]);
//					DatosDavivienda.RISKMOTOR.setTipoIdEmpresa(this.tipoIdEm);
					DatosDavivienda.RISKMOTOR.setNumIdEmpresa(this.nitEmpre);

					if (datoCtaSinEspacios[1].contains("otros") || datoCtaSinEspacios[1].contains("OTROS")
							|| datoCtaSinEspacios[1].contains("OTROS BANCOS")
							|| datoCtaSinEspacios[1].contains("otros bancos")
							|| datoCtaSinEspacios[1].contains("CUENTAS CORRIENTES OTROS BANCOS")
							|| datoCtaSinEspacios[1].contains("CUENTAS AHORRO OTROS BANCOS")
							|| datoCtaSinEspacios[1].contains("electronicos")
							|| datoCtaSinEspacios[1].contains("electr�nicos")
							|| datoCtaSinEspacios[1].contains("ELECTRONICOS")
							|| datoCtaSinEspacios[1].contains("ELECTR�NICOS")) {
						DatosDavivienda.RISKMOTOR.setCuentaDestino("Monedero otro Banco");
						
						if (datoCtaSinEspacios[1].contains("electronicos")|| datoCtaSinEspacios[1].contains("electr�nicos")|| datoCtaSinEspacios[1].contains("ELECTRONICOS")|| datoCtaSinEspacios[1].contains("ELECTR�NICOS")) {
							DatosDavivienda.RISKMOTOR.setTransaccion("Pago a monederos");
						}
						
					} else if (datoCtaSinEspacios[1].contains("cuenta de ahorros")
							|| datoCtaSinEspacios[1].contains("CUENTA DE AHORROS")) {

						DatosDavivienda.RISKMOTOR.setCuentaDestino("Cuenta Ahorros");

					} else if (datoCtaSinEspacios[1].contains("Cuenta Corriente")
							|| datoCtaSinEspacios[1].contains("CUENTA CORRIENTE")) {

						DatosDavivienda.RISKMOTOR.setCuentaDestino("Cuenta corriente");

					} else if (datoCtaSinEspacios[1].contains("Daviplata")
							|| datoCtaSinEspacios[1].contains("DAVIPLATA")) {

						DatosDavivienda.RISKMOTOR.setCuentaDestino("Daviplata");

					} else if (datoCtaSinEspacios[1].contains("TARJETA PREPAGO MAESTRO")
							|| datoCtaSinEspacios[1].contains("tarjeta prepago maestro")) {

//						DatosDavivienda.RISKMOTOR.setCuentaDestino("Tarjeta Cr�dito");
						DatosDavivienda.RISKMOTOR.setCuentaDestino("Tarjeta Prepago");

					} else if (!datoCtaSinEspacios[1].contains("Davivienda")
							|| !datoCtaSinEspacios[1].contains("Daviplata")) {

						DatosDavivienda.RISKMOTOR.setCuentaDestino(this.TipoProd(this.referencia1));
					}

					DatosDavivienda.RISKMOTOR.setReferencia_1("N/A");

					if (this.servicio.contains("N�mina") || this.servicio.contains("Pago a Proveedores")) {
						String[] datoCtaSinEspacios2 = null;

						for (String desMigrada : datoNumDestCuentas) {
							datoCtaSinEspacios2 = desMigrada.split("\\|");

							if (datoCtaSinEspacios2[0].contains("DEP�SITOS ELECTRONICOS")
									|| datoCtaSinEspacios2[0].contains("DEP�SITOS ELECTR�NICOS"))
								datoCtaSinEspacios2[0] = "DEP�SITOS ELECTRONICOS";

							if (datoCtaSinEspacios2[0].equals(datoCtaSinEspacios[1])
									&& datoCtaSinEspacios2[1].contains(DXCUtil.right(datoCtaSinEspacios[4], 4))) {
								DatosDavivienda.RISKMOTOR.setNumCuentaDestino(datoCtaSinEspacios2[1]);

								if (controlDest == null)
									controlDest = new ControllerDestinosMasivos(pageDestino);

								if (datoCtaSinEspacios2[0].equals(datoCtaSinEspacios[1])
										&& datoCtaSinEspacios2[1].contains(DXCUtil.right(datoCtaSinEspacios[4], 4))) {
									String cuentaHomologada = controlDest.BuscarCuentasMigrada(datoCtaSinEspacios2[0],
											datoCtaSinEspacios2[1]);
									DatosDavivienda.RISKMOTOR.setNumCuentaOrigenMigrada(cuentaHomologada);
								}
							}
						}

					}

					if (datoCtaSinEspacios[5] != null || !datoCtaSinEspacios[5].isEmpty()) {
						if (datoCtaSinEspacios[5].equals("Pago Rechazado")
								&& (datoCtaSinEspacios[6] == null || datoCtaSinEspacios[6].isEmpty())) {

							if (!datoCtaSinEspacios[5].equals("null") && !datoCtaSinEspacios[5].equals("")
									&& !datoCtaSinEspacios[5].equals(" ")) {
								Reporter.reportEvent(Reporter.MIC_FAIL,
										"Pago Rechazado y el mensaje fue [" + datoCtaSinEspacios[5] + "]");
								DatosDavivienda.RISKMOTOR.setCausaRechazo(datoCtaSinEspacios[5]);
								DatosDavivienda.RISKMOTOR.setResultado("Rechazada");
							}

						} else if (datoCtaSinEspacios[5].equals("Pago Rechazado")
								&& (datoCtaSinEspacios[6] != null || !datoCtaSinEspacios[6].isEmpty())) {

							if (!datoCtaSinEspacios[6].equals("null") && !datoCtaSinEspacios[6].equals("")
									&& !datoCtaSinEspacios[6].equals(" ")) {
								Reporter.reportEvent(Reporter.MIC_FAIL,
										"Pago Rechazado y el mensaje fue [" + datoCtaSinEspacios[6] + "]");
								DatosDavivienda.RISKMOTOR.setCausaRechazo(datoCtaSinEspacios[6]);
								DatosDavivienda.RISKMOTOR.setResultado("Rechazada");
							}

						} else if (!datoCtaSinEspacios[5].isEmpty()
								&& (datoCtaSinEspacios[6] != null || !datoCtaSinEspacios[6].isEmpty())) {

							if (!datoCtaSinEspacios[6].equals("null") && !datoCtaSinEspacios[6].equals("")
									&& !datoCtaSinEspacios[6].equals(" ")) {
								Reporter.reportEvent(Reporter.MIC_FAIL,
										"Pago Rechazado y el mensaje fue [" + datoCtaSinEspacios[6] + "]");
								DatosDavivienda.RISKMOTOR.setCausaRechazo(datoCtaSinEspacios[6]);
								DatosDavivienda.RISKMOTOR.setResultado("Rechazada");
							}

						} else if (datoCtaSinEspacios[5].equals("Exitoso")) {
							DatosDavivienda.RISKMOTOR.setResultado("Exitosa");
							Reporter.reportEvent(Reporter.MIC_FAIL,
									"Se esperaba declinaci�n y el mensaje fue [" + datoCtaSinEspacios[5] + "]");
						}

						DatosDavivienda.RISKMOTOR.setResultado("Exitosa");

					}
				}

				break;
			// -----------------------------------------------------------------------------------------------------------------------
			case MotorRiesgo.RISK_MEDIO:
			case MotorRiesgo.RISK_BAJO:

				if (DXCUtil.itemInArray(this.estadoTx, arrEstadosTxAprob)) {

					DatosDavivienda.RISKMOTOR.setTemporalResultado(MotorRiesgo.RESULT_SUCCESS);
					DatosDavivienda.RISKMOTOR.setTemporalObservacion(servicio);
					DatosDavivienda.RISKMOTOR.setTemporalTime(pageConf.getFechaHoraTx());
					DatosDavivienda.RISKMOTOR.setTime();

//					DatosDavivienda.RISKMOTOR.setNumeroTx(this.numAprova);
					DatosDavivienda.RISKMOTOR.setMonto(datoCtaSinEspacios[2]);

//					DatosDavivienda.RISKMOTOR.setTipoIdEmpresa(this.tipoIdEm);
					DatosDavivienda.RISKMOTOR.setNumIdEmpresa(this.nitEmpre);
					DatosDavivienda.RISKMOTOR.setReferencia_1("N/A");

					DatosDavivienda.RISKMOTOR.setCuentaOrigen(this.TipoProd(this.tipoCta));
					DatosDavivienda.RISKMOTOR.setNumCuentaOrigen(this.numCta);
					DatosDavivienda.RISKMOTOR.setCuentaDestino(this.TipoProd(this.tipoCta));

					if (datoCtaSinEspacios[1].contains("otros") || datoCtaSinEspacios[1].contains("OTROS")
							|| datoCtaSinEspacios[1].contains("OTROS BANCOS")
							|| datoCtaSinEspacios[1].contains("otros bancos")
							|| datoCtaSinEspacios[1].contains("CUENTAS CORRIENTES OTROS BANCOS")
							|| datoCtaSinEspacios[1].contains("CUENTAS AHORRO OTROS BANCOS")
							|| datoCtaSinEspacios[1].contains("electronicos")
							|| datoCtaSinEspacios[1].contains("electr�nicos")
							|| datoCtaSinEspacios[1].contains("ELECTRONICOS")
							|| datoCtaSinEspacios[1].contains("ELECTR�NICOS")) {

						DatosDavivienda.RISKMOTOR.setCuentaDestino("Monedero otro Banco");

						if (datoCtaSinEspacios[1].contains("electronicos")|| datoCtaSinEspacios[1].contains("electr�nicos")|| datoCtaSinEspacios[1].contains("ELECTRONICOS")|| datoCtaSinEspacios[1].contains("ELECTR�NICOS")) {
							DatosDavivienda.RISKMOTOR.setTransaccion("Pago a monederos");
						}

					} else if (datoCtaSinEspacios[1].contains("cuenta de ahorros")
							|| datoCtaSinEspacios[1].contains("CUENTA DE AHORROS")) {

						DatosDavivienda.RISKMOTOR.setCuentaDestino("Cuenta Ahorros");

					} else if (datoCtaSinEspacios[1].contains("Cuenta Corriente")
							|| datoCtaSinEspacios[1].contains("CUENTA CORRIENTE")) {

						DatosDavivienda.RISKMOTOR.setCuentaDestino("Cuenta corriente");

					} else if (datoCtaSinEspacios[1].contains("Daviplata")
							|| datoCtaSinEspacios[1].contains("DAVIPLATA")) {

						DatosDavivienda.RISKMOTOR.setCuentaDestino("Daviplata");

					} else if (datoCtaSinEspacios[1].contains("TARJETA PREPAGO MAESTRO")
							|| datoCtaSinEspacios[1].contains("tarjeta prepago maestro")) {

//						DatosDavivienda.RISKMOTOR.setCuentaDestino("Tarjeta Cr�dito");
						DatosDavivienda.RISKMOTOR.setCuentaDestino("Tarjeta Prepago");

					} else if (!datoCtaSinEspacios[1].contains("Davivienda")
							|| !datoCtaSinEspacios[1].contains("Daviplata")) {

						DatosDavivienda.RISKMOTOR.setCuentaDestino(this.TipoProd(this.referencia1));

					}

					if (this.servicio.contains("Mismo NIT") || this.servicio.contains("Cuenta No Inscrita")
							|| this.servicio.contains("Cuenta Inscrita") || this.servicio.contains("Cuenta Inscrita")) {

						DatosDavivienda.RISKMOTOR.setNumCuentaDestino(this.referencia2);

					}

					if (this.servicio.contains("N�mina") || this.servicio.contains("Pago a Proveedores")) {
						String[] datoCtaSinEspacios2 = null;

						for (String desMigrada : datoNumDestCuentas) {
							datoCtaSinEspacios2 = desMigrada.split("\\|");

							if (datoCtaSinEspacios2[0].contains("DEP�SITOS ELECTRONICOS")
									|| datoCtaSinEspacios2[0].contains("DEP�SITOS ELECTR�NICOS"))
								datoCtaSinEspacios2[0] = "DEP�SITOS ELECTRONICOS";

							if (datoCtaSinEspacios2[0].equals(datoCtaSinEspacios[1])
									&& datoCtaSinEspacios2[1].contains(DXCUtil.right(datoCtaSinEspacios[4], 4))) {
								DatosDavivienda.RISKMOTOR.setNumCuentaDestino(datoCtaSinEspacios2[1]);

								if (controlDest == null)
									controlDest = new ControllerDestinosMasivos(pageDestino);

								if (datoCtaSinEspacios2[0].equals(datoCtaSinEspacios[1])
										&& datoCtaSinEspacios2[1].contains(DXCUtil.right(datoCtaSinEspacios[4], 4))) {

									String cuentaHomologada = controlDest.BuscarCuentasMigrada(datoCtaSinEspacios2[0],
											datoCtaSinEspacios2[1]);
									DatosDavivienda.RISKMOTOR.setNumCuentaOrigenMigrada(cuentaHomologada);
								}
							}
						}

					}

					DatosDavivienda.RISKMOTOR.setCausaRechazo(this.estado);

					if (this.estado.contains("rechazada") || this.estado.contains("denegado")
							|| this.estado.contains("no")) {

						DatosDavivienda.RISKMOTOR.setResultado("Declinada");
						DatosDavivienda.RISKMOTOR.setCausaRechazo(this.estado);

					} else if (this.estado.contains("EXCEDE") || this.estado.contains("TOPE")
							|| this.estado.contains("1480")) {

						DatosDavivienda.RISKMOTOR.setResultado("Rechazado");
						DatosDavivienda.RISKMOTOR.setCausaRechazo(this.estado);

					} else {

						DatosDavivienda.RISKMOTOR.setResultado("Exitosa");
						DatosDavivienda.RISKMOTOR.setCausaRechazo(" ");
					}

					if (this.estado.contains("rechazada") || this.estado.contains("no pudo ser realizada")) {

						DatosDavivienda.RISKMOTOR.setTemporalResultado("Declinada");
						DatosDavivienda.RISKMOTOR.setCausaRechazo(this.estado);

					} else if (this.estado.contains("TX EXCEDE TOPE SALDO")) {

						DatosDavivienda.RISKMOTOR.setTemporalResultado("Rechazado");
					}

					if (datoCtaSinEspacios[5] != null || !datoCtaSinEspacios[5].isEmpty()) {

						if (datoCtaSinEspacios[5].equals("Pago Rechazado")
								&& (datoCtaSinEspacios[6] == null || datoCtaSinEspacios[6].isEmpty())) {

							if (!datoCtaSinEspacios[5].equals("null") && !datoCtaSinEspacios[5].equals("")
									&& !datoCtaSinEspacios[5].equals(" ")) {
								Reporter.reportEvent(Reporter.MIC_FAIL,
										"Pago Rechazado y el mensaje fue [" + datoCtaSinEspacios[5] + "]");
								DatosDavivienda.RISKMOTOR.setCausaRechazo(datoCtaSinEspacios[5]);
								DatosDavivienda.RISKMOTOR.setResultado("Rechazada");
							}

						} else if (datoCtaSinEspacios[5].equals("Pago Rechazado")
								&& (datoCtaSinEspacios[6] != null || !datoCtaSinEspacios[6].isEmpty())) {

							if (!datoCtaSinEspacios[6].equals("null") && !datoCtaSinEspacios[6].equals("")
									&& !datoCtaSinEspacios[6].equals(" ")) {
								Reporter.reportEvent(Reporter.MIC_FAIL,
										"Pago Rechazado y el mensaje fue [" + datoCtaSinEspacios[6] + "]");
								DatosDavivienda.RISKMOTOR.setCausaRechazo(datoCtaSinEspacios[6]);
								DatosDavivienda.RISKMOTOR.setResultado("Rechazada");
							}

						} else if (!datoCtaSinEspacios[5].isEmpty()
								&& (datoCtaSinEspacios[6] != null || !datoCtaSinEspacios[6].isEmpty())) {

							if (!datoCtaSinEspacios[6].equals("null") && !datoCtaSinEspacios[6].equals("")
									&& !datoCtaSinEspacios[6].equals(" ")) {
								Reporter.reportEvent(Reporter.MIC_FAIL,
										"Pago Rechazado y el mensaje fue [" + datoCtaSinEspacios[6] + "]");
								DatosDavivienda.RISKMOTOR.setCausaRechazo(datoCtaSinEspacios[6]);
								DatosDavivienda.RISKMOTOR.setResultado("Rechazada");
							}
						}

					}

				} else {

					Reporter.reportEvent(Reporter.MIC_FAIL,
							"Se esperaba transaccion exitosa y el estado fue [" + this.estadoTx + "]");

					DatosDavivienda.RISKMOTOR.setTemporalObservacion(servicio);
					DatosDavivienda.RISKMOTOR.setTemporalTime(pageConf.getFechaHoraTx());
					DatosDavivienda.RISKMOTOR.setTime();
					DatosDavivienda.RISKMOTOR.setCausaRechazo(this.estado);
					DatosDavivienda.RISKMOTOR.setResultado("Declinada");
					DatosDavivienda.RISKMOTOR.setNumCuentaOrigen(this.numCta);
//					DatosDavivienda.RISKMOTOR.setNumeroTx(this.numAprova);
					DatosDavivienda.RISKMOTOR.setMonto(datoCtaSinEspacios[2]);
//					DatosDavivienda.RISKMOTOR.setTipoIdEmpresa(this.tipoIdEm);
					DatosDavivienda.RISKMOTOR.setNumIdEmpresa(this.nitEmpre);

					DatosDavivienda.RISKMOTOR.setCuentaOrigen(this.TipoProd(this.tipoCta));
					DatosDavivienda.RISKMOTOR.setReferencia_1("N/A");

					if (datoCtaSinEspacios[1].contains("otros") || datoCtaSinEspacios[1].contains("OTROS")
							|| datoCtaSinEspacios[1].contains("OTROS BANCOS")
							|| datoCtaSinEspacios[1].contains("otros bancos")
							|| datoCtaSinEspacios[1].contains("CUENTAS CORRIENTES OTROS BANCOS")
							|| datoCtaSinEspacios[1].contains("CUENTAS AHORRO OTROS BANCOS")
							|| datoCtaSinEspacios[1].contains("electronicos")
							|| datoCtaSinEspacios[1].contains("electr�nicos")
							|| datoCtaSinEspacios[1].contains("ELECTRONICOS")
							|| datoCtaSinEspacios[1].contains("ELECTR�NICOS")) {

						DatosDavivienda.RISKMOTOR.setCuentaDestino("Monedero otro Banco");

						if (datoCtaSinEspacios[1].contains("electronicos")|| datoCtaSinEspacios[1].contains("electr�nicos")|| datoCtaSinEspacios[1].contains("ELECTRONICOS")|| datoCtaSinEspacios[1].contains("ELECTR�NICOS")) {
							DatosDavivienda.RISKMOTOR.setTransaccion("Pago a monederos");
						}

					} else if (datoCtaSinEspacios[1].contains("cuenta de ahorros")
							|| datoCtaSinEspacios[1].contains("CUENTA DE AHORROS")) {

						DatosDavivienda.RISKMOTOR.setCuentaDestino("Cuenta Ahorros");

					} else if (datoCtaSinEspacios[1].contains("Cuenta Corriente")
							|| datoCtaSinEspacios[1].contains("CUENTA CORRIENTE")) {

						DatosDavivienda.RISKMOTOR.setCuentaDestino("Cuenta corriente");

					} else if (datoCtaSinEspacios[1].contains("Daviplata")
							|| datoCtaSinEspacios[1].contains("DAVIPLATA")) {

						DatosDavivienda.RISKMOTOR.setCuentaDestino("Daviplata");

					} else if (datoCtaSinEspacios[1].contains("TARJETA PREPAGO MAESTRO")
							|| datoCtaSinEspacios[1].contains("tarjeta prepago maestro")) {

						DatosDavivienda.RISKMOTOR.setCuentaDestino("Tarjeta Cr�dito");

					} else if (!datoCtaSinEspacios[1].contains("Davivienda")
							|| !datoCtaSinEspacios[1].contains("Daviplata")) {

						DatosDavivienda.RISKMOTOR.setCuentaDestino(this.TipoProd(this.referencia1));
					}

					if (this.servicio.contains("N�mina") || this.servicio.contains("Pago a Proveedores")) {
						String[] datoCtaSinEspacios2 = null;

						for (String desMigrada : datoNumDestCuentas) {
							datoCtaSinEspacios2 = desMigrada.split("\\|");

							if (datoCtaSinEspacios2[0].contains("DEP�SITOS ELECTRONICOS")
									|| datoCtaSinEspacios2[0].contains("DEP�SITOS ELECTR�NICOS"))
								datoCtaSinEspacios2[0] = "DEP�SITOS ELECTRONICOS";

							if (datoCtaSinEspacios2[0].equals(datoCtaSinEspacios[1])
									&& datoCtaSinEspacios2[1].contains(DXCUtil.right(datoCtaSinEspacios[4], 4))) {
								DatosDavivienda.RISKMOTOR.setNumCuentaDestino(datoCtaSinEspacios2[1]);
								if (controlDest == null)
									controlDest = new ControllerDestinosMasivos(pageDestino);

								if (datoCtaSinEspacios2[0].equals(datoCtaSinEspacios[1])
										&& datoCtaSinEspacios2[1].contains(DXCUtil.right(datoCtaSinEspacios[4], 4))) {

									String cuentaHomologada = controlDest.BuscarCuentasMigrada(datoCtaSinEspacios2[0],
											datoCtaSinEspacios2[1]);
									DatosDavivienda.RISKMOTOR.setNumCuentaOrigenMigrada(cuentaHomologada);

								}
							}
						}

					}

					if (datoCtaSinEspacios[5] != null || !datoCtaSinEspacios[5].isEmpty()) {

						if (datoCtaSinEspacios[5].equals("Pago Rechazado")
								&& (datoCtaSinEspacios[6] == null || datoCtaSinEspacios[6].isEmpty())) {

							if (!datoCtaSinEspacios[5].equals("null") && !datoCtaSinEspacios[5].equals("")
									&& !datoCtaSinEspacios[5].equals(" ")) {

								Reporter.reportEvent(Reporter.MIC_FAIL,
										"Pago Rechazado y el mensaje fue [" + datoCtaSinEspacios[5] + "]");
								DatosDavivienda.RISKMOTOR.setCausaRechazo(datoCtaSinEspacios[5]);
								DatosDavivienda.RISKMOTOR.setResultado("Rechazada");
							}

						} else if (datoCtaSinEspacios[5].equals("Pago Rechazado")
								&& (datoCtaSinEspacios[6] != null || !datoCtaSinEspacios[6].isEmpty())) {

							if (!datoCtaSinEspacios[6].equals("null") && !datoCtaSinEspacios[6].equals("")
									&& !datoCtaSinEspacios[6].equals(" ")) {
								Reporter.reportEvent(Reporter.MIC_FAIL,
										"Pago Rechazado y el mensaje fue [" + datoCtaSinEspacios[6] + "]");
								DatosDavivienda.RISKMOTOR.setCausaRechazo(datoCtaSinEspacios[6]);
								DatosDavivienda.RISKMOTOR.setResultado("Rechazada");
							}

						} else if (!datoCtaSinEspacios[5].isEmpty()
								&& (datoCtaSinEspacios[6] != null || !datoCtaSinEspacios[6].isEmpty())) {

							if (!datoCtaSinEspacios[6].equals("null") && !datoCtaSinEspacios[6].equals("")
									&& !datoCtaSinEspacios[6].equals(" ")) {
								Reporter.reportEvent(Reporter.MIC_FAIL,
										"Pago Rechazado y el mensaje fue [" + datoCtaSinEspacios[6] + "]");
								DatosDavivienda.RISKMOTOR.setCausaRechazo(datoCtaSinEspacios[6]);
								DatosDavivienda.RISKMOTOR.setResultado("Rechazada");
							}
						}

					}

				}
				break;
			// -----------------------------------------------------------------------------------------------------------------------
			default:
				Reporter.reportEvent(Reporter.MIC_FAIL, "Riesgo [" + riesgo + "] NO contemplado");
				break;
			}
		}
	}

// =======================================================================================================================

// ===========================================[validarIngresoMR]===========================================================================

//	/**
//	 * Para el caso de Motor de Riesgo, dependiendo de la respuesta dada por el
//	 * intento de confirmar la transacci�n, se eval�a, seg�n el riesgo del cliente
//	 * de la iteraci�n actual, la adici�n o no del registro actual.
//	 */
//
//	public void validarIngresoMR() throws Exception {
//
//		DatosDavivienda.RISKMOTOR.adicionarRegistro();
//		// SETEA LOS REGISTROS QUE SON GUARDADOS DE MANERA TEMPORAL
//		Date fechaHoraLogMR = new Date();
//		DatosDavivienda.RISKMOTOR.setTemporalTime(fechaHoraLogMR);
//		DatosDavivienda.RISKMOTOR.setTime();
//		DatosDavivienda.RISKMOTOR.setMonto();
//		DatosDavivienda.RISKMOTOR.setNumeroTx();
//		DatosDavivienda.RISKMOTOR.setResultado();
//		DatosDavivienda.RISKMOTOR.setObservacion();
//		DatosDavivienda.RISKMOTOR.setObservacion(this.desdeElDetalle);
//		
//		DatosDavivienda.RISKMOTOR.setCanal("WEB_PYME");
//		DatosDavivienda.RISKMOTOR.setAmbienteDePruebas(this.nombreAmbiente);
//
//		String uuid = SettingsRun.getTestData().getParameter("Hash").trim();
//		DatosDavivienda.RISKMOTOR.setHash(uuid);
//
////		DatosDavivienda.RISKMOTOR.setUserAgent(this.userAgent);
//
////-----------------------------------------------------------------------------------------------------------------------
//
//		this.servicio = SettingsRun.getTestData().getParameter("Servicio").trim();
//		DatosDavivienda.RISKMOTOR.setTransaccion(this.getTransaccion(this.tipoPrueba, this.servicio));
//
//		String userType = "Token"; // VALOR POR DEFECTO
//		if (DatosEmpresarial.TIPO_TOKEN.equals(DatosEmpresarial.TOKEN_OTP))
//			userType = "Virtual";
//
//		String intentos = SettingsRun.getTestData().getParameter("Ingresos Fallidos").trim();
//		int intentosFall = 0; // VALOR POR DEFECTO
//		if (DXCUtil.isInteger(intentos))
//			intentosFall = Integer.valueOf(intentos);
////-----------------------------------------------------------------------------------------------------------------------
//		// SETEA LOS DATOS FALTANTES
//		DatosDavivienda.RISKMOTOR.setUserType(userType);
//		DatosDavivienda.RISKMOTOR.setRisk(riesgoBc);
//		if (!this.servicio.equals("Tx Internacionales Recibir desde el exterior")&& !this.servicio.equals("Tx Internacionales Enviar al exterior"))
//			DatosDavivienda.RISKMOTOR.setRiskEFM(riesgoEfm);
//
//
//
//		DatosDavivienda.RISKMOTOR.setNumRetosFallidos(intentosFall);
//		// System.out.println(this.ipPublica);
////		DatosDavivienda.RISKMOTOR.setDireccionIp(this.ipPublica);
//		DatosDavivienda.RISKMOTOR.setNavegador_SistemaOperativo(this.navegador);
//		DatosDavivienda.RISKMOTOR.setCliente(DatosEmpresarial.CLI_EMPRESAR);
//		DatosDavivienda.RISKMOTOR.setDocumento(DatosEmpresarial.TIPO_ID_LOGUEO, DatosEmpresarial.NUM_ID_LOGUEO);
//
//		if (!this.servicio.contains("N�mina") || !this.servicio.contains("Pago a Proveedores")
//				|| !this.servicio.contains("AFC") || !this.servicio.contains("Cr�dito.3ros")) {
//
//			DatosDavivienda.RISKMOTOR.setTemporalMonto(this.montoReal);
//		}
//
//		if (!this.tipoPrueba.equals(TP_LOGIN))
//			this.transaccion = this.getTransaccion(this.tipoPrueba, this.servicio);
//		DatosDavivienda.RISKMOTOR.setTransaccion(this.transaccion);
//
//		DatosDavivienda.RISKMOTOR.setHash(uuid);
//
//		if (this.numeroTx != null)
//			DatosDavivienda.RISKMOTOR.setNumeroTx(this.numeroTx);
//
//		if (this.desdeElDetalle.contains("SI") && this.tipoPrueba.equals("Tx Pend Aprobaci�n"))
//			this.desdeElDetalle = "Aprobado dentro del detalle";
//		else if (this.desdeElDetalle.contains("NO") && this.tipoPrueba.equals("Tx Pend Aprobaci�n"))
//			this.desdeElDetalle = "Aprobado furea del detalle";
//
//		DatosDavivienda.RISKMOTOR.setObservacion(this.desdeElDetalle);
//
//		if (this.descripcionsin != null || !this.descripcionsin.isEmpty() || !this.descripcionsin.equals(" "))
//			DatosDavivienda.RISKMOTOR.setObservacion(this.descripcionsin + " - " + this.desdeElDetalle);
//
//		String riesgoBc = SettingsRun.getTestData().getParameter("Nivel de Riesgo BC").trim();
//		String riesgoEfm = SettingsRun.getTestData().getParameter("Nivel de Riesgo SAS EFM").trim();
//
//		DatosDavivienda.RISKMOTOR.setRisk(riesgoBc);
//		if (!this.servicio.equals("Tx Internacionales Recibir desde el exterior")
//				&& !this.servicio.equals("Tx Internacionales Enviar al exterior"))
//			DatosDavivienda.RISKMOTOR.setRiskEFM(riesgoEfm);
//
//		// Nivel de riesgo Motor
//		this.riesgo = this.prioridaRiesgo;
//
//		switch (this.riesgo.toUpperCase()) {
//		// -----------------------------------------------------------------------------------------------------------------------
//		case MotorRiesgo.RISK_ALTO:
//			if (this.estadoTx.equals("DECLINADA")) {
//
//				DatosDavivienda.RISKMOTOR.setTemporalResultado(MotorRiesgo.RESULT_SUCCESS);
//
//				DatosDavivienda.RISKMOTOR.setTemporalObservacion(servicio);
//
//				DatosDavivienda.RISKMOTOR.setTemporalTime(pageConf.getFechaHoraTx());
//
//				DatosDavivienda.RISKMOTOR.setTime();
//
//				if (activityAmount != null)
//					DatosDavivienda.RISKMOTOR.setMonto(activityAmount);
//
//
//
//				// Datos Cuenta Origen
//
//				if (!this.servicio.equals("Tx Internacionales Recibir desde el exterior")) {
//					DatosDavivienda.RISKMOTOR.setCuentaOrigen(this.TipoProd(this.tipoCta));
//					DatosDavivienda.RISKMOTOR.set_CtaOrigenODestMIGRADA(this.numCta);
//				}
//
//				if (this.servicio.equals("Tx Internacionales Recibir desde el exterior")) {
//
//					DatosDavivienda.RISKMOTOR.setCuentaOrigen("Cuenta en el extranjero");
//					DatosDavivienda.RISKMOTOR.set_CtaOrigenODestMIGRADA("N/A");
//				}
////				DatosDavivienda.RISKMOTOR.setTipCuentaOrigen(this.tipoCta,this.numCta);
//
//				// Datos Cuenta destino
//				if (this.bancoDesMotor != null) {
//					if (!this.bancoDesMotor.equals(" ")) {
//						if (this.bancoDesMotor.contains("Davivienda") || this.bancoDesMotor.contains("DAVIVIENDA")
//								|| this.bancoDesMotor.contains("Daviplata")) {
//
//							DatosDavivienda.RISKMOTOR.setCuentaDestino(this.TipoProd(this.referencia1));
//
//						} else if (this.bancoDesMotor != null || !this.bancoDesMotor.isEmpty()
//								|| !this.bancoDesMotor.equals(" ")
//										&& (!this.bancoDesMotor.contains("Davivienda")
//												|| !this.bancoDesMotor.contains("DAVIVIENDA"))
//										&& (!this.bancoDesMotor.contains("Daviplata")
//												|| !this.bancoDesMotor.contains("DAVIPLATA"))) {
////							DatosDavivienda.RISKMOTOR.setTransaccion("Transferencias a monederos");
//							DatosDavivienda.RISKMOTOR.setCuentaDestino("Monedero otro Banco");
//						}
//					}
//				}
//
//				if (this.servicio.contains("Servicios")) {
//
//					if (DXCUtil.isNumeric(this.referencia1)) {
//						if (referencia2.isEmpty()) {
////							DatosDavivienda.RISKMOTOR.setReferencia_1(this.referencia1);
//							Reporter.write(this.referencia1);
//						} else {
////							DatosDavivienda.RISKMOTOR.setReferencia_1(this.referencia1, this.referencia2);
//							Reporter.write(this.referencia1+" - "+ this.referencia2);
//						}
//
//					} else {
//						Reporter.write(this.referencia2);
////						DatosDavivienda.RISKMOTOR.setReferencia_1(this.referencia2);
//					}
//
//					DatosDavivienda.RISKMOTOR.setCuentaDestino("Cuenta Corriente");
//
//				} else {
//
//					DatosDavivienda.RISKMOTOR.setCuentaDestino(this.TipoProd(this.referencia1));
//
//					if (this.servicio.equals("Tx Internacionales Recibir desde el exterior")) {
//
//						DatosDavivienda.RISKMOTOR.setTransaccion("Recibir Dinero del Exterior");
//						DatosDavivienda.RISKMOTOR.setCuentaDestino(this.referencia1);
//						DatosDavivienda.RISKMOTOR.setObservacion("Tipo Cta Destino " + this.referencia2);
//
//					}
//
//					if (this.servicio.equals("Tx Internacionales Enviar al exterior")) {
//
//						DatosDavivienda.RISKMOTOR.setTransaccion("Enviar Dinero al Exterior");
//						DatosDavivienda.RISKMOTOR.setCuentaDestino("Cuenta en el extranjero");
//						DatosDavivienda.RISKMOTOR.setObservacion("");
//
//					}
//
//					if (this.servicio.contains("Cuenta No Inscrita") || this.servicio.contains("Mismo NIT")|| this.servicio.contains("Cuenta Inscrita")) {
//
//						DatosDavivienda.RISKMOTOR.setCuentaDestino(this.TipoProd(this.referencia1));
//
//						if (this.referencia1.contains("DEP�SITOS ELECTRONICOS")
//								|| this.referencia1.contains("DEP�SITOS ELECTR�NICOS")
//								|| this.referencia1.contains("Dep�sitos electronicos"))
//							
//							DatosDavivienda.RISKMOTOR.setCuentaDestino("Monedero otro Banco");
//						DatosDavivienda.RISKMOTOR.setTransaccion("Transferencias a monederos");
//					}
//
//				}
//
//				// Bucar Cuenta Migrada
//				if (!this.servicio.equals("Tx Internacionales Recibir desde el exterior")) {
//
//					if (this.servicio.contains("N�mi") || this.servicio.contains("Proveedores")
//							|| this.servicio.contains("Proveedores") || this.servicio.contains("AFC")
//							|| this.servicio.contains("Pago a Cr�ditos de Terceros")
//							|| this.servicio.contains("Pagos a cr�ditos de terceros")
//							|| this.servicio.contains("Cr�dito.3ros")
//							|| this.servicio.contains("Transferencias Mismo NIT")
//							|| this.servicio.contains("Transferencia NIT Propio") || this.servicio.contains("Mismo NIT")
//							|| this.servicio.contains("Transferencias Cuenta Inscrita")
//							|| this.servicio.contains("Cuenta Inscrita")
//							|| this.servicio.contains("Transferencias Cuenta No Inscrita")
//							|| this.servicio.contains("Transferencias Cuenta NO Inscrita")
//							|| this.servicio.contains("Cuenta No Inscrita")) {
//
//						if (controlDest == null)
//							controlDest = new ControllerDestinosMasivos(pageDestino);
//
//						String cuentaHomologada = controlDest.BuscarCuentasMigrada(this.TipoProd(this.tipoCta),this.numCta);
//
//						DatosDavivienda.RISKMOTOR.set_CtaOrigenODestMIGRADA(cuentaHomologada);
//					}
//
//				} else {
//					if (this.referencia1.contains("DEP�SITOS ELECTRONICOS")|| this.referencia1.contains("DEP�SITOS ELECTR�NICOS")
//							|| this.referencia1.contains("Dep�sitos electronicos"))
//
//						this.referencia1 = "DEP�SITOS ELECTRONICOS";
//
//					if (controlDest == null)
//						controlDest = new ControllerDestinosMasivos(pageDestino);
//
//					String cuentaHomologada = controlDest.BuscarCuentasMigrada(this.TipoProd(this.tipoCta),this.numCta);
//
//					DatosDavivienda.RISKMOTOR.set_CtaOrigenODestMIGRADA(cuentaHomologada);
//
//				}
//
//				// Dato de Esto de la transacion
//
//
//				if (this.estado.contains("rechazada") || this.estado.contains("denegado")
//						|| this.estado.contains("no")) {
//
//					DatosDavivienda.RISKMOTOR.setResultado("Declinada");
//
//				} else if (this.estado.contains("EXCEDE") || this.estado.contains("TOPE")
//						|| this.estado.contains("1480")) {
//
//					DatosDavivienda.RISKMOTOR.setResultado("Rechazado");
//				} else {
//					DatosDavivienda.RISKMOTOR.setResultado("Exitosa");
//				}
//
//				if (this.estado.contains("rechazada") || this.estado.contains("no pudo ser realizada")) {
//
//					DatosDavivienda.RISKMOTOR.setTemporalResultado("Declinada");
//
//				} else if (this.estado.contains("TX EXCEDE TOPE SALDO")) {
//
//					DatosDavivienda.RISKMOTOR.setTemporalResultado("Rechazado");
//				}
//
//			} else {
//
//				// Informacion Motor de Riegos Alto si Se esperaba declinaci�n y el mensaje fue
//				// diferente
//
//				Reporter.reportEvent(Reporter.MIC_FAIL,
//						"Se esperaba declinaci�n y el mensaje fue [" + this.estadoTx + "]");
//
//				DatosDavivienda.RISKMOTOR.setTemporalObservacion(servicio);
//				DatosDavivienda.RISKMOTOR.setTemporalTime(pageConf.getFechaHoraTx());
//				DatosDavivienda.RISKMOTOR.setTime();
//
//				if (activityAmount != null)
//					DatosDavivienda.RISKMOTOR.setMonto(activityAmount);
//
//
//				// Datos Cuenta Origen
//
//				if (!this.servicio.equals("Tx Internacionales Recibir desde el exterior")) {
//					DatosDavivienda.RISKMOTOR.setCuentaOrigen(this.TipoProd(this.tipoCta));
//					DatosDavivienda.RISKMOTOR.set_CtaOrigenODestMIGRADA(this.numCta);
//				}
//
//				if (this.servicio.equals("Tx Internacionales Recibir desde el exterior")) {
//
//					DatosDavivienda.RISKMOTOR.setCuentaOrigen("Cuenta en el extranjero");
//					DatosDavivienda.RISKMOTOR.set_CtaOrigenODestMIGRADA("N/A");
//				}
//
//				// Datos Cuenta destino
//				if (this.bancoDesMotor != null) {
//					if (!this.bancoDesMotor.equals(" ")) {
//						if (this.bancoDesMotor.contains("Davivienda") || this.bancoDesMotor.contains("DAVIVIENDA")
//								|| this.bancoDesMotor.contains("Daviplata")) {
//
//							DatosDavivienda.RISKMOTOR.setCuentaDestino(this.TipoProd(this.referencia1));
//
//						} else if (this.bancoDesMotor != null || !this.bancoDesMotor.isEmpty()
//								|| !this.bancoDesMotor.equals(" ")
//										&& (!this.bancoDesMotor.contains("Davivienda")
//												|| !this.bancoDesMotor.contains("DAVIVIENDA"))
//										&& (!this.bancoDesMotor.contains("Daviplata")
//												|| !this.bancoDesMotor.contains("DAVIPLATA"))) {
//
//							DatosDavivienda.RISKMOTOR.setCuentaDestino("Monedero otro Banco");
//						}
//					}
//				}
//
//				if (this.servicio.contains("Servicios")) {
//
//					if (DXCUtil.isNumeric(this.referencia1)) {
//						if (referencia2.isEmpty()) {
////							DatosDavivienda.RISKMOTOR.setReferencia_1(this.referencia1);
//							Reporter.write(this.referencia1);
//						} else {
////							DatosDavivienda.RISKMOTOR.setReferencia_1(this.referencia1, this.referencia2);
//							Reporter.write(this.referencia1+" - "+ this.referencia2);
//						}
//
//					} else {
//						Reporter.write(this.referencia2);
////						DatosDavivienda.RISKMOTOR.setReferencia_1(this.referencia2);
//					}
//
//					DatosDavivienda.RISKMOTOR.setCuentaDestino("Cuenta Corriente");
//
//				} else {
//
//					DatosDavivienda.RISKMOTOR.setCuentaDestino(this.TipoProd(this.referencia1));
//
//					if (this.servicio.equals("Tx Internacionales Recibir desde el exterior")) {
//
//						DatosDavivienda.RISKMOTOR.setTransaccion("Recibir Dinero del Exterior");
//						DatosDavivienda.RISKMOTOR.setCuentaDestino(this.referencia1);
//						
//						DatosDavivienda.RISKMOTOR.setObservacion("Tipo Cta Destino " + this.referencia2);
//
//					}
//
//					if (this.servicio.equals("Tx Internacionales Enviar al exterior")) {
//
//						DatosDavivienda.RISKMOTOR.setTransaccion("Enviar Dinero al Exterior");
//						DatosDavivienda.RISKMOTOR.setCuentaDestino("Cuenta en el extranjero");
//						DatosDavivienda.RISKMOTOR.setObservacion("");
//
//					}
//
//					if (this.servicio.contains("Cuenta No Inscrita") || this.servicio.contains("Mismo NIT")
//							|| this.servicio.contains("Cuenta Inscrita")) {
//
//						DatosDavivienda.RISKMOTOR.setCuentaDestino(this.TipoProd(this.referencia1));
//
//						if (this.referencia1.contains("DEP�SITOS ELECTRONICOS")
//								|| this.referencia1.contains("DEP�SITOS ELECTR�NICOS")
//								|| this.referencia1.contains("Dep�sitos electronicos"))
//							
//							DatosDavivienda.RISKMOTOR.setCuentaDestino("Monedero otro Banco");
//						
//						DatosDavivienda.RISKMOTOR.setTransaccion("Transferencias a monederos");
//					}
//
//				}
//
//				// Bucar Cuenta Migrada
//				if (!this.servicio.equals("Tx Internacionales Recibir desde el exterior")) {
//
//					if (this.servicio.contains("N�mi") || this.servicio.contains("Proveedores")
//							|| this.servicio.contains("Proveedores") || this.servicio.contains("AFC")
//							|| this.servicio.contains("Pago a Cr�ditos de Terceros")
//							|| this.servicio.contains("Pagos a cr�ditos de terceros")
//							|| this.servicio.contains("Cr�dito.3ros")
//							|| this.servicio.contains("Transferencias Mismo NIT")
//							|| this.servicio.contains("Transferencia NIT Propio") || this.servicio.contains("Mismo NIT")
//							|| this.servicio.contains("Transferencias Cuenta Inscrita")
//							|| this.servicio.contains("Cuenta Inscrita")
//							|| this.servicio.contains("Transferencias Cuenta No Inscrita")
//							|| this.servicio.contains("Transferencias Cuenta NO Inscrita")
//							|| this.servicio.contains("Cuenta No Inscrita")) {
//
//						if (controlDest == null)
//							controlDest = new ControllerDestinosMasivos(pageDestino);
//
//						String cuentaHomologada = controlDest.BuscarCuentasMigrada(this.TipoProd(this.tipoCta),this.numCta);
//
//						DatosDavivienda.RISKMOTOR.set_CtaOrigenODestMIGRADA(cuentaHomologada);
//					}
//
//				} else {
//
//					if (this.referencia1.contains("DEP�SITOS ELECTRONICOS")
//							|| this.referencia1.contains("DEP�SITOS ELECTR�NICOS")
//							|| this.referencia1.contains("Dep�sitos electronicos"))
//
//						this.referencia1 = "DEP�SITOS ELECTRONICOS";
//
//
//					if (controlDest == null)
//						controlDest = new ControllerDestinosMasivos(pageDestino);
//
//					String cuentaHomologada = controlDest.BuscarCuentasMigrada(this.TipoProd(this.tipoCta),this.numCta);
//
//					DatosDavivienda.RISKMOTOR.set_CtaOrigenODestMIGRADA(cuentaHomologada);
//
//				}
//
//				// Estado de la Tx
//				DatosDavivienda.RISKMOTOR.setResultado("Exitosa");
//
//				if (this.estado.contains("rechazada") || this.estado.contains("no pudo ser realizada")) {
//					DatosDavivienda.RISKMOTOR.setResultado("Declinada");
//				} else if (this.estado.contains("TX EXCEDE TOPE SALDO")) {
//					DatosDavivienda.RISKMOTOR.setResultado("Rechazado");
//				}
//
//			}
//
//			break;
//		// -----------------------------------------------------------------------------------------------------------------------
//		case MotorRiesgo.RISK_MEDIO:
//		case MotorRiesgo.RISK_BAJO:
//
//			// if (this.isTxExitosa(msgRespuesta)) {
//			if (DXCUtil.itemInArray(this.estadoTx, arrEstadosTxAprob)) {
//
//				DatosDavivienda.RISKMOTOR.setTemporalResultado(MotorRiesgo.RESULT_SUCCESS);
//				DatosDavivienda.RISKMOTOR.setTemporalObservacion(servicio);
//				DatosDavivienda.RISKMOTOR.setTemporalTime(pageConf.getFechaHoraTx());
//				DatosDavivienda.RISKMOTOR.setTime();
//				
//
//
//				if (activityAmount != null)
//					DatosDavivienda.RISKMOTOR.setMonto(activityAmount);
//
//
//				// Datos Cuenta Origen
//
//				if (!this.servicio.equals("Tx Internacionales Recibir desde el exterior")) {
//					DatosDavivienda.RISKMOTOR.setCuentaOrigen(this.TipoProd(this.tipoCta));
//					DatosDavivienda.RISKMOTOR.set_CtaOrigenODestMIGRADA(this.numCta);
//				}
//
//				if (this.servicio.equals("Tx Internacionales Recibir desde el exterior")) {
//
//					DatosDavivienda.RISKMOTOR.setCuentaOrigen("Cuenta en el extranjero");
//					DatosDavivienda.RISKMOTOR.set_CtaOrigenODestMIGRADA("N/A");
//				}
////				DatosDavivienda.RISKMOTOR.setTipCuentaOrigen(this.tipoCta,this.numCta);
//
//				// Datos Cuenta destino
//				if (this.bancoDesMotor != null) {
//					if (!this.bancoDesMotor.equals(" ")) {
//						if (this.bancoDesMotor.contains("Davivienda") || this.bancoDesMotor.contains("DAVIVIENDA")
//								|| this.bancoDesMotor.contains("Daviplata")) {
//
//							DatosDavivienda.RISKMOTOR.setCuentaDestino(this.TipoProd(this.referencia1));
//
//						} else if (this.bancoDesMotor != null || !this.bancoDesMotor.isEmpty()
//								|| !this.bancoDesMotor.equals(" ")
//										&& (!this.bancoDesMotor.contains("Davivienda")
//												|| !this.bancoDesMotor.contains("DAVIVIENDA"))
//										&& (!this.bancoDesMotor.contains("Daviplata")
//												|| !this.bancoDesMotor.contains("DAVIPLATA"))) {
//
//							DatosDavivienda.RISKMOTOR.setCuentaDestino("Monedero otro Banco");
//						}
//					}
//				}
//
//				if (this.servicio.contains("Servicios")) {
//
//					if (DXCUtil.isNumeric(this.referencia1)) {
//						if (referencia2.isEmpty()) {
////							DatosDavivienda.RISKMOTOR.setReferencia_1(this.referencia1);
//							Reporter.write(this.referencia1);
//						} else {
////							DatosDavivienda.RISKMOTOR.setReferencia_1(this.referencia1, this.referencia2);
//							Reporter.write(this.referencia1+" - "+ this.referencia2);
//						}
//
//					} else {
//						Reporter.write(this.referencia2);
////						DatosDavivienda.RISKMOTOR.setReferencia_1(this.referencia2);
//					}
//
//					DatosDavivienda.RISKMOTOR.setCuentaDestino("Cuenta Corriente");
//
//				} else {
//
//					DatosDavivienda.RISKMOTOR.setCuentaDestino(this.TipoProd(this.referencia1));
//
//					if (this.servicio.equals("Tx Internacionales Recibir desde el exterior")) {
//
//						DatosDavivienda.RISKMOTOR.setTransaccion("Recibir Dinero del Exterior");
//						DatosDavivienda.RISKMOTOR.setCuentaDestino(this.referencia1);
//						DatosDavivienda.RISKMOTOR.setObservacion("Tipo Cta Destino " + this.referencia2);
//
//					}
//
//					if (this.servicio.equals("Tx Internacionales Enviar al exterior")) {
//
//						DatosDavivienda.RISKMOTOR.setTransaccion("Enviar Dinero al Exterior");
//						DatosDavivienda.RISKMOTOR.setCuentaDestino("Cuenta en el extranjero");
//						DatosDavivienda.RISKMOTOR.setObservacion("");
//
//					}
//
//					if (this.servicio.contains("Cuenta No Inscrita") || this.servicio.contains("Mismo NIT")
//							|| this.servicio.contains("Cuenta Inscrita")) {
//
//						DatosDavivienda.RISKMOTOR.setCuentaDestino(this.TipoProd(this.referencia1));
//
//						if (this.referencia1.contains("DEP�SITOS ELECTRONICOS")
//								|| this.referencia1.contains("DEP�SITOS ELECTR�NICOS")
//								|| this.referencia1.contains("Dep�sitos electronicos"))
//							DatosDavivienda.RISKMOTOR.setCuentaDestino("Monedero otro Banco");
//						DatosDavivienda.RISKMOTOR.setTransaccion("Transferencias a monederos");
//					}
//
//				}
//
//				// Bucar Cuenta Migrada
//				if (!this.servicio.equals("Tx Internacionales Recibir desde el exterior")) {
//
//					if (this.servicio.contains("N�mi") || this.servicio.contains("Proveedores")
//							|| this.servicio.contains("Proveedores") || this.servicio.contains("AFC")
//							|| this.servicio.contains("Pago a Cr�ditos de Terceros")
//							|| this.servicio.contains("Pagos a cr�ditos de terceros")
//							|| this.servicio.contains("Cr�dito.3ros")
//							|| this.servicio.contains("Transferencias Mismo NIT")
//							|| this.servicio.contains("Transferencia NIT Propio") || this.servicio.contains("Mismo NIT")
//							|| this.servicio.contains("Transferencias Cuenta Inscrita")
//							|| this.servicio.contains("Cuenta Inscrita")
//							|| this.servicio.contains("Transferencias Cuenta No Inscrita")
//							|| this.servicio.contains("Transferencias Cuenta NO Inscrita")
//							|| this.servicio.contains("Cuenta No Inscrita")) {
//
//						if (controlDest == null)
//							controlDest = new ControllerDestinosMasivos(pageDestino);
//
//						String cuentaHomologada = controlDest.BuscarCuentasMigrada(this.TipoProd(this.tipoCta),this.numCta);
//
//						DatosDavivienda.RISKMOTOR.set_CtaOrigenODestMIGRADA(cuentaHomologada);
//					}
//
//				} else {
//
//					if (this.referencia1.contains("DEP�SITOS ELECTRONICOS")
//							|| this.referencia1.contains("DEP�SITOS ELECTR�NICOS")
//							|| this.referencia1.contains("Dep�sitos electronicos"))
//
//						this.referencia1 = "DEP�SITOS ELECTRONICOS";
//
//
//					if (controlDest == null)
//						controlDest = new ControllerDestinosMasivos(pageDestino);
//
//					String cuentaHomologada = controlDest.BuscarCuentasMigrada(this.TipoProd(this.tipoCta),this.numCta);
//
//					DatosDavivienda.RISKMOTOR.set_CtaOrigenODestMIGRADA(cuentaHomologada);
//
//				}
//
//				if (this.estado.contains("rechazada") || this.estado.contains("denegado")
//						|| this.estado.contains("no")) {
//
//					DatosDavivienda.RISKMOTOR.setResultado("Declinada");
//
//				} else if (this.estado.contains("EXCEDE") || this.estado.contains("TOPE")
//						|| this.estado.contains("1480")) {
//
//					DatosDavivienda.RISKMOTOR.setResultado("Rechazado");
//
//				} else {
//					DatosDavivienda.RISKMOTOR.setResultado("Exitosa");
//				}
//
//				if (this.estado.contains("rechazada") || this.estado.contains("no pudo ser realizada")) {
//					DatosDavivienda.RISKMOTOR.setTemporalResultado("Declinada");
//				} else if (this.estado.contains("TX EXCEDE TOPE SALDO")) {
//					DatosDavivienda.RISKMOTOR.setTemporalResultado("Rechazado");
//				}
//
//			} else {
//
//				Reporter.reportEvent(Reporter.MIC_FAIL,"Se esperaba transaccion exitosa y el estado fue [" + this.estadoTx + "]");
//
//				DatosDavivienda.RISKMOTOR.setTemporalObservacion(servicio);
//				DatosDavivienda.RISKMOTOR.setTemporalTime(pageConf.getFechaHoraTx());
//				DatosDavivienda.RISKMOTOR.setTime();
//				DatosDavivienda.RISKMOTOR.setResultado("Declinada");
//
//				if (activityAmount != null)
//					DatosDavivienda.RISKMOTOR.setMonto(activityAmount);
//
////				if (activityAmount2 != null)
////					DatosDavivienda.RISKMOTOR.setMonto2(activityAmount2);
//
//				// Datos Cuenta Origen
//
//				if (!this.servicio.equals("Tx Internacionales Recibir desde el exterior")) {
//					DatosDavivienda.RISKMOTOR.setCuentaOrigen(this.TipoProd(this.tipoCta));
//					DatosDavivienda.RISKMOTOR.set_CtaOrigenODestMIGRADA(this.numCta);
//				}
//
//				if (this.servicio.equals("Tx Internacionales Recibir desde el exterior")) {
//
//					DatosDavivienda.RISKMOTOR.setCuentaOrigen("Cuenta en el extranjero");
//					DatosDavivienda.RISKMOTOR.set_CtaOrigenODestMIGRADA("N/A");
//				}
////				DatosDavivienda.RISKMOTOR.setTipCuentaOrigen(this.tipoCta,this.numCta);
//
//				// Datos Cuenta destino
//				if (this.bancoDesMotor != null) {
//					if (!this.bancoDesMotor.equals(" ")) {
//						if (this.bancoDesMotor.contains("Davivienda") || this.bancoDesMotor.contains("DAVIVIENDA")
//								|| this.bancoDesMotor.contains("Daviplata")) {
//
//							DatosDavivienda.RISKMOTOR.setCuentaDestino(this.TipoProd(this.referencia1));
//
//						} else if (this.bancoDesMotor != null || !this.bancoDesMotor.isEmpty()
//								|| !this.bancoDesMotor.equals(" ")
//										&& (!this.bancoDesMotor.contains("Davivienda")
//												|| !this.bancoDesMotor.contains("DAVIVIENDA"))
//										&& (!this.bancoDesMotor.contains("Daviplata")
//												|| !this.bancoDesMotor.contains("DAVIPLATA"))) {
////							DatosDavivienda.RISKMOTOR.setTransaccion("Transferencias a monederos");
//							DatosDavivienda.RISKMOTOR.setCuentaDestino("Monedero otro Banco");
//						}
//					}
//				}
//
//				if (this.servicio.contains("Servicios")) {
//
//					if (DXCUtil.isNumeric(this.referencia1)) {
//						if (referencia2.isEmpty()) {
////							DatosDavivienda.RISKMOTOR.setReferencia_1(this.referencia1);
//							Reporter.write(this.referencia1);
//						} else {
////							DatosDavivienda.RISKMOTOR.setReferencia_1(this.referencia1, this.referencia2);
//							Reporter.write(this.referencia1+" - "+ this.referencia2);
//						}
//
//					} else {
//						Reporter.write(this.referencia2);
////						DatosDavivienda.RISKMOTOR.setReferencia_1(this.referencia2);
//					}
//
//					DatosDavivienda.RISKMOTOR.setCuentaDestino("Cuenta Corriente");
//
//				} else {
//
//					DatosDavivienda.RISKMOTOR.setCuentaDestino(this.TipoProd(this.referencia1));
//
//					if (this.servicio.equals("Tx Internacionales Recibir desde el exterior")) {
//
//						DatosDavivienda.RISKMOTOR.setTransaccion("Recibir Dinero del Exterior");
//						DatosDavivienda.RISKMOTOR.setCuentaDestino(this.referencia1);
//						DatosDavivienda.RISKMOTOR.setObservacion("Tipo Cta Destino " + this.referencia2);
//
//					}
//
//					if (this.servicio.equals("Tx Internacionales Enviar al exterior")) {
//
//						DatosDavivienda.RISKMOTOR.setTransaccion("Enviar Dinero al Exterior");
//						DatosDavivienda.RISKMOTOR.setCuentaDestino("Cuenta en el extranjero");
//						DatosDavivienda.RISKMOTOR.setObservacion("");
//
//					}
//
//					if (this.servicio.contains("Cuenta No Inscrita") || this.servicio.contains("Mismo NIT")
//							|| this.servicio.contains("Cuenta Inscrita")) {
//
//						DatosDavivienda.RISKMOTOR.setCuentaDestino(this.TipoProd(this.referencia1));
//
//						if (this.referencia1.contains("DEP�SITOS ELECTRONICOS")
//								|| this.referencia1.contains("DEP�SITOS ELECTR�NICOS")
//								|| this.referencia1.contains("Dep�sitos electronicos"))
//							DatosDavivienda.RISKMOTOR.setCuentaDestino("Monedero otro Banco");
//						DatosDavivienda.RISKMOTOR.setTransaccion("Transferencias a monederos");
//					}
//
//				}
//
//				// Bucar Cuenta Migrada
//				if (!this.servicio.equals("Tx Internacionales Recibir desde el exterior")) {
//
//					if (this.servicio.contains("N�mi") || this.servicio.contains("Proveedores")
//							|| this.servicio.contains("Proveedores") || this.servicio.contains("AFC")
//							|| this.servicio.contains("Pago a Cr�ditos de Terceros")
//							|| this.servicio.contains("Pagos a cr�ditos de terceros")
//							|| this.servicio.contains("Cr�dito.3ros")
//							|| this.servicio.contains("Transferencias Mismo NIT")
//							|| this.servicio.contains("Transferencia NIT Propio") || this.servicio.contains("Mismo NIT")
//							|| this.servicio.contains("Transferencias Cuenta Inscrita")
//							|| this.servicio.contains("Cuenta Inscrita")
//							|| this.servicio.contains("Transferencias Cuenta No Inscrita")
//							|| this.servicio.contains("Transferencias Cuenta NO Inscrita")
//							|| this.servicio.contains("Cuenta No Inscrita")) {
//
//						if (controlDest == null)
//							controlDest = new ControllerDestinosMasivos(pageDestino);
//
//						String cuentaHomologada = controlDest.BuscarCuentasMigrada(this.TipoProd(this.tipoCta),this.numCta);
//
//						DatosDavivienda.RISKMOTOR.set_CtaOrigenODestMIGRADA(cuentaHomologada);
//					}
//
//				} else {
//
//					if (this.referencia1.contains("DEP�SITOS ELECTRONICOS")
//							|| this.referencia1.contains("DEP�SITOS ELECTR�NICOS")
//							|| this.referencia1.contains("Dep�sitos electronicos"))
//
//						this.referencia1 = "DEP�SITOS ELECTRONICOS";
//
//
//					if (controlDest == null)
//						controlDest = new ControllerDestinosMasivos(pageDestino);
//
//					String cuentaHomologada = controlDest.BuscarCuentasMigrada(this.TipoProd(this.tipoCta),this.numCta);
//
//					DatosDavivienda.RISKMOTOR.set_CtaOrigenODestMIGRADA(cuentaHomologada);
//
//				}
//
//			}
//
//			break;
//		// -----------------------------------------------------------------------------------------------------------------------
//		default:
//			Reporter.reportEvent(Reporter.MIC_FAIL, "Riesgo [" + riesgo + "] NO contemplado");
//			break;
//		}
//	}

	/**
	 * Para el caso de Motor de Riesgo, dependiendo de la respuesta dada por el
	 * intento de confirmar la transacci�n, se eval�a, seg�n el riesgo del cliente
	 * de la iteraci�n actual, la adici�n o no del registro actual.
	 */

	public void validarIngresoMR() throws Exception {

		DatosDavivienda.RISKMOTOR.adicionarRegistro();
		// SETEA LOS REGISTROS QUE SON GUARDADOS DE MANERA TEMPORAL
//		if (tx.equals("Login")) {
		Date fechaHoraLogMR = new Date();
		DatosDavivienda.RISKMOTOR.setTemporalTime(fechaHoraLogMR);
		DatosDavivienda.RISKMOTOR.setTime();
//		}
		DatosDavivienda.RISKMOTOR.setMonto();
		DatosDavivienda.RISKMOTOR.setNumeroTx();
		DatosDavivienda.RISKMOTOR.setResultado();
		DatosDavivienda.RISKMOTOR.setObservacion();
		DatosDavivienda.RISKMOTOR.setObservacion(this.desdeElDetalle);

		String uuid = SettingsRun.getTestData().getParameter("Hash").trim();
		DatosDavivienda.RISKMOTOR.setHash(uuid);
		// Obtener el User-Agent

//		DatosDavivienda.RISKMOTOR.setUserAgent(this.userAgent);

//-----------------------------------------------------------------------------------------------------------------------

		this.servicio = SettingsRun.getTestData().getParameter("Servicio").trim();
		DatosDavivienda.RISKMOTOR.setTransaccion(this.getTransaccion(this.tipoPrueba, this.servicio));

		String userType = "Token"; // VALOR POR DEFECTO
		if (DatosEmpresarial.TIPO_TOKEN.equals(DatosEmpresarial.TOKEN_OTP))
			userType = "Virtual";

		String intentos = SettingsRun.getTestData().getParameter("Ingresos Fallidos").trim();
		int intentosFall = 0; // VALOR POR DEFECTO
		if (DXCUtil.isInteger(intentos))
			intentosFall = Integer.valueOf(intentos);
//-----------------------------------------------------------------------------------------------------------------------
		// SETEA LOS DATOS FALTANTES
		DatosDavivienda.RISKMOTOR.setUserType(userType);
		DatosDavivienda.RISKMOTOR.setRisk(riesgoBc);
		if (!this.servicio.equals("Tx Internacionales Recibir desde el exterior")
				&& !this.servicio.equals("Tx Internacionales Enviar al exterior"))
			DatosDavivienda.RISKMOTOR.setRiskEFM(riesgoEfm);

//		if (transaccion.contains("Transferencias mismo nit")) {
//			DatosDavivienda.RISKMOTOR.setIndicadorMismoNit("SI");
//		} else {
//			DatosDavivienda.RISKMOTOR.setIndicadorMismoNit("NO");
//		}

		DatosDavivienda.RISKMOTOR.setNumRetosFallidos(intentosFall);
		// System.out.println(this.ipPublica);
//		DatosDavivienda.RISKMOTOR.setDireccionIp(this.ipPublica);
		DatosDavivienda.RISKMOTOR.setNavegador_SistemaOperativo(this.navegador);
		DatosDavivienda.RISKMOTOR.setCliente(DatosEmpresarial.CLI_EMPRESAR);
		DatosDavivienda.RISKMOTOR.setDocumento(DatosEmpresarial.TIPO_ID_LOGUEO, DatosEmpresarial.NUM_ID_LOGUEO);

		if (!this.servicio.contains("N�mina") || !this.servicio.contains("Pago a Proveedores")
				|| !this.servicio.contains("AFC") || !this.servicio.contains("Cr�dito.3ros")) {

			DatosDavivienda.RISKMOTOR.setTemporalMonto(this.montoReal);
		}

		if (!this.tipoPrueba.equals(TP_LOGIN))
			this.transaccion = this.getTransaccion(this.tipoPrueba, this.servicio);
		DatosDavivienda.RISKMOTOR.setTransaccion(this.transaccion);

		DatosDavivienda.RISKMOTOR.setHash(uuid);

		if (this.numeroTx != null)
			DatosDavivienda.RISKMOTOR.setNumeroTx(this.numeroTx);

		if (this.desdeElDetalle.contains("SI") && this.tipoPrueba.equals("Tx Pend Aprobaci�n"))
			this.desdeElDetalle = "Aprobado dentro del detalle";
		else if (this.desdeElDetalle.contains("NO") && this.tipoPrueba.equals("Tx Pend Aprobaci�n"))
			this.desdeElDetalle = "Aprobado furea del detalle";

		DatosDavivienda.RISKMOTOR.setObservacion(this.desdeElDetalle);

		if (this.descripcionsin != null || !this.descripcionsin.isEmpty() || !this.descripcionsin.equals(" "))
			DatosDavivienda.RISKMOTOR.setObservacion(this.descripcionsin + " - " + this.desdeElDetalle);

		String riesgoBc = SettingsRun.getTestData().getParameter("Nivel de Riesgo BC").trim();
		String riesgoEfm = SettingsRun.getTestData().getParameter("Nivel de Riesgo SAS EFM").trim();

		DatosDavivienda.RISKMOTOR.setRisk(riesgoBc);
		if (!this.servicio.equals("Tx Internacionales Recibir desde el exterior")
				&& !this.servicio.equals("Tx Internacionales Enviar al exterior"))
			DatosDavivienda.RISKMOTOR.setRiskEFM(riesgoEfm);

		// Nivel de riesgo Motor
		this.riesgo = this.prioridaRiesgo;

		switch (this.riesgo.toUpperCase()) {
		// -----------------------------------------------------------------------------------------------------------------------
		case MotorRiesgo.RISK_ALTO:
			if (this.estadoTx.equals("DECLINADA")) {

				DatosDavivienda.RISKMOTOR.setTemporalResultado(MotorRiesgo.RESULT_SUCCESS);

				DatosDavivienda.RISKMOTOR.setTemporalObservacion(servicio);

				DatosDavivienda.RISKMOTOR.setTemporalTime(pageConf.getFechaHoraTx());

				DatosDavivienda.RISKMOTOR.setTime();

				if (activityAmount != null)
					DatosDavivienda.RISKMOTOR.setMonto(activityAmount);

//				if (activityAmount2 != null)
//					DatosDavivienda.RISKMOTOR.setMonto(activityAmount2);

//				DatosDavivienda.RISKMOTOR.setTipoIdEmpresa(this.tipoIdEm);
				DatosDavivienda.RISKMOTOR.setNumIdEmpresa(this.nitEmpre);

				// Datos Cuenta Origen

				if (!this.servicio.equals("Tx Internacionales Recibir desde el exterior")) {
					DatosDavivienda.RISKMOTOR.setCuentaOrigen(this.TipoProd(this.tipoCta));
					DatosDavivienda.RISKMOTOR.setNumCuentaOrigen(this.numCta);
				}

				if (this.servicio.equals("Tx Internacionales Recibir desde el exterior")) {

					DatosDavivienda.RISKMOTOR.setCuentaOrigen("Cuenta en el extranjero");
					DatosDavivienda.RISKMOTOR.setNumCuentaOrigen("N/A");
				}
//				DatosDavivienda.RISKMOTOR.setTipCuentaOrigen(this.tipoCta,this.numCta);

				// Datos Cuenta destino
				if (this.bancoDesMotor != null) {
					if (!this.bancoDesMotor.equals(" ")) {
						if (this.bancoDesMotor.contains("Davivienda") || this.bancoDesMotor.contains("DAVIVIENDA")
								|| this.bancoDesMotor.contains("Daviplata")) {

							DatosDavivienda.RISKMOTOR.setCuentaDestino(this.TipoProd(this.referencia1));

						} else if (this.bancoDesMotor != null || !this.bancoDesMotor.isEmpty()
								|| !this.bancoDesMotor.equals(" ")
										&& (!this.bancoDesMotor.contains("Davivienda")
												|| !this.bancoDesMotor.contains("DAVIVIENDA"))
										&& (!this.bancoDesMotor.contains("Daviplata")
												|| !this.bancoDesMotor.contains("DAVIPLATA"))) {
//							DatosDavivienda.RISKMOTOR.setTransaccion("Transferencias a monederos");
							DatosDavivienda.RISKMOTOR.setCuentaDestino("Monedero otro Banco");
						}
					}
				}

				if (this.servicio.contains("Servicios")) {

					if (DXCUtil.isNumeric(this.referencia1)) {
						if (referencia2.isEmpty()) {
							DatosDavivienda.RISKMOTOR.setReferencia_1(this.referencia1);
						} else {
							DatosDavivienda.RISKMOTOR.setReferencia_1(this.referencia1, this.referencia2);
						}

					} else {

						DatosDavivienda.RISKMOTOR.setReferencia_1(this.referencia2);
					}

					DatosDavivienda.RISKMOTOR.setNumCuentaDestino("No Aplica");
					DatosDavivienda.RISKMOTOR.setCuentaDestino("Cuenta Corriente");

				} else {

					DatosDavivienda.RISKMOTOR.setCuentaDestino(this.TipoProd(this.referencia1));
					DatosDavivienda.RISKMOTOR.setReferencia_1("N/A");

					if (this.servicio.equals("Tx Internacionales Recibir desde el exterior")) {

						DatosDavivienda.RISKMOTOR.setTransaccion("Recibir Dinero del Exterior");
						DatosDavivienda.RISKMOTOR.setCuentaDestino(this.referencia1);
						DatosDavivienda.RISKMOTOR.setObservacion("Tipo Cta Destino " + this.referencia2);

					}

					if (this.servicio.equals("Tx Internacionales Enviar al exterior")) {

						DatosDavivienda.RISKMOTOR.setTransaccion("Enviar Dinero al Exterior");
						DatosDavivienda.RISKMOTOR.setCuentaDestino("Cuenta en el extranjero");
						DatosDavivienda.RISKMOTOR.setObservacion("");

					}

					if (this.servicio.contains("Cuenta No Inscrita") || this.servicio.contains("Mismo NIT")
							|| this.servicio.contains("Cuenta Inscrita")) {

						DatosDavivienda.RISKMOTOR.setNumCuentaDestino(this.referencia2);
						DatosDavivienda.RISKMOTOR.setCuentaDestino(this.TipoProd(this.referencia1));

						if (this.referencia1.contains("DEP�SITOS ELECTRONICOS")|| this.referencia1.contains("DEP�SITOS ELECTR�NICOS")|| this.referencia1.contains("Dep�sitos electronicos")) {
							DatosDavivienda.RISKMOTOR.setCuentaDestino("Monedero otro Banco");
							DatosDavivienda.RISKMOTOR.setTransaccion("Transferencias a monederos");
						}
					}

				}

				// Bucar Cuenta Migrada
				if (!this.servicio.equals("Tx Internacionales Recibir desde el exterior")) {

					if (this.servicio.contains("N�mi") || this.servicio.contains("Proveedores")
							|| this.servicio.contains("Proveedores") || this.servicio.contains("AFC")
							|| this.servicio.contains("Pago a Cr�ditos de Terceros")
							|| this.servicio.contains("Pagos a cr�ditos de terceros")
							|| this.servicio.contains("Cr�dito.3ros")
							|| this.servicio.contains("Transferencias Mismo NIT")
							|| this.servicio.contains("Transferencia NIT Propio") || this.servicio.contains("Mismo NIT")
							|| this.servicio.contains("Transferencias Cuenta Inscrita")
							|| this.servicio.contains("Cuenta Inscrita")
							|| this.servicio.contains("Transferencias Cuenta No Inscrita")
							|| this.servicio.contains("Transferencias Cuenta NO Inscrita")
							|| this.servicio.contains("Cuenta No Inscrita")) {

						if (controlDest == null)
							controlDest = new ControllerDestinosMasivos(pageDestino);

						String cuentaHomologada = controlDest.BuscarCuentasMigrada(this.TipoProd(this.tipoCta),
								this.numCta);

						DatosDavivienda.RISKMOTOR.setNumCuentaOrigenMigrada(cuentaHomologada);
					}

				} else {
					if (this.referencia1.contains("DEP�SITOS ELECTRONICOS")
							|| this.referencia1.contains("DEP�SITOS ELECTR�NICOS")
							|| this.referencia1.contains("Dep�sitos electronicos"))

						this.referencia1 = "DEP�SITOS ELECTRONICOS";

					DatosDavivienda.RISKMOTOR.setNumCuentaDestino(this.referencia2);

					if (controlDest == null)
						controlDest = new ControllerDestinosMasivos(pageDestino);

					String cuentaHomologada = controlDest.BuscarCuentasMigrada(this.TipoProd(this.referencia1),
							this.referencia2);

					DatosDavivienda.RISKMOTOR.setNumCuentaOrigenMigrada(cuentaHomologada);

				}

				// Dato de Esto de la transacion

				DatosDavivienda.RISKMOTOR.setCausaRechazo(this.estado);

				if (this.estado.contains("rechazada") || this.estado.contains("denegado")
						|| this.estado.contains("no")) {

					DatosDavivienda.RISKMOTOR.setResultado("Declinada");
					DatosDavivienda.RISKMOTOR.setCausaRechazo(" ");

				} else if (this.estado.contains("EXCEDE") || this.estado.contains("TOPE")
						|| this.estado.contains("1480")) {

					DatosDavivienda.RISKMOTOR.setResultado("Rechazado");
					DatosDavivienda.RISKMOTOR.setCausaRechazo(this.estado);
				} else {
					DatosDavivienda.RISKMOTOR.setResultado("Exitosa");
					DatosDavivienda.RISKMOTOR.setCausaRechazo(this.estado);
				}

				if (this.estado.contains("rechazada") || this.estado.contains("no pudo ser realizada")) {

					DatosDavivienda.RISKMOTOR.setTemporalResultado("Declinada");

				} else if (this.estado.contains("TX EXCEDE TOPE SALDO")) {

					DatosDavivienda.RISKMOTOR.setTemporalResultado("Rechazado");
				}

			} else {

				// Informacion Motor de Riegos Alto si Se esperaba declinaci�n y el mensaje fue
				// diferente

				Reporter.reportEvent(Reporter.MIC_FAIL,
						"Se esperaba declinaci�n y el mensaje fue [" + this.estadoTx + "]");

				DatosDavivienda.RISKMOTOR.setTemporalObservacion(servicio);
				DatosDavivienda.RISKMOTOR.setTemporalTime(pageConf.getFechaHoraTx());
				DatosDavivienda.RISKMOTOR.setTime();
//				DatosDavivienda.RISKMOTOR.setNumeroTx(this.numAprova);
				DatosDavivienda.RISKMOTOR.setCausaRechazo(this.estado);
//				DatosDavivienda.RISKMOTOR.setTipoIdEmpresa(this.tipoIdEm);
				DatosDavivienda.RISKMOTOR.setNumIdEmpresa(this.nitEmpre);

				if (activityAmount != null)
					DatosDavivienda.RISKMOTOR.setMonto(activityAmount);

//				if (activityAmount2 != null)
//					DatosDavivienda.RISKMOTOR.setMonto(activityAmount2);

				// Datos Cuenta Origen

				if (!this.servicio.equals("Tx Internacionales Recibir desde el exterior")) {
					DatosDavivienda.RISKMOTOR.setCuentaOrigen(this.TipoProd(this.tipoCta));
					DatosDavivienda.RISKMOTOR.setNumCuentaOrigen(this.numCta);
				}

				if (this.servicio.equals("Tx Internacionales Recibir desde el exterior")) {

					DatosDavivienda.RISKMOTOR.setCuentaOrigen("Cuenta en el extranjero");
					DatosDavivienda.RISKMOTOR.setNumCuentaOrigen("N/A");
				}
//				DatosDavivienda.RISKMOTOR.setTipCuentaOrigen(this.tipoCta,this.numCta);

				// Datos Cuenta destino
				if (this.bancoDesMotor != null) {
					if (!this.bancoDesMotor.equals(" ")) {
						if (this.bancoDesMotor.contains("Davivienda") || this.bancoDesMotor.contains("DAVIVIENDA")
								|| this.bancoDesMotor.contains("Daviplata")) {

							DatosDavivienda.RISKMOTOR.setCuentaDestino(this.TipoProd(this.referencia1));

						} else if (this.bancoDesMotor != null || !this.bancoDesMotor.isEmpty()|| !this.bancoDesMotor.equals(" ")&& (!this.bancoDesMotor.contains("Davivienda")|| !this.bancoDesMotor.contains("DAVIVIENDA"))&& (!this.bancoDesMotor.contains("Daviplata")|| !this.bancoDesMotor.contains("DAVIPLATA"))) {
//							DatosDavivienda.RISKMOTOR.setTransaccion("Transferencias a monederos");
							DatosDavivienda.RISKMOTOR.setCuentaDestino("Monedero otro Banco");
						}
					}
				}

				if (this.servicio.contains("Servicios")) {

					if (DXCUtil.isNumeric(this.referencia1)) {
						if (referencia2.isEmpty()) {
							DatosDavivienda.RISKMOTOR.setReferencia_1(this.referencia1);
						} else {
							DatosDavivienda.RISKMOTOR.setReferencia_1(this.referencia1, this.referencia2);
						}

					} else {

						DatosDavivienda.RISKMOTOR.setReferencia_1(this.referencia2);
					}

					DatosDavivienda.RISKMOTOR.setNumCuentaDestino("No Aplica");
					DatosDavivienda.RISKMOTOR.setCuentaDestino("Cuenta Corriente");

				} else {

					DatosDavivienda.RISKMOTOR.setCuentaDestino(this.TipoProd(this.referencia1));
					DatosDavivienda.RISKMOTOR.setReferencia_1("N/A");

					if (this.servicio.equals("Tx Internacionales Recibir desde el exterior")) {

						DatosDavivienda.RISKMOTOR.setTransaccion("Recibir Dinero del Exterior");
						DatosDavivienda.RISKMOTOR.setCuentaDestino(this.referencia1);
						DatosDavivienda.RISKMOTOR.setObservacion("Tipo Cta Destino " + this.referencia2);

					}

					if (this.servicio.equals("Tx Internacionales Enviar al exterior")) {

						DatosDavivienda.RISKMOTOR.setTransaccion("Enviar Dinero al Exterior");
						DatosDavivienda.RISKMOTOR.setCuentaDestino("Cuenta en el extranjero");
						DatosDavivienda.RISKMOTOR.setObservacion("");

					}

					if (this.servicio.contains("Cuenta No Inscrita") || this.servicio.contains("Mismo NIT")|| this.servicio.contains("Cuenta Inscrita")) {

						DatosDavivienda.RISKMOTOR.setNumCuentaDestino(this.referencia2);
						DatosDavivienda.RISKMOTOR.setCuentaDestino(this.TipoProd(this.referencia1));

						if (this.referencia1.contains("DEP�SITOS ELECTRONICOS")|| this.referencia1.contains("DEP�SITOS ELECTR�NICOS")|| this.referencia1.contains("Dep�sitos electronicos")) {
							DatosDavivienda.RISKMOTOR.setCuentaDestino("Monedero otro Banco");
						DatosDavivienda.RISKMOTOR.setTransaccion("Transferencias a monederos");
						}
					}

				}

				// Bucar Cuenta Migrada
				if (!this.servicio.equals("Tx Internacionales Recibir desde el exterior")) {

					if (this.servicio.contains("N�mi") || this.servicio.contains("Proveedores")
							|| this.servicio.contains("Proveedores") || this.servicio.contains("AFC")
							|| this.servicio.contains("Pago a Cr�ditos de Terceros")
							|| this.servicio.contains("Pagos a cr�ditos de terceros")
							|| this.servicio.contains("Cr�dito.3ros")
							|| this.servicio.contains("Transferencias Mismo NIT")
							|| this.servicio.contains("Transferencia NIT Propio") || this.servicio.contains("Mismo NIT")
							|| this.servicio.contains("Transferencias Cuenta Inscrita")
							|| this.servicio.contains("Cuenta Inscrita")
							|| this.servicio.contains("Transferencias Cuenta No Inscrita")
							|| this.servicio.contains("Transferencias Cuenta NO Inscrita")
							|| this.servicio.contains("Cuenta No Inscrita")) {

						if (controlDest == null)
							controlDest = new ControllerDestinosMasivos(pageDestino);

						String cuentaHomologada = controlDest.BuscarCuentasMigrada(this.TipoProd(this.tipoCta),
								this.numCta);

						DatosDavivienda.RISKMOTOR.setNumCuentaOrigenMigrada(cuentaHomologada);
					}

				} else {

					if (this.referencia1.contains("DEP�SITOS ELECTRONICOS")
							|| this.referencia1.contains("DEP�SITOS ELECTR�NICOS")
							|| this.referencia1.contains("Dep�sitos electronicos"))

						this.referencia1 = "DEP�SITOS ELECTRONICOS";

					DatosDavivienda.RISKMOTOR.setNumCuentaDestino(this.referencia2);

					if (controlDest == null)
						controlDest = new ControllerDestinosMasivos(pageDestino);

					String cuentaHomologada = controlDest.BuscarCuentasMigrada(this.TipoProd(this.referencia1),
							this.referencia2);

					DatosDavivienda.RISKMOTOR.setNumCuentaOrigenMigrada(cuentaHomologada);

				}

				// Estado de la Tx
				DatosDavivienda.RISKMOTOR.setResultado("Exitosa");

				if (this.estado.contains("rechazada") || this.estado.contains("no pudo ser realizada")) {
					DatosDavivienda.RISKMOTOR.setResultado("Declinada");
				} else if (this.estado.contains("TX EXCEDE TOPE SALDO")) {
					DatosDavivienda.RISKMOTOR.setResultado("Rechazado");
				}

			}

			break;
		// -----------------------------------------------------------------------------------------------------------------------
		case MotorRiesgo.RISK_MEDIO:
		case MotorRiesgo.RISK_BAJO:

			// if (this.isTxExitosa(msgRespuesta)) {
			if (DXCUtil.itemInArray(this.estadoTx, arrEstadosTxAprob)) {

				DatosDavivienda.RISKMOTOR.setTemporalResultado(MotorRiesgo.RESULT_SUCCESS);
				DatosDavivienda.RISKMOTOR.setTemporalObservacion(servicio);
				DatosDavivienda.RISKMOTOR.setTemporalTime(pageConf.getFechaHoraTx());
				DatosDavivienda.RISKMOTOR.setTime();
//				DatosDavivienda.RISKMOTOR.setTipoIdEmpresa(this.tipoIdEm);
				DatosDavivienda.RISKMOTOR.setNumIdEmpresa(this.nitEmpre);

//				DatosDavivienda.RISKMOTOR.setTipCuentaDestino(tipoCtaDest);
//				DatosDavivienda.RISKMOTOR.setNumeroTx(this.numAprova);

				if (activityAmount != null)
					DatosDavivienda.RISKMOTOR.setMonto(activityAmount);

//				if (activityAmount2 != null)
//					DatosDavivienda.RISKMOTOR.setMonto2(activityAmount2);

				// Datos Cuenta Origen

				if (!this.servicio.equals("Tx Internacionales Recibir desde el exterior")) {
					DatosDavivienda.RISKMOTOR.setCuentaOrigen(this.TipoProd(this.tipoCta));
					DatosDavivienda.RISKMOTOR.setNumCuentaOrigen(this.numCta);
				}

				if (this.servicio.equals("Tx Internacionales Recibir desde el exterior")) {

					DatosDavivienda.RISKMOTOR.setCuentaOrigen("Cuenta en el extranjero");
					DatosDavivienda.RISKMOTOR.setNumCuentaOrigen("N/A");
				}
//				DatosDavivienda.RISKMOTOR.setTipCuentaOrigen(this.tipoCta,this.numCta);

				// Datos Cuenta destino
				if (this.bancoDesMotor != null) {
					if (!this.bancoDesMotor.equals(" ")) {
						if (this.bancoDesMotor.contains("Davivienda") || this.bancoDesMotor.contains("DAVIVIENDA")
								|| this.bancoDesMotor.contains("Daviplata")) {

							DatosDavivienda.RISKMOTOR.setCuentaDestino(this.TipoProd(this.referencia1));

						} else if (this.bancoDesMotor != null || !this.bancoDesMotor.isEmpty()
								|| !this.bancoDesMotor.equals(" ")
										&& (!this.bancoDesMotor.contains("Davivienda")
												|| !this.bancoDesMotor.contains("DAVIVIENDA"))
										&& (!this.bancoDesMotor.contains("Daviplata")
												|| !this.bancoDesMotor.contains("DAVIPLATA"))) {
//							DatosDavivienda.RISKMOTOR.setTransaccion("Transferencias a monederos");
							DatosDavivienda.RISKMOTOR.setCuentaDestino("Monedero otro Banco");
						}
					}
				}

				if (this.servicio.contains("Servicios")) {

					if (DXCUtil.isNumeric(this.referencia1)) {
						if (referencia2.isEmpty()) {
							DatosDavivienda.RISKMOTOR.setReferencia_1(this.referencia1);
						} else {
							DatosDavivienda.RISKMOTOR.setReferencia_1(this.referencia1, this.referencia2);
						}

					} else {

						DatosDavivienda.RISKMOTOR.setReferencia_1(this.referencia2);
					}

					DatosDavivienda.RISKMOTOR.setNumCuentaDestino("No Aplica");
					DatosDavivienda.RISKMOTOR.setCuentaDestino("Cuenta Corriente");

				} else {

					DatosDavivienda.RISKMOTOR.setCuentaDestino(this.TipoProd(this.referencia1));
					DatosDavivienda.RISKMOTOR.setReferencia_1("N/A");

					if (this.servicio.equals("Tx Internacionales Recibir desde el exterior")) {

						DatosDavivienda.RISKMOTOR.setTransaccion("Recibir Dinero del Exterior");
						DatosDavivienda.RISKMOTOR.setCuentaDestino(this.referencia1);
						DatosDavivienda.RISKMOTOR.setObservacion("Tipo Cta Destino " + this.referencia2);

					}

					if (this.servicio.equals("Tx Internacionales Enviar al exterior")) {

						DatosDavivienda.RISKMOTOR.setTransaccion("Enviar Dinero al Exterior");
						DatosDavivienda.RISKMOTOR.setCuentaDestino("Cuenta en el extranjero");
						DatosDavivienda.RISKMOTOR.setObservacion("");

					}

					if (this.servicio.contains("Cuenta No Inscrita") || this.servicio.contains("Mismo NIT")
							|| this.servicio.contains("Cuenta Inscrita")) {

						DatosDavivienda.RISKMOTOR.setNumCuentaDestino(this.referencia2);
						DatosDavivienda.RISKMOTOR.setCuentaDestino(this.TipoProd(this.referencia1));

						if (this.referencia1.contains("DEP�SITOS ELECTRONICOS")|| this.referencia1.contains("DEP�SITOS ELECTR�NICOS")|| this.referencia1.contains("Dep�sitos electronicos")) {
							
							DatosDavivienda.RISKMOTOR.setCuentaDestino("Monedero otro Banco");
							DatosDavivienda.RISKMOTOR.setTransaccion("Transferencias a monederos");
						}
					}

				}

				// Bucar Cuenta Migrada
				if (!this.servicio.equals("Tx Internacionales Recibir desde el exterior")) {

					if (this.servicio.contains("N�mi") || this.servicio.contains("Proveedores")
							|| this.servicio.contains("Proveedores") || this.servicio.contains("AFC")
							|| this.servicio.contains("Pago a Cr�ditos de Terceros")
							|| this.servicio.contains("Pagos a cr�ditos de terceros")
							|| this.servicio.contains("Cr�dito.3ros")
							|| this.servicio.contains("Transferencias Mismo NIT")
							|| this.servicio.contains("Transferencia NIT Propio") || this.servicio.contains("Mismo NIT")
							|| this.servicio.contains("Transferencias Cuenta Inscrita")
							|| this.servicio.contains("Cuenta Inscrita")
							|| this.servicio.contains("Transferencias Cuenta No Inscrita")
							|| this.servicio.contains("Transferencias Cuenta NO Inscrita")
							|| this.servicio.contains("Cuenta No Inscrita")) {

						if (controlDest == null)
							controlDest = new ControllerDestinosMasivos(pageDestino);

						String cuentaHomologada = controlDest.BuscarCuentasMigrada(this.TipoProd(this.tipoCta),
								this.numCta);

						DatosDavivienda.RISKMOTOR.setNumCuentaOrigenMigrada(cuentaHomologada);
					}

				} else {

					if (this.referencia1.contains("DEP�SITOS ELECTRONICOS")
							|| this.referencia1.contains("DEP�SITOS ELECTR�NICOS")
							|| this.referencia1.contains("Dep�sitos electronicos"))

						this.referencia1 = "DEP�SITOS ELECTRONICOS";

					DatosDavivienda.RISKMOTOR.setNumCuentaDestino(this.referencia2);

					if (controlDest == null)
						controlDest = new ControllerDestinosMasivos(pageDestino);

					String cuentaHomologada = controlDest.BuscarCuentasMigrada(this.TipoProd(this.referencia1),
							this.referencia2);

					DatosDavivienda.RISKMOTOR.setNumCuentaOrigenMigrada(cuentaHomologada);

				}

				DatosDavivienda.RISKMOTOR.setCausaRechazo(this.estado);
				if (this.estado.contains("rechazada") || this.estado.contains("denegado")
						|| this.estado.contains("no")) {

					DatosDavivienda.RISKMOTOR.setResultado("Declinada");

				} else if (this.estado.contains("EXCEDE") || this.estado.contains("TOPE")
						|| this.estado.contains("1480")) {

					DatosDavivienda.RISKMOTOR.setResultado("Rechazado");

				} else {
					DatosDavivienda.RISKMOTOR.setResultado("Exitosa");
					DatosDavivienda.RISKMOTOR.setCausaRechazo(" ");
				}

				if (this.estado.contains("rechazada") || this.estado.contains("no pudo ser realizada")) {
					DatosDavivienda.RISKMOTOR.setTemporalResultado("Declinada");
				} else if (this.estado.contains("TX EXCEDE TOPE SALDO")) {
					DatosDavivienda.RISKMOTOR.setTemporalResultado("Rechazado");
				}

			} else {

				Reporter.reportEvent(Reporter.MIC_FAIL,
						"Se esperaba transaccion exitosa y el estado fue [" + this.estadoTx + "]");

				DatosDavivienda.RISKMOTOR.setTemporalObservacion(servicio);
				DatosDavivienda.RISKMOTOR.setTemporalTime(pageConf.getFechaHoraTx());
				DatosDavivienda.RISKMOTOR.setTime();
				DatosDavivienda.RISKMOTOR.setCausaRechazo(this.estado);
				DatosDavivienda.RISKMOTOR.setResultado("Declinada");
//				DatosDavivienda.RISKMOTOR.setTipoIdEmpresa(this.tipoIdEm);
				DatosDavivienda.RISKMOTOR.setNumIdEmpresa(this.nitEmpre);

				if (activityAmount != null)
					DatosDavivienda.RISKMOTOR.setMonto(activityAmount);

//				if (activityAmount2 != null)
//					DatosDavivienda.RISKMOTOR.setMonto2(activityAmount2);

				// Datos Cuenta Origen

				if (!this.servicio.equals("Tx Internacionales Recibir desde el exterior")) {
					DatosDavivienda.RISKMOTOR.setCuentaOrigen(this.TipoProd(this.tipoCta));
					DatosDavivienda.RISKMOTOR.setNumCuentaOrigen(this.numCta);
				}

				if (this.servicio.equals("Tx Internacionales Recibir desde el exterior")) {

					DatosDavivienda.RISKMOTOR.setCuentaOrigen("Cuenta en el extranjero");
					DatosDavivienda.RISKMOTOR.setNumCuentaOrigen("N/A");
				}
//				DatosDavivienda.RISKMOTOR.setTipCuentaOrigen(this.tipoCta,this.numCta);

				// Datos Cuenta destino
				if (this.bancoDesMotor != null) {
					if (!this.bancoDesMotor.equals(" ")) {
						if (this.bancoDesMotor.contains("Davivienda") || this.bancoDesMotor.contains("DAVIVIENDA")
								|| this.bancoDesMotor.contains("Daviplata")) {

							DatosDavivienda.RISKMOTOR.setCuentaDestino(this.TipoProd(this.referencia1));

						} else if (this.bancoDesMotor != null || !this.bancoDesMotor.isEmpty()
								|| !this.bancoDesMotor.equals(" ")
										&& (!this.bancoDesMotor.contains("Davivienda")
												|| !this.bancoDesMotor.contains("DAVIVIENDA"))
										&& (!this.bancoDesMotor.contains("Daviplata")
												|| !this.bancoDesMotor.contains("DAVIPLATA"))) {
//							DatosDavivienda.RISKMOTOR.setTransaccion("Transferencias a monederos");
							DatosDavivienda.RISKMOTOR.setCuentaDestino("Monedero otro Banco");
						}
					}
				}

				if (this.servicio.contains("Servicios")) {

					if (DXCUtil.isNumeric(this.referencia1)) {
						if (referencia2.isEmpty()) {
							DatosDavivienda.RISKMOTOR.setReferencia_1(this.referencia1);
						} else {
							DatosDavivienda.RISKMOTOR.setReferencia_1(this.referencia1, this.referencia2);
						}

					} else {

						DatosDavivienda.RISKMOTOR.setReferencia_1(this.referencia2);
					}

					DatosDavivienda.RISKMOTOR.setNumCuentaDestino("No Aplica");
					DatosDavivienda.RISKMOTOR.setCuentaDestino("Cuenta Corriente");

				} else {

					DatosDavivienda.RISKMOTOR.setCuentaDestino(this.TipoProd(this.referencia1));
					DatosDavivienda.RISKMOTOR.setReferencia_1("N/A");

					if (this.servicio.equals("Tx Internacionales Recibir desde el exterior")) {

						DatosDavivienda.RISKMOTOR.setTransaccion("Recibir Dinero del Exterior");
						DatosDavivienda.RISKMOTOR.setCuentaDestino(this.referencia1);
						DatosDavivienda.RISKMOTOR.setObservacion("Tipo Cta Destino " + this.referencia2);

					}

					if (this.servicio.equals("Tx Internacionales Enviar al exterior")) {

						DatosDavivienda.RISKMOTOR.setTransaccion("Enviar Dinero al Exterior");
						DatosDavivienda.RISKMOTOR.setCuentaDestino("Cuenta en el extranjero");
						DatosDavivienda.RISKMOTOR.setObservacion("");

					}

					if (this.servicio.contains("Cuenta No Inscrita") || this.servicio.contains("Mismo NIT")
							|| this.servicio.contains("Cuenta Inscrita")) {

						DatosDavivienda.RISKMOTOR.setNumCuentaDestino(this.referencia2);
						DatosDavivienda.RISKMOTOR.setCuentaDestino(this.TipoProd(this.referencia1));

						if (this.referencia1.contains("DEP�SITOS ELECTRONICOS")|| this.referencia1.contains("DEP�SITOS ELECTR�NICOS")|| this.referencia1.contains("Dep�sitos electronicos")) {
							
							DatosDavivienda.RISKMOTOR.setCuentaDestino("Monedero otro Banco");
							DatosDavivienda.RISKMOTOR.setTransaccion("Transferencias a monederos");
						}
					}

				}

				// Bucar Cuenta Migrada
				if (!this.servicio.equals("Tx Internacionales Recibir desde el exterior")) {

					if (this.servicio.contains("N�mi") || this.servicio.contains("Proveedores")
							|| this.servicio.contains("Proveedores") || this.servicio.contains("AFC")
							|| this.servicio.contains("Pago a Cr�ditos de Terceros")
							|| this.servicio.contains("Pagos a cr�ditos de terceros")
							|| this.servicio.contains("Cr�dito.3ros")
							|| this.servicio.contains("Transferencias Mismo NIT")
							|| this.servicio.contains("Transferencia NIT Propio") || this.servicio.contains("Mismo NIT")
							|| this.servicio.contains("Transferencias Cuenta Inscrita")
							|| this.servicio.contains("Cuenta Inscrita")
							|| this.servicio.contains("Transferencias Cuenta No Inscrita")
							|| this.servicio.contains("Transferencias Cuenta NO Inscrita")
							|| this.servicio.contains("Cuenta No Inscrita")) {

						if (controlDest == null)
							controlDest = new ControllerDestinosMasivos(pageDestino);

						String cuentaHomologada = controlDest.BuscarCuentasMigrada(this.TipoProd(this.tipoCta),
								this.numCta);

						DatosDavivienda.RISKMOTOR.setNumCuentaOrigenMigrada(cuentaHomologada);
					}

				} else {

					if (this.referencia1.contains("DEP�SITOS ELECTRONICOS")
							|| this.referencia1.contains("DEP�SITOS ELECTR�NICOS")
							|| this.referencia1.contains("Dep�sitos electronicos"))

						this.referencia1 = "DEP�SITOS ELECTRONICOS";

					DatosDavivienda.RISKMOTOR.setNumCuentaDestino(this.referencia2);

					if (controlDest == null)
						controlDest = new ControllerDestinosMasivos(pageDestino);

					String cuentaHomologada = controlDest.BuscarCuentasMigrada(this.TipoProd(this.referencia1),
							this.referencia2);

					DatosDavivienda.RISKMOTOR.setNumCuentaOrigenMigrada(cuentaHomologada);

				}

			}

			break;
		// -----------------------------------------------------------------------------------------------------------------------
		default:
			Reporter.reportEvent(Reporter.MIC_FAIL, "Riesgo [" + riesgo + "] NO contemplado");
			break;
		}
	}

// =======================================================================================================================

// ===========================================[TransferenciasInternacionales]===========================================================================

	/**
	 * Metodo retorna el tipo de producto
	 * 
	 * @param tipoProdUpper
	 * @return
	 */
	public String TipoProd(String tipoProdUpper) {
		String tipoProd = "No Aplica";

		if (tipoProdUpper.contains("AHORROS") || tipoProdUpper.contains("ahorros") || tipoProdUpper.contains("Ahorros")
				|| tipoProdUpper.contains("ahorro") || tipoProdUpper.contains("Ahorro"))
			tipoProd = "Cuenta Ahorros";
		else if (tipoProdUpper.contains("CORRIENTE") || tipoProdUpper.contains("corriente")
				|| tipoProdUpper.contains("Corriente"))
			tipoProd = "Cuenta Corriente";
		else if (tipoProdUpper.contains("CR�DITO") || tipoProdUpper.contains("Cr�dito")
				|| tipoProdUpper.contains("cr�dito") || tipoProdUpper.contains("CREDITO")
				|| tipoProdUpper.contains("Credito") || tipoProdUpper.contains("credito")) // CR�DIPLUS
			tipoProd = "Tarjeta Cr�dito";

		return tipoProd;
	}

// =======================================================================================================================

// ===========================================[TransferenciasInternacionales]===========================================================================
	/**
	 * Realiza la transacci�n de recibir dinero del exterior.<b>
	 * TransferenciasInternacionales
	 * 
	 * @throws Exception
	 */
	public void Recibirdinerodelexterior(boolean soloGuardar) throws Exception {
		NotificacionSMS.TIPO_NOTIFICACION = NotificacionSMS.TIPO_NOTIF_PYMES_TRANS_RECIBIR;

		String msg = "";

		String numCambiario1 = DXCUtil.left(SettingsRun.getTestData().getParameter("Numeral cambiario 1"), 4);
		String numCambiario2 = DXCUtil.left(SettingsRun.getTestData().getParameter("Numeral cambiario 2"), 4);

		String mensaje = "Los siguientes campos est�n vac�os: ";
		if (this.numerodereferenciaExterna.isEmpty()) {
			mensaje += "Numero de referencia Externa";
		}
		if (this.modena.isEmpty()) {
			mensaje += "Tipo Moneda, ";
		}
		if (this.concepTx.isEmpty()) {
			mensaje += "Concepto de la transferencia, ";
		}
		if (numCambiario1.isEmpty()) {
			mensaje += "Numeral cambiario 1, ";
		}
		if (numCambiario2.isEmpty()) {
			mensaje += "Numeral cambiario 2, ";
		}
		if (this.valorNumeral1.isEmpty()) {
			mensaje += "Valor numeral cambiario 1, ";
		}
		if (this.valorNumeral2.isEmpty()) {
			mensaje += "Valor numeral cambiario 2, ";
		}
		if (!mensaje.equals("Los siguientes campos est�n vac�os: ")) {
			// Quita la �ltima coma y espacio
			mensaje = mensaje.substring(0, mensaje.length() - 2);
			Reporter.reportEvent(Reporter.MIC_FAIL, mensaje);
		}

		this.inicioCrearTx(); // DEJA LA PANTALLA EN LA SELECCI�N DEL ORIGEN

		// Espera hasta que el iframe de divisas est� disponible
		if (this.pageRecibirTransferenciasInternacionales.switchToFrameDivisas()) {

			msg = pageRecibirTransferenciasInternacionales.seleccionarTransferencia("Recibir");// Se en carga de
																								// selecionar el modulo
																								// de Divisas

			if (msg != null && !msg.equals(""))
				this.terminarIteracionXError(msg);

			String msgError = pageRecibirTransferenciasInternacionales.TxInternacionalesOrigen(
					this.numerodereferenciaExterna, this.modena, this.concepTx, numCambiario1, numCambiario2,
					this.valorNumeral1, this.valorNumeral2);

			if (msgError != null) {

				this.terminarIteracionXError(msgError);
			}

			// Cuenta destino
			msgError = pageRecibirTransferenciasInternacionales.seleccionarCuenta(this.referencia1, this.referencia2);
			if (msgError != null) {
				this.terminarIteracionXError(msgError);
			}

			msgError = pageRecibirTransferenciasInternacionales.Cotizacion();
			if (msgError != null) {
				this.terminarIteracionXError(msgError);
			}

			msgError = pageRecibirTransferenciasInternacionales.Confirmacion();
			if (msgError != null) {
				this.terminarIteracionXError(msgError);
			}

			this.accionConfirmarInternacional(soloGuardar);

			if (!DatosDavivienda.IS_RISKMOTOR) {
				if (DatosDavivienda.STRATUS != null) {
					this.pageRecibirTransferenciasInternacionales.validacionSaldosStratus(this.referencia1,
							this.referencia2, false);
					String saldoIni = this.pageRecibirTransferenciasInternacionales.getSaldoTotalInicialOrigen();
					String saldodis = this.pageRecibirTransferenciasInternacionales.getSaldoDispoInicialOrigen();
					String saldoFin = this.pageRecibirTransferenciasInternacionales.getSaldoTotalFinalOrigen();
					String saldoDispoFin = this.pageRecibirTransferenciasInternacionales.getSaldoTotalFinalOrigen();
					this.pageConsultatxInternacional.setSaldoTotalInicial(saldoIni);
					this.pageConsultatxInternacional.setSaldoDisInicial(saldodis);
					this.pageConsultatxInternacional.setSaldoTotalFinal(saldoFin);
					this.pageConsultatxInternacional.setSaldoDisponibleFinal(saldoDispoFin);
				}
			}

			pageRecibirTransferenciasInternacionales.setTime(pageConf.getFechaHoraTx());
		}

	}

// =======================================================================================================================

// ===========================================[EnviarTransferenciasInternacionales]===========================================================================
	/**
	 * Metodo se encarga de realizar el flujo completo de transacci�n de Enviar
	 * dinero al exterior.<b>
	 * 
	 * @throws Exception
	 */
	public void EnviarTransferenciasInternacionales(boolean soloGuardar) throws Exception {

		NotificacionSMS.TIPO_NOTIFICACION = NotificacionSMS.TIPO_NOTIF_PYMES_TRANS_ENVIADAS;

		// Pertenece al flujo de enviar trans internacionales*
		String msg = "";
		String numeral1 = DXCUtil.left(SettingsRun.getTestData().getParameter("Numeral cambiario 1"), 4);
		String numeral2 = DXCUtil.left(SettingsRun.getTestData().getParameter("Numeral cambiario 2"), 4);

		if (this.tipoCta.contains("AHORRO") || this.tipoCta.contains("Ahorro") || this.tipoCta.contains("ahorro")) {
			this.tipoCta = "CUENTA AHORROS";
		} else if (this.tipoCta.contains("CORRIENTE") || this.tipoCta.contains("Corriente")
				|| this.tipoCta.contains("corriente")) {
			this.tipoCta = "CUENTA CORRIENTE";
		}

		this.inicioCrearTx(); // DEJA LA PANTALLA [Pagos, Transferencias yotros] y seleciona el link del
								// servicio a realizar

		// Espera hasta que el iframe de divisas est� disponible
		if (this.pageEnviarTransInternacional.switchToFrameDivisas()) {

			msg = pageEnviarTransInternacional.seleccionarTransferencia("Enviar");// Se en carga de selecionar el modulo
																					// de Divisas

			if (msg != null && !msg.equals(""))
				this.terminarIteracionXError(msg);

			/**
			 * Metodo que retorna el mensaje de alerta si este existe en divisas primer
			 * mensaje Importante Para enviar o recibir transferencias cierra el mensaje
			 */

//			msg = pageRecibirTransferenciasInternacionales.closeActiveIntAlert();
//			if (msg != null && !msg.equals("") && !msg.contains("Para enviar o recibir transferencias"))
//				this.terminarIteracionXError(msg);

			// Seleciona la cuenta origen Inicial de la tx si es cuenta Ahorros o Corriente.
			msg = pageEnviarTransInternacional.seleccionarCuenta(this.servicio, this.tipoIdEm, this.nitEmpre,
					this.tipoCta, this.numCta);

			if (!msg.equals(""))
				this.terminarIteracionXError(msg);

			msg = pageEnviarTransInternacional.ingresarValores(this.tipoMoneda, this.valorcon, this.concepto, numeral1,
					numeral2, this.valorNumeral1, this.valorNumeral2, this.descripcionsin, this.tipoEnvio);

			if (msg != null && !msg.equals(""))
				this.terminarIteracionXError(msg);

			// Realiza el flujo Si es Nuevo Envio
			if (this.tipoEnvio.equals("Nuevo Envio")) {

				// Ingresa todos los datos del Beneficiario
				msg = pageEnviarTransInternacional.ingresarBeneficiario(this.nombreBene, this.paisDestino,
						this.ciudadDestino, this.direccionBene, this.cuentaBene, this.infoPago, this.referenciaPago,
						this.tipoEnvio);

				if (msg != null && !msg.equals(""))
					this.terminarIteracionXError(msg);

				msg = pageEnviarTransInternacional.datosDestino(this.tipoCodigo, this.numeroCodigo, this.intermediario,
						this.tipoCodigoInter, this.numeroCodigoInter);

				if (msg != null && !msg.equals(""))
					this.terminarIteracionXError(msg);

				// Realiza el flujo Si es Envio Frecuente
			} else if (this.tipoEnvio.equals("Envio Frecuente")) {

				// Seleciona el Beneficiario frecuente
				msg = pageEnviarTransInternacional.SeleccionarBeneficiario(this.nombreBene, this.paisDestino,
						this.cuentaBene, this.FechaEnvioFrecuente);

				if (msg != null && !msg.equals(""))
					this.terminarIteracionXError(msg);

				msg = pageEnviarTransInternacional.ValidarBeneficiario(this.nombreBene, this.paisDestino,
						this.ciudadDestino, this.direccionBene, this.cuentaBene, this.infoPago, this.referenciaPago);
				if (msg != null && !msg.equals(""))
					this.terminarIteracionXError(msg);

				msg = pageEnviarTransInternacional.ValidaciondatosSelecDestino(this.tipoCodigo, this.numeroCodigo,
						this.intermediario, this.tipoCodigoInter, this.numeroCodigoInter);

				if (msg != null && !msg.equals(""))
					this.terminarIteracionXError(msg);
			}

			msg = pageEnviarTransInternacional.cotizacion();
			if (msg != null && !msg.equals(""))
				this.terminarIteracionXError(msg);

			msg = pageEnviarTransInternacional.confirmacion(intermediario);
			if (msg != null && !msg.equals(""))
				this.terminarIteracionXError(msg);

			this.accionConfirmarInternacional(soloGuardar);

			if (!DatosDavivienda.IS_RISKMOTOR) {
				if (DatosDavivienda.STRATUS != null) {
					String tipoProdUpper = this.tipoCta;
					String tipoProd = " "; // VALOR POR DEFECTO

					if (tipoProdUpper.contains("AHORROS") || tipoProdUpper.contains("ahorros"))
						tipoProd = "AHORROS";
					else if (tipoProdUpper.contains("CORRIENTE") || tipoProdUpper.contains("corriente")) // CR�DIPLUS
						tipoProd = "CORRIENTE";

					this.pageEnviarTransInternacional.validacionSaldosStratus(this.servicio, this.tipoIdEm,
							this.nitEmpre, tipoProd, this.numCta, false);
					String saldoIni = this.pageEnviarTransInternacional.getSaldoTotalInicialOrigen();
					String saldodis = this.pageEnviarTransInternacional.getSaldoDispoInicialOrigen();
					String saldoFin = this.pageEnviarTransInternacional.getSaldoTotalFinalOrigen();
					String saldoDispoFin = this.pageEnviarTransInternacional.getSaldoTotalFinalOrigen();
					this.pageConsultatxInternacional.setSaldoTotalInicial(saldoIni);
					this.pageConsultatxInternacional.setSaldoDisInicial(saldodis);
					this.pageConsultatxInternacional.setSaldoTotalFinal(saldoFin);
					this.pageConsultatxInternacional.setSaldoDisponibleFinal(saldoDispoFin);
				}
			}

			pageRecibirTransferenciasInternacionales.setTime(pageConf.getFechaHoraTx());
		}

	}

// =======================================================================================================================

// ===========================================[EnviarTransferenciasInternacionalesPendAprobacion]===========================================================================
	/**
	 * Metodo se en carga de dejar la Tx de Divisas en estado pendiente de
	 * aprobaci�n
	 * 
	 * @throws Exception
	 */
	public void EnviarTransferenciasInternacionalesPendAprobacion() throws Exception {
		NotificacionSMS.TIPO_NOTIFICACION = NotificacionSMS.TIPO_NOTIF_PYMES_TRANS_ENVIADAS;
		// Pertenece al flujo de enviar trans internacionales*
		String msg = "";

		if (this.tipoCta.equals("Cuenta de Ahorros")) {
			this.tipoCta = "CUENTA AHORROS";
		} else {
			this.tipoCta = "CUENTA CORRIENTE";
		}

		this.inicioCrearTx(); // DEJA LA PANTALLA EN LA SELECCI�N DEL ORIGEN

		DXCUtil.wait(5);

		msg = pageEnviarTransInternacional.seleccionarTransferencia("Enviar"); // Se en carga de selecionar el modulo de
																				// Divisas

		if (!msg.equals(""))
			this.terminarIteracionXError(msg);

//		pageEnviarTransInternacional.closeActiveIntAlert();
		pageRecibirTransferenciasInternacionales.closeActiveIntAlert();
		msg = pageEnviarTransInternacional.seleccionarCuenta(this.servicio, this.tipoIdEm, this.nitEmpre, this.tipoCta,
				this.numCta);

		if (!msg.equals(""))
			this.terminarIteracionXError(msg);

		String numeral1 = DXCUtil.left(SettingsRun.getTestData().getParameter("Numeral cambiario 1"), 4);
		String numeral2 = DXCUtil.left(SettingsRun.getTestData().getParameter("Numeral cambiario 2"), 4);

		msg = pageEnviarTransInternacional.ingresarValores(this.tipoMoneda, this.valorcon, this.concepto, numeral1,
				numeral2, this.valorNumeral1, this.valorNumeral2, this.descripcionsin, this.tipoEnvio);

		if (!msg.equals(""))
			Reporter.reportEvent(Reporter.MIC_FAIL, msg);

		pageEnviarTransInternacional.ingresarBeneficiario(this.nombreBene, this.paisDestino, this.ciudadDestino,
				this.direccionBene, this.cuentaBene, this.infoPago, this.referenciaPago, this.tipoEnvio);

		pageEnviarTransInternacional.datosDestino(this.tipoCodigo, this.numeroCodigo, this.intermediario,
				this.tipoCodigoInter, this.numeroCodigoInter);

		pageEnviarTransInternacional.cotizacion();

		pageEnviarTransInternacional.confirmacion(intermediario);

//		NotificacionSMS.TIPO_NOTIFICACION = NotificacionSMS.TIPO_NOTIF_PYMES_TRANS_ENVIADAS;
		this.accionGuardarSinAprobarInternacional();

		pageRecibirTransferenciasInternacionales.setTime(pageConf.getFechaHoraTx());

	}

// =======================================================================================================================

// ===========================================[accionGuardarSinAprobarInternacional]===========================================================================

	/**
	 * Metodo se encarga de las tx Divisas guardar sin probar
	 * 
	 * @throws Exception
	 */
	private void accionGuardarSinAprobarInternacional() throws Exception {
		// CONFIRMAR GUARDAR SIN APROBAR TX
		String msgRta, msgError;
		msgRta = pageConf.guardarSinAprobarInternacional();
		if (msgRta != null)
			this.terminarIteracionXError(msgRta);

	}
// =======================================================================================================================

// ===========================================[accionConfirmarInternacional]===========================================================================

	/**
	 * Metodo se encarga en aprobar las Tx de Divisas
	 * 
	 * @param soloGuardar
	 * @throws Exception
	 */
	private void accionConfirmarInternacional(boolean soloGuardar) throws Exception {
		this.estadoTx = null;
		// CONFIRMAR TX
		String msgRta, msgError;
		// SI LLEGA A ESTE PUNTO SE GUARD� SIN APROBAR DE FORMA EXITOSA : EXTRAE EL
		// N�MERO DE DOCUMENTO
		if (!this.servicio.contains("Recibir"))
			if (DatosDavivienda.IS_RISKMOTOR) {
				this.valorTx = pageConf.getValorFinalTx();
				if (this.valorTx != null)
					SetActivityAmount(this.valorTx.replace("$", "").replace(" ", ""));

				this.bancoDesTx = pageConf.getBancoDesTx();
				if (this.bancoDesTx != null)
					SetBancoDesTx(this.bancoDesTx);
			}
		if (soloGuardar) {
			msgRta = pageConf.guardarSinAprobarInternacional();
			pageRecibirTransferenciasInternacionales.setTime(pageConf.getFechaHoraTx());
			if (msgRta != null && !msgRta.contains("guardada"))
				this.terminarIteracionXError(msgRta);

		} else {
			int numRetosFalla = 0;
			if (DatosDavivienda.IS_RISKMOTOR)
				numRetosFalla = Integer.valueOf(SettingsRun.getTestData().getParameter("Ingresos Fallidos").trim());

			msgRta = pageConf.aprobarTx(numRetosFalla, true);

			if (this.servicio.contains("Tx Internacionales Enviar al exterior")
					|| this.servicio.contains("Tx Internacionales Enviar al exterior Pendiente Aprobaci�n")
					|| this.servicio.contains("Tx Internacionales Recibir desde el exterior")) {
				if (msgRta != null && (!msgRta.contains("Transacci�n recibida")
						&& !msgRta.contains("No se habilito el campo del c�digo de confirmaci�n")
						&& !msgRta.contains("no podemos atender su solicitud")))
					msgRta = pageConf.closeActiveIntAlert();
				else if (msgRta == null) {
					this.numeroTx = pageConf.getNumeroDocumentoInterna();
					if (this.numeroTx == null) {

						msgRta = pageConf.closeActiveIntAlert();
						pageRecibirTransferenciasInternacionales.setTime(pageConf.getFechaHoraTx());
					}
				}
			}
			if (DatosDavivienda.IS_RISKMOTOR) {
				this.estadoTx = msgRta;
				this.SetEstado(msgRta);
				if (msgRta != null && DXCUtil.itemContainsAnyArrayItem(msgRta, arrMsgBuscarEstado)) {
					this.estadoTx = "Transferencia Realizada";// @estado de la transaccion
				} else if (msgRta != null && DXCUtil.itemContainsAnyArrayItem(msgRta, this.arrMsgTxDeclinada)) {
					this.estadoTx = "DECLINADA";
					this.riesgo = this.prioridaRiesgo;
					if (this.riesgo.equals("ALTO")) {
						this.numeroTx = pageConf.getNumeroDocumentoInterna();
//						SetNumApr(this.numeroTx);
					}
					if (this.servicio.contains("N�mina") || this.servicio.contains("Pago a Proveedores")
							|| this.servicio.contains("AFC") || this.servicio.contains("Cr�dito.3ros")) {
						this.validarIngresoMRDestinosMasivo();
					} else {

						this.validarIngresoMR();

					}
//					this.pageOrigen.terminarIteracion();
				}
			}

//			else {
//				System.out.println("En construcci�n depende del tipo de la prueba");
//			}

			if (msgRta != null && !msgRta.contains(PageConfirmacion.MSG_EXITO_GUARD)
					&& !msgRta.contains(PageConfirmacion.MSG_EXITO_PAGO)
					&& !msgRta.contains(PageConfirmacion.MSG_EXITO_APROB_INTER)
					&& !msgRta.contains(PageConfirmacion.MSG_EXITO_APROB_INTER2)
					&& !msgRta.contains(PageConfirmacion.MSG_EXITO_DOC_Y_FOR_INTER)
					&& !msgRta.contains(PageConfirmacion.MSG_EXITO_DOC_Y_FOR_COMPLETE_INF_INTER))
				if (!DatosDavivienda.IS_RISKMOTOR) {
					this.terminarIteracionXError(msgRta);
				} else {
					this.terminarIteracionXErrorMotor(msgRta);
				}
		}

		if (msgRta != null && !msgRta.contains("recibida") && this.tipoPrueba.equals("Tx En L�nea")) {
			String msg = null;
			if (!msgRta.contains(PageConfirmacion.MSG_EXITO_DOC_Y_FOR_COMPLETE_INF_INTER)) {
				msg = this.pageDocumentos_Y_Formularios.ModuloDocumetosYFormularios(this.tipoPrueba);
				if (msg != null && !msg.contains("En este m�dulo puede visualizar las operaciones") && !msg
						.contains("Los campos que no se presentan en la declaraci�n de cambio ser�n autocompletados"))
					this.terminarIteracionXErrorMotor(msg);
			} else {
				msg = this.pageDocumentos_Y_Formularios.IralModuloDocumetosYFormularios();
				if (msg != null && !msg.isEmpty())
					this.terminarIteracionXErrorMotor(msg);
			}

			String numCambiario1 = DXCUtil.left(SettingsRun.getTestData().getParameter("Numeral cambiario 1"), 4);
			String numCambiario2 = DXCUtil.left(SettingsRun.getTestData().getParameter("Numeral cambiario 2"), 4);
			String tipoOperacion = SettingsRun.getTestData().getParameter("Tipo de operaci�n");
			String desInversion = SettingsRun.getTestData().getParameter("Destino de la inversi�n");
			String opciondeinversi�n = SettingsRun.getTestData().getParameter("Opci�n de inversi�n");
			String deducciones = SettingsRun.getTestData().getParameter("Deducciones");
			String cargueDocu = SettingsRun.getTestData().getParameter("Cargue Archivo Documentos");

			// Divide la ruta en un array de strings separados por comas
			String[] rutaArch = cargueDocu.split(",");

			msg = this.pageDocumentos_Y_Formularios.DatosDocumetosYFormularios(this.concepTx, tipoOperacion,
					desInversion, opciondeinversi�n, this.valorsin, numCambiario1, this.valorNumeral1, numCambiario2,
					this.valorNumeral2, deducciones, rutaArch);

			if (msg != null && !msg.isEmpty()) {
				if (msg.contains("Para continuar debe completar la informaci�n solicitada")) {
					msg = "Para continuar debe completar la informaci�n solicitada";
				}
				this.terminarIteracionXErrorMotor(msg);
			}

		} else {
			if (!soloGuardar) {
				if (this.numeroTx == null)
					this.numeroTx = pageConf.getNumeroDocumentoInterna();
				if (this.numeroTx == null)
					this.terminarIteracionXError("No se encontr� el N�mero de Documento de la transacci�n");

				SettingsRun.getTestData().setParameter("N�mero Aprobaci�n", this.numeroTx);

				this.pageConsultatxInternacional.SetNumAprInterna(this.numeroTx);

//				SetNumApr(this.numeroTx);
			}
		}

	}

// =======================================================================================================================

// ===========================================[aprobarTxPendienteIntern]===========================================================================
	/**
	 * Intenta aprobar una transacci�n cuyo n�mero de transacci�n se encuentra
	 * almacenado en el par�metro "N�mero Aprobaci�n".<br>
	 * Este metodo solo es utilizado para transferencias Internacionales "Divisas".
	 * 
	 * @throws Exception
	 */
	public String aprobarTxPendienteIntern(boolean desdeDetalle) throws Exception {

		this.pageAprobInter.inicioAprobaciones(desdeDetalle);

		int numRetosFalla = 0;
		String msgRta = null;
		if (DatosDavivienda.IS_RISKMOTOR)
			numRetosFalla = Integer.valueOf(SettingsRun.getTestData().getParameter("Ingresos Fallidos").trim());

//			String msgRta = pageConf.aprobarTx(numRetosFalla, numCodSeg);
		msgRta = pageConf.aprobarTx(numRetosFalla, desdeDetalle);

		if (msgRta != null && !msgRta.contains(PageConfirmacion.MSG_EXITO_GUARD)
				&& !msgRta.contains(PageConfirmacion.MSG_EXITO_PAGO)
				&& !msgRta.contains(PageConfirmacion.MSG_EXITO_ENV_OTP)
				&& !msgRta.contains(PageConfirmacion.MSG_EXITO_APROB)
				&& !msgRta.contains(PageConfirmacion.MSG_EXITO_APROB_INTER)
				&& !msgRta.contains(PageConfirmacion.MSG_EXITO_APROB_INTER2)
				&& !msgRta.contains(PageConfirmacion.MSG_EXITO_DOCYFOR))

			this.terminarIteracionXError(msgRta);

		if (DatosDavivienda.IS_RISKMOTOR) {
			this.estadoTx = msgRta;
			this.SetEstado(msgRta);
			if (msgRta != null && DXCUtil.itemContainsAnyArrayItem(msgRta, arrMsgBuscarEstado)) {
				this.estadoTx = "Transferencia Realizada";// Buscar estado de la transaccion
			} else if (DXCUtil.itemContainsAnyArrayItem(msgRta, arrMsgTxDeclinada))
				this.estadoTx = "DECLINADA";

		}
//		else {
//			System.out.println("En construcci�n, depende del tipo de la prueba");
//		}
		String msg = null;

		if (msgRta != null && msgRta.contains(PageConfirmacion.MSG_EXITO_DOCYFOR)) {

			msg = this.pageDocumentos_Y_Formularios.closeActiveIntAlert();

			if (msgRta.contains(PageConfirmacion.MSG_EXITO_DOCYFOR)) {
				Reporter.reportEvent(Reporter.MIC_INFO,
						"Tiene al menos una operaci�n que requiere adjuntar documentos.");
				;
			}

			msg = this.pageDocumentos_Y_Formularios.ModuloDocumetosYFormularios(this.tipoPrueba);

			if (msg != null && !msg.contains("En este m�dulo puede visualizar las operaciones") && !msg
					.contains("Los campos que no se presentan en la declaraci�n de cambio ser�n autocompletados"))
				this.terminarIteracionXErrorMotor(msg);

			String numCambiario1 = DXCUtil.left(SettingsRun.getTestData().getParameter("Numeral cambiario 1"), 4);
			String numCambiario2 = DXCUtil.left(SettingsRun.getTestData().getParameter("Numeral cambiario 2"), 4);
			String tipoOperacion = SettingsRun.getTestData().getParameter("Tipo de operaci�n");
			String desInversion = SettingsRun.getTestData().getParameter("Destino de la inversi�n");
			String opciondeinversi�n = SettingsRun.getTestData().getParameter("Opci�n de inversi�n");
			String deducciones = SettingsRun.getTestData().getParameter("Deducciones");
			String cargueDocu = SettingsRun.getTestData().getParameter("Cargue Archivo Documentos");

			// Divide la ruta en un array de strings separados por comas
			String[] rutaArch = cargueDocu.split(",");

			msg = this.pageDocumentos_Y_Formularios.DatosDocumetosYFormularios(this.concepTx, tipoOperacion,
					desInversion, opciondeinversi�n, this.valorsin, numCambiario1, this.valorNumeral1, numCambiario2,
					this.valorNumeral2, deducciones, rutaArch);

			if (msg != null) {

				if (msg.contains("Para continuar debe completar la informaci�n solicitada")) {
					msg = "Para continuar debe completar la informaci�n solicitada";
				}

				this.terminarIteracionXErrorMotor(msg);
			}

		} else {

			// SI LLEGA A ESTE PUNTO SE GUARD� SIN APROBAR DE FORMA EXITOSA : EXTRAE EL
			// N�MERO DE DOCUMENTO
			this.numeroTx = pageConf.getNumeroDocumentoInterna();

			if (this.numeroTx == null || this.numeroTx.isEmpty())
				this.terminarIteracionXError("No se encontr� el N�mero de Documento de la transacci�n");

			SettingsRun.getTestData().setParameter("N�mero Aprobaci�n", this.numeroTx);

			this.pageDivisas.SetNumAprInterna(this.numeroTx);
			this.pageConsultatxInternacional.SetNumAprInterna(this.numeroTx);

			msgRta = this.pageConsultatxInternacional.closeActiveIntAlert();

		}

		return msgRta;

	}

// =======================================================================================================================

// ===========================================[OrdenesDePago]===========================================================================
	/**
	 * Realiza el flujo de Ordenes de Pago. Realiza las validacion de aprobacion
	 * OTP/TOKEN.<br>
	 * Realiza la consulta de las Ordenes de Pago (Proceso/Estado).
	 * 
	 * @throws Exception
	 */

	public void OrdenesDePago() throws Exception {

		String msgResp = null;
		String numProceso = null;

		String convenioPago = SettingsRun.getTestData().getParameter("Convenio de Pago");
		String nomProcesoPago = SettingsRun.getTestData().getParameter("Nombre Proceso de Pago");
		String cantidadPagos = SettingsRun.getTestData().getParameter("Cantidad Total de Pagos");
		String valorPagos = SettingsRun.getTestData().getParameter("Valor Total a Pagar");
		String rutaArchivoOrpa = SettingsRun.getTestData().getParameter("Archivo Destinos");

		String[] valoresFila = new String[14];

		NotificacionSMS.TIPO_NOTIFICACION = NotificacionSMS.TIPO_NOTIF_PYMES_TF_NOINSC;
		this.inicioCrearTx(); // DEJA LA PANTALLA EN LA SELECCI�N DEL ORIGEN

		String numProcesoOrpa = this.pageValores.ingresarValoresOrpa(convenioPago, nomProcesoPago, cantidadPagos,
				valorPagos, rutaArchivoOrpa);

		msgResp = this.pageConf.aprobarTxOrdenDePago();

		Reporter.write("*** PASSED ------ " + msgResp);

		valoresFila = this.pageOrigen.ExtraerDatosDe2doExcel(2);

		this.pageOrigen.RevisionProOrdenesDePago(numProcesoOrpa, nomProcesoPago, cantidadPagos, valorPagos,
				convenioPago);

		this.pageOrigen.RevisionEstOrdenesDePago(numProcesoOrpa, cantidadPagos, convenioPago);

	}

// =======================================================================================================================

// ===========================================[Validacion Stratus]===========================================================================

	/**
	 * Este metodo se encarga de Obtener los datos de la validacion de saaldos de
	 * Stratus
	 * 
	 * @throws Exception
	 */
	public void validacionSaldosStratus() throws Exception {
		this.pageOrigen.validacionSaldosStratus(this.servicio, this.tipoIdEm, this.nitEmpre, this.tipoCta, this.numCta,
				false);
		String saldoIni = this.pageOrigen.getSaldoTotalInicialOrigen();
		String saldodis = this.pageOrigen.getSaldoDispoInicialOrigen();
		String saldoFin = this.pageOrigen.getSaldoTotalFinalOrigen();
		String saldoDispoFin = this.pageOrigen.getSaldoTotalFinalOrigen();
		this.pageConsultasyExtractos.setSaldoTotalInicial(saldoIni);
		this.pageConsultasyExtractos.setSaldoDisInicial(saldodis);
		this.pageConsultasyExtractos.setSaldoTotalFinal(saldoFin);
		this.pageConsultasyExtractos.setSaldoDisponibleFinal(saldoDispoFin);
	}

// ===========================================[getTransaccion]===========================================================================

	/**
	 * Retorna el valor de "Transacci�n", dependiendo del tipo de prueba y del
	 * servicio, este dato deber�a ser escrito en el Set de Motor de Riesgo.
	 */
	public String getTransaccion(String tipoPrueba, String servicio) throws Exception {

		String transaccion = tipoPrueba;

		if (tipoPrueba.equals(TP_PEND_APR)) {

			if (this.tipoPrueba.equals(TP_PEND_APR) && this.desdeElDetalle.contains("NO"))
				transaccion = MotorRiesgo.TX_EMP_APROB_TX;

			if (this.tipoPrueba.equals(TP_PEND_APR) && this.desdeElDetalle.contains("SI"))
				tipoPrueba = TP_EN_LINEA;

			if (servicio.equals("Retiro sin Tarjeta"))
				transaccion = MotorRiesgo.TX_PYM_APROB_RET;
		}

		if (tipoPrueba.equals(TP_EN_LINEA)) {
			switch (this.servicio) {

			case "N�mina":
			case "Pago de N�minas":
				transaccion = MotorRiesgo.TX_EMP_PAGO_NOMI;
				break;
			case "Pago a Proveedores":
			case "Proveedores":
				transaccion = MotorRiesgo.TX_EMP_PAGO_PROV;
				break;
			case "Pago de Servicios":
			case "Servicios":
				transaccion = MotorRiesgo.TX_EMP_PAGO_SERVIC;
				break;
			case "Transferencia NIT Propio":
			case "Mismo NIT":
				transaccion = MotorRiesgo.TX_EMP_TF_MISMONIT;
				break;
			case "Transferencias Cuenta Inscrita":
			case "Cuenta Inscrita":
				transaccion = MotorRiesgo.TX_EMP_TF_TERCEROS;
				break;
			case "Transferencias Cuenta No Inscrita":
			case "Cuenta No Inscrita":
				transaccion = MotorRiesgo.TX_EMP_TF_CTANO;
				break;
			case "Tx Internacionales Enviar al exterior":
				transaccion = MotorRiesgo.TX_PYM_ENVIAR_AL_EXTERIOR;
				break;
			case "Tx Internacionales Recibir desde el exterior":
				transaccion = MotorRiesgo.TX_PYM_RECIBIR_DESDE_EL_EXTERIOR;
				break;
			}
		}
		return transaccion;
	}

// =======================================================================================================================

}