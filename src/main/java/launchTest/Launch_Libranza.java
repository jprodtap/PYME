package launchTest;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import org.openqa.selenium.By;

import dav.Libranza.ControllerLibranza;
import dav.middlePymes.ControllerValiPymeMiddle;
import dav.middlePymes.PageUsuariosEmpresa;
import dav.pymes.PageLoginPymes;
import dav.pymes.moduloTx.PageAdminParametros;
import dav.pymes.moduloTx.PageOrigen;
import dav.transversal.DatosDavivienda;
import dav.transversal.DatosEmpresarial;
import dav.transversal.MotorRiesgo;
import dxc.dav.library.reporting.EvidencePdfFile;
import dxc.execution.BaseTestNG;
import dxc.library.reporting.Evidence;
import dxc.library.reporting.Reporter;
import dxc.library.settings.SettingsRun;
//import dxc.execution.Evidence;
//import dxc.execution.Reporter;
//import dxc.execution.SettingsRun;
import dxc.util.DXCUtil; 

public class Launch_Libranza extends BaseTestNG {
	// =======================================================================================================================
	// Page & Crontroller
	PageLoginPymes pageLogin = null;
	PageOrigen pageOrigen = null;
	PageUsuariosEmpresa pageUsuariosEmpresa = null;
	PageAdminParametros pageAdminParametros = null;
	ControllerValiPymeMiddle controllerValiPymeMiddle = null;
	ControllerLibranza controllerLibranza = null;

	Properties info;

	// =======================================================================================================================

	// =======================================================================================================================
	// VARIABLES GLOBALES DE TX Y MOTOR
	final String TP_LOGIN = "Login";
	final String TP_EN_LINEA = "Tx En Línea";
	final String TP_PEND_APR = "Tx Pend Aprobación";
	final String CN_APRO_PEND = "1";
	final String DE_El_DETALLE = "SI";
	String transaccion = MotorRiesgo.TX_EMP_LOGIN_SUCC; // VALOR POR DEFECTO

	// -----------------------------------------------------------------------------------------------------------------------

	String contratacion = null;

	String tipoPrueba, desde_el_Detalle, servicio, riesgoBc, riesgoEfm, riesgo, userAgent, navegador, empresa,
			numAprobaciones, informe;
	String numCliEmp, tipoDoc, numDoc, clave, tipoTok, datoTok;
	String na = "N/A";
	// =======================================================================================================================
	String tipoIdEm, clieEmpresa, idEmpresa, numIdUser, tipoIDUser, combo, nombreEmpre;
	// =======================================================================================================================
	// LOCATOR PYME FRONT
	By log30 = By.xpath("/html/body/h2[1]/p");
	By locCmEmpresa = By.xpath("//select[@id='dropMasterEmpresa']");

	// Login Front
	By cerrarSesion = By.xpath("//*[@id='CerrarSesion']");
	By cmCerrSes = By.cssSelector("a[id='CerrarSesion']");
	By fecha = By.xpath("//*[@id='tabla-top']/tbody/tr[1]/td[1]");

	By ip = By.xpath("/html/body/pre");

	// =======================================================================================================================

	// ================================LAUNCHDATA=======================================================================================

	public void launchData() { // DATOS DEL LANZAMIENTO
//		SettingsRun.EXEC_CLASS = this.getClass().getSimpleName(); // NOMBRE CARPE 5656TA DE EVIDENCIAS
//		Evidence.siTomarla(Evidence.FORMATO_EXCEL);// INDICAR LA TOMA DE EVIDENCIAS Y EL FORMATO
		Reporter.initializeEvidenceType(new EvidencePdfFile());
		SettingsRun.DEFAULT_HEADER = 4; // EL ARCHIVO DE DATOS EL HEADER ESTÁ EN LA FILA 4 : USARLO SI EL HEADER NO ESTÁ									// EN 1

		// -----------------------------------------------------------------------------------------------------------------------

		// PARÁMETROS REQUERIDOS EN LA HOJA DE DATOS GLOBAL PARA EL LAUNCH QUE SE ESTÉ
		// HACIENDO
		SettingsRun.ARRAY_DATA_PARAMS = new String[] { "Selección" };

		// -----------------------------------------------------------------------------------------------------------------------

	}
	// =======================================================================================================================

	// =======================================================================================================================

	// ====================================LAUNCHCLOSE===================================================================================

		public void launchClose() { // CIERRE DEL LANZAMIENTO
	//
	//// -----------------------------------------------------------------------------------------------------------------------
//			// CIERRA EL ARCHIVO DE MOTOR DE RIESGO
//			if (DatosDavivienda.RISKMOTOR != null)
//				DatosDavivienda.RISKMOTOR.cerrarMotorRiesgo();
	//
	//// -----------------------------------------------------------------------------------------------------------------------
	//
			if (this.pageLogin != null)
				this.pageLogin.closeAllBrowsers();
	//// -----------------------------------------------------------------------------------------------------------------------
	//
////		 if (this.controller != null)
////		 this.controller.destroy();
	//
		}
	// =======================================================================================================================

	// ===================================DOINGCONFIGURATIONS====================================================================================

	// SOLICITUD DATOS GLOBALES, CONFIGURACIONES INICIALES Y/O VALIDACIONES
	// INICIALES
	public void doingConfigurations() throws Exception {

		// HACE LA CONFIGURACIÓN REQUERIDA PREVIA REQUERIDA PARA TODAS LAS ITERACIONES
		// -----------------------------------------------------------------------------------------------------------------------
		// SELECIONA SI CONFIA EN LA CONTRATACIÓN

		String nombreAmbiente = SettingsRun.getGlobalData("AMBIENTE_PYME");

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
			Reporter.reportEvent(Reporter.MIC_FAIL, "Opción no válida");
			break;
		}

		if (nombreAmbiente.isEmpty()) {
			Reporter.reportEvent(Reporter.MIC_FAIL, "Nombre del ambiente seleccionado: Portal" + nombreAmbiente);
		} else {
			Reporter.reportEvent(Reporter.MIC_HEADER, "Nombre del ambiente seleccionado: Portal" + nombreAmbiente);
		}

		DatosEmpresarial.AMBIENTE_TEST = nombreAmbiente;

		contratacion = SettingsRun.getGlobalData("CONTRATACION");

	}
	// =======================================================================================================================

	// ===========================================DOINGTEST===========================================================================

	// MÉTODO QUE ENMARCA LAS PRUEBAS A REALIZAR POR CADA LANZAMIENTO
	public void doingTest() throws Exception {
//			this.runAutomationBetweenHours(info.getProperty("HoraDesVentana"), info.getProperty("HoraTerVentana"));

		// -----------------------------------------------------------------------------------------------------------------------
		/*
		 * Datos Fijos Middle Login, estos datos se encuentran el archivo
		 * data.properties
		 */
		// numCli tipoDoc numDoc clave tipoTok datoTok
		DatosEmpresarial.loadLoginDataFija("0", SettingsRun.getGlobalData("MIDDLE.tipoDoc"),
				SettingsRun.getGlobalData("MIDDLE.numeroDeId"),
				SettingsRun.getGlobalData("MIDDLE.clavePersonal"),
				SettingsRun.getGlobalData("MIDDLE.tipoToken"),
				SettingsRun.getGlobalData("MIDDLE.numeroToken"));
 
		// -----------------------------------------------------------------------------------------------------------------------

		// Organiza los datos del cliente Middle con un array
		String[] datosLogin = DatosEmpresarial.getLoginData();
		// Reporta los datos del logeo
//			Reporter.write("Datos de Logueo [" + DXCUtil.arrayToString(datosLogin, " - ") + "]");
		if (contratacion.equals("SI") || contratacion.equals("SOLO"))
			Reporter.reportEvent(Reporter.MIC_INFO,
					"*** Datos de Logueo Middle: [" + DXCUtil.arrayToString(datosLogin, " - ") + "]");
		// numCli tipoDoc numDoc clave tipoTok datoTok
		numCliEmp = datosLogin[0];
		tipoDoc = datosLogin[1];
		numDoc = datosLogin[2];
		clave = datosLogin[3];
		tipoTok = datosLogin[4];
		datoTok = datosLogin[5];

		// -----------------------------------------------------------------------------------------------------------------------
		// Guarda los Datos del cliente Middle
		PageLoginPymes.datosMidell(numCliEmp, tipoDoc, numDoc, clave, tipoTok, datoTok);
		// Guarda el Dato del Token Middle
		PageUsuariosEmpresa.datosMidellToke(datoTok);

		// -----------------------------------------------------------------------------------------------------------------------

		String msgError = null;
		// PARÁMETROS REQUERIDOS EN EL ARCHIVO EXCEL
		this.navegador = SettingsRun.getTestData().getParameter("Navegador").trim();
		this.servicio = SettingsRun.getTestData().getParameter("Servicio").trim();
		this.empresa = SettingsRun.getTestData().getParameter("Nombre Empresa").trim();

		String clienteEmpresarial = SettingsRun.getTestData().getParameter("Cliente Empresarial").trim();
		String Idusuario = SettingsRun.getTestData().getParameter("Id usuario").trim();
		String tipoIdentificación = SettingsRun.getTestData().getParameter("Tipo Identificación").trim();
		String tipoIDEmpresa = SettingsRun.getTestData().getParameter("Tipo ID Empresa").trim();
		String numeroIDEmpresa = SettingsRun.getTestData().getParameter("Numero ID Empresa").trim();
		// -----------------------------------------------------------------------------------------------------------------------

		/*
		 * Metodo se encarga de validar, que los datos obligatorios esten en el archivo
		 * a cargar
		 */
		// this.validarOCorregirData();

		// -----------------------------------------------------------------------------------------------------------------------

		String msgInicio = "";

		if (contratacion.equals("SI") || contratacion.equals("SOLO")) {
			msgInicio = "*** Cliente Empresarial: [" + clienteEmpresarial + "]";
			Reporter.reportEvent(Reporter.MIC_INFO, msgInicio);
			msgInicio = "*** Tipo Identificación Usuario: [" + tipoIdentificación + "] - Tipo Identificación Usuario: ["
					+ Idusuario + "]";
			Reporter.reportEvent(Reporter.MIC_INFO, msgInicio);
			msgInicio = "*** Tipo Servicio a contratar: [" + this.servicio + "]";
			Reporter.reportEvent(Reporter.MIC_INFO, msgInicio);
			msgInicio = "*** Empresa a contratar: [" + this.empresa + "]";
			Reporter.reportEvent(Reporter.MIC_INFO, msgInicio);
			msgInicio = "*** Tipo Identificación Empresa: [" + tipoIDEmpresa + "] - Numero Identificación Empresa: ["
					+ numeroIDEmpresa + "]";
			Reporter.reportEvent(Reporter.MIC_INFO, msgInicio);
		}

		// *************************************************************************************************************************
		/*
		 * OPCIÓN SI DESEAS REALIZAR LA CONTRATACIÓN, 1RE REALIZA EL FLUJO DE
		 * CONTRATACIÓN DESDE MIDDLE Y PROCEDE A REALIZAR EL FLIJO EN FRONT
		 */
		if (contratacion.equals("SI")) {
			// INTENTA HACER EL LOGUEO
			this.pageLogin = new PageLoginPymes(this.navegador);// Carga en que navegador se va realizar la prueba
			msgError = this.pageLogin.loginMiddle(); // Método para hacer el logueo en el portal Middle Pyme.
			this.pageLogin.selecionambienteClose("SI");// Indicativo para el ambiente middle// Marca si esta en el
														// Ambiente Middle o FRONT

			// -----------------------------------------------------------------------------------------------------------------------

			// SI ES NULL EL MENSAJE DE ALERTA INGRESA AL LOGIN
			if (msgError == null) {

				this.controllerValiPymeMiddle = new ControllerValiPymeMiddle(this.pageLogin);
				// -----------------------------------------------------------------------------------------------------------------------

				this.numAprobaciones = SettingsRun.getTestData().getParameter("Números de Aprobaciones").trim();

				/*
				 * VALIDA SI EL NUMERO DE FIRMAS ES IGUAL A 1 SI NO ES IGUAL REALIZA LA
				 * VALIDACIÓN DE MIDDLE AL 2 CLIENTE
				 */
				boolean unaFirma = this.numAprobaciones.equals(CN_APRO_PEND);
				// CONVIERTE EL NUMERO DE FIRMAS EN LA VARIABLE numfirm
				int numfirm = Integer.parseInt(this.numAprobaciones);
				int contador = 1;

				/*
				 * VALIDA SI EL NUMERO DE FIRMAS ES IDIFERENTE A 1 REALIZA LA VALIDACIÓN DE
				 * MIDDLE AL 2 CLIENTE
				 */
				if (!unaFirma) {
					// REALIZA EL BUCLE CON LA CANTIDA DE FIRMAS A PARAMETRIZAR
					controllerValiPymeMiddle.ValidacionMiddlefirmas(1);
					do {

						if (contador != numfirm) { // AUMENTA EL VALOR DE LA VARIABLE
							contador++;
						}
						/*
						 * REALIZA EL FLUJO DE PYME MIDDLE, PARA REALIZAR LA CONTRARACION DE LOS
						 * SERVICIOS A LOS DEMAS USUARIOS SI ES POR FIRMAS, SUMA 1 A LA VARIABLE
						 * CONTADOR
						 */
						controllerValiPymeMiddle.ValidacionMiddlefirmas(contador);

						// EL BUCLE CONTINÚA MIENTRAS EL CONTADOR VARIABLE SEA MENOR QUE NUMFIRM.
					} while (contador < numfirm);

				} else {
// -----------------------------------------------------------------------------------------------------------------------
					// REALIZA LA CONTRATACIÓN, EL FLUJO DE PYME MIDDLE
					controllerValiPymeMiddle.ValidacionMiddlefirmas(1);
				}

				// -----------------------------------------------------------------------------------------------------------------------
				// Cierra la sesion en la actual que se cuentra y procede al cierre del Browser.
				this.pageLogin.CerrarSesionMiddle();

				// SI ES NULL EL MENSAJE DE ALERTA INGRESA AL LOGIN
				if (msgError == null) {
					// LLAMA EL METODO DEL FLUJO LOGIN FRONT
					this.Front();
				}
			}
		}
		// *************************************************************************************************************************

		// *************************************************************************************************************************
		// OPCIÓN SI SELECIONA "Solo Contratar servicios" SOLO REALIZA EL FLUJO DE
		// MIDDLE
		if (contratacion.equals("SOLO")) {

			// INTENTA HACER EL LOGUEO
			this.pageLogin = new PageLoginPymes(this.navegador);// Carga en que navegador se va realizar la prueba
			msgError = this.pageLogin.loginMiddle(); // Método para hacer el logueo en el portal Middle Pyme.
			this.pageLogin.selecionambienteClose("SI");// Indicativo para el ambiente middle// Marca si esta en el
														// Ambiente Middle o FRONT
			// -----------------------------------------------------------------------------------------------------------------------
			// SI ES NULL EL MENSAJE DE ALERTA INGRESA AL LOGIN
			if (msgError == null) {
				this.controllerValiPymeMiddle = new ControllerValiPymeMiddle(this.pageLogin);

				// -----------------------------------------------------------------------------------------------------------------------
				// NUMEROS DE FIRMAS A RALIZAR
				this.numAprobaciones = SettingsRun.getTestData().getParameter("Números de Aprobaciones").trim();

				/*
				 * VALIDA SI EL NUMERO DE FIRMAS ES IGUAL A 1 SI NO ES IGUAL REALIZA LA
				 * VALIDACIÓN DE MIDDLE AL 2 CLIENTE
				 */
				boolean unaFirma = this.numAprobaciones.equals(CN_APRO_PEND);
				// CONVIERTE EL NUMERO DE FIRMAS EN LA VARIABLE numfirm
				int numfirm = Integer.parseInt(this.numAprobaciones);
				int contador = 1;
 
				/*
				 * VALIDA SI EL NUMERO DE FIRMAS ES IDIFERENTE A 1 REALIZA LA VALIDACIÓN DE
				 * MIDDLE AL 2 CLIENTE
				 */
				if (!unaFirma) {
					// REALIZA EL BUCLE CON LA CANTIDA DE FIRMAS A PARAMETRIZAR
					controllerValiPymeMiddle.ValidacionMiddlefirmas(1);
					do {

						if (contador != numfirm) { // AUMENTA EL VALOR DE LA VARIABLE
							contador++;
						}
						/*
						 * REALIZA EL FLUJO DE PYME MIDDLE, PARA REALIZAR LA CONTRARACION DE LOS
						 * SERVICIOS A LOS DEMAS USUARIOS SI ES POR FIRMAS, SUMA 1 A LA VARIABLE
						 * CONTADOR
						 */
						controllerValiPymeMiddle.ValidacionMiddlefirmas(contador);

						// EL BUCLE CONTINÚA MIENTRAS EL CONTADOR VARIABLE SEA MENOR QUE NUMFIRM.
					} while (contador < numfirm);

				} else {
// -----------------------------------------------------------------------------------------------------------------------
					// REALIZA LA CONTRATACIÓN, EL FLUJO DE PYME MIDDLE
					controllerValiPymeMiddle.ValidacionMiddlefirmas(1);
				}
				// -----------------------------------------------------------------------------------------------------------------------
				// Cierra la sesion en la actual que se cuentra y procede al cierre del Browser.
				this.pageLogin.CerrarSesionMiddle();
			}
			// -----------------------------------------------------------------------------------------------------------------------
		}
		// *************************************************************************************************************************

		// *************************************************************************************************************************
		// -----------------------------------------------------------------------------------------------------------------------
		// OPCIÓN SI SELECIONA "NO" SOLO REALIZA EL FLUJO DESDE EL PORTAL FRONT
		if (contratacion.equals("NO")) {
			this.Front();
		}

		// -----------------------------------------------------------------------------------------------------------------------

	}

	// ===========================================FRONT===========================================================================
	/**  
	 * Método que llama logueo en el portal Front Pyme. Realiza el primer ingreso
	 */

	public void Front() throws Exception {

		DXCUtil.Movercursor();
		// -----------------------------------------------------------------------------------------------------------------------
		String msgError = null;
		int intento = 0;
		String msgInicio = "";
		String msgInicio2 = "";
		// -----------------------------------------------------------------------------------------------------------------------
		// Datos Login front Login, estos datos se encuentran el archivo del carge DATA
		DatosEmpresarial.loadLoginData("Cliente Empresarial", "Tipo Identificación", "Id usuario",
				"Clave personal o CVE", "Tipo Token", "Semilla / Valor Estático / Celular");
		String[] datosLogin = DatosEmpresarial.getLoginData();
		Reporter.reportEvent(Reporter.MIC_INFO,
				"*** Datos de Logueo Front: [" + DXCUtil.arrayToString(datosLogin, " - ") + "]");
		this.numAprobaciones = SettingsRun.getTestData().getParameter("Números de Aprobaciones").trim();
		Reporter.reportEvent(Reporter.MIC_INFO, "*** Números de firmas: [" + this.numAprobaciones + "]");

		do {

			// -----------------------------------------------------------------------------------------------------------------------
			// INTENTA HACER EL LOGUEO
			this.pageLogin = new PageLoginPymes(this.navegador, Evidence.getNbEvidenceDirectory());
//				DXCUtil.ApiGet();
			msgError = this.pageLogin.loginFront();// Método para hacer el logueo en el portal front Pyme.

			this.pageLogin.selecionambienteClose("NO"); // Indicativo para el ambiente Front// Marca si esta en el
														// Ambiente Middle o FRONT

			// -----------------------------------------------------------------------------------------------------------------------
			// REINTENTA HACER EL LOGUEO SI SE PRESENTA MENSAJE DE ALERTA
			if (this.pageLogin.element(log30) != null) {
				String urlCargar = this.pageLogin.URL_FRONT;
				this.pageLogin.NavegadorFront(urlCargar);
				intento++;

			}

			// -----------------------------------------------------------------------------------------------------------------------

		} while (this.pageLogin.element(log30) != null && intento < 3);

		// ***********************************************************************************************************************

		// SI ES NULL EL MENSAJE DE ALERTA INGRESA AL LOGIN
		if (msgError == null) {
			DXCUtil.wait(10);
			this.pageLogin.closeCurrentBrowser();

			// -----------------------------------------------------------------------------------------------------------------------
			// INTERATUA CON LA VENTANA EMERGENTE DE FRONT PYME
			this.pageLogin.changeWindow(this.pageLogin.accedioAlPortal());

//				 HACE EL PROCESO PARA TIPO DE PRUEBA "TRANSACCION EN LINEA" O "TRANSACCION PENDIENTE DE APROBACIÓN"
			if (msgError == null) {
				// -----------------------------------------------------------------------------------------------------------------------
				// Obtiene el nombre de la empresa a selecionar

				this.pageOrigen = new PageOrigen(this.pageLogin);
				// -----------------------------------------------------------------------------------------------------------------------

				/*
				 * Intenta seleccionar la empresa. Retorna [null] si pudo hacer la selección, en
				 * caso contrario retorna mensajede error.
				 */

				Reporter.reportEvent(Reporter.MIC_INFO, "Datos empresa: " + "[" + this.empresa + "]");
				msgError = this.pageOrigen.seleccionarEmpresa(this.empresa);
				// SI ES NULL EL MENSAJE DE ALERTA, SIGUE CON LAS DEMÁS VALIDACIONES
				if (msgError == null) {

					// -----------------------------------------------------------------------------------------------------------------------

					this.pageAdminParametros = new PageAdminParametros(this.pageLogin);
					// Datos requeridos para la configuracion de los datos generales

					String tipoAbono = null;
					String ctaInscrita = null;
					this.numAprobaciones = SettingsRun.getTestData().getParameter("Números de Aprobaciones").trim();

// -----------------------------------------------------------------------------------------------------------------------

					// Este método hace la configuración en parámetros generales, y guarda la
					// evidencia.
					msgError = this.pageAdminParametros.hacerConfiguracion(this.numAprobaciones, tipoAbono,
							ctaInscrita);

// -----------------------------------------------------------------------------------------------------------------------
					if (msgError != null) {

						this.transar(); // Se encuentra logueado en el portal Pymes, empieza arealizar las TX.
			 			
					} else {
						Reporter.reportEvent(Reporter.MIC_FAIL, msgError);
						this.pageOrigen.terminarIteracion();

					}

				} else {
					Reporter.reportEvent(Reporter.MIC_FAIL, msgError);
					this.pageOrigen.terminarIteracion();
				}

			}

		}
  
	}

	// =======================================================================================================================

	// ===========================================FRONT DOBLE
	// FIRMA===========================================================================
	/**
	 * Método que llama logueo en el portal Front Pyme si es diferente a una firma.
	 * Realiza el logueo con el usuario a realizar la aprobación
	 */
	public void Frontfirmas(int firmas) throws Exception {
		String msgError = null;
		int intento = 0;

		// Datos Login front Login, estos datos se encuentran el archivo del carge DATA
		DatosEmpresarial.loadLoginData("Cliente Empresarial", "Tipo Identificación " + firmas, "Id usuario " + firmas,
				"Clave personal o CVE " + firmas, "Tipo Token " + firmas,
				"Semilla / Valor Estático / Celular " + firmas);

		// -----------------------------------------------------------------------------------------------------------------------
		do {
			// INTENTA HACER EL LOGUEO
			this.pageLogin = new PageLoginPymes(this.navegador, Evidence.getNbEvidenceDirectory());
			msgError = this.pageLogin.loginFrontFirmas();// Método para hacer el logueo en el portal front Pyme.
			this.pageLogin.selecionambienteClose("NO");// Indicativo para el ambiente Front// Marca si esta en el
														// Ambiente Middle o FRONT
			if (this.pageLogin.element(log30) != null) {
				String urlCargar = this.pageLogin.URL_FRONT;
				this.pageLogin.NavegadorFront(urlCargar);
				intento++;
			}
		} while (this.pageLogin.element(log30) != null && intento < 2);

		// -----------------------------------------------------------------------------------------------------------------------
		// INTERATUA CON LA VENTANA EMERGENTE DE FRONT PYME
		if (msgError == null) {

			// Cierra la ventana actual. Si sólo hay una ventana abierta, se cierra todo
			// elBrowser.
			DXCUtil.wait(10);
			this.pageLogin.closeCurrentBrowser();
			// INTERATUA CON LA VENTANA EMERGENTE DE FRONT PYME
			this.pageLogin.changeWindow(this.pageLogin.accedioAlPortal());

			// -----------------------------------------------------------------------------------------------------------------------
			// Valida la empresa al momento de ingresar en FRONT

			this.pageOrigen = new PageOrigen(this.pageLogin);

			msgError = this.pageOrigen.seleccionarEmpresa(this.empresa);

			if (msgError != null) {
				Reporter.reportEvent(Reporter.MIC_FAIL, msgError);
				this.pageOrigen.terminarIteracion();
			}
		}
	}

	// =======================================================================================================================

	// ===========================================TRANSACCIONAL===========================================================================
	/**
	 * Se encuentra logueado en el portal Pymes, se intenta realizar la transacción
	 * requerida. Realiza la tx dependiendo si es transacion "Tx En Línea" o "Tx
	 * Pend Aprobación". Donde realiza la probacion de una vez o primero guarda sin
	 * aprobar, y des pues aprueba la tx, en el modulo de pendiente de aprobación
	 * 
	 */  
	public void transar() throws Exception {
//		type name = new type();

//		name.metodio.Autorizarlibranza.()

		// metodo en page o controler

		// -----------------------------------------------------------------------------------------------------------------------
		// SI LLEGÓ A ESTE PUNTO, SE PUDO REALIZAR LA APROBACION
		// Obtiene el número de firmas
		this.numAprobaciones = SettingsRun.getTestData().getParameter("Números de Aprobaciones").trim();

		boolean unaFirma = this.numAprobaciones.equals(CN_APRO_PEND);

		int numfirm = Integer.parseInt(this.numAprobaciones);
		int contador = 1;
		this.controllerLibranza = new ControllerLibranza(this.pageLogin);
		if (!unaFirma) {
			String[] datosAprobados = this.controllerLibranza.AprobLibranza(unaFirma);
			this.pageLogin.changeToDefaultFrame();
			// aqui va consulta historico varias firmas
			this.pageLogin.CerrarSesionFront();// cierra sesion del login de doble firma

			do {

				if (contador != numfirm) {
					contador++;
				}
				this.Frontfirmas(contador);
				this.controllerLibranza = new ControllerLibranza(this.pageLogin);
				this.controllerLibranza.aprobarSegundaFirma(datosAprobados);
				this.controllerLibranza.consultaHistoricos(datosAprobados);
//				name.met	odio.Autorizarlibranza.();
//				name.aprobar(); 
//				name.historico();

				// aqui va la aprobación de varias firmas.
				this.pageLogin.CerrarSesionFront();

			} while (contador < numfirm);

		} else if (unaFirma) {

			// aqui va consulta historico una firma

			String[] datosAprobados = this.controllerLibranza.AprobLibranza(unaFirma);
//			String [] arrayDatos = new String [2]; 
//			arrayDatos = new String[] {"1901, 108617, 1362577426, , $25.000.000, 60, $38.000, 2000/1/1, Pensionado, $4.000.000, Aprobado, $0, sssssss", "1901, 108619, 1362577426, , $25.000.000, 60, $38.000, 2000/1/1, Pensionado, $4.000.000, Negado, $0, ddddddd"};
//			String [] solicitud = new String [2]; 
//			solicitud = new String[] {"108584", "108585"};
			this.controllerLibranza.consultaHistoricos(datosAprobados); // datosAprobados
    
//			name.aprobar(); 	
//			name.historico();
		}
	}

	// =======================================================================================================================

}
