package launchTest;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.sql.Driver;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.ClickAction;

import dav.Consultas_Y_Extractos.*;
import dav.OficinaVirual.PageOficinaVirtual;
import dav.middlePymes.ControllerValiPymeMiddle;
import dav.middlePymes.PageUsuariosEmpresa;
import dav.pymes.PageLoginPymes;
import dav.pymes.moduloTx.PageAdminParametros;
import dav.pymes.moduloTx.PageOrigen;
import dav.transversal.DatosDavivienda;
import dav.transversal.DatosEmpresarial;
import dav.transversal.MovimientoStratus;
import dav.transversal.Stratus;
import dxc.dav.library.reporting.EvidencePdfFile;
import dxc.execution.BaseTestNG;
import dxc.library.reporting.Evidence;
import dxc.library.reporting.Reporter;
import dxc.library.settings.SettingsRun;
//import dxc.execution.Evidence;
//import dxc.execution.Reporter;
//import dxc.execution.SettingsRun;
import dxc.util.DXCUtil;

public class Launch_OficinaYAtencion extends BaseTestNG {

	// =======================================================================================================================
	// Page & Crontroller
	PageLoginPymes pageLogin = null;
	PageOficinaVirtual pageOficeVirtual = null;
	PageOrigen pageOrigen = null;
	PageUsuariosEmpresa pageUsuariosEmpresa = null;
	PageConsultasyExtractos pageConsultasyExtractos = null;
	PageSaldos pageSaldos = null;
	PageCheques pageCheques = null;
	PageAdminParametros pageAdminParametros = null;
	PageTxaTravezDelPortal pageTxaTravezDelPortal = null;
	ControllerValiPymeMiddle controllerValiPymeMiddle = null;

	Properties info;

	// =======================================================================================================================
	String tipoIdEm, clieEmpresa, idEmpresa, numIdUser, tipoIDUser, combo, nombreEmpre, TipoSolicitud;

	// =======================================================================================================================
	// VARIABLES GLOBALES DE TX Y MOTOR

	final String HA_CONS_DESDE_LISTA = "SI";
	final String CN_APRO_PEND = "1";

// -----------------------------------------------------------------------------------------------------------------------

	String contratacion = null;
	String servicio, navegador, empresa, informe;
	String numCliEmp, tipoDoc, numDoc, clave, tipoTok, datoTok;
	String na = "N/A";
	// =======================================================================================================================

	// =======================================================================================================================
	// LOCATOR PYME FRONT
	By log30 = By.xpath("/html/body/h2[1]/p");
	By locCmEmpresa = By.xpath("//select[@id='dropMasterEmpresa']");

	// Login Front
	By cerrarSesion = By.xpath("//*[@id='CerrarSesion']");
	By cmCerrSes = By.cssSelector("a[id='CerrarSesion']");
	WebElement cerrarses;
	By fecha = By.xpath("//*[@id='tabla-top']/tbody/tr[1]/td[1]");

	// Oficina Virtual-Atencion en linea
	By Solicitud = By.xpath("//*[@id=\"im_MainContent\"]/div[2]/div[2]/div[1]/button");
	By BuscarPregunta = By.xpath("//*[@id=\"rn_SearchButton_3_SubmitButton\"]");
	By Asesor = By.xpath("//*[@id='rn_PageContent']/div[1]/div/div[2]/div[3]/a");
	WebElement nuevaSolicitud;
	WebElement BuscarP;
	WebElement ConAsesor;

	// =======================================================================================================================

	// ================================LAUNCHDATA=======================================================================================

	public void launchData() { // DATOS DEL LANZAMIENTO
//		SettingsRun.EXEC_CLASS = this.getClass().getSimpleName(); // OBLIGATORIO - NOMBRE DE LA CARPETA DE EVIDENCIAS
//		Evidence.siTomarla(Evidence.FORMATO_EXCEL); // TOMAR EVIDENCIAS POR ROW Y DEJA LO ÚLTIMO EN EL FORMATO INDICADO
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

//	public void launchClose() { // CIERRE DEL LANZAMIENTO
//
//		// -----------------------------------------------------------------------------------------------------------------------
//		// CIERRA EL ARCHIVO DE MOTOR DE RIESGO
//		if (DatosDavivienda.RISKMOTOR != null)
//			DatosDavivienda.RISKMOTOR.cerrarMotorRiesgo();
//
//		// -----------------------------------------------------------------------------------------------------------------------
//
//		if (this.pageLogin != null)
//			this.pageLogin.closeAllBrowsers();
//		// -----------------------------------------------------------------------------------------------------------------------
//
////		 if (this.controller != null)
////		 this.controller.destroy();
//		
//		// CIERRA EL ARCHIVO DE MOTOR DE RIESGO
//		if (DatosDavivienda.STRATUS != null)
//			DatosDavivienda.STRATUS.closeStratus();
//
//	}
	// =======================================================================================================================

	// ===================================DOINGCONFIGURATIONS====================================================================================

	// SOLICITUD DATOS GLOBALES, CONFIGURACIONES INICIALES Y/O VALIDACIONES
	// INICIALES
	public void doingConfigurations() throws Exception {
		// Descomentariar la siguiente linea cuando se genere el JAR
		DXCUtil.PATH_RESOURCES = "/resources";
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
		// HACE LA CONFIGURACIÓN REQUERIDA PREVIA REQUERIDA PARA TODAS LAS ITERACIONES
		// -----------------------------------------------------------------------------------------------------------------------
		// SELECIONA SI CONFIA EN LA CONTRATACIÓN

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
			Reporter.reportEvent(Reporter.MIC_FAIL, "Opción no válida");
			break;
		}

		if (nombreAmbiente.isEmpty()) {
			Reporter.reportEvent(Reporter.MIC_FAIL, "Nombre del ambiente seleccionado: " + nombreAmbiente);
		} else {
			Reporter.reportEvent(Reporter.MIC_HEADER, "Nombre del ambiente seleccionado: " + nombreAmbiente);
		}

		DatosEmpresarial.AMBIENTE_TEST = nombreAmbiente;

		contratacion = info.getProperty("CONTRATACION");

	}
	// =======================================================================================================================

	// ===========================================DOINGTEST===========================================================================

	// MÉTODO QUE ENMARCA LAS PRUEBAS A REALIZAR POR CADA LANZAMIENTO
	public void doingTest() throws Exception {
		this.runAutomationBetweenHours(SettingsRun.getGlobalData("TiemPermitido"),SettingsRun.getGlobalData("HoraDesVentana"),SettingsRun.getGlobalData("HoraTerVentana"));
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
			this.servicio = "Consultas de Productos";
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
			this.pageLogin = new PageLoginPymes(this.navegador, Evidence.getNbEvidenceDirectory());// Carga en que
																									// navegador se va
																									// realizar la
																									// prueba
			msgError = this.pageLogin.loginMiddle(); // Método para hacer el logueo en el portal Middle Pyme.
			this.pageLogin.selecionambienteClose("SI");// Indicativo para el ambiente middle// Marca si esta en el
														// Ambiente Middle o FRONT

			// -----------------------------------------------------------------------------------------------------------------------

			// SI ES NULL EL MENSAJE DE ALERTA INGRESA AL LOGIN
			if (msgError == null) {

				this.controllerValiPymeMiddle = new ControllerValiPymeMiddle(this.pageLogin);
				// -----------------------------------------------------------------------------------------------------------------------

				/*
				 * VALIDA SI EL NUMERO DE FIRMAS ES IGUAL A 1 SI NO ES IGUAL REALIZA LA
				 * VALIDACIÓN DE MIDDLE AL 2 CLIENTE
				 */
				// CONVIERTE EL NUMERO DE FIRMAS EN LA VARIABLE numfirm
				int contador = 1;

				/*
				 * VALIDA SI EL NUMERO DE FIRMAS ES IDIFERENTE A 1 REALIZA LA VALIDACIÓN DE
				 * MIDDLE AL 2 CLIENTE
				 */

				// -----------------------------------------------------------------------------------------------------------------------
				// REALIZA LA CONTRATACIÓN, EL FLUJO DE PYME MIDDLE
				controllerValiPymeMiddle.ValidacionMiddlefirmas(1);

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
			this.pageLogin = new PageLoginPymes(this.navegador, Evidence.getNbEvidenceDirectory());// Carga en que
																									// navegador se va
																									// realizar la
																									// prueba
			msgError = this.pageLogin.loginMiddle(); // Método para hacer el logueo en el portal Middle Pyme.
			this.pageLogin.selecionambienteClose("SI");// Indicativo para el ambiente middle// Marca si esta en el
														// Ambiente Middle o FRONT
			// -----------------------------------------------------------------------------------------------------------------------
			// SI ES NULL EL MENSAJE DE ALERTA INGRESA AL LOGIN
			if (msgError == null) {
				this.controllerValiPymeMiddle = new ControllerValiPymeMiddle(this.pageLogin);

				// -----------------------------------------------------------------------------------------------------------------------
				// NUMEROS DE FIRMAS A RALIZAR

				/*
				 * VALIDA SI EL NUMERO DE FIRMAS ES IGUAL A 1 SI NO ES IGUAL REALIZA LA
				 * VALIDACIÓN DE MIDDLE AL 2 CLIENTE
				 */
				// CONVIERTE EL NUMERO DE FIRMAS EN LA VARIABLE numfirm
				int contador = 1;

				/*
				 * VALIDA SI EL NUMERO DE FIRMAS ES IDIFERENTE A 1 REALIZA LA VALIDACIÓN DE
				 * MIDDLE AL 2 CLIENTE
				 */

				// -----------------------------------------------------------------------------------------------------------------------
				// REALIZA EL FLUJO DE PYME MIDDLE
				controllerValiPymeMiddle.ValidacionMiddlefirmas(1);

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

	}
// =======================================================================================================================

// ===========================================FRONT===========================================================================
	/**
	 * Método que llama logueo en el portal Front Pyme. Realiza el primer ingreso
	 */
	public void Front() throws Exception {

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

// ***********************************************************************************************************************

		do {
// -----------------------------------------------------------------------------------------------------------------------
			// INTENTA HACER EL LOGUEO
			this.pageLogin = new PageLoginPymes(this.navegador, Evidence.getNbEvidenceDirectory());
			msgError = this.pageLogin.loginFront();// Método para hacer el logueo en el portal front Pyme.
			this.pageLogin.selecionambienteClose("NO"); // Indicativo para el ambiente Front// Marca si esta en el//
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
			// Cierra la ventana actual del login de Front ya que va interatuar con la
			// ventana emergente
			DXCUtil.wait(10);
			this.pageLogin.closeCurrentBrowser();
// -----------------------------------------------------------------------------------------------------------------------
			// INTERATUA CON LA VENTANA EMERGENTE DE FRONT PYME
			this.pageLogin.changeWindow(this.pageLogin.accedioAlPortal());
// -----------------------------------------------------------------------------------------------------------------------
			// Obtiene el nombre de la empresa a selecionar
			this.pageOrigen = new PageOrigen(this.pageLogin);
// -----------------------------------------------------------------------------------------------------------------------

			/*
			 * Intenta seleccionar la empresa. Retorna [null] si pudo hacer la selección, en
			 * caso contrario retorna mensajede error.
			 */
			this.pageAdminParametros = new PageAdminParametros(this.pageLogin);
			msgError = this.pageOrigen.seleccionarEmpresa(this.empresa);
			// SI ES NULL EL MENSAJE DE ALERTA, SIGUE CON LAS DEMÁS VALIDACIONES
//			String tipoAbono = SettingsRun.getTestData().getParameter("Tipo de abono").trim();
//			String ctaInscrita = SettingsRun.getTestData().getParameter("Cuentas Inscriptas").trim();
			String tipoAbono = "uno a uno";
			String ctaInscrita = "NO";
			String numAprobaciones = SettingsRun.getTestData().getParameter("Números de Aprobaciones").trim();
			String Servicio1 = SettingsRun.getTestData().getParameter("Servicio").trim();
			String TipoRedireccion = SettingsRun.getTestData().getParameter("Tipo de Redireccion").trim();
//-----------------------------------------------------------------------------------------------------------------------

			System.out.println(Servicio1);
			// Seleccion servicio
			//Validacion para entrar al servicio Oficina Virtual
			if (Servicio1.equals("Oficina Virtual")) {

				// Este método hace la configuración en parámetros generales, y guarda la
				// evidencia.
				msgError = this.pageAdminParametros.hacerConfiguracion(numAprobaciones, tipoAbono, ctaInscrita);
				DXCUtil.wait(2);
				this.pageOrigen = new PageOrigen(this.pageLogin);
				this.pageOrigen.irAOpcion(null, "Servicios especiales", "Oficina virtual", "Gestión de solicitudes");
				//Validacion de la redireccion con el boton de nueva solicitud
				do {
					this.pageLogin.changeWindow(this.pageLogin.accedioAlPortal());
					DXCUtil.wait(2);
					nuevaSolicitud = this.pageOrigen.element(Solicitud);
				} while (nuevaSolicitud == null);

				this.pageOficeVirtual = new PageOficinaVirtual(this.pageLogin);
				//Este metodo hace el procedimiento para crear una nueva solicitud
				this.pageOficeVirtual.CrearSolicitud();
				DXCUtil.wait(2);
				//Proceso de cerrar sesion
				this.pageOficeVirtual.closeCurrentBrowser();
				this.pageLogin.changeWindow(this.pageLogin.accedioAlPortal());
				cerrarses = this.pageOrigen.element(cerrarSesion);
				if (cerrarses != null) {
					cerrarses.click(); // DA CLICK EN EL LOCATOR DE CERRAR SESION
				}
				DXCUtil.wait(4);
				
				//Validacion para entrar al servicio Atencion en linea
			} else if (Servicio1.equals("Atencion en linea")) {
				//Validacion de Tipo de redireccion
				if (TipoRedireccion.equals("Preguntas Frecuentes")) {
					DXCUtil.wait(2);
					this.pageOrigen = new PageOrigen(this.pageLogin);
					Evidence.save("Empresa seleccionada");
					//Seleccion de opcion
					this.pageOrigen.irAOpcion(null, "Atención en línea", "Preguntas Frecuentes", null);
					int contador = 0;
					DXCUtil.wait(4);
					this.pageLogin.changeWindow(this.pageLogin.accedioAlPortal());
					BuscarP = this.pageOrigen.element(BuscarPregunta);
					//Validacion de funcionamiento por mal de la pagina
					if (BuscarP == null) {
						do {
							DXCUtil.wait(1);
							contador++;
							Evidence.save("Redireccion Preguntas Fecuentes");
						} while (BuscarP == null && contador < 1);
						Reporter.reportEvent(Reporter.MIC_FAIL, "Pagina NO Disponible");
					}
					//Validacion de funcionamiento por bien de la pagina
					if (BuscarP != null) {
						Evidence.save("Redireccion Preguntas Fecuentes");
						Reporter.reportEvent(Reporter.MIC_PASS, "Redireccion Funcionando");
						this.pageOficeVirtual = new PageOficinaVirtual(this.pageLogin);
						DXCUtil.wait(2);
					}

				}
				//Validacion de Tipo de redireccion
				if (TipoRedireccion.equals("Chatee con nosotros")) {
					DXCUtil.wait(2);
					this.pageOrigen = new PageOrigen(this.pageLogin);
					Evidence.save("Empresa seleccionada");
					//Seleccion de opcion
					this.pageOrigen.irAOpcion(null, "Atención en línea", "Chatee con nosotros", null);
					int contador = 0;
					DXCUtil.wait(4);
					this.pageLogin.changeWindow(this.pageLogin.accedioAlPortal());
					ConAsesor = this.pageOrigen.element(Asesor);
					//Validacion de funcionamiento por mal de la pagina
					if (ConAsesor == null) {
						do {
							DXCUtil.wait(1);
							contador++;
							Evidence.save("Redireccion Chatee con nosotros");
						} while (ConAsesor == null && contador < 1);
						Reporter.reportEvent(Reporter.MIC_WARNING, "Pagina NO Disponible");
						this.pageOficeVirtual = new PageOficinaVirtual(this.pageLogin);
						DXCUtil.wait(2);
					}
					//Validacion de funcionamiento por bien de la pagina
					if (ConAsesor != null) {
						Reporter.reportEvent(Reporter.MIC_PASS, "Redireccion Funcionando");
						Evidence.save("Redireccion Chatee con nosotros");
						this.pageOficeVirtual = new PageOficinaVirtual(this.pageLogin);
						DXCUtil.wait(2);
					}
				}
				//Proceso de cerrar sesion
				this.pageOficeVirtual.closeCurrentBrowser();
				this.pageLogin.changeWindow(this.pageLogin.accedioAlPortal());
				cerrarses = this.pageOrigen.element(cerrarSesion);
				if (cerrarses != null) {
					cerrarses.click(); // DA CLICK EN EL LOCATOR DE CERRAR SESION
				}
				DXCUtil.wait(4);

			}

		}

		SettingsRun.exitTestIteration();
	}

// =======================================================================================================================

// ===========================================[Stratus]===========================================================================

	public void LoginStratus() throws Exception {

		String strusu = info.getProperty("STRATUS.usuario");
		String strpass = info.getProperty("STRATUS.password");

		// Se comentan las siguientes 3 lineas para que no ejecute en stratus

		DatosDavivienda.STRATUS = new Stratus(strusu, strpass, "EMPRESAS");
		Reporter.reportEvent(Reporter.MIC_INFO, "Se cargan datos para logueo" + " de Stratus");
	}

// ======================================================================================================================

}
