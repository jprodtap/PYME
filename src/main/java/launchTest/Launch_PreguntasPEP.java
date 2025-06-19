package launchTest;

import java.io.File;
import java.io.FileInputStream;
import java.util.Date;
import java.util.Properties;

import org.openqa.selenium.By;

import dav.Consultas_Y_Extractos.PageConsultasyExtractos;
import dav.FiduciaEstrusturada.ControllerFiducia;
import dav.FiduciaEstrusturada.PageServiciosMiddle;
import dav.PreguntasPEP.ControllerPreguntasPep;
import dav.PreguntasPEP.PagePreguntas;
import dav.c360.PageInicioC360;
import dav.c360.PageLogin;
import dav.divisas.PageAprobacionInter;
import dav.middlePymes.ControllerValiPymeMiddle;
import dav.middlePymes.PageUsuariosEmpresa;
import dav.pymes.PageLoginPymes;
import dav.pymes.moduloCrearTx.ControllerCrearTx;
import dav.pymes.moduloTx.PageAdminParametros;
import dav.pymes.moduloTx.PageOrigen;
import dav.transversal.DatosDavivienda;
import dav.transversal.DatosEmpresarial;
import dav.transversal.MotorRiesgo;
import dxc.dav.library.reporting.EvidencePdfFile;
import dxc.execution.BasePageWeb;
import dxc.execution.BaseTestNG;
import dxc.library.reporting.Reporter;
import dxc.library.settings.SettingsRun;
//import dxc.execution.Evidence;
//import dxc.execution.Reporter;
//import dxc.execution.SettingsRun;
import dxc.util.DXCUtil;

public class Launch_PreguntasPEP extends BaseTestNG {
	// =======================================================================================================================
	// Page & Crontroller
	PageLoginPymes pageLogin = null;
	PageOrigen pageOrigen = null;
	PageUsuariosEmpresa pageUsuariosEmpresa = null;
	PageAdminParametros pageAdminParametros = null;
	PageConsultasyExtractos pageConsultasyExtractos = null;
	ControllerCrearTx controller = null;
	PageAprobacionInter pageAprobInter = null;
	PageLogin pageLoginC360 = null;
	PageInicioC360 pageInicioC360 = null;
	ControllerPreguntasPep controllerPreguntasPep = null;

	// =======================================================================================================================

	// =======================================================================================================================
	// VARIABLES GLOBALES DE TX Y MOTOR
	final String TP_LOGIN = "Login";
	final String TP_EN_LINEA = "Tx En Linea";
	final String TP_PEND_APR = "Tx Pend Aprobación";
	final String CN_APRO_PEND = "1";
	final String DE_El_DETALLE = "SI";
	String transaccion = MotorRiesgo.TX_EMP_LOGIN_SUCC; // VALOR POR DEFECTO

	// -----------------------------------------------------------------------------------------------------------------------

	public static String realizarMR = null;
	Date fechaHoraLogMR;
	String tipoPrueba, desde_el_Detalle, servicio, riesgoBc, riesgoEfm, riesgo, navegador, empresa, numAprobaciones,
			informe, validarPreguntas;
	String numCliEmp, tipoDoc, numDoc, clave, tipoTok, datoTok;
	String na = "N/A";
	// =======================================================================================================================
	String tipoIdEm, clieEmpresa, idEmpresa, numIdUser, tipoIDUser, combo, nombreEmpre, fiducia;
	String servicioSeleccionado = "";
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
		// -----------------------------------------------------------------------------------------------------------------------
		Reporter.writeTitle("\n*** PRUEBAS PORTAL PYME Y CLIENTE 360 PREGUNTAS PEP ***");
		// PARAMETROS REQUERIDOS EN LA HOJA DE DATOS GLOBAL PARA EL LAUNCH QUE SE ESTA
		// HACIENDO
		SettingsRun.ARRAY_DATA_PARAMS = new String[] { "Selección" };
		// -----------------------------------------------------------------------------------------------------------------------
	}
	// =======================================================================================================================
	// ====================================LAUNCHCLOSE===================================================================================

	public void launchClose() { // CIERRE DEL LANZAMIENTO

		// -----------------------------------------------------------------------------------------------------------------------

		if (this.pageLogin != null)
			this.pageLogin.closeAllBrowsers();
		// -----------------------------------------------------------------------------------------------------------------------

	}

	// ===================================DOINGCONFIGURATIONS====================================================================================

	// SOLICITUD DATOS GLOBALES, CONFIGURACIONES INICIALES Y/O VALIDACIONES
	// INICIALES
	public void doingConfigurations() throws Exception {
		// HACE LA CONFIGURACIAN REQUERIDA PREVIA REQUERIDA PARA TODAS LAS ITERACIONES
		// -----------------------------------------------------------------------------------------------------------------------
		// SELECIONA SI CONFIA EN LA CONTRATACI�N

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
			Reporter.reportEvent(Reporter.MIC_FAIL, "Opción no valida");
			break;
		}

		if (nombreAmbiente.isEmpty()) {
			Reporter.reportEvent(Reporter.MIC_FAIL, "No tiene Nombre del ambiente, por favor ingrese uno");
		} else {
			Reporter.reportEvent(Reporter.MIC_HEADER,
					"Nombre del ambiente seleccionado: ".toUpperCase() + nombreAmbiente);
		}
		DatosEmpresarial.AMBIENTE_TEST = nombreAmbiente;
		this.navegador = SettingsRun.getTestData().getParameter("Navegador").trim();
	}
	// =======================================================================================================================
	// ===========================================DOINGTEST===========================================================================

	// METODO QUE ENMARCA LAS PRUEBAS A REALIZAR POR CADA LANZAMIENTO
	public void doingTest() throws Exception {
		String msgError = null;
		// -----------------------------------------------------------------------------------------------------------------------
		// PARAMETROS REQUERIDOS EN EL ARCHIVO EXCEL
		// -----------------------------------------------------------------------------------------------------------------------
		this.validarPreguntas = SettingsRun.getTestData().getParameter("Tipo de Prueba");

		this.pageInicioC360 = this.logueoC360();
		Reporter.write("*** POR CONFIRMAR LAS PREGUNTAS EN CLIENTE 360 ***");
		this.controllerPreguntasPep = new ControllerPreguntasPep(this.pageInicioC360);
		this.controllerPreguntasPep.validarPreguntasPersonas();
		this.pageLoginC360.closeAllBrowsers();
// -----------------------------------------------------------------------------------------------------------------------
		// UNA VEZ FINALICE LAS VALIDACIONES DE PREGUNTAS PEP EN C360, SE DIRIGIRA A
		this.Front();
// -----------------------------------------------------------------------------------------------------------------------
	}

	// ===========================================FRONT===========================================================================
	/**
	 * MEtodo que llama logueo en el portal Front Pyme. Realiza el primer ingreso
	 */

	public void Front() throws Exception {

		DXCUtil.Movercursor();
		// -----------------------------------------------------------------------------------------------------------------------
		String msgError = null;
		int intento = 0;
		String msgInicio = "";
		String msgInicio2 = "";
		this.empresa = SettingsRun.getTestData().getParameter("Nombre Empresa").trim();
		// -----------------------------------------------------------------------------------------------------------------------

		// Datos Login front Login, estos datos se encuentran el archivo del carge DATA
		DatosEmpresarial.loadLoginData("Cliente Empresarial", "Tipo Identificación", "Id usuario",
				"Clave personal o CVE", "Tipo Token", "Semilla / Valor Estático / Celular");
		String[] datosLogin = DatosEmpresarial.getLoginData();
		// ***********************************************************************************************************************

		do {
			// -----------------------------------------------------------------------------------------------------------------------
			// INTENTA HACER EL LOGUEO
			this.pageLogin = new PageLoginPymes(navegador);
			msgError = this.pageLogin.loginFront();// Metodo para hacer el logueo en el portal front Pyme.
			fechaHoraLogMR = this.pageLogin.getFechaHoraLogMR();
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
			Reporter.reportEvent(Reporter.MIC_INFO,
					"*** Datos de Logueo Front: [" + DXCUtil.arrayToString(datosLogin, " - ") + "]");
			// Cierra la ventana actual del login de Front ya que va interatuar con la
			// ventana emergente
			DXCUtil.wait(3);

			boolean isWindowOpened = this.pageLogin.WaitForNumberOfWindos();
			if (!isWindowOpened) {
				this.pageOrigen.terminarIteracion(Reporter.MIC_FAIL, "No se abrió La ventana emergente");
			}

			this.pageLogin.closeCurrentBrowser();
			DXCUtil.wait(3);

// -----------------------------------------------------------------------------------------------------------------------
			// INTERATUA CON LA VENTANA EMERGENTE DE FRONT PYME
			this.pageLogin.changeWindow(this.pageLogin.accedioAlPortal());
			this.pageOrigen = new PageOrigen(this.pageLogin);
			msgError = this.pageOrigen.seleccionarEmpresa(this.empresa);
			if (msgError == null) {
				// PUEDE REALIZAR LAS PREGUNTAS POR PRIMERA VEZ O ACTUALIZAR SI YA LAS CONTIENE
				Reporter.write("*** POR " + this.validarPreguntas + " PREGUNTAS PEP ***");
				this.controllerPreguntasPep = new ControllerPreguntasPep(pageLogin);
				this.controllerPreguntasPep.ingresarPreguntasPep();
				this.pageLogin.CerrarSesionFront();

				// UNA VEZ REALIZA EL REGISTRO DE LAS PREGUNTAS EN PORTAL PYME SE VERIFICARA EN
				// 360 LAS PREGUNTAS
				this.pageInicioC360 = this.logueoC360();
				Reporter.write("*** POR CONFIRMAR LAS PREGUNTAS EN CLIENTE 360 ***");
				this.controllerPreguntasPep = new ControllerPreguntasPep(this.pageInicioC360);
				this.controllerPreguntasPep.getValidacionPreguntas();
				this.pageLoginC360.closeAllBrowsers();
			} else {
				this.pageOrigen.terminarIteracion(Reporter.MIC_FAIL, msgError);
			}
		}

	}

	/**
	 * Metodo que llama y hace el logueo
	 * 
	 * @throws Exception
	 */
	public PageInicioC360 logueoC360() throws Exception {
		// INTENTA HACER EL LOGUEO - C360
		this.pageLoginC360 = new PageLogin(navegador);
		this.pageInicioC360 = new PageInicioC360(pageLoginC360);
		this.pageLoginC360.login360(SettingsRun.getGlobalData("C360.Usuario"),
				SettingsRun.getGlobalData("C360.Contrasena"));

		return this.pageInicioC360;
	}
}
