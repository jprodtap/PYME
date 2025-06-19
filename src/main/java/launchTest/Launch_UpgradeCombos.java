package launchTest;

import java.io.File;
import java.io.FileInputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import org.openqa.selenium.By;
import dav.middlePymes.*;
import dav.UpgradeDeCombos.*;
import dav.Consultas_Y_Extractos.PageConsultasyExtractos;
import dav.TransaccionesProgramadas.PageTransaccionesProgramadas;
import dav.pymes.moduloTx.PageAdminParametros;
import dav.pymes.moduloTx.PageOrigen;
import dav.pymes.PageLoginPymes;
import dav.pymes.moduloCrearTx.ControllerCrearTx;
import dav.pymes.moduloCrearTx.PageConfirmacion;
import dav.transversal.DatosDavivienda;
import dav.transversal.DatosEmpresarial;
import dav.transversal.MotorRiesgo;
import dav.transversal.Stratus;
import dxc.dav.library.reporting.EvidencePdfFile;
import dxc.execution.BasePageWeb;
import dxc.execution.BaseTestNG;
import dxc.library.reporting.Evidence;
import dxc.library.reporting.Reporter;
import dxc.library.settings.SettingsRun;
//import dxc.execution.Evidence;
//import dxc.execution.Reporter;
//import dxc.execution.SettingsRun;
import dxc.util.DXCUtil;

public class Launch_UpgradeCombos extends BaseTestNG {
	
	// =======================================================================================================================
	// Page & Crontroller
	PageLoginPymes pageLogin = null;
	PageOrigen pageOrigen = null;
	PageUsuariosEmpresa pageUsuariosEmpresa = null;
	PageAdminParametros pageAdminParametros = null;
	PageConsultasyExtractos pageConsultasyExtractos = null;
	ControllerCrearTx controller = null;
	ControllerValiPymeMiddle controllerValiPymeMiddle = null;
	PageTransaccionesProgramadas pageTransaccionesProgramadas = null;
	PageInicioMiddle pageInicioMiddle = null;
	PageUpgradeDeCombos pageUpgrade = null;
	Properties info;

// =======================================================================================================================	
// =======================================================================================================================
	// VARIABLES GLOBALES DE TX Y MOTOR

	final String TP_LOGIN = "Login";
	final String TP_EN_LINEA = "Tx En Linea";
	final String TP_PEND_APR = "Tx Pend Aprobación";
	final String CN_APRO_PEND = "1";
	final String DE_El_DETALLE = "SI";
	String transaccion = MotorRiesgo.TX_EMP_LOGIN_SUCC; // VALOR POR DEFECTO
	String cantidadServicios;

// -----------------------------------------------------------------------------------------------------------------------

	String contratacion = null;
	public static String realizarMR = null;
	Date fechaHoraLogMR;
	String tipoPrueba, desde_el_Detalle, servicio, riesgoBc, riesgoEfm, riesgo, navegador, empresa, numAprobaciones,
			informe;
	String numCliEmp, tipoDoc, numDoc, clave, tipoTok, datoTok;
	String na = "N/A";

// =======================================================================================================================	

	String tipoIdEm, clieEmpresa, idEmpresa, numIdUser, tipoIDUser, combo, nombreEmpre, asignarComboAmbiente;

// =======================================================================================================================
	// LOCATOR PYME FRONT
	By log30 = By.xpath("/html/body/h2[1]/p");
	By locCmEmpresa = By.xpath("//select[@id='dropMasterEmpresa']");

	// Login Front
	By cerrarSesion = By.xpath("//*[@id='CerrarSesion']");
	By cmCerrSes = By.cssSelector("a[id='CerrarSesion']");
	By fecha = By.xpath("//*[@id='tabla-top']/tbody/tr[1]/td[1]");
	By locMsgAlerta = By.xpath("//*[@id=\"lblMasterAlerta\"]");
	
	public static String[] ctasDestino;
	public static String[] tipoDestino;
	public static String[] valDestino;
	

// =======================================================================================================================

// ================================LAUNCHDATA=======================================================================================

	public void launchData() { // DATOS DEL LANZAMIENTO
//		SettingsRun.EXEC_CLASS = this.getClass().getSimpleName(); // NOMBRE CARPE 5656TA DE EVIDENCIAS
		Reporter.initializeEvidenceType(new EvidencePdfFile());
		SettingsRun.DEFAULT_HEADER = 4; // EL ARCHIVO DE DATOS EL HEADER ESTA EN LA FILA 4 : USARLO SI EL HEADER NO ESTA
										// EN 1

// -----------------------------------------------------------------------------------------------------------------------

		// PARAMETROS REQUERIDOS EN LA HOJA DE DATOS GLOBAL PARA EL LAUNCH QUE SE ESTA
		// HACIENDO
		SettingsRun.ARRAY_DATA_PARAMS = new String[] { "Selección" };

// -----------------------------------------------------------------------------------------------------------------------

//		Evidence.siTomarla(Evidence.FORMATO_ZIP); // INDICAR LA TOMA DE EVIDENCIAS Y EL FORMATO

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

		//if (this.pageLogin != null)
		//	this.pageLogin.closeAllBrowsers();
// -----------------------------------------------------------------------------------------------------------------------

//	 if (this.controller != null)
//	 this.controller.destroy();

	}
// =======================================================================================================================

// ===================================DOINGCONFIGURATIONS====================================================================================

	// SOLICITUD DATOS GLOBALES, CONFIGURACIONES INICIALES Y/O VALIDACIONES
	// INICIALES
	public void doingConfigurations() throws Exception {
		// Descomentariar la siguiente linea cuando se genere el JAR
//		DXCUtil.PATH_RESOURCES = "/resources";
		info = new Properties();
		String propNbFile = "data.properties";

		if (!DXCUtil.PATH_RESOURCES.isEmpty()) {
			String temp = new File(".").getAbsolutePath(); // VIENE POR EJEMPLO: [C:/Users/userX/Desktop/.]
			String pathTestFilesDir = DXCUtil.left(temp, temp.length() - 1); // LE QUITA EL PUNTO FINAL
			String nbFileProperties = pathTestFilesDir + propNbFile;
			File propFile = new File(nbFileProperties);
			info.load(new FileInputStream(propFile));
		} else { // EL LANZAMIENTO ES POR IDE

			String source = "/" + propNbFile;
			info.load(LaunchTestPyme.class.getResourceAsStream(source));
		}
		// HACE LA CONFIGURACIAN REQUERIDA PREVIA REQUERIDA PARA TODAS LAS ITERACIONES
// -----------------------------------------------------------------------------------------------------------------------				
		// SELECIONA SI CONFIA EN LA CONTRATACIï¿½N

		String nombreAmbiente = info.getProperty("AMBIENTE_PYME");

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
			Reporter.reportEvent(Reporter.MIC_FAIL, "Opción no valida");
			break;
		}

		if (nombreAmbiente.isEmpty()) {
			Reporter.reportEvent(Reporter.MIC_FAIL, "Nombre del ambiente seleccionado: " + nombreAmbiente);
		} else {
			Reporter.reportEvent(Reporter.MIC_HEADER, "Nombre del ambiente seleccionado: " + nombreAmbiente);
		}

		DatosEmpresarial.AMBIENTE_TEST = nombreAmbiente;

		contratacion = info.getProperty("CONTRATACION");
		realizarMR = info.getProperty("MOTOR.motorDeRiesgo");

	}
// =======================================================================================================================

// ===========================================DOINGTEST===========================================================================

	// METODO QUE ENMARCA LAS PRUEBAS A REALIZAR POR CADA LANZAMIENTO
	public void doingTest() throws Exception {
//		this.runAutomationBetweenHours(info.getProperty("HoraDesVentana"), info.getProperty("HoraTerVentana"));

		// -----------------------------------------------------------------------------------------------------------------------
		/*
		 * Datos Fijos Middle Login, estos datos se encuentran el archivo
		 * data.properties
		 */
		// numCli tipoDoc numDoc clave tipoTok datoTok
		DatosEmpresarial.loadLoginDataFija("0", info.getProperty("MIDDLE.tipoDoc"),
				info.getProperty("MIDDLE.numeroDeId"), info.getProperty("MIDDLE.clavePersonal"),
				info.getProperty("MIDDLE.tipoToken"), info.getProperty("MIDDLE.numeroToken"));

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
		this.tipoPrueba = SettingsRun.getTestData().getParameter("Tipo prueba").trim();
		this.desde_el_Detalle = SettingsRun.getTestData().getParameter("Desde_el_Detalle").trim();
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

//		Inicializa los valores temporales.
//		Util para aquellos escenarios en que no se ha adicionado un registro, pero se deben ir guardando determinados datos: Fecha y hora de la transacción /Resultado / Monto / Observación / Número de transacción.
		if (this.tipoPrueba.isEmpty() || this.tipoPrueba.equals(" ")) {
			Reporter.write("Falta Ingresar Datos de Tipo Prueba, Campo es Obligatorio ");
			SettingsRun.exitTestIteration();
		}

// -----------------------------------------------------------------------------------------------------------------------
		this.asignarComboAmbiente = SettingsRun.getTestData().getParameter("Asignar Combo").trim();
//*************************************************************************************************************************
		/*
		 * OPCIÓN SI DESEAS REALIZAR LA CONTRATACIÓN, 1RE REALIZA EL FLUJO DE
		 * CONTRATACIÓN DESDE MIDDLE Y PROCEDE A REALIZAR EL FLIJO EN FRONT
		 */
		if (asignarComboAmbiente.equals("Middle")) {
			
			// INTENTA HACER EL LOGUEO
				this.pageLogin = new PageLoginPymes(this.navegador);// Carga en que navegador se va realizar la prueba
				msgError = this.pageLogin.loginMiddle(); // Método para hacer el logueo en el portal Middle Pyme.
				this.pageLogin.selecionambienteClose("SI");// Indicativo para el ambiente middle// Marca si esta en el
																	// Ambiente Middle o FRONT
			// -----------------------------------------------------------------------------------------------------------------------
						// SI ES NULL EL MENSAJE DE ALERTA INGRESA AL LOGIN
				if (msgError == null) {
							
					this.controllerValiPymeMiddle = new ControllerValiPymeMiddle(this.pageLogin);
					this.pageUpgrade = new PageUpgradeDeCombos(pageLogin);
					
					pageUpgrade.AsignarMaxCombosMiddle(pageLogin);
					pageUpgrade.InicioUpgradeCombosMiddle(pageLogin);
					
				} else {
					
					this.pageLogin.CerrarSesionMiddle();
					
				}
		}
		
		if (asignarComboAmbiente.equals("Front")) {
			this.Front();
		}
	}
// =======================================================================================================================	

// ===========================================FRONT===========================================================================
	/**
	 * MEtodo que llama logueo en el portal Front Pyme. Realiza el primer ingreso
	 */

	public void Front() throws Exception {

		this.servicio = SettingsRun.getTestData().getParameter("Servicio").trim();
		DXCUtil.Movercursor();
// -----------------------------------------------------------------------------------------------------------------------	
		String msgError = null;
		String msgAlerta;
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

// ***********************************************************************************************************************
//**************************************************** quitar
		

		do {
//// -----------------------------------------------------------------------------------------------------------------------			
			// INTENTA HACER EL LOGUEO
//			LoginStratus();
			this.pageLogin = new PageLoginPymes(this.navegador);
			msgError = this.pageLogin.loginFront();// Metodo para hacer el logueo en el portal front Pyme.
			fechaHoraLogMR = this.pageLogin.getFechaHoraLogMR();
			PageLoginPymes.selecionambienteClose("NO"); // Indicativo para el ambiente Front// Marca si esta en el
														// Ambiente Middle o FRONT
// -----------------------------------------------------------------------------------------------------------------------
			// REINTENTA HACER EL LOGUEO SI SE PRESENTA MENSAJE DE ALERTA
			if (this.pageLogin.element(log30) != null) {
				String urlCargar = PageLoginPymes.URL_FRONT;
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

		
//		this.pageStratusAdef = new PageStratusAdef();
//		this.pageStratusAdef.InicioStratusAdef();
//		this.pageStratusAdef.consultaStratusAdef();
		// SI ES NULL EL MENSAJE DE ALERTA INGRESA AL LOGIN
		if (msgError == null) {
 
			// Cierra la ventana actual del login de Front ya que va interatuar con la
			// ventana emergente
		this.pageLogin.closeCurrentBrowser();
			// -----------------------------------------------------------------------------------------------------------------------
			// INTERATUA CON LA VENTANA EMERGENTE DE FRONT PYME
		this.pageLogin.changeWindow(this.pageLogin.accedioAlPortal());

		this.empresa = SettingsRun.getTestData().getParameter("Nombre Empresa").trim();

			// Obtiene el nombre de la empresa a selecionar
			this.pageOrigen = new PageOrigen(this.pageLogin);			
			msgError = this.pageOrigen.seleccionarEmpresa(this.empresa);

			if (msgError == null) {
				
				DXCUtil.wait(2);
				
				msgError = pageOrigen.irAOpcion("Combos o Planes", "Administración", "Administración Portal", "Adquiera productos y Servicios", "Combos o planes");

				if (msgError == null) {

					this.pageUpgrade = new PageUpgradeDeCombos(this.pageLogin);
					
					pageUpgrade.InicioUpgradeCombosFront();
					
					/* 
					 * Datos Fijos Middle Login, estos datos se encuentran el archivo
					 * data.properties
					 */
					// numCli tipoDoc numDoc clave tipoTok datoTok
					DatosEmpresarial.loadLoginDataFija("0", info.getProperty("MIDDLE.tipoDoc"),
							info.getProperty("MIDDLE.numeroDeId"), info.getProperty("MIDDLE.clavePersonal"),
							info.getProperty("MIDDLE.tipoToken"), info.getProperty("MIDDLE.numeroToken"));
					// INTENTA HACER EL LOGUEO
					this.pageLogin = new PageLoginPymes(this.navegador, Evidence.getNbEvidenceDirectory());// Carga en que
																											// navegador se
																											// va realizar
																											// la
					
					// INTENTA HACER EL LOGUEO
					this.pageLogin = new PageLoginPymes(this.navegador);// Carga en que navegador se va realizar la prueba
					msgError = this.pageLogin.loginMiddle(); // Método para hacer el logueo en el portal Middle Pyme.
					this.pageLogin.selecionambienteClose("SI");// Indicativo para el ambiente middle// Marca si esta en el
																		// Ambiente Middle o FRONT

					
					if (msgError == null) {
								
						this.controllerValiPymeMiddle = new ControllerValiPymeMiddle(this.pageLogin);
						this.pageUpgrade = new PageUpgradeDeCombos(pageLogin);
						
						pageUpgrade.InformesUpgradeMiddle(pageLogin);
						
					} else {
						
						this.pageLogin.CerrarSesionMiddle();
						
					}
					
				}

			} else {
				Reporter.reportEvent(Reporter.MIC_FAIL, msgError);
				this.pageOrigen.terminarIteracion();
			}
		
//**************************************************** quitar

			String Informe = SettingsRun.getTestData().getParameter("Informe");

			//Se realiza las consultas de Middle informes para la transaccion de ADEF
			if (Informe.equals("SI")) {

				Reporter.write(" ");
				Reporter.write("*** CONSULTA DE MOVIMIENTOS EN INFORMES MIDDLE");
				Reporter.write(" ");
				
//				this.pageLogin.CerrarSesionFront();
				this.pageLogin.selecionambienteClose("SI");// Indicativo para el ambiente middle// Marca si esta en
				// el
				// Ambiente Middle o FRONT
				// -----------------------------------------------------------------------------------------------------------------------
				/* 
				 * Datos Fijos Middle Login, estos datos se encuentran el archivo
				 * data.properties
				 */
				// numCli tipoDoc numDoc clave tipoTok datoTok
				DatosEmpresarial.loadLoginDataFija("0", info.getProperty("MIDDLE.tipoDoc"),
						info.getProperty("MIDDLE.numeroDeId"), info.getProperty("MIDDLE.clavePersonal"),
						info.getProperty("MIDDLE.tipoToken"), info.getProperty("MIDDLE.numeroToken"));
				// INTENTA HACER EL LOGUEO
				this.pageLogin = new PageLoginPymes(this.navegador, Evidence.getNbEvidenceDirectory());// Carga en que
																										// navegador se
																										// va realizar
																										// la
				// Guarda los Datos del cliente Middle
//				PageLoginPymes.datosMidell(numCliEmp, tipoDoc, numDoc, clave, tipoTok, datoTok);
				// Guarda el Dato del Token Middle
//				PageUsuariosEmpresa.datosMidellToke(datoTok);
				String msgErrorMid = this.pageLogin.loginMiddle(); // Metodo para hacer el logueo en el portal
				String msgalerta = null;

				if (msgErrorMid == null) {

					/*
					 * this.pageMiddleConsultasAdef = new PageMiddleConsultasAdef(this.pageLogin);
					 * 
					 * //Realiza la consulta en las deferentes ventanas de Informes Middle para ADEF
					 * msgalerta = this.pageMiddleConsultasAdef.InformeMiddleAdef();
					 * 
					 * if (msgalerta != null) {
					 * 
					 * Reporter.reportEvent(Reporter.MIC_FAIL, msgalerta);
					 * this.pageOrigen.terminarIteracion(); this.pageLogin.CerrarSesionMiddle(); }
					 * this.pageLogin.CerrarSesionMiddle();
					 */
				}
			}

		}

	}

	// ===========================================[Stratus]===========================================================================

	public void LoginStratus() throws Exception {

		String strusu = info.getProperty("STRATUS.usuario");
		String strpass = info.getProperty("STRATUS.password");

		// Se comentan las siguientes 3 lineas para que no ejecute en stratus
		if (DatosDavivienda.STRATUS == null) {
			DatosDavivienda.STRATUS = new Stratus(strusu, strpass, "EMPRESAS");
			Reporter.reportEvent(Reporter.MIC_INFO, "Se cargan datos para logueo" + " de Stratus");
		}

	}

	// ======================================================================================================================


}
