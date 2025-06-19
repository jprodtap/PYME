package launchTest;

import java.io.File;
import java.io.FileInputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import org.openqa.selenium.By;
import dav.middlePymes.*;
import dav.ActualizacionDeDatos.*;
import dav.AutogestionServicios.PageAutogestionServicioMiddle;
import dav.AutogestionServicios.PageAutogestionServicios;
import dav.Autoservicio.PageAutoservicio;
import dav.Consultas_Y_Extractos.PageConsultasyExtractos;
import dav.TransaccionesProgramadas.PageTransaccionesProgramadas;
import dav.pymes.moduloTx.PageAdminParametros;
import dav.pymes.moduloTx.PageOrigen;
import dav.pymes.PageLoginPymes;
import dav.pymes.moduloCrearTx.ControllerCrearTx;
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

public class Launch_ActualizacionDeDatos extends BaseTestNG {

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
	PageActualizacionDeDatos pageActualizacionDeDatos = null;
	PageActualizacionFileNet pageActualizacionFileNet = null;
	PageActualizacionInformesMiddle pageInformeMiddle = null;

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
	By locMsgAlerta = By.xpath("//*[@id=\"lblMasterAlerta\"]");

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

//		 if (this.controller != null)
//		 this.controller.destroy();

	}
	// =======================================================================================================================

	// ===================================DOINGCONFIGURATIONS====================================================================================

	// SOLICITUD DATOS GLOBALES, CONFIGURACIONES INICIALES Y/O VALIDACIONES
	// INICIALES
	public void doingConfigurations() throws Exception {
		// Descomentariar la siguiente linea cuando se genere el JAR
//			DXCUtil.PATH_RESOURCES = "/resources";
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
//				Reporter.write("Datos de Logueo [" + DXCUtil.arrayToString(datosLogin, " - ") + "]");
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
		// PARAMETROS REQUERIDOS EN EL ARCHIVO EXCEL
		this.navegador = SettingsRun.getTestData().getParameter("Navegador").trim();

		// -----------------------------------------------------------------------------------------------------------------------

		// PARAMETROS REQUERIDOS EN EL ARCHIVO EXCEL
		this.navegador = SettingsRun.getTestData().getParameter("Navegador").trim();

		// -----------------------------------------------------------------------------------------------------------------------

		/*
		 * Metodo se encarga de validar, que los datos obligatorios esten en el archivo
		 * a cargar
		 */
//			 this.validarOCorregirData();

		// -----------------------------------------------------------------------------------------------------------------------

		String msgInicio = "";

		// -----------------------------------------------------------------------------------------------------------------------

		// *************************************************************************************************************************

		// *************************************************************************************************************************
		// -----------------------------------------------------------------------------------------------------------------------
		// OPCIï¿½N SI SELECIONA "NO" SOLO REALIZA EL FLUJO DESDE EL PORTAL FRONT
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

		do {

			// -----------------------------------------------------------------------------------------------------------------------
			// INTENTA HACER EL LOGUEO
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

				this.pageActualizacionDeDatos = new PageActualizacionDeDatos(this.pageLogin);

				msgError = this.pageActualizacionDeDatos.InicioActualizacionDatos(true);

				if (!msgError.equals("Se actualizaron exitosamente los datos de su empresa")) {
					msgError = this.pageActualizacionDeDatos.MsgAlertaActualizacionDatos();
					this.pageOrigen.terminarIteracion(Reporter.MIC_FAIL, msgError);
				} else {
					Reporter.reportEvent(Reporter.MIC_PASS, msgError);
				}

			} else {
				Reporter.reportEvent(Reporter.MIC_FAIL, msgError);
				this.pageOrigen.terminarIteracion();
			}

			String Informe = SettingsRun.getTestData().getParameter("Informe");

			if (Informe.equals("SI") && msgError.equals("Se actualizaron exitosamente los datos de su empresa")) {

				this.pageLogin.CerrarSesionFront();
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
//					PageLoginPymes.datosMidell(numCliEmp, tipoDoc, numDoc, clave, tipoTok, datoTok);
				// Guarda el Dato del Token Middle
//					PageUsuariosEmpresa.datosMidellToke(datoTok);
				String msgErrorMid = this.pageLogin.loginMiddle(); // M�todo para hacer el logueo en el portal
				String msgFileNet = null; // Middle

				if (msgErrorMid == null) {

					this.pageInformeMiddle = new PageActualizacionInformesMiddle(this.pageLogin);
					this.pageActualizacionFileNet = new PageActualizacionFileNet(this.pageLogin);
					this.pageInformeMiddle.InformeActualizacionDeDatos();
					this.pageLogin.CerrarSesionMiddle();

					this.pageActualizacionFileNet.InicioFileNet();
					msgFileNet = this.pageActualizacionFileNet.EncontrarArchivo(false);

					if (msgFileNet.equals("No encontrado")) {
						DXCUtil.wait(20);
						msgFileNet = this.pageActualizacionFileNet.EncontrarArchivo(true);

						if (msgFileNet.equals("No encontrado")) {
							Reporter.reportEvent(Reporter.MIC_FAIL, "El archivo en FileNet no fue encontrado");
						} else {
							Reporter.reportEvent(Reporter.MIC_PASS, "Se encontro archivo en FileNet");
						}

					} else {
						Reporter.reportEvent(Reporter.MIC_PASS, "Se encontro archivo en FileNet");
					}

				} else {
					Reporter.reportEvent(Reporter.MIC_FAIL, msgErrorMid);
					this.pageOrigen.terminarIteracion();
					this.pageLogin.CerrarSesionMiddle();
				}
			}

		}

	}
}
