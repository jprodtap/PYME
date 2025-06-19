package launchTest;

import dav.pymes.PageLoginPymes;
import dav.pymes.moduloCrearTx.ControllerCrearTx;
import dav.transversal.DatosDavivienda;
import dav.transversal.DatosEmpresarial;
import dav.transversal.DavUtil;
import dav.transversal.MotorRiesgo;
import dav.transversal.Stratus;
import dxc.dav.library.reporting.EvidencePdfFile;
import dxc.execution.BaseTestNG;
import dxc.execution.BaseTestNG_XBloque;
import dxc.library.reporting.Reporter;
import dxc.library.settings.SettingsRun;
//import dxc.execution.Evidence; 
//import dxc.execution.Reporter;
//import dxc.execution.SettingsRun;
import dxc.util.DXCUtil;

public class LaunchPymes_MotorRiesgo extends BaseTestNG {

	final String TP_LOGIN    = "Login";
	final String TP_EN_LINEA = "Tx En Línea";
	final String TP_PEND_APR = "Tx Pend Aprobación";
	
	//String[] arrEstExito = { "EXITO", "PAGAD", "REALIZAD", "PENDRESP", "PROGRAMAD", "RESPUESTA", "OTROBANCO" };
	   
	PageLoginPymes pageLogin = null;
	ControllerCrearTx controller = null;
	
	
	String tipoPrueba, servicio, riesgo, navegador;
	
//***********************************************************************************************************************
	public void launchData() { // DATOS DEL LANZAMIENTO
//		SettingsRun.EXEC_CLASS = this.getClass().getSimpleName(); // OBLIGATORIO - NOMBRE DE LA CARPETA DE EVIDENCIAS
//		Evidence.siTomarla(Evidence.FORMATO_EXCEL); // TOMAR EVIDENCIAS POR ROW Y DEJA LO �LTIMO EN EL FORMATO INDICADO
		Reporter.initializeEvidenceType(new EvidencePdfFile());
		SettingsRun.DEFAULT_HEADER = 2; // EL ARCHIVO DE DATOS EL HEADER EST� EN LA FILA 2 : USARLO SI EL HEADER NO EST� EN 1
//-----------------------------------------------------------------------------------------------------------------------
		// PAR�METROS REQUERIDOS EN LA HOJA DE DATOS GLOBAL PARA EL LAUNCH QUE SE EST� HACIENDO
		SettingsRun.ARRAY_DATA_PARAMS = new String[] {"Navegador", "Cliente Empresarial", "Tipo Documento",
				"N�mero Documento", "Clave", "Tipo Cliente", "Dato para obtener Din�mica", "Hash", "Nivel Riesgo",
				"Tipo prueba", "Servicio", "Ingresos Fallidos", "Valor Transacci�n", "Nombre Empresa", "Tipo Id Empresa",
				"Id Empresa", "Tipo Producto Origen", "N�mero Producto Origen", "Tipo de Carga", "Archivo Destinos",
				"Filas a Tomar", "Banco Destino", "Tipo Producto Destino", "N�mero Producto Destino",
				"Nombre del Titular", "Tipo identificaci�n", "N�mero identificaci�n", "N�mero del Convenio", "Referencia"};
//-----------------------------------------------------------------------------------------------------------------------
	}
//***********************************************************************************************************************
	public void loadEnvironment() throws Exception {
		DatosEmpresarial.AMBIENTE_TEST = DavUtil.seleccionAmbiente();
	}
//***********************************************************************************************************************
	public void launchClose() { // CIERRE DEL LANZAMIENTO
		if (this.pageLogin != null)
			this.pageLogin.closeAllBrowsers();
		//if (this.controller != null)
			//this.controller.destroy();
		// CIERRA EL ARCHIVO DE MOTOR DE RIESGO
		if (DatosDavivienda.RISKMOTOR != null)
			DatosDavivienda.RISKMOTOR.cerrarMotorRiesgo();
	}
//***********************************************************************************************************************
	// SOLICITUD DATOS GLOBALES, CONFIGURACIONES INICIALES Y/O VALIDACIONES INICIALES
	public void doingConfigurations() throws Exception {
		
		// ADICI�N DE NUEVOS PAR�METROS REQUERIDOS
//		SettingsRun.getGlobalData().addParametersNotExist("Fecha Transacci�n", "Hora Transacci�n", "N�mero Aprobaci�n");
//-----------------------------------------------------------------------------------------------------------------------
		// CONFIGURACI�N DEL ARCHIVO DEL SET DE MOTOR DE RIESGO
		DatosDavivienda.IS_RISKMOTOR = true; // DEJA MARCA QUE INDICA QUE SE TRANSAR� PARA MOTOR DE RIESGO
		String nbArchivo = MotorRiesgo.preguntarPorArchivoMR();
		if (nbArchivo == null)
			DatosDavivienda.RISKMOTOR = new MotorRiesgo(DatosDavivienda.CANAL_PYME_FRONT);
		else // HAY UN ARCHIVO DE MOTOR DE RIESGO
			DatosDavivienda.RISKMOTOR = new MotorRiesgo(DatosDavivienda.CANAL_PYME_FRONT, nbArchivo);
//-----------------------------------------------------------------------------------------------------------------------
		// HACE LA CONFIGURACI�N REQUERIDA PREVIA REQUERIDA PARA TODAS LAS ITERACIONES
	}
//=======================================================================================================================
	// M�TODO QUE ENMARCA LAS PRUEBAS A REALIZAR POR CADA LANZAMIENTO
//=======================================================================================================================
	public void doingTest() throws Exception {
		
		DatosEmpresarial.loadLoginData("Cliente Empresarial", "Tipo Documento", "N�mero Documento", "Clave",
				"Tipo Cliente", "Dato para obtener Din�mica");
		
		this.tipoPrueba = SettingsRun.getTestData().getParameter("Tipo prueba").trim();
		this.servicio   = SettingsRun.getTestData().getParameter("Servicio").trim();
		this.riesgo     = SettingsRun.getTestData().getParameter("Nivel Riesgo").trim();
		this.navegador  = SettingsRun.getTestData().getParameter("Navegador").trim();
		
		this.validarOCorregirData();
//-----------------------------------------------------------------------------------------------------------------------
		boolean isRiskAlto = riesgo.toUpperCase().equals(MotorRiesgo.RISK_ALTO);
		
		String msgInicio = "*** Riesgo [" + riesgo + "] : " + tipoPrueba; 
		if (tipoPrueba.equals(TP_EN_LINEA) || tipoPrueba.equals(TP_PEND_APR))
			msgInicio += " " + servicio;
		Reporter.write(msgInicio);
		DatosDavivienda.RISKMOTOR.inicializaValoresTemp();
//-----------------------------------------------------------------------------------------------------------------------
		// INTENTA HACER EL LOGUEO
		if (this.pageLogin == null)
			this.pageLogin = new PageLoginPymes(this.navegador);
		String msgError = this.pageLogin.loginFront();
//-----------------------------------------------------------------------------------------------------------------------
		// TRANSACCI�N DE LOGUEO EXITOSO
		if (tipoPrueba.equals(TP_LOGIN)) {
			
			DatosDavivienda.RISKMOTOR.setTemporalTime(this.pageLogin.getFechaHoraLogMR());
			DatosDavivienda.RISKMOTOR.setTemporalMonto("0");
			
			if (isRiskAlto) {
				if (msgError == null)// HIZO LOGUEO Y NO ES CORRECTO
					Reporter.reportEvent(Reporter.MIC_FAIL, "No se adiciona al MR >>> Dej� realizar el logueo y NO deber�a");

				else if (!msgError.contains(PageLoginPymes.MSG_LOG_NO_ACCESO)) // NO HIZO LOGUEO Y EL ERROR NO ES EL ESPERADO
					Reporter.reportEvent(Reporter.MIC_FAIL, "No se adiciona a MR >>> [ERROR DE LOGUEO] " + msgError);

				else { // NO HIZO LOGUEO Y EL ERROR ES EL ESPERADO PARA RIESGO ALTO
					DatosDavivienda.RISKMOTOR.setTemporalResultado(MotorRiesgo.RESULT_DENY);
					this.adicionarRegistroMR();
				}
			}
			else { // ES RIESGO MEDIO / BAJO
				if (msgError != null) // NO HIZO LOGUEO
					Reporter.reportEvent(Reporter.MIC_FAIL, "No se adiciona a MR >>> [ERROR DE LOGUEO] " + msgError);
				
				else { // HIZO LOGUEO Y ES CORRECTO QUE LO HAYA HECHO
					DatosDavivienda.RISKMOTOR.setTemporalResultado(MotorRiesgo.RESULT_SUCCESS);
					this.adicionarRegistroMR();
				}
			}
		}
//-----------------------------------------------------------------------------------------------------------------------
		// TRANSACCI�N QUE REQUIERE HABER INGRESADO AL PORTAL (DEBI� HACER LOGUEO)
		else {
			if (msgError != null) // NO HIZO LOGUEO
				Reporter.reportEvent(Reporter.MIC_FAIL, "No se adiciona a MR >>> [ERROR DE LOGUEO] " + msgError);

			else { // EST� LOGUEADO : INTENTAR HACER LA TRANSACCI�N
				this.transar();
				// SI LLEGA A ESTE PUNTO ES PORQUE NO HUBO ERROR, NO SALT� A LA SIGUIENTE ITERACI�N
				// SE HAN IDO ADICIONANDO DE FORMA TEMPORAL DATOS AL MOTOR, SE ADICIONA EL REGISTRO
				this.adicionarRegistroMR();
			}
		}
//-----------------------------------------------------------------------------------------------------------------------
		// REVISA SI ES MOMENTO DE CERRAR SESI�N
		if (this.pageLogin.isTimeCloseSession()) {
			this.pageLogin.closeSession(); // CIERRA EL BROWSER
			this.pageLogin = null; // PARA QUE CREE DE NUEVO EL DRIVER 
		}
	}
//***********************************************************************************************************************
	// ESCRIBIR TODOS LOS M�TODOS REQUERIDOS, QUE APOYEN LAS PRUEBAS
//***********************************************************************************************************************
	/**
	 * Valida que la m�nima data exista, en caso contrario termina la iteraci�n actual - siempre y cuando [es1raFirma]
	 * sea [false], porque cuando es [true] retorna [false] indicando que hubo error en la validaci�n de data.
	 */
	private boolean validarOCorregirData() throws Exception {
		
		String nomEmp    = SettingsRun.getTestData().getParameter("Nombre Empresa").trim();
		String tipoIdEmp = SettingsRun.getTestData().getParameter("Tipo Id Empresa").trim();
		String idEmp     = SettingsRun.getTestData().getParameter("Id Empresa").trim();
		String tipoProdOrigen = SettingsRun.getTestData().getParameter("Tipo Producto Origen").trim();
		String numProdOrigen  = SettingsRun.getTestData().getParameter("N�mero Producto Origen").trim();
		String bancoDest    = SettingsRun.getTestData().getParameter("Banco Destino").trim();
		String tipoProdDest = SettingsRun.getTestData().getParameter("Tipo Producto Destino").trim();
		String numProdDest  = SettingsRun.getTestData().getParameter("N�mero Producto Destino").trim();
		String nomTitular   = SettingsRun.getTestData().getParameter("Nombre del Titular").trim();
		String tipoIdDest   = SettingsRun.getTestData().getParameter("Tipo identificaci�n").trim();
		String numIdDest    = SettingsRun.getTestData().getParameter("N�mero identificaci�n").trim();
		String nomConvenio  = SettingsRun.getTestData().getParameter("N�mero del Convenio").trim();	
		String refPago      = SettingsRun.getTestData().getParameter("Referencia").trim();	
		String valorPago    = SettingsRun.getTestData().getParameter("Valor Transacci�n").trim();
		String numIngrFall  = SettingsRun.getTestData().getParameter("Ingresos Fallidos").trim();
		boolean isArchivo = !SettingsRun.getTestData().getParameter("Tipo de Carga").isEmpty();
		String serv = servicio.toUpperCase();
		String msgError = null;
//-----------------------------------------------------------------------------------------------------------------------
		if (DatosEmpresarial.CLI_EMPRESAR.isEmpty() || DatosEmpresarial.TIPO_ID_LOGUEO.isEmpty() ||
				DatosEmpresarial.NUM_ID_LOGUEO.isEmpty() || DatosEmpresarial.CLAVE.isEmpty() ||
				DatosEmpresarial.TIPO_TOKEN.isEmpty())
			msgError = "No se ingres� TODA la informaci�n del cliente para loguearse";
		else if (tipoPrueba.isEmpty())
			msgError = "No se indic� el tipo de prueba a realizar";
		else if (!tipoPrueba.equals(TP_LOGIN) && servicio.isEmpty())
			msgError = "No se indic� el servicio a usar para [" + tipoPrueba + "]";
		else if (riesgo.isEmpty())
			msgError = "No se indic� el nivel de riesgo del cliente";
		else if (numIngrFall.isEmpty() || !DXCUtil.isInteger(numIngrFall)) {
			Reporter.write("Se cambia el valor de 'Ingresos Fallidos' a [0]");
			SettingsRun.getTestData().setParameter("Ingresos Fallidos", "0");
		}
		else if (servicio.equals("Retiro sin Tarjeta") && this.tipoPrueba.equals(TP_EN_LINEA))
			msgError = "[Retiro sin Tarjeta] NO est� automatizado para [Tx En L�nea]";
//-----------------------------------------------------------------------------------------------------------------------
		else if (tipoPrueba.equals(TP_EN_LINEA) && (nomEmp.isEmpty()||tipoIdEmp.isEmpty() || 
				idEmp.isEmpty() || tipoProdOrigen.isEmpty() || numProdOrigen.isEmpty()))
			msgError = "No se indicaron algunos Datos de la Empresa due�a del Producto Origen para: [" + servicio + "]";
		else if ((serv.contains("NOMINA") ||serv.contains("N�MINA") || servicio.contains("PROVEEDORES") ||
				serv.contains("OTROS")) && (bancoDest.isEmpty()||
				tipoProdDest.isEmpty() || numProdDest.isEmpty() || nomTitular.isEmpty() 
				|| tipoIdDest.isEmpty() || numIdDest.isEmpty()))
			msgError = "No se indicaron algunos Datos del destino para: [" + servicio + "]";
		else if (serv.contains("MISMO") && (tipoProdDest.isEmpty() || numProdDest.isEmpty()))
			msgError = "No se indicaron algunos Datos del destino para: [" + servicio + "]";
		//Transferencia otras Cuentas DAV
		else if (serv.contains("INSCR") && (tipoProdDest.isEmpty() || 
				numProdDest.isEmpty() || nomTitular.isEmpty() || tipoIdDest.isEmpty() || numIdDest.isEmpty()))
			msgError = "No se indicaron algunos Datos del destino para: [" + servicio + "]";
		else if (serv.contains("SERVICIO") && (refPago.isEmpty() || 
				nomConvenio.isEmpty()))
			msgError = "No se indicaron algunos Datos del destino para: [" + servicio + "]";
		
		else if (!servicio.contains("Pago Servicios") && !isArchivo && valorPago.isEmpty())
			msgError = "No se indic� el valor a pagar para: [" + servicio + "]";
//-----------------------------------------------------------------------------------------------------------------------
		if (msgError != null) {
			Reporter.reportEvent(Reporter.MIC_NOEXEC, "No se adiciona al MR >>> [ERROR DE DATA] " + msgError);
			if (this.pageLogin != null && this.pageLogin.isTimeCloseSession()) {
				this.pageLogin.closeSession(); // CIERRA EL BROWSER
				this.pageLogin = null; // PARA QUE CREE DE NUEVO EL DRIVER 
			}
			SettingsRun.exitTestIteration(); // SALE DE LA ITERACI�N ACTUAL
		}
		return true;
	}
//***********************************************************************************************************************
	/**
	 * Realiza el registro en el set de Motor de Riesgo.<br>
	 * Los datos temporales ya deben existir: Time / Monto / NumeroTx / Resultado / Observacion
	 */
	public void adicionarRegistroMR() throws Exception {
		
		DatosDavivienda.RISKMOTOR.adicionarRegistro();
		// SETEA LOS REGISTROS QUE SON GUARDADOS DE MANERA TEMPORAL
		DatosDavivienda.RISKMOTOR.setTime();
		DatosDavivienda.RISKMOTOR.setMonto();
		DatosDavivienda.RISKMOTOR.setNumeroTx();
		DatosDavivienda.RISKMOTOR.setResultado();
		DatosDavivienda.RISKMOTOR.setObservacion();
//-----------------------------------------------------------------------------------------------------------------------
		String transaccion = MotorRiesgo.TX_EMP_LOGIN_SUCC; // VALOR POR DEFECTO
		if (!tipoPrueba.equals(TP_LOGIN))
			transaccion = this.getTransaccion(tipoPrueba, servicio);
		
		String userType = "Token"; // VALOR POR DEFECTO
		if (DatosEmpresarial.TIPO_TOKEN.equals(DatosEmpresarial.TOKEN_OTP))
			userType = "Virtual";
		
		String intentos = SettingsRun.getTestData().getParameter("Ingresos Fallidos").trim();
		int intentosFall = 0; // VALOR POR DEFECTO
		if (DXCUtil.isInteger(intentos)) intentosFall = Integer.valueOf(intentos);
//-----------------------------------------------------------------------------------------------------------------------
		// SETEA LOS DATOS FALTANTES
		DatosDavivienda.RISKMOTOR.setHash(SettingsRun.getTestData().getParameter("Hash").trim());
		DatosDavivienda.RISKMOTOR.setUserType(userType);
		DatosDavivienda.RISKMOTOR.setRisk(riesgo);
		DatosDavivienda.RISKMOTOR.setTransaccion(transaccion);
		DatosDavivienda.RISKMOTOR.setNumRetosFallidos(intentosFall);
		DatosDavivienda.RISKMOTOR.setDireccionIp(DXCUtil.getIP());
		DatosDavivienda.RISKMOTOR.setNavegador_SistemaOperativo(this.navegador);
		DatosDavivienda.RISKMOTOR.setCliente(DatosEmpresarial.CLI_EMPRESAR);
		DatosDavivienda.RISKMOTOR.setDocumento(DatosEmpresarial.TIPO_ID_LOGUEO, DatosEmpresarial.NUM_ID_LOGUEO);
		Reporter.reportEvent(Reporter.MIC_PASS, "Se adiciona al MR");
	}
//***********************************************************************************************************************
	/**
	 * Retorna el valor de "Transacci�n", dependiendo del tipo de prueba y del servicio, este dato deber�a ser escrito
	 * en el Set de Motor de Riesgo.
	 */
	private String getTransaccion(String tipoPrueba, String servicio) throws Exception {
		String transaccion = tipoPrueba;
		
		if (tipoPrueba.equals(TP_PEND_APR)) {
			transaccion = MotorRiesgo.TX_EMP_APROB_TX;
			if (servicio.equals("Retiro sin Tarjeta"))
				transaccion = MotorRiesgo.TX_PYM_APROB_RET;
		}
		else if (tipoPrueba.equals(TP_EN_LINEA)) {
			switch (servicio) {
			case "Pago N�mina":
				transaccion = MotorRiesgo.TX_EMP_PAGO_NOMI; break;
			case "Pago Proveedores":
				transaccion = MotorRiesgo.TX_EMP_PAGO_PROV; break;
			case "Pago Servicios":
				transaccion = MotorRiesgo.TX_EMP_PAGO_SERVIC; break;
			case "Transferencia mismo NIT":
				transaccion = MotorRiesgo.TX_EMP_TF_MISMONIT; break;
			case "Transferencia cuenta inscrita":
			case "Transferencia cuenta no inscrita":
				transaccion = MotorRiesgo.TX_EMP_TF_TERCEROS; break;
			}
		}
		return transaccion;
	}
//***********************************************************************************************************************
	/**
	 * Se encuentra logueado en el portal Pymes, se intenta realizar la transacci�n requerida.
	 */
	public void transar() throws Exception {

		if(this.controller == null) this.controller = new ControllerCrearTx(this.pageLogin);
//-----------------------------------------------------------------------------------------------------------------------
		boolean primeroGuardar = this.tipoPrueba.equals(TP_PEND_APR);
		switch (this.servicio) {
		case "Pago N�mina": case "Pago Proveedores":
			this.controller.crearPagoNomina_Proveedores(primeroGuardar);
			break;
		case "Pago Servicios":
			this.controller.crearPagoServicios(primeroGuardar);
			break;
		case "Transferencia mismo NIT":
			this.controller.crearTranferenciaMismoNit(primeroGuardar);
			break;
		case "Transferencia cuenta inscrita":
			this.controller.crearTransferenciaCtaInscrita(primeroGuardar);
			break;
		case "Transferencia cuenta no inscrita":
			this.controller.crearTransferenciaCtaNoInscrita(primeroGuardar);
			break;
		}
		
//-----------------------------------------------------------------------------------------------------------------------
		// SI LLEG� A ESTE PUNTO, SE PUDO REALIZAR LA CREACI�N DE LA TRANSACCI�N
		if (primeroGuardar) // PRIMERO GUARD�, AHORA SE DEBE SOLICITAR APROBACI�N
			this.controller.aprobarTxPendiente(false);
	}
//***********************************************************************************************************************
}