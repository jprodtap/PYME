package launchTest;

import java.io.File;
import java.io.FileInputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import org.openqa.selenium.By;
import dav.middlePymes.*;
import dav.Consultas_Y_Extractos.PageConsultasyExtractos;
import dav.Autoservicio.PageAutoservicio;
import dav.Autoservicio.PageConsultaUsuarioMiddle;
import dav.TransaccionesProgramadas.PageTransaccionesProgramadas;
import dav.divisas.*;
import dav.pymes.moduloTx.PageAdminParametros;
import dav.pymes.moduloTx.PageOrigen;
import dav.pymes.PageLoginPymes;
import dav.pymes.moduloCrearTx.ControllerCrearTx;
import dav.transversal.DatosDavivienda;
import dav.transversal.DatosEmpresarial;
import dav.transversal.MotorRiesgo;
import dxc.dav.library.reporting.EvidencePdfFile;
import dxc.execution.BaseTestNG;
import dxc.library.reporting.Reporter;
import dxc.library.settings.SettingsRun;
//import dxc.execution.Evidence;
//import dxc.execution.Reporter;
//import dxc.execution.SettingsRun;
import dxc.util.DXCUtil;

public class Launch_Autoservicios extends BaseTestNG {

// =======================================================================================================================
	// Page & Crontroller
	PageLoginPymes pageLogin = null;
	PageOrigen pageOrigen = null;
	PageUsuariosEmpresa pageUsuariosEmpresa = null;
	PageAdminParametros pageAdminParametros = null;
	PageConsultasyExtractos pageConsultasyExtractos = null;
	ControllerCrearTx controller = null;
	ControllerValiPymeMiddle controllerValiPymeMiddle = null;
	PageAprobacionInter pageAprobInter = null;
	PageConsultatxInternacional pageConsultatxInternacional = null;
	PageTransaccionesProgramadas pageTransaccionesProgramadas = null;
	PageAutoservicio pageAutoservicio = null;
	PageConsultaUsuarioMiddle pageConsultaUsuarioMiddle = null;

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
	String tipoIdEm, clieEmpresa, idEmpresa, numIdUser, tipoIDUser, combo, nombreEmpre;
// =======================================================================================================================
	// LOCATOR PYME FRONT
	By log30 = By.xpath("/html/body/h2[1]/p");
	By locCmEmpresa = By.xpath("//select[@id='dropMasterEmpresa']");

	// Login Front
	By cerrarSesion = By.xpath("//*[@id='CerrarSesion']");
	By cmCerrSes = By.cssSelector("a[id='CerrarSesion']");
	By fecha = By.xpath("//*[@id='tabla-top']/tbody/tr[1]/td[1]");

// =======================================================================================================================

// ================================LAUNCHDATA=======================================================================================

	public void launchData() { // DATOS DEL LANZAMIENTO
//		SettingsRun.EXEC_CLASS = this.getClass().getSimpleName(); // OBLIGATORIO - NOMBRE DE LA CARPETA DE EVIDENCIAS
//		Evidence.siTomarla(Evidence.FORMATO_EXCEL); // TOMAR EVIDENCIAS POR ROW Y DEJA LO �LTIMO EN EL FORMATO INDICADO
		Reporter.initializeEvidenceType(new EvidencePdfFile());
		SettingsRun.DEFAULT_HEADER = 4; // EL ARCHIVO DE DATOS EL HEADER ESTA EN LA FILA 4 : USARLO SI EL HEADER NO ESTA
										// EN 1

// -----------------------------------------------------------------------------------------------------------------------

		// PARAMETROS REQUERIDOS EN LA HOJA DE DATOS GLOBAL PARA EL LAUNCH QUE SE ESTA
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

		if (this.pageLogin != null)
			this.pageLogin.closeAllBrowsers();
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
		// SELECIONA SI CONFIA EN LA CONTRATACI�N

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

// -----------------------------------------------------------------------------------------------------------------------

		// PARAMETROS REQUERIDOS EN EL ARCHIVO EXCEL
		this.navegador = SettingsRun.getTestData().getParameter("Navegador").trim();

// -----------------------------------------------------------------------------------------------------------------------		

		/*
		 * Metodo se encarga de validar, que los datos obligatorios esten en el archivo
		 * a cargar
		 */
//		 this.validarOCorregirData();

// -----------------------------------------------------------------------------------------------------------------------

		String msgInicio = "";

// -----------------------------------------------------------------------------------------------------------------------

//*************************************************************************************************************************

//*************************************************************************************************************************
// -----------------------------------------------------------------------------------------------------------------------
		// OPCI�N SI SELECIONA "NO" SOLO REALIZA EL FLUJO DESDE EL PORTAL FRONT
		if (contratacion.equals("NO")) {
			this.Front();
		}
	}
// =======================================================================================================================	

// ===========================================FRONT===========================================================================
	/**
	 * MEtodo que llama logueo en el portal Front Pyme. Realiza el primer ingreso
	 */

	public void Front() throws Exception {

		DXCUtil.Movercursor();
// -----------------------------------------------------------------------------------------------------------------------	
		String msgError = null;
		int intento = 0;

// ***********************************************************************************************************************

//		do {

		// INTENTA HACER EL LOGUEO
		this.pageLogin = new PageLoginPymes(this.navegador);
//			msgError = this.pageLogin.loginFront();// Metodo para hacer el logueo en el portal front Pyme.
//			PageLoginPymes.selecionambienteClose("NO"); // Indicativo para el ambiente Front// Marca si esta en el
//														// Ambiente Middle o FRONT
// -----------------------------------------------------------------------------------------------------------------------
		// REINTENTA HACER EL LOGUEO SI SE PRESENTA MENSAJE DE ALERTA

// -----------------------------------------------------------------------------------------------------------------------
		/*
		 * SI EL ACCESO ES DENEGADO POR SER RIESGO ALTO SE ESCRIBE EN EL ARCHIVO DE
		 * SALIDA SI EL ACCESO ES DENEGADO PERO ES RIEGO BAJO O MEDIO ESCRIBE ERROR EN
		 * EL ARCHIVO DE SALIDA
		 */

// -----------------------------------------------------------------------------------------------------------------------			
// -----------------------------------------------------------------------------------------------------------------------

//		} while (this.pageLogin.element(log30) != null && intento < 3);

		// ***********************************************************************************************************************

		// SI ES NULL EL MENSAJE DE ALERTA INGRESA AL LOGIN
		if (msgError == null) {

			// Cierra la ventana actual del login de Front ya que va interatuar con la
			// ventana emergente

			// -----------------------------------------------------------------------------------------------------------------------
			// INTERATUA CON LA VENTANA EMERGENTE DE FRONT PYME
//			this.pageLogin.changeWindow(this.pageLogin.accedioAlPortal());

			// La siguiente información nos permitira saber cuantos servicios que se deben
			// seleccionar por ejecuciones

			this.pageAutoservicio = new PageAutoservicio(this.pageLogin);
			String portalAutoservicio = SettingsRun.getTestData().getParameter("Portal Autoservicios");
			this.pageLogin.loginMiddleActivartoken();
			if (msgError == null) {
				if ("FRONT".equals(portalAutoservicio)) {
					String urlCargar = PageLoginPymes.URL_FRONT;
					this.pageLogin.maximizeBrowser();
					this.pageLogin.NavegadorFront(urlCargar);
					PageLoginPymes.selecionambienteClose("NO"); // Indicativo para el ambiente Front// Marca si esta en
																// el
//					// Ambiente Middle o FRONT
					DXCUtil.wait(2);
					this.pageAutoservicio.autoservicio();
				} else if ("MIDDLE".equals(portalAutoservicio)) {
//					this.pageLogin = new PageLoginPymes(this.navegador);// Carga en que navegador se va realizar la
					// Middle
					this.pageLogin.loginMiddleActivartoken();
					this.pageLogin.selecionambienteClose("SI");// Indicativo para el ambiente middle// Marca si esta en
																// el
					// Ambiente Middle o FRONT
					this.pageAutoservicio = new PageAutoservicio(this.pageLogin);
					DXCUtil.wait(2);

					this.pageAutoservicio.autoservicio();

				}
				this.pageLogin.closeCurrentBrowser();
				DXCUtil.wait(2);

				String tipoPruebaAutoservicio = SettingsRun.getTestData().getParameter("Tipo Prueba");
				if (tipoPruebaAutoservicio.equals("Activar Token")) {
					Reporter.reportEvent(Reporter.MIC_INFO, "Se consultara el cliente en Middle...");
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
					this.pageLogin = new PageLoginPymes(this.navegador);// Carga en que navegador se va realizar la
					// Guarda los Datos del cliente Middle
//				PageLoginPymes.datosMidell(numCliEmp, tipoDoc, numDoc, clave, tipoTok, datoTok);
					// Guarda el Dato del Token Middle
					String msgErrorMid = this.pageLogin.loginMiddle(); // M�todo para hacer el logueo en el portal
																		// Middle
					if (msgErrorMid == null) {
						this.pageConsultaUsuarioMiddle = new PageConsultaUsuarioMiddle(this.pageLogin);
						this.pageConsultaUsuarioMiddle.consultaMiddle();
						this.pageLogin.CerrarSesionMiddle();
						Reporter.reportEvent(Reporter.MIC_PASS, "Se realizo correctamente la asignación de el/los tokens.");
					} else {
						Reporter.reportEvent(Reporter.MIC_FAIL, msgErrorMid);
						this.pageOrigen.terminarIteracion();
					}
				} else if (tipoPruebaAutoservicio.equals("Cambiar clave por olvido")) {
					Reporter.reportEvent(Reporter.MIC_PASS, "Se realizo correctamente el cambio de clave.");
				}

			}
		}

	}
}
