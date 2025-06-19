package launchTest;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import dav.middlePymes.*;
import dav.CobrosMiddle.ControllerMiddleCobros;
import dav.CobrosMiddle.PageAdminCombosCobros;
import dav.Consultas_Y_Extractos.PageConsultasyExtractos;
import dav.TransaccionesProgramadas.PageTransaccionesProgramadas;
import dav.divisas.*;
import dav.pymes.moduloTx.PageAdminParametros;
import dav.pymes.moduloTx.PageOrigen;
import dav.pymes.PageLoginPymes;
import dav.pymes.moduloCrearTx.ControllerCrearTx;
import dav.pymes.moduloCrearTx.ControllerDestinosMasivos;
import dav.transversal.DatosDavivienda;
import dav.transversal.DatosEmpresarial;
import dav.transversal.MotorRiesgo;
import dav.transversal.Stratus;
import dxc.dav.library.reporting.EvidencePdfFile;
import dxc.execution.BaseTestNG;
import dxc.library.reporting.Reporter;
import dxc.library.settings.SettingsRun;
//import dxc.execution.Evidence;
//import dxc.execution.Reporter;
//import dxc.execution.SettingsRun;
import dxc.util.DXCUtil;
import dav.Recaudos.*;
import dav.RetiroSinTarjeta.ControllerRetiroSinTarjeta;

public class Launch_RetiroSinTarjeta extends BaseTestNG {

// =======================================================================================================================
	// Page & Crontroller
	PageLoginPymes pageLogin = null;
	PageOrigen pageOrigen = null;
	PageUsuariosEmpresa pageUsuariosEmpresa = null;
	PageAdminParametros pageAdminParametros = null;
	PageConsultasyExtractos pageConsultasyExtractos = null;
	ControllerCrearTx controller = null;
	ControllerDestinosMasivos controllerDestinosMasivos = null;
	ControllerValiPymeMiddle controllerValiPymeMiddle = null;
	PageAprobacionInter pageAprobInter = null;
	PageConsultatxInternacional pageConsultatxInternacional = null;
	PageTransaccionesProgramadas pageTransaccionesProgramadas = null;
	ControllerRetiroSinTarjeta controllerRetiroSinTarjeta = null;

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
	String cobros = null;
	String stratus = null;
	public static String realizarMR, ipPublica = null;
	Date fechaHoraLogMR;
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
	WebElement cerrarses;
	By ip = By.xpath("/html/body/pre");

// =======================================================================================================================
	List<HashMap<String, String>> resultsMiddle = new ArrayList<>();
	HashMap<String, Double> segmentosStratus = new HashMap<String, Double>();

	double[] validacionDescuento = null;
	String[] estadotxFinal = null;
// ================================LAUNCHDATA=======================================================================================

	public void launchData() { // DATOS DEL LANZAMIENTO

//		SettingsRun.EXEC_CLASS = this.getClass().getSimpleName(); // OBLIGATORIO - NOMBRE DE LA CARPETA DE EVIDENCIAS
//		Evidence.siTomarla(Evidence.FORMATO_WORD); // TOMAR EVIDENCIAS POR ROW Y DEJA LO ÚLTIMO EN EL FORMATO INDICADO
		Reporter.initializeEvidenceType(new EvidencePdfFile());
		SettingsRun.DEFAULT_HEADER = 4; // EL ARCHIVO DE DATOS EL HEADER ESTÁ EN LA FILA 4 : USARLO SI EL HEADER NO ESTÁ
										// EN 1

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

// -----------------------------------------------------------------------------------------------------------------------
		// CIERRA EL ARCHIVO DE MOTOR DE RIESGO
		if (DatosDavivienda.RISKMOTOR != null)
			DatosDavivienda.RISKMOTOR.cerrarMotorRiesgo();

// -----------------------------------------------------------------------------------------------------------------------

//		if (this.pageLogin != null)
//			this.pageLogin.closeAllBrowsers();
// -----------------------------------------------------------------------------------------------------------------------

//	 if (this.controller != null)
//	 this.controller.destroy();

	}
// =======================================================================================================================

	// ===================================DOINGCONFIGURATIONS====================================================================================

	// SOLICITUD DATOS GLOBALES, CONFIGURACIONES INICIALES Y/O VALIDACIONES
	// INICIALES
	public void doingConfigurations() throws Exception {
		SettingsRun.getTestData().addParametersNotExist("rutaArchivo");
		// HACE LA CONFIGURACIÓN REQUERIDA PREVIA REQUERIDA PARA TODAS LAS ITERACIONES
		// ------------------
		// -----------------------------------------------------------------------------------------------------
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
		cobros = SettingsRun.getGlobalData("VALIDACION.COBROS");
		stratus = SettingsRun.getGlobalData("VALIDAR.STRATUS");
		realizarMR = SettingsRun.getGlobalData("MOTOR.motorDeRiesgo");

//
	}
// =======================================================================================================================

// ===========================================DOINGTEST===========================================================================

	// MÉTODO QUE ENMARCA LAS PRUEBAS A REALIZAR POR CADA LANZAMIENTO
	public void doingTest() throws Exception {
		this.runAutomationBetweenHours(SettingsRun.getGlobalData("TiemPermitido"),SettingsRun.getGlobalData("HoraDesVentana"),SettingsRun.getGlobalData("HoraTerVentana"));

//		this.riesgoBc = SettingsRun.getTestData().getParameter("Nivel de Riesgo BC").trim();
//		this.riesgoEfm = SettingsRun.getTestData().getParameter("Nivel de Riesgo SAS EFM").trim();
//		if (realizarMR.equals("SI")) {
//			if (SettingsRun.getGlobalData("MOTOR.tipoMotor").contains("EFM")) {
//				this.riesgo = this.riesgoEfm;
//
//			} else if (SettingsRun.getGlobalData("MOTOR.tipoMotor").contains("BC")) {
//				this.riesgo = this.riesgoBc;
//			}
//		}
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
//		Reporter.write("Datos de Logueo [" + DXCUtil.arrayToString(datosLogin, " - ") + "]");
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
//		this.tipoPrueba = SettingsRun.getTestData().getParameter("Tipo prueba").trim();
//		this.desde_el_Detalle = SettingsRun.getTestData().getParameter("Desde_el_Detalle").trim();
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

// -----------------------------------------------------------------------------------------------------------------------

//*************************************************************************************************************************
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

//				this.numAprobaciones = SettingsRun.getTestData().getParameter("Numero de Aprobaciones").trim();

				/*
				 * VALIDA SI EL NUMERO DE FIRMAS ES IGUAL A 1 SI NO ES IGUAL REALIZA LA
				 * VALIDACIÓN DE MIDDLE AL 2 CLIENTE
				 */
				
//				boolean unaFirma = this.numAprobaciones.equals(CN_APRO_PEND);
//				// CONVIERTE EL NUMERO DE FIRMAS EN LA VARIABLE numfirm
//				int numfirm = Integer.parseInt(this.numAprobaciones);
//				int contador = 1;


					controllerValiPymeMiddle.ValidacionMiddlefirmas(1);
				

// -----------------------------------------------------------------------------------------------------------------------
				// Cierra la sesion en la actual que se cuentra y procede al cierre del Browser.
				this.pageLogin.CerrarSesionMiddle();

				// SI ES NULL EL MENSAJE DE ALERTA INGRESA AL LOGIN
				if (msgError == null) {

					this.LoginStratus();
//							HashMap<String, String[]> cobros = DatosDavivienda.STRATUS.irTablaValores("PYMES");

//// ----------------------------------RetiroSinTarjeta---------------------------------------------------------------
//					
					this.Front();
				}
			}
		}
//*************************************************************************************************************************		

//*************************************************************************************************************************	
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
				this.numAprobaciones = SettingsRun.getTestData().getParameter("Numero de Aprobaciones").trim();

				/*
				 * VALIDA SI EL NUMERO DE FIRMAS ES IGUAL A 1 SI NO ES IGUAL REALIZA LA
				 * VALIDACIÓN DE MIDDLE AL 2 CLIENTE
				 */
//				boolean unaFirma = this.numAprobaciones.equals(CN_APRO_PEND);
//				// CONVIERTE EL NUMERO DE FIRMAS EN LA VARIABLE numfirm
//				int numfirm = Integer.parseInt(this.numAprobaciones);
//				int contador = 1;

				/*
				 * VALIDA SI EL NUMERO DE FIRMAS ES IDIFERENTE A 1 REALIZA LA VALIDACIÓN DE
				 * MIDDLE AL 2 CLIENTE
				 */
				
					controllerValiPymeMiddle.ValidacionMiddlefirmas(1);
//				} else {
// -----------------------------------------------------------------------------------------------------------------------
					
				// Cierra la sesion en la actual que se cuentra y procede al cierre del Browser.
				this.pageLogin.CerrarSesionMiddle();
			}

// -----------------------------------------------------------------------------------------------------------------------
		}
//*************************************************************************************************************************

//*************************************************************************************************************************
		// -----------------------------------------------------------------------------------------------------------------------
		// OPCIÓN SI SELECIONA "NO" SOLO REALIZA EL FLUJO DESDE EL PORTAL FRONT
		if (contratacion.equals("NO")) {

//			
			// SI ES NULL EL MENSAJE DE ALERTA INGRESA AL LOGIN
			this.Front();

			

		}

// -----------------------------------------------------------------------------------------------------------------------

	}
// =======================================================================================================================	

// ===========================================FRONT===========================================================================
	/**
	 * Método que llama logueo en el portal Front Pyme. Realiza el primer ingreso
	 */

	public void Front() throws Exception {

		if (realizarMR.equals("NO")) {
			// Realiza el Login en Stratus
			this.LoginStratus();
		}

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
		this.numAprobaciones = SettingsRun.getTestData().getParameter("Numero de Aprobaciones").trim();
		Reporter.reportEvent(Reporter.MIC_INFO, "*** Números de firmas: [" + this.numAprobaciones + "]");

// ***********************************************************************************************************************

		do {

// -----------------------------------------------------------------------------------------------------------------------			
			// INTENTA HACER EL LOGUEO
			this.pageLogin = new PageLoginPymes(this.navegador);
//			DXCUtil.ApiGet();
			msgError = this.pageLogin.loginFront();// Método para hacer el logueo en el portal front Pyme.
			fechaHoraLogMR = this.pageLogin.getFechaHoraLogMR();
			this.pageLogin.selecionambienteClose("NO"); // Indicativo para el ambiente Front// Marca si esta en el
														// Ambiente Middle o FRONT

			if (DatosDavivienda.IS_RISKMOTOR && msgError != null) {
				Reporter.reportEvent(Reporter.MIC_FAIL, " *** No se adiciona a MR >>>" + msgError);
			}
// -----------------------------------------------------------------------------------------------------------------------
			// REINTENTA HACER EL LOGUEO SI SE PRESENTA MENSAJE DE ALERTA
			if (this.pageLogin.element(log30) != null) {
				String urlCargar = this.pageLogin.URL_FRONT;
				this.pageLogin.NavegadorFront(urlCargar);
				intento++;

			}
// -----------------------------------------------------------------------------------------------------------------------
			/*
			 * SI EL ACCESO ES DENEGADO POR SER RIESGO ALTO SE ESCRIBE EN EL ARCHIVO DE
			 * SALIDA SI EL ACCESO ES DENEGADO PERO ES RIEGO BAJO O MEDIO ESCRIBE ERROR EN
			 * EL ARCHIVO DE SALIDA
			 */

// -----------------------------------------------------------------------------------------------------------------------			

// -----------------------------------------------------------------------------------------------------------------------

		} while (this.pageLogin.element(log30) != null && intento < 3);

// ***********************************************************************************************************************

		// SI ES NULL EL MENSAJE DE ALERTA INGRESA AL LOGIN
		if (msgError == null) {

			boolean isWindowOpened = this.pageLogin.WaitForNumberOfWindos();
			if (isWindowOpened) {
				Reporter.reportEvent(Reporter.MIC_PASS, "La ventana emergente se abrió correctamente");
			} else {
				Reporter.reportEvent(Reporter.MIC_FAIL, "No se abrió La ventana emergente");
				this.pageOrigen.terminarIteracion();
			}
			this.pageLogin.closeCurrentBrowser();

// -----------------------------------------------------------------------------------------------------------------------
			// INTERATUA CON LA VENTANA EMERGENTE DE FRONT PYME
			this.pageLogin.changeWindow(this.pageLogin.accedioAlPortal());

//			 HACE EL PROCESO PARA TIPO DE PRUEBA "TRANSACCION EN LINEA" O "TRANSACCION PENDIENTE DE APROBACIÓN"
// -----------------------------------------------------------------------------------------------------------------------
			// Obtiene el nombre de la empresa a selecionar

			this.pageOrigen = new PageOrigen(this.pageLogin);
// -----------------------------------------------------------------------------------------------------------------------

			/*
			 * Intenta seleccionar la empresa. Retorna [null] si pudo hacer la selección, en
			 * caso contrario retorna mensajede error.
			 */

			msgError = this.pageOrigen.seleccionarEmpresa(this.empresa);
			// SI ES NULL EL MENSAJE DE ALERTA, SIGUE CON LAS DEMÁS VALIDACIONES
			if (msgError == null) {

// -----------------------------------------------------------------------------------------------------------------------

				this.pageAdminParametros = new PageAdminParametros(this.pageLogin);

				// Datos requeridos para la configuracion de los datos generales

//					this.numAprobaciones = SettingsRun.getTestData().getParameter("Numero de Aprobaciones").trim();
//
//// -----------------------------------------------------------------------------------------------------------------------
//
//				
// ----------------------------------ReTiroSinTarjeta---------------------------------------------------------------

//   Inicializacion Controller
				this.tipoPrueba = SettingsRun.getTestData().getParameter("Tipo prueba").trim();
				this.controllerRetiroSinTarjeta = new ControllerRetiroSinTarjeta(this.pageLogin);
				if (tipoPrueba.contains("Aprobar")||tipoPrueba.contains("Eliminar")) {
					this.controllerRetiroSinTarjeta.PendienteRetiro();
				} 
				if(tipoPrueba== null || tipoPrueba.equals("")) {
					Reporter.reportEvent(Reporter.MIC_FAIL, "Seleccione el tipo de prueba");
				}
				this.pageLogin.CerrarSesionFront();
				DXCUtil.wait(3);
				SettingsRun.exitTestIteration();
			
			}		

		}

	}

// =======================================================================================================================

// ===========================================FRONT DOBLE FIRMA===========================================================================
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

//-----------------------------------------------------------------------------------------------------------------------
		do {
			// INTENTA HACER EL LOGUEO
			this.pageLogin = new PageLoginPymes(this.navegador);
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

// =======================================================================================================================

// ===========================================MOTOR DE RIESGO===========================================================================

	/**
	 * Retorna el valor de "Transacción", dependiendo del tipo de prueba y del
	 * servicio, este dato debería ser escrito en el Set de Motor de Riesgo.
	 */

// =======================================================================================================================	

// ===========================================ADICIONAR A MOTOR DE RIESGO===========================================================================

// =======================================================================================================================	

// ===========================================[Stratus]===========================================================================

	/**
	 * Realiza el Login de Stratus de Forma general
	 * 
	 * @throws Exception
	 */
	public void LoginStratus() throws Exception {
		if (stratus.equals("SI")) {

			// Obtiene los datos desde el archivo Dataproperties.
			String strusu = SettingsRun.getGlobalData("STRATUS.usuario");
			String strpass = SettingsRun.getGlobalData("STRATUS.password");

			// Se comentan las siguientes 3 lineas para que no ejecute en stratus
			if (DatosDavivienda.STRATUS == null) {
				DatosDavivienda.STRATUS = new Stratus(strusu, strpass, "EMPRESAS");
				Reporter.reportEvent(Reporter.MIC_INFO, "Se cargan datos para logueo" + " de Stratus");
			}
		}
		if (stratus.equals("NO")) {
			Reporter.reportEvent(Reporter.MIC_INFO, "Se Quito el proceso de stratus en el Properties");
		}

		
	}

// ======================================================================================================================

}
